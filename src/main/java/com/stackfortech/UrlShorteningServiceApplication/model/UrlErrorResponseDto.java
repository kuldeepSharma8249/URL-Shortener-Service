package com.stackfortech.UrlShorteningServiceApplication.model;

public class UrlErrorResponseDto {
    private String status;
    private String error;

    public UrlErrorResponseDto(){

    }
    public UrlErrorResponseDto(String error, String status) {
        this.error = error;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
