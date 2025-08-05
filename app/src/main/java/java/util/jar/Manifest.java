package java.util.jar;

import java.io.DataOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: rt.jar:java/util/jar/Manifest.class */
public class Manifest implements Cloneable {
    private final Attributes attr;
    private final Map<String, Attributes> entries;
    private final JarVerifier jv;

    public Manifest() {
        this.attr = new Attributes();
        this.entries = new HashMap();
        this.jv = null;
    }

    public Manifest(InputStream inputStream) throws IOException {
        this(null, inputStream);
    }

    Manifest(JarVerifier jarVerifier, InputStream inputStream) throws IOException {
        this.attr = new Attributes();
        this.entries = new HashMap();
        read(inputStream);
        this.jv = jarVerifier;
    }

    public Manifest(Manifest manifest) {
        this.attr = new Attributes();
        this.entries = new HashMap();
        this.attr.putAll(manifest.getMainAttributes());
        this.entries.putAll(manifest.getEntries());
        this.jv = manifest.jv;
    }

    public Attributes getMainAttributes() {
        return this.attr;
    }

    public Map<String, Attributes> getEntries() {
        return this.entries;
    }

    public Attributes getAttributes(String str) {
        return getEntries().get(str);
    }

    Attributes getTrustedAttributes(String str) {
        Attributes attributes = getAttributes(str);
        if (attributes != null && this.jv != null && !this.jv.isTrustedManifestEntry(str)) {
            throw new SecurityException("Untrusted manifest entry: " + str);
        }
        return attributes;
    }

    public void clear() {
        this.attr.clear();
        this.entries.clear();
    }

    public void write(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        this.attr.writeMain(dataOutputStream);
        for (Map.Entry<String, Attributes> entry : this.entries.entrySet()) {
            StringBuffer stringBuffer = new StringBuffer("Name: ");
            String key = entry.getKey();
            if (key != null) {
                byte[] bytes = key.getBytes(InternalZipConstants.CHARSET_UTF8);
                key = new String(bytes, 0, 0, bytes.length);
            }
            stringBuffer.append(key);
            stringBuffer.append("\r\n");
            make72Safe(stringBuffer);
            dataOutputStream.writeBytes(stringBuffer.toString());
            entry.getValue().write(dataOutputStream);
        }
        dataOutputStream.flush();
    }

    static void make72Safe(StringBuffer stringBuffer) {
        int length = stringBuffer.length();
        if (length > 72) {
            int i2 = 70;
            while (i2 < length - 2) {
                stringBuffer.insert(i2, "\r\n ");
                i2 += 72;
                length += 3;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:34:0x0106  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void read(java.io.InputStream r8) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 328
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.jar.Manifest.read(java.io.InputStream):void");
    }

    private String parseName(byte[] bArr, int i2) {
        if (toLower(bArr[0]) == 110 && toLower(bArr[1]) == 97 && toLower(bArr[2]) == 109 && toLower(bArr[3]) == 101 && bArr[4] == 58 && bArr[5] == 32) {
            try {
                return new String(bArr, 6, i2 - 6, InternalZipConstants.CHARSET_UTF8);
            } catch (Exception e2) {
                return null;
            }
        }
        return null;
    }

    private int toLower(int i2) {
        return (i2 < 65 || i2 > 90) ? i2 : 97 + (i2 - 65);
    }

    public boolean equals(Object obj) {
        if (obj instanceof Manifest) {
            Manifest manifest = (Manifest) obj;
            return this.attr.equals(manifest.getMainAttributes()) && this.entries.equals(manifest.getEntries());
        }
        return false;
    }

    public int hashCode() {
        return this.attr.hashCode() + this.entries.hashCode();
    }

    public Object clone() {
        return new Manifest(this);
    }

    /* loaded from: rt.jar:java/util/jar/Manifest$FastInputStream.class */
    static class FastInputStream extends FilterInputStream {
        private byte[] buf;
        private int count;
        private int pos;

        FastInputStream(InputStream inputStream) {
            this(inputStream, 8192);
        }

        FastInputStream(InputStream inputStream, int i2) {
            super(inputStream);
            this.count = 0;
            this.pos = 0;
            this.buf = new byte[i2];
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read() throws IOException {
            if (this.pos >= this.count) {
                fill();
                if (this.pos >= this.count) {
                    return -1;
                }
            }
            byte[] bArr = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            return Byte.toUnsignedInt(bArr[i2]);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int read(byte[] bArr, int i2, int i3) throws IOException {
            int i4 = this.count - this.pos;
            if (i4 <= 0) {
                if (i3 >= this.buf.length) {
                    return this.in.read(bArr, i2, i3);
                }
                fill();
                i4 = this.count - this.pos;
                if (i4 <= 0) {
                    return -1;
                }
            }
            if (i3 > i4) {
                i3 = i4;
            }
            System.arraycopy(this.buf, this.pos, bArr, i2, i3);
            this.pos += i3;
            return i3;
        }

        public int readLine(byte[] bArr, int i2, int i3) throws IOException {
            byte[] bArr2 = this.buf;
            int i4 = 0;
            while (i4 < i3) {
                int i5 = this.count - this.pos;
                if (i5 <= 0) {
                    fill();
                    i5 = this.count - this.pos;
                    if (i5 <= 0) {
                        return -1;
                    }
                }
                int i6 = i3 - i4;
                if (i6 > i5) {
                    i6 = i5;
                }
                int i7 = this.pos;
                int i8 = i7 + i6;
                while (i7 < i8) {
                    int i9 = i7;
                    i7++;
                    if (bArr2[i9] == 10) {
                        break;
                    }
                }
                int i10 = i7 - this.pos;
                System.arraycopy(bArr2, this.pos, bArr, i2, i10);
                i2 += i10;
                i4 += i10;
                this.pos = i7;
                if (bArr2[i7 - 1] == 10) {
                    break;
                }
            }
            return i4;
        }

        public byte peek() throws IOException {
            if (this.pos == this.count) {
                fill();
            }
            if (this.pos == this.count) {
                return (byte) -1;
            }
            return this.buf[this.pos];
        }

        public int readLine(byte[] bArr) throws IOException {
            return readLine(bArr, 0, bArr.length);
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long j2) throws IOException {
            if (j2 <= 0) {
                return 0L;
            }
            long j3 = this.count - this.pos;
            if (j3 <= 0) {
                return this.in.skip(j2);
            }
            if (j2 > j3) {
                j2 = j3;
            }
            this.pos = (int) (this.pos + j2);
            return j2;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public int available() throws IOException {
            return (this.count - this.pos) + this.in.available();
        }

        @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            if (this.in != null) {
                this.in.close();
                this.in = null;
                this.buf = null;
            }
        }

        private void fill() throws IOException {
            this.pos = 0;
            this.count = 0;
            int i2 = this.in.read(this.buf, 0, this.buf.length);
            if (i2 > 0) {
                this.count = i2;
            }
        }
    }
}
