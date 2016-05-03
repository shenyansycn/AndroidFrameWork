package org.android.framework.engine.net;

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

/**
 * Created by ShenYan on 14-2-12.
 */
class JsonAndXmlNetProcess {
    byte[] process(String url, String sendtext, String netStyle, String mainURL) {
        BufferedWriter output = null;
        InputStream input = null;
        ByteArrayOutputStream buf = null;
        try {

            LogInfo.out("url = " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            if (netStyle.equals(NetStyle.NETSTYLE_CMWAP)) {
                conn.setRequestProperty("X-Online-Host", mainURL);
            }
            conn.setDoInput(true);
            if (sendtext != null && !sendtext.equals("")) {
                conn.setDoOutput(true);
            }
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "text/html");

            // if (NetUtil.isWifi(mContext)) {
            // conn.setConnectTimeout(15000);
            // conn.setReadTimeout(15000);
            // } else {
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
            // }

            conn.connect();
            if (sendtext != null && !sendtext.equals("")) {
                output = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "UTF-8"));
                LogInfo.out("sendtext = " + sendtext);
                output.write(sendtext);
                output.flush();
                output.close();
            }
            //            LogInfo.out("start input");
            input = conn.getInputStream();

            byte[] response = null;
            buf = new ByteArrayOutputStream();
            byte[] ch = new byte[256];
            int len;
            //            LogInfo.out("GET input");
            while ((len = input.read(ch)) >= 0) {
                buf.write(ch, 0, len);
            }
            response = buf.toByteArray();
            buf.close();
            input.close();
            String backstring = new String(response, "UTF-8");
            LogInfo.out("backstring = " + backstring);
            if (null == response || response.length <= 0) {
                return NetErrorCode.OTHEREXCEPTION.getBytes();
            } else {
                return response;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return NetErrorCode.MALFORMEDURLEXCEPTION.getBytes();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            return NetErrorCode.SOCKETTIMEOUTEXCEPTION.getBytes();
        } catch (ProtocolException e) {
            e.printStackTrace();
            return NetErrorCode.PROTOCOLEXCEPTION.getBytes();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return NetErrorCode.UNSUPPORTEDENCODINGEXCEPTION.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
            String data = e.toString();
            if (data.toLowerCase().indexOf("unknownhost") != -1) {
                return NetErrorCode.UNKNOWNHOSTEXCEPTION.getBytes();
            } else {
                return NetErrorCode.IOEXCEPTION.getBytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return NetErrorCode.EXCEPTION.getBytes();
        } finally {
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
