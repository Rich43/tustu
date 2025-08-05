package bw;

import ax.Q;
import ax.U;
import bH.ab;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.apache.commons.net.nntp.NNTPReply;

/* loaded from: TunerStudioMS.jar:bw/f.class */
public class f extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    final DefaultListModel f9167a = new DefaultListModel();

    /* renamed from: b, reason: collision with root package name */
    JList f9168b = new JList(this.f9167a);

    /* renamed from: c, reason: collision with root package name */
    Cdo f9169c = new Cdo();

    /* renamed from: d, reason: collision with root package name */
    Cdo f9170d = new Cdo();

    /* renamed from: e, reason: collision with root package name */
    JLabel f9171e = new JLabel("", 0);

    /* renamed from: f, reason: collision with root package name */
    JLabel f9172f = new JLabel("", 0);

    /* renamed from: g, reason: collision with root package name */
    List f9173g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    i f9174h = null;

    /* renamed from: i, reason: collision with root package name */
    HashMap f9175i = new HashMap();

    /* renamed from: j, reason: collision with root package name */
    j f9176j = new j(this);

    public f(Window window) {
        a();
        c();
    }

    private void a() {
        setBorder(BorderFactory.createTitledBorder(ab.a().a("Conversion Calculator")));
        setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        add("West", new JScrollPane(this.f9168b));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(1));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(2, 3));
        jPanel2.add(this.f9171e);
        jPanel2.add(new JLabel(""));
        jPanel2.add(this.f9172f);
        jPanel2.add(this.f9169c);
        jPanel2.add(new JLabel("-->>", 0));
        jPanel2.add(this.f9170d);
        jPanel.add(jPanel2);
        add(BorderLayout.CENTER, jPanel);
        this.f9170d.setEditable(false);
        this.f9169c.addKeyListener(new g(this));
        this.f9169c.addFocusListener(this.f9176j);
        this.f9168b.addListSelectionListener(new h(this));
        setPreferredSize(eJ.a(NNTPReply.AUTHENTICATION_REQUIRED, 240));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public i a(Object obj) {
        if (obj == null) {
            return null;
        }
        for (i iVar : this.f9173g) {
            if (iVar.f9179a.equals(obj)) {
                return iVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        if (this.f9174h != null) {
            String str = this.f9174h.f9182d;
            Q q2 = (Q) this.f9175i.get(str);
            if (q2 == null) {
                q2 = new Q();
                try {
                    q2.a(str);
                    this.f9175i.put(str, q2);
                } catch (U e2) {
                    Logger.getLogger(f.class.getName()).log(Level.WARNING, "Unable to parser expression", (Throwable) e2);
                    bV.d(a("Unable to parse expression") + ": " + str, this);
                }
            }
            try {
                q2.a("inputVar", this.f9169c.e());
                this.f9170d.a(q2.d());
            } catch (U e3) {
                Logger.getLogger(f.class.getName()).log(Level.WARNING, "Unable to evaluate expression", (Throwable) e3);
                bV.d(a("Unable to evaluate expression") + ": " + str, this);
            }
        }
    }

    private void c() {
        this.f9173g.add(new i(this, "Degrees F to Degrees C", "°F", "°C", "(inputVar - 32) * 5 / 9", 1));
        this.f9173g.add(new i(this, "Degrees C to Degrees F", "°C", "°F", "(inputVar * 9 / 5) + 32", 1));
        this.f9173g.add(new i(this, "Cubic Inches to cc", "CID", "cc", "inputVar * 16.3871", 0));
        this.f9173g.add(new i(this, "Cubic Inches to Liters", "CID", "L", "inputVar * 0.0163871", 2));
        this.f9173g.add(new i(this, "Liters to Cubic Inches", "L", "CID", "inputVar / 0.0163871", 1));
        this.f9173g.add(new i(this, "cc to Cubic Inches", "cc", "CID", "inputVar / 16.3871", 1));
        this.f9173g.add(new i(this, "kPa to PSI", "kPa", "PSI", "inputVar * 0.145038", 2));
        this.f9173g.add(new i(this, "kPa to inHg", "kPa", "inHg", "inputVar * 0.2953", 2));
        this.f9173g.add(new i(this, "inHg to kPa", "inHg", "kPa", "inputVar / 0.2953", 1));
        this.f9173g.add(new i(this, "PSI to kPa", "PSI", "kPa", "inputVar / 0.145038", 1));
        this.f9173g.add(new i(this, "MPH to KPH", "MPH", "KPH", "inputVar * 1.60934", 1));
        this.f9173g.add(new i(this, "MPH to ft/sec", "MPH", "ft/sec", "inputVar * 1.46667", 2));
        this.f9173g.add(new i(this, "MPH to m/s", "MPH", "m/s", "inputVar * 0.44704", 3));
        this.f9173g.add(new i(this, "KPH to MPH", "KPH", "MPH", "inputVar / 1.60934", 1));
        this.f9173g.add(new i(this, "KPH to m/s", "KPH", "MPH", "inputVar / 0.277778", 1));
        this.f9173g.add(new i(this, "lb/hr to cc/min", "lb/hr", "cc/min", "inputVar * 10.5", 1));
        this.f9173g.add(new i(this, "cc/min to lb/hr", "cc/min", "lb/hr", "inputVar / 10.5", 1));
        this.f9173g.add(new i(this, "1/4 mile ET to 1/8 mile ET", "s.", "s.", "inputVar / 1.55", 2));
        this.f9173g.add(new i(this, "1/8 mile ET to 1/4 mile ET", "s.", "s.", "inputVar * 1.55", 2));
        Iterator it = this.f9173g.iterator();
        while (it.hasNext()) {
            this.f9167a.addElement(((i) it.next()).f9179a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return ab.a().a(str);
    }
}
