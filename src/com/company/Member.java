package com.company;

public interface Member {
    public default boolean isStaff() {
        return false;
    }
}
