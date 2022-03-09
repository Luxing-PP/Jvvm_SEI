package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class FLOAD_N extends LOAD_N {

    public FLOAD_N(int index) {
        super(index);
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        float floatValue = myVars.getFloat(index);
        stack.pushFloat(floatValue);
    }
}
