package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: com.efiAnalytics.ui.eb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eb.class */
class C1644eb implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1643ea f11572a;

    C1644eb(C1643ea c1643ea) {
        this.f11572a = c1643ea;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f11572a.f11569a.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
