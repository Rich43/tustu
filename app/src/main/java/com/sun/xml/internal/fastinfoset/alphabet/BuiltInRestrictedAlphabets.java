package com.sun.xml.internal.fastinfoset.alphabet;

import com.sun.xml.internal.org.jvnet.fastinfoset.RestrictedAlphabet;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/alphabet/BuiltInRestrictedAlphabets.class */
public final class BuiltInRestrictedAlphabets {
    public static final char[][] table = new char[2];

    /* JADX WARN: Type inference failed for: r0v1, types: [char[], char[][]] */
    static {
        table[0] = RestrictedAlphabet.NUMERIC_CHARACTERS.toCharArray();
        table[1] = RestrictedAlphabet.DATE_TIME_CHARACTERS.toCharArray();
    }
}
