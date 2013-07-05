package com.scipublish.PDFSpider.utils;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-5
 * Time: PM6:41
 * To change this template use File | Settings | File Templates.
 */
public class Pair {
    private String name;
    private String value;

    public Pair(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
