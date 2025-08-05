package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aF.class */
class aF implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10732a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aD f10733b;

    aF(aD aDVar, BinTableView binTableView) {
        this.f10733b = aDVar;
        this.f10732a = binTableView;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        this.f10733b.f10729i.O();
    }
}
