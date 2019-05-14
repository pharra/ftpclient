package com.ftp.client.entity;

public class File {
    private boolean isDir;
    private String name;

    public boolean isDir() {
        return isDir;
    }

    public void setIsDir(boolean isDir) {
        this.isDir = isDir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
