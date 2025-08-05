package aP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/* loaded from: TunerStudioMS.jar:aP/iF.class */
class iF implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iE f3644a;

    iF(iE iEVar) {
        this.f3644a = iEVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        com.efiAnalytics.ui.aN.a("file:///" + new File(".").getAbsolutePath() + "/help/learnMore.html");
    }
}
