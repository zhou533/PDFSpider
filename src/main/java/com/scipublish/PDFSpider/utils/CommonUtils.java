package com.scipublish.PDFSpider.utils;

import com.scipublish.PDFSpider.configuration.Configuration;
import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.model.StoreItem;
import com.scipublish.PDFSpider.service.HttpClientFactory;
import com.scipublish.PDFSpider.thread.ThreadCommon;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-12
 * Time: AM10:17
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtils {

    private static final Logger LOGGER = Logger.getLogger(CommonUtils.class);

    public static void DownloadFile(DownloadItem item) throws Exception{
//
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
                        if (header.getElements().length > 0 &&
                                header.getElements()[0].getName().equalsIgnoreCase("attachment") &&
                                header.getElements()[0].getParameterByName("filename") != null){
                            String filename = header.getElements()[0].getParameterByName("filename").getValue();
                            if (filename != null){
                                LOGGER.info("filename:" + filename);
                                item.setFilename(filename);
                                break;
                            }
                        }
                    }
                }
            }
            if (item.getFilename() == null){
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
                LOGGER.info("save " + item.getUrl() + " error", e);
                throw e;
            }

        } catch (ClientProtocolException e) {
            LOGGER.info("get " + item.getUrl() + " error", e);
            throw e;
        } catch (IOException e) {
            LOGGER.info("get " + item.getUrl() + " error", e);
            throw e;
        }finally {
            httpGet.releaseConnection();
        }
    }

    public static void ParseFile(DownloadItem item) throws Exception{
        String allText = null;
        String fileFullName = Configuration.getInstance().getSavePath() + "PDFs/" + item.getFilename();
        try {
            allText = PDFUtils.getExtractedText(fileFullName);
        } catch (IOException e) {
            LOGGER.error("parse " + fileFullName + " failed..", e);
            throw e;
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

                    throw e;
                }
            }

        }
    }

    public static String getFilenameByUrl(String url){
        String name = null;
        try {
            URL u = new URL(url);
            name = StringUtils.replaceChars(u.getPath(), '/', '_');

        } catch (MalformedURLException e) {

        }finally {
            return name;
        }
    }

    public static boolean hasYearAfter1970(String url){
        List<String> years = RegexUtils.getMatcher("/\\d{3,4}/", url);
        if (years.size() > 0 && years.get(0) != null){
            String year = StringUtils.remove(years.get(0), '/');
            if (year != null){
                Integer y = Integer.valueOf(year);
                if (y < 1970){
                    return false;
                }
            }
        }

        return true;
    }
}
