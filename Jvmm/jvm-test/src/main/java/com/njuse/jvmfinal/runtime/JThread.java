package com.njuse.jvmfinal.runtime;

import com.njuse.jvmfinal.util.ColorUtil;

import java.util.Stack;

public class JThread {
    private ThreadStack stack;

    public JThread() {
        stack = new ThreadStack();
    }

    public void pushFrame(StackFrame frame) {
//        StackFrame callerFrame = getTopFrame();
//        PC = callerFrame.getBottomPC()+callerFrame.getMethod().getCode().length;
//        frame.setBottomPC(PC);
        stack.pushFrame(frame);

        ColorUtil.printCyan("\nPUSH:");
        printFrame();
    }

    public void popFrame() {
        stack.popFrame();
        ColorUtil.printCyan("\nPOP:");
        printFrame();
//        StackFrame nowFrame =getTopFrame();
//        PC = nowFrame.getBottomPC()+nowFrame.getRelativePC();
    }

    public StackFrame getTopFrame() {
        return stack.getTopFrame();
    }

    private void printFrame(){
        ColorUtil.printYellow("-------FrameOnThread-------");
        Stack<StackFrame> nowstack =(Stack<StackFrame>)stack.getStack().clone();
        StackFrame aFrame = null;
        while (!nowstack.isEmpty()){
            aFrame = nowstack.pop();
            ColorUtil.printBlue(aFrame.getCurrentClass().getName()+": "+aFrame.getMethod().name);
        }
        ColorUtil.printYellow("-------FrameOnThread-------\n");
    }

    public boolean isEmpty(){
        return (stack.getCurrentSize()==0)?true:false;
    }
}
