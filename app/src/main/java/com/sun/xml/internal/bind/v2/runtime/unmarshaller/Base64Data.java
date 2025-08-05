package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.istack.internal.Nullable;
import com.sun.media.jfxmedia.locator.Locator;
import com.sun.xml.internal.bind.DatatypeConverterImpl;
import com.sun.xml.internal.bind.v2.runtime.output.Pcdata;
import com.sun.xml.internal.bind.v2.runtime.output.UTF8XmlOutput;
import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/Base64Data.class */
public final class Base64Data extends Pcdata {
    private DataHandler dataHandler;
    private byte[] data;
    private int dataLen;

    @Nullable
    private String mimeType;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Base64Data.class.desiredAssertionStatus();
    }

    public void set(byte[] data, int len, @Nullable String mimeType) {
        this.data = data;
        this.dataLen = len;
        this.dataHandler = null;
        this.mimeType = mimeType;
    }

    public void set(byte[] data, @Nullable String mimeType) {
        set(data, data.length, mimeType);
    }

    public void set(DataHandler data) {
        if (!$assertionsDisabled && data == null) {
            throw new AssertionError();
        }
        this.dataHandler = data;
        this.data = null;
    }

    public DataHandler getDataHandler() {
        if (this.dataHandler == null) {
            this.dataHandler = new DataHandler(new DataSource() { // from class: com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data.1
                @Override // javax.activation.DataSource
                public String getContentType() {
                    return Base64Data.this.getMimeType();
                }

                @Override // javax.activation.DataSource
                public InputStream getInputStream() {
                    return new ByteArrayInputStream(Base64Data.this.data, 0, Base64Data.this.dataLen);
                }

                @Override // javax.activation.DataSource
                public String getName() {
                    return null;
                }

                @Override // javax.activation.DataSource
                public OutputStream getOutputStream() {
                    throw new UnsupportedOperationException();
                }
            });
        }
        return this.dataHandler;
    }

    public byte[] getExact() {
        get();
        if (this.dataLen != this.data.length) {
            byte[] buf = new byte[this.dataLen];
            System.arraycopy(this.data, 0, buf, 0, this.dataLen);
            this.data = buf;
        }
        return this.data;
    }

    public InputStream getInputStream() throws IOException {
        if (this.dataHandler != null) {
            return this.dataHandler.getInputStream();
        }
        return new ByteArrayInputStream(this.data, 0, this.dataLen);
    }

    public boolean hasData() {
        return this.data != null;
    }

    public byte[] get() {
        if (this.data == null) {
            try {
                ByteArrayOutputStreamEx baos = new ByteArrayOutputStreamEx(1024);
                InputStream is = this.dataHandler.getDataSource().getInputStream();
                baos.readFrom(is);
                is.close();
                this.data = baos.getBuffer();
                this.dataLen = baos.size();
            } catch (IOException e2) {
                this.dataLen = 0;
            }
        }
        return this.data;
    }

    public int getDataLen() {
        return this.dataLen;
    }

    public String getMimeType() {
        if (this.mimeType == null) {
            return Locator.DEFAULT_CONTENT_TYPE;
        }
        return this.mimeType;
    }

    @Override // java.lang.CharSequence
    public int length() {
        get();
        return ((this.dataLen + 2) / 3) * 4;
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        byte b2;
        byte b1;
        int offset = index % 4;
        int base = (index / 4) * 3;
        switch (offset) {
            case 0:
                return DatatypeConverterImpl.encode(this.data[base] >> 2);
            case 1:
                if (base + 1 < this.dataLen) {
                    b1 = this.data[base + 1];
                } else {
                    b1 = 0;
                }
                return DatatypeConverterImpl.encode(((this.data[base] & 3) << 4) | ((b1 >> 4) & 15));
            case 2:
                if (base + 1 < this.dataLen) {
                    byte b12 = this.data[base + 1];
                    if (base + 2 < this.dataLen) {
                        b2 = this.data[base + 2];
                    } else {
                        b2 = 0;
                    }
                    return DatatypeConverterImpl.encode(((b12 & 15) << 2) | ((b2 >> 6) & 3));
                }
                return '=';
            case 3:
                if (base + 2 < this.dataLen) {
                    return DatatypeConverterImpl.encode(this.data[base + 2] & 63);
                }
                return '=';
            default:
                throw new IllegalStateException();
        }
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        StringBuilder buf = new StringBuilder();
        get();
        for (int i2 = start; i2 < end; i2++) {
            buf.append(charAt(i2));
        }
        return buf;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata, java.lang.CharSequence
    public String toString() {
        get();
        return DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata
    public void writeTo(char[] buf, int start) {
        get();
        DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen, buf, start);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.Pcdata
    public void writeTo(UTF8XmlOutput output) throws IOException {
        get();
        output.text(this.data, this.dataLen);
    }

    public void writeTo(XMLStreamWriter output) throws XMLStreamException, IOException {
        get();
        DatatypeConverterImpl._printBase64Binary(this.data, 0, this.dataLen, output);
    }
}
