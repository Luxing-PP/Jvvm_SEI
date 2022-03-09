package com.njuse.seecjvm.instructions.load;

import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;

public class DLOAD_N extends LOAD_N {
    public DLOAD_N(int index) {
        checkIndex(index);
        this.index = index;
    }
    /**
     * TODO：实现这条指令
     * 其中成员index是这条指令的参数，已经读取好了
     */
    @Override
    public void execute(StackFrame frame) {
        Vars myVars=frame.getLocalVars();
        OperandStack stack=frame.getOperandStack();

        int i1 = myVars.getVarSlots()[index+1].getValue();
        int i2 = myVars.getVarSlots()[index].getValue();

        long l1 = (i1&0x000000ffffffffL)<<32;
        long l2 = (i2&0x000000ffffffffL);
        long myLong = l1 |l2;
        double ret = Double.longBitsToDouble(myLong);
        stack.pushDouble(ret);
    }
}
