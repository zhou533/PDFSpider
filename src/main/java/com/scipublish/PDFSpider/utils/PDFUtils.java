package com.scipublish.PDFSpider.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-5
 * Time: AM10:06
 * To change this template use File | Settings | File Templates.
 */
public class PDFUtils {

    public static String getExtractedText(String fileName) throws IOException {
        PDDocument doc = null;
        String text;
        try{
            doc = PDDocument.load(fileName);
            PDFTextStripper stripper = new PDFTextStripper();
            text = stripper.getText(doc);
        }
        finally {
            if (doc != null){
                doc.close();
            }
        }
        return text;
    }
}
