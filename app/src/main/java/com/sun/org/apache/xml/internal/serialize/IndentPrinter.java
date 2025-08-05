package com.sun.org.apache.xml.internal.serialize;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serialize/IndentPrinter.class */
public class IndentPrinter extends Printer {
    private StringBuffer _line;
    private StringBuffer _text;
    private int _spaces;
    private int _thisIndent;
    private int _nextIndent;

    public IndentPrinter(Writer writer, OutputFormat format) {
        super(writer, format);
        this._line = new StringBuffer(80);
        this._text = new StringBuffer(20);
        this._spaces = 0;
        this._nextIndent = 0;
        this._thisIndent = 0;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void enterDTD() {
        if (this._dtdWriter == null) {
            this._line.append(this._text);
            this._text = new StringBuffer(20);
            flushLine(false);
            this._dtdWriter = new StringWriter();
            this._docWriter = this._writer;
            this._writer = this._dtdWriter;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public String leaveDTD() {
        if (this._writer == this._dtdWriter) {
            this._line.append(this._text);
            this._text = new StringBuffer(20);
            flushLine(false);
            this._writer = this._docWriter;
            return this._dtdWriter.toString();
        }
        return null;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void printText(String text) {
        this._text.append(text);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void printText(StringBuffer text) {
        this._text.append(text.toString());
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void printText(char ch) {
        this._text.append(ch);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void printText(char[] chars, int start, int length) {
        this._text.append(chars, start, length);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void printSpace() {
        if (this._text.length() > 0) {
            if (this._format.getLineWidth() > 0 && this._thisIndent + this._line.length() + this._spaces + this._text.length() > this._format.getLineWidth()) {
                flushLine(false);
                try {
                    this._writer.write(this._format.getLineSeparator());
                } catch (IOException except) {
                    if (this._exception == null) {
                        this._exception = except;
                    }
                }
            }
            while (this._spaces > 0) {
                this._line.append(' ');
                this._spaces--;
            }
            this._line.append(this._text);
            this._text = new StringBuffer(20);
        }
        this._spaces++;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void breakLine() {
        breakLine(false);
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void breakLine(boolean preserveSpace) {
        if (this._text.length() > 0) {
            while (this._spaces > 0) {
                this._line.append(' ');
                this._spaces--;
            }
            this._line.append(this._text);
            this._text = new StringBuffer(20);
        }
        flushLine(preserveSpace);
        try {
            this._writer.write(this._format.getLineSeparator());
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void flushLine(boolean preserveSpace) {
        if (this._line.length() > 0) {
            try {
                if (this._format.getIndenting() && !preserveSpace) {
                    int indent = this._thisIndent;
                    if (2 * indent > this._format.getLineWidth() && this._format.getLineWidth() > 0) {
                        indent = this._format.getLineWidth() / 2;
                    }
                    while (indent > 0) {
                        this._writer.write(32);
                        indent--;
                    }
                }
                this._thisIndent = this._nextIndent;
                this._spaces = 0;
                this._writer.write(this._line.toString());
                this._line = new StringBuffer(40);
            } catch (IOException except) {
                if (this._exception == null) {
                    this._exception = except;
                }
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void flush() {
        if (this._line.length() > 0 || this._text.length() > 0) {
            breakLine();
        }
        try {
            this._writer.flush();
        } catch (IOException except) {
            if (this._exception == null) {
                this._exception = except;
            }
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void indent() {
        this._nextIndent += this._format.getIndent();
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void unindent() {
        this._nextIndent -= this._format.getIndent();
        if (this._nextIndent < 0) {
            this._nextIndent = 0;
        }
        if (this._line.length() + this._spaces + this._text.length() == 0) {
            this._thisIndent = this._nextIndent;
        }
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public int getNextIndent() {
        return this._nextIndent;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void setNextIndent(int indent) {
        this._nextIndent = indent;
    }

    @Override // com.sun.org.apache.xml.internal.serialize.Printer
    public void setThisIndent(int indent) {
        this._thisIndent = indent;
    }
}
