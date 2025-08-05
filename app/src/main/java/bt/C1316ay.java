package bt;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: bt.ay, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/ay.class */
class C1316ay implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1303al f8899a;

    C1316ay(C1303al c1303al) {
        this.f8899a = c1303al;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8899a.e(((G.aM) this.f8899a.f8845b.get(0)).aJ());
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f8899a.d(((G.aM) this.f8899a.f8845b.get(0)).aJ());
    }
}
