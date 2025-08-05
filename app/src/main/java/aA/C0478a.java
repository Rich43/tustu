package aa;

import G.R;
import G.aM;
import W.InterfaceC0192r;
import W.M;
import W.N;
import W.O;
import bH.W;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* renamed from: aa.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aa/a.class */
public class C0478a implements O {

    /* renamed from: a, reason: collision with root package name */
    private final Map f4171a = new HashMap();

    public C0478a(List list) {
        a(list);
    }

    public void a(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Y.d dVar = (Y.d) it.next();
            this.f4171a.put(dVar.b(), dVar);
        }
    }

    @Override // W.O
    public String a() {
        return "Constants";
    }

    @Override // W.O
    public String b() {
        return "";
    }

    @Override // W.O
    public void a(R r2, BufferedWriter bufferedWriter, N n2, InterfaceC0192r interfaceC0192r) throws IOException {
        if (n2 == null || !n2.a().equals(a())) {
            throw new IOException("Invalid section. This INI Section writer only supports " + a());
        }
        int i2 = 0;
        Iterator it = n2.iterator();
        while (it.hasNext()) {
            M m2 = (M) it.next();
            String strA = m2.a();
            aM aMVarC = (m2.d() || m2.f() == null) ? null : r2.c(m2.f());
            if (aMVarC != null && m2.f() != null && this.f4171a.containsKey(m2.f())) {
                Y.d dVar = (Y.d) this.f4171a.get(m2.f());
                String[] strArrSplit = strA.split(",");
                if (strArrSplit.length > 2 && dVar != null) {
                    strArrSplit[2] = "  0x" + W.a(Integer.toHexString(dVar.a() - r2.O().y(aMVarC.d())).toUpperCase(), '0', 4);
                    StringBuilder sb = new StringBuilder();
                    for (int i3 = 0; i3 < strArrSplit.length; i3++) {
                        sb.append(strArrSplit[i3]);
                        if (i3 < strArrSplit.length - 1) {
                            sb.append(",");
                        }
                    }
                    strA = sb.toString();
                }
            } else if (aMVarC == null) {
            }
            i2 = strA.trim().isEmpty() ? i2 + 1 : 0;
            if (i2 < 5) {
                bufferedWriter.append((CharSequence) strA).append("\n");
            }
        }
        bufferedWriter.flush();
    }
}
