package org.android.framework.engine.net;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by ShenYan on 14-2-12.
 */
class SoapNetProcess {
    byte[] process(SOAP2Task soap2Task) {
        byte[] response = null;
        try {
            SoapObject rpc = new SoapObject(soap2Task.getNameSpace(), soap2Task.getMethod_Name());
            rpc.addProperty("xmlData", soap2Task.getSendString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.bodyOut = soap2Task.getSoapObject();
            envelope.dotNet = true;
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(soap2Task.getUrl());
            ht.call(soap2Task.getNameSpace() + soap2Task.getMethod_Name(), envelope);
            Object obkect = envelope.getResponse();
            if (obkect != null) {
                response = obkect.toString().getBytes();
                if (null == response || response.length <= 0) {
                    return NetErrorCode.OTHEREXCEPTION.getBytes();
                } else {
                    return response;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return NetErrorCode.XMLPULLPARSEREXCEPTION.getBytes();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
            return NetErrorCode.SOAPFAULT.getBytes();
        } catch (HttpResponseException e) {
            e.printStackTrace();
            return NetErrorCode.HTTPRESPONSEEXCEPTION.getBytes();
        } catch (IOException e){
           e.printStackTrace();
            return NetErrorCode.IOEXCEPTION.getBytes();
        }

        return response;
    }
}
