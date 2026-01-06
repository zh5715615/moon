package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.entity.PledgeEntity;
import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.PledgeService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.utils.GsonUtil;
import tcbv.zhaohui.moon.utils.Rsp;

import javax.annotation.Resource;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tcbv.zhaohui.moon.vo.PromoteHistoryVo;
import tcbv.zhaohui.moon.vo.PromoteInfoVo;

import static tcbv.zhaohui.moon.beans.Constants.PROMOTE_TOTAL;

@RestController
@RequestMapping("/api/v1/moon/promote")
@Slf4j
@Api(tags = "推广相关接口")
public class PromoteController {
    @Resource
    private UserInfoService userInfoService;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private PledgeService pledgeService;

    @Autowired
    private Web3Config web3Config;

    @Syslog(module = "PROMOTE")
    @PutMapping("/link/{promoCode}")
    @ApiOperation("点击推广链接")
    @JwtAddressRequired
    public Rsp walletLogin(@PathVariable("promoCode") String promoCode) {
        String userId = JwtContext.getUserId();
        String errMsg = userInfoService.bindPromoter(userId, promoCode);
        if (null != errMsg) {
            return Rsp.error(errMsg);
        }
        return Rsp.ok();
    }

    // 辅助方法：安全地获取单元格字符串值
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // 防止数字被转成科学计数法，比如手机号、身份证号等
                    double value = cell.getNumericCellValue();
                    long longValue = (long) value;
                    if (longValue == value) {
                        return String.valueOf(longValue);
                    } else {
                        return String.valueOf(value);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    @PostMapping("/airdrop")
    @ApiOperation("空投")
    @JwtAddressRequired(role = "admin")
    public Rsp airdrop(@RequestParam("file") MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            return Rsp.error("文件为空");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            return Rsp.error("非法文件名");
        }

        // 判断是否为 Excel 文件
        String extension = "";
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < originalFilename.length() - 1) {
            extension = originalFilename.substring(lastDotIndex).toLowerCase();
        }

        if (!".xls".equals(extension) && !".xlsx".equals(extension)) {
            return Rsp.error("仅支持 .xls 或 .xlsx 格式的 Excel 文件");
        }

        String uniqueFilename = UUID.randomUUID() + extension;
        Path uploadPath = Paths.get("." + File.separator);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath);

        // 读取 Excel 第一列和第二列
        List<String[]> firstTwoColumns = new ArrayList<>();
        try (InputStream is = Files.newInputStream(filePath)) {
            Workbook workbook;
            if (".xls".equals(extension)) {
                workbook = new HSSFWorkbook(is); // 适用于 .xls
            } else {
                workbook = new XSSFWorkbook(is); // 适用于 .xlsx
            }

            Sheet sheet = workbook.getSheetAt(0); // 默认读第一个 sheet
            for (Row row : sheet) {
                String col1 = getCellValueAsString(row.getCell(0));
                String col2 = getCellValueAsString(row.getCell(1));
                firstTwoColumns.add(new String[]{col1, col2});
            }
            workbook.close();
        } catch (Exception e) {
            return Rsp.error("解析 Excel 文件失败: " + e.getMessage());
        }

        // 可选：打印或处理读取到的数据
         log.info("读取到的数据: {}", GsonUtil.toJson(firstTwoColumns));
        int cnt = 0;
        for (String[] firstTwoColumn : firstTwoColumns) {
            if (cnt > 0) {
                String txHash = spaceJediService.transfer(firstTwoColumn[0], new BigDecimal(firstTwoColumn[1]));
                log.info("txHash: {}", txHash);
            }
            cnt++;
        }

        // 如果你希望返回读取的内容，可以这样：
        // return Rsp.ok(firstTwoColumns);

        return Rsp.ok(); // 或者根据业务需求返回其他内容
    }

    @GetMapping
    @ApiOperation("推广奖励信息")
    public Rsp<PromoteInfoVo> promote() {
        PromoteInfoVo promoteInfoVo = new PromoteInfoVo();
        promoteInfoVo.setTotal(PROMOTE_TOTAL);
        BigDecimal balance = spaceJediService.balanceOf(web3Config.getPromoterAddress());
        promoteInfoVo.setFree(balance.doubleValue());
        return Rsp.okData(promoteInfoVo);
    }

    @GetMapping("/history")
    @ApiOperation("推广历史")
    public Rsp<List<PromoteHistoryVo>> promoteHistory() {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "create_time"));
        Page<PledgeEntity> pageEntity = pledgeService.queryPledgePromotionByPage(new PledgeEntity(), pageRequest);
        if (pageEntity == null || pageEntity.getContent().isEmpty()) {
            return Rsp.okData(Collections.emptyList());
        }
        List<PromoteHistoryVo> promoteHistoryVos = new ArrayList<>();
        for (PledgeEntity pledgeEntity : pageEntity.getContent()) {
            PromoteHistoryVo promoteHistoryVo = new PromoteHistoryVo();
            promoteHistoryVo.setAddress(pledgeEntity.getAddress());
            promoteHistoryVo.setPledgeAmount(pledgeEntity.getAmount().doubleValue());
            promoteHistoryVo.setPledgeTime(pledgeEntity.getCreateTime());
            promoteHistoryVo.setHash(pledgeEntity.getPledgeHash());
            promoteHistoryVos.add(promoteHistoryVo);
        }
        return Rsp.okData(promoteHistoryVos);
    }
}
