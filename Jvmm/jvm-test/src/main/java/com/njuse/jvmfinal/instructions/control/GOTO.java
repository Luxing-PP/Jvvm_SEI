package com.njuse.jvmfinal.instructions.control;


import com.njuse.jvmfinal.instructions.base.BranchInstruction;
import com.njuse.jvmfinal.runtime.StackFrame;

public class GOTO extends BranchInstruction {

    @Override
    public void execute(StackFrame frame) {
        //nextPC-3是因为在执行这条指令的时候，PC已经指向下一条指令的开始了，
        //而根据手册可以知道，GOTO指令的offset是相对于GOTO的起始字节
        //因此要先-3，然后再加上offset
        int branchPC = frame.getNextPC() - 3 + super.offset;// 3 = opcode + signed short offset
        frame.setNextPC(branchPC);
    }
}
