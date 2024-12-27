package transfer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

public class FileSenderUI {
    public static void main(String[] args) {
        final File[]fileToSend = new File[1];

        // Create the frame
        JFrame frame = new JFrame("Lalisa File Sender");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create the label
        JLabel titleLabel = new JLabel("Lalisa File Sender");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setBounds(100,10,200,30);

        // Create the file path label
        JLabel filePathLabel = new JLabel("No file selected.");
        filePathLabel.setPreferredSize(new Dimension(300, 20));
        filePathLabel.setBounds(100,50,200,30);

        // Create the Choose File button
        JButton chooseFileButton = new JButton("Choose File");
        chooseFileButton.setBounds(80,90,120,30);
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    fileToSend[0] = fileChooser.getSelectedFile();
                    filePathLabel.setText(fileToSend[0].getName());
                } else {
                    filePathLabel.setText("No file selected.");
                }
            }
        });

        // Create the Send button
        JButton sendButton = new JButton("Send");
        sendButton.setBounds(200,90,120,30);
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath = filePathLabel.getText();
                if (!filePath.equals("No file selected.")) {
                    JOptionPane.showMessageDialog(frame, "Sending file: " + filePath);
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsoluteFile());
                        Socket socket= new Socket("localhost",1111);;

                        DataOutputStream dataOutputStream=new DataOutputStream(socket.getOutputStream());
                        String fileName = fileToSend[0].getName();
                        byte [] fileNameByte= fileName.getBytes();
                        byte [] fileContentByte=new byte[(int)fileToSend[0].length()];

                        fileInputStream.read(fileContentByte);
                        dataOutputStream.writeInt(fileNameByte.length);
                        dataOutputStream.write(fileNameByte);

                        dataOutputStream.writeInt(fileContentByte.length);
                        dataOutputStream.write(fileContentByte);


                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Please choose a file to send.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        // Add components to the frame
        frame.add(titleLabel);
        frame.add(chooseFileButton);
        frame.add(filePathLabel);
        frame.add(sendButton);

        // Make the frame visible
        frame.setVisible(true);
    }
}


























