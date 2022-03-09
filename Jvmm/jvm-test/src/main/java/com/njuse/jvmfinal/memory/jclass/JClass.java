package com.njuse.jvmfinal.memory.jclass;

import com.njuse.jvmfinal.classloader.classfileparser.ClassFile;
import com.njuse.jvmfinal.classloader.classfileparser.FieldInfo;
import com.njuse.jvmfinal.classloader.classfileparser.MethodInfo;
import com.njuse.jvmfinal.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.jvmfinal.classloader.classfilereader.classpath.EntryPrivilege;
import com.njuse.jvmfinal.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.runtime.Vars;
import com.njuse.jvmfinal.runtime.struct.NonArrayObject;
import com.njuse.jvmfinal.util.ColorUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class JClass {
    private short accessFlags;
    private String name;
    private String superClassName;
    private String[] interfaceNames;
    private RuntimeConstantPool runtimeConstantPool;
    private Field[] fields;
    private Method[] methods;
    private EntryPrivilege loadEntryType; //请自行设计是否记录、如何记录加载器
    private JClass superClass;
    private JClass[] interfaces;
    private int instanceSlotCount;
    private int staticSlotCount;
    private Vars staticVars; // 请自行设计数据结构,官方文档不就要求了一个数组+能放ojref
    private InitState initState=null; // 请自行设计初始化状态,这有啥好设计的

    //fixme 一次性全部弄完不更香嘛,每次都重复算= =
    public boolean isPublic;
    public boolean isInterface;
    public boolean isSuper;
    public boolean isFinal;
    public boolean isAbstract;
    public boolean isSynthetic;
    public boolean isAnnotation;
    public boolean isEnum;


    public JClass(ClassFile classFile) {
        this.accessFlags = classFile.getAccessFlags();
        this.name = classFile.getClassName();
        if (!this.name.equals("java/lang/Object")) {
            // index of super class of java/lang/Object is 0
            this.superClassName = classFile.getSuperClassName();
        } else {
            this.superClassName = "";
        }
        this.interfaceNames = classFile.getInterfaceNames();
        this.fields = parseFields(classFile.getFields());
        this.methods = parseMethods(classFile.getMethods());
        this.runtimeConstantPool = parseRuntimeConstantPool(classFile.getConstantPool());
        parseAccessFlags(classFile.getAccessFlags());
    }

    private Field[] parseFields(FieldInfo[] info) {
        int len = info.length;
        fields = new Field[len];
        for (int i = 0; i < len; i++) {
            fields[i] = new Field(info[i], this);
        }
        return fields;
    }
    private Method[] parseMethods(MethodInfo[] info) {
        int len = info.length;
        methods = new Method[len];
        for (int i = 0; i < len; i++) {
            methods[i] = new Method(info[i], this);
        }
        return methods;
    }
    private void parseAccessFlags(short accessFlags){
        isPublic = 0 != (accessFlags & AccessFlags.ACC_PUBLIC);
        isFinal = 0 != (accessFlags & AccessFlags.ACC_FINAL);
        isSuper = 0 !=(accessFlags & AccessFlags.ACC_SUPER);
        isInterface = 0 != (accessFlags & AccessFlags.ACC_INTERFACE);
        isAbstract = 0 != (accessFlags & AccessFlags.ACC_ABSTRACT);
        isSynthetic = 0 != (accessFlags & AccessFlags.ACC_SYNTHETIC);
        isAnnotation = 0 != (accessFlags & AccessFlags.ACC_ANNOTATION);
        isEnum = 0 != (accessFlags & AccessFlags.ACC_ENUM);
    }

    private RuntimeConstantPool parseRuntimeConstantPool(ConstantPool cp) {
        return new RuntimeConstantPool(cp, this);
    }

    //
    public String getPackageName() {
        int index = name.lastIndexOf('/');
        if (index >= 0) return name.substring(0, index);
        else return "";
    }

    public void initClass(JThread thread, JClass clazz){
        clazz.initState = InitState.BUSY;
        ColorUtil.printCyan("clazz: "+clazz.getName()+" InitStart");

        // find clinit
        Method clinit = clazz.getInitClassMethod();
        if(clinit!=null){
            StackFrame clinitFrame = new StackFrame(thread,clinit,clinit.getMaxStack(),clinit.getMaxLocal());
            thread.pushFrame(clinitFrame);
        }else {
            ColorUtil.printRed("Error clinit=null in :"+clazz.getName());
        }
        //initSuperclass
        if(!clazz.isInterface){
            JClass superClass = clazz.getSuperClass();
            if(superClass != null && superClass.getInitState()==InitState.PREPARED){
                initClass(thread,superClass);
            }
        }
        clazz.initState = InitState.SUCCESS;
    }


    public Method resolveMethod(String name, String descriptor) {
        for (Method m : methods) {
            if (m.getDescriptor().equals(descriptor) && m.getName().equals(name)) {
                return m;
            }
        }
        return null;
    }


    private Method getMethodInClass(String name, String descriptor, boolean isStatic) {
        JClass clazz = this;
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getDescriptor().equals(descriptor)
                    && m.getName().equals(name)
                    && m.isStatic() == isStatic) {
                return m;
            }
        }
        return null;
    }
    public Method getMainMethod() {
        return getMethodInClass("main", "([Ljava/lang/String;)V", true);
    }
    public Method getInitClassMethod(){
        return getMethodInClass("<clinit>","()V",true);
    }
    public boolean isAccessibleTo(JClass caller) {
        if(isPublic){
            return true;
        }
        if(caller.getPackageName().equals(getPackageName())){
            return true;
        }
        return false;
    }
    public NonArrayObject newObject(){
        return new NonArrayObject(this);
    }
}
