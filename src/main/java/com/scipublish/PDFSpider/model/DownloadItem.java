package com.scipublish.PDFSpider.model;

import com.scipublish.PDFSpider.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-4
 * Time: PM2:18
 * To change this template use File | Settings | File Templates.
 */
public class DownloadItem {
    private String url;
    private String filename;
    private String itemId;

    public DownloadItem(String url, String itemId) {
        this.url = url;
        this.itemId = itemId;
    }

    public String getUrl() {
        return url;
    }

    public String getItemId() {
        return itemId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPrefix(){
        if (StringUtils.isEmpty(this.filename)){
            return "";
        }

        if(!StringUtils.contains(this.filename, '_')){
            return "";
        }

        String[] strs = StringUtils.split(this.filename, '_');
        if (strs == null || strs.length == 0){
            return "";
        }

        return strs[0];
    }
}
