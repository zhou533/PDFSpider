package com.scipublish.PDFSpider.service;


import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-6-7
 * Time: AM10:06
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientFactory {

    private static final String USERAGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/27.0.1453.94 Safari/537.36";
    private static final String CHARSET = "UTF-8";
    private static DefaultHttpClient httpClient;

    static {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParamBean paramBean = new HttpProtocolParamBean(params);
        paramBean.setVersion(HttpVersion.HTTP_1_1);
        paramBean.setContentCharset(CHARSET);
        paramBean.setUserAgent(USERAGENT);
        paramBean.setUseExpectContinue(true);
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        PoolingClientConnectionManager cm = new PoolingClientConnectionManager(schemeRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(200);

        httpClient = new DefaultHttpClient(cm, params);
    }

    public static HttpClient getInstance(){
        return httpClient;
    }

    public HttpClientFactory() {
    }
}
