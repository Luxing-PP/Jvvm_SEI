package com.njuse.jvmfinal.memory;

import com.njuse.jvmfinal.memory.jclass.JClass;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class MethodArea {
    private static Map<String, JClass> classMap=new LinkedHashMap<>();
    public static JClass findClass(String className) {
        Iterator<String> iterator = classMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            if(key.equals(className)){
                return classMap.get(className);
            }
        }
        return null;
    }

    public static void addClass(String className, JClass clazz) {
        classMap.put(className, clazz);
    }
}
