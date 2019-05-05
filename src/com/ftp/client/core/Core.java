package com.ftp.client.core;

import java.io.*;
import java.net.Socket;

import com.ftp.client.utils.Command;

public class Core {
    private String username = null;
    private String password = null;

    private BufferedReader reader = null;
    private PrintWriter writer = null;

    public Core(String url, String username, String password, int port) throws Exception {
        try {
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
        writer.println(Command.USER(this.username));

        String response = reader.readLine();
        System.out.println(response);
        if (!response.startsWith("331")) {
            throw new IOException("login failed:" + response);
        }


        writer.println(Command.PASS(this.password));

        response = reader.readLine();
        System.out.println(response);
        if (!response.startsWith("230")) {
            throw new IOException("login failed:" + response);
        }
    }

}
