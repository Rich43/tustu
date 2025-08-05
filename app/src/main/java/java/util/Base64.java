package java.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/* loaded from: rt.jar:java/util/Base64.class */
public class Base64 {
    private Base64() {
    }

    public static Encoder getEncoder() {
        return Encoder.RFC4648;
    }

    public static Encoder getUrlEncoder() {
        return Encoder.RFC4648_URLSAFE;
    }

    public static Encoder getMimeEncoder() {
        return Encoder.RFC2045;
    }

    public static Encoder getMimeEncoder(int i2, byte[] bArr) {
        Objects.requireNonNull(bArr);
        int[] iArr = Decoder.fromBase64;
        for (byte b2 : bArr) {
            if (iArr[b2 & 255] != -1) {
                throw new IllegalArgumentException("Illegal base64 line separator character 0x" + Integer.toString(b2, 16));
            }
        }
        if (i2 <= 0) {
            return Encoder.RFC4648;
        }
        return new Encoder(false, bArr, (i2 >> 2) << 2, true);
    }

    public static Decoder getDecoder() {
        return Decoder.RFC4648;
    }

    public static Decoder getUrlDecoder() {
        return Decoder.RFC4648_URLSAFE;
    }

    public static Decoder getMimeDecoder() {
        return Decoder.RFC2045;
    }

    /* loaded from: rt.jar:java/util/Base64$Encoder.class */
    public static class Encoder {
        private final byte[] newline;
        private final int linemax;
        private final boolean isURL;
        private final boolean doPadding;
        private static final int MIMELINEMAX = 76;
        private static final char[] toBase64 = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
        private static final char[] toBase64URL = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-', '_'};
        private static final byte[] CRLF = {13, 10};
        static final Encoder RFC4648 = new Encoder(false, null, -1, true);
        static final Encoder RFC4648_URLSAFE = new Encoder(true, null, -1, true);
        static final Encoder RFC2045 = new Encoder(false, CRLF, 76, true);

        private Encoder(boolean z2, byte[] bArr, int i2, boolean z3) {
            this.isURL = z2;
            this.newline = bArr;
            this.linemax = i2;
            this.doPadding = z3;
        }

        private final int outLength(int i2) {
            int length;
            if (this.doPadding) {
                length = 4 * ((i2 + 2) / 3);
            } else {
                int i3 = i2 % 3;
                length = (4 * (i2 / 3)) + (i3 == 0 ? 0 : i3 + 1);
            }
            if (this.linemax > 0) {
                length += ((length - 1) / this.linemax) * this.newline.length;
            }
            return length;
        }

        public byte[] encode(byte[] bArr) {
            byte[] bArr2 = new byte[outLength(bArr.length)];
            int iEncode0 = encode0(bArr, 0, bArr.length, bArr2);
            if (iEncode0 != bArr2.length) {
                return Arrays.copyOf(bArr2, iEncode0);
            }
            return bArr2;
        }

        public int encode(byte[] bArr, byte[] bArr2) {
            if (bArr2.length < outLength(bArr.length)) {
                throw new IllegalArgumentException("Output byte array is too small for encoding all input bytes");
            }
            return encode0(bArr, 0, bArr.length, bArr2);
        }

        public String encodeToString(byte[] bArr) {
            byte[] bArrEncode = encode(bArr);
            return new String(bArrEncode, 0, 0, bArrEncode.length);
        }

        public ByteBuffer encode(ByteBuffer byteBuffer) {
            int iEncode0;
            byte[] bArrCopyOf = new byte[outLength(byteBuffer.remaining())];
            if (byteBuffer.hasArray()) {
                iEncode0 = encode0(byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.arrayOffset() + byteBuffer.limit(), bArrCopyOf);
                byteBuffer.position(byteBuffer.limit());
            } else {
                byte[] bArr = new byte[byteBuffer.remaining()];
                byteBuffer.get(bArr);
                iEncode0 = encode0(bArr, 0, bArr.length, bArrCopyOf);
            }
            if (iEncode0 != bArrCopyOf.length) {
                bArrCopyOf = Arrays.copyOf(bArrCopyOf, iEncode0);
            }
            return ByteBuffer.wrap(bArrCopyOf);
        }

        public OutputStream wrap(OutputStream outputStream) {
            Objects.requireNonNull(outputStream);
            return new EncOutputStream(outputStream, this.isURL ? toBase64URL : toBase64, this.newline, this.linemax, this.doPadding);
        }

        public Encoder withoutPadding() {
            if (!this.doPadding) {
                return this;
            }
            return new Encoder(this.isURL, this.newline, this.linemax, false);
        }

        private int encode0(byte[] bArr, int i2, int i3, byte[] bArr2) {
            char[] cArr = this.isURL ? toBase64URL : toBase64;
            int i4 = i2;
            int i5 = ((i3 - i2) / 3) * 3;
            int i6 = i2 + i5;
            if (this.linemax > 0 && i5 > (this.linemax / 4) * 3) {
                i5 = (this.linemax / 4) * 3;
            }
            int i7 = 0;
            while (i4 < i6) {
                int iMin = Math.min(i4 + i5, i6);
                int i8 = i4;
                int i9 = i7;
                while (i8 < iMin) {
                    int i10 = i8;
                    int i11 = i8 + 1;
                    int i12 = i11 + 1;
                    int i13 = ((bArr[i10] & 255) << 16) | ((bArr[i11] & 255) << 8);
                    i8 = i12 + 1;
                    int i14 = i13 | (bArr[i12] & 255);
                    int i15 = i9;
                    int i16 = i9 + 1;
                    bArr2[i15] = (byte) cArr[(i14 >>> 18) & 63];
                    int i17 = i16 + 1;
                    bArr2[i16] = (byte) cArr[(i14 >>> 12) & 63];
                    int i18 = i17 + 1;
                    bArr2[i17] = (byte) cArr[(i14 >>> 6) & 63];
                    i9 = i18 + 1;
                    bArr2[i18] = (byte) cArr[i14 & 63];
                }
                int i19 = ((iMin - i4) / 3) * 4;
                i7 += i19;
                i4 = iMin;
                if (i19 == this.linemax && i4 < i3) {
                    for (byte b2 : this.newline) {
                        int i20 = i7;
                        i7++;
                        bArr2[i20] = b2;
                    }
                }
            }
            if (i4 < i3) {
                int i21 = i4;
                int i22 = i4 + 1;
                int i23 = bArr[i21] & 255;
                int i24 = i7;
                int i25 = i7 + 1;
                bArr2[i24] = (byte) cArr[i23 >> 2];
                if (i22 == i3) {
                    i7 = i25 + 1;
                    bArr2[i25] = (byte) cArr[(i23 << 4) & 63];
                    if (this.doPadding) {
                        int i26 = i7 + 1;
                        bArr2[i7] = 61;
                        i7 = i26 + 1;
                        bArr2[i26] = 61;
                    }
                } else {
                    int i27 = i22 + 1;
                    int i28 = bArr[i22] & 255;
                    int i29 = i25 + 1;
                    bArr2[i25] = (byte) cArr[((i23 << 4) & 63) | (i28 >> 4)];
                    i7 = i29 + 1;
                    bArr2[i29] = (byte) cArr[(i28 << 2) & 63];
                    if (this.doPadding) {
                        i7++;
                        bArr2[i7] = 61;
                    }
                }
            }
            return i7;
        }
    }

    /* loaded from: rt.jar:java/util/Base64$Decoder.class */
    public static class Decoder {
        private final boolean isURL;
        private final boolean isMIME;
        private static final int[] fromBase64 = new int[256];
        private static final int[] fromBase64URL;
        static final Decoder RFC4648;
        static final Decoder RFC4648_URLSAFE;
        static final Decoder RFC2045;

        private Decoder(boolean z2, boolean z3) {
            this.isURL = z2;
            this.isMIME = z3;
        }

        static {
            Arrays.fill(fromBase64, -1);
            for (int i2 = 0; i2 < Encoder.toBase64.length; i2++) {
                fromBase64[Encoder.toBase64[i2]] = i2;
            }
            fromBase64[61] = -2;
            fromBase64URL = new int[256];
            Arrays.fill(fromBase64URL, -1);
            for (int i3 = 0; i3 < Encoder.toBase64URL.length; i3++) {
                fromBase64URL[Encoder.toBase64URL[i3]] = i3;
            }
            fromBase64URL[61] = -2;
            RFC4648 = new Decoder(false, false);
            RFC4648_URLSAFE = new Decoder(true, false);
            RFC2045 = new Decoder(false, true);
        }

        public byte[] decode(byte[] bArr) {
            byte[] bArrCopyOf = new byte[outLength(bArr, 0, bArr.length)];
            int iDecode0 = decode0(bArr, 0, bArr.length, bArrCopyOf);
            if (iDecode0 != bArrCopyOf.length) {
                bArrCopyOf = Arrays.copyOf(bArrCopyOf, iDecode0);
            }
            return bArrCopyOf;
        }

        public byte[] decode(String str) {
            return decode(str.getBytes(StandardCharsets.ISO_8859_1));
        }

        public int decode(byte[] bArr, byte[] bArr2) {
            if (bArr2.length < outLength(bArr, 0, bArr.length)) {
                throw new IllegalArgumentException("Output byte array is too small for decoding all input bytes");
            }
            return decode0(bArr, 0, bArr.length, bArr2);
        }

        public ByteBuffer decode(ByteBuffer byteBuffer) {
            byte[] bArrArray;
            int iArrayOffset;
            int length;
            int iPosition = byteBuffer.position();
            try {
                if (byteBuffer.hasArray()) {
                    bArrArray = byteBuffer.array();
                    iArrayOffset = byteBuffer.arrayOffset() + byteBuffer.position();
                    length = byteBuffer.arrayOffset() + byteBuffer.limit();
                    byteBuffer.position(byteBuffer.limit());
                } else {
                    bArrArray = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bArrArray);
                    iArrayOffset = 0;
                    length = bArrArray.length;
                }
                byte[] bArr = new byte[outLength(bArrArray, iArrayOffset, length)];
                return ByteBuffer.wrap(bArr, 0, decode0(bArrArray, iArrayOffset, length, bArr));
            } catch (IllegalArgumentException e2) {
                byteBuffer.position(iPosition);
                throw e2;
            }
        }

        public InputStream wrap(InputStream inputStream) {
            Objects.requireNonNull(inputStream);
            return new DecInputStream(inputStream, this.isURL ? fromBase64URL : fromBase64, this.isMIME);
        }

        private int outLength(byte[] bArr, int i2, int i3) {
            int[] iArr = this.isURL ? fromBase64URL : fromBase64;
            int i4 = 0;
            int i5 = i3 - i2;
            if (i5 == 0) {
                return 0;
            }
            if (i5 < 2) {
                if (this.isMIME && iArr[0] == -1) {
                    return 0;
                }
                throw new IllegalArgumentException("Input byte[] should at least have 2 bytes for base64 bytes");
            }
            if (this.isMIME) {
                int i6 = 0;
                while (true) {
                    if (i2 >= i3) {
                        break;
                    }
                    int i7 = i2;
                    i2++;
                    int i8 = bArr[i7] & 255;
                    if (i8 == 61) {
                        i5 -= (i3 - i2) + 1;
                        break;
                    }
                    if (iArr[i8] == -1) {
                        i6++;
                    }
                }
                i5 -= i6;
            } else if (bArr[i3 - 1] == 61) {
                i4 = 0 + 1;
                if (bArr[i3 - 2] == 61) {
                    i4++;
                }
            }
            if (i4 == 0 && (i5 & 3) != 0) {
                i4 = 4 - (i5 & 3);
            }
            return (3 * ((i5 + 3) / 4)) - i4;
        }

        /* JADX WARN: Code restructure failed: missing block: B:20:0x005a, code lost:
        
            if (r15 == 18) goto L21;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00dc, code lost:
        
            if (r15 != 6) goto L35;
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00df, code lost:
        
            r1 = r13;
            r13 = r13 + 1;
            r11[r1] = (byte) (r14 >> 16);
         */
        /* JADX WARN: Code restructure failed: missing block: B:36:0x00f2, code lost:
        
            if (r15 != 0) goto L38;
         */
        /* JADX WARN: Code restructure failed: missing block: B:37:0x00f5, code lost:
        
            r1 = r13;
            r13 = r13 + 1;
            r11[r1] = (byte) (r14 >> 16);
            r13 = r13 + 1;
            r11[r13] = (byte) (r14 >> 8);
         */
        /* JADX WARN: Code restructure failed: missing block: B:39:0x0118, code lost:
        
            if (r15 != 12) goto L66;
         */
        /* JADX WARN: Code restructure failed: missing block: B:41:0x0124, code lost:
        
            throw new java.lang.IllegalArgumentException("Last unit does not have enough valid bits");
         */
        /* JADX WARN: Code restructure failed: missing block: B:43:0x0127, code lost:
        
            if (r9 >= r10) goto L63;
         */
        /* JADX WARN: Code restructure failed: missing block: B:45:0x012e, code lost:
        
            if (r7.isMIME == false) goto L64;
         */
        /* JADX WARN: Code restructure failed: missing block: B:46:0x0131, code lost:
        
            r2 = r9;
            r9 = r9 + 1;
         */
        /* JADX WARN: Code restructure failed: missing block: B:47:0x013a, code lost:
        
            if (r12[r8[r2]] >= 0) goto L65;
         */
        /* JADX WARN: Code restructure failed: missing block: B:50:0x015a, code lost:
        
            throw new java.lang.IllegalArgumentException("Input byte array has incorrect ending byte at " + r9);
         */
        /* JADX WARN: Code restructure failed: missing block: B:52:0x015d, code lost:
        
            return r13;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private int decode0(byte[] r8, int r9, int r10, byte[] r11) {
            /*
                Method dump skipped, instructions count: 350
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Base64.Decoder.decode0(byte[], int, int, byte[]):int");
        }
    }

    /* loaded from: rt.jar:java/util/Base64$EncOutputStream.class */
    private static class EncOutputStream extends FilterOutputStream {
        private int leftover;
        private int b0;
        private int b1;
        private int b2;
        private boolean closed;
        private final char[] base64;
        private final byte[] newline;
        private final int linemax;
        private final boolean doPadding;
        private int linepos;

        EncOutputStream(OutputStream outputStream, char[] cArr, byte[] bArr, int i2, boolean z2) {
            super(outputStream);
            this.leftover = 0;
            this.closed = false;
            this.linepos = 0;
            this.base64 = cArr;
            this.newline = bArr;
            this.linemax = i2;
            this.doPadding = z2;
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(int i2) throws IOException {
            write(new byte[]{(byte) (i2 & 255)}, 0, 1);
        }

        private void checkNewline() throws IOException {
            if (this.linepos == this.linemax) {
                this.out.write(this.newline);
                this.linepos = 0;
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) throws IOException {
            if (this.closed) {
                throw new IOException("Stream is closed");
            }
            if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
                throw new ArrayIndexOutOfBoundsException();
            }
            if (i3 == 0) {
                return;
            }
            if (this.leftover != 0) {
                if (this.leftover == 1) {
                    i2++;
                    this.b1 = bArr[i2] & 255;
                    i3--;
                    if (i3 == 0) {
                        this.leftover++;
                        return;
                    }
                }
                int i4 = i2;
                i2++;
                this.b2 = bArr[i4] & 255;
                i3--;
                checkNewline();
                this.out.write(this.base64[this.b0 >> 2]);
                this.out.write(this.base64[((this.b0 << 4) & 63) | (this.b1 >> 4)]);
                this.out.write(this.base64[((this.b1 << 2) & 63) | (this.b2 >> 6)]);
                this.out.write(this.base64[this.b2 & 63]);
                this.linepos += 4;
            }
            int i5 = i3 / 3;
            this.leftover = i3 - (i5 * 3);
            while (true) {
                int i6 = i5;
                i5--;
                if (i6 <= 0) {
                    break;
                }
                checkNewline();
                int i7 = i2;
                int i8 = i2 + 1;
                int i9 = i8 + 1;
                int i10 = ((bArr[i7] & 255) << 16) | ((bArr[i8] & 255) << 8);
                i2 = i9 + 1;
                int i11 = i10 | (bArr[i9] & 255);
                this.out.write(this.base64[(i11 >>> 18) & 63]);
                this.out.write(this.base64[(i11 >>> 12) & 63]);
                this.out.write(this.base64[(i11 >>> 6) & 63]);
                this.out.write(this.base64[i11 & 63]);
                this.linepos += 4;
            }
            if (this.leftover == 1) {
                int i12 = i2;
                int i13 = i2 + 1;
                this.b0 = bArr[i12] & 255;
            } else if (this.leftover == 2) {
                int i14 = i2;
                int i15 = i2 + 1;
                this.b0 = bArr[i14] & 255;
                int i16 = i15 + 1;
                this.b1 = bArr[i15] & 255;
            }
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                if (this.leftover == 1) {
                    checkNewline();
                    this.out.write(this.base64[this.b0 >> 2]);
                    this.out.write(this.base64[(this.b0 << 4) & 63]);
                    if (this.doPadding) {
                        this.out.write(61);
                        this.out.write(61);
                    }
                } else if (this.leftover == 2) {
                    checkNewline();
                    this.out.write(this.base64[this.b0 >> 2]);
                    this.out.write(this.base64[((this.b0 << 4) & 63) | (this.b1 >> 4)]);
                    this.out.write(this.base64[(this.b1 << 2) & 63]);
                    if (this.doPadding) {
                        this.out.write(61);
                    }
                }
                this.leftover = 0;
                this.out.close();
            }
        }
    }

    /* loaded from: rt.jar:java/util/Base64$DecInputStream.class */
    private static class DecInputStream extends InputStream {
        private final InputStream is;
        private final boolean isMIME;
        private final int[] base64;
        private int bits = 0;
        private int nextin = 18;
        private int nextout = -8;
        private boolean eof = false;
        private boolean closed = false;
        private byte[] sbBuf = new byte[1];

        DecInputStream(InputStream inputStream, int[] iArr, boolean z2) {
            this.is = inputStream;
            this.base64 = iArr;
            this.isMIME = z2;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            if (read(this.sbBuf, 0, 1) == -1) {
                return -1;
            }
            return this.sbBuf[0] & 255;
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            if (this.closed) {
                throw new IOException("Stream is closed");
            }
            if (this.eof && this.nextout < 0) {
                return -1;
            }
            if (i2 < 0 || i3 < 0 || i3 > bArr.length - i2) {
                throw new IndexOutOfBoundsException();
            }
            if (this.nextout >= 0) {
                while (i3 != 0) {
                    int i4 = i2;
                    i2++;
                    bArr[i4] = (byte) (this.bits >> this.nextout);
                    i3--;
                    this.nextout -= 8;
                    if (this.nextout < 0) {
                        this.bits = 0;
                    }
                }
                return i2 - i2;
            }
            while (true) {
                if (i3 <= 0) {
                    break;
                }
                int i5 = this.is.read();
                if (i5 == -1) {
                    this.eof = true;
                    if (this.nextin != 18) {
                        if (this.nextin == 12) {
                            throw new IOException("Base64 stream has one un-decoded dangling byte.");
                        }
                        int i6 = i2;
                        i2++;
                        bArr[i6] = (byte) (this.bits >> 16);
                        int i7 = i3 - 1;
                        if (this.nextin == 0) {
                            if (i7 == 0) {
                                this.bits >>= 8;
                                this.nextout = 0;
                            } else {
                                i2++;
                                bArr[i2] = (byte) (this.bits >> 8);
                            }
                        }
                    }
                    if (i2 != i2) {
                        return i2 - i2;
                    }
                    return -1;
                }
                if (i5 == 61) {
                    if (this.nextin == 18 || this.nextin == 12 || (this.nextin == 6 && this.is.read() != 61)) {
                        throw new IOException("Illegal base64 ending sequence:" + this.nextin);
                    }
                    int i8 = i2;
                    i2++;
                    bArr[i8] = (byte) (this.bits >> 16);
                    int i9 = i3 - 1;
                    if (this.nextin == 0) {
                        if (i9 == 0) {
                            this.bits >>= 8;
                            this.nextout = 0;
                        } else {
                            i2++;
                            bArr[i2] = (byte) (this.bits >> 8);
                        }
                    }
                    this.eof = true;
                } else {
                    int i10 = this.base64[i5];
                    if (i10 == -1) {
                        if (!this.isMIME) {
                            throw new IOException("Illegal base64 character " + Integer.toString(i10, 16));
                        }
                    } else {
                        this.bits |= i10 << this.nextin;
                        if (this.nextin == 0) {
                            this.nextin = 18;
                            this.nextout = 16;
                            while (this.nextout >= 0) {
                                int i11 = i2;
                                i2++;
                                bArr[i11] = (byte) (this.bits >> this.nextout);
                                i3--;
                                this.nextout -= 8;
                                if (i3 == 0 && this.nextout >= 0) {
                                    return i2 - i2;
                                }
                            }
                            this.bits = 0;
                        } else {
                            this.nextin -= 6;
                        }
                    }
                }
            }
            return i2 - i2;
        }

        @Override // java.io.InputStream
        public int available() throws IOException {
            if (this.closed) {
                throw new IOException("Stream is closed");
            }
            return this.is.available();
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (!this.closed) {
                this.closed = true;
                this.is.close();
            }
        }
    }
}
