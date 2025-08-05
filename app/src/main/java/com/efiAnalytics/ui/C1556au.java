package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* renamed from: com.efiAnalytics.ui.au, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/au.class */
class C1556au implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10851a;

    C1556au(BinTableView binTableView) {
        this.f10851a = binTableView;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        this.f10851a.a(actionEvent.getActionCommand());
    }
}
