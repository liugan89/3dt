package com.takumi.wms.model;

/**
 * 用户/操作员
 */
public class Operator {

    private Operator() {
    }

    private static Operator operator = new Operator();

    public static Operator getOperator() {
        return operator;
    }

    private String account;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
