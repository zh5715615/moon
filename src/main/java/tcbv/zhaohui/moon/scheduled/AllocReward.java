package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public abstract class AllocReward {
    protected abstract Integer getGameType();


    protected void saveRewardRecord(List<TbTxRecord> winnerList, List<TbTxRecord> loserList,
                                  BigDecimal loserAmount, BigDecimal poolTotalAmount, TbUserDao userDao,
                                  TbRewardRecordDao rewardRecordDao, IMoonBaseService moonBaseService) {
        log.info("loser List is {}", GsonUtil.toJson(loserList));
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

        log.info("winner list is {}", GsonUtil.toJson(winnerList));
        if (CollectionUtils.isEmpty(winnerList)) {
            return;
        }

        List<BigInteger> totalAmountList = new ArrayList<>();
        List<String> userAddressList = new ArrayList<>();
        BigDecimal totalAmount = winnerList.stream()
                .map(TbTxRecord::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalAmount.compareTo(BigDecimal.ZERO) <= 0 || poolTotalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("poolTotalAmount: {}, totalAmount: {}. Skip reward distribution.", poolTotalAmount, totalAmount);
            return;
        }
        List<TbRewardRecord> rewardRecordList = new ArrayList<>();
        for (TbTxRecord tbTxRecord : winnerList) {
            BigDecimal betAmount = tbTxRecord.getAmount() == null ? BigDecimal.ZERO : tbTxRecord.getAmount();
            if (betAmount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }
            BigDecimal rate = betAmount.divide(totalAmount, 8, RoundingMode.HALF_UP);
            BigDecimal reward = loserAmount.multiply(rate).setScale(6, RoundingMode.HALF_UP);
            BigDecimal totalReward = poolTotalAmount.multiply(rate).setScale(6, RoundingMode.HALF_UP);
            log.info("loserAmount is {}, rate is {}", loserAmount, rate);
            log.info("reward is {}", reward);
            log.info("tbTxRecord is {}", GsonUtil.toJson(tbTxRecord));
            TbUser user = userDao.queryById(tbTxRecord.getUserId());
            if (user == null || StringUtils.isBlank(user.getAddress())) {
                log.warn("user not found or address empty for record: {}", tbTxRecord.getUserId());
                continue;
            }
            log.info("user is {}", GsonUtil.toJson(user));
            totalAmountList.add(EthMathUtil.decimalToBigInteger(totalReward, 6));
            userAddressList.add(user.getAddress());

            TbRewardRecord rewardRecord = new TbRewardRecord();
            rewardRecord.setId(UUID.randomUUID().toString());
            rewardRecord.setTurns(tbTxRecord.getTurns());
            rewardRecord.setUserId(tbTxRecord.getUserId());
            rewardRecord.setRewardAmount(reward);
            rewardRecord.setGameType(tbTxRecord.getGameType() + "");
            rewardRecord.setCreateTime(CustomizeTimeUtil.formatTimestamp(System.currentTimeMillis()));
            rewardRecordList.add(rewardRecord);
        }
        if (CollectionUtils.isEmpty(userAddressList)) {
            log.warn("No valid winner records found for distribution.");
            return;
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
            if (!CollectionUtils.isEmpty(rewardRecordList)) {
                rewardRecordDao.insertBatch(rewardRecordList);
            }
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

        BigDecimal loserAmount = BigDecimal.valueOf(txRecordDao.betNumber(getGameType(), turns, loser));
        BigDecimal winnerAmount = BigDecimal.valueOf(txRecordDao.betNumber(getGameType(), turns, winner));
        BigDecimal poolTotalAmount = loserAmount.add(winnerAmount);
        List<TbTxRecord> winnerList = txRecordDao.winnerList(getGameType(), turns, winner);
        List<TbTxRecord> loserList = txRecordDao.loserList(getGameType(), turns, loser);
        saveRewardRecord(winnerList, loserList, loserAmount, poolTotalAmount, userDao, rewardRecordDao, moonBaseService);
    }
}
