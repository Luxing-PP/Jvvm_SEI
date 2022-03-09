package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.InterfaceMethodrefInfo;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Stack;

@Getter
@Setter
public class InterfaceMethodRef extends MemberRef {
    private Method method;

    public InterfaceMethodRef(RuntimeConstantPool runtimeConstantPool, InterfaceMethodrefInfo interfaceMethodrefInfo) {
        super(runtimeConstantPool, interfaceMethodrefInfo);
        //method
    }

    public Method resolveInterfaceMethodRef(JClass clazz) {
        this.method=resolve(clazz);
        return method;
    }

    public Method resolveInterfaceMethodRef() {
        try {
            resolveClassRef();
            this.method=resolve(clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return method;
    }

    public Method resolve(JClass clazz) {
        assert clazz != null;

        JClass currentClazz = clazz;
        Method method;
        while (currentClazz!=null){
            method = currentClazz.resolveMethod(name,descriptor);
            if(method!=null){
                return method;
            }
            currentClazz=currentClazz.getSuperClass();
        }
        //stack式满足快速添加和递归
        JClass[] ifs = clazz.getInterfaces();
        Stack<JClass> interfaces = new Stack<>();
        interfaces.addAll(Arrays.asList(ifs));
        while (!interfaces.isEmpty()) {
            JClass clz = interfaces.pop();
            method = clz.resolveMethod(name,descriptor);
            if(method!=null){
                return method;
            }
            interfaces.addAll(Arrays.asList(clz.getInterfaces()));
        }
        //mmm
        return null;
    }
}
