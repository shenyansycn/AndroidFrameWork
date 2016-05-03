package org.android.framework.engine.net;

import org.android.framework.log.LogInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by ShenYan on 14-2-12.
 */
class UploadFileNetProcess {
    String process(String url, List<NameValuePair> nameValuePairs, String netStyle, String mainURL) {
        LogInfo.out("url = " + url);
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("charset", HTTP.UTF_8);
        if (netStyle.equals(NetStyle.NETSTYLE_CMWAP)) {
            httpParams.setParameter("X-Online-Host", mainURL);
        }
        HttpConnectionParams.setConnectionTimeout(httpParams, 10000);//http链接超时
        HttpConnectionParams.setSoTimeout(httpParams, 20000);//http数据请求超时
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        //        SchemeRegistry registry = new SchemeRegistry();
        //        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        //        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        //        HttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, null), httpParams);
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("charset", HTTP.UTF_8);

        try {
            MultipartEntity entity = new MultipartEntity();
            int size = nameValuePairs.size();
            for (int index = 0; index < size; index++) {
                String nameString = nameValuePairs.get(index).getName().toLowerCase();
                if (nameString.contains("image") || nameString.contains("faceicon")) {
                    // If the key equals to "image", we use FileBody to transfer
                    // the data
                    // 如果有image 字段就图片路径放进去，上传图片
                    File file = new File(nameValuePairs.get(index).getValue());
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(file));
                } else {
                    // Normal string data
                    //                    entity.addPart(nameValuePairs.GET(index).getName(), new StringBody(nameValuePairs.GET(index).getValue()));
                    // 上传参数
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue(), Charset.forName("UTF-8")));

                }
            }

            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != 200) {
                // 网络请求错误
                //                return "303";
                return NetErrorCode.MALFORMEDURLEXCEPTION;
            }
            HttpEntity entity1 = response.getEntity();
            String strRet = EntityUtils.toString(entity1);
            boolean isHtml = strRet.contains("<HTML>");
            LogInfo.out("uploadfile back result = " + strRet);
            if (isHtml) {
                // 判断是否返回错误
                //                return "303";
                return NetErrorCode.ISHTMLEXECPTION;
            }

            if (strRet.length() > 10) {
                // 如果无网状态，或者没有验证的网络时，判断返回的长度。因为正常情况下返回的字符串没这么长。
                /*return "302";*/
            }
            //			if (entity != null) {
            //				entity.consumeContent();
            //			}

            return strRet;
        } catch (IOException e) {
            e.printStackTrace();
            return NetErrorCode.IOEXCEPTION;
        }
    }
}
