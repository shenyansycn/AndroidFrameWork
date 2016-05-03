package org.android.framework.engine.net.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 串行Data
 * @author ShenYan
 */
public class UtilDateSerializer implements JsonSerializer<java.util.Date> {
    @Override
    public JsonElement serialize(java.util.Date src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getTime());
    }
}