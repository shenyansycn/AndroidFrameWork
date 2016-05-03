package org.android.framework.engine.http;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2014/5/21.
 */
abstract class BaseTask {
    protected Context mContext;
    protected String url = "";
    protected Map<String, String> params = null;
    protected String requestBody = "";
    protected HttpRequestCallBack callBackListener;
    protected RequestType requestType;

    protected BaseTask(Context mContext, String url, RequestType requestType, Map<String, String> params, String requestBody, HttpRequestCallBack callBackListener) {
        this.mContext = mContext;
        this.url = url;
        this.requestType = requestType;
        this.params = params;
        this.requestBody = requestBody;
        this.callBackListener = callBackListener;
    }

    abstract void submit();
    abstract BaseResultBean doResult(byte[] result);
}
