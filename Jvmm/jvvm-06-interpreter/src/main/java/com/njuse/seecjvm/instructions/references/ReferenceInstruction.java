package com.njuse.seecjvm.instructions.references;

import com.njuse.seecjvm.ColorUtil;
import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.runtime.StackFrame;

public abstract class ReferenceInstruction extends Index16Instruction {

    protected boolean isClassInit(JClass clazz, StackFrame frame){
        if (clazz.getInitState() == InitState.PREPARED) {
            frame.setNextPC(frame.getNextPC() - 3);//opcode + operand = 3bytes
            clazz.initClass(frame.getThread(), clazz);
            return false;
        }else if(clazz.getInitState()==InitState.SUCCESS){
            return true;
        }else {
            ColorUtil.printRed("Error in ReferenceInstruction Unexpected state");
            return false;
        }
    }
}
