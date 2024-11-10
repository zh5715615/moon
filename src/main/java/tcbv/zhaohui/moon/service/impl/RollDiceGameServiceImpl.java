package tcbv.zhaohui.moon.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dao.TbRewardRecordDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.dto.*;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbRewardRecord;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.scheduled.TimerMaps;
import tcbv.zhaohui.moon.service.RollDiceGameService;
import tcbv.zhaohui.moon.vo.PageResultVo;
import tcbv.zhaohui.moon.vo.PlayResidueTimesVO;
import tcbv.zhaohui.moon.vo.UserRewardListVO;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author dawn
 * @date 2024/11/2 14:22
 */
@Service
@Slf4j
public class RollDiceGameServiceImpl implements RollDiceGameService {
    @Resource
    private TbGameResultDao tbGameResultDao;
    @Resource
    private TbTxRecordDao tbTxRecordDao;
    @Resource
    private TbRewardRecordDao tbRewardRecordDao;

    /**
     * @param gameType
     * @return 根据游戏类型判断是否允许下注
     */
    @Override
    public PlayResidueTimesVO getQueueAndMemSize(Integer gameType) {
        if (gameType == null) {
            throw new RuntimeException("游戏类型为空");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime gameOneTime = TimerMaps.getRemainingTime(TimerMaps.GAMEONE);
        LocalDateTime gameTwoTime = TimerMaps.getRemainingTime(TimerMaps.GAMETWO);
        if (gameOneTime == null || gameTwoTime == null) {
            throw new RuntimeException("定时任务查询失败");
        }
        // 计算两个时间之间的间隔
        Duration oneTime = Duration.between(gameOneTime, now);
        Duration twoTime = Duration.between(gameTwoTime, now);
        // 获取间隔的绝对值，以秒为单位
        long oneTimes = Math.abs(oneTime.getSeconds());
        long twoTimes = Math.abs(twoTime.getSeconds());
        //根据类型查询最新的轮次
        Integer gameTypeNumber = tbGameResultDao.findGameTypeNumber(gameType);
        if (gameTypeNumber == null) {
            gameTypeNumber = 0;
        }
        PlayResidueTimesVO result = new PlayResidueTimesVO();
        result.setGameType(gameType);
        result.setTurns(gameTypeNumber + 1);
        if (gameType == 1) {
            result.setIsOk(oneTimes - 60 > 0 ? true : false);
        } else if (gameType == 2) {
            result.setIsOk(twoTimes - 60 > 0 ? true : false);
        } else {
            throw new RuntimeException("游戏类型失败");
        }
        return result;
    }

    /**
     * @param dto 添加游戏下注单
     * @return
     */
    @Override
    @Transactional
    public Boolean addGameOrderFor(AddGameOrderForDTO dto) {
        String userId = dto.getUserId();
        Integer gameType = dto.getGameType();
        Integer dtoTurns = dto.getTurns();
        Integer paramType = dto.getParamType(); // 投注类型
        //判断是否还能下注
        PlayResidueTimesVO queueAndMemSize = getQueueAndMemSize(gameType);
        Integer newTurns = queueAndMemSize.getTurns();
        if (!Objects.equals(dtoTurns, newTurns)) {
            log.error("当前轮次" + newTurns + "下注轮次" + dtoTurns);
            throw new RuntimeException("下注轮次和当前最新轮次不匹配");
        }
        Boolean isOk = queueAndMemSize.getIsOk();
        if (!isOk) {
            throw new RuntimeException("开奖时间，禁止下注");
        }
        //查询是否已经下注了
        TbTxRecord tbTxRecord = tbTxRecordDao.queryByIdAndGameInfo(userId, gameType);
        if (tbTxRecord != null) {
            throw new RuntimeException("已下注，请勿重复下注");
        }
        //todo判断余额是否充足

        TbTxRecord param = TbTxRecord.builder()
                .id(UUID.randomUUID().toString()).userId(userId).gameType(gameType)
                .singleAndDouble(paramType).raseAndFall(paramType)
                .eventId(dto.getTxHash()).eventResult(paramType.toString())
                //.txHash()
                .turns(dtoTurns).amount(dto.getAmount())
                .build();
        tbTxRecordDao.insert(param);

        return true;
    }

    /**
     * @param dto 根据轮次查询是否开奖
     * @return
     */
    @Override
    public Boolean verifyGamePrizeDraw(VerifyGamePrizeDrawDTO dto) {
        Integer gameTypeAndTurnsNumber = tbGameResultDao.findGameTypeAndTurnsNumber(dto.getTurns(), dto.getGameType());
        if (gameTypeAndTurnsNumber != null) {
            return true;
        }
        return false;
    }

    /**
     * @param dto 查询游戏下注单列表
     * @return
     */
    @Override
    public PageResultVo<TbTxRecord> userOrderList(OrderListDTO dto) {
        Page<Object> objects = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<TbTxRecord> byUserDraw = tbTxRecordDao.findByUserDraw(dto.getUserId(), dto.getGameType());
        PageResultVo result = new PageResultVo();
        result.setList(byUserDraw);
        result.setTotal(objects.getTotal());
        result.setPageSize(objects.getPageSize());
        result.setPageNum(objects.getPageNum());
        return result;
    }

    /**
     * @param dto 查询中奖列表
     * @return
     */
    @Override
    public PageResultVo<UserRewardListVO> userRewardList(RewardListDTO dto) {
        Page<Object> objects = PageHelper.startPage(dto.getPageNum(), dto.getPageSize());
        List<UserRewardListVO> byUserDraw = tbRewardRecordDao.findByUserDraw(dto.getUserId(), dto.getGameType());

        if (byUserDraw != null && byUserDraw.size() > 0) {
            for (UserRewardListVO userRewardListVO : byUserDraw) {
                if (dto.getGameType() == 1) {
                    userRewardListVO.setResult(userRewardListVO.getSingleAndDoubleB());
                    if (userRewardListVO.getSingleAndDoubleC() == userRewardListVO.getSingleAndDoubleB()) {
                        userRewardListVO.setIsWinning(true);
                    } else {
                        userRewardListVO.setIsWinning(false);
                    }
                } else if (dto.getGameType() == 2) {
                    userRewardListVO.setResult(userRewardListVO.getRaseAndFallB());
                    if (userRewardListVO.getRaseAndFallC() == userRewardListVO.getRaseAndFallB()) {
                        userRewardListVO.setIsWinning(true);
                    } else {
                        userRewardListVO.setIsWinning(false);
                    }
                }
            if(!userRewardListVO.getIsWinning()){
                userRewardListVO.setRewardAmount(new BigDecimal(0));
            }
            }
        }
        PageResultVo result = new PageResultVo();
        result.setList(byUserDraw);
        result.setTotal(objects.getTotal());
        result.setPageSize(objects.getPageSize());
        result.setPageNum(objects.getPageNum());
        return result;
    }

    /**
     * @param dto 查询各游戏轮次的开奖结果
     * @return
     */
    @Override
    public TbGameResult gameResultList(GameResultDto dto) {
        return tbGameResultDao.findGameTypeAndTurnsInfo(dto.getTurns(), dto.getGameType());
    }

    /**
     * @param gameType 前一轮次游戏开奖结果
     * @return
     */
    @Override
    public TbGameResult preTurnsResult(Integer gameType) {
        return tbGameResultDao.queryByGameTypeNow(gameType);
    }

    @Override
    public boolean isBetOn(String userId, Integer gameType, Integer turns) {
        List<TbTxRecord> tbTxRecords = tbTxRecordDao.isBetOn(userId, turns, gameType);
        if (CollectionUtils.isEmpty(tbTxRecords)) {
            return false;
        }
        if (tbTxRecords.size() > 1) {
            log.error("数据库中存在脏数据，userId = {}, gameType = {}, turns = {}", userId, gameType, turns);
            return true;
        }
        return true;
    }

    @Override
    public Double betNumber(int gameType, int turns, Integer betType) {
        return tbTxRecordDao.betNumber(gameType, turns, betType);
    }
}
