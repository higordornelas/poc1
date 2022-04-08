package com.higor.poc1.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Problem {

    private Integer status;
    private String type;
    private String title;
    private String detail;

    private List<Field> fields;

    public static class Field {

        private String name;
        private String userMessage;

        public String getName() {
            return name;
        }

        public String getUserMessage() {
            return userMessage;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUserMessage(String userMessage) {
            this.userMessage = userMessage;
        }

        public Field() {
        }

    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
