package com.takumi.wms.utils.converter;

import android.util.Log;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * Created by Chenguandu on 2017-9-4.
 */

public class DoubleTypeAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter out, Double value) {
        try {
            if (value == null){
                value = 0D;
            }
            out.value(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Double read(JsonReader in) {
        try {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                Log.e("TypeAdapter", "null is not a number");
                return 0D;
            }
            if (in.peek() == JsonToken.BOOLEAN) {
                boolean b = in.nextBoolean();
                Log.e("TypeAdapter", b + " is not a number");
                return 0D;
            }
            if (in.peek() == JsonToken.STRING) {
                String str = in.nextString();
                if (NumberUtils.isNumber(str)){
                    return Double.parseDouble(str);
                } else {
                    Log.e("TypeAdapter", str + " is not a number");
                    return 0D;
                }
            } else {
                Double value = in.nextDouble();
                return value == null ? 0D : value;
            }
        }catch(NumberFormatException e){
            Log.e("TypeAdapter", e.getMessage(), e);
        } catch (Exception e) {
            Log.e("TypeAdapter", e.getMessage(), e);
        }
        return 0D;
    }
}