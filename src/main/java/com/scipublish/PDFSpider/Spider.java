package com.scipublish.PDFSpider;

import com.scipublish.PDFSpider.configuration.Configuration;
import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.thread.DownloadThread;
import com.scipublish.PDFSpider.thread.ThreadCommon;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-3
 * Time: PM6:22
 * To change this template use File | Settings | File Templates.
 */
public class Spider {

    private static final Logger LOGGER = Logger.getLogger(Spider.class);

    public static void main(String[] args){
        if (args == null || args.length == 0){
            LOGGER.info("Please indicate configuration file.");
            return;
        }

        String cfgFilename = args[0];
        if (cfgFilename == null || !Configuration.hasValidExt(cfgFilename)){
            LOGGER.info("Invalid configuration file, please check it out.");
            return;
        }

        LOGGER.info("Loading configuration file: " + cfgFilename);

        Configuration configuration = Configuration.getInstance();
        if (!configuration.load(cfgFilename)){
            LOGGER.info("Load configuration file failed!! Please check it out.");
            return;
        }

        //init download threads
        for (int i = 0; i < configuration.getThreadCount(); i++){
            Thread downloadThread = new Thread(new DownloadThread());
            downloadThread.start();
        }

        //prepare all urls which need to crawl
        int urlCount = configuration.getArguments().totalCount();
        for (int i = 0; i < urlCount; i++){
            List<String> argList = configuration.getArguments().argListAtIndex(i);
            String url = configuration.getUrl();

            StringBuilder crawlUrl = new StringBuilder();
            int idx = 0, loc = 0, argIdx = 0;
            while (idx < url.length() && (loc = url.indexOf("%s",idx)) >= 0){
                if (loc > idx)
                    crawlUrl.append(url.substring(idx,loc));
                crawlUrl.append(argList.get(argIdx));
                argIdx++;
                idx = loc;
                idx += "%s".length();
            }
            if (idx < url.length()){
                crawlUrl.append(url.substring(idx, url.length()));
            }

            //
            DownloadItem item = new DownloadItem(crawlUrl.toString());
            try {
                ThreadCommon.ITEM_DOWNLOAD_QUEUE.put(item);
            }catch (InterruptedException e){
                e.printStackTrace();;
            }

        }

        //
        ThreadCommon.ITEMS_INPUT_END = true;
    }
}
