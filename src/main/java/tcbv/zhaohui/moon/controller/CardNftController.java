package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tcbv.zhaohui.moon.beans.NFTAttributesBean;
import tcbv.zhaohui.moon.beans.NFTMetadataBean;
import tcbv.zhaohui.moon.beans.NFTTokenMintInfoBean;
import tcbv.zhaohui.moon.beans.events.CancelOrderEventBean;
import tcbv.zhaohui.moon.beans.events.NFTTradeOrderEventBean;
import tcbv.zhaohui.moon.beans.events.SubmitOrderEventBean;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dto.TradeOrderDto;
import tcbv.zhaohui.moon.dto.TransactionDto;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.NftOrderEntity;
import tcbv.zhaohui.moon.enums.ChainTxBizType;
import tcbv.zhaohui.moon.enums.NftOrderStatusEnum;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.oss.BucketType;
import tcbv.zhaohui.moon.oss.OssConfig;
import tcbv.zhaohui.moon.oss.OssService;
import tcbv.zhaohui.moon.oss.SysOss;
import tcbv.zhaohui.moon.service.ChainTxTaskService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.chain.CardNFTTokenService;
import tcbv.zhaohui.moon.service.NftOrderService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxTaskDispatcher;
import tcbv.zhaohui.moon.utils.GsonUtil;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.TaskStatusVo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static tcbv.zhaohui.moon.beans.Constants.OSS_NFT_IMAGE_PREFIX;
import static tcbv.zhaohui.moon.oss.OssClient.CONTENT_TYPE_IMAGE;

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
@Api(tags="卡片NFT管理")
@Slf4j
public class CardNftController {
    @Autowired
    private CardNFTTokenService cardNftService;

    @Autowired
    private NftOrderService nftOrderService;

    @Autowired
    private OssService ossService;

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private Web3Config web3Config;

    @Autowired
    private ChainTxTaskService chainTxTaskService;

    @Autowired
    private ChainTxTaskDispatcher chainTxTaskDispatcher;

    @Syslog(module = "NFT")
    @PostMapping("/mint")
    @ApiOperation("铸造NFT")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "name", value = "卡片名称", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "string", name = "description", value = "卡片描述", required = false),
            @ApiImplicitParam(paramType = "form", dataType = "file", name = "file", value = "卡片图片", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "attributesMapStr", value = "卡片属性", required = false)
    })
    @JwtAddressRequired(role = "admin")
    public Rsp<NFTTokenMintInfoBean> mint(
            @NotBlank(message = "卡片名称不能为空") @RequestParam("name") String name,
            @RequestParam("description") String description,
            @NotNull(message = "卡片图片不能为空") @RequestParam("file") MultipartFile file,
            @RequestParam("attributesMapStr") String attributesMapStr
    ) throws Exception {
        NFTMetadataBean nftMetadataBean = new NFTMetadataBean();
        nftMetadataBean.setName(name);
        String bucketName = BucketType.PUBLIC_BUCKET.getBucketName();
        SysOss sysOss = ossService.upload(BucketType.PUBLIC_BUCKET, OSS_NFT_IMAGE_PREFIX, file, CONTENT_TYPE_IMAGE);
        String url = ossConfig.getEndpoint() + "/" + bucketName + "/" + sysOss.getFileName();
        nftMetadataBean.setImage(url);
        List<NFTAttributesBean> attributes;
        if (!StringUtils.isEmpty(attributesMapStr)) {
            Map<String, String> attributesMap = GsonUtil.fromJson(attributesMapStr, Map.class);
            if (!CollectionUtils.isEmpty(attributesMap)) {
                attributes = attributesMap.entrySet().stream()
                        .map(e -> new NFTAttributesBean(e.getKey(), e.getValue()))
                        .collect(Collectors.toList());
            } else {
                attributes = Collections.emptyList();
            }
        } else {
            attributes = Collections.emptyList();
        }
        nftMetadataBean.setAttributes(attributes);
        nftMetadataBean.setDescription(description);
        NFTTokenMintInfoBean nftTokenMintInfoBean = cardNftService.mint(web3Config.getContractOwnerAddress(), nftMetadataBean);
        return Rsp.okData(nftTokenMintInfoBean);
    }

    @PostMapping("/submitOrder")
    @ApiOperation("挂单卡片")
    @JwtAddressRequired
    public Rsp<TaskStatusVo> submitOrder(@RequestBody @Validated TransactionDto dto) {
        ChainTaskParams params = new ChainTaskParams();
        params.setTxHash(dto.getTxHash());
        params.setUserId(JwtContext.getUserId());
        params.setAddress(JwtContext.getAddress());
        ChainTxTaskEntity task = new ChainTxTaskEntity();
        task.setTxHash(dto.getTxHash());
        task.setBizType(ChainTxBizType.NFT_SUBMIT_ORDER.name());
        task.setBizParams(GsonUtil.toJson(params, false));
        chainTxTaskService.insert(task);
        chainTxTaskDispatcher.dispatch(task);
        return Rsp.okData(TaskStatusVo.of(task.getId(), 0, null));
    }

    @PostMapping("/tradeOrder")
    @ApiOperation("订单成交")
    @JwtAddressRequired
    public Rsp<TaskStatusVo> tradeOrder(@RequestBody @Validated TradeOrderDto dto) {
        ChainTaskParams params = new ChainTaskParams();
        params.setTxHash(dto.getTxHash());
        params.setUserId(JwtContext.getUserId());
        params.setAddress(JwtContext.getAddress());
        params.setNftOrderId(dto.getNftOrderId());
        ChainTxTaskEntity task = new ChainTxTaskEntity();
        task.setTxHash(dto.getTxHash());
        task.setBizType(ChainTxBizType.NFT_TRADE_ORDER.name());
        task.setBizParams(GsonUtil.toJson(params, false));
        chainTxTaskService.insert(task);
        chainTxTaskDispatcher.dispatch(task);
        return Rsp.okData(TaskStatusVo.of(task.getId(), 0, null));
    }

    @PostMapping("/cancelOrder")
    @ApiOperation("取消订单")
    @JwtAddressRequired
    public Rsp<TaskStatusVo> cancelOrder(@RequestBody @Validated TradeOrderDto dto) {
        ChainTaskParams params = new ChainTaskParams();
        params.setTxHash(dto.getTxHash());
        params.setUserId(JwtContext.getUserId());
        params.setAddress(JwtContext.getAddress());
        params.setNftOrderId(dto.getNftOrderId());
        ChainTxTaskEntity task = new ChainTxTaskEntity();
        task.setTxHash(dto.getTxHash());
        task.setBizType(ChainTxBizType.NFT_CANCEL_ORDER.name());
        task.setBizParams(GsonUtil.toJson(params, false));
        chainTxTaskService.insert(task);
        chainTxTaskDispatcher.dispatch(task);
        return Rsp.okData(TaskStatusVo.of(task.getId(), 0, null));
    }
}
