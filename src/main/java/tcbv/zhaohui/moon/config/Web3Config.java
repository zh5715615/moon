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

    @Value("${web3.gas-fee.price}")
    private String gasPrice;  //交易gas费

    @Value("${web3.gas-fee.limit}")
    private String gasLimit; //交易gas上限

    @Value("${web3.contract.usdt.address}")
    private String usdtContractAddress; //合约地址

    @Value("${web3.contract.space-jedi.address}")
    private String spaceJediContractAddress; //合约地址

    @Value("${web3.contract.card-nft.address}")
    private String cardNftContractAddress; //合约地址

    @Value("${web3.contract.dapp-pool.address}")
    private String dappPoolContractAddress; //合约地址
}
