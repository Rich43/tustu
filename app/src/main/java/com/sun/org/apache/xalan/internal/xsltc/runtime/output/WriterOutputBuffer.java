package com.sun.org.apache.xalan.internal.xsltc.runtime.output;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/runtime/output/WriterOutputBuffer.class */
class WriterOutputBuffer implements OutputBuffer {
    private static final int KB = 1024;
    private static int BUFFER_SIZE;
    private Writer _writer;

    static {
        BUFFER_SIZE = 4096;
        String osName = SecuritySupport.getSystemProperty("os.name");
        if (osName.equalsIgnoreCase("solaris")) {
            BUFFER_SIZE = 32768;
        }
    }

    public WriterOutputBuffer(Writer writer) {
        this._writer = new BufferedWriter(writer, BUFFER_SIZE);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public String close() {
        try {
            this._writer.flush();
            return "";
        } catch (IOException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public OutputBuffer append(String s2) {
        try {
            this._writer.write(s2);
            return this;
        } catch (IOException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public OutputBuffer append(char[] s2, int from, int to) {
        try {
            this._writer.write(s2, from, to);
            return this;
        } catch (IOException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.runtime.output.OutputBuffer
    public OutputBuffer append(char ch) {
        try {
            this._writer.write(ch);
            return this;
        } catch (IOException e2) {
            throw new RuntimeException(e2.toString());
        }
    }
}
