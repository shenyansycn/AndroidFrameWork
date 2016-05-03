package org.android.framework.engine.net.bean;

public class BaseSendWithPageInfoBean extends BaseSendBean {

    public BaseSendWithPageInfoBean(String funCode) {
        super(funCode);
    }

    private String prelastid = "";
    private String presum = "";
    private String pagenum = "";

    public String getPrelastid() {
        return prelastid;
    }

    public void setPrelastid(String prelastid) {
        this.prelastid = prelastid;
    }

    public String getPresum() {
        return presum;
    }

    public void setPresum(String presum) {
        this.presum = presum;
    }

    public String getPagenum() {
        return pagenum;
    }

    public void setPagenum(String pagenum) {
        this.pagenum = pagenum;
    }

}
