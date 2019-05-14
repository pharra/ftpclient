package com.ftp.client;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Toolkit;
import javax.swing.filechooser.FileSystemView;


import java.io.File;
import java.util.Arrays;

public class Main {


    //初始化参数--------------------------------
    static String url = "127.0.0.1";
    static String username = "liyz";
    static String password = "000000";
    //初始化参数--------------------------------


    private JFrame frame;
    private JTable table;
    private JTextField urlText;
    private JTextField usernameText;
    private JPasswordField passwordText;
    private JButton login;
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
        this.frame.getContentPane().add(this.urlText);

        // username
        JLabel usernameLabel = new JLabel("用户名");
        usernameLabel.setFont(new Font("宋体", Font.PLAIN, 14));
        usernameLabel.setBounds(210, 10, 100, 25);
        this.frame.getContentPane().add(usernameLabel);

        this.usernameText = new JTextField();
        this.usernameText.setFont(new Font("宋体", Font.PLAIN, 14));
        this.usernameText.setBounds(260, 10, 100, 25);
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


        //登录按钮------------------------------------------------
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
                    username = usernameText.getText().trim();
                    password = Arrays.toString(passwordText.getPassword()).trim();


                } catch (Exception e1) {
                    e1.printStackTrace();
                    JOptionPane.showConfirmDialog(null, "用户名或者密码错误\n username：" + username, "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        //上传按钮--------------------------------------------------
        this.upload = new JButton("上传");
        this.upload.setFont(new Font("宋体", Font.PLAIN, 14));
        this.upload.setBackground(UIManager.getColor("Button.highlight"));
        this.upload.setBounds(120, 50, 100, 25);
        this.frame.getContentPane().add(this.upload);
        this.upload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                //上传点击按钮触发------------------------------------
                System.out.println("上传！！！！！");
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
                    System.out.println("path: " + path);
                    try {
                        System.out.println("文件上传成功");
                    } catch (Exception e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }
                //上传点击按钮触发------------------------------------
            }
        });

        //上传按钮--------------------------------------------------


        //刷新按钮--------------------------------------------------
        this.refresh = new JButton("刷新");
        refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {

                    setTableInfo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.refresh.setFont(new Font("宋体", Font.PLAIN, 14));
        this.refresh.setBackground(UIManager.getColor("Button.highlight"));
        this.refresh.setBounds(230, 50, 100, 25);
        this.frame.getContentPane().add(this.refresh);
        //刷新按钮--------------------------------------------------

    }


    //显示基本信息-----------------------------------------------
    private void setTableInfo() {
//        //table数据初始化  从FTP读取所有文件
//        String[][] data1 = new String[file.length][4];
//        for (int row = 0; row < file.length; row++) {
//
//            data1[row][0] = file[row].getName();
//            if (file[row].isDirectory()) {
//                data1[row][1] = "文件夹";
//            } else if (file[row].isFile()) {
//                String[] geshi = file[row].getName().split("\\.");
//                data1[row][1] = geshi[1];
//            }
//            data1[row][2] = file[row].getSize() + "";
//            data1[row][3] = "下载";
//        }


//        //table列名-----------------------------------------------------
//        String[] columnNames = {"文件", "文件类型", "文件大小(B)", ""};
//        DefaultTableModel model = new DefaultTableModel();
//        model.setDataVector(data1, columnNames);
//
//        //加滚动条--------------------------------------------------------
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setBounds(32, 73, 400, 384);
//        frame.getContentPane().add(scrollPane);
//        //加滚动条-----------------------------------------------------
//
//        //table功能------------------------------------------------------
//        table = new JTable(model);
//        scrollPane.setViewportView(table);
//        table.setColumnSelectionAllowed(true);
//        table.setCellSelectionEnabled(true);
//        table.setFont(new Font("微软雅黑", Font.PLAIN, 12));
//        table.setBorder(new LineBorder(new Color(0, 0, 0)));
//        table.getColumnModel().getColumn(0).setPreferredWidth(200);
//        table.setToolTipText("可以点击下载");
//
//        //table button初始化(最后一列的按键)--------------------
//        ButtonColumn buttonsColumn = new ButtonColumn(table, 3);
    }
}