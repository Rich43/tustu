package javax.swing.plaf;

import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

/* loaded from: rt.jar:javax/swing/plaf/FileChooserUI.class */
public abstract class FileChooserUI extends ComponentUI {
    public abstract FileFilter getAcceptAllFileFilter(JFileChooser jFileChooser);

    public abstract FileView getFileView(JFileChooser jFileChooser);

    public abstract String getApproveButtonText(JFileChooser jFileChooser);

    public abstract String getDialogTitle(JFileChooser jFileChooser);

    public abstract void rescanCurrentDirectory(JFileChooser jFileChooser);

    public abstract void ensureFileIsVisible(JFileChooser jFileChooser, File file);

    public JButton getDefaultButton(JFileChooser jFileChooser) {
        return null;
    }
}
