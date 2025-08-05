package ai;

import bH.W;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;

/* renamed from: ai.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ai/d.class */
public class C0514d {
    public static String a(String str) {
        try {
            return "file:///" + W.b(new File(".").getCanonicalPath() + str, FXMLLoader.ESCAPE_PREFIX, "/");
        } catch (IOException e2) {
            e2.printStackTrace();
            return "file:///." + str;
        }
    }
}
