package tcbv.zhaohui.moon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.*;
import tcbv.zhaohui.moon.dto.KeyInfoDto;
import tcbv.zhaohui.moon.entity.WalletEntity;
import tcbv.zhaohui.moon.oss.BucketType;
import tcbv.zhaohui.moon.oss.OssService;
import tcbv.zhaohui.moon.oss.StringMultipartFile;
import tcbv.zhaohui.moon.oss.SysOss;
import tcbv.zhaohui.moon.service.WalletService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.utils.*;
import tcbv.zhaohui.moon.vo.KeyInfoCipherVo;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.*;
import java.security.SecureRandom;
import java.security.Security;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static tcbv.zhaohui.moon.beans.Constants.OSS_WALLET_PREFIX;
import static tcbv.zhaohui.moon.oss.OssClient.CONTENT_TYPE_JSON;

/**
 * @author: zhaohui
 * @Title: AccountMgrController
 * @Description:
 * @date: 2025/12/2 16:20
 */
@RestController
@RequestMapping("/api/v1/account")
@Validated
@ResponseBody
@Api(tags="钱包地址管理")
@Slf4j
public class AccountMgrController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OssService ossService;

    @Autowired
    private WalletService walletService;

    @Value("${star-wars.secret.aes-key}")
    private String aesKey;

    private ExecutorService executorService;

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    @PostConstruct
    public void init() {
        int threadPoolSize = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(threadPoolSize);
    }

    private String randomPassword() {
        byte[] bytes = new byte[32];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    private String generateWallet() throws Exception {
        // 1. 生成助记词 (128-bit entropy => 12 words)
        byte[] initialEntropy = new byte[16];
        new SecureRandom().nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        log.info("助记词 (Mnemonic): {}", mnemonic);

        // 2. 从助记词生成种子（无 passphrase）
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");

        // 3. 派生 BIP44 路径: m/44'/60'/0'/0/0
        Bip32ECKeyPair master = Bip32ECKeyPair.generateKeyPair(seed);
        int[] bip44 = {
                44 | Bip32ECKeyPair.HARDENED_BIT,
                60 | Bip32ECKeyPair.HARDENED_BIT,
                Bip32ECKeyPair.HARDENED_BIT,
                0,
                0
        };
        Bip32ECKeyPair derived = Bip32ECKeyPair.deriveKeyPair(master, bip44);

        // 4. 获取地址
        Credentials credentials = Credentials.create(derived.getPrivateKey().toString(16));
        String address = credentials.getAddress();
        log.info("钱包地址: {}", address);

        // 5. 创建 Keystore (UTC / JSON 文件格式)
        String keystorePassword = randomPassword(); // 用户设置的 keystore 密码
        WalletFile walletFile = Wallet.createStandard(keystorePassword, derived);

        // 6. 将 WalletFile 转为 JSON 字符串
        String keystoreJson = objectMapper.writeValueAsString(walletFile);
        log.info("Keystore JSON: {}", keystoreJson);

        log.debug("可用 MetaMask → 导入账户 → 选择该文件，并输入密码: {}", keystorePassword);

        byte[] bytes = keystoreJson.getBytes();
        MultipartFile mockFile = new StringMultipartFile(
                new String(bytes),
                address.toLowerCase() + ".json",
                "application/json"
        );
        SysOss sysOss = ossService.upload(BucketType.PRIVATE_BUCKET, OSS_WALLET_PREFIX, mockFile, CONTENT_TYPE_JSON);

        WalletEntity walletEntity = new WalletEntity();
        walletEntity.setAddress(address);
        String encryptPwd = AesUtil.encrypt(aesKey, keystorePassword);
        walletEntity.setEncryptPwd(encryptPwd);
        walletEntity.setKeystorePath(sysOss.getFileName());
        log.info(GsonUtil.toJson(walletEntity));
        walletService.insert(walletEntity);
        return address;
    }

    @GetMapping("/generate")
    @ApiOperation("生成钱包地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "int", name = "number", value = "生成钱包数量", required = true)
    })
    public Rsp<List<String>> generate(@RequestParam("number") int number) throws Exception {
        List<Future<String>> futures = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            Future<String> future = executorService.submit(() -> {
                try {
                    return generateWallet();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            futures.add(future);
        }

        List<String> addresses = new ArrayList<>();
        for (Future<String> future : futures) {
            addresses.add(future.get());
        }
        return Rsp.okData(addresses);
    }

    @PostMapping("/generteKeystoreByMnemonicAndPassword")
    @ApiOperation("通过助记词和密码生成keystore")
    public Rsp<KeyInfoCipherVo> generateKeystoreByMnemonicAndPassword(@RequestBody @Valid KeyInfoDto dto) throws Exception {
        // 1. 助记词转种子
        byte[] seed = MnemonicUtils.generateSeed(dto.getMnemonic(), "");

        // 2. 从种子派生主密钥，再按 BIP44 路径派生第一个账户（标准以太坊路径）
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        int[] bip44 = {
                44 | Bip32ECKeyPair.HARDENED_BIT,
                60 | Bip32ECKeyPair.HARDENED_BIT,
                Bip32ECKeyPair.HARDENED_BIT,
                0,
                0
        };
        Bip32ECKeyPair derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeypair, bip44);

        // 3. 创建 Credentials
        Credentials credentials = Credentials.create(derivedKeyPair);
        String address = credentials.getAddress();
        log.info("钱包地址: {}", address);

        // 4. 设置 keystore 密码（用于加密私钥）
        String keystorePassword = dto.getPassword(); // 用户设置的密码，非助记词 passphrase

        // 5. 保存到文件
        WalletFile walletFile = Wallet.createStandard(keystorePassword, derivedKeyPair);

        // 6. 将 WalletFile 转为 JSON 字符串
        String keystoreJson = objectMapper.writeValueAsString(walletFile);
        log.info("Keystore JSON: {}", keystoreJson);

        byte[] bytes = keystoreJson.getBytes();
        MultipartFile mockFile = new StringMultipartFile(
                new String(bytes),
                address.toLowerCase() + ".json",
                "application/json"
        );
        ossService.upload(BucketType.PRIVATE_BUCKET, OSS_WALLET_PREFIX, mockFile, CONTENT_TYPE_JSON);

        String encryptPwd = AesUtil.encrypt(aesKey, keystorePassword);
        KeyInfoCipherVo keyInfoCipherVo = new KeyInfoCipherVo();
        keyInfoCipherVo.setAddress(address);
        keyInfoCipherVo.setEncryptPwd(encryptPwd);
        keyInfoCipherVo.setKeystoreContent(Base64.getEncoder().encodeToString(bytes));
        return Rsp.okData(keyInfoCipherVo);
    }

    @PutMapping("/exportKeystores")
    @ApiOperation("导出所有（500）keystore文件")
    public Rsp exportKeystores() throws Exception {
        List<WalletEntity> walletEntityList = walletService.queryAll();
        String walletStorePath = "wallets";
        KpFileUtil.mkdir(walletStorePath);
        for (WalletEntity walletEntity : walletEntityList) {
            FileOutputStream baos = new FileOutputStream(walletStorePath + File.separator + walletEntity.getAddress() + ".json");
            ossService.downloadFileByName(BucketType.PRIVATE_BUCKET, walletEntity.getKeystorePath(), baos);
            String password = AesUtil.decrypt(aesKey, walletEntity.getEncryptPwd());
            KpFileUtil.stringToFile(password, walletStorePath + File.separator + walletEntity.getAddress() + ".pwd");
        }
        return Rsp.ok();
    }
}
