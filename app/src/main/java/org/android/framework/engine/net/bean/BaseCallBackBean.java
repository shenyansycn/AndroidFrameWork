package org.android.framework.engine.net.bean;

/**
 * 实体类的基类
 *
 * @author qinwei
 */
public class BaseCallBackBean {
    protected String errorCode;
    protected String errorInfo;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }
}
