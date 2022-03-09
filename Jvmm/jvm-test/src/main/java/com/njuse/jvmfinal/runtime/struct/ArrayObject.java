package com.njuse.jvmfinal.runtime.struct;

import com.njuse.jvmfinal.runtime.Vars;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter
@Setter
public class ArrayObject extends JObject {
    public static final int T_BOOLEAN=4;
    public static final int T_CHAR=5;
    public static final int T_FLOAT=6;
    public static final int T_DOUBLE=7;
    public static final int T_BYTE=8;
    public static final int T_SHORT=9;
    public static final int T_INT=10;
    public static final int T_LONG=11;

    private ArrayList array = new ArrayList();
    private int atype;

    public ArrayObject(int count, int atype){
        this.clazz = null;
        this.atype =atype;
        array=parseAtype(count,atype);
        numInHeap++;

    }

    private ArrayList parseAtype(int count,int atype){
        switch (atype){
            case T_BOOLEAN:
                ArrayList<Boolean> ret_boolean = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_boolean.add(false);
                }
                return ret_boolean;
            case T_CHAR:
                ArrayList<Character> ret_char = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_char.add((char)0);
                }
                return ret_char;
            case T_FLOAT:
                ArrayList<Float> ret_float = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_float.add(0.0f);
                }
                return ret_float;
            case T_DOUBLE:
                ArrayList<Double> ret_double = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_double.add(0.0);
                }
                return ret_double;
            case T_BYTE:
                ArrayList<Byte> ret_byte = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_byte.add((byte)0);
                }
                return ret_byte;
            case T_SHORT:
                ArrayList<Short> ret_short = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_short.add((short)0);
                }
                return ret_short;
            case T_INT:
                ArrayList<Integer> ret_int = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_int.add(0);
                }
                return ret_int;
            case T_LONG:
                ArrayList<Long> ret_long = new ArrayList<>();
                for (int i=0;i<count;i++){
                    ret_long.add(0L);
                }
                return ret_long;
            default:
                throw new RuntimeException("Array atype mistake！");

        }
    }
}
