package L;

import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:L/I.class */
public class I extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    List f1544a;

    protected I(List list) {
        this.f1544a = list;
    }

    public synchronized double a(ax.S s2) {
        double d2 = Double.MAX_VALUE;
        Iterator it = this.f1544a.iterator();
        while (it.hasNext()) {
            double dB = ((ax.ab) it.next()).b(s2);
            if (dB < d2) {
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
        sb.append("min( ");
        for (int i2 = 0; i2 < this.f1544a.size(); i2++) {
            sb.append(((ax.ab) this.f1544a.get(i2)).toString());
            if (i2 + 1 < this.f1544a.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
