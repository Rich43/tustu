package javax.swing.filechooser;

import java.io.File;
import java.io.IOException;
import javax.swing.UIManager;

/* compiled from: FileSystemView.java */
/* loaded from: rt.jar:javax/swing/filechooser/GenericFileSystemView.class */
class GenericFileSystemView extends FileSystemView {
    private static final String newFolderString = UIManager.getString("FileChooser.other.newFolder");

    GenericFileSystemView() {
    }

    @Override // javax.swing.filechooser.FileSystemView
    public File createNewFolder(File file) throws IOException {
        if (file == null) {
            throw new IOException("Containing directory is null:");
        }
        File fileCreateFileObject = createFileObject(file, newFolderString);
        if (fileCreateFileObject.exists()) {
            throw new IOException("Directory already exists:" + fileCreateFileObject.getAbsolutePath());
        }
        fileCreateFileObject.mkdirs();
        return fileCreateFileObject;
    }
}
