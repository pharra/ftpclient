package com.ftp.client.core;

import java.io.*;
import java.net.Socket;

import com.ftp.client.utils.Command;

public class Core {
    private String username = null;
    private String password = null;
    private String url = null;

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public Core(String url, String username, String password, int port) throws Exception {
        try {
            this.url = url;
            Socket socket = new Socket(url, port);//建立与服务器的socket连接

            this.username = username;
            this.password = password;

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String response = reader.readLine();
            if (!response.startsWith("220")) {
                throw new Exception("unknow response after connect!");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Core(String url, String username, String password) throws Exception {
        this(url, username, password, 21);
    }

    private void login() throws IOException {

        this.exec(Command.USER(this.username), "331");

        this.exec(Command.PASS(this.password), "230");
    }

    public BufferedReader exec(String command, String shall) throws IOException {
        writer.println(command);

        String response = reader.readLine();
        System.out.println(response);
        if (!response.startsWith(shall)) {
            throw new IOException("login failed:" + response);
        }
        return reader;
    }

}
