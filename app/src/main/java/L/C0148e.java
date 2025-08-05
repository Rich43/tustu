package L;

import java.util.Iterator;
import java.util.List;

/* renamed from: L.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/e.class */
public class C0148e extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    List f1655a;

    protected C0148e(List list) {
        this.f1655a = list;
    }

    public synchronized double a(ax.S s2) {
        double dB = 0.0d;
        Iterator it = this.f1655a.iterator();
        while (it.hasNext()) {
            dB += ((ax.ab) it.next()).b(s2);
        }
        return dB / this.f1655a.size();
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("avg( ");
        for (int i2 = 0; i2 < this.f1655a.size(); i2++) {
            sb.append(((ax.ab) this.f1655a.get(i2)).toString());
            if (i2 + 1 < this.f1655a.size()) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
