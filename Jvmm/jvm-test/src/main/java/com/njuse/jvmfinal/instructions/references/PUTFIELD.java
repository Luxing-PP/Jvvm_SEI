package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;
import com.njuse.jvmfinal.util.ColorUtil;

import java.util.UnknownFormatConversionException;

public class PUTFIELD extends ReferenceInstruction {

    //fixme 类生成还有一点点不解，别的OVER
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool=frame.getCurrentClass().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);

        //must be not a array
        //FIXME If the field is protected, and it is a member of a superclass of the current class, and the field is not declared in the same run-time package (§5.3) as the current class, then the class of objectref must be either the current class or a subclass of the current class.
        //没有做错误输入判定

        Field field=null;
        try{
            field=fieldRef.getResolvedFieldRef();
            JClass targetClazz = field.getClazz();

            //貌似不用初始化
            if(makeClassInit(targetClazz,frame)==false){
                return;
            }
        }catch (Exception e){
            System.out.println("Error!,FieldRef");
        }

        ColorUtil.printWhite(toString()+"Field : "+field.getName());

        //接续descriptor 然后读值
        //省略了输入正确性的检测


        assert field != null;
        String descriptor = field.getDescriptor();
        int slotID = field.getSlotID();

        JObject ref=null;
        switch (descriptor.charAt(0)) {
            case 'Z':
            case 'B':
            case 'C':
            case 'S':
            case 'I':
                //false=0?
                int intValue =  stack.popInt();
                ref=stack.popObjectRef();
                ((NonArrayObject)ref).getFields().setInt(slotID,intValue);
                break;
            case 'J':
                long longValue =  stack.popLong();
                ref=stack.popObjectRef();
                ((NonArrayObject)ref).getFields().setLong(slotID,longValue);
                break;
            case 'D':
                double doubleValue = stack.popDouble();
                ref=stack.popObjectRef();
                ((NonArrayObject)ref).getFields().setDouble(slotID,doubleValue);
                break;
            case 'F':
                float floatValue= stack.popFloat();
                ref=stack.popObjectRef();
                ((NonArrayObject)ref).getFields().setFloat(slotID,floatValue);
                break;
            case 'L':
                JObject refVal = stack.popObjectRef();
                ref=stack.popObjectRef();
                ((NonArrayObject)ref).getFields().setObjectRef(slotID,ref);
                break;
            case '[':
//                throw new UnknownFormatConversionException("MISTAKE IN PUTFIELD [");
                break;
            default:
                throw new UnknownFormatConversionException("MISTAKE IN PUTFIELD");
//                ColorUtil.printRed("Descriptor Skip refer to manual in"+this.toString());
        }
    }


}
