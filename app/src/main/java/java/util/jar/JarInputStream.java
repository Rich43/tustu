package java.util.jar;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import sun.misc.JarIndex;
import sun.security.util.ManifestEntryVerifier;

/* loaded from: rt.jar:java/util/jar/JarInputStream.class */
public class JarInputStream extends ZipInputStream {
    private Manifest man;
    private JarEntry first;
    private JarVerifier jv;
    private ManifestEntryVerifier mev;
    private final boolean doVerify;
    private boolean tryManifest;

    public JarInputStream(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    public JarInputStream(InputStream inputStream, boolean z2) throws IOException {
        super(inputStream);
        this.doVerify = z2;
        JarEntry jarEntry = (JarEntry) super.getNextEntry();
        if (jarEntry != null && jarEntry.getName().equalsIgnoreCase("META-INF/")) {
            jarEntry = (JarEntry) super.getNextEntry();
        }
        this.first = checkManifest(jarEntry);
    }

    private JarEntry checkManifest(JarEntry jarEntry) throws IOException {
        if (jarEntry != null && JarFile.MANIFEST_NAME.equalsIgnoreCase(jarEntry.getName())) {
            this.man = new Manifest();
            byte[] bytes = getBytes(new BufferedInputStream(this));
            this.man.read(new ByteArrayInputStream(bytes));
            closeEntry();
            if (this.doVerify) {
                this.jv = new JarVerifier(jarEntry.getName(), bytes);
                this.mev = new ManifestEntryVerifier(this.man, this.jv.manifestName);
            }
            return (JarEntry) super.getNextEntry();
        }
        return jarEntry;
    }

    private byte[] getBytes(InputStream inputStream) throws IOException {
        byte[] bArr = new byte[8192];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(2048);
        while (true) {
            int i2 = inputStream.read(bArr, 0, bArr.length);
            if (i2 != -1) {
                byteArrayOutputStream.write(bArr, 0, i2);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    public Manifest getManifest() {
        return this.man;
    }

    @Override // java.util.zip.ZipInputStream
    public ZipEntry getNextEntry() throws IOException {
        JarEntry jarEntryCheckManifest;
        if (this.first == null) {
            jarEntryCheckManifest = (JarEntry) super.getNextEntry();
            if (this.tryManifest) {
                jarEntryCheckManifest = checkManifest(jarEntryCheckManifest);
                this.tryManifest = false;
            }
        } else {
            jarEntryCheckManifest = this.first;
            if (this.first.getName().equalsIgnoreCase(JarIndex.INDEX_NAME)) {
                this.tryManifest = true;
            }
            this.first = null;
        }
        if (this.jv != null && jarEntryCheckManifest != null) {
            if (this.jv.nothingToVerify()) {
                this.jv = null;
                this.mev = null;
            } else {
                this.jv.beginEntry(jarEntryCheckManifest, this.mev);
            }
        }
        return jarEntryCheckManifest;
    }

    public JarEntry getNextJarEntry() throws IOException {
        return (JarEntry) getNextEntry();
    }

    @Override // java.util.zip.ZipInputStream, java.util.zip.InflaterInputStream, java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4;
        if (this.first == null) {
            i4 = super.read(bArr, i2, i3);
        } else {
            i4 = -1;
        }
        if (this.jv != null) {
            this.jv.update(i4, bArr, i2, i3, this.mev);
        }
        return i4;
    }

    @Override // java.util.zip.ZipInputStream
    protected ZipEntry createZipEntry(String str) {
        JarEntry jarEntry = new JarEntry(str);
        if (this.man != null) {
            jarEntry.attr = this.man.getAttributes(str);
        }
        return jarEntry;
    }
}
