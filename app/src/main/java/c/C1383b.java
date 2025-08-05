package c;

import G.InterfaceC0042ab;
import G.R;
import G.S;
import G.T;
import V.g;
import bH.C;
import bH.C1007o;
import java.util.ArrayList;
import java.util.List;

/* renamed from: c.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:c/b.class */
public class C1383b implements S, InterfaceC0042ab {

    /* renamed from: c, reason: collision with root package name */
    private static C1383b f9258c = null;

    /* renamed from: d, reason: collision with root package name */
    private C1384c f9259d;

    /* renamed from: a, reason: collision with root package name */
    int f9257a = 300;

    /* renamed from: b, reason: collision with root package name */
    List f9260b = new ArrayList();

    private C1383b() {
        this.f9259d = null;
        for (String str : T.a().d()) {
            try {
                T.a().c(str).h().a(this);
            } catch (Exception e2) {
                C.c("Failed to enable EnableByConditionManager for " + str);
            }
        }
        T.a().a(this);
        this.f9259d = new C1384c(this);
        this.f9259d.start();
    }

    public static C1383b a() {
        if (f9258c == null) {
            f9258c = new C1383b();
        }
        return f9258c;
    }

    @Override // G.S
    public void a(R r2) {
    }

    @Override // G.S
    public void b(R r2) {
        r2.h().b(this);
        this.f9260b.clear();
    }

    @Override // G.S
    public void c(R r2) {
        r2.h().a(this);
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        this.f9259d.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        for (InterfaceC1385d interfaceC1385d : this.f9260b) {
            if (interfaceC1385d.a_() != null && !interfaceC1385d.a_().isEmpty()) {
                try {
                    boolean zA = C1007o.a(interfaceC1385d.a_(), interfaceC1385d.b_());
                    if (interfaceC1385d.isEnabled() != zA) {
                        interfaceC1385d.setEnabled(zA);
                    }
                } catch (g e2) {
                    C.a("Unable to evaluate enable condition for " + interfaceC1385d.getClass().getName() + ": " + interfaceC1385d.a_());
                }
            }
        }
    }

    public void a(InterfaceC1385d interfaceC1385d) {
        this.f9260b.add(interfaceC1385d);
        this.f9259d.a();
    }
}
