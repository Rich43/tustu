package av;

import G.C0135r;
import G.R;
import G.T;
import G.aL;
import W.C0172ab;
import ao.hF;
import ao.hH;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/g.class */
public abstract class AbstractC0868g implements hF {

    /* renamed from: b, reason: collision with root package name */
    protected R f6284b = null;

    /* renamed from: c, reason: collision with root package name */
    protected Map f6285c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    protected InterfaceC0877p f6286d = null;

    protected void a(String str, String str2) throws V.h {
        a(str, str2, null);
    }

    protected void a(String str, String str2, C0135r[] c0135rArr) throws V.h {
        T.a().b(str);
        this.f6284b = new R();
        this.f6284b.a(str);
        this.f6284b.q(new File(str2).getAbsolutePath());
        if (c0135rArr != null) {
            for (C0135r c0135r : c0135rArr) {
                this.f6284b.a(c0135r);
            }
        }
        try {
            this.f6284b = new C0172ab().a(this.f6284b, str2, true, C0172ab.f2059i);
            aL.a(this.f6284b);
            T.a().a(this.f6284b);
            T.a().a(str);
        } catch (V.a e2) {
            Logger.getLogger(AbstractC0868g.class.getName()).log(Level.SEVERE, "Error loading config.", (Throwable) e2);
            throw new V.h(e2.getMessage());
        } catch (V.g e3) {
            Logger.getLogger(AbstractC0868g.class.getName()).log(Level.SEVERE, "Error Setting Working Config.", (Throwable) e3);
            throw new V.h(e3.getMessage());
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x005f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void g() {
        /*
            Method dump skipped, instructions count: 450
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: av.AbstractC0868g.g():void");
    }

    @Override // ao.hF
    public Iterator c() {
        return this.f6285c.keySet().iterator();
    }

    @Override // ao.hF
    public int d() {
        return this.f6285c.size();
    }

    @Override // ao.hF
    public hH b(String str) {
        return (hH) this.f6285c.get(str);
    }

    @Override // ao.hF
    public void e() {
    }

    @Override // ao.hF
    public boolean b() {
        return this.f6284b.h().aM();
    }

    @Override // ao.hF
    public void a() {
        this.f6284b.h().g();
        Iterator it = this.f6285c.values().iterator();
        while (it.hasNext()) {
            ((C0870i) it.next()).q();
        }
    }

    public R h() {
        return this.f6284b;
    }

    @Override // ao.hF
    public boolean k(String str) {
        return false;
    }

    protected void a(InterfaceC0877p interfaceC0877p) {
        this.f6286d = interfaceC0877p;
    }
}
