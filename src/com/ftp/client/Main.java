package com.ftp.client;

import com.ftp.client.core.CoreFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;


import com.ftp.client.entity.File;
import com.ftp.client.utils.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

public class Main {


    private String url = "192.168.1.1";
    private String username = "root";
    private String password = "";

    private JFrame frame;
    private JTable table;
    private JTextField urlText;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JButton login;
    private JButton quit;
    private JButton upload;
    private JButton refresh;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        Main window = new Main();
        window.frame.setVisible(true);
    }

    /**
     * Create the application.
     */
    public Main() {

        // window
        this.frame = new JFrame();
        this.frame.setTitle("FTP Client");
        this.frame.setSize(600, 600);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(null);


        // url
        JLabel addressLabel = new JLabel("地址");
        addressLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        addressLabel.setBounds(10, 10, 50, 25);
        this.frame.getContentPane().add(addressLabel);

        this.urlText = new JTextField();
        this.urlText.setFont(new Font("宋体", Font.PLAIN, 14));
        this.urlText.setBounds(60, 10, 100, 25);
        this.urlText.setText(this.url);
        this.frame.getContentPane().add(this.urlText);

        // username
        JLabel usernameLabel = new JLabel("用户名");
        usernameLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        usernameLabel.setBounds(210, 10, 100, 25);
        this.frame.getContentPane().add(usernameLabel);

        this.usernameText = new JTextField();
        this.usernameText.setFont(new Font("宋体", Font.PLAIN, 14));
        this.usernameText.setBounds(260, 10, 100, 25);
        this.usernameText.setText(this.username);
        this.frame.getContentPane().add(this.usernameText);

        // password
        JLabel passwordLabel = new JLabel("密码");
        passwordLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        passwordLabel.setBounds(410, 10, 100, 25);
        this.frame.getContentPane().add(passwordLabel);

        this.passwordText = new JPasswordField();
        this.passwordText.setFont(new Font("宋体", Font.PLAIN, 14));
        this.passwordText.setBounds(460, 10, 100, 25);
        this.frame.getContentPane().add(this.passwordText);


        // 登录按钮
        this.login = new JButton("登录");
        this.login.setFont(new Font("宋体", Font.PLAIN, 14));
        this.login.setBackground(UIManager.getColor("Button.highlight"));
        this.login.setBounds(10, 50, 100, 25);
        this.frame.getContentPane().add(this.login);
        this.login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    url = urlText.getText().trim();
                    username = usernameText.getText();
                    password = passwordText.getText();
                    CoreFactory.getCore(url, username, password);
                    System.out.println("login successful");
                    setTable((new ListFile(url, username, password)).list());
                    login.setVisible(false);
                    quit.setVisible(true);
                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(null, "用户名或者密码错误\n username：" + username, "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 退出按钮
        this.quit = new JButton("退出");
        this.quit.setFont(new Font("宋体", Font.PLAIN, 14));
        this.quit.setBackground(UIManager.getColor("Button.highlight"));
        this.quit.setBounds(10, 50, 100, 25);
        this.quit.setVisible(false);
        this.frame.getContentPane().add(this.quit);
        this.quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                url = urlText.getText().trim();
                username = usernameText.getText();
                password = passwordText.getText();
                CoreFactory.quit();
                System.out.println("quit successful");
                quit.setVisible(false);
                login.setVisible(true);
                ((DefaultTableModel) table.getModel()).setColumnCount(0);

            }
        });


        // 上传按钮
        this.upload = new JButton("上传");
        this.upload.setFont(new Font("宋体", Font.PLAIN, 14));
        this.upload.setBackground(UIManager.getColor("Button.highlight"));
        this.upload.setBounds(120, 50, 100, 25);
        this.frame.getContentPane().add(this.upload);
        this.upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                // 上传点击按钮
                int result = 0;
                File file = null;
                String path = null;
                JFileChooser fileChooser = new JFileChooser();
                FileSystemView fsv = FileSystemView.getFileSystemView();
                System.out.println(fsv.getHomeDirectory());                //得到桌面路径
                fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                fileChooser.setDialogTitle("请选择要上传的文件...");
                fileChooser.setApproveButtonText("确定");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                result = fileChooser.showOpenDialog(null);
                if (JFileChooser.APPROVE_OPTION == result) {
                    path = fileChooser.getSelectedFile().getPath();
                    String name = fileChooser.getSelectedFile().getName();
                    long size = 0;
                    try {
                        size = getFileSize(name);
                        if (size != 0) {
                            JOptionPane.showMessageDialog(null, "已有文件存在，使用断点续传功能", "提示", JOptionPane.PLAIN_MESSAGE);
                        }
                        (new Upload(url, username, password)).uploadBrokenFile(path, name, size);

                        JOptionPane.showMessageDialog(null, "上传成功", "提示", JOptionPane.PLAIN_MESSAGE);
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "上传文件失败", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });


        //刷新按钮
        this.refresh = new JButton("刷新");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {

                    setTable((new ListFile(url, username, password)).list());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.refresh.setFont(new Font("宋体", Font.PLAIN, 14));
        this.refresh.setBackground(UIManager.getColor("Button.highlight"));
        this.refresh.setBounds(230, 50, 100, 25);
        this.frame.getContentPane().add(this.refresh);


        // 加滚动条
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 100, 580, 450);
        frame.getContentPane().add(scrollPane);


        this.table = new JTable();
        scrollPane.setViewportView(this.table);
        this.table.setColumnSelectionAllowed(true);
        this.table.setCellSelectionEnabled(true);
        this.table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        this.table.setBorder(new LineBorder(new Color(0, 0, 0)));
        this.table.setToolTipText("可以点击下载");
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {

                if (table.columnAtPoint(mouseEvent.getPoint()) == 2) {
                    int row = table.rowAtPoint(mouseEvent.getPoint());
                    String path = table.getValueAt(row, 0).toString();
                    if (table.getValueAt(row, 2).toString().equals("进入")) {
                        try {
                            (new Directory(url, username, password)).ChangeDirectory(path);
                            setTable((new ListFile(url, username, password)).list());
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(null, "进入目录错误", "提示", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        int result = 0;
                        String local_path = null;
                        JFileChooser fileChooser = new JFileChooser();
                        FileSystemView fsv = FileSystemView.getFileSystemView();
                        System.out.println(fsv.getHomeDirectory());                //得到桌面路径
                        fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        fileChooser.setDialogTitle("请选择要下载到的文件夹地址...");
                        fileChooser.setApproveButtonText("确定");
                        result = fileChooser.showOpenDialog(null);
                        if (JFileChooser.APPROVE_OPTION == result) {
                            local_path = fileChooser.getSelectedFile().getPath() + "\\" + path;
                            java.io.File file = new java.io.File(local_path + ".part");
                            java.io.File local_file = new java.io.File(local_path);
                            if (local_file.exists() && !local_file.canWrite()) {
                                JOptionPane.showMessageDialog(null, "文件没有写入权限", "提示", JOptionPane.ERROR_MESSAGE);
                            }
                            long size = 0;
                            if (file.exists()) {
                                size = file.length();
                                JOptionPane.showMessageDialog(null, "下载成功未完成，使用断点续传模式", "提示", JOptionPane.PLAIN_MESSAGE);
                            }
                            try {
                                (new Download(url, username, password)).downloadBrokenFile(path, local_path + ".part", size);
                                if (!file.renameTo(local_file)) {
                                    JOptionPane.showMessageDialog(null, "下载文件成功，创建文件失败", "提示", JOptionPane.ERROR_MESSAGE);
                                }
                                JOptionPane.showMessageDialog(null, "下载成功", "提示", JOptionPane.PLAIN_MESSAGE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

    }

    private long getFileSize(String file) throws IOException {
        try {
            String response = CoreFactory.getCore(url, username, password).exec(Command.SIZE(file), "213");
            String[] split = response.split(" ");
            return  Long.valueOf(split[1]);
        } catch (Exception e) {
            CoreFactory.getCore(url, username, password).exec(null, null);
            CoreFactory.getCore(url, username, password).exec(null, null);
            CoreFactory.getCore(url, username, password).exec(null, null);
            return 0;
        }
    }


    private void setTable(ArrayList<File> files) {
        // table数据初始化
        String[][] data1 = new String[files.size()][3];
        for (int row = 0; row < files.size(); row++) {

            data1[row][0] = files.get(row).getName();
            if (files.get(row).isDir()) {
                data1[row][1] = "文件夹";
                data1[row][2] = "进入";
            } else {
                data1[row][1] = "文件";
                data1[row][2] = "下载";
            }
        }


        //table列名-----------------------------------------------------
        String[] columnNames = {"文件", "文件类型", ""};
        DefaultTableModel model = new DefaultTableModel();
        model.setDataVector(data1, columnNames);

        this.table.setModel(model);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(200);

    }
}