package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.output.NamespaceContextImpl;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/UTF8XmlOutput.class */
public class UTF8XmlOutput extends XmlOutputAbstractImpl {
    protected final OutputStream out;
    private int prefixCount;
    private final Encoded[] localNames;
    protected int octetBufferIndex;
    private String header;
    private CharacterEscapeHandler escapeHandler;
    private static final byte[] _XMLNS_EQUALS = toBytes(" xmlns=\"");
    private static final byte[] _XMLNS_COLON = toBytes(" xmlns:");
    private static final byte[] _EQUALS = toBytes("=\"");
    private static final byte[] _CLOSE_TAG = toBytes("</");
    private static final byte[] _EMPTY_TAG = toBytes("/>");
    private static final byte[] _XML_DECL = toBytes("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    private Encoded[] prefixes = new Encoded[8];
    private final Encoded textBuffer = new Encoded();
    protected final byte[] octetBuffer = new byte[1024];
    protected boolean closeStartTagPending = false;
    private final byte[] XMLNS_EQUALS = (byte[]) _XMLNS_EQUALS.clone();
    private final byte[] XMLNS_COLON = (byte[]) _XMLNS_COLON.clone();
    private final byte[] EQUALS = (byte[]) _EQUALS.clone();
    private final byte[] CLOSE_TAG = (byte[]) _CLOSE_TAG.clone();
    private final byte[] EMPTY_TAG = (byte[]) _EMPTY_TAG.clone();
    private final byte[] XML_DECL = (byte[]) _XML_DECL.clone();

    public UTF8XmlOutput(OutputStream out, Encoded[] localNames, CharacterEscapeHandler escapeHandler) {
        this.escapeHandler = null;
        this.out = out;
        this.localNames = localNames;
        for (int i2 = 0; i2 < this.prefixes.length; i2++) {
            this.prefixes[i2] = new Encoded();
        }
        this.escapeHandler = escapeHandler;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, XMLStreamException, IOException {
        super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
        this.octetBufferIndex = 0;
        if (!fragment) {
            write(this.XML_DECL);
        }
        if (this.header != null) {
            this.textBuffer.set(this.header);
            this.textBuffer.write(this);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws SAXException, XMLStreamException, IOException {
        flushBuffer();
        super.endDocument(fragment);
    }

    protected final void closeStartTag() throws IOException {
        if (this.closeStartTagPending) {
            write(62);
            this.closeStartTagPending = false;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) throws IOException {
        closeStartTag();
        int base = pushNsDecls();
        write(60);
        writeName(prefix, localName);
        writeNsDecls(base);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws IOException {
        closeStartTag();
        int base = pushNsDecls();
        write(60);
        writeName(name);
        writeNsDecls(base);
    }

    private int pushNsDecls() {
        int total = this.nsContext.count();
        NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
        if (total > this.prefixes.length) {
            int m2 = Math.max(total, this.prefixes.length * 2);
            Encoded[] buf = new Encoded[m2];
            System.arraycopy(this.prefixes, 0, buf, 0, this.prefixes.length);
            for (int i2 = this.prefixes.length; i2 < buf.length; i2++) {
                buf[i2] = new Encoded();
            }
            this.prefixes = buf;
        }
        int base = Math.min(this.prefixCount, ns.getBase());
        int size = this.nsContext.count();
        for (int i3 = base; i3 < size; i3++) {
            String p2 = this.nsContext.getPrefix(i3);
            Encoded e2 = this.prefixes[i3];
            if (p2.length() == 0) {
                e2.buf = EMPTY_BYTE_ARRAY;
                e2.len = 0;
            } else {
                e2.set(p2);
                e2.append(':');
            }
        }
        this.prefixCount = size;
        return base;
    }

    protected void writeNsDecls(int base) throws IOException {
        NamespaceContextImpl.Element ns = this.nsContext.getCurrent();
        int size = this.nsContext.count();
        for (int i2 = ns.getBase(); i2 < size; i2++) {
            writeNsDecl(i2);
        }
    }

    protected final void writeNsDecl(int prefixIndex) throws IOException {
        String p2 = this.nsContext.getPrefix(prefixIndex);
        if (p2.length() == 0) {
            if (this.nsContext.getCurrent().isRootElement() && this.nsContext.getNamespaceURI(prefixIndex).length() == 0) {
                return;
            } else {
                write(this.XMLNS_EQUALS);
            }
        } else {
            Encoded e2 = this.prefixes[prefixIndex];
            write(this.XMLNS_COLON);
            write(e2.buf, 0, e2.len - 1);
            write(this.EQUALS);
        }
        doText(this.nsContext.getNamespaceURI(prefixIndex), true);
        write(34);
    }

    private void writePrefix(int prefix) throws IOException {
        this.prefixes[prefix].write(this);
    }

    private void writeName(Name name) throws IOException {
        writePrefix(this.nsUriIndex2prefixIndex[name.nsUriIndex]);
        this.localNames[name.localNameIndex].write(this);
    }

    private void writeName(int prefix, String localName) throws IOException {
        writePrefix(prefix);
        this.textBuffer.set(localName);
        this.textBuffer.write(this);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws IOException {
        write(32);
        if (name.nsUriIndex == -1) {
            this.localNames[name.localNameIndex].write(this);
        } else {
            writeName(name);
        }
        write(this.EQUALS);
        doText(value, true);
        write(34);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws IOException {
        write(32);
        if (prefix == -1) {
            this.textBuffer.set(localName);
            this.textBuffer.write(this);
        } else {
            writeName(prefix, localName);
        }
        write(this.EQUALS);
        doText(value, true);
        write(34);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws IOException {
        this.closeStartTagPending = true;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws IOException {
        if (this.closeStartTagPending) {
            write(this.EMPTY_TAG);
            this.closeStartTagPending = false;
        } else {
            write(this.CLOSE_TAG);
            writeName(name);
            write(62);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws IOException {
        if (this.closeStartTagPending) {
            write(this.EMPTY_TAG);
            this.closeStartTagPending = false;
        } else {
            write(this.CLOSE_TAG);
            writeName(prefix, localName);
            write(62);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needSP) throws IOException {
        closeStartTag();
        if (needSP) {
            write(32);
        }
        doText(value, false);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needSP) throws IOException {
        closeStartTag();
        if (needSP) {
            write(32);
        }
        value.writeTo(this);
    }

    private void doText(String value, boolean isAttribute) throws IOException {
        if (this.escapeHandler != null) {
            StringWriter sw = new StringWriter();
            this.escapeHandler.escape(value.toCharArray(), 0, value.length(), isAttribute, sw);
            this.textBuffer.set(sw.toString());
        } else {
            this.textBuffer.setEscape(value, isAttribute);
        }
        this.textBuffer.write(this);
    }

    public final void text(int value) throws IOException {
        closeStartTag();
        boolean minus = value < 0;
        this.textBuffer.ensureSize(11);
        byte[] buf = this.textBuffer.buf;
        int idx = 11;
        do {
            int r2 = value % 10;
            if (r2 < 0) {
                r2 = -r2;
            }
            idx--;
            buf[idx] = (byte) (48 | r2);
            value /= 10;
        } while (value != 0);
        if (minus) {
            idx--;
            buf[idx] = 45;
        }
        write(buf, idx, 11 - idx);
    }

    public void text(byte[] data, int dataLen) throws IOException {
        closeStartTag();
        int start = 0;
        while (dataLen > 0) {
            int batchSize = Math.min(((this.octetBuffer.length - this.octetBufferIndex) / 4) * 3, dataLen);
            this.octetBufferIndex = DatatypeConverterImpl._printBase64Binary(data, start, batchSize, this.octetBuffer, this.octetBufferIndex);
            if (batchSize < dataLen) {
                flushBuffer();
            }
            start += batchSize;
            dataLen -= batchSize;
        }
    }

    public final void write(int i2) throws IOException {
        if (this.octetBufferIndex < this.octetBuffer.length) {
            byte[] bArr = this.octetBuffer;
            int i3 = this.octetBufferIndex;
            this.octetBufferIndex = i3 + 1;
            bArr[i3] = (byte) i2;
            return;
        }
        this.out.write(this.octetBuffer);
        this.octetBufferIndex = 1;
        this.octetBuffer[0] = (byte) i2;
    }

    protected final void write(byte[] b2) throws IOException {
        write(b2, 0, b2.length);
    }

    protected final void write(byte[] b2, int start, int length) throws IOException {
        if (this.octetBufferIndex + length < this.octetBuffer.length) {
            System.arraycopy(b2, start, this.octetBuffer, this.octetBufferIndex, length);
            this.octetBufferIndex += length;
        } else {
            this.out.write(this.octetBuffer, 0, this.octetBufferIndex);
            this.out.write(b2, start, length);
            this.octetBufferIndex = 0;
        }
    }

    protected final void flushBuffer() throws IOException {
        this.out.write(this.octetBuffer, 0, this.octetBufferIndex);
        this.octetBufferIndex = 0;
    }

    static byte[] toBytes(String s2) {
        byte[] buf = new byte[s2.length()];
        for (int i2 = s2.length() - 1; i2 >= 0; i2--) {
            buf[i2] = (byte) s2.charAt(i2);
        }
        return buf;
    }
}
