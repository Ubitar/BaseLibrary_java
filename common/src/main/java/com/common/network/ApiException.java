package com.common.network;

import com.common.bean.BaseResponse;

public class ApiException extends Exception {
    private int code;
    private String displayMessage;
    private BaseResponse data;

    public ApiException(int code, String displayMessage) {
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public ApiException(int code, String message, String displayMessage) {
        super(message);
        this.code = code;
        this.displayMessage = displayMessage;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public BaseResponse getData() {
        return data;
    }

    public void setData(BaseResponse data) {
        this.data = data;
    }
}