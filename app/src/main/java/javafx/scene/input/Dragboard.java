package javafx.scene.input;

import com.sun.javafx.scene.input.DragboardHelper;
import com.sun.javafx.tk.PermissionHelper;
import com.sun.javafx.tk.TKClipboard;
import java.security.Permission;
import java.util.Set;
import javafx.scene.image.Image;

/* loaded from: jfxrt.jar:javafx/scene/input/Dragboard.class */
public final class Dragboard extends Clipboard {
    private boolean dataAccessRestricted;

    Dragboard(TKClipboard peer) {
        super(peer);
        this.dataAccessRestricted = true;
    }

    @Override // javafx.scene.input.Clipboard
    Object getContentImpl(DataFormat dataFormat) {
        SecurityManager securityManager;
        if (this.dataAccessRestricted && (securityManager = System.getSecurityManager()) != null) {
            Permission clipboardPerm = PermissionHelper.getAccessClipboardPermission();
            securityManager.checkPermission(clipboardPerm);
        }
        return super.getContentImpl(dataFormat);
    }

    public final Set<TransferMode> getTransferModes() {
        return this.peer.getTransferModes();
    }

    @Deprecated
    public TKClipboard impl_getPeer() {
        return this.peer;
    }

    @Deprecated
    public static Dragboard impl_createDragboard(TKClipboard peer) {
        return new Dragboard(peer);
    }

    public void setDragView(Image image, double offsetX, double offsetY) {
        this.peer.setDragView(image);
        this.peer.setDragViewOffsetX(offsetX);
        this.peer.setDragViewOffsetY(offsetY);
    }

    public void setDragView(Image image) {
        this.peer.setDragView(image);
    }

    public void setDragViewOffsetX(double offsetX) {
        this.peer.setDragViewOffsetX(offsetX);
    }

    public void setDragViewOffsetY(double offsetY) {
        this.peer.setDragViewOffsetY(offsetY);
    }

    public Image getDragView() {
        return this.peer.getDragView();
    }

    public double getDragViewOffsetX() {
        return this.peer.getDragViewOffsetX();
    }

    public double getDragViewOffsetY() {
        return this.peer.getDragViewOffsetY();
    }

    static {
        DragboardHelper.setDragboardAccessor((dragboard, restricted) -> {
            dragboard.dataAccessRestricted = restricted;
        });
    }
}
