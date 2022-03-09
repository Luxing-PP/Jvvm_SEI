package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2F extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int originalValue = stack.popInt();
        float value = Float.valueOf(String.valueOf(originalValue));
        stack.pushFloat(value);
    }
}
