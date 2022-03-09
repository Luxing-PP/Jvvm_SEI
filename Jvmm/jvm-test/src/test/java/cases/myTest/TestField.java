package cases.myTest;

import cases.TestUtil;

public class TestField {
    int fieldA;
    int fieldB=5;


    public static void main(String[] args) {
        TestUtil.reach(-3);
        TestField mytest = new TestField();
        mytest.getField();
        mytest.putField();
        TestUtil.reach(-4);
    }

    private void getField(){
        int m=fieldB;
        TestUtil.equalInt(m,4);
    }

    private void putField(){
        fieldA=10;
        TestUtil.equalInt(fieldA,0);
    }
}
