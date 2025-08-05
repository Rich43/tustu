package aR;

import G.T;
import aP.cZ;
import br.C1242f;
import d.InterfaceC1711c;
import java.util.ArrayList;
import java.util.Properties;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aR/y.class */
public class y implements InterfaceC1711c {

    /* renamed from: a, reason: collision with root package name */
    public static String f3888a = "stopAutoTune";

    /* renamed from: b, reason: collision with root package name */
    public static String f3889b = "tableIndex";

    /* renamed from: c, reason: collision with root package name */
    d.k f3890c = new d.k();

    @Override // d.InterfaceC1711c
    public String a() {
        return f3888a;
    }

    @Override // d.InterfaceC1711c
    public String b() {
        return C1818g.b("Stop Auto Tune");
    }

    @Override // d.InterfaceC1711c
    public String c() {
        return "Project";
    }

    @Override // d.InterfaceC1711c
    public boolean d() {
        return (T.a().c() == null || aE.a.A() == null) ? false : true;
    }

    @Override // d.InterfaceC1711c
    public void a(Properties properties) throws d.e {
        String property = properties.getProperty(f3889b);
        if (property == null) {
            throw new d.e("Table Index is required.");
        }
        try {
            try {
                C1242f.a().b(Integer.parseInt(property));
            } catch (V.a e2) {
                throw new d.e(e2.getLocalizedMessage());
            }
        } catch (Exception e3) {
            throw new d.e("Invalid Table Index: " + property);
        }
    }

    @Override // d.InterfaceC1711c
    public void b(Properties properties) throws d.e {
        String property = properties.getProperty(f3889b);
        if (property == null) {
            throw new d.e("Table Index is required.");
        }
        try {
            int i2 = Integer.parseInt(property);
            if (cZ.a().o() != null) {
                if (i2 < 0 || i2 > cZ.a().o().c()) {
                    throw new d.e("Invalid Table Index: " + property);
                }
            }
        } catch (Exception e2) {
            throw new d.e("Invalid Table Index: " + property);
        }
    }

    @Override // d.InterfaceC1711c
    public d.k e() {
        this.f3890c.clear();
        d.i iVar = new d.i(f3889b, "");
        iVar.a(0);
        if (cZ.a().o() != null) {
            int iC = cZ.a().o().c();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 <= iC; i2++) {
                arrayList.add(Integer.toString(i2));
            }
            iVar.a(arrayList);
        }
        iVar.c("This is the index of the Tab in which you wish to start Auto Tune on. It starts with tab 0 and increase as you move to the right.");
        this.f3890c.add(iVar);
        return this.f3890c;
    }

    @Override // d.InterfaceC1711c
    public boolean f() {
        return true;
    }

    @Override // d.InterfaceC1711c
    public boolean g() {
        return true;
    }
}
