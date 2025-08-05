package com.efiAnalytics.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Platform;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eE.class */
class eE implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ eD f11479a;

    eE(eD eDVar) {
        this.f11479a = eDVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        Platform.runLater(new eF(this));
    }
}
