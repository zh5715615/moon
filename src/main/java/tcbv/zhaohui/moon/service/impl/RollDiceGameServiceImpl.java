package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.dto.AddGameOrderForDTO;
import tcbv.zhaohui.moon.dto.GamePrizeDrawDTO;
import tcbv.zhaohui.moon.dto.VerifyGamePrizeDrawDTO;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.scheduled.TimerMaps;
import tcbv.zhaohui.moon.service.RollDiceGameService;
import tcbv.zhaohui.moon.vo.PlayResidueTimesVO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    /**
     * @param gameType
     * @return 根据游戏类型判断是否允许下注
     */
    @Override
    public PlayResidueTimesVO getQueueAndMemSize(Integer gameType) {
        if (gameType == null) {
            throw new RuntimeException("游戏类型为空");
        }
        LocalDateTime gameOneTime = TimerMaps.getRemainingTime("gameOne");
        LocalDateTime gameTwoTime = TimerMaps.getRemainingTime("gameTwo");
        if (gameOneTime == null || gameTwoTime == null) {
            throw new RuntimeException("定时任务查询失败");
        }
        //根据类型查询最新的轮次
        Integer gameTypeNumber = tbGameResultDao.findGameTypeNumber(gameType);

        PlayResidueTimesVO result = new PlayResidueTimesVO();
        result.setGameType(gameType);
        result.setTurns(gameTypeNumber + 1);
        if (gameType.equals("1")) {
           // result.setIsOk(gameOne - 60 > 0 ? true : false);
        } else if (gameType.equals("2")) {
           // result.setIsOk(gameTwo - 60 > 0 ? true : false);
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
    public Boolean addGameOrderFor(AddGameOrderForDTO dto) {
        String userId = dto.getUserId();
        Integer gameType = dto.getGameType();
        Integer dtoTurns = dto.getTurns();
        Integer paramType = dto.getParamType();
        //判断是否还能下注
        PlayResidueTimesVO queueAndMemSize = getQueueAndMemSize(gameType);
        Integer newTurns = queueAndMemSize.getTurns();
        if (dtoTurns != newTurns) {
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
        TbTxRecord param = TbTxRecord.builder()
                .id(UUID.randomUUID().toString()).userId(userId).gameType(gameType)
                .singleAndDouble(paramType).raseAndFall(paramType)
                .eventId(dto.getEventId()).eventResult(paramType.toString())
                //.txHash()
                .turns(dtoTurns).amount(dto.getAmount())
                .build();
        tbTxRecordDao.insert(param);

        return true;
    }

    /**
     *
     * @param dto 根据轮次查询是否开奖
     * @return
     */
    @Override
    public Boolean verifyGamePrizeDraw(VerifyGamePrizeDrawDTO dto) {
        return null;
    }

    /**
     *
     * @param dto 游戏开奖
     * @return
     */
    @Override
    public Boolean gamePrizeDraw(GamePrizeDrawDTO dto) {
        return null;
    }
}
