package com.njuse.jvmfinal.instructions.stack;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
//check
public class POP extends NoOperandsInstruction {
    //强制要求只有 1 Slot的可以POP

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack =frame.getOperandStack();
        stack.popSlot();
    }
}
