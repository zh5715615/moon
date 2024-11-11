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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public abstract class AllocReward {
    protected abstract Integer getGameType();

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
        List<TbTxRecord> list = txRecordDao.winnerList(getGameType(), turns, winner);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<BigInteger> rewardAmountList = new ArrayList<>();
        List<String> userAddressList = new ArrayList<>();
        BigDecimal totalAmount = list.stream().map(TbTxRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<TbRewardRecord> rewardRecordList = new ArrayList<>();
        for (TbTxRecord tbTxRecord : list) {
            double rate = tbTxRecord.getAmount().doubleValue() / totalAmount.doubleValue();
            BigDecimal reward = BigDecimal.valueOf(loserAmount * rate);
            TbUser user = userDao.queryById(tbTxRecord.getUserId());
            rewardAmountList.add(reward.toBigInteger());
            userAddressList.add(user.getAddress());

            TbRewardRecord rewardRecord = new TbRewardRecord();
            rewardRecord.setId(UUID.randomUUID().toString());
            rewardRecord.setTurns(tbTxRecord.getTurns());
            rewardRecord.setUserId(tbTxRecord.getUserId());
            rewardRecord.setRewardAmount(EthMathUtil.bigIntegerToBigDecimal(reward.toBigInteger(), 1));
            rewardRecord.setGameType(tbTxRecord.getGameType() + "");
            rewardRecord.setCreateTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
            rewardRecordList.add(rewardRecord);
        }
        try {
            String txHash = moonBaseService.allocReward(userAddressList, rewardAmountList);
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
}
