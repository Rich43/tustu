package com.sun.javafx.tk.quantum;

import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKStageListener;
import com.sun.javafx.tk.Toolkit;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassStage.class */
abstract class GlassStage implements TKStage {
    private static final JavaSecurityAccess javaSecurityAccess;
    private static final List<GlassStage> windows;
    private static List<TKStage> importantWindows;
    private GlassScene scene;
    protected TKStageListener stageListener;
    private boolean visible;
    private boolean important = true;
    private AccessControlContext accessCtrlCtx = null;
    protected static final AtomicReference<GlassStage> activeFSWindow;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GlassStage.class.desiredAssertionStatus();
        javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
        windows = new ArrayList();
        importantWindows = new ArrayList();
        activeFSWindow = new AtomicReference<>();
    }

    protected GlassStage() {
        windows.add(this);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void close() {
        if (!$assertionsDisabled && this.scene != null) {
            throw new AssertionError();
        }
        windows.remove(this);
        importantWindows.remove(this);
        notifyWindowListeners();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setTKStageListener(TKStageListener listener) {
        this.stageListener = listener;
    }

    protected final GlassScene getScene() {
        return this.scene;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setScene(TKScene scene) {
        if (this.scene != null) {
            this.scene.setStage(null);
        }
        this.scene = (GlassScene) scene;
        if (this.scene != null) {
            this.scene.setStage(this);
        }
    }

    final AccessControlContext getAccessControlContext() {
        if (this.accessCtrlCtx == null) {
            throw new RuntimeException("Stage security context has not been set!");
        }
        return this.accessCtrlCtx;
    }

    public final void setSecurityContext(AccessControlContext ctx) {
        if (this.accessCtrlCtx != null) {
            throw new RuntimeException("Stage security context has been already set!");
        }
        AccessControlContext acc = AccessController.getContext();
        this.accessCtrlCtx = (AccessControlContext) javaSecurityAccess.doIntersectionPrivilege(() -> {
            return AccessController.getContext();
        }, acc, ctx);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void requestFocus() {
    }

    @Override // com.sun.javafx.tk.TKStage
    public void requestFocus(FocusCause cause) {
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            if (this.important) {
                importantWindows.add(this);
                notifyWindowListeners();
            }
        } else if (this.important) {
            importantWindows.remove(this);
            notifyWindowListeners();
        }
        if (this.scene != null) {
            this.scene.stageVisible(visible);
        }
    }

    boolean isVisible() {
        return this.visible;
    }

    protected void setPlatformEnabled(boolean enabled) {
    }

    void windowsSetEnabled(boolean enabled) {
        for (GlassStage window : (GlassStage[]) windows.toArray(new GlassStage[windows.size()])) {
            if (window != this && windows.contains(window)) {
                window.setPlatformEnabled(enabled);
            }
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setImportant(boolean important) {
        this.important = important;
    }

    private static void notifyWindowListeners() {
        Toolkit.getToolkit().notifyWindowListeners(importantWindows);
    }

    static void requestClosingAllWindows() {
        GlassStage fsWindow = activeFSWindow.get();
        if (fsWindow != null) {
            fsWindow.setFullScreen(false);
        }
        for (GlassStage window : (GlassStage[]) windows.toArray(new GlassStage[windows.size()])) {
            if (windows.contains(window) && window.isVisible() && window.stageListener != null) {
                AccessController.doPrivileged(() -> {
                    window.stageListener.closing();
                    return null;
                }, window.getAccessControlContext());
            }
        }
    }
}
