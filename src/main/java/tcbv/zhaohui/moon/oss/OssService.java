package tcbv.zhaohui.moon.oss;

import cn.hutool.core.collection.CollectionUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: zhaohui
 * @Title: OssService
 * @Description:
 * @date: 2025/12/2 21:04
 */
@Slf4j
@Service
public class OssService {

    private static final String CONTENT_TYPE_DEFAULT = "application/octet-stream";

    @Resource
    private OssClient ossClient;


    public void downloadFileByName(String fileName, OutputStream os) {
        ossClient.download(fileName, os);
    }


    public SysOss upload(String prefix, MultipartFile file) {
        // 限制上传文件的类型
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString();
        }
        String suffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());
//        List<OutsourceParams> limits = outsourceParamsService.lambdaQuery().eq(OutsourceParams::getBelongGroup, OutsourceParamConstant.LIMIT_UPLOAD_FORMAT).list();
//        if (CollectionUtils.isEmpty(limits)) {
//            if (!StringUtils.inStringIgnoreCase(suffix, ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx")) {
//                throw new RuntimeException("文件类型错误，请上传图片或文档");
//            }
//        } else {
//            if (limits.stream().noneMatch(item -> item.getValue().equalsIgnoreCase(suffix))) {
//                throw new RuntimeException(String.format("文件类型[%s]错误，请上传图片或文档。", suffix));
//            }
//        }
//        if (!PDFUtils.analysisPDF(file)) {
//            throw new BusinessException("文件上传失败，当前文件可能存在恶意js代码");
//        }

        UploadResult uploadResult;
        try {

//     TODO       uploadResult = ossClient.uploadSuffix(file.getBytes(), suffix, file.getContentType());
            uploadResult = ossClient.uploadSuffix(file.getBytes(), prefix, suffix, CONTENT_TYPE_DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(fileName);
        oss.setService("obs");
        if (AccessPolicyType.PRIVATE == ossClient.getAccessPolicy()) {
            oss.setUrl(ossClient.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;

        // TODO
//        sysOssService.save(oss);
//        return query(oss.getId());
    }

    public SysOss query(String id) {
        if (id == null) {
            return null;
        }
        // TODO
        SysOss oss = new SysOss();
        if (oss == null) {
            return null;
        }
        if (AccessPolicyType.PRIVATE == ossClient.getAccessPolicy()) {
            oss.setUrl(ossClient.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }

    public List<SysOss> queryByBusinessId(String businessId, String ossFileType) {
        if (StringUtils.isEmpty(businessId)) {
            return new ArrayList<>();
        }

        // TODO
        List<SysOss> ossList = new ArrayList<>();
        if (ossList == null || ossList.isEmpty()) {
            return new ArrayList<>();
        }

        ossList.forEach(item -> {
            if (AccessPolicyType.PRIVATE == ossClient.getAccessPolicy()) {
                item.setUrl(ossClient.getPrivateUrl(item.getFileName(), 120));
            }
        });
        return ossList;
    }

    public void saveOssIdList(String businessId, List<Long> ossIdList, String ossFileType) {
        if (StringUtils.isNotEmpty(businessId) && CollectionUtil.isNotEmpty(ossIdList)) {
            // TODO
//            ossIdList.forEach(item -> sysOssService.lambdaUpdate().eq(SysOss::getId, item)
//                    .set(SysOss::getStatus, StatusYesOrNo.YES.getCode())
//                    .set(SysOss::getFileType, ossFileType)
//                    .set(SysOss::getBusinessId, businessId).update());
        }
    }


    public void modifyOssUserId(String old, String userId) {
        // TODO
//        sysOssService.lambdaUpdate().eq(SysOss::getBusinessId, old)
////                .eq(SysOss::getStatus, StatusYesOrNo.YES.getCode())
//                .set(SysOss::getBusinessId, userId)
////                .set(SysOss::getStatus, StatusYesOrNo.YES.getCode())
//                .update();
    }

    @Transactional
    public void delete(Long id) {
        // TODO
//        sysOssService.lambdaUpdate().eq(SysOss::getId, id).eq(SysOss::getStatus, StatusYesOrNo.YES.getCode())
//                .set(SysOss::getStatus, StatusYesOrNo.NO.getCode()).update();
    }

    public void deleteOriginOssIds(String businessId) {
        // TODO
//        sysOssService.lambdaUpdate().eq(SysOss::getBusinessId, businessId).eq(SysOss::getStatus, StatusYesOrNo.YES.getCode())
//                .set(SysOss::getStatus, StatusYesOrNo.NO.getCode()).update();
    }
}