package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;
import com.njuse.jvmfinal.util.ColorUtil;

public class GETFIELD extends ReferenceInstruction {

    @Override
    public void execute(StackFrame frame) {
        //Not Array
        /*
        If the field is protected, and it is a member of a superclass of the current class, and the field is not declared in the same run-time package (§5.3) as the current class, then the class of objectref must be either the current class or a subclass of the current class.
         */
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool=frame.getCurrentClass().getRuntimeConstantPool();
        Constant constant = runtimeConstantPool.getConstant(index);
        assert constant instanceof FieldRef;

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
            if(makeClassInit(targetClazz,frame)==false){
                return;
            }
        }catch (Exception e){
            throw new RuntimeException();
        }

        if(thisRef.isNull()){
            throw new NullPointerException();
        }

        ColorUtil.printWhite(toString()+"Field : "+field.getName());

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
                int intValue = fields.getInt(slotID);
                stack.pushInt(intValue);
                break;
            case 'J':
                long longValue= fields.getLong(slotID);
                stack.pushLong(longValue);
                break;
            case 'D':
                double doubleValue = fields.getDouble(slotID);
                stack.pushDouble(doubleValue);
                break;
            case 'F':
                float floatValue = fields.getFloat(slotID);
                stack.pushFloat(floatValue);
                break;
            case 'L':
            case '[':
                JObject ref = fields.getObjectRef(slotID);
                stack.pushObjectRef(ref);
                break;
            default:
                System.out.println("Descriptor Skip refer to manual");
        }
    }
}
