package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/UUDecoderStream.class */
public class UUDecoderStream extends FilterInputStream {
    private String name;
    private int mode;
    private byte[] buffer;
    private int bufsize;
    private int index;
    private boolean gotPrefix;
    private boolean gotEnd;
    private LineInputStream lin;

    public UUDecoderStream(InputStream in) {
        super(in);
        this.bufsize = 0;
        this.index = 0;
        this.gotPrefix = false;
        this.gotEnd = false;
        this.lin = new LineInputStream(in);
        this.buffer = new byte[45];
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

    /* JADX WARN: Code restructure failed: missing block: B:12:0x003d, code lost:
    
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x005b, code lost:
    
        throw new java.io.IOException("UUDecoder error: " + r9.toString());
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void readPrefix() throws java.io.IOException {
        /*
            r7 = this;
            r0 = r7
            boolean r0 = r0.gotPrefix
            if (r0 == 0) goto L8
            return
        L8:
            r0 = r7
            com.sun.xml.internal.messaging.saaj.packaging.mime.util.LineInputStream r0 = r0.lin
            java.lang.String r0 = r0.readLine()
            r8 = r0
            r0 = r8
            if (r0 != 0) goto L1e
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.String r2 = "UUDecoder error: No Begin"
            r1.<init>(r2)
            throw r0
        L1e:
            r0 = r8
            r1 = 1
            r2 = 0
            java.lang.String r3 = "begin"
            r4 = 0
            r5 = 5
            boolean r0 = r0.regionMatches(r1, r2, r3, r4, r5)
            if (r0 == 0) goto L8
            r0 = r7
            r1 = r8
            r2 = 6
            r3 = 9
            java.lang.String r1 = r1.substring(r2, r3)     // Catch: java.lang.NumberFormatException -> L3d
            int r1 = java.lang.Integer.parseInt(r1)     // Catch: java.lang.NumberFormatException -> L3d
            r0.mode = r1     // Catch: java.lang.NumberFormatException -> L3d
            goto L5c
        L3d:
            r9 = move-exception
            java.io.IOException r0 = new java.io.IOException
            r1 = r0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r3 = r2
            r3.<init>()
            java.lang.String r3 = "UUDecoder error: "
            java.lang.StringBuilder r2 = r2.append(r3)
            r3 = r9
            java.lang.String r3 = r3.toString()
            java.lang.StringBuilder r2 = r2.append(r3)
            java.lang.String r2 = r2.toString()
            r1.<init>(r2)
            throw r0
        L5c:
            r0 = r7
            r1 = r8
            r2 = 10
            java.lang.String r1 = r1.substring(r2)
            r0.name = r1
            r0 = r7
            r1 = 1
            r0.gotPrefix = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.messaging.saaj.packaging.mime.util.UUDecoderStream.readPrefix():void");
    }

    private boolean decode() throws IOException {
        String line;
        if (this.gotEnd) {
            return false;
        }
        this.bufsize = 0;
        do {
            line = this.lin.readLine();
            if (line == null) {
                throw new IOException("Missing End");
            }
            if (line.regionMatches(true, 0, AsmConstants.END, 0, 3)) {
                this.gotEnd = true;
                return false;
            }
        } while (line.length() == 0);
        int count = line.charAt(0);
        if (count < 32) {
            throw new IOException("Buffer format error");
        }
        int count2 = (count - 32) & 63;
        if (count2 == 0) {
            String line2 = this.lin.readLine();
            if (line2 == null || !line2.regionMatches(true, 0, AsmConstants.END, 0, 3)) {
                throw new IOException("Missing End");
            }
            this.gotEnd = true;
            return false;
        }
        int need = ((count2 * 8) + 5) / 6;
        if (line.length() < need + 1) {
            throw new IOException("Short buffer error");
        }
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
