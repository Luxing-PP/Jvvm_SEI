package com.njuse.jvmfinal;


import com.njuse.jvmfinal.classloader.ClassLoader;
import com.njuse.jvmfinal.classloader.classfilereader.ClassFileReader;
import com.njuse.jvmfinal.execution.Interpreter;
import com.njuse.jvmfinal.memory.jclass.JClass;
import com.njuse.jvmfinal.memory.jclass.Method;
import com.njuse.jvmfinal.runtime.JThread;
import com.njuse.jvmfinal.runtime.StackFrame;
import com.njuse.jvmfinal.util.ColorUtil;


import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Starter {

    public static void main(String[] args) {
//        Decoder.check();

        String cp = String.join(File.separator, "src", "test", "java");
        Starter.runTest("cases.light.TestField",cp);
//        Starter.runTest("cases.light.TestField",cp);
//        try {
//            Starter.runTest("cases.light.LightEasyTestUtilTest", cp);
//            fail("You should throw an exception but you were not.");
//        } catch (RuntimeException e) {
//            String message = e.getMessage();
//            assertEquals("Your exception has wrong output: ", "7!=6", message);
//        }
//
//        Starter.runTest("cases.light.LightEasyStaticTest", cp);
//
//        Starter.runTest("cases.light.LightEasyBranchTest", cp);

//        runTest("cases.light.LightEasyBranchTest", cp);
    }

    /**
     * ⚠️警告：不要改动这个方法签名，这是和测试用例的唯一接口
     */
    public static void runTest(String mainClassName, String cp) {
        //1.设置环境变量
        //2.初始化主类
        ClassFileReader.setUserClasspath(cp);
        JClass clazz=null;

        try{
            clazz=ClassLoader.getInstance().loadClass(mainClassName,null);
        }catch (Exception e){
            ColorUtil.printRed("Error! in First Load");
        }


        JThread thread = new JThread();

        //启动类的初始化
        clazz.initClass(thread,clazz);
        Interpreter.interpret(thread);

        //正式执行
        Method main = clazz.getMainMethod();
        StackFrame mainFrame = new StackFrame(thread, main, main.getMaxStack(), main.getMaxLocal());
        thread.pushFrame(mainFrame);
        Interpreter.interpret(thread);
    }

}
