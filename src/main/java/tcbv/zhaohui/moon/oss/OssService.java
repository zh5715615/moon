package tcbv.zhaohui.moon.oss;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tcbv.zhaohui.moon.exceptions.OssException;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static tcbv.zhaohui.moon.exceptions.OssException.FILE_READ_ERROR;

/**
 * @author: zhaohui
 * @Title: OssService
 * @Description:
 * @date: 2025/12/2 21:04
 */
@Slf4j
@Service
public class OssService {

    @Resource
    private OssClient ossClient;

    public void downloadFileByName(BucketType bucketType, String fileName, OutputStream os) {
        ossClient.download(bucketType, fileName, os);
    }

    public SysOss upload(BucketType bucketType, String prefix, MultipartFile file, String contentType) {
        // 限制上传文件的类型
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString();
        }
        String suffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());

        UploadResult uploadResult = null;
        try {
            uploadResult = ossClient.upload(file.getBytes(), bucketType, prefix, fileName, contentType);
        } catch (IOException e) {
            throw new OssException(FILE_READ_ERROR, "文件读取失败: " + e.getMessage());
        }
        // 保存文件信息
        SysOss oss = new SysOss();
        oss.setUrl(uploadResult.getUrl());
        oss.setFileSuffix(suffix);
        oss.setFileName(uploadResult.getFilename());
        oss.setOriginalName(fileName);
        oss.setService("obs");
        if (AccessPolicyType.PRIVATE == ossClient.getAccessPolicy()) {
            oss.setUrl(ossClient.getPrivateUrl(bucketType, oss.getFileName(), 120));
        }
        return oss;
    }

    public String signUrl(BucketType bucketType, String ossPath){
        return ossClient.signUrl(bucketType, ossPath);
    }
}