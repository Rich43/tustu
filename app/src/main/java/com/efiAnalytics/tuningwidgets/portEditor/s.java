package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.aM;
import G.aN;
import G.aR;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/s.class */
public class s extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f10593a;

    /* renamed from: b, reason: collision with root package name */
    aM f10594b;

    /* renamed from: c, reason: collision with root package name */
    aN f10595c;

    /* renamed from: d, reason: collision with root package name */
    int f10596d = 0;

    /* renamed from: e, reason: collision with root package name */
    JComboBox f10597e = new JComboBox();

    public s(R r2, String str, String str2, boolean z2) {
        this.f10593a = null;
        this.f10594b = null;
        this.f10595c = null;
        this.f10593a = r2;
        this.f10594b = r2.c(str);
        if (z2) {
            setLayout(new GridLayout(1, 0));
        } else {
            setLayout(new GridLayout(0, 1));
        }
        if (str2 != null && !str2.equals("")) {
            add(new JLabel(str2));
        }
        this.f10597e.addItem("On");
        this.f10597e.addItem("Off");
        this.f10597e.addActionListener(new t(this));
        this.f10595c = new u(this);
        try {
            aR.a().a(r2.c(), this.f10594b.aJ(), this.f10595c);
        } catch (V.a e2) {
            bH.C.a("Failed to subscribe " + this.f10594b.aJ() + " for update notification.", e2, this);
        }
        add(this.f10597e);
        a();
    }

    public void a() {
        try {
            if (((int) this.f10594b.i(this.f10593a.h())[this.f10596d][0]) == 0) {
                this.f10597e.setSelectedItem("Off");
            } else {
                this.f10597e.setSelectedItem("On");
            }
        } catch (V.g e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        if (str.equals("On")) {
            a(1);
        } else {
            a(0);
        }
    }

    public void a(int i2) {
        if (!this.f10597e.getSelectedItem().equals("" + i2)) {
            this.f10597e.setSelectedItem("" + i2);
        }
        try {
            double[][] dArrI = this.f10594b.i(this.f10593a.h());
            dArrI[this.f10596d][0] = i2;
            this.f10594b.a(this.f10593a.h(), dArrI);
        } catch (V.g e2) {
            bH.C.a("Error updating Bianary Value to: " + i2);
        } catch (V.j e3) {
            bH.C.a("Error updating Bianary Value to: " + i2 + ", checking range for all " + this.f10594b.aJ());
            try {
                double[][] dArrI2 = this.f10594b.i(this.f10593a.h());
                for (int i3 = 0; i3 < dArrI2.length; i3++) {
                    if (dArrI2[i3][0] > this.f10594b.r()) {
                        bH.C.d(this.f10594b.aJ() + " value at " + i3 + " out of bounds, setting to max: " + this.f10594b.r());
                        dArrI2[i3][0] = this.f10594b.r();
                    } else if (dArrI2[i3][0] < this.f10594b.q()) {
                        bH.C.d(this.f10594b.aJ() + " value at " + i3 + " out of bounds, setting to min: " + this.f10594b.q());
                        dArrI2[i3][0] = this.f10594b.q();
                    }
                }
                this.f10594b.a(this.f10593a.h(), dArrI2);
            } catch (V.g e4) {
                bH.C.a("Failed to set Value for " + this.f10594b.aJ() + ", index = " + this.f10596d, e4, this);
            } catch (V.j e5) {
                bH.C.a("Value Out of Bounds, Failed to set Value for " + this.f10594b.aJ() + ", index = " + this.f10596d + ", ex message: " + e3.getMessage());
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        bV.a(this, z2);
    }

    public void b(int i2) {
        this.f10596d = i2;
        a();
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        aR.a().a(this.f10595c);
    }
}
