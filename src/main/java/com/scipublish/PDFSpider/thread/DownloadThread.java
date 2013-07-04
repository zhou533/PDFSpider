package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.service.HttpClientFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
                LOGGER.info("download queue is empty for 10m, so exit");
                return;
            }else if (item == null){
                continue;
            }

            byte[] buffer = new byte[2048];
            HttpClient httpClient = HttpClientFactory.getInstance();
            HttpGet httpGet = new HttpGet(item.getUrl());
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                LOGGER.info("StatusCode:" + httpResponse.getStatusLine().getStatusCode());
                //HttpParams httpParams = httpResponse.getParams();
                //String contentType = httpParams.getParameter("");
                HttpEntity httpEntity = httpResponse.getEntity();

                try {
                    LOGGER.info("Downloading file...");
                    InputStream inputStream = httpEntity.getContent();
                    OutputStream outputStream = new FileOutputStream("/SCIPublish/PDFs/Demo.pdf");
                    for (int length; (length = inputStream.read(buffer)) > 0;) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                    LOGGER.info("save " + item.getUrl() + " done");
                }catch (Exception e){
                    LOGGER.error("save " + item.getUrl() + " error", e);
                }

            } catch (ClientProtocolException e) {
                LOGGER.error("get " + item.getUrl() + " error", e);
            } catch (IOException e) {
                LOGGER.error("get " + item.getUrl() + " error", e);
            }finally {
                httpGet.releaseConnection();
            }


        }
    }
}
