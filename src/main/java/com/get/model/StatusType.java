package com.get.model;

public enum StatusType {
    NEW("NEW"),
    INPROGRESS("INPROGRESS"),
    FINISHED("FINISHED");
    
    String statusType;

    StatusType(String statusType) {
        this.statusType = statusType;
    }

    public String getStatusType() {
        return statusType;
    }
}
