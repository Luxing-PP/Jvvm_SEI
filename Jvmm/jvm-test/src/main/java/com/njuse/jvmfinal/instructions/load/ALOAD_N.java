package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.JObject;

public class ALOAD_N extends LOAD_N {

    public ALOAD_N(int index) {
        super(index);
    }
    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();


        JObject ref = myVars.getObjectRef(index);
        stack.pushObjectRef(ref);
    }
}
