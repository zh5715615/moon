package tcbv.zhaohui.moon.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 以太坊配置
 */
@Configuration
@Getter
public class Web3Config {
    @Value("${web3.url}")
    private String ethUrl; //以太坊rpc地址

    @Value("${web3.chain-id}")
    private long chainId; //链id

    @Value("${web3.account.private-key}")
    private String userPrivKey; //用户私钥

    @Value("${web3.account.address}")
    private String userAddress; //用户地址

    @Value("${web3.week-account.private-key}")
    private String weekPoolPrivKey; //用户私钥

    @Value("${web3.week-account.address}")
    private String weekPoolAddress; //用户地址

    @Value("${web3.month-account.private-key}")
    private String monthPoolPrivKey; //用户私钥

    @Value("${web3.month-account.address}")
    private String monthPoolAddress; //用户地址


    @Value("${web3.gas-fee.price}")
    private String gasPrice;  //交易gas费

    @Value("${web3.gas-fee.limit}")
    private String gasLimit; //交易gas上限

    @Value("${blockchain.proxy.enable:false}")
    private boolean proxy;

    @Value("${blockchain.proxy.hostname:127.0.0.1}")
    private String hostname;

    @Value("${blockchain.proxy.port:1081}")
    private int port;

    @Value("${web3.contract.moon-base.address}")
    private String moonBaseAddress;

    @Value("${web3.contract.moon-nft.address}")
    private String moonNFTAddress;

    @Value("${web3.contract.moon-token.address}")
    private String moonTokenAddress;
}
