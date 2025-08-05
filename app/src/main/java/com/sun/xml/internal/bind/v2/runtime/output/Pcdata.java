package com.sun.xml.internal.bind.v2.runtime.output;

import java.io.IOException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/Pcdata.class */
public abstract class Pcdata implements CharSequence {
    public abstract void writeTo(UTF8XmlOutput uTF8XmlOutput) throws IOException;

    @Override // java.lang.CharSequence
    public abstract String toString();

    public void writeTo(char[] buf, int start) {
        toString().getChars(0, length(), buf, start);
    }
}
