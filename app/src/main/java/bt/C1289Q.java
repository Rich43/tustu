package bt;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: bt.Q, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/Q.class */
class C1289Q implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    int f8695a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1288P f8696b;

    C1289Q(C1288P c1288p, int i2) {
        this.f8696b = c1288p;
        this.f8695a = -1;
        this.f8695a = i2;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f8696b.c(this.f8695a);
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
