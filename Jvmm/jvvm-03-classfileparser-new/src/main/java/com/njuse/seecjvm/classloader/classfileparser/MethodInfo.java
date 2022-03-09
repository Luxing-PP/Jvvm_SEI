package com.njuse.seecjvm.classloader.classfileparser;

import com.njuse.seecjvm.classloader.classfileparser.attribute.AttributeInfo;
import com.njuse.seecjvm.classloader.classfileparser.attribute.CodeAttribute;
import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.seecjvm.classloader.classfileparser.constantpool.info.UTF8Info;

import java.nio.ByteBuffer;
import java.util.function.Supplier;

public class MethodInfo {
    private int accessFlags;
    private String name;
    private String descriptor;
    private AttributeInfo[] attributes;
    private CodeAttribute code;

    public MethodInfo(ConstantPool constantPool, Supplier<AttributeInfo> attributeBuilder, ByteBuffer in) {
        BuildUtil info = new BuildUtil(in);
        accessFlags = info.getU2();
        int nameIndex = info.getU2();
        int descriptorIndex = info.getU2();
        int attributesCount = info.getU2();
        attributes = new AttributeInfo[attributesCount];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = attributeBuilder.get();
        }
        this.name = ((UTF8Info) constantPool.get(nameIndex)).getString();
        this.descriptor = ((UTF8Info) constantPool.get(descriptorIndex)).getString();
    }

    public short getAccessFlags() {
        return (short) (accessFlags & 0xFFFF);
    }

    public String getName() {
        return name;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public CodeAttribute getCodeAttribute() {
        if (code == null) {
            for (AttributeInfo attribute : attributes) {
                if (attribute instanceof CodeAttribute) {
                    code = (CodeAttribute) attribute;
                    return code;
                }
            }
        }
        return code;
    }
}
