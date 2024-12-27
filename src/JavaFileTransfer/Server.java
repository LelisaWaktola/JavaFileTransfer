package transfer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    static  ArrayList<MyFile> myFiles= new ArrayList<>();

    public static void main(String[] args) throws IOException {

        int fileId = 0;

        JFrame frame= new JFrame("Lalisa server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
        frame.setLayout(new BoxLayout(frame.getContentPane(),BoxLayout.Y_AXIS));

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));

        JScrollPane jScrollPane= new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(jScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel titleLabel= new JLabel("File reciver");
        titleLabel.setFont(new Font("Arial", Font.BOLD,20));
        titleLabel.setBorder(new EmptyBorder(20,0,10,0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        frame.add(titleLabel);
        frame.add(jScrollPane);
        frame.setVisible(true);

        ServerSocket serverSocket= new ServerSocket(1111);

        while(true){

            try{

                Socket socket= serverSocket.accept();
                DataInputStream dataInputStream=new DataInputStream(socket.getInputStream());
                int fileNameLength=dataInputStream.readInt();

                if(fileNameLength>0){
                    byte [] fileNameByte= new byte[fileNameLength];
                    dataInputStream.readFully(fileNameByte,0, fileNameLength);
                    String fileName=new String(fileNameByte);

                    int fileContentLength=dataInputStream.readInt();

                    if(fileContentLength>0){
                        byte [] fileContentByte=new byte[fileContentLength];
                        dataInputStream.readFully(fileContentByte,0,fileContentLength);

                        JPanel jpFileRow= new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow,BoxLayout.Y_AXIS));

                        JLabel jlFileName=new JLabel(fileName);
                        jlFileName.setFont(new Font("Arial", Font.BOLD,20));
                        jlFileName.setBorder(new EmptyBorder(10,0,10,0));

                        if(getFileExtension(fileName).equalsIgnoreCase("txt")){
                                 jpFileRow.setName(String.valueOf(fileId));
                                 jpFileRow.addMouseListener(getMyMouseListener());

                                 jpFileRow.add(jlFileName);
                                 jPanel.add(jpFileRow);
                                 frame.validate();
                        }else {
                            jpFileRow.setName(String.valueOf(fileId));
                            jpFileRow.addMouseListener(getMyMouseListener());
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);

                            frame.validate();
                        }

                    }

                }

            }catch(Exception e){
                e.printStackTrace();
            }


        }



    }
    public static JFrame createFrame(String fileName, byte [] fileData, String fileExtension){

        JFrame jFrame= new JFrame("lelisa file downloder");
        jFrame.setSize(400,400);
        JPanel jPanel=new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel,BoxLayout.Y_AXIS));

        JLabel jlTitle=new JLabel("Lelisa file downloder");
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlTitle.setFont(new Font("Arial",Font.BOLD,25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));

        JLabel jlPrompt=new JLabel("Are you sure you want to download file : "+fileName);
        jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        jlPrompt.setFont(new Font("Arial",Font.BOLD,20));
        jlPrompt.setBorder(new EmptyBorder(20,0,10,0));

        JButton jbNo=new JButton("No");
        jbNo.setPreferredSize(new Dimension(150,75));
        jbNo.setFont(new Font("Arial",Font.BOLD,20));

        JButton jbYes=new JButton("Yes");
        jbYes.setPreferredSize(new Dimension(150,75));

        JLabel jlFileContent =new JLabel();
        jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButtons= new JPanel();
        jpButtons.setBorder(new EmptyBorder(20,0,10,0));
        jpButtons.add(jbYes);
        jpButtons.add(jbYes);

        if(fileExtension.equalsIgnoreCase("txt")){

            jlFileContent.setText("<html>"+new String(fileData)+"</html>");
        }else{
            jlFileContent.setIcon(new ImageIcon(fileData));
        }
        jbYes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File fileToDownload=new File(fileName);

                try{

                    FileOutputStream fileOutputStream=new FileOutputStream(fileToDownload);
                    fileOutputStream.write(fileData);
                    fileOutputStream.close();
                    jFrame.dispose();


                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        });
        jPanel.add(jlTitle);
        jPanel.add(jlPrompt);
        jPanel.add(jlFileContent);
        jPanel.add(jpButtons);

        jFrame.add(jPanel);

        return jFrame;
    }

    public static MouseListener getMyMouseListener(){
        return  new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel jPanel=(JPanel) e.getSource();
                int fileId=Integer.parseInt(jPanel.getName());
                for(MyFile myfile: myFiles){
                        if(myfile.getId()==fileId){
                            JFrame jfPreview=createFrame(myfile.getName(), myfile.getData(), myfile.getFileExtension());
                            jfPreview.setVisible(true);
                        }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }
    public static String getFileExtension(String fileName){

        int i =fileName.lastIndexOf('.');
        if(i>0){
            return fileName.substring(i+1);
        }else{
            return "No extension Found.";
        }

    }
}


























































































