package com.njuse.jvmfinal.instructions.load;


import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.util.ColorUtil;

public class ILOAD_N extends LOAD_N {

    public ILOAD_N(int index) {
       super(index);
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        int value =  myVars.getInt(index);
        stack.pushInt(value);
    }
}
