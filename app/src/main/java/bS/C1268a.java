package bs;

import G.R;
import G.dn;
import bH.W;
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
import r.C1798a;
import s.C1818g;

/* renamed from: bs.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/a.class */
public class C1268a extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    R f8570a;

    /* renamed from: b, reason: collision with root package name */
    dn f8571b = null;

    /* renamed from: c, reason: collision with root package name */
    ArrayList f8572c;

    public C1268a(R r2, dn dnVar) {
        this.f8570a = null;
        this.f8572c = null;
        this.f8570a = r2;
        setLayout(new BorderLayout());
        this.f8572c = C1272e.a().a(r2, dnVar);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 2));
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Filters")));
        if (this.f8572c != null) {
            Iterator it = this.f8572c.iterator();
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
        jButton.addActionListener(new C1269b(this));
        return jButton;
    }

    private void a(JButton jButton, bL.k kVar) {
        if (kVar.e() != 128) {
            jButton.setText(C1818g.b(kVar.a()) + ": " + W.a(kVar.c()));
        } else {
            jButton.setText(C1818g.b(kVar.a()) + (kVar.f().equals("") ? C1818g.b("Off") : C1818g.b("On")));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(JButton jButton) {
        bL.k kVarA = a(jButton.getName());
        if (kVarA.e() == 128) {
            String strA = bV.a("{Custom filter condition}", false, "Performs custom filter as evaluated by " + C1798a.f13268b + ".\nSee website for more details. \nex. (throttle > 25 ) || rpm > 3000 \nfilters any record where throttle is greater than 25% OR rpm is greater than 3000.\nTo deactivate custom filter, press cancel or clear and press Ok.", true, (Component) this, (fw) new C1271d(this, kVarA), (fx) new C1270c(this, this));
            if (strA == null || strA.equals("")) {
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
        Iterator it = this.f8572c.iterator();
        while (it.hasNext()) {
            bL.k kVar = (bL.k) it.next();
            if (kVar.g().equals(str)) {
                return kVar;
            }
        }
        return null;
    }
}
