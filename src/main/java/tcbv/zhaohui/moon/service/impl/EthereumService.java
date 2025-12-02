package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import tcbv.zhaohui.moon.beans.BlockInfoBean;
import tcbv.zhaohui.moon.beans.EthTransactionBean;
import tcbv.zhaohui.moon.beans.TransactionBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.service.IEthereumService;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EthereumService implements IEthereumService {
    protected Web3j web3j;

    protected Credentials credentials;

    protected Credentials weekCredentials;

    protected Credentials moonCredentials;

    protected ContractGasProvider contractGasProvider;

    @Autowired
    protected Web3Config web3Config;

    @Override
    public void init() {
        HttpService httpService = new HttpService(web3Config.getEthUrl());
        web3j = Web3j.build(httpService);
        credentials = Credentials.create(web3Config.getUserPrivKey());
        contractGasProvider = new ContractGasProvider() {
            @Override
            public BigInteger getGasPrice(String method) {
                return new BigInteger(web3Config.getGasPrice());
            }

            @Override
            public BigInteger getGasPrice() {
                return new BigInteger(web3Config.getGasPrice());
            }

            @Override
            public BigInteger getGasLimit(String method) {
                return new BigInteger(web3Config.getGasLimit());
            }

            @Override
            public BigInteger getGasLimit() {
                return new BigInteger(web3Config.getGasLimit());
            }
        };
    }

    @Override
    public BlockInfoBean getBlockInfo(long height) throws IOException {
        BlockInfoBean blockInfoBean = new BlockInfoBean();
        EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(height), false).send().getBlock();
        blockInfoBean.setBlockHash(block.getHash());
        blockInfoBean.setTimestamp(block.getTimestamp().longValue());
        blockInfoBean.setTxCount(block.getTransactions().size());
        blockInfoBean.setBlockHeigh(block.getNumber().longValue());
        List<String> txHashList = new ArrayList<>();
        block.getTransactions().forEach(transactionResult -> txHashList.add((String) transactionResult.get()));
        blockInfoBean.setTxHashList(txHashList);
        return blockInfoBean;
    }

    @Override
    public long lastHeigh() {
        if (web3j == null) {
            return 0;
        }
        EthBlockNumber ethBlockNumber = null;
        try {
            ethBlockNumber = web3j.ethBlockNumber().send();
        } catch (IOException e) {
            log.error("Get node height failed: ", e);
            return 0L;
        }
        if (ethBlockNumber == null) {
            log.error("Get node height failed, ethBlockNumber is null");
            return 0L;
        }
        BigInteger blockNumber = ethBlockNumber.getBlockNumber();
        if (blockNumber == null) {
            log.error("Get node height failed, blockNumber is null");
            return 0L;
        }
        return blockNumber.longValue();
    }

    protected TransactionBean getBaseTransactionInfo(Transaction transaction) throws IOException {
        if (transaction == null) {
            return null;
        }
        TransactionBean transactionBean = new TransactionBean();
        transactionBean.setTxHash(transaction.getHash());
        long blockNumber = transaction.getBlockNumber().longValue();
        transactionBean.setBlockHeight(blockNumber);
        BlockInfoBean blockInfoBean = getBlockInfo(blockNumber);
        transactionBean.setFromAddress(transaction.getFrom());
        if (blockInfoBean != null) {
            transactionBean.setTimestamp(blockInfoBean.getTimestamp());
            return transactionBean;
        } else {
            return null;
        }
    }

    protected Transaction getTransaction(String txHash) throws IOException {
        Optional<Transaction> optional = web3j.ethGetTransactionByHash(txHash).send().getTransaction();
        return optional.orElse(null);
    }

    @Override
    public TransactionBean getTransactionInfo(String txHash) throws IOException {
        Transaction transaction = getTransaction(txHash);
        TransactionBean transactionBean = getBaseTransactionInfo(transaction);
        if (transactionBean == null) {
            return null;
        }
        EthTransactionBean ethTransactionBean = new EthTransactionBean();
        BeanUtils.copyProperties(transactionBean, ethTransactionBean);
        ethTransactionBean.setFromAddress(transaction.getFrom());
        ethTransactionBean.setToAddress(transaction.getTo());
        BigInteger amountBigInteger = transaction.getValue();
        BigDecimal bigDecimal = new BigDecimal(amountBigInteger);
        double amount = bigDecimal.divide(BigDecimal.TEN.pow(18)).doubleValue();
        ethTransactionBean.setAmount(amount);
        return ethTransactionBean;
    }

    @Override
    public double getEthBalance(String address) {
        EthGetBalance ethGetBalance = null;
        try {
            ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        } catch (IOException e) {
            log.error("Get {} balance failed: ", address, e);
            return 0;
        }
        if (ethGetBalance == null) {
            log.error("Get {} balance failed, balance is null", address);
            return 0;
        }
        BigInteger balance = ethGetBalance.getBalance();
        if (balance == null) {
            log.error("Get {} balance failed, balance is null", address);
            return 0;
        }
        BigDecimal bigDecimal = new BigDecimal(balance);
        return bigDecimal.divide(BigDecimal.TEN.pow(18)).doubleValue();
    }

    @Override
    public long getAccountNonce(String address) throws IOException {
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).send();
        if (ethGetTransactionCount == null) {
            return 0;
        }
        return ethGetTransactionCount.getTransactionCount().longValue();
    }

    /**
     * 解析log返回的data
     * @param event 合约中定义的事件
     * @param log 监听到的log
     * @return 解析后的数据
     */
    private EventValues staticExtractEventParameters(Event event, Log log) {
        final List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (topics == null || topics.size() == 0 || !topics.get(0).equals(encodedEventSignature)) {
            return null;
        }
        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                log.getData(), event.getNonIndexedParameters());
        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }

    @Override
    public String sendEth(String toAddress, double amount) throws IOException {
        long nonce = getAccountNonce(web3Config.getUserAddress());
        BigInteger gasPrice = new BigInteger(web3Config.getGasPrice());
        BigInteger gasLimit = new BigInteger(web3Config.getGasLimit());
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                BigInteger.valueOf(nonce), gasPrice, gasLimit, toAddress, EthMathUtil.doubleToBigInteger(amount, 18));
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, web3Config.getChainId(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        if (ethSendTransaction.hasError()) {
            log.info("transfer error: {}", ethSendTransaction.getError().getMessage());
            throw new RuntimeException("transfer error: " + ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("Transfer transactionHash:" + transactionHash);
            return transactionHash;
        }
    }
}
