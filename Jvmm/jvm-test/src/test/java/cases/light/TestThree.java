package cases.light;

import cases.TestUtil;

public class TestThree {
    public int fieldA = 0;
    public int fieldB = 1;
    public int fieldC = 100;

    public int small=-1;
    public int middle=0;
    public int big=1;

    public int good=777;
    public int fail=7;

    public static void main(String[] args) {

        TestThree mytest =new TestThree();



        int good=mytest.testIINC();
        TestUtil.equalInt(good,2);
        mytest.testCompare();

        mytest=null;
        if(mytest==null){
            TestUtil.reach(9999);
        }
    }

    public int testIINC(){
        fieldA++;
        TestUtil.equalInt(fieldA,1);
        fieldA=fieldA+1;
        TestUtil.equalInt(fieldA,2);
        return fieldA;
    }

    public void testCompare(){
        if(small<big){
            TestUtil.reach(good);
        }else if(middle>=big){
            TestUtil.reach(8888);
        }

        if(small==small){
            TestUtil.reach(good);
        }
        if(small>big){
            myFail();
        }
        if(middle<=small){
            myFail();
        }
    }

    public void myFail(){
        TestUtil.reach(fail);
    }
}
