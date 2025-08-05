package ao;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ao/cB.class */
class cB implements ActionListener {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bP f5456a;

    cB(bP bPVar) {
        this.f5456a = bPVar;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) throws SecurityException {
        if ((actionEvent.getModifiers() & 1) == 0 || (actionEvent.getModifiers() & 2) == 0) {
            this.f5456a.d();
            return;
        }
        try {
            Desktop.getDesktop().edit(new File(((Object) h.h.a()) + File.separator + h.i.k() + "LogFile.txt"));
        } catch (IOException e2) {
            Logger.getLogger(bP.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
