package org.android.framework.engine.net.xml;

import org.android.framework.engine.net.bean.BaseCallBackBean;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by ShenYan on 14-2-12.
 */
public abstract class SoapXmlAbstractBaseParser extends XmlAbstractBaseParser {
    public abstract SoapObject getSoapObject(String nameSpace, String method_Name);
    @Override
    protected BaseCallBackBean myParser(XmlPullParser parser) throws XmlPullParserException, IOException {
        return null;
    }
}
