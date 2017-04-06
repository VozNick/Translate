package com.example.vmm408.translatefeb_27.model;

public class ResponseModel {
    private Integer code;
    private String[] text;

    public ResponseModel() {
    }

    public ResponseModel(Integer code, String[] text) {
        this.code = code;
        this.text = text;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setText(String[] text) {
        this.text = text;
    }

    public String[] getText() {
        return text;
    }
}
