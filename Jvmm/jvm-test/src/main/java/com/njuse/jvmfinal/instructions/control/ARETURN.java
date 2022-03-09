package com.njuse.jvmfinal.instructions.control;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;

public class ARETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack oriStack = frame.getOperandStack();
        JThread thread = frame.getThread();

        JObject value = oriStack.popObjectRef();

        thread.popFrame();
        OperandStack newStack = thread.getTopFrame().getOperandStack();
        newStack.pushObjectRef(value);
    }
}
