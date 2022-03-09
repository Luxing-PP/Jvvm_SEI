package com.njuse.jvmfinal.instructions.store;


import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class ISTORE extends Index8Instruction {

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        int value = stack.popInt();
        myVars.setInt(index,value);
    }
}
