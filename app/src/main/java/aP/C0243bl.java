package aP;

import G.C0088bu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.CRC32;
import x.C1891a;

/* renamed from: aP.bl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/bl.class */
class C0243bl implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1891a f3080a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ G.R f3081b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0088bu f3082c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ C0240bi f3083d;

    C0243bl(C0240bi c0240bi, C1891a c1891a, G.R r2, C0088bu c0088bu) {
        this.f3083d = c0240bi;
        this.f3080a = c1891a;
        this.f3081b = r2;
        this.f3082c = c0088bu;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        String strA = com.efiAnalytics.ui.bV.a(this.f3080a, "Password to access this dialog.");
        if (strA == null || strA.length() <= 0) {
            return;
        }
        CRC32 crc32 = new CRC32();
        crc32.reset();
        crc32.update(strA.getBytes());
        G.aM aMVarC = this.f3081b.c(this.f3082c.aa());
        if (aMVarC == null) {
            com.efiAnalytics.ui.bV.d("Invalid Password Parameter.", this.f3080a);
            return;
        }
        try {
            aMVarC.a(this.f3081b.h(), crc32.getValue());
        } catch (V.g e2) {
            Logger.getLogger(C0240bi.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        } catch (V.j e3) {
            Logger.getLogger(C0240bi.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            com.efiAnalytics.ui.bV.d("Invalid Password?", this.f3080a);
        }
        com.efiAnalytics.tuningwidgets.panels.W.a();
    }
}
