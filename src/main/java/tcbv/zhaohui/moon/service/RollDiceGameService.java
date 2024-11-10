package tcbv.zhaohui.moon.service;


import org.springframework.web.bind.annotation.RequestBody;
import tcbv.zhaohui.moon.dto.*;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.vo.PageResultVo;
import tcbv.zhaohui.moon.vo.PlayResidueTimesVO;
import tcbv.zhaohui.moon.vo.UserRewardListVO;

import javax.validation.Valid;
import java.math.BigInteger;

/**
 * @author dawn
 * @date 2024/11/2 14:21
 */
public interface RollDiceGameService {
    /**
     * @param gameType
     * @return 根据游戏类型判断是否允许下注
     */
    PlayResidueTimesVO getQueueAndMemSize(Integer gameType);

    /**
     * @param dto 添加游戏下注单
     * @return
     */
    Boolean addGameOrderFor(AddGameOrderForDTO dto);

    /**
     *
     * @param dto 根据轮次查询是否开奖
     * @return
     */
    Boolean verifyGamePrizeDraw(VerifyGamePrizeDrawDTO dto);

    /**
     *
     * @param dto 查询游戏下注单列表
     * @return
     */
    PageResultVo<TbTxRecord> userOrderList(@RequestBody @Valid OrderListDTO dto);

    /**
     *
     * @param dto 查询中奖列表
     * @return
     */
    PageResultVo<UserRewardListVO> userRewardList(@RequestBody @Valid RewardListDTO dto);

    /**
     *
     * @param dto 查询各游戏轮次的开奖结果
     * @return
     */

    TbGameResult gameResultList(@RequestBody @Valid GameResultDto dto);

    /**
     *
     * @param gameType 前一轮次游戏开奖结果
     * @return
     */
    TbGameResult preTurnsResult(Integer gameType);

    /**
     * 是否已经投注
     * @param gameType 游戏类型
     * @param turns 轮次
     * @return true 是 | false 否
     */
    boolean isBetOn(String userId, Integer gameType, Integer turns);

    /**
     * 下注统计
     * @param gameType 游戏类型
     * @param turns 轮次
     * @param betType 下注类型
     * @return 统计数量
     */
    Double betNumber(int gameType, int turns, Integer betType);
}
