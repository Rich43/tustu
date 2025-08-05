package G;

import java.util.ArrayList;
import javax.swing.JSplitPane;

/* loaded from: TunerStudioMS.jar:G/cW.class */
public class cW {
    public static ArrayList a(String str, ArrayList arrayList) {
        if (str.equals("std_injection")) {
            arrayList.add("reqFuel");
            arrayList.add("alternate");
            arrayList.add("nCylinders");
            arrayList.add("nInjectors");
            arrayList.add(JSplitPane.DIVIDER);
        } else if (str.equals("std_constants")) {
            arrayList.add("reqFuel0");
            arrayList.add("alternate0");
            arrayList.add("nCylinders0");
            arrayList.add("nInjectors0");
            arrayList.add("divider0");
            arrayList.add("injOpen0");
            arrayList.add("battFac0");
        } else if (str.equals("std_accel")) {
        }
        return arrayList;
    }
}
