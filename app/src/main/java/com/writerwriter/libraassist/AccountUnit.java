package com.writerwriter.libraassist;

/**
 * Created by Larry on 2017/12/10.
 */

public class AccountUnit {
    private String account;
    private String password;
    private String libraryName;

    public AccountUnit() {
    }

    public AccountUnit(String account, String password, String libraryName) {
        this.account = account;
        this.password = password;
        this.libraryName = libraryName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }
}
