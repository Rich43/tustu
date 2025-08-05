package bg;

import G.C0073bf;
import G.R;
import G.T;
import G.bO;
import W.C0178d;
import aP.C0338f;
import aa.C0480c;
import aa.C0483f;
import bH.C;
import bH.C1011s;
import bH.W;
import com.efiAnalytics.apps.ts.tuningViews.C1429b;
import com.efiAnalytics.apps.ts.tuningViews.F;
import com.efiAnalytics.apps.ts.tuningViews.G;
import com.efiAnalytics.ui.C1609ct;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.apache.commons.net.nntp.NNTPReply;
import r.C1807j;
import s.C1818g;
import v.C1887g;

/* renamed from: bg.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bg/a.class */
public class C1121a extends JPanel implements InterfaceC1140t {

    /* renamed from: a, reason: collision with root package name */
    R f8059a;

    /* renamed from: b, reason: collision with root package name */
    C1135o f8060b;

    /* renamed from: c, reason: collision with root package name */
    C1134n f8061c;

    /* renamed from: d, reason: collision with root package name */
    C1131k f8062d = new C1131k(this);

    /* renamed from: e, reason: collision with root package name */
    Map f8063e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    JDialog f8064f = null;

    /* renamed from: g, reason: collision with root package name */
    JButton f8065g;

    /* renamed from: h, reason: collision with root package name */
    JButton f8066h;

    /* renamed from: i, reason: collision with root package name */
    JButton f8067i;

    /* renamed from: j, reason: collision with root package name */
    JButton f8068j;

    /* renamed from: k, reason: collision with root package name */
    JButton f8069k;

    /* renamed from: l, reason: collision with root package name */
    JButton f8070l;

    public C1121a(R r2) {
        this.f8059a = r2;
        b();
        setCursor(Cursor.getPredefinedCursor(3));
        SwingUtilities.invokeLater(new RunnableC1122b(this));
    }

    private void b() {
        setLayout(new BorderLayout());
        this.f8060b = new C1135o();
        this.f8060b.a(this);
        JScrollPane jScrollPane = new JScrollPane(this.f8060b);
        jScrollPane.setPreferredSize(eJ.a(320, 200));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, jScrollPane);
        this.f8061c = new C1134n();
        jPanel.add("North", c());
        try {
            JPanel jPanel2 = new JPanel();
            Dimension dimensionA = eJ.a(28, 28);
            jPanel2.setLayout(new BoxLayout(jPanel2, 1));
            this.f8069k = new JButton(new ImageIcon(cO.a().a(cO.f11141ae, this, 24)));
            this.f8069k.setPreferredSize(dimensionA);
            jPanel2.add(this.f8069k);
            this.f8069k.addActionListener(new C1123c(this));
            this.f8070l = new JButton(new ImageIcon(cO.a().a(cO.f11142af, this, 24)));
            this.f8070l.setPreferredSize(dimensionA);
            jPanel2.add(this.f8070l);
            this.f8070l.addActionListener(new C1124d(this));
            jPanel.add("East", jPanel2);
        } catch (Exception e2) {
            C.a("Failed to create up/down buttons.");
            C.a(e2);
        }
        add(BorderLayout.CENTER, jPanel);
        this.f8062d.setMinimumSize(eJ.a(640, 320));
        this.f8062d.setPreferredSize(eJ.a(640, 320));
        this.f8062d.setBorder(BorderFactory.createLoweredBevelBorder());
        add("South", this.f8062d);
    }

    private JPanel c() {
        int iA = eJ.a(30);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, eJ.a(2), eJ.a(2)));
        try {
            this.f8065g = new JButton(new ImageIcon(cO.a().a(cO.f11116F, this)));
            this.f8065g.setToolTipText(C1818g.b("Create New Component"));
            this.f8065g.setPreferredSize(new Dimension(iA, iA));
            this.f8065g.addActionListener(new C1125e(this));
            jPanel.add(this.f8065g);
            this.f8066h = new JButton(new ImageIcon(cO.a().a(cO.f11103s, this)));
            this.f8066h.setToolTipText(C1818g.b("Edit Selected Component"));
            this.f8066h.setPreferredSize(new Dimension(iA, iA));
            this.f8066h.addActionListener(new C1126f(this));
            jPanel.add(this.f8066h);
            this.f8067i = new JButton(new ImageIcon(cO.a().a(cO.f11089e, this)));
            this.f8067i.setToolTipText(C1818g.b("Delete Selected Component"));
            this.f8067i.setPreferredSize(new Dimension(iA, iA));
            this.f8067i.addActionListener(new C1127g(this));
            jPanel.add(this.f8067i);
            jPanel.add(new JLabel());
            this.f8068j = new JButton(new ImageIcon(cO.a().a(cO.f11123M, this)));
            this.f8068j.setToolTipText(C1818g.b("Save Configuration"));
            this.f8068j.setPreferredSize(new Dimension(iA, iA));
            this.f8068j.addActionListener(new C1128h(this));
            jPanel.add(this.f8068j);
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this);
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(jPanel, "West");
        return jPanel2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        for (C0073bf c0073bf : this.f8059a.af()) {
            try {
                this.f8063e.put(c0073bf.aJ(), new C1887g().a(new InputStreamReader(new ByteArrayInputStream(c0073bf.d().a()))));
                this.f8060b.a(c0073bf);
            } catch (V.a e2) {
                Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to load Tuning View", (Throwable) e2);
                bV.d("Failed to Load Tuning View:\n" + e2.getLocalizedMessage(), this);
            } catch (IOException e3) {
                Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to load Tuning View", (Throwable) e3);
                bV.d("Failed to Load Tuning View:\n" + e3.getLocalizedMessage(), this);
            }
        }
    }

    public void a(F f2) {
        String str;
        int i2 = -1;
        do {
            int i3 = i2;
            i2++;
            str = i3 < 0 ? "tv" + W.b(f2.b(), " ", "") : "tv" + W.b(f2.b(), " ", "") + "_" + i2;
        } while (!a(str));
        C0073bf c0073bf = new C0073bf();
        c0073bf.v(str);
        c0073bf.u(f2.e());
        c0073bf.a(this.f8063e.size());
        try {
            String strA = C1887g.a(f2);
            c0073bf.c(f2.b());
            c0073bf.a(strA);
            c0073bf.b();
            this.f8063e.put(c0073bf.aJ(), f2);
            this.f8060b.a(c0073bf);
        } catch (V.a e2) {
            C.a("Error turning Tune View to String.\n" + e2.getLocalizedMessage(), e2, this);
        } catch (IOException e3) {
            C.a("Error turning Tune View to String.", e3, this);
        }
    }

    private boolean a(String str) {
        Iterator it = this.f8063e.keySet().iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(str)) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        this.f8060b.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        this.f8060b.c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        C0073bf c0073bfA = this.f8060b.a();
        if (c0073bfA != null) {
            this.f8060b.b(c0073bfA);
            this.f8063e.remove(c0073bfA.aJ());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void j() {
        this.f8059a.af().clear();
        try {
            for (C0073bf c0073bf : this.f8060b.f8090b) {
                bO bOVarD = c0073bf.d();
                bOVarD.v(c0073bf.aJ());
                F f2 = (F) this.f8063e.get(bOVarD.aJ());
                if (f2 != null) {
                    f2.b(c0073bf.c());
                } else {
                    C.a("Cannot update TuningView attributes, no instance loaded!");
                }
                try {
                    c0073bf.a(C1887g.a(f2));
                } catch (V.a e2) {
                    Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to update TuningView in EcuTuningView", (Throwable) e2);
                }
                this.f8059a.a(bOVarD);
                this.f8059a.a(c0073bf);
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(new C0483f());
            arrayList.add(new C0480c());
            C0338f.a().a(this.f8059a, arrayList);
        } catch (IOException e3) {
            bV.d("Failed to get Encoded Data for Tuning View.\n" + e3.getLocalizedMessage(), this);
        }
    }

    @Override // bg.InterfaceC1140t
    public void a(C0073bf c0073bf) {
        if (c0073bf != null) {
            F fB = (F) this.f8063e.get(c0073bf.aJ());
            if (fB == null) {
                try {
                    fB = b(c0073bf);
                } catch (V.a e2) {
                    bV.d("Failed to load TuneView frm EcuTuningView, see log for more detail.", this);
                    Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to load TuneView frm EcuTuningView", (Throwable) e2);
                    return;
                } catch (IOException e3) {
                    bV.d("Failed to load TuneView frm EcuTuningView, see log for more detail.", this);
                    Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to load TuneView frm EcuTuningView", (Throwable) e3);
                    return;
                }
            }
            try {
                this.f8062d.a(fB.d());
            } catch (IOException e4) {
                Logger.getLogger(C1121a.class.getName()).log(Level.SEVERE, "Failed to get Preview Image File.", (Throwable) e4);
            }
        } else {
            this.f8062d.a();
        }
        k();
    }

    private void k() {
        int iD = this.f8060b.d();
        this.f8069k.setEnabled(iD > 0 && iD < this.f8063e.size());
        this.f8070l.setEnabled(iD >= 0 && iD < this.f8063e.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void l() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean m() {
        return true;
    }

    private F b(C0073bf c0073bf) throws IOException {
        byte[] bArrA = c0073bf.d().a();
        File file = new File(C1807j.H(), C1011s.a(bArrA));
        file.delete();
        C0178d.a(file, bArrA);
        return new C1887g().a(file);
    }

    public void a(Component component) {
        this.f8064f = new JDialog(bV.a(component), C1818g.b("Ini Based Tuning View Editor"));
        this.f8064f.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C1129i(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new C1130j(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f8064f.add("South", jPanel);
        this.f8064f.pack();
        bV.a((Window) bV.a(component), (Component) this.f8064f);
        this.f8064f.setVisible(true);
        validate();
        this.f8064f.pack();
        this.f8064f.setResizable(false);
    }

    public void a() {
        C1429b c1429b = new C1429b();
        String[] strArrD = T.a().d();
        String[] strArr = new String[strArrD.length];
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            R rC = T.a().c(strArrD[i2]);
            arrayList.add(rC);
            strArr[i2] = rC.i();
        }
        C1132l c1132l = new C1132l(this, c1429b, strArr);
        try {
            c1429b.a(G.a(arrayList));
        } catch (V.a e2) {
            bV.d("Failed to load Tuning Views:\n" + e2.getLocalizedMessage(), this);
        }
        Window windowB = bV.b(this);
        Window window = windowB;
        if (!(window instanceof Dialog) && !(window instanceof Frame)) {
            window = null;
        }
        C1609ct c1609ct = new C1609ct(window, c1429b, "Select Tuning View", c1132l, 7);
        if (c1132l != null) {
            c1609ct.a(c1132l);
        }
        c1609ct.setSize(640, NNTPReply.AUTHENTICATION_REQUIRED);
        bV.a((Component) windowB, (Component) c1609ct);
        c1609ct.setVisible(true);
    }
}
