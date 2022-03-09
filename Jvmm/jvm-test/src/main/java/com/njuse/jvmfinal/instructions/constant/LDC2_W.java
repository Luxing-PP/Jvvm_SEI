package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LDC2_W extends Index16Instruction {
    @Override
    public void execute(StackFrame frame) {
        // must be a run-time constant of type long or double (§5.1). The numeric value of that run-time constant is pushed onto the operand stack as a long or double, respectively.
        OperandStack stack = frame.getOperandStack();
        RuntimeConstantPool runtimeConstantPool = frame.getRuntimeConstantPool();
        Constant constant = runtimeConstantPool.getConstant(index);

        if(constant instanceof LongWrapper){
            long longValue = ((LongWrapper) constant).getValue();
            stack.pushLong(longValue);
        }else if(constant instanceof DoubleWrapper){
            double doubleValue = ((DoubleWrapper) constant).getValue();
            stack.pushDouble(doubleValue);
        }

        else throw new ClassFormatError();

    }
}
