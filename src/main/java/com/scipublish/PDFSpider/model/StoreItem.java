package com.scipublish.PDFSpider.model;

import au.com.bytecode.opencsv.CSVWriter;
import com.scipublish.PDFSpider.configuration.Configuration;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-5
 * Time: AM10:24
 * To change this template use File | Settings | File Templates.
 */
public class StoreItem {
    private static final Logger LOGGER = Logger.getLogger(StoreItem.class);

    private List<String> emails;
    private String prefix;

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void save(){
        String pathName = Configuration.getInstance().getSavePath();
        File path = new File(pathName);
        if (!path.exists()){
            LOGGER.info("dir does not exist:" + pathName);
            path.mkdirs();
        }

        String fileName = pathName +
                Configuration.getInstance().getTaskName()+
                Configuration.getInstance().getTimeSymbol() + ".csv";

        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file, true);
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            for (String e : this.emails){
                String[] strs = new String[2];
                strs[0] = (this.prefix == null) ? "" : this.prefix;
                strs[1] = (e == null) ? "" : e;
                csvWriter.writeNext(strs);
            }

            csvWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {

        return this.prefix + StringUtils.join(this.emails,";");
    }
}
