package com.company;



public class User {
    private String name,rank,password;
    private int rankPower;
    private boolean isBanned;

    public User(String name, String rank,boolean isBanned,String password,int rankPower) {
        this.name = name;
        this.rank = rank;
        this.isBanned = isBanned;
        this.password = password;
        this.rankPower = rankPower;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public boolean isBanned() {
        return isBanned;
    }
    public String getIsBanned(){
        return String.valueOf(isBanned);
    }

    public void setBanned(boolean banned) {
        isBanned = banned;
    }

    public String getPassword() {
        return password;
    }

    public int getRankPower() {
        return rankPower;
    }

    public void setRankPower(int rankPower) {
        this.rankPower = rankPower;
    }
}
