package bt;

import G.C0077bj;
import G.C0083bp;
import G.C0094c;
import G.InterfaceC0109co;
import G.cY;
import bH.C1007o;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import s.C1818g;

/* renamed from: bt.P, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/P.class */
public class C1288P extends JPanel implements InterfaceC0109co, InterfaceC1349h, InterfaceC1565bc {

    /* renamed from: c, reason: collision with root package name */
    Color f8686c;

    /* renamed from: g, reason: collision with root package name */
    G.R f8690g;

    /* renamed from: h, reason: collision with root package name */
    C0077bj f8694h;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f8684a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f8685b = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    Color f8687d = UIManager.getColor("TextField.foreground");

    /* renamed from: e, reason: collision with root package name */
    Color f8688e = Color.RED;

    /* renamed from: f, reason: collision with root package name */
    Color f8689f = Color.GREEN;

    /* renamed from: i, reason: collision with root package name */
    private int f8691i = -1;

    /* renamed from: j, reason: collision with root package name */
    private int f8692j = -1;

    /* renamed from: k, reason: collision with root package name */
    private ArrayList f8693k = new ArrayList();

    public C1288P(G.R r2, C0077bj c0077bj) {
        this.f8686c = UIManager.getColor("TextField.background");
        this.f8694h = null;
        this.f8694h = c0077bj;
        this.f8690g = r2;
        String strM = c0077bj.M() != null ? c0077bj.M() : "";
        String strA = C1818g.a(strM, strM);
        if (strA != null && !strA.equals("")) {
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0), strA));
        } else if (strA != null && strA.equals(".")) {
            setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(0)));
        }
        G.aR aRVarA = G.aR.a();
        G.aM aMVarC = r2.c(c0077bj.b());
        if (aMVarC == null || !c0077bj.h()) {
            setLayout(new BoxLayout(this, 1));
        } else {
            setLayout(new GridLayout(1, 0));
        }
        for (int i2 = 0; i2 < aMVarC.b(); i2++) {
            C0083bp c0083bp = new C0083bp();
            c0083bp.a(i2);
            c0083bp.u(c0077bj.c());
            c0083bp.a(c0077bj.b());
            String strA2 = c0077bj.a();
            if (strA2 != null) {
                String strB = bH.W.b(C1818g.b(strA2), "%INDEX%", (1 + i2) + "");
                try {
                    c0083bp.b(cY.a().a(r2, strB));
                } catch (V.g e2) {
                    c0083bp.e(strB);
                    Logger.getLogger(C1324bf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            } else if (c0077bj.d() != null && c0077bj.d().length > i2) {
                c0083bp.b(new C0094c(c0077bj.d()[i2] + bH.S.a() + " "));
            }
            if (c0077bj.f() == null) {
                this.f8686c = UIManager.getColor("TextField.background");
            } else {
                this.f8686c = new Color(c0077bj.f().b(), c0077bj.f().c(), c0077bj.f().d());
            }
            aT aTVar = new aT(r2, c0083bp);
            aTVar.setBackground(this.f8686c);
            aTVar.a(new C1289Q(this, i2));
            if (c0077bj.h()) {
                aTVar.b(10);
            }
            add(aTVar);
            this.f8685b.add(aTVar);
            if (c0083bp.b() != null && r2.c(c0083bp.b()) != null) {
                this.f8684a.add(c0083bp.b());
                try {
                    aRVarA.a(r2.c(), c0083bp.b(), aTVar);
                } catch (V.a e3) {
                    e3.printStackTrace();
                    bH.C.a("Error subscribing to ParameterValue Changes. Parameter:" + c0083bp.b(), e3, this);
                }
            }
        }
        Iterator itG = c0077bj.g();
        while (itG.hasNext()) {
            a((G.bU) itG.next());
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        Iterator it = this.f8685b.iterator();
        while (it.hasNext()) {
            aT aTVar = (aT) it.next();
            for (FocusListener focusListener : aTVar.getFocusListeners()) {
                aTVar.removeFocusListener(focusListener);
            }
        }
        Object[] components = getComponents();
        for (int i2 = 0; i2 < getComponentCount(); i2++) {
            if (components[i2] instanceof InterfaceC1565bc) {
                ((InterfaceC1565bc) components[i2]).close();
            }
        }
    }

    @Override // G.InterfaceC0109co
    public void setCurrentOutputChannelValue(String str, double d2) {
    }

    private void c() {
        for (int i2 = 0; i2 < this.f8685b.size(); i2++) {
            if (i2 == this.f8692j && this.f8686c.equals(this.f8689f)) {
                ((aT) this.f8685b.get(i2)).setBackground(Color.YELLOW);
                ((aT) this.f8685b.get(i2)).setForeground(Color.BLACK);
            } else if (i2 == this.f8692j && i2 == this.f8691i) {
                ((aT) this.f8685b.get(i2)).setBackground(a(this.f8689f, this.f8688e));
                ((aT) this.f8685b.get(i2)).setForeground(Color.BLACK);
            } else if (i2 == this.f8692j) {
                ((aT) this.f8685b.get(i2)).setBackground(this.f8689f);
                ((aT) this.f8685b.get(i2)).setForeground(Color.BLACK);
            } else if (i2 == this.f8691i && this.f8686c.equals(this.f8688e)) {
                ((aT) this.f8685b.get(i2)).setBackground(Color.MAGENTA);
                ((aT) this.f8685b.get(i2)).setForeground(Color.BLACK);
            } else if (i2 == this.f8691i) {
                ((aT) this.f8685b.get(i2)).setBackground(this.f8688e);
                ((aT) this.f8685b.get(i2)).setForeground(Color.BLACK);
            } else {
                Color color = this.f8686c;
                ((aT) this.f8685b.get(i2)).setBackground(color);
                ((aT) this.f8685b.get(i2)).setForeground((color.getRed() + color.getBlue()) + color.getGreen() > 250 ? Color.BLACK : Color.WHITE);
            }
        }
    }

    private Color a(Color color, Color color2) {
        int red = (2 * (color.getRed() + color2.getRed())) / 3;
        int green = (2 * (color.getGreen() + color2.getGreen())) / 3;
        int blue = (2 * (color.getBlue() + color2.getBlue())) / 3;
        return new Color(red <= 255 ? red : 255, green <= 255 ? green : 255, blue <= 255 ? blue : 255);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(int i2) {
        d(i2);
    }

    public void a(G.bU bUVar) {
        this.f8693k.add(bUVar);
    }

    private void d(int i2) {
        Iterator it = this.f8693k.iterator();
        while (it.hasNext()) {
            try {
                ((G.bU) it.next()).a(i2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void a(int i2) {
        this.f8692j = i2;
        c();
    }

    public void b(int i2) {
        this.f8691i = i2;
        c();
    }

    public JComponent b() {
        if (this.f8691i < 0 || this.f8691i >= getComponentCount()) {
            return null;
        }
        return (JComponent) getComponent(this.f8691i);
    }

    @Override // bt.InterfaceC1349h
    public void a() {
        if (this.f8694h != null) {
            try {
                C1685fp.a((Component) this, C1007o.a(this.f8694h.c(), this.f8690g));
            } catch (Exception e2) {
                bH.C.a(e2.getMessage());
            }
        }
    }
}
