package com.ftp.client.utils;

public class Command {

    //  如果是文件名列出文件信息,如果是目录则列出文件列表
    public static String LIST(String name) {
        return "LIST " + name;
    }

    //更改当前目录
    public static String CWD(String directory) {
        return "CWD " + directory;
    }

    // 在服务器上建立指定目录
    public static String XMKD(String directory) {
        return "XMKD " + directory;
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
        return "QUIT";
    }

    // 由特定偏移量重启文件传递
    public static String REST(long offset) {
        return "REST " + offset;
    }

    // 从服务器上找回（复制）文件
    public static String RETR(String filename) {
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

    // 	唯一地保存文件
    public static String STOU(String filename) {
        return "STOU " + filename;
    }

    // 打开数据流端口
    public static String PORT(String port) {
        return "PORT " + port;
    }

    public static String PASV() {
        return "PASV";
    }

    //无动作
    public static String NOOP() {
        return "NOOP";
    }

    //通知服务器，从数据通道发送给你的数据要附加到这个文件末尾
    public static String APPE(String filename) {
        return "APPE " + filename;
    }

    // 获取目录下文件列表
    public static String LIST() {
        return "LIST";
    }

    public static String NLST() {
        return "NLST .";
    }

    public static String SIZE(String file) {
        return "SIZE " + file;
    }

    public static String PWD() {
        return "PWD";
    }

    //对旧路径重命名
    public static String RNFR(String oldpath){
        return "RNFR "+oldpath;
    }
    //对新路径重命名
    public static String RNTO(String newpath){
        return "RNTO "+newpath;
    }
    //删除指定文件
    public static String DELE(String filename){
        return "DELE "+filename;
    }

}
