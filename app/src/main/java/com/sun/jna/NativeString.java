package com.sun.jna;

import java.nio.CharBuffer;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/NativeString.class */
class NativeString implements CharSequence, Comparable {
    private Pointer pointer;
    private boolean wide;

    public NativeString(String string) {
        this(string, false);
    }

    public NativeString(String string, boolean wide) {
        if (string == null) {
            throw new NullPointerException("String must not be null");
        }
        this.wide = wide;
        if (wide) {
            int len = (string.length() + 1) * Native.WCHAR_SIZE;
            this.pointer = new Memory(len);
            this.pointer.setString(0L, string, true);
        } else {
            byte[] data = Native.getBytes(string);
            this.pointer = new Memory(data.length + 1);
            this.pointer.write(0L, data, 0, data.length);
            this.pointer.setByte(data.length, (byte) 0);
        }
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public boolean equals(Object other) {
        return (other instanceof CharSequence) && compareTo(other) == 0;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.pointer.getString(0L, this.wide);
    }

    public Pointer getPointer() {
        return this.pointer;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return toString().length();
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return CharBuffer.wrap(toString()).subSequence(start, end);
    }

    @Override // java.lang.Comparable
    public int compareTo(Object other) {
        if (other == null) {
            return 1;
        }
        return toString().compareTo(other.toString());
    }
}
