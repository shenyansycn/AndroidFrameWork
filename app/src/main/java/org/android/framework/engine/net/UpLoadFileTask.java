package org.android.framework.engine.net;

import org.android.framework.engine.net.bean.BaseCallBackBean;
import org.apache.http.NameValuePair;

import java.util.List;

/**
 * Created by ShenYan on 14-2-12.
 */
public class UpLoadFileTask extends BaseTask {
    private List<NameValuePair> nameValuePairs;
    public UpLoadFileTask(String url,List<NameValuePair> nameValuePairList, NetTaskCallBackInterface netTaskCallBackInterface) {
        super(url,"", netTaskCallBackInterface);
        this.nameValuePairs = nameValuePairList;
    }

    @Override
    BaseCallBackBean resultProcess(byte[] bytes) {
        return null;
    }

    @Override
    String getSendString() {
        return null;
    }

    public List<NameValuePair> getNameValuePairs() {
        return nameValuePairs;
    }
}
