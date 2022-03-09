interface Printable{
    int getMyNumber();
}



class WYM implements Printable{
    @java.lang.Override
    public int getMyNumber() {
        return 0;
    }
}

class XXZ implements Printable{
    @java.lang.Override
    public int getMyNumber() {
        return 1;
    }
}

public class HackTest{
    public static void main(String[] args) {
        Printable ym = new WYM();
        Printable xz = new XXZ();
        //没有hack之前这里会抛出异常，因为wym的number是0而xxz是1
        TestUtil.equalInt(ym.getMyNumber(), xz.getMyNumber());
        TestUtil.equalInt(1, 1);
    }
}