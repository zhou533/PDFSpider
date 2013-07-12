package com.scipublish.PDFSpider;

import com.scipublish.PDFSpider.utils.CommonUtils;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-12
 * Time: PM4:31
 * To change this template use File | Settings | File Templates.
 */
public class UtilsTest {
    @Test
    public void testHasYearAfter1970() throws Exception {
        String url = "http://downloads.hindawi.com/journals/psyche/1900/063514.pdf";
        if (CommonUtils.hasYearAfter1970(url)){
            System.out.println("yes");
        }else {
            System.out.println("no");
        }
    }
}
