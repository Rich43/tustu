package L;

import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:L/Q.class */
public class Q extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    List f1576a;

    protected Q(List list) {
        this.f1576a = list;
    }

    public synchronized double a(ax.S s2) {
        double dB = 0.0d;
        Iterator it = this.f1576a.iterator();
        while (it.hasNext()) {
            dB += ((ax.ab) it.next()).b(s2);
        }
        return dB;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SUM( ");
        for (int i2 = 0; i2 < this.f1576a.size(); i2++) {
            sb.append(((ax.ab) this.f1576a.get(i2)).toString());
            if (i2 + 1 < this.f1576a.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
