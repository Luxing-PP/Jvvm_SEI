package com.njuse.jvmfinal.instructions.math.ByteOp;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class ISHL extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {

        OperandStack stack =frame.getOperandStack();
        int val2 = stack.popInt();
        int val1 = stack.popInt();

//        String val2String = Integer.toBinaryString(val2);
//        val2String = val2String.substring(val2String.length()-6,val2String.length()-1);
//        int s = Integer.valueOf(val2String,2);


        int result = val1 << val2;
        stack.pushInt(result);
    }
}
