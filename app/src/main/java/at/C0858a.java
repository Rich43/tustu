package at;

import bH.C1011s;
import bH.W;
import h.h;
import java.io.File;

/* renamed from: at.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:at/a.class */
public class C0858a {

    /* renamed from: a, reason: collision with root package name */
    public static String f6266a = ".settings";

    public static String[] a() {
        String[] list = h.e().list(new b());
        if (list == null) {
            return new String[0];
        }
        for (int i2 = 0; i2 < list.length; i2++) {
            list[i2] = W.b(list[i2], f6266a, "");
        }
        return list;
    }

    public static File a(String str) {
        return new File(h.e(), str + f6266a);
    }

    public static boolean b(String str) {
        return a(str).delete();
    }

    public static void a(String str, File file) {
        C1011s.a(file, a(str));
    }
}
