package bD;

import com.efiAnalytics.ui.eJ;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/* renamed from: bD.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bD/c.class */
class C0957c extends JTable {

    /* renamed from: a, reason: collision with root package name */
    C0960f f6659a;

    C0957c(F f2) {
        super(f2);
        this.f6659a = new C0960f(this);
        super.setAutoResizeMode(1);
        TableColumn column = getColumnModel().getColumn(F.f6614a);
        column.setPreferredWidth(eJ.a(23));
        column.setMaxWidth(eJ.a(23));
        getColumnModel().getColumn(F.f6615b).setPreferredWidth(eJ.a(250));
        getColumnModel().getColumn(F.f6616c).setPreferredWidth(eJ.a(300));
        getColumnModel().getColumn(F.f6617d).setPreferredWidth(eJ.a(50));
        getColumnModel().getColumn(F.f6618e).setPreferredWidth(eJ.a(100));
        super.setRowHeight(getFont().getSize() + eJ.a(4));
        super.setShowGrid(false);
        TableRowSorter tableRowSorter = new TableRowSorter(f2);
        setRowSorter(tableRowSorter);
        ArrayList arrayList = new ArrayList();
        tableRowSorter.setSortable(F.f6614a, false);
        arrayList.add(new RowSorter.SortKey(F.f6615b, SortOrder.ASCENDING));
        tableRowSorter.setComparator(F.f6616c, new C0958d(this));
        arrayList.add(new RowSorter.SortKey(F.f6617d, SortOrder.ASCENDING));
        tableRowSorter.setComparator(F.f6618e, new C0959e(this));
        tableRowSorter.setSortKeys(arrayList);
        tableRowSorter.sort();
    }

    public void a(boolean z2) {
        if (z2) {
            TableColumn column = getColumnModel().getColumn(F.f6616c);
            column.setMinWidth(0);
            column.setMaxWidth(0);
            column.setPreferredWidth(0);
            column.setWidth(0);
            return;
        }
        TableColumn column2 = getColumnModel().getColumn(F.f6616c);
        int iA = eJ.a(300);
        column2.setPreferredWidth(iA);
        column2.setWidth(iA);
        column2.setMaxWidth(iA * 2);
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        return this.f6659a;
    }
}
