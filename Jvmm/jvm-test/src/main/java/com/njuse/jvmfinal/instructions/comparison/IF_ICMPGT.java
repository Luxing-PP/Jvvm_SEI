package com.njuse.jvmfinal.instructions.comparison;
//check
public class IF_ICMPGT extends IF_ICMPCOND {
    @Override
    protected boolean condition(int v1, int v2) {
        return v1 > v2;
    }
}
