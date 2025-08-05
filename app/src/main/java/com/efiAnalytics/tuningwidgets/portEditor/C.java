package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.aH;
import G.aM;
import G.aN;
import G.aR;
import bH.C0998f;
import bH.W;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.JComboBox;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/C.class */
public class C extends JComboBox implements F, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f10514a;

    /* renamed from: b, reason: collision with root package name */
    aM f10516b;

    /* renamed from: c, reason: collision with root package name */
    aN f10517c;

    /* renamed from: m, reason: collision with root package name */
    private aM f10518m;

    /* renamed from: n, reason: collision with root package name */
    private aM f10519n;

    /* renamed from: i, reason: collision with root package name */
    boolean f10525i;

    /* renamed from: j, reason: collision with root package name */
    boolean f10526j;

    /* renamed from: l, reason: collision with root package name */
    private R f10515l = null;

    /* renamed from: d, reason: collision with root package name */
    ArrayList f10520d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    C0998f f10521e = new C0998f();

    /* renamed from: f, reason: collision with root package name */
    int f10522f = -1;

    /* renamed from: g, reason: collision with root package name */
    int f10523g = -1;

    /* renamed from: h, reason: collision with root package name */
    boolean f10524h = false;

    /* renamed from: k, reason: collision with root package name */
    boolean f10527k = false;

    public C(R r2, String str, String str2, String str3, boolean z2, boolean z3) {
        this.f10514a = null;
        this.f10516b = null;
        this.f10517c = null;
        this.f10518m = null;
        this.f10519n = null;
        this.f10525i = false;
        this.f10526j = false;
        this.f10514a = r2;
        this.f10525i = z2;
        this.f10526j = z3;
        this.f10516b = this.f10514a.c(str);
        this.f10518m = this.f10514a.c(str2);
        if (str3 != null && !str3.isEmpty()) {
            this.f10519n = this.f10514a.c(str3);
        }
        a(this.f10514a);
        addActionListener(new D(this));
        this.f10517c = new E(this);
        try {
            aR.a().a(this.f10514a.c(), this.f10516b.aJ(), this.f10517c);
        } catch (V.a e2) {
            bH.C.a("Failed to subscribe " + this.f10516b.aJ() + " for update notification.", e2, this);
        }
    }

    protected void a(String str) {
        if (str == null || this.f10515l == null || str.length() <= 0 || this.f10524h) {
            return;
        }
        aH aHVarG = this.f10515l.g(str);
        try {
            double[][] dArrI = this.f10516b.i(this.f10514a.h());
            if (!this.f10527k) {
                dArrI[this.f10523g][this.f10522f] = aHVarG.a();
            } else if (this.f10516b.e() >= 4) {
                dArrI[this.f10523g][this.f10522f] = aHVarG.x();
            } else {
                dArrI[this.f10523g][this.f10522f] = aHVarG.x() - this.f10515l.O().af();
            }
            this.f10516b.a(this.f10514a.h(), dArrI);
            if (this.f10519n != null) {
                this.f10521e.a();
                this.f10521e.a(aHVarG.aJ().getBytes());
                double[][] dArrI2 = this.f10519n.i(this.f10514a.h());
                dArrI2[this.f10523g][this.f10522f] = this.f10521e.b();
                this.f10519n.a(this.f10514a.h(), dArrI2);
            }
            double[][] dArrI3 = this.f10518m.i(this.f10514a.h());
            dArrI3[this.f10523g][this.f10522f] = aHVarG.l();
            if (this.f10525i) {
                if (aHVarG.t()) {
                    dArrI3[this.f10523g][this.f10522f] = ((int) dArrI3[this.f10523g][this.f10522f]) | 64;
                }
                if (!aHVarG.p()) {
                    dArrI3[this.f10523g][this.f10522f] = ((int) dArrI3[this.f10523g][this.f10522f]) | 128;
                }
            }
            try {
                this.f10518m.a(this.f10514a.h(), dArrI3);
            } catch (V.j e2) {
                bV.d("There is an error in your ini not allowing size to be set properly.", this);
            }
            a(aHVarG);
        } catch (V.g e3) {
            bH.C.a("Error updating:" + aHVarG.aJ() + " offset to " + this.f10516b.aJ());
        } catch (V.j e4) {
            bH.C.a("Value Out of Bounds updating:" + aHVarG.aJ() + " offset to " + this.f10516b.aJ());
            Logger.getLogger(C.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
        } catch (Exception e5) {
            bH.C.a("Unkown Error updating:" + (aHVarG == null ? FXMLLoader.NULL_KEYWORD : aHVarG.aJ()) + " offset to " + (this.f10516b == null ? FXMLLoader.NULL_KEYWORD : this.f10516b.aJ()));
            e5.printStackTrace();
        }
    }

    public String a() {
        return (String) getSelectedItem();
    }

    public void a(int i2) {
        this.f10523g = i2;
    }

    public void b(int i2) {
        this.f10522f = i2;
        b();
    }

    public void b() {
        if (this.f10523g < 0 || this.f10522f < 0) {
            return;
        }
        try {
            double d2 = this.f10516b.i(this.f10514a.h())[this.f10523g][this.f10522f];
            boolean z2 = false;
            int i2 = 0;
            while (true) {
                if (i2 >= getItemCount()) {
                    break;
                }
                aH aHVarG = this.f10515l.g((String) getItemAt(i2));
                if (!this.f10527k) {
                    if (aHVarG != null && aHVarG.a() == d2) {
                        setSelectedItem(getItemAt(i2));
                        z2 = true;
                        break;
                    }
                    i2++;
                } else {
                    if (aHVarG == null) {
                        continue;
                    } else {
                        if ((this.f10516b.e() >= 4 ? aHVarG.x() : aHVarG.x() - this.f10515l.O().af()) == d2) {
                            setSelectedItem(getItemAt(i2));
                            z2 = true;
                            break;
                        }
                    }
                    i2++;
                }
            }
            if (!z2 && getItemCount() > 0) {
                setSelectedItem(getItemAt(0));
            }
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        aR.a().a(this.f10517c);
    }

    public void a(p pVar) {
        this.f10520d.add(pVar);
    }

    private void a(aH aHVar) {
        Iterator it = this.f10520d.iterator();
        while (it.hasNext()) {
            ((p) it.next()).a(aHVar);
        }
    }

    @Override // com.efiAnalytics.tuningwidgets.portEditor.F
    public void a(R r2) {
        this.f10515l = r2;
        this.f10524h = true;
        super.removeAllItems();
        if (r2.O().al().equals("XCP")) {
            this.f10527k = true;
        }
        String[] strArrS = r2.s();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < strArrS.length; i2++) {
            aH aHVarG = r2.g(strArrS[i2]);
            if (aHVarG.b() != null && !aHVarG.b().equals("formula") && !aHVarG.b().equals(ControllerParameter.PARAM_CLASS_BITS) && (!this.f10526j || aHVarG.l() != 4)) {
                arrayList.add(strArrS[i2]);
            }
        }
        for (String str : W.a((String[]) arrayList.toArray(new String[arrayList.size()]))) {
            addItem(str);
        }
        this.f10524h = false;
    }
}
