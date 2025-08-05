package com.efiAnalytics.apps.ts.tuningViews.tuneComps;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/tuneComps/b.class */
class b extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BurnButtonTv f9850a;

    b(BurnButtonTv burnButtonTv) {
        this.f9850a = burnButtonTv;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        this.f9850a.f9818i = true;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseReleased(MouseEvent mouseEvent) {
        this.f9850a.f9818i = false;
    }
}
