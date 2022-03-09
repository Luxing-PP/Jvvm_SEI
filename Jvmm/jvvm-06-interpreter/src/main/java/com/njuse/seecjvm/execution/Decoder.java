package com.njuse.seecjvm.execution;

import com.njuse.seecjvm.instructions.base.Instruction;
import com.njuse.seecjvm.instructions.base.OpCode;
import com.njuse.seecjvm.instructions.constant.*;
import com.njuse.seecjvm.instructions.control.GOTO;
import com.njuse.seecjvm.instructions.control.IRETURN;
import com.njuse.seecjvm.instructions.control.RETURN;
import com.njuse.seecjvm.instructions.invoke.INVOKE_INTERFACE;
import com.njuse.seecjvm.instructions.invoke.INVOKE_SPECIAL;
import com.njuse.seecjvm.instructions.invoke.INVOKE_STATIC;
import com.njuse.seecjvm.instructions.invoke.INVOKE_VIRTUAL;
import com.njuse.seecjvm.instructions.load.ALOAD_N;
import com.njuse.seecjvm.instructions.load.ILOAD;
import com.njuse.seecjvm.instructions.load.ILOAD_N;
import com.njuse.seecjvm.instructions.references.*;
import com.njuse.seecjvm.instructions.stack.DUP;
import com.njuse.seecjvm.instructions.stack.POP;
import com.njuse.seecjvm.instructions.store.ASTORE;
import com.njuse.seecjvm.instructions.store.ASTORE_N;
import com.njuse.seecjvm.instructions.store.ISTORE_N;

import java.util.HashMap;

import static com.njuse.seecjvm.instructions.base.OpCode.RETURN_;

public class Decoder {

    private static HashMap<Integer, Instruction> opMap;

    static {
        opMap = new HashMap<>();
        opMap.put(OpCode.NOP, new NOP());
        opMap.put(OpCode.ACONST_NULL, new ACONST_NULL());
        opMap.put(OpCode.ICONST_M1, new ICONST_N(-1));
        opMap.put(OpCode.ICONST_0, new ICONST_N(0));
        opMap.put(OpCode.ICONST_1, new ICONST_N(1));
        opMap.put(OpCode.ICONST_2, new ICONST_N(2));
        opMap.put(OpCode.ICONST_3, new ICONST_N(3));
        opMap.put(OpCode.ICONST_4, new ICONST_N(4));
        opMap.put(OpCode.ICONST_5, new ICONST_N(5));
//        opMap.put(OpCode.LCONST_0, new LCONST_N(0));
        opMap.put(OpCode.BIPUSH, new BIPUSH());
        opMap.put(OpCode.SIPUSH, new SIPUSH());
        opMap.put(OpCode.LDC, new LDC());
//        opMap.put(OpCode.LDC_W, new LDC_W());
//        opMap.put(OpCode.LDC2_W, new LDC2_W());
        opMap.put(OpCode.ILOAD, new ILOAD());

        opMap.put(OpCode.POP, new POP());
//        opMap.put(OpCode.POP2, new POP2());
        opMap.put(OpCode.DUP, new DUP());

        opMap.put(OpCode.ILOAD_0, new ILOAD_N(0));
        opMap.put(OpCode.ILOAD_1, new ILOAD_N(1));
        opMap.put(OpCode.ILOAD_2, new ILOAD_N(2));
        opMap.put(OpCode.ILOAD_3, new ILOAD_N(3));
//        opMap.put(OpCode.LLOAD_0, new LLOAD_N(0));
//        opMap.put(OpCode.LLOAD_1, new LLOAD_N(1));
        opMap.put(OpCode.ALOAD_0, new ALOAD_N(0));
        opMap.put(OpCode.ALOAD_1, new ALOAD_N(1));
        opMap.put(OpCode.ALOAD_2, new ALOAD_N(2));
        opMap.put(OpCode.ALOAD_3, new ALOAD_N(3));
//        opMap.put(OpCode.IALOAD, new IALOAD());
//        opMap.put(OpCode.LALOAD, new LALOAD());
        opMap.put(OpCode.ASTORE, new ASTORE());
        opMap.put(OpCode.ISTORE_0, new ISTORE_N(0));
        opMap.put(OpCode.ISTORE_1, new ISTORE_N(1));
        opMap.put(OpCode.ISTORE_2, new ISTORE_N(2));
        opMap.put(OpCode.ISTORE_3, new ISTORE_N(3));
//        opMap.put(OpCode.LSTORE_0, new LSTORE_N(0));
//        opMap.put(OpCode.DSTORE_3, new DSTORE_N(3));
        opMap.put(OpCode.ASTORE_0, new ASTORE_N(0));
        opMap.put(OpCode.ASTORE_1, new ASTORE_N(1));
        opMap.put(OpCode.ASTORE_2, new ASTORE_N(2));
        opMap.put(OpCode.ASTORE_3, new ASTORE_N(3));

        opMap.put(OpCode.GOTO_, new GOTO());

//        opMap.put(OpCode.JSR, new JSR());
//        opMap.put(OpCode.RET, new RET());

//        opMap.put(OpCode.TABLESWITCH, new TABLESWITCH());
//        opMap.put(OpCode.LOOKUPSWITCH, new LOOKUPSWITCH());
        opMap.put(OpCode.IRETURN, new IRETURN());
//        opMap.put(OpCode.LRETURN, new LRETURN());
//        opMap.put(OpCode.FRETURN, new FRETURN());
//        opMap.put(OpCode.DRETURN, new DRETURN());
//        opMap.put(OpCode.ARETURN, new ARETURN());
        opMap.put(RETURN_, new RETURN());
        opMap.put(OpCode.GETSTATIC, new GETSTATIC());
        opMap.put(OpCode.PUTSTATIC, new PUTSTATIC());
        opMap.put(OpCode.GETFIELD, new GETFIELD());
        opMap.put(OpCode.PUTFIELD, new PUTFIELD());
        opMap.put(OpCode.INVOKEVIRTUAL, new INVOKE_VIRTUAL());
        opMap.put(OpCode.INVOKESPECIAL, new INVOKE_SPECIAL());
        //TODO: 插入 invoke static PASS
        opMap.put(OpCode.INVOKESTATIC,new INVOKE_STATIC());

        opMap.put(OpCode.INVOKEINTERFACE, new INVOKE_INTERFACE());
        opMap.put(OpCode.NEW_, new NEW());

    }

    public static Instruction decode(int opcode) {
        Instruction instruction = opMap.get(opcode);
        if (instruction == null) {
            throw new UnsupportedOperationException("Unsupported instruction " + String.format("0x%08X", opcode));
        }
        return instruction;
    }
}
