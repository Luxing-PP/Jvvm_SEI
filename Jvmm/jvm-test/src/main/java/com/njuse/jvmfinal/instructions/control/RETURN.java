package com.njuse.jvmfinal.instructions.control;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;

//check
public class RETURN extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        JThread thread = frame.getThread();
        thread.popFrame();
    }
}
