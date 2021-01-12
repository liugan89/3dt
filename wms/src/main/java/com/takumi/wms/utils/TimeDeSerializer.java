package com.takumi.wms.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;

public class TimeDeSerializer implements JsonDeserializer {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.SSSZ");

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jo = json.getAsJsonObject();
        return null;
    }
}
