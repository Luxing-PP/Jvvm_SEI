package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class DSTORE_N extends STORE_N {

    public DSTORE_N(int index) {
        setIndex(index);
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        double value = stack.popDouble();
        myVars.setDouble(index,value);
    }
}
