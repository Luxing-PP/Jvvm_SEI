package com.njuse.jvmfinal.instructions.references;


import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.util.ColorUtil;

public abstract class ReferenceInstruction extends Index16Instruction {

    protected boolean makeClassInit(JClass clazz, StackFrame frame){
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
