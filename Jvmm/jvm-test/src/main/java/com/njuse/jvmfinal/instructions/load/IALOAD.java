package com.njuse.jvmfinal.instructions.load;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.ArrayObject;

import java.util.ArrayList;

public class IALOAD extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack =frame.getOperandStack();
        int index = stack.popInt();
        ArrayObject arrayRef = (ArrayObject) stack.popObjectRef();

        assert (arrayRef.getAtype()==ArrayObject.T_INT);

        int value = (Integer) arrayRef.getArray().get(index);
        stack.pushInt(value);
    }
}
