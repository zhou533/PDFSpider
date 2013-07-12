package com.scipublish.PDFSpider.model;

import com.scipublish.PDFSpider.journal.JournalParser;
import com.scipublish.PDFSpider.journal.JournalParserFactory;
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
    private String detail;

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

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrefix(){
        JournalParser parser = JournalParserFactory.getParser(this.url);
        return parser.parse(this.url, this.filename);
    }
}
