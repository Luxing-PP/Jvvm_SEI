package cases.light;

import cases.TestUtil;

public class TestFour {

    public static void main(String[] args) {
        TestFour mytest = new TestFour();
        mytest.testIINC();
        mytest.testif();
    }

    public void testIINC(){
        int i=1;
        i=i++;
        TestUtil.equalInt(i,2);
        i++;
        TestUtil.equalInt(i,3);
        ++i;
        TestUtil.equalInt(i,4);

        for(int m=0;m<5;m++){
            TestUtil.reach(m);
        }
    }
    public void testif(){
        if(-1<-2){
            TestUtil.reach(777);
        }else if(0>=1){
            TestUtil.reach(8888);
        }
    }
}
