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
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;


public class PUTFIELD extends Index16Instruction {
    /**
     * TODO 实现这条指令 费解没测试，找个同学的看看好了
     * 其中 对应的index已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool=frame.getMethod().getClazz().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);

        //must be not a array
        //FIXME If the field is protected, and it is a member of a superclass of the current class, and the field is not declared in the same run-time package (§5.3) as the current class, then the class of objectref must be either the current class or a subclass of the current class.
        //没有做错误输入判定

        Field field=null;
        try{
            field=fieldRef.getResolvedFieldRef();
            JClass targetClazz = field.getClazz();

            //貌似不用初始化
            if (targetClazz.getInitState() == InitState.PREPARED) {
                frame.setNextPC(frame.getNextPC() - 3);//opcode + operand = 3bytes
                targetClazz.initClass(frame.getThread(), targetClazz);
                return;
            }
        }catch (Exception e){
            System.out.println("Error!,FieldRef");
        }


        //接续descriptor 然后读值
        //省略了输入正确性的检测


        String descriptor = field.getDescriptor();
        int slotID = field.getSlotID();
        JObject thisRef;
        switch (descriptor.charAt(0)) {
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
            case 'I':
                //false=0?
                int intValue =  stack.popInt();
                thisRef = stack.popObjectRef();
                ((NonArrayObject)thisRef).getFields().setInt(slotID,intValue);
                break;
            case 'J':
                long longValue =  stack.popLong();
                thisRef = stack.popObjectRef();
                ((NonArrayObject)thisRef).getFields().setLong(slotID,longValue);
                break;
            case 'D':
                double doubleValue = stack.popDouble();
                thisRef = stack.popObjectRef();
                ((NonArrayObject)thisRef).getFields().setDouble(slotID,doubleValue);
                break;
            case 'F':
                float floatValue= stack.popFloat();
                thisRef = stack.popObjectRef();
                ((NonArrayObject)thisRef).getFields().setFloat(slotID,floatValue);
                break;
            default:
                System.out.println("Descriptor Skip refer to manual");
        }
    }
}
