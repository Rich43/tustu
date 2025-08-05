package aJ;

import bH.C0995c;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:aJ/b.class */
public class b {

    /* renamed from: a, reason: collision with root package name */
    List f2528a = new ArrayList();

    public void a(byte[] bArr) {
        if (bArr.length < 512) {
            throw new IndexOutOfBoundsException("Boot Sector should be at least 512 bytes.");
        }
        this.f2528a.clear();
        byte[] bArr2 = new byte[16];
        int i2 = 446;
        do {
            System.arraycopy(bArr, i2, bArr2, 0, bArr2.length);
            if (!C0995c.e(bArr2)) {
                c cVar = new c();
                cVar.a(bArr2);
                this.f2528a.add(cVar);
            }
            i2 += 16;
        } while (i2 < 510);
    }

    public List a() {
        return this.f2528a;
    }
}
