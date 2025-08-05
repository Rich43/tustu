package com.efiAnalytics.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* renamed from: com.efiAnalytics.ui.aw, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aw.class */
class C1558aw implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10853a;

    C1558aw(BinTableView binTableView) {
        this.f10853a = binTableView;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        this.f10853a.ag();
    }
}
