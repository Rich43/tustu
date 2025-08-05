package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* renamed from: bt.bk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bk.class */
class C1329bk extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1328bj f9029a;

    C1329bk(C1328bj c1328bj) {
        this.f9029a = c1328bj;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f9029a.e(this.f9029a.f9020a.b());
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        if (this.f9029a.f9020a != null) {
            this.f9029a.d(this.f9029a.f9020a.b());
        }
    }
}
