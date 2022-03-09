package com.njuse.jvmfinal.instructions.constant;


import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.StringWrapper;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;

public class LDC extends Index8Instruction {
    @Override
    public void execute(StackFrame frame) {
        loadConstant(frame, index);
    }

    public static void loadConstant(StackFrame frame, int index) {
        OperandStack stack = frame.getOperandStack();
        //fixme must be a run-time constant of type int or float, or a reference to a string literal, or a symbolic reference to a class, method type, or method handle
        //运行时常量池中对应的元素
        Constant constant = frame.getRuntimeConstantPool().getConstant(index);
        if (constant instanceof IntWrapper) {
            int value=((IntWrapper) constant).getValue();
            stack.pushInt(value);
        }
        else if (constant instanceof FloatWrapper) {
            float value = ((FloatWrapper)constant).getValue();
            stack.pushFloat(value);
        }else if(constant instanceof StringWrapper){
            String classname = ((StringWrapper)constant).getValue();
            //fixme
            NonArrayObject ref=null;
            try{
                ref = new NonArrayObject(ClassLoader.getInstance().loadClass(classname,frame.getCurrentClass().getLoadEntryType()));
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            stack.pushObjectRef(ref);

        }else if(constant instanceof ClassRef){
            NonArrayObject ref = null;
            try{
                ref=new NonArrayObject(((ClassRef)constant).getResolvedClass());
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            stack.pushObjectRef(ref);
        }
        // 可能是SYMBOLIC了
        else throw new ClassFormatError();

    }
}
