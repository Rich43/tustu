package javafx.embed.swing;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.embed.swing.Disposer;
import com.sun.javafx.embed.swing.DisposerRecord;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.DirtyBits;
import com.sun.javafx.sg.prism.NGExternalNode;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.stage.FocusUngrabEvent;
import com.sun.javafx.stage.WindowHelper;
import com.sun.javafx.tk.TKStage;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowFocusListener;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.nio.IntBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import javafx.stage.Window;
import javafx.stage.WindowEvent;
import javax.swing.JComponent;
import sun.awt.UngrabEvent;
import sun.swing.JLightweightFrame;
import sun.swing.LightweightContent;

/* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode.class */
public class SwingNode extends Node {
    private static boolean isThreadMerged;
    private double fxWidth;
    private double fxHeight;
    private int swingPrefWidth;
    private int swingPrefHeight;
    private int swingMaxWidth;
    private int swingMaxHeight;
    private int swingMinWidth;
    private int swingMinHeight;
    private volatile JComponent content;
    private volatile JLightweightFrame lwFrame;
    private NGExternalNode peer;
    private boolean skipBackwardUnrgabNotification;
    private boolean grabbed;
    private static final OptionalMethod<JLightweightFrame> jlfNotifyDisplayChanged;
    private static OptionalMethod<JLightweightFrame> jlfOverrideNativeWindowHandle;
    private static final OptionalMethod<JLightweightFrame> jlfSetHostBounds;
    private final ReentrantLock paintLock = new ReentrantLock();
    private volatile int scale = 1;
    private EventHandler windowHiddenHandler = event -> {
        if (this.lwFrame != null && (event.getTarget() instanceof Window)) {
            Window w2 = (Window) event.getTarget();
            TKStage tk = w2.impl_getPeer();
            if (tk != null) {
                if (isThreadMerged) {
                    jlfOverrideNativeWindowHandle.invoke(this.lwFrame, 0L, null);
                } else {
                    tk.postponeClose();
                    SwingFXUtils.runOnEDT(() -> {
                        jlfOverrideNativeWindowHandle.invoke(this.lwFrame, 0L, () -> {
                            SwingFXUtils.runOnFxThread(() -> {
                                tk.closePostponed();
                            });
                        });
                    });
                }
            }
        }
    };
    private Window hWindow = null;
    private List<Runnable> peerRequests = new ArrayList();
    private final InvalidationListener locationListener = observable -> {
        locateLwFrame();
    };
    private final EventHandler<FocusUngrabEvent> ungrabHandler = event -> {
        if (!this.skipBackwardUnrgabNotification && this.lwFrame != null) {
            AccessController.doPrivileged(new PostEventAction(new UngrabEvent(this.lwFrame)));
        }
    };
    private final ChangeListener<Boolean> windowVisibleListener = (observable, oldValue, newValue) -> {
        if (!newValue.booleanValue()) {
            disposeLwFrame();
        } else {
            setContent(this.content);
        }
    };
    private final ChangeListener<Window> sceneWindowListener = (observable, oldValue, newValue) -> {
        if (oldValue != null) {
            removeWindowListeners(oldValue);
        }
        notifyNativeHandle(newValue);
        if (newValue != null) {
            addWindowListeners(newValue);
        }
    };

    static {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javafx.embed.swing.SwingNode.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                boolean unused = SwingNode.isThreadMerged = Boolean.valueOf(System.getProperty("javafx.embed.singleThread")).booleanValue();
                return null;
            }
        });
        jlfNotifyDisplayChanged = new OptionalMethod<>(JLightweightFrame.class, "notifyDisplayChanged", Integer.TYPE);
        jlfOverrideNativeWindowHandle = new OptionalMethod<>(JLightweightFrame.class, "overrideNativeWindowHandle", Long.TYPE, Runnable.class);
        jlfSetHostBounds = new OptionalMethod<>(JLightweightFrame.class, "setHostBounds", Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
    }

    final JLightweightFrame getLightweightFrame() {
        return this.lwFrame;
    }

    public SwingNode() {
        setFocusTraversable(true);
        setEventHandler(MouseEvent.ANY, new SwingMouseEventHandler());
        setEventHandler(KeyEvent.ANY, new SwingKeyEventHandler());
        setEventHandler(ScrollEvent.SCROLL, new SwingScrollEventHandler());
        focusedProperty().addListener((observable, oldValue, newValue) -> {
            activateLwFrame(newValue.booleanValue());
        });
        Font.getFamilies();
    }

    private void notifyNativeHandle(Window window) {
        TKStage tkStage;
        if (this.hWindow != window) {
            if (this.hWindow != null) {
                this.hWindow.removeEventHandler(WindowEvent.WINDOW_HIDDEN, this.windowHiddenHandler);
            }
            if (window != null) {
                window.addEventHandler(WindowEvent.WINDOW_HIDDEN, this.windowHiddenHandler);
            }
            this.hWindow = window;
        }
        if (this.lwFrame != null) {
            long rawHandle = 0;
            if (window != null && (tkStage = window.impl_getPeer()) != null) {
                rawHandle = tkStage.getRawHandle();
            }
            jlfOverrideNativeWindowHandle.invoke(this.lwFrame, Long.valueOf(rawHandle), null);
        }
    }

    public void setContent(JComponent content) {
        this.content = content;
        SwingFXUtils.runOnEDT(() -> {
            setContentImpl(content);
        });
    }

    public JComponent getContent() {
        return this.content;
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$OptionalMethod.class */
    private static final class OptionalMethod<T> {
        private final Method method;

        public OptionalMethod(Class<T> cls, String name, Class<?>... args) {
            Method m2;
            try {
                m2 = cls.getMethod(name, args);
            } catch (NoSuchMethodException e2) {
                m2 = null;
            } catch (Throwable ex) {
                throw new RuntimeException("Error when calling " + cls.getName() + ".getMethod('" + name + "').", ex);
            }
            this.method = m2;
        }

        public boolean isSupported() {
            return this.method != null;
        }

        public Object invoke(T object, Object... args) {
            if (this.method != null) {
                try {
                    return this.method.invoke(object, args);
                } catch (Throwable ex) {
                    throw new RuntimeException("Error when calling " + object.getClass().getName() + "." + this.method.getName() + "().", ex);
                }
            }
            return null;
        }
    }

    private void setContentImpl(JComponent content) {
        if (this.lwFrame != null) {
            this.lwFrame.dispose();
            this.lwFrame = null;
        }
        if (content != null) {
            this.lwFrame = new JLightweightFrame();
            SwingNodeWindowFocusListener snfListener = new SwingNodeWindowFocusListener(this);
            this.lwFrame.addWindowFocusListener(snfListener);
            jlfNotifyDisplayChanged.invoke(this.lwFrame, Integer.valueOf(this.scale));
            this.lwFrame.setContent(new SwingNodeContent(content, this));
            this.lwFrame.setVisible(true);
            if (getScene() != null) {
                notifyNativeHandle(getScene().getWindow());
            }
            SwingNodeDisposer disposeRec = new SwingNodeDisposer(this.lwFrame);
            Disposer.addRecord(this, disposeRec);
            SwingFXUtils.runOnFxThread(() -> {
                locateLwFrame();
                if (focusedProperty().get()) {
                    activateLwFrame(true);
                }
            });
        }
    }

    void setImageBuffer(int[] data, int x2, int y2, int w2, int h2, int linestride, int scale) {
        Runnable r2 = () -> {
            Window win = getScene().getWindow();
            float uiScale = WindowHelper.getWindowAccessor().getUIScale(win);
            this.peer.setImageBuffer(IntBuffer.wrap(data), x2, y2, w2, h2, w2 / uiScale, h2 / uiScale, linestride, scale);
        };
        SwingFXUtils.runOnFxThread(() -> {
            if (this.peer != null) {
                r2.run();
            } else {
                this.peerRequests.clear();
                this.peerRequests.add(r2);
            }
        });
    }

    void setImageBounds(int x2, int y2, int w2, int h2) {
        Runnable r2 = () -> {
            Window win = getScene().getWindow();
            float uiScale = WindowHelper.getWindowAccessor().getUIScale(win);
            this.peer.setImageBounds(x2, y2, w2, h2, w2 / uiScale, h2 / uiScale);
        };
        SwingFXUtils.runOnFxThread(() -> {
            if (this.peer != null) {
                r2.run();
            } else {
                this.peerRequests.add(r2);
            }
        });
    }

    void repaintDirtyRegion(int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight) {
        Runnable r2 = () -> {
            this.peer.repaintDirtyRegion(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
            impl_markDirty(DirtyBits.NODE_CONTENTS);
        };
        SwingFXUtils.runOnFxThread(() -> {
            if (this.peer != null) {
                r2.run();
            } else {
                this.peerRequests.add(r2);
            }
        });
    }

    @Override // javafx.scene.Node
    public boolean isResizable() {
        return true;
    }

    @Override // javafx.scene.Node
    public void resize(double width, double height) {
        super.resize(width, height);
        if (width != this.fxWidth || height != this.fxHeight) {
            this.fxWidth = width;
            this.fxHeight = height;
            impl_geomChanged();
            impl_markDirty(DirtyBits.NODE_GEOMETRY);
            SwingFXUtils.runOnEDT(() -> {
                if (this.lwFrame != null) {
                    locateLwFrame();
                }
            });
        }
    }

    @Override // javafx.scene.Node
    public double prefWidth(double height) {
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(getScene().getWindow());
        return this.swingPrefWidth / uiScale;
    }

    @Override // javafx.scene.Node
    public double prefHeight(double width) {
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(getScene().getWindow());
        return this.swingPrefHeight / uiScale;
    }

    @Override // javafx.scene.Node
    public double maxWidth(double height) {
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(getScene().getWindow());
        return this.swingMaxWidth / uiScale;
    }

    @Override // javafx.scene.Node
    public double maxHeight(double width) {
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(getScene().getWindow());
        return this.swingMaxHeight / uiScale;
    }

    @Override // javafx.scene.Node
    public double minWidth(double height) {
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(getScene().getWindow());
        return this.swingMinWidth / uiScale;
    }

    @Override // javafx.scene.Node
    public double minHeight(double width) {
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(getScene().getWindow());
        return this.swingMinHeight / uiScale;
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected boolean impl_computeContains(double localX, double localY) {
        return true;
    }

    private void removeSceneListeners(Scene scene) {
        Window window = scene.getWindow();
        if (window != null) {
            removeWindowListeners(window);
        }
        scene.windowProperty().removeListener(this.sceneWindowListener);
    }

    private void addSceneListeners(Scene scene) {
        Window window = scene.getWindow();
        if (window != null) {
            addWindowListeners(window);
            notifyNativeHandle(window);
        }
        scene.windowProperty().addListener(this.sceneWindowListener);
    }

    private void addWindowListeners(Window window) {
        window.xProperty().addListener(this.locationListener);
        window.yProperty().addListener(this.locationListener);
        window.addEventHandler(FocusUngrabEvent.FOCUS_UNGRAB, this.ungrabHandler);
        window.showingProperty().addListener(this.windowVisibleListener);
        this.scale = Math.round(WindowHelper.getWindowAccessor().getRenderScale(window));
        setLwFrameScale(this.scale);
    }

    private void removeWindowListeners(Window window) {
        window.xProperty().removeListener(this.locationListener);
        window.yProperty().removeListener(this.locationListener);
        window.removeEventHandler(FocusUngrabEvent.FOCUS_UNGRAB, this.ungrabHandler);
        window.showingProperty().removeListener(this.windowVisibleListener);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected NGNode impl_createPeer() {
        this.peer = new NGExternalNode();
        this.peer.setLock(this.paintLock);
        for (Runnable request : this.peerRequests) {
            request.run();
        }
        this.peerRequests = null;
        if (getScene() != null) {
            addSceneListeners(getScene());
        }
        sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null) {
                removeSceneListeners(oldValue);
                disposeLwFrame();
            }
            if (newValue != null) {
                if (this.content != null && this.lwFrame == null) {
                    setContent(this.content);
                }
                addSceneListeners(newValue);
            }
        });
        impl_treeVisibleProperty().addListener((observable2, oldValue2, newValue2) -> {
            setLwFrameVisible(newValue2.booleanValue());
        });
        return this.peer;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public void impl_updatePeer() throws SecurityException {
        super.impl_updatePeer();
        if (impl_isDirty(DirtyBits.NODE_VISIBLE) || impl_isDirty(DirtyBits.NODE_BOUNDS)) {
            locateLwFrame();
        }
        if (impl_isDirty(DirtyBits.NODE_CONTENTS)) {
            this.peer.markContentDirty();
        }
    }

    private void locateLwFrame() {
        if (getScene() == null || this.lwFrame == null || getScene().getWindow() == null || !getScene().getWindow().isShowing()) {
            return;
        }
        Window w2 = getScene().getWindow();
        float renderScale = WindowHelper.getWindowAccessor().getRenderScale(w2);
        float uiScale = WindowHelper.getWindowAccessor().getUIScale(w2);
        int lwScale = Math.round(renderScale);
        boolean sendScale = this.scale != lwScale;
        this.scale = lwScale;
        Point2D loc = localToScene(0.0d, 0.0d);
        int windowX = (int) (w2.getX() * uiScale);
        int windowY = (int) (w2.getY() * uiScale);
        int windowW = (int) (w2.getWidth() * uiScale);
        int windowH = (int) (w2.getHeight() * uiScale);
        int frameX = (int) Math.round((w2.getX() + getScene().getX() + loc.getX()) * uiScale);
        int frameY = (int) Math.round((w2.getY() + getScene().getY() + loc.getY()) * uiScale);
        int frameW = (int) (this.fxWidth * uiScale);
        int frameH = (int) (this.fxHeight * uiScale);
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                if (sendScale) {
                    jlfNotifyDisplayChanged.invoke(this.lwFrame, Integer.valueOf(this.scale));
                }
                this.lwFrame.setSize(frameW, frameH);
                this.lwFrame.setLocation(frameX, frameY);
                jlfSetHostBounds.invoke(this.lwFrame, Integer.valueOf(windowX), Integer.valueOf(windowY), Integer.valueOf(windowW), Integer.valueOf(windowH));
            }
        });
    }

    private void activateLwFrame(boolean activate) {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                this.lwFrame.emulateActivation(activate);
            }
        });
    }

    private void disposeLwFrame() {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                this.lwFrame.dispose();
                this.lwFrame = null;
            }
        });
    }

    private void setLwFrameVisible(boolean visible) {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(() -> {
            if (this.lwFrame != null) {
                this.lwFrame.setVisible(visible);
            }
        });
    }

    private void setLwFrameScale(final int scale) {
        if (this.lwFrame == null) {
            return;
        }
        SwingFXUtils.runOnEDT(new Runnable() { // from class: javafx.embed.swing.SwingNode.2
            @Override // java.lang.Runnable
            public void run() {
                if (SwingNode.this.lwFrame != null) {
                    SwingNode.jlfNotifyDisplayChanged.invoke(SwingNode.this.lwFrame, Integer.valueOf(scale));
                }
            }
        });
    }

    @Override // javafx.scene.Node
    @Deprecated
    public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
        bounds.deriveWithNewBounds(0.0f, 0.0f, 0.0f, (float) this.fxWidth, (float) this.fxHeight, 0.0f);
        tx.transform(bounds, bounds);
        return bounds;
    }

    @Override // javafx.scene.Node
    @Deprecated
    public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
        return alg.processLeafNode(this, ctx);
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$SwingNodeDisposer.class */
    private static class SwingNodeDisposer implements DisposerRecord {
        JLightweightFrame lwFrame;

        SwingNodeDisposer(JLightweightFrame ref) {
            this.lwFrame = ref;
        }

        @Override // com.sun.javafx.embed.swing.DisposerRecord
        public void dispose() {
            if (this.lwFrame != null) {
                this.lwFrame.dispose();
                this.lwFrame = null;
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$SwingNodeWindowFocusListener.class */
    private static class SwingNodeWindowFocusListener implements WindowFocusListener {
        private WeakReference<SwingNode> swingNodeRef;

        SwingNodeWindowFocusListener(SwingNode swingNode) {
            this.swingNodeRef = new WeakReference<>(swingNode);
        }

        @Override // java.awt.event.WindowFocusListener
        public void windowGainedFocus(java.awt.event.WindowEvent e2) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.requestFocus();
                }
            });
        }

        @Override // java.awt.event.WindowFocusListener
        public void windowLostFocus(java.awt.event.WindowEvent e2) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.ungrabFocus(true);
                }
            });
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$SwingNodeContent.class */
    private static class SwingNodeContent implements LightweightContent {
        private JComponent comp;
        private volatile FXDnD dnd;
        private WeakReference<SwingNode> swingNodeRef;

        public SwingNodeContent(JComponent comp, SwingNode swingNode) {
            this.comp = comp;
            this.swingNodeRef = new WeakReference<>(swingNode);
        }

        @Override // sun.swing.LightweightContent
        public JComponent getComponent() {
            return this.comp;
        }

        @Override // sun.swing.LightweightContent
        public void paintLock() {
            SwingNode swingNode = this.swingNodeRef.get();
            if (swingNode != null) {
                swingNode.paintLock.lock();
            }
        }

        @Override // sun.swing.LightweightContent
        public void paintUnlock() {
            SwingNode swingNode = this.swingNodeRef.get();
            if (swingNode != null) {
                swingNode.paintLock.unlock();
            }
        }

        @Override // sun.swing.LightweightContent
        public void imageBufferReset(int[] data, int x2, int y2, int width, int height, int linestride) {
            imageBufferReset(data, x2, y2, width, height, linestride, 1);
        }

        @Override // sun.swing.LightweightContent
        public void imageBufferReset(int[] data, int x2, int y2, int width, int height, int linestride, int scale) {
            SwingNode swingNode = this.swingNodeRef.get();
            if (swingNode != null) {
                swingNode.setImageBuffer(data, x2, y2, width, height, linestride, scale);
            }
        }

        @Override // sun.swing.LightweightContent
        public void imageReshaped(int x2, int y2, int width, int height) {
            SwingNode swingNode = this.swingNodeRef.get();
            if (swingNode != null) {
                swingNode.setImageBounds(x2, y2, width, height);
            }
        }

        @Override // sun.swing.LightweightContent
        public void imageUpdated(int dirtyX, int dirtyY, int dirtyWidth, int dirtyHeight) {
            SwingNode swingNode = this.swingNodeRef.get();
            if (swingNode != null) {
                swingNode.repaintDirtyRegion(dirtyX, dirtyY, dirtyWidth, dirtyHeight);
            }
        }

        @Override // sun.swing.LightweightContent
        public void focusGrabbed() {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode;
                Scene scene;
                if (!PlatformUtil.isLinux() && (swingNode = this.swingNodeRef.get()) != null && (scene = swingNode.getScene()) != null && scene.getWindow() != null && scene.getWindow().impl_getPeer() != null) {
                    scene.getWindow().impl_getPeer().grabFocus();
                    swingNode.grabbed = true;
                }
            });
        }

        @Override // sun.swing.LightweightContent
        public void focusUngrabbed() {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.ungrabFocus(false);
                }
            });
        }

        @Override // sun.swing.LightweightContent
        public void preferredSizeChanged(int width, int height) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.swingPrefWidth = width;
                    swingNode.swingPrefHeight = height;
                    swingNode.impl_notifyLayoutBoundsChanged();
                }
            });
        }

        @Override // sun.swing.LightweightContent
        public void maximumSizeChanged(int width, int height) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.swingMaxWidth = width;
                    swingNode.swingMaxHeight = height;
                    swingNode.impl_notifyLayoutBoundsChanged();
                }
            });
        }

        @Override // sun.swing.LightweightContent
        public void minimumSizeChanged(int width, int height) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.swingMinWidth = width;
                    swingNode.swingMinHeight = height;
                    swingNode.impl_notifyLayoutBoundsChanged();
                }
            });
        }

        @Override // sun.swing.LightweightContent
        public void setCursor(Cursor cursor) {
            SwingFXUtils.runOnFxThread(() -> {
                SwingNode swingNode = this.swingNodeRef.get();
                if (swingNode != null) {
                    swingNode.setCursor(SwingCursors.embedCursorToCursor(cursor));
                }
            });
        }

        private void initDnD() {
            SwingNode swingNode;
            synchronized (this) {
                if (this.dnd == null && (swingNode = this.swingNodeRef.get()) != null) {
                    this.dnd = new FXDnD(swingNode);
                }
            }
        }

        @Override // sun.swing.LightweightContent
        public synchronized <T extends DragGestureRecognizer> T createDragGestureRecognizer(Class<T> cls, DragSource dragSource, Component component, int i2, DragGestureListener dragGestureListener) {
            initDnD();
            return (T) this.dnd.createDragGestureRecognizer(cls, dragSource, component, i2, dragGestureListener);
        }

        @Override // sun.swing.LightweightContent
        public DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent dge) throws InvalidDnDOperationException {
            initDnD();
            return this.dnd.createDragSourceContextPeer(dge);
        }

        @Override // sun.swing.LightweightContent
        public void addDropTarget(DropTarget dt) {
            initDnD();
            this.dnd.addDropTarget(dt);
        }

        @Override // sun.swing.LightweightContent
        public void removeDropTarget(DropTarget dt) {
            initDnD();
            this.dnd.removeDropTarget(dt);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void ungrabFocus(boolean postUngrabEvent) {
        if (!PlatformUtil.isLinux() && this.grabbed && getScene() != null && getScene().getWindow() != null && getScene().getWindow().impl_getPeer() != null) {
            this.skipBackwardUnrgabNotification = !postUngrabEvent;
            getScene().getWindow().impl_getPeer().ungrabFocus();
            this.skipBackwardUnrgabNotification = false;
            this.grabbed = false;
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$PostEventAction.class */
    private class PostEventAction implements PrivilegedAction<Void> {
        private AWTEvent event;

        public PostEventAction(AWTEvent event) {
            this.event = event;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public Void run2() {
            EventQueue eq = Toolkit.getDefaultToolkit().getSystemEventQueue();
            eq.postEvent(this.event);
            return null;
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$SwingMouseEventHandler.class */
    private class SwingMouseEventHandler implements EventHandler<MouseEvent> {
        private final Set<MouseButton> mouseClickedAllowed;

        private SwingMouseEventHandler() {
            this.mouseClickedAllowed = new HashSet();
        }

        @Override // javafx.event.EventHandler
        public void handle(MouseEvent event) {
            int swingID;
            JLightweightFrame frame = SwingNode.this.lwFrame;
            if (frame == null || (swingID = SwingEvents.fxMouseEventTypeToMouseID(event)) < 0) {
                return;
            }
            event.consume();
            EventType<?> type = event.getEventType();
            if (type == MouseEvent.MOUSE_PRESSED) {
                this.mouseClickedAllowed.add(event.getButton());
            } else if (type != MouseEvent.MOUSE_RELEASED) {
                if (type == MouseEvent.MOUSE_DRAGGED) {
                    this.mouseClickedAllowed.clear();
                } else if (type == MouseEvent.MOUSE_CLICKED) {
                    if (event.getClickCount() == 1 && !this.mouseClickedAllowed.contains(event.getButton())) {
                        return;
                    } else {
                        this.mouseClickedAllowed.remove(event.getButton());
                    }
                }
            }
            int swingModifiers = SwingEvents.fxMouseModsToMouseMods(event);
            boolean swingPopupTrigger = event.isPopupTrigger();
            int swingButton = SwingEvents.fxMouseButtonToMouseButton(event);
            long swingWhen = System.currentTimeMillis();
            Window win = SwingNode.this.getScene().getWindow();
            float uiScale = WindowHelper.getWindowAccessor().getUIScale(win);
            int relX = (int) Math.round(event.getX() * uiScale);
            int relY = (int) Math.round(event.getY() * uiScale);
            int absX = (int) Math.round(event.getScreenX() * uiScale);
            int absY = (int) Math.round(event.getScreenY() * uiScale);
            java.awt.event.MouseEvent mouseEvent = new java.awt.event.MouseEvent(frame, swingID, swingWhen, swingModifiers, relX, relY, absX, absY, event.getClickCount(), swingPopupTrigger, swingButton);
            AccessController.doPrivileged(SwingNode.this.new PostEventAction(mouseEvent));
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$SwingScrollEventHandler.class */
    private class SwingScrollEventHandler implements EventHandler<ScrollEvent> {
        private SwingScrollEventHandler() {
        }

        @Override // javafx.event.EventHandler
        public void handle(ScrollEvent event) {
            double deltaX;
            JLightweightFrame frame = SwingNode.this.lwFrame;
            if (frame == null) {
                return;
            }
            int swingModifiers = SwingEvents.fxScrollModsToMouseWheelMods(event);
            boolean isShift = (swingModifiers & 64) != 0;
            if (!isShift && event.getDeltaY() != 0.0d) {
                sendMouseWheelEvent(frame, event.getX(), event.getY(), swingModifiers, event.getDeltaY() / event.getMultiplierY());
            }
            if (isShift && event.getDeltaY() != 0.0d) {
                deltaX = event.getDeltaY() / event.getMultiplierY();
            } else {
                deltaX = event.getDeltaX() / event.getMultiplierX();
            }
            double delta = deltaX;
            if (delta != 0.0d) {
                sendMouseWheelEvent(frame, event.getX(), event.getY(), swingModifiers | 64, delta);
            }
        }

        private void sendMouseWheelEvent(Component source, double fxX, double fxY, int swingModifiers, double delta) {
            int wheelRotation = (int) delta;
            int signum = (int) Math.signum(delta);
            if (signum * delta < 1.0d) {
                wheelRotation = signum;
            }
            Window w2 = SwingNode.this.getScene().getWindow();
            float uiScale = WindowHelper.getWindowAccessor().getUIScale(w2);
            int x2 = (int) Math.round(fxX * uiScale);
            int y2 = (int) Math.round(fxY * uiScale);
            MouseWheelEvent mouseWheelEvent = new MouseWheelEvent(source, 507, System.currentTimeMillis(), swingModifiers, x2, y2, 0, 0, 0, false, 0, 1, -wheelRotation);
            AccessController.doPrivileged(SwingNode.this.new PostEventAction(mouseWheelEvent));
        }
    }

    /* loaded from: jfxrt.jar:javafx/embed/swing/SwingNode$SwingKeyEventHandler.class */
    private class SwingKeyEventHandler implements EventHandler<KeyEvent> {
        private SwingKeyEventHandler() {
        }

        @Override // javafx.event.EventHandler
        public void handle(KeyEvent event) {
            JLightweightFrame frame = SwingNode.this.lwFrame;
            if (frame == null || event.getCharacter().isEmpty()) {
                return;
            }
            if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.TAB) {
                event.consume();
            }
            int swingID = SwingEvents.fxKeyEventTypeToKeyID(event);
            if (swingID < 0) {
                return;
            }
            int swingModifiers = SwingEvents.fxKeyModsToKeyMods(event);
            int swingKeyCode = event.getCode().impl_getCode();
            char swingChar = event.getCharacter().charAt(0);
            if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                String text = event.getText();
                if (text.length() == 1) {
                    swingChar = text.charAt(0);
                }
            }
            long swingWhen = System.currentTimeMillis();
            java.awt.event.KeyEvent keyEvent = new java.awt.event.KeyEvent(frame, swingID, swingWhen, swingModifiers, swingKeyCode, swingChar);
            AccessController.doPrivileged(SwingNode.this.new PostEventAction(keyEvent));
        }
    }
}
