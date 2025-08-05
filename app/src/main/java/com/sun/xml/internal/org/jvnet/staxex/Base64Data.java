package com.sun.xml.internal.org.jvnet.staxex;

import com.sun.media.jfxmedia.locator.Locator;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/Base64Data.class */
public class Base64Data implements CharSequence, Cloneable {
    private DataHandler dataHandler;
    private byte[] data;
    private int dataLen;
    private boolean dataCloneByRef;
    private String mimeType;
    private static final Logger logger;
    private static final int CHUNK_SIZE;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Base64Data.class.desiredAssertionStatus();
        logger = Logger.getLogger(Base64Data.class.getName());
        int bufSize = 1024;
        try {
            String bufSizeStr = getProperty("com.sun.xml.internal.org.jvnet.staxex.Base64DataStreamWriteBufferSize");
            if (bufSizeStr != null) {
                bufSize = Integer.parseInt(bufSizeStr);
            }
        } catch (Exception e2) {
            logger.log(Level.INFO, "Error reading com.sun.xml.internal.org.jvnet.staxex.Base64DataStreamWriteBufferSize property", (Throwable) e2);
        }
        CHUNK_SIZE = bufSize;
    }

    public Base64Data() {
    }

    public Base64Data(Base64Data that) {
        that.get();
        if (that.dataCloneByRef) {
            this.data = that.data;
        } else {
            this.data = new byte[that.dataLen];
            System.arraycopy(that.data, 0, this.data, 0, that.dataLen);
        }
        this.dataCloneByRef = true;
        this.dataLen = that.dataLen;
        this.dataHandler = null;
        this.mimeType = that.mimeType;
    }

    public void set(byte[] data, int len, String mimeType, boolean cloneByRef) {
        this.data = data;
        this.dataLen = len;
        this.dataCloneByRef = cloneByRef;
        this.dataHandler = null;
        this.mimeType = mimeType;
    }

    public void set(byte[] data, int len, String mimeType) {
        set(data, len, mimeType, false);
    }

    public void set(byte[] data, String mimeType) {
        set(data, data.length, mimeType, false);
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
            this.dataHandler = new Base64StreamingDataHandler(new Base64DataSource());
        } else if (!(this.dataHandler instanceof StreamingDataHandler)) {
            this.dataHandler = new FilterDataHandler(this.dataHandler);
        }
        return this.dataHandler;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/Base64Data$Base64DataSource.class */
    private final class Base64DataSource implements DataSource {
        private Base64DataSource() {
        }

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
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/Base64Data$Base64StreamingDataHandler.class */
    private final class Base64StreamingDataHandler extends StreamingDataHandler {
        Base64StreamingDataHandler(DataSource source) {
            super(source);
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
        public InputStream readOnce() throws IOException {
            return getDataSource().getInputStream();
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
        public void moveTo(File dst) throws IOException {
            FileOutputStream fout = new FileOutputStream(dst);
            try {
                fout.write(Base64Data.this.data, 0, Base64Data.this.dataLen);
            } finally {
                fout.close();
            }
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/staxex/Base64Data$FilterDataHandler.class */
    private static final class FilterDataHandler extends StreamingDataHandler {
        FilterDataHandler(DataHandler dh) {
            super(dh.getDataSource());
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
        public InputStream readOnce() throws IOException {
            return getDataSource().getInputStream();
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler
        public void moveTo(File dst) throws IOException {
            byte[] buf = new byte[8192];
            InputStream in = null;
            OutputStream out = null;
            try {
                in = getDataSource().getInputStream();
                out = new FileOutputStream(dst);
                while (true) {
                    int amountRead = in.read(buf);
                    if (amountRead == -1) {
                        break;
                    } else {
                        out.write(buf, 0, amountRead);
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e2) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e4) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e5) {
                    }
                }
                throw th;
            }
        }

        @Override // com.sun.xml.internal.org.jvnet.staxex.StreamingDataHandler, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
        }
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
                this.dataCloneByRef = true;
            } catch (IOException e2) {
                this.dataLen = 0;
            }
        }
        return this.data;
    }

    public int getDataLen() {
        get();
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
                return Base64Encoder.encode(this.data[base] >> 2);
            case 1:
                if (base + 1 < this.dataLen) {
                    b1 = this.data[base + 1];
                } else {
                    b1 = 0;
                }
                return Base64Encoder.encode(((this.data[base] & 3) << 4) | ((b1 >> 4) & 15));
            case 2:
                if (base + 1 < this.dataLen) {
                    byte b12 = this.data[base + 1];
                    if (base + 2 < this.dataLen) {
                        b2 = this.data[base + 2];
                    } else {
                        b2 = 0;
                    }
                    return Base64Encoder.encode(((b12 & 15) << 2) | ((b2 >> 6) & 3));
                }
                return '=';
            case 3:
                if (base + 2 < this.dataLen) {
                    return Base64Encoder.encode(this.data[base + 2] & 63);
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

    @Override // java.lang.CharSequence
    public String toString() {
        get();
        return Base64Encoder.print(this.data, 0, this.dataLen);
    }

    public void writeTo(char[] buf, int start) {
        get();
        Base64Encoder.print(this.data, 0, this.dataLen, buf, start);
    }

    public void writeTo(XMLStreamWriter output) throws XMLStreamException, IOException {
        if (this.data == null) {
            try {
                InputStream is = this.dataHandler.getDataSource().getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                Base64EncoderStream encWriter = new Base64EncoderStream(output, outStream);
                byte[] buffer = new byte[CHUNK_SIZE];
                while (true) {
                    int b2 = is.read(buffer);
                    if (b2 != -1) {
                        encWriter.write(buffer, 0, b2);
                    } else {
                        outStream.close();
                        encWriter.close();
                        return;
                    }
                }
            } catch (IOException e2) {
                this.dataLen = 0;
                throw e2;
            }
        } else {
            String s2 = Base64Encoder.print(this.data, 0, this.dataLen);
            output.writeCharacters(s2);
        }
    }

    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public Base64Data m2272clone() {
        try {
            Base64Data clone = (Base64Data) super.clone();
            clone.get();
            if (clone.dataCloneByRef) {
                this.data = clone.data;
            } else {
                this.data = new byte[clone.dataLen];
                System.arraycopy(clone.data, 0, this.data, 0, clone.dataLen);
            }
            this.dataCloneByRef = true;
            this.dataLen = clone.dataLen;
            this.dataHandler = null;
            this.mimeType = clone.mimeType;
            return clone;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Base64Data.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
            return null;
        }
    }

    static String getProperty(final String propName) {
        if (System.getSecurityManager() == null) {
            return System.getProperty(propName);
        }
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.xml.internal.org.jvnet.staxex.Base64Data.1
            @Override // java.security.PrivilegedAction
            public Object run() {
                return System.getProperty(propName);
            }
        });
    }
}
