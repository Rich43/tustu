package L;

import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:L/G.class */
public class G extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    List f1538a;

    protected G(List list) {
        this.f1538a = list;
    }

    public synchronized double a(ax.S s2) {
        double d2 = Double.MIN_VALUE;
        Iterator it = this.f1538a.iterator();
        while (it.hasNext()) {
            double dB = ((ax.ab) it.next()).b(s2);
            if (dB > d2) {
                d2 = dB;
            }
        }
        return d2;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("max( ");
        for (int i2 = 0; i2 < this.f1538a.size(); i2++) {
            sb.append(((ax.ab) this.f1538a.get(i2)).toString());
            if (i2 + 1 < this.f1538a.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
