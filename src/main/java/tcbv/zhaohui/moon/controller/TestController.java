package tcbv.zhaohui.moon.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.tuples.generated.Tuple3;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhaohui
 * @Title: TestController
 * @Description:
 * @date: 2025/12/24 18:49
 */
@RestController
@RequestMapping("/api/v1/moon/test")
@Slf4j
public class TestController {
    private int packageCnt;

    private Map<String, Integer> userPackageCnt = new HashMap<>();

    @GetMapping("/getPackageCnt")
    public Tuple3<Integer, Double, Integer> getPackageCnt() {
        double price = 0.0;
        int stage;
        if (packageCnt < 5) {
            price = 0.01;
            stage = 1;
        } else if (packageCnt < 9) {
            price = 0.015;
            stage = 2;
        } else if (packageCnt < 13) {
            price = 0.02;
            stage = 3;
        } else if (packageCnt < 17) {
            price = 0.025;
            stage = 4;
        } else if (packageCnt < 20) {
            price = 0.03;
            stage = 5;
        } else {
            stage = 0;
        }
        return new Tuple3<>(packageCnt, price, stage);
    }

    @GetMapping("/getUserBuyPackageCnt")
    public int getUserBuyPackageCnt(@RequestParam("address") String address) {
        if (userPackageCnt.containsKey(address)) {
            return userPackageCnt.get(address);
        }
        return 0;
    }

    @GetMapping("/buySpaceJediPackage")
    public String buySpaceJediPackage(@RequestParam("address") String address, @RequestParam("buyCnt") int buyCnt) {
        int userOwnCnt = 0;
        if (userPackageCnt.containsKey(address)) {
            userOwnCnt = userPackageCnt.get(address);
        }
        if (packageCnt >= 20) {
            return "所有套餐已售罄";
        }
        if (userOwnCnt + buyCnt > 3) {
            return "每个地址只能购买3个套餐";
        }
        Tuple3<Integer, Double, Integer> tuple3 = getPackageCnt();
        double price = tuple3.component2();
        int stage = tuple3.component3();
        if (stage == 1 && packageCnt + buyCnt > 5) {
            buyCnt = 5 - packageCnt;
            log.info("第一阶段只剩{}个", buyCnt);
        }
        if (stage == 2 && packageCnt + buyCnt > 9) {
            buyCnt = 9 - packageCnt;
            log.info("第二阶段只剩{}个", buyCnt);
        }
        if (stage == 3 && packageCnt + buyCnt > 13) {
            buyCnt = 13 - packageCnt;
            log.info("第三阶段只剩{}个", buyCnt);
        }
        if (stage == 4 && packageCnt + buyCnt > 17) {
            buyCnt = 17 - packageCnt;
            log.info("第四阶段只剩{}个", buyCnt);
        }
        if (packageCnt + buyCnt > 20) {
            buyCnt = 20 - packageCnt;
            log.info("第五阶段只剩{}个", buyCnt);
        }
        int packageSjNumber = 1350;
        double totalCost = packageSjNumber * price * buyCnt;
        packageCnt += buyCnt;
        userPackageCnt.put(address, userOwnCnt + buyCnt);
        return "购买成功";
    }
}
