package com.haphest.a3dtracking.model;

/**
 * Created by Administrator on 2019/5/20/020.
 */

public class Token {

    private static Token token;

    public static Token getToken() {
        return token;
    }

    public static void setToken(Token t) {
        token = t;
    }

    private String iss;
    private String sub;
    private String aud;
    private int exp;
    private int iat;
    private String user_group;
    private String role;
    private String sub_description;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getIat() {
        return iat;
    }

    public void setIat(int iat) {
        this.iat = iat;
    }

    public String getUser_group() {
        return user_group;
    }

    public void setUser_group(String user_group) {
        this.user_group = user_group;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSub_description() {
        return sub_description;
    }

    public void setSub_description(String sub_description) {
        this.sub_description = sub_description;
    }
}
