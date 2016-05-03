package org.android.framework.engine.net;

import android.content.Context;
import android.os.AsyncTask;

import org.android.framework.engine.net.bean.BaseCallBackBean;

/**
 * Created by ShenYan on 14-1-10.
 */
abstract class BaseTask {
    /**
     * 任务标记
     */
    protected Object tag = "";

    public Object getTag() {
        return tag;
    }

    public BaseTask setTag(Object tag) {
        this.tag = tag;
        return this;
    }


    /**
     * 访问的网络地址
     */
    protected String url = "";

    public String getUrl() {
        return url;
    }

    public BaseTask setUrl(String url) {
        this.url = url;
        return this;
    }


    protected NetTaskCallBackInterface netTaskCallBackInterface;

    public NetTaskCallBackInterface getNetTaskCallBackInterface() {
        return netTaskCallBackInterface;
    }

    //    public BaseTask setNetTaskCallBackInterface(NetTaskCallBackInterface netTaskCallBackInterface) {
    //        this.netTaskCallBackInterface = netTaskCallBackInterface;
    //        return this;
    //    }

    /**
     * 错误代码
     */
    private NetErrorCode errorCode;

    public NetErrorCode getNetErrorCode() {
        return errorCode;
    }

    public void setNetErrorCode(NetErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    private BaseCallBackBean baseCallBackEntity;

    //    public BaseTask setBaseCallBackEntity(BaseCallBackEntity baseCallBackEntity) {
    //        this.baseCallBackEntity = baseCallBackEntity;
    //        return this;
    //    }

    public BaseTask(String url, String requestType, NetTaskCallBackInterface netTaskCallBackInterface) {
        this.url = url;
        this.netTaskCallBackInterface = netTaskCallBackInterface;
    }

    protected void process(byte[] bytes) {
        baseCallBackEntity = resultProcess(bytes);
    }

    public BaseCallBackBean getResult() {
        return baseCallBackEntity;
    }

    abstract BaseCallBackBean resultProcess(byte[] bytes);

    abstract String getSendString();

    private NetConnection netConnection;

    public AsyncTask submit(Context context) {
        if (url == null || "".equals(url)) {
            throw new IllegalArgumentException("please set URL");
        } else {
            if (NetUtil.isNetworkAvailable(context)) {
                netConnection = new NetConnection(netTaskCallBackInterface, this, NetUtil.getNetStyle(context));
                netConnection.execute(getUrl());
            } else {
                setNetErrorCode(new NetErrorCode("net not available, please check net setting."));
                getNetTaskCallBackInterface().failCallBack(getTag(), getNetErrorCode());
            }
            return netConnection;
        }
    }

    public void cancelSubmit() {
        if (netConnection != null && netConnection.getStatus() == AsyncTask.Status.RUNNING) {
            netConnection.cancel(true);
        }
    }
}
