package com.scipublish.PDFSpider.service;


import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParamBean;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-6-7
 * Time: AM10:06
 * To change this template use File | Settings | File Templates.
 */
public class HttpClientFactory {

    private static final String CHARSET = "UTF-8";
    private static DefaultHttpClient httpClient;
    private static final String[] USERAGENTS = {
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20130406 Firefox/23.0",
            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:18.0) Gecko/20100101 Firefox/18.0",
            "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533+ (KHTML, like Gecko) Element Browser 5.0",
            "IBM WebExplorer /v0.94", "Galaxy/1.0 [en] (Mac OS X 10.5.6; U; en)",
            "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
            "Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14",
            "Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25",
            "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1468.0 Safari/537.36",
            "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.0; Trident/5.0; TheWorld)"};

    static {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParamBean paramBean = new HttpProtocolParamBean(params);
        paramBean.setVersion(HttpVersion.HTTP_1_1);
        paramBean.setContentCharset(CHARSET);
        paramBean.setUserAgent(HttpClientFactory.randomUserAgent());
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

    static {

    }

    public static HttpClient getInstance(){
        return httpClient;
    }

    public static HttpClient getHttpsClient(){
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParamBean paramBean = new HttpProtocolParamBean(params);
            paramBean.setVersion(HttpVersion.HTTP_1_1);
            paramBean.setContentCharset(CHARSET);
            paramBean.setUserAgent(HttpClientFactory.randomUserAgent());
            paramBean.setUseExpectContinue(true);
            params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 300000);
            params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 300000);

            //HttpHost proxy = new HttpHost("127.0.0.1", 8087);
            //params.setParameter(ConnRoutePNames.DEFAULT_PROXY,proxy);

            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
            registry.register(new Scheme("https", 443, ssf));
            PoolingClientConnectionManager mgr = new PoolingClientConnectionManager(registry);
            return new DefaultHttpClient(mgr, params);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public HttpClientFactory() {
    }

    private static String randomUserAgent(){
        Random random = new Random();
        int idx = random.nextInt(USERAGENTS.length);
        return USERAGENTS[idx];
    }
}
