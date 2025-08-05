package w;

import r.o;

/* renamed from: w.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:w/a.class */
public class C1888a implements o {
    @Override // r.o
    public boolean a(String str, String str2) {
        if (str2 == null) {
            return false;
        }
        if (str2.isEmpty()) {
            return true;
        }
        return (str2 == null || str == null || str.length() >= 5) ? str != null && str.startsWith("BigStuff Gen4") && str2.startsWith("BigStuff Gen4") : str2.equals("76") || str2.equals("77") || str2.equals("88") || str2.equals("97") || str2.equals("98") || str2.length() == 2;
    }
}
