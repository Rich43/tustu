package aP;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/Q.class */
public class Q implements A.w {

    /* renamed from: a, reason: collision with root package name */
    private static Q f2771a = null;

    private Q() {
    }

    public static Q a() {
        if (f2771a == null) {
            f2771a = new Q();
        }
        return f2771a;
    }

    @Override // A.w
    public void a(String str) {
        ArrayList arrayListB = b();
        arrayListB.add(str);
        a(arrayListB);
    }

    public ArrayList b() {
        String strC = C1798a.a().c("badComPorts", "");
        ArrayList arrayList = new ArrayList();
        StringTokenizer stringTokenizer = new StringTokenizer(strC, CallSiteDescriptor.TOKEN_DELIMITER);
        while (stringTokenizer.hasMoreTokens()) {
            arrayList.add(stringTokenizer.nextToken());
        }
        return arrayList;
    }

    protected void a(ArrayList arrayList) {
        String str = "";
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            str = str + ((String) it.next()) + CallSiteDescriptor.TOKEN_DELIMITER;
        }
        C1798a.a().b("badComPorts", str);
        try {
            C1798a.a().e();
        } catch (V.a e2) {
            bH.C.c("error saving user properties, trying again.");
            try {
                C1798a.a().e();
                bH.C.c("succeeded on 2nd attempt; saving user properties.");
            } catch (V.a e3) {
                Logger.getLogger(Q.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }

    @Override // A.w
    public void b(String str) {
        ArrayList arrayListB = b();
        Iterator it = arrayListB.iterator();
        while (it.hasNext()) {
            if (((String) it.next()).equals(str)) {
                it.remove();
            }
        }
        a(arrayListB);
    }

    @Override // A.w
    public boolean c(String str) {
        return b().contains(str);
    }
}
