package com.njuse.seecjvm.classloader.classfileparser.constantpool.info;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;


public class ClassInfo extends ConstantPoolInfo {
    /**
     * Add some codes here.
     *
     * tips:
     * step1
     *      ClassInfo need a private field, what is it?
     * step2
     *      You need to add some args in constructor
     *      and don't forget to set tag
     *
     *      super method and super key word will help you
     */

    //todo attribute of ClassInfo


    private int nameIndex;

    //todo constructor of ClassInfo
    public ClassInfo(ConstantPool constantPool, int nameIndex){
        //1.初始化 2.查错 3.设置值 4.设置Tag
        super(constantPool);
        this.nameIndex=nameIndex;

        super.tag=ConstantPoolInfo.CLASS;
    }

    //todo getClassName
    public String getClassName() {
        /**
         * Add some codes here.
         * tips: classname is in UTF8Info
         */
        UTF8Info utf8Info = (UTF8Info) myCP.get(nameIndex);
        return utf8Info.getString();
    }

    @Override
    public String toString() {
        return getClassName();
    }
}
