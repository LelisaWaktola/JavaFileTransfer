package transfer;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FileTransferServer {

    private static final int PORT = 12345; // Port number for the server
    private JTextArea logArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileTransferServer::new);
    }

    public FileTransferServer() {
        JFrame frame = new JFrame("File Transfer Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        logArea = new JTextArea();
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);

        frame.add(scrollPane, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Server");
        startButton.addActionListener(e -> startServer());
        frame.add(startButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void startServer() {
        appendLog("File Transfer Server started...");

        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    appendLog("Waiting for a connection...");
                    Socket clientSocket = serverSocket.accept();
                    appendLog("Client connected: " + clientSocket.getInetAddress());

                    // Start a new thread for each client connection
                    new Thread(new ClientHandler(clientSocket)).start();
                }
            } catch (IOException e) {
                appendLog("Error starting the server: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    private void appendLog(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    private class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try (InputStream inputStream = clientSocket.getInputStream();
                 DataInputStream dataInputStream = new DataInputStream(inputStream)) {

                // Read the file name from the client
                String fileName = dataInputStream.readUTF();
                appendLog("Receiving file: " + fileName);

                // Create a file output stream to save the received file
                File outputFile = new File("received_" + fileName);
                try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read file data from the client and write to the output file
                    while ((bytesRead = dataInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }

                    appendLog("File received and saved as: " + outputFile.getAbsolutePath());
                }

            } catch (IOException e) {
                System.err.println("Error handling client connection: " + e.getMessage());
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Client disconnected.");
                } catch (IOException e) {
                    System.err.println("Error closing client socket: " + e.getMessage());
                }
            }
        }
    }
}

