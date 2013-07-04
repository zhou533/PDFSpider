package com.scipublish.PDFSpider.configuration;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chouchris
 * Date: 13-6-4
 * Time: PM2:44
 * To change this template use File | Settings | File Templates.
 */
public class Arguments {
    private static final Logger LOGGER = Logger.getLogger(Arguments.class);
    private List<Argument> argList = new ArrayList<Argument>();

    public void addArgument(Argument arg){
        argList.add(arg);
    }

    public int totalCount(){
        int count = 0;
        Iterator<Argument> iterator = argList.iterator();
        while (iterator.hasNext()){
            Argument arg = iterator.next();
            if (count == 0){
                count = arg.count();
            }else{
                count *= arg.count();
            }
        }
        return count;
    }

    public List<String> argListAtIndex(int index){
        List<String> result = new ArrayList<String>();
        /*Iterator<Argument> iterator = argList.iterator();
        while (iterator.hasNext()){
            Argument arg = iterator.next();
            String argStr = arg.argAtIndex(index);
            result.add(argStr);
        }*/
        LOGGER.info("argList:"+argList);
        for (int i = 0; i < argList.size(); i++){
            /*List<Argument> leftList = argList.size() - 1 - i > 0 ? argList.subList(i + 1, argList.size()-1) : null;
            LOGGER.info("" + i + " leftList:"+leftList);
            int leftCount = 1;
            if (leftList != null){
                Iterator<Argument> iterator = leftList.iterator();
                while (iterator.hasNext()){
                    leftCount *= iterator.next().count();
                }
            }*/
            int leftCount = 1;
            for (int j = i+1; j < argList.size(); j++){
                leftCount *= argList.get(j).count();
            }
            LOGGER.info("" + i + " leftCount:"+leftCount);
            int div = index/leftCount;
            String argStr = argList.get(i).argAtIndex(div);
            LOGGER.info("" + i + " argStr:"+argStr +" idx:"+div);
            result.add(argStr);

            index = index%leftCount;
        }

        return result;
    }
}
