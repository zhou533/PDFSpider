package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.configuration.Configuration;
import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.model.StoreItem;
import com.scipublish.PDFSpider.service.HttpClientFactory;
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

            byte[] buffer = new byte[2048];
            HttpClient httpClient = HttpClientFactory.getInstance();
            HttpGet httpGet = new HttpGet(item.getUrl());
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                LOGGER.info("StatusCode:" + httpResponse.getStatusLine().getStatusCode());
                Header[] headers = httpResponse.getHeaders("Content-Disposition");
                if (headers != null && headers.length > 0){
                    for (Header header : headers){
                        if (header.getName().equalsIgnoreCase("Content-Disposition")){
                            if (header.getElements().length > 0 && header.getElements()[0].getName().equalsIgnoreCase("attachment")){
                                String filename = header.getElements()[0].getParameterByName("filename").getValue();
                                if (filename != null){
                                    LOGGER.info("filename:" + filename);
                                    item.setFilename(filename);
                                    break;
                                }
                            }
                        }
                    }
                }else {
                    String filename = item.getItemId() + ".pdf";
                    item.setFilename(filename);
                }


                HttpEntity httpEntity = httpResponse.getEntity();
                try {
                    LOGGER.info("Downloading file...");
                    InputStream inputStream = httpEntity.getContent();

                    String path = Configuration.getInstance().getSavePath() + "PDFs/";
                    File file = new File(path);
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    String filename = path + item.getFilename();
                    OutputStream outputStream = new FileOutputStream(filename);
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

            //parse pdf
            String allText = null;
            String fileFullName = Configuration.getInstance().getSavePath() + "PDFs/" + item.getFilename();
            try {
                allText = PDFUtils.getExtractedText(fileFullName);
            } catch (IOException e) {
                LOGGER.error("parse " + fileFullName + " failed..", e);
            }

            if (allText != null){
                List<String> emails = RegexUtils.getEmails(allText);
                if (emails != null && emails.size() > 0){
                    StoreItem storeItem = new StoreItem();
                    storeItem.setEmails(emails);
                    storeItem.setPrefix(item.getPrefix());
                    try {
                        LOGGER.info("Saving item:" + storeItem.toString());
                        ThreadCommon.ITEM_STORE_QUEUE.put(storeItem);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }

            }
        }
    }
}
