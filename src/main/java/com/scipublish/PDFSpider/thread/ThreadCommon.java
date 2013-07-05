package com.scipublish.PDFSpider.thread;

import com.scipublish.PDFSpider.model.DownloadItem;
import com.scipublish.PDFSpider.model.StoreItem;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-4
 * Time: PM12:51
 * To change this template use File | Settings | File Templates.
 */
public class ThreadCommon {
    public static final BlockingDeque<DownloadItem> ITEM_DOWNLOAD_QUEUE = new LinkedBlockingDeque<DownloadItem>(100);
    public static final BlockingDeque<StoreItem> ITEM_STORE_QUEUE = new LinkedBlockingDeque<StoreItem>(100);

    public static volatile boolean ITEMS_INPUT_END = false;
    public static volatile boolean ITEMS_DOWNLOAD_END = false;
}
