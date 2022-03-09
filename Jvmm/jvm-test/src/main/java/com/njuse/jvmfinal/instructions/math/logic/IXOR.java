package com.njuse.jvmfinal.instructions.math.logic;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class IXOR extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();

        int val2 = stack.popInt();
        int val1 = stack.popInt();

        int ret = val1^val2;
        stack.pushInt(ret);
    }
}
