package com.njuse.jvmfinal.classloader;

import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.ref.ClassRef;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.JObject;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;
import com.njuse.jvmfinal.runtime.struct.NullObject;
import com.njuse.jvmfinal.util.ColorUtil;

public class ClassLinker {
    private JClass clazz;

    public ClassLinker(JClass clazz){
        this.clazz=clazz;
    }

    public JClass linkClass(){
        verify(clazz);
        prepare(clazz);
        return clazz;
    }

    private void verify(JClass clazz) {
        //do in Classfile's creation
    }

    private void prepare(JClass clazz) {
        calInstanceFieldSlotIDs(clazz);
        calStaticFieldSlotIDs(clazz);
        allocAndInitStaticVars(clazz);
        clazz.setInitState(InitState.PREPARED);
    }

    private void calInstanceFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        if (clazz.getSuperClass() != null) {
            slotID = clazz.getSuperClass().getInstanceSlotCount();
        }
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (!f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) slotID++;
            }
        }
        clazz.setInstanceSlotCount(slotID);
    }

    private void calStaticFieldSlotIDs(JClass clazz) {
        int slotID = 0;
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (f.isStatic()) {
                f.setSlotID(slotID);
                slotID++;
                if (f.isLongOrDouble()) slotID++;
            }
        }
        clazz.setStaticSlotCount(slotID);

    }

    private void initDefaultValue(JClass clazz, Field field) {
        Vars clazzVars = clazz.getStaticVars();
        int slotID = field.getSlotID();
        String descriptor = field.getDescriptor();
        if (descriptor.contains("[") || descriptor.toCharArray()[0] == 'L') {
            clazzVars.setObjectRef(slotID, new NullObject());
        } else {
            switch (descriptor) {
                case "Z":
                    //false=0?
                    clazzVars.setInt(slotID, 0);
                    break;
                case "B":
                    clazzVars.setInt(slotID, (byte) 0);
                    break;
                case "I":
                    clazzVars.setInt(slotID, 0);
                    break;
                case "C":
                    clazzVars.setInt(slotID, '\u0000');
                    break;
                case "S":
                    clazzVars.setInt(slotID, (short) 0);
                    break;
                case "J":
                    clazzVars.setLong(slotID, 0L);
                    break;
                case "D":
                    clazzVars.setDouble(slotID, 0.0d);
                    break;
                case "F":
                    clazzVars.setFloat(slotID, 0.0f);
                    break;
                default:
                    System.out.println("Descriptor Error!");
            }
        }
        int m = 0;
    }

    private void loadValueFromRTCP(JClass clazz, Field field) {
        Vars clazzVars = clazz.getStaticVars();
        RuntimeConstantPool runtimeConstantPool = clazz.getRuntimeConstantPool();
        int slotID = field.getSlotID();
        String descriptor = field.getDescriptor();
        //莫非它的意思是不handle 数组等？
        char m = descriptor.toCharArray()[0];
        switch (m) {
            case 'Z':
            case 'B':
            case 'I':
            case 'C':
            case 'S':
                //false=0?
                int intValue = ((IntWrapper) runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setInt(slotID, intValue);
                break;
            case 'J':
                long longValue = ((LongWrapper) runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setLong(slotID, longValue);
                break;
            case 'D':
                double doubleValue = ((DoubleWrapper) runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setDouble(slotID, doubleValue);
                break;
            case 'F':
                float floatValue = ((FloatWrapper) runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setFloat(slotID, floatValue);
                break;
            case 'L':
                //fixme
                JClass type=null;
                try{
                    type = ClassLoader.getInstance().loadClass(descriptor.substring(1,descriptor.length()-1),clazz.getLoadEntryType());
                }catch (ClassNotFoundException e){
                    e.printStackTrace();
                }
                assert type !=null;
                NonArrayObject ref = new NonArrayObject(type);
                clazzVars.setObjectRef(slotID,ref);
                break;
            default:
               ColorUtil.printRed("Descriptor Skip refer to manual"+this.toString());
        }
    }

    private void allocAndInitStaticVars(JClass clazz) {
        //开了个空的空间
        clazz.setStaticVars(new Vars(clazz.getStaticSlotCount()));
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            if (f.isStatic()) {
                if (f.isFinal()) {
                    loadValueFromRTCP(clazz, f);
                } else {
                    initDefaultValue(clazz, f);
                    {
                    }
                }
            }
        }
    }
}
