package ao;

import java.awt.Component;
import javax.swing.SwingUtilities;

/* renamed from: ao.F, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/F.class */
public class C0588F extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Component f5079a;

    /* renamed from: b, reason: collision with root package name */
    long f5080b;

    /* renamed from: c, reason: collision with root package name */
    boolean f5081c = true;

    /* renamed from: d, reason: collision with root package name */
    boolean f5082d = false;

    public C0588F(Component component, long j2) {
        this.f5079a = null;
        this.f5080b = 0L;
        this.f5080b = j2;
        this.f5079a = component;
        start();
    }

    public void a() {
        this.f5081c = true;
    }

    public boolean b() {
        return this.f5082d;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f5081c) {
            this.f5081c = false;
            try {
                Thread.currentThread();
                Thread.sleep(this.f5080b);
            } catch (Exception e2) {
            }
            if (!this.f5081c) {
                SwingUtilities.invokeLater(new RunnableC0589G(this));
            }
        }
    }
}
