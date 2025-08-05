package com.efiAnalytics.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aW.class */
class aW extends MouseAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aU f10816a;

    aW(aU aUVar) {
        this.f10816a = aUVar;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        this.f10816a.a(this.f10816a.g(mouseEvent.getX()), this.f10816a.h(mouseEvent.getY()));
    }
}
