package com.njuse.jvmfinal.instructions.comparison;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class DCMPL extends NoOperandsInstruction {


    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        double d2 = stack.popDouble();
        double d1 = stack.popDouble();
        int result=0;
        if(Double.isNaN(d1)||Double.isNaN(d2)){
            result=-1;
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
