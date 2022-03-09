package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class LCONST_N extends NoOperandsInstruction {
    private long val;
    private static long[] valid = {0, 1};

    public LCONST_N(long val) {
        if (val!=valid[0]&&val!=valid[1]) throw new IllegalArgumentException();
        this.val = val;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        stack.pushLong(val);
    }

    @Override
    public String toString() {
        String suffix = ""+val;
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 1) + suffix;
    }
}
