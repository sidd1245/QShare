package server;

import com.sun.net.httpserver.HttpServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class WebServer {
    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {

            Path htmlPath = Path.of("web/index.html");
            String response = Files.readString(htmlPath);

            exchange.sendResponseHeaders(200, response.getBytes().length);

            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        });

        server.createContext("/upload", exchange -> {

            System.out.println(exchange.getRequestMethod());

            String response = "POST received";

            exchange.sendResponseHeaders(
                    200,
                    response.getBytes().length
            );
            InputStream is = exchange.getRequestBody();
            byte[] data = is.readAllBytes();
            String body = new String(data);
            int filenameStart =
                    body.indexOf("filename=\"") + 10;

            int filenameEnd =
                    body.indexOf("\"", filenameStart);

            String filename =
                    body.substring(
                            filenameStart,
                            filenameEnd
                    );

            System.out.println(filename);
            int fileStart =
                    body.indexOf("\r\n\r\n") + 4;
            String boundary =
                    body.substring(
                            0,
                            body.indexOf("\r\n")
                    );

            int fileEnd =
                    body.lastIndexOf(boundary) - 2;
            byte[] fileBytes =
                    Arrays.copyOfRange(
                            data,
                            fileStart,
                            fileEnd
                    );
            Path output =
                    Path.of("uploads", filename);

            Files.write(output, fileBytes);
            exchange.close();
        });
        server.start();

        System.out.println("Server started");
    }
}