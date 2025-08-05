package aP;

import bH.InterfaceC1010r;
import java.awt.HeadlessException;
import javax.swing.JOptionPane;

/* loaded from: TunerStudioMS.jar:aP/hK.class */
class hK implements InterfaceC1010r {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hJ f3534a;

    hK(hJ hJVar) {
        this.f3534a = hJVar;
    }

    @Override // bH.InterfaceC1010r
    public void a(String str, Exception exc, Object obj) throws HeadlessException {
        if (exc != null) {
            System.out.println("\t" + exc.getMessage());
            exc.printStackTrace();
            if (exc.getMessage() != null && str.indexOf(exc.getMessage()) == -1) {
                str = str + "\nReported Error:\n" + exc.getMessage();
            }
        }
        JOptionPane.showMessageDialog(cZ.a().c(), str);
    }
}
