package tcbv.zhaohui.moon.controller;

import com.google.common.collect.TreeMultimap;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import tcbv.zhaohui.moon.config.MoonConstant;
import tcbv.zhaohui.moon.entity.TbNftReward;
import tcbv.zhaohui.moon.scheduled.TimerMaps;
import tcbv.zhaohui.moon.service.TbNftRewardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import tcbv.zhaohui.moon.service.impl.MoonNFTService;
import tcbv.zhaohui.moon.vo.NFTRankVo;

import java.util.*;

/**
 * (TbNftReward)表控制层
 *
 * @author makejava
 * @since 2024-11-19 20:14:47
 */
@RestController
@RequestMapping("/nftRank")
public class TbNftRewardController {
    /**
     * 服务对象
     */
    @Autowired
    private TbNftRewardService tbNftRewardService;

    @Autowired
    private MoonNFTService moonNFTService;


    private List<NFTRankVo> getNFTRankList(List<TbNftReward> nftRewardList) {
        List<NFTRankVo> nftRankVoList = new ArrayList<>();
        int i = 1;
        for (TbNftReward nftReward : nftRewardList) {
            NFTRankVo nftRankVo = new NFTRankVo();
            BeanUtils.copyProperties(nftReward, nftRankVo);
            nftRankVo.setRank(i);
            nftRankVoList.add(nftRankVo);
            i++;
        }
        return nftRankVoList;
    }
    /**
     * 查询周排名
     * @return 查询结果
     */
    @PostMapping("/weekRank")
    @ApiOperation(value = "周排名")
    public ResponseEntity<List<NFTRankVo>> weekRank() {
        PageRequest pageRequest = PageRequest.of(0, MoonConstant.NFT_WEEK_COUNT, Sort.by(Sort.Direction.DESC, "nft_amount"));
        Page<TbNftReward> page = this.tbNftRewardService.queryByPage(new TbNftReward(MoonConstant.NFT_WEEK_CYCLE), pageRequest);
        if (page.getTotalElements() < 1) {
            return ResponseEntity.ok(Collections.emptyList());
        }

        return ResponseEntity.ok(getNFTRankList(page.getContent()));
    }

    @PostMapping("/monthRank")
    @ApiOperation(value = "月排名")
    public ResponseEntity<List<NFTRankVo>> monthRank() {
        PageRequest pageRequest = PageRequest.of(0, MoonConstant.NFT_MONTH_COUNT, Sort.by(Sort.Direction.DESC, "nft_amount"));
        Page<TbNftReward> page = this.tbNftRewardService.queryByPage(new TbNftReward(MoonConstant.NFT_MONTH_CYCLE), pageRequest);
        return ResponseEntity.ok(getNFTRankList(page.getContent()));
    }

    @PostMapping("/realtimeRank")
    @ApiOperation(value = "实时排名")
    public ResponseEntity<List<NFTRankVo>> realtimeRank() {
        return ResponseEntity.ok(TimerMaps.getNftRankVoList());
    }
}

