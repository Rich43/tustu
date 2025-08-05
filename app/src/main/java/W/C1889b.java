package w;

import r.o;

/* renamed from: w.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:w/b.class */
public class C1889b implements o {
    @Override // r.o
    public boolean a(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        if (str2.isEmpty()) {
            return true;
        }
        if ((str2.equals("20") || str2.equals("\u0014")) && (str.equals("20") || str.equals("\u0014"))) {
            return true;
        }
        if (str2.length() <= 6 || str.length() <= 6) {
            return str != null && str2.equals(str);
        }
        return str2.substring(0, 6).equals(str.substring(0, 6));
    }
}
