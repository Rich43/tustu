package com.efiAnalytics.ui;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;

/* renamed from: com.efiAnalytics.ui.aa, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/aa.class */
class C1536aa extends DefaultCellEditor {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ S f10821a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1536aa(S s2) {
        super(new Cdo());
        this.f10821a = s2;
        ((Cdo) getComponent()).setHorizontalAlignment(0);
    }

    @Override // javax.swing.DefaultCellEditor, javax.swing.table.TableCellEditor
    public Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3) {
        Cdo cdo = (Cdo) getComponent();
        cdo.setFont(this.f10821a.getFont());
        cdo.setText(jTable.getModel().getValueAt(i2, i3).toString());
        return cdo;
    }
}
