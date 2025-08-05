package bH;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* renamed from: bH.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/y.class */
class C1017y implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C1015w f7064a;

    C1017y(C1015w c1015w) {
        this.f7064a = c1015w;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws NumberFormatException {
        this.f7064a.f7062b.setText(Float.intBitsToFloat(Integer.parseInt(W.b(W.b(W.b(this.f7064a.f7061a.getText(), PdfOps.DOUBLE_QUOTE__TOKEN, ""), " ", ""), LanguageTag.PRIVATEUSE, ""), 16)) + "");
    }
}
