package com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.seecjvm.classloader.classfileparser.constantpool.info.MethodrefInfo;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Stack;

@Getter
@Setter
public class MethodRef extends MemberRef {
    private Method method;

    public MethodRef(RuntimeConstantPool runtimeConstantPool, MethodrefInfo methodrefInfo) {
        super(runtimeConstantPool, methodrefInfo);
}

    /**
     * TODO：实现这个方法
     * 这个方法用来实现对象方法的动态查找
     * @param clazz 对象的引用
     */
    public Method resolveMethodRef(JClass clazz){
        //貌似不考虑没找到
        method = lookUpMethod(name, descriptor, clazz);
        if (method == null) {
            System.out.println(toString()+"NotFind");
        }
        return method;
    }

    /**
     * TODO: 实现这个方法
     * 这个方法用来解析methodRef对应的方法
     * 与上面的动态查找相比，这里的查找始终是从这个Ref对应的class开始查找的
     */
    public Method resolveMethodRef(){
        //无法理解为什么这个东西在文档里但是，没有具体用处
        JClass D = runtimeConstantPool.getClazz();
        try{
            resolveClassRef();
        }catch (Exception e){
            System.out.println("ResolveClassRef Fail!");
        }
        //1.判断一
        if(clazz.isInterface()){
            System.out.println("IncompatibleClassChangeError.");
        }
        return resolveMethodRef(clazz);
    }

    private Method lookUpMethod(String name, String descriptor, JClass clazz){
        method = lookUpSuperClazz(name,descriptor,clazz);
        if(method != null)return method;

        method = lookUpSuperInterface(name,descriptor,clazz);
        if(method != null)return method;

        return null;
    }
    private Method lookUpSuperClazz(String name, String descriptor, JClass clazz){
        //漏了第一句，不懂什么意思，同态？
        for(Method m:clazz.getMethods()){
            if(m.getDescriptor().equals(descriptor)&&m.getName().equals(name)){
                return m;
            }
        }
        if (clazz.getSuperClass() != null) {
            return lookUpSuperClazz(name, descriptor, clazz.getSuperClass());
        }
        return null;
    }
    private Method lookUpSuperInterface(String name, String descriptor, JClass clazz){
        //fixme 用了全新stack 来实现快速添加（慢一点也可以用ArrayList）
        JClass[] ifs=clazz.getInterfaces();
        Stack<JClass> interfaces = new Stack<>();
        interfaces.addAll(Arrays.asList(ifs));
        while (!interfaces.isEmpty()){
            JClass oneInterface = interfaces.pop();
            if(oneInterface.resolveMethod(name,descriptor).isPresent()){
                return oneInterface.resolveMethod(name,descriptor).get();
            }
            interfaces.addAll(Arrays.asList(oneInterface.getInterfaces()));
        }
//        for(JClass myInterface : ifs){
//            for(Method m : myInterface.getMethods()){
//                if(m.getDescriptor().equals(descriptor)&&m.getName().equals(name)){
//                      todo 理论上有个Abstract标记的检查，但不知道在哪？
//                    if(!m.isPrivate()&&!m.isStatic()){
//                        return m;
//                    }
//                }
//            }
//        }
        return null;
    }

    @Override
    public String toString() {
        return "MethodRef to " + className;
    }
}
