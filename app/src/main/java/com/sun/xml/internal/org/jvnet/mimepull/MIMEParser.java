package com.sun.xml.internal.org.jvnet.mimepull;

import com.sun.xml.internal.org.jvnet.mimepull.MIMEEvent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEParser.class */
class MIMEParser implements Iterable<MIMEEvent> {
    private static final Logger LOGGER;
    private static final String HEADER_ENCODING = "ISO8859-1";
    private static final int NO_LWSP = 1000;
    private final InputStream in;
    private final byte[] bndbytes;

    /* renamed from: bl, reason: collision with root package name */
    private final int f12079bl;
    private final MIMEConfig config;
    private final int[] gss;
    private boolean parsed;
    private boolean eof;
    private final int capacity;
    private byte[] buf;
    private int len;
    private boolean bol;
    static final /* synthetic */ boolean $assertionsDisabled;
    private STATE state = STATE.START_MESSAGE;
    private final int[] bcs = new int[128];
    private boolean done = false;

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEParser$STATE.class */
    private enum STATE {
        START_MESSAGE,
        SKIP_PREAMBLE,
        START_PART,
        HEADERS,
        BODY,
        END_PART,
        END_MESSAGE
    }

    static {
        $assertionsDisabled = !MIMEParser.class.desiredAssertionStatus();
        LOGGER = Logger.getLogger(MIMEParser.class.getName());
    }

    MIMEParser(InputStream in, String boundary, MIMEConfig config) {
        this.in = in;
        this.bndbytes = getBytes("--" + boundary);
        this.f12079bl = this.bndbytes.length;
        this.config = config;
        this.gss = new int[this.f12079bl];
        compileBoundaryPattern();
        this.capacity = config.chunkSize + 2 + this.f12079bl + 4 + 1000;
        createBuf(this.capacity);
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<MIMEEvent> iterator() {
        return new MIMEEventIterator();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEParser$MIMEEventIterator.class */
    class MIMEEventIterator implements Iterator<MIMEEvent> {
        MIMEEventIterator() {
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return !MIMEParser.this.parsed;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public MIMEEvent next() {
            switch (MIMEParser.this.state) {
                case START_MESSAGE:
                    if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                        MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.START_MESSAGE);
                    }
                    MIMEParser.this.state = STATE.SKIP_PREAMBLE;
                    return MIMEEvent.START_MESSAGE;
                case SKIP_PREAMBLE:
                    if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                        MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.SKIP_PREAMBLE);
                    }
                    MIMEParser.this.skipPreamble();
                    break;
                case START_PART:
                    break;
                case HEADERS:
                    if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                        MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.HEADERS);
                    }
                    InternetHeaders ih = MIMEParser.this.readHeaders();
                    MIMEParser.this.state = STATE.BODY;
                    MIMEParser.this.bol = true;
                    return new MIMEEvent.Headers(ih);
                case BODY:
                    if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                        MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.BODY);
                    }
                    ByteBuffer buf = MIMEParser.this.readBody();
                    MIMEParser.this.bol = false;
                    return new MIMEEvent.Content(buf);
                case END_PART:
                    if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                        MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.END_PART);
                    }
                    if (MIMEParser.this.done) {
                        MIMEParser.this.state = STATE.END_MESSAGE;
                    } else {
                        MIMEParser.this.state = STATE.START_PART;
                    }
                    return MIMEEvent.END_PART;
                case END_MESSAGE:
                    if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                        MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.END_MESSAGE);
                    }
                    MIMEParser.this.parsed = true;
                    return MIMEEvent.END_MESSAGE;
                default:
                    throw new MIMEParsingException("Unknown Parser state = " + ((Object) MIMEParser.this.state));
            }
            if (MIMEParser.LOGGER.isLoggable(Level.FINER)) {
                MIMEParser.LOGGER.log(Level.FINER, "MIMEParser state={0}", STATE.START_PART);
            }
            MIMEParser.this.state = STATE.HEADERS;
            return MIMEEvent.START_PART;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public InternetHeaders readHeaders() {
        if (!this.eof) {
            fillBuf();
        }
        return new InternetHeaders(new LineInputStream());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ByteBuffer readBody() {
        if (!this.eof) {
            fillBuf();
        }
        int start = match(this.buf, 0, this.len);
        if (start == -1) {
            if (!$assertionsDisabled && !this.eof && this.len < this.config.chunkSize) {
                throw new AssertionError();
            }
            int chunkSize = this.eof ? this.len : this.config.chunkSize;
            if (this.eof) {
                this.done = true;
                throw new MIMEParsingException("Reached EOF, but there is no closing MIME boundary.");
            }
            return adjustBuf(chunkSize, this.len - chunkSize);
        }
        int chunkLen = start;
        if (!this.bol || start != 0) {
            if (start > 0 && (this.buf[start - 1] == 10 || this.buf[start - 1] == 13)) {
                chunkLen--;
                if (this.buf[start - 1] == 10 && start > 1 && this.buf[start - 2] == 13) {
                    chunkLen--;
                }
            } else {
                return adjustBuf(start + 1, (this.len - start) - 1);
            }
        }
        if (start + this.f12079bl + 1 < this.len && this.buf[start + this.f12079bl] == 45 && this.buf[start + this.f12079bl + 1] == 45) {
            this.state = STATE.END_PART;
            this.done = true;
            return adjustBuf(chunkLen, 0);
        }
        int lwsp = 0;
        for (int i2 = start + this.f12079bl; i2 < this.len && (this.buf[i2] == 32 || this.buf[i2] == 9); i2++) {
            lwsp++;
        }
        if (start + this.f12079bl + lwsp < this.len && this.buf[start + this.f12079bl + lwsp] == 10) {
            this.state = STATE.END_PART;
            return adjustBuf(chunkLen, (((this.len - start) - this.f12079bl) - lwsp) - 1);
        }
        if (start + this.f12079bl + lwsp + 1 < this.len && this.buf[start + this.f12079bl + lwsp] == 13 && this.buf[start + this.f12079bl + lwsp + 1] == 10) {
            this.state = STATE.END_PART;
            return adjustBuf(chunkLen, (((this.len - start) - this.f12079bl) - lwsp) - 2);
        }
        if (start + this.f12079bl + lwsp + 1 < this.len) {
            return adjustBuf(chunkLen + 1, (this.len - chunkLen) - 1);
        }
        if (this.eof) {
            this.done = true;
            throw new MIMEParsingException("Reached EOF, but there is no closing MIME boundary.");
        }
        return adjustBuf(chunkLen, this.len - chunkLen);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ByteBuffer adjustBuf(int chunkSize, int remaining) {
        if (!$assertionsDisabled && this.buf == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && chunkSize < 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && remaining < 0) {
            throw new AssertionError();
        }
        byte[] temp = this.buf;
        createBuf(remaining);
        System.arraycopy(temp, this.len - remaining, this.buf, 0, remaining);
        this.len = remaining;
        return ByteBuffer.wrap(temp, 0, chunkSize);
    }

    private void createBuf(int min) {
        this.buf = new byte[min < this.capacity ? this.capacity : min];
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void skipPreamble() {
        while (true) {
            if (!this.eof) {
                fillBuf();
            }
            int start = match(this.buf, 0, this.len);
            if (start == -1) {
                if (this.eof) {
                    throw new MIMEParsingException("Missing start boundary");
                }
                adjustBuf((this.len - this.f12079bl) + 1, this.f12079bl - 1);
            } else if (start > this.config.chunkSize) {
                adjustBuf(start, this.len - start);
            } else {
                int lwsp = 0;
                for (int i2 = start + this.f12079bl; i2 < this.len && (this.buf[i2] == 32 || this.buf[i2] == 9); i2++) {
                    lwsp++;
                }
                if (start + this.f12079bl + lwsp < this.len && (this.buf[start + this.f12079bl + lwsp] == 10 || this.buf[start + this.f12079bl + lwsp] == 13)) {
                    if (this.buf[start + this.f12079bl + lwsp] == 10) {
                        adjustBuf(start + this.f12079bl + lwsp + 1, (((this.len - start) - this.f12079bl) - lwsp) - 1);
                        break;
                    } else if (start + this.f12079bl + lwsp + 1 < this.len && this.buf[start + this.f12079bl + lwsp + 1] == 10) {
                        adjustBuf(start + this.f12079bl + lwsp + 2, (((this.len - start) - this.f12079bl) - lwsp) - 2);
                        break;
                    }
                }
                adjustBuf(start + 1, (this.len - start) - 1);
            }
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "Skipped the preamble. buffer len={0}", Integer.valueOf(this.len));
        }
    }

    private static byte[] getBytes(String s2) {
        char[] chars = s2.toCharArray();
        int size = chars.length;
        byte[] bytes = new byte[size];
        int i2 = 0;
        while (i2 < size) {
            int i3 = i2;
            int i4 = i2;
            i2++;
            bytes[i3] = (byte) chars[i4];
        }
        return bytes;
    }

    private void compileBoundaryPattern() {
        for (int i2 = 0; i2 < this.bndbytes.length; i2++) {
            this.bcs[this.bndbytes[i2] & Byte.MAX_VALUE] = i2 + 1;
        }
        for (int i3 = this.bndbytes.length; i3 > 0; i3--) {
            int j2 = this.bndbytes.length - 1;
            while (true) {
                if (j2 >= i3) {
                    if (this.bndbytes[j2] == this.bndbytes[j2 - i3]) {
                        this.gss[j2 - 1] = i3;
                        j2--;
                    }
                } else {
                    while (j2 > 0) {
                        j2--;
                        this.gss[j2] = i3;
                    }
                }
            }
        }
        this.gss[this.bndbytes.length - 1] = 1;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0031, code lost:
    
        r8 = r8 + java.lang.Math.max((r11 + 1) - r6.bcs[r0 & Byte.MAX_VALUE], r6.gss[r11]);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int match(byte[] r7, int r8, int r9) {
        /*
            r6 = this;
            r0 = r9
            r1 = r6
            byte[] r1 = r1.bndbytes
            int r1 = r1.length
            int r0 = r0 - r1
            r10 = r0
        L9:
            r0 = r8
            r1 = r10
            if (r0 > r1) goto L58
            r0 = r6
            byte[] r0 = r0.bndbytes
            int r0 = r0.length
            r1 = 1
            int r0 = r0 - r1
            r11 = r0
        L18:
            r0 = r11
            if (r0 < 0) goto L56
            r0 = r7
            r1 = r8
            r2 = r11
            int r1 = r1 + r2
            r0 = r0[r1]
            r12 = r0
            r0 = r12
            r1 = r6
            byte[] r1 = r1.bndbytes
            r2 = r11
            r1 = r1[r2]
            if (r0 == r1) goto L50
            r0 = r8
            r1 = r11
            r2 = 1
            int r1 = r1 + r2
            r2 = r6
            int[] r2 = r2.bcs
            r3 = r12
            r4 = 127(0x7f, float:1.78E-43)
            r3 = r3 & r4
            r2 = r2[r3]
            int r1 = r1 - r2
            r2 = r6
            int[] r2 = r2.gss
            r3 = r11
            r2 = r2[r3]
            int r1 = java.lang.Math.max(r1, r2)
            int r0 = r0 + r1
            r8 = r0
            goto L9
        L50:
            int r11 = r11 + (-1)
            goto L18
        L56:
            r0 = r8
            return r0
        L58:
            r0 = -1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.org.jvnet.mimepull.MIMEParser.match(byte[], int, int):int");
    }

    private void fillBuf() {
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "Before fillBuf() buffer len={0}", Integer.valueOf(this.len));
        }
        if (!$assertionsDisabled && this.eof) {
            throw new AssertionError();
        }
        while (true) {
            if (this.len >= this.buf.length) {
                break;
            }
            try {
                int read = this.in.read(this.buf, this.len, this.buf.length - this.len);
                if (read == -1) {
                    this.eof = true;
                    try {
                        if (LOGGER.isLoggable(Level.FINE)) {
                            LOGGER.fine("Closing the input stream.");
                        }
                        this.in.close();
                    } catch (IOException ioe) {
                        throw new MIMEParsingException(ioe);
                    }
                } else {
                    this.len += read;
                }
            } catch (IOException ioe2) {
                throw new MIMEParsingException(ioe2);
            }
        }
        if (LOGGER.isLoggable(Level.FINER)) {
            LOGGER.log(Level.FINER, "After fillBuf() buffer len={0}", Integer.valueOf(this.len));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doubleBuf() {
        byte[] temp = new byte[2 * this.len];
        System.arraycopy(this.buf, 0, temp, 0, this.len);
        this.buf = temp;
        if (!this.eof) {
            fillBuf();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/MIMEParser$LineInputStream.class */
    class LineInputStream {
        private int offset;
        static final /* synthetic */ boolean $assertionsDisabled;

        LineInputStream() {
        }

        static {
            $assertionsDisabled = !MIMEParser.class.desiredAssertionStatus();
        }

        public String readLine() throws IOException {
            int hdrLen = 0;
            int lwsp = 0;
            while (true) {
                if (this.offset + hdrLen < MIMEParser.this.len) {
                    if (MIMEParser.this.buf[this.offset + hdrLen] != 10) {
                        if (this.offset + hdrLen + 1 == MIMEParser.this.len) {
                            MIMEParser.this.doubleBuf();
                        }
                        if (this.offset + hdrLen + 1 < MIMEParser.this.len) {
                            if (MIMEParser.this.buf[this.offset + hdrLen] == 13 && MIMEParser.this.buf[this.offset + hdrLen + 1] == 10) {
                                lwsp = 2;
                                break;
                            }
                            hdrLen++;
                        } else {
                            if ($assertionsDisabled || MIMEParser.this.eof) {
                                return null;
                            }
                            throw new AssertionError();
                        }
                    } else {
                        lwsp = 1;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (hdrLen == 0) {
                MIMEParser.this.adjustBuf(this.offset + lwsp, (MIMEParser.this.len - this.offset) - lwsp);
                return null;
            }
            String hdr = new String(MIMEParser.this.buf, this.offset, hdrLen, MIMEParser.HEADER_ENCODING);
            this.offset += hdrLen + lwsp;
            return hdr;
        }
    }
}
