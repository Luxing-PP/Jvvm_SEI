package com.njuse.jvmfinal.instructions.conversion;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class I2S extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int originalValue = stack.popInt();
        short value = (short) originalValue;
        int real_value = 0xffff0000 | value;
        stack.pushInt(real_value);
    }
}
