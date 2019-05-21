package com.ftp.client.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.ftp.client.utils.Command;

public class Core {
    private String username = null;
    private String password = null;
    private String url = null;

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public Core(String url, String username, String password, int port) throws IOException {

        this.url = url;
        Socket socket = new Socket(url, port);//建立与服务器的socket连接

        this.username = username;
        this.password = password;

        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

        String response = reader.readLine();
        if (!response.startsWith("220")) {
            throw new IOException("unknow response after connect!");
        }

        this.login();


    }

    public Core(String url, String username, String password) throws IOException {
        this(url, username, password, 21);
    }

    private void login() throws IOException {

        this.exec(Command.USER(this.username), "331");

        this.exec(Command.PASS(this.password), "230");
    }

    public void quit() {
        try {
            this.exec(Command.QUIT(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String exec(String command, String shall) throws IOException {
        writer.println(command);

        String response = reader.readLine();
        if (shall == null) {
            return response;
        }

        if (!response.startsWith(shall)) {
            throw new IOException("exec failed:" + response);
        }
        return response;
    }


    // 主动模式
    public Socket listenPort(int port) throws IOException {
        ServerSocket dataSocketServ = new ServerSocket(port);
        Socket dataSocket = dataSocketServ.accept();
        return dataSocket;
    }

    // 被动模式
    public Socket usePortConnectRemote(int remotePort) throws IOException {
        Socket local = new Socket(this.url, remotePort);
        return local;
    }

}
