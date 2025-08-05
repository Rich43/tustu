package bB;

import bH.C;
import bH.I;
import bH.aa;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: TunerStudioMS.jar:bB/l.class */
public class l extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    h f6557a;

    /* renamed from: b, reason: collision with root package name */
    d f6558b;

    /* renamed from: c, reason: collision with root package name */
    aa f6559c;

    /* renamed from: f, reason: collision with root package name */
    private q f6560f;

    /* renamed from: d, reason: collision with root package name */
    JButton f6561d;

    /* renamed from: e, reason: collision with root package name */
    JButton f6562e;

    /* renamed from: g, reason: collision with root package name */
    private int f6563g;

    public l(aa aaVar, q qVar) {
        this(aaVar);
        a(qVar);
    }

    public l(aa aaVar) {
        this.f6560f = null;
        this.f6559c = aaVar;
        setLayout(new BorderLayout(5, eJ.a(8)));
        setBorder(BorderFactory.createEmptyBorder(eJ.a(10), eJ.a(10), eJ.a(10), eJ.a(10)));
        this.f6557a = new h(aaVar);
        JScrollPane jScrollPane = new JScrollPane(this.f6557a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        add(BorderLayout.CENTER, jPanel);
        jScrollPane.setPreferredSize(eJ.a(400, 180));
        this.f6557a.getSelectionModel().addListSelectionListener(new p(this, null));
        if (I.b()) {
            jPanel.add("South", new JLabel(a("Use Command, CTRL or Shift to multi-select"), 0));
        } else {
            jPanel.add("South", new JLabel(a("Use CTRL or Shift to multi-select"), 0));
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        add("South", jPanel2);
        this.f6558b = new d(aaVar);
        this.f6558b.setEnabled(false);
        jPanel2.add(BorderLayout.CENTER, this.f6558b);
        jPanel2.setBorder(BorderFactory.createTitledBorder(a("Edit Field Attributes")));
        try {
            this.f6561d = new JButton(a(ToolWindow.SAVE_POLICY_FILE), new ImageIcon(cO.a().a(cO.f11123M, this)));
            this.f6561d.addActionListener(new m(this));
            this.f6561d.setEnabled(false);
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new FlowLayout(2));
            jPanel2.add("South", jPanel3);
            this.f6562e = new JButton("Reset Default Limits");
            this.f6562e.addActionListener(new n(this));
            this.f6562e.setEnabled(false);
            jPanel3.add(this.f6562e);
            jPanel3.add(this.f6561d);
        } catch (V.a e2) {
            C.a(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        r rVarB = this.f6560f.b(this.f6558b.e());
        if (rVarB == null || rVarB.e().trim().isEmpty()) {
            String[] strArrA = this.f6558b.a();
            if (strArrA.length > 0) {
                for (String str : strArrA) {
                    r rVarA = this.f6557a.a(str);
                    if (rVarA != null) {
                        rVarA.b(Double.NaN);
                        rVarA.a(Double.NaN);
                        rVarA.a(-1);
                        this.f6560f.a(rVarA);
                        this.f6560f.a(rVarA.e());
                    }
                }
                this.f6557a.b();
            }
        } else {
            String[] strArrA2 = this.f6558b.a();
            if (strArrA2.length > 1) {
                for (String str2 : strArrA2) {
                    r rVarA2 = this.f6557a.a(str2);
                    if (rVarA2 != null) {
                        rVarA2.b(rVarB.b());
                        rVarA2.a(rVarB.a());
                        rVarA2.a(rVarB.f());
                        this.f6560f.a(rVarB);
                        this.f6560f.a(rVarB.e());
                    }
                }
                this.f6557a.b();
            } else {
                this.f6557a.a(rVarB);
                this.f6560f.a(rVarB);
                this.f6560f.a(rVarB.e());
                this.f6557a.b();
            }
        }
        this.f6558b.f();
        this.f6558b.d();
        this.f6558b.setEnabled(false);
        this.f6561d.setEnabled(false);
        this.f6562e.setEnabled(false);
    }

    private String a(String str) {
        return this.f6559c != null ? this.f6559c.a(str) : str;
    }

    public q a() {
        return this.f6560f;
    }

    public void a(q qVar) {
        this.f6560f = qVar;
        if (qVar != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(qVar.a());
            for (String str : qVar.b()) {
                if (!a(arrayList, str)) {
                    r rVarB = qVar.b(str);
                    if (rVarB == null) {
                        a aVar = new a();
                        aVar.a(str);
                        aVar.a(Double.NaN);
                        aVar.b(Double.NaN);
                        aVar.a(-1);
                        rVarB = aVar;
                    }
                    arrayList.add(rVarB);
                }
            }
            Collections.sort(arrayList, new o(this));
            this.f6557a.a(arrayList);
            this.f6557a.a();
        }
        this.f6558b.a(qVar.b());
        this.f6558b.f();
    }

    public void a(Window window) {
        JDialog jDialogB = bV.b(this, window, a("Field Min & Max Editor"), this);
        jDialogB.pack();
        bV.a((Component) window, (Component) jDialogB);
        jDialogB.setVisible(true);
    }

    private boolean a(List list, String str) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((r) it.next()).e().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean c() {
        r rVarE = this.f6558b.e();
        if (rVarE == null || rVarE.e().trim().isEmpty()) {
            return false;
        }
        this.f6558b.d();
        String[] strArrA = this.f6558b.a();
        if (strArrA.length <= 1) {
            this.f6557a.a(rVarE);
            this.f6560f.a(rVarE);
            return true;
        }
        for (String str : strArrA) {
            r rVarA = this.f6557a.a(str);
            if (rVarA != null) {
                rVarA.b(rVarE.b());
                rVarA.a(rVarE.a());
                rVarA.a(rVarE.f());
                this.f6560f.a(rVarA);
            }
        }
        this.f6557a.b();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        if (this.f6558b.c() && bV.a(a("The Field limit currently being edited has not been saved.") + "\n" + a("Would you like to save it now?"), (Component) this.f6558b, true)) {
            if (!c()) {
                if (this.f6563g >= 0) {
                    this.f6557a.getSelectionModel().setSelectionInterval(i2, i2);
                    return;
                }
                return;
            }
            this.f6558b.f();
        }
        if (i2 < 0) {
            this.f6558b.f();
            this.f6558b.setEnabled(false);
            this.f6561d.setEnabled(false);
            return;
        }
        this.f6563g = i2;
        r rVarA = this.f6557a.a(i2);
        a().b(rVarA);
        this.f6558b.a(rVarA.e(), rVarA.a(), rVarA.b(), rVarA.f());
        this.f6558b.setEnabled(true);
        this.f6561d.setEnabled(true);
        this.f6562e.setEnabled(true);
        if (this.f6557a.getSelectedRowCount() > 1) {
            int[] selectedRows = this.f6557a.getSelectedRows();
            String str = "";
            int i3 = 0;
            while (i3 < selectedRows.length) {
                str = i3 == 0 ? str + this.f6557a.a(selectedRows[i3]).e() : str + ", " + this.f6557a.a(selectedRows[i3]).e();
                i3++;
            }
            this.f6558b.a(str);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        if (this.f6558b.c() && bV.a(a("The Field limit currently being edited has not been saved.") + "\n" + a("Would you like to save it now?"), (Component) this.f6558b, true)) {
            c();
        }
    }
}
