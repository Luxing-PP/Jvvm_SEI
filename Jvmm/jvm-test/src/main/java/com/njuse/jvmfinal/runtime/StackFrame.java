package com.njuse.jvmfinal.runtime;

import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class StackFrame {
    private OperandStack operandStack;
    //fixme explain: 改来改去还是类框架代码版本，这个版本不用记录返回PC，也不用计算栈帧长度，保持了很好的独立性,这波是负优化QAQ
//    private int bottomPC = 0 ; //第一个数据
//    private int relativePC = 0; //以第一个数据为0 的index
    private int nextPC = 0;
    private Vars localVars;
    //fixme explain: StackFrame 和 Thread 呈聚合关系 是理论基础，实际上无论（类似堆区那样num index也好，链表式的记录上下文也好）都是要在Thread上操作的
    private JThread thread;
    private Method method;
    private JClass currentClass;
    private RuntimeConstantPool runtimeConstantPool; //use for symbolic reference

    public StackFrame(JThread thread, Method method, int maxStackSize, int maxVarSize) {
        this.thread = thread;
        this.method = method;
        //fixme 满足JVM P27 相对来说可读性会高一丝？
        this.currentClass=method.clazz;
        this.runtimeConstantPool=method.clazz.getRuntimeConstantPool();

        assert currentClass.equals(method.getClazz());

        operandStack = new OperandStack(maxStackSize);
        localVars = new Vars(maxVarSize);
    }
}
