package javax.swing.filechooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.MessageFormat;
import javafx.fxml.FXMLLoader;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;

/* compiled from: FileSystemView.java */
/* loaded from: rt.jar:javax/swing/filechooser/WindowsFileSystemView.class */
class WindowsFileSystemView extends FileSystemView {
    private static final String newFolderString = UIManager.getString("FileChooser.win32.newFolder");
    private static final String newFolderNextString = UIManager.getString("FileChooser.win32.newFolder.subsequent");

    WindowsFileSystemView() {
    }

    @Override // javax.swing.filechooser.FileSystemView
    public Boolean isTraversable(File file) {
        return Boolean.valueOf(isFileSystemRoot(file) || isComputerNode(file) || file.isDirectory());
    }

    @Override // javax.swing.filechooser.FileSystemView
    public File getChild(File file, String str) {
        if (str.startsWith(FXMLLoader.ESCAPE_PREFIX) && !str.startsWith("\\\\") && isFileSystem(file)) {
            String absolutePath = file.getAbsolutePath();
            if (absolutePath.length() >= 2 && absolutePath.charAt(1) == ':' && Character.isLetter(absolutePath.charAt(0))) {
                return createFileObject(absolutePath.substring(0, 2) + str);
            }
        }
        return super.getChild(file, str);
    }

    @Override // javax.swing.filechooser.FileSystemView
    public String getSystemTypeDescription(File file) {
        if (file == null) {
            return null;
        }
        try {
            return getShellFolder(file).getFolderType();
        } catch (FileNotFoundException e2) {
            return null;
        }
    }

    @Override // javax.swing.filechooser.FileSystemView
    public File getHomeDirectory() {
        File[] roots = getRoots();
        if (roots.length == 0) {
            return null;
        }
        return roots[0];
    }

    @Override // javax.swing.filechooser.FileSystemView
    public File createNewFolder(File file) throws IOException {
        if (file == null) {
            throw new IOException("Containing directory is null:");
        }
        File fileCreateFileObject = createFileObject(file, newFolderString);
        for (int i2 = 2; fileCreateFileObject.exists() && i2 < 100; i2++) {
            fileCreateFileObject = createFileObject(file, MessageFormat.format(newFolderNextString, new Integer(i2)));
        }
        if (fileCreateFileObject.exists()) {
            throw new IOException("Directory already exists:" + fileCreateFileObject.getAbsolutePath());
        }
        fileCreateFileObject.mkdirs();
        return fileCreateFileObject;
    }

    @Override // javax.swing.filechooser.FileSystemView
    public boolean isDrive(File file) {
        return isFileSystemRoot(file);
    }

    @Override // javax.swing.filechooser.FileSystemView
    public boolean isFloppyDrive(final File file) {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.swing.filechooser.WindowsFileSystemView.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return file.getAbsolutePath();
            }
        });
        return str != null && (str.equals("A:\\") || str.equals("B:\\"));
    }

    @Override // javax.swing.filechooser.FileSystemView
    public File createFileObject(String str) {
        if (str.length() >= 2 && str.charAt(1) == ':' && Character.isLetter(str.charAt(0))) {
            if (str.length() == 2) {
                str = str + FXMLLoader.ESCAPE_PREFIX;
            } else if (str.charAt(2) != '\\') {
                str = str.substring(0, 2) + FXMLLoader.ESCAPE_PREFIX + str.substring(2);
            }
        }
        return super.createFileObject(str);
    }

    @Override // javax.swing.filechooser.FileSystemView
    protected File createFileSystemRoot(File file) {
        return new FileSystemView.FileSystemRoot(file) { // from class: javax.swing.filechooser.WindowsFileSystemView.2
            @Override // java.io.File
            public boolean exists() {
                return true;
            }
        };
    }
}
