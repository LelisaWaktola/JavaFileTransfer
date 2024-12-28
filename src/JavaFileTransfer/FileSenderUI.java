package Transfer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

// File sender UI
public class FileSenderUI {
    public static void main(String[] args) {
        final File[] fileToSend = new File[1];

        JFrame frame = new JFrame("File Sender");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("File Sender");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(100, 10, 200, 30);

        JLabel filePathLabel = new JLabel("No file selected.");
        filePathLabel.setBounds(100, 50, 200, 30);

        JButton chooseFileButton = new JButton("Choose File");
        chooseFileButton.setBounds(80, 90, 120, 30);
        chooseFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileToSend[0] = fileChooser.getSelectedFile();
                filePathLabel.setText(fileToSend[0].getName());
            } else {
                filePathLabel.setText("No file selected.");
            }
        });

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(200, 90, 120, 30);
        sendButton.addActionListener(e -> {
            if (fileToSend[0] != null) {
                try (Socket socket = new Socket("localhost", 12345);
                     FileInputStream fis = new FileInputStream(fileToSend[0]);
                     DataOutputStream dos = new DataOutputStream(socket.getOutputStream())) {

                    dos.writeUTF(fileToSend[0].getName());
                    byte[] fileBytes = new byte[(int) fileToSend[0].length()];
                    fis.read(fileBytes);
                    dos.writeInt(fileBytes.length);
                    dos.write(fileBytes);

                    JOptionPane.showMessageDialog(frame, "File sent successfully!");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Error sending file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please choose a file first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(titleLabel);
        frame.add(chooseFileButton);
        frame.add(filePathLabel);
        frame.add(sendButton);
        frame.setVisible(true);
    }
}
