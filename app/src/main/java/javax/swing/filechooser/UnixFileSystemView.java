package javax.swing.filechooser;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import javax.swing.UIManager;

/* compiled from: FileSystemView.java */
/* loaded from: rt.jar:javax/swing/filechooser/UnixFileSystemView.class */
class UnixFileSystemView extends FileSystemView {
    private static final String newFolderString = UIManager.getString("FileChooser.other.newFolder");
    private static final String newFolderNextString = UIManager.getString("FileChooser.other.newFolder.subsequent");

    UnixFileSystemView() {
    }

    @Override // javax.swing.filechooser.FileSystemView
    public File createNewFolder(File file) throws IOException {
        if (file == null) {
            throw new IOException("Containing directory is null:");
        }
        File fileCreateFileObject = createFileObject(file, newFolderString);
        for (int i2 = 1; fileCreateFileObject.exists() && i2 < 100; i2++) {
            fileCreateFileObject = createFileObject(file, MessageFormat.format(newFolderNextString, new Integer(i2)));
        }
        if (fileCreateFileObject.exists()) {
            throw new IOException("Directory already exists:" + fileCreateFileObject.getAbsolutePath());
        }
        fileCreateFileObject.mkdirs();
        return fileCreateFileObject;
    }

    @Override // javax.swing.filechooser.FileSystemView
    public boolean isFileSystemRoot(File file) {
        return file != null && file.getAbsolutePath().equals("/");
    }

    @Override // javax.swing.filechooser.FileSystemView
    public boolean isDrive(File file) {
        return isFloppyDrive(file);
    }

    @Override // javax.swing.filechooser.FileSystemView
    public boolean isFloppyDrive(File file) {
        return false;
    }

    @Override // javax.swing.filechooser.FileSystemView
    public boolean isComputerNode(File file) {
        String parent;
        if (file != null && (parent = file.getParent()) != null && parent.equals("/net")) {
            return true;
        }
        return false;
    }
}
