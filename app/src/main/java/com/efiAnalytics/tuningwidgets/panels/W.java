package com.efiAnalytics.tuningwidgets.panels;

import G.C0088bu;
import com.efiAnalytics.ui.bV;
import java.awt.Component;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/W.class */
public class W {

    /* renamed from: a, reason: collision with root package name */
    static Map f10313a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    static int f10314b = C1798a.a().b(C1798a.f13325ag, 10) * 60000;

    public static boolean a(G.R r2, C0088bu c0088bu, Component component) {
        Iterator it = c0088bu.ab().iterator();
        while (it.hasNext()) {
            G.aM aMVarC = r2.c((String) it.next());
            if (aMVarC == null) {
                bV.d("Password Protection is not configured correctly for this Dialog.", component);
            } else {
                try {
                    long j2 = (long) aMVarC.j(r2.h());
                    if (j2 != 0) {
                        Long l2 = (Long) f10313a.get(Long.valueOf(j2));
                        if (l2 != null && System.currentTimeMillis() - l2.longValue() < f10314b) {
                            f10313a.put(Long.valueOf(j2), Long.valueOf(System.currentTimeMillis()));
                            return true;
                        }
                        String str = c0088bu.M() + " is Password protected. Enter the password.";
                        while (true) {
                            String strA = bV.a(component, str);
                            if (strA == null) {
                                return false;
                            }
                            CRC32 crc32 = new CRC32();
                            crc32.reset();
                            crc32.update(strA.getBytes());
                            if (crc32.getValue() == j2) {
                                f10313a.put(Long.valueOf(j2), Long.valueOf(System.currentTimeMillis()));
                                return true;
                            }
                            bV.d("Invalid Password", component);
                        }
                    }
                } catch (V.g e2) {
                    Logger.getLogger(W.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    bV.d("Error Checking Password!!! See Log for details.", component);
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean a(G.R r2, C0088bu c0088bu) {
        Iterator it = c0088bu.ab().iterator();
        while (it.hasNext()) {
            G.aM aMVarC = r2.c((String) it.next());
            if (aMVarC != null) {
                try {
                    return ((long) aMVarC.j(r2.h())) != 0;
                } catch (V.g e2) {
                    Logger.getLogger(W.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    return false;
                }
            }
            bH.C.b("Password Protection is not configured correctly for this Dialog.");
        }
        return false;
    }

    public static void a(int i2) {
        f10314b = i2 * 60000;
    }

    public static void a() {
        f10313a.clear();
    }
}
