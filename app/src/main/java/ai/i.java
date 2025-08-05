package aI;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/i.class */
class i extends Thread implements h {

    /* renamed from: a, reason: collision with root package name */
    OutputStream f2458a;

    /* renamed from: b, reason: collision with root package name */
    int f2459b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ e f2460c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    i(e eVar, File file) {
        super("UncompessedSdWriter");
        this.f2460c = eVar;
        this.f2458a = null;
        this.f2459b = 0;
        setDaemon(true);
        this.f2458a = eVar.a(file);
    }

    @Override // aI.h
    public void a(byte[] bArr) throws IOException {
        this.f2459b += bArr.length;
        this.f2458a.write(bArr);
    }

    @Override // java.lang.Thread
    public void start() {
    }

    @Override // aI.h
    public void a() {
        try {
            this.f2458a.close();
        } catch (IOException e2) {
            Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // aI.h
    public int b() {
        return this.f2459b;
    }
}
