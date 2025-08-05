package G;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* renamed from: G.cu, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/cu.class */
class C0115cu extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private final HashMap f1159b;

    /* renamed from: c, reason: collision with root package name */
    private final Map f1160c;

    /* renamed from: d, reason: collision with root package name */
    private final Map f1161d;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0113cs f1162a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    private C0115cu(C0113cs c0113cs) {
        super("OutputChannel Publisher");
        this.f1162a = c0113cs;
        this.f1159b = new HashMap();
        this.f1160c = Collections.synchronizedMap(new HashMap());
        this.f1161d = new HashMap();
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (true) {
            try {
                for (String str : (String[]) this.f1160c.keySet().toArray(new String[this.f1160c.keySet().size()])) {
                    byte[] bArr = (byte[]) this.f1160c.get(str);
                    this.f1160c.remove(str);
                    this.f1162a.b(str, bArr);
                }
                a();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                wait();
            } catch (InterruptedException e3) {
                e3.printStackTrace();
            }
        }
    }

    private byte[] a(String str, int i2) {
        byte[] bArr = (byte[]) this.f1159b.get(str);
        if (bArr == null || bArr.length != i2) {
            bArr = new byte[i2];
            this.f1159b.put(str, bArr);
        }
        return bArr;
    }

    public synchronized void a(String str, byte[] bArr) {
        byte[] bArrA = a(str, bArr.length);
        System.arraycopy(bArr, 0, bArrA, 0, bArr.length);
        this.f1160c.put(str, bArrA);
        notify();
    }

    private void a() {
        synchronized (this.f1161d) {
            for (String str : this.f1161d.keySet()) {
                this.f1162a.b(str, ((Double) this.f1161d.get(str)).doubleValue());
            }
            this.f1161d.clear();
        }
    }

    public void a(String str, double d2) {
        synchronized (this.f1161d) {
            this.f1161d.put(str, Double.valueOf(d2));
        }
        if (T.a().c() == null || !T.a().c().R()) {
            a();
        }
    }
}
