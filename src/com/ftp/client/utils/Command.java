package com.ftp.client.utils;

public class Command {

    //  如果是文件名列出文件信息,如果是目录则列出文件列表
    public static String LIST(String name) {
        return "LIST " + name;
    }


    // 在服务器上建立指定目录
    public static String MKD(String directory) {
        return "MKD " + directory;
    }

    // 列出指定目录内容
    public static String NLST(String directory) {
        return "NLST " + directory;
    }

    // 系统登录用户
    public static String USER(String username) {
        return "USER " + username;
    }

    // 系统登录密码
    public static String PASS(String password) {
        return "PASS " + password;
    }

    // 从 FTP服务器上退出登录
    public static String QUIT() {
        return "PASS";
    }

    // 由特定偏移量重启文件传递
    public static String REST(int offset) {
        return "REST " + offset;
    }

    // 从服务器上找回（复制）文件
    public static String RETR(int filename) {
        return "RETR " + filename;
    }

    // 在服务器上删除指定目录
    public static String RMD(String directory) {
        return "RMD " + directory;
    }


    // 储存（复制）文件到服务器上
    public static String STOR(String filename) {
        return "STOR " + filename;
    }

    // 储存文件到服务器名称上
    public static String STOU(String filename) {
        return "STOU " + filename;
    }


}