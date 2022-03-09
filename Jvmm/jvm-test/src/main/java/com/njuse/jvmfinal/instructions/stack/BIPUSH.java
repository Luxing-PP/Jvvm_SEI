package com.njuse.jvmfinal.instructions.stack;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public class BIPUSH extends Instruction {
    byte value;


    @Override
    public void fetchOperands(ByteBuffer reader) {
        value = reader.get();
    }

    @Override
    public void execute(StackFrame frame) {
        OperandStack stack =frame.getOperandStack();
        stack.pushInt(value);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " value: " + value;
    }
}
