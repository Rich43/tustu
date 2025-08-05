package com.efiAnalytics.ui;

import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.plaf.UIResource;

/* renamed from: com.efiAnalytics.ui.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ac.class */
class C1538ac extends TransferHandler implements UIResource {

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ S f10823d;

    C1538ac(S s2) {
        this.f10823d = s2;
    }

    @Override // javax.swing.TransferHandler
    protected Transferable createTransferable(JComponent jComponent) {
        int[] selectedRows;
        int[] selectedColumns;
        if (!(jComponent instanceof JTable)) {
            return null;
        }
        JTable jTable = (JTable) jComponent;
        if (!jTable.getRowSelectionAllowed() && !jTable.getColumnSelectionAllowed()) {
            return null;
        }
        if (jTable.getRowSelectionAllowed()) {
            selectedRows = jTable.getSelectedRows();
        } else {
            int rowCount = jTable.getRowCount();
            selectedRows = new int[rowCount];
            for (int i2 = 0; i2 < rowCount; i2++) {
                selectedRows[i2] = i2;
            }
        }
        if (jTable.getColumnSelectionAllowed()) {
            selectedColumns = jTable.getSelectedColumns();
        } else {
            int columnCount = jTable.getColumnCount();
            selectedColumns = new int[columnCount];
            for (int i3 = 0; i3 < columnCount; i3++) {
                selectedColumns[i3] = i3;
            }
        }
        if (selectedRows == null || selectedColumns == null || selectedRows.length == 0 || selectedColumns.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i4 : selectedRows) {
            for (int i5 : selectedColumns) {
                Object valueAt = jTable.getValueAt(i4, i5);
                sb.append(valueAt == null ? "" : valueAt.toString()).append("\t");
            }
            sb.deleteCharAt(sb.length() - 1).append("\n");
        }
        sb.deleteCharAt(sb.length() - 1);
        return new C1539ad(this, sb);
    }

    @Override // javax.swing.TransferHandler
    public int getSourceActions(JComponent jComponent) {
        return 1;
    }
}
