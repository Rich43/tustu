package be;

/* renamed from: be.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/b.class */
public class C1086b {

    /* renamed from: a, reason: collision with root package name */
    public static String f7955a = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_ ";

    public static R a(String str) {
        R r2 = new R();
        if (str.isEmpty()) {
            r2.c();
            r2.a("Log Field Name Required.");
            return r2;
        }
        if (a(r2, f7955a, str).a()) {
            return r2;
        }
        r2.a("Log Field Name - " + r2.d());
        return r2;
    }

    private static R a(R r2, String str, String str2) {
        for (char c2 : str2.toCharArray()) {
            if (str.indexOf(c2) == -1) {
                r2.a("Invalid Character: " + c2);
                r2.c();
                return r2;
            }
        }
        r2.b();
        return r2;
    }
}
