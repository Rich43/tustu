package ao;

import g.C1733k;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/* renamed from: ao.bl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bl.class */
public class C0648bl {

    /* renamed from: a, reason: collision with root package name */
    public static String f5415a = "/help/index.html";

    /* renamed from: b, reason: collision with root package name */
    public static String f5416b = "/help/veAnalysis.html";

    /* renamed from: c, reason: collision with root package name */
    public static String f5417c = "/help/fieldDictionary.html";

    /* renamed from: d, reason: collision with root package name */
    public static String f5418d = "/help/formulas.html";

    public static String a(String str) {
        try {
            return "file:///" + C1733k.a(new File(".").getCanonicalPath() + str, FXMLLoader.ESCAPE_PREFIX, "/");
        } catch (IOException e2) {
            e2.printStackTrace();
            return "file:///." + str;
        }
    }
}
