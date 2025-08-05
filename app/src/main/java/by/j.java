package by;

import G.R;

/* loaded from: TunerStudioMS.jar:by/j.class */
public class j {
    public static i a(R r2) {
        a aVar = new a();
        aVar.b("Channels");
        for (String str : bH.R.a(r2.s())) {
            if (!str.contains(" ")) {
                aVar.a(str);
            }
        }
        return aVar;
    }

    public static i b(R r2) {
        a aVar = new a();
        aVar.b("Cal Parameters");
        for (String str : bH.R.a(r2.k())) {
            aVar.a(str);
        }
        return aVar;
    }
}
