package com.njuse.jvmfinal.instructions.invoke;


import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.Slot;

public class INVOKE_VIRTUAL extends InvokeInstruction {
    @Override
    public void execute(StackFrame frame) {
        OperandStack stack = frame.getOperandStack();
        //test only
        int oritop=stack.getTop();

        JClass currentClz = frame.getCurrentClass();
        Constant methodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert methodRef instanceof MethodRef;
        Method method = ((MethodRef) methodRef).resolveMethodRef();

        //copy arguments
        Slot[] argv=copyArguments(frame,method);

        //lookup virtual function
        JObject objectRef = stack.popObjectRef();
        JClass clazz = objectRef.getClazz();
        Method toInvoke = ((MethodRef) methodRef).resolveMethodRef(clazz);

        StackFrame newFrame = prepareNewFrameWithRef(frame,argv,objectRef,toInvoke);

        frame.getThread().pushFrame(newFrame);

        if (method.isNative()) {
            if (method.getName().equals("registerNatives")) {
                frame.getThread().popFrame();
            } else {
                System.out.println("Native method:"
                        + method.getClazz().getName()
                        + method.name
                        + method.descriptor);
                frame.getThread().popFrame();
            }
        }

        int newtop = stack.getTop();
        //check stack
        if((oritop-newtop)!=method.getArgc()+1){
            throw new StackOverflowError("INVOKE_VIRTUAL MISTAKE");
        }
    }
}
