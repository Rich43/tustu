package D;

import bH.C1005m;
import f.h;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:D/b.class */
public class b {
    public a a(String str, String str2) throws h {
        a aVar = new a();
        if (str2 == null || str2.isEmpty() || str == null || str.isEmpty()) {
            aVar.a(4);
        }
        aVar.b(str2);
        aVar.a(str);
        aVar.h(C1005m.a(d.a() + aVar.i("inquire")));
        return aVar;
    }

    public c a(a aVar) {
        c cVar = new c();
        try {
            String strA = C1005m.a(d.a() + aVar.i("act"));
            a aVar2 = new a();
            aVar2.h(strA);
            cVar.a(aVar2.i());
            cVar.a(aVar2);
        } catch (h e2) {
            cVar.a(1);
        } catch (IOException e3) {
            cVar.a(32768);
        }
        return cVar;
    }
}
