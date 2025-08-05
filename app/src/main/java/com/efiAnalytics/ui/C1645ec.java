package com.efiAnalytics.ui;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/* renamed from: com.efiAnalytics.ui.ec, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ec.class */
public class C1645ec extends AbstractTableModel {

    /* renamed from: b, reason: collision with root package name */
    private TableModel f11573b;

    /* renamed from: c, reason: collision with root package name */
    private double f11574c = 1.0d;

    /* renamed from: d, reason: collision with root package name */
    private double f11575d = 0.0d;

    /* renamed from: a, reason: collision with root package name */
    C1646ed f11576a = new C1646ed(this);

    @Override // javax.swing.table.TableModel
    public int getRowCount() {
        return this.f11573b.getRowCount();
    }

    @Override // javax.swing.table.TableModel
    public int getColumnCount() {
        if (this.f11573b != null) {
            return this.f11573b.getColumnCount();
        }
        return 1;
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public String getColumnName(int i2) {
        return this.f11573b != null ? this.f11573b.getColumnName(i2) : "";
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public Class getColumnClass(int i2) {
        return this.f11573b != null ? this.f11573b.getColumnClass(i2) : Object.class;
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public boolean isCellEditable(int i2, int i3) {
        if (this.f11573b != null) {
            return this.f11573b.isCellEditable(i2, i3);
        }
        return false;
    }

    @Override // javax.swing.table.TableModel
    public Object getValueAt(int i2, int i3) {
        double dDoubleValue;
        if (this.f11573b == null) {
            return Double.valueOf(Double.NaN);
        }
        Object valueAt = this.f11573b.getValueAt(i2, i3);
        try {
            dDoubleValue = valueAt instanceof Double ? ((Double) valueAt).doubleValue() : Double.parseDouble(valueAt.toString());
        } catch (NumberFormatException e2) {
            dDoubleValue = Double.NaN;
        }
        return new Double((dDoubleValue + this.f11575d) * this.f11574c);
    }

    @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
    public void setValueAt(Object obj, int i2, int i3) {
        if (this.f11573b == null) {
            return;
        }
        try {
            double dDoubleValue = ((obj instanceof Double ? ((Double) obj).doubleValue() : Double.parseDouble(obj.toString())) / this.f11574c) - this.f11575d;
        } catch (NumberFormatException e2) {
        }
    }

    public void a(TableModel tableModel) {
        if (tableModel != null) {
            tableModel.removeTableModelListener(this.f11576a);
        }
        this.f11573b = tableModel;
        tableModel.addTableModelListener(this.f11576a);
    }

    public void a(double d2) {
        this.f11574c = d2;
    }

    public void b(double d2) {
        this.f11575d = d2;
    }

    public void a() {
        super.fireTableDataChanged();
    }
}
