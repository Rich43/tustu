package d;

import c.InterfaceC1386e;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

/* loaded from: TunerStudioMS.jar:d/k.class */
public class k extends ArrayList {

    /* renamed from: a, reason: collision with root package name */
    k f12109a = null;

    /* renamed from: b, reason: collision with root package name */
    private InterfaceC1386e f12110b = null;

    /* renamed from: c, reason: collision with root package name */
    private String f12111c = null;

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public boolean add(i iVar) {
        return super.add(iVar);
    }

    public Properties a() {
        Properties properties = new Properties();
        Iterator it = iterator();
        while (it.hasNext()) {
            i iVar = (i) it.next();
            if (iVar.c() != null && iVar.d() != null) {
                properties.setProperty(iVar.c(), iVar.d());
            }
        }
        if (this.f12109a != null) {
            properties.putAll(this.f12109a.a());
        }
        return properties;
    }

    public i a(String str) {
        Iterator it = iterator();
        while (it.hasNext()) {
            i iVar = (i) it.next();
            if (iVar.c().equals(str)) {
                return iVar;
            }
        }
        return null;
    }
}
