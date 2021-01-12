package com.haphest.a3dtracking.navigation.widget;

import com.haphest.a3dtracking.model.Product;

import java.util.List;

//货位层数
public class Floor {
    public int number;//当前层数
    public List<Product> products;//当前层的产品（假设有多个产品）
    public Product product;//当前层的产品（假设有1个产品）
    public boolean hasProducts;//当前层是否有产品

    public Floor(int number) {
        this.number = number;
    }
}
