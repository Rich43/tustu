package java.awt.peer;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;

/* loaded from: rt.jar:java/awt/peer/DesktopPeer.class */
public interface DesktopPeer {
    boolean isSupported(Desktop.Action action);

    void open(File file) throws IOException;

    void edit(File file) throws IOException;

    void print(File file) throws IOException;

    void mail(URI uri) throws IOException;

    void browse(URI uri) throws IOException;
}
