package com.njuse.jvmfinal.classloader.classfilereader;

import com.njuse.jvmfinal.classloader.classfilereader.classpath.*;
import com.njuse.jvmfinal.util.ColorUtil;
import com.njuse.jvmfinal.util.IOUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;

/**
 * 应该只用于初始化
 */
public class ClassFileReader {
    public static String Tag="ClassFileReader: ";
    private static ClassFileReader classFileReader = new ClassFileReader();
    private static final String FILE_SEPARATOR = File.separator;
    private static final String PATH_SEPARATOR = File.pathSeparator;
    private static Entry bootClasspath = null;//bootstrap class entry
    private static Entry extClasspath = null;//extension class entry
    private static Entry userClasspath = null;//user class entry

    static {
//        bootClasspath=chooseEntryType(System.getProperty("java.home")+FILE_SEPARATOR+"lib");
        bootClasspath=chooseEntryType(System.getProperty("java.home")+FILE_SEPARATOR+"lib"+FILE_SEPARATOR+"rt.jar");
        extClasspath=chooseEntryType(System.getProperty("java.home")+FILE_SEPARATOR+"lib"+FILE_SEPARATOR+"ext");
    }
    public static void setUserClasspath(String classpath) {
        userClasspath = chooseEntryType(classpath);
    }

    public static Entry chooseEntryType(String classpath) {
        String[] split_cp = classpath.split("\\.");
        if(classpath.contains(File.pathSeparator)){
            return new CompositeEntry(classpath);
        } else if(classpath.contains("*")){
            return new WildEntry(classpath);
        } else if (split_cp[split_cp.length-1].equals("jar")||
                split_cp[split_cp.length-1].equals("JAR")||
                split_cp[split_cp.length-1].equals("zip")||
                split_cp[split_cp.length-1].equals("ZIP")){
            return new ArchivedEntry(classpath);
        } else {
            return new DirEntry(classpath);
        }
}
    public ClassFileReader getInstance(){
        return classFileReader;
    }

    /**
     * 照搬的双亲加载,改成了static暂时没发现有啥问题
     * @param className
     * @param privilege
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Pair<byte[], Integer> readClassFile(String className, EntryPrivilege privilege) throws IOException, ClassNotFoundException {
        className = IOUtil.transformClassName(className);
        int value = (privilege == null) ? EntryPrivilege.USER_ENTRY : privilege.getValue();
        byte[] data;
        if (value>=EntryPrivilege.BOOT_ENTRY){
            if((data= bootClasspath.readClassFile(className))!=null){
//                ColorUtil.printGreen(className+" is found in boot");
                return Pair.of(data,EntryPrivilege.BOOT_ENTRY);
            }
        }
        if (value>= EntryPrivilege.EXT_ENTRY) {
            if ((data = extClasspath.readClassFile(className)) != null) {
//                ColorUtil.printGreen(className+" is found in ext");
                return Pair.of(data, EntryPrivilege.EXT_ENTRY);
            }
        }
        if (value>= EntryPrivilege.USER_ENTRY) {
            if ((data = userClasspath.readClassFile(className)) != null) {
//                ColorUtil.printGreen(className+" is found in use");
                return Pair.of(data, EntryPrivilege.USER_ENTRY);
            }
        }
        throw new ClassNotFoundException();
    }
}
