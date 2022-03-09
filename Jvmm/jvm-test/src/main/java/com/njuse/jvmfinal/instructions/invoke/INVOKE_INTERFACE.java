package com.njuse.jvmfinal.instructions.invoke;

import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.InterfaceMethodRef;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.Slot;
import com.njuse.jvmfinal.util.ColorUtil;

import java.nio.ByteBuffer;

public class INVOKE_INTERFACE extends InvokeInstruction {

    //fixme

    @Override
    public void fetchOperands(ByteBuffer reader) {
        //貌似是说index（short）已经读取了？
        //卧槽，super。。。
//        int count =(int)reader.get() & 0xFF;
//        int fourthOpr = (int)reader.get() & 0xFF;
//        if(count==0||fourthOpr!=0){
//            System.out.println("Error! in Invoke_Interface");
//        }
        super.fetchOperands(reader);
        reader.get();
        reader.get();
    }

    @Override
    public void execute(StackFrame frame) {
        JClass currentClazz = frame.getMethod().getClazz();
        Constant interfaceMethodRef = currentClazz.getRuntimeConstantPool().getConstant(super.index);
        assert interfaceMethodRef instanceof InterfaceMethodRef;
        Method interfaceMethod =((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef();

        //check descriptor


        if(interfaceMethod.isNative()){
            return;
        }

        //look up method
        JClass C = null;
        JObject thisRef = frame.getOperandStack().popObjectRef();
        //The resolved interface method must not be an instance initialization method, or the class or interface initialization method
        if(!interfaceMethod.getName().equals("<init>")&&!interfaceMethod.getName().equals("<clinit>")){
            //引用方法的类
            C = thisRef.getClazz();
        }else {
            System.out.println("INVOKE_INTERFACE ERROR!");
        }

        //makeInit
        assert C != null;
        if(!makeClassInit(C,frame)){
            return;
        }

        Method toInvoke = ((InterfaceMethodRef) interfaceMethodRef).resolveInterfaceMethodRef(C);
        Slot[] args = copyArguments(frame, interfaceMethod);
        StackFrame newFrame = prepareNewFrameWithRef(frame,args,thisRef,toInvoke);
        frame.getThread().pushFrame(newFrame);
    }

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
