package L;

import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:L/N.class */
public class N extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1565a;

    /* renamed from: b, reason: collision with root package name */
    List f1566b;

    public N(ax.ab abVar, List list) {
        this.f1566b = new ArrayList();
        this.f1565a = abVar;
        this.f1566b = list;
    }

    public double a(ax.S s2) throws ax.U {
        int iRound = (int) Math.round(this.f1565a.b(s2));
        try {
            return ((ax.ab) this.f1566b.get(iRound)).b(s2);
        } catch (IndexOutOfBoundsException e2) {
            throw new ax.U("IndexOutOfBounds! index=" + iRound + ", function: " + toString());
        }
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("selectExpression( ");
        sb.append(this.f1565a.toString());
        for (ax.ab abVar : this.f1566b) {
            sb.append(", ");
            sb.append(abVar.toString());
        }
        sb.append(" )");
        return sb.toString();
    }
}
