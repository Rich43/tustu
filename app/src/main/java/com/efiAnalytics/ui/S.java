package com.efiAnalytics.ui;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/S.class */
class S extends JTable {

    /* renamed from: a, reason: collision with root package name */
    boolean f10697a;

    /* renamed from: b, reason: collision with root package name */
    String[] f10698b;

    /* renamed from: c, reason: collision with root package name */
    TableCellEditor f10699c;

    /* renamed from: d, reason: collision with root package name */
    boolean f10700d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ C1705w f10701e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public S(C1705w c1705w, String[] strArr, boolean z2) {
        super(z2 ? strArr.length : 1, z2 ? 1 : strArr.length);
        this.f10701e = c1705w;
        this.f10697a = false;
        this.f10700d = false;
        this.f10697a = z2;
        setSelectionMode(1);
        setCellSelectionEnabled(true);
        setFillsViewportHeight(z2);
        C1537ab c1537ab = new C1537ab(this);
        this.f10699c = new C1536aa(this);
        for (int i2 = 0; i2 < getColumnCount(); i2++) {
            TableColumn column = this.columnModel.getColumn(i2);
            column.setCellRenderer(c1537ab);
            column.setCellEditor(this.f10699c);
        }
        a(c1705w.f11762a.i(), c1705w.f11762a.getRowHeight());
        this.f10698b = new String[strArr.length];
        for (int i3 = 0; i3 < strArr.length; i3++) {
            String str = strArr[i3];
            this.f10698b[i3] = str;
            if (str != null) {
                getModel().setValueAt(Double.valueOf(Double.parseDouble(str)), z2 ? i3 : 0, z2 ? 0 : i3);
            }
        }
        addMouseListener(new T(this, c1705w, z2));
        addFocusListener(new W(this, c1705w));
        setTransferHandler(new Y(this, c1705w, z2));
    }

    @Override // javax.swing.JTable
    public boolean editCellAt(int i2, int i3, EventObject eventObject) {
        boolean zEditCellAt = super.editCellAt(i2, i3, eventObject);
        if (zEditCellAt && this.editorComp != null && (this.editorComp instanceof JTextComponent)) {
            JTextComponent jTextComponent = (JTextComponent) this.editorComp;
            if (eventObject == null || (eventObject instanceof KeyEvent)) {
                jTextComponent.selectAll();
            } else if (eventObject instanceof MouseEvent) {
                SwingUtilities.invokeLater(new Z(this, jTextComponent));
            }
        }
        return zEditCellAt;
    }

    public String[] a() {
        String[] strArr = new String[this.f10697a ? getRowCount() : getColumnCount()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = getValueAt(this.f10697a ? i2 : 0, this.f10697a ? 0 : i2).toString();
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2, int i3) {
        if (!this.f10697a) {
            i2 -= 4;
        }
        setRowHeight(i3);
        setMinimumSize(new Dimension(i2, i3));
        for (int i4 = 0; i4 < getColumnCount(); i4++) {
            TableColumn column = this.columnModel.getColumn(i4);
            column.setMinWidth(i2);
            column.setPreferredWidth(i2);
        }
    }
}
