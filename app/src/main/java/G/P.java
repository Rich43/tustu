package G;

import bH.C0995c;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/P.class */
class P extends Thread {

    /* renamed from: a, reason: collision with root package name */
    ConcurrentLinkedQueue f467a = new ConcurrentLinkedQueue();

    /* renamed from: b, reason: collision with root package name */
    boolean f468b = true;

    /* renamed from: c, reason: collision with root package name */
    int f469c = 0;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ J f470d;

    P(J j2) {
        this.f470d = j2;
        super.setName("CommDebugLogWriter");
        setDaemon(true);
        super.setPriority(1);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (true) {
            Iterator it = this.f467a.iterator();
            while (it.hasNext()) {
                O o2 = (O) it.next();
                this.f467a.remove(o2);
                if (o2.a() != null) {
                    a(o2.b(), o2.c(), o2.a());
                } else if (o2.d() != null) {
                    a(o2.b(), o2.c(), o2.a());
                } else {
                    a(o2.b(), o2.c());
                }
            }
            this.f469c++;
            notify();
            try {
                wait(500L);
            } catch (InterruptedException e2) {
                Logger.getLogger(J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public synchronized void a(O o2) {
        this.f467a.add(o2);
        notify();
    }

    void a(String str, String str2) {
        if (J.I()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Time: ").append(str2).append(": ").append(str);
            System.out.println(sb);
        }
    }

    void a(String str, String str2, byte[] bArr) {
        if (this.f470d.f419D > 0 || J.I()) {
            StringBuilder sb = new StringBuilder();
            if (bArr != null) {
                sb.append("Time: ").append(str2).append(": ").append(str).append(", ").append(bArr.length).append(" bytes");
            } else {
                sb.append("Time: ").append(str2).append(": ").append(str);
            }
            if (bArr != null && this.f468b) {
                sb.append("\n").append(C0995c.a(bArr, 16)).append("\n");
            } else if (bArr == null) {
                sb.append("\n").append("No bytes!\n");
            }
            if (J.I()) {
                bH.C.e(sb.toString());
            } else {
                this.f470d.m(sb.toString());
            }
        }
    }
}
