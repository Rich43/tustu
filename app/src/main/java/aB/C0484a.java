package ab;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

/* renamed from: ab.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ab/a.class */
public class C0484a {
    public static List a(Class cls, String str) {
        return Arrays.asList((Object[]) new Gson().fromJson(str, cls));
    }

    public static Object b(Class cls, String str) {
        return new Gson().fromJson(str, cls);
    }
}
