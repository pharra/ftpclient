package com.ftp.client.utils;

import com.ftp.client.core.CoreFactory;
import com.ftp.client.core.Core;
import com.ftp.client.entity.File;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ListFile extends ConnectionMode {

    public ListFile(String url, String username, String password) throws Exception {
        this.core = CoreFactory.getCore(url, username, password);
    }

    public ArrayList<File> list() throws IOException {
        int dataPort = this.getPasvPort();
        Socket dataSocket = this.core.usePortConnectRemote(dataPort);
        this.core.exec(Command.LIST(), "150");
        BufferedReader input = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
        char[] buffer = new char[4096];
        int bytesRead = 0;
        StringBuffer tmp = new StringBuffer();
        while ((bytesRead = input.read(buffer)) != -1) {
            tmp.append(buffer, 0, bytesRead);
        }

        ArrayList<File> files = new ArrayList<File>();
        for (String info : tmp.toString().replace("\r", "").split("\n")) {
            File file = new File();
            String[] infoList = info.split(" ");
            file.setName(infoList[infoList.length - 1]);
            file.setIsDir(infoList[0].startsWith("d"));
            files.add(file);
        }
        dataSocket.close();
        return files;
    }
//
//    //测试代码
//    public static void main(String[] args) throws Exception {
//        ListFile listFile = new ListFile("192.168.1.1", "root", "wf");
//        listFile.list();
//    }
}
