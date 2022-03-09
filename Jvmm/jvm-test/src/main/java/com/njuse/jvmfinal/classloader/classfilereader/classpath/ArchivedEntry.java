package com.njuse.jvmfinal.classloader.classfilereader.classpath;

import com.njuse.jvmfinal.util.ColorUtil;
import com.njuse.jvmfinal.util.IOUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ArchivedEntry extends Entry{
    public ArchivedEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        is=null;
        try {
            JarFile jarFile = new JarFile(classpath);
            Enumeration enu = jarFile.entries();
            while (enu.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) enu.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    name=name.substring(0,name.lastIndexOf("."));
                    if(name.equals(className)){
//                        ColorUtil.printBlue(name);
                        is = jarFile.getInputStream(jarEntry);
                        break;
                    }
                }
            }
        } catch (Exception e) {
          e.printStackTrace();
        }

        if(is!=null){
            return IOUtil.readFileByBytes(is);
        }else {
            return null;
        }
    }
}

