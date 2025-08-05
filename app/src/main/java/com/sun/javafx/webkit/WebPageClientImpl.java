package com.sun.javafx.webkit;

import com.sun.javafx.scene.traversal.Direction;
import com.sun.javafx.util.Utils;
import com.sun.webkit.CursorManager;
import com.sun.webkit.WebPageClient;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import java.security.AccessController;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/WebPageClientImpl.class */
public final class WebPageClientImpl implements WebPageClient<WebView> {
    private final Accessor accessor;
    private Tooltip tooltip;
    private boolean isTooltipRegistered = false;
    private String oldTooltipText = "";
    private static WebConsoleListener consoleListener = null;
    private static final boolean backBufferSupported = Boolean.valueOf((String) AccessController.doPrivileged(() -> {
        return System.getProperty("com.sun.webkit.pagebackbuffer", "true");
    })).booleanValue();

    static void setConsoleListener(WebConsoleListener consoleListener2) {
        consoleListener = consoleListener2;
    }

    public WebPageClientImpl(Accessor accessor) {
        this.accessor = accessor;
    }

    @Override // com.sun.webkit.WebPageClient
    public void setFocus(boolean focus) {
        WebView view = this.accessor.getView();
        if (view != null && focus) {
            view.requestFocus();
        }
    }

    @Override // com.sun.webkit.WebPageClient
    public void setCursor(long cursorID) {
        WebView view = this.accessor.getView();
        if (view != null) {
            Object cursor = CursorManager.getCursorManager().getCursor(cursorID);
            view.setCursor(cursor instanceof Cursor ? (Cursor) cursor : Cursor.DEFAULT);
        }
    }

    @Override // com.sun.webkit.WebPageClient
    public void setTooltip(String tooltipText) {
        WebView view = this.accessor.getView();
        if (tooltipText != null) {
            if (this.tooltip == null) {
                this.tooltip = new Tooltip(tooltipText);
            } else {
                this.tooltip.setText(tooltipText);
                if (!this.oldTooltipText.equals(tooltipText)) {
                    Tooltip.uninstall(view, this.tooltip);
                    this.isTooltipRegistered = false;
                }
            }
            this.oldTooltipText = tooltipText;
            if (!this.isTooltipRegistered) {
                Tooltip.install(view, this.tooltip);
                this.isTooltipRegistered = true;
                return;
            }
            return;
        }
        if (this.isTooltipRegistered) {
            Tooltip.uninstall(view, this.tooltip);
            this.isTooltipRegistered = false;
        }
    }

    @Override // com.sun.webkit.WebPageClient
    public void transferFocus(boolean forward) {
        this.accessor.getView().impl_traverse(forward ? Direction.NEXT : Direction.PREVIOUS);
    }

    @Override // com.sun.webkit.WebPageClient
    public WCRectangle getScreenBounds(boolean available) {
        Rectangle2D bounds;
        WebView view = this.accessor.getView();
        Screen screen = Utils.getScreen(view);
        if (screen != null) {
            if (available) {
                bounds = screen.getVisualBounds();
            } else {
                bounds = screen.getBounds();
            }
            Rectangle2D r2 = bounds;
            return new WCRectangle((float) r2.getMinX(), (float) r2.getMinY(), (float) r2.getWidth(), (float) r2.getHeight());
        }
        return null;
    }

    @Override // com.sun.webkit.WebPageClient
    public int getScreenDepth() {
        return 24;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.webkit.WebPageClient
    public WebView getContainer() {
        return this.accessor.getView();
    }

    @Override // com.sun.webkit.WebPageClient
    public WCPoint screenToWindow(WCPoint ptScreen) {
        Window window;
        WebView view = this.accessor.getView();
        Scene scene = view.getScene();
        if (scene != null && (window = scene.getWindow()) != null) {
            Point2D pt = view.sceneToLocal((ptScreen.getX() - window.getX()) - scene.getX(), (ptScreen.getY() - window.getY()) - scene.getY());
            return new WCPoint((float) pt.getX(), (float) pt.getY());
        }
        return new WCPoint(0.0f, 0.0f);
    }

    @Override // com.sun.webkit.WebPageClient
    public WCPoint windowToScreen(WCPoint ptWindow) {
        Window window;
        WebView view = this.accessor.getView();
        Scene scene = view.getScene();
        if (scene != null && (window = scene.getWindow()) != null) {
            Point2D pt = view.localToScene(ptWindow.getX(), ptWindow.getY());
            return new WCPoint((float) (pt.getX() + scene.getX() + window.getX()), (float) (pt.getY() + scene.getY() + window.getY()));
        }
        return new WCPoint(0.0f, 0.0f);
    }

    @Override // com.sun.webkit.WebPageClient
    public WCPageBackBuffer createBackBuffer() {
        if (isBackBufferSupported()) {
            return WCGraphicsManager.getGraphicsManager().createPageBackBuffer();
        }
        return null;
    }

    @Override // com.sun.webkit.WebPageClient
    public boolean isBackBufferSupported() {
        return backBufferSupported;
    }

    @Override // com.sun.webkit.WebPageClient
    public void addMessageToConsole(String message, int lineNumber, String sourceId) {
        if (consoleListener != null) {
            try {
                consoleListener.messageAdded(this.accessor.getView(), message, lineNumber, sourceId);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override // com.sun.webkit.WebPageClient
    public void didClearWindowObject(long context, long windowObject) {
    }
}
