package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.instructions.base.Index16Instruction;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.Slot;


//fixme 复用arg的导出 栈帧的准备
public abstract class InvokeInstruction extends Index16Instruction {



    protected Slot[] copyArguments(StackFrame frame, Method method) {
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }
        return argv;
    }

    protected StackFrame prepareNewFrameWithRef(StackFrame frame, Slot[] argv, JObject objectRef, Method toInvoke) {
        int argCount = argv.length;

        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal());
        //set ref
        Vars localVars = newFrame.getLocalVars();
        Slot thisSlot = new Slot();
        thisSlot.setObject(objectRef);

        //set locVars
        localVars.setSlot(0, thisSlot);
        for (int i = 1; i < argCount + 1; i++) {
            localVars.setSlot(i, argv[argCount - i]);
        }
        return newFrame;
    }
    protected StackFrame prepareNewFrameNoRef(StackFrame frame, Slot[] argv,Method toInvoke) {
        int argCount = argv.length;

        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal());

        Vars localVars = newFrame.getLocalVars();

        //后进先出
        for (int i = 0; i < argCount; i++) {
            localVars.setSlot(i, argv[argCount-i-1]);
        }
        return newFrame;
    }

    /*
    StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal());

        Vars localVars = newFrame.getLocalVars();
        int argc = toInvoke.getArgc();
        //后进先出
        for (int i = 0; i < args.length; i++) {
            localVars.setSlot(i, args[argc-i-1]);
        }

     */
}
