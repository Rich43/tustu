package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* loaded from: TunerStudioMS.jar:aP/jq.class */
class jq implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    String f3803a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ jp f3804b;

    jq(jp jpVar, String str) {
        this.f3804b = jpVar;
        this.f3803a = str;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        com.efiAnalytics.ui.aN.a(this.f3803a);
    }
}
