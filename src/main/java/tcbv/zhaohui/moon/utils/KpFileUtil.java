package tcbv.zhaohui.moon.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * (KpFileUtil) 文件工具操作
 *
 * @author zhaohui
 * @since 2023/4/4 17:15
 */
public class KpFileUtil {
    private KpFileUtil() {}

    /**
     * 文件路径拼接
     * @param args 数据目录列表
     * @return 拼接的文件路径
     */
    public static String osPathJoin(String... args) {
        List<String> pathList = Arrays.asList(args);
        return pathList.stream().map(String::toString).collect(Collectors.joining(File.separator)).replace("//", "/");
    }

    /**
     * 判断文件是否存在
     * @param filePath 文件路径
     * @return true存在，false不存在
     */
    public static boolean isFileExist(String filePath) {
        return Files.exists(Paths.get(filePath));
    }

    /**
     * 创建目录
     * @param dirPath 目录名
     */
    public static void mkdir(String dirPath) throws IOException {
        boolean exist = isFileExist(dirPath);
        if(exist) {
            return;
        }

        File file = new File(dirPath);
        boolean ret = file.mkdirs();
        if (ret) {
            return;
        }

        String errMsg = String.format("创建目录%s失败", dirPath);
        throw new IOException(errMsg);
    }

    /**
     * 创建文件
     * @param filePath 文件名
     */
    public static void createFile(String filePath) throws IOException {
        if (isFileExist(filePath)) {
            return;
        }
        String dirPath = osPathJoin(filePath, "..");
        mkdir(dirPath);
        File file = new File(filePath);
        boolean ret = file.createNewFile();
        if (ret) {
            return;
        }
        String errMsg = String.format("创建文件%s失败", filePath);
        throw new IOException(errMsg);
    }

    /**
     * 文件转字符串
     * @param filePath 文件路径
     * @return 文件内容（字符串）
     * @throws IOException 文件io异常
     */
    public static String fileToString(String filePath) throws IOException {
        boolean exist = isFileExist(filePath);
        if (!exist) {
            String errMsg = String.format("文件路径%s不存在", filePath);
            throw new IOException(errMsg);
        }

        Path path = Paths.get(filePath);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * 字符串存入文件，如果文件不存在，则创建文件
     * @param str 字符串内容
     * @param filePath 文件路径
     * @throws IOException 文件IO异常
     */
    public static void stringToFile(String str, String filePath) throws IOException {
        createFile(filePath);
        Path path = Paths.get(filePath);
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        Files.write(path, bytes);
    }

    /**
     * 重命名文件
     * @param filePath 源文件所在路径
     * @param oldFileName 原文件名
     * @param newFileName 新文件名
     * @return 是否重命名成功
     */
    public static boolean renameFile(String filePath, String oldFileName, String newFileName) throws IOException {
        return renameFile(filePath, oldFileName, filePath, newFileName);
    }

    /**
     * 迁移文件
     * @param oldPath 源路径
     * @param oldFileName 原文件名
     * @param newPath 目标路径
     * @param newFileName 新文件名
     * @return 是否迁移成功
     */
    public static boolean renameFile(String oldPath, String oldFileName, String newPath, String newFileName) throws IOException {
        // 创建原文件对象
        File oldFile = new File(oldPath, oldFileName);
        // 创建新文件对象
        File newFile = new File(newPath, newFileName);
        //如果目标路径不存在则创建
        if (!isFileExist(newPath)) {
            mkdir(newPath);
        }
        // 重命名文件
        if (oldFile.exists() && oldFile.isFile()) {
            return oldFile.renameTo(newFile);
        }
        return false;
    }
}
