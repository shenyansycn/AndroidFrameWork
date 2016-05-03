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
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2014/5/21.
 */
class HttpPostRequest implements Runnable {
    private Context mContext;
    private String url = "";
    private Map<String, String> params = null;
    private String requestBody = "";
    private HttpRequestCallBack callBackListener = null;
    private BaseTask baseTask = null;

    HttpPostRequest(BaseTask baseTask) {
        this.baseTask = baseTask;
        this.url = baseTask.url;
        this.mContext = baseTask.mContext;
        this.params = baseTask.params;
        this.requestBody = baseTask.requestBody;
        this.callBackListener = baseTask.callBackListener;
    }

    public HttpPostRequest(Context mContext, String url, Map<String, String> params, String requestBody, HttpRequestCallBack callBackListener) {
        this.url = url;
        this.mContext = mContext;
        this.params = params;
        this.requestBody = requestBody;
        this.callBackListener = callBackListener;
    }

    private final int SUCCESS = 0x01;
    private final int FAIL = 0x02;

    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    break;
                case FAIL:
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

            LogInfo.out("url = " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setRequestProperty("content-type", "text/html");
            if (NetUtil.isWifi(mContext)) {
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
            } else {
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
            }

            conn.connect();
            if (requestBody != null && !requestBody.equals("")) {
                output = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                LogInfo.out("sendtext = " + requestBody);
                output.write(requestBody);
                output.flush();
                output.close();
            }
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
                message.obj = response;
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
