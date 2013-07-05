package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.model.StoreItem;
import org.apache.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-5
 * Time: AM10:35
 * To change this template use File | Settings | File Templates.
 */
public class StoreThread implements Runnable{

    private static final Logger LOGGER = Logger.getLogger(StoreThread.class);

    @Override
    public void run() {

        while (true){
            try {
                StoreItem item = ThreadCommon.ITEM_STORE_QUEUE.poll(20, TimeUnit.MINUTES);
                if (item == null && ThreadCommon.ITEMS_DOWNLOAD_END){
                    return;
                }
                if (item == null){
                    continue;
                }
                item.save();
                LOGGER.info("save item:" + item.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
