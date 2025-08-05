package java.awt.peer;

import java.io.FilenameFilter;

/* loaded from: rt.jar:java/awt/peer/FileDialogPeer.class */
public interface FileDialogPeer extends DialogPeer {
    void setFile(String str);

    void setDirectory(String str);

    void setFilenameFilter(FilenameFilter filenameFilter);
}
