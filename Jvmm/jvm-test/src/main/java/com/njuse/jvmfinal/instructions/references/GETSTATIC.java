package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.FieldRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
//check
public class GETSTATIC extends ReferenceInstruction {
    @Override
    public void execute(StackFrame frame) {
        RuntimeConstantPool runtimeConstantPool = frame.getCurrentClass().getRuntimeConstantPool();
        FieldRef fieldRef = (FieldRef) runtimeConstantPool.getConstant(index);
        Field field;
        try {
            field = fieldRef.getResolvedFieldRef();
            JClass targetClazz = field.getClazz();

            //check class whether init
            if(makeClassInit(targetClazz,frame)==false){
                return;
            }

            if (!field.isStatic()) {
                throw new IncompatibleClassChangeError();
            }
            String descriptor = field.getDescriptor();
            int slotID = field.getSlotID();
            Vars staticVars = targetClazz.getStaticVars();
            OperandStack stack = frame.getOperandStack();
            switch (descriptor.charAt(0)) {
                case 'Z':
                case 'B':
                case 'C':
                case 'S':
                case 'I':
                    stack.pushInt(staticVars.getInt(slotID));
                    break;
                case 'F':
                    stack.pushFloat(staticVars.getFloat(slotID));
                    break;
                case 'J':
                    stack.pushLong(staticVars.getLong(slotID));
                    break;
                case 'D':
                    stack.pushDouble(staticVars.getDouble(slotID));
                    break;
                case 'L':
                case '[':
                    stack.pushObjectRef(staticVars.getObjectRef(slotID));
                    break;
                default:
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
