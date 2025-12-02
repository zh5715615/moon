package tcbv.zhaohui.moon.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.crypto.*;
import org.web3j.utils.Numeric;
import tcbv.zhaohui.moon.oss.OssService;
import tcbv.zhaohui.moon.oss.StringMultipartFile;
import tcbv.zhaohui.moon.utils.Rsp;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;

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
public class AccountMgrController {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OssService ossService;

    @GetMapping("/generate")
    @ApiOperation("生成钱包地址")
    public Rsp generate() throws CipherException, IOException {
        // 1. 生成助记词 (128-bit entropy => 12 words)
        byte[] initialEntropy = new byte[16];
        new SecureRandom().nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);

        System.out.println("助记词 (Mnemonic):");
        System.out.println(mnemonic);
        System.out.println();

        // 2. 从助记词生成种子（无 passphrase）
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, "");

        // 3. 派生 BIP44 路径: m/44'/60'/0'/0/0
        Bip32ECKeyPair master = Bip32ECKeyPair.generateKeyPair(seed);
        int a[] = {
                44 | Bip32ECKeyPair.HARDENED_BIT,
                60 | Bip32ECKeyPair.HARDENED_BIT,
                0  | Bip32ECKeyPair.HARDENED_BIT,
                0,
                0
        };
        Bip32ECKeyPair derived = Bip32ECKeyPair.deriveKeyPair(master, a);

        // 4. 获取地址
        Credentials credentials = Credentials.create(derived.getPrivateKey().toString(16));
        String address = credentials.getAddress();
        System.out.println("钱包地址:");
        System.out.println(address);
        System.out.println();

        // 5. 创建 Keystore (UTC / JSON 文件格式)
        String keystorePassword = "YourSecurePassword123!"; // 用户设置的 keystore 密码
        WalletFile walletFile = Wallet.createStandard(keystorePassword, derived);

        // 6. 将 WalletFile 转为 JSON 字符串
        String keystoreJson = objectMapper.writeValueAsString(walletFile);
        System.out.println("Keystore JSON:");
        System.out.println(keystoreJson);
        System.out.println();

        // 7. 保存到文件（MetaMask 兼容格式：UTC--timestamp--address）
        String fileName = "UTC--" + System.currentTimeMillis() + "--" + address.toLowerCase();
        Files.write(Paths.get(fileName), keystoreJson.getBytes());
        System.out.println("Keystore 文件已保存为: " + fileName);
        System.out.println("可用 MetaMask → 导入账户 → 选择该文件，并输入密码: " + keystorePassword);

        byte[] bytes = keystoreJson.getBytes();
        MultipartFile mockFile = new StringMultipartFile(
                new String(bytes),
                fileName,
                "application/json"
        );
        ossService.upload("wallet", mockFile);
        return Rsp.okData("生成成功");
    }
}
