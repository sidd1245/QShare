package server;

import io.javalin.Javalin;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

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
                    ctx.uploadedFiles("file");

            if (file.isEmpty()) {
                ctx.status(400);
                ctx.result("No file selected");
                return;
            }

            Files.createDirectories(
                    Path.of("uploads")
            );
            List<String> uploadedNames = new ArrayList<>();
            for (var File : file) {
                Path destination =
                        Path.of(
                                "uploads",
                                File.filename()
                        );

                Files.copy(
                        File.content(),
                        destination
                );

                System.out.println(
                        "Saved: " + File.filename()
                );
            }

            StringBuilder html = new StringBuilder();

            html.append("""
                    <html>
                    <head>
                    <title>Upload Successful</title>
                    </head>
                    <body>
                    <h1>Upload Successful</h1>
                    <ul>
                    """);

            for (String name : uploadedNames) {
                html.append("<li>")
                        .append(name)
                        .append("</li>");
            }

            html.append("""
                    </ul>
                    
                    <a href="/">Upload More Files</a>
                    <br><br>
                    <a href="/files">View Uploaded Files</a>
                    
                    </body>
                    </html>
                    """);

            ctx.contentType("text/html");
            ctx.result(html.toString());
        });
        app.get("/files", ctx -> {

            Path uploadDir = Path.of("uploads");

            Files.createDirectories(uploadDir);

            StringBuilder html = new StringBuilder();

            html.append("""
                    <html>
                    <head>
                    <title>Uploaded Files</title>
                    </head>
                    <body>
                    <h1>Uploaded Files</h1>
                    <ul>
                    """);

            try (var stream = Files.list(uploadDir)) {

                stream.forEach(file -> {

                    String fileName =
                            file.getFileName().toString();
                    String encodedName =
                            URLEncoder.encode(
                                    fileName,
                                    StandardCharsets.UTF_8
                            );

                    html.append("""
                                    <li>
                                    <a href="/download/
                                    """)
                            .append(encodedName)
                            .append("\">")
                            .append(fileName)
                            .append("</a> </li> ");
                });
            }

            html.append("""
                    </ul>
                    
                    <a href="/">Back</a>
                    
                    </body>
                    </html>
                    """);

            ctx.contentType("text/html");
            ctx.result(html.toString());
        });
        app.get("/download/{filename}", ctx -> {

            String filename =
                    ctx.pathParam("filename");

            Path file =
                    Path.of(
                            "uploads",
                            filename
                    );

            if (!Files.exists(file)) {
                ctx.status(404);
                ctx.result("File not found");
                return;
            }

            ctx.header(
                    "Content-Disposition",
                    "attachment; filename=\"" + filename + "\""
            );

            ctx.result(
                    Files.newInputStream(file)
            );
        });
        app.start(8080);
    }
}