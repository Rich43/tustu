package aP;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/* renamed from: aP.hs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hs.class */
class C0411hs extends WindowAdapter {

    /* renamed from: a, reason: collision with root package name */
    C0406hn f3610a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0406hn f3611b;

    public C0411hs(C0406hn c0406hn, C0406hn c0406hn2) {
        this.f3611b = c0406hn;
        this.f3610a = c0406hn2;
    }

    @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
    public void windowClosing(WindowEvent windowEvent) {
        this.f3610a.g();
    }
}
