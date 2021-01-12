package com.takumi.wms.utils;

import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.takumi.wms.model.InventoryAttribute;
import com.takumi.wms.model.Lot;
import com.takumi.wms.model.Operator;
import com.takumi.wms.model.OrderShipGroup;
import com.takumi.wms.model.StatusItem;
import com.takumi.wms.model.dto.inout.InOutCommandDtos;
import com.takumi.wms.net.NetService;
import com.takumi.wms.net.NetUtil;
import com.takumi.wms.obj.OssFile;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaTest {
    public static void main(String[] args) {
        try {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

            String s = "{\n" + "  \"shipGroupSeqId\": \"2018090901\",\n" + "  \"shipmentId\": \"zf001\",\n" + "  \"noticeInformationLine\": [{\n" + "    \"orderItemSeqId\": \"14200\",\n" + "    \"productId\": \"21001\",\n" + "    \"orderId\": \"CC1111-XX\",\n" + "    \"quantity\": 500000.000000\n" + "  }, {\n" + "    \"orderItemSeqId\": \"14201\",\n" + "    \"productId\": \"21001\",\n" + "    \"orderId\": \"CC2222-YY\",\n" + "    \"quantity\": 500000.000000\n" + "  }]\n" + "}";
            OrderShipGroup o = gson.fromJson(s, OrderShipGroup.class);
            System.out.println(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
