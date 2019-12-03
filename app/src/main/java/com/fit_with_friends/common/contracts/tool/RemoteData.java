package com.fit_with_friends.common.contracts.tool;

public class RemoteData {

    public Boolean success = false;
    public String message;
    public String code;
    public String error;

    public String getError() {
        if (success)
            return "";

        return error == null || error.isEmpty() ? message : error;
    }
}
