package com.njuse.seecjvm.runtime.struct;

import com.njuse.seecjvm.memory.jclass.JClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class JObject {
    protected static int numInHeap;
    protected int id;
    protected JClass clazz;
    protected boolean isNull = false;

    //官方的一种实现是 一个指向类型 一个指向方法表
    static {
        numInHeap = 0;
    }

    public JObject() {
        id = numInHeap;
    }

    public boolean isInstanceOf(JClass clazz) {
        return this.clazz.isAssignableFrom(clazz);
    }

}
