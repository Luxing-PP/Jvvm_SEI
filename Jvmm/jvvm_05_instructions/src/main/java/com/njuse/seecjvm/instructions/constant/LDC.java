package com.njuse.seecjvm.instructions.constant;

import com.njuse.seecjvm.instructions.base.Index8Instruction;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;

public class LDC extends Index8Instruction {
    @Override
    public void execute(StackFrame frame) {
        loadConstant(frame, index);
    }

    /**
     * TODO：实现这条指令
     * 只需要考虑IntWrapper和FloatWrapper这两种情况
     */
    public static void loadConstant(StackFrame frame, int index) {
        //当前操作数栈
        OperandStack stack = frame.getOperandStack();
        //运行时常量池中对应的元素
        Constant constant = frame.getMethod().getClazz().getRuntimeConstantPool().getConstant(index);
        if (constant instanceof IntWrapper) {
            //TODO 如果这个元素是IntWrapper， insert your code here
            int value=((IntWrapper) constant).getValue();
            stack.pushInt(value);
        }
        else if (constant instanceof FloatWrapper) {
            //TODO 如果这个元素是FloatWrapper， insert your code here
            float value = ((FloatWrapper)constant).getValue();
            stack.pushFloat(value);
        }

        else throw new ClassFormatError();

    }
}
