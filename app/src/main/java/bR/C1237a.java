package br;

import G.dk;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.fw;
import com.efiAnalytics.ui.fx;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import org.apache.commons.math3.geometry.VectorFormat;
import r.C1798a;
import s.C1818g;

/* renamed from: br.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/a.class */
public class C1237a extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    G.R f8401a;

    /* renamed from: b, reason: collision with root package name */
    dk f8402b = null;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f8403c;

    public C1237a(G.R r2, dk dkVar) {
        this.f8401a = null;
        this.f8403c = null;
        this.f8401a = r2;
        setLayout(new BorderLayout());
        this.f8403c = C1241e.a().a(r2, dkVar);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 2));
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Filters")));
        if (this.f8403c != null) {
            Iterator it = this.f8403c.iterator();
            while (it.hasNext()) {
                bL.k kVar = (bL.k) it.next();
                if (kVar.h()) {
                    jPanel.add(a(kVar));
                }
            }
            add(BorderLayout.CENTER, jPanel);
        }
    }

    private JButton a(bL.k kVar) {
        JButton jButton = new JButton();
        jButton.setName(kVar.g());
        a(jButton, kVar);
        jButton.addActionListener(new C1238b(this));
        return jButton;
    }

    private void a(JButton jButton, bL.k kVar) {
        if (kVar.e() != 128) {
            jButton.setText(C1818g.b(kVar.a()) + ": " + bH.W.a(kVar.c()));
        } else {
            jButton.setText(C1818g.b(kVar.a()) + (kVar.f().equals("") ? C1818g.b("Off") : C1818g.b("On")));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(JButton jButton) {
        bL.k kVarA = a(jButton.getName());
        if (kVarA.e() == 128) {
            String strA = bV.a(VectorFormat.DEFAULT_PREFIX + C1818g.b("Custom filter condition") + "}", false, C1818g.b("Performs custom filter as evaluated by " + C1798a.f13268b + ".") + "\n" + C1818g.b("See website for more details.") + " \nex. (coolant > 200) && rpm > 5000 \n" + C1818g.b("filters any record where coolant temp is greater than 200 and rpm is greater than 500.") + "\n" + C1818g.b("To deactivate custom filter, press cancel or clear and press Ok."), true, (Component) this, (fw) new C1240d(this, kVarA), (fx) new C1239c(this, this), C1818g.d());
            if (strA == null) {
                return;
            }
            if (strA.trim().equals("")) {
                kVarA.c("");
            } else {
                kVarA.c(strA);
            }
        } else {
            String strA2 = bV.a((Component) this, true, C1818g.b(kVarA.a()), "" + kVarA.c());
            if (strA2 == null || strA2.equals("")) {
                return;
            } else {
                kVarA.a(Double.parseDouble(strA2));
            }
        }
        a(jButton, kVarA);
    }

    private bL.k a(String str) {
        Iterator it = this.f8403c.iterator();
        while (it.hasNext()) {
            bL.k kVar = (bL.k) it.next();
            if (kVar.g().equals(str)) {
                return kVar;
            }
        }
        return null;
    }
}
