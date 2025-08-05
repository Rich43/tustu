package com.sun.javafx.tk.quantum;

import com.sun.javafx.embed.AbstractEvents;
import com.sun.javafx.embed.EmbeddedStageInterface;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.Toolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.List;
import javafx.application.Platform;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/EmbeddedStage.class */
final class EmbeddedStage extends GlassStage implements EmbeddedStageInterface {
    private HostInterface host;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !EmbeddedStage.class.desiredAssertionStatus();
    }

    public EmbeddedStage(HostInterface host) {
        this.host = host;
    }

    @Override // com.sun.javafx.tk.TKStage
    public TKScene createTKScene(boolean depthBuffer, boolean msaa, AccessControlContext acc) {
        EmbeddedScene scene = new EmbeddedScene(this.host, depthBuffer, msaa);
        scene.setSecurityContext(acc);
        return scene;
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void setScene(TKScene scene) {
        if (scene != null && !$assertionsDisabled && !(scene instanceof EmbeddedScene)) {
            throw new AssertionError();
        }
        super.setScene(scene);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setBounds(float x2, float y2, boolean xSet, boolean ySet, float w2, float h2, float cw, float ch, float xGravity, float yGravity) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setBounds: x=" + x2 + " y=" + y2 + " xSet=" + xSet + " ySet=" + ySet + " w=" + w2 + " h= cw=" + cw + " ch=" + ch);
        }
        float newW = w2 > 0.0f ? w2 : cw;
        float newH = h2 > 0.0f ? h2 : ch;
        if (newW > 0.0f && newH > 0.0f) {
            this.host.setPreferredSize((int) newW, (int) newH);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public float getUIScale() {
        return 1.0f;
    }

    @Override // com.sun.javafx.tk.TKStage
    public float getRenderScale() {
        TKScene scene = getScene();
        if (scene instanceof EmbeddedScene) {
            return ((EmbeddedScene) scene).getRenderScale();
        }
        return 1.0f;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setMinimumSize(int minWidth, int minHeight) {
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setMaximumSize(int maxWidth, int maxHeight) {
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage
    protected void setPlatformEnabled(boolean enabled) {
        super.setPlatformEnabled(enabled);
        this.host.setEnabled(enabled);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setIcons(List icons) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setIcons");
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setTitle(String title) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setTitle " + title);
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void setVisible(boolean visible) {
        this.host.setEmbeddedStage(visible ? this : null);
        super.setVisible(visible);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setOpacity(float opacity) {
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setIconified(boolean iconified) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setIconified " + iconified);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setMaximized(boolean maximized) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setMaximized " + maximized);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedScene.setAlwaysOnTop " + alwaysOnTop);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setResizable(boolean resizable) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setResizable " + resizable);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setFullScreen(boolean fullScreen) {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.setFullScreen " + fullScreen);
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void requestFocus() {
        if (!this.host.requestFocus()) {
            return;
        }
        super.requestFocus();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void toBack() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.toBack");
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void toFront() {
        if (QuantumToolkit.verbose) {
            System.err.println("EmbeddedStage.toFront");
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public boolean grabFocus() {
        return this.host.grabFocus();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void ungrabFocus() {
        this.host.ungrabFocus();
    }

    private void notifyStageListener(Runnable r2) {
        AccessControlContext acc = getAccessControlContext();
        AccessController.doPrivileged(() -> {
            r2.run();
            return null;
        }, acc);
    }

    private void notifyStageListenerLater(Runnable r2) {
        Platform.runLater(() -> {
            notifyStageListener(r2);
        });
    }

    @Override // com.sun.javafx.embed.EmbeddedStageInterface
    public void setLocation(int x2, int y2) {
        Runnable r2 = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedLocation(x2, y2);
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            notifyStageListener(r2);
        } else {
            notifyStageListenerLater(r2);
        }
    }

    @Override // com.sun.javafx.embed.EmbeddedStageInterface
    public void setSize(int width, int height) {
        Runnable r2 = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedSize(width, height);
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            notifyStageListener(r2);
        } else {
            notifyStageListenerLater(r2);
        }
    }

    @Override // com.sun.javafx.embed.EmbeddedStageInterface
    public void setFocused(boolean focused, int focusCause) {
        Runnable r2 = () -> {
            if (this.stageListener != null) {
                this.stageListener.changedFocused(focused, AbstractEvents.focusCauseToPeerFocusCause(focusCause));
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            notifyStageListener(r2);
        } else {
            notifyStageListenerLater(r2);
        }
    }

    @Override // com.sun.javafx.embed.EmbeddedStageInterface
    public void focusUngrab() {
        Runnable r2 = () -> {
            if (this.stageListener != null) {
                this.stageListener.focusUngrab();
            }
        };
        if (Toolkit.getToolkit().isFxUserThread()) {
            notifyStageListener(r2);
        } else {
            notifyStageListenerLater(r2);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void requestInput(String text, int type, double width, double height, double Mxx, double Mxy, double Mxz, double Mxt, double Myx, double Myy, double Myz, double Myt, double Mzx, double Mzy, double Mzz, double Mzt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.TKStage
    public void releaseInput() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setRTL(boolean b2) {
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setEnabled(boolean enabled) {
    }

    @Override // com.sun.javafx.tk.TKStage
    public long getRawHandle() {
        return 0L;
    }
}
