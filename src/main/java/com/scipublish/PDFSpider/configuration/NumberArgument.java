package com.scipublish.PDFSpider.configuration;

import com.sun.tools.javac.util.Pair;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-6-3
 * Time: PM5:51
 * To change this template use File | Settings | File Templates.
 */
public class NumberArgument extends Argument{

    public NumberArgument(String argString) throws Exception{
        super(argString);
    }


    @Override
    public int count() {
        int count = 0;
        Iterator<Pair<String, String>> iterator = args.iterator();
        while (iterator.hasNext()){
            Pair<String, String> arg = iterator.next();
            int from = Integer.parseInt(arg.fst);
            int to = Integer.parseInt(arg.snd);
            count += (to - from + 1);
        }

        return count;
    }

    @Override
    public String argAtIndex(int index) {
        Iterator<Pair<String, String>> iterator = args.iterator();
        while (iterator.hasNext()){
            Pair<String, String> arg = iterator.next();
            int from = Integer.parseInt(arg.fst);
            int to = Integer.parseInt(arg.snd);

            int dest = from + index;
            if (dest > to){
                //
                int len = to - from + 1;
                index -= len;
                continue;
            }
            return String.valueOf(dest);
        }
        return null;
    }

    @Override
    protected boolean isValidArgument(String arg) {
        return super.isValidArgument(arg);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
