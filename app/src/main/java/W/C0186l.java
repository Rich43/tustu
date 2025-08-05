package W;

import java.util.List;

/* renamed from: W.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/l.class */
public class C0186l implements InterfaceC0185k {

    /* renamed from: a, reason: collision with root package name */
    private String[] f2163a;

    public C0186l(List list) {
        this.f2163a = (String[]) list.toArray(new String[0]);
    }

    @Override // W.InterfaceC0185k
    public String a(float f2) {
        try {
            return this.f2163a[(int) f2];
        } catch (Exception e2) {
            return Float.toString(f2);
        }
    }
}
