package com.njuse.seecjvm.instructions.control;

import com.njuse.seecjvm.instructions.base.BranchInstruction;
import com.njuse.seecjvm.runtime.StackFrame;

public class GOTO extends BranchInstruction {

    /**
     * TODO：实现这条指令
     * 这是一个已完成的例子
     */
    @Override
    public void execute(StackFrame frame) {
        //nextPC-3是因为在执行这条指令的时候，PC已经指向下一条指令的开始了，
        //而根据手册可以知道，GOTO指令的offset是相对于GOTO的起始字节
        //因此要先-3，然后再加上offset
        int branchPC = frame.getNextPC() - 3 + super.offset;// 3 = opcode + signed short offset
        //设置PC为跳转后地址
        frame.setNextPC(branchPC);
    }
}
