package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;


public class GETFIELD extends Index16Instruction {

    /**
     * TODO 实现这条指令 费解没测试，找个同学的看看好了
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool=frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        NonArrayObject thisRef = (NonArrayObject) stack.popObjectRef();

        //popObjRef to find clazz
        //好像理解错了= = 文档是一样的
//        JClass targetClazz = stack.popObjectRef().getClazz();
        Field field=null;
        try{
            field=fieldRef.getResolvedFieldRef();
            JClass targetClazz = field.getClazz();
            if(field.isStatic()){
                throw new IncompatibleClassChangeError();
            }
            if (targetClazz.getInitState() == InitState.PREPARED) {
                frame.setNextPC(frame.getNextPC() - 3);//opcode + operand = 3bytes
                targetClazz.initClass(frame.getThread(), targetClazz);
                return;
            }
        }catch (Exception e){
            System.out.println("Error!,FieldRef");
        }

        if(thisRef.isNull()){
            throw new NullPointerException();
        }


        //接续descriptor 然后读值
        String descriptor = field.getDescriptor();
        int slotID = field.getSlotID();
        Vars fields = thisRef.getFields();
        switch (descriptor.charAt(0)) {
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
            case 'I':
                //false=0?
//                int intValue = ((IntWrapper)runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                int intValue = fields.getInt(slotID);
                stack.pushInt(intValue);
            case 'J':
                long longValue= fields.getLong(slotID);
                stack.pushLong(longValue);
            case 'D':
                double doubleValue = fields.getDouble(slotID);
                stack.pushDouble(doubleValue);
            case 'F':
                float floatValue = fields.getFloat(slotID);
                stack.pushFloat(floatValue);
            default:
                System.out.println("Descriptor Skip refer to manual");
        }
    }
}
