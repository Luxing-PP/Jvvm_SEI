package edu.nju;

import java.io.*;

/**
 * format : dir/subdir/.../
 */
public class DirEntry extends Entry{
    String className;
    InputStream in;

    public DirEntry(String classpath) {
        super(classpath);
    }

    //todo 貌似要转换绝对路径才对
    @Override
    public byte[] readClassFile(String className) throws IOException {
        this.className=className;
        if(isOk()){
            return IOUtil.readFileByBytes(in);
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isOk() {
        try {
            in = new FileInputStream(classpath+FILE_SEPARATOR+className);
        }catch (FileNotFoundException exp){
            return false;
        }
        return true;
    }
}
