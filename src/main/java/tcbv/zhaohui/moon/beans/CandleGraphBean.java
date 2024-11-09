package tcbv.zhaohui.moon.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CandleGraphBean {
    private long startTime; //开盘时间

    private long endTime; //收盘时间

    private double openPrice; //开盘价

    private double closePrice; //收盘价
}
