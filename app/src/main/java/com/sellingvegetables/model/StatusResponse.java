package com.sellingvegetables.model;

import java.io.Serializable;

public class StatusResponse implements Serializable {
    protected int code;
    protected String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
