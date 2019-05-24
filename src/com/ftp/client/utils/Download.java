package com.ftp.client.utils;

import com.ftp.client.core.CoreFactory;

import java.io.*;
import java.net.Socket;

/**
 * 下载文件
 */
public class Download extends ConnectionMode {

    private String username;
    private String password;

    public Download(String url, String username, String password) throws IOException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.core = CoreFactory.getCore(url, username, password);
    }


    private void downFile(String from_file_name, String to_path, Socket dataSocket, boolean append) throws IOException {
        BufferedOutputStream output = new BufferedOutputStream(
                new FileOutputStream(new File(to_path), append));
        BufferedInputStream input = new BufferedInputStream(
                dataSocket.getInputStream());
        byte[] buffer = new byte[4096];
        int bytesRead = 0;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
        output.flush();
        output.close();
        input.close();
    }


    /**
     * 主动模式 文件下载
     *
     * @param from_file_name FTP服务器中的文件名
     * @param to_path        需要下载到的本地路径
     * @throws IOException
     */
    public void downloadPort(String from_file_name, String to_path) throws IOException {
        int[] bits = this.getPort();
        int dataPort = bits[0] * 256 + bits[1];
        //System.out.println(dataPort);
        this.core.exec(Command.PORT(this.handleUrl() + bits[0] + "," + bits[1]), "200");
        this.core.exec(Command.RETR(from_file_name), "150");
        Socket dataSocket = this.core.listenPort(dataPort);
        this.downFile(from_file_name, to_path, dataSocket, false);
        dataSocket.close();
        this.core.exec(null, "226");
    }

    /**
     * 被动模式 文件下载
     *
     * @param from_file_name FTP服务器中的文件名
     * @param to_path        需要下载到的本地路径
     * @throws IOException
     */
    public void downloadPasv(String from_file_name, String to_path) throws IOException {
        int dataPort = this.getPasvPort();
        this.core.exec(Command.RETR(from_file_name), "150");
        Socket dataSocket = this.core.usePortConnectRemote(dataPort);
        this.downFile(from_file_name, to_path, dataSocket, false);
        dataSocket.close();
        this.core.exec(null, "226");
    }

    /**
     * 下载文件
     * 断点续传
     * 采用被动模式
     *
     * @param from_file_name FTP服务器中的文件名
     * @param to_path        需要下载到的本地路径
     * @param size           文件已下载的大小（file.length())
     * @throws IOException
     */
    public void downloadBrokenFile(String from_file_name, String to_path, long size) throws IOException {
        int dataPort = this.getPasvPort();
        this.core.exec(Command.REST(size), null);
        Socket dataSocket = this.core.usePortConnectRemote(dataPort);
        this.core.exec(Command.RETR(from_file_name), "150");

        this.downFile(from_file_name, to_path, dataSocket, true);
        dataSocket.close();
        this.core.exec(null, "226");
    }


    //测试代码
    /*private long getFileSize(File file){
        if(file.exists() && file.isFile()){
            return file.length();
        }
        return 0;
    }
    private void login() throws IOException {
        this.core.exec(Command.USER(this.username), "331");
        this.core.exec(Command.PASS(this.password), "230");
    }
    public static void main(String[] args) throws Exception{
        Download download = new Download("192.168.137.1","test","test");
        download.login();
        String file_path = "D:\\321.pdf";
        File f = new File(file_path);
        long size = download.getFileSize(f);
        System.out.println(size);
        //download.downloadBrokenFile("321.pdf","D:\\",size);
        download.downloadPasv("321.pdf","D:\\321.pdf.part");
    }*/
}
