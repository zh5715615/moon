package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import tcbv.zhaohui.moon.dao.TbTxRecordDao;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.entity.TbUser;
import tcbv.zhaohui.moon.service.IMoonBaseService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AllocReward {
    protected void allocReward(IMoonBaseService moonBaseService, TbUserDao userDao, TbTxRecordDao txRecordDao, int turns, int winner) {
        int loser = 1;
        if (winner == 1) {
            loser = 2;
        } else if (winner == 2) {
            loser = 1;
        } else {
            log.error("开奖异常，没有胜方败方");
            return;
        }
        double loserAmount = txRecordDao.betNumber(1, turns, loser);
        List<TbTxRecord> list = txRecordDao.winnerList(1, turns, winner);
        if (CollectionUtils.isEmpty(list)) {
            return;
        }

        List<BigInteger> rewardAmountList = new ArrayList<>();
        List<String> userAddressList = new ArrayList<>();
        BigDecimal totalAmount = list.stream().map(TbTxRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        for (TbTxRecord tbTxRecord : list) {
            double rate = tbTxRecord.getAmount().doubleValue() / totalAmount.doubleValue();
            BigDecimal reward = BigDecimal.valueOf(loserAmount * rate);
            TbUser user = userDao.queryById(tbTxRecord.getUserId());
            rewardAmountList.add(reward.toBigInteger());
            userAddressList.add(user.getAddress());
        }
        try {
            moonBaseService.allocReward(userAddressList, rewardAmountList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
