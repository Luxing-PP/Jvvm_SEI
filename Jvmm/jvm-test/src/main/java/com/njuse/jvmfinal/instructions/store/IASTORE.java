package com.njuse.jvmfinal.instructions.store;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.ArrayObject;

import java.util.ArrayList;

public class IASTORE extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int value = stack.popInt();
        int index = stack.popInt();
        ArrayObject arrayRef = (ArrayObject) stack.popObjectRef();

        assert (arrayRef.getAtype()==ArrayObject.T_INT);

        ArrayList<Integer> array = arrayRef.getArray();
        array.set(index,value);
    }
}
