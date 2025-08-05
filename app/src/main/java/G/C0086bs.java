package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* renamed from: G.bs, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bs.class */
public class C0086bs extends C0088bu implements Serializable {

    /* renamed from: f, reason: collision with root package name */
    private int f999f = 1;

    /* renamed from: a, reason: collision with root package name */
    List f1000a = new ArrayList();

    public void a(C0051ak c0051ak) {
        this.f1000a.add(c0051ak);
    }

    public List a() {
        return this.f1000a;
    }

    public int b() {
        return this.f999f;
    }

    public void a(int i2) {
        this.f999f = i2;
    }
}
