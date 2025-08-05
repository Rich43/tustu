package aP;

import java.awt.HeadlessException;
import javax.bluetooth.RemoteDevice;
import javax.swing.JOptionPane;
import r.C1798a;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/L.class */
public class L {
    public void a() throws HeadlessException {
        for (RemoteDevice remoteDevice : aC.b.c()) {
            if (d(remoteDevice)) {
                bH.C.c("Found BT Adapter, prompting user: " + aC.b.a(remoteDevice));
                a(remoteDevice);
            } else {
                bH.C.c("Found BT Adapter, but marked for no prompt: " + aC.b.a(remoteDevice));
            }
        }
    }

    private void a(RemoteDevice remoteDevice) throws HeadlessException {
        String[] strArr = {C1818g.b("Yes"), C1818g.b("Ask Later"), C1818g.b("Do Not Ask Again")};
        int iShowOptionDialog = JOptionPane.showOptionDialog(cZ.a().c(), C1818g.b(C1798a.f13268b + " has found an up paired EFI Analytics Bluetooth adapter.") + "\n" + C1818g.b("Would you like " + C1798a.f13268b + " to pair with it now?") + "\n" + C1818g.b("Found") + ": " + aC.b.a(remoteDevice), C1818g.b("Pair EFIA Bluetooth"), -1, 3, null, strArr, strArr[0]);
        if (iShowOptionDialog != 0) {
            if (iShowOptionDialog == 2) {
                e(remoteDevice);
            }
        } else if (aC.b.a(remoteDevice, "1234")) {
            com.efiAnalytics.ui.bV.d(C1818g.b("successfully Paired with " + aC.b.a(remoteDevice)), cZ.a().c());
        } else {
            b(remoteDevice);
        }
    }

    private void b(RemoteDevice remoteDevice) {
        aX.c cVar = new aX.c();
        cVar.a(remoteDevice);
        cVar.a(cZ.a().c());
    }

    private String c(RemoteDevice remoteDevice) {
        return "autoPairWith" + remoteDevice.getBluetoothAddress();
    }

    private boolean d(RemoteDevice remoteDevice) {
        return C1798a.a().c(c(remoteDevice), true);
    }

    private void e(RemoteDevice remoteDevice) {
        C1798a.a().b(c(remoteDevice), Boolean.toString(false));
    }

    public void a(int i2) {
        new M(this, i2).start();
    }
}
