package edu.nju;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.*;

/**
 * format : dir/subdir/target.jar
 */
public class ArchivedEntry extends Entry{
    String source_path =String.join(FILE_SEPARATOR,"src","test","testfilepath","dir")+FILE_SEPARATOR;
    InputStream in;
    public ArchivedEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        try {
            if(isOk()){
//                in = new FileInputStream(source_path);
                return IOUtil.readFileByBytes(in);
            }
            else {
                return null;
            }
        }catch (FileNotFoundException exp){
            return null;
        }
    }

    public boolean isOk(){
        try {
            JarFile jarFile = new JarFile(classpath);
            Enumeration enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enu.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    in = jarFile.getInputStream(jarEntry);

                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
