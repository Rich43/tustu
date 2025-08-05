package aE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import javafx.fxml.FXMLLoader;

/* loaded from: TunerStudioMS.jar:aE/g.class */
public class g extends Properties {

    /* renamed from: a, reason: collision with root package name */
    private String f2376a;

    public g(String str) {
        this.f2376a = "";
        this.f2376a = str;
    }

    public void a(File file) throws V.a {
        try {
            store(new FileOutputStream(file), "Vehicles Attributes.\n#" + this.f2376a + " by Philip Tobin");
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error Saving Vehicle. \n" + e2.getMessage() + "\nSee log file for more detail.");
        }
    }

    public g b(File file) {
        try {
            load(new FileInputStream(file));
            return this;
        } catch (Exception e2) {
            e2.printStackTrace();
            throw new V.a("Error occured trying to open vehicle in directory\n" + (file != null ? file.getName() : FXMLLoader.NULL_KEYWORD) + "\nError Message: " + e2.getMessage() + "\nCheck Log for details.");
        }
    }
}
