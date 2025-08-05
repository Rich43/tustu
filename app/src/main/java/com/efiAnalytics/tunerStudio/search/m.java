package com.efiAnalytics.tunerStudio.search;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/m.class */
class m implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ l f10200a;

    m(l lVar) {
        this.f10200a = lVar;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f10200a.f();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f10200a.e();
        this.f10200a.d();
    }
}
