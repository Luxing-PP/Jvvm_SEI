package com.njuse.jvmfinal.instructions.stack;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public class SIPUSH extends Instruction {
    byte high;
    byte low;

    @Override
    public void fetchOperands(ByteBuffer reader) {
        high=reader.get();
        low=reader.get();
    }

    @Override
    public void execute(StackFrame frame) {
        //fixme 未测试
        OperandStack stack = frame.getOperandStack();
        int value = (int)(((short) high) << 8) | ((short) low & 0xFF);
        stack.pushInt(value);
    }
}
