package bF;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.table.TableCellEditor;

/* loaded from: TunerStudioMS.jar:bF/H.class */
class H implements FocusListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ D f6816a;

    H(D d2) {
        this.f6816a = d2;
    }

    @Override // java.awt.event.FocusListener
    public void focusGained(FocusEvent focusEvent) {
    }

    @Override // java.awt.event.FocusListener
    public void focusLost(FocusEvent focusEvent) {
        TableCellEditor cellEditor = this.f6816a.getCellEditor();
        if (cellEditor != null) {
            if (bH.H.a(cellEditor.getCellEditorValue().toString())) {
                cellEditor.stopCellEditing();
            } else {
                cellEditor.cancelCellEditing();
            }
        }
    }
}
