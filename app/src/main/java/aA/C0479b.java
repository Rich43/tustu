package aa;

import G.B;
import G.C0043ac;
import G.R;
import G.dh;
import W.InterfaceC0192r;
import W.N;
import W.O;
import W.P;
import bH.W;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* renamed from: aa.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aa/b.class */
public class C0479b implements O {
    @Override // W.O
    public String a() {
        return "Datalog";
    }

    @Override // W.O
    public String b() {
        return "    ;  The entries are saved in the datalog file in the order in which they \n    ;  appear in the list below. \n    ;  \n    ;   Channel - Case sensitive name of output channel to be logged.\n    ;   Label   - String written to header line of log. \n    ;   Type    - normally float or int, no longer used.\n    ;   Format  - C-style output format of data or tag: \n    ;             Boolean tags: yesNo, onOff, highLow, activeInactive\n    ;             For Hex output: hex\n    ;   Enabled Cond - This field will only be logged if the enable \n    ;                 condition is blank or resolves to true. \n    ;   Lag      - If for any reason you need to have a field lag \n    ;                  n records behind realtime. Use a number or expression\n   ;       Channel          Label          Type    Format  Enabled Cond  Lag\n   ;       --------------   ----------     -----   ------  ------------  ---\n\n";
    }

    @Override // W.O
    public void a(R r2, BufferedWriter bufferedWriter, N n2, InterfaceC0192r interfaceC0192r) throws IOException {
        Iterator it = r2.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac = (C0043ac) it.next();
            if (interfaceC0192r == null || interfaceC0192r.a(c0043ac)) {
                a(c0043ac, bufferedWriter);
            }
        }
        bufferedWriter.write("\n");
        bufferedWriter.write("\n");
    }

    private void a(C0043ac c0043ac, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.append("     ");
        bufferedWriter.append("entry = ");
        bufferedWriter.append((CharSequence) c0043ac.a());
        bufferedWriter.append(", ");
        for (int length = c0043ac.a().length(); length < P.f1944a; length++) {
            bufferedWriter.append(' ');
        }
        bufferedWriter.append(PdfOps.DOUBLE_QUOTE__TOKEN).append((CharSequence) c0043ac.b()).append("\",");
        for (int length2 = c0043ac.b().length(); length2 < P.f1944a; length2++) {
            bufferedWriter.append(' ');
        }
        if (c0043ac.d() > 0) {
            bufferedWriter.write("float,   ");
        } else {
            bufferedWriter.write("int,     ");
        }
        bufferedWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
        if (c0043ac.e() != 0) {
            bufferedWriter.append((CharSequence) c0043ac.k());
        } else if (c0043ac.d() > 0) {
            bufferedWriter.append("%.").append((CharSequence) (c0043ac.d() + PdfOps.f_TOKEN));
        } else {
            bufferedWriter.append("%d");
        }
        bufferedWriter.write(PdfOps.DOUBLE_QUOTE__TOKEN);
        boolean z2 = false;
        if (c0043ac.aH() != null && !c0043ac.aH().trim().isEmpty()) {
            bufferedWriter.append(", {");
            bufferedWriter.append((CharSequence) c0043ac.aH().trim());
            bufferedWriter.append(" }");
            z2 = true;
        }
        if (c0043ac.g() != null && c0043ac.g().a() > 0.0d) {
            if (z2) {
                bufferedWriter.write(", ");
            } else {
                bufferedWriter.write(", {  }, ");
            }
            a(c0043ac.g(), bufferedWriter);
        }
        if (c0043ac.aI() != null && !c0043ac.aI().trim().isEmpty()) {
            bufferedWriter.write(VectorFormat.DEFAULT_SEPARATOR);
            bufferedWriter.write(c0043ac.aI());
        }
        bufferedWriter.append("\n");
    }

    private void a(dh dhVar, BufferedWriter bufferedWriter) {
        if (dhVar instanceof B) {
            bufferedWriter.append((CharSequence) W.a(Double.toString(dhVar.a()), ' ', P.f1945b));
            return;
        }
        bufferedWriter.append("{ ");
        bufferedWriter.append((CharSequence) dhVar.toString());
        bufferedWriter.append(" }");
    }
}
