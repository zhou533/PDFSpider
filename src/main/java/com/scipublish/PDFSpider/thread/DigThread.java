package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.model.DigItem;
import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.service.HttpService;
import com.scipublish.PDFSpider.utils.CommonUtils;
import com.scipublish.PDFSpider.utils.RegexUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
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

            if (item == null && ThreadCommon.ITEMS_INPUT_END){
                ThreadCommon.ITEMS_DOWNLOAD_END = true;
                LOGGER.info("download queue is empty for 10m, so exit");
                return;
            }else if (item == null){
                continue;
            }

            byte[] bytes = HttpService.get(item.getUrl());
            if (bytes == null){
                continue;
            }

            List<String> pdfUrls = new ArrayList<String>();
            Document document = Jsoup.parse(new String(bytes));
            Elements elements = document.select("a");
            for (Element element : elements){
                String url = element.attr("href");

                if (RegexUtils.checkPDFUrl(url)){
                    if (CommonUtils.hasYearAfter1970(url)){
                        LOGGER.info("link:" + url);
                        pdfUrls.add(url);
                    }

                }else {
                    List<NameValuePair> params = URLEncodedUtils.parse(url, Charset.forName("UTF-8"));
                    if (params != null && params.size() > 0){
                        for (NameValuePair pair : params){
                            String value = pair.getValue();
                            if (value != null && RegexUtils.checkPDFUrl(value)){

                                if (CommonUtils.hasYearAfter1970(url)){
                                    LOGGER.info("link:" + value);
                                    pdfUrls.add(value);
                                }
                            }
                        }
                    }
                }
            }

            if (pdfUrls.size() > 0){
                Date startTime = new Date();

                for (String url : pdfUrls){
                    String itemId = CommonUtils.getFilenameByUrl(url);
                    DownloadItem downloadItem = new DownloadItem(url, itemId);

                    //
                    try {
                        CommonUtils.DownloadFile(downloadItem);
                    } catch (Exception e) {
                        LOGGER.info("Download failed:"+url,e);
                    }

                    //parse pdf
                    try {
                        CommonUtils.ParseFile(downloadItem);
                    } catch (Exception e) {
                        LOGGER.info("Parse failed:"+downloadItem.getFilename());
                    }
                }

                Date endTime = new Date();
                long interval = (endTime.getTime() - startTime.getTime());
                LOGGER.info("interval:"+(interval/1000)+"s");
                if (interval < 60 * 1000){
                    try {
                        LOGGER.info("dig thread sleep:"+(60 - interval/1000)+"s");
                        Thread.sleep(60 * 1000 - interval);
                    } catch (InterruptedException e) {


                    }
                }

            }else {
                try {
                    LOGGER.info("dig thread sleep:60s");
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {


                }
            }
        }
    }
}
