package com.ftp.client.core;

import java.io.IOException;

public class CoreFactory {
    private static Core core = null;

    public static Core getCore(String url, String username, String password, int port) throws IOException {
        if (core == null) {
            core = new Core(url, username, password, port);
        }
        return core;
    }

    public static Core getCore(String url, String username, String password) throws IOException {
        if (core == null) {
            core = new Core(url, username, password);
        }
        return core;
    }

    public static void quit() {
        if (core == null) {
            return;
        }
        core.quit();
        core = null;
    }
}
