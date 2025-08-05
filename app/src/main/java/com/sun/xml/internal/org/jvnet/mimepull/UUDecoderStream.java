package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/UUDecoderStream.class */
final class UUDecoderStream extends FilterInputStream {
    private String name;
    private int mode;
    private byte[] buffer;
    private int bufsize;
    private int index;
    private boolean gotPrefix;
    private boolean gotEnd;
    private LineInputStream lin;
    private boolean ignoreErrors;
    private boolean ignoreMissingBeginEnd;
    private String readAhead;

    public UUDecoderStream(InputStream in) {
        super(in);
        this.buffer = new byte[45];
        this.bufsize = 0;
        this.index = 0;
        this.gotPrefix = false;
        this.gotEnd = false;
        this.lin = new LineInputStream(in);
        this.ignoreErrors = PropUtil.getBooleanSystemProperty("mail.mime.uudecode.ignoreerrors", false);
        this.ignoreMissingBeginEnd = PropUtil.getBooleanSystemProperty("mail.mime.uudecode.ignoremissingbeginend", false);
    }

    public UUDecoderStream(InputStream in, boolean ignoreErrors, boolean ignoreMissingBeginEnd) {
        super(in);
        this.buffer = new byte[45];
        this.bufsize = 0;
        this.index = 0;
        this.gotPrefix = false;
        this.gotEnd = false;
        this.lin = new LineInputStream(in);
        this.ignoreErrors = ignoreErrors;
        this.ignoreMissingBeginEnd = ignoreMissingBeginEnd;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.index >= this.bufsize) {
            readPrefix();
            if (!decode()) {
                return -1;
            }
            this.index = 0;
        }
        byte[] bArr = this.buffer;
        int i2 = this.index;
        this.index = i2 + 1;
        return bArr[i2] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        int i2 = 0;
        while (true) {
            if (i2 >= len) {
                break;
            }
            int c2 = read();
            if (c2 == -1) {
                if (i2 == 0) {
                    i2 = -1;
                }
            } else {
                buf[off + i2] = (byte) c2;
                i2++;
            }
        }
        return i2;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        return ((this.in.available() * 3) / 4) + (this.bufsize - this.index);
    }

    public String getName() throws IOException {
        readPrefix();
        return this.name;
    }

    public int getMode() throws IOException {
        readPrefix();
        return this.mode;
    }

    /* JADX WARN: Code restructure failed: missing block: B:37:0x00f8, code lost:
    
        r7.readAhead = r0;
        r7.gotPrefix = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:?, code lost:
    
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readPrefix() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 265
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.org.jvnet.mimepull.UUDecoderStream.readPrefix():void");
    }

    private boolean decode() throws IOException {
        String line;
        if (this.gotEnd) {
            return false;
        }
        this.bufsize = 0;
        while (true) {
            if (this.readAhead != null) {
                line = this.readAhead;
                this.readAhead = null;
            } else {
                line = this.lin.readLine();
            }
            if (line == null) {
                if (!this.ignoreMissingBeginEnd) {
                    throw new DecodingException("UUDecoder: Missing end at EOF");
                }
                this.gotEnd = true;
                return false;
            }
            if (line.equals(AsmConstants.END)) {
                this.gotEnd = true;
                return false;
            }
            if (line.length() != 0) {
                int count = line.charAt(0);
                if (count < 32) {
                    if (!this.ignoreErrors) {
                        throw new DecodingException("UUDecoder: Buffer format error");
                    }
                } else {
                    int count2 = (count - 32) & 63;
                    if (count2 == 0) {
                        String line2 = this.lin.readLine();
                        if ((line2 == null || !line2.equals(AsmConstants.END)) && !this.ignoreMissingBeginEnd) {
                            throw new DecodingException("UUDecoder: Missing End after count 0 line");
                        }
                        this.gotEnd = true;
                        return false;
                    }
                    int need = ((count2 * 8) + 5) / 6;
                    if (line.length() < need + 1) {
                        if (!this.ignoreErrors) {
                            throw new DecodingException("UUDecoder: Short buffer error");
                        }
                    } else {
                        int i2 = 1;
                        while (this.bufsize < count2) {
                            int i3 = i2;
                            int i4 = i2 + 1;
                            byte a2 = (byte) ((line.charAt(i3) - ' ') & 63);
                            i2 = i4 + 1;
                            byte b2 = (byte) ((line.charAt(i4) - ' ') & 63);
                            byte[] bArr = this.buffer;
                            int i5 = this.bufsize;
                            this.bufsize = i5 + 1;
                            bArr[i5] = (byte) (((a2 << 2) & 252) | ((b2 >>> 4) & 3));
                            if (this.bufsize < count2) {
                                i2++;
                                b2 = (byte) ((line.charAt(i2) - ' ') & 63);
                                byte[] bArr2 = this.buffer;
                                int i6 = this.bufsize;
                                this.bufsize = i6 + 1;
                                bArr2[i6] = (byte) (((b2 << 4) & 240) | ((b2 >>> 2) & 15));
                            }
                            if (this.bufsize < count2) {
                                byte a3 = b2;
                                int i7 = i2;
                                i2++;
                                byte b3 = (byte) ((line.charAt(i7) - ' ') & 63);
                                byte[] bArr3 = this.buffer;
                                int i8 = this.bufsize;
                                this.bufsize = i8 + 1;
                                bArr3[i8] = (byte) (((a3 << 6) & 192) | (b3 & 63));
                            }
                        }
                        return true;
                    }
                }
            }
        }
    }
}
