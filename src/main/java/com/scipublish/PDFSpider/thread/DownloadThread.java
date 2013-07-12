package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.configuration.Configuration;
import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.model.StoreItem;
import com.scipublish.PDFSpider.service.HttpClientFactory;
import com.scipublish.PDFSpider.utils.CommonUtils;
import com.scipublish.PDFSpider.utils.PDFUtils;
import com.scipublish.PDFSpider.utils.RegexUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-4
 * Time: PM12:48
 * To change this template use File | Settings | File Templates.
 */
public class DownloadThread implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(DownloadThread.class);

    @Override
    public void run() {

        while (true){
            DownloadItem item = null;
            try {
                LOGGER.debug(ThreadCommon.ITEM_DOWNLOAD_QUEUE.size());
                item = ThreadCommon.ITEM_DOWNLOAD_QUEUE.poll(10, TimeUnit.MINUTES);
            }catch (InterruptedException ie){
                LOGGER.error(ie);
            }

            if (item == null && ThreadCommon.ITEMS_INPUT_END){
                ThreadCommon.ITEMS_DOWNLOAD_END = true;
                LOGGER.info("download queue is empty for 10m, so exit");
                return;
            }else if (item == null){
                continue;
            }

            //
            try {
                CommonUtils.DownloadFile(item);
            } catch (Exception e) {

            }

            //parse pdf
            try {
                CommonUtils.ParseFile(item);
            } catch (Exception e) {

            }
        }
    }
}
