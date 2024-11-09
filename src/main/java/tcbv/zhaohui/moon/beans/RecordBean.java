package tcbv.zhaohui.moon.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordBean {
    private String token;  //支付token的 address

    private BigInteger amount; //支付 token 的数量

    private BigInteger fee;    //fee数量

    private BigInteger redeemAmount; //兑换的数量

    private String  player; //玩家地址

    private String  extraData;  //额外的数据
}
