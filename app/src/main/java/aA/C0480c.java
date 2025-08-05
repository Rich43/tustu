package aa;

import G.C0073bf;
import G.R;
import G.bO;
import W.InterfaceC0192r;
import W.N;
import W.O;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;

/* renamed from: aa.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aa/c.class */
public class C0480c implements O {

    /* renamed from: a, reason: collision with root package name */
    static String f4172a = "EncodedData";

    /* renamed from: b, reason: collision with root package name */
    static String f4173b = ";This section allows for objects to be stored in the ini.\n     ;This section is typically managed only by the application.\n     ;Each record contains a reference name and the encoded data\n     ;Format:\n     ;   referenceName = base64Data";

    @Override // W.O
    public String a() {
        return f4172a;
    }

    @Override // W.O
    public String b() {
        return f4173b;
    }

    @Override // W.O
    public void a(R r2, BufferedWriter bufferedWriter, N n2, InterfaceC0192r interfaceC0192r) throws IOException {
        for (bO bOVar : r2.ag()) {
            if (a(r2, bOVar) && (interfaceC0192r == null || interfaceC0192r.a(bOVar))) {
                bufferedWriter.append((CharSequence) bOVar.aJ()).append(" = ");
                bufferedWriter.append((CharSequence) bOVar.b());
                bufferedWriter.newLine();
            }
        }
    }

    private boolean a(R r2, bO bOVar) {
        Iterator it = r2.af().iterator();
        while (it.hasNext()) {
            if (((C0073bf) it.next()).aJ().equals(bOVar.aJ())) {
                return true;
            }
        }
        return false;
    }
}
