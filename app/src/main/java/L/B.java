package L;

import java.util.List;

/* loaded from: TunerStudioMS.jar:L/B.class */
public class B extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1523a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1524b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1525c;

    protected B(List list) {
        this.f1523a = (ax.ab) list.get(0);
        this.f1524b = (ax.ab) list.get(1);
        this.f1525c = (ax.ab) list.get(2);
    }

    public synchronized double a(ax.S s2) {
        return this.f1523a.b(s2) != 0.0d ? this.f1524b.b(s2) : this.f1525c.b(s2);
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IF( ");
        sb.append(this.f1523a.toString()).append(", ");
        sb.append(this.f1524b.toString()).append(", ");
        sb.append(this.f1525c.toString());
        sb.append(" )");
        return sb.toString();
    }
}
