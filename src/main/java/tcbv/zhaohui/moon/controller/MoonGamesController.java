package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.*;
import tcbv.zhaohui.moon.service.RollDiceGameService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.PlayResidueTimesVO;

import javax.annotation.Resource;
import javax.validation.Valid;

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

    @GetMapping("/playResidueTimes")
    @ApiOperation(value = "根据类型获取轮次和是否允许下注")
    @ApiImplicitParams({
            @ApiImplicitParam(name="gameType", value="游戏类型: 1投骰子 | 2猜BNB涨跌 | 3猜事件", required = true)
    })
    public Rsp<PlayResidueTimesVO> playResidueTimes(@RequestParam(name = "gameType", required = true) Integer gameType) {
        return Rsp.okData(rollDiceGameService.getQueueAndMemSize(gameType));
    }

    @PostMapping("/addGameOrderFor")
    @ApiOperation(value = "添加游戏下注单")
    public Rsp addGameOrderFor(@RequestBody @Valid AddGameOrderForDTO dto) {
        return Rsp.okData(rollDiceGameService.addGameOrderFor(dto));
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
}
