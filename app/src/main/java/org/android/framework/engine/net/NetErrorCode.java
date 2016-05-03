package org.android.framework.engine.net;

import org.android.framework.engine.net.bean.BaseCallBackBean;

import java.io.UnsupportedEncodingException;

/**
 * 错误提示类
 *
 * @author shenyan
 */
public class NetErrorCode extends BaseCallBackBean {

    public NetErrorCode(byte[] bytes) {
        try {
            this.errorCode = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public NetErrorCode(String code) {
        this.errorCode = code;
    }

    public String getErrorExceptionType() {
        return null;
    }

    public static final String MALFORMEDURLEXCEPTION = "10";
    public static final String PROTOCOLEXCEPTION = "11";
    public static final String UNSUPPORTEDENCODINGEXCEPTION = "12";
    public static final String IOEXCEPTION = "13";
    public static final String UNKNOWNHOSTEXCEPTION = "14";
    public static final String OTHEREXCEPTION = "15";
    public static final String PARSEREXCEPTION = "16";
    public static final String NULLEXCEPTION = "17";
    public static final String EXCEPTION = "18";
    public static final String SOCKETTIMEOUTEXCEPTION = "19";
    public static final String ISHTMLEXECPTION = "isHtml";
    public static final String SOAPFAULT = "SoapFault";
    public static final String HTTPRESPONSEEXCEPTION = "HttpResponseException";
    public static final String XMLPULLPARSEREXCEPTION = "XmlPullParserException";


    public static final Byte[] BYTE_MALFORMEDURLEXCEPTION = new Byte[]{10};
    public static final Byte[] BYTE_PROTOCOLEXCEPTION = new Byte[]{11};

    public static boolean isException(byte[] response) {
        String string = "";
        try {
            string = new String(response, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
        return isException(string);
    }

    static boolean isException(String string) {
        if (string.equals(IOEXCEPTION) || string.equals(MALFORMEDURLEXCEPTION) || string.equals(PROTOCOLEXCEPTION) || string.equals(UNSUPPORTEDENCODINGEXCEPTION) || string.equals(UNKNOWNHOSTEXCEPTION) || string.equals(OTHEREXCEPTION) || string.equals(EXCEPTION) || string.equals(SOCKETTIMEOUTEXCEPTION) || string.equals(ISHTMLEXECPTION) || string.equals(SOAPFAULT) || string.equals(HTTPRESPONSEEXCEPTION) || string.equals(XMLPULLPARSEREXCEPTION)) {
            return true;
        } else {
            return false;
        }
    }
}
