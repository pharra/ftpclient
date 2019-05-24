package com.ftp.client.utils;

import com.ftp.client.core.CoreFactory;
import com.ftp.client.core.Core;
import com.ftp.client.entity.File;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ListFile extends ConnectionMode {

    public ListFile(String url, String username, String password) throws IOException {
        this.core = CoreFactory.getCore(url, username, password);
    }


    private boolean isDir(String name) throws IOException {
        try {
            this.core.exec(Command.CWD(name), "250");
        } catch (IOException e) {
            this.core.exec(null, null);
            this.core.exec(null, null);
            this.core.exec(null, null);
            return false;
        }
        this.core.exec(Command.CWD(".."), "250");
        return true;
    }

    public ArrayList<File> list() throws IOException {
        return this.list(".");
    }

    public ArrayList<File> list(String path) throws IOException {
        int dataPort = this.getPasvPort();
        Socket dataSocket = this.core.usePortConnectRemote(dataPort);
        this.core.exec(Command.NLST(path), null);
        BufferedReader input = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        char[] buffer = new char[4096];
        int bytesRead = 0;
        StringBuffer tmp = new StringBuffer();
        while ((bytesRead = input.read(buffer)) != -1) {
            tmp.append(buffer, 0, bytesRead);
        }
        dataSocket.close();
        this.core.exec(null, "226");
        ArrayList<File> files = new ArrayList<File>();
        for (String info : tmp.toString().replace("\r", "").split("\n")) {
            if (info.equals("")) {
                continue;
            }
            File file = new File();
            file.setName(info);
            file.setIsDir(this.isDir(file.getName()));
            files.add(file);
        }

        return files;
    }
//
//    //测试代码
//    public static void main(String[] args) throws Exception {
//        ListFile listFile = new ListFile("192.168.1.1", "root", "wf");
//        listFile.list();
//    }
}
