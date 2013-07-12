package com.scipublish.PDFSpider.journal.impl;

import com.scipublish.PDFSpider.journal.JournalParser;
import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-12
 * Time: PM2:40
 * To change this template use File | Settings | File Templates.
 */
public class ScirpParser implements JournalParser{
    @Override
    public String parse(String url, String details) {
        if (StringUtils.isEmpty(details)){
            return "";
        }

        if(!StringUtils.contains(details, '_')){
            return "";
        }

        String[] strs = StringUtils.split(details, '_');
        if (strs == null || strs.length == 0){
            return "";
        }

        return strs[0];
    }
}
