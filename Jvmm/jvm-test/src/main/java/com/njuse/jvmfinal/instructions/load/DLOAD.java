package com.njuse.jvmfinal.instructions.load;


import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class DLOAD extends Index8Instruction {


    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        int i1 = myVars.getInt(index+1);
        int i2 = myVars.getInt(index);

        long l1 = (i1&0x000000ffffffffL)<<32;
        long l2 = (i2&0x000000ffffffffL);
        long myLong = l1 |l2;
        double ret = Double.longBitsToDouble(myLong);
        stack.pushDouble(ret);
    }
}
