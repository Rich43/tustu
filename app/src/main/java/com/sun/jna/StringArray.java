package com.sun.jna;

import com.sun.jna.Function;
import java.util.ArrayList;
import java.util.List;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/StringArray.class */
public class StringArray extends Memory implements Function.PostCallRead {
    private boolean wide;
    private List natives;
    private Object[] original;

    public StringArray(String[] strings) {
        this(strings, false);
    }

    public StringArray(String[] strings, boolean wide) {
        this((Object[]) strings, wide);
    }

    public StringArray(WString[] strings) {
        this((Object[]) strings, true);
    }

    private StringArray(Object[] strings, boolean wide) {
        super((strings.length + 1) * Pointer.SIZE);
        this.natives = new ArrayList();
        this.original = strings;
        this.wide = wide;
        for (int i2 = 0; i2 < strings.length; i2++) {
            Pointer p2 = null;
            if (strings[i2] != null) {
                NativeString ns = new NativeString(strings[i2].toString(), wide);
                this.natives.add(ns);
                p2 = ns.getPointer();
            }
            setPointer(Pointer.SIZE * i2, p2);
        }
        setPointer(Pointer.SIZE * strings.length, null);
    }

    @Override // com.sun.jna.Function.PostCallRead
    public void read() {
        boolean returnWide = this.original instanceof WString[];
        for (int si = 0; si < this.original.length; si++) {
            Pointer p2 = getPointer(si * Pointer.SIZE);
            Object s2 = null;
            if (p2 != null) {
                s2 = p2.getString(0L, this.wide);
                if (returnWide) {
                    s2 = new WString((String) s2);
                }
            }
            this.original[si] = s2;
        }
    }
}
