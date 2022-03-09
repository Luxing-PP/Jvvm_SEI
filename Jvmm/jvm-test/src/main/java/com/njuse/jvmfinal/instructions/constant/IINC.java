package com.njuse.jvmfinal.instructions.constant;

import com.njuse.jvmfinal.instructions.base.Index8Instruction;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.util.ColorUtil;

import java.awt.*;
import java.nio.ByteBuffer;

public class IINC extends Index8Instruction {
    private int myConst = 0;
    @Override
    public void fetchOperands(ByteBuffer reader) {
        super.fetchOperands(reader);
        myConst = (int)reader.get();
    }

    @Override
    public void execute(StackFrame frame) {
        Vars myVars = frame.getLocalVars();
        int locVar = myVars.getInt(index);
        locVar = locVar + myConst;
        ColorUtil.printRed("IINC: "+"index: "+index+"locVar:"+locVar+"myConst"+myConst);
        myVars.setInt(index,locVar);
        int newVar = myVars.getInt(index);
        ColorUtil.printRed(""+newVar);
    }
}
