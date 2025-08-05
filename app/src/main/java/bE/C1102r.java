package be;

import G.C0043ac;
import G.C0048ah;
import G.aH;
import s.C1818g;

/* renamed from: be.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/r.class */
public class C1102r {

    /* renamed from: a, reason: collision with root package name */
    public static String f7991a = C1818g.b("Output Channel");

    /* renamed from: b, reason: collision with root package name */
    public static String f7992b = C1818g.b("Gauge Template");

    /* renamed from: c, reason: collision with root package name */
    public static String f7993c = C1818g.b("Data Log Field");

    public static G.Q a(String str, G.R r2) {
        if (str.equals(f7991a)) {
            return new aH(r2.ac());
        }
        if (str.equals(f7992b)) {
            return new C0048ah();
        }
        if (str.equals(f7993c)) {
            return new C0043ac();
        }
        return null;
    }
}
