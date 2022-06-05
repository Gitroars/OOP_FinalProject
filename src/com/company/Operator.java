package com.company;

public class Operator extends Group{

    public Operator(String username,String password) {
        this.username = username;
        this.password = password;
        this.canAdd = true;
        this.canUpdate = false;
        this.canDelete = false;
        this.canClear = false;
        this.canPrint = true;
        this.canImport = false;
        this.canExport = true;
    }
}
