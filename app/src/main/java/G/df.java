package G;

/* loaded from: TunerStudioMS.jar:G/df.class */
public class df {
    public static void a(R r2) {
        J jC = r2.C();
        F fO = r2.O();
        if (fO.Q() != null && jC.p() && !fO.W()) {
            C0132o c0132oA = new da().a(r2, C0130m.d(fO), 2000);
            if (c0132oA.a() == 1) {
                int iA = (int) fO.S().a();
                jC.g(true);
                try {
                    if (!jC.a(iA)) {
                        throw new V.g("Controller Turbo Activated, failed to change application Baud!\nYou will need to power cycle the controller to reconnect");
                    }
                    fO.i(true);
                    bH.C.d("TurboBaud Activated");
                } finally {
                    jC.g(false);
                }
            } else {
                bH.C.b("Error reported when activating turbo baud: " + c0132oA.c());
            }
        } else if (fO.Q() == null) {
            bH.C.d("No turboBaudOnCommand, Turbo Baud disabled. ");
        }
        if (fO.Q() != null && r2.ai() && new da().a(r2, C0130m.b(fO), 2000).a() == 1) {
            bH.C.d("Server Turbo Baud Activate sent.");
        }
    }

    public static void b(R r2) {
        J jC = r2.C();
        F fO = r2.O();
        if (fO.R() != null && jC.p() && fO.W()) {
            C0132o c0132oA = new da().a(r2, C0130m.e(fO), 2000);
            if (c0132oA.a() == 1) {
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
            } else {
                bH.C.b("Error deactivating Turbo Baud: " + c0132oA.c());
            }
        } else {
            if (fO.W() && fO.Q() == null) {
                throw new V.g("No turboBaudOffCommand defined! Cannot disable Turbo");
            }
            if (fO.Q() == null) {
                bH.C.d("No turboBaudOffCommand, Turbo Baud disabled. ");
            }
        }
        if (fO.Q() != null && r2.ai() && new da().a(r2, C0130m.c(fO), 2000).a() == 1) {
            bH.C.d("Server Turbo Baud Deactivate sent.");
        }
    }
}
