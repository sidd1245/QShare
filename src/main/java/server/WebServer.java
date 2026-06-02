package server;

import io.javalin.Javalin;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class WebServer {

    public static void main(String[] args) {

        Javalin app = Javalin.create();

        app.get("/", ctx -> {

            InputStream is =
                    WebServer.class
                            .getClassLoader()
                            .getResourceAsStream(
                                    "web/index.html"
                            );

            if (is == null) {
                ctx.status(404);
                ctx.result("index.html not found");
                return;
            }

            ctx.contentType("text/html");

            ctx.result(
                    new String(
                            is.readAllBytes()
                    )
            );
        });
        app.post("/upload", ctx -> {

            var file =
                    ctx.uploadedFile("file");

            if (file == null) {
                ctx.status(400);
                ctx.result("No file selected");
                return;
            }

            Files.createDirectories(
                    Path.of("uploads")
            );

            Path destination =
                    Path.of(
                            "uploads",
                            file.filename()
                    );

            Files.copy(
                    file.content(),
                    destination
            );

            System.out.println(
                    "Saved: " + destination
            );

            ctx.result(
                    "Upload successful"
            );
        });
        app.start(8080);
    }
}