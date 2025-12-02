package tcbv.zhaohui.moon.oss;

import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhaohui
 * @Title: StringMultipartFile
 * @Description:
 * @date: 2025/12/2 21:12
 */
public class StringMultipartFile implements MultipartFile {

    private final byte[] content;
    private final String fileName;
    private final String contentType;

    public StringMultipartFile(String content, String fileName, String contentType) {
        this.content = content.getBytes(StandardCharsets.UTF_8);
        this.fileName = fileName;
        this.contentType = contentType;
    }

    @Override
    public String getName() {
        return "file"; // 表单字段名，可自定义
    }

    @Override
    public String getOriginalFilename() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream fos = new FileOutputStream(dest)) {
            fos.write(content);
        }
    }
}