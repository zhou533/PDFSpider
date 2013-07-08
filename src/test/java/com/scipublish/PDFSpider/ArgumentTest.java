package com.scipublish.PDFSpider;

import com.scipublish.PDFSpider.configuration.NumberArgument;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-8
 * Time: AM11:25
 * To change this template use File | Settings | File Templates.
 */
public class ArgumentTest {
    @Test
    public void testArgumentNumber() throws Exception {


        String numArg1 = "12-24";
        NumberArgument arg1 = new NumberArgument(numArg1);

        String numArg2 = "12-34,45-78";
        NumberArgument arg2 = new NumberArgument(numArg2);

        System.out.println("numArg1 '" + numArg1 + "' count:" + arg1.count());
        System.out.println("numArg2 '" + numArg2 + "' count:" + arg2.count());

        String numArg3 = "1-3,5-6";
        NumberArgument arg3 = new NumberArgument(numArg3, "100");
        int count = arg3.count();
        System.out.println("numArg3 '" + numArg3 + "' count:" + count);
        for (int i = 0; i < count; i++){
            System.out.println("numArg3 index:" + i + " value:" + arg3.argAtIndex(i));
        }
    }
}
