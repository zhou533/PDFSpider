package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.model.DigItem;
import com.scipublish.PDFSpider.service.HttpService;
import com.scipublish.PDFSpider.utils.RegexUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-9
 * Time: AM10:35
 * To change this template use File | Settings | File Templates.
 */
public class DigThread implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(DigThread.class);

    @Override
    public void run() {

        while (true){
            DigItem item = null;
            try {
                item = ThreadCommon.ITEM_DIG_QUEUE.poll(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                LOGGER.error(e);
            }

            if (item == null){
                continue;
            }

            byte[] bytes = HttpService.get(item.getUrl());
            if (bytes == null){
                continue;
            }

            Document document = Jsoup.parse(new String(bytes));
            Elements elements = document.select("a");
            for (Element element : elements){
                String url = element.attr("href");

                if (RegexUtils.checkPDFUrl(url)){
                    LOGGER.info("link:" + url);
                }else {
                    List<NameValuePair> params = URLEncodedUtils.parse(url, Charset.forName("UTF-8"));
                    if (params != null && params.size() > 0){
                        for (NameValuePair pair : params){
                            String value = pair.getValue();
                            if (value != null && RegexUtils.checkPDFUrl(value)){
                                LOGGER.info("link:" + value);
                            }
                        }
                    }
                }
            }
        }
    }
}
