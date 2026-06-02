package network;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileTransferProtocol {
    public static void sendFile(Socket socket, String file) {
        Path filePath = Path.of(file);
        try (FileInputStream fin = new FileInputStream(filePath.toFile());) {
            System.out.println("Path: " + filePath);
            System.out.println("Absolute: " + filePath.toAbsolutePath());
            System.out.println("Exists: " + Files.exists(filePath));
            System.out.println("Regular: " + Files.isRegularFile(filePath));
            System.out.println("Size: " + Files.size(filePath));
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.writeUTF(filePath.getFileName().toString());
            dos.writeLong(Files.size(filePath));
            int bytesRead;
            byte[] buffer = new byte[65536];
            while ((bytesRead = fin.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }
            dos.flush();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String response = dis.readUTF();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void receiveFile(Socket socket) throws IOException {
        DataInputStream dis = new DataInputStream(socket.getInputStream());
        String fname = dis.readUTF();
        Path outputDir = Path.of("received");
        Files.createDirectories(outputDir);
//        System.out.println(Thread.currentThread().getName() + " receiving " + fname);
        try (FileOutputStream fos = new FileOutputStream(outputDir.resolve(fname).toFile());) {
            long size = dis.readLong();
            System.out.println("Receiving: " + fname);
            System.out.println("Expected size: " + size);
            long read = 0;
            int byteread;
            long lastReport = 0;
            byte[] buffer = new byte[65536];
            long start = System.currentTimeMillis();
            while (read < size) {
                byteread = dis.read(buffer);
                if (byteread == -1)
                    break;
                fos.write(buffer, 0, byteread);
                read += byteread;
//                if (read - lastReport >= 1024 * 1024) { // every 1 MB
//                    System.out.println(Thread.currentThread().getName() + " received " + read + "/" + size);
//                    lastReport = read;
//                }
            }

            long time = System.currentTimeMillis() - start;
            double mbps = (size / 1024.0 / 1024.0) / (time / 1000.0);
            System.out.println("Speed: " + mbps + " MB/s");

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            if (read == size) {
                dos.writeUTF("SUCCESS");
            } else {
                dos.writeUTF("FAILED");
            }
            System.out.println("Expected: " + size + " Received: " + read);
        } catch (EOFException e) {
            System.out.println("Client disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}