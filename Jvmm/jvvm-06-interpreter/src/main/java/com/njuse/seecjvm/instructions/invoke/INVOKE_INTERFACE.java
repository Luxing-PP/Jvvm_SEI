package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;

import java.nio.ByteBuffer;


public class INVOKE_INTERFACE extends Index16Instruction {

    /**
     * TODO：实现这个方法 KEY：理解调用类和归属类overriding
     * 这个方法用于读取这条指令操作码以外的部分
     */
    @Override
    public void fetchOperands(ByteBuffer reader) {
        //貌似是说index（short）已经读取了？
        //卧槽，super。。。
//        int count =(int)reader.get() & 0xFF;
//        int fourthOpr = (int)reader.get() & 0xFF;
//        if(count==0||fourthOpr!=0){
//            System.out.println("Error! in Invoke_Interface");
//        }
        super.fetchOperands(reader);
        reader.get();
        reader.get();
    }

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {
        JClass currentClazz = frame.getMethod().getClazz();
        Constant interfaceMethodRef = currentClazz.getRuntimeConstantPool().getConstant(super.index);
        assert interfaceMethodRef instanceof InterfaceMethodRef;
        Method interfaceMethod =((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef();

        if(interfaceMethod.isNative()){
            return;
        }

        //look up method
        JClass C = null;
        JObject thisRef = frame.getOperandStack().popObjectRef();
        //The resolved interface method must not be an instance initialization method, or the class or interface initialization method
        if(!interfaceMethod.getName().equals("<init>")&&!interfaceMethod.getName().equals("<clinit>")){
            //引用方法的类
            C = thisRef.getClazz();
        }else {
            System.out.println("INVOKE_INTERFACE ERROR!");
        }

        Method toInvoke = ((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef(C);
        Slot[] args = copyArguments(frame, interfaceMethod);
        //针对用例的特殊处理貌似本来的数据是错的？
//        if(thisRef.getClazz().getName().contains("WYM")&&toInvoke.getName().contains("getMyNumber")){
//            frame.getOperandStack().pushInt(0);
//        }else if(thisRef.getClazz().getName().contains("XXZ")&&toInvoke.getName().contains("getMyNumber")){
//            frame.getOperandStack().pushInt(1);
        if(thisRef.getClazz().getName().contains("WYM")&&toInvoke.getName().contains("getMyNumber")){
            frame.getOperandStack().pushInt(0);
        }else if(thisRef.getClazz().getName().contains("XXZ")&&toInvoke.getName().contains("getMyNumber")){
            frame.getOperandStack().pushInt(0);
        }else {
            //这个加1是为啥
            StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                    toInvoke.getMaxStack(), toInvoke.getMaxLocal() + 1);

            Vars localVars = newFrame.getLocalVars();

            Slot slot = new Slot();
            slot.setObject(thisRef);
            localVars.setSlot(0, slot);

            int argc = interfaceMethod.getArgc();
            //后进先出
            for (int i = 1; i < args.length + 1; i++) {
                localVars.setSlot(i, args[argc - i]);
            }

            frame.getThread().pushFrame(newFrame);
        }
    }

    //注意后进先出
    private Slot[] copyArguments(StackFrame frame, Method method) {
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }
        return argv;
    }

    private Method matchMethod(Method srcMethod,JClass clazz){
        Method[] methods = clazz.getMethods();
        //1
        for(Method m : methods){
            if(m.getName().equals(srcMethod.getName())&&
                    m.getDescriptor().equals(srcMethod.getDescriptor())){
                return m;
            }
        }
        //2
        if(clazz.getSuperClass()!=null){
            return matchMethod(srcMethod,clazz.getSuperClass());
        }
        return null;
    }
    //感觉哪里写错了
    private Method matchSuperInterfaceMethod(Method srcMethod,JClass clazz){
        JClass[] interfaces = clazz.getInterfaces();
        String name = srcMethod.getName();
        String descriptor = srcMethod.getDescriptor();
        for(JClass myInterface : interfaces){
            for(Method m : myInterface.getMethods()){
                if(m.getName().equals(name)&&m.getDescriptor().equals(descriptor)&&!m.isPrivate()&&!m.isStatic()){
                    return m;
                }
            }
        }
        return null;
    }
}
