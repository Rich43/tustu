package com.efiAnalytics.tuningwidgets.portEditor;

import G.R;
import G.aM;
import G.aN;
import G.aR;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/portEditor/v.class */
public class v extends JComboBox implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    R f10600a;

    /* renamed from: b, reason: collision with root package name */
    aM f10601b;

    /* renamed from: c, reason: collision with root package name */
    aN f10602c;

    /* renamed from: d, reason: collision with root package name */
    String[] f10603d = null;

    /* renamed from: e, reason: collision with root package name */
    int f10604e = 0;

    /* renamed from: f, reason: collision with root package name */
    int f10605f = 0;

    public v(R r2, String str, String[] strArr) {
        this.f10600a = null;
        this.f10601b = null;
        this.f10602c = null;
        this.f10600a = r2;
        this.f10601b = r2.c(str);
        for (String str2 : strArr) {
            addItem(b(str2));
        }
        addActionListener(new w(this));
        this.f10602c = new x(this);
        try {
            aR.a().a(this.f10600a.c(), this.f10601b.aJ(), this.f10602c);
        } catch (V.a e2) {
            bH.C.a("Failed to subscribe " + this.f10601b.aJ() + " for update notification.", e2, this);
        }
        b();
    }

    public boolean a() {
        return ((y) getSelectedItem()).a().getBytes()[0] != 32;
    }

    private y b(String str) {
        y yVar = new y(this);
        yVar.a(str);
        if (str.equals(" ")) {
            yVar.b(C1818g.b("No additional Condition"));
        } else if (str.equals("&")) {
            yVar.b(C1818g.b("And"));
        } else if (str.equals(CallSiteDescriptor.OPERATOR_DELIMITER)) {
            yVar.b(C1818g.b("Or"));
        } else if (str.equals("t")) {
            yVar.b(C1818g.b("Time True"));
        } else {
            yVar.b(str);
        }
        return yVar;
    }

    public void a(int i2) {
        this.f10605f = i2;
    }

    public void b(int i2) {
        this.f10604e = i2;
        b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c(String str) {
        a(str);
        try {
            double[][] dArrI = this.f10601b.i(this.f10600a.h());
            if (dArrI[0].length == 1) {
                dArrI[this.f10604e][0] = str.charAt(0);
            } else {
                dArrI[this.f10605f][this.f10604e] = str.charAt(0);
            }
            this.f10601b.a(this.f10600a.h(), dArrI);
        } catch (V.g e2) {
            bH.C.a("Error updating: operator to " + str);
        } catch (V.j e3) {
            bH.C.a("Value Out of Bounds?? validating all values for " + this.f10601b.aJ());
            try {
                double[][] dArrI2 = this.f10601b.i(this.f10600a.h());
                for (int i2 = 0; i2 < dArrI2.length; i2++) {
                    if (dArrI2[i2][0] > this.f10601b.r()) {
                        bH.C.d(this.f10601b.aJ() + " value at " + i2 + " out of bounds, setting to max: " + this.f10601b.r());
                        dArrI2[i2][0] = this.f10601b.r();
                    } else if (dArrI2[i2][0] < this.f10601b.q()) {
                        bH.C.d(this.f10601b.aJ() + " value at " + i2 + " out of bounds, setting to min: " + this.f10601b.q());
                        dArrI2[i2][0] = this.f10601b.q();
                    }
                }
                this.f10601b.a(this.f10600a.h(), dArrI2);
            } catch (V.g e4) {
                bH.C.a("Error updating: operator to " + str);
            } catch (V.j e5) {
                bH.C.a("Value Out of Bounds?? vFailed to fix: " + this.f10601b.aJ());
                Logger.getLogger(v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    public void a(String str) {
        for (int i2 = 0; i2 < getItemCount(); i2++) {
            y yVar = (y) getItemAt(i2);
            if (str.equals(yVar.a())) {
                super.setSelectedItem(yVar);
                return;
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x003b, code lost:
    
        setSelectedItem(getItemAt(r8));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void b() {
        /*
            r5 = this;
            r0 = r5
            G.aM r0 = r0.f10601b     // Catch: V.g -> L50
            r1 = r5
            G.R r1 = r1.f10600a     // Catch: V.g -> L50
            G.Y r1 = r1.h()     // Catch: V.g -> L50
            double[][] r0 = r0.i(r1)     // Catch: V.g -> L50
            r6 = r0
            r0 = r5
            r1 = r6
            r2 = r5
            int r2 = r2.f10605f     // Catch: V.g -> L50
            r3 = r5
            int r3 = r3.f10604e     // Catch: V.g -> L50
            byte r0 = r0.a(r1, r2, r3)     // Catch: V.g -> L50
            r7 = r0
            r0 = 0
            r8 = r0
        L1f:
            r0 = r8
            r1 = r5
            int r1 = r1.getItemCount()     // Catch: V.g -> L50
            if (r0 >= r1) goto L4d
            r0 = r5
            r1 = r8
            java.lang.Object r0 = r0.getItemAt(r1)     // Catch: V.g -> L50
            com.efiAnalytics.tuningwidgets.portEditor.y r0 = (com.efiAnalytics.tuningwidgets.portEditor.y) r0     // Catch: V.g -> L50
            java.lang.String r0 = r0.a()     // Catch: V.g -> L50
            byte[] r0 = r0.getBytes()     // Catch: V.g -> L50
            r1 = 0
            r0 = r0[r1]     // Catch: V.g -> L50
            r1 = r7
            if (r0 != r1) goto L47
            r0 = r5
            r1 = r5
            r2 = r8
            java.lang.Object r1 = r1.getItemAt(r2)     // Catch: V.g -> L50
            r0.setSelectedItem(r1)     // Catch: V.g -> L50
            goto L4d
        L47:
            int r8 = r8 + 1
            goto L1f
        L4d:
            goto L55
        L50:
            r6 = move-exception
            r0 = r6
            r0.printStackTrace()
        L55:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.efiAnalytics.tuningwidgets.portEditor.v.b():void");
    }

    private byte a(double[][] dArr, int i2, int i3) {
        return dArr[0].length == 1 ? (byte) dArr[i3][0] : (byte) dArr[i2][i3];
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        aR.a().a(this.f10602c);
    }
}
