package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.TKStage;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.stage.Stage;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/GlassAppletWindow.class */
class GlassAppletWindow implements AppletWindow {
    private final Window glassWindow;
    private WeakReference<Stage> topStage;
    private String serverName;

    GlassAppletWindow(long nativeParent, String serverName) {
        if (0 == nativeParent) {
            if (serverName != null) {
                throw new RuntimeException("GlassAppletWindow constructor used incorrectly.");
            }
            this.glassWindow = Application.GetApplication().createWindow(null, 0);
        } else {
            this.serverName = serverName;
            this.glassWindow = Application.GetApplication().createWindow(nativeParent);
        }
    }

    Window getGlassWindow() {
        return this.glassWindow;
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void setBackgroundColor(int color) {
        Application.invokeLater(() -> {
            float RR = ((color >> 16) & 255) / 255.0f;
            float GG = ((color >> 8) & 255) / 255.0f;
            float BB = (color & 255) / 255.0f;
            this.glassWindow.setBackground(RR, GG, BB);
        });
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void setForegroundColor(int color) {
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void setVisible(boolean state) {
        Application.invokeLater(() -> {
            this.glassWindow.setVisible(state);
        });
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void setSize(int width, int height) {
        Application.invokeLater(() -> {
            this.glassWindow.setSize(width, height);
        });
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public int getWidth() {
        AtomicInteger width = new AtomicInteger(0);
        Application.invokeAndWait(() -> {
            width.set(this.glassWindow.getWidth());
        });
        return width.get();
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public int getHeight() {
        AtomicInteger height = new AtomicInteger(0);
        Application.invokeAndWait(() -> {
            height.set(this.glassWindow.getHeight());
        });
        return height.get();
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void setPosition(int x2, int y2) {
        Application.invokeLater(() -> {
            this.glassWindow.setPosition(x2, y2);
        });
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public int getPositionX() {
        AtomicInteger x2 = new AtomicInteger(0);
        Application.invokeAndWait(() -> {
            x2.set(this.glassWindow.getX());
        });
        return x2.get();
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public int getPositionY() {
        AtomicInteger y2 = new AtomicInteger(0);
        Application.invokeAndWait(() -> {
            y2.set(this.glassWindow.getY());
        });
        return y2.get();
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public float getUIScale() {
        AtomicReference<Float> uiScale = new AtomicReference<>(Float.valueOf(0.0f));
        Application.invokeAndWait(() -> {
            uiScale.set(Float.valueOf(this.glassWindow.getPlatformScale()));
        });
        return uiScale.get().floatValue();
    }

    void dispose() {
        QuantumToolkit.runWithRenderLock(() -> {
            this.glassWindow.close();
            return null;
        });
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void setStageOnTop(Stage topStage) {
        if (null != topStage) {
            this.topStage = new WeakReference<>(topStage);
        } else {
            this.topStage = null;
        }
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public int getRemoteLayerId() {
        AtomicInteger id = new AtomicInteger(-1);
        Application.invokeAndWait(() -> {
            View view = this.glassWindow.getView();
            if (view != null) {
                id.set(view.getNativeRemoteLayerId(this.serverName));
            }
        });
        return id.get();
    }

    @Override // com.sun.javafx.tk.AppletWindow
    public void dispatchEvent(Map eventInfo) {
        Application.invokeAndWait(() -> {
            this.glassWindow.dispatchNpapiEvent(eventInfo);
        });
    }

    void assertStageOrder() {
        Stage ts;
        Window pw;
        if (null != this.topStage && null != (ts = this.topStage.get())) {
            TKStage tsp = ts.impl_getPeer();
            if ((tsp instanceof WindowStage) && ((WindowStage) tsp).isVisible() && null != (pw = ((WindowStage) tsp).getPlatformWindow())) {
                pw.toFront();
            }
        }
    }
}
