package org.android.framework.engine.net.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.android.framework.engine.net.bean.BaseCallBackBean;

import java.text.DateFormat;

/**
 * Json工具类
 *
 * @author shenyan
 */

public class JsonUtils {

    /**
     * 将json数据直接解析为对象
     *
     * @param jsonData
     */
    public static BaseCallBackBean parseBeanFromJson(String jsonData, Class<?> class1) {
        Gson gson = new Gson();
        return (BaseCallBackBean) gson.fromJson(jsonData, class1);
    }

    /**
     * 将对象转换为json
     *
     * @param bean
     * @return
     */
    public static String bean2json(Object bean) {
        Gson gson = new GsonBuilder().registerTypeAdapter(java.util.Date.class, new UtilDateSerializer()).setDateFormat(DateFormat.LONG).create();
        return gson.toJson(bean);
    }

}
