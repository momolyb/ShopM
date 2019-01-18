package com.tim.shopm.model;

public interface LoadDataCallBack<T> {
    void onSuccess(T t);
    void onError(String msg,int code);
}
