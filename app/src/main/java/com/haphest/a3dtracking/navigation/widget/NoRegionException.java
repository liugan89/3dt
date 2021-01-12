package com.haphest.a3dtracking.navigation.widget;

public class NoRegionException extends Exception {

    @Override
    public String getMessage() {
        return "不允许空白的Location或Area被添加到Area中";
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }
}
