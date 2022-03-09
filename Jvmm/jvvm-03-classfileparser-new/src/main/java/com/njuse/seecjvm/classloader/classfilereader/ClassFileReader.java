package com.njuse.seecjvm.classloader.classfilereader;

import com.njuse.seecjvm.classloader.classfilereader.classpath.DirEntry;
import com.njuse.seecjvm.classloader.classfilereader.classpath.Entry;
import com.njuse.seecjvm.classloader.classfilereader.classpath.EntryType;
import com.njuse.seecjvm.util.PathUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;

/**
 * This class is the simulated implementation of Java Classloader.
 */
@Getter
@Setter
public class ClassFileReader {
    private static ClassFileReader reader = new ClassFileReader();
    private static final String FILE_SEPARATOR = File.separator;
    private static final String PATH_SEPARATOR = File.pathSeparator;

    private ClassFileReader() {
    }

    public static ClassFileReader getInstance() {
        return reader;
    }

    private static Entry userClasspath = null;//user class entry

    public static void setUserClasspath(String classpath) {
        userClasspath = chooseEntryType(classpath);
    }

    /**
     * select Entry by type of classpath
     */
    public static Entry chooseEntryType(String classpath) {
        return new DirEntry(classpath);
    }

    /**
     * read class file in privilege order
     * USER_ENTRY has highest privileges
     * If there is no relevant class loaded before
     * default privilege is USER_ENTRY
     *
     * @param className class to be read
     * @param privilege privilege of relevant class
     * @return content of class file and the privilege of loaded class
     */
    public Pair<byte[], Integer> readClassFile(String className, EntryType privilege) throws IOException, ClassNotFoundException {
        int value = (privilege == null) ? EntryType.USER_ENTRY : privilege.getValue();
        String realClassName = className + ".class";
        realClassName = PathUtil.transform(realClassName);
        byte[] data;
        if (value >= EntryType.USER_ENTRY) {
            if ((data = userClasspath.readClass(realClassName)) != null) {
                return Pair.of(data, EntryType.USER_ENTRY);
            }
        }
        throw new ClassNotFoundException();
    }
}
