package com.company;



public class Superadmin extends Group implements Staff{
    public Superadmin(String username,String password){
        super(username,password);
        this.setRankPower(2);
        this.setCanAdd(true);
        this.setCanUpdate(true);
        this.setCanDelete(true);
    }
    public Superadmin(String username, String password,boolean isBanned) {
        super(username,password,isBanned);
        this.setRankPower(2);
        this.setCanAdd(true);
        this.setCanUpdate(true);
        this.setCanDelete(true);
    }

    @Override
    public boolean isStaff() {
        return Staff.super.isStaff();
    }
}
