package org.icepdf.core.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/* loaded from: icepdf-core.jar:org/icepdf/core/io/SequenceInputStream.class */
public class SequenceInputStream extends InputStream {
    private Iterator<InputStream> m_itInputStreams;
    private InputStream m_isCurrent;

    public SequenceInputStream(InputStream... in) {
        this((List<InputStream>) Arrays.asList(in));
    }

    public SequenceInputStream(List<InputStream> inputStreams) {
        this(inputStreams, -1);
    }

    public SequenceInputStream(List<InputStream> inputStreams, int streamSwitchValue) {
        List<InputStream> in = new ArrayList<>();
        for (int i2 = 0; i2 < inputStreams.size(); i2++) {
            if (i2 > 0 && streamSwitchValue != -1) {
                in.add(new ByteArrayInputStream(new byte[]{(byte) streamSwitchValue}));
            }
            in.add(inputStreams.get(i2));
        }
        this.m_itInputStreams = in.iterator();
        try {
            useNextInputStream();
        } catch (IOException e2) {
            throw new IllegalStateException("Could not use first InputStream in SequenceInputStream(List) : " + ((Object) e2));
        }
    }

    private InputStream getCurrentInputStream() {
        return this.m_isCurrent;
    }

    private void useNextInputStream() throws IOException {
        closeCurrentInputStream();
        this.m_isCurrent = null;
        while (this.m_itInputStreams.hasNext()) {
            InputStream in = this.m_itInputStreams.next();
            if (in != null) {
                this.m_isCurrent = in;
                return;
            }
        }
    }

    private void closeCurrentInputStream() throws IOException {
        InputStream in = getCurrentInputStream();
        if (in != null) {
            in.close();
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        InputStream in = getCurrentInputStream();
        if (in != null) {
            return in.available();
        }
        return 0;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        while (true) {
            InputStream in = getCurrentInputStream();
            if (in == null) {
                useNextInputStream();
                in = getCurrentInputStream();
                if (in == null) {
                    return -1;
                }
            }
            int readByte = in.read();
            if (readByte >= 0) {
                return readByte;
            }
            useNextInputStream();
        }
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int off, int len) throws IOException {
        if (buffer == null) {
            throw new NullPointerException();
        }
        if (off < 0 || off >= buffer.length || len < 0 || off + len > buffer.length || off + len < 0) {
            throw new IndexOutOfBoundsException("Offset: " + off + ", Length: " + len + ", Buffer length: " + buffer.length);
        }
        if (len == 0) {
            return 0;
        }
        int totalRead = 0;
        while (true) {
            if (totalRead >= len) {
                break;
            }
            InputStream in = getCurrentInputStream();
            if (in == null) {
                useNextInputStream();
                in = getCurrentInputStream();
                if (in == null) {
                    if (totalRead <= 0) {
                        return -1;
                    }
                }
            }
            int currRead = in.read(buffer, off + totalRead, len - totalRead);
            if (currRead > 0) {
                totalRead += currRead;
            } else {
                useNextInputStream();
            }
        }
        return totalRead;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        do {
            useNextInputStream();
        } while (getCurrentInputStream() != null);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(": ");
        List<InputStream> inputStreams = new ArrayList<>();
        while (this.m_itInputStreams.hasNext()) {
            InputStream in = this.m_itInputStreams.next();
            sb.append("\n  ");
            sb.append(in.toString());
            sb.append(",");
            inputStreams.add(in);
        }
        this.m_itInputStreams = inputStreams.iterator();
        sb.append('\n');
        return sb.toString();
    }
}
