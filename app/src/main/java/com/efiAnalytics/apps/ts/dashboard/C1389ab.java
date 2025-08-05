package com.efiAnalytics.apps.ts.dashboard;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import r.C1806i;

/* renamed from: com.efiAnalytics.apps.ts.dashboard.ab, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/apps/ts/dashboard/ab.class */
public class C1389ab {
    public static Z a(String[] strArr, Z z2) {
        Component[] componentArrC = z2.c();
        for (int i2 = 0; i2 < componentArrC.length; i2++) {
            if (componentArrC[i2] instanceof SingleChannelDashComponent) {
                a(strArr, (SingleChannelDashComponent) componentArrC[i2]);
            }
        }
        return z2;
    }

    public static AbstractC1420s a(String[] strArr, SingleChannelDashComponent singleChannelDashComponent) {
        G.R rC = G.T.a().c(singleChannelDashComponent.getEcuConfigurationName());
        if (rC == null || rC.g(singleChannelDashComponent.getOutputChannel()) == null) {
            for (String str : strArr) {
                G.R rC2 = G.T.a().c(str);
                if (rC2.g(singleChannelDashComponent.getOutputChannel()) != null) {
                    singleChannelDashComponent.setEcuConfigurationName(rC2.c());
                    return singleChannelDashComponent;
                }
            }
        }
        return singleChannelDashComponent;
    }

    public static C1425x a(C1425x c1425x) {
        c1425x.l(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(c1425x.X());
        c1425x.setBackground(Color.DARK_GRAY);
        c1425x.l(C1806i.a().a("d67nhtrbd4es8j"));
        return c1425x;
    }

    public static boolean a() {
        return true;
    }

    public static boolean b() {
        return true;
    }
}
