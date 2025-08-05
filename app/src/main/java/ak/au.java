package ak;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:ak/au.class */
public class au extends C0546f {
    public au() {
        super(",", false);
    }

    @Override // ak.C0546f
    protected int b(String str) {
        return 0;
    }

    @Override // ak.C0546f, W.V
    public Iterator b() throws V.a {
        ArrayList arrayList = new ArrayList();
        Iterator itB = super.b();
        while (itB.hasNext()) {
            W.T t2 = (W.T) itB.next();
            if (!t2.a().startsWith("Clock")) {
                arrayList.add(t2);
            }
        }
        return arrayList.iterator();
    }

    @Override // ak.C0546f, W.V
    public float[] c() throws V.a {
        float[] fArrC = super.c();
        float[] fArr = new float[fArrC.length - 1];
        System.arraycopy(fArrC, 1, fArr, 0, fArr.length);
        return fArr;
    }
}
