package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/LineInputStream.class */
public final class LineInputStream extends FilterInputStream {
    private char[] lineBuffer;

    public LineInputStream(InputStream in) {
        super(in);
        this.lineBuffer = null;
    }

    public String readLine() throws IOException {
        int c1;
        InputStream in = this.in;
        char[] buf = this.lineBuffer;
        if (buf == null) {
            char[] cArr = new char[128];
            this.lineBuffer = cArr;
            buf = cArr;
        }
        int room = buf.length;
        int offset = 0;
        while (true) {
            c1 = in.read();
            if (c1 == -1 || c1 == 10) {
                break;
            }
            if (c1 == 13) {
                int c2 = in.read();
                if (c2 == 13) {
                    c2 = in.read();
                }
                if (c2 != 10) {
                    if (!(in instanceof PushbackInputStream)) {
                        PushbackInputStream pushbackInputStream = new PushbackInputStream(in);
                        this.in = pushbackInputStream;
                        in = pushbackInputStream;
                    }
                    ((PushbackInputStream) in).unread(c2);
                }
            } else {
                room--;
                if (room < 0) {
                    buf = new char[offset + 128];
                    room = (buf.length - offset) - 1;
                    System.arraycopy(this.lineBuffer, 0, buf, 0, offset);
                    this.lineBuffer = buf;
                }
                int i2 = offset;
                offset++;
                buf[i2] = (char) c1;
            }
        }
        if (c1 == -1 && offset == 0) {
            return null;
        }
        return String.copyValueOf(buf, 0, offset);
    }
}
