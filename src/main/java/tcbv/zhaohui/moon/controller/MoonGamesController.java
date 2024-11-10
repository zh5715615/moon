package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.dao.TbGameResultDao;
import tcbv.zhaohui.moon.dto.*;
import tcbv.zhaohui.moon.entity.TbGameResult;
import tcbv.zhaohui.moon.entity.TbTxRecord;
import tcbv.zhaohui.moon.scheduled.TimerMaps;
import tcbv.zhaohui.moon.service.RollDiceGameService;
import tcbv.zhaohui.moon.service.impl.MoonBaseService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.PageResultVo;
import tcbv.zhaohui.moon.vo.PlayResidueTimesVO;
import tcbv.zhaohui.moon.vo.UserRewardListVO;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * (TbWatchAddress)表控制层
 *
 * @author makejava
 * @since 2023-08-27 16:26:22
 */
@RestController
@RequestMapping("/game")
public class MoonGamesController {
    @Resource
    private RollDiceGameService rollDiceGameService;

    @Autowired
    private MoonBaseService moonBaseService;

    @Resource
    private TbGameResultDao gameResultDao;

    @GetMapping("/playResidueTimes")
    @ApiOperation(value = "根据类型获取轮次和是否允许下注")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameType", value="游戏类型: 1投骰子 | 2猜BNB涨跌 | 3猜事件", required = true)
    })
    public Rsp<PlayResidueTimesVO> playResidueTimes(@RequestParam(name = "gameType", required = true) Integer gameType) {
        boolean status;
        if (Objects.equals(gameType, MoonConstant.DICE_ROLLER_GAME)) {
            status = TimerMaps.getDicRollerStatus();
        } else if (gameType.equals(MoonConstant.GUESS_BNB_PRICE_NAME)) {
            status = TimerMaps.getGuessBnbPriceStatus();
        } else {
            return Rsp.error("暂时没有这个游戏类型");
        }
        PlayResidueTimesVO playResidueTimesVO = new PlayResidueTimesVO();
        playResidueTimesVO.setGameType(gameType);
        playResidueTimesVO.setTurns(gameResultDao.maxTurns(gameType));
        playResidueTimesVO.setIsOk(status);

        return Rsp.okData(playResidueTimesVO);
    }

    @PostMapping("/addGameOrderFor")
    @ApiOperation(value = "添加游戏下注单")
    public Rsp addGameOrderFor(@RequestBody @Valid AddGameOrderForDTO dto) {
//        try {
//            RecordBean recordBean = moonBaseService.getRecord(new BigInteger(dto.getRecordId(), 16));
//            if (recordBean == null) {
//                return Rsp.error("记录不存在");
//            }
//            if (!recordBean.getPlayer().equals(dto.getAddress())) {
//                return Rsp.error("用户地址不匹配，tx address is " + dto.getAddress() + " record address is " + recordBean.getPlayer());
//            }
//        } catch (Exception e) {
//            return Rsp.error(e.getMessage());
//        }
        return Rsp.okData(rollDiceGameService.addGameOrderFor(dto));
    }

    @PostMapping("/userOrderList")
    @ApiOperation(value = "查询游戏下注单列表")
    public Rsp<PageResultVo<TbTxRecord>> userOrderList(@RequestBody @Valid OrderListDTO dto) {
        return Rsp.okData(rollDiceGameService.userOrderList(dto));
    }

    @PostMapping("/userRewardList")
    @ApiOperation(value = "查询中奖列表")
    public Rsp<PageResultVo<UserRewardListVO>> userRewardList(@RequestBody @Valid RewardListDTO dto) {
        return Rsp.okData(rollDiceGameService.userRewardList(dto));
    }

    @PostMapping("/gameResultList")
    @ApiOperation(value = "查询各游戏轮次的开奖结果")
    public Rsp<TbGameResult> gameResultList(@RequestBody @Valid GameResultDto dto) {
        return Rsp.okData(rollDiceGameService.gameResultList(dto));
    }

    @GetMapping("/preTurnsResult")
    @ApiOperation(value = "前一轮次游戏开奖结果")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameType", value="游戏类型: 1投骰子 | 2猜BNB涨跌 | 3猜事件", required = true)
    })
    public Rsp<TbGameResult> preTurnsResult(@RequestParam("gameType") @NotBlank(message = "游戏类型不能为空") Integer gameType) {
        return Rsp.okData(rollDiceGameService.preTurnsResult(gameType));
    }

    @PostMapping("/addNewGame")
    @ApiOperation(value = "添加时事竞猜")
    public Rsp addNewGame(@RequestBody @Valid AddNewGameDTO dto) {
        return Rsp.okData(true);
    }


    @PostMapping("/newGamePrizeDraw")
    @ApiOperation(value = "时事竞猜开奖")
    public Rsp newGamePrizeDraw(@RequestBody @Valid NewGamePrizeDrawDTO dto) {
        return Rsp.okData(true);
    }
    @PostMapping("/gamePrizeDraw")
    @ApiOperation(value = "确认游戏是否开奖")
    public Rsp verifyGamePrizeDraw(@RequestBody @Valid VerifyGamePrizeDrawDTO dto) {
        return Rsp.okData(rollDiceGameService.verifyGamePrizeDraw(dto));
    }
    @PostMapping("/userMarketing")
    @ApiOperation(value = "用户推广码生成")
    public Rsp userMarketing(@RequestBody @Valid UserMarketingDTO dto) {
        return Rsp.okData(true);
    }

    @GetMapping("/isBetOn")
    @ApiOperation(value = "是否重复投注")
    @ApiImplicitParams({
            @ApiImplicitParam(name="userId", value="用户id", required = true),
            @ApiImplicitParam(name="gameType", value="游戏类型: 1摇色子 | 2猜BNB涨跌", required = true),
            @ApiImplicitParam(name="turns", value="游戏轮次", required = true),
    })
    public Rsp<Boolean> isBetOn(@RequestParam("userId") @NotBlank(message = "用户id不能为空") String userId,
                       @RequestParam("gameType") @NotNull(message = "游戏类型不能为空") Integer gameType,
                       @RequestParam("turns") @NotNull(message = "轮次不能为空") Integer turns) {
        boolean ret = rollDiceGameService.isBetOn(userId, gameType, turns);
        return Rsp.okData(ret);
    }

    @GetMapping("/dicRollerBetNumber")
    @ApiOperation(value = "投骰子单双投注统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name="turns", value="游戏轮次", required = true),
            @ApiImplicitParam(name="betType", value="投注结果 1单 | 2双", required = true)
    })
    public Rsp<Double> dicRollerSingleNumber(@RequestParam("turns") @NotNull(message = "轮次不能为空") Integer turns,
                                     @RequestParam("betType") @NotNull(message = "下注类型不能为空") Integer betType) {
        return Rsp.okData(rollDiceGameService.betNumber(1, turns, betType));
    }

    @GetMapping("/guessBnbBetNumber")
    @ApiOperation(value = "猜BNB涨跌投注统计")
    @ApiImplicitParams({
            @ApiImplicitParam(name="turns", value="游戏轮次", required = true),
            @ApiImplicitParam(name="betType", value="投注结果 1涨 | 2跌", required = true)
    })
    public Rsp<Double> guessBnbBetNumber(@RequestParam("turns") @NotNull(message = "轮次不能为空") Integer turns,
                                    @RequestParam("betType") @NotNull(message = "下注类型不能为空") Integer betType) {
        return Rsp.okData(rollDiceGameService.betNumber(2, turns, betType));
    }
}
