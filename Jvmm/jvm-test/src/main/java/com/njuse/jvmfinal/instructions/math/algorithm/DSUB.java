package com.njuse.jvmfinal.instructions.math.algorithm;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DSUB extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();

        double val2 = stack.popDouble();
        double val1 = stack.popDouble();

        double result =val1-val2;
        stack.pushDouble(result);
    }
}
