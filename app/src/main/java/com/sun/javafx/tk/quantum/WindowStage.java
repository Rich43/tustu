package com.sun.javafx.tk.quantum;

import com.sun.glass.events.WindowEvent;
import com.sun.glass.ui.Application;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.View;
import com.sun.glass.ui.Window;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.iio.common.PushbroomScaler;
import com.sun.javafx.iio.common.ScalerFactory;
import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKStage;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.Permission;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/WindowStage.class */
class WindowStage extends GlassStage {
    protected Window platformWindow;
    protected Stage fxStage;
    private StageStyle style;
    private GlassStage owner;
    private Modality modality;
    private final boolean securityDialog;
    private boolean transparent;
    private static List<WindowStage> activeWindows = new LinkedList();
    private static Map<Window, WindowStage> platformWindows = new HashMap();
    private static GlassAppletWindow appletWindow = null;
    private static final Locale LOCALE = Locale.getDefault();
    private static final ResourceBundle RESOURCES = ResourceBundle.getBundle(WindowStage.class.getPackage().getName() + ".QuantumMessagesBundle", LOCALE);
    private static final Permission fullScreenPermission = new AllPermission();
    private static final Permission alwaysOnTopPermission = new AllPermission();
    private OverlayWarning warning = null;
    private boolean rtl = false;
    private boolean isPrimaryStage = false;
    private boolean isAppletStage = false;
    private boolean isPopupStage = false;
    private boolean isInFullScreen = false;
    private boolean isAlwaysOnTop = false;
    private boolean inAllowedEventHandler = false;
    private boolean fullScreenFromUserEvent = false;
    private KeyCombination savedFullScreenExitKey = null;
    private boolean isClosePostponed = false;
    private Window deadWindow = null;

    static void setAppletWindow(GlassAppletWindow aw2) {
        appletWindow = aw2;
    }

    static GlassAppletWindow getAppletWindow() {
        return appletWindow;
    }

    public WindowStage(javafx.stage.Window peerWindow, boolean securityDialog, StageStyle stageStyle, Modality modality, TKStage owner) {
        this.owner = null;
        this.modality = Modality.NONE;
        this.transparent = false;
        this.style = stageStyle;
        this.owner = (GlassStage) owner;
        this.modality = modality;
        this.securityDialog = securityDialog;
        if (peerWindow instanceof Stage) {
            this.fxStage = (Stage) peerWindow;
        } else {
            this.fxStage = null;
        }
        this.transparent = stageStyle == StageStyle.TRANSPARENT;
        if (owner == null && this.modality == Modality.WINDOW_MODAL) {
            this.modality = Modality.NONE;
        }
    }

    final void setIsPrimary() {
        this.isPrimaryStage = true;
        if (appletWindow != null) {
            this.isAppletStage = true;
        }
    }

    final void setIsPopup() {
        this.isPopupStage = true;
    }

    final boolean isSecurityDialog() {
        return this.securityDialog;
    }

    public final WindowStage init(GlassSystemMenu sysmenu) {
        initPlatformWindow();
        this.platformWindow.setEventHandler(new GlassWindowEventHandler(this));
        if (sysmenu.isSupported()) {
            sysmenu.createMenuBar();
            this.platformWindow.setMenuBar(sysmenu.getMenuBar());
        }
        return this;
    }

    private void initPlatformWindow() {
        int windowMask;
        if (this.platformWindow == null) {
            Application app = Application.GetApplication();
            if (this.isPrimaryStage && null != appletWindow) {
                this.platformWindow = app.createWindow(appletWindow.getGlassWindow().getNativeWindow());
            } else {
                Window ownerWindow = null;
                if (this.owner instanceof WindowStage) {
                    ownerWindow = ((WindowStage) this.owner).platformWindow;
                }
                boolean resizable = false;
                boolean focusable = true;
                int windowMask2 = this.rtl ? 128 : 0;
                if (this.isPopupStage) {
                    windowMask = windowMask2 | 8;
                    if (this.style == StageStyle.TRANSPARENT) {
                        windowMask |= 2;
                    }
                    focusable = false;
                } else {
                    switch (this.style) {
                        case UNIFIED:
                            if (app.supportsUnifiedWindows()) {
                                windowMask2 |= 256;
                            }
                        case DECORATED:
                            windowMask = windowMask2 | 113;
                            if (ownerWindow != null || this.modality != Modality.NONE) {
                                windowMask &= -97;
                            }
                            resizable = true;
                            break;
                        case UTILITY:
                            windowMask = windowMask2 | 21;
                            break;
                        default:
                            windowMask = windowMask2 | (this.transparent ? 2 : 0) | 16;
                            break;
                    }
                }
                this.platformWindow = app.createWindow(ownerWindow, Screen.getMainScreen(), windowMask);
                this.platformWindow.setResizable(resizable);
                this.platformWindow.setFocusable(focusable);
                if (this.securityDialog) {
                    this.platformWindow.setLevel(2);
                }
            }
        }
        platformWindows.put(this.platformWindow, this);
    }

    final Window getPlatformWindow() {
        return this.platformWindow;
    }

    static WindowStage findWindowStage(Window platformWindow) {
        return platformWindows.get(platformWindow);
    }

    protected GlassStage getOwner() {
        return this.owner;
    }

    protected ViewScene getViewScene() {
        return (ViewScene) getScene();
    }

    StageStyle getStyle() {
        return this.style;
    }

    @Override // com.sun.javafx.tk.TKStage
    public TKScene createTKScene(boolean depthBuffer, boolean msaa, AccessControlContext acc) {
        ViewScene scene = new ViewScene(depthBuffer, msaa);
        scene.setSecurityContext(acc);
        return scene;
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void setScene(TKScene scene) {
        GlassScene oldScene = getScene();
        if (oldScene == scene) {
            return;
        }
        exitFullScreen();
        super.setScene(scene);
        if (scene != null) {
            GlassScene newScene = getViewScene();
            View view = newScene.getPlatformView();
            QuantumToolkit.runWithRenderLock(() -> {
                this.platformWindow.setView(view);
                if (oldScene != null) {
                    oldScene.updateSceneState();
                }
                newScene.updateSceneState();
                return null;
            });
            requestFocus();
        } else {
            QuantumToolkit.runWithRenderLock(() -> {
                if (this.platformWindow != null) {
                    this.platformWindow.setView(null);
                }
                if (oldScene != null) {
                    oldScene.updateSceneState();
                    return null;
                }
                return null;
            });
        }
        if (oldScene != null) {
            ViewPainter painter = ((ViewScene) oldScene).getPainter();
            QuantumRenderer.getInstance().disposePresentable(painter.presentable);
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setBounds(float x2, float y2, boolean xSet, boolean ySet, float w2, float h2, float cw, float ch, float xGravity, float yGravity) {
        int px;
        int py;
        if (this.isAppletStage) {
            ySet = false;
            xSet = false;
        }
        float pScale = this.platformWindow.getPlatformScale();
        if (xSet || ySet) {
            Screen screen = this.platformWindow.getScreen();
            List<Screen> screens = Screen.getScreens();
            if (screens.size() > 1) {
                float wx = xSet ? x2 : this.platformWindow.getX();
                float wy = ySet ? y2 : this.platformWindow.getY();
                float relx = (screen.getX() + (screen.getWidth() / 2.0f)) - wx;
                float rely = (screen.getY() + (screen.getHeight() / 2.0f)) - wy;
                float distsq = (relx * relx) + (rely * rely);
                for (Screen s2 : Screen.getScreens()) {
                    float relx2 = (s2.getX() + (s2.getWidth() / 2.0f)) - wx;
                    float rely2 = (s2.getY() + (s2.getHeight() / 2.0f)) - wy;
                    float distsq2 = (relx2 * relx2) + (rely2 * rely2);
                    if (distsq2 < distsq) {
                        screen = s2;
                        distsq = distsq2;
                    }
                }
            }
            float sx = screen == null ? 0.0f : screen.getX();
            float sy = screen == null ? 0.0f : screen.getY();
            px = xSet ? Math.round(sx + ((x2 - sx) * pScale)) : 0;
            py = ySet ? Math.round(sy + ((y2 - sy) * pScale)) : 0;
        } else {
            py = 0;
            px = 0;
        }
        int pw = (int) (w2 > 0.0f ? Math.ceil(w2 * pScale) : w2);
        int ph = (int) (h2 > 0.0f ? Math.ceil(h2 * pScale) : h2);
        int pcw = (int) (cw > 0.0f ? Math.ceil(cw * pScale) : cw);
        int pch = (int) (ch > 0.0f ? Math.ceil(ch * pScale) : ch);
        this.platformWindow.setBounds(px, py, xSet, ySet, pw, ph, pcw, pch, xGravity, yGravity);
    }

    @Override // com.sun.javafx.tk.TKStage
    public float getUIScale() {
        return this.platformWindow.getPlatformScale();
    }

    @Override // com.sun.javafx.tk.TKStage
    public float getRenderScale() {
        return this.platformWindow.getRenderScale();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setMinimumSize(int minWidth, int minHeight) {
        float pScale = this.platformWindow.getPlatformScale();
        this.platformWindow.setMinimumSize((int) Math.ceil(minWidth * pScale), (int) Math.ceil(minHeight * pScale));
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setMaximumSize(int maxWidth, int maxHeight) {
        float pScale = this.platformWindow.getPlatformScale();
        this.platformWindow.setMaximumSize((int) Math.ceil(maxWidth * pScale), (int) Math.ceil(maxHeight * pScale));
    }

    static Image findBestImage(List icons, int width, int height) {
        int adjw;
        int adjh;
        double scaleMeasure;
        Image image = null;
        double bestSimilarity = 3.0d;
        for (Object icon : icons) {
            Image im = (Image) icon;
            if (im != null && (im.getPixelFormat() == PixelFormat.BYTE_RGB || im.getPixelFormat() == PixelFormat.BYTE_BGRA_PRE || im.getPixelFormat() == PixelFormat.BYTE_GRAY)) {
                int iw = im.getWidth();
                int ih = im.getHeight();
                if (iw > 0 && ih > 0) {
                    double scaleFactor = Math.min(width / iw, height / ih);
                    if (scaleFactor >= 2.0d) {
                        double scaleFactor2 = Math.floor(scaleFactor);
                        adjw = iw * ((int) scaleFactor2);
                        adjh = ih * ((int) scaleFactor2);
                        scaleMeasure = 1.0d - (0.5d / scaleFactor2);
                    } else if (scaleFactor >= 1.0d) {
                        adjw = iw;
                        adjh = ih;
                        scaleMeasure = 0.0d;
                    } else if (scaleFactor >= 0.75d) {
                        adjw = (iw * 3) / 4;
                        adjh = (ih * 3) / 4;
                        scaleMeasure = 0.3d;
                    } else if (scaleFactor >= 0.6666d) {
                        adjw = (iw * 2) / 3;
                        adjh = (ih * 2) / 3;
                        scaleMeasure = 0.33d;
                    } else {
                        double scaleDivider = Math.ceil(1.0d / scaleFactor);
                        double d2 = 1.0d / scaleDivider;
                        adjw = (int) Math.round(iw / scaleDivider);
                        adjh = (int) Math.round(ih / scaleDivider);
                        scaleMeasure = 1.0d - (1.0d / scaleDivider);
                    }
                    double similarity = ((width - adjw) / width) + ((height - adjh) / height) + scaleMeasure;
                    if (similarity < bestSimilarity) {
                        bestSimilarity = similarity;
                        image = im;
                    }
                    if (similarity == 0.0d) {
                        break;
                    }
                }
            }
        }
        return image;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setIcons(List icons) {
        int SMALL_ICON_HEIGHT = 32;
        int SMALL_ICON_WIDTH = 32;
        if (PlatformUtil.isMac()) {
            SMALL_ICON_HEIGHT = 128;
            SMALL_ICON_WIDTH = 128;
        } else if (PlatformUtil.isWindows()) {
            SMALL_ICON_HEIGHT = 32;
            SMALL_ICON_WIDTH = 32;
        } else if (PlatformUtil.isLinux()) {
            SMALL_ICON_HEIGHT = 128;
            SMALL_ICON_WIDTH = 128;
        }
        if (icons == null || icons.size() < 1) {
            this.platformWindow.setIcon(null);
            return;
        }
        Image image = findBestImage(icons, SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT);
        if (image == null) {
            return;
        }
        PushbroomScaler scaler = ScalerFactory.createScaler(image.getWidth(), image.getHeight(), image.getBytesPerPixelUnit(), SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT, true);
        ByteBuffer buf = (ByteBuffer) image.getPixelBuffer();
        byte[] bytes = new byte[buf.limit()];
        int iheight = image.getHeight();
        for (int z2 = 0; z2 < iheight; z2++) {
            buf.position(z2 * image.getScanlineStride());
            buf.get(bytes, 0, image.getScanlineStride());
            scaler.putSourceScanline(bytes, 0);
        }
        buf.rewind();
        Image img = image.iconify(scaler.getDestination(), SMALL_ICON_WIDTH, SMALL_ICON_HEIGHT);
        this.platformWindow.setIcon(PixelUtils.imageToPixels(img));
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setTitle(String title) {
        this.platformWindow.setTitle(title);
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void setVisible(boolean visible) {
        if (!visible) {
            removeActiveWindow(this);
            if (this.modality == Modality.WINDOW_MODAL) {
                if (this.owner != null && (this.owner instanceof WindowStage)) {
                    ((WindowStage) this.owner).setEnabled(true);
                }
            } else if (this.modality == Modality.APPLICATION_MODAL) {
                windowsSetEnabled(true);
            } else if (!this.isPopupStage && this.owner != null && (this.owner instanceof WindowStage)) {
                WindowStage ownerStage = (WindowStage) this.owner;
                ownerStage.requestToFront();
            }
        }
        QuantumToolkit.runWithRenderLock(() -> {
            if (this.platformWindow != null) {
                this.platformWindow.setVisible(visible);
            }
            super.setVisible(visible);
            return null;
        });
        if (visible) {
            if (this.modality == Modality.WINDOW_MODAL) {
                if (this.owner != null && (this.owner instanceof WindowStage)) {
                    ((WindowStage) this.owner).setEnabled(false);
                }
            } else if (this.modality == Modality.APPLICATION_MODAL) {
                windowsSetEnabled(false);
            }
            if (this.isAppletStage && null != appletWindow) {
                appletWindow.assertStageOrder();
            }
        }
        applyFullScreen();
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage
    boolean isVisible() {
        return this.platformWindow.isVisible();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setOpacity(float opacity) {
        this.platformWindow.setAlpha(opacity);
        GlassScene gs = getScene();
        if (gs != null) {
            gs.entireSceneNeedsRepaint();
        }
    }

    public boolean needsUpdateWindow() {
        return this.transparent && Application.GetApplication().shouldUpdateWindow();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setIconified(boolean iconified) {
        if (this.platformWindow.isMinimized() == iconified) {
            return;
        }
        this.platformWindow.minimize(iconified);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setMaximized(boolean maximized) {
        if (this.platformWindow.isMaximized() == maximized) {
            return;
        }
        this.platformWindow.maximize(maximized);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        if (this.securityDialog || this.isAlwaysOnTop == alwaysOnTop) {
            return;
        }
        if (alwaysOnTop) {
            if (hasPermission(alwaysOnTopPermission)) {
                this.platformWindow.setLevel(2);
            } else {
                alwaysOnTop = false;
                if (this.stageListener != null) {
                    this.stageListener.changedAlwaysOnTop(false);
                }
            }
        } else {
            this.platformWindow.setLevel(1);
        }
        this.isAlwaysOnTop = alwaysOnTop;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setResizable(boolean resizable) {
        this.platformWindow.setResizable(resizable);
    }

    boolean isTrustedFullScreen() {
        return hasPermission(fullScreenPermission);
    }

    void exitFullScreen() {
        setFullScreen(false);
    }

    boolean isApplet() {
        return this.isPrimaryStage && null != appletWindow;
    }

    private boolean hasPermission(Permission perm) {
        try {
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                sm.checkPermission(perm, getAccessControlContext());
                return true;
            }
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    public final KeyCombination getSavedFullScreenExitKey() {
        return this.savedFullScreenExitKey;
    }

    private void applyFullScreen() {
        if (this.platformWindow == null) {
            return;
        }
        View v2 = this.platformWindow.getView();
        if (isVisible() && v2 != null && v2.isInFullscreen() != this.isInFullScreen) {
            if (this.isInFullScreen) {
                boolean isTrusted = isTrustedFullScreen();
                if (!isTrusted && !this.fullScreenFromUserEvent) {
                    exitFullScreen();
                    fullscreenChanged(false);
                } else {
                    v2.enterFullscreen(false, false, false);
                    if (this.warning != null && this.warning.inWarningTransition()) {
                        this.warning.setView(getViewScene());
                    } else {
                        boolean showWarning = true;
                        KeyCombination key = null;
                        String exitMessage = null;
                        if (isTrusted && this.fxStage != null) {
                            key = this.fxStage.getFullScreenExitKeyCombination();
                            exitMessage = this.fxStage.getFullScreenExitHint();
                        }
                        this.savedFullScreenExitKey = key == null ? defaultFullScreenExitKeycombo : key;
                        if ("".equals(exitMessage) || this.savedFullScreenExitKey.equals(KeyCombination.NO_MATCH)) {
                            showWarning = false;
                        }
                        if (showWarning && exitMessage == null) {
                            if (key == null) {
                                exitMessage = RESOURCES.getString("OverlayWarningESC");
                            } else {
                                String f2 = RESOURCES.getString("OverlayWarningKey");
                                exitMessage = String.format(f2, this.savedFullScreenExitKey.toString());
                            }
                        }
                        if (showWarning && this.warning == null) {
                            setWarning(new OverlayWarning(getViewScene()));
                        }
                        if (showWarning && this.warning != null) {
                            this.warning.warn(exitMessage);
                        }
                    }
                }
            } else {
                if (this.warning != null) {
                    this.warning.cancel();
                    setWarning(null);
                }
                v2.exitFullscreen(false);
            }
            this.fullScreenFromUserEvent = false;
            return;
        }
        if (!isVisible() && this.warning != null) {
            this.warning.cancel();
            setWarning(null);
        }
    }

    void setWarning(OverlayWarning newWarning) {
        this.warning = newWarning;
        getViewScene().synchroniseOverlayWarning();
    }

    OverlayWarning getWarning() {
        return this.warning;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setFullScreen(boolean fullScreen) {
        if (this.isInFullScreen == fullScreen) {
            return;
        }
        if (isInAllowedEventHandler()) {
            this.fullScreenFromUserEvent = true;
        }
        GlassStage fsWindow = activeFSWindow.get();
        if (fullScreen && fsWindow != null) {
            fsWindow.setFullScreen(false);
        }
        this.isInFullScreen = fullScreen;
        applyFullScreen();
        if (fullScreen) {
            activeFSWindow.set(this);
        }
    }

    void fullscreenChanged(boolean fs) {
        if (!fs) {
            if (activeFSWindow.compareAndSet(this, null)) {
                this.isInFullScreen = false;
            }
        } else {
            this.isInFullScreen = true;
            activeFSWindow.set(this);
        }
        AccessController.doPrivileged(() -> {
            if (this.stageListener != null) {
                this.stageListener.changedFullscreen(fs);
                return null;
            }
            return null;
        }, getAccessControlContext());
    }

    @Override // com.sun.javafx.tk.TKStage
    public void toBack() {
        this.platformWindow.toBack();
        if (this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void toFront() {
        this.platformWindow.requestFocus();
        this.platformWindow.toFront();
        if (this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public void postponeClose() {
        this.isClosePostponed = true;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void closePostponed() {
        if (this.deadWindow != null) {
            this.deadWindow.close();
            this.deadWindow = null;
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void close() {
        super.close();
        QuantumToolkit.runWithRenderLock(() -> {
            if (this.platformWindow != null) {
                platformWindows.remove(this.platformWindow);
                if (this.isClosePostponed) {
                    this.deadWindow = this.platformWindow;
                } else {
                    this.platformWindow.close();
                }
                this.platformWindow = null;
            }
            GlassScene oldScene = getViewScene();
            if (oldScene != null) {
                oldScene.updateSceneState();
                return null;
            }
            return null;
        });
    }

    void setPlatformWindowClosed() {
        this.platformWindow = null;
    }

    static void addActiveWindow(WindowStage window) {
        activeWindows.remove(window);
        activeWindows.add(window);
    }

    static void removeActiveWindow(WindowStage window) {
        activeWindows.remove(window);
    }

    final void handleFocusDisabled() {
        if (activeWindows.isEmpty()) {
            return;
        }
        WindowStage window = activeWindows.get(activeWindows.size() - 1);
        window.setIconified(false);
        window.requestToFront();
        window.requestFocus();
    }

    @Override // com.sun.javafx.tk.TKStage
    public boolean grabFocus() {
        return this.platformWindow.grabFocus();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void ungrabFocus() {
        this.platformWindow.ungrabFocus();
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void requestFocus() {
        this.platformWindow.requestFocus();
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage, com.sun.javafx.tk.TKStage
    public void requestFocus(FocusCause cause) {
        switch (cause) {
            case TRAVERSED_FORWARD:
                this.platformWindow.requestFocus(WindowEvent.FOCUS_GAINED_FORWARD);
                break;
            case TRAVERSED_BACKWARD:
                this.platformWindow.requestFocus(544);
                break;
            case ACTIVATED:
                this.platformWindow.requestFocus(WindowEvent.FOCUS_GAINED);
                break;
            case DEACTIVATED:
                this.platformWindow.requestFocus(541);
                break;
        }
    }

    @Override // com.sun.javafx.tk.quantum.GlassStage
    protected void setPlatformEnabled(boolean enabled) {
        super.setPlatformEnabled(enabled);
        this.platformWindow.setEnabled(enabled);
        if (enabled) {
            if (this.platformWindow.isEnabled()) {
                requestToFront();
                return;
            }
            return;
        }
        removeActiveWindow(this);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setEnabled(boolean enabled) {
        if (this.owner != null && (this.owner instanceof WindowStage)) {
            ((WindowStage) this.owner).setEnabled(enabled);
        }
        if (enabled && (this.platformWindow == null || this.platformWindow.isClosed())) {
            return;
        }
        setPlatformEnabled(enabled);
        if (enabled && this.isAppletStage && null != appletWindow) {
            appletWindow.assertStageOrder();
        }
    }

    @Override // com.sun.javafx.tk.TKStage
    public long getRawHandle() {
        return this.platformWindow.getRawHandle();
    }

    protected void requestToFront() {
        if (this.platformWindow != null) {
            this.platformWindow.toFront();
            this.platformWindow.requestFocus();
        }
    }

    public void setInAllowedEventHandler(boolean inAllowedEventHandler) {
        this.inAllowedEventHandler = inAllowedEventHandler;
    }

    private boolean isInAllowedEventHandler() {
        return this.inAllowedEventHandler;
    }

    @Override // com.sun.javafx.tk.TKStage
    public void requestInput(String text, int type, double width, double height, double Mxx, double Mxy, double Mxz, double Mxt, double Myx, double Myy, double Myz, double Myt, double Mzx, double Mzy, double Mzz, double Mzt) {
        this.platformWindow.requestInput(text, type, width, height, Mxx, Mxy, Mxz, Mxt, Myx, Myy, Myz, Myt, Mzx, Mzy, Mzz, Mzt);
    }

    @Override // com.sun.javafx.tk.TKStage
    public void releaseInput() {
        this.platformWindow.releaseInput();
    }

    @Override // com.sun.javafx.tk.TKStage
    public void setRTL(boolean b2) {
        this.rtl = b2;
    }
}
