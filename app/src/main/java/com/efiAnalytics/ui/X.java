package com.efiAnalytics.ui;

import java.awt.Component;
import java.awt.event.FocusEvent;
import javax.swing.table.TableCellEditor;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/X.class */
class X implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ FocusEvent f10711a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ W f10712b;

    X(W w2, FocusEvent focusEvent) {
        this.f10712b = w2;
        this.f10711a = focusEvent;
    }

    @Override // java.lang.Runnable
    public void run() {
        TableCellEditor cellEditor = this.f10712b.f10710b.getCellEditor();
        if (cellEditor != null) {
            cellEditor.stopCellEditing();
        }
        Component oppositeComponent = this.f10711a.getOppositeComponent();
        if (this.f10712b.f10710b.f10700d) {
            this.f10712b.f10710b.f10700d = false;
        } else {
            if (this.f10712b.f10710b.equals(oppositeComponent)) {
                return;
            }
            this.f10712b.f10710b.clearSelection();
        }
    }
}
