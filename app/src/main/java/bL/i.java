package bL;

import bH.C;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;

/* loaded from: TunerStudioMS.jar:bL/i.class */
public class i {

    /* renamed from: a, reason: collision with root package name */
    j[] f7146a = null;

    public i(C1701s c1701s, int i2) {
        a(c1701s, i2);
    }

    public void a(C1701s c1701s, int i2) {
        this.f7146a = new j[i2];
        for (int i3 = 0; i3 < this.f7146a.length; i3++) {
            this.f7146a[i3] = new j(this);
            this.f7146a[i3].a(c1701s.getRowCount(), c1701s.getColumnCount());
            this.f7146a[i3] = (j) C1677fh.a(c1701s, this.f7146a[i3]);
            this.f7146a[i3].a(System.currentTimeMillis());
        }
    }

    public C1701s a(long j2, int i2) {
        j jVar = (j) a(j2);
        if (Math.abs(j2 - jVar.c()) > i2) {
            return null;
        }
        return jVar;
    }

    public C1701s a(long j2) {
        long jC = Long.MAX_VALUE;
        for (int i2 = 0; i2 < this.f7146a.length; i2++) {
            if (this.f7146a[i2].c() < j2) {
                return Math.abs(this.f7146a[i2].c() - j2) > Math.abs(jC - j2) ? this.f7146a[i2 - 1] : this.f7146a[i2];
            }
            jC = this.f7146a[i2].c();
        }
        C.c("timestamp is older than we have returning the oldest table I have. timestamp=" + j2 + ", lastTimestamp=" + jC);
        return this.f7146a[this.f7146a.length - 1];
    }

    public void a(C1701s c1701s, long j2) {
        j jVar = this.f7146a[this.f7146a.length - 1];
        for (int length = this.f7146a.length - 1; length > 0; length--) {
            this.f7146a[length] = this.f7146a[length - 1];
        }
        this.f7146a[0] = (j) C1677fh.a(c1701s, jVar);
        this.f7146a[0].a(j2);
    }

    public int a() {
        return this.f7146a.length;
    }
}
