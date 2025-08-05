package ao;

import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1705w;
import g.C1733k;
import java.awt.Component;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

/* loaded from: TunerStudioMS.jar:ao/hI.class */
public class hI extends C1705w implements TableModelListener {

    /* renamed from: w, reason: collision with root package name */
    private gN f6038w = null;

    public hI() {
        b(false);
        f().addMouseListener(new hL(this));
    }

    void a(hy hyVar) {
        this.f6038w = hyVar;
    }

    public void a(hH hHVar) {
        String strA;
        h().a(hHVar.g());
        h().b(hHVar.h());
        b(hHVar.i());
        if (this.f6038w != null && (strA = this.f6038w.a("yAxis_" + hHVar.z())) != null && !strA.equals("")) {
            hHVar.d(strA);
        }
        super.a((C1701s) hHVar);
        h().c(b(hHVar));
    }

    private boolean b(hH hHVar) {
        return ((int) Math.log10(hHVar.F())) + hHVar.i() <= 3 && hHVar.h() < 1 && bH.I.a() && ((int) Math.log10(hHVar.B())) + hHVar.g() <= 3;
    }

    public void a(int i2, int i3) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        String strA = this.f6038w != null ? this.f6038w.a("yAxis_" + ((C1701s) h().getModel()).z()) : "";
        if (strA == null || strA.equals("")) {
            JMenuItem jMenuItem = new JMenuItem("Set Y-Axis Override");
            jMenuItem.setFont(this.f11762a.getFont());
            jMenuItem.addActionListener(new hJ(this));
            jPopupMenu.add(jMenuItem);
        } else {
            JMenuItem jMenuItem2 = new JMenuItem("Clear Y-Axis Override");
            jMenuItem2.setFont(this.f11762a.getFont());
            jMenuItem2.addActionListener(new hK(this));
            jPopupMenu.add(jMenuItem2);
        }
        jPopupMenu.show(this, i2, i3);
    }

    public void a() {
        C1701s c1701s = (C1701s) h().getModel();
        c1701s.d(h.i.a("yAxisField", "MAP"));
        if (this.f6038w != null) {
            this.f6038w.a("yAxis_" + c1701s.z(), "");
        }
    }

    public void b() {
        C1701s c1701s = (C1701s) h().getModel();
        String strA = C1733k.a("{Y-Axis Override for " + c1701s.z() + "}", false, "Override Y-Axis Field.", true, (Component) this);
        if (strA == null || strA.equals("")) {
            return;
        }
        c1701s.d(strA);
        if (this.f6038w != null) {
            this.f6038w.a("yAxis_" + c1701s.z(), strA);
        }
    }

    @Override // com.efiAnalytics.ui.C1705w, javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        repaint();
        super.tableChanged(tableModelEvent);
    }
}
