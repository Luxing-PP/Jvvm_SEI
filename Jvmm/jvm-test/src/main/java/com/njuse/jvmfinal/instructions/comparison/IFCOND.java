package com.njuse.jvmfinal.instructions.comparison;


import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public abstract class IFCOND extends BranchInstruction {

    /**
     * 其中，condition 方法是对具体条件的计算，当条件满足时返回true，否则返回false
     */

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int v1 = stack.popInt();
        boolean judge=condition(v1);

        if(judge){
            int branchPC = frame.getNextPC() - 3 + super.offset;// 3 = opcode + signed short offset
            frame.setNextPC(branchPC);
        }
    }

    protected abstract boolean condition(int value);

}
