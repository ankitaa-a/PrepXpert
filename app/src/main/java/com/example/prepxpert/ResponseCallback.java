package com.example.prepxpert;

public interface ResponseCallback {

    void onResponse(String response);

    void onError(Throwable throwable);
}
