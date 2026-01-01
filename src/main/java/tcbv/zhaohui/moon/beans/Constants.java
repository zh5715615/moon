package tcbv.zhaohui.moon.beans;

/**
 * @author: zhaohui
 * @Title: Constants
 * @Description:
 * @date: 2025/12/12 13:58
 */
public class Constants {
    /** oss存储对象相关常量 begin **/
    public static final String OSS_WALLET_PREFIX = "wallet";
    public static final String OSS_NFT_PREFIX = "nft";
    public static final String OSS_NFT_METADATA_PREFIX = OSS_NFT_PREFIX + "/metadata";
    public static final String OSS_NFT_IMAGE_PREFIX = OSS_NFT_PREFIX + "/image";
    /** oss存储对象相关常量 end **/

    /** 预售相关常量 begin **/
    public static final int PRESALE_TOTAL = 3000;         //预售总数3000套
    public static final int INNER_PRESALE_TOTAL = 1000;   //内步预售1000套
    public static final int PRESALE_SJ_NUMBER_PER = 1350; //每套1350
    /** 预售相关常量 end **/

    /** 推广相关常量 begin **/
    public static final int PROMOTE_TOTAL = 13500000; //推广奖励1000
    /** 推广相关常量 end **/
}
