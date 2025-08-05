package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.aM;
import G.aN;
import G.aR;
import com.efiAnalytics.ui.InterfaceC1565bc;
import javax.swing.JCheckBox;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/h.class */
class h extends JCheckBox implements aN, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f10560a;

    /* renamed from: b, reason: collision with root package name */
    aM f10561b;

    /* renamed from: d, reason: collision with root package name */
    private int f10562d;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ OutputPortEditor f10563c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public h(OutputPortEditor outputPortEditor, R r2, String str, String str2) {
        super(str2);
        this.f10563c = outputPortEditor;
        this.f10560a = null;
        this.f10561b = null;
        this.f10562d = 0;
        this.f10560a = r2;
        this.f10561b = r2.c(str);
        try {
            aR.a().a(r2.c(), str, this);
        } catch (V.a e2) {
            bH.C.a("Failed to subscribe " + str + " for update notification.", e2, this);
        }
        addItemListener(new i(this, outputPortEditor));
    }

    @Override // G.aN
    public void a(String str, String str2) {
        b();
        this.f10563c.f10538v.repaint();
    }

    protected void a() {
        try {
            double[][] dArrI = this.f10561b.i(this.f10560a.h());
            dArrI[this.f10562d][0] = isSelected() ? 1.0d : 0.0d;
            this.f10561b.a(this.f10560a.h(), dArrI);
        } catch (V.g e2) {
            bH.C.a("Failed to set Value for " + this.f10561b.aJ() + ", index = " + this.f10562d, e2, this);
        } catch (V.j e3) {
            bH.C.a("Value Out of Bounds, Attempting to fix value.");
            try {
                double[][] dArrI2 = this.f10561b.i(this.f10560a.h());
                for (int i2 = 0; i2 < dArrI2.length; i2++) {
                    if (dArrI2[i2][0] > this.f10561b.r()) {
                        bH.C.d(this.f10561b.aJ() + " value at " + i2 + " out of bounds, setting to max: " + this.f10561b.r());
                        dArrI2[i2][0] = this.f10561b.r();
                    } else if (dArrI2[i2][0] < this.f10561b.q()) {
                        bH.C.d(this.f10561b.aJ() + " value at " + i2 + " out of bounds, setting to min: " + this.f10561b.q());
                        dArrI2[i2][0] = this.f10561b.q();
                    }
                }
                this.f10561b.a(this.f10560a.h(), dArrI2);
            } catch (V.g e4) {
                bH.C.a("Failed to set Value for " + this.f10561b.aJ() + ", index = " + this.f10562d, e4, this);
            } catch (V.j e5) {
                bH.C.a("Value Out of Bounds, Failed to set Value for " + this.f10561b.aJ() + ", index = " + this.f10562d + ", ex message: " + e3.getMessage());
            }
        }
    }

    public void b() {
        try {
            super.setSelected(this.f10561b.i(this.f10560a.h())[this.f10562d][0] != 0.0d);
        } catch (V.g e2) {
            bH.C.a("Failed to getValue for " + this.f10561b.aJ() + ", index = " + this.f10562d, e2, this);
        }
    }

    public void a(int i2) {
        this.f10562d = i2;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        aR.a().a(this);
    }
}
