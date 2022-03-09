package com.njuse.jvmfinal.instructions.load;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;

public abstract class LOAD_N extends NoOperandsInstruction {
    protected int index;
    private static int[] valid = {0, 1, 2, 3};

    public LOAD_N(int i){
        checkIndex(i);
        this.index=i;
    }

    public static void checkIndex(int i) {
        for (int m :valid){
            if(m == i){
                return;
            }
        }
        throw new RuntimeException();
    }


    @Override
    public String toString() {
        String suffix = index + "";
        return this.getClass().getSimpleName().replace("N", suffix);
    }
}
