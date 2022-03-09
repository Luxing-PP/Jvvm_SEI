package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class FCONST_N extends NoOperandsInstruction {
    private float val;
    private static float[] valid = {0,1,2};

    public FCONST_N(float val) {
        if (val!=valid[0]&&val!=valid[1]&&val!=valid[2]) throw new IllegalArgumentException();
        this.val = val;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        stack.pushFloat(val);
    }

    @Override
    public String toString() {
        String suffix ="" + val;
        String simpleName = this.getClass().getSimpleName();
        return simpleName.substring(0, simpleName.length() - 1) + suffix;
    }
}
