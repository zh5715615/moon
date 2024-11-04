package tcbv.zhaohui.moon.service;


import org.springframework.web.bind.annotation.RequestBody;
import tcbv.zhaohui.moon.dto.AddGameOrderForDTO;
import tcbv.zhaohui.moon.vo.PlayResidueTimesVO;

import javax.validation.Valid;

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
     *
     * @param dto 添加游戏下注单
     * @return
     */
   Boolean addGameOrderFor(  AddGameOrderForDTO dto);
}
