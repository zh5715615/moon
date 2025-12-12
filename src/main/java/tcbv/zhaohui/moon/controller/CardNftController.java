package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tcbv.zhaohui.moon.beans.NFTMetadataBean;
import tcbv.zhaohui.moon.oss.OssConfig;
import tcbv.zhaohui.moon.oss.OssService;
import tcbv.zhaohui.moon.oss.SysOss;
import tcbv.zhaohui.moon.service.ICardNFTTokenService;
import tcbv.zhaohui.moon.utils.Rsp;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author: zhaohui
 * @Title: CardNftController
 * @Description:
 * @date: 2025/12/12 10:49
 */
@RestController
@RequestMapping("/api/v1/card/nft")
@Validated
@ResponseBody
@Api(tags="钱包地址管理")
@Slf4j
public class CardNftController {
    @Autowired
    private ICardNFTTokenService cardNftService;

    @Autowired
    private OssService ossService;

    @Autowired
    private OssConfig ossConfig;

    @PostMapping("/mint")
    @ApiOperation("铸造NFT")
    public Rsp mint(MultipartFile file) throws Exception {
        NFTMetadataBean nftMetadataBean = new NFTMetadataBean();
        nftMetadataBean.setName("测试卡片");
        String bucketName = "card-nft";
        SysOss sysOss = ossService.upload(bucketName, "image", file);
        String url = ossConfig.getEndpoint() + "/" + bucketName + "/" + sysOss.getFileName();
        nftMetadataBean.setImage(url);
        nftMetadataBean.setAttributes(Collections.emptyList());
        String txHash = cardNftService.mint("0xa24bDb249e80574A96D8B02b148E81B9be684675", nftMetadataBean);
        return Rsp.ok(txHash);
    }
}
