package com.sun.javafx.tk.quantum;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import com.sun.glass.ui.ClipboardAssistance;
import com.sun.glass.ui.CommonDialogs;
import com.sun.glass.ui.EventLoop;
import com.sun.glass.ui.Screen;
import com.sun.glass.ui.Timer;
import com.sun.glass.ui.View;
import com.sun.javafx.PlatformUtil;
import com.sun.javafx.application.PlatformImpl;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.font.PrismFontLoader;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.PathIterator;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.logging.PulseLogger;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.async.AbstractRemoteResource;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.text.PrismTextLayoutFactory;
import com.sun.javafx.tk.AppletWindow;
import com.sun.javafx.tk.CompletionListener;
import com.sun.javafx.tk.FileChooserType;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.ImageLoader;
import com.sun.javafx.tk.PlatformImage;
import com.sun.javafx.tk.RenderJob;
import com.sun.javafx.tk.ScreenConfigurationAccessor;
import com.sun.javafx.tk.TKClipboard;
import com.sun.javafx.tk.TKDragGestureListener;
import com.sun.javafx.tk.TKDragSourceListener;
import com.sun.javafx.tk.TKDropTargetListener;
import com.sun.javafx.tk.TKScene;
import com.sun.javafx.tk.TKScreenConfigurationListener;
import com.sun.javafx.tk.TKStage;
import com.sun.javafx.tk.TKSystemMenu;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.tk.quantum.PathIteratorHelper;
import com.sun.javafx.tk.quantum.PrismImageLoader2;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.prism.BasicStroke;
import com.sun.prism.Graphics;
import com.sun.prism.GraphicsPipeline;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.RTTexture;
import com.sun.prism.ResourceFactory;
import com.sun.prism.ResourceFactoryListener;
import com.sun.prism.Texture;
import com.sun.prism.impl.Disposer;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.Stop;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.impl.prism.PrFilterContext;
import com.sun.scenario.effect.impl.prism.PrImage;
import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import javafx.application.ConditionalFeature;
import javafx.geometry.Dimension2D;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/QuantumToolkit.class */
public final class QuantumToolkit extends Toolkit {
    public static final boolean verbose;
    public static final boolean pulseDebug;
    private static final boolean multithreaded;
    private static boolean debug;
    private static Integer pulseHZ;
    static final boolean liveResize;
    static final boolean drawInPaint;
    private static boolean singleThreaded;
    private static boolean noRenderJobs;
    private float _maxPixelScale;
    private Runnable pulseRunnable;
    private Runnable userRunnable;
    private Runnable timerRunnable;
    private PaintCollector collector;
    private QuantumRenderer renderer;
    private GraphicsPipeline pipeline;
    private ClassLoader ccl;
    private static ScreenConfigurationAccessor screenAccessor;
    private DelayedRunnable animationRunnable;
    static BasicStroke tmpStroke;
    private QuantumClipboard clipboard;
    static final /* synthetic */ boolean $assertionsDisabled;
    private AtomicBoolean toolkitRunning = new AtomicBoolean(false);
    private AtomicBoolean animationRunning = new AtomicBoolean(false);
    private AtomicBoolean nextPulseRequested = new AtomicBoolean(false);
    private AtomicBoolean pulseRunning = new AtomicBoolean(false);
    private int inPulse = 0;
    private CountDownLatch launchLatch = new CountDownLatch(1);
    final int PULSE_INTERVAL = (int) (TimeUnit.SECONDS.toMillis(1) / getRefreshRate());
    final int FULLSPEED_INTERVAL = 1;
    boolean nativeSystemVsync = false;
    private Timer pulseTimer = null;
    private Thread shutdownHook = null;
    private HashMap<Object, EventLoop> eventLoopMap = null;
    private final PerformanceTracker perfTracker = new PerformanceTrackerImpl();
    private Map<Object, Object> contextMap = Collections.synchronizedMap(new HashMap());
    private GlassSystemMenu systemMenu = new GlassSystemMenu();

    static {
        $assertionsDisabled = !QuantumToolkit.class.desiredAssertionStatus();
        verbose = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("quantum.verbose"));
        })).booleanValue();
        pulseDebug = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("quantum.pulse"));
        })).booleanValue();
        multithreaded = ((Boolean) AccessController.doPrivileged(() -> {
            String value = System.getProperty("quantum.multithreaded");
            if (value == null) {
                return true;
            }
            boolean result = Boolean.parseBoolean(value);
            if (verbose) {
                System.out.println(result ? "Multi-Threading Enabled" : "Multi-Threading Disabled");
            }
            return Boolean.valueOf(result);
        })).booleanValue();
        debug = ((Boolean) AccessController.doPrivileged(() -> {
            return Boolean.valueOf(Boolean.getBoolean("quantum.debug"));
        })).booleanValue();
        pulseHZ = (Integer) AccessController.doPrivileged(() -> {
            return Integer.getInteger("javafx.animation.pulse");
        });
        liveResize = ((Boolean) AccessController.doPrivileged(() -> {
            boolean isSWT = "swt".equals(System.getProperty("glass.platform"));
            String result = ((PlatformUtil.isMac() || PlatformUtil.isWindows()) && !isSWT) ? "true" : "false";
            return Boolean.valueOf("true".equals(System.getProperty("javafx.live.resize", result)));
        })).booleanValue();
        drawInPaint = ((Boolean) AccessController.doPrivileged(() -> {
            boolean isSWT = "swt".equals(System.getProperty("glass.platform"));
            String result = (PlatformUtil.isMac() && isSWT) ? "true" : "false";
            return Boolean.valueOf("true".equals(System.getProperty("javafx.draw.in.paint", result)));
        })).booleanValue();
        singleThreaded = ((Boolean) AccessController.doPrivileged(() -> {
            Boolean result = Boolean.valueOf(Boolean.getBoolean("quantum.singlethreaded"));
            if (result.booleanValue()) {
                System.out.println("Warning: Single GUI Threadiong is enabled, FPS should be slower");
            }
            return result;
        })).booleanValue();
        noRenderJobs = ((Boolean) AccessController.doPrivileged(() -> {
            Boolean result = Boolean.valueOf(Boolean.getBoolean("quantum.norenderjobs"));
            if (result.booleanValue()) {
                System.out.println("Warning: Quantum will not submit render jobs, nothing should draw");
            }
            return result;
        })).booleanValue();
        screenAccessor = new ScreenConfigurationAccessor() { // from class: com.sun.javafx.tk.quantum.QuantumToolkit.3
            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getMinX(Object obj) {
                return ((Screen) obj).getX();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getMinY(Object obj) {
                return ((Screen) obj).getY();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getWidth(Object obj) {
                return ((Screen) obj).getWidth();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getHeight(Object obj) {
                return ((Screen) obj).getHeight();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getVisualMinX(Object obj) {
                return ((Screen) obj).getVisibleX();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getVisualMinY(Object obj) {
                return ((Screen) obj).getVisibleY();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getVisualWidth(Object obj) {
                return ((Screen) obj).getVisibleWidth();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public int getVisualHeight(Object obj) {
                return ((Screen) obj).getVisibleHeight();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public float getDPI(Object obj) {
                return ((Screen) obj).getResolutionX();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public float getUIScale(Object obj) {
                return ((Screen) obj).getUIScale();
            }

            @Override // com.sun.javafx.tk.ScreenConfigurationAccessor
            public float getRenderScale(Object obj) {
                return ((Screen) obj).getRenderScale();
            }
        };
        tmpStroke = new BasicStroke();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean init() {
        this.renderer = QuantumRenderer.getInstance();
        this.collector = PaintCollector.createInstance(this);
        this.pipeline = GraphicsPipeline.getPipeline();
        this.shutdownHook = new Thread("Glass/Prism Shutdown Hook") { // from class: com.sun.javafx.tk.quantum.QuantumToolkit.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                QuantumToolkit.this.dispose();
            }
        };
        AccessController.doPrivileged(() -> {
            Runtime.getRuntime().addShutdownHook(this.shutdownHook);
            return null;
        });
        return true;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void startup(Runnable userStartupRunnable) {
        this.ccl = Thread.currentThread().getContextClassLoader();
        try {
            this.userRunnable = userStartupRunnable;
            Application.run(() -> {
                runToolkit();
            });
            try {
                this.launchLatch.await();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        } catch (RuntimeException ex) {
            if (verbose) {
                ex.printStackTrace();
            }
            throw ex;
        } catch (Throwable t2) {
            if (verbose) {
                t2.printStackTrace();
            }
            throw new RuntimeException(t2);
        }
    }

    private void assertToolkitRunning() {
    }

    boolean shouldWaitForRenderingToComplete() {
        return !multithreaded;
    }

    private static void initSceneGraph() {
        javafx.stage.Screen.getPrimary();
    }

    void runToolkit() {
        Thread user = Thread.currentThread();
        if (!this.toolkitRunning.getAndSet(true)) {
            user.setName("JavaFX Application Thread");
            user.setContextClassLoader(this.ccl);
            setFxUserThread(user);
            assignScreensAdapters();
            this.renderer.createResourceFactory();
            this.pulseRunnable = () -> {
                pulseFromQueue();
            };
            this.timerRunnable = () -> {
                try {
                    postPulse();
                } catch (Throwable th) {
                    th.printStackTrace(System.err);
                }
            };
            this.pulseTimer = Application.GetApplication().createTimer(this.timerRunnable);
            Application.GetApplication().setEventHandler(new Application.EventHandler() { // from class: com.sun.javafx.tk.quantum.QuantumToolkit.2
                @Override // com.sun.glass.ui.Application.EventHandler
                public void handleQuitAction(Application app, long time) {
                    GlassStage.requestClosingAllWindows();
                }

                @Override // com.sun.glass.ui.Application.EventHandler
                public boolean handleThemeChanged(String themeName) {
                    return PlatformImpl.setAccessibilityTheme(themeName);
                }
            });
        }
        initSceneGraph();
        this.launchLatch.countDown();
        try {
            try {
                Application.invokeAndWait(this.userRunnable);
                if (getMasterTimer().isFullspeed()) {
                    this.pulseTimer.start(1);
                } else {
                    this.nativeSystemVsync = Screen.getVideoRefreshPeriod() != 0.0d;
                    if (this.nativeSystemVsync) {
                        this.pulseTimer.start();
                    } else {
                        this.pulseTimer.start(this.PULSE_INTERVAL);
                    }
                }
                if (PrismSettings.verbose) {
                    System.err.println(" vsync: " + PrismSettings.isVsyncEnabled + " vpipe: " + this.pipeline.isVsyncSupported());
                }
                PerformanceTracker.logEvent("Toolkit.startup - finished");
            } catch (Throwable th) {
                th.printStackTrace(System.err);
                if (PrismSettings.verbose) {
                    System.err.println(" vsync: " + PrismSettings.isVsyncEnabled + " vpipe: " + this.pipeline.isVsyncSupported());
                }
                PerformanceTracker.logEvent("Toolkit.startup - finished");
            }
        } catch (Throwable th2) {
            if (PrismSettings.verbose) {
                System.err.println(" vsync: " + PrismSettings.isVsyncEnabled + " vpipe: " + this.pipeline.isVsyncSupported());
            }
            PerformanceTracker.logEvent("Toolkit.startup - finished");
            throw th2;
        }
    }

    public static <T> T runWithoutRenderLock(Supplier<T> supplier) {
        boolean locked = ViewPainter.renderLock.isHeldByCurrentThread();
        if (locked) {
            try {
                ViewPainter.renderLock.unlock();
            } catch (Throwable th) {
                if (locked) {
                    ViewPainter.renderLock.lock();
                }
                throw th;
            }
        }
        T t2 = supplier.get();
        if (locked) {
            ViewPainter.renderLock.lock();
        }
        return t2;
    }

    public static <T> T runWithRenderLock(Supplier<T> supplier) {
        ViewPainter.renderLock.lock();
        try {
            T t2 = supplier.get();
            ViewPainter.renderLock.unlock();
            return t2;
        } catch (Throwable th) {
            ViewPainter.renderLock.unlock();
            throw th;
        }
    }

    boolean hasNativeSystemVsync() {
        return this.nativeSystemVsync;
    }

    boolean isVsyncEnabled() {
        return PrismSettings.isVsyncEnabled && this.pipeline.isVsyncSupported();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void checkFxUserThread() {
        super.checkFxUserThread();
        this.renderer.checkRendererIdle();
    }

    protected static Thread getFxUserThread() {
        return Toolkit.getFxUserThread();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Future addRenderJob(RenderJob r2) {
        if (noRenderJobs) {
            CompletionListener listener = r2.getCompletionListener();
            if (r2 instanceof PaintRenderJob) {
                ((PaintRenderJob) r2).getScene().setPainting(false);
            }
            if (listener != null) {
                try {
                    listener.done(r2);
                    return null;
                } catch (Throwable th) {
                    th.printStackTrace();
                    return null;
                }
            }
            return null;
        }
        if (singleThreaded) {
            r2.run();
            return null;
        }
        return this.renderer.submitRenderJob(r2);
    }

    void postPulse() {
        if (this.toolkitRunning.get() && ((this.animationRunning.get() || this.nextPulseRequested.get() || this.collector.hasDirty()) && !setPulseRunning())) {
            Application.invokeLater(this.pulseRunnable);
            if (debug) {
                System.err.println("QT.postPulse@(" + System.nanoTime() + "): " + pulseString());
                return;
            }
            return;
        }
        if (debug) {
            System.err.println("QT.postPulse#(" + System.nanoTime() + ") DROP: " + pulseString());
        }
    }

    private String pulseString() {
        return (this.toolkitRunning.get() ? "T" : "t") + (this.animationRunning.get() ? "A" : "a") + (this.pulseRunning.get() ? Constants._TAG_P : "p") + (this.nextPulseRequested.get() ? "N" : PdfOps.n_TOKEN) + (this.collector.hasDirty() ? PdfOps.D_TOKEN : PdfOps.d_TOKEN);
    }

    private boolean setPulseRunning() {
        return this.pulseRunning.getAndSet(true);
    }

    private void endPulseRunning() {
        this.pulseRunning.set(false);
        if (debug) {
            System.err.println("QT.endPulse: " + System.nanoTime());
        }
    }

    void pulseFromQueue() {
        try {
            pulse();
        } finally {
            endPulseRunning();
        }
    }

    protected void pulse() {
        pulse(true);
    }

    void pulse(boolean collect) {
        boolean z2;
        try {
            this.inPulse++;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseStart();
            }
            if (!this.toolkitRunning.get()) {
                if (z2) {
                    return;
                } else {
                    return;
                }
            }
            this.nextPulseRequested.set(false);
            if (this.animationRunnable != null) {
                this.animationRunning.set(true);
                this.animationRunnable.run();
            } else {
                this.animationRunning.set(false);
            }
            firePulse();
            if (collect) {
                this.collector.renderAll();
            }
            this.inPulse--;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseEnd();
            }
        } finally {
            this.inPulse--;
            if (PulseLogger.PULSE_LOGGING_ENABLED) {
                PulseLogger.pulseEnd();
            }
        }
    }

    void vsyncHint() {
        if (isVsyncEnabled()) {
            if (debug) {
                System.err.println("QT.vsyncHint: postPulse: " + System.nanoTime());
            }
            postPulse();
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public AppletWindow createAppletWindow(long parent, String serverName) {
        GlassAppletWindow parentWindow = new GlassAppletWindow(parent, serverName);
        WindowStage.setAppletWindow(parentWindow);
        return parentWindow;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void closeAppletWindow() {
        GlassAppletWindow gaw = WindowStage.getAppletWindow();
        if (null != gaw) {
            gaw.dispose();
            WindowStage.setAppletWindow(null);
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKStage createTKStage(Window peerWindow, boolean securityDialog, StageStyle stageStyle, boolean primary, Modality modality, TKStage owner, boolean rtl, AccessControlContext acc) {
        assertToolkitRunning();
        WindowStage stage = new WindowStage(peerWindow, securityDialog, stageStyle, modality, owner);
        stage.setSecurityContext(acc);
        if (primary) {
            stage.setIsPrimary();
        }
        stage.setRTL(rtl);
        stage.init(this.systemMenu);
        return stage;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean canStartNestedEventLoop() {
        return this.inPulse == 0;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object enterNestedEventLoop(Object key) {
        checkFxUserThread();
        if (key == null) {
            throw new NullPointerException();
        }
        if (!canStartNestedEventLoop()) {
            throw new IllegalStateException("Cannot enter nested loop during animation or layout processing");
        }
        if (this.eventLoopMap == null) {
            this.eventLoopMap = new HashMap<>();
        }
        if (this.eventLoopMap.containsKey(key)) {
            throw new IllegalArgumentException("Key already associated with a running event loop: " + key);
        }
        EventLoop eventLoop = Application.GetApplication().createEventLoop();
        this.eventLoopMap.put(key, eventLoop);
        Object ret = eventLoop.enter();
        if (!isNestedLoopRunning()) {
            notifyLastNestedLoopExited();
        }
        return ret;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void exitNestedEventLoop(Object key, Object rval) {
        checkFxUserThread();
        if (key == null) {
            throw new NullPointerException();
        }
        if (this.eventLoopMap == null || !this.eventLoopMap.containsKey(key)) {
            throw new IllegalArgumentException("Key not associated with a running event loop: " + key);
        }
        EventLoop eventLoop = this.eventLoopMap.get(key);
        this.eventLoopMap.remove(key);
        eventLoop.leave(rval);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKStage createTKPopupStage(Window peerWindow, StageStyle popupStyle, TKStage owner, AccessControlContext acc) {
        assertToolkitRunning();
        boolean securityDialog = owner instanceof WindowStage ? ((WindowStage) owner).isSecurityDialog() : false;
        WindowStage stage = new WindowStage(peerWindow, securityDialog, popupStyle, null, owner);
        stage.setSecurityContext(acc);
        stage.setIsPopup();
        stage.init(this.systemMenu);
        return stage;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKStage createTKEmbeddedStage(HostInterface host, AccessControlContext acc) {
        assertToolkitRunning();
        EmbeddedStage stage = new EmbeddedStage(host);
        stage.setSecurityContext(acc);
        return stage;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ScreenConfigurationAccessor setScreenConfigurationListener(final TKScreenConfigurationListener listener) {
        Screen.setEventHandler(new Screen.EventHandler() { // from class: com.sun.javafx.tk.quantum.QuantumToolkit.4
            @Override // com.sun.glass.ui.Screen.EventHandler
            public void handleSettingsChanged() {
                QuantumToolkit.notifyScreenListener(listener);
            }
        });
        return screenAccessor;
    }

    private static void assignScreensAdapters() {
        GraphicsPipeline pipeline = GraphicsPipeline.getPipeline();
        for (Screen screen : Screen.getScreens()) {
            screen.setAdapterOrdinal(pipeline.getAdapterOrdinal(screen));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void notifyScreenListener(TKScreenConfigurationListener listener) {
        assignScreensAdapters();
        listener.screenConfigurationChanged();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object getPrimaryScreen() {
        return Screen.getMainScreen();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public List<?> getScreens() {
        return Screen.getScreens();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
        return screenAccessor;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PerformanceTracker getPerformanceTracker() {
        return this.perfTracker;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PerformanceTracker createPerformanceTracker() {
        return new PerformanceTrackerImpl();
    }

    public float getMaxRenderScale() {
        if (this._maxPixelScale == 0.0f) {
            for (Object o2 : getScreens()) {
                this._maxPixelScale = Math.max(this._maxPixelScale, ((Screen) o2).getRenderScale());
            }
        }
        return this._maxPixelScale;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ImageLoader loadImage(String url, int width, int height, boolean preserveRatio, boolean smooth) {
        return new PrismImageLoader2(url, width, height, preserveRatio, getMaxRenderScale(), smooth);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ImageLoader loadImage(InputStream stream, int width, int height, boolean preserveRatio, boolean smooth) {
        return new PrismImageLoader2(stream, width, height, preserveRatio, smooth);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public AbstractRemoteResource<? extends ImageLoader> loadImageAsync(AsyncOperationListener listener, String url, int width, int height, boolean preserveRatio, boolean smooth) {
        return new PrismImageLoader2.AsyncImageLoader(listener, url, width, height, preserveRatio, smooth);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void defer(Runnable runnable) {
        if (this.toolkitRunning.get()) {
            Application.invokeLater(runnable);
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void exit() {
        checkFxUserThread();
        this.pulseTimer.stop();
        PaintCollector.getInstance().waitForRenderingToComplete();
        notifyShutdownHooks();
        runWithRenderLock(() -> {
            Application app = Application.GetApplication();
            app.terminate();
            return null;
        });
        dispose();
        super.exit();
    }

    public void dispose() {
        if (this.toolkitRunning.compareAndSet(true, false)) {
            this.pulseTimer.stop();
            this.renderer.stopRenderer();
            try {
                AccessController.doPrivileged(() -> {
                    Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
                    return null;
                });
            } catch (IllegalStateException e2) {
            }
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isForwardTraversalKey(KeyEvent e2) {
        return e2.getCode() == KeyCode.TAB && e2.getEventType() == KeyEvent.KEY_PRESSED && !e2.isShiftDown();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isBackwardTraversalKey(KeyEvent e2) {
        return e2.getCode() == KeyCode.TAB && e2.getEventType() == KeyEvent.KEY_PRESSED && e2.isShiftDown();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Map<Object, Object> getContextMap() {
        return this.contextMap;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getRefreshRate() {
        if (pulseHZ == null) {
            return 60;
        }
        return pulseHZ.intValue();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void setAnimationRunnable(DelayedRunnable animationRunnable) {
        if (animationRunnable != null) {
            this.animationRunning.set(true);
        }
        this.animationRunnable = animationRunnable;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void requestNextPulse() {
        this.nextPulseRequested.set(true);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void waitFor(Toolkit.Task t2) {
        if (t2.isFinished()) {
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createColorPaint(Color color) {
        return new com.sun.prism.paint.Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }

    private com.sun.prism.paint.Color toPrismColor(Color color) {
        return (com.sun.prism.paint.Color) Toolkit.getPaintAccessor().getPlatformPaint(color);
    }

    private List<Stop> convertStops(List<javafx.scene.paint.Stop> paintStops) {
        List<Stop> stops = new ArrayList<>(paintStops.size());
        for (javafx.scene.paint.Stop s2 : paintStops) {
            stops.add(new Stop(toPrismColor(s2.getColor()), (float) s2.getOffset()));
        }
        return stops;
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createLinearGradientPaint(LinearGradient paint) {
        int cmi = 2;
        CycleMethod cycleMethod = paint.getCycleMethod();
        if (cycleMethod == CycleMethod.NO_CYCLE) {
            cmi = 0;
        } else if (cycleMethod == CycleMethod.REFLECT) {
            cmi = 1;
        }
        List<Stop> stops = convertStops(paint.getStops());
        return new com.sun.prism.paint.LinearGradient((float) paint.getStartX(), (float) paint.getStartY(), (float) paint.getEndX(), (float) paint.getEndY(), null, paint.isProportional(), cmi, stops);
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createRadialGradientPaint(RadialGradient paint) {
        int cmi;
        float cx = (float) paint.getCenterX();
        float cy = (float) paint.getCenterY();
        float fa = (float) paint.getFocusAngle();
        float fd = (float) paint.getFocusDistance();
        if (paint.getCycleMethod() == CycleMethod.NO_CYCLE) {
            cmi = 0;
        } else if (paint.getCycleMethod() == CycleMethod.REFLECT) {
            cmi = 1;
        } else {
            cmi = 2;
        }
        List<Stop> stops = convertStops(paint.getStops());
        return new com.sun.prism.paint.RadialGradient(cx, cy, fa, fd, (float) paint.getRadius(), null, paint.isProportional(), cmi, stops);
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createImagePatternPaint(ImagePattern paint) {
        if (paint.getImage() == null) {
            return com.sun.prism.paint.Color.TRANSPARENT;
        }
        return new com.sun.prism.paint.ImagePattern((Image) paint.getImage().impl_getPlatformImage(), (float) paint.getX(), (float) paint.getY(), (float) paint.getWidth(), (float) paint.getHeight(), paint.isProportional(), Toolkit.getPaintAccessor().isMutable(paint));
    }

    private void initStroke(StrokeType pgtype, double strokewidth, StrokeLineCap pgcap, StrokeLineJoin pgjoin, float miterLimit, float[] dashArray, float dashOffset) {
        int type;
        int cap;
        int join;
        if (pgtype == StrokeType.CENTERED) {
            type = 0;
        } else if (pgtype == StrokeType.INSIDE) {
            type = 1;
        } else {
            type = 2;
        }
        if (pgcap == StrokeLineCap.BUTT) {
            cap = 0;
        } else if (pgcap == StrokeLineCap.SQUARE) {
            cap = 2;
        } else {
            cap = 1;
        }
        if (pgjoin == StrokeLineJoin.BEVEL) {
            join = 2;
        } else if (pgjoin == StrokeLineJoin.MITER) {
            join = 0;
        } else {
            join = 1;
        }
        tmpStroke.set(type, (float) strokewidth, cap, join, miterLimit);
        if (dashArray != null && dashArray.length > 0) {
            tmpStroke.set(dashArray, dashOffset);
        } else {
            tmpStroke.set((float[]) null, 0.0f);
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void accumulateStrokeBounds(Shape shape, float[] bbox, StrokeType pgtype, double strokewidth, StrokeLineCap pgcap, StrokeLineJoin pgjoin, float miterLimit, BaseTransform tx) {
        initStroke(pgtype, strokewidth, pgcap, pgjoin, miterLimit, null, 0.0f);
        if (tx.isTranslateOrIdentity()) {
            tmpStroke.accumulateShapeBounds(bbox, shape, tx);
        } else {
            Shape.accumulate(bbox, tmpStroke.createStrokedShape(shape), tx);
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean strokeContains(Shape shape, double x2, double y2, StrokeType pgtype, double strokewidth, StrokeLineCap pgcap, StrokeLineJoin pgjoin, float miterLimit) {
        initStroke(pgtype, strokewidth, pgcap, pgjoin, miterLimit, null, 0.0f);
        return tmpStroke.createStrokedShape(shape).contains((float) x2, (float) y2);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Shape createStrokedShape(Shape shape, StrokeType pgtype, double strokewidth, StrokeLineCap pgcap, StrokeLineJoin pgjoin, float miterLimit, float[] dashArray, float dashOffset) {
        initStroke(pgtype, strokewidth, pgcap, pgjoin, miterLimit, dashArray, dashOffset);
        return tmpStroke.createStrokedShape(shape);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Dimension2D getBestCursorSize(int preferredWidth, int preferredHeight) {
        return CursorUtils.getBestCursorSize(preferredWidth, preferredHeight);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getMaximumCursorColors() {
        return 2;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getKeyCodeForChar(String character) {
        if (character.length() == 1) {
            return com.sun.glass.events.KeyEvent.getKeyCodeForChar(character.charAt(0));
        }
        return 0;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PathElement[] convertShapeToFXPath(Object shape) {
        PathElement closePath;
        if (shape == null) {
            return new PathElement[0];
        }
        List<PathElement> elements = new ArrayList<>();
        Shape geomShape = (Shape) shape;
        PathIterator itr = geomShape.getPathIterator(null);
        PathIteratorHelper helper = new PathIteratorHelper(itr);
        PathIteratorHelper.Struct struct = new PathIteratorHelper.Struct();
        while (!helper.isDone()) {
            boolean z2 = helper.getWindingRule() == 0;
            int type = helper.currentSegment(struct);
            if (type == 0) {
                closePath = new MoveTo(struct.f0, struct.f1);
            } else if (type == 1) {
                closePath = new LineTo(struct.f0, struct.f1);
            } else if (type == 2) {
                closePath = new QuadCurveTo(struct.f0, struct.f1, struct.f2, struct.f3);
            } else if (type == 3) {
                closePath = new CubicCurveTo(struct.f0, struct.f1, struct.f2, struct.f3, struct.f4, struct.f5);
            } else if (type == 4) {
                closePath = new ClosePath();
            } else {
                throw new IllegalStateException("Invalid element type: " + type);
            }
            PathElement el = closePath;
            helper.next();
            elements.add(el);
        }
        return (PathElement[]) elements.toArray(new PathElement[elements.size()]);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public HitInfo convertHitInfoToFX(Object hit) {
        Integer textHitPos = (Integer) hit;
        HitInfo hitInfo = new HitInfo();
        hitInfo.setCharIndex(textHitPos.intValue());
        hitInfo.setLeading(true);
        return hitInfo;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Filterable toFilterable(javafx.scene.image.Image img) {
        return PrImage.create((Image) img.impl_getPlatformImage());
    }

    @Override // com.sun.javafx.tk.Toolkit
    public FilterContext getFilterContext(Object config) {
        if (config == null || !(config instanceof Screen)) {
            return PrFilterContext.getDefaultInstance();
        }
        Screen screen = (Screen) config;
        return PrFilterContext.getInstance(screen);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public AbstractMasterTimer getMasterTimer() {
        return MasterTimer.getInstance();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public FontLoader getFontLoader() {
        return PrismFontLoader.getInstance();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TextLayoutFactory getTextLayoutFactory() {
        return PrismTextLayoutFactory.getFactory();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object createSVGPathObject(SVGPath svgpath) {
        int windingRule = svgpath.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
        Path2D path = new Path2D(windingRule);
        path.appendSVGPath(svgpath.getContent());
        return path;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Path2D createSVGPath2D(SVGPath svgpath) {
        int windingRule = svgpath.getFillRule() == FillRule.NON_ZERO ? 1 : 0;
        Path2D path = new Path2D(windingRule);
        path.appendSVGPath(svgpath.getContent());
        return path;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean imageContains(Object image, float x2, float y2) {
        if (image == null) {
            return false;
        }
        Image pImage = (Image) image;
        int intX = ((int) x2) + pImage.getMinX();
        int intY = ((int) y2) + pImage.getMinY();
        if (pImage.isOpaque()) {
            return true;
        }
        if (pImage.getPixelFormat() == PixelFormat.INT_ARGB_PRE) {
            IntBuffer ib = (IntBuffer) pImage.getPixelBuffer();
            int index = intX + (intY * pImage.getRowLength());
            return index < ib.limit() && (ib.get(index) & (-16777216)) != 0;
        }
        if (pImage.getPixelFormat() == PixelFormat.BYTE_BGRA_PRE) {
            ByteBuffer bb2 = (ByteBuffer) pImage.getPixelBuffer();
            int index2 = (intX * pImage.getBytesPerPixelUnit()) + (intY * pImage.getScanlineStride()) + 3;
            return index2 < bb2.limit() && (bb2.get(index2) & 255) != 0;
        }
        if (pImage.getPixelFormat() == PixelFormat.BYTE_ALPHA) {
            ByteBuffer bb3 = (ByteBuffer) pImage.getPixelBuffer();
            int index3 = (intX * pImage.getBytesPerPixelUnit()) + (intY * pImage.getScanlineStride());
            return index3 < bb3.limit() && (bb3.get(index3) & 255) != 0;
        }
        return true;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isNestedLoopRunning() {
        return Application.isNestedLoopRunning();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isSupported(ConditionalFeature feature) {
        switch (feature) {
            case SCENE3D:
                return GraphicsPipeline.getPipeline().is3DSupported();
            case EFFECT:
                return GraphicsPipeline.getPipeline().isEffectSupported();
            case SHAPE_CLIP:
                return true;
            case INPUT_METHOD:
                return Application.GetApplication().supportsInputMethods();
            case TRANSPARENT_WINDOW:
                return Application.GetApplication().supportsTransparentWindows();
            case UNIFIED_WINDOW:
                return Application.GetApplication().supportsUnifiedWindows();
            case TWO_LEVEL_FOCUS:
                return Application.GetApplication().hasTwoLevelFocus();
            case VIRTUAL_KEYBOARD:
                return Application.GetApplication().hasVirtualKeyboard();
            case INPUT_TOUCH:
                return Application.GetApplication().hasTouch();
            case INPUT_MULTITOUCH:
                return Application.GetApplication().hasMultiTouch();
            case INPUT_POINTER:
                return Application.GetApplication().hasPointer();
            default:
                return false;
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isMSAASupported() {
        return GraphicsPipeline.getPipeline().isMSAASupported();
    }

    private int toGlassKeyCode(KeyCode keyCode) {
        switch (keyCode) {
            case CAPS:
                return 20;
            case NUM_LOCK:
                return 144;
            default:
                return 0;
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Optional<Boolean> isKeyLocked(KeyCode keyCode) {
        return Application.GetApplication().isKeyLocked(toGlassKeyCode(keyCode));
    }

    static TransferMode clipboardActionToTransferMode(int action) {
        switch (action) {
            case 0:
                return null;
            case 1:
            case 1073741825:
                return TransferMode.COPY;
            case 2:
            case 1073741826:
                return TransferMode.MOVE;
            case 1073741824:
                return TransferMode.LINK;
            case Clipboard.ACTION_ANY /* 1342177279 */:
                return TransferMode.COPY;
            default:
                return null;
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKClipboard getSystemClipboard() {
        if (this.clipboard == null) {
            this.clipboard = QuantumClipboard.getClipboardInstance(new ClipboardAssistance(Clipboard.SYSTEM));
        }
        return this.clipboard;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKSystemMenu getSystemMenu() {
        return this.systemMenu;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKClipboard getNamedClipboard(String name) {
        return null;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void startDrag(TKScene scene, Set<TransferMode> tm, TKDragSourceListener l2, Dragboard dragboard) {
        if (dragboard == null) {
            throw new IllegalArgumentException("dragboard should not be null");
        }
        GlassScene view = (GlassScene) scene;
        view.setTKDragSourceListener(l2);
        QuantumClipboard gc = (QuantumClipboard) dragboard.impl_getPeer();
        gc.setSupportedTransferMode(tm);
        gc.flush();
        gc.close();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void enableDrop(TKScene s2, TKDropTargetListener l2) {
        if (!$assertionsDisabled && !(s2 instanceof GlassScene)) {
            throw new AssertionError();
        }
        GlassScene view = (GlassScene) s2;
        view.setTKDropTargetListener(l2);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void registerDragGestureListener(TKScene s2, Set<TransferMode> tm, TKDragGestureListener l2) {
        if (!$assertionsDisabled && !(s2 instanceof GlassScene)) {
            throw new AssertionError();
        }
        GlassScene view = (GlassScene) s2;
        view.setTKDragGestureListener(l2);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void installInputMethodRequests(TKScene scene, InputMethodRequests requests) {
        if (!$assertionsDisabled && !(scene instanceof GlassScene)) {
            throw new AssertionError();
        }
        GlassScene view = (GlassScene) scene;
        view.setInputMethodRequests(requests);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/tk/quantum/QuantumToolkit$QuantumImage.class */
    static class QuantumImage implements ImageLoader, ResourceFactoryListener {
        private RTTexture rt;
        private Image image;
        private ResourceFactory rf;

        QuantumImage(Image image) {
            this.image = image;
        }

        RTTexture getRT(int w2, int h2, ResourceFactory rfNew) {
            boolean rttOk = this.rt != null && this.rf == rfNew && this.rt.getContentWidth() == w2 && this.rt.getContentHeight() == h2;
            if (rttOk) {
                this.rt.lock();
                if (this.rt.isSurfaceLost()) {
                    rttOk = false;
                }
            }
            if (!rttOk) {
                if (this.rt != null) {
                    this.rt.dispose();
                }
                if (this.rf != null) {
                    this.rf.removeFactoryListener(this);
                    this.rf = null;
                }
                this.rt = rfNew.createRTTexture(w2, h2, Texture.WrapMode.CLAMP_TO_ZERO);
                if (this.rt != null) {
                    this.rf = rfNew;
                    this.rf.addFactoryListener(this);
                }
            }
            return this.rt;
        }

        void dispose() {
            if (this.rt != null) {
                this.rt.dispose();
                this.rt = null;
            }
        }

        void setImage(Image img) {
            this.image = img;
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public Exception getException() {
            if (this.image == null) {
                return new IllegalStateException("Unitialized image");
            }
            return null;
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public int getFrameCount() {
            return 1;
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public PlatformImage getFrame(int index) {
            return this.image;
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public int getFrameDelay(int index) {
            return 0;
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public int getLoopCount() {
            return 0;
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public int getWidth() {
            return this.image.getWidth();
        }

        @Override // com.sun.javafx.tk.ImageLoader
        public int getHeight() {
            return this.image.getHeight();
        }

        @Override // com.sun.prism.ResourceFactoryListener
        public void factoryReset() {
            dispose();
        }

        @Override // com.sun.prism.ResourceFactoryListener
        public void factoryReleased() {
            dispose();
            if (this.rf != null) {
                this.rf.removeFactoryListener(this);
                this.rf = null;
            }
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ImageLoader loadPlatformImage(Object platformImage) {
        if (platformImage instanceof QuantumImage) {
            return (QuantumImage) platformImage;
        }
        if (platformImage instanceof Image) {
            return new QuantumImage((Image) platformImage);
        }
        throw new UnsupportedOperationException("unsupported class for loadPlatformImage");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PlatformImage createPlatformImage(int w2, int h2) {
        ByteBuffer bytebuf = ByteBuffer.allocate(w2 * h2 * 4);
        return Image.fromByteBgraPreData(bytebuf, w2, h2);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object renderToImage(final Toolkit.ImageRenderingContext p2) {
        Object saveImage = p2.platformImage;
        final Paint currentPaint = p2.platformPaint instanceof Paint ? (Paint) p2.platformPaint : null;
        RenderJob re = new RenderJob(new Runnable() { // from class: com.sun.javafx.tk.quantum.QuantumToolkit.5
            private com.sun.prism.paint.Color getClearColor() {
                if (currentPaint == null) {
                    return com.sun.prism.paint.Color.WHITE;
                }
                if (currentPaint.getType() == Paint.Type.COLOR) {
                    return (com.sun.prism.paint.Color) currentPaint;
                }
                if (currentPaint.isOpaque()) {
                    return com.sun.prism.paint.Color.TRANSPARENT;
                }
                return com.sun.prism.paint.Color.WHITE;
            }

            private void draw(Graphics g2, int x2, int y2, int w2, int h2) {
                g2.setLights(p2.lights);
                g2.setDepthBuffer(p2.depthBuffer);
                g2.clear(getClearColor());
                if (currentPaint != null && currentPaint.getType() != Paint.Type.COLOR) {
                    g2.getRenderTarget().setOpaque(currentPaint.isOpaque());
                    g2.setPaint(currentPaint);
                    g2.fillQuad(0.0f, 0.0f, w2, h2);
                }
                if (x2 != 0 || y2 != 0) {
                    g2.translate(-x2, -y2);
                }
                if (p2.transform != null) {
                    g2.transform(p2.transform);
                }
                if (p2.root != null) {
                    if (p2.camera != null) {
                        g2.setCamera(p2.camera);
                    }
                    NGNode ngNode = p2.root;
                    ngNode.render(g2);
                }
            }

            @Override // java.lang.Runnable
            public void run() {
                ResourceFactory rf = GraphicsPipeline.getDefaultResourceFactory();
                if (rf.isDeviceReady()) {
                    int x2 = p2.f11964x;
                    int y2 = p2.f11965y;
                    int w2 = p2.width;
                    int h2 = p2.height;
                    if (w2 <= 0 || h2 <= 0) {
                        return;
                    }
                    try {
                        try {
                            QuantumImage pImage = p2.platformImage instanceof QuantumImage ? (QuantumImage) p2.platformImage : new QuantumImage(null);
                            RTTexture rt = pImage.getRT(w2, h2, rf);
                            if (rt == null) {
                                Disposer.cleanUp();
                                rf.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                                return;
                            }
                            Graphics g2 = rt.createGraphics();
                            draw(g2, x2, y2, w2, h2);
                            int[] pixels = pImage.rt.getPixels();
                            if (pixels != null) {
                                pImage.setImage(Image.fromIntArgbPreData(pixels, w2, h2));
                            } else {
                                IntBuffer ib = IntBuffer.allocate(w2 * h2);
                                if (pImage.rt.readPixels(ib, pImage.rt.getContentX(), pImage.rt.getContentY(), w2, h2)) {
                                    pImage.setImage(Image.fromIntArgbPreData(ib, w2, h2));
                                } else {
                                    pImage.dispose();
                                    pImage = null;
                                }
                            }
                            rt.unlock();
                            p2.platformImage = pImage;
                            Disposer.cleanUp();
                            rf.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                        } catch (Throwable t2) {
                            t2.printStackTrace(System.err);
                            Disposer.cleanUp();
                            rf.getTextureResourcePool().freeDisposalRequestedAndCheckResources(true);
                        }
                    } catch (Throwable th) {
                        Disposer.cleanUp();
                        rf.getTextureResourcePool().freeDisposalRequestedAndCheckResources(false);
                        throw th;
                    }
                }
            }
        });
        CountDownLatch latch = new CountDownLatch(1);
        re.setCompletionListener(job -> {
            latch.countDown();
        });
        addRenderJob(re);
        while (true) {
            try {
                latch.await();
                Object image = p2.platformImage;
                p2.platformImage = saveImage;
                return image;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public CommonDialogs.FileChooserResult showFileChooser(TKStage ownerWindow, String title, File initialDirectory, String initialFileName, FileChooserType fileChooserType, List<FileChooser.ExtensionFilter> extensionFilters, FileChooser.ExtensionFilter selectedFilter) {
        WindowStage blockedStage = null;
        try {
            blockedStage = blockOwnerStage(ownerWindow);
            CommonDialogs.FileChooserResult fileChooserResultShowFileChooser = CommonDialogs.showFileChooser(ownerWindow instanceof WindowStage ? ((WindowStage) ownerWindow).getPlatformWindow() : null, initialDirectory, initialFileName, title, fileChooserType == FileChooserType.SAVE ? 1 : 0, fileChooserType == FileChooserType.OPEN_MULTIPLE, convertExtensionFilters(extensionFilters), extensionFilters.indexOf(selectedFilter));
            if (blockedStage != null) {
                blockedStage.setEnabled(true);
            }
            return fileChooserResultShowFileChooser;
        } catch (Throwable th) {
            if (blockedStage != null) {
                blockedStage.setEnabled(true);
            }
            throw th;
        }
    }

    @Override // com.sun.javafx.tk.Toolkit
    public File showDirectoryChooser(TKStage ownerWindow, String title, File initialDirectory) {
        WindowStage blockedStage = null;
        try {
            blockedStage = blockOwnerStage(ownerWindow);
            File fileShowFolderChooser = CommonDialogs.showFolderChooser(ownerWindow instanceof WindowStage ? ((WindowStage) ownerWindow).getPlatformWindow() : null, initialDirectory, title);
            if (blockedStage != null) {
                blockedStage.setEnabled(true);
            }
            return fileShowFolderChooser;
        } catch (Throwable th) {
            if (blockedStage != null) {
                blockedStage.setEnabled(true);
            }
            throw th;
        }
    }

    private WindowStage blockOwnerStage(TKStage stage) {
        if (stage instanceof WindowStage) {
            TKStage ownerStage = ((WindowStage) stage).getOwner();
            if (ownerStage instanceof WindowStage) {
                WindowStage ownerWindowStage = (WindowStage) ownerStage;
                ownerWindowStage.setEnabled(false);
                return ownerWindowStage;
            }
            return null;
        }
        return null;
    }

    private static List<CommonDialogs.ExtensionFilter> convertExtensionFilters(List<FileChooser.ExtensionFilter> extensionFilters) {
        CommonDialogs.ExtensionFilter[] glassExtensionFilters = new CommonDialogs.ExtensionFilter[extensionFilters.size()];
        int i2 = 0;
        for (FileChooser.ExtensionFilter extensionFilter : extensionFilters) {
            int i3 = i2;
            i2++;
            glassExtensionFilters[i3] = new CommonDialogs.ExtensionFilter(extensionFilter.getDescription(), extensionFilter.getExtensions());
        }
        return Arrays.asList(glassExtensionFilters);
    }

    @Override // com.sun.javafx.tk.Toolkit
    public long getMultiClickTime() {
        return View.getMultiClickTime();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getMultiClickMaxX() {
        return View.getMultiClickMaxX();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getMultiClickMaxY() {
        return View.getMultiClickMaxY();
    }

    @Override // com.sun.javafx.tk.Toolkit
    public String getThemeName() {
        return Application.GetApplication().getHighContrastTheme();
    }
}
