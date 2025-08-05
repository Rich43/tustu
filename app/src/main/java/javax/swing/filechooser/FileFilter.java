package javax.swing.filechooser;

import java.io.File;

/* loaded from: rt.jar:javax/swing/filechooser/FileFilter.class */
public abstract class FileFilter {
    public abstract boolean accept(File file);

    public abstract String getDescription();
}
