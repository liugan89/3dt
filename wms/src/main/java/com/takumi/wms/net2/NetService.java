package com.takumi.wms.net2;

import com.takumi.wms.BuildConfig;

/**
 * 网络中要用到的一些参数
 */
public class NetService {
    //baseUrl
    public static String BaseURL = BuildConfig.API_HOST + "api/";

    public static final String addProductionInput = "Productions/@/_commands/AddProductionLineInput";
    public static final String addProductionOutput = "Productions/@/_commands/AddProductionLineOutput";
    public static final String completeProduction = "Productions/@/_commands/DocumentAction";
    public static final String getAttributeSetInstances = "queries/attributeSetInstances/getAttributeSetInstances";
    public static final String getProductions = "Productions/@";
    public static final String deleteProductionOut = "Productions/@/ProductionLines/@/ProductionLineOutputs/@";

    public static String GetUrl(String base, String... ss) {
        String r = null;
        for (String s : ss) {
            base = base.replaceFirst("@", s);
        }
        return base;
    }

    public static void main(String[] args) {
        //System.out.println(NetService.GetUrl(NetService.bindDevices, "1", "2"));
    }
}
