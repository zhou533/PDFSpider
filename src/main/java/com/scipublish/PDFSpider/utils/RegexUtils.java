package com.scipublish.PDFSpider.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-5
 * Time: AM10:07
 * To change this template use File | Settings | File Templates.
 */
public class RegexUtils {

    private static List<String> getMatcher(String regex, String source){
        List<String> result = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(source.toLowerCase());
        while (matcher.find()) {
            result.add(matcher.group());//只取第一组
        }
        return result;
    }

    public static List<String> getEmails(String source){
        return getMatcher("[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}", source);
    }
}
