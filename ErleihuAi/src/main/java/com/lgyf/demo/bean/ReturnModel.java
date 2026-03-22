package com.lgyf.demo.bean;

public class ReturnModel<T> {
    private T data;
    private T meta;

    public ReturnModel() {
        super();
    }

    public ReturnModel(T data, T meta) {
        this.data = data;
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }
}
