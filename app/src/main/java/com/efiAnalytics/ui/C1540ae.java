package com.efiAnalytics.ui;

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/* renamed from: com.efiAnalytics.ui.ae, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ae.class */
class C1540ae extends JTable {

    /* renamed from: a, reason: collision with root package name */
    TableCellRenderer f10826a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1705w f10827b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C1540ae(C1705w c1705w, Object[][] objArr, Object[] objArr2) {
        super(objArr, objArr2);
        this.f10827b = c1705w;
        this.f10826a = null;
        setSelectionModel(new C1541af(this, c1705w));
    }

    public void a(TableCellRenderer tableCellRenderer) {
        this.f10826a = tableCellRenderer;
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        if (this.f10826a == null) {
            this.f10826a = new C1544ai(this.f10827b);
        }
        return this.f10826a;
    }

    public void a(int i2) {
        setPreferredSize(new Dimension(i2, 0));
    }

    @Override // javax.swing.JTable
    public TableCellEditor getCellEditor(int i2, int i3) {
        TableCellEditor cellEditor = super.getCellEditor(i2, i3);
        if (cellEditor != null) {
            cellEditor.cancelCellEditing();
        }
        return cellEditor;
    }

    @Override // javax.swing.JTable
    public boolean isCellEditable(int i2, int i3) {
        return false;
    }
}
