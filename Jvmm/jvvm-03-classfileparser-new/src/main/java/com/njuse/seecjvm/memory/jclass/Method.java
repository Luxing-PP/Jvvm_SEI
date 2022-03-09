package com.njuse.seecjvm.memory.jclass;

import com.njuse.seecjvm.classloader.classfileparser.MethodInfo;
import com.njuse.seecjvm.classloader.classfileparser.attribute.CodeAttribute;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Getter
@Setter
public class Method extends ClassMember {
    private int maxStack;
    private int maxLocal;
    private int argc;
    private byte[] code;

    public Method(MethodInfo info, JClass clazz) {
        this.clazz = clazz;
        accessFlags = info.getAccessFlags();
        name = info.getName();
        descriptor = info.getDescriptor();

        CodeAttribute codeAttribute = info.getCodeAttribute();
        if (codeAttribute != null) {
            maxLocal = codeAttribute.getMaxLocal();
            maxStack = codeAttribute.getMaxStack();
            code = codeAttribute.getCode();
        }
        argc = calculateArgcFromDescriptor(descriptor);
    }
    //todo calculateArgcFromDescriptor
    private int calculateArgcFromDescriptor(String descriptor) {
        /**
         * Add some codes here.
         * Here are some examples in README!!!
         *
         * You should refer to JVM specification for more details
         *
         * Beware of long and double type
         */
        int count=0;
        boolean isArrayType=false;
        boolean isObjectType=false;
        boolean haveInnerObject=false;

        char[] cut_descriptor;
        int j=descriptor.indexOf(")");
        System.out.println(descriptor.substring(1,j));
        cut_descriptor = descriptor.substring(1,j).toCharArray();

        if(cut_descriptor.length==0){
            return 0;
        }
        //不会正则枯了
        for(int i=0;i<cut_descriptor.length;i++){
            if(isArrayType){
                if(haveInnerObject){
                    if(cut_descriptor[i]==';'){
                        count++;
                        isArrayType=false;
                        haveInnerObject=false;
                    }
                }else if(cut_descriptor[i]!='['){
                    if(cut_descriptor[i]=='L'){
                        haveInnerObject=true;
                    }else {
                        count++;
                        isArrayType=false;
                    }
                }
            }else if(isObjectType){
                if(cut_descriptor[i]==';'){
                    count++;
                    isObjectType=false;
                }
            }else if(isOneLenElement(cut_descriptor[i])){
                count++;
            }else if(isTwoLenElement(cut_descriptor[i])){
                count+=2;
            }else if(cut_descriptor[i]=='['){
                isArrayType=true;
            }else if(cut_descriptor[i]=='L'&&!isArrayType){
                isObjectType=true;
            }else {
                System.out.println("Error in calculate");
            }
        }

        return count;
    }

    private boolean isOneLenElement(char c){
        if (c=='B'||c=='C'||c=='I'||c=='F'||c=='Z'||c=='S'){
            return true;
        }
        return false;
    }
    private boolean isTwoLenElement(char c){
        if (c=='D'||c=='J'){
            return true;
        }
        return false;
    }
}
