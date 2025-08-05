package W;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:W/ay.class */
public class ay extends FileOutputStream {
    public ay(File file) {
        super(file);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(int i2) throws IOException {
        super.write(az.a(i2));
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        az.a(bArr);
        super.write(bArr);
    }

    @Override // java.io.FileOutputStream, java.io.OutputStream
    public void write(byte[] bArr, int i2, int i3) throws IOException {
        az.a(bArr);
        super.write(bArr, i2, i3);
    }
}
