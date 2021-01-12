package com.takumi.wms.model;

import java.util.List;
import java.util.Map;

/**
 * 全局保存的一些常用数据的单例缓存。
 */
public class SingletonCache {
    public static List<Product> products = null;//所有产品，每次启动程序只获取一次

    public static List<Locator> locators = null;

    public static List<StatusItem> statusItems = null;

    public static Map<String, String> properties = null;
}
