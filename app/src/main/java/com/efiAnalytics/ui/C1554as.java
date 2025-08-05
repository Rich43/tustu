package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.as, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/as.class */
class C1554as implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10849a;

    C1554as(BinTableView binTableView) {
        this.f10849a = binTableView;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10849a.a(actionEvent.getActionCommand());
    }
}
