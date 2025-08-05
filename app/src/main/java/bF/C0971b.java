package bF;

import java.util.ArrayList;

/* renamed from: bF.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bF/b.class */
public class C0971b implements x {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f6837a;

    public void a(int i2, String str) {
        b(i2);
        this.f6837a.set(i2, str);
    }

    private void b(int i2) {
        for (int size = this.f6837a.size(); size < i2; size++) {
            this.f6837a.add("");
        }
    }

    @Override // bF.x
    public String a(int i2) {
        return (String) this.f6837a.get(i2);
    }
}
