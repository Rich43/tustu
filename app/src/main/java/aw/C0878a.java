package aw;

import ao.C0595M;
import bH.aa;
import bx.j;
import bx.l;
import bx.s;
import bx.t;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/* renamed from: aw.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aw/a.class */
public class C0878a extends JPanel implements l {

    /* renamed from: a, reason: collision with root package name */
    aa f6307a;

    /* renamed from: b, reason: collision with root package name */
    t f6308b;

    public C0878a(aa aaVar) {
        this.f6307a = null;
        this.f6307a = aaVar;
        setLayout(new BorderLayout());
        this.f6308b = new t(aaVar);
        Iterator it = C0595M.a().d().iterator();
        while (it.hasNext()) {
            this.f6308b.a((j) it.next());
        }
        JScrollPane jScrollPane = new JScrollPane(this.f6308b);
        this.f6308b.setFillsViewportHeight(true);
        jScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        jScrollPane.setPreferredSize(new Dimension(eJ.a(150), eJ.a(150)));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(b("Data Filters")));
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        add(BorderLayout.CENTER, jPanel);
    }

    private String b(String str) {
        return this.f6307a != null ? this.f6307a.a(str) : str;
    }

    public Collection a() {
        return this.f6308b.b();
    }

    public void a(s sVar) {
        this.f6308b.a(sVar);
    }

    public void a(String str, boolean z2) {
        if (z2) {
            this.f6308b.a(str);
        } else {
            this.f6308b.b(str);
        }
    }

    @Override // bx.l
    public void a(j jVar) {
        this.f6308b.a(jVar);
    }

    @Override // bx.l
    public void a(String str) {
        this.f6308b.d(str);
    }

    @Override // bx.l
    public void b(j jVar) {
        j jVarE = this.f6308b.e(jVar.a());
        jVarE.b(jVar.b());
        jVarE.c(jVar.c());
    }
}
