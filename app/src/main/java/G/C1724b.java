package g;

import W.C0184j;
import W.C0188n;
import bB.r;
import bH.C;
import h.C1737b;
import h.InterfaceC1736a;
import i.InterfaceC1742b;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import k.C1756d;
import sun.util.locale.LanguageTag;

/* renamed from: g.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:g/b.class */
public class C1724b extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f12176a;

    /* renamed from: b, reason: collision with root package name */
    HashMap f12178b;

    /* renamed from: c, reason: collision with root package name */
    HashMap f12179c;

    /* renamed from: d, reason: collision with root package name */
    boolean f12180d;

    /* renamed from: l, reason: collision with root package name */
    private boolean f12181l;

    /* renamed from: e, reason: collision with root package name */
    boolean f12182e;

    /* renamed from: f, reason: collision with root package name */
    Component f12183f;

    /* renamed from: m, reason: collision with root package name */
    private boolean f12184m;

    /* renamed from: g, reason: collision with root package name */
    String[] f12185g;

    /* renamed from: h, reason: collision with root package name */
    ArrayList f12186h;

    /* renamed from: i, reason: collision with root package name */
    ArrayList f12187i;

    /* renamed from: o, reason: collision with root package name */
    private C0188n f12189o;

    /* renamed from: j, reason: collision with root package name */
    NumberFormat f12190j;

    /* renamed from: k, reason: collision with root package name */
    private static int f12177k = Integer.MAX_VALUE;

    /* renamed from: n, reason: collision with root package name */
    private static bB.b f12188n = new bB.c();

    public static bB.b a() {
        return f12188n;
    }

    public static void a(bB.b bVar) {
        f12188n = bVar;
    }

    public C1724b(Component component) {
        this(component, false);
    }

    public C1724b(Component component, boolean z2) {
        super("DataLoader_" + Math.random());
        this.f12176a = 0;
        this.f12178b = new HashMap();
        this.f12179c = new HashMap();
        this.f12180d = false;
        this.f12181l = false;
        this.f12182e = false;
        this.f12183f = null;
        this.f12184m = true;
        this.f12185g = null;
        this.f12186h = new ArrayList();
        this.f12187i = new ArrayList();
        this.f12189o = null;
        this.f12190j = new DecimalFormat("######.#####");
        this.f12180d = z2;
        this.f12183f = component;
    }

    public void a(InterfaceC1736a interfaceC1736a) {
        this.f12187i.add(interfaceC1736a);
    }

    private void a(ArrayList arrayList) {
        Iterator it = this.f12187i.iterator();
        while (it.hasNext()) {
            ((InterfaceC1736a) it.next()).a(arrayList);
        }
    }

    public void b() {
        String strA = h.i.a("fieldMapping", h.i.a("DEFAULT_fieldMapping", "FieldMaps/MegaSquirt.properties"));
        if (strA.equals("Auto")) {
            return;
        }
        h.g.b(strA);
        System.out.println("Using " + strA + " for Field Mappings");
    }

    public void a(boolean z2) {
        this.f12181l = z2;
    }

    public void a(String str) {
        a(new String[]{str});
    }

    public void a(String[] strArr) {
        this.f12185g = strArr;
    }

    @Override // java.lang.Thread
    public void start() {
        super.start();
        C.d("Started Data Loader Thread");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Thread.currentThread().setPriority(1);
        if (this.f12185g != null) {
            try {
                b(this.f12185g);
            } catch (Exception e2) {
                String str = "Entire log file not loaded:\n" + e2.getMessage();
                a(1.0d);
                a(e2, str);
            } finally {
                System.gc();
            }
        }
    }

    public void c() {
        this.f12182e = true;
    }

    private boolean c(ArrayList arrayList, String str) {
        try {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                if (((C0184j) it.next()).a().equals(str)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            C.a("Exception checking for existance of appended field.");
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:321:0x0d70, code lost:
    
        r22 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0d7a, code lost:
    
        if (r22 >= r0.size()) goto L494;
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0d7d, code lost:
    
        r0 = r0.a(r22);
     */
    /* JADX WARN: Code restructure failed: missing block: B:325:0x0d88, code lost:
    
        if (r0 == null) goto L496;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x0d8b, code lost:
    
        r0 = (W.C0184j) r0.get(r22);
        r0.b(false);
        r0.a(r0.a());
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x0da7, code lost:
    
        r22 = r22 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0db2, code lost:
    
        if (r0.g() == null) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x0dba, code lost:
    
        if (r0.h() != false) goto L333;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:0x0dbd, code lost:
    
        r0.c(r0.g());
     */
    /* JADX WARN: Code restructure failed: missing block: B:334:0x0dcc, code lost:
    
        if (r0.h() == null) goto L336;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x0dcf, code lost:
    
        r0.c(r0.h());
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x0dde, code lost:
    
        if (r0.d() > 0) goto L347;
     */
    /* JADX WARN: Code restructure failed: missing block: B:340:0x0de4, code lost:
    
        if (h.i.j() == false) goto L342;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x0de7, code lost:
    
        com.efiAnalytics.ui.bV.d("Log File contains no data or is not recognized.\nIf this is an ASCII type log file, \nCheck the selected File Delimiter under the Options Menu.", new java.awt.Frame());
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x0e07, code lost:
    
        if (com.efiAnalytics.ui.bV.a("No Data Was found in this log file.\nDelimiter Auto Detect is currently disabled under the options menu.\n\nWould you like to try loading this file again with Auto Detect Delimiter enabled?", (java.awt.Component) com.efiAnalytics.ui.bV.c(), true) == false) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x0e0a, code lost:
    
        h.i.c("delimiter", "Auto");
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x0e16, code lost:
    
        return b(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0e1c, code lost:
    
        if (r19 != false) goto L350;
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0e1f, code lost:
    
        a(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:350:0x0e28, code lost:
    
        r22 = false;
        r23 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0e35, code lost:
    
        if (r23 >= r0.size()) goto L497;
     */
    /* JADX WARN: Code restructure failed: missing block: B:353:0x0e38, code lost:
    
        r0 = (W.C0184j) r0.get(r23);
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0e49, code lost:
    
        if (r0.q() != false) goto L356;
     */
    /* JADX WARN: Code restructure failed: missing block: B:355:0x0e4c, code lost:
    
        r0.b(r0);
        r22 = true;
        r23 = r23 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:357:0x0e62, code lost:
    
        if (r0.r() == false) goto L500;
     */
    /* JADX WARN: Code restructure failed: missing block: B:358:0x0e65, code lost:
    
        r0.t();
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x0e6a, code lost:
    
        r23 = r23 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x0e72, code lost:
    
        if (r22 == false) goto L363;
     */
    /* JADX WARN: Code restructure failed: missing block: B:362:0x0e75, code lost:
    
        a(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x0e7b, code lost:
    
        a(1.0d);
        r0.a();
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x0ffb, code lost:
    
        r13 = r13 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public W.C0188n b(java.lang.String[] r8) throws V.a {
        /*
            Method dump skipped, instructions count: 4160
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: g.C1724b.b(java.lang.String[]):W.n");
    }

    private boolean a(String str, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((C0184j) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String a(ArrayList arrayList, String str) {
        int i2 = 1;
        int iIndexOf = str.indexOf(LanguageTag.SEP);
        if (iIndexOf > 0) {
            String strSubstring = str.substring(iIndexOf + 1);
            str = str.substring(0, iIndexOf);
            i2 = 1 + Integer.parseInt(strSubstring);
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            C0184j c0184j = (C0184j) it.next();
            if (c0184j.a().equals(str)) {
                int i3 = c0184j.i();
                return i3 - i2 >= 0 ? a(c0184j.c(i3 - i2)) : "0";
            }
        }
        return "0";
    }

    private String a(float f2) {
        return this.f12190j.format(f2);
    }

    public C1726d b(String str) {
        C1726d c1726d = (C1726d) this.f12178b.get(str);
        if (c1726d != null) {
            return c1726d;
        }
        C1726d c1726d2 = new C1726d();
        c1726d2.a(".", str);
        this.f12178b.put(str, c1726d2);
        return c1726d2;
    }

    private int c(String str) {
        boolean z2 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (str.charAt(i2) == '[') {
                z2 = true;
            } else if (str.charAt(i2) == ']') {
                z2 = false;
            }
            if (z2 && str.charAt(i2) == '|') {
                return i2;
            }
        }
        return -1;
    }

    protected String b(ArrayList arrayList, String str) {
        String strA;
        String strA2;
        String strSubstring;
        String strSubstring2;
        int iC = c(str);
        if (iC == -1) {
            return str;
        }
        if (iC < str.indexOf(".")) {
            strA = a(str.substring(iC + 1, str.indexOf("]")), "]", "");
            strA2 = a(str.substring(0, iC), "[", "");
            strSubstring = str.substring(0, str.indexOf(strA2) - 1);
            strSubstring2 = str.substring(str.indexOf(strA) + strA.length() + 1, str.length());
        } else {
            String strSubstring3 = str.substring(0, iC);
            String strSubstring4 = str.substring(iC + 1);
            strA = a(strSubstring3, "[", "");
            strA2 = a(strSubstring4, "]", "");
            strSubstring = str.substring(0, str.indexOf(strA) - 1);
            strSubstring2 = str.substring(str.indexOf(strA2) + strA2.length() + 1, str.length());
        }
        return strSubstring + b(strA).a(a(arrayList, strA2)) + strSubstring2;
    }

    private boolean a(Exception exc, String str) {
        if (this.f12179c.get(exc.getMessage()) != null) {
            return false;
        }
        this.f12179c.put(exc.getMessage(), exc);
        exc.printStackTrace();
        if (this.f12183f == null || this.f12176a >= 3) {
            return false;
        }
        if (str == null || str.equals("")) {
            str = exc.getMessage();
        }
        C1733k.a(str + "\nFurther occurances of this error will be supressed.", this.f12183f);
        this.f12176a++;
        return true;
    }

    private Float a(String str, String str2) {
        if (str2 != null && !str2.equals("")) {
            try {
                Double.parseDouble(str2);
            } catch (Exception e2) {
            }
        }
        String strSubstring = "" + C1727e.a(a(str, "logVal", str2));
        int iIndexOf = strSubstring.indexOf(46);
        if (iIndexOf != -1 && strSubstring.length() - iIndexOf > 3) {
            strSubstring = strSubstring.substring(0, iIndexOf + 4);
        }
        return Float.valueOf(Float.parseFloat(strSubstring));
    }

    private C0184j[] b(ArrayList arrayList) {
        String[] strArrF = h.i.f("APPEND_FIELD_");
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        for (String str : strArrF) {
            if (str.length() > "APPEND_FIELD_".length()) {
                C0184j c0184j = new C0184j(str.substring("APPEND_FIELD_".length(), str.length()));
                String strB = h.i.b(str);
                try {
                    strB = h.g.a().c(strB);
                    c0184j.c(strB);
                    if (C1737b.a().b(c0184j.a())) {
                        arrayList3.add(c0184j);
                    }
                } catch (Exception e2) {
                    C.a("Unable to map fields for formula: " + strB);
                }
            }
        }
        Iterator it = Z.f.a().b().iterator();
        while (it.hasNext()) {
            arrayList3.addAll(((Z.d) it.next()).a(arrayList));
        }
        int size = arrayList3.size();
        for (int i2 = 0; i2 < size && arrayList3.size() > 0; i2++) {
            int i3 = 0;
            while (i3 < arrayList3.size()) {
                C0184j c0184j2 = (C0184j) arrayList3.get(i3);
                if (a(arrayList, arrayList2, c0184j2)) {
                    arrayList2.add(c0184j2);
                    arrayList3.remove(i3);
                    i3--;
                }
                i3++;
            }
        }
        Iterator it2 = arrayList3.iterator();
        while (it2.hasNext()) {
            C.d("not loading field '" + ((C0184j) it2.next()).a() + "', not all the dependent fields are available.");
        }
        return (C0184j[]) arrayList2.toArray(new C0184j[arrayList2.size()]);
    }

    private boolean a(ArrayList arrayList, ArrayList arrayList2, C0184j c0184j) {
        try {
            if (c0184j.a().equals("AFR") || c0184j.a().equals("Lambda")) {
                return true;
            }
            String[] strArrB = C1756d.a().a(c0184j.j()).b();
            for (int i2 = 0; i2 < strArrB.length; i2++) {
                if (!strArrB[i2].equals(c0184j.a()) && !d(arrayList, strArrB[i2]) && e(arrayList2, strArrB[i2]) == null) {
                    return false;
                }
            }
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private boolean d(ArrayList arrayList, String str) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (((C0184j) arrayList.get(i2)).a().equals(str)) {
                return true;
            }
        }
        return false;
    }

    private C0184j e(ArrayList arrayList, String str) {
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            if (((C0184j) arrayList.get(i2)).a().equals(str)) {
                return (C0184j) arrayList.get(i2);
            }
        }
        return null;
    }

    private String a(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        while (true) {
            int i2 = iIndexOf;
            if (i2 < 0) {
                return str;
            }
            str = str.substring(0, i2) + str3 + str.substring(i2 + str2.length());
            iIndexOf = str.indexOf(str2);
        }
    }

    private void f() {
        Iterator it = this.f12186h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1742b) it.next()).a();
        }
    }

    private void a(C0188n c0188n) {
        Iterator it = this.f12186h.iterator();
        while (it.hasNext()) {
            InterfaceC1742b interfaceC1742b = (InterfaceC1742b) it.next();
            try {
                if (this.f12180d) {
                    interfaceC1742b.b(c0188n);
                } else {
                    interfaceC1742b.a(c0188n);
                }
            } catch (Exception e2) {
                C.a("Exception caught notifying a listener. Stack:");
                e2.printStackTrace();
            }
        }
    }

    private void a(double d2) {
        Iterator it = this.f12186h.iterator();
        while (it.hasNext()) {
            try {
                ((InterfaceC1742b) it.next()).a(d2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    private void g() {
        Iterator it = this.f12186h.iterator();
        while (it.hasNext()) {
            ((InterfaceC1742b) it.next()).b();
        }
    }

    public void a(InterfaceC1742b interfaceC1742b) {
        if (this.f12186h.contains(interfaceC1742b)) {
            return;
        }
        this.f12186h.add(interfaceC1742b);
    }

    private C0184j a(C0184j c0184j) {
        r rVarA = f12188n.a(c0184j.a());
        if (rVarA != null) {
            c0184j.g((float) rVarA.a());
            c0184j.f((float) rVarA.b());
            if (rVarA.f() != -1) {
                c0184j.e(rVarA.f());
            }
        }
        return c0184j;
    }

    public static void a(int i2) {
        f12177k = i2;
    }

    boolean d() {
        return this.f12184m;
    }

    public void b(boolean z2) {
        this.f12184m = z2;
    }

    public boolean e() {
        return this.f12181l;
    }
}
