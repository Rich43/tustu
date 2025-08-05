package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* renamed from: com.efiAnalytics.ui.av, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/av.class */
class C1557av implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10852a;

    C1557av(BinTableView binTableView) {
        this.f10852a = binTableView;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
        ((Cdo) focusEvent.getSource()).selectAll();
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
    }
}
