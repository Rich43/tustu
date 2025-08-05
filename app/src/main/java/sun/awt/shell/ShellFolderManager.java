package sun.awt.shell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Callable;
import sun.awt.shell.ShellFolder;

/* loaded from: rt.jar:sun/awt/shell/ShellFolderManager.class */
class ShellFolderManager {
    ShellFolderManager() {
    }

    public ShellFolder createShellFolder(File file) throws FileNotFoundException {
        return new DefaultShellFolder(null, file);
    }

    public Object get(String str) {
        if (str.equals("fileChooserDefaultFolder")) {
            File file = new File(System.getProperty("user.home"));
            try {
                return createShellFolder(file);
            } catch (FileNotFoundException e2) {
                return file;
            }
        }
        if (str.equals("roots")) {
            return File.listRoots();
        }
        if (str.equals("fileChooserComboBoxFolders")) {
            return get("roots");
        }
        if (str.equals("fileChooserShortcutPanelFolders")) {
            return new File[]{(File) get("fileChooserDefaultFolder")};
        }
        return null;
    }

    public boolean isComputerNode(File file) {
        return false;
    }

    public boolean isFileSystemRoot(File file) {
        return (!(file instanceof ShellFolder) || ((ShellFolder) file).isFileSystem()) && file.getParentFile() == null;
    }

    protected ShellFolder.Invoker createInvoker() {
        return new DirectInvoker();
    }

    /* loaded from: rt.jar:sun/awt/shell/ShellFolderManager$DirectInvoker.class */
    private static class DirectInvoker implements ShellFolder.Invoker {
        private DirectInvoker() {
        }

        @Override // sun.awt.shell.ShellFolder.Invoker
        public <T> T invoke(Callable<T> callable) throws Exception {
            return callable.call();
        }
    }
}
