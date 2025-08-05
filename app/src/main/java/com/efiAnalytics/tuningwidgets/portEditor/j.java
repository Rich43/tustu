package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.aM;
import G.aS;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.MenuContainer;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.fxml.FXMLLoader;
import javax.swing.JLabel;
import javax.swing.JPanel;
import r.C1806i;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/j.class */
class j extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f10566a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f10567b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    JPanel f10568c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10569d;

    public j(OutputPortEditor outputPortEditor, R r2, aS aSVar) {
        this.f10569d = outputPortEditor;
        this.f10568c = null;
        aM aMVarC = r2.c(aSVar.m());
        setLayout(new GridLayout(0, 1, 8, 8));
        int iB = aMVarC != null ? (aMVarC.c().a() == 1 || !C1806i.a().a("HF-0FD-0HHFJG")) ? 1 : aMVarC.c().b() : 0;
        this.f10568c = new JPanel();
        this.f10568c.setLayout(new FlowLayout(2));
        JLabel jLabel = new JLabel(C1818g.b(FXMLLoader.CONTROLLER_SUFFIX) + "                ");
        String strO = aSVar.o();
        if (strO != null && r2.c(strO) != null && C1806i.a().a("HF-0FD-0HHFJG")) {
            this.f10568c.add(jLabel);
        }
        this.f10568c.add(new JLabel(" " + C1818g.b("Output Channel") + " "));
        this.f10568c.add(new JLabel("                   "));
        this.f10568c.add(new JLabel(Constants.INDENT + C1818g.b("Threshold")));
        this.f10568c.add(new JLabel(Constants.INDENT + C1818g.b("Hysteresis")));
        add(this.f10568c);
        for (int i2 = 0; i2 <= iB; i2++) {
            z zVar = new z(r2, aSVar, i2);
            this.f10566a.add(zVar);
            add(zVar);
            if (i2 < iB) {
                ArrayList arrayList = new ArrayList();
                Iterator itC = aSVar.c();
                while (itC.hasNext()) {
                    arrayList.add(itC.next() + "");
                }
                v vVar = new v(r2, aSVar.m(), (String[]) arrayList.toArray(new String[arrayList.size()]));
                vVar.a(i2);
                this.f10567b.add(vVar);
                vVar.addItemListener(new k(this, outputPortEditor));
                C1534f c1534f = new C1534f(outputPortEditor);
                c1534f.setLayout(new BorderLayout(8, 8));
                c1534f.add("West", vVar);
                add(c1534f);
            }
        }
        a(0);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        bV.a(this.f10568c, z2);
        for (int i2 = 0; i2 < this.f10566a.size(); i2++) {
            bV.a((z) this.f10566a.get(i2), z2);
            if (i2 < this.f10567b.size()) {
                ((v) this.f10567b.get(i2)).setEnabled(z2);
            }
            if (z2 && i2 < this.f10567b.size()) {
                z2 = ((v) this.f10567b.get(i2)).a();
            }
        }
    }

    public void a(int i2) {
        Iterator it = this.f10567b.iterator();
        while (it.hasNext()) {
            ((v) it.next()).b(i2);
        }
        Iterator it2 = this.f10566a.iterator();
        while (it2.hasNext()) {
            ((z) it2.next()).a(i2);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            MenuContainer component = getComponent(i2);
            if (component instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) component).close();
            }
        }
    }
}
