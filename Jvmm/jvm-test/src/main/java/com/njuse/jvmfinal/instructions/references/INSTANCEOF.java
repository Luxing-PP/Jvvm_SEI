package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;

public class INSTANCEOF extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool = frame.getRuntimeConstantPool();
        JObject objectRef = stack.popObjectRef();
        //reference to a class, array, or interface type
        Constant symbolicRef = runtimeConstantPool.getConstant(index);

        if(objectRef.isNull()){
            int result = 0;
            stack.pushInt(result);
        }


    }
}
