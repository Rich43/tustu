package com.sun.javafx.stage;

import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.Scene;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/EmbeddedWindow.class */
public class EmbeddedWindow extends Window {
    private HostInterface host;

    public EmbeddedWindow(HostInterface host) {
        this.host = host;
    }

    @Override // javafx.stage.Window
    public final void setScene(Scene value) {
        super.setScene(value);
    }

    @Override // javafx.stage.Window
    public final void show() {
        super.show();
    }

    @Override // javafx.stage.Window
    protected void impl_visibleChanging(boolean visible) {
        super.impl_visibleChanging(visible);
        Toolkit toolkit = Toolkit.getToolkit();
        if (visible && this.impl_peer == null) {
            this.impl_peer = toolkit.createTKEmbeddedStage(this.host, WindowHelper.getAccessControlContext(this));
            this.peerListener = new WindowPeerListener(this);
        }
    }
}
