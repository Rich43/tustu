package bB;

import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/* loaded from: TunerStudioMS.jar:bB/h.class */
public class h extends JTable {

    /* renamed from: a, reason: collision with root package name */
    aa f6547a;

    /* renamed from: b, reason: collision with root package name */
    k f6548b = new k(this);

    /* renamed from: c, reason: collision with root package name */
    private List f6549c = new ArrayList();

    public h(aa aaVar) {
        this.f6547a = null;
        this.f6547a = aaVar;
        setModel(this.f6548b);
        setSelectionMode(2);
        setRowSelectionAllowed(true);
        getColumnModel().getColumn(0).setMinWidth(eJ.a(160));
        getColumnModel().getColumn(0).setMaxWidth(eJ.a(280));
        getColumnModel().getColumn(1).setMinWidth(eJ.a(160));
        getColumnModel().getColumn(1).setMaxWidth(eJ.a(280));
        setRowHeight(getFont().getSize() + eJ.a(2));
        super.addKeyListener(new j(this));
    }

    public void a() {
        int i2 = 0;
        FontMetrics fontMetrics = getFontMetrics(getFont());
        int iA = eJ.a(4);
        Iterator it = this.f6549c.iterator();
        while (it.hasNext()) {
            int iStringWidth = fontMetrics.stringWidth(((r) it.next()).e()) + iA;
            if (iStringWidth > i2) {
                i2 = iStringWidth;
            }
        }
        DefaultTableColumnModel defaultTableColumnModel = (DefaultTableColumnModel) getColumnModel();
        TableColumn column = defaultTableColumnModel.getColumn(0);
        defaultTableColumnModel.getColumn(0).setPreferredWidth(i2);
        column.setWidth(i2);
    }

    public void b() {
        this.f6548b.fireTableDataChanged();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String b(String str) {
        return this.f6547a != null ? this.f6547a.a(str) : str;
    }

    public void a(List list) {
        this.f6549c = list;
        this.f6548b.fireTableDataChanged();
    }

    public List c() {
        return this.f6549c;
    }

    public r a(int i2) {
        if (i2 < 0 || i2 >= this.f6549c.size()) {
            return null;
        }
        return (r) this.f6549c.get(i2);
    }

    public boolean a(r rVar) {
        for (int i2 = 0; i2 < this.f6549c.size(); i2++) {
            if (rVar.e().equals(((r) this.f6549c.get(i2)).e())) {
                this.f6549c.set(i2, rVar);
                this.f6548b.fireTableDataChanged();
                return true;
            }
        }
        this.f6549c.add(rVar);
        Collections.sort(this.f6549c, new i(this));
        this.f6548b.fireTableDataChanged();
        return false;
    }

    public r a(String str) {
        for (r rVar : this.f6549c) {
            if (rVar.e().equals(str)) {
                return rVar;
            }
        }
        return null;
    }
}
