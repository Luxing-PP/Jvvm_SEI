package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.memory.JHeap;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;

public class NEW extends ReferenceInstruction {

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool=frame.getCurrentClass().getRuntimeConstantPool();
        Constant constant = runtimeConstantPool.getConstant(index);

        assert constant instanceof ClassRef;

        ClassRef classRef = (ClassRef)constant;
        //and the instance variables of the new object are initialized to their default initial values (§2.3, §2.4).


        JClass clazz=null;
        try {
            clazz =  classRef.getResolvedClass();
            if(classRef.getResolvedClass().isInterface||classRef.getResolvedClass().isAbstract){
                throw new InstantiationException();
            }

            if(makeClassInit(classRef.getResolvedClass(),frame)==false){
                return;
            }

            NonArrayObject ref = clazz.newObject();
            JHeap.getInstance().addObj(ref);
            stack.pushObjectRef(ref);

        }catch (Exception e){
            System.out.println("NonArrayObject mistake");
        }
    }
}
