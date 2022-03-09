package com.njuse.seecjvm.instructions.invoke;

import com.njuse.seecjvm.instructions.base.Index16Instruction;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.seecjvm.runtime.OperandStack;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.Slot;

public class INVOKE_STATIC extends Index16Instruction {

    /**
     * TODO 实现这条指令，注意其中的非标准部分： 但为什么这样dack就可以呢？
     * 1. TestUtil.equalInt(int a, int b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`, 其中，这个异常的message为
     * ：${第一个参数的值}!=${第二个参数的值}
     * 例如，TestUtil.equalInt(1, 2)应该抛出
     * RuntimeException("1!=2")
     *
     * 2. TestUtil.fail(): 抛出`RuntimeException`
     *
     * 3. TestUtil.equalFloat(float a, float b): 如果a和b相等，则跳过这个方法，
     * 否则抛出`RuntimeException`. 对于异常的message不作要求
     *
     */
    @Override
    public void execute(StackFrame frame) {
        JClass currentClz = frame.getMethod().getClazz();
        Constant methodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert methodRef instanceof MethodRef;
        Method toInvoke = ((MethodRef) methodRef).resolveMethodRef();

        if(toInvoke.isNative())return;
        //对非常规的处理
        if(((MethodRef) methodRef).getClassName().contains("TestUtil")){
            OperandStack opr = frame.getOperandStack();
            if(toInvoke.getName().contains("equalInt")){
                int v2 = opr.popInt();
                int v1 = opr.popInt();
                if(v1!=v2){
                    throw new RuntimeException(v1+"!="+v2);
                }
                opr.pushInt(v1);
                opr.pushInt(v2);
            }else if(toInvoke.getName().contains("equalFloat")){
                float v2 = opr.popFloat();
                float v1 = opr.popFloat();
                if(v1-v2>0.0001 || v1-v2<-0.0001){
                    throw new RuntimeException();
                }
                opr.pushFloat(v1);
                opr.pushFloat(v2);
            }else if(toInvoke.getName().contains("fail")){
                throw new RuntimeException();
            }
        }
        //好像用不上C
        JClass C;
        if(!toInvoke.getName().equals("<init>")&&!toInvoke.getName().equals("<clinit>")&&toInvoke.isStatic()){
            C = toInvoke.getClazz();
        }else {
            System.out.println("INVOKE_STATIC MISTAKE!");
        }

        //2.
        Slot[] args = copyArguments(frame,toInvoke);
        //去掉了+1
        StackFrame newFrame = new StackFrame(frame.getThread(), toInvoke,
                toInvoke.getMaxStack(), toInvoke.getMaxLocal());

        Vars localVars = newFrame.getLocalVars();
        int argc = toInvoke.getArgc();
        //后进先出
        for (int i = 0; i < args.length; i++) {
            localVars.setSlot(i, args[argc-i-1]);
        }

        frame.getThread().pushFrame(newFrame);
    }

    private Slot[] copyArguments(StackFrame frame, Method method) {
        int argc = method.getArgc();
        Slot[] argv = new Slot[argc];
        for (int i = 0; i < argc; i++) {
            argv[i] = frame.getOperandStack().popSlot();
        }
        return argv;
    }
}
