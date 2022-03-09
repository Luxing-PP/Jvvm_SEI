package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


@Getter
public class UTF8Info extends ConstantPoolInfo {

    /**
     * Add some codes here.
     *
     * tips:
     * step1
     *      UTF8Info need some fields, what are they?
     * step2
     *      You need to add some args in constructor
     *      and don't forget to set tag
     *
     *      super method and super key word will help you
     *
     * step3
     *      The length of String is unknown for getConstantPoolInfo method
     *      How to return the instance with its length?
     *
     *      return a Pair<UTF8Info,Integer> or get the length of string in UTF8Info?
     *
     */
    //todo attributes of UTF8Info
    private int length;
    private byte[] bytes;

    //todo constructor of UTF8Info
    public UTF8Info(ConstantPool constantPool, int length, byte[] bytes){
        super(constantPool);
        this.length=length;
        this.bytes=bytes;

        super.tag=ConstantPoolInfo.UTF8;
    }

    /**
     * Add some codes here.
     * return the string of UTF8Info
     */
    //todo getInstance
    static Pair<UTF8Info, Integer> getInstance(ConstantPool constantPool, byte[] in, int offset) {
        return null;
    }

    //todo return string
    public String getString() {
        //系统自带方法
        return new String(bytes, StandardCharsets.UTF_8);
    }
    public String getMyString(){
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
