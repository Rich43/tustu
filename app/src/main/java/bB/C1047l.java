package bb;

import bH.aa;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/* renamed from: bb.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bb/l.class */
public class C1047l extends JTable {

    /* renamed from: b, reason: collision with root package name */
    private aa f7779b;

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f7780c = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    C1048m f7781a = new C1048m(this);

    public C1047l(aa aaVar) {
        this.f7779b = aaVar;
        setModel(this.f7781a);
        setSelectionMode(0);
        setRowSelectionAllowed(true);
    }

    public void a(List list) {
        this.f7780c.clear();
        this.f7780c.addAll(list);
        a();
    }

    public void a() {
        this.f7781a.fireTableDataChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return this.f7779b != null ? this.f7779b.a(str) : str;
    }

    public List b() {
        return this.f7780c;
    }

    public void a(int i2) {
        getSelectionModel().setSelectionInterval(i2, i2);
    }

    public ae.q c() {
        if (getSelectionModel().getMinSelectionIndex() >= 0) {
            return (ae.q) this.f7780c.get(getSelectionModel().getMinSelectionIndex());
        }
        return null;
    }

    public int d() {
        return this.f7780c.size();
    }
}
