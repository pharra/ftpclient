package com.ftp.client.utils;

import com.ftp.client.core.Core;
import com.ftp.client.core.CoreFactory;
import com.ftp.client.entity.File;

import java.io.IOException;
import java.util.ArrayList;

public class Directory extends ConnectionMode {

    private String username;
    private String password;

    public Directory(String url, String username, String password) throws IOException {
        this.core = CoreFactory.getCore(url, username, password);
        this.username = username;
        this.password = password;
    }

    /**
     * 更改目录
     *
     * @param pathName 目的目录名
     */
    public void ChangeDirectory(String pathName) throws IOException {
        this.core.exec(Command.CWD(pathName), "250");
    }

    /**
     * 新建目录
     *
     * @param pathName 新建目录名
     */
    public void MakeDirectory(String pathName) throws IOException {
        this.core.exec(Command.XMKD(pathName), "257");
    }

    /**
     * 删除空目录
     *
     * @param pathName 待删除空目录名
     */
    public void DeleteDirectory(String pathName) throws IOException {
        ArrayList<File> files = (new ListFile(url, username, password)).list(pathName);
        for (File file : files) {
            this.DeleteFile(pathName + "/" + file.getName());
        }
        this.core.exec(Command.RMD(pathName), "250");
    }

    public String GetWorkDirectory() throws IOException {
        return this.core.exec(Command.PWD(), "257").replace("\"", "\t").split("\t")[1];
    }

    public void DeleteFile(String file) throws IOException {
        this.core.exec(Command.DELE(file), "250");
    }

    //测试代码
    /*public static void main(String[] args) throws Exception {
        Directory directory = new Directory("127.0.0.1","1096177569@qq.com","zlg55555ovvo");
        String pathName1 = "C#";
        directory.ChangeDirectory(pathName1);
        System.out.print("跳转到"+ pathName1 +"目录");
        String pathName2 = "test";
        directory.MakeDirectory(pathName2);
        System.out.print("新建"+ pathName2 +"目录成功");
        directory.DeleteNullDirectory(pathName2);
        System.out.print("删除目录"+ pathName2 +"成功");
    }*/
}
