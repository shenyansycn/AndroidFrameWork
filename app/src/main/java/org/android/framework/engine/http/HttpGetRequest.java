package org.android.framework.engine.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.android.framework.engine.net.NetUtil;
import org.android.framework.log.LogInfo;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2014/5/21.
 */
class HttpGetRequest implements Runnable {
    private Context mContext;
    private String url = "";
    private Map<String, String> params = null;
    private HttpRequestCallBack callBackListener = null;

    private BaseTask baseTask = null;

    public HttpGetRequest(BaseTask baseTask) {
        this.baseTask = baseTask;
        this.mContext = baseTask.mContext;
        this.url = baseTask.url;
        this.params = baseTask.params;
        this.callBackListener = baseTask.callBackListener;

    }

    //    public HttpGetRequest(Context mContext, String url, Map<String, String> params, HttpRequestCallBack callBackListener) {
    //        this.mContext = mContext;
    //        this.url = url;
    //        this.params = params;
    //        this.callBackListener = callBackListener;
    //    }

    private final int SUCCESS = 0x01;
    private final int FAIL = 0x02;

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    if (baseTask.callBackListener != null) {
                        baseTask.callBackListener.onSuccess(msg.obj);
                    }
                    break;
                case FAIL:
                    if (baseTask.callBackListener != null) {
                        baseTask.callBackListener.onFail();
                    }
                    break;
            }
        }
    };

    @Override
    public void run() {
        BufferedWriter output = null;
        InputStream input = null;
        ByteArrayOutputStream buf = null;
        try {
            if (params != null && !params.isEmpty()) {
                StringBuffer buffer = new StringBuffer();
                Iterator iter = params.entrySet().iterator();
                boolean isFrist = true;
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    if (isFrist) {
                        isFrist = false;
                    } else {
                        buffer.append("&");
                    }
                    buffer.append(entry.getKey());
                    buffer.append("=");
                    buffer.append(entry.getValue());
                }
                StringBuffer sb = new StringBuffer();
                sb.append(new String(url));
                sb.append("?");
                sb.append(buffer.toString());
                url = sb.toString();
            }
            LogInfo.out("url = " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            if (NetUtil.isWifi(mContext)) {
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
            } else {
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
            }

            conn.connect();
            input = conn.getInputStream();

            byte[] response;
            buf = new ByteArrayOutputStream();
            byte[] ch = new byte[256];
            int len;
            while ((len = input.read(ch)) >= 0) {
                buf.write(ch, 0, len);
            }
            response = buf.toByteArray();
            buf.close();
            input.close();
            String backstring = new String(response, "UTF-8");
            LogInfo.out("backstring = " + backstring);
            if (null == response || response.length <= 0) {
                mHandler.sendEmptyMessage(FAIL);
            } else {
                Message message = new Message();
                message.what = SUCCESS;
                message.obj = baseTask.doResult(response);
                mHandler.sendMessage(message);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            String data = e.toString();
            if (data.toLowerCase().indexOf("unknownhost") != -1) {
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mHandler.sendEmptyMessage(FAIL);
            try {
                if (buf != null) {
                    buf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
