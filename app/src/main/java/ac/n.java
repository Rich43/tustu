package ac;

import G.R;
import java.util.Date;

/* loaded from: TunerStudioMS.jar:ac/n.class */
public class n implements InterfaceC0492d {

    /* renamed from: a, reason: collision with root package name */
    public static String f4232a = null;

    @Override // ac.InterfaceC0492d
    public String a(R[] rArr) {
        StringBuilder sb = new StringBuilder();
        sb.append('\"');
        for (int i2 = 0; i2 < rArr.length; i2++) {
            String strI = rArr[i2].i();
            if (rArr[i2].P() != null) {
                strI = strI + ": " + rArr[i2].P();
            }
            sb.append(strI);
            if (i2 + 1 < rArr.length) {
                sb.append(", ");
            }
        }
        sb.append("\"\n");
        sb.append("\"Capture Date: ").append(new Date().toString());
        if (f4232a != null) {
            sb.append(", File author: ").append(f4232a);
        }
        sb.append("\"\n");
        return sb.toString();
    }
}
