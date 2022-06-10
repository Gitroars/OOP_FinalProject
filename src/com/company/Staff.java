package com.company;

public interface Staff {
    public default boolean isStaff() {
        return true;
    }
}
