package com.njuse.jvmfinal.classloader.classfilereader.classpath;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class Entry {
    public final String PATH_SEPARATOR = File.pathSeparator;
    public final String FILE_SEPARATOR = File.separator;
    public String classpath;
    public InputStream is=null;

    public Entry(String classpath){
        this.classpath = classpath;
    }
    public abstract byte[] readClassFile(String className) throws IOException;
}
