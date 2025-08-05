package ak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:ak/ay.class */
public class ay extends R {

    /* renamed from: a, reason: collision with root package name */
    float[] f4802a;

    /* renamed from: b, reason: collision with root package name */
    float f4803b;

    public ay() {
        super(",", false);
        this.f4802a = null;
        this.f4803b = 0.0f;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() {
        try {
            for (az azVar : l(h())) {
                String strA = azVar.a();
                C0543c c0543c = new C0543c();
                if (strA.equals("Interval")) {
                    strA = "Time";
                    c0543c.a(0.001f);
                    c0543c.a(3);
                }
                c0543c.a(strA);
                c0543c.b(azVar.b());
                c0543c.c(azVar.c());
                c0543c.d(azVar.d());
                this.f4823g.add(c0543c);
            }
        } catch (V.f e2) {
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (Exception e3) {
            bH.C.a("Failed to get units from this row:\n");
            Logger.getLogger(C0546f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        this.f4802a = new float[this.f4823g.size()];
        for (int i2 = 0; i2 < this.f4802a.length; i2++) {
            this.f4802a[i2] = Float.NaN;
        }
        ArrayList arrayList = new ArrayList();
        Iterator it = this.f4823g.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next());
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a, NumberFormatException {
        String strL;
        aD aDVar;
        float f2;
        float f3;
        while (true) {
            try {
                strL = l();
                if (!strL.isEmpty() && !strL.startsWith(this.f4822f)) {
                    break;
                }
            } catch (IOException e2) {
                Logger.getLogger(ay.class.getName()).log(Level.WARNING, "IO Error reading row from file on row " + this.f4832p + ".", (Throwable) e2);
                throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
            }
        }
        boolean z2 = false;
        do {
            if (z2) {
                try {
                    strL = l();
                } catch (IOException e3) {
                    Logger.getLogger(ay.class.getName()).log(Level.WARNING, "IO Error reading row from file on row " + this.f4832p + ".", (Throwable) e3);
                    throw new V.a("IO Error reading row from file on row " + this.f4832p + ".");
                }
            }
            aDVar = new aD(strL, this.f4822f);
            f2 = Float.parseFloat(aDVar.a(0));
            z2 = true;
        } while (f2 <= this.f4803b);
        this.f4803b = f2;
        for (int i2 = 0; i2 < aDVar.c(); i2++) {
            String strA = aDVar.a(i2);
            if (!strA.isEmpty()) {
                try {
                    f3 = Float.parseFloat(strA);
                } catch (NumberFormatException e4) {
                    f3 = Float.NaN;
                }
                this.f4802a[i2] = f3;
            }
        }
        return this.f4802a;
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 1;
    }

    private List l(String str) {
        ArrayList arrayList = new ArrayList();
        aD aDVar = new aD(str, ",");
        while (aDVar.a()) {
            arrayList.add(m(aDVar.b()));
        }
        return arrayList;
    }

    private az m(String str) throws V.a {
        aD aDVar = new aD(str, CallSiteDescriptor.OPERATOR_DELIMITER);
        try {
            az azVar = new az(this, bH.W.i(aDVar.b()));
            try {
                azVar.a(bH.W.i(aDVar.b()));
            } catch (Exception e2) {
            }
            try {
                azVar.a(Float.parseFloat(aDVar.b()));
            } catch (Exception e3) {
            }
            try {
                float f2 = Float.parseFloat(aDVar.b());
                if (f2 > azVar.c()) {
                    azVar.b(f2);
                }
            } catch (Exception e4) {
            }
            return azVar;
        } catch (Exception e5) {
            throw new V.a("Invalid Header data for Race Capture log: " + str);
        }
    }
}
