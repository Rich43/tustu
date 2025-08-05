package com.sun.java.util.jar.pack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/CodingMethod.class */
interface CodingMethod {
    void readArrayFrom(InputStream inputStream, int[] iArr, int i2, int i3) throws IOException;

    void writeArrayTo(OutputStream outputStream, int[] iArr, int i2, int i3) throws IOException;

    byte[] getMetaCoding(Coding coding);
}
