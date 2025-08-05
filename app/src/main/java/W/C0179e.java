package W;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/* renamed from: W.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/e.class */
public class C0179e extends BufferedOutputStream {

    /* renamed from: a, reason: collision with root package name */
    FileOutputStream f2126a;

    public C0179e(FileOutputStream fileOutputStream) {
        super(fileOutputStream);
        this.f2126a = fileOutputStream;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.f2126a.flush();
        } catch (Exception e2) {
        }
        try {
            this.f2126a.close();
            bH.C.d("FileOutputStream Closed.");
        } catch (Exception e3) {
        }
        super.close();
    }
}
