package L;

import ax.AbstractC0902e;
import ax.C0885A;
import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.List;
import javax.management.JMX;

/* renamed from: L.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/n.class */
public class C0157n extends AbstractC0902e {

    /* renamed from: e, reason: collision with root package name */
    private static C0157n f1674e = null;

    /* renamed from: f, reason: collision with root package name */
    private int f1676f = 0;

    /* renamed from: c, reason: collision with root package name */
    long f1678c = -1;

    /* renamed from: g, reason: collision with root package name */
    private int f1680g = 0;

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1675a = new C0158o(this);

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1677b = new C0159p(this);

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1679d = new C0160q(this);

    private C0157n() {
    }

    public static C0157n a() {
        if (f1674e == null) {
            f1674e = new C0157n();
        }
        return f1674e;
    }

    @Override // ax.AbstractC0902e
    public ax.ac a(String str, List list) throws C0885A {
        ax.ac acVarC = null;
        if (str.equalsIgnoreCase("smoothBasic")) {
            acVarC = b(str, list);
        } else if (str.equalsIgnoreCase("lastValue")) {
            acVarC = d(str, list);
        } else if (str.equalsIgnoreCase("pastValue")) {
            acVarC = h(str, list);
        } else if (str.equalsIgnoreCase("historicalValue")) {
            acVarC = i(str, list);
        } else if (str.equalsIgnoreCase("accumulate")) {
            acVarC = j(str, list);
        } else if (str.equalsIgnoreCase("isNaN")) {
            acVarC = q(str, list);
        } else if (str.equalsIgnoreCase("aerodynamicDragHp")) {
            acVarC = r(str, list);
        } else if (str.equalsIgnoreCase("accelHp")) {
            acVarC = s(str, list);
        } else if (str.equalsIgnoreCase("rollingDragHp")) {
            acVarC = t(str, list);
        } else if (str.equalsIgnoreCase("smoothFiltered")) {
            acVarC = u(str, list);
        } else if (str.equalsIgnoreCase(JMX.MAX_VALUE_FIELD)) {
            acVarC = v(str, list);
        } else if (str.equalsIgnoreCase(JMX.MIN_VALUE_FIELD)) {
            acVarC = w(str, list);
        } else if (str.equalsIgnoreCase("min")) {
            acVarC = g(str, list);
        } else if (str.equalsIgnoreCase("max")) {
            acVarC = e(str, list);
        } else if (str.equalsIgnoreCase("avg")) {
            acVarC = f(str, list);
        } else if (str.equalsIgnoreCase("highSpeedRecordNumber")) {
            acVarC = k(str, list);
        } else if (str.equalsIgnoreCase("selectExpression")) {
            acVarC = l(str, list);
        } else if (str.equalsIgnoreCase(Constants.ELEMNAME_IF_STRING)) {
            acVarC = m(str, list);
        } else if (str.equalsIgnoreCase(Keywords.FUNC_SUM_STRING)) {
            acVarC = n(str, list);
        } else if (str.equalsIgnoreCase("isTrueFor")) {
            acVarC = o(str, list);
        } else if (str.equalsIgnoreCase("timeTrue")) {
            acVarC = p(str, list);
        } else if (str.equalsIgnoreCase("toggle")) {
            acVarC = c(str, list);
        }
        return acVarC;
    }

    private ax.ac b(String str, List list) throws C0885A {
        if (list.size() != 2) {
            throw new C0885A(str, list.size(), 1);
        }
        return new P((ax.ab) list.get(0), (ax.ab) list.get(1));
    }

    private ax.ac c(String str, List list) throws C0885A {
        if (list.size() == 1 || list.size() == 2) {
            return list.size() == 2 ? new aa((ax.ab) list.get(0), (ax.ab) list.get(1)) : new aa((ax.ab) list.get(0));
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac d(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new F((ax.ab) list.get(0), this.f1675a);
    }

    private ax.ac e(String str, List list) throws C0885A {
        if (list.size() < 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new G(list);
    }

    private ax.ac f(String str, List list) throws C0885A {
        if (list.size() < 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0148e(list);
    }

    private ax.ac g(String str, List list) throws C0885A {
        if (list.size() < 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new I(list);
    }

    private ax.ac h(String str, List list) throws C0885A {
        if (list.size() != 2) {
            throw new C0885A(str, list.size(), 2);
        }
        return new K((ax.ab) list.get(0), (ax.ab) list.get(1));
    }

    private ax.ac i(String str, List list) throws C0885A {
        if (list.size() != 2) {
            throw new C0885A(str, list.size(), 2);
        }
        return new F((ax.ab) list.get(0), this.f1675a, (ax.ab) list.get(1));
    }

    private ax.ac j(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C0145b((ax.ab) list.get(0), this.f1675a);
    }

    private ax.ac k(String str, List list) throws C0885A {
        if (list.size() != 0) {
            throw new C0885A(str, list.size(), 1);
        }
        return new A(this.f1679d);
    }

    public void a(int i2) {
        this.f1676f = i2;
    }

    public void b(int i2) {
        this.f1680g = i2;
    }

    public void a(long j2) {
        this.f1678c = j2;
    }

    private ax.ac l(String str, List list) throws C0885A {
        if (list.size() >= 2) {
            return new N((ax.ab) list.remove(0), list);
        }
        throw new C0885A(str, list.size(), 2);
    }

    private ax.ac m(String str, List list) throws C0885A {
        if (list.size() == 3) {
            return new B(list);
        }
        throw new C0885A(str, list.size(), 3);
    }

    private ax.ac n(String str, List list) throws C0885A {
        if (list.size() >= 2) {
            return new Q(list);
        }
        throw new C0885A(str, list.size(), 2);
    }

    private ax.ac o(String str, List list) throws C0885A {
        if (list.size() == 2) {
            return new E((ax.ab) list.get(0), (ax.ab) list.get(1));
        }
        throw new C0885A(str + "\nProper Usage:\nisTrueFor([TrueFalseExpression], [SecondsTrueExpression])", list.size(), 2);
    }

    private ax.ac p(String str, List list) throws C0885A {
        if (list.size() == 1) {
            return new Z((ax.ab) list.get(0));
        }
        throw new C0885A(str + "\nProper Usage:\ntimeTrue([TrueFalseExpression])", list.size(), 2);
    }

    private ax.ac q(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new C((ax.ab) list.get(0));
    }

    private ax.ac r(String str, List list) throws C0885A {
        if (list.size() != 4) {
            throw new C0885A(str + "\nProper Usage:\naerodynamicDragHp(velocity (m/s), airDensity (kg/m^-3), dragCoefficent, frontalArea (m^2))", list.size(), 4);
        }
        return new C0146c((ax.ab) list.get(0), (ax.ab) list.get(1), (ax.ab) list.get(2), (ax.ab) list.get(3));
    }

    private ax.ac s(String str, List list) throws C0885A {
        if (list.size() != 4) {
            throw new C0885A(str + "\nProper Usage:\naccelHp(velocity (MPH), deltaVelocity(MPH), deltaTime(s), weight(lb))", list.size(), 4);
        }
        return new C0144a((ax.ab) list.get(0), (ax.ab) list.get(1), (ax.ab) list.get(2), (ax.ab) list.get(3));
    }

    private ax.ac t(String str, List list) throws C0885A {
        if (list.size() != 3) {
            throw new C0885A(str + "\nProper Usage:\nrollingDragHp(speed (MPH), tirePressure (psi), weight (lbs))", list.size(), 3);
        }
        return new M((ax.ab) list.get(0), (ax.ab) list.get(1), (ax.ab) list.get(2));
    }

    private ax.ac u(String str, List list) throws C0885A {
        if (list.size() != 1) {
            throw new C0885A(str, list.size(), 1);
        }
        return new O((ax.ab) list.get(0), this.f1675a);
    }

    private ax.ac v(String str, List list) throws C0885A {
        if (list.size() == 1 || list.size() == 2) {
            return new H(list);
        }
        throw new C0885A(str, list.size(), 1);
    }

    private ax.ac w(String str, List list) throws C0885A {
        if (list.size() == 1 || list.size() == 2) {
            return new J(list);
        }
        throw new C0885A(str, list.size(), 1);
    }
}
