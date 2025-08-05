package r;

import G.R;
import W.C0200z;
import bH.C;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.Z;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.apache.commons.net.nntp.NNTPReply;
import s.C1818g;
import v.C1883c;

/* renamed from: r.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:r/b.class */
public class C1799b extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    File f13422a = null;

    /* renamed from: b, reason: collision with root package name */
    C1425x f13423b = new C1425x();

    /* renamed from: c, reason: collision with root package name */
    JComboBox f13424c = new JComboBox();

    /* renamed from: d, reason: collision with root package name */
    JTextField f13425d = new JTextField("", 25);

    /* renamed from: e, reason: collision with root package name */
    JCheckBox f13426e = new JCheckBox(C1818g.b("Other"));

    /* renamed from: f, reason: collision with root package name */
    JButton f13427f = new JButton("...");

    /* renamed from: h, reason: collision with root package name */
    private R f13428h = null;

    /* renamed from: g, reason: collision with root package name */
    Runnable f13429g = new RunnableC1803f(this);

    public C1799b() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Select Dashboard")));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f13423b.setMinimumSize(eJ.a(NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 240));
        this.f13423b.setPreferredSize(eJ.a(NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 240));
        this.f13424c.addItem("");
        this.f13424c.addItemListener(new C1800c(this));
        jPanel.add("North", this.f13424c);
        jPanel.add("West", this.f13426e);
        this.f13426e.addItemListener(new C1801d(this));
        jPanel.add(BorderLayout.CENTER, this.f13425d);
        this.f13425d.setEnabled(this.f13426e.isSelected());
        this.f13425d.setEditable(false);
        jPanel.add("East", this.f13427f);
        this.f13427f.setEnabled(this.f13426e.isSelected());
        this.f13427f.addActionListener(new C1805h(this));
        if (!C1806i.a().a("43wunjt58j7tjtht")) {
            JLabel jLabel = new JLabel(C1818g.b("Lite! Edition will always load default cluster on project load!"));
            jLabel.setForeground(Color.red);
            jPanel.add("South", jLabel);
        }
        add("North", jPanel);
        this.f13423b.setEnabled(false);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new bz.b());
        jPanel2.add(this.f13423b);
        add(BorderLayout.CENTER, jPanel2);
    }

    public void a(String str) {
        a(new File(str));
    }

    public void a(File file) {
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles == null) {
            C.b("No dashfiles found in :\n\t" + ((Object) file));
            return;
        }
        for (File file2 : fileArrListFiles) {
            this.f13424c.addItem(new C1804g(this, file2));
        }
    }

    public void a() {
        try {
            for (File file : C1807j.p()) {
                this.f13424c.addItem(new C1804g(this, file));
            }
        } catch (V.a e2) {
            bV.d(e2.getMessage(), this);
        }
    }

    public File b() {
        return this.f13422a;
    }

    public void b(File file) {
        this.f13422a = file;
        this.f13423b.E();
        try {
            this.f13423b.a(cO.a().a(cO.f11108x));
            this.f13423b.j(C1425x.f9600p);
        } catch (V.a e2) {
            C.b("Cluster Wait image failed to load: " + e2.getLocalizedMessage());
        }
        this.f13423b.b(false);
        new C1802e(this, file).start();
    }

    public void a(String[] strArr, o oVar) {
        if (oVar == null) {
            return;
        }
        try {
            C1807j.p();
            int i2 = 0;
            while (i2 < this.f13424c.getItemCount()) {
                if (this.f13424c.getItemAt(i2) instanceof C1804g) {
                    C1804g c1804g = (C1804g) this.f13424c.getItemAt(i2);
                    if (!a(oVar, strArr, C0200z.a(c1804g.a()))) {
                        this.f13424c.removeItem(c1804g);
                        i2--;
                    }
                }
                i2++;
            }
        } catch (V.a e2) {
            C.a("Unexpected Error applying filters", e2, this);
        }
    }

    private boolean a(o oVar, String[] strArr, String str) {
        for (String str2 : strArr) {
            if (oVar.a(str2, str)) {
                return true;
            }
        }
        return false;
    }

    public boolean c() {
        return this.f13426e.isSelected();
    }

    public void a(String str, Z z2) {
        String str2 = C1818g.b(Action.DEFAULT) + " (" + str + ")";
        if (b(str2)) {
            return;
        }
        String str3 = ((Object) C1807j.y()) + File.separator + str2 + ".dash";
        try {
            new C1883c(C1807j.G()).a(str3, z2);
        } catch (V.a e2) {
            C.a("Error creating default dashboard.\n" + e2.getMessage() + "\nCheck Log for more details", e2, this);
        }
        File file = new File(str3);
        file.deleteOnExit();
        C1804g c1804g = new C1804g(this, file);
        this.f13424c.addItem(c1804g);
        if (this.f13424c.getSelectedItem() == null || !this.f13424c.getSelectedItem().equals(c1804g)) {
            this.f13424c.setSelectedItem(c1804g);
        }
    }

    private boolean b(String str) {
        for (int i2 = 0; i2 < this.f13424c.getItemCount(); i2++) {
            if ((this.f13424c.getItemAt(i2) instanceof C1804g) && ((C1804g) this.f13424c.getItemAt(i2)).toString().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public void d() {
        this.f13423b.b(false);
    }

    public Z e() {
        return this.f13423b.i();
    }

    public boolean f() {
        if (this.f13422a != null) {
            return true;
        }
        bV.d(C1818g.b("Please select a dashboard layout"), this);
        return false;
    }

    public boolean g() {
        return (this.f13426e.isSelected() || this.f13424c.getSelectedItem().toString().indexOf(C1818g.b(Action.DEFAULT)) == -1) ? false : true;
    }
}
