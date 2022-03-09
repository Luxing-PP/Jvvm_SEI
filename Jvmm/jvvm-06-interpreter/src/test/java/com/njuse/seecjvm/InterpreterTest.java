package com.njuse.seecjvm;

import com.njuse.seecjvm.classloader.ClassLoader;
import com.njuse.seecjvm.classloader.classfilereader.ClassFileReader;
import com.njuse.seecjvm.execution.Interpreter;
import com.njuse.seecjvm.memory.jclass.JClass;
import com.njuse.seecjvm.memory.jclass.Method;
import com.njuse.seecjvm.runtime.JThread;
import com.njuse.seecjvm.runtime.StackFrame;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class InterpreterTest {
    private ClassLoader loader = ClassLoader.getInstance();
    private static String testPath = String.join(File.separator,"src","test","testpath");

    @Before
    public void setClasspath(){
        ClassFileReader.setUserClasspath(testPath);
    }

    @Test
    public void testInvokeStatic() throws ClassNotFoundException {
        try {
            execTest("InvokeStaticTest");
            //should not reach here
            throw new RuntimeException();
        } catch (RuntimeException e) {
            String message = e.getMessage();
            System.out.println(message);
            if (!message.equals("3!=2")) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testInvokeStatic2() throws ClassNotFoundException {
        try {
            execTest("InvokeStaticTest2");
            //should not reach here
            throw new RuntimeException();
        } catch (RuntimeException e) {
            String message = e.getMessage();
            System.out.println(message);
            if (!message.equals("4!=2")) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testInvokeInterface() throws ClassNotFoundException {
        execTest("InvokeInterfaceTest");
    }

    /**
     * 加分项：
     * 通过hack某些类的实现使得这个测试用例不抛出异常！
     */
    @Test
    public void bonus() throws ClassNotFoundException {
        execTest("HackTest");
    }

    @Test
    public void author() throws ClassNotFoundException{
        execTest("LightEasyBranchTest");
    }




    /**
     * 用来执行测试的方法，不用关注细节
     * @param className 被测试类的类名
     */
    private void execTest(String className) throws ClassNotFoundException {
        JClass clazz = loader.loadClass(className, null);
        JThread thread = new JThread();
        Method main = clazz.getMainMethod();
        StackFrame mainFrame = new StackFrame(thread, main, main.getMaxStack(), main.getMaxLocal());
        thread.pushFrame(mainFrame);
        Interpreter.interpret(thread);
    }




}
