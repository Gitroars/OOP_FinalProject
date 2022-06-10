package com.company;

public class Group {
    private String username,password;
    private int rankPower;
    private boolean canAdd,canUpdate,canDelete;
    private boolean isBanned = false;

    public Group(String username,String password){
        this.username = username;
        this.password = password;
    }
    public Group(String username,String password,boolean isBanned){
        this.username = username;
        this.password = password;
        this.isBanned = isBanned;
    }




    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRankPower() {
        return rankPower;
    }

    public void setRankPower(int rankPower) {
        this.rankPower = rankPower;
    }

    public boolean isBanned() {
        return isBanned;
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public boolean isCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(boolean canUpdate) {
        this.canUpdate = canUpdate;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }



    @Override
    public String toString() {
        return  username + "," + password +","+isBanned+"\n";
    }
}
