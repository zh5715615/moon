package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.AddGameOrderForDTO;
import tcbv.zhaohui.moon.dto.AddNewGameDTO;
import tcbv.zhaohui.moon.dto.NewGamePrizeDrawDTO;
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


}
