package com.plug.api;

public class Response {

    private String status;
    private Object content;
    private String message;

    private Response (String status, Object content ) {
        this.status = status;
        this.content = content;
    }

    private Response (String status, String message ) {
        this.status = status;
        this.message = message;
    }

    public static Response ok(Object value) {
        return new Response("ok", value);
    }

    public static Response error(String message) {
        return new Response("error", message);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
