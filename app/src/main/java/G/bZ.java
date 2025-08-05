package G;

import bH.C0995c;
import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/bZ.class */
public class bZ extends aM implements Cloneable {

    /* renamed from: d, reason: collision with root package name */
    public static String f883d = "channelValueOnConnect";

    /* renamed from: e, reason: collision with root package name */
    public static String f884e = "continuousChannelValue";

    /* renamed from: c, reason: collision with root package name */
    Y f882c = null;

    /* renamed from: f, reason: collision with root package name */
    private String f885f = null;

    @Override // G.aM
    public void a(String str) throws V.g {
        if (str == null || !(str.equals(ControllerParameter.PARAM_CLASS_BITS) || str.equals(ControllerParameter.PARAM_CLASS_SCALAR) || str.equals(ControllerParameter.PARAM_CLASS_ARRAY) || str.equals("string") || str.equals(f883d) || str.equals(f884e))) {
            throw new V.g("Invalid Parameter Class for PcVariable " + aJ() + " attemped parameterClass: " + str + "\nParameter Class must be 1 of: " + ControllerParameter.PARAM_CLASS_BITS + "," + ControllerParameter.PARAM_CLASS_SCALAR + "," + ControllerParameter.PARAM_CLASS_ARRAY);
        }
        ((aM) this).f595b = str;
    }

    @Override // G.aM
    protected void a(Y y2, int i2, int i3, int[] iArr, boolean z2) throws V.g {
        if (G()) {
            return;
        }
        Y yK = k(y2);
        if ((!C0995c.c(yK.a(0, i3, iArr.length), iArr)) || z2) {
            super.a(yK, 0, i3, iArr, z2);
            if (y2.i() != null) {
                aR.a().b(y2.i(), aJ());
                y2.c(i2, i3, iArr);
                aR.a().a(y2.i(), aJ());
            }
        }
    }

    @Override // G.aM
    protected int[] a(Y y2, int i2, int i3, int i4) {
        return super.a(k(y2), 0, i3, i4);
    }

    @Override // G.aM
    public int[][] a(Y y2) throws V.g {
        if (a() * m() * e() > k((Y) null).c(0)) {
            throw new V.g("Attempt to retrieve data beyond page size!\n\tCheck offset and size for parameter:" + aJ());
        }
        int[][] iArr = new int[a()][m()];
        int iE = 0;
        for (int[] iArr2 : iArr) {
            for (int i2 = 0; i2 < iArr[0].length; i2++) {
                iArr2[i2] = C0995c.b(k((Y) null).b(0), iE, e(), true, z());
                iE += e();
            }
        }
        return iArr;
    }

    @Override // G.aM
    protected Y k(Y y2) {
        if (this.f882c == null) {
            A aC2 = super.c();
            this.f882c = new Y(null, new int[1][super.e() * aC2.a() * aC2.b()]);
            this.f882c.f();
        }
        return this.f882c;
    }

    @Override // G.aM
    protected int[] b(Y y2, int i2) {
        return k(y2).b(0);
    }

    @Override // G.aM
    public boolean a(int i2, int i3, int i4) {
        return false;
    }

    @Override // G.aM
    public void d(double d2) {
        try {
            if (i().equals(ControllerParameter.PARAM_CLASS_BITS)) {
                super.a(k((Y) null), (int) d2);
            } else {
                super.a(k((Y) null), d2);
            }
        } catch (V.g e2) {
            bH.C.a("Unable to set defaultValue for " + aJ() + ", " + e2.getMessage());
        } catch (V.j e3) {
            bH.C.a("Value Out of Bounds: defaultValue for " + aJ());
        }
        super.d(d2);
    }

    @Override // G.aM
    protected boolean o(Y y2) {
        return true;
    }

    public bZ Q() {
        try {
            bZ bZVar = (bZ) super.clone();
            if (this.f882c != null) {
                bZVar.f882c = this.f882c.a();
            } else {
                bZVar.f882c = null;
            }
            return bZVar;
        } catch (CloneNotSupportedException e2) {
            Logger.getLogger(bZ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public String R() {
        return this.f885f;
    }

    public void g(String str) {
        this.f885f = str;
    }
}
