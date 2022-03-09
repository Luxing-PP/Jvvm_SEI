package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2D extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int originalValue = stack.popInt();
        double value = Double.valueOf(String.valueOf(originalValue));
        stack.pushDouble(value);
    }
}
