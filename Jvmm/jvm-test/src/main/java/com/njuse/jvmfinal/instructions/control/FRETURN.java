package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class FRETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack oriStack = frame.getOperandStack();
        JThread thread = frame.getThread();

        float value = oriStack.popFloat();

        thread.popFrame();
        OperandStack newStack = thread.getTopFrame().getOperandStack();
        newStack.pushFloat(value);
    }
}
