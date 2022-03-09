package com.njuse.seecjvm.classloader.classfileparser;

import com.njuse.seecjvm.classloader.classfileparser.attribute.AttributeInfo;
import com.njuse.seecjvm.classloader.classfileparser.attribute.ConstantValueAttr;
import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.seecjvm.classloader.classfileparser.constantpool.info.UTF8Info;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class FieldInfo {
    private short accessFlags;
    private String name;
    private String descriptor;
    private AttributeInfo[] attributes;

    public FieldInfo(ConstantPool constantPool, Supplier<AttributeInfo> attributeBuilder, ByteBuffer in) {
        this.accessFlags = in.getShort();
        short nameIndex = in.getShort();
        short descriptorIndex = in.getShort();
        short attributesCount = in.getShort();
        this.attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributesCount; i++) {
            this.attributes[i] = attributeBuilder.get();
        }
        this.name = ((UTF8Info) constantPool.get(nameIndex)).getString();
        this.descriptor = ((UTF8Info) constantPool.get(descriptorIndex)).getString();

    }

    public short getAccessFlags() {
        return accessFlags;
    }


    public String getName() {
        return name;
    }


    public String getDescriptor() {
        return descriptor;
    }

}
