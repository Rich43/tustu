package W;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: W.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/b.class */
public abstract class AbstractC0176b extends V {

    /* renamed from: e, reason: collision with root package name */
    private BufferedInputStream f2114e = null;

    /* renamed from: f, reason: collision with root package name */
    private C0177c f2115f = null;

    /* renamed from: g, reason: collision with root package name */
    private byte[] f2116g = null;

    /* renamed from: h, reason: collision with root package name */
    private byte[] f2117h = null;

    /* renamed from: i, reason: collision with root package name */
    private byte[] f2118i = null;

    /* renamed from: a, reason: collision with root package name */
    long f2119a = 0;

    /* renamed from: b, reason: collision with root package name */
    int f2120b = 0;

    @Override // W.V
    public boolean a(String str) throws V.a {
        try {
            File file = new File(str);
            this.f2114e = new BufferedInputStream(new FileInputStream(file));
            this.f2114e.skip(this.f2115f.b());
            this.f2119a = file.length();
            if (this.f2115f.f() > 0) {
                this.f2116g = new byte[this.f2115f.f()];
            }
            this.f2117h = new byte[this.f2115f.e()];
            this.f2118i = new byte[this.f2115f.f() + this.f2115f.e()];
            return true;
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            throw new V.a("File not found:\n" + str);
        } catch (IOException e3) {
            e3.printStackTrace();
            throw new V.a("Unable to open file:\n" + str);
        }
    }

    @Override // W.V
    public void a() {
        try {
            this.f2114e.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    @Override // W.V
    public Iterator b() {
        ArrayList arrayList = new ArrayList();
        Iterator itD = this.f2115f.d();
        while (itD.hasNext()) {
            arrayList.add(itD.next());
        }
        return arrayList.iterator();
    }

    @Override // W.V
    public float[] c() throws V.a {
        try {
            if (this.f2116g != null) {
                int i2 = this.f2114e.read(this.f2116g);
                if (this.f2115f.g() != null) {
                    int iA = (int) this.f2115f.g().a(this.f2116g);
                    while (iA != this.f2117h.length && i2 > 0) {
                        System.out.println("Skipping:" + iA);
                        this.f2114e.skip(iA);
                        i2 = this.f2114e.read(this.f2116g);
                        iA = (int) this.f2115f.g().a(this.f2116g);
                    }
                }
            }
            this.f2114e.read(this.f2117h);
            if (this.f2116g != null) {
                System.arraycopy(this.f2116g, 0, this.f2118i, 0, this.f2116g.length);
                System.arraycopy(this.f2117h, 0, this.f2118i, this.f2116g.length, this.f2117h.length);
            } else {
                this.f2118i = this.f2117h;
            }
            float[] fArr = new float[this.f2115f.a()];
            for (int i3 = 0; i3 < this.f2115f.a(); i3++) {
                fArr[i3] = this.f2115f.a(i3).a(this.f2118i);
            }
            this.f2120b++;
            return fArr;
        } catch (IOException e2) {
            e2.printStackTrace();
            throw new V.a("Error reading from file. Stopping.");
        }
    }

    @Override // W.V
    public long d() {
        return (this.f2119a - this.f2115f.b()) / this.f2115f.c();
    }

    @Override // W.V
    public boolean e() {
        return ((long) ((this.f2120b * this.f2115f.c()) + this.f2115f.b())) < this.f2119a - ((long) this.f2115f.c());
    }

    public void a(C0177c c0177c) {
        this.f2115f = c0177c;
    }

    @Override // W.V
    public boolean f() {
        return true;
    }

    @Override // W.V
    public HashMap g() {
        return null;
    }

    @Override // W.V
    public String h() {
        return null;
    }
}
