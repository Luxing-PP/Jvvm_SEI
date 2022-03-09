package com.njuse.jvmfinal.runtime;

import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.Slot;
import com.njuse.jvmfinal.util.ColorUtil;

import java.util.EmptyStackException;
import java.util.Stack;

public class OperandStack {
    private int top;
    private int maxStackSize;
    private Slot[] slots;


    public OperandStack(int maxStackSize) {
        assert maxStackSize >= 0;
        this.maxStackSize = maxStackSize;
        slots = new Slot[maxStackSize];
        for (int i = 0; i < maxStackSize; i++) slots[i] = new Slot();
        top = 0;
    }

    public void pushInt(int value) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top].setValue(value);
        top++;
        ColorUtil.printPurple("Stack push : "+value);
    }
    public int popInt() {
        top--;
        if (top < 0) throw new EmptyStackException();
        int ret = slots[top].getValue();
        slots[top] = new Slot();
        ColorUtil.printPurple("Stack pop : "+ret);
        return ret;
    }
    public void pushFloat(float value) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top].setValue(Float.floatToIntBits(value));
        top++;
        ColorUtil.printPurple("Stack push : "+value);
    }
    public float popFloat() {
        top--;
        if (top < 0) throw new EmptyStackException();
        float ret = Float.intBitsToFloat(slots[top].getValue());
        ColorUtil.printPurple("Stack pop : "+ret);
        slots[top] = new Slot();
        return ret;
    }
    public void pushLong(long value) {
        int i1=(int)(value>>32);
        int i2=(int)value;
        //依次
        pushInt(i2);
        pushInt(i1);
    }
    public long popLong() {
        //后进先出
        int i1 = popInt();
        int i2 = popInt();
        //合成
        long l1 = (i1&0x000000ffffffffL)<<32;
        long l2 = (i2&0x000000ffffffffL);
        long ret = l1 |l2;
        return ret;
    }

    public void pushDouble(double value) {
        pushLong(Double.doubleToLongBits(value));
    }

    public double popDouble() {
        long longTypeRet=popLong();
        double ret = Double.longBitsToDouble(longTypeRet);
        return ret;
    }

    public void pushObjectRef(JObject ref) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top].setObject(ref);
        top++;
        ColorUtil.printPurple("Stack push : "+ref.toString());
    }

    public JObject popObjectRef() {
        top--;
        if (top < 0) throw new EmptyStackException();
        JObject ret = slots[top].getObject();
        slots[top] = new Slot();
        return ret;
    }

    public void pushSlot(Slot slot) {
        if (top >= maxStackSize) throw new StackOverflowError();
        slots[top] = slot;
        top++;
        ColorUtil.printPurple("Stack push : Slot");
    }

    public Slot popSlot() {
        top--;
        if (top < 0) throw new EmptyStackException();
        Slot ret = slots[top];
        slots[top] = new Slot();
        return ret;
    }

    public int getTop(){
        return this.top;
    }

    public void printStack(){
        ColorUtil.printYellow("-------NumOnStack-------");
        while (getTop()!=0){
            int value = popInt();
            ColorUtil.printBlue("Val:"+value);
        }
        ColorUtil.printYellow("-------NumOnStack-------\n");
    }
}
