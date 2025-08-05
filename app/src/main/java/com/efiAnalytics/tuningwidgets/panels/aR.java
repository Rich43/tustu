package com.efiAnalytics.tuningwidgets.panels;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aR.class */
class aR extends KeyAdapter {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f10377a;

    aR(aQ aQVar) {
        this.f10377a = aQVar;
    }

    @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        try {
            this.f10377a.f10376b.setText(((Integer.parseInt(this.f10377a.f10375a.getText() + keyEvent.getKeyChar()) * 10) / 22) + "");
        } catch (NumberFormatException e2) {
            keyEvent.consume();
        }
    }
}
