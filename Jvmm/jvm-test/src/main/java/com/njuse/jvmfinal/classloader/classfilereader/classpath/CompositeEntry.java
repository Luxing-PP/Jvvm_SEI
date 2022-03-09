package com.njuse.jvmfinal.classloader.classfilereader.classpath;

import com.njuse.jvmfinal.classloader.classfilereader.ClassFileReader;

import java.io.IOException;

/**
 * format : dir/subdir;dir/subdir/*;dir/target.jar*
 */
public class CompositeEntry extends Entry{
    public CompositeEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String[] classpaths = classpath.split(PATH_SEPARATOR);
        for(int i=0;i<classpaths.length;i++){
            byte[] target= ClassFileReader.chooseEntryType(classpaths[i]).readClassFile(className);
            if(target!=null){
                return target;
            }
        }
        return null;
    }
}
