package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: com.efiAnalytics.ui.bk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bk.class */
class C1573bk implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1571bi f11006a;

    C1573bk(C1571bi c1571bi) {
        this.f11006a = c1571bi;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f11006a.b();
    }
}
