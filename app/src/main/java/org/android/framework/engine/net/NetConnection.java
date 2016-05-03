package org.android.framework.engine.net;

import android.os.AsyncTask;

/**
 * 网络请求类，用于post发送json和xml文件，解析返回后的文件并返回实体类
 *
 * @author shenyan
 */
class NetConnection extends AsyncTask<String, String, BaseTask> {

    private final int HTTP_HEAD_LENGTH = 7;
    private final String HTTP_HEAD = "http://";
    private String cmwapURL = "10.0.0.172";


    /**
     * 构造函数
     */
    NetConnection(NetTaskCallBackInterface back, BaseTask baseTask, String netStyle) {
        super();
        nwri = back;
        this.baseTask = baseTask;
        this.netStyle = netStyle;
    }

    private String netStyle = "";
    private String mainURL = "";
    private String subURL = "";

    private void checkURL(String url) {
        if (url != null && !"".equals(url)) {
            if (url.length() > HTTP_HEAD_LENGTH && HTTP_HEAD.equals(url.substring(0, HTTP_HEAD_LENGTH))) {
                url = url.substring(HTTP_HEAD_LENGTH, url.length());
            }
            int poisition = url.indexOf("/");
            mainURL = url.substring(0, poisition);
            subURL = url.substring(poisition, url.length());
        }
    }

    private NetTaskCallBackInterface nwri = null;
    private BaseTask baseTask = null;


    @Override
    protected void onPostExecute(BaseTask result) {
        super.onPostExecute(result);
        if (result.getNetErrorCode() != null) {
            nwri.failCallBack(result.getTag(), result.getNetErrorCode());
        } else {
            nwri.successCallBack(result.getTag(), result.getResult());
        }
    }

    @Override
    protected BaseTask doInBackground(String... params) {
        checkURL(params[0]);
        String url = "";
        if (netStyle.equals(NetStyle.NETSTYLE_CMWAP)) {
            url = "http://" + cmwapURL + subURL;
        } else {
            url = "http://" + mainURL + subURL;
        }
        if (baseTask instanceof SOAP2Task) {
            SOAP2Task soap2Task = (SOAP2Task) baseTask;
            SoapNetProcess process = new SoapNetProcess();
            byte[] bytes =  process.process(soap2Task);
            if (NetErrorCode.isException(bytes)){
                bytes =  process.process(soap2Task);
                if (NetErrorCode.isException(bytes)){
                    bytes =  process.process(soap2Task);
                }
            }
            if (NetErrorCode.isException(bytes)) {
                baseTask.setNetErrorCode(new NetErrorCode(bytes));
            } else {
                baseTask.process(bytes);
            }
            return baseTask;
        } else if (baseTask instanceof UpLoadFileTask) {
            UpLoadFileTask upLoadFileTask = (UpLoadFileTask) baseTask;
            UploadFileNetProcess process = new UploadFileNetProcess();
            String result = process.process(url, upLoadFileTask.getNameValuePairs(), netStyle, mainURL);
            if (NetErrorCode.isException(result)) {
                result = process.process(url, upLoadFileTask.getNameValuePairs(), netStyle, mainURL);
                if (NetErrorCode.isException(result)) {
                    result = process.process(url, upLoadFileTask.getNameValuePairs(), netStyle, mainURL);
                }
            }
            if (NetErrorCode.isException(result)) {
                baseTask.setNetErrorCode(new NetErrorCode(result));
            }
            return baseTask;
        } else {
            String sendtext = baseTask.getSendString();
            JsonAndXmlNetProcess process = new JsonAndXmlNetProcess();
            byte[] bytes = process.process(url, sendtext, netStyle, mainURL);
            if (NetErrorCode.isException(bytes)) {
                bytes = process.process(url, sendtext, netStyle, mainURL);
                if (NetErrorCode.isException(bytes)) {
                    bytes = process.process(url, sendtext, netStyle, mainURL);
                }
            }
            if (NetErrorCode.isException(bytes)) {
                baseTask.setNetErrorCode(new NetErrorCode(bytes));
            } else {
                baseTask.process(bytes);
            }
            return baseTask;
        }
    }



}
