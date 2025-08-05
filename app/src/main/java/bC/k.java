package bC;

import bH.C;
import bH.aa;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cI;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.commons.net.nntp.NNTPReply;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: TunerStudioMS.jar:bC/k.class */
public class k extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    aa f6590a;

    /* renamed from: b, reason: collision with root package name */
    b f6591b;

    /* renamed from: c, reason: collision with root package name */
    Z.c f6592c;

    /* renamed from: d, reason: collision with root package name */
    e f6593d;

    /* renamed from: e, reason: collision with root package name */
    JPanel f6594e;

    /* renamed from: h, reason: collision with root package name */
    private int f6595h;

    /* renamed from: f, reason: collision with root package name */
    JButton f6596f;

    /* renamed from: g, reason: collision with root package name */
    String f6597g = "Standardize Field naming will map many possible field name to a single standard field name.\nLog files for various systems often use different field names for fields that actually represent the same thing. This can break your Calculated Fields and Filters, or require you to set many redundant Min/Max limits for essentially the same field. This can also create confusion finding a field when switching systems. By using Standardized Field Names, you can have the same field name set no matter what system the log file is from.";

    /* renamed from: i, reason: collision with root package name */
    private Runnable f6598i;

    /* renamed from: j, reason: collision with root package name */
    private o f6599j;

    public k(aa aaVar) {
        this.f6590a = aaVar;
        setLayout(new BorderLayout(eJ.a(10), eJ.a(8)));
        setBorder(BorderFactory.createEmptyBorder(eJ.a(10), eJ.a(10), eJ.a(10), eJ.a(10)));
        JPanel jPanel = new JPanel();
        add(BorderLayout.CENTER, jPanel);
        jPanel.setLayout(new BorderLayout(eJ.a(10), eJ.a(8)));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        try {
            JButton jButton = new JButton(new ImageIcon(cO.a().a(cO.f11116F, this)));
            jButton.setPreferredSize(eJ.a(28, 28));
            jPanel2.add("West", jButton);
            jButton.addActionListener(new l(this));
            jPanel2.add("East", new cI(this.f6597g));
        } catch (V.a e2) {
            C.a(e2);
        }
        jPanel.add("North", jPanel2);
        this.f6591b = new b(aaVar);
        Component jScrollPane = new JScrollPane(this.f6591b);
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        jScrollPane.setPreferredSize(eJ.a(NNTPReply.NO_CURRENT_ARTICLE_SELECTED, 200));
        this.f6599j = new o(this);
        this.f6593d = new e(aaVar);
        this.f6593d.a(this.f6599j);
        this.f6594e = new JPanel();
        this.f6594e.setLayout(new BorderLayout());
        this.f6594e.add(BorderLayout.CENTER, this.f6593d);
        jPanel.add("South", this.f6594e);
        try {
            this.f6596f = new JButton(b(ToolWindow.SAVE_POLICY_FILE), new ImageIcon(cO.a().a(cO.f11123M, this)));
            this.f6596f.addActionListener(new m(this));
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new FlowLayout(2));
            this.f6594e.add("South", jPanel3);
            jPanel3.add(this.f6596f);
        } catch (V.a e3) {
            C.a(e3);
        }
        this.f6591b.getSelectionModel().addListSelectionListener(new p(this, null));
        this.f6593d.g();
    }

    public void a(Z.c cVar) {
        this.f6592c = cVar;
        this.f6593d.a(cVar);
        List listA = cVar.a();
        Collections.sort(listA, new n(this));
        this.f6591b.a(listA);
    }

    public void a(String str) {
        for (int i2 = 0; i2 < this.f6591b.getRowCount(); i2++) {
            if (str.equals(this.f6591b.a(i2).a())) {
                this.f6591b.getSelectionModel().setSelectionInterval(i2, i2);
                return;
            }
        }
    }

    public void a() {
        this.f6593d.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        if (this.f6593d.e() && bV.a(b("The Root Field currently being edited has not been saved.") + "\n" + b("Would you like to save it now?"), (Component) this.f6593d, true)) {
            if (!c()) {
                if (this.f6595h >= 0) {
                    this.f6591b.getSelectionModel().setSelectionInterval(i2, i2);
                    return;
                }
                return;
            }
            this.f6593d.g();
        }
        if (i2 < 0) {
            this.f6593d.g();
        } else {
            this.f6595h = i2;
            this.f6593d.a(this.f6591b.a(i2));
        }
    }

    private String b(String str) {
        return this.f6590a != null ? this.f6590a.a(str) : str;
    }

    public void b() {
        d();
        String strA = "";
        do {
            strA = bV.a((Component) this, false, "New Standard Field Name", strA);
            if (strA != null && !strA.isEmpty()) {
                if (this.f6593d.a(strA)) {
                    this.f6593d.a(new Z.e(strA));
                    return;
                }
                bV.d("Invalid characters in field name", this);
            }
            if (strA == null) {
                return;
            }
        } while (!strA.isEmpty());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c() {
        Z.e eVarD = this.f6593d.d();
        if (!a(eVarD)) {
            return false;
        }
        this.f6592c.a(eVarD);
        this.f6593d.f();
        this.f6591b.a(eVarD);
        return true;
    }

    private boolean a(Z.e eVar) {
        if (eVar == null || !c(eVar.a())) {
            return false;
        }
        Iterator it = eVar.b().iterator();
        while (it.hasNext()) {
            if (!c((String) it.next())) {
                return false;
            }
        }
        return true;
    }

    private boolean c(String str) {
        return str.trim().length() > 0;
    }

    public void a(Window window) {
        JDialog jDialogB = bV.b(this, window, b("Root Field Editor"), this);
        jDialogB.pack();
        bV.a((Component) window, (Component) jDialogB);
        jDialogB.setVisible(true);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        d();
    }

    public void a(Runnable runnable) {
        this.f6598i = runnable;
    }

    private void d() {
        if (this.f6593d.e()) {
            if (!bV.a(b("The Root Field currently being edited has not been saved.") + "\n" + b("Would you like to save it now?"), (Component) this.f6593d, true) || c()) {
                return;
            }
            C.c("Failed to save RootField");
            return;
        }
        Z.b.a().b();
        if (!this.f6599j.c() || this.f6598i == null) {
            return;
        }
        SwingUtilities.invokeLater(this.f6598i);
    }
}
