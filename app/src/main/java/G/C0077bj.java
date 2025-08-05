package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: G.bj, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/bj.class */
public class C0077bj extends C0088bu implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f928a = null;

    /* renamed from: f, reason: collision with root package name */
    private String f929f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f930g = null;

    /* renamed from: h, reason: collision with root package name */
    private String[] f931h = null;

    /* renamed from: i, reason: collision with root package name */
    private C0128k f932i = null;

    /* renamed from: j, reason: collision with root package name */
    private ArrayList f933j = new ArrayList();

    /* renamed from: k, reason: collision with root package name */
    private boolean f934k = false;

    public String a() {
        return this.f928a;
    }

    public void a(String str) {
        this.f928a = str;
    }

    public String b() {
        return this.f929f;
    }

    public void a(String[] strArr) {
        this.f931h = strArr;
    }

    public void b(String str) {
        this.f929f = str;
    }

    public String c() {
        return this.f930g;
    }

    public void c(String str) {
        this.f930g = str;
    }

    public String[] d() {
        return this.f931h;
    }

    public C0128k f() {
        return this.f932i;
    }

    public void a(C0128k c0128k) {
        this.f932i = c0128k;
    }

    public void a(bU bUVar) {
        this.f933j.add(bUVar);
    }

    public Iterator g() {
        return this.f933j.iterator();
    }

    public boolean h() {
        return this.f934k;
    }

    public void a(boolean z2) {
        this.f934k = z2;
    }
}
