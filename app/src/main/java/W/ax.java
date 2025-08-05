package W;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:W/ax.class */
public class ax extends FileInputStream {
    public ax(File file) {
        super(file);
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read(byte[] bArr) {
        int i2 = read(bArr);
        a(bArr);
        return i2;
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read(byte[] bArr, int i2, int i3) throws IOException {
        int i4 = super.read(bArr, i2, i3);
        a(bArr);
        return i4;
    }

    @Override // java.io.FileInputStream, java.io.InputStream
    public int read() throws IOException {
        int i2 = super.read();
        az.a(i2);
        return i2;
    }

    private byte[] a(byte[] bArr) {
        return az.a(bArr);
    }
}
