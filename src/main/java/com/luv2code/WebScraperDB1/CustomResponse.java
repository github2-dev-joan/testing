package com.luv2code.WebScraperDB1;

import java.util.List;

public class CustomResponse <T>{
    private boolean status;

    private String message;

    private List<T> content;

    public CustomResponse(boolean status, String message, List<T> content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    public CustomResponse(boolean status,String message){
        this.status = status;
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
