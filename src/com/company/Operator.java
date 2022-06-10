package com.company;

public class Operator extends Group{

    public Operator(String username,String password){
        super(username,password);
        this.setRankPower(0);
        this.setCanAdd(true);
        this.setCanUpdate(false);
        this.setCanDelete(false);
    }

    public Operator(String username,String password,boolean isBanned) {
        super(username,password,isBanned);
        this.setRankPower(0);
        this.setCanAdd(true);
        this.setCanUpdate(false);
        this.setCanDelete(false);
    }


}
