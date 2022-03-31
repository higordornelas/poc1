package com.higor.poc1.api.exceptionhandler;

public enum ProblemType {

    SYSTEM_ERROR("/system-error", "System error"),
    RESOURCE_NOT_FOUND("/resource-not-found", "Resource not found"),
    ENTITY_IN_USE("/entity-in-use", "Entity in use"),
    BUSINESS_ERROR("/business-error", "Business error"),
    WRONG_MESSAGE("/wrong-message", "Wrong message"),
    INVALID_PARAMETER("/invalid-parameter", "Invalid parameter");

    private String title;
    private String uri;

    ProblemType(String path, String title) {
        this.uri = "http://localhost:8080" + path;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }
}
