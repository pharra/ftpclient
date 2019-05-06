package com.ftp.client.utils;

import com.ftp.client.core.Core;

import java.io.IOException;

abstract class ConnectionMode {
    protected String url;
    protected Core core;

    /**
     * 处理URL，在FTP命令中使用
     * 输入 127.0.0.1
     * 返回 127,0,0,1,
     * @return
     */
    protected String handleUrl(){
        String[] str = this.url.split("\\.");
        String newUrl="";
        for(String tmp : str){
            newUrl = newUrl + tmp +",";
        }
        return newUrl;
    }

    /**
     * 主动模式统一操作
     * 将两个字节的端口号进行随机获取，所得的最终端口号为 前者*256+后者
     * 并保证最终端口号>1024
     * @return 返回两个字节的端口号
     * @throws IOException
     */
    protected int[] getPort() throws IOException {
        int frontBit=(int)(Math.random()*255%254);
        int backBit=(int)(Math.random()*255%254);
        int dataPort=frontBit*256+backBit;
        if(dataPort<=1024) {
            frontBit += 4;
        }
        int[] ports = new int[2];
        ports[0]=frontBit;
        ports[1]=backBit;
        return ports;
    }

    /**
     * 被动模式统一操作
     * 向FTP服务器请求端口号
     * 请求后的字节如：227 Entering Passive Mode (192,168,137,1,220,220).
     * 最后两位为字节号，最终端口为 前者*256+后者
     * @return 返回请求到的端口号
     * @throws IOException
     */
    protected int getPasvPort() throws IOException {
        String response = this.core.exec(Command.PASV(),"227");
        int opening = response.indexOf('(');
        int closing = response.indexOf(')', opening + 1);
        String dataLink = response.substring(opening + 1, closing);
        String[] pasvMode = dataLink.split(",");
        int len = pasvMode.length;
        int dataPort = Integer.parseInt(pasvMode[len-2])*256
                + Integer.parseInt(pasvMode[len-1]);
        return dataPort;
    }
}
