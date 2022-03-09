package com.njuse.seecjvm.instructions.comparison;

import com.njuse.seecjvm.instructions.base.NoOperandsInstruction;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class DCMPG extends NoOperandsInstruction {

    /**
     * TODO：实现这条指令
     */
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double d2 = stack.popDouble();
        double d1 = stack.popDouble();
        int result=0;
        if(Double.isNaN(d1)||Double.isNaN(d2)){
            result=1;
        }else if(d1>d2){
            result=1;
        }else if(d1==d2){
            result=0;
        }else if(d1<d2){
            result=-1;
        }

        stack.pushInt(result);
    }
}
