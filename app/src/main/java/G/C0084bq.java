package G;

import java.io.Serializable;
import java.util.ArrayList;

/* renamed from: G.bq, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bq.class */
public class C0084bq extends C0088bu implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f991a = new ArrayList();

    public C0085br[] a() {
        C0085br[] c0085brArr = new C0085br[this.f991a.size()];
        for (int i2 = 0; i2 < this.f991a.size(); i2++) {
            c0085brArr[i2] = (C0085br) this.f991a.get(i2);
        }
        return c0085brArr;
    }

    public void a(C0085br c0085br) {
        this.f991a.add(c0085br);
    }

    public int b() {
        return this.f991a.size();
    }
}
