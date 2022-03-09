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
    public void testJmp() throws ClassNotFoundException {
        execTest("JmpTest");
    }

    @Test
    public void testConditions() throws ClassNotFoundException {
        execTest("ConditionTest");
    }

    @Test
    public void testMath() throws ClassNotFoundException {
        execTest("MathTest");
    }

    /**
     * 这个用例是选做的加分项！
     * 大家按需完成即可( ´▽` )ﾉ
     */
    @Test
    public void testConversion() throws ClassNotFoundException {
        execTest("ConversionTest");
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
