package com.njuse.jvmfinal.instructions.store;


import com.njuse.jvmfinal.instructions.base.NoOperandsInstruction;

public abstract class STORE_N extends NoOperandsInstruction {
    protected int index;
    private static int[] valid = {0, 1, 2, 3};

    protected void setIndex(int i) {
        assert (i >= valid[0] && i <= valid[valid.length - 1]);
        this.index=i;
    }

    @Override
    public String toString() {
        String suffix = index + "";
        return this.getClass().getSimpleName().replace("N", suffix);
    }
}
