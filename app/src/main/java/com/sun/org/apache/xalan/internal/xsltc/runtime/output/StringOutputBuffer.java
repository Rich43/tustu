package com.sun.org.apache.xalan.internal.xsltc.runtime.output;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/output/StringOutputBuffer.class */
class StringOutputBuffer implements OutputBuffer {
    private StringBuffer _buffer = new StringBuffer();

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public String close() {
        return this._buffer.toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public OutputBuffer append(String s2) {
        this._buffer.append(s2);
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public OutputBuffer append(char[] s2, int from, int to) {
        this._buffer.append(s2, from, to);
        return this;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public OutputBuffer append(char ch) {
        this._buffer.append(ch);
        return this;
    }
}
