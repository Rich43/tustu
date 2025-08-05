package com.efiAnalytics.ui;

import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

/* renamed from: com.efiAnalytics.ui.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/h.class */
class C1690h implements PopupMenuListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1642e f11719a;

    C1690h(C1642e c1642e) {
        this.f11719a = c1642e;
    }

    @Override // javax.swing.event.PopupMenuListener
    public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
    }

    @Override // javax.swing.event.PopupMenuListener
    public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
        this.f11719a.a();
    }

    @Override // javax.swing.event.PopupMenuListener
    public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
    }
}
