package cases.light;

import cases.TestUtil;

public class TestArray  {
    public static void main(String[] args) {
        int[] array = new int[10];
        array=new int[]{0,1,2,3};
        int m =array[3];
        TestUtil.equalInt(m,3);
    }
}
