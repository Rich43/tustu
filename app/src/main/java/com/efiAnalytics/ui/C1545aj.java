package com.efiAnalytics.ui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* renamed from: com.efiAnalytics.ui.aj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aj.class */
class C1545aj implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1705w f10837a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1544ai f10838b;

    C1545aj(C1544ai c1544ai, C1705w c1705w) {
        this.f10838b = c1544ai;
        this.f10837a = c1705w;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        this.f10838b.f10836c.f11763b.repaint();
    }
}
