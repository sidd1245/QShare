package util;

import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AppPaths {

    public static final Path APP_DIR =
            Path.of(
                    System.getProperty("user.home"),
                    "QShare"
            );

    public static final Path UPLOAD_DIR =
            APP_DIR.resolve("uploads");

    public static final Path REFRESH_LOG =
            APP_DIR.resolve("refresh.log");

    public static final Path ERROR_LOG =
            APP_DIR.resolve("error.log");

    public static final Path CRASH_LOG =
            APP_DIR.resolve("crash.log");

    public static void ensureAppDir() throws java.io.IOException {
        Files.createDirectories(APP_DIR);
    }

    public static void ensureUploadDir() throws java.io.IOException {
        ensureAppDir();
        Files.createDirectories(UPLOAD_DIR);
    }

    public static void writeLog(Path path, String message) {
        try {
            ensureAppDir();
            Files.writeString(
                    path,
                    message,
                    StandardCharsets.UTF_8
            );
        } catch (Exception ignored) {
        }
    }

    public static void writeException(Path path, Exception exception) {
        try {
            ensureAppDir();
            try (PrintWriter writer =
                         new PrintWriter(path.toFile())) {
                exception.printStackTrace(writer);
            }
        } catch (Exception ignored) {
        }
    }

    private AppPaths() {}
}
