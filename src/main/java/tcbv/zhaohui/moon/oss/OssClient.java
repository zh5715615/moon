package tcbv.zhaohui.moon.oss;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.Date;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

/**
 * @author: zhaohui
 * @Title: OssClient
 * @Description:
 * @date: 2025/12/2 20:55
 */
@Service
@Slf4j
public class OssClient {
    public static final String IS_HTTPS = "Y";

    public static final String CLOUD_SERVICE = "aliyun";

    @Resource
    private OssConfig ossConfig;

    private AmazonS3 client;

    @PostConstruct
    public void init() {
        try {
            //财信自定义
            AwsClientBuilder.EndpointConfiguration endpointConfig =
                    new AwsClientBuilder.EndpointConfiguration(ossConfig.getEndpoint(), ossConfig.getRegion());

            AWSCredentials credentials = new BasicAWSCredentials(ossConfig.getAccessKey(), ossConfig.getSecretKey());
            AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
            ClientConfiguration clientConfig = new ClientConfiguration();
            if (IS_HTTPS.equalsIgnoreCase(ossConfig.getIsHttps())) {
                clientConfig.setProtocol(Protocol.HTTPS);
            } else {
                clientConfig.setProtocol(Protocol.HTTP);
            }
            AmazonS3ClientBuilder build = AmazonS3Client.builder()
                    .withEndpointConfiguration(endpointConfig)
                    .withClientConfiguration(clientConfig)
                    .withCredentials(credentialsProvider)
                    .disableChunkedEncoding();
            build.enablePathStyleAccess();
            this.client = build.build();

            createBucket();
        } catch (Exception e) {

            throw new RuntimeException("配置错误! 请检查系统配置:[" + e.getMessage() + "]");
        }
    }

    private void createBucket() {
        try {
            String bucketName = ossConfig.getBucketName();
            if (client.doesBucketExistV2(bucketName)) {
                return;
            }
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            AccessPolicyType accessPolicy = getAccessPolicy();
            createBucketRequest.setCannedAcl(accessPolicy.getAcl());
            client.createBucket(createBucketRequest);
            client.setBucketPolicy(bucketName, getPolicy(bucketName, accessPolicy.getPolicyType()));
        } catch (Exception e) {
            throw new RuntimeException("创建Bucket失败, 请核对配置信息:[" + e.getMessage() + "]");
        }
    }

    public AccessPolicyType getAccessPolicy() {
        return AccessPolicyType.getByType(ossConfig.getAccessPolicy());
    }


    public void download(String key, OutputStream os) {
        GetObjectRequest request = new GetObjectRequest(ossConfig.getBucketName(), key);

        try (S3Object object = client.getObject(request);
             S3ObjectInputStream inputStream = object.getObjectContent();) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
        } catch (Exception ex) {
            log.error("请求OSS下载异常：", ex);
            throw new RuntimeException("请求OSS下载异常");
        }
    }


    private String getPolicy(String bucketName, PolicyType policyType) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n\"Statement\": [\n{\n\"Action\": [\n");
        if (policyType == PolicyType.WRITE) {
            builder.append("\"s3:GetBucketLocation\",\n\"s3:ListBucketMultipartUploads\"\n");
        } else if (policyType == PolicyType.READ_WRITE) {
            builder.append("\"s3:GetBucketLocation\",\n\"s3:ListBucket\",\n\"s3:ListBucketMultipartUploads\"\n");
        } else {
            builder.append("\"s3:GetBucketLocation\"\n");
        }
        builder.append("],\n\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("\"\n},\n");
        if (policyType == PolicyType.READ) {
            builder.append("{\n\"Action\": [\n\"s3:ListBucket\"\n],\n\"Effect\": \"Deny\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
            builder.append(bucketName);
            builder.append("\"\n},\n");
        }
        builder.append("{\n\"Action\": ");
        switch (policyType) {
            case WRITE:
                builder.append("[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n");
                break;
            case READ_WRITE:
                builder.append("[\n\"s3:AbortMultipartUpload\",\n\"s3:DeleteObject\",\n\"s3:GetObject\",\n\"s3:ListMultipartUploadParts\",\n\"s3:PutObject\"\n],\n");
                break;
            default:
                builder.append("\"s3:GetObject\",\n");
                break;
        }
        builder.append("\"Effect\": \"Allow\",\n\"Principal\": \"*\",\n\"Resource\": \"arn:aws:s3:::");
        builder.append(bucketName);
        builder.append("/*\"\n}\n],\n\"Version\": \"2012-10-17\"\n}\n");
        return builder.toString();
    }

    public UploadResult upload(byte[] data, String path, String contentType) {
        return upload(new ByteArrayInputStream(data), path, contentType);
    }

    public UploadResult uploadSuffix(byte[] data, String prefix, String suffix, String contentType) {
        return upload(data, getPath(prefix, suffix), contentType);
    }

    public UploadResult upload(InputStream inputStream, String path, String contentType) {
        if (!(inputStream instanceof ByteArrayInputStream)) {
            inputStream = new ByteArrayInputStream(IoUtil.readBytes(inputStream));
        }
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(inputStream.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), path, inputStream, metadata);
            // 设置上传对象的 Acl 为公共读
            putObjectRequest.setCannedAcl(getAccessPolicy().getAcl());
            client.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("上传文件失败，请检查配置信息:[" + e.getMessage() + "]");
        }

        // 创建凭证提供者
        AwsBasicCredentials credentials = AwsBasicCredentials.create(ossConfig.getAccessKey(), ossConfig.getSecretKey());
        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        // 创建 S3 客户端 (用于 Presigner)
        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(ossConfig.getEndpoint())) // 重写端点以兼容非 AWS 的 S3 服务
                .region(Region.of(ossConfig.getRegion())) // 对于大多数 S3 兼容服务，使用 US_EAST_1 即可，除非特别指定
                .serviceConfiguration(
                        S3Configuration.builder()
                                .pathStyleAccessEnabled(true) // 强制使用路径风格
                                .build())
                .build();

        // 构建预签名请求
        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofSeconds(60))
                .getObjectRequest(req -> req.bucket(ossConfig.getBucketName()).key(path))
                .build();

        // 生成预签名 URL
        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(getObjectPresignRequest);

        // 获取可分享的 URL
        String presignedUrl = presignedRequest.url().toString();
        System.out.println("预签名 URL: " + presignedUrl);

        // 可选：打印 HTTP 方法和过期时间
        System.out.println("HTTP 方法: " + presignedRequest.httpRequest().method());
        System.out.println("过期时间: " + presignedRequest.expiration());
        return UploadResult.builder().url(getUrl() + "/" + path).filename(path).build();
    }


    private String getPath(String prefix, String suffix) {
        // 生成uuid
        String uuid = IdUtil.fastSimpleUUID();
        // 文件路径
        String yyyyMM = DateUtil.format(new Date(), "yyyyMM");
        String path = yyyyMM + "/" + uuid;
        if (StringUtils.isNotBlank(prefix)) {
            path = prefix + "/" + path;
        }
        return path + suffix;
    }

    private String getUrl() {
        String domain = ossConfig.getDomain();
        String endpoint = ossConfig.getEndpoint();
        String header = IS_HTTPS.equalsIgnoreCase(ossConfig.getIsHttps()) ? "https://" : "http://";
        // 云服务商直接返回
        if (StringUtils.containsAny(endpoint,CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return header + domain;
            }
            return header + ossConfig.getBucketName() + "." + endpoint;
        }
        // minio 单独处理
        if (StringUtils.isNotBlank(domain)) {
            return header + domain + "/" + ossConfig.getBucketName();
        }
        return header + endpoint + "/" + ossConfig.getBucketName();
    }

    public String getPrivateUrl(String objectKey, Integer second) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(ossConfig.getBucketName(), objectKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(new Date(System.currentTimeMillis() + 1000L * second));
        URL url = client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }
}
