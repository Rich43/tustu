package com.sun.org.apache.xalan.internal.xsltc.runtime.output;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/output/OutputBuffer.class */
interface OutputBuffer {
    String close();

    OutputBuffer append(char c2);

    OutputBuffer append(String str);

    OutputBuffer append(char[] cArr, int i2, int i3);
}
