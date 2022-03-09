package com.njuse.jvmfinal.instructions.comparison;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.util.ColorUtil;

public class LCMP extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();

        long v2 = stack.popLong();
        long v1 = stack.popLong();

        int result=0;

        if(v1>v2){
            result=1;
        }else if(v1==v2){
            result=0;
        }else if(v1<v2){
            result=-1;
        }else {
            ColorUtil.printRed("Error in"+getClass().getSimpleName());
        }

        stack.pushInt(result);
    }
}
