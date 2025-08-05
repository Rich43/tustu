package com.efiAnalytics.ui;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.table.TableCellEditor;

/* renamed from: com.efiAnalytics.ui.ay, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ay.class */
class C1560ay implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ BinTableView f10855a;

    C1560ay(BinTableView binTableView) {
        this.f10855a = binTableView;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        TableCellEditor cellEditor = this.f10855a.getCellEditor();
        if (cellEditor != null) {
            if (bH.H.a(cellEditor.getCellEditorValue().toString())) {
                cellEditor.stopCellEditing();
            } else {
                cellEditor.cancelCellEditing();
            }
        }
    }
}
