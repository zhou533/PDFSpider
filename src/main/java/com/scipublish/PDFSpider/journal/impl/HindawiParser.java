package com.scipublish.PDFSpider.journal.impl;

import com.scipublish.PDFSpider.journal.JournalParser;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-12
 * Time: PM2:25
 * To change this template use File | Settings | File Templates.
 */
public class HindawiParser implements JournalParser {
    @Override
    public String parse(String url, String details) {

        try {
            URL url1 = new URL(url);
            String path = url1.getPath();
            String[] strs = StringUtils.split(path,'/');
            StringBuilder stringBuilder = new StringBuilder();
            if (strs.length > 0){
                stringBuilder.append(strs[0]);
            }
            if (strs.length > 1){
                stringBuilder.append("_");
                stringBuilder.append(strs[1]);
            }
            return stringBuilder.toString();
        } catch (MalformedURLException e) {


        }

        return "";
    }
}
