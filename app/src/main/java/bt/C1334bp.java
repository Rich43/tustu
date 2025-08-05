package bt;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/* renamed from: bt.bp, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bp.class */
class C1334bp extends FocusAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1333bo f9053a;

    C1334bp(C1333bo c1333bo) {
        this.f9053a = c1333bo;
    }

    @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f9053a.g();
    }
}
