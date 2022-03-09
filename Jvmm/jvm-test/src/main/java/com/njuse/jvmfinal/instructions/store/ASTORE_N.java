package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.JObject;

public class ASTORE_N extends STORE_N {

    public ASTORE_N(int index){
        setIndex(index);
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        //The objectref on the top of the operand stack must be of type returnAddress or of type reference.
        JObject ref = stack.popObjectRef();
        myVars.setObjectRef(index,ref);
    }
}
