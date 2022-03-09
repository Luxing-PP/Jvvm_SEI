package com.njuse.jvmfinal.memory;

import com.njuse.jvmfinal.runtime.struct.JObject;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class JHeap {
    private static JHeap jHeap = new JHeap();
    private static Set<JObject> objects;
    private static int maxSize = 50;
    private static int currentSize = 0;
//    private static Map<Integer, Boolean> objectState;//true to present object is new added

    public static JHeap getInstance() {
        return jHeap;
    }

    private JHeap() {
        objects = new LinkedHashSet<>();
//        objectState = new LinkedHashMap<>();
    }

    public void addObj(JObject obj) {
        if (currentSize >= maxSize) throw new OutOfMemoryError();
        objects.add(obj);
//        objectState.put(obj.getId(), true);
        currentSize++;
    }

//    public Set<JObject> getObjects() {
//        return objects;
//    }

//    public static Map<Integer, Boolean> getObjectState() {
//        return objectState;
//    }

//    public static void reset() {
//        objects = new LinkedHashSet<>();
//        objectState = new LinkedHashMap<>();
//    }
}

