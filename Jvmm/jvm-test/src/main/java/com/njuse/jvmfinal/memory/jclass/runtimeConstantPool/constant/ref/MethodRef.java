package com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref;

import com.njuse.jvmfinal.classloader.classfileparser.constantpool.info.MethodrefInfo;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

@Getter
@Setter
public class MethodRef extends MemberRef {
    private Method method;

    public MethodRef(RuntimeConstantPool runtimeConstantPool, MethodrefInfo methodrefInfo) {
        super(runtimeConstantPool, methodrefInfo);
    }

    public Method resolveMethodRef(JClass clazz){
        //貌似不考虑没找到
        method = lookUpMethod(name, descriptor, clazz);
        if (method == null) {
            System.out.println(toString()+"NotFind");
        }
        return method;
    }

    public Method resolveMethodRef(){
        //无法理解为什么这个东西在文档里但是，没有具体用处
        JClass D = runtimeConstantPool.getClazz();
        try{
            resolveClassRef();
        }catch (Exception e){
            System.out.println("ResolveClassRef Fail!");
        }
        //1.判断一
        if(clazz.isInterface){
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
        //有个很复杂的Abstract 功能但没测，懒得弄了
        JClass[] ifs=clazz.getInterfaces();
        Stack<JClass> interfaces = new Stack<>();
        ArrayList<Method> tempMethod = new ArrayList<>();

        interfaces.addAll(Arrays.asList(ifs));
        while (!interfaces.isEmpty()){
            JClass oneInterface = interfaces.pop();
            Method m = oneInterface.resolveMethod(name,descriptor);
            if(m!=null&&!m.isAbstract()&&!m.isStatic()&&!m.isPrivate()){
                return m;
            }else if(!m.isAbstract()){
                tempMethod.add(m);
            }
            interfaces.addAll(Arrays.asList(oneInterface.getInterfaces()));
        }

        if(tempMethod.size()==1){
            return tempMethod.get(0);
        }

        return null;
    }
}
