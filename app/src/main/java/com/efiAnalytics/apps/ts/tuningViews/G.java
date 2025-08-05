package com.efiAnalytics.apps.ts.tuningViews;

import G.C0073bf;
import W.C0200z;
import bH.C1011s;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import r.C1798a;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/tuningViews/G.class */
public class G {
    public static List a(List list) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            arrayList.addAll(a((G.R) it.next()));
        }
        arrayList.addAll(b(list));
        arrayList.addAll(a());
        String[] strArr = new String[list.size()];
        for (int i2 = 0; i2 < list.size(); i2++) {
            strArr[i2] = ((G.R) list.get(i2)).i();
        }
        return a(arrayList, strArr);
    }

    public static List a(G.R r2) {
        ArrayList arrayList = new ArrayList();
        for (C0073bf c0073bf : r2.af()) {
            try {
                C1438k c1438k = new C1438k(c0073bf.e().b(c0073bf.aJ(), c0073bf.a()));
                c1438k.a("Current ECU Definition");
                arrayList.add(c1438k);
            } catch (Exception e2) {
                bH.C.a("Failed to load Default TuneView from ecuConfig! name: " + c0073bf.aJ() + ", md5: " + c0073bf.a() + "\nError: " + e2.getLocalizedMessage());
            }
        }
        File[] fileArrListFiles = C1807j.l().listFiles(new H());
        r.o oVarB = r.p.a().b();
        if (fileArrListFiles != null) {
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                if (fileArrListFiles[i2].getName().toLowerCase().endsWith(C1798a.cp.toLowerCase())) {
                    String strA = C0200z.a(fileArrListFiles[i2]);
                    if (oVarB == null || oVarB.a(r2.i(), strA)) {
                        C1438k c1438k2 = new C1438k(fileArrListFiles[i2]);
                        c1438k2.a("Application");
                        arrayList.add(c1438k2);
                    }
                }
            }
        } else {
            bH.C.b("No TuneView files found in :\n\t" + ((Object) C1807j.l()));
        }
        return arrayList;
    }

    public static List a() {
        ArrayList arrayList = new ArrayList();
        File[] fileArrListFiles = C1807j.k().listFiles();
        if (fileArrListFiles != null) {
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                if (fileArrListFiles[i2].getName().toLowerCase().endsWith(C1798a.cp.toLowerCase())) {
                    arrayList.add(new C1438k(fileArrListFiles[i2]));
                }
            }
        } else {
            bH.C.b("No TuneView files found in :\n\t" + ((Object) C1807j.l()));
        }
        return arrayList;
    }

    public static List b(List list) throws V.a {
        ArrayList arrayList = new ArrayList();
        File[] fileArrB = b();
        if (fileArrB != null) {
            for (int i2 = 0; i2 < fileArrB.length; i2++) {
                if (fileArrB[i2].getName().toLowerCase().endsWith(C1798a.cp.toLowerCase())) {
                    C1438k c1438k = new C1438k(fileArrB[i2]);
                    c1438k.a("Current Project");
                    arrayList.add(c1438k);
                }
            }
        } else {
            bH.C.b("No TuneView files found in :\n\t" + ((Object) C1807j.l()));
        }
        if (arrayList.isEmpty()) {
            ArrayList arrayList2 = new ArrayList();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                arrayList2.addAll(a((G.R) it.next()));
            }
            int i3 = 0;
            Iterator<E> it2 = arrayList2.iterator();
            while (it2.hasNext()) {
                int i4 = i3;
                i3++;
                C1011s.a(((C1438k) it2.next()).a(), new File(C1807j.a(aE.a.A()), a(i4)));
            }
            if (i3 > 0) {
                return b(list);
            }
        }
        return arrayList;
    }

    public static File[] b() {
        return C1807j.a(aE.a.A()).listFiles(new I());
    }

    public static void c() {
        File[] fileArrB = b();
        for (int length = fileArrB.length - 1; length >= 0; length--) {
            String strA = a(length);
            if (!strA.equals(fileArrB[length].getName())) {
                File file = new File(fileArrB[length].getParentFile(), strA);
                if (file.exists()) {
                    for (int i2 = length; i2 >= 0; i2--) {
                        File file2 = new File(fileArrB[i2].getParentFile(), fileArrB[i2].getName() + "~");
                        if (!file2.exists() || file2.delete()) {
                            fileArrB[i2].renameTo(file2);
                            fileArrB[i2] = file2;
                        } else {
                            bH.C.b("Can not delete file to correct TuneView file names for project. \n" + file2.getAbsolutePath());
                        }
                    }
                } else if (fileArrB[length].renameTo(file)) {
                    fileArrB[length] = file;
                } else {
                    bH.C.b("Failed to rename TuningView File " + fileArrB[length].getName() + " to " + file.getName());
                }
            }
        }
    }

    public static String a(int i2) {
        return C1438k.f9785a + bH.W.a(i2 + ".", '0', 4) + C1798a.cp;
    }

    public static File b(int i2) {
        return new File(C1807j.a(aE.a.A()), a(i2));
    }

    public static List a(List list, String[] strArr) {
        r.o oVarB = r.p.a().b();
        int i2 = 0;
        while (i2 < list.size()) {
            if (!a(oVarB, strArr, C0200z.a(((C1438k) list.get(i2)).a()))) {
                list.remove(i2);
                i2--;
            }
            i2++;
        }
        return list;
    }

    private static boolean a(r.o oVar, String[] strArr, String str) {
        for (String str2 : strArr) {
            if (oVar.a(str2, str)) {
                return true;
            }
        }
        return false;
    }
}
