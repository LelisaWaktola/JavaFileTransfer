package Transfer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
// File receiver server
public class FileReceiverServer {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        ArrayList<MyFile> receivedFiles = new ArrayList<>();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started and waiting for files...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     DataInputStream dis = new DataInputStream(clientSocket.getInputStream())) {

                    String fileName = dis.readUTF();
                    int fileLength = dis.readInt();
                    byte[] fileBytes = new byte[fileLength];
                    dis.readFully(fileBytes);

                    MyFile receivedFile = new MyFile(receivedFiles.size(), fileName, fileBytes, getFileExtension(fileName));
                    receivedFiles.add(receivedFile);

                    System.out.println("Received file: " + fileName);
                    saveFile(fileName, fileBytes);
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void saveFile(String fileName, byte[] fileBytes) throws IOException {
        // Define the directory to save the files
        File dir = new File("ReceivedFiles");

        // Create the directory if it doesn't exist
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Create a file in the directory with the received file's name
        File file = new File(dir, "received_" + fileName);

        // Write the received bytes to the file
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
            System.out.println("File saved as: " + file.getAbsolutePath());
        }
    }


    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex + 1) : "unknown";
    }
}

