package com.sun.jna;

import java.nio.CharBuffer;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/WString.class */
public final class WString implements CharSequence, Comparable {
    private String string;

    public WString(String s2) {
        if (s2 == null) {
            throw new NullPointerException("String initializer must be non-null");
        }
        this.string = s2;
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.string;
    }

    public boolean equals(Object o2) {
        return (o2 instanceof WString) && toString().equals(o2.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(Object o2) {
        return toString().compareTo(o2.toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return toString().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return toString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return CharBuffer.wrap(toString()).subSequence(start, end);
    }
}
