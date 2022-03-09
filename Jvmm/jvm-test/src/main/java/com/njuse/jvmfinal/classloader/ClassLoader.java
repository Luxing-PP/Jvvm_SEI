package com.njuse.jvmfinal.classloader;

import com.njuse.jvmfinal.classloader.classfileparser.ClassFile;
import com.njuse.jvmfinal.classloader.classfilereader.ClassFileReader;
import com.njuse.jvmfinal.classloader.classfilereader.classpath.EntryPrivilege;
import com.njuse.jvmfinal.memory.MethodArea;
import com.njuse.jvmfinal.memory.jclass.Field;
import com.njuse.jvmfinal.memory.jclass.InitState;
import com.njuse.jvmfinal.memory.jclass.JClass;

import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.DoubleWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.FloatWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.IntWrapper;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.constant.wrapper.LongWrapper;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.NullObject;
import com.njuse.jvmfinal.util.ColorUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public class ClassLoader {
    private static ClassLoader classLoader = new ClassLoader();

    //因为ClassLoader既不是静态的但又承担静态的功能
    public static ClassLoader getInstance() {
        return classLoader;
    }


    public JClass loadClass(String className, EntryPrivilege initiatingEntry) throws ClassNotFoundException {
        JClass ret;
        if ((ret = MethodArea.findClass(className)) == null) {
            return loadNonArrayClass(className, initiatingEntry);
        }
        return ret;
    }

    //返回的这个JClass 相当于在内存区生成了对象
    private JClass loadNonArrayClass(String className, EntryPrivilege initiatingEntry) throws ClassNotFoundException {
        try {
            Pair<byte[], Integer> res = ClassFileReader.readClassFile(className, initiatingEntry);
            byte[] data = res.getKey();
            EntryPrivilege definingEntry = new EntryPrivilege(res.getValue());
            //define class
            JClass clazz = defineClass(data, definingEntry);
            //link class
            ClassLinker linker = new ClassLinker(clazz);
            clazz = linker.linkClass();
            return clazz;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException();
        }
    }

    private JClass defineClass(byte[] data, EntryPrivilege definingEntry) throws ClassNotFoundException {
        ClassFile classFile = new ClassFile(data);
        JClass clazz = new JClass(classFile);
        clazz.setLoadEntryType(definingEntry);

        resolveSuperClass(clazz);
        resolveInterfaces(clazz);
        MethodArea.addClass(clazz.getName(), clazz);
        return clazz;
    }

    private void resolveSuperClass(JClass clazz) throws ClassNotFoundException {
        if (!clazz.getName().equals("java/lang/Object")) {
            String superName = clazz.getSuperClassName();
            clazz.setSuperClass(loadClass(superName, clazz.getLoadEntryType()));
        }
    }
    private void resolveInterfaces(JClass clazz) throws ClassNotFoundException {
        String[] myInterfaces = clazz.getInterfaceNames();
        JClass[] interfaces = new JClass[myInterfaces.length];
        for (int i = 0; i < myInterfaces.length; i++) {
            interfaces[i] = loadClass(myInterfaces[i], clazz.getLoadEntryType());
        }
        clazz.setInterfaces(interfaces);
    }

}
