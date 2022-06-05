package com.company;

public class Group {
    String username,password,key;
    boolean canAdd,canUpdate,canDelete,canClear;
    boolean canPrint,canImport,canExport;
    public Group(){

    }
    public Group(String username,String password){
        this.username = username;
        this.password = password;

    }

    public Group(String username, String password, boolean canAdd, boolean canUpdate, boolean canDelete, boolean canClear, boolean canPrint, boolean canImport, boolean canExport) {
        this.username = username;
        this.password = password;
        this.canAdd = canAdd;
        this.canUpdate = canUpdate;
        this.canDelete = canDelete;
        this.canClear = canClear;
        this.canPrint = canPrint;
        this.canImport = canImport;
        this.canExport = canExport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return  username + "," + password +"\n";
    }
}
