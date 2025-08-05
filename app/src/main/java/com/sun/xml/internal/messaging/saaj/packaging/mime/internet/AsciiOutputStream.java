package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;

/* compiled from: MimeUtility.java */
/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/AsciiOutputStream.class */
class AsciiOutputStream extends OutputStream {
    private boolean breakOnNonAscii;
    private boolean checkEOL;
    private int ascii = 0;
    private int non_ascii = 0;
    private int linelen = 0;
    private boolean longLine = false;
    private boolean badEOL = false;
    private int lastb = 0;
    private int ret = 0;

    public AsciiOutputStream(boolean breakOnNonAscii, boolean encodeEolStrict) {
        this.checkEOL = false;
        this.breakOnNonAscii = breakOnNonAscii;
        this.checkEOL = encodeEolStrict && breakOnNonAscii;
    }

    @Override // java.io.OutputStream
    public void write(int b2) throws IOException {
        check(b2);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2) throws IOException {
        write(b2, 0, b2.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b2, int off, int len) throws IOException {
        int len2 = len + off;
        for (int i2 = off; i2 < len2; i2++) {
            check(b2[i2]);
        }
    }

    private final void check(int b2) throws IOException {
        int b3 = b2 & 255;
        if (this.checkEOL && ((this.lastb == 13 && b3 != 10) || (this.lastb != 13 && b3 == 10))) {
            this.badEOL = true;
        }
        if (b3 == 13 || b3 == 10) {
            this.linelen = 0;
        } else {
            this.linelen++;
            if (this.linelen > 998) {
                this.longLine = true;
            }
        }
        if (MimeUtility.nonascii(b3)) {
            this.non_ascii++;
            if (this.breakOnNonAscii) {
                this.ret = 3;
                throw new EOFException();
            }
        } else {
            this.ascii++;
        }
        this.lastb = b3;
    }

    public int getAscii() {
        if (this.ret != 0) {
            return this.ret;
        }
        if (this.badEOL) {
            return 3;
        }
        if (this.non_ascii == 0) {
            if (this.longLine) {
                return 2;
            }
            return 1;
        }
        if (this.ascii > this.non_ascii) {
            return 2;
        }
        return 3;
    }
}
