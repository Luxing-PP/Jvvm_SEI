package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2B extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int value = stack.popInt();
        byte ret = (byte)value;
//        int real_ret =  0xffffff00|ret;
        stack.pushInt(ret);
    }
}
