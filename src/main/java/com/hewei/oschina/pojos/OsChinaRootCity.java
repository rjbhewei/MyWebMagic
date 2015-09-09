package com.hewei.oschina.pojos;
/**
 * 
 * @author hewei
 * 
 * @date 2015/9/9  18:31
 *
 * @version 5.0
 *
 * @desc 
 *
 */
public class OsChinaRootCity implements OsChinaPojo{
    private String name;
    private String url;

    public OsChinaRootCity(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
