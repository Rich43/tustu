package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: com.efiAnalytics.ui.fr, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fr.class */
class C1687fr implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1686fq f11698a;

    C1687fr(C1686fq c1686fq) {
        this.f11698a = c1686fq;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        this.f11698a.f11693b.selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
