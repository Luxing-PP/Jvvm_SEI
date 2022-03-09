package cases.light;

import cases.TestUtil;

public class TestLoadAndStore {
    public int fieldA = 0;
    public int fieldB = 1;
    public int fieldC =100;


    public TestLoadAndStore(int val){
        TestUtil.reach(fieldC);
        fieldC=val;
        TestUtil.reach(fieldC);
    }


    public static void main(String[] args) {

        TestLoadAndStore mytest =new TestLoadAndStore(50);
        TestUtil.reach(mytest.fieldA);
        TestUtil.reach(mytest.fieldB);

        mytest.testConst(4,3,2,1);

    }



    public void testConst(int int1,int int2,int int3,int int4){
        TestUtil.reach(int1);
        TestUtil.reach(int2);
        TestUtil.reach(int3);
        TestUtil.reach(int4);

        if(int1>=int2){
            int3=int1;
            int4=int2;
            TestUtil.reach(int1);
            TestUtil.reach(int2);
            TestUtil.reach(int3);
            TestUtil.reach(int4);
        }
    }
}
