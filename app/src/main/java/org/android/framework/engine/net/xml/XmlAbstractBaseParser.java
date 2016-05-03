package org.android.framework.engine.net.xml;

import org.android.framework.engine.net.NetErrorCode;
import org.android.framework.engine.net.bean.BaseCallBackBean;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 解析抽象类
 *
 * @author shenyan
 */
public abstract class XmlAbstractBaseParser {

    public BaseCallBackBean parser(byte[] bytes) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            InputStreamReader reader = new InputStreamReader(in);
            parser.setInput(reader);
            BaseCallBackBean be = myParser(parser);
            if (null == be) {
                return new NetErrorCode(NetErrorCode.PARSEREXCEPTION);
            } else {
                return be;
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return new NetErrorCode(NetErrorCode.PARSEREXCEPTION);
        } catch (IOException e) {
            e.printStackTrace();
            return new NetErrorCode(NetErrorCode.PARSEREXCEPTION);
        }
    }

    public abstract String getSendXML();

    protected abstract BaseCallBackBean myParser(XmlPullParser parser) throws XmlPullParserException, IOException;
}
