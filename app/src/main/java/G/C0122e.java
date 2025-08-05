package G;

import com.intel.bluetooth.BluetoothConsts;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/e.class */
public class C0122e {
    public static void a(R r2) {
        J jC = r2.C();
        F fO = r2.O();
        if (fO.Q() == null || !jC.p() || fO.W()) {
            if (fO.Q() == null) {
                bH.C.d("No turboBaudOnCommand, Turbo Baud disabled. ");
                return;
            }
            return;
        }
        da daVar = new da();
        C0130m c0130mD = C0130m.d(fO);
        c0130mD.a(30);
        c0130mD.v("Turbo Active 1");
        c0130mD.a(true);
        C0132o c0132oA = daVar.a(r2, c0130mD, BluetoothConsts.TCP_OBEX_DEFAULT_PORT);
        if (c0132oA.a() != 1) {
            throw new V.g("Controller Turbo failed to Activate!\nYou may need to power cycle the controller to reconnect. \n" + c0132oA.c());
        }
        int iA = (int) fO.S().a();
        jC.g(true);
        a(50);
        try {
            if (!jC.a(iA)) {
                throw new V.g("Controller Turbo Activated, failed to change application Baud!\nYou will need to power cycle the controller to reconnect");
            }
            fO.i(true);
            bH.C.d("TurboBaud Activated");
            a(550);
            c0130mD.v("Turbo Active 2");
            c0130mD.i(10);
            c0130mD.a(true);
            daVar.a(r2, c0130mD, BluetoothConsts.TCP_OBEX_DEFAULT_PORT);
            a(10);
            c0130mD.v("Turbo Active 3");
            C0132o c0132oA2 = daVar.a(r2, c0130mD, BluetoothConsts.TCP_OBEX_DEFAULT_PORT);
            a(10);
            if (c0132oA2.a() != 1) {
                int iR = fO.r();
                if (jC.a(iR)) {
                    fO.i(false);
                    bH.C.d("Turbo Baud Change Failed, reverted to standard baud: " + iR);
                } else {
                    bH.C.d("Turbo Baud Change Failed, Failed to revert to standard baud");
                }
            }
        } finally {
            jC.g(false);
        }
    }

    private static void a(int i2) {
        try {
            Thread.sleep(i2);
        } catch (InterruptedException e2) {
            Logger.getLogger(df.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public static void b(R r2) {
        J jC = r2.C();
        F fO = r2.O();
        if (fO.R() == null || !jC.p() || !fO.W()) {
            if (fO.W() && fO.Q() == null) {
                throw new V.g("No turboBaudOffCommand defined! Cannot disable Turbo");
            }
            if (fO.Q() == null) {
                bH.C.d("No turboBaudOffCommand, Turbo Baud disabled. ");
                return;
            }
            return;
        }
        da daVar = new da();
        C0130m c0130mE = C0130m.e(fO);
        c0130mE.a(true);
        if (daVar.a(r2, c0130mE, 2000).a() != 1) {
            throw new V.g("Controller Turbo failed to Deactivate!");
        }
        int iR = fO.r();
        jC.g(true);
        try {
            if (!jC.a(iR)) {
                jC.g(false);
                throw new V.g("Controller Turbo Dectivated, failed to change application Baud!\nYou will need to reload project to reconnect.");
            }
            fO.i(false);
            jC.g(false);
            bH.C.d("TurboBaud Deactivated");
        } finally {
            jC.g(false);
        }
    }

    public static void c(R r2) {
        J jC = r2.C();
        F fO = r2.O();
        if (fO.Q() == null || !jC.p() || !fO.W()) {
            if (fO.Q() == null) {
                bH.C.d("No turboBaudOnCommand, Turbo Baud disabled. ");
                return;
            }
            return;
        }
        da daVar = new da();
        C0130m c0130mD = C0130m.d(fO);
        c0130mD.v("Turbo Baud Ping");
        c0130mD.a(true);
        c0130mD.b(1);
        c0130mD.i(5);
        C0132o c0132oA = daVar.a(r2, c0130mD, 50);
        if (c0132oA.a() != 1) {
            bH.C.b("Turbo ping not successful: " + c0132oA.c());
        }
    }
}
