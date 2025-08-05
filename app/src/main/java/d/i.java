package d;

import c.InterfaceC1386e;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:d/i.class */
public class i implements c.f {

    /* renamed from: c, reason: collision with root package name */
    private String f12101c;

    /* renamed from: d, reason: collision with root package name */
    private String f12102d;

    /* renamed from: e, reason: collision with root package name */
    private String f12103e = null;

    /* renamed from: f, reason: collision with root package name */
    private String f12104f = null;

    /* renamed from: g, reason: collision with root package name */
    private InterfaceC1386e f12105g = null;

    /* renamed from: h, reason: collision with root package name */
    private int f12106h = 1;

    /* renamed from: a, reason: collision with root package name */
    List f12107a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    i f12108b = null;

    public i(String str, String str2) {
        this.f12101c = str;
        this.f12102d = str2;
    }

    public String c() {
        return this.f12101c;
    }

    public String d() {
        return this.f12102d;
    }

    public String e() {
        return this.f12103e == null ? d() : this.f12103e;
    }

    public void a(String str) {
        this.f12103e = str;
    }

    public i a(i iVar, String str) {
        i iVar2 = new i(iVar.f12101c, str);
        iVar2.f12103e = iVar.f12103e;
        iVar2.f12105g = iVar.f12105g;
        iVar2.f12106h = iVar.f12106h;
        iVar2.f12107a = iVar.f12107a;
        iVar2.f12104f = iVar.f12104f;
        return iVar2;
    }

    public void b(String str) {
        this.f12102d = str;
    }

    @Override // c.f
    public int a() {
        return this.f12106h;
    }

    @Override // c.f
    public List b() {
        return this.f12107a;
    }

    public void a(List list) {
        this.f12107a = list;
    }

    public void a(int i2) {
        this.f12106h = i2;
    }

    public String f() {
        return this.f12104f;
    }

    public void c(String str) {
        this.f12104f = str;
    }
}
