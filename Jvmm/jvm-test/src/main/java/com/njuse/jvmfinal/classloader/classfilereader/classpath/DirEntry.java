package com.njuse.jvmfinal.classloader.classfilereader.classpath;

import com.njuse.jvmfinal.util.IOUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * example: /xxx/xxx/cases.light.LightEasyTestUtilTest
 */
public class DirEntry extends Entry {

    public DirEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        String path = IOUtil.transform(classpath+FILE_SEPARATOR+className+".class");
        try{
            is=new FileInputStream(path);
        }catch (FileNotFoundException e){
            return null;
        }

        if((is=new FileInputStream(path))!=null){
            return IOUtil.readFileByBytes(is);
        }else {
            return null;
        }
    }
}
