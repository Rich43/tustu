package com.efiAnalytics.tunerStudio.search;

import G.C0129l;
import G.R;
import G.S;
import G.T;
import aP.C0338f;
import bH.W;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import com.efiAnalytics.ui.eX;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/search/q.class */
public class q extends JTable implements S, eX {

    /* renamed from: a, reason: collision with root package name */
    public static int f10208a = 4000;

    /* renamed from: j, reason: collision with root package name */
    private static final String f10215j = C1818g.b("Disabled Device");

    /* renamed from: k, reason: collision with root package name */
    private static final String f10216k = C1818g.b("Activate Device");

    /* renamed from: l, reason: collision with root package name */
    private static final String f10217l = C1818g.b("Connect");

    /* renamed from: m, reason: collision with root package name */
    private static final String f10218m = C1818g.b("Disconnect");

    /* renamed from: n, reason: collision with root package name */
    private static final String f10219n = C1818g.b("Checking Status");

    /* renamed from: o, reason: collision with root package name */
    private static final String f10220o = C1818g.b("Error");

    /* renamed from: p, reason: collision with root package name */
    private static final String f10221p = C1818g.b("Work offline");

    /* renamed from: b, reason: collision with root package name */
    List f10209b = new CopyOnWriteArrayList();

    /* renamed from: c, reason: collision with root package name */
    z f10210c = new z(this);

    /* renamed from: e, reason: collision with root package name */
    v f10212e = new v(this);

    /* renamed from: f, reason: collision with root package name */
    v f10213f = new v(this);

    /* renamed from: i, reason: collision with root package name */
    private int f10214i = eJ.a(50);

    /* renamed from: g, reason: collision with root package name */
    w f10222g = new w(this);

    /* renamed from: h, reason: collision with root package name */
    int f10223h = 0;

    /* renamed from: d, reason: collision with root package name */
    q f10211d = this;

    public q() {
        setModel(this.f10210c);
        setSelectionMode(0);
        setRowSelectionAllowed(true);
        getColumnModel().getColumn(0).setMinWidth(eJ.a(50) + getFont().getSize());
        getColumnModel().getColumn(0).setMaxWidth(eJ.a(50) + getFont().getSize());
        getColumnModel().getColumn(2).setMinWidth(eJ.a(150));
        getColumnModel().getColumn(2).setMaxWidth(eJ.a(150));
        setRowHeight(getFont().getSize() + this.f10214i);
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(0);
        getColumnModel().getColumn(1).setCellRenderer(defaultTableCellRenderer);
        this.f10212e.a(this);
        getColumnModel().getColumn(2).setCellRenderer(this.f10213f);
        getColumnModel().getColumn(2).setCellEditor(this.f10212e);
        T.a().a(this);
        putClientProperty("terminateEditOnFocusLost", true);
        addMouseListener(new r(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JPopupMenu j() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JMenuItem jMenuItem = new JMenuItem(C1818g.b("Transfer / Edit Device Activation"));
        jPopupMenu.add(jMenuItem);
        jMenuItem.addActionListener(new s(this));
        return jPopupMenu;
    }

    @Override // javax.swing.JTable
    public TableCellRenderer getCellRenderer(int i2, int i3) {
        TableCellRenderer cellRenderer = super.getCellRenderer(i2, i3);
        if (i3 == 0 && this.f10209b.size() > i2) {
            f fVar = (f) this.f10209b.get(i2);
            Component tableCellRendererComponent = cellRenderer.getTableCellRendererComponent(this, this.f10210c.getValueAt(i2, i3), getSelectedRow() == i2, hasFocus(), i2, i3);
            if (System.currentTimeMillis() - fVar.e() > f10208a) {
                tableCellRendererComponent.setEnabled(false);
            } else {
                tableCellRendererComponent.setEnabled(true);
            }
        }
        return cellRenderer;
    }

    @Override // javax.swing.JTable
    public TableCellEditor getCellEditor(int i2, int i3) {
        TableCellEditor cellEditor = super.getCellEditor(i2, i3);
        if (i3 == 2) {
            cellEditor.getTableCellEditorComponent(this, this.f10210c.getValueAt(i2, i3), getSelectedRow() == i2, i2, i3);
        }
        return cellEditor;
    }

    public void a(f fVar) {
        if (f(fVar)) {
            return;
        }
        int minSelectionIndex = getSelectionModel().getMinSelectionIndex();
        this.f10209b.add(fVar);
        this.f10210c.fireTableDataChanged();
        if (minSelectionIndex >= 0) {
            getSelectionModel().setSelectionInterval(minSelectionIndex, minSelectionIndex);
        }
    }

    private boolean f(f fVar) {
        try {
            Iterator it = this.f10209b.iterator();
            while (it.hasNext()) {
                if (((f) it.next()).a().equals(fVar.a())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            bH.C.a("problem comparing devices. DiscoveredDevice: " + ((Object) fVar));
            return false;
        }
    }

    public void b(f fVar) {
        int minSelectionIndex = getSelectionModel().getMinSelectionIndex();
        if (!g(fVar)) {
            this.f10209b.add(fVar);
            this.f10210c.fireTableDataChanged();
        }
        if (minSelectionIndex >= 0) {
            getSelectionModel().setSelectionInterval(minSelectionIndex, minSelectionIndex);
        }
    }

    private boolean g(f fVar) {
        boolean z2 = false;
        for (int i2 = 0; i2 < this.f10209b.size(); i2++) {
            f fVar2 = (f) this.f10209b.get(i2);
            if ((fVar.f().k().equals(fVar2.f().k()) && W.c(fVar.a()) && W.c(fVar2.a())) || (fVar.a() != null && fVar.a().equals(fVar2.a()))) {
                fVar2.a(fVar.f());
                if ((fVar2.g() == null && fVar.g() != null) || (fVar2.g() != null && fVar.g() != null && fVar2.g().c().equals(fVar.g().c()))) {
                    fVar2.a(fVar.g());
                }
                fVar2.a(fVar.e());
                if (fVar2.a() == null || fVar2.a().isEmpty()) {
                    fVar2.a(fVar.a());
                }
                if (fVar2.h() == null || fVar2.h().a() == null || fVar2.h().a().isEmpty()) {
                    fVar2.a(fVar.h());
                }
                z2 = true;
            }
        }
        if (z2) {
            a();
        }
        return z2;
    }

    public void c(f fVar) {
        for (int i2 = 0; i2 < this.f10209b.size(); i2++) {
            f fVar2 = (f) this.f10209b.get(i2);
            if (fVar.f().c().equals(fVar2.f().c()) && fVar.f().g() == fVar2.f().g()) {
                this.f10209b.remove(i2);
                a();
                return;
            }
        }
    }

    @Override // com.efiAnalytics.ui.eX
    public void a(int i2, int i3) {
        String strA = this.f10212e.a();
        f fVar = (f) this.f10209b.get(i2);
        if (strA.equals(f10221p)) {
            e(fVar);
        } else if (strA.equals(f10216k)) {
            d(fVar);
        } else if (strA.equals(f10215j)) {
            bV.d(C1818g.b("Communicating with this device is currently disabled. Contact EFI Analytics for further information.") + "\nsupport@efianalytics.com", this);
        } else if (strA.equals(f10218m)) {
            T.a().c().C().c();
        } else if (!strA.equals(f10217l)) {
            bH.C.c("Unknown Action: " + strA);
        } else if (j(fVar)) {
            try {
                T.a().c().C().d();
            } catch (C0129l e2) {
                Logger.getLogger(q.class.getName()).log(Level.SEVERE, "Failed to go online", (Throwable) e2);
            }
            C0338f.a().R();
        } else {
            a(fVar, true);
        }
        removeEditor();
        a();
    }

    public void a(f fVar, boolean z2) {
        C0338f.a().a(fVar, z2);
    }

    public void d(f fVar) {
        C0338f.a().a(fVar);
        b(fVar);
        a();
        b();
    }

    public void e(f fVar) {
        if (fVar == null || j(fVar)) {
            C0338f.a().R();
        } else {
            a(fVar, true);
        }
    }

    protected void a() {
        boolean z2 = false;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (f fVar : this.f10209b) {
            if (System.currentTimeMillis() - fVar.e() > f10208a) {
                arrayList2.add(fVar);
            } else {
                if (arrayList2.size() > 0) {
                    z2 = true;
                }
                arrayList.add(fVar);
            }
        }
        if (z2 || this.f10223h != arrayList.size()) {
            this.f10209b.clear();
            this.f10209b.addAll(arrayList);
            this.f10209b.addAll(arrayList2);
            SwingUtilities.invokeLater(new t(this));
        }
        this.f10223h = arrayList.size();
    }

    public void b() {
        SwingUtilities.invokeLater(new u(this));
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        r2.C().b(this.f10222g);
    }

    @Override // G.S
    public void c(R r2) {
        r2.C().a(this.f10222g);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean h(f fVar) {
        R rC = T.a().c();
        return rC != null && (rC.C() instanceof bQ.l) && rC.C().q() && i(fVar);
    }

    private boolean i(f fVar) {
        return (aE.a.A() == null || fVar.h() == null || fVar.h().b() == null || !fVar.h().b().equals(new File(aE.a.A().t()))) ? false : true;
    }

    private boolean j(f fVar) {
        return T.a().c() != null && i(fVar);
    }
}
