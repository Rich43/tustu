package br;

import aP.cZ;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/* renamed from: br.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/f.class */
public class C1242f {

    /* renamed from: b, reason: collision with root package name */
    private static C1242f f8449b = null;

    /* renamed from: a, reason: collision with root package name */
    List f8450a = new ArrayList();

    public static C1242f a() {
        if (f8449b == null) {
            f8449b = new C1242f();
        }
        return f8449b;
    }

    public void a(al alVar) {
        this.f8450a.add(alVar);
    }

    public void b() {
        this.f8450a.clear();
    }

    public List c() {
        ArrayList arrayList = new ArrayList();
        for (al alVar : this.f8450a) {
            if (alVar instanceof C1255s) {
                arrayList.add(((C1255s) alVar).e());
            }
        }
        return arrayList;
    }

    public synchronized void a(String str) throws V.a {
        if (cZ.a().o() == null) {
            throw new V.a("VE Analyze Tabs unavailable.");
        }
        cZ.a().o().a();
        for (al alVar : this.f8450a) {
            if (alVar instanceof C1255s) {
                C1255s c1255s = (C1255s) alVar;
                if (c1255s.e().equals(str)) {
                    c1255s.a(!c1255s.a());
                    return;
                }
            }
        }
        bH.C.b("Could not toggle AutoTune as table not found for table: " + str);
    }

    public synchronized void a(int i2) throws V.a {
        if (cZ.a().o() == null) {
            throw new V.a("VE Analyze Tabs unavailable.");
        }
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new RunnableC1243g(this, i2));
            return;
        }
        cZ.a().o().a();
        try {
            ((al) this.f8450a.get(i2)).a(true);
        } catch (Exception e2) {
            throw new V.a("Failed to start AutoTune for table index: " + i2 + "\nError: " + e2.getLocalizedMessage());
        }
    }

    public synchronized void b(int i2) throws V.a {
        if (cZ.a().o() == null) {
            throw new V.a("VE Analyze Tabs unavailable.");
        }
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new RunnableC1244h(this, i2));
            return;
        }
        cZ.a().o().a();
        try {
            ((al) this.f8450a.get(i2)).a(false);
        } catch (Exception e2) {
            throw new V.a("Failed to stop AutoTune for table index: " + i2 + "\nError: " + e2.getLocalizedMessage());
        }
    }
}
