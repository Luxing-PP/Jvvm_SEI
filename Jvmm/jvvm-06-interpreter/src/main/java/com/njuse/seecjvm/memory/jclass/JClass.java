package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.ClassLoader;
import com.njuse.seecjvm.classloader.classfileparser.ClassFile;
import com.njuse.seecjvm.classloader.classfileparser.FieldInfo;
import com.njuse.seecjvm.classloader.classfileparser.MethodInfo;
import com.njuse.seecjvm.classloader.classfileparser.constantpool.ConstantPool;
import com.njuse.seecjvm.classloader.classfilereader.classpath.EntryType;
import com.njuse.seecjvm.execution.Interpreter;
import com.njuse.seecjvm.memory.jclass.runtimeConstantPool.RuntimeConstantPool;
import com.njuse.seecjvm.runtime.JThread;
import com.njuse.seecjvm.runtime.StackFrame;
import com.njuse.seecjvm.runtime.Vars;
import com.njuse.seecjvm.runtime.struct.NonArrayObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Optional;

@Getter
@Setter
public class JClass {
    private short accessFlags;
    private String name;
    private String superClassName;
    private String[] interfaceNames;
    private RuntimeConstantPool runtimeConstantPool;
    private Field[] fields;//
    private Method[] methods;//
    private EntryType loadEntryType;
    private JClass superClass;
    private JClass[] interfaces;
    private int instanceSlotCount;
    private int staticSlotCount;
    private Vars staticVars;
    private InitState initState;

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
    }

    //used for new array class!!!
    public JClass() {

    }

    public Optional<Method> resolveMethod(String name, String descriptor) {
        for (Method m : methods) {
            if (m.getDescriptor().equals(descriptor) && m.getName().equals(name)) {
                return Optional.of(m);
            }
        }
        return Optional.empty();
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

    private RuntimeConstantPool parseRuntimeConstantPool(ConstantPool cp) {
        return new RuntimeConstantPool(cp, this);
    }

    public NonArrayObject newObject() {
        return new NonArrayObject(this);
    }

    public JClass getComponentClass() {
        if (this.name.charAt(0) != '[') throw new RuntimeException("Invalid Array:" + this.name);
        ClassLoader loader = ClassLoader.getInstance();
        String componentTypeDescriptor = this.name.substring(1);
        String classToLoad = null;
        if (componentTypeDescriptor.charAt(0) == '[') {
            classToLoad = componentTypeDescriptor;
        } else if (componentTypeDescriptor.charAt(0) == 'L') {
            //remove first and last char 'L' and ';'
            classToLoad = componentTypeDescriptor.substring(1, componentTypeDescriptor.length() - 1);
        } else if (getPrimitiveType() != null) {
            classToLoad = getPrimitiveType();
        }
        try {
            return loader.loadClass(classToLoad, this.loadEntryType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Cannot load arrayClass:" + classToLoad);
    }

    /**
     * @return null if this classname is not a primitive type
     */
    private String getPrimitiveType() {
        HashMap<String, String> primitiveType = new HashMap<>();
        primitiveType.put("void", "V");
        primitiveType.put("boolean", "Z");
        primitiveType.put("byte", "B");
        primitiveType.put("short", "S");
        primitiveType.put("char", "C");
        primitiveType.put("int", "I");
        primitiveType.put("long", "J");
        primitiveType.put("float", "F");
        primitiveType.put("double", "D");
        return primitiveType.get(this.name);
    }

    /**
     * Class Init Methods
     */

    //if in multi-thread, jclass need a initstate lock
    private void initStart(JClass clazz) {
        clazz.initState = InitState.BUSY;
    }

    private void initSucceed(JClass clazz) {
        clazz.initState = InitState.SUCCESS;
    }

    private void initFail() {
        this.initState = InitState.FAIL;
    }

    /**
     * TODO 实现这个方法： 我有点不懂什么是后进先出了
     * 这个方法初始化了这个类的静态部分
     */
    public void initClass(JThread thread, JClass clazz) {
        //FIXME 1. Synchronize on the initialization lock, LC, for C. This involves waiting until the current thread can acquire LC.
//        switch (clazz.getInitState()){
//            case BUSY:
////                initClass(thread,clazz);
//                System.out.println("Busy"+clazz.name);
//                return;
//            case FAIL:
//                System.out.println("LCNoClassDefFoundError");
//                return;
//            case SUCCESS:
//                return;
//            case PREPARED:
//                System.out.println("clazzName="+clazz.name);
//                Method clinit = this.getInitClassMethod();
//                if(clinit!=null){
//                    StackFrame clinitFrame = new StackFrame(thread,clinit,clinit.getMaxStack(),clinit.getMaxLocal());
//                    thread.pushFrame(clinitFrame);
//                }
//                break;
//        }
        //prepared
        //fixme 没有interface?
//        if(!clazz.isInterface()){
//            JClass SC = clazz.getSuperClass();
//            JClass[] SIs = clazz.getInterfaces();
//            if(SC!=null){
//                SC.initClass(thread,SC);
//            }
//            for (JClass SI:SIs){
//                SI.initClass(thread,SI);
//            }
//        }else {
//            JClass[] SIss = clazz.getInterfaces();
//            if(SIss!=null){
//                for(JClass SI:SIss){
//                    SI.initClass(thread,SI);
//                }
//            }
//        }
        initStart(clazz);
        System.out.println("!!! clazz: "+clazz.getName()+"initStart");
        // find clinit
        Method clinit = this.getInitClassMethod();
        if(clinit!=null){
            StackFrame clinitFrame = new StackFrame(thread,clinit,clinit.getMaxStack(),clinit.getMaxLocal());
            thread.pushFrame(clinitFrame);
        }
        //initSuperclass
        if(!clazz.isInterface()){
            JClass superClass = clazz.getSuperClass();
            if(superClass != null && superClass.getInitState()==InitState.PREPARED){
                initClass(thread,superClass);
            }
        }
        initSucceed(clazz);
    }



    /**
     * search method in class and its superclass
     *
     * @return
     */
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


    /**
     * Get extra Info
     */

    public String getPackageName() {
        int index = name.lastIndexOf('/');
        if (index >= 0) return name.substring(0, index);
        else return "";
    }

    public boolean isPublic() {
        return 0 != (this.accessFlags & AccessFlags.ACC_PUBLIC);
    }

    public boolean isInterface() {
        return 0 != (this.accessFlags & AccessFlags.ACC_INTERFACE);
    }

    public boolean isAbstract() {
        return 0 != (this.accessFlags & AccessFlags.ACC_ABSTRACT);
    }

    public boolean isAccSuper() {
        return 0 != (this.accessFlags & AccessFlags.ACC_SUPER);
    }

    public boolean isArray() {
        return this.name.charAt(0) == '[';
    }

    public boolean isJObjectClass() {
        return this.name.equals("java/lang/Object");
    }

    public boolean isJlCloneable() {
        return this.name.equals("java/lang/Cloneable");
    }

    public boolean isJIOSerializable() {
        return this.name.equals("java/io/Serializable");
    }

    public boolean isAccessibleTo(JClass caller) {
        boolean isPublic = isPublic();
        boolean inSamePackage = this.getPackageName().equals(caller.getPackageName());
        return isPublic || inSamePackage;
    }

    //refer to jvm8 6.5 instanceof inst
    public boolean isAssignableFrom(JClass other) {
        JClass s = other;
        JClass t = this;
        if (s == t) return true;
        if (!s.isArray()) {
            if (!s.isInterface()) {
                if (!t.isInterface()) {
                    return s.isSubClassOf(t);
                } else {
                    return s.isImplementOf(t);
                }
            } else {
                if (!t.isInterface()) {
                    return t.isJObjectClass();
                } else {
                    return t.isSuperInterfaceOf(s);
                }
            }
        } else {
            if (!t.isArray()) {
                if (!t.isInterface()) {
                    return t.isJObjectClass();
                } else {
                    return t.isJIOSerializable() || t.isJlCloneable();
                }
            } else {
                JClass sc = s.getComponentClass();
                JClass tc = t.getComponentClass();
                return sc == tc || t.isJIOSerializable();
            }
        }
    }

    private boolean isSubClassOf(JClass otherClass) {
        JClass superClass = this.getSuperClass();
        while (superClass != null) {
            if (superClass == otherClass) return true;
            superClass = superClass.getSuperClass();
        }
        return false;
    }

    private boolean isImplementOf(JClass otherInterface) {
        JClass superClass = this;
        while (superClass != null) {
            for (JClass i : this.getInterfaces()) {
                if (i == otherInterface || i.isSubInterfaceOf(otherInterface)) return true;
            }
            superClass = this.getSuperClass();
        }
        return false;
    }

    private boolean isSubInterfaceOf(JClass otherInterface) {
        JClass[] superInterfaces = this.getInterfaces();
        for (JClass i : superInterfaces) {
            if (i == otherInterface || i.isSubInterfaceOf(otherInterface)) return true;
        }
        return false;
    }

    private boolean isSuperInterfaceOf(JClass otherInterface) {
        return otherInterface.isSubInterfaceOf(this);
    }
}
