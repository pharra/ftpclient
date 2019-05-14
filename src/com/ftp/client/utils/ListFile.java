package com.ftp.client.utils;

import com.ftp.client.core.CoreFactory;
import com.ftp.client.core.Core;
import com.ftp.client.entity.File;

import java.io.IOException;
import java.util.ArrayList;

public class ListFile {
    private Core core;

    public ListFile(String url, String username, String password) throws Exception {
        this.core = CoreFactory.getCore(url, username, password);
    }

    public ArrayList<File> dir(String pathName) throws IOException {
        ArrayList<File> files = new ArrayList<File>();
        String[] response = this.core.exec(Command.DIR(), "200", "226");
        if (!response[1].startsWith("125")) {
            throw new IOException("exec failed:" + response[1]);
        }
        for (int i = 2; i < response.length - 2; i++) {
            File file = new File();
            file.setIsDir(response[i].split(" ")[2].contains("<DIR>"));
            file.setName(response[i].split(" ")[3]);
            files.add(file);
        }
        if (!response[response.length - 2].startsWith("226")) {
            throw new IOException("exec failed:" + response[response.length - 2]);
        }
        return files;
    }
}
