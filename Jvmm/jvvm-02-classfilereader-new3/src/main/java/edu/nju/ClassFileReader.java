package edu.nju;

import java.io.File;
import java.io.IOException;

public class ClassFileReader {
    private static final String FILE_SEPARATOR = File.separator;
    private static final String PATH_SEPARATOR = File.pathSeparator;

    private static Entry bootStrapReader;

    public static Entry chooseEntryType(String classpath) {
        /**
         * tips:
         *      Every case can correspond to a class
         *      These cases are disordered
         *      You should take care of the order of if-else
         * case 1 classpath with wildcard
         * case 2 normal dir path
         * case 3 archived file
         * case 4 classpath with path separator
         */
        if(classpath.contains(PATH_SEPARATOR)){
            return new CompositeEntry(classpath);
        }
        else if(classpath.contains("*")){
            return new WildEntry(classpath);
        }
        else if(!classpath.contains(".")){
            return new DirEntry(classpath);
        }
        else {
            String[] split_cp = classpath.split("\\.");
            if(     split_cp[split_cp.length-1].equals("jar")||
                    split_cp[split_cp.length-1].equals("JAR")||
                    split_cp[split_cp.length-1].equals("zip")||
                    split_cp[split_cp.length-1].equals("ZIP")
            ){
                return new ArchivedEntry(classpath);
            }else {
                System.out.println("Error!0");
                return null;
            }
        }
    }

    /**
     *
     * @param classpath where to find target class
     * @param className format: /package/.../class
     * @return content of classfile
     */
    public static byte[] readClassFile(String classpath,String className) throws ClassNotFoundException{
        //1. test null
        if(className==null||classpath==null){
            throw new ClassNotFoundException();
        }

        className = IOUtil.transform(className);
        className += ".class";

        //TEST ONLY
        System.out.println(className);
        System.out.println(classpath);
        //END

        bootStrapReader = chooseEntryType(classpath);
        byte[] ret = new byte[0];
        try {
            ret = bootStrapReader.readClassFile(className);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (ret==null)throw new ClassNotFoundException();
        return ret;
    }

    //for test only
    public static void main(String[] args){
        System.out.println(IOUtil.transform("good/me"));
    }
}
