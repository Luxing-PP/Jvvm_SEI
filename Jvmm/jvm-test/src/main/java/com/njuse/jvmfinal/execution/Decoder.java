package com.njuse.jvmfinal.execution;


import com.njuse.jvmfinal.instructions.base.Instruction;
import com.njuse.jvmfinal.instructions.base.OpCode;
import com.njuse.jvmfinal.instructions.comparison.*;
import com.njuse.jvmfinal.instructions.constant.*;
import com.njuse.jvmfinal.instructions.control.*;
import com.njuse.jvmfinal.instructions.conversion.*;
import com.njuse.jvmfinal.instructions.invoke.INVOKE_INTERFACE;
import com.njuse.jvmfinal.instructions.invoke.INVOKE_SPECIAL;
import com.njuse.jvmfinal.instructions.invoke.INVOKE_STATIC;
import com.njuse.jvmfinal.instructions.invoke.INVOKE_VIRTUAL;
import com.njuse.jvmfinal.instructions.load.*;
import com.njuse.jvmfinal.instructions.math.ByteOp.ISHL;
import com.njuse.jvmfinal.instructions.math.algorithm.*;
import com.njuse.jvmfinal.instructions.math.logic.IXOR;
import com.njuse.jvmfinal.instructions.references.*;
import com.njuse.jvmfinal.instructions.stack.BIPUSH;
import com.njuse.jvmfinal.instructions.stack.POP;
import com.njuse.jvmfinal.instructions.stack.SIPUSH;
import com.njuse.jvmfinal.instructions.stack.duplicate.DUP;

import com.njuse.jvmfinal.instructions.store.*;
import com.njuse.jvmfinal.runtime.OperandStack;
import com.njuse.jvmfinal.util.ColorUtil;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;


public class Decoder {
    //确定坐标的话数组貌似效率高点，搞对象还是要搞对象的，没有对象怎么多态doge
    //初始化问题之类的， 都是又fetchoper处理的有啥好担心的
    private static Instruction[] opArray = new Instruction[0xff+1];
    static {
        opArray[OpCode.NOP]=new NOP();

        opArray[OpCode.INVOKEINTERFACE]= new INVOKE_INTERFACE();
        opArray[OpCode.INVOKESPECIAL]=new INVOKE_SPECIAL();
        opArray[OpCode.INVOKESTATIC]=new INVOKE_STATIC();
        opArray[OpCode.INVOKEVIRTUAL]=new INVOKE_VIRTUAL();


        //none done
        opArray[OpCode.ACONST_NULL]=new ACONST_NULL();

        opArray[OpCode.IINC]=new IINC();
        opArray[OpCode.ICONST_M1]=new ICONST_N(-1);
        opArray[OpCode.ICONST_0]=new ICONST_N(0);
        opArray[OpCode.ICONST_1]=new ICONST_N(1);
        opArray[OpCode.ICONST_2]=new ICONST_N(2);
        opArray[OpCode.ICONST_3]=new ICONST_N(3);
        opArray[OpCode.ICONST_4]=new ICONST_N(4);
        opArray[OpCode.ICONST_5]=new ICONST_N(5);

        opArray[OpCode.DCONST_0]=new DCONST_N(0.0);
        opArray[OpCode.DCONST_1]=new DCONST_N(1.0);

        opArray[OpCode.LCONST_0]=new LCONST_N(0);
        opArray[OpCode.LCONST_1]=new LCONST_N(1);

        opArray[OpCode.FCONST_0]=new FCONST_N(0);
        opArray[OpCode.FCONST_1]=new FCONST_N(1);
        opArray[OpCode.FCONST_2]=new FCONST_N(2);

        opArray[OpCode.LDC]=new LDC();
        opArray[OpCode.LDC2_W]=new LDC2_W();

        opArray[OpCode.ALOAD]=new ALOAD();
        opArray[OpCode.ILOAD]=new ILOAD();
        opArray[OpCode.DLOAD]=new DLOAD();
        opArray[OpCode.LLOAD]=new LLOAD();
        opArray[OpCode.FLOAD]=new FLOAD();

        opArray[OpCode.ISTORE]=new ISTORE();
        opArray[OpCode.ASTORE]=new ASTORE();
        opArray[OpCode.DSTORE]=new DSTORE();
        opArray[OpCode.FSTORE]=new FSTORE();
        opArray[OpCode.LSTORE]=new LSTORE();

        opArray[OpCode.ISTORE_0]=new ISTORE_N(0);
        opArray[OpCode.ISTORE_1]=new ISTORE_N(1);
        opArray[OpCode.ISTORE_2]=new ISTORE_N(2);
        opArray[OpCode.ISTORE_3]=new ISTORE_N(3);

        opArray[OpCode.ASTORE_0]=new ASTORE_N(0);
        opArray[OpCode.ASTORE_1]=new ASTORE_N(1);
        opArray[OpCode.ASTORE_2]=new ASTORE_N(2);
        opArray[OpCode.ASTORE_3]=new ASTORE_N(3);

        opArray[OpCode.DSTORE_0]=new DSTORE_N(0);
        opArray[OpCode.DSTORE_1]=new DSTORE_N(1);
        opArray[OpCode.DSTORE_2]=new DSTORE_N(2);
        opArray[OpCode.DSTORE_3]=new DSTORE_N(3);


        opArray[OpCode.POP]=new POP();
        opArray[OpCode.DUP]=new DUP();
        opArray[OpCode.BIPUSH]=new BIPUSH();
        opArray[OpCode.SIPUSH]=new SIPUSH();

        opArray[OpCode.ALOAD_0]=new ALOAD_N(0);
        opArray[OpCode.ALOAD_1]=new ALOAD_N(1);
        opArray[OpCode.ALOAD_2]=new ALOAD_N(2);
        opArray[OpCode.ALOAD_3]=new ALOAD_N(3);

        opArray[OpCode.ILOAD_0]=new ILOAD_N(0);
        opArray[OpCode.ILOAD_1]=new ILOAD_N(1);
        opArray[OpCode.ILOAD_2]=new ILOAD_N(2);
        opArray[OpCode.ILOAD_3]=new ILOAD_N(3);

        opArray[OpCode.DLOAD_0]=new DLOAD_N(0);
        opArray[OpCode.DLOAD_1]=new DLOAD_N(1);
        opArray[OpCode.DLOAD_2]=new DLOAD_N(2);
        opArray[OpCode.DLOAD_3]=new DLOAD_N(3);

        opArray[OpCode.LLOAD_0]=new LLOAD_N(0);
        opArray[OpCode.LLOAD_1]=new LLOAD_N(1);
        opArray[OpCode.LLOAD_2]=new LLOAD_N(2);
        opArray[OpCode.LLOAD_3]=new LLOAD_N(3);

        opArray[OpCode.FLOAD_0]=new FLOAD_N(0);
        opArray[OpCode.FLOAD_1]=new FLOAD_N(1);
        opArray[OpCode.FLOAD_2]=new FLOAD_N(2);
        opArray[OpCode.FLOAD_3]=new FLOAD_N(3);

        opArray[OpCode.ARETURN]=new ARETURN();
        opArray[OpCode.DRETURN]=new DRETURN();
        opArray[OpCode.FRETURN]=new FRETURN();
        opArray[OpCode.IRETURN]=new IRETURN();
        opArray[OpCode.LRETURN]=new LRETURN();
        opArray[OpCode.RETURN_]=new RETURN();

        opArray[OpCode.GETFIELD]=new GETFIELD();
        opArray[OpCode.GETSTATIC]=new GETSTATIC();
        opArray[OpCode.PUTFIELD]=new PUTFIELD();
        opArray[OpCode.PUTSTATIC]=new PUTSTATIC();

        opArray[OpCode.DCMPL]=new DCMPL();
        opArray[OpCode.DCMPG]=new DCMPG();
        opArray[OpCode.FCMPG]=new FCMPG();
        opArray[OpCode.FCMPL]=new FCMPL();

        opArray[OpCode.LCMP]=new LCMP();
        opArray[OpCode.GOTO_]=new GOTO();
        opArray[OpCode.IF_ICMPEQ]=new IF_ICMPEQ();
        opArray[OpCode.IF_ICMPGE]=new IF_ICMPGE();
        opArray[OpCode.IF_ICMPGT]=new IF_ICMPGT();
        opArray[OpCode.IF_ICMPLE]=new IF_ICMPLE();
        opArray[OpCode.IF_ICMPLT]=new IF_ICMPLT();
        opArray[OpCode.IF_ICMPNE]=new IF_ICMPNE();
        opArray[OpCode.IFNONNULL]=new IFNONNULL();
        opArray[OpCode.IFNULL]=new IFNULL();

        opArray[OpCode.IFEQ]=new IFEQ();
        opArray[OpCode.IFGE]=new IFGE();
        opArray[OpCode.IFGT]=new IFGT();
        opArray[OpCode.IFLE]=new IFLE();
        opArray[OpCode.IFLT]=new IFLT();
        opArray[OpCode.IFNE]=new IFNE();

        //array
        opArray[OpCode.NEWARRAY]=new NEWARRAY();
        opArray[OpCode.IASTORE]=new IASTORE();
        opArray[OpCode.IALOAD]=new IALOAD();

        //algorithm
        opArray[OpCode.IADD]=new IADD();
        opArray[OpCode.IDIV]=new IDIV();
        opArray[OpCode.IMUL]=new IMUL();
        opArray[OpCode.ISUB]=new ISUB();
        opArray[OpCode.INEG]=new INEG();

        opArray[OpCode.DADD]=new DADD();
        opArray[OpCode.DDIV]=new DDIV();
        opArray[OpCode.DMUL]=new DMUL();
        opArray[OpCode.DSUB]=new DSUB();

        opArray[OpCode.LADD]=new LADD();
        opArray[OpCode.FADD]=new FADD();
        //logic
        opArray[OpCode.IXOR]=new IXOR();

        //Byte
        opArray[OpCode.ISHL]=new ISHL();
        //conversion
        opArray[OpCode.I2B]=new I2B();
        opArray[OpCode.I2C]=new I2C();
        opArray[OpCode.I2S]=new I2S();
        opArray[OpCode.I2D]=new I2D();
        opArray[OpCode.I2L]=new I2L();

        opArray[OpCode.D2F]=new D2F();
        opArray[OpCode.D2I]=new D2I();
        opArray[OpCode.D2L]=new D2L();

        opArray[OpCode.F2D]=new F2D();
        opArray[OpCode.F2I]=new F2I();
        opArray[OpCode.F2L]=new F2L();

        opArray[OpCode.L2D]=new L2D();
        opArray[OpCode.L2F]=new L2F();
        opArray[OpCode.L2I]=new L2I();

        opArray[OpCode.NEW_]=new NEW();


    }
    public static Instruction decode(int opcode) {
        Instruction instruction = opArray[opcode];
        if (instruction == null) {
            throw new UnsupportedOperationException("Unsupported instruction " + String.format("0x%08X", opcode));
        }
        return instruction;
    }

    public static void check(){
        for(int i=0;i<opArray.length;i++){
            if(opArray[i]==null){
                ColorUtil.printRed("Error! Decoder fail in index"+i);
            }
        }
    }
}
