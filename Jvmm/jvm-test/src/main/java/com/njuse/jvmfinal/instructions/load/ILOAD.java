package com.njuse.jvmfinal.instructions.load;


import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;

public class ILOAD extends Index8Instruction {

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

//        int value =  myVars.getVarSlots()[index].getValue();
        int value =  myVars.getInt(index);
        stack.pushInt(value);
    }
}
