package com.njuse.jvmfinal.instructions.base;



import com.njuse.jvmfinal.runtime.StackFrame;

import java.nio.ByteBuffer;

public abstract class Instruction {
    public abstract void execute(StackFrame frame);
    public abstract void fetchOperands(ByteBuffer reader);
}
