package aP;

import G.InterfaceC0131n;

/* renamed from: aP.ao, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ao.class */
public class C0219ao extends Thread {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC0131n f2920a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0207ac f2921b;

    public C0219ao(C0207ac c0207ac, InterfaceC0131n interfaceC0131n) {
        this.f2921b = c0207ac;
        this.f2920a = null;
        this.f2920a = interfaceC0131n;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() throws IllegalArgumentException {
        this.f2921b.o();
    }
}
