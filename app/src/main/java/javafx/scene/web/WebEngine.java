package javafx.scene.web;

import com.sun.glass.ui.Application;
import com.sun.glass.ui.Clipboard;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.scene.web.Debugger;
import com.sun.javafx.sg.prism.NGNode;
import com.sun.javafx.tk.TKPulseListener;
import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.CursorManagerImpl;
import com.sun.javafx.webkit.EventLoopImpl;
import com.sun.javafx.webkit.ThemeClientImpl;
import com.sun.javafx.webkit.UIClientImpl;
import com.sun.javafx.webkit.UtilitiesImpl;
import com.sun.javafx.webkit.WebPageClientImpl;
import com.sun.javafx.webkit.prism.PrismGraphicsManager;
import com.sun.javafx.webkit.prism.PrismInvoker;
import com.sun.javafx.webkit.prism.theme.PrismRenderer;
import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.javafx.webkit.theme.Renderer;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.prism.Graphics;
import com.sun.webkit.CursorManager;
import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.EventLoop;
import com.sun.webkit.InspectorClient;
import com.sun.webkit.Invoker;
import com.sun.webkit.LoadListenerClient;
import com.sun.webkit.ThemeClient;
import com.sun.webkit.Timer;
import com.sun.webkit.Utilities;
import com.sun.webkit.WebPage;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.network.URLs;
import com.sun.webkit.network.Util;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectPropertyBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.StringPropertyBase;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Rectangle2D;
import javafx.print.JobSettings;
import javafx.print.PageLayout;
import javafx.print.PageRange;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.util.Callback;
import jssc.SerialPortException;
import org.w3c.dom.Document;

/* loaded from: jfxrt.jar:javafx/scene/web/WebEngine.class */
public final class WebEngine {
    private static final Logger logger;
    private static int instanceCount;
    private final ObjectProperty<WebView> view;
    private final LoadWorker loadWorker;
    private final WebPage page;
    private final SelfDisposer disposer;
    private final DebuggerImpl debugger;
    private boolean userDataDirectoryApplied;
    private final DocumentProperty document;
    private final ReadOnlyStringWrapper location;
    private final ReadOnlyStringWrapper title;
    private BooleanProperty javaScriptEnabled;
    private StringProperty userStyleSheetLocation;
    private final ObjectProperty<File> userDataDirectory;
    private StringProperty userAgent;
    private final ObjectProperty<EventHandler<WebEvent<String>>> onAlert;
    private final ObjectProperty<EventHandler<WebEvent<String>>> onStatusChanged;
    private final ObjectProperty<EventHandler<WebEvent<Rectangle2D>>> onResized;
    private final ObjectProperty<EventHandler<WebEvent<Boolean>>> onVisibilityChanged;
    private final ObjectProperty<Callback<PopupFeatures, WebEngine>> createPopupHandler;
    private final ObjectProperty<Callback<String, Boolean>> confirmHandler;
    private final ObjectProperty<Callback<PromptData, String>> promptHandler;
    private final ObjectProperty<EventHandler<WebErrorEvent>> onError;
    private final WebHistory history;

    static /* synthetic */ int access$1410() {
        int i2 = instanceCount;
        instanceCount = i2 - 1;
        return i2;
    }

    static {
        Accessor.setPageAccessor(w2 -> {
            if (w2 == null) {
                return null;
            }
            return w2.getPage();
        });
        Invoker.setInvoker(new PrismInvoker());
        Renderer.setRenderer(new PrismRenderer());
        WCGraphicsManager.setGraphicsManager(new PrismGraphicsManager());
        CursorManager.setCursorManager(new CursorManagerImpl());
        EventLoop.setEventLoop(new EventLoopImpl());
        ThemeClient.setDefaultRenderTheme(new RenderThemeImpl());
        Utilities.setUtilities(new UtilitiesImpl());
        logger = Logger.getLogger(WebEngine.class.getName());
        instanceCount = 0;
    }

    public final Worker<Void> getLoadWorker() {
        return this.loadWorker;
    }

    public final Document getDocument() {
        return this.document.getValue2();
    }

    public final ReadOnlyObjectProperty<Document> documentProperty() {
        return this.document;
    }

    public final String getLocation() {
        return this.location.getValue2();
    }

    public final ReadOnlyStringProperty locationProperty() {
        return this.location.getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateLocation(String value) {
        this.location.set(value);
        this.document.invalidate(false);
        this.title.set((String) null);
    }

    public final String getTitle() {
        return this.title.getValue2();
    }

    public final ReadOnlyStringProperty titleProperty() {
        return this.title.getReadOnlyProperty();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTitle() {
        this.title.set(this.page.getTitle(this.page.getMainFrame()));
    }

    public final void setJavaScriptEnabled(boolean value) {
        javaScriptEnabledProperty().set(value);
    }

    public final boolean isJavaScriptEnabled() {
        if (this.javaScriptEnabled == null) {
            return true;
        }
        return this.javaScriptEnabled.get();
    }

    public final BooleanProperty javaScriptEnabledProperty() {
        if (this.javaScriptEnabled == null) {
            this.javaScriptEnabled = new BooleanPropertyBase(true) { // from class: javafx.scene.web.WebEngine.1
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    WebEngine.checkThread();
                    WebEngine.this.page.setJavaScriptEnabled(get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebEngine.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "javaScriptEnabled";
                }
            };
        }
        return this.javaScriptEnabled;
    }

    public final void setUserStyleSheetLocation(String value) {
        userStyleSheetLocationProperty().set(value);
    }

    public final String getUserStyleSheetLocation() {
        if (this.userStyleSheetLocation == null) {
            return null;
        }
        return this.userStyleSheetLocation.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] readFully(BufferedInputStream in) throws IOException {
        byte[] chunk;
        int outSize = 0;
        List<byte[]> outList = new ArrayList<>();
        byte[] buffer = new byte[4096];
        while (true) {
            int nBytes = in.read(buffer);
            if (nBytes < 0) {
                break;
            }
            if (nBytes == buffer.length) {
                chunk = buffer;
                buffer = new byte[4096];
            } else {
                chunk = new byte[nBytes];
                System.arraycopy(buffer, 0, chunk, 0, nBytes);
            }
            outList.add(chunk);
            outSize += nBytes;
        }
        byte[] out = new byte[outSize];
        int outPos = 0;
        for (byte[] chunk2 : outList) {
            System.arraycopy(chunk2, 0, out, outPos, chunk2.length);
            outPos += chunk2.length;
        }
        return out;
    }

    public final StringProperty userStyleSheetLocationProperty() {
        if (this.userStyleSheetLocation == null) {
            this.userStyleSheetLocation = new StringPropertyBase(null) { // from class: javafx.scene.web.WebEngine.2
                private static final String DATA_PREFIX = "data:text/css;charset=utf-8;base64,";

                @Override // javafx.beans.property.StringPropertyBase
                public void invalidated() {
                    String dataUrl;
                    WebEngine.checkThread();
                    String url = get();
                    if (url == null || url.length() <= 0) {
                        dataUrl = null;
                    } else if (url.startsWith(DATA_PREFIX)) {
                        dataUrl = url;
                    } else if (url.startsWith("file:") || url.startsWith("jar:") || url.startsWith("data:")) {
                        try {
                            URLConnection conn = URLs.newURL(url).openConnection();
                            conn.connect();
                            BufferedInputStream in = new BufferedInputStream(conn.getInputStream());
                            byte[] inBytes = WebEngine.this.readFully(in);
                            String out = Base64.getMimeEncoder().encodeToString(inBytes);
                            dataUrl = DATA_PREFIX + out;
                        } catch (IOException e2) {
                            throw new RuntimeException(e2);
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid stylesheet URL");
                    }
                    WebEngine.this.page.setUserStyleSheetLocation(dataUrl);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebEngine.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "userStyleSheetLocation";
                }
            };
        }
        return this.userStyleSheetLocation;
    }

    public final File getUserDataDirectory() {
        return this.userDataDirectory.get();
    }

    public final void setUserDataDirectory(File value) {
        this.userDataDirectory.set(value);
    }

    public final ObjectProperty<File> userDataDirectoryProperty() {
        return this.userDataDirectory;
    }

    public final void setUserAgent(String value) {
        userAgentProperty().set(value);
    }

    public final String getUserAgent() {
        return this.userAgent == null ? this.page.getUserAgent() : this.userAgent.get();
    }

    public final StringProperty userAgentProperty() {
        if (this.userAgent == null) {
            this.userAgent = new StringPropertyBase(this.page.getUserAgent()) { // from class: javafx.scene.web.WebEngine.3
                @Override // javafx.beans.property.StringPropertyBase
                public void invalidated() {
                    WebEngine.checkThread();
                    WebEngine.this.page.setUserAgent(get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return WebEngine.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "userAgent";
                }
            };
        }
        return this.userAgent;
    }

    public final EventHandler<WebEvent<String>> getOnAlert() {
        return this.onAlert.get();
    }

    public final void setOnAlert(EventHandler<WebEvent<String>> handler) {
        this.onAlert.set(handler);
    }

    public final ObjectProperty<EventHandler<WebEvent<String>>> onAlertProperty() {
        return this.onAlert;
    }

    public final EventHandler<WebEvent<String>> getOnStatusChanged() {
        return this.onStatusChanged.get();
    }

    public final void setOnStatusChanged(EventHandler<WebEvent<String>> handler) {
        this.onStatusChanged.set(handler);
    }

    public final ObjectProperty<EventHandler<WebEvent<String>>> onStatusChangedProperty() {
        return this.onStatusChanged;
    }

    public final EventHandler<WebEvent<Rectangle2D>> getOnResized() {
        return this.onResized.get();
    }

    public final void setOnResized(EventHandler<WebEvent<Rectangle2D>> handler) {
        this.onResized.set(handler);
    }

    public final ObjectProperty<EventHandler<WebEvent<Rectangle2D>>> onResizedProperty() {
        return this.onResized;
    }

    public final EventHandler<WebEvent<Boolean>> getOnVisibilityChanged() {
        return this.onVisibilityChanged.get();
    }

    public final void setOnVisibilityChanged(EventHandler<WebEvent<Boolean>> handler) {
        this.onVisibilityChanged.set(handler);
    }

    public final ObjectProperty<EventHandler<WebEvent<Boolean>>> onVisibilityChangedProperty() {
        return this.onVisibilityChanged;
    }

    public final Callback<PopupFeatures, WebEngine> getCreatePopupHandler() {
        return this.createPopupHandler.get();
    }

    public final void setCreatePopupHandler(Callback<PopupFeatures, WebEngine> handler) {
        this.createPopupHandler.set(handler);
    }

    public final ObjectProperty<Callback<PopupFeatures, WebEngine>> createPopupHandlerProperty() {
        return this.createPopupHandler;
    }

    public final Callback<String, Boolean> getConfirmHandler() {
        return this.confirmHandler.get();
    }

    public final void setConfirmHandler(Callback<String, Boolean> handler) {
        this.confirmHandler.set(handler);
    }

    public final ObjectProperty<Callback<String, Boolean>> confirmHandlerProperty() {
        return this.confirmHandler;
    }

    public final Callback<PromptData, String> getPromptHandler() {
        return this.promptHandler.get();
    }

    public final void setPromptHandler(Callback<PromptData, String> handler) {
        this.promptHandler.set(handler);
    }

    public final ObjectProperty<Callback<PromptData, String>> promptHandlerProperty() {
        return this.promptHandler;
    }

    public final EventHandler<WebErrorEvent> getOnError() {
        return this.onError.get();
    }

    public final void setOnError(EventHandler<WebErrorEvent> handler) {
        this.onError.set(handler);
    }

    public final ObjectProperty<EventHandler<WebErrorEvent>> onErrorProperty() {
        return this.onError;
    }

    public WebEngine() {
        this(null, false);
    }

    public WebEngine(String url) {
        this(url, true);
    }

    private WebEngine(String url, boolean callLoad) {
        this.view = new SimpleObjectProperty(this, "view");
        this.loadWorker = new LoadWorker();
        this.debugger = new DebuggerImpl();
        this.userDataDirectoryApplied = false;
        this.document = new DocumentProperty();
        this.location = new ReadOnlyStringWrapper(this, "location");
        this.title = new ReadOnlyStringWrapper(this, "title");
        this.userDataDirectory = new SimpleObjectProperty(this, "userDataDirectory");
        this.onAlert = new SimpleObjectProperty(this, "onAlert");
        this.onStatusChanged = new SimpleObjectProperty(this, "onStatusChanged");
        this.onResized = new SimpleObjectProperty(this, "onResized");
        this.onVisibilityChanged = new SimpleObjectProperty(this, "onVisibilityChanged");
        this.createPopupHandler = new SimpleObjectProperty(this, "createPopupHandler", p2 -> {
            return this;
        });
        this.confirmHandler = new SimpleObjectProperty(this, "confirmHandler");
        this.promptHandler = new SimpleObjectProperty(this, "promptHandler");
        this.onError = new SimpleObjectProperty(this, "onError");
        checkThread();
        Accessor accessor = new AccessorImpl();
        this.page = new WebPage(new WebPageClientImpl(accessor), new UIClientImpl(accessor), null, new InspectorClientImpl(), new ThemeClientImpl(accessor), false);
        this.page.addLoadListenerClient(new PageLoadListener());
        this.history = new WebHistory(this.page);
        this.disposer = new SelfDisposer(this.page);
        Disposer.addRecord(this, this.disposer);
        if (callLoad) {
            load(url);
        }
        if (instanceCount == 0 && Timer.getMode() == Timer.Mode.PLATFORM_TICKS) {
            PulseTimer.start();
        }
        instanceCount++;
    }

    public void load(String url) {
        checkThread();
        this.loadWorker.cancelAndReset();
        if (url == null || url.equals("") || url.equals("about:blank")) {
            url = "";
        } else {
            try {
                url = Util.adjustUrlForWebKit(url);
            } catch (MalformedURLException e2) {
                this.loadWorker.dispatchLoadEvent(getMainFrame(), 0, url, null, 0.0d, 0);
                this.loadWorker.dispatchLoadEvent(getMainFrame(), 5, url, null, 0.0d, 2);
                return;
            }
        }
        applyUserDataDirectory();
        this.page.open(this.page.getMainFrame(), url);
    }

    public void loadContent(String content) {
        loadContent(content, Clipboard.HTML_TYPE);
    }

    public void loadContent(String content, String contentType) {
        checkThread();
        this.loadWorker.cancelAndReset();
        applyUserDataDirectory();
        this.page.load(this.page.getMainFrame(), content, contentType);
    }

    public void reload() {
        checkThread();
        this.page.refresh(this.page.getMainFrame());
    }

    public WebHistory getHistory() {
        return this.history;
    }

    public Object executeScript(String script) {
        checkThread();
        applyUserDataDirectory();
        return this.page.executeScript(this.page.getMainFrame(), script);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getMainFrame() {
        return this.page.getMainFrame();
    }

    WebPage getPage() {
        return this.page;
    }

    void setView(WebView view) {
        this.view.setValue(view);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stop() {
        checkThread();
        this.page.stop(this.page.getMainFrame());
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0145 A[LOOP:0: B:6:0x0012->B:30:0x0145, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0139 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void applyUserDataDirectory() {
        /*
            Method dump skipped, instructions count: 340
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javafx.scene.web.WebEngine.applyUserDataDirectory():void");
    }

    private static File defaultUserDataDirectory() {
        return new File(Application.GetApplication().getDataDirectory(), "webview");
    }

    private static void createDirectories(File directory) throws IOException {
        Path path = directory.toPath();
        try {
            Files.createDirectories(path, PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwx------")));
        } catch (UnsupportedOperationException e2) {
            Files.createDirectories(path, new FileAttribute[0]);
        }
    }

    private void fireError(EventType<WebErrorEvent> eventType, String message, Throwable exception) {
        EventHandler<WebErrorEvent> handler = getOnError();
        if (handler != null) {
            handler.handle(new WebErrorEvent(this, eventType, message, exception));
        }
    }

    void dispose() {
        this.disposer.dispose();
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$SelfDisposer.class */
    private static final class SelfDisposer implements DisposerRecord {
        private WebPage page;
        private DirectoryLock userDataDirectoryLock;

        private SelfDisposer(WebPage page) {
            this.page = page;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            if (this.page == null) {
                return;
            }
            this.page.dispose();
            this.page = null;
            if (this.userDataDirectoryLock != null) {
                this.userDataDirectoryLock.close();
            }
            WebEngine.access$1410();
            if (WebEngine.instanceCount != 0 || Timer.getMode() != Timer.Mode.PLATFORM_TICKS) {
                return;
            }
            PulseTimer.stop();
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$AccessorImpl.class */
    private static final class AccessorImpl extends Accessor {
        private final WeakReference<WebEngine> engine;

        private AccessorImpl(WebEngine w2) {
            this.engine = new WeakReference<>(w2);
        }

        @Override // com.sun.javafx.webkit.Accessor
        public WebEngine getEngine() {
            return this.engine.get();
        }

        @Override // com.sun.javafx.webkit.Accessor
        public WebPage getPage() {
            WebEngine w2 = getEngine();
            if (w2 == null) {
                return null;
            }
            return w2.page;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.javafx.webkit.Accessor
        public WebView getView() {
            WebEngine w2 = getEngine();
            if (w2 == null) {
                return null;
            }
            return (WebView) w2.view.get();
        }

        @Override // com.sun.javafx.webkit.Accessor
        public void addChild(Node child) {
            WebView view = getView();
            if (view != null) {
                view.getChildren().add(child);
            }
        }

        @Override // com.sun.javafx.webkit.Accessor
        public void removeChild(Node child) {
            WebView view = getView();
            if (view != null) {
                view.getChildren().remove(child);
            }
        }

        @Override // com.sun.javafx.webkit.Accessor
        public void addViewListener(InvalidationListener l2) {
            WebEngine w2 = getEngine();
            if (w2 != null) {
                w2.view.addListener(l2);
            }
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$PulseTimer.class */
    private static final class PulseTimer {
        private static final AnimationTimer animation = new AnimationTimer() { // from class: javafx.scene.web.WebEngine.PulseTimer.1
            @Override // javafx.animation.AnimationTimer
            public void handle(long l2) {
            }
        };
        private static final TKPulseListener listener = () -> {
            Platform.runLater(() -> {
                Timer.getTimer().notifyTick();
            });
        };

        private PulseTimer() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void start() {
            Toolkit.getToolkit().addSceneTkPulseListener(listener);
            animation.start();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void stop() {
            Toolkit.getToolkit().removeSceneTkPulseListener(listener);
            animation.stop();
        }
    }

    static void checkThread() {
        Toolkit.getToolkit().checkFxUserThread();
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$PageLoadListener.class */
    private static final class PageLoadListener implements LoadListenerClient {
        private final WeakReference<WebEngine> engine;

        private PageLoadListener(WebEngine engine) {
            this.engine = new WeakReference<>(engine);
        }

        @Override // com.sun.webkit.LoadListenerClient
        public void dispatchLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
            WebEngine w2 = this.engine.get();
            if (w2 == null) {
                return;
            }
            w2.loadWorker.dispatchLoadEvent(frame, state, url, contentType, progress, errorCode);
        }

        @Override // com.sun.webkit.LoadListenerClient
        public void dispatchResourceLoadEvent(long frame, int state, String url, String contentType, double progress, int errorCode) {
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$LoadWorker.class */
    private final class LoadWorker implements Worker<Void> {
        private final ReadOnlyObjectWrapper<Worker.State> state;
        private final ReadOnlyObjectWrapper<Void> value;
        private final ReadOnlyObjectWrapper<Throwable> exception;
        private final ReadOnlyDoubleWrapper workDone;
        private final ReadOnlyDoubleWrapper totalWorkToBeDone;
        private final ReadOnlyDoubleWrapper progress;
        private final ReadOnlyBooleanWrapper running;
        private final ReadOnlyStringWrapper message;
        private final ReadOnlyStringWrapper title;

        private LoadWorker() {
            this.state = new ReadOnlyObjectWrapper<>(this, "state", Worker.State.READY);
            this.value = new ReadOnlyObjectWrapper<>(this, "value", null);
            this.exception = new ReadOnlyObjectWrapper<>(this, "exception");
            this.workDone = new ReadOnlyDoubleWrapper(this, "workDone", -1.0d);
            this.totalWorkToBeDone = new ReadOnlyDoubleWrapper(this, "totalWork", -1.0d);
            this.progress = new ReadOnlyDoubleWrapper(this, "progress", -1.0d);
            this.running = new ReadOnlyBooleanWrapper(this, "running", false);
            this.message = new ReadOnlyStringWrapper(this, "message", "");
            this.title = new ReadOnlyStringWrapper(this, "title", "WebEngine Loader");
        }

        @Override // javafx.concurrent.Worker
        public final Worker.State getState() {
            WebEngine.checkThread();
            return this.state.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyObjectProperty<Worker.State> stateProperty() {
            WebEngine.checkThread();
            return this.state.getReadOnlyProperty();
        }

        private void updateState(Worker.State value) {
            WebEngine.checkThread();
            this.state.set(value);
            this.running.set(value == Worker.State.SCHEDULED || value == Worker.State.RUNNING);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.concurrent.Worker
        public final Void getValue() {
            WebEngine.checkThread();
            return this.value.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyObjectProperty<Void> valueProperty() {
            WebEngine.checkThread();
            return this.value.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public final Throwable getException() {
            WebEngine.checkThread();
            return this.exception.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyObjectProperty<Throwable> exceptionProperty() {
            WebEngine.checkThread();
            return this.exception.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public final double getWorkDone() {
            WebEngine.checkThread();
            return this.workDone.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyDoubleProperty workDoneProperty() {
            WebEngine.checkThread();
            return this.workDone.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public final double getTotalWork() {
            WebEngine.checkThread();
            return this.totalWorkToBeDone.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyDoubleProperty totalWorkProperty() {
            WebEngine.checkThread();
            return this.totalWorkToBeDone.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public final double getProgress() {
            WebEngine.checkThread();
            return this.progress.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyDoubleProperty progressProperty() {
            WebEngine.checkThread();
            return this.progress.getReadOnlyProperty();
        }

        private void updateProgress(double p2) {
            this.totalWorkToBeDone.set(100.0d);
            this.workDone.set(p2 * 100.0d);
            this.progress.set(p2);
        }

        @Override // javafx.concurrent.Worker
        public final boolean isRunning() {
            WebEngine.checkThread();
            return this.running.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyBooleanProperty runningProperty() {
            WebEngine.checkThread();
            return this.running.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public final String getMessage() {
            return this.message.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyStringProperty messageProperty() {
            return this.message.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public final String getTitle() {
            return this.title.get();
        }

        @Override // javafx.concurrent.Worker
        public final ReadOnlyStringProperty titleProperty() {
            return this.title.getReadOnlyProperty();
        }

        @Override // javafx.concurrent.Worker
        public boolean cancel() {
            if (isRunning()) {
                WebEngine.this.stop();
                return true;
            }
            return false;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void cancelAndReset() {
            cancel();
            this.exception.set(null);
            this.message.set("");
            this.totalWorkToBeDone.set(-1.0d);
            this.workDone.set(-1.0d);
            this.progress.set(-1.0d);
            updateState(Worker.State.READY);
            this.running.set(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void dispatchLoadEvent(long frame, int state, String url, String contentType, double workDone, int errorCode) {
            if (frame != WebEngine.this.getMainFrame()) {
            }
            switch (state) {
                case 0:
                    this.message.set("Loading " + url);
                    WebEngine.this.updateLocation(url);
                    updateProgress(0.0d);
                    updateState(Worker.State.SCHEDULED);
                    updateState(Worker.State.RUNNING);
                    break;
                case 1:
                    this.message.set("Loading complete");
                    updateProgress(1.0d);
                    updateState(Worker.State.SUCCEEDED);
                    break;
                case 2:
                    this.message.set("Loading " + url);
                    WebEngine.this.updateLocation(url);
                    break;
                case 3:
                    this.message.set("Replaced " + url);
                    WebEngine.this.location.set(url);
                    break;
                case 5:
                    this.message.set("Loading failed");
                    this.exception.set(describeError(errorCode));
                    updateState(Worker.State.FAILED);
                    break;
                case 6:
                    this.message.set("Loading stopped");
                    updateState(Worker.State.CANCELLED);
                    break;
                case 11:
                    WebEngine.this.updateTitle();
                    break;
                case 14:
                    if (this.state.get() != Worker.State.RUNNING) {
                        dispatchLoadEvent(frame, 0, url, contentType, workDone, errorCode);
                    }
                    WebEngine.this.document.invalidate(true);
                    break;
                case 30:
                    updateProgress(workDone);
                    break;
            }
        }

        private Throwable describeError(int errorCode) {
            String reason = "Unknown error";
            switch (errorCode) {
                case 1:
                    reason = "Unknown host";
                    break;
                case 2:
                    reason = "Malformed URL";
                    break;
                case 3:
                    reason = "SSL handshake failed";
                    break;
                case 4:
                    reason = "Connection refused by server";
                    break;
                case 5:
                    reason = "Connection reset by server";
                    break;
                case 6:
                    reason = "No route to host";
                    break;
                case 7:
                    reason = "Connection timed out";
                    break;
                case 8:
                    reason = SerialPortException.TYPE_PERMISSION_DENIED;
                    break;
                case 9:
                    reason = "Invalid response from server";
                    break;
                case 10:
                    reason = "Too many redirects";
                    break;
                case 11:
                    reason = "File not found";
                    break;
            }
            return new Throwable(reason);
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$DocumentProperty.class */
    private final class DocumentProperty extends ReadOnlyObjectPropertyBase<Document> {
        private boolean available;
        private Document document;

        private DocumentProperty() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void invalidate(boolean available) {
            if (this.available || available) {
                this.available = available;
                this.document = null;
                fireValueChangedEvent();
            }
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public Document get() {
            if (!this.available) {
                return null;
            }
            if (this.document == null) {
                this.document = WebEngine.this.page.getDocument(WebEngine.this.page.getMainFrame());
                if (this.document == null) {
                    this.available = false;
                }
            }
            return this.document;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return WebEngine.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return Constants.DOCUMENT_PNAME;
        }
    }

    @Deprecated
    public Debugger impl_getDebugger() {
        return this.debugger;
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$DebuggerImpl.class */
    private final class DebuggerImpl implements Debugger {
        private boolean enabled;
        private Callback<String, Void> messageCallback;

        private DebuggerImpl() {
        }

        @Override // com.sun.javafx.scene.web.Debugger
        public boolean isEnabled() {
            WebEngine.checkThread();
            return this.enabled;
        }

        @Override // com.sun.javafx.scene.web.Debugger
        public void setEnabled(boolean enabled) {
            WebEngine.checkThread();
            if (enabled != this.enabled) {
                if (enabled) {
                    WebEngine.this.page.setDeveloperExtrasEnabled(true);
                    WebEngine.this.page.connectInspectorFrontend();
                } else {
                    WebEngine.this.page.disconnectInspectorFrontend();
                    WebEngine.this.page.setDeveloperExtrasEnabled(false);
                }
                this.enabled = enabled;
            }
        }

        @Override // com.sun.javafx.scene.web.Debugger
        public void sendMessage(String message) {
            WebEngine.checkThread();
            if (!this.enabled) {
                throw new IllegalStateException("Debugger is not enabled");
            }
            if (message != null) {
                WebEngine.this.page.dispatchInspectorMessageFromFrontend(message);
                return;
            }
            throw new NullPointerException("message is null");
        }

        @Override // com.sun.javafx.scene.web.Debugger
        public Callback<String, Void> getMessageCallback() {
            WebEngine.checkThread();
            return this.messageCallback;
        }

        @Override // com.sun.javafx.scene.web.Debugger
        public void setMessageCallback(Callback<String, Void> callback) {
            WebEngine.checkThread();
            this.messageCallback = callback;
        }
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$InspectorClientImpl.class */
    private static final class InspectorClientImpl implements InspectorClient {
        private final WeakReference<WebEngine> engine;

        private InspectorClientImpl(WebEngine engine) {
            this.engine = new WeakReference<>(engine);
        }

        @Override // com.sun.webkit.InspectorClient
        public boolean sendMessageToFrontend(String message) {
            Callback<String, Void> messageCallback;
            boolean result = false;
            WebEngine webEngine = this.engine.get();
            if (webEngine != null && (messageCallback = webEngine.debugger.messageCallback) != null) {
                AccessController.doPrivileged(() -> {
                    messageCallback.call(message);
                    return null;
                }, webEngine.page.getAccessControlContext());
                result = true;
            }
            return result;
        }
    }

    private static final boolean printStatusOK(PrinterJob job) {
        switch (job.getJobStatus()) {
            case NOT_STARTED:
            case PRINTING:
                return true;
            default:
                return false;
        }
    }

    public void print(PrinterJob job) {
        if (!printStatusOK(job)) {
            return;
        }
        PageLayout pl = job.getJobSettings().getPageLayout();
        float width = (float) pl.getPrintableWidth();
        float height = (float) pl.getPrintableHeight();
        int pageCount = this.page.beginPrinting(width, height);
        JobSettings jobSettings = job.getJobSettings();
        if (jobSettings.getPageRanges() != null) {
            PageRange[] pageRanges = jobSettings.getPageRanges();
            for (PageRange p2 : pageRanges) {
                for (int i2 = p2.getStartPage(); i2 <= p2.getEndPage() && i2 <= pageCount; i2++) {
                    if (printStatusOK(job)) {
                        Node printable = new Printable(i2 - 1, width);
                        job.printPage(printable);
                    }
                }
            }
        } else {
            for (int i3 = 0; i3 < pageCount; i3++) {
                if (printStatusOK(job)) {
                    Node printable2 = new Printable(i3, width);
                    job.printPage(printable2);
                }
            }
        }
        this.page.endPrinting();
    }

    /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$Printable.class */
    final class Printable extends Node {
        private final NGNode peer;

        Printable(int pageIndex, float width) {
            this.peer = new Peer(pageIndex, width);
        }

        @Override // javafx.scene.Node
        protected NGNode impl_createPeer() {
            return this.peer;
        }

        @Override // javafx.scene.Node
        public Object impl_processMXNode(MXNodeAlgorithm alg, MXNodeAlgorithmContext ctx) {
            return null;
        }

        @Override // javafx.scene.Node
        public BaseBounds impl_computeGeomBounds(BaseBounds bounds, BaseTransform tx) {
            return bounds;
        }

        @Override // javafx.scene.Node
        protected boolean impl_computeContains(double d2, double d1) {
            return false;
        }

        /* loaded from: jfxrt.jar:javafx/scene/web/WebEngine$Printable$Peer.class */
        private final class Peer extends NGNode {
            private final int pageIndex;
            private final float width;

            Peer(int pageIndex, float width) {
                this.pageIndex = pageIndex;
                this.width = width;
            }

            @Override // com.sun.javafx.sg.prism.NGNode
            protected void renderContent(Graphics g2) {
                WCGraphicsContext gc = WCGraphicsManager.getGraphicsManager().createGraphicsContext(g2);
                WebEngine.this.page.print(gc, this.pageIndex, this.width);
            }

            @Override // com.sun.javafx.sg.prism.NGNode
            protected boolean hasOverlappingContents() {
                return false;
            }
        }
    }
}
