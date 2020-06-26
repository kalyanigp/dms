package com.ecomm.define.controller;

/**
 * Created by vamshikirangullapelly on 27/05/2020.
 */
public class Test {
    public Object createObject(String classname)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        Object obj = null;

        Class theclass = Class.forName(classname);

        obj = theclass.newInstance();

        return obj;
    }
}
