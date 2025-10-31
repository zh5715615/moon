package tcbv.zhaohui.moon.scheduled;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tcbv.zhaohui.moon.vo.NFTRankVo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class TimerMapsTest {

    @AfterEach
    void tearDown() {
        TimerMaps.clearNFTRankVoList();
    }

    @Test
    void shouldHandleConcurrentNftRankUpdates() throws InterruptedException {
        int taskCount = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            int rank = i;
            executorService.submit(() -> {
                TimerMaps.addNFTRankVo(new NFTRankVo(rank, "0x" + rank, rank));
                latch.countDown();
            });
        }

        executorService.shutdown();
        Assertions.assertTrue(latch.await(5, TimeUnit.SECONDS));
        Assertions.assertTrue(executorService.awaitTermination(5, TimeUnit.SECONDS));

        Assertions.assertEquals(taskCount, TimerMaps.getNftRankVoList().size());
    }

    @Test
    void shouldExposeDefensiveCopyOfRankList() {
        TimerMaps.addNFTRankVo(new NFTRankVo(1, "0x1", 1));
        Assertions.assertThrows(UnsupportedOperationException.class, () ->
                TimerMaps.getNftRankVoList().add(new NFTRankVo(2, "0x2", 2))
        );
    }

    @Test
    void shouldUpdateGameStatusAtomically() {
        TimerMaps.startDiceRoller();
        TimerMaps.startGuessBnbPrice();
        TimerMaps.startGuessEvent();

        Assertions.assertTrue(TimerMaps.getDicRollerStatus());
        Assertions.assertTrue(TimerMaps.getGuessBnbPriceStatus());
        Assertions.assertTrue(TimerMaps.getGuessEventStatus());

        TimerMaps.stopDiceRoller();
        TimerMaps.stopGuessBnbPrice();
        TimerMaps.stopGuessEvent();

        Assertions.assertFalse(TimerMaps.getDicRollerStatus());
        Assertions.assertFalse(TimerMaps.getGuessBnbPriceStatus());
        Assertions.assertFalse(TimerMaps.getGuessEventStatus());
    }
}
