package bh;

import G.R;
import G.dg;
import bH.C;
import com.efiAnalytics.tuningwidgets.panels.SelectableTablePanel;
import com.efiAnalytics.ui.C1705w;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1662et;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import n.C1762b;

/* renamed from: bh.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/n.class */
public class C1154n extends JPanel implements dg, InterfaceC1565bc {

    /* renamed from: h, reason: collision with root package name */
    private C1161u f8118h = new C1161u(this);

    /* renamed from: a, reason: collision with root package name */
    C1762b f8119a = new C1762b(16);

    /* renamed from: b, reason: collision with root package name */
    C1160t f8120b = new C1160t(this);

    /* renamed from: c, reason: collision with root package name */
    JScrollPane f8121c = new JScrollPane();

    /* renamed from: d, reason: collision with root package name */
    SelectableTablePanel f8122d;

    /* renamed from: e, reason: collision with root package name */
    SelectableTablePanel f8123e;

    /* renamed from: f, reason: collision with root package name */
    InterfaceC1662et f8124f;

    /* renamed from: g, reason: collision with root package name */
    R f8125g;

    public C1154n(R r2, InterfaceC1662et interfaceC1662et) {
        this.f8122d = null;
        this.f8123e = null;
        this.f8124f = interfaceC1662et;
        this.f8125g = r2;
        setLayout(new BorderLayout());
        this.f8121c.setHorizontalScrollBarPolicy(31);
        this.f8121c.setVerticalScrollBarPolicy(22);
        this.f8121c.setViewportView(this.f8118h);
        add(BorderLayout.CENTER, this.f8121c);
        this.f8119a.a(new C1158r(this, r2));
        JPanel jPanel = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setAlignment(2);
        jPanel.setLayout(flowLayout);
        jPanel.add(this.f8119a);
        this.f8119a.d(false);
        add("North", jPanel);
        r2.C().a(this.f8120b);
        this.f8119a.c(r2.C().C());
        r2.p().a(this);
        this.f8118h.setLayout(new BoxLayout(this.f8118h, 1));
        this.f8122d = new SelectableTablePanel(r2);
        this.f8122d.setName("selectionTable1");
        this.f8122d.a(new C1155o(this));
        this.f8122d.a(b("selectionTable1", C1141a.a(r2)));
        this.f8123e = new SelectableTablePanel(r2);
        this.f8123e.setName("selectionTable2");
        this.f8123e.a(new C1156p(this));
        this.f8123e.a(b("selectionTable2", C1141a.b(r2)));
        this.f8121c.addComponentListener(new C1157q(this));
    }

    public void a(Component component) {
        this.f8118h.add(component);
    }

    public void a(int i2) {
        if (i2 >= 1) {
            this.f8118h.add(this.f8122d, 0);
        } else {
            this.f8118h.remove(this.f8122d);
        }
        if (i2 >= 2) {
            this.f8118h.add(this.f8123e, 1);
        } else {
            this.f8118h.remove(this.f8123e);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        for (int i2 = 0; i2 < this.f8118h.getComponentCount(); i2++) {
            if (this.f8118h.getComponent(i2) instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) this.f8118h.getComponent(i2)).close();
            }
        }
        this.f8123e.close();
        try {
            this.f8125g.C().b(this.f8120b);
            this.f8125g.p().b(this);
        } catch (Exception e2) {
            C.c("b failed");
        }
    }

    void a(String str, String str2) {
        if (this.f8124f != null) {
            this.f8124f.a(str, str2);
        }
    }

    String b(String str, String str2) {
        if (this.f8124f == null) {
            return str2;
        }
        String strA = this.f8124f.a(str);
        return (strA == null || strA.isEmpty()) ? str2 : strA;
    }

    @Override // G.dg
    public void a(boolean z2) {
        this.f8119a.a(z2);
    }

    @Override // G.dg
    public void b(boolean z2) {
        this.f8119a.b(z2);
    }

    private List a(List list, Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof C1705w) {
                list.add((C1705w) component);
            } else if (component instanceof Container) {
                a(list, (Container) component);
            }
        }
        return list;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public List b() {
        return a(new ArrayList(), this.f8118h);
    }

    public C1161u a() {
        return this.f8118h;
    }
}
