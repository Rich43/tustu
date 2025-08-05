package G;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:G/bF.class */
public class bF extends bE implements Serializable {

    /* renamed from: b, reason: collision with root package name */
    ArrayList f836b = new ArrayList();

    @Override // G.bE
    public String g() {
        return "thermGenerator";
    }

    public void a(bG bGVar) {
        this.f836b.add(bGVar);
    }

    public Iterator a() {
        return this.f836b.iterator();
    }
}
