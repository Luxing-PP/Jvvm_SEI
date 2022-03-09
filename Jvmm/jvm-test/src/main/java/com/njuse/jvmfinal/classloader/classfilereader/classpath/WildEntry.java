package com.njuse.jvmfinal.classloader.classfilereader.classpath;



import com.njuse.jvmfinal.util.IOUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class WildEntry extends Entry{
    String classname="";
    String CompositePath="";
    ArrayList<String> classpaths = new ArrayList<>();


    public WildEntry(String classpath) {
        super(classpath);
    }

    @Override
    public byte[] readClassFile(String className) throws IOException {
        this.classname=className;
        if(isOk()){
            classpath+=FILE_SEPARATOR;
            System.out.println(classpath);

            for(int i=0;i<classpaths.size()-1;i++){
                CompositePath+=classpath+classpaths.get(i)+PATH_SEPARATOR;
            }
            CompositePath+=classpath+classpaths.get(classpaths.size()-1);

            return new CompositeEntry(IOUtil.transform(CompositePath)).readClassFile(classname);
        }else {
            return null;
        }
    }

    public boolean isOk() {
        //这里要保留最后的那个\不然找不到文件（但在本地测试却可以找出来= =）
        classpath=classpath.replace("*","");
        classpath=IOUtil.transform(classpath);

        String[] nameList = new File(classpath).list();

        if(nameList==null){
            return false;
        }

        for (String name:nameList){
            String[] splitName = name.split("\\.");
            if(     splitName[splitName.length-1].equals("JAR")||
                    splitName[splitName.length-1].equals("jar")||
                    splitName[splitName.length-1].equals("ZIP")||
                    splitName[splitName.length-1].equals("zip")
            ){
                classpaths.add(name);
            }
        }

        if(classpaths.size()<1){
            return false;
        }else {
            return true;
        }
    }
}
