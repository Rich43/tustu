package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aS.class */
class aS extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f10378a;

    aS(aQ aQVar) {
        this.f10378a = aQVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        try {
            this.f10378a.f10375a.setText(((Integer.parseInt(this.f10378a.f10376b.getText() + keyEvent.getKeyChar()) * 22) / 10) + "");
        } catch (NumberFormatException e2) {
            keyEvent.consume();
        }
    }
}
