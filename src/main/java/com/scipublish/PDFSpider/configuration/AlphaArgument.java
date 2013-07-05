package com.scipublish.PDFSpider.configuration;

import com.scipublish.PDFSpider.utils.Pair;
import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-6-3
 * Time: PM5:47
 * To change this template use File | Settings | File Templates.
 */
public class AlphaArgument extends Argument{
    private static final Logger LOGGER = Logger.getLogger(AlphaArgument.class);

    public AlphaArgument(String argString) throws Exception{
        super(argString);
    }


    @Override
    public int count() {
        int count = 0;
        Iterator<Pair> iterator = args.iterator();
        while (iterator.hasNext()){
            Pair arg = iterator.next();
            String from = arg.getName();
            String to = arg.getValue();
            count += offset(from, to);
        }
        return count;
    }

    @Override
    public String argAtIndex(int index) {
        Iterator<Pair> iterator = args.iterator();
        while (iterator.hasNext()){
            Pair arg = iterator.next();
            String from = arg.getName();
            String to = arg.getValue();
            int count = offset(from, to);
            if (index >= count){
                index -= count;
                continue;
            }

            //

            Integer[] offsets = new Integer[from.length()];
            for (int i = 0, idx = from.length()-1; i < from.length(); i++,idx--){
                int b = ((int)Math.pow(26,idx));
                int div = index/b;

                //char fchar = from.charAt(i);
                //char tchar = to.charAt(i);
                //
                offsets[i] = div;


                index = index%b;

            }


            StringBuilder strBuild = new StringBuilder();
            for (int i = from.length()-1; i >= 0; i--){
                char fchar = from.charAt(i);
                char baseChar = isLowerCase(fchar) ? 'a' : 'A';
                int foffset = fchar - baseChar;
                if ((offsets[i] + foffset) >= 26){
                    int offset = (offsets[i] + foffset) - 26;
                    strBuild.append((char) (offset + baseChar));
                    if ((i-1) >= 0){
                        offsets[i-1] += 1;
                    }else {
                        strBuild.append(baseChar);
                    }
                }else {
                    strBuild.append((char) (baseChar + offsets[i] + foffset));
                }
            }
            return strBuild.reverse().toString();
        }
        return null;
    }

    @Override
    protected boolean isValidArgument(String arg) {
        if (!super.isValidArgument(arg)) {
            return false;
        }

        String[] values = arg.split("-");
        String from = values[0];
        String to = (values.length == 2) ? values[1] : values[0];
        if (from.length() != to.length()){
            return false;
        }

        int count = from.length();
        for (int i = 0; i < count; i++){
            char fchar = from.charAt(i);
            char tchar = to.charAt(i);
            if ((!isUpperCase(fchar) && !isLowerCase(fchar)) ||
                    (!isUpperCase(tchar) && !isLowerCase(tchar))){
                return false;
            }

            if ((isUpperCase(fchar) && isLowerCase(tchar)) ||
                    (isLowerCase(fchar)) && isUpperCase(tchar)){
                return false;
            }

            /*if (fchar > tchar && i > 0 && from.charAt(i-1) >= to.charAt(i-1)){
                return false;
            }*/
        }

        if (compare(from, to) < 0){
            return false;
        }

        LOGGER.info("valid arg:" + arg);
        return true;
    }

    /*
    private
    */


    /*
    global
     */
    private static boolean isUpperCase(char c){return (c >= 65 && c <= 90);}
    private static boolean isLowerCase(char c){return (c >= 97 && c <= 122);}

    private static int compare(String arg1, String arg2){
        if (arg1.length() > arg2.length()){
            return -1;
        }else if (arg1.length() < arg2.length()){
            return 1;
        }

        for (int i = 0; i < arg1.length(); i++){
            char c1 = arg1.charAt(i);
            char c2 = arg2.charAt(i);
            if (c1 > c2)
                return -1;
            else if (c1 < c1)
                return 1;
        }

        return 0;
    }

    private static int digitalize(String arg){
        int result = 0;
        for (int idx = arg.length()-1, i = 0; idx >=0; idx--,i++){
            char c = arg.charAt(idx);
            if (isUpperCase(c)){
                c -= 32;
            }

            int tmp = c - 64;

            result += (tmp * Math.pow(26,i));
        }
        return result;
    }
    public static int offset(String from, String to){
        return digitalize(to) - digitalize(from) + 1;
    }

}
