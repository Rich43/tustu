package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;

/* renamed from: G.x, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/x.class */
public class C0141x implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    private String f1327a = Action.DEFAULT;

    /* renamed from: b, reason: collision with root package name */
    private List f1328b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private int f1329c = 0;

    public String a() {
        return this.f1327a;
    }

    public void a(String str) {
        this.f1327a = str;
    }

    public void a(C0142y c0142y) {
        this.f1328b.add(c0142y);
    }

    public List b() {
        return this.f1328b;
    }

    public int c() {
        return this.f1329c;
    }

    public void a(int i2) {
        this.f1329c = i2;
    }
}
