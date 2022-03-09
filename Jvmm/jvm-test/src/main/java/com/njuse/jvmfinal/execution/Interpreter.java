package com.njuse.jvmfinal.execution;

import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.util.ColorUtil;


import java.awt.*;
import java.nio.ByteBuffer;

public class Interpreter {
    private static ByteBuffer codeReader;

    public static void interpret(JThread thread) {
        if(thread.isEmpty())return;
        initCodeReader(thread);
        loop(thread);
    }

    private static void initCodeReader(JThread thread) {
        StackFrame topFrame = thread.getTopFrame();

        if(topFrame.getMethod()==null) {
            ColorUtil.printRed("Error! in : interpreter");
        }
        //fixme 不知道为什么断言全部无效了
//        assert (topFrame !=null);
//        assert thread.getTopFrame().getMethod()!=null;


        byte[] code = thread.getTopFrame().getMethod().getCode();
        codeReader = ByteBuffer.wrap(code);
        int nextPC = thread.getTopFrame().getNextPC();
        ColorUtil.printCyan("PC="+nextPC);
        codeReader.position(nextPC);
    }

    private static void loop(JThread thread) {
        while (true) {
            StackFrame oriTop = thread.getTopFrame();
//            //parse code attribute for VO
//            Method method = oriTop.getMethod();
//            if (!method.isParsed()) {
//                method.parseCode();
//            }

            codeReader.position(oriTop.getNextPC());
            int opcode = codeReader.get() & 0xff;
            Instruction instruction = Decoder.decode(opcode);
            instruction.fetchOperands(codeReader);

            int nextPC = codeReader.position();
            oriTop.setNextPC(nextPC);

            ColorUtil.printGreen(instruction.toString());

            instruction.execute(oriTop);

            StackFrame newTop = thread.getTopFrame();
            if (newTop == null) {
                return;
            }

            if (oriTop != newTop) {
                initCodeReader(thread);
            }
        }

    }
}
