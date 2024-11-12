package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.dao.TbRewardRecordDao;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.entity.TbRewardRecord;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.entity.TbUser;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.utils.CustomizeTimeUtil;
import tcbv.zhaohui.moon.utils.EthMathUtil;
import tcbv.zhaohui.moon.utils.GsonUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class AllocReward {
    protected abstract Integer getGameType();


    protected void saveRewardRecord(List<TbTxRecord> winnerList, List<TbTxRecord> loserList,
                                  double loserAmount, double poolTotalAmount, TbUserDao userDao,
                                  TbRewardRecordDao rewardRecordDao, IMoonBaseService moonBaseService) {
        log.info("list is {}", GsonUtil.toJson(winnerList));
        if (CollectionUtils.isEmpty(winnerList)) {
            log.info("loserList is {}", GsonUtil.toJson(loserList));
            if (!CollectionUtils.isEmpty(loserList)) {
                List<TbRewardRecord> loserRecordList = new ArrayList<>();
                for (TbTxRecord txRecord : loserList) {
                    TbRewardRecord loserRecord = new TbRewardRecord();
                    loserRecord.setId(UUID.randomUUID().toString());
                    loserRecord.setTurns(txRecord.getTurns());
                    loserRecord.setUserId(txRecord.getUserId());
                    loserRecord.setRewardAmount(txRecord.getAmount().negate());
                    loserRecord.setGameType(txRecord.getGameType() + "");
                    loserRecord.setCreateTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
                    loserRecordList.add(loserRecord);
                }
                rewardRecordDao.insertBatch(loserRecordList);
            }
            return;
        }

        List<BigInteger> totalAmountList = new ArrayList<>();
        List<String> userAddressList = new ArrayList<>();
        BigDecimal totalAmount = winnerList.stream().map(TbTxRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<TbRewardRecord> rewardRecordList = new ArrayList<>();
        for (TbTxRecord tbTxRecord : winnerList) {
            double rate = tbTxRecord.getAmount().doubleValue() / totalAmount.doubleValue();
            log.info("loserAmount is {}, rate is {}", loserAmount, rate);
            BigDecimal reward = BigDecimal.valueOf(loserAmount * rate);
            BigDecimal totalReward = BigDecimal.valueOf(poolTotalAmount * rate);
            log.info("reward is {}", reward.doubleValue());
            log.info("tbTxRecord is {}", GsonUtil.toJson(tbTxRecord));
            TbUser user = userDao.queryById(tbTxRecord.getUserId());
            log.info("user is {}", user == null ? null : GsonUtil.toJson(user));
            totalAmountList.add(totalReward.toBigInteger().multiply(BigInteger.TEN.pow(6)));
            userAddressList.add(user.getAddress());

            TbRewardRecord rewardRecord = new TbRewardRecord();
            rewardRecord.setId(UUID.randomUUID().toString());
            rewardRecord.setTurns(tbTxRecord.getTurns());
            rewardRecord.setUserId(tbTxRecord.getUserId());
            rewardRecord.setRewardAmount(EthMathUtil.bigIntegerToBigDecimal(reward.toBigInteger(), 0));
            rewardRecord.setGameType(tbTxRecord.getGameType() + "");
            rewardRecord.setCreateTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
            rewardRecordList.add(rewardRecord);
        }
        try {
            String txHash = moonBaseService.allocReward(userAddressList, totalAmountList);
            log.info("中奖发放奖励hash值为{}", txHash);
            for (TbRewardRecord rewardRecord : rewardRecordList) {
                rewardRecord.setTxHash(txHash);
            }
        } catch (Exception e) {
            log.error("上链异常：", e);
        } finally {
            rewardRecordDao.insertBatch(rewardRecordList);
        }
    }

    protected void allocReward(IMoonBaseService moonBaseService, TbUserDao userDao, TbTxRecordDao txRecordDao,
                               TbRewardRecordDao rewardRecordDao, int turns, int winner) {
        int loser;
        if (getGameType().equals(MoonConstant.DICE_ROLLER_GAME)) {
            if (winner == MoonConstant.DICE_ROLLER_SINGLE) {
                loser = MoonConstant.DICE_ROLLER_DOUBLE;
            } else if (winner == MoonConstant.DICE_ROLLER_DOUBLE) {
                loser = MoonConstant.DICE_ROLLER_SINGLE;
            } else {
                log.error("开奖异常，没有胜方败方");
                return;
            }
        } else if (getGameType().equals(MoonConstant.GUESS_BNB_PRICE_GAME)) {
            if (winner == MoonConstant.GUESS_BNB_RISE) {
                loser = MoonConstant.GUESS_BNB_FALL;
            } else if (winner == MoonConstant.GUESS_BNB_FALL) {
                loser = MoonConstant.GUESS_BNB_RISE;
            } else {
                log.error("开奖异常，没有胜方败方");
                return;
            }
        } else {
            log.error("游戏类型错误");
            return;
        }

        double loserAmount = txRecordDao.betNumber(getGameType(), turns, loser);
        double winnerAmount = txRecordDao.betNumber(getGameType(), turns, winner);
        double poolTotalAmount = loserAmount + winnerAmount;
        List<TbTxRecord> winnerList = txRecordDao.winnerList(getGameType(), turns, winner);
        List<TbTxRecord> loserList = txRecordDao.loserList(getGameType(), turns, loser);
        saveRewardRecord(winnerList, loserList, loserAmount, poolTotalAmount, userDao, rewardRecordDao, moonBaseService);
    }
}
