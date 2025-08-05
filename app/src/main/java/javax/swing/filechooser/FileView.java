package javax.swing.filechooser;

import java.io.File;
import javax.swing.Icon;

/* loaded from: rt.jar:javax/swing/filechooser/FileView.class */
public abstract class FileView {
    public String getName(File file) {
        return null;
    }

    public String getDescription(File file) {
        return null;
    }

    public String getTypeDescription(File file) {
        return null;
    }

    public Icon getIcon(File file) {
        return null;
    }

    public Boolean isTraversable(File file) {
        return null;
    }
}
