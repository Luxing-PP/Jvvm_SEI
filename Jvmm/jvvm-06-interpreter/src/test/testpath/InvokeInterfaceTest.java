
interface Human{
    int bar();
}

class Parent {
    public int foo() {
        return 0;
    }
}

class Child extends Parent implements Human {
    public int foo() {
        return 1;
    }

    @Override
    public int bar() {
        return foo();
    }
}

class Boy extends Child implements Human {

    @Override
    public int bar() {
        return 3;
    }
}

public class InvokeInterfaceTest{
    public static void main(String[] args) {
        //invoke interface
        Human h = new Boy();
        TestUtil.equalInt(h.bar(), 3);
        //invoke interface
        Human h1 = new Child();
        TestUtil.equalInt(h1.bar(), 1);
    }
}