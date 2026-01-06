package tcbv.zhaohui.moon.service;

import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: zhaohui
 * @Title: PasswordManager
 * @Description:
 * @date: 2026/1/6 20:34
 */
@Component
public class PasswordManager {

    private final Path passwordFile = Paths.get(System.getProperty("user.home"), ".star-wars", "password"); // 可改为绝对路径
    private final AtomicReference<String> currentPassword = new AtomicReference<>("");

    @PostConstruct
    public void init() throws IOException {
        // 初始加载
        loadPassword();
        // 启动监听线程
        watchFile();
    }

    private void loadPassword() {
        try {
            String pwd = Files.readString(passwordFile).trim();
            currentPassword.set(pwd);
            System.out.println("✅ 密码已加载: " + maskPassword(pwd));
        } catch (IOException e) {
            System.err.println("❌ 无法读取密码文件: " + passwordFile);
            e.printStackTrace();
        }
    }

    private void watchFile() {
        Thread watcher = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                passwordFile.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
                System.out.println("👀 开始监听密码文件: " + passwordFile);

                while (!Thread.currentThread().isInterrupted()) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changed = (Path) event.context();
                        if (passwordFile.getFileName().equals(changed)) {
                            System.out.println("🔄 检测到密码文件更新，重新加载...");
                            loadPassword();
                        }
                    }
                    key.reset();
                }
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
                System.err.println("⚠️ 密码监听线程异常退出");
            }
        }, "PasswordFileWatcher");
        watcher.setDaemon(true); // 随 JVM 退出
        watcher.start();
    }

    public String getPassword() {
        return currentPassword.get();
    }

    private String maskPassword(String pwd) {
        if (pwd == null || pwd.length() <= 4) return "****";
        return pwd.substring(0, 2) + "**" + pwd.substring(pwd.length() - 2);
    }
}
