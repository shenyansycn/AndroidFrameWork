package org.android.framework.engine.net;

import org.android.framework.engine.net.bean.BaseCallBackBean;

/**
 * Json任务返回接口
 *
 * @author qinwei
 */
public interface NetTaskCallBackInterface {
    /**
     * 返回接口
     *
     * @param
     */
    void successCallBack(Object tag, BaseCallBackBean entity);

    void failCallBack(Object tag, NetErrorCode errorCode);
}
