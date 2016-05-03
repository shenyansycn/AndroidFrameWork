package org.android.framework.engine.net;

import org.android.framework.engine.net.bean.BaseCallBackBean;
import org.android.framework.engine.net.bean.BaseSendBean;
import org.android.framework.engine.net.json.JsonUtils;

import java.io.UnsupportedEncodingException;

public class JsonTask extends BaseTask {
    private Class<?> baseClass;

    //    public JsonTask setBaseClass(Class<?> baseClass) {
    //        this.baseClass = baseClass;
    //        return this;
    //    }


    protected BaseSendBean baseSendBean;

    //    public JsonTask setBaseSendEntity(BaseSendEntity baseSendEntity) {
    //        this.baseSendEntity = baseSendEntity;
    //        return this;
    //    }

    public JsonTask(String url, String requestType, NetTaskCallBackInterface netTaskCallBackInterface, BaseSendBean baseSendBean, Class<?> baseClass) {
        super(url, requestType, netTaskCallBackInterface);
        this.baseSendBean = baseSendBean;
        this.baseClass = baseClass;
    }

    @Override
    BaseCallBackBean resultProcess(byte[] bytes) {
        String text = "";
        try {
            text = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (text != null && !"".equals(text)) {
            return JsonUtils.parseBeanFromJson(text, baseClass);
        } else {
            return null;
        }
    }

    @Override
    String getSendString() {
        return JsonUtils.bean2json(baseSendBean);
    }
}
