package com.njuse.jvmfinal.instructions.comparison;

import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;

public class IFNONNULL extends BranchInstruction {

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        JObject ref = stack.popObjectRef();
        boolean judge = !ref.isNull();

        if(judge){
            int branchPC = frame.getNextPC() - 3 + super.offset;// 3 = opcode + signed short offset
            frame.setNextPC(branchPC);
        }
    }
}
