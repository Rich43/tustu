package bx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: bx.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bx/a.class */
public abstract class AbstractC1375a implements InterfaceC1376b {

    /* renamed from: a, reason: collision with root package name */
    private ArrayList f9186a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private List f9187b = new ArrayList();

    @Override // bx.InterfaceC1376b
    public ArrayList d() {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.f9186a);
        return arrayList;
    }

    @Override // bx.InterfaceC1376b
    public boolean a(String str) {
        for (int i2 = 0; i2 < this.f9186a.size(); i2++) {
            if (((j) this.f9186a.get(i2)).a().equals(str)) {
                this.f9186a.remove(i2);
                b(str);
                e();
                return true;
            }
        }
        return false;
    }

    @Override // bx.InterfaceC1376b
    public void b(j jVar) {
        for (int i2 = 0; i2 < this.f9186a.size(); i2++) {
            if (((j) this.f9186a.get(i2)).a().equals(jVar.a())) {
                this.f9186a.set(i2, jVar);
                d(jVar);
                e();
                return;
            }
        }
        this.f9186a.add(jVar);
        c(jVar);
        e();
    }

    public void e() {
        b(this.f9186a);
    }

    protected abstract void b(ArrayList arrayList);

    public void f() {
        a(this.f9186a);
    }

    protected abstract void a(ArrayList arrayList);

    @Override // bx.InterfaceC1376b
    public void a(l lVar) {
        this.f9187b.add(lVar);
    }

    private void c(j jVar) {
        Iterator it = this.f9187b.iterator();
        while (it.hasNext()) {
            ((l) it.next()).a(jVar);
        }
    }

    private void d(j jVar) {
        Iterator it = this.f9187b.iterator();
        while (it.hasNext()) {
            ((l) it.next()).b(jVar);
        }
    }

    private void b(String str) {
        Iterator it = this.f9187b.iterator();
        while (it.hasNext()) {
            ((l) it.next()).a(str);
        }
    }
}
