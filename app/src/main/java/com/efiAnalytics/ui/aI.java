package com.efiAnalytics.ui;

import javax.swing.InputVerifier;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aI.class */
class aI extends InputVerifier {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10741a;

    aI(BinTableView binTableView) {
        this.f10741a = binTableView;
    }

    @Override // javax.swing.InputVerifier
    public boolean verify(JComponent jComponent) {
        return true;
    }
}
