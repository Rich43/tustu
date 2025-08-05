package aa;

import G.B;
import G.C0048ah;
import G.C0094c;
import G.R;
import G.cZ;
import G.dh;
import V.g;
import W.InterfaceC0192r;
import W.N;
import W.O;
import W.P;
import bH.W;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.geometry.VectorFormat;
import org.icepdf.core.util.PdfOps;

/* renamed from: aa.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aa/d.class */
public class C0481d implements O {
    @Override // W.O
    public String a() {
        return "GaugeConfigurations";
    }

    @Override // W.O
    public String b() {
        return "    ;-------------------------------------------------------------------------------\n    ; Define a gauge's characteristics here, then go to a specific layout\n    ; block (Tuning or FrontPage) and use the name you've defined here to\n    ; display that gauge in a particular position.\n    ;\n    ; Name  = Case-sensitive, user-defined name for this gauge configuration.\n    ; Var   = Case-sensitive name of variable to be displayed, see the\n    ;         OutputChannels block in this file for possible values.\n    ; Title = Title displayed at the top of the gauge.\n    ; Units = Units displayed below value on gauge.\n    ; Lo    = Lower scale limit of gauge.\n    ; Hi    = Upper scale limit of gauge.\n    ; LoD   = Lower limit at which danger color is used for gauge background.\n    ; LoW   = Lower limit at which warning color is used.\n    ; HiW   = Upper limit at which warning color is used.\n    ; HiD   = Upper limit at which danger color is used.\n    ; vd    = Decimal places in displayed value\n    ; ld    = Label decimal places for display of Lo and Hi, above.\n\n    ; The following can be either numeric values or an expression:\n    ; Lo, Hi, LoD, LoW, HiW, HiD, ld\n    ; For title and Units TunerStudio String functions can be used.\n\n    ;Name               Var            Title                 Units     Lo     Hi     LoD    LoW   HiW   HiD vd ld";
    }

    @Override // W.O
    public void a(R r2, BufferedWriter bufferedWriter, N n2, InterfaceC0192r interfaceC0192r) {
        HashMap map = new HashMap();
        ArrayList arrayList = new ArrayList();
        Iterator itB = r2.B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (interfaceC0192r == null || interfaceC0192r.a(c0048ah)) {
                ArrayList arrayList2 = (ArrayList) map.get(c0048ah.p());
                if (arrayList2 == null) {
                    arrayList2 = new ArrayList();
                    if (c0048ah.p() == null) {
                        map.put("", arrayList2);
                    } else {
                        map.put(c0048ah.p().trim(), arrayList2);
                    }
                }
                arrayList2.add(c0048ah);
                if (c0048ah.p() != null) {
                    arrayList.add(c0048ah.p().trim());
                } else {
                    arrayList.add("");
                }
            }
        }
        if (map.get("") != null) {
            Iterator it = ((ArrayList) map.get("")).iterator();
            while (it.hasNext()) {
                a((C0048ah) it.next(), bufferedWriter);
            }
        }
        for (String str : map.keySet()) {
            if (!str.equals("")) {
                bufferedWriter.write("    ");
                bufferedWriter.write("gaugeCategory = \"");
                bufferedWriter.write(str);
                bufferedWriter.write("\"\n");
                Iterator it2 = ((ArrayList) map.get(str)).iterator();
                while (it2.hasNext()) {
                    a((C0048ah) it2.next(), bufferedWriter);
                }
            }
        }
        bufferedWriter.write("\n");
        bufferedWriter.write("\n");
    }

    private void a(C0048ah c0048ah, BufferedWriter bufferedWriter) {
        bufferedWriter.append("     ");
        bufferedWriter.append((CharSequence) c0048ah.aJ());
        for (int length = c0048ah.aJ().length(); length < P.f1944a; length++) {
            bufferedWriter.append(' ');
        }
        bufferedWriter.append("= ");
        bufferedWriter.append((CharSequence) c0048ah.i());
        for (int length2 = c0048ah.i().length(); length2 < P.f1944a; length2++) {
            bufferedWriter.append(' ');
        }
        bufferedWriter.append(", ");
        cZ cZVarK = c0048ah.k();
        for (int length3 = cZVarK.toString().length(); length3 < P.f1945b; length3++) {
            bufferedWriter.append(' ');
        }
        if (cZVarK instanceof C0094c) {
            try {
                bufferedWriter.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                bufferedWriter.append((CharSequence) cZVarK.a());
                bufferedWriter.append(PdfOps.DOUBLE_QUOTE__TOKEN);
            } catch (g e2) {
                Logger.getLogger(P.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                bufferedWriter.append("\"Error\"");
            }
        } else {
            bufferedWriter.append("{ ");
            bufferedWriter.append((CharSequence) cZVarK.toString());
            bufferedWriter.append(" }");
        }
        bufferedWriter.append(", ");
        cZ cZVarJ = c0048ah.j();
        for (int length4 = cZVarJ.toString().length(); length4 < P.f1945b; length4++) {
            bufferedWriter.append(' ');
        }
        if (cZVarJ instanceof C0094c) {
            try {
                bufferedWriter.append(PdfOps.DOUBLE_QUOTE__TOKEN);
                bufferedWriter.append((CharSequence) cZVarJ.a());
                bufferedWriter.append(PdfOps.DOUBLE_QUOTE__TOKEN);
            } catch (g e3) {
                Logger.getLogger(P.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                bufferedWriter.append("\"Error\"");
            }
        } else {
            bufferedWriter.append("{ ");
            bufferedWriter.append((CharSequence) cZVarJ.toString());
            bufferedWriter.append(" }");
        }
        bufferedWriter.append(", ");
        a(c0048ah.b(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.e(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.o(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.f(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.g(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.h(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.m(), bufferedWriter);
        bufferedWriter.append(", ");
        a(c0048ah.m(), bufferedWriter);
        if (c0048ah.aH() != null && !c0048ah.aH().trim().isEmpty()) {
            bufferedWriter.append(", {");
            bufferedWriter.append((CharSequence) c0048ah.aH().trim());
            bufferedWriter.append(" }");
        }
        if (c0048ah.aI() != null && !c0048ah.aI().trim().isEmpty()) {
            bufferedWriter.write(VectorFormat.DEFAULT_SEPARATOR);
            bufferedWriter.write(c0048ah.aI());
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
