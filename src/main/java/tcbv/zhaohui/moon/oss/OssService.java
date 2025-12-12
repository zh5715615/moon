package tcbv.zhaohui.moon.oss;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
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


    public SysOss upload(String bucketName, String prefix, MultipartFile file) {
        // 限制上传文件的类型
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            fileName = UUID.randomUUID().toString();
        }
        String suffix = StringUtils.substring(fileName, fileName.lastIndexOf("."), fileName.length());

        UploadResult uploadResult = null;
        try {
            if (bucketName.equals("star-wars")) {
                uploadResult = ossClient.uploadWalletSuffix(file.getBytes(), prefix, suffix, CONTENT_TYPE_DEFAULT);
            } else if (bucketName.equals("card-nft")) {
                uploadResult = ossClient.uploadNftSuffix(file.getBytes(), prefix, suffix, CONTENT_TYPE_DEFAULT);
            }

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
    }

    public SysOss query(String id) {
        if (id == null) {
            return null;
        }

        SysOss oss = new SysOss();
        if (AccessPolicyType.PRIVATE == ossClient.getAccessPolicy()) {
            oss.setUrl(ossClient.getPrivateUrl(oss.getFileName(), 120));
        }
        return oss;
    }
}