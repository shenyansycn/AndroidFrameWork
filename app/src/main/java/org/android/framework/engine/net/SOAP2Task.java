package org.android.framework.engine.net;

import org.android.framework.engine.net.bean.BaseCallBackBean;
import org.android.framework.engine.net.xml.SoapXmlAbstractBaseParser;
import org.ksoap2.serialization.SoapObject;

/**
 * Created by ShenYan on 14-1-20.
 */
public class SOAP2Task extends BaseTask {
    private SoapXmlAbstractBaseParser parser;
    private String nameSpace = "";
    private String method_Name = "";

    public SOAP2Task(String url, String nameSpace, String method_Name, NetTaskCallBackInterface netTaskCallBackInterface, SoapXmlAbstractBaseParser parser) {
        super(url, "", netTaskCallBackInterface);
        this.parser = parser;
        this.nameSpace = nameSpace;
        this.method_Name = method_Name;
    }

    @Override
    BaseCallBackBean resultProcess(byte[] bytes) {
        return parser.parser(bytes);
    }

    @Override
    String getSendString() {
        return parser.getSendXML();
    }

    SoapObject getSoapObject() {
        return parser.getSoapObject(nameSpace, method_Name);
    }

    String getNameSpace() {
        return nameSpace;
    }

    String getMethod_Name() {
        return method_Name;
    }
}
