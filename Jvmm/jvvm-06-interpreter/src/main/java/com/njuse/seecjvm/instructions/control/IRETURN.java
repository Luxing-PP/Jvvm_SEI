package com.njuse.seecjvm.instructions.control;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.JThread;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class IRETURN extends NoOperandsInstruction {

    /**
     * TODO： 实现这条指令 PASS
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack oriStack = frame.getOperandStack();
        JThread thread = frame.getThread();

        int value = oriStack.popInt();

        thread.popFrame();
        OperandStack newStack = thread.getTopFrame().getOperandStack();
        newStack.pushInt(value);
    }
}
