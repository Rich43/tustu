package javafx.scene.input;

import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.Toolkit;
import java.io.File;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.Permission;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.util.Pair;

/* loaded from: jfxrt.jar:javafx/scene/input/Clipboard.class */
public class Clipboard {
    private boolean contentPut = false;
    private final AccessControlContext acc = AccessController.getContext();
    TKClipboard peer;
    private static Clipboard systemClipboard;
    private static Clipboard localClipboard;

    public static Clipboard getSystemClipboard() {
        try {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager != null) {
                Permission clipboardPerm = PermissionHelper.getAccessClipboardPermission();
                securityManager.checkPermission(clipboardPerm);
            }
            return getSystemClipboardImpl();
        } catch (SecurityException e2) {
            return getLocalClipboardImpl();
        }
    }

    Clipboard(TKClipboard peer) {
        Toolkit.getToolkit().checkFxUserThread();
        if (peer == null) {
            throw new NullPointerException();
        }
        peer.setSecurityContext(this.acc);
        this.peer = peer;
    }

    public final void clear() {
        setContent(null);
    }

    public final Set<DataFormat> getContentTypes() {
        return this.peer.getContentTypes();
    }

    public final boolean setContent(Map<DataFormat, Object> content) {
        Toolkit.getToolkit().checkFxUserThread();
        if (content == null) {
            this.contentPut = false;
            this.peer.putContent(new Pair[0]);
            return true;
        }
        Pair<DataFormat, Object>[] data = new Pair[content.size()];
        int index = 0;
        for (Map.Entry<DataFormat, Object> entry : content.entrySet()) {
            int i2 = index;
            index++;
            data[i2] = new Pair<>(entry.getKey(), entry.getValue());
        }
        this.contentPut = this.peer.putContent(data);
        return this.contentPut;
    }

    public final Object getContent(DataFormat dataFormat) {
        Toolkit.getToolkit().checkFxUserThread();
        return getContentImpl(dataFormat);
    }

    Object getContentImpl(DataFormat dataFormat) {
        return this.peer.getContent(dataFormat);
    }

    public final boolean hasContent(DataFormat dataFormat) {
        Toolkit.getToolkit().checkFxUserThread();
        return this.peer.hasContent(dataFormat);
    }

    public final boolean hasString() {
        return hasContent(DataFormat.PLAIN_TEXT);
    }

    public final String getString() {
        return (String) getContent(DataFormat.PLAIN_TEXT);
    }

    public final boolean hasUrl() {
        return hasContent(DataFormat.URL);
    }

    public final String getUrl() {
        return (String) getContent(DataFormat.URL);
    }

    public final boolean hasHtml() {
        return hasContent(DataFormat.HTML);
    }

    public final String getHtml() {
        return (String) getContent(DataFormat.HTML);
    }

    public final boolean hasRtf() {
        return hasContent(DataFormat.RTF);
    }

    public final String getRtf() {
        return (String) getContent(DataFormat.RTF);
    }

    public final boolean hasImage() {
        return hasContent(DataFormat.IMAGE);
    }

    public final Image getImage() {
        return (Image) getContent(DataFormat.IMAGE);
    }

    public final boolean hasFiles() {
        return hasContent(DataFormat.FILES);
    }

    public final List<File> getFiles() {
        return (List) getContent(DataFormat.FILES);
    }

    @Deprecated
    public boolean impl_contentPut() {
        return this.contentPut;
    }

    private static synchronized Clipboard getSystemClipboardImpl() {
        if (systemClipboard == null) {
            systemClipboard = new Clipboard(Toolkit.getToolkit().getSystemClipboard());
        }
        return systemClipboard;
    }

    private static synchronized Clipboard getLocalClipboardImpl() {
        if (localClipboard == null) {
            localClipboard = new Clipboard(Toolkit.getToolkit().createLocalClipboard());
        }
        return localClipboard;
    }
}
