package com.hewei.csdn.pojos;

import com.hewei.common.pojos.BaseEsPojo;

/**
 * 
 * @author hewei
 * 
 * @date 2015/9/10  21:08
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class CsdnActivityDetails implements BaseEsPojo {
    private String title;
    private String addr;
    private String time;
    private String signType;
    private String signStopTime;
    private String content;

    public CsdnActivityDetails(String title, String addr, String time, String signType, String signStopTime, String content) {
        this.title = title;
        this.addr = addr;
        this.time = time;
        this.signType = signType;
        this.signStopTime = signStopTime;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getAddr() {
        return addr;
    }

    public String getTime() {
        return time;
    }

    public String getSignType() {
        return signType;
    }

    public String getSignStopTime() {
        return signStopTime;
    }

    public String getContent() {
        return content;
    }
}
