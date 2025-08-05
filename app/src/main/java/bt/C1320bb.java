package bt;

import G.C0072be;
import G.C0076bi;
import G.C0126i;
import G.cZ;
import bH.C1007o;
import c.InterfaceC1385d;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.InterfaceC1662et;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: bt.bb, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/bb.class */
public class C1320bb extends JPanel implements InterfaceC1349h, InterfaceC1357p, InterfaceC1385d, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f8992a;

    /* renamed from: b, reason: collision with root package name */
    C0076bi f8993b;

    /* renamed from: c, reason: collision with root package name */
    C0072be f8994c;

    /* renamed from: d, reason: collision with root package name */
    C1337bs f8995d;

    /* renamed from: e, reason: collision with root package name */
    U f8996e;

    /* renamed from: f, reason: collision with root package name */
    JPanel f8997f;

    /* renamed from: g, reason: collision with root package name */
    bM f8998g;

    /* renamed from: k, reason: collision with root package name */
    private String f8999k;

    /* renamed from: h, reason: collision with root package name */
    JCheckBox f9000h;

    /* renamed from: i, reason: collision with root package name */
    JLabel f9001i;

    /* renamed from: j, reason: collision with root package name */
    InterfaceC1662et f9002j;

    /* renamed from: l, reason: collision with root package name */
    private bN f9003l;

    /* renamed from: m, reason: collision with root package name */
    private static String f9004m = "checked3D";

    public C1320bb(G.R r2, C0076bi c0076bi, C0072be c0072be) {
        this(r2, c0076bi, c0072be, new C1350i("Multiview_" + c0072be.aJ()));
    }

    public C1320bb(G.R r2, C0076bi c0076bi, C0072be c0072be, InterfaceC1662et interfaceC1662et) throws IllegalArgumentException {
        String string;
        this.f8992a = null;
        this.f8993b = null;
        this.f8994c = null;
        this.f8995d = null;
        this.f8996e = null;
        this.f8997f = new JPanel();
        this.f8998g = null;
        this.f8999k = null;
        this.f9001i = new JLabel("", 2);
        this.f9002j = null;
        this.f9003l = null;
        this.f8992a = r2;
        this.f8993b = c0076bi;
        this.f8994c = c0072be;
        this.f8999k = c0072be.aH();
        setLayout(new BorderLayout());
        this.f9002j = interfaceC1662et;
        boolean zEquals = a(f9004m, "false").equals("true");
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f9000h = new JCheckBox(C1818g.b("3D View"));
        this.f9000h.setFocusable(false);
        this.f9000h.setSelected(zEquals);
        this.f9000h.setMnemonic('D');
        this.f9000h.addActionListener(new C1321bc(this));
        jPanel.add("East", this.f9000h);
        cZ cZVarN = c0072be.N();
        if (cZVarN != null) {
            try {
                string = C1818g.b(cZVarN.a());
            } catch (V.g e2) {
                string = cZVarN.toString();
            }
            this.f9001i.setText(string);
            jPanel.add("West", this.f9001i);
        }
        try {
            C0126i.a(r2.c(), cZVarN, new C1322bd(this, cZVarN));
        } catch (V.a e3) {
            com.efiAnalytics.ui.bV.d("Invalid Title Defined: " + cZVarN.toString() + "\nError: " + e3.getLocalizedMessage(), this);
        }
        add("North", jPanel);
        add(BorderLayout.CENTER, this.f8997f);
        a(this.f9000h.isSelected());
        addFocusListener(new C1323be(this));
        bV bVVar = new bV(r2, c0072be, e().f9061f, this);
        r2.C().a(bVVar);
        this.f9003l = bVVar;
        if (this.f8995d != null) {
            this.f8995d.e();
            this.f8995d.a(this.f9003l);
        }
        if (this.f8996e != null) {
            this.f8996e.d();
            this.f8996e.a(this.f9003l);
        }
    }

    private String a(String str, String str2) {
        String strA = null;
        if (this.f9002j != null) {
            strA = this.f9002j.a(str);
        }
        return strA == null ? str2 : strA;
    }

    public void d() {
        if (this.f8996e != null) {
            this.f8996e.requestFocusInWindow();
        } else if (this.f8995d != null) {
            this.f8995d.requestFocusInWindow();
        }
    }

    public void a(boolean z2) {
        if (z2) {
            this.f8996e = f();
            a(this.f8996e);
            this.f8998g = new bM(this, this.f8996e.b());
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8998g);
        } else {
            if (this.f8994c == null) {
                com.efiAnalytics.ui.bV.d(this.f8993b.a() + " 2D Table not found in configuration.", this);
                return;
            }
            if (this.f8998g != null) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8998g);
                this.f8998g = null;
            }
            this.f8995d = e();
            a(this.f8995d);
        }
        d();
    }

    private void a(C1337bs c1337bs) {
        this.f8997f.removeAll();
        this.f8997f.setLayout(new GridLayout());
        this.f8997f.add(c1337bs);
        this.f8997f.repaint();
        validate();
    }

    private void a(U u2) {
        this.f8997f.removeAll();
        this.f8997f.setLayout(new BorderLayout());
        this.f8997f.add(BorderLayout.CENTER, u2);
        this.f8997f.repaint();
        validate();
    }

    public C1337bs e() {
        if (this.f8995d == null) {
            this.f8995d = new C1337bs(this.f8992a, this.f8994c);
            if (this.f9003l != null) {
                this.f8995d.e();
            }
        }
        return this.f8995d;
    }

    public U f() {
        if (this.f8996e == null) {
            this.f8996e = new U(this.f8992a, this.f8993b, false);
            if (this.f9003l != null) {
                this.f8996e.d();
                this.f8996e.a(this.f9003l);
            }
        }
        return this.f8996e;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        Object[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (components[i2] instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) components[i2]).close();
            }
        }
        if (this.f9003l != null) {
            this.f9003l.b();
            if (this.f9003l instanceof G.aG) {
                this.f8992a.C().b((G.aG) this.f9003l);
            }
        }
        if (this.f8998g != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8998g);
            this.f8998g = null;
        }
        if (this.f8995d != null) {
            this.f8995d.close();
            this.f8995d = null;
        }
        if (this.f8996e != null) {
            this.f8996e.close();
            this.f8996e = null;
        }
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (a_() != null) {
            try {
                setEnabled(C1007o.a(a_(), this.f8992a));
            } catch (Exception e2) {
                bH.C.a(e2.getMessage());
                e2.printStackTrace();
            }
        }
    }

    @Override // c.InterfaceC1385d
    public String a_() {
        return this.f8999k;
    }

    @Override // c.InterfaceC1385d
    public void b_(String str) {
        this.f8999k = str;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        if (this.f8995d != null) {
            this.f8995d.setEnabled(z2);
        }
        if (this.f8996e != null) {
            this.f8996e.setEnabled(z2);
        }
        this.f9000h.setEnabled(z2);
    }

    public void a(bN bNVar) {
        if (this.f9003l != null) {
            this.f9003l.b();
            if (this.f9003l instanceof G.aG) {
                this.f8992a.C().b((G.aG) this.f9003l);
            }
        }
        this.f9003l = bNVar;
        if (this.f8995d != null) {
            this.f8995d.a(bNVar);
            bNVar.a();
        }
        if (this.f8996e != null) {
            this.f8996e.a(bNVar);
            bNVar.a();
        }
    }

    @Override // bt.InterfaceC1357p
    public void a(double d2, double d3) throws NumberFormatException {
        if (this.f9000h.isSelected()) {
            this.f8996e.a(d2, d3);
        } else {
            this.f8995d.a(d2, d3);
        }
    }

    @Override // bt.InterfaceC1357p
    public int c() {
        if (this.f9000h.isSelected()) {
            return 1;
        }
        return this.f8995d.f();
    }

    @Override // c.InterfaceC1385d
    public G.R b_() {
        return this.f8992a;
    }

    public C0072be g() {
        return this.f8994c;
    }

    @Override // bt.InterfaceC1357p
    public void a(float[] fArr, float[] fArr2) {
        this.f8995d.a(fArr, fArr2);
    }
}
