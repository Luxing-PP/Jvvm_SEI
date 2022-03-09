package com.njuse.jvmfinal.classloader.classfilereader.classpath;

public class EntryPrivilege {
    private int value;
    public final static int BOOT_ENTRY = 0x1;
    public final static int EXT_ENTRY = 0x3;
    public final static int USER_ENTRY = 0x7;

    public EntryPrivilege(int value) { this.value = value; }

    public int getValue() {
        return value;
    }
}
