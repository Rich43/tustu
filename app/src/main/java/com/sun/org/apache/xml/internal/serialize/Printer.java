package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/Printer.class */
public class Printer {
    protected final OutputFormat _format;
    protected Writer _writer;
    private static final int BufferSize = 4096;
    private int _pos;
    private final char[] _buffer = new char[4096];
    protected IOException _exception = null;
    protected StringWriter _dtdWriter = null;
    protected Writer _docWriter = null;

    public Printer(Writer writer, OutputFormat format) {
        this._pos = 0;
        this._writer = writer;
        this._format = format;
        this._pos = 0;
    }

    public IOException getException() {
        return this._exception;
    }

    public void enterDTD() throws IOException {
        if (this._dtdWriter == null) {
            flushLine(false);
            this._dtdWriter = new StringWriter();
            this._docWriter = this._writer;
            this._writer = this._dtdWriter;
        }
    }

    public String leaveDTD() throws IOException {
        if (this._writer == this._dtdWriter) {
            flushLine(false);
            this._writer = this._docWriter;
            return this._dtdWriter.toString();
        }
        return null;
    }

    public void printText(String text) throws IOException {
        try {
            int length = text.length();
            for (int i2 = 0; i2 < length; i2++) {
                if (this._pos == 4096) {
                    this._writer.write(this._buffer);
                    this._pos = 0;
                }
                this._buffer[this._pos] = text.charAt(i2);
                this._pos++;
            }
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
            throw except;
        }
    }

    public void printText(StringBuffer text) throws IOException {
        try {
            int length = text.length();
            for (int i2 = 0; i2 < length; i2++) {
                if (this._pos == 4096) {
                    this._writer.write(this._buffer);
                    this._pos = 0;
                }
                this._buffer[this._pos] = text.charAt(i2);
                this._pos++;
            }
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
            throw except;
        }
    }

    public void printText(char[] chars, int start, int length) throws IOException {
        while (true) {
            try {
                int i2 = length;
                length--;
                if (i2 > 0) {
                    if (this._pos == 4096) {
                        this._writer.write(this._buffer);
                        this._pos = 0;
                    }
                    this._buffer[this._pos] = chars[start];
                    start++;
                    this._pos++;
                } else {
                    return;
                }
            } catch (IOException except) {
                if (this._exception == null) {
                    this._exception = except;
                }
                throw except;
            }
        }
    }

    public void printText(char ch) throws IOException {
        try {
            if (this._pos == 4096) {
                this._writer.write(this._buffer);
                this._pos = 0;
            }
            this._buffer[this._pos] = ch;
            this._pos++;
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
            throw except;
        }
    }

    public void printSpace() throws IOException {
        try {
            if (this._pos == 4096) {
                this._writer.write(this._buffer);
                this._pos = 0;
            }
            this._buffer[this._pos] = ' ';
            this._pos++;
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
            throw except;
        }
    }

    public void breakLine() throws IOException {
        try {
            if (this._pos == 4096) {
                this._writer.write(this._buffer);
                this._pos = 0;
            }
            this._buffer[this._pos] = '\n';
            this._pos++;
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
            throw except;
        }
    }

    public void breakLine(boolean preserveSpace) throws IOException {
        breakLine();
    }

    public void flushLine(boolean preserveSpace) throws IOException {
        try {
            this._writer.write(this._buffer, 0, this._pos);
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
        }
        this._pos = 0;
    }

    public void flush() throws IOException {
        try {
            this._writer.write(this._buffer, 0, this._pos);
            this._writer.flush();
            this._pos = 0;
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
            throw except;
        }
    }

    public void indent() {
    }

    public void unindent() {
    }

    public int getNextIndent() {
        return 0;
    }

    public void setNextIndent(int indent) {
    }

    public void setThisIndent(int indent) {
    }
}
