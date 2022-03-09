package com.njuse.jvmfinal.instructions.control;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
//check
public class IRETURN extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack oriStack = frame.getOperandStack();
        JThread thread = frame.getThread();

        int value = oriStack.popInt();
        thread.popFrame();

        OperandStack newStack = thread.getTopFrame().getOperandStack();
        assert newStack != oriStack;
        newStack.pushInt(value);
    }
}
