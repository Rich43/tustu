package bd;

import G.R;
import G.T;
import ai.C0512b;
import ai.InterfaceC0515e;

/* renamed from: bd.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bd/a.class */
public class C1067a implements InterfaceC0515e {
    @Override // ai.InterfaceC0515e
    public C0512b a() {
        R rC = T.a().c();
        return (rC == null || rC.i() == null || rC.i().length() == 0) ? b() : c(rC.i()) ? c() : b(rC.i()) ? e() : a(rC.i()) ? d() : b();
    }

    private C0512b b() {
        C0512b c0512b = new C0512b();
        c0512b.a("Additional Help");
        c0512b.b("/help/manuals.html");
        return c0512b;
    }

    private C0512b c() {
        C0512b c0512b = new C0512b();
        c0512b.a("B&G MegaSquirt Help");
        c0512b.b("/help/msManuals.html");
        return c0512b;
    }

    private C0512b d() {
        C0512b c0512b = new C0512b();
        c0512b.a("MSExtra Help");
        c0512b.b("/help/msExtraManuals.html");
        return c0512b;
    }

    private C0512b e() {
        C0512b c0512b = new C0512b();
        c0512b.a("MS3 Help");
        c0512b.b("/help/ms3Manuals.html");
        return c0512b;
    }

    private boolean a(String str) {
        return str.startsWith("MS2Extra") || str.startsWith("MS3") || str.startsWith("MS1/Extra") || str.startsWith("Trans") || str.startsWith("MSnS-extra") || str.indexOf("JimStim") != -1 || str.startsWith("SEQ Rev") || str.startsWith("IOExte") || str.startsWith("MS/Extra");
    }

    private boolean b(String str) {
        return str.startsWith("MS3") || str.startsWith("Trans");
    }

    private boolean c(String str) {
        return str.startsWith("Mega") || str.toLowerCase().indexOf("gpio") != -1 || str.equals("20") || str.startsWith("SEQ Rev") || str.startsWith("MShift") || str.startsWith("MSII");
    }
}
