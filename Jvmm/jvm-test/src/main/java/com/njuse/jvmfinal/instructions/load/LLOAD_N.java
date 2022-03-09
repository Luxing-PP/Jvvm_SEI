package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class LLOAD_N extends LOAD_N {

    public LLOAD_N(int index) {
       super(index);
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        long longValue =myVars.getLong(index);
        stack.pushLong(longValue);
    }
}
