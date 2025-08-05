package L;

import ax.AbstractC0902e;
import ax.C0885A;
import java.util.List;
import javax.management.JMX;

/* renamed from: L.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/m.class */
public class C0156m extends AbstractC0902e {

    /* renamed from: a, reason: collision with root package name */
    private static C0156m f1673a = null;

    public C0156m() {
        bH.C.c("Dummy Function Factory Created.");
    }

    @Override // ax.AbstractC0902e
    public ax.ac a(String str, List list) throws C0885A {
        ax.ac acVarB = null;
        if (str.equalsIgnoreCase("smoothBasic") || str.equalsIgnoreCase("lastValue") || str.equalsIgnoreCase("pastValue") || str.equalsIgnoreCase("historicalValue") || str.equalsIgnoreCase("accumulate") || str.equalsIgnoreCase("isNaN") || str.equalsIgnoreCase("aerodynamicDragHp") || str.equalsIgnoreCase("accelHp") || str.equalsIgnoreCase("rollingDragHp") || str.equalsIgnoreCase("smoothFiltered") || str.equalsIgnoreCase(JMX.MAX_VALUE_FIELD) || str.equalsIgnoreCase(JMX.MIN_VALUE_FIELD) || str.equalsIgnoreCase("getLogTime") || str.equalsIgnoreCase("min") || str.equalsIgnoreCase("max") || str.equalsIgnoreCase("avg") || str.equalsIgnoreCase("isTrueFor") || str.equalsIgnoreCase("timeTrue") || str.equalsIgnoreCase("toggle")) {
            acVarB = b(str, list);
        } else if (str.equalsIgnoreCase("cosXXXX")) {
        }
        if (acVarB == null) {
            acVarB = C0157n.a().a(str, list);
        }
        return acVarB;
    }

    private ax.ac b(String str, List list) {
        return new C0150g(str);
    }
}
