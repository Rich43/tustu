package w;

import r.o;

/* renamed from: w.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:w/c.class */
public class C1890c implements o {
    @Override // r.o
    public boolean a(String str, String str2) {
        if (str2 == null || str2.length() <= 6 || str.length() <= 7) {
            return false;
        }
        return str2.substring(0, 1).equals(str.substring(0, 7));
    }
}
