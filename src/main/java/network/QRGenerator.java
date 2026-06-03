package network;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

public class QRGenerator {
    public static BufferedImage generateQR(String text)
            throws Exception {

        QRCodeWriter writer = new QRCodeWriter();

        BitMatrix matrix =
                writer.encode(
                        text,
                        BarcodeFormat.QR_CODE,
                        300,
                        300
                );

        return MatrixToImageWriter.toBufferedImage(matrix);
    }
    public static void main(String[] args) throws Exception {

        QRCodeWriter writer =
                new QRCodeWriter();
        String url =
                "http://" +
                        NetworkUtil.getLocalIp() +
                        ":8080";
        BitMatrix matrix =
                writer.encode(
                        url,
                        BarcodeFormat.QR_CODE,
                        300,
                        300
                );

        MatrixToImageWriter.writeToPath(
                matrix,
                "PNG",
                Path.of("qr.png")
        );

        System.out.println("QR created");
    }
}
