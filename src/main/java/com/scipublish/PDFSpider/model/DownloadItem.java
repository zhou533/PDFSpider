package com.scipublish.PDFSpider.model;

import com.scipublish.PDFSpider.configuration.Configuration;

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
}
