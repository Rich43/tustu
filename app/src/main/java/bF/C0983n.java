package bF;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/* renamed from: bF.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/n.class */
class C0983n extends JTable {

    /* renamed from: a, reason: collision with root package name */
    C0985p f6873a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0973d f6874b;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0983n(C0973d c0973d, Object[][] objArr, String[] strArr) {
        super(objArr, strArr);
        this.f6874b = c0973d;
        this.f6873a = null;
        setSelectionModel(new C0984o(this, c0973d));
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        if (this.f6873a == null) {
            this.f6873a = new C0985p(this.f6874b);
        }
        if (this.f6874b.f6846a.isEnabled()) {
            this.f6873a.setForeground(UIManager.getColor("Label.foreground"));
        } else {
            this.f6873a.setForeground(Color.GRAY);
        }
        return this.f6873a;
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
