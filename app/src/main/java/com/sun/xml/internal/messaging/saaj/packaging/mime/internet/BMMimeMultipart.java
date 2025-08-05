package com.sun.xml.internal.messaging.saaj.packaging.mime.internet;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.ASCIIUtility;
import com.sun.xml.internal.messaging.saaj.packaging.mime.util.OutputUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.BitSet;
import javax.activation.DataSource;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/internet/BMMimeMultipart.class */
public class BMMimeMultipart extends MimeMultipart {
    private boolean begining;
    int[] bcs;
    int[] gss;
    private static final int BUFFER_SIZE = 4096;
    private byte[] buffer;
    private byte[] prevBuffer;
    private BitSet lastPartFound;
    private InputStream in;
    private String boundary;

    /* renamed from: b, reason: collision with root package name */
    int f12077b;
    private boolean lazyAttachments;
    byte[] buf;

    public BMMimeMultipart() {
        this.begining = true;
        this.bcs = new int[256];
        this.gss = null;
        this.buffer = new byte[4096];
        this.prevBuffer = new byte[4096];
        this.lastPartFound = new BitSet(1);
        this.in = null;
        this.boundary = null;
        this.f12077b = 0;
        this.lazyAttachments = false;
        this.buf = new byte[1024];
    }

    public BMMimeMultipart(String subtype) {
        super(subtype);
        this.begining = true;
        this.bcs = new int[256];
        this.gss = null;
        this.buffer = new byte[4096];
        this.prevBuffer = new byte[4096];
        this.lastPartFound = new BitSet(1);
        this.in = null;
        this.boundary = null;
        this.f12077b = 0;
        this.lazyAttachments = false;
        this.buf = new byte[1024];
    }

    public BMMimeMultipart(DataSource ds, ContentType ct) throws MessagingException {
        super(ds, ct);
        this.begining = true;
        this.bcs = new int[256];
        this.gss = null;
        this.buffer = new byte[4096];
        this.prevBuffer = new byte[4096];
        this.lastPartFound = new BitSet(1);
        this.in = null;
        this.boundary = null;
        this.f12077b = 0;
        this.lazyAttachments = false;
        this.buf = new byte[1024];
        this.boundary = ct.getParameter("boundary");
    }

    public InputStream initStream() throws MessagingException {
        if (this.in == null) {
            try {
                this.in = this.ds.getInputStream();
                if (!(this.in instanceof ByteArrayInputStream) && !(this.in instanceof BufferedInputStream) && !(this.in instanceof SharedInputStream)) {
                    this.in = new BufferedInputStream(this.in);
                }
                if (!this.in.markSupported()) {
                    throw new MessagingException("InputStream does not support Marking");
                }
            } catch (Exception e2) {
                throw new MessagingException("No inputstream from datasource");
            }
        }
        return this.in;
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart
    protected void parse() throws MessagingException {
        if (this.parsed) {
            return;
        }
        initStream();
        SharedInputStream sin = null;
        if (this.in instanceof SharedInputStream) {
            sin = (SharedInputStream) this.in;
        }
        String bnd = "--" + this.boundary;
        byte[] bndbytes = ASCIIUtility.getBytes(bnd);
        try {
            parse(this.in, bndbytes, sin);
            this.parsed = true;
        } catch (IOException ioex) {
            throw new MessagingException("IO Error", ioex);
        } catch (Exception ex) {
            throw new MessagingException("Error", ex);
        }
    }

    public boolean lastBodyPartFound() {
        return this.lastPartFound.get(0);
    }

    public MimeBodyPart getNextPart(InputStream stream, byte[] pattern, SharedInputStream sin) throws Exception {
        if (!stream.markSupported()) {
            throw new Exception("InputStream does not support Marking");
        }
        if (this.begining) {
            compile(pattern);
            if (!skipPreamble(stream, pattern, sin)) {
                throw new Exception("Missing Start Boundary, or boundary does not start on a new line");
            }
            this.begining = false;
        }
        if (lastBodyPartFound()) {
            throw new Exception("No parts found in Multipart InputStream");
        }
        if (sin != null) {
            long start = sin.getPosition();
            this.f12077b = readHeaders(stream);
            if (this.f12077b == -1) {
                throw new Exception("End of Stream encountered while reading part headers");
            }
            long[] v2 = {-1};
            this.f12077b = readBody(stream, pattern, v2, null, sin);
            if (!ignoreMissingEndBoundary && this.f12077b == -1 && !lastBodyPartFound()) {
                throw new MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
            }
            long end = v2[0];
            MimeBodyPart mbp = createMimeBodyPart(sin.newStream(start, end));
            addBodyPart(mbp);
            return mbp;
        }
        InternetHeaders headers = createInternetHeaders(stream);
        ByteOutputStream baos = new ByteOutputStream();
        this.f12077b = readBody(stream, pattern, null, baos, null);
        if (!ignoreMissingEndBoundary && this.f12077b == -1 && !lastBodyPartFound()) {
            throw new MessagingException("Missing End Boundary for Mime Package : EOF while skipping headers");
        }
        MimeBodyPart mbp2 = createMimeBodyPart(headers, baos.getBytes(), baos.getCount());
        addBodyPart(mbp2);
        return mbp2;
    }

    public boolean parse(InputStream stream, byte[] pattern, SharedInputStream sin) throws Exception {
        while (!this.lastPartFound.get(0) && this.f12077b != -1) {
            getNextPart(stream, pattern, sin);
        }
        return true;
    }

    private int readHeaders(InputStream is) throws Exception {
        int b2 = is.read();
        while (b2 != -1) {
            if (b2 == 13) {
                b2 = is.read();
                if (b2 == 10) {
                    b2 = is.read();
                    if (b2 == 13) {
                        b2 = is.read();
                        if (b2 == 10) {
                            return b2;
                        }
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else {
                b2 = is.read();
            }
        }
        if (b2 == -1) {
            throw new Exception("End of inputstream while reading Mime-Part Headers");
        }
        return b2;
    }

    private int readBody(InputStream is, byte[] pattern, long[] posVector, ByteOutputStream baos, SharedInputStream sin) throws Exception {
        if (!find(is, pattern, posVector, baos, sin)) {
            throw new Exception("Missing boundary delimitier while reading Body Part");
        }
        return this.f12077b;
    }

    private boolean skipPreamble(InputStream is, byte[] pattern, SharedInputStream sin) throws Exception {
        if (!find(is, pattern, sin)) {
            return false;
        }
        if (this.lastPartFound.get(0)) {
            throw new Exception("Found closing boundary delimiter while trying to skip preamble");
        }
        return true;
    }

    public int readNext(InputStream is, byte[] buff, int patternLength, BitSet eof, long[] posVector, SharedInputStream sin) throws Exception {
        int bufferLength = is.read(this.buffer, 0, patternLength);
        if (bufferLength == -1) {
            eof.flip(0);
        } else if (bufferLength < patternLength) {
            long pos = 0;
            int i2 = bufferLength;
            while (true) {
                if (i2 >= patternLength) {
                    break;
                }
                if (sin != null) {
                    pos = sin.getPosition();
                }
                int temp = is.read();
                if (temp == -1) {
                    eof.flip(0);
                    if (sin != null) {
                        posVector[0] = pos;
                    }
                } else {
                    this.buffer[i2] = (byte) temp;
                    i2++;
                }
            }
            bufferLength = i2;
        }
        return bufferLength;
    }

    public boolean find(InputStream is, byte[] pattern, SharedInputStream sin) throws Exception {
        int l2 = pattern.length;
        int lx = l2 - 1;
        BitSet eof = new BitSet(1);
        long[] posVector = new long[1];
        while (true) {
            is.mark(l2);
            readNext(is, this.buffer, l2, eof, posVector, sin);
            if (eof.get(0)) {
                return false;
            }
            int i2 = lx;
            while (i2 >= 0 && this.buffer[i2] == pattern[i2]) {
                i2--;
            }
            if (i2 < 0) {
                if (!skipLWSPAndCRLF(is)) {
                    throw new Exception("Boundary does not terminate with CRLF");
                }
                return true;
            }
            int s2 = Math.max((i2 + 1) - this.bcs[this.buffer[i2] & Byte.MAX_VALUE], this.gss[i2]);
            is.reset();
            is.skip(s2);
        }
    }

    public boolean find(InputStream is, byte[] pattern, long[] posVector, ByteOutputStream out, SharedInputStream sin) throws Exception {
        int l2 = pattern.length;
        int lx = l2 - 1;
        int s2 = 0;
        long endPos = -1;
        boolean first = true;
        BitSet eof = new BitSet(1);
        while (true) {
            is.mark(l2);
            if (!first) {
                byte[] tmp = this.prevBuffer;
                this.prevBuffer = this.buffer;
                this.buffer = tmp;
            }
            if (sin != null) {
                endPos = sin.getPosition();
            }
            int bufferLength = readNext(is, this.buffer, l2, eof, posVector, sin);
            if (bufferLength == -1) {
                this.f12077b = -1;
                if (s2 == l2 && sin == null) {
                    out.write(this.prevBuffer, 0, s2);
                    return true;
                }
                return true;
            }
            if (bufferLength < l2) {
                if (sin == null) {
                    out.write(this.buffer, 0, bufferLength);
                }
                this.f12077b = -1;
                return true;
            }
            int i2 = lx;
            while (i2 >= 0 && this.buffer[i2] == pattern[i2]) {
                i2--;
            }
            if (i2 < 0) {
                if (s2 > 0) {
                    if (s2 <= 2) {
                        if (s2 == 2) {
                            if (this.prevBuffer[1] == 10) {
                                if (this.prevBuffer[0] != 13 && this.prevBuffer[0] != 10) {
                                    out.write(this.prevBuffer, 0, 1);
                                }
                                if (sin != null) {
                                    posVector[0] = endPos;
                                }
                            } else {
                                throw new Exception("Boundary characters encountered in part Body without a preceeding CRLF");
                            }
                        } else if (s2 == 1) {
                            if (this.prevBuffer[0] != 10) {
                                throw new Exception("Boundary characters encountered in part Body without a preceeding CRLF");
                            }
                            if (sin != null) {
                                posVector[0] = endPos;
                            }
                        }
                    } else if (s2 > 2) {
                        if (this.prevBuffer[s2 - 2] == 13 && this.prevBuffer[s2 - 1] == 10) {
                            if (sin != null) {
                                posVector[0] = endPos - 2;
                            } else {
                                out.write(this.prevBuffer, 0, s2 - 2);
                            }
                        } else if (this.prevBuffer[s2 - 1] == 10) {
                            if (sin != null) {
                                posVector[0] = endPos - 1;
                            } else {
                                out.write(this.prevBuffer, 0, s2 - 1);
                            }
                        } else {
                            throw new Exception("Boundary characters encountered in part Body without a preceeding CRLF");
                        }
                    }
                }
                if (!skipLWSPAndCRLF(is)) {
                }
                return true;
            }
            if (s2 > 0 && sin == null) {
                if (this.prevBuffer[s2 - 1] == 13 && this.buffer[0] == 10) {
                    int i3 = lx - 1;
                    int j2 = lx - 1;
                    while (j2 > 0 && this.buffer[j2 + 1] == pattern[j2]) {
                        j2--;
                    }
                    if (j2 == 0) {
                        out.write(this.prevBuffer, 0, s2 - 1);
                    } else {
                        out.write(this.prevBuffer, 0, s2);
                    }
                } else {
                    out.write(this.prevBuffer, 0, s2);
                }
            }
            s2 = Math.max((i2 + 1) - this.bcs[this.buffer[i2] & Byte.MAX_VALUE], this.gss[i2]);
            is.reset();
            is.skip(s2);
            if (first) {
                first = false;
            }
        }
    }

    private boolean skipLWSPAndCRLF(InputStream is) throws Exception {
        this.f12077b = is.read();
        if (this.f12077b == 10) {
            return true;
        }
        if (this.f12077b == 13) {
            this.f12077b = is.read();
            if (this.f12077b == 13) {
                this.f12077b = is.read();
            }
            if (this.f12077b == 10) {
                return true;
            }
            throw new Exception("transport padding after a Mime Boundary  should end in a CRLF, found CR only");
        }
        if (this.f12077b == 45) {
            this.f12077b = is.read();
            if (this.f12077b != 45) {
                throw new Exception("Unexpected singular '-' character after Mime Boundary");
            }
            this.lastPartFound.flip(0);
            this.f12077b = is.read();
        }
        while (this.f12077b != -1 && (this.f12077b == 32 || this.f12077b == 9)) {
            this.f12077b = is.read();
            if (this.f12077b == 10) {
                return true;
            }
            if (this.f12077b == 13) {
                this.f12077b = is.read();
                if (this.f12077b == 13) {
                    this.f12077b = is.read();
                }
                if (this.f12077b == 10) {
                    return true;
                }
            }
        }
        if (this.f12077b == -1) {
            if (!this.lastPartFound.get(0)) {
                throw new Exception("End of Multipart Stream before encountering  closing boundary delimiter");
            }
            return true;
        }
        return false;
    }

    private void compile(byte[] pattern) {
        int l2 = pattern.length;
        for (int i2 = 0; i2 < l2; i2++) {
            this.bcs[pattern[i2]] = i2 + 1;
        }
        this.gss = new int[l2];
        for (int i3 = l2; i3 > 0; i3--) {
            int j2 = l2 - 1;
            while (true) {
                if (j2 >= i3) {
                    if (pattern[j2] == pattern[j2 - i3]) {
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
        this.gss[l2 - 1] = 1;
    }

    @Override // com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeMultipart
    public void writeTo(OutputStream os) throws MessagingException, IOException {
        if (this.in != null) {
            this.contentType.setParameter("boundary", this.boundary);
        }
        String bnd = "--" + this.contentType.getParameter("boundary");
        for (int i2 = 0; i2 < this.parts.size(); i2++) {
            OutputUtil.writeln(bnd, os);
            ((MimeBodyPart) this.parts.get(i2)).writeTo(os);
            OutputUtil.writeln(os);
        }
        if (this.in != null) {
            OutputUtil.writeln(bnd, os);
            if ((os instanceof ByteOutputStream) && this.lazyAttachments) {
                ((ByteOutputStream) os).write(this.in);
                return;
            }
            ByteOutputStream baos = new ByteOutputStream(this.in.available());
            baos.write(this.in);
            baos.writeTo(os);
            this.in = baos.newInputStream();
            return;
        }
        OutputUtil.writeAsAscii(bnd, os);
        OutputUtil.writeAsAscii("--", os);
    }

    public void setInputStream(InputStream is) {
        this.in = is;
    }

    public InputStream getInputStream() {
        return this.in;
    }

    public void setBoundary(String bnd) {
        this.boundary = bnd;
        if (this.contentType != null) {
            this.contentType.setParameter("boundary", bnd);
        }
    }

    public String getBoundary() {
        return this.boundary;
    }

    public boolean isEndOfStream() {
        return this.f12077b == -1;
    }

    public void setLazyAttachments(boolean flag) {
        this.lazyAttachments = flag;
    }
}
