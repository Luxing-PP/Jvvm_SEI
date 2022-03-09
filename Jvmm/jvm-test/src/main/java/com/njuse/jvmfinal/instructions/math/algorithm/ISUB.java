package com.njuse.jvmfinal.instructions.math.algorithm;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ISUB extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {

        OperandStack stack = frame.getOperandStack();

        int val2 = stack.popInt();

        int val1 = stack.popInt();

        int res = val1 - val2;

        stack.pushInt(res);
    }
}
