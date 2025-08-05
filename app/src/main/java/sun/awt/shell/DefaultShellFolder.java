package sun.awt.shell;

import java.io.File;
import java.io.ObjectStreamException;

/* loaded from: rt.jar:sun/awt/shell/DefaultShellFolder.class */
class DefaultShellFolder extends ShellFolder {
    DefaultShellFolder(ShellFolder shellFolder, File file) {
        super(shellFolder, file.getAbsolutePath());
    }

    @Override // sun.awt.shell.ShellFolder
    protected Object writeReplace() throws ObjectStreamException {
        return new File(getPath());
    }

    @Override // sun.awt.shell.ShellFolder, java.io.File
    public File[] listFiles() {
        File[] fileArrListFiles = super.listFiles();
        if (fileArrListFiles != null) {
            for (int i2 = 0; i2 < fileArrListFiles.length; i2++) {
                fileArrListFiles[i2] = new DefaultShellFolder(this, fileArrListFiles[i2]);
            }
        }
        return fileArrListFiles;
    }

    @Override // sun.awt.shell.ShellFolder
    public boolean isLink() {
        return false;
    }

    @Override // java.io.File
    public boolean isHidden() {
        String name = getName();
        return name.length() > 0 && name.charAt(0) == '.';
    }

    @Override // sun.awt.shell.ShellFolder
    public ShellFolder getLinkLocation() {
        return null;
    }

    @Override // sun.awt.shell.ShellFolder
    public String getDisplayName() {
        return getName();
    }

    @Override // sun.awt.shell.ShellFolder
    public String getFolderType() {
        if (isDirectory()) {
            return "File Folder";
        }
        return "File";
    }

    @Override // sun.awt.shell.ShellFolder
    public String getExecutableType() {
        return null;
    }
}
