package aa;

import G.C0073bf;
import G.R;
import W.InterfaceC0192r;
import W.N;
import W.O;
import java.io.BufferedWriter;
import java.io.IOException;
import org.icepdf.core.util.PdfOps;

/* renamed from: aa.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aa/f.class */
public class C0483f implements O {

    /* renamed from: a, reason: collision with root package name */
    static String f4174a = "TuningViews";

    /* renamed from: b, reason: collision with root package name */
    static String f4175b = "\n;This section allows TuningViews to be embedded in the ini file.\n     ;A tuningView entry requires 4 parameters. These entries are typically\n     ;only made by the application\n     ;Format:\n     ;   tuningView = referenceName, \"User Title\", [md5 of .tuneView file], {optional enableCondition}, base64 data\n";

    @Override // W.O
    public String a() {
        return f4174a;
    }

    @Override // W.O
    public String b() {
        return f4175b;
    }

    @Override // W.O
    public void a(R r2, BufferedWriter bufferedWriter, N n2, InterfaceC0192r interfaceC0192r) throws IOException {
        for (C0073bf c0073bf : r2.af()) {
            if (interfaceC0192r == null || interfaceC0192r.a(c0073bf)) {
                bufferedWriter.append("tuningView = ");
                bufferedWriter.append((CharSequence) c0073bf.aJ()).append(", ");
                bufferedWriter.append(PdfOps.DOUBLE_QUOTE__TOKEN).append((CharSequence) c0073bf.c()).append("\", ");
                bufferedWriter.append((CharSequence) c0073bf.a()).append(", ");
                bufferedWriter.append("{ ").append((CharSequence) (c0073bf.aH() != null ? c0073bf.aH() : "")).append(" }");
                bufferedWriter.newLine();
            }
        }
    }
}
