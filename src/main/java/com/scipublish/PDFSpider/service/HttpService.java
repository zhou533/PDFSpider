package com.scipublish.PDFSpider.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-6-7
 * Time: AM10:19
 * To change this template use File | Settings | File Templates.
 */
public class HttpService {
    private static final Logger LOGGER = Logger.getLogger(HttpService.class);

    public HttpService() {
    }

    /*

     */
    public static byte[] get(String uri){
        LOGGER.debug("get: " + uri);
        HttpClient httpClient;
        if (uri.startsWith("https")){
            httpClient = HttpClientFactory.getHttpsClient();
        }else {
            httpClient = HttpClientFactory.getInstance();
        }
        HttpGet httpGet = new HttpGet(uri);
        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            byte[] bytes = EntityUtils.toByteArray(httpEntity);
            return bytes;
        }catch (ClientProtocolException e){
            LOGGER.error("get " + uri + " error", e);
        }catch (IOException e){
            LOGGER.error("get " + uri + " error", e);
        }finally {
            httpGet.releaseConnection();
        }

        return null;
    }
}
