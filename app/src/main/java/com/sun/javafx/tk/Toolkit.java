package com.sun.javafx.tk;

import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.beans.event.AbstractNotifyListener;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.HighlightRegion;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.VersionInfo;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.SceneHelper;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGLightBase;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Color4f;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import javafx.application.ConditionalFeature;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.Scene;
import javafx.scene.effect.BlurType;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/Toolkit.class */
public abstract class Toolkit {
    private static String tk;
    private static Toolkit TOOLKIT;
    private static final String QUANTUM_TOOLKIT = "com.sun.javafx.tk.quantum.QuantumToolkit";
    private static final String DEFAULT_TOOLKIT = "com.sun.javafx.tk.quantum.QuantumToolkit";
    private final Map<TKPulseListener, AccessControlContext> stagePulseListeners = new WeakHashMap();
    private final Map<TKPulseListener, AccessControlContext> scenePulseListeners = new WeakHashMap();
    private final Map<TKPulseListener, AccessControlContext> postScenePulseListeners = new WeakHashMap();
    private final Map<TKListener, AccessControlContext> toolkitListeners = new WeakHashMap();
    private final Set<Runnable> shutdownHooks = new HashSet();
    private TKPulseListener lastTkPulseListener = null;
    private AccessControlContext lastTkPulseAcc = null;
    private CountDownLatch pauseScenesLatch = null;
    private Set<HighlightRegion> highlightRegions;
    private static Thread fxUserThread = null;
    private static final Map gradientMap = new WeakHashMap();
    private static final boolean verbose = ((Boolean) AccessController.doPrivileged(() -> {
        return Boolean.valueOf(Boolean.getBoolean("javafx.verbose"));
    })).booleanValue();
    private static final String[] msLibNames = {"api-ms-win-core-console-l1-1-0", "api-ms-win-core-console-l1-2-0", "api-ms-win-core-datetime-l1-1-0", "api-ms-win-core-debug-l1-1-0", "api-ms-win-core-errorhandling-l1-1-0", "api-ms-win-core-file-l1-1-0", "api-ms-win-core-file-l1-2-0", "api-ms-win-core-file-l2-1-0", "api-ms-win-core-handle-l1-1-0", "api-ms-win-core-heap-l1-1-0", "api-ms-win-core-interlocked-l1-1-0", "api-ms-win-core-libraryloader-l1-1-0", "api-ms-win-core-localization-l1-2-0", "api-ms-win-core-memory-l1-1-0", "api-ms-win-core-namedpipe-l1-1-0", "api-ms-win-core-processenvironment-l1-1-0", "api-ms-win-core-processthreads-l1-1-0", "api-ms-win-core-processthreads-l1-1-1", "api-ms-win-core-profile-l1-1-0", "api-ms-win-core-rtlsupport-l1-1-0", "api-ms-win-core-string-l1-1-0", "api-ms-win-core-synch-l1-1-0", "api-ms-win-core-synch-l1-2-0", "api-ms-win-core-sysinfo-l1-1-0", "api-ms-win-core-timezone-l1-1-0", "api-ms-win-core-util-l1-1-0", "api-ms-win-crt-conio-l1-1-0", "api-ms-win-crt-convert-l1-1-0", "api-ms-win-crt-environment-l1-1-0", "api-ms-win-crt-filesystem-l1-1-0", "api-ms-win-crt-heap-l1-1-0", "api-ms-win-crt-locale-l1-1-0", "api-ms-win-crt-math-l1-1-0", "api-ms-win-crt-multibyte-l1-1-0", "api-ms-win-crt-private-l1-1-0", "api-ms-win-crt-process-l1-1-0", "api-ms-win-crt-runtime-l1-1-0", "api-ms-win-crt-stdio-l1-1-0", "api-ms-win-crt-string-l1-1-0", "api-ms-win-crt-time-l1-1-0", "api-ms-win-crt-utility-l1-1-0", "ucrtbase", "vcruntime140", "msvcp140", "msvcp140_1", "msvcp140_2", "concrt140"};
    private static WritableImageAccessor writableImageAccessor = null;
    private static PaintAccessor paintAccessor = null;
    private static ImageAccessor imageAccessor = null;

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/Toolkit$ImageAccessor.class */
    public interface ImageAccessor {
        boolean isAnimation(Image image);

        ReadOnlyObjectProperty<PlatformImage> getImageProperty(Image image);

        int[] getPreColors(PixelFormat<ByteBuffer> pixelFormat);

        int[] getNonPreColors(PixelFormat<ByteBuffer> pixelFormat);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/Toolkit$ImageRenderingContext.class */
    public static class ImageRenderingContext {
        public NGNode root;

        /* renamed from: x, reason: collision with root package name */
        public int f11964x;

        /* renamed from: y, reason: collision with root package name */
        public int f11965y;
        public int width;
        public int height;
        public BaseTransform transform;
        public boolean depthBuffer;
        public Object platformPaint;
        public NGCamera camera;
        public NGLightBase[] lights;
        public Object platformImage;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/Toolkit$PaintAccessor.class */
    public interface PaintAccessor {
        boolean isMutable(Paint paint);

        Object getPlatformPaint(Paint paint);

        void addListener(Paint paint, AbstractNotifyListener abstractNotifyListener);

        void removeListener(Paint paint, AbstractNotifyListener abstractNotifyListener);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/Toolkit$Task.class */
    public interface Task {
        boolean isFinished();
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/Toolkit$WritableImageAccessor.class */
    public interface WritableImageAccessor {
        void loadTkImage(WritableImage writableImage, Object obj);

        Object getTkImageLoader(WritableImage writableImage);
    }

    public abstract boolean init();

    public abstract boolean canStartNestedEventLoop();

    public abstract Object enterNestedEventLoop(Object obj);

    public abstract void exitNestedEventLoop(Object obj, Object obj2);

    public abstract boolean isNestedLoopRunning();

    public abstract TKStage createTKStage(Window window, boolean z2, StageStyle stageStyle, boolean z3, Modality modality, TKStage tKStage, boolean z4, AccessControlContext accessControlContext);

    public abstract TKStage createTKPopupStage(Window window, StageStyle stageStyle, TKStage tKStage, AccessControlContext accessControlContext);

    public abstract TKStage createTKEmbeddedStage(HostInterface hostInterface, AccessControlContext accessControlContext);

    public abstract AppletWindow createAppletWindow(long j2, String str);

    public abstract void closeAppletWindow();

    public abstract void requestNextPulse();

    public abstract Future addRenderJob(RenderJob renderJob);

    public abstract ImageLoader loadImage(String str, int i2, int i3, boolean z2, boolean z3);

    public abstract ImageLoader loadImage(InputStream inputStream, int i2, int i3, boolean z2, boolean z3);

    public abstract AsyncOperation loadImageAsync(AsyncOperationListener<? extends ImageLoader> asyncOperationListener, String str, int i2, int i3, boolean z2, boolean z3);

    public abstract ImageLoader loadPlatformImage(Object obj);

    public abstract PlatformImage createPlatformImage(int i2, int i3);

    public abstract void startup(Runnable runnable);

    public abstract void defer(Runnable runnable);

    public abstract Map<Object, Object> getContextMap();

    public abstract int getRefreshRate();

    public abstract void setAnimationRunnable(DelayedRunnable delayedRunnable);

    public abstract PerformanceTracker getPerformanceTracker();

    public abstract PerformanceTracker createPerformanceTracker();

    public abstract void waitFor(Task task);

    protected abstract Object createColorPaint(Color color);

    protected abstract Object createLinearGradientPaint(LinearGradient linearGradient);

    protected abstract Object createRadialGradientPaint(RadialGradient radialGradient);

    protected abstract Object createImagePatternPaint(ImagePattern imagePattern);

    public abstract void accumulateStrokeBounds(Shape shape, float[] fArr, StrokeType strokeType, double d2, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2, BaseTransform baseTransform);

    public abstract boolean strokeContains(Shape shape, double d2, double d3, StrokeType strokeType, double d4, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2);

    public abstract Shape createStrokedShape(Shape shape, StrokeType strokeType, double d2, StrokeLineCap strokeLineCap, StrokeLineJoin strokeLineJoin, float f2, float[] fArr, float f3);

    public abstract int getKeyCodeForChar(String str);

    public abstract Dimension2D getBestCursorSize(int i2, int i3);

    public abstract int getMaximumCursorColors();

    public abstract PathElement[] convertShapeToFXPath(Object obj);

    public abstract HitInfo convertHitInfoToFX(Object obj);

    public abstract Filterable toFilterable(Image image);

    public abstract FilterContext getFilterContext(Object obj);

    public abstract boolean isForwardTraversalKey(KeyEvent keyEvent);

    public abstract boolean isBackwardTraversalKey(KeyEvent keyEvent);

    public abstract AbstractMasterTimer getMasterTimer();

    public abstract FontLoader getFontLoader();

    public abstract TextLayoutFactory getTextLayoutFactory();

    public abstract Object createSVGPathObject(SVGPath sVGPath);

    public abstract Path2D createSVGPath2D(SVGPath sVGPath);

    public abstract boolean imageContains(Object obj, float f2, float f3);

    public abstract TKClipboard getSystemClipboard();

    public abstract TKSystemMenu getSystemMenu();

    public abstract TKClipboard getNamedClipboard(String str);

    public abstract ScreenConfigurationAccessor setScreenConfigurationListener(TKScreenConfigurationListener tKScreenConfigurationListener);

    public abstract Object getPrimaryScreen();

    public abstract List<?> getScreens();

    public abstract ScreenConfigurationAccessor getScreenConfigurationAccessor();

    public abstract void registerDragGestureListener(TKScene tKScene, Set<TransferMode> set, TKDragGestureListener tKDragGestureListener);

    public abstract void startDrag(TKScene tKScene, Set<TransferMode> set, TKDragSourceListener tKDragSourceListener, Dragboard dragboard);

    public abstract void enableDrop(TKScene tKScene, TKDropTargetListener tKDropTargetListener);

    public abstract void installInputMethodRequests(TKScene tKScene, InputMethodRequests inputMethodRequests);

    public abstract Object renderToImage(ImageRenderingContext imageRenderingContext);

    public abstract Optional<Boolean> isKeyLocked(KeyCode keyCode);

    public abstract CommonDialogs.FileChooserResult showFileChooser(TKStage tKStage, String str, File file, String str2, FileChooserType fileChooserType, List<FileChooser.ExtensionFilter> list, FileChooser.ExtensionFilter extensionFilter);

    public abstract File showDirectoryChooser(TKStage tKStage, String str, File file);

    public abstract long getMultiClickTime();

    public abstract int getMultiClickMaxX();

    public abstract int getMultiClickMaxY();

    private static String lookupToolkitClass(String name) {
        if ("prism".equalsIgnoreCase(name) || "quantum".equalsIgnoreCase(name)) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        return name;
    }

    public static synchronized void loadMSWindowsLibraries() {
        for (String libName : msLibNames) {
            try {
                NativeLibLoader.loadLibrary(libName);
            } catch (Throwable t2) {
                if (verbose) {
                    System.err.println("Error: failed to load " + libName + ".dll : " + ((Object) t2));
                }
            }
        }
    }

    private static String getDefaultToolkit() {
        if (PlatformUtil.isWindows() || PlatformUtil.isMac() || PlatformUtil.isLinux() || PlatformUtil.isIOS() || PlatformUtil.isAndroid()) {
            return "com.sun.javafx.tk.quantum.QuantumToolkit";
        }
        throw new UnsupportedOperationException(System.getProperty("os.name") + " is not supported");
    }

    public static synchronized Toolkit getToolkit() {
        Class<?> clz;
        if (TOOLKIT != null) {
            return TOOLKIT;
        }
        if (PlatformUtil.isWindows()) {
            loadMSWindowsLibraries();
        }
        AccessController.doPrivileged(() -> {
            VersionInfo.setupSystemProperties();
            return null;
        });
        boolean userSpecifiedToolkit = true;
        String forcedToolkit = null;
        try {
            forcedToolkit = System.getProperty("javafx.toolkit");
        } catch (SecurityException e2) {
        }
        if (forcedToolkit == null) {
            forcedToolkit = tk;
        }
        if (forcedToolkit == null) {
            userSpecifiedToolkit = false;
            forcedToolkit = getDefaultToolkit();
        }
        if (forcedToolkit.indexOf(46) == -1) {
            forcedToolkit = lookupToolkitClass(forcedToolkit);
        }
        boolean printToolkit = verbose || (userSpecifiedToolkit && !forcedToolkit.endsWith("StubToolkit"));
        try {
            clz = Class.forName(forcedToolkit, false, Toolkit.class.getClassLoader());
        } catch (Exception any) {
            TOOLKIT = null;
            any.printStackTrace();
        }
        if (!Toolkit.class.isAssignableFrom(clz)) {
            throw new IllegalArgumentException("Unrecognized FX Toolkit class: " + forcedToolkit);
        }
        TOOLKIT = (Toolkit) clz.newInstance();
        if (TOOLKIT.init()) {
            if (printToolkit) {
                System.err.println("JavaFX: using " + forcedToolkit);
            }
            return TOOLKIT;
        }
        TOOLKIT = null;
        throw new RuntimeException("No toolkit found");
    }

    protected static Thread getFxUserThread() {
        return fxUserThread;
    }

    protected static void setFxUserThread(Thread t2) {
        if (fxUserThread != null) {
            throw new IllegalStateException("Error: FX User Thread already initialized");
        }
        fxUserThread = t2;
    }

    public void checkFxUserThread() {
        if (!isFxUserThread()) {
            throw new IllegalStateException("Not on FX application thread; currentThread = " + Thread.currentThread().getName());
        }
    }

    public boolean isFxUserThread() {
        return Thread.currentThread() == fxUserThread;
    }

    protected Toolkit() {
    }

    private void runPulse(TKPulseListener listener, AccessControlContext acc) {
        if (acc == null) {
            throw new IllegalStateException("Invalid AccessControlContext");
        }
        AccessController.doPrivileged(() -> {
            listener.pulse();
            return null;
        }, acc);
    }

    public void firePulse() {
        Map<TKPulseListener, AccessControlContext> stagePulseList = new WeakHashMap<>();
        Map<TKPulseListener, AccessControlContext> scenePulseList = new WeakHashMap<>();
        Map<TKPulseListener, AccessControlContext> postScenePulseList = new WeakHashMap<>();
        synchronized (this) {
            stagePulseList.putAll(this.stagePulseListeners);
            scenePulseList.putAll(this.scenePulseListeners);
            postScenePulseList.putAll(this.postScenePulseListeners);
        }
        for (Map.Entry<TKPulseListener, AccessControlContext> entry : stagePulseList.entrySet()) {
            runPulse(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<TKPulseListener, AccessControlContext> entry2 : scenePulseList.entrySet()) {
            runPulse(entry2.getKey(), entry2.getValue());
        }
        for (Map.Entry<TKPulseListener, AccessControlContext> entry3 : postScenePulseList.entrySet()) {
            runPulse(entry3.getKey(), entry3.getValue());
        }
        if (this.lastTkPulseListener != null) {
            runPulse(this.lastTkPulseListener, this.lastTkPulseAcc);
        }
    }

    public void addStageTkPulseListener(TKPulseListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            AccessControlContext acc = AccessController.getContext();
            this.stagePulseListeners.put(listener, acc);
        }
    }

    public void removeStageTkPulseListener(TKPulseListener listener) {
        synchronized (this) {
            this.stagePulseListeners.remove(listener);
        }
    }

    public void addSceneTkPulseListener(TKPulseListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            AccessControlContext acc = AccessController.getContext();
            this.scenePulseListeners.put(listener, acc);
        }
    }

    public void removeSceneTkPulseListener(TKPulseListener listener) {
        synchronized (this) {
            this.scenePulseListeners.remove(listener);
        }
    }

    public void addPostSceneTkPulseListener(TKPulseListener listener) {
        if (listener == null) {
            return;
        }
        synchronized (this) {
            AccessControlContext acc = AccessController.getContext();
            this.postScenePulseListeners.put(listener, acc);
        }
    }

    public void removePostSceneTkPulseListener(TKPulseListener listener) {
        synchronized (this) {
            this.postScenePulseListeners.remove(listener);
        }
    }

    public void addTkListener(TKListener listener) {
        if (listener == null) {
            return;
        }
        AccessControlContext acc = AccessController.getContext();
        this.toolkitListeners.put(listener, acc);
    }

    public void removeTkListener(TKListener listener) {
        this.toolkitListeners.remove(listener);
    }

    public void setLastTkPulseListener(TKPulseListener listener) {
        this.lastTkPulseAcc = AccessController.getContext();
        this.lastTkPulseListener = listener;
    }

    public void addShutdownHook(Runnable hook) {
        if (hook == null) {
            return;
        }
        synchronized (this.shutdownHooks) {
            this.shutdownHooks.add(hook);
        }
    }

    public void removeShutdownHook(Runnable hook) {
        synchronized (this.shutdownHooks) {
            this.shutdownHooks.remove(hook);
        }
    }

    protected void notifyShutdownHooks() {
        List<Runnable> hooks;
        synchronized (this.shutdownHooks) {
            hooks = new ArrayList<>(this.shutdownHooks);
            this.shutdownHooks.clear();
        }
        for (Runnable hook : hooks) {
            hook.run();
        }
    }

    public void notifyWindowListeners(List<TKStage> windows) {
        for (Map.Entry<TKListener, AccessControlContext> entry : this.toolkitListeners.entrySet()) {
            TKListener listener = entry.getKey();
            AccessControlContext acc = entry.getValue();
            if (acc == null) {
                throw new IllegalStateException("Invalid AccessControlContext");
            }
            AccessController.doPrivileged(() -> {
                listener.changedTopLevelWindows(windows);
                return null;
            }, acc);
        }
    }

    public void notifyLastNestedLoopExited() {
        for (TKListener listener : this.toolkitListeners.keySet()) {
            listener.exitedLastNestedLoop();
        }
    }

    public InputStream getInputStream(String url, Class base) throws IOException {
        if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("file:") || url.startsWith("jar:")) {
            return new URL(url).openStream();
        }
        return base.getResource(url).openStream();
    }

    public boolean getDefaultImageSmooth() {
        return true;
    }

    public void exit() {
        fxUserThread = null;
    }

    private Object checkSingleColor(List<Stop> stops) {
        if (stops.size() == 2) {
            Color c2 = stops.get(0).getColor();
            if (c2.equals(stops.get(1).getColor())) {
                return getPaintAccessor().getPlatformPaint(c2);
            }
            return null;
        }
        return null;
    }

    private Object getPaint(LinearGradient paint) {
        Object p2 = gradientMap.get(paint);
        if (p2 != null) {
            return p2;
        }
        Object p3 = checkSingleColor(paint.getStops());
        if (p3 == null) {
            p3 = createLinearGradientPaint(paint);
        }
        gradientMap.put(paint, p3);
        return p3;
    }

    private Object getPaint(RadialGradient paint) {
        Object p2 = gradientMap.get(paint);
        if (p2 != null) {
            return p2;
        }
        Object p3 = checkSingleColor(paint.getStops());
        if (p3 == null) {
            p3 = createRadialGradientPaint(paint);
        }
        gradientMap.put(paint, p3);
        return p3;
    }

    public Object getPaint(Paint paint) {
        if (paint instanceof Color) {
            return createColorPaint((Color) paint);
        }
        if (paint instanceof LinearGradient) {
            return getPaint((LinearGradient) paint);
        }
        if (paint instanceof RadialGradient) {
            return getPaint((RadialGradient) paint);
        }
        if (paint instanceof ImagePattern) {
            return createImagePatternPaint((ImagePattern) paint);
        }
        return null;
    }

    protected static final double clampStopOffset(double offset) {
        if (offset > 1.0d) {
            return 1.0d;
        }
        if (offset < 0.0d) {
            return 0.0d;
        }
        return offset;
    }

    public TKClipboard createLocalClipboard() {
        return new LocalClipboard();
    }

    public boolean isSupported(ConditionalFeature feature) {
        return false;
    }

    public boolean isMSAASupported() {
        return false;
    }

    public void stopDrag(Dragboard dragboard) {
    }

    public Color4f toColor4f(Color color) {
        return new Color4f((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }

    public AbstractShadow.ShadowMode toShadowMode(BlurType blurType) {
        switch (blurType) {
            case ONE_PASS_BOX:
                return AbstractShadow.ShadowMode.ONE_PASS_BOX;
            case TWO_PASS_BOX:
                return AbstractShadow.ShadowMode.TWO_PASS_BOX;
            case THREE_PASS_BOX:
                return AbstractShadow.ShadowMode.THREE_PASS_BOX;
            default:
                return AbstractShadow.ShadowMode.GAUSSIAN;
        }
    }

    public KeyCode getPlatformShortcutKey() {
        return PlatformUtil.isMac() ? KeyCode.META : KeyCode.CONTROL;
    }

    public void pauseScenes() {
        this.pauseScenesLatch = new CountDownLatch(1);
        Iterator<Window> i2 = Window.impl_getWindows();
        while (i2.hasNext()) {
            Window w2 = i2.next();
            Scene scene = w2.getScene();
            if (scene != null) {
                removeSceneTkPulseListener(scene.impl_getScenePulseListener());
            }
        }
        getMasterTimer().pause();
        SceneHelper.setPaused(true);
    }

    public void resumeScenes() {
        SceneHelper.setPaused(false);
        getMasterTimer().resume();
        Iterator<Window> i2 = Window.impl_getWindows();
        while (i2.hasNext()) {
            Window w2 = i2.next();
            Scene scene = w2.getScene();
            if (scene != null) {
                addSceneTkPulseListener(scene.impl_getScenePulseListener());
            }
        }
        this.pauseScenesLatch.countDown();
        this.pauseScenesLatch = null;
    }

    public void pauseCurrentThread() {
        CountDownLatch cdl = this.pauseScenesLatch;
        if (cdl == null) {
            return;
        }
        try {
            cdl.await();
        } catch (InterruptedException e2) {
        }
    }

    public Set<HighlightRegion> getHighlightedRegions() {
        if (this.highlightRegions == null) {
            this.highlightRegions = new HashSet();
        }
        return this.highlightRegions;
    }

    public static void setWritableImageAccessor(WritableImageAccessor accessor) {
        writableImageAccessor = accessor;
    }

    public static WritableImageAccessor getWritableImageAccessor() {
        return writableImageAccessor;
    }

    public static void setPaintAccessor(PaintAccessor accessor) {
        paintAccessor = accessor;
    }

    public static PaintAccessor getPaintAccessor() {
        return paintAccessor;
    }

    public static void setImageAccessor(ImageAccessor accessor) {
        imageAccessor = accessor;
    }

    public static ImageAccessor getImageAccessor() {
        return imageAccessor;
    }

    public String getThemeName() {
        return null;
    }
}
