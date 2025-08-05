package aP;

import java.io.File;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileView;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:aP/hY.class */
public class hY extends FileView {

    /* renamed from: a, reason: collision with root package name */
    ImageIcon f3551a;

    public hY() {
        this.f3551a = null;
        this.f3551a = a("resources/TSicon.gif");
    }

    @Override // javax.swing.filechooser.FileView
    public String getName(File file) {
        return null;
    }

    @Override // javax.swing.filechooser.FileView
    public String getDescription(File file) {
        return null;
    }

    @Override // javax.swing.filechooser.FileView
    public Boolean isTraversable(File file) {
        if (a(file)) {
            return new Boolean(false);
        }
        return null;
    }

    @Override // javax.swing.filechooser.FileView
    public String getTypeDescription(File file) {
        if (a(file)) {
            return C1798a.f13268b + " Project";
        }
        return null;
    }

    @Override // javax.swing.filechooser.FileView
    public Icon getIcon(File file) {
        if (a(file)) {
            return this.f3551a;
        }
        return null;
    }

    protected boolean a(File file) {
        return aE.a.a(file);
    }

    protected ImageIcon a(String str) {
        URL resource = getClass().getResource(str);
        if (resource != null) {
            return new ImageIcon(resource);
        }
        System.err.println("Couldn't find file: " + str);
        return null;
    }
}
