package com.ftp.client.utils;

import com.ftp.client.core.Core;
import com.ftp.client.core.CoreFactory;

import java.io.IOException;

public class Directory extends ConnectionMode{

    private Core core;

    public Directory(String url,String username,String password) throws Exception {
        this.core = CoreFactory.getCore(url, username, password);
    }

    /**
     * 更改目录
     * @param pathName 目的目录名
     * */
    public void ChangeDirectory(String pathName) throws IOException {
        this.core.exec(Command.CWD(pathName),"250");
    }

    /**
     * 新建目录
     * @param pathName 新建目录名
     * */
    public void MakeDirectory(String pathName) throws IOException{
        this.core.exec(Command.XMKD(pathName),"257");
    }

    /**
     * 删除空目录
     * @param pathName 待删除空目录名
     * */
    public void DeleteNullDirectory(String pathName) throws IOException {
        this.core.exec(Command.XRMD(pathName),"250");
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
