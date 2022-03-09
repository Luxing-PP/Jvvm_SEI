package com.njuse.jvmfinal.runtime.struct;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Slot {
    private JObject object;
    private Integer value;


    //fixme 对象的引用怎么实现好呢？
    public Slot clone() {
        Slot toClone = new Slot();
        toClone.object = this.object;
        toClone.value = this.value;
        return toClone;
    }
}
