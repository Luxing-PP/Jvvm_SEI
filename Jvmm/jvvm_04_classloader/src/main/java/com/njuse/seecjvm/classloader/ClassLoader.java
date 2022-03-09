package com.njuse.seecjvm.classloader;

import com.njuse.seecjvm.classloader.classfileparser.ClassFile;
import com.njuse.seecjvm.classloader.classfilereader.ClassFileReader;
import com.njuse.seecjvm.classloader.classfilereader.classpath.EntryType;
import com.njuse.seecjvm.memory.MethodArea;
import com.njuse.seecjvm.memory.jclass.Field;
import com.njuse.seecjvm.memory.jclass.InitState;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.JObject;
import com.njuse.seecjvm.runtime.struct.NullObject;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class ClassLoader {
    private static ClassLoader classLoader = new ClassLoader();
    private ClassFileReader classFileReader;
    private MethodArea methodArea;

    private ClassLoader() {
        classFileReader = ClassFileReader.getInstance();
        methodArea = MethodArea.getInstance();
    }

    public static ClassLoader getInstance() {
        return classLoader;
    }

    /**
     * load phase
     *
     * @param className       name of class
     * @param initiatingEntry null value represents load MainClass
     */
    public JClass loadClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        if ((ret = methodArea.findClass(className)) == null) {
            return loadNonArrayClass(className, initiatingEntry);
        }
        return ret;
    }

    private JClass loadNonArrayClass(String className, EntryType initiatingEntry) throws ClassNotFoundException {
        try {
            Pair<byte[], Integer> res = classFileReader.readClassFile(className, initiatingEntry);
            byte[] data = res.getKey();
            EntryType definingEntry = new EntryType(res.getValue());
            //define class
            JClass clazz = defineClass(data, definingEntry);
            //link class
            linkClass(clazz);
            return clazz;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    /**
     *
     * define class
     * @param data binary of class file
     * @param definingEntry defining loader of class
     */
    private JClass defineClass(byte[] data, EntryType definingEntry) throws ClassNotFoundException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        clazz.setLoadEntryType(definingEntry);

        resolveSuperClass(clazz);
        resolveInterfaces(clazz);
        methodArea.addClass(clazz.getName(),clazz);
        return clazz;
    }

    /**
     * load superclass before add to method area
     */
    private void resolveSuperClass(JClass clazz) throws ClassNotFoundException {
        if(!clazz.getName().equals("java/lang/Object")){
            String superName = clazz.getSuperClassName();
            clazz.setSuperClass(loadClass(clazz.getSuperClassName(),clazz.getLoadEntryType()));
        }
    }

    /**
     * load interfaces before add to method area
     */
    private void resolveInterfaces(JClass clazz) throws ClassNotFoundException {
        //todo
        /**
         * Add some codes here.
         *
         * Use the load entry(defining entry) as initiating entry of interfaces
         */
        //先设置和后设置的区别是啥
        String[] myInterfaces=clazz.getInterfaceNames();
        JClass[] interfaces= new JClass[myInterfaces.length];
        for(int i=0;i<myInterfaces.length;i++){
            interfaces[i]=loadClass(myInterfaces[i],clazz.getLoadEntryType());
        }
        clazz.setInterfaces(interfaces);
    }

    /**
     * link phase
     */
    private void linkClass(JClass clazz) {
        verify(clazz);
        prepare(clazz);
    }

    /**
     * You don't need to write any code here.
     */
    private void verify(JClass clazz) {
        //do nothing
    }

    private void prepare(JClass clazz) {
        //todo
        /**
         * Add some codes here.
         *
         * step1 (We do it for you here)
         *      count the fields slot id in instance
         *      count the fields slot id in class
         * step2
         *      alloc memory for fields(We do it for you here) and init static vars
         * step3
         *      set the init state
         */
        calInstanceFieldSlotIDs(clazz);
        calStaticFieldSlotIDs(clazz);
        allocAndInitStaticVars(clazz);
        clazz.setInitState(InitState.PREPARED);
    }

    /**
     * count the number of field slots in instance
     * long and double takes two slots
     * the field is not static
     */
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

    /**
     * count the number of field slots in class
     * long and double takes two slots
     * the field is static
     */
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

    /**
     * PASS primitive type is set to 0
     * PASS ref type is set to null
     */
    private void initDefaultValue(JClass clazz, Field field) {
        //todo
        /**
         * Add some codes here.
         * step 1
         *      get static vars of class
         * step 2
         *      get the slotID index of field
         * step 3
         *      switch by descriptor or some part of descriptor
         *      Handle basic type ZBCSIJDF and references (with new NullObject())
         */
        Vars clazzVars = clazz.getStaticVars();
        int slotID=field.getSlotID();
        String descriptor=field.getDescriptor();
        if(descriptor.contains("[")||descriptor.toCharArray()[0]=='L'){
            clazzVars.setObjectRef(slotID, new NullObject());
        }else {
            switch (descriptor){
                case "Z":
                    //false=0?
                    clazzVars.setInt(slotID,0);
                    break;
                case "B":
                    clazzVars.setInt(slotID,(byte)0);
                    break;
                case "I":
                    clazzVars.setInt(slotID,0);
                    break;
                case "C":
                    clazzVars.setInt(slotID,'\u0000');
                    break;
                case "S":
                    clazzVars.setInt(slotID,(short)0);
                    break;
                case "J":
                    clazzVars.setLong(slotID,0L);
                    break;
                case "D":
                    clazzVars.setDouble(slotID,0.0d);
                    break;
                case "F":
                    clazzVars.setFloat(slotID,0.0f);
                    break;
                default:
                    System.out.println("Descriptor Error!");
            }
        }
        int m=0;
    }

    /**
     * PASS load const value from runtimeConstantPool for primitive type
     * CONFUSED String is not support now
     */
    private void loadValueFromRTCP(JClass clazz, Field field) {
        //todo
        /**
         * Add some codes here.
         *
         * step 1
         *      get static vars and runtime constant pool of class
         * step 2
         *      get the slotID and constantValue index of field
         * step 3
         *      switch by descriptor or some part of descriptor
         *      just handle basic type ZBCSIJDF, you don't have to throw any exception
         *      use wrapper to get value
         *
         *  Example
         *      long longVal = ((LongWrapper) runtimeConstantPool.getConstant(constantPoolIndex)).getValue();
         */
        Vars clazzVars=clazz.getStaticVars();
        RuntimeConstantPool runtimeConstantPool=clazz.getRuntimeConstantPool();
        int slotID=field.getSlotID();
        String descriptor = field.getDescriptor();
        //莫非它的意思是不handle 数组等？
        switch (descriptor) {
            case "Z":
            case "B":
            case "I":
            case "C":
            case "S":
                //false=0?
                int intValue = ((IntWrapper)runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setInt(slotID, intValue);
                break;
            case "J":
                long longValue = ((LongWrapper)runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setLong(slotID, longValue);
                break;
            case "D":
                double doubleValue =((DoubleWrapper)runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setDouble(slotID, doubleValue);
                break;
            case "F":
                float floatValue=((FloatWrapper)runtimeConstantPool.getConstant(field.getConstValueIndex())).getValue();
                clazzVars.setFloat(slotID, floatValue);
                break;
            default:
                System.out.println("Descriptor Skip refer to manual");
        }
    }

    /**
     * PASS the value of static final field is in com.njuse.seecjvm.runtime constant pool
     * PASS others will be set to default value
     */
    private void allocAndInitStaticVars(JClass clazz) {
        //开了个空的空间
        clazz.setStaticVars(new Vars(clazz.getStaticSlotCount()));
        Field[] fields = clazz.getFields();
        for (Field f : fields) {
            //todo
            /**
             * Add some codes here.
             *
             * Refer to manual for details.
             */
            //常量按常量的规格找值，别的丢去初始化
            if(f.isStatic()){
                if(f.isFinal()){
                    loadValueFromRTCP(clazz,f);
                }else {
                    initDefaultValue(clazz,f);
                }
            }
        }
    }
}
