package com.njuse.jvmfinal.instructions.invoke;



import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.Constant;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.MethodRef;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.struct.Slot;
import com.njuse.jvmfinal.util.ColorUtil;
//check not certain
public class INVOKE_STATIC extends InvokeInstruction {

    @Override
    public void execute(StackFrame frame) {

        JClass currentClz = frame.getCurrentClass();
        Constant methodRef = currentClz.getRuntimeConstantPool().getConstant(super.index);
        assert methodRef instanceof MethodRef;
        Method toInvoke = ((MethodRef) methodRef).resolveMethodRef();

        if(toInvoke.isNative())return;

        ColorUtil.printCyan(this.toString()+" "+toInvoke.getName());
        //hack
        if(((MethodRef) methodRef).getClassName().contains("TestUtil")){
            OperandStack opr = frame.getOperandStack();
            if(toInvoke.getName().contains("equalInt")){

                int v2 = opr.popInt();
                int v1 = opr.popInt();
                //Test Only

                if(v1!=v2){
                    //Test Only
//                    ColorUtil.printRed("RuntimeException"+v1+"!="+v2);
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
            }else if(toInvoke.getName().contains("reach")){
                int v = opr.popInt();
                System.out.println(v);
                opr.pushInt(v);
            }
        }

        //好像用不上C
        JClass C=null;
        //fixme
        if(!toInvoke.getName().equals("<init>")&&!toInvoke.getName().equals("<clinit>")&&toInvoke.isStatic()){
            C = toInvoke.getClazz();
        }else {
           ColorUtil.printRed("INVOKE_STATIC MISTAKE!");
        }

        //MakeInit
        assert C != null;
        if(!makeClassInit(C,frame)){
            return;
        }

        //2.
        Slot[] args = copyArguments(frame,toInvoke);
        StackFrame newFrame = prepareNewFrameNoRef(frame,args,toInvoke);
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
