package com.efiAnalytics.ui;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javafx.application.Platform;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eG.class */
class eG extends ComponentAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ eD f11481a;

    eG(eD eDVar) {
        this.f11481a = eDVar;
    }

    @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
    public void componentResized(ComponentEvent componentEvent) {
        Platform.runLater(new eH(this));
    }
}
