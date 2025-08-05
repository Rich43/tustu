package bw;

import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.calculators.calculator.Calculator;
import java.awt.Window;

/* renamed from: bw.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bw/a.class */
public class C1370a {
    public static void a(Window window, String str) {
        if (str.equals("Two Point Calculator")) {
            b(window);
            return;
        }
        if (str.equals("Calculator")) {
            a(window);
        } else if (str.equals("Unit Conversion Calculator")) {
            c(window);
        } else {
            bV.d("Unknown Calculator: " + str, window);
        }
    }

    private static void a(Window window) {
        new Calculator(window);
    }

    private static void b(Window window) {
        bV.a(new C1371b(), window, "Two Point Calculator", (InterfaceC1565bc) null);
    }

    private static void c(Window window) {
        bV.a(new f(window), window, "Unit Conversion Calculator", (InterfaceC1565bc) null);
    }
}
