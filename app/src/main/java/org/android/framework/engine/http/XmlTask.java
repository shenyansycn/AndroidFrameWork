//package org.android.framework.engine.http;
//
//import android.content.Context;
//
//import java.util.Map;
//
///**
// * Created by Administrator on 2014/5/21.
// */
//public class XmlTask extends BaseTask {
//    protected XmlTask(Context mContext, String url, Map<String, String> params, HttpRequestCallBack callBack) {
//        super(mContext, url, params, "", callBack);
//
//    }
//
//    @Override
//    void submit() {
//        HttpGetRequest request = new HttpGetRequest(mContext, url, params, callBackListener);
//        NetTaskExecutorService.getInstance().submit(request);
//    }
//}
