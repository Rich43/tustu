package l;

import L.AbstractC0161r;
import ax.S;
import java.util.List;
import r.C1810m;

/* renamed from: l.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:l/a.class */
public class C1759a extends AbstractC0161r {

    /* renamed from: a, reason: collision with root package name */
    public static String f12905a = "logPlaybackActive";

    public C1759a() {
        super(null);
    }

    C1759a(List list) {
        super(list);
    }

    @Override // L.AbstractC0161r
    public String a() {
        return f12905a;
    }

    @Override // L.AbstractC0161r
    public int b() {
        return 0;
    }

    @Override // L.AbstractC0161r
    public int c() {
        return 0;
    }

    @Override // L.AbstractC0161r
    public double a(S s2) {
        return C1810m.a().b() ? 1.0d : 0.0d;
    }

    public String toString() {
        return f12905a + "()";
    }

    @Override // L.AbstractC0161r
    public AbstractC0161r a(List list) {
        return new C1759a(list);
    }
}
