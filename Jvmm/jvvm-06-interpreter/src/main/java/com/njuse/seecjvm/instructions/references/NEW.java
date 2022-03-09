package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.JHeap;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;

public class NEW extends ReferenceInstruction {
    /**
     * TODO 实现这条指令 为什么那两个加起来是3呢0 0 index占两个坑？
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool=frame.getMethod().getClazz().getRuntimeConstantPool();
        ClassRef classRef = (ClassRef) runtimeConstantPool.getConstant(index);
        //FIXME The named class or interface type is resolved (§5.4.3.1) and should result in a class type
        //FIXME Memory for a new instance of that class is allocated from the garbage-collected heap, and the instance variables of the new object are initialized to their default initial values (§2.3, §2.4).
        //FIXME The objectref, a reference to the instance, is pushed onto the operand stack.
        //其实不确定是不是nonArray
        //这个newObject方法又是哪里来的
        JClass clazz=null;
        try {
            clazz =  classRef.getResolvedClass();
            if(classRef.getResolvedClass().isInterface()||classRef.getResolvedClass().isAbstract()){
                throw new InstantiationException();
            }

            if(isClassInit(classRef.getResolvedClass(),frame)==false){
                return;
            }

            NonArrayObject ref = clazz.newObject();
            JHeap.getInstance().addObj(ref);
            stack.pushObjectRef(ref);
        }catch (Exception e){
            System.out.println("NonArrayObject mistake");
        }





        //FIXME On successful resolution of the class, it is initialized (§5.5) if it has not already been initialized.
    }
}
