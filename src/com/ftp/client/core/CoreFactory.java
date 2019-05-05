package com.ftp.client.core;

import com.ftp.client.core.Core;

public class CoreFactory {
    private static Core core = null;

    public static Core getCore(String url, String username, String password, int port) throws Exception {
        if (core == null) {
            core = new Core(url, username, password, port);
        }
        return core;
    }

    public static Core getCore(String url, String username, String password) throws Exception {
        if (core == null) {
            core = new Core(url, username, password);
        }
        return core;
    }
}
