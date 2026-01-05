package tcbv.zhaohui.moon.service.chain.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.web3j.crypto.*;
import org.web3j.protocol.exceptions.TransactionException;
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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Numeric;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.entity.WalletEntity;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.oss.BucketType;
import tcbv.zhaohui.moon.oss.OssService;
import tcbv.zhaohui.moon.service.chain.EthereumService;
import tcbv.zhaohui.moon.service.WalletService;
import tcbv.zhaohui.moon.utils.AesUtil;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static tcbv.zhaohui.moon.exceptions.ChainException.*;

@Service("ethereumService")
@Slf4j
public class EthereumServiceImpl implements EthereumService {
    protected Web3j web3j;

    protected Credentials credentials;

    protected Credentials promoteCredentials;

    protected Map<String, Credentials> mgr500Account = new ConcurrentHashMap<>();

    protected ContractGasProvider contractGasProvider;

    @Autowired
    protected Web3Config web3Config;

    @Autowired
    private OssService ossService;

    @Autowired
    private WalletService walletService;

    @Value("${star-wars.secret.aes-key}")
    private String aesKey;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    private String parseSecretKey(String address, String encryptPassword) throws Exception {
        String password = AesUtil.decrypt(aesKey, encryptPassword);
        String fileUrl = "wallet/" + address.toLowerCase() + ".json";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ossService.downloadFileByName(BucketType.PRIVATE_BUCKET, fileUrl, baos);
        String keystoreJson = baos.toString(StandardCharsets.UTF_8.name());
        WalletFile walletFile = objectMapper.readValue(keystoreJson, WalletFile.class);
        BigInteger privateKey = Wallet.decrypt(password, walletFile).getPrivateKey();
        return Numeric.toHexStringWithPrefix(privateKey);
    }

    private void initMgr500Account() throws Exception {
        List<WalletEntity> walletEntityList = walletService.queryAll();
        if (CollectionUtils.isEmpty(walletEntityList)) {
            return;
        }
        for (WalletEntity walletEntity : walletEntityList) {
            try {
                String privateKey = parseSecretKey(walletEntity.getAddress(), walletEntity.getEncryptPwd());
                mgr500Account.put(walletEntity.getAddress(), Credentials.create(privateKey));
            } catch (CipherException e) {
                log.error("initMgr500Account error, address:{}", walletEntity.getAddress(), e);
            }
        }
    }

    @Override
    public void init() {
        HttpService httpService = new HttpService(web3Config.getEthUrl());
        web3j = Web3j.build(httpService);
        try {
            String privateKey = parseSecretKey(web3Config.getContractOwnerAddress(), web3Config.getContractOwnerEncryptPassword());
            credentials = Credentials.create(privateKey);
            String promotePrivateKey = parseSecretKey(web3Config.getPromoterAddress(), web3Config.getPromoterEncryptPassword());
            promoteCredentials = Credentials.create(promotePrivateKey);
//            initMgr500Account();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public Web3j getWeb3j() {
        return web3j;
    }

    @Override
    public Credentials getCredentials() {
        return credentials;
    }

    @Override
    public Credentials getPromoteCredentials() {
        return promoteCredentials;
    }

    @Override
    public Map<String, Credentials> getCredentialsMap() {
        return mgr500Account;
    }

    @Override
    public ContractGasProvider getContractGasProvider() {
        return contractGasProvider;
    }

    @Override
    public Web3Config getWeb3Config() {
        return web3Config;
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
        BigDecimal amount = bigDecimal.divide(BigDecimal.TEN.pow(18));
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
    public String sendEth(String toAddress, BigDecimal amount) throws IOException {
        long nonce = getAccountNonce(web3Config.getContractOwnerAddress());
        BigInteger gasPrice = new BigInteger(web3Config.getGasPrice());
        BigInteger gasLimit = new BigInteger(web3Config.getGasLimit());
        RawTransaction rawTransaction = RawTransaction.createEtherTransaction(
                BigInteger.valueOf(nonce), gasPrice, gasLimit, toAddress, EthMathUtil.decimalToBigInteger(amount, 18));
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, web3Config.getChainId(), credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
        if (ethSendTransaction.hasError()) {
            log.info("transfer error: {}", ethSendTransaction.getError().getMessage());
            throw new ChainException(INVOKE_EXCEPTION, "transfer error: " + ethSendTransaction.getError().getMessage());
        } else {
            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("Transfer transactionHash:" + transactionHash);
            return transactionHash;
        }
    }

    @Override
    public String parseTransactionException(TransactionException te) {
        TransactionReceipt receipt = te.getTransactionReceipt().orElse(null);
        if (receipt == null) {
            return null;
        }
        String status = receipt.getStatus();
        String revertReason = receipt.getRevertReason();
        if (StringUtils.isBlank(revertReason)) {
            return "Transaction error: status is " + status + ", Unkown error";
        }
        String[] split = revertReason.split(":");
        String errorMsg = revertReason;
        if (split.length >= 2) {
            errorMsg = split[1].trim();
        }
        return "Transaction error: status is " + status + ", Fail with error '" + errorMsg + "'";
    }

    private String computeMethodId(String functionSignature) {
        byte[] hash = Hash.sha3(functionSignature.getBytes());
        return "0x" + Numeric.toHexString(hash).substring(2, 10);
    }

    @Override
    public String getMethodId(String abiJson, String methodName) throws ChainException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode abiArray = null;
        try {
            abiArray = mapper.readTree(abiJson);
        } catch (JsonProcessingException e) {
            throw new ChainException(QUERY_EXCEPTION, "Failed to parse ABI JSON");
        }

        for (JsonNode item : abiArray) {
            if ("function".equals(item.get("type").asText()) && methodName.equals(item.get("name").asText())) {
                String name = item.get("name").asText();
                StringBuilder signature = new StringBuilder(name).append("(");

                Iterator<JsonNode> inputs = item.get("inputs").elements();
                while (inputs.hasNext()) {
                    JsonNode input = inputs.next();
                    signature.append(input.get("type").asText());
                    if (inputs.hasNext()) {
                        signature.append(",");
                    }
                }
                signature.append(")");

                return computeMethodId(signature.toString());
            }
        }
        throw new ChainException(METHOD_NOT_FOUND, "Method not found");
    }

    @Override
    public void checkTransaction(String txHash, String contractAddress, String methodId) throws ChainException {
        Optional<TransactionReceipt> optional = null;
        try {
            optional = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt();
        } catch (IOException e) {
            throw new ChainException(QUERY_EXCEPTION, "Failed to query transaction receipt by txHash[" + txHash + "]: " + e.getMessage());
        }
        if (!optional.isPresent()) {
            throw new ChainException(TXHASH_NOT_FOUND, "Txhash[" + txHash + "] not found");
        }
        TransactionReceipt txReceipt = optional.get();
        String toAddress = txReceipt.getTo();
        if (!contractAddress.equalsIgnoreCase(toAddress)) {
            throw new ChainException(CONTRACT_ADDRESS_NOT_MATCH, "Contract address not match，expected: " + contractAddress + "，actual: " + toAddress);
        }

        if (!txReceipt.getStatus().equals("0x1")) {
            throw new ChainException(TX_OF_INVOKE_FAILED, "This is tx of invoke failed");
        }

        Transaction tx = null;
        try {
            tx = web3j.ethGetTransactionByHash(txHash).send().getTransaction().get();
        } catch (IOException e) {
            throw new ChainException(QUERY_EXCEPTION, "Failed to query transaction by txHash[" + txHash + "]: " + e.getMessage());
        }
        String txMethodId = tx.getInput().substring(0, 10);
        if (!txMethodId.equals(methodId)) {
            throw new ChainException(METHOD_NOT_MATCH, "Method not match，expected: " + methodId + "，actual: " + txMethodId);
        }
    }

    protected void init(EthereumService ethereumService) {
        this.web3j = ethereumService.getWeb3j();
        this.credentials = ethereumService.getCredentials();
        this.promoteCredentials = ethereumService.getPromoteCredentials();
        this.mgr500Account = ethereumService.getCredentialsMap();
        this.contractGasProvider = ethereumService.getContractGasProvider();
        this.web3Config = ethereumService.getWeb3Config();
    }
}
