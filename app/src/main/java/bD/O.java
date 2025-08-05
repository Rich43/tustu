package bD;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:bD/O.class */
class O implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ I f6648a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ N f6649b;

    O(N n2, I i2) {
        this.f6649b = n2;
        this.f6648a = i2;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f6649b.f6644b.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
