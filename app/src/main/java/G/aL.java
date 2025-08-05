package G;

import com.efiAnalytics.plugin.ecu.ControllerParameter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/aL.class */
public class aL {

    /* renamed from: a, reason: collision with root package name */
    public static String f588a = "UNALLOCATED";

    /* renamed from: b, reason: collision with root package name */
    private static boolean f589b = true;

    public static void a(R r2) {
        if (r2.ae() || !f589b) {
            return;
        }
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (int i2 = 0; i2 < r2.O().g(); i2++) {
            a(r2, i2);
        }
        r2.aa();
        r2.b(true);
        bH.C.c("Fill Constants: " + (System.currentTimeMillis() - jCurrentTimeMillis));
    }

    public static void a(R r2, int i2) {
        ArrayList arrayList = new ArrayList();
        Iterator itA = r2.a(i2);
        while (itA.hasNext()) {
            arrayList.add(itA.next());
        }
        ArrayList arrayListB = b(a(arrayList));
        ArrayList arrayList2 = new ArrayList();
        C0102ch c0102ch = null;
        int i3 = 0;
        while (i3 < arrayListB.size()) {
            aM aMVar = (aM) arrayListB.get(i3);
            if (aMVar.O()) {
                arrayListB.remove(i3);
                r2.b(aMVar.aJ());
                i3--;
            } else if (c0102ch == null) {
                c0102ch = new C0102ch(i2);
                c0102ch.a(aMVar.g());
                c0102ch.b(aMVar.y());
                arrayList2.add(c0102ch);
            } else if (c0102ch.a(aMVar.g(), aMVar.y())) {
                c0102ch.b(aMVar.g(), aMVar.y());
            } else {
                c0102ch = new C0102ch(i2);
                c0102ch.a(aMVar.g());
                c0102ch.b(aMVar.y());
                arrayList2.add(c0102ch);
            }
            i3++;
        }
        if (!arrayList2.isEmpty() && ((C0102ch) arrayList2.get(0)).b() > 0) {
            r2.a(a("UNALLOCATED_" + i2, i2, 0, ((C0102ch) arrayList2.get(0)).b()));
        }
        for (int i4 = 0; i4 < arrayList2.size() - 1; i4++) {
            C0102ch c0102ch2 = (C0102ch) arrayList2.get(i4);
            C0102ch c0102ch3 = (C0102ch) arrayList2.get(i4 + 1);
            int iB = c0102ch2.b() + c0102ch2.c();
            r2.a(a("UNALLOCATED_SPACE_" + i2 + "_" + i4, i2, iB, c0102ch3.b() - iB));
        }
        if (arrayList2.isEmpty()) {
            return;
        }
        C0102ch c0102ch4 = (C0102ch) arrayList2.get(arrayList2.size() - 1);
        if (c0102ch4.b() + c0102ch4.c() < r2.O().f(i2)) {
            int iB2 = c0102ch4.b() + c0102ch4.c();
            r2.a(a("UNALLOCATED_TOP_" + i2, i2, iB2, r2.O().f(i2) - iB2));
        }
    }

    private static aM a(String str, int i2, int i3, int i4) {
        try {
            aM aMVar = new aM();
            aMVar.a(ControllerParameter.PARAM_CLASS_ARRAY);
            aMVar.c(i2);
            aMVar.b("U08");
            aMVar.v(str);
            aMVar.a(new bW(i3));
            aMVar.a(1, i4);
            aMVar.b(1.0d);
            aMVar.c("RAW");
            aMVar.k(true);
            aMVar.i(true);
            return aMVar;
        } catch (V.g e2) {
            Logger.getLogger(aL.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return null;
        }
    }

    public static void b(R r2, int i2) {
        ArrayList arrayList = new ArrayList();
        Iterator itA = r2.a(i2);
        while (itA.hasNext()) {
            aM aMVar = (aM) itA.next();
            if (aMVar.O()) {
                arrayList.add(aMVar.aJ());
            }
        }
        Iterator<E> it = arrayList.iterator();
        while (it.hasNext()) {
            r2.b((String) it.next());
        }
    }

    public static ArrayList a(ArrayList arrayList) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            for (int i3 = i2 + 1; i3 < arrayList.size(); i3++) {
                aM aMVar = (aM) arrayList.get(i2);
                aM aMVar2 = (aM) arrayList.get(i3);
                if (aMVar.y() > aMVar2.y()) {
                    arrayList.set(i2, aMVar2);
                    arrayList.set(i3, aMVar);
                }
            }
        }
        return arrayList;
    }

    public static ArrayList b(ArrayList arrayList) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            for (int i3 = i2 + 1; i3 < arrayList.size(); i3++) {
                aM aMVar = (aM) arrayList.get(i2);
                aM aMVar2 = (aM) arrayList.get(i3);
                if (aMVar.g() > aMVar2.g()) {
                    arrayList.set(i2, aMVar2);
                    arrayList.set(i3, aMVar);
                }
            }
        }
        return arrayList;
    }
}
