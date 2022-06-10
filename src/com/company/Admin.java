package com.company;

public class Admin extends Group implements Staff{

    public Admin(String username, String password) {
        super(username,password);
        this.setRankPower(1);
        this.setCanAdd(true);
        this.setCanUpdate(true);
        this.setCanDelete(false);
    }
    public Admin(String username, String password,boolean isBanned) {
        super(username,password,isBanned);
        this.setRankPower(1);
        this.setCanAdd(true);
        this.setCanUpdate(true);
        this.setCanDelete(false);
    }

    @Override
    public boolean isStaff() {
        return Staff.super.isStaff();
    }


}
