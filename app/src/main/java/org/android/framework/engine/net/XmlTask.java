package org.android.framework.engine.net;

import org.android.framework.engine.net.bean.BaseCallBackBean;
import org.android.framework.engine.net.xml.XmlAbstractBaseParser;

/**
 * Created by ShenYan on 14-1-10.
 */
public class XmlTask extends BaseTask {
    private XmlAbstractBaseParser parser;

    public XmlTask(String url, String requestType, NetTaskCallBackInterface netTaskCallBackInterface, XmlAbstractBaseParser parser) {
        super(url,requestType, netTaskCallBackInterface);
        this.parser = parser;
    }

    @Override
    BaseCallBackBean resultProcess(byte[] bytes) {
        return parser.parser(bytes);
    }

    @Override
    String getSendString() {
        return parser.getSendXML();
    }
}
