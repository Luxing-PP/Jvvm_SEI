package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.Slot;

public class INVOKE_SPECIAL extends InvokeInstruction {
    @Override
    public void execute(StackFrame frame) {
        //noinspection Duplicates
        JClass currentClz = frame.getCurrentClass();
        Constant methodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert methodRef instanceof MethodRef;
        Method method = ((MethodRef) methodRef).resolveMethodRef();

        /*
        If the resolved method is protected, and it is a member of a superclass of the current class,
        and the method is not declared in the same run-time package (§5.3) as the current class,
        then the class of objectref must be either the current class or a subclass of the current class.
         */



        Slot[] args = copyArguments(frame, method);

        //format parent.play() of parent
        JObject thisRef = frame.getOperandStack().popObjectRef();

        //the field to be found
        JClass c;
        //fixme 没有测试是superclass 默认了只要名字不对就是superclass
        if (frame.getCurrentClass().isSuper && !method.getName().equals("<init>")
                && !(method.getClazz().getName().equals(frame.getCurrentClass().getName())) ) {
            c = frame.getCurrentClass().getSuperClass();
        } else {
            c = method.getClazz();//the class to be found
        }
        Method toInvoke = ((MethodRef) methodRef).resolveMethodRef(c);


        StackFrame newFrame = prepareNewFrameWithRef(frame,args,thisRef,toInvoke);

        frame.getThread().pushFrame(newFrame);
    }
}
