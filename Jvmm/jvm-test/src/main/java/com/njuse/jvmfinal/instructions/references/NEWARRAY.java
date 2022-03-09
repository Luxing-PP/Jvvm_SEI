package com.njuse.jvmfinal.instructions.references;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.ArrayObject;
import com.njuse.jvmfinal.util.ColorUtil;

import java.nio.ByteBuffer;

public class NEWARRAY extends Instruction {
    public int atype;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        byte value =reader.get();
        atype=(int)value;
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        int count = stack.popInt();

        ArrayObject ref =new ArrayObject(count,atype);

        stack.pushObjectRef(ref);
        ColorUtil.printCyan(toString()+" Count: "+count+" Type "+atype);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
