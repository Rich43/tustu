package com.sun.xml.internal.messaging.saaj.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.transform.stream.StreamSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/util/JAXMStreamSource.class */
public class JAXMStreamSource extends StreamSource {
    InputStream in;
    Reader reader;
    private static final boolean lazyContentLength = SAAJUtil.getSystemBoolean("saaj.lazy.contentlength");

    public JAXMStreamSource(InputStream is) throws IOException {
        if (lazyContentLength) {
            this.in = is;
        } else {
            if (is instanceof ByteInputStream) {
                this.in = (ByteInputStream) is;
                return;
            }
            ByteOutputStream bout = new ByteOutputStream();
            bout.write(is);
            this.in = bout.newInputStream();
        }
    }

    public JAXMStreamSource(Reader rdr) throws IOException {
        if (lazyContentLength) {
            this.reader = rdr;
            return;
        }
        CharWriter cout = new CharWriter();
        char[] temp = new char[1024];
        while (true) {
            int len = rdr.read(temp);
            if (-1 != len) {
                cout.write(temp, 0, len);
            } else {
                this.reader = new CharReader(cout.getChars(), cout.getCount());
                return;
            }
        }
    }

    @Override // javax.xml.transform.stream.StreamSource
    public InputStream getInputStream() {
        return this.in;
    }

    @Override // javax.xml.transform.stream.StreamSource
    public Reader getReader() {
        return this.reader;
    }

    public void reset() throws IOException {
        if (this.in != null) {
            this.in.reset();
        }
        if (this.reader != null) {
            this.reader.reset();
        }
    }
}
