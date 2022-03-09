package com.njuse.jvmfinal.instructions.comparison;

import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;

public class FCMPL extends NoOperandsInstruction {
    @Override
    public void execute(StackFrame frame) {
            OperandStack stack = frame.getOperandStack();
            float f2 = stack.popFloat();
            float f1 = stack.popFloat();

            int result = 0;

            if (Float.isNaN(f1) || Float.isNaN(f2)) {
                result = -1;
            } else if (f1 > f2) {
                result = 1;
            } else if (f1 == f2) {
                result = 0;
            } else if (f1 < f2) {
                result = -1;
            }

            stack.pushInt(result);
        }
}

