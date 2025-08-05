package java.util.jar;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/* loaded from: rt.jar:java/util/jar/JarOutputStream.class */
public class JarOutputStream extends ZipOutputStream {
    private static final int JAR_MAGIC = 51966;
    private boolean firstEntry;

    public JarOutputStream(OutputStream outputStream, Manifest manifest) throws IOException {
        super(outputStream);
        this.firstEntry = true;
        if (manifest == null) {
            throw new NullPointerException("man");
        }
        putNextEntry(new ZipEntry(JarFile.MANIFEST_NAME));
        manifest.write(new BufferedOutputStream(this));
        closeEntry();
    }

    public JarOutputStream(OutputStream outputStream) throws IOException {
        super(outputStream);
        this.firstEntry = true;
    }

    @Override // java.util.zip.ZipOutputStream
    public void putNextEntry(ZipEntry zipEntry) throws IOException {
        byte[] bArr;
        if (this.firstEntry) {
            byte[] extra = zipEntry.getExtra();
            if (extra == null || !hasMagic(extra)) {
                if (extra == null) {
                    bArr = new byte[4];
                } else {
                    byte[] bArr2 = new byte[extra.length + 4];
                    System.arraycopy(extra, 0, bArr2, 4, extra.length);
                    bArr = bArr2;
                }
                set16(bArr, 0, JAR_MAGIC);
                set16(bArr, 2, 0);
                zipEntry.setExtra(bArr);
            }
            this.firstEntry = false;
        }
        super.putNextEntry(zipEntry);
    }

    private static boolean hasMagic(byte[] bArr) {
        int i2 = 0;
        while (i2 < bArr.length) {
            try {
                if (get16(bArr, i2) == JAR_MAGIC) {
                    return true;
                }
                i2 += get16(bArr, i2 + 2) + 4;
            } catch (ArrayIndexOutOfBoundsException e2) {
                return false;
            }
        }
        return false;
    }

    private static int get16(byte[] bArr, int i2) {
        return Byte.toUnsignedInt(bArr[i2]) | (Byte.toUnsignedInt(bArr[i2 + 1]) << 8);
    }

    private static void set16(byte[] bArr, int i2, int i3) {
        bArr[i2 + 0] = (byte) i3;
        bArr[i2 + 1] = (byte) (i3 >> 8);
    }
}
