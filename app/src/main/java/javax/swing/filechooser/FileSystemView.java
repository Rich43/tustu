package javax.swing.filechooser;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import sun.awt.shell.ShellFolder;

/* loaded from: rt.jar:javax/swing/filechooser/FileSystemView.class */
public abstract class FileSystemView {
    static FileSystemView windowsFileSystemView = null;
    static FileSystemView unixFileSystemView = null;
    static FileSystemView genericFileSystemView = null;
    private boolean useSystemExtensionHiding = UIManager.getDefaults().getBoolean("FileChooser.useSystemExtensionHiding");

    public abstract File createNewFolder(File file) throws IOException;

    public static FileSystemView getFileSystemView() {
        if (File.separatorChar == '\\') {
            if (windowsFileSystemView == null) {
                windowsFileSystemView = new WindowsFileSystemView();
            }
            return windowsFileSystemView;
        }
        if (File.separatorChar == '/') {
            if (unixFileSystemView == null) {
                unixFileSystemView = new UnixFileSystemView();
            }
            return unixFileSystemView;
        }
        if (genericFileSystemView == null) {
            genericFileSystemView = new GenericFileSystemView();
        }
        return genericFileSystemView;
    }

    public FileSystemView() {
        final WeakReference weakReference = new WeakReference(this);
        UIManager.addPropertyChangeListener(new PropertyChangeListener() { // from class: javax.swing.filechooser.FileSystemView.1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                FileSystemView fileSystemView = (FileSystemView) weakReference.get();
                if (fileSystemView == null) {
                    UIManager.removePropertyChangeListener(this);
                } else if (propertyChangeEvent.getPropertyName().equals("lookAndFeel")) {
                    fileSystemView.useSystemExtensionHiding = UIManager.getDefaults().getBoolean("FileChooser.useSystemExtensionHiding");
                }
            }
        });
    }

    public boolean isRoot(File file) {
        if (file == null || !file.isAbsolute()) {
            return false;
        }
        for (File file2 : getRoots()) {
            if (file2.equals(file)) {
                return true;
            }
        }
        return false;
    }

    public Boolean isTraversable(File file) {
        return Boolean.valueOf(file.isDirectory());
    }

    public String getSystemDisplayName(File file) {
        if (file == null) {
            return null;
        }
        String name = file.getName();
        if (!name.equals(Constants.ATTRVAL_PARENT) && !name.equals(".") && ((this.useSystemExtensionHiding || !isFileSystem(file) || isFileSystemRoot(file)) && ((file instanceof ShellFolder) || file.exists()))) {
            try {
                name = getShellFolder(file).getDisplayName();
                if (name == null || name.length() == 0) {
                    name = file.getPath();
                }
            } catch (FileNotFoundException e2) {
                return null;
            }
        }
        return name;
    }

    public String getSystemTypeDescription(File file) {
        return null;
    }

    public Icon getSystemIcon(File file) {
        if (file == null) {
            return null;
        }
        try {
            ShellFolder shellFolder = getShellFolder(file);
            Image icon = shellFolder.getIcon(false);
            if (icon != null) {
                return new ImageIcon(icon, shellFolder.getFolderType());
            }
            return UIManager.getIcon(file.isDirectory() ? "FileView.directoryIcon" : "FileView.fileIcon");
        } catch (FileNotFoundException e2) {
            return null;
        }
    }

    public boolean isParent(File file, File file2) {
        if (file == null || file2 == null) {
            return false;
        }
        if (file instanceof ShellFolder) {
            File parentFile = file2.getParentFile();
            if (parentFile != null && parentFile.equals(file)) {
                return true;
            }
            for (File file3 : getFiles(file, false)) {
                if (file2.equals(file3)) {
                    return true;
                }
            }
            return false;
        }
        return file.equals(file2.getParentFile());
    }

    public File getChild(File file, String str) {
        if (file instanceof ShellFolder) {
            for (File file2 : getFiles(file, false)) {
                if (file2.getName().equals(str)) {
                    return file2;
                }
            }
        }
        return createFileObject(file, str);
    }

    public boolean isFileSystem(File file) {
        if (file instanceof ShellFolder) {
            ShellFolder shellFolder = (ShellFolder) file;
            return shellFolder.isFileSystem() && !(shellFolder.isLink() && shellFolder.isDirectory());
        }
        return true;
    }

    public boolean isHiddenFile(File file) {
        return file.isHidden();
    }

    public boolean isFileSystemRoot(File file) {
        return ShellFolder.isFileSystemRoot(file);
    }

    public boolean isDrive(File file) {
        return false;
    }

    public boolean isFloppyDrive(File file) {
        return false;
    }

    public boolean isComputerNode(File file) {
        return ShellFolder.isComputerNode(file);
    }

    public File[] getRoots() {
        File[] fileArr = (File[]) ShellFolder.get("roots");
        for (int i2 = 0; i2 < fileArr.length; i2++) {
            if (isFileSystemRoot(fileArr[i2])) {
                fileArr[i2] = createFileSystemRoot(fileArr[i2]);
            }
        }
        return fileArr;
    }

    public File getHomeDirectory() {
        return createFileObject(System.getProperty("user.home"));
    }

    public File getDefaultDirectory() {
        File fileCreateFileSystemRoot = (File) ShellFolder.get("fileChooserDefaultFolder");
        if (isFileSystemRoot(fileCreateFileSystemRoot)) {
            fileCreateFileSystemRoot = createFileSystemRoot(fileCreateFileSystemRoot);
        }
        return fileCreateFileSystemRoot;
    }

    public File createFileObject(File file, String str) {
        if (file == null) {
            return new File(str);
        }
        return new File(file, str);
    }

    public File createFileObject(String str) {
        File file = new File(str);
        if (isFileSystemRoot(file)) {
            file = createFileSystemRoot(file);
        }
        return file;
    }

    /* JADX WARN: Removed duplicated region for block: B:36:0x009b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.io.File[] getFiles(java.io.File r4, boolean r5) {
        /*
            r3 = this;
            java.util.ArrayList r0 = new java.util.ArrayList
            r1 = r0
            r1.<init>()
            r6 = r0
            r0 = r4
            boolean r0 = r0 instanceof sun.awt.shell.ShellFolder
            if (r0 != 0) goto L1f
            r0 = r3
            r1 = r4
            sun.awt.shell.ShellFolder r0 = r0.getShellFolder(r1)     // Catch: java.io.FileNotFoundException -> L18
            r4 = r0
            goto L1f
        L18:
            r7 = move-exception
            r0 = 0
            java.io.File[] r0 = new java.io.File[r0]
            return r0
        L1f:
            r0 = r4
            sun.awt.shell.ShellFolder r0 = (sun.awt.shell.ShellFolder) r0
            r1 = r5
            if (r1 != 0) goto L2b
            r1 = 1
            goto L2c
        L2b:
            r1 = 0
        L2c:
            java.io.File[] r0 = r0.listFiles(r1)
            r7 = r0
            r0 = r7
            if (r0 != 0) goto L3b
            r0 = 0
            java.io.File[] r0 = new java.io.File[r0]
            return r0
        L3b:
            r0 = r7
            r8 = r0
            r0 = r8
            int r0 = r0.length
            r9 = r0
            r0 = 0
            r10 = r0
        L47:
            r0 = r10
            r1 = r9
            if (r0 >= r1) goto Laa
            r0 = r8
            r1 = r10
            r0 = r0[r1]
            r11 = r0
            java.lang.Thread r0 = java.lang.Thread.currentThread()
            boolean r0 = r0.isInterrupted()
            if (r0 == 0) goto L61
            goto Laa
        L61:
            r0 = r11
            boolean r0 = r0 instanceof sun.awt.shell.ShellFolder
            if (r0 != 0) goto L8e
            r0 = r3
            r1 = r11
            boolean r0 = r0.isFileSystemRoot(r1)
            if (r0 == 0) goto L7a
            r0 = r3
            r1 = r11
            java.io.File r0 = r0.createFileSystemRoot(r1)
            r11 = r0
        L7a:
            r0 = r11
            sun.awt.shell.ShellFolder r0 = sun.awt.shell.ShellFolder.getShellFolder(r0)     // Catch: java.io.FileNotFoundException -> L84 java.lang.InternalError -> L89
            r11 = r0
            goto L8e
        L84:
            r12 = move-exception
            goto La4
        L89:
            r12 = move-exception
            goto La4
        L8e:
            r0 = r5
            if (r0 == 0) goto L9b
            r0 = r3
            r1 = r11
            boolean r0 = r0.isHiddenFile(r1)
            if (r0 != 0) goto La4
        L9b:
            r0 = r6
            r1 = r11
            boolean r0 = r0.add(r1)
        La4:
            int r10 = r10 + 1
            goto L47
        Laa:
            r0 = r6
            r1 = r6
            int r1 = r1.size()
            java.io.File[] r1 = new java.io.File[r1]
            java.lang.Object[] r0 = r0.toArray(r1)
            java.io.File[] r0 = (java.io.File[]) r0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.filechooser.FileSystemView.getFiles(java.io.File, boolean):java.io.File[]");
    }

    public File getParentDirectory(File file) {
        File parentFile;
        if (file == null || !file.exists()) {
            return null;
        }
        try {
            File parentFile2 = getShellFolder(file).getParentFile();
            if (parentFile2 == null) {
                return null;
            }
            if (isFileSystem(parentFile2)) {
                File fileCreateFileSystemRoot = parentFile2;
                if (!fileCreateFileSystemRoot.exists() && ((parentFile = parentFile2.getParentFile()) == null || !isFileSystem(parentFile))) {
                    fileCreateFileSystemRoot = createFileSystemRoot(fileCreateFileSystemRoot);
                }
                return fileCreateFileSystemRoot;
            }
            return parentFile2;
        } catch (FileNotFoundException e2) {
            return null;
        }
    }

    ShellFolder getShellFolder(File file) throws FileNotFoundException {
        if (!(file instanceof ShellFolder) && !(file instanceof FileSystemRoot) && isFileSystemRoot(file)) {
            file = createFileSystemRoot(file);
        }
        try {
            return ShellFolder.getShellFolder(file);
        } catch (InternalError e2) {
            System.err.println("FileSystemView.getShellFolder: f=" + ((Object) file));
            e2.printStackTrace();
            return null;
        }
    }

    protected File createFileSystemRoot(File file) {
        return new FileSystemRoot(file);
    }

    /* loaded from: rt.jar:javax/swing/filechooser/FileSystemView$FileSystemRoot.class */
    static class FileSystemRoot extends File {
        public FileSystemRoot(File file) {
            super(file, "");
        }

        public FileSystemRoot(String str) {
            super(str);
        }

        @Override // java.io.File
        public boolean isDirectory() {
            return true;
        }

        @Override // java.io.File
        public String getName() {
            return getPath();
        }
    }
}
