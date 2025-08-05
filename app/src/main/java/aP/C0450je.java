package aP;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/* renamed from: aP.je, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/je.class */
class C0450je extends JTable {

    /* renamed from: a, reason: collision with root package name */
    DefaultTableCellRenderer f3784a;

    /* renamed from: b, reason: collision with root package name */
    Color f3785b;

    /* renamed from: d, reason: collision with root package name */
    private jl f3786d;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ iX f3787c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0450je(iX iXVar, TableModel tableModel) {
        super(tableModel);
        this.f3787c = iXVar;
        this.f3784a = null;
        this.f3785b = new Color(240, 240, 255);
        this.f3786d = null;
        super.setColumnSelectionAllowed(true);
        super.getTableHeader().setReorderingAllowed(false);
        DefaultListSelectionModel defaultListSelectionModel = new DefaultListSelectionModel();
        defaultListSelectionModel.setSelectionMode(2);
        setSelectionModel(defaultListSelectionModel);
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        if (i3 == 0) {
            if (i3 == 0 && i2 % 2 == 0) {
                a().setBackground(this.f3785b);
            } else {
                a().setBackground(Color.WHITE);
            }
            a().setForeground(Color.black);
        }
        return a();
    }

    protected DefaultTableCellRenderer a() {
        if (this.f3784a == null) {
            this.f3784a = (DefaultTableCellRenderer) super.getDefaultRenderer(String.class);
            this.f3784a.setVerticalTextPosition(0);
            this.f3784a.setHorizontalTextPosition(0);
        }
        return this.f3784a;
    }

    @Override // javax.swing.JTable, javax.swing.JComponent
    protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
        if (keyEvent.getID() == 401 && i2 == 0) {
            if (keyEvent.getModifiers() == 1) {
                switch (keyEvent.getKeyCode()) {
                    case 56:
                        this.f3787c.h();
                        return true;
                    case 61:
                        SwingUtilities.invokeLater(new RunnableC0451jf(this));
                        return true;
                }
            }
            switch (keyEvent.getKeyCode()) {
                case 44:
                    this.f3787c.g();
                    return true;
                case 45:
                    SwingUtilities.invokeLater(new RunnableC0452jg(this));
                    return true;
                case 46:
                    this.f3787c.a();
                    return true;
                case 61:
                    this.f3787c.b();
                    return true;
                case 81:
                    this.f3787c.a();
                    return true;
                case 87:
                    this.f3787c.g();
                    return true;
                case 107:
                    this.f3787c.j();
                    return true;
                case 109:
                    SwingUtilities.invokeLater(new RunnableC0453jh(this));
                    return true;
                default:
                    if (keyEvent.getModifiers() == 0 && a(keyEvent.getKeyChar()) && !bH.H.a(keyEvent.getKeyChar() + "") && keyEvent.getKeyCode() != 10 && keyEvent.getKeyCode() != 9 && keyEvent.getKeyCode() != 38 && keyEvent.getKeyCode() != 40 && keyEvent.getKeyCode() != 37 && keyEvent.getKeyCode() != 32 && keyEvent.getKeyCode() != 39) {
                        return true;
                    }
                    break;
            }
        }
        if (keyEvent.getID() == 401 && keyEvent.getKeyCode() == 10 && isEditing()) {
            this.f3787c.f3706p = true;
        }
        return super.processKeyBinding(keyStroke, keyEvent, i2, z2);
    }

    private boolean a(char c2) {
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".indexOf(c2) != -1;
    }

    @Override // javax.swing.JTable
    public TableCellEditor getDefaultEditor(Class cls) {
        if (this.f3786d == null) {
            this.f3786d = new jl(this.f3787c);
            this.f3786d.getComponent().setFont(getFont());
            addFocusListener(new C0454ji(this));
        }
        return this.f3786d;
    }

    @Override // javax.swing.JTable
    public void removeEditor() {
        int editingColumn = super.getEditingColumn();
        int editingRow = super.getEditingRow();
        super.removeEditor();
        if (!this.f3787c.f3706p || editingColumn < 0 || editingRow < 0) {
            return;
        }
        SwingUtilities.invokeLater(new jj(this, editingRow, editingColumn));
        this.f3787c.f3706p = false;
    }

    @Override // javax.swing.JTable
    public boolean editCellAt(int i2, int i3, EventObject eventObject) {
        if ((eventObject instanceof KeyEvent) && ((KeyEvent) eventObject).getKeyCode() == 32) {
            return false;
        }
        boolean zEditCellAt = super.editCellAt(i2, i3, eventObject);
        if (zEditCellAt) {
            this.f3786d.a();
        }
        return zEditCellAt;
    }
}
