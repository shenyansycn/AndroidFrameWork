package org.android.framework.engine.net.json;

/**
 *  定义对Date的转换
 */

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * 反串行Data
 * @author ShenYan
 */
public class UtilDateDeserializer implements JsonDeserializer<java.util.Date> {
    @Override
    public java.util.Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new java.util.Date(json.getAsJsonPrimitive().getAsLong());
    }
}
