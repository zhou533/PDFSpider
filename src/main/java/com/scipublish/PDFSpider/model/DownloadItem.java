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

    public DownloadItem(String url) {
        this.url = url;

    }

    public String getUrl() {
        return url;
    }
}
