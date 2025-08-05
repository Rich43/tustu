package jdk.internal.util.xml.impl;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import jdk.internal.util.xml.XMLStreamException;

/* loaded from: rt.jar:jdk/internal/util/xml/impl/XMLWriter.class */
public class XMLWriter {
    private Writer _writer;
    private CharsetEncoder _encoder;

    public XMLWriter(OutputStream outputStream, String str, Charset charset) throws XMLStreamException {
        this._encoder = null;
        this._encoder = charset.newEncoder();
        try {
            this._writer = getWriter(outputStream, str, charset);
        } catch (UnsupportedEncodingException e2) {
            throw new XMLStreamException(e2);
        }
    }

    public boolean canEncode(char c2) {
        if (this._encoder == null) {
            return false;
        }
        return this._encoder.canEncode(c2);
    }

    public void write(String str) throws XMLStreamException {
        try {
            this._writer.write(str.toCharArray());
        } catch (IOException e2) {
            throw new XMLStreamException("I/O error", e2);
        }
    }

    public void write(String str, int i2, int i3) throws XMLStreamException {
        try {
            this._writer.write(str, i2, i3);
        } catch (IOException e2) {
            throw new XMLStreamException("I/O error", e2);
        }
    }

    public void write(char[] cArr, int i2, int i3) throws XMLStreamException {
        try {
            this._writer.write(cArr, i2, i3);
        } catch (IOException e2) {
            throw new XMLStreamException("I/O error", e2);
        }
    }

    void write(int i2) throws XMLStreamException {
        try {
            this._writer.write(i2);
        } catch (IOException e2) {
            throw new XMLStreamException("I/O error", e2);
        }
    }

    void flush() throws XMLStreamException {
        try {
            this._writer.flush();
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    void close() throws XMLStreamException {
        try {
            this._writer.close();
        } catch (IOException e2) {
            throw new XMLStreamException(e2);
        }
    }

    private void nl() throws XMLStreamException {
        try {
            this._writer.write(System.getProperty("line.separator"));
        } catch (IOException e2) {
            throw new XMLStreamException("I/O error", e2);
        }
    }

    private Writer getWriter(OutputStream outputStream, String str, Charset charset) throws XMLStreamException, UnsupportedEncodingException {
        if (charset != null) {
            return new OutputStreamWriter(new BufferedOutputStream(outputStream), charset);
        }
        return new OutputStreamWriter(new BufferedOutputStream(outputStream), str);
    }
}
