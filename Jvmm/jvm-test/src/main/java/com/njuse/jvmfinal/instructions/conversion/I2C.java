package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2C extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack stack =  frame.getOperandStack();
        int originalValue = stack.popInt();
        char value =  (char)originalValue;
        stack.pushInt((int) value);
    }
}
