package com.writerwriter.libraassist;

/**
 * Created by Larry on 2017/12/10.
 */

public class AccountUnit {
    public static final int FINISH = 0;
    public static final int PENDING = 1;
    public static final int ERROR = 2;

    private String account;
    private String password;
    private String libraryName;
    private int state;

    public AccountUnit() {
    }

    public AccountUnit(String account, String password, String libraryName) {
        this.account = account;
        this.password = password;
        this.libraryName = libraryName;
        this.state = FINISH;
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

    public int getState() { return state; }

    public void setState(int state) { if(state<3&&state>=0) this.state = state;  }
}
