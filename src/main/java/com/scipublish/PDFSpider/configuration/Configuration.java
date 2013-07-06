package com.scipublish.PDFSpider.configuration;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-7-4
 * Time: PM12:20
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {
    private static final Logger LOGGER = Logger.getLogger(Configuration.class);
    private static Configuration instance = new Configuration();

    private String url;
    private String savePath;
    private String taskName;
    private Arguments arguments = new Arguments();
    private Integer threadCount;
    private String timeSymbol;


    private Configuration(){
        this.url = null;
        this.savePath = null;
        this.taskName = "Undefined";
        this.threadCount = 2;
        timeSymbol = "0000_00_00_00_00_00";
    }

    /**
     *
     */
    public static Configuration getInstance(){
        return instance;
    }

    /**
     * getter
     */
    public String getSavePath() {
        return savePath;
    }

    public Arguments getArguments() {
        return arguments;
    }

    public String getTaskName() {
        return taskName;
    }

    public Integer getThreadCount() {
        return threadCount;
    }

    public String getUrl() {
        return url;
    }

    public String getTimeSymbol() {
        return timeSymbol;
    }

    /**
     *
     */
    public static boolean hasValidExt(String filename){
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex < 0){
            return false;
        }
        String ext = filename.substring(dotIndex);
        if (ext.equalsIgnoreCase(".xml")){
            return true;
        }
        return false;
    }

    /**
     *
     */
    public boolean load(String filename){
        boolean result = false;
        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_yyyyMMdd-HHmmss");
            timeSymbol = simpleDateFormat.format(date);

            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File(filename));

            Element config = doc.getRootElement();
            if (config == null){
                throw new Exception("invalid configuration file.");
            }

            Element taskUrlElement = config.element("task_url");
            if (taskUrlElement == null || taskUrlElement.getText() == null){
                throw new Exception("an available url is necessary.");
            }
            this.url = taskUrlElement.getText();
            if (!this.url.matches("[a-zA-z]+://[^\\s]*")){
                throw new Exception("an available url is necessary.");
            }

            Element taskArgListElement = config.element("task_arg_list");
            if (taskArgListElement == null){
                throw new Exception("please indicate task arguments.");
            }

            int argCount = getSubStringCount(this.url, "%s");
            List taskArgList = taskArgListElement.elements("task_arg");
            if (argCount != taskArgList.size()){
                throw new Exception("arguments amount error.");
            }

            Iterator<Element> iterator = taskArgList.iterator();
            while (iterator.hasNext()){
                Element taskArgElement = iterator.next();

                Attribute type = taskArgElement.attribute("type");
                if (type == null){
                    throw new Exception("invalid argument type..");
                }

                LOGGER.info(type.getValue() + ":" + taskArgElement.getText());
                if (type.getValue().equalsIgnoreCase("alpha")){
                    AlphaArgument alphaArgument = new AlphaArgument(taskArgElement.getText());
                    arguments.addArgument(alphaArgument);
                }else if (type.getValue().equalsIgnoreCase("number")){
                    NumberArgument numberArgument = new NumberArgument(taskArgElement.getText());
                    arguments.addArgument(numberArgument);
                }else {
                    throw new Exception("undefined argument type..");
                }
            }

            //
            Element taskNameElement = config.element("task_name");
            if (taskNameElement != null){
                taskName = taskNameElement.getText();
            }

            //
            Element taskSavedDirElement = config.element("task_saved_dir");
            if (taskSavedDirElement != null){
                savePath = taskSavedDirElement.getText();
            }

            //
            Element taskThreadCountElement = config.element("task_thread_count");
            if (taskThreadCountElement != null){
                threadCount = Integer.valueOf(taskThreadCountElement.getText());
            }

            result = true;
            LOGGER.info("Configuration loaded...");
        }catch (Exception e){
            LOGGER.error("configuration file parse failed:", e);
        }
        return result;
    }

    /*
    private
     */
    private int getSubStringCount(String destStr, String subString){
        int count = 0, idx = 0;
        while (idx < destStr.length() && (idx = destStr.indexOf(subString,idx)) >= 0){
            idx += subString.length();
            count++;
        }
        return count;
    }

}
