package aV;

import G.C0129l;
import ai.C0512b;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: aV.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aV/a.class */
public class C0474a extends JPanel implements InterfaceC1565bc {

    /* renamed from: b, reason: collision with root package name */
    aW.a f3937b;

    /* renamed from: a, reason: collision with root package name */
    JDialog f3936a = null;

    /* renamed from: c, reason: collision with root package name */
    JCheckBox f3938c = new JCheckBox(C1818g.b("GPS Enabled"));

    /* renamed from: d, reason: collision with root package name */
    C0512b f3939d = new C0512b();

    public C0474a(aK.a aVar) throws IllegalArgumentException {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, this.f3938c);
        JLabel jLabel = new JLabel();
        jLabel.setText("<html><font color=\"blue\"><u>" + C1818g.b("More Information") + "</u></font>&nbsp;&nbsp;</html>");
        this.f3939d.a(C1818g.b("GPS Setup Help"));
        this.f3939d.b("/help/gps.html");
        jLabel.addMouseListener(new b(this));
        jPanel.add("East", jLabel);
        add("North", jPanel);
        this.f3938c.setSelected(x.a().c());
        this.f3938c.addActionListener(new c(this));
        this.f3937b = new aW.a(f.c(), aVar);
        add(BorderLayout.CENTER, this.f3937b);
        C1685fp.a((Component) this.f3937b, this.f3938c.isSelected());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        try {
            x.a().d();
            if (x.a().b() != null && x.a().b().a() != null) {
                this.f3937b.a(x.a().b().a());
            }
        } catch (C0129l e2) {
            bV.d(e2.getLocalizedMessage(), this);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (this.f3936a != null) {
            this.f3936a.dispose();
        }
    }

    public boolean a() {
        if (!this.f3938c.isSelected()) {
            x.a().e();
            return true;
        }
        A.f fVarB = this.f3937b.b();
        if (fVarB == null) {
            bV.d(C1818g.b("Please Select a Connection Type"), this);
            return false;
        }
        if (!this.f3937b.c()) {
            bV.d(C1818g.b("Please Correct the setting"), this);
            return false;
        }
        boolean z2 = x.a().a(x.f3954a) == null || !x.a().a(x.f3954a).equals(fVarB.h());
        x.a().a(x.f3954a, fVarB.h());
        for (A.r rVar : fVarB.l()) {
            if (rVar.a() != 5) {
                if (!z2 && !x.a().a(x.f3954a, rVar).equals(this.f3937b.a(rVar.c()))) {
                    z2 = true;
                }
                x.a().a(x.f3954a, rVar, this.f3937b.a(rVar.c()));
            }
        }
        if (!z2) {
            return true;
        }
        x.a().e();
        try {
            x.a().d();
            return true;
        } catch (C0129l e2) {
            bV.d(e2.getLocalizedMessage(), this);
            Logger.getLogger(C0474a.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    public void a(Component component) {
        this.f3936a = new JDialog(bV.a(component), C1818g.b("GPS Configuration"));
        this.f3936a.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new d(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new e(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f3936a.add("South", jPanel);
        this.f3936a.pack();
        bV.a((Window) bV.a(component), (Component) this.f3936a);
        this.f3936a.setVisible(true);
        validate();
        this.f3936a.pack();
    }
}
