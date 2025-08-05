package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:G/bD.class */
public class bD extends Q implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f829a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    HashMap f830b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    HashMap f831c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    Map f832d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f833e = new ArrayList();

    public void a(C0053am c0053am) {
        this.f833e.add(c0053am);
    }

    public List a() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f833e);
        return arrayList;
    }

    public void a(bK bKVar) throws V.g {
        if (bKVar == null || bKVar.aJ() == null) {
            throw new V.g("Can not add wheelPattern unless it has a valid name.");
        }
        this.f832d.put(bKVar.aJ(), bKVar);
    }

    public bK a(String str) {
        return (bK) this.f832d.get(str);
    }

    public void a(C0050aj c0050aj) {
        this.f831c.put(c0050aj.a(), c0050aj);
    }

    public C0050aj b(String str) {
        return (C0050aj) this.f831c.get(str);
    }

    public void a(aA aAVar) {
        this.f829a.add(aAVar);
    }

    public Iterator b() {
        return this.f829a.iterator();
    }

    public void a(C0088bu c0088bu) {
        this.f830b.put(c0088bu.aJ(), c0088bu);
    }

    public C0088bu c(String str) {
        return (C0088bu) this.f830b.get(str);
    }

    public C0076bi d(String str) {
        for (C0088bu c0088bu : this.f830b.values()) {
            if (c0088bu instanceof C0076bi) {
                C0076bi c0076bi = (C0076bi) c0088bu;
                if (c0076bi.a() != null && c0076bi.a().equals(str)) {
                    return c0076bi;
                }
            }
        }
        return null;
    }

    public Iterator c() {
        return this.f830b.values().iterator();
    }
}
