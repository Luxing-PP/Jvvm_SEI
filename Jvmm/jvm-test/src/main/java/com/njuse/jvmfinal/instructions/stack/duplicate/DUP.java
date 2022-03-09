package com.njuse.jvmfinal.instructions.stack.duplicate;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.Slot;

public class DUP extends NoOperandsInstruction {
    //The dup instruction must not be used unless value is a value of a category 1 computational type
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        //check stack
        int oriTop = stack.getTop();

        Slot value = stack.popSlot();
        Slot duplicate = value.clone();

        stack.pushSlot(value);
        stack.pushSlot(duplicate);

        //check stack
        int newTop = stack.getTop();
        if((newTop-oriTop)!=1){
            throw new StackOverflowError("DUP MISTAKE");
        }
    }


}
