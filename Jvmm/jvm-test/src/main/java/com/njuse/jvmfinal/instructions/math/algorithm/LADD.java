package com.njuse.jvmfinal.instructions.math.algorithm;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LADD extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();

        long val2 = stack.popLong();
        long val1 = stack.popLong();

        long result =val1+val2;
        stack.pushLong(result);
    }
}
