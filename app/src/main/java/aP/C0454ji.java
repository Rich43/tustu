package aP;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.table.TableCellEditor;

/* renamed from: aP.ji, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ji.class */
class C0454ji implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0450je f3791a;

    C0454ji(C0450je c0450je) {
        this.f3791a = c0450je;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        TableCellEditor cellEditor = this.f3791a.getCellEditor();
        if (cellEditor != null) {
            if (bH.H.a(cellEditor.getCellEditorValue().toString())) {
                cellEditor.stopCellEditing();
            } else {
                cellEditor.cancelCellEditing();
            }
        }
    }
}
