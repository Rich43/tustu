package com.efiAnalytics.ui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eV.class */
public class eV extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

    /* renamed from: a, reason: collision with root package name */
    private int f11562a;

    /* renamed from: c, reason: collision with root package name */
    private int f11563c;

    /* renamed from: d, reason: collision with root package name */
    private final JButton f11565d = new JButton();

    /* renamed from: b, reason: collision with root package name */
    List f11564b = new ArrayList();

    public eV() {
        this.f11565d.addActionListener(new eW(this));
    }

    public void a(eX eXVar) {
        this.f11564b.add(eXVar);
    }

    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        this.f11562a = i2;
        this.f11563c = i3;
        return c();
    }

    public Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3) {
        this.f11562a = i2;
        this.f11563c = i3;
        return c();
    }

    public String a() {
        return this.f11565d.getActionCommand();
    }

    public void a(String str) {
        this.f11565d.setActionCommand(str);
    }

    public String b() {
        return this.f11565d.getText();
    }

    public void b(String str) {
        this.f11565d.setText(str);
    }

    public void a(boolean z2) {
        this.f11565d.setEnabled(z2);
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public void addCellEditorListener(CellEditorListener cellEditorListener) {
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    @Override // javax.swing.CellEditor
    public Object getCellEditorValue() {
        return c().getText();
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean isCellEditable(EventObject eventObject) {
        return true;
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public void removeCellEditorListener(CellEditorListener cellEditorListener) {
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean shouldSelectCell(EventObject eventObject) {
        return true;
    }

    @Override // javax.swing.AbstractCellEditor, javax.swing.CellEditor
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    public JButton c() {
        return this.f11565d;
    }
}
