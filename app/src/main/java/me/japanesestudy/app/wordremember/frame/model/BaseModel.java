package me.japanesestudy.app.wordremember.frame.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guyu on 2018/1/15.
 */

public class BaseModel<T> {
    protected List<T> cache;
    protected String lastMethod = "";
    public static String SPLIT_CHAR = "<split>";
    boolean isClear(){
        return cache == null || cache.isEmpty();
    }
    public BaseModel() {
        cache = new ArrayList<T>();
    }
    void clean(){
        if(cache != null)
            cache.clear();
    }
    void setMethod(Object...prams) {
        String methodName = getMethodName();
        for(Object obj : prams) {
            methodName = methodName + SPLIT_CHAR + prams.toString();
        }
        if(lastMethod.equals(methodName))
            return;
        else {
            clean();
            lastMethod = methodName;
        }
    }
    String getMethodName() {
        int aid = 0;
        for(int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            String className = Thread.currentThread().getStackTrace()[i].getClassName();
            try {
                Class cuClass = Class.forName(className);
                if(!cuClass.equals(BaseModel.class) && BaseModel.class.isAssignableFrom(cuClass)) {
                    aid = i;
                    break;
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(aid == 0) {
            aid = 0;
        }
        return Thread.currentThread().getStackTrace()[aid].getMethodName();
    }
}
