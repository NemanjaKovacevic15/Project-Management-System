package com.get.model;

public enum UserRoleType {
    USER("USER"),
    DEVELOPER("DEVELOPER"),
    PROJECT_MANAGER("PROJECT_MANAGER"),
    ADMIN("ADMIN");

    String userRoleType;

    private UserRoleType(String userProfileType) {
        this.userRoleType = userProfileType;
    }

    public String getUserRoleType() {
        return userRoleType;
    }

}
