package com.ftp.client.utils;

import com.ftp.client.core.Core;
import com.ftp.client.core.CoreFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 上传文件
 */
public class Upload extends ConnectionMode {
    private String username;
    private String password;

    public Upload(String url, String username, String password) throws IOException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.core = CoreFactory.getCore(url, username, password);
    }


    /**
     * 主动模式 上传文件
     *
     * @param file_path 上传文件的文件路径
     * @param to_path   FTP中的文件相对路径
     * @throws IOException
     */
    public void uploadPort(String file_path, String to_path) throws IOException {
        File f = new File(file_path);
        if (!f.exists()) {
            throw new IOException("File not Exists...");
        }
        FileInputStream is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        int[] bits = this.getPort();
        int dataPort = bits[0] * 256 + bits[1];
        //System.out.println(dataPort);
        this.core.exec(Command.PORT(this.handleUrl() + bits[0] + "," + bits[1]), "200");
        this.core.exec(Command.STOR(to_path), "150");
        Socket dataSocket = this.core.listenPort(dataPort);
        BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        input.close();
        output.close();
        dataSocket.close();
        this.core.exec(null, "226");
    }

    /**
     * 被动模式 上传文件
     *
     * @param file_path 上传文件的文件路径
     * @param to_path   FTP中的文件相对路径
     * @throws IOException
     */
    public void uploadPasv(String file_path, String to_path) throws IOException {
        File f = new File(file_path);
        if (!f.exists()) {
            throw new IOException("File not Exists...");
        }
        FileInputStream is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        int dataPort = this.getPasvPort();
        this.core.exec(Command.STOR(to_path), "150");
        Socket dataSocket = this.core.usePortConnectRemote(dataPort);
        BufferedOutputStream output = new BufferedOutputStream(
                dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        input.close();
        output.close();
        dataSocket.close();
        this.core.exec(null, "226");
    }

    /**
     * 上传文件
     * 断点续传
     * 采用被动模式
     *
     * @param file_path 上传文件的文件路径
     * @param file_name 在FTP服务器上已存在的文件名
     * @param size      在FTP服务器上已存在的文件长度（使用file.length()）
     * @throws IOException
     */
    public void uploadBrokenFile(String file_path, String file_name, long size) throws IOException {
        File f = new File(file_path);
        if (!f.exists()) {
            throw new IOException("File not Exists...");
        }
        FileInputStream is = new FileInputStream(f);
        BufferedInputStream input = new BufferedInputStream(is);
        input.skip(size);
        int dataPort = this.getPasvPort();
        Socket dataSocket = this.core.usePortConnectRemote(dataPort);
        this.core.exec(Command.APPE(file_name + ".part"), "150");
        BufferedOutputStream output = new BufferedOutputStream(
                dataSocket.getOutputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        input.close();
        output.close();
        dataSocket.close();
        this.core.exec(null, "226");
        rename(file_name);
    }

    /**
     * 重命名
     */
    private void rename(String file_name) throws IOException {
        String response = this.core.exec(Command.SIZE(file_name), null);
        System.out.println(response);
        if (!response.startsWith("550")) {
            this.core.exec(Command.DELE(file_name), "250");
        }
        this.core.exec(Command.RNFR(file_name + ".part"), "350");
        this.core.exec(Command.RNTO(file_name), "250");
        /*int time = 0;
        String[] tmp = file_name.split("\\.");
        String name = tmp[0];
        while(!response.startsWith("550")){
            time++;
            response = this.core.exec(Command.SIZE(name+" - ("+time+")"+"\\."+tmp[1]),null);
        }
        if(time!=0){
            this.core.exec(Command.RNFR(file_name+".part"),null);
            this.core.exec(Command.RNTO(name+" - ("+time+")"+"\\."+tmp[1]),null);

        }else{
            this.core.exec(Command.RNFR(file_name+".part"),null);
            this.core.exec(Command.RNTO(file_name),null);
        }*/
    }

    //测试代码
    /*private void login() throws IOException {
        this.core.exec(Command.USER(this.username), "331");
        this.core.exec(Command.PASS(this.password), "230");
    }
    public static void main(String[] args) throws Exception{
        Upload upload = new Upload("192.168.137.1","test","test");
        upload.login();
        String file_path = "D:\\FTPServer\\2018.docx";
        File f = new File(file_path);
        long size = f.length();;
        System.out.println(size);
        upload.uploadPasv("D:\\2018.docx","hh\\2018.docx");
    }*/
}
