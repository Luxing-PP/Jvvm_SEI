package com.njuse.jvmfinal.instructions.store;


import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class ISTORE_N extends STORE_N {
    public ISTORE_N(int index) {
        setIndex(index);
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        int value = stack.popInt();
        myVars.setInt(index,value);
    }
}
