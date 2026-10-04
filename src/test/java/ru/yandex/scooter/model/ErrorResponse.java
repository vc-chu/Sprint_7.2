package ru.yandex.scooter.model;

public class ErrorResponse {

    private int code;
    private String message;

    public ErrorResponse() {
        // Нужен Rest Assured / Jackson для десериализации JSON
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}