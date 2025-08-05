package sun.awt.windows;

import java.awt.Desktop;
import java.awt.peer.DesktopPeer;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/* loaded from: rt.jar:sun/awt/windows/WDesktopPeer.class */
final class WDesktopPeer implements DesktopPeer {
    private static String ACTION_OPEN_VERB = "open";
    private static String ACTION_EDIT_VERB = "edit";
    private static String ACTION_PRINT_VERB = "print";

    private static native String ShellExecute(String str, String str2);

    WDesktopPeer() {
    }

    @Override // java.awt.peer.DesktopPeer
    public boolean isSupported(Desktop.Action action) {
        return true;
    }

    @Override // java.awt.peer.DesktopPeer
    public void open(File file) throws IOException {
        ShellExecute(file, ACTION_OPEN_VERB);
    }

    @Override // java.awt.peer.DesktopPeer
    public void edit(File file) throws IOException {
        ShellExecute(file, ACTION_EDIT_VERB);
    }

    @Override // java.awt.peer.DesktopPeer
    public void print(File file) throws IOException {
        ShellExecute(file, ACTION_PRINT_VERB);
    }

    @Override // java.awt.peer.DesktopPeer
    public void mail(URI uri) throws IOException {
        ShellExecute(uri, ACTION_OPEN_VERB);
    }

    @Override // java.awt.peer.DesktopPeer
    public void browse(URI uri) throws IOException {
        ShellExecute(uri, ACTION_OPEN_VERB);
    }

    private void ShellExecute(File file, String str) throws IOException {
        String strShellExecute = ShellExecute(file.getAbsolutePath(), str);
        if (strShellExecute != null) {
            throw new IOException("Failed to " + str + " " + ((Object) file) + ". Error message: " + strShellExecute);
        }
    }

    private void ShellExecute(URI uri, String str) throws IOException {
        String strShellExecute = ShellExecute(uri.toString(), str);
        if (strShellExecute != null) {
            throw new IOException("Failed to " + str + " " + ((Object) uri) + ". Error message: " + strShellExecute);
        }
    }
}
