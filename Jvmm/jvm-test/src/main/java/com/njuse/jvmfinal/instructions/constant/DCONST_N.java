package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DCONST_N extends NoOperandsInstruction {
    private double val;
    private static double[] valid = {0.0,1.0};

    public DCONST_N(double val){
        if (val!=valid[0]&&val!=valid[1]) throw new IllegalArgumentException();
        this.val = val;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        stack.pushDouble(val);
    }


    @Override
    public String toString() {
        String suffix =""+val;
        return getClass().getSimpleName()+" :"+suffix;
    }
}
