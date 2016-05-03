package org.android.framework.engine.http;

import android.content.Context;

import org.android.framework.engine.net.json.JsonUtils;

import java.util.Map;

/**
 * Created by Administrator on 2014/5/21.
 */
public class JsonTask extends BaseTask {
    private BaseRequestBodyBean requestBodyBean;
    private Class<?> baseClass;

    protected JsonTask(Context mContext, String url, RequestType requestType, Map<String, String> params, BaseRequestBodyBean requestBodyBean, Class<?> baseClass, HttpRequestCallBack callBack) {
        super(mContext, url, requestType, params, JsonUtils.bean2json(requestBodyBean), callBack);
        this.requestBodyBean = requestBodyBean;
        this.baseClass = baseClass;
    }

    @Override
    void submit() {
//        Runnable runnable = null;
//        if (requestType == RequestType.POST) {
//        } else {
//            runnable = new HttpGetRequest(mContext, url, params, callBackListener);
//        }
//        NetTaskExecutorService.getInstance().submit(runnable);
    }

    @Override
    BaseResultBean doResult(byte[] result) {
        return null;
    }
}
