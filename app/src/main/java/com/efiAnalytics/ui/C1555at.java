package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.at, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/at.class */
class C1555at implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10850a;

    C1555at(BinTableView binTableView) {
        this.f10850a = binTableView;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10850a.f10662aa = !this.f10850a.f10662aa;
    }
}
