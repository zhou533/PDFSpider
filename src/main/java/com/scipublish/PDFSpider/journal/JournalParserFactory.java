package com.scipublish.PDFSpider.journal;

import com.scipublish.PDFSpider.journal.impl.DefaultJournalParser;
import com.scipublish.PDFSpider.journal.impl.HindawiParser;
import com.scipublish.PDFSpider.journal.impl.ScirpParser;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-12
 * Time: PM2:42
 * To change this template use File | Settings | File Templates.
 */
public class JournalParserFactory {
    public static JournalParser getParser(String url){

        String host = null;
        try {
            URL url1 = new URL(url);
            host = url1.getHost();
        }catch (Exception e){

        }

        if (StringUtils.equalsIgnoreCase(host,"downloads.hindawi.com")){
            return new HindawiParser();
        }else if (StringUtils.equalsIgnoreCase(host,"www.scirp.org")){
            return new ScirpParser();
        }

        return new DefaultJournalParser();
    }
}
