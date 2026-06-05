package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import network.NetworkUtil;
import network.QRGenerator;
import server.WebServer;
import javafx.scene.image.Image;
import util.AppPaths;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.nio.file.Files;

public class MainApp extends Application {

    private Label ipLabel;
    private Label countLabel;
    private ImageView qrView;

    @Override
    public void start(Stage stage) throws Exception {
        WebServer.startserver();
        ipLabel = new Label();
        countLabel = new Label();

        qrView = new ImageView();
        qrView.setFitWidth(250);
        qrView.setFitHeight(250);
        qrView.setPreserveRatio(true);

        stage.setOnCloseRequest(event -> {
            WebServer.stopserver();
            Platform.exit();
            System.exit(0);
        });
        Button refreshButton =
                new Button("Refresh Network");

        Button uploadsButton =
                new Button("Open Uploads Folder");

        refreshButton.setOnAction(e -> refresh());

        uploadsButton.setOnAction(e -> {

            try {

                AppPaths.ensureUploadDir();

                Desktop.getDesktop().open(
                        AppPaths.UPLOAD_DIR.toFile()
                );

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox root = new VBox(15);

        root.setPadding(
                new Insets(20)
        );

        root.getChildren().addAll(
                new Label("QShare"),
                qrView,
                ipLabel,
                countLabel,
                refreshButton,
                uploadsButton
        );
        refresh();
        Scene scene =
                new Scene(root, 450, 500);

        stage.setTitle("QShare");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/app.png")));
        stage.setScene(scene);

        stage.show();
    }

    private void refresh() {
        AppPaths.writeLog(
                AppPaths.REFRESH_LOG,
                "refresh entered\n"
        );

        try {

            String ip = "127.0.0.1";

            try {
                String detectedIp = NetworkUtil.getLocalIp();

                if (detectedIp != null && !detectedIp.isBlank()) {
                    ip = detectedIp;
                }
            } catch (Exception e) {
                AppPaths.writeException(
                        AppPaths.ERROR_LOG,
                        e
                );
            }

            System.out.println("IP = " + ip);

            String url =
                    "http://" + ip + ":8080";

            ipLabel.setText(
                    "Server URL: " + url
            );

            BufferedImage image =
                    QRGenerator.generateQR(url);

            qrView.setImage(
                    SwingFXUtils.toFXImage(
                            image,
                            null
                    )
            );

            AppPaths.ensureUploadDir();

            try (var stream = Files.list(AppPaths.UPLOAD_DIR)) {
                long count = stream.count();
                countLabel.setText(
                        "Uploaded Files: " + count
                );
            }

        } catch (Exception e) {
            AppPaths.writeException(
                    AppPaths.ERROR_LOG,
                    e
            );

            ipLabel.setText(
                    "Refresh Error: " + e.getClass().getSimpleName()
            );
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
