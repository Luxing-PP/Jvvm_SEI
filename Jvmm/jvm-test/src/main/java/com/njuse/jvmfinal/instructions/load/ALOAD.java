package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;

public class ALOAD extends Index8Instruction {

    @Override
    public void execute(StackFrame frame) {
            Vars myVars=frame.getLocalVars();
            OperandStack stack=frame.getOperandStack();


            JObject ref = myVars.getObjectRef(index);
            stack.pushObjectRef(ref);
    }
}

