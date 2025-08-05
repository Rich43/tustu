package bg;

import G.C0073bf;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import s.C1818g;

/* renamed from: bg.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/o.class */
public class C1135o extends JPanel {

    /* renamed from: b, reason: collision with root package name */
    List f8090b;

    /* renamed from: d, reason: collision with root package name */
    List f8092d = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    C1137q f8091c = new C1137q(this);

    /* renamed from: a, reason: collision with root package name */
    JTable f8089a = new JTable();

    public C1135o() {
        this.f8090b = new ArrayList();
        this.f8090b = this.f8090b;
        this.f8089a.setModel(this.f8091c);
        this.f8089a.setSelectionMode(0);
        this.f8089a.getSelectionModel().addListSelectionListener(new C1136p(this));
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Tuning Views in Loaded ECU Definition")));
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, new JScrollPane(this.f8089a));
    }

    public void a(C0073bf c0073bf) {
        this.f8090b.add(c0073bf);
        this.f8091c.fireTableDataChanged();
    }

    public boolean b(C0073bf c0073bf) {
        boolean zRemove = this.f8090b.remove(c0073bf);
        this.f8091c.fireTableDataChanged();
        this.f8089a.validate();
        return zRemove;
    }

    public C0073bf a() {
        int selectedRow = this.f8089a.getSelectedRow();
        return selectedRow >= 0 ? (C0073bf) this.f8090b.get(selectedRow) : null;
    }

    public void b() {
        int selectedRow = this.f8089a.getSelectedRow();
        if (selectedRow > 0) {
            this.f8090b.add(selectedRow - 1, (C0073bf) this.f8090b.remove(selectedRow));
            for (int i2 = 0; i2 < this.f8090b.size(); i2++) {
                ((C0073bf) this.f8090b.get(i2)).a(i2);
            }
            this.f8091c.fireTableDataChanged();
            this.f8089a.getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);
        }
    }

    public void c() {
        int selectedRow = this.f8089a.getSelectedRow();
        if (selectedRow < 0 || selectedRow >= this.f8090b.size() - 1) {
            return;
        }
        this.f8090b.add(selectedRow + 1, (C0073bf) this.f8090b.remove(selectedRow));
        for (int i2 = 0; i2 < this.f8090b.size(); i2++) {
            ((C0073bf) this.f8090b.get(i2)).a(i2);
        }
        this.f8091c.fireTableDataChanged();
        this.f8089a.getSelectionModel().setSelectionInterval(selectedRow + 1, selectedRow + 1);
    }

    public void a(InterfaceC1140t interfaceC1140t) {
        this.f8092d.add(interfaceC1140t);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        C0073bf c0073bfA = a();
        Iterator it = this.f8092d.iterator();
        while (it.hasNext()) {
            ((InterfaceC1140t) it.next()).a(c0073bfA);
        }
    }

    public int d() {
        return this.f8089a.getSelectionModel().getMaxSelectionIndex();
    }
}
