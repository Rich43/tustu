package com.efiAnalytics.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aC.class */
class aC implements KeyListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10719a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aB f10720b;

    aC(aB aBVar, BinTableView binTableView) {
        this.f10720b = aBVar;
        this.f10719a = binTableView;
    }

    @Override // java.awt.event.KeyListener
    public void keyTyped(KeyEvent keyEvent) {
        a(keyEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyPressed(KeyEvent keyEvent) {
        a(keyEvent);
    }

    @Override // java.awt.event.KeyListener
    public void keyReleased(KeyEvent keyEvent) {
        a(keyEvent);
    }

    private void a(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 27) {
            this.f10720b.cancelCellEditing();
            keyEvent.consume();
        }
    }
}
