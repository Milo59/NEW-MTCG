package org.example.server.http;

public enum StatusCode {
    OK(200, "OK"),
    NOT_FOUND(404, "Not Found"),
    CREATED(201, "Created"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    METHODE_NOT_ALLOWED(405, "Method Not Allowed"),
    ;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code;

    public String message;
}
