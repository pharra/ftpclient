package com.ftp.client.utils;

import com.ftp.client.core.CoreFactory;
import com.ftp.client.core.Core;

import java.io.IOException;

public class ListFile {
    private Core core;

    public ListFile(String url, String username, String password) throws Exception {
        this.core = CoreFactory.getCore(url, username, password);
    }

    public void dir(String pathName) throws IOException {
        String response = this.core.exec(Command.DIR(), "200");
    }
}
