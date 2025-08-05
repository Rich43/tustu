package com.efiAnalytics.ui;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aH.class */
class aH implements ListSelectionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10739a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ aG f10740b;

    aH(aG aGVar, BinTableView binTableView) {
        this.f10740b = aGVar;
        this.f10739a = binTableView;
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        this.f10740b.f10738e.getTableHeader().repaint();
    }
}
