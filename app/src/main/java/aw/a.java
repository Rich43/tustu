package aW;

import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aW/a.class */
public class a extends JPanel implements p {

    /* renamed from: a, reason: collision with root package name */
    JComboBox f3961a;

    /* renamed from: b, reason: collision with root package name */
    JScrollPane f3962b;

    /* renamed from: c, reason: collision with root package name */
    A.g f3963c;

    /* renamed from: d, reason: collision with root package name */
    A.i f3964d;

    /* renamed from: e, reason: collision with root package name */
    A.f f3965e;

    /* renamed from: f, reason: collision with root package name */
    o f3966f;

    /* renamed from: g, reason: collision with root package name */
    d f3967g;

    /* renamed from: h, reason: collision with root package name */
    List f3968h;

    public a(A.i iVar, A.g gVar) {
        this(iVar, gVar, true);
    }

    public a(A.i iVar, A.g gVar, boolean z2) {
        this.f3961a = new JComboBox();
        this.f3962b = null;
        this.f3965e = null;
        this.f3966f = null;
        this.f3967g = new d(this);
        this.f3968h = new ArrayList();
        this.f3963c = gVar;
        this.f3964d = iVar;
        List listA = iVar.a();
        this.f3961a.addItem("");
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            this.f3961a.addItem(new c(this, (A.q) it.next()));
        }
        this.f3961a.addItemListener(new b(this));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Connection Type")));
        jPanel.setLayout(new FlowLayout(1));
        jPanel.add(this.f3961a);
        if (z2) {
            add("North", jPanel);
        }
        this.f3966f = new o(this);
        this.f3966f.a(this);
        this.f3962b = new JScrollPane(this.f3966f);
        Dimension dimensionA = eJ.a(360, 130);
        this.f3962b.setMinimumSize(dimensionA);
        this.f3962b.setPreferredSize(dimensionA);
        this.f3962b.setMaximumSize(dimensionA);
        this.f3962b.setBorder(BorderFactory.createTitledBorder(C1818g.b("Connection Settings")));
        add(BorderLayout.CENTER, this.f3962b);
        if (gVar.a() != null) {
            a(gVar.a());
        }
    }

    public void a(A.f fVar) {
        if (fVar == null) {
            this.f3961a.setSelectedIndex(0);
            return;
        }
        for (int i2 = 0; i2 < this.f3961a.getItemCount(); i2++) {
            if (this.f3961a.getItemAt(i2).toString().equals(fVar.h())) {
                if (this.f3961a.getSelectedIndex() != i2) {
                    this.f3961a.setSelectedIndex(i2);
                }
                for (A.r rVar : fVar.l()) {
                    this.f3966f.a(rVar.c(), fVar.a(rVar.c()));
                }
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (this.f3965e != null) {
            this.f3965e.b(this.f3967g);
        }
        this.f3965e = b();
        if (this.f3965e == null) {
            this.f3966f.a();
            validate();
            b("Driver", null);
            return;
        }
        this.f3965e.a(this.f3967g);
        b("Driver", this.f3965e.h());
        List<A.r> listL = this.f3965e.l();
        this.f3966f.a();
        this.f3966f.a(listL);
        for (A.r rVar : listL) {
            this.f3966f.a(rVar.c(), this.f3965e.a(rVar.c()));
        }
        validate();
    }

    public void a() {
        d();
    }

    public Object a(String str) {
        return this.f3966f.a(str);
    }

    public A.f b() {
        Object selectedItem = this.f3961a.getSelectedItem();
        if (!(selectedItem instanceof c)) {
            return null;
        }
        c cVar = (c) selectedItem;
        try {
            return (this.f3965e == null || !cVar.equals(this.f3965e)) ? cVar.a().c("DEFAULT_INSTANCE") : this.f3965e;
        } catch (IllegalAccessException e2) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        } catch (InstantiationException e3) {
            Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return null;
        }
    }

    public boolean c() {
        return this.f3966f.b();
    }

    public void a(p pVar) {
        this.f3968h.add(pVar);
    }

    private void b(String str, String str2) {
        Iterator it = this.f3968h.iterator();
        while (it.hasNext()) {
            ((p) it.next()).a(str, str2);
        }
    }

    @Override // aW.p
    public void a(String str, String str2) {
        b(str, str2);
    }

    public A.f b(String str) {
        for (int i2 = 0; i2 < this.f3961a.getItemCount(); i2++) {
            if (this.f3961a.getItemAt(i2) instanceof c) {
                c cVar = (c) this.f3961a.getItemAt(i2);
                if (cVar.a().a().equals(str)) {
                    try {
                        return cVar.a().c("DEFAULT_INSTANCE");
                    } catch (IllegalAccessException e2) {
                        Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    } catch (InstantiationException e3) {
                        Logger.getLogger(a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }
}
