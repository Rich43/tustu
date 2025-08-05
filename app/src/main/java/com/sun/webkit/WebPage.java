package com.sun.webkit;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.javafx.tk.Toolkit;
import com.sun.webkit.event.WCFocusEvent;
import com.sun.webkit.event.WCInputMethodEvent;
import com.sun.webkit.event.WCKeyEvent;
import com.sun.webkit.event.WCMouseEvent;
import com.sun.webkit.event.WCMouseWheelEvent;
import com.sun.webkit.graphics.RenderTheme;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCGraphicsManager;
import com.sun.webkit.graphics.WCImage;
import com.sun.webkit.graphics.WCPageBackBuffer;
import com.sun.webkit.graphics.WCPoint;
import com.sun.webkit.graphics.WCRectangle;
import com.sun.webkit.graphics.WCRenderQueue;
import com.sun.webkit.graphics.WCSize;
import com.sun.webkit.network.CookieManager;
import com.sun.webkit.network.URLs;
import java.net.CookieHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import netscape.javascript.JSException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: jfxrt.jar:com/sun/webkit/WebPage.class */
public final class WebPage {
    private static final int MAX_FRAME_QUEUE_SIZE = 10;
    private long pPage;
    private int width;
    private int height;
    private int fontSmoothingType;
    private final WCFrameView hostWindow;
    private final AccessControlContext accessControlContext;
    private int updateContentCycleID;
    private static boolean firstWebPageCreated;
    private WCPageBackBuffer backbuffer;
    private final WebPageClient pageClient;
    private final UIClient uiClient;
    private final PolicyClient policyClient;
    private InputMethodClient imClient;
    private final InspectorClient inspectorClient;
    private final RenderTheme renderTheme;
    private final ScrollBarTheme scrollbarTheme;
    public static final int DND_DST_ENTER = 0;
    public static final int DND_DST_OVER = 1;
    public static final int DND_DST_CHANGE = 2;
    public static final int DND_DST_EXIT = 3;
    public static final int DND_DST_DROP = 4;
    public static final int DND_SRC_ENTER = 100;
    public static final int DND_SRC_OVER = 101;
    public static final int DND_SRC_CHANGE = 102;
    public static final int DND_SRC_EXIT = 103;
    public static final int DND_SRC_DROP = 104;
    private static final Logger log = Logger.getLogger(WebPage.class.getName());
    private static final Logger paintLog = Logger.getLogger(WebPage.class.getName() + ".paint");
    private static final ReentrantLock PAGE_LOCK = new ReentrantLock();
    private boolean isDisposed = false;
    private final Set<Long> frames = new HashSet();
    private final Map<Integer, String> requestURLs = new HashMap();
    private final Set<Integer> requestStarted = new HashSet();
    private final Queue<RenderFrame> frameQueue = new LinkedList();
    private RenderFrame currentFrame = new RenderFrame();
    private List<WCRectangle> dirtyRects = new LinkedList();
    private final List<LoadListenerClient> loadListenerClients = new LinkedList();

    private static native int twkWorkerThreadCount();

    private static native void twkInitWebCore(boolean z2, boolean z3, boolean z4);

    private native long twkCreatePage(boolean z2);

    private native void twkInit(long j2, boolean z2, float f2);

    private native void twkDestroyPage(long j2);

    private native long twkGetMainFrame(long j2);

    private native long twkGetParentFrame(long j2);

    private native long[] twkGetChildFrames(long j2);

    private native String twkGetName(long j2);

    private native String twkGetURL(long j2);

    private native String twkGetInnerText(long j2);

    private native String twkGetRenderTree(long j2);

    private native String twkGetContentType(long j2);

    private native String twkGetTitle(long j2);

    private native String twkGetIconURL(long j2);

    private static native Document twkGetDocument(long j2);

    private static native Element twkGetOwnerElement(long j2);

    private native void twkOpen(long j2, String str);

    private native void twkOverridePreference(long j2, String str, String str2);

    private native void twkResetToConsistentStateBeforeTesting(long j2);

    private native void twkLoad(long j2, String str, String str2);

    private native boolean twkIsLoading(long j2);

    private native void twkStop(long j2);

    private native void twkStopAll(long j2);

    private native void twkRefresh(long j2);

    private native boolean twkGoBackForward(long j2, int i2);

    private native boolean twkCopy(long j2);

    private native boolean twkFindInPage(long j2, String str, boolean z2, boolean z3, boolean z4);

    private native boolean twkFindInFrame(long j2, String str, boolean z2, boolean z3, boolean z4);

    private native float twkGetZoomFactor(long j2, boolean z2);

    private native void twkSetZoomFactor(long j2, float f2, boolean z2);

    private native Object twkExecuteScript(long j2, String str);

    private native void twkReset(long j2);

    private native int twkGetFrameHeight(long j2);

    private native int twkBeginPrinting(long j2, float f2, float f3);

    private native void twkEndPrinting(long j2);

    private native void twkPrint(long j2, WCRenderQueue wCRenderQueue, int i2, float f2);

    private native float twkAdjustFrameHeight(long j2, float f2, float f3, float f4);

    private native int[] twkGetVisibleRect(long j2);

    private native void twkScrollToPosition(long j2, int i2, int i3);

    private native int[] twkGetContentSize(long j2);

    private native void twkSetTransparent(long j2, boolean z2);

    private native void twkSetBackgroundColor(long j2, int i2);

    private native void twkSetBounds(long j2, int i2, int i3, int i4, int i5);

    private native void twkPrePaint(long j2);

    private native void twkUpdateContent(long j2, WCRenderQueue wCRenderQueue, int i2, int i3, int i4, int i5);

    private native void twkUpdateRendering(long j2);

    private native void twkPostPaint(long j2, WCRenderQueue wCRenderQueue, int i2, int i3, int i4, int i5);

    private native String twkGetEncoding(long j2);

    private native void twkSetEncoding(long j2, String str);

    private native void twkProcessFocusEvent(long j2, int i2, int i3);

    private native boolean twkProcessKeyEvent(long j2, int i2, String str, String str2, int i3, boolean z2, boolean z3, boolean z4, boolean z5, double d2);

    private native boolean twkProcessMouseEvent(long j2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, boolean z2, boolean z3, boolean z4, boolean z5, boolean z6, double d2);

    private native boolean twkProcessMouseWheelEvent(long j2, int i2, int i3, int i4, int i5, float f2, float f3, boolean z2, boolean z3, boolean z4, boolean z5, double d2);

    private native boolean twkProcessInputTextChange(long j2, String str, String str2, int[] iArr, int i2);

    private native boolean twkProcessCaretPositionChange(long j2, int i2);

    private native int[] twkGetTextLocation(long j2, int i2);

    private native int twkGetInsertPositionOffset(long j2);

    private native int twkGetCommittedTextLength(long j2);

    private native String twkGetCommittedText(long j2);

    private native String twkGetSelectedText(long j2);

    private native int twkProcessDrag(long j2, int i2, String[] strArr, String[] strArr2, int i3, int i4, int i5, int i6, int i7);

    private native boolean twkExecuteCommand(long j2, String str, String str2);

    private native boolean twkQueryCommandEnabled(long j2, String str);

    private native boolean twkQueryCommandState(long j2, String str);

    private native String twkQueryCommandValue(long j2, String str);

    private native boolean twkIsEditable(long j2);

    private native void twkSetEditable(long j2, boolean z2);

    private native String twkGetHtml(long j2);

    private native boolean twkGetUsePageCache(long j2);

    private native void twkSetUsePageCache(long j2, boolean z2);

    private native boolean twkGetDeveloperExtrasEnabled(long j2);

    private native void twkSetDeveloperExtrasEnabled(long j2, boolean z2);

    private native boolean twkIsJavaScriptEnabled(long j2);

    private native void twkSetJavaScriptEnabled(long j2, boolean z2);

    private native boolean twkIsContextMenuEnabled(long j2);

    private native void twkSetContextMenuEnabled(long j2, boolean z2);

    private native void twkSetUserStyleSheetLocation(long j2, String str);

    private native String twkGetUserAgent(long j2);

    private native void twkSetUserAgent(long j2, String str);

    private native void twkSetLocalStorageDatabasePath(long j2, String str);

    private native void twkSetLocalStorageEnabled(long j2, boolean z2);

    private native int twkGetUnloadEventListenersCount(long j2);

    private native void twkConnectInspectorFrontend(long j2);

    private native void twkDisconnectInspectorFrontend(long j2);

    private native void twkDispatchInspectorMessageFromFrontend(long j2, String str);

    private static native void twkDoJSCGarbageCollection();

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("jfxwebkit");
            log.finer("jfxwebkit loaded");
            if (CookieHandler.getDefault() == null) {
                boolean setDefault = Boolean.valueOf(System.getProperty("com.sun.webkit.setDefaultCookieHandler", "true")).booleanValue();
                if (setDefault) {
                    CookieHandler.setDefault(new CookieManager());
                }
            }
            boolean useJIT = Boolean.valueOf(System.getProperty("com.sun.webkit.useJIT", "true")).booleanValue();
            boolean useDFGJIT = Boolean.valueOf(System.getProperty("com.sun.webkit.useDFGJIT", "true")).booleanValue();
            boolean useCSS3D = Boolean.valueOf(System.getProperty("com.sun.webkit.useCSS3D", "false")).booleanValue();
            boolean useCSS3D2 = useCSS3D && Platform.isSupported(ConditionalFeature.SCENE3D);
            twkInitWebCore(useJIT, useDFGJIT, useCSS3D2);
            Runnable shutdownHook = () -> {
                synchronized (WebPage.class) {
                    MainThread.twkSetShutdown(true);
                }
            };
            Toolkit.getToolkit().addShutdownHook(shutdownHook);
            Runtime.getRuntime().addShutdownHook(new Thread(shutdownHook));
            return null;
        });
        firstWebPageCreated = false;
    }

    private static void collectJSCGarbages() {
        Invoker.getInvoker().checkEventThread();
        Disposer.addRecord(new Object(), WebPage::collectJSCGarbages);
        twkDoJSCGarbageCollection();
    }

    public WebPage(WebPageClient pageClient, UIClient uiClient, PolicyClient policyClient, InspectorClient inspectorClient, ThemeClient themeClient, boolean editable) {
        this.pPage = 0L;
        Invoker.getInvoker().checkEventThread();
        this.pageClient = pageClient;
        this.uiClient = uiClient;
        this.policyClient = policyClient;
        this.inspectorClient = inspectorClient;
        if (themeClient != null) {
            this.renderTheme = themeClient.createRenderTheme();
            this.scrollbarTheme = themeClient.createScrollBarTheme();
        } else {
            this.renderTheme = null;
            this.scrollbarTheme = null;
        }
        this.accessControlContext = AccessController.getContext();
        this.hostWindow = new WCFrameView(this);
        this.pPage = twkCreatePage(editable);
        twkInit(this.pPage, false, WCGraphicsManager.getGraphicsManager().getDevicePixelScale());
        if (pageClient != null && pageClient.isBackBufferSupported()) {
            this.backbuffer = pageClient.createBackBuffer();
            this.backbuffer.ref();
        }
        if (!firstWebPageCreated) {
            Disposer.addRecord(new Object(), WebPage::collectJSCGarbages);
            firstWebPageCreated = true;
        }
    }

    long getPage() {
        return this.pPage;
    }

    private WCWidget getHostWindow() {
        return this.hostWindow;
    }

    public AccessControlContext getAccessControlContext() {
        return this.accessControlContext;
    }

    static boolean lockPage() {
        return Invoker.getInvoker().lock(PAGE_LOCK);
    }

    static boolean unlockPage() {
        return Invoker.getInvoker().unlock(PAGE_LOCK);
    }

    private void addDirtyRect(WCRectangle toPaint) {
        if (toPaint.getWidth() <= 0.0f || toPaint.getHeight() <= 0.0f) {
            return;
        }
        Iterator<WCRectangle> it = this.dirtyRects.iterator();
        while (it.hasNext()) {
            WCRectangle rect = it.next();
            if (rect.contains(toPaint)) {
                return;
            }
            if (toPaint.contains(rect)) {
                it.remove();
            } else {
                WCRectangle u2 = rect.createUnion(toPaint);
                if (u2.getIntWidth() * u2.getIntHeight() < (rect.getIntWidth() * rect.getIntHeight()) + (toPaint.getIntWidth() * toPaint.getIntHeight())) {
                    it.remove();
                    toPaint = u2;
                }
            }
        }
        this.dirtyRects.add(toPaint);
    }

    public boolean isDirty() {
        lockPage();
        try {
            boolean z2 = !this.dirtyRects.isEmpty();
            unlockPage();
            return z2;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    private void updateDirty(WCRectangle clip) {
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Entering, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
        }
        if (this.isDisposed || this.width <= 0 || this.height <= 0) {
            this.dirtyRects.clear();
            return;
        }
        if (clip == null) {
            clip = new WCRectangle(0.0f, 0.0f, this.width, this.height);
        }
        List<WCRectangle> oldDirtyRects = this.dirtyRects;
        this.dirtyRects = new LinkedList();
        twkPrePaint(getPage());
        while (!oldDirtyRects.isEmpty()) {
            WCRectangle r2 = oldDirtyRects.remove(0).intersection(clip);
            if (r2.getWidth() > 0.0f && r2.getHeight() > 0.0f) {
                paintLog.log(Level.FINEST, "Updating: {0}", r2);
                WCRenderQueue rq = WCGraphicsManager.getGraphicsManager().createRenderQueue(r2, true);
                twkUpdateContent(getPage(), rq, r2.getIntX() - 1, r2.getIntY() - 1, r2.getIntWidth() + 2, r2.getIntHeight() + 2);
                this.currentFrame.addRenderQueue(rq);
            }
        }
        WCRenderQueue rq2 = WCGraphicsManager.getGraphicsManager().createRenderQueue(clip, false);
        twkPostPaint(getPage(), rq2, clip.getIntX(), clip.getIntY(), clip.getIntWidth(), clip.getIntHeight());
        this.currentFrame.addRenderQueue(rq2);
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Dirty rects processed, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
        }
        if (this.currentFrame.getRQList().size() > 0) {
            synchronized (this.frameQueue) {
                paintLog.log(Level.FINEST, "About to update frame queue, frameQueue: {0}", this.frameQueue);
                Iterator<RenderFrame> it = this.frameQueue.iterator();
                while (it.hasNext()) {
                    RenderFrame frame = it.next();
                    Iterator it2 = this.currentFrame.getRQList().iterator();
                    while (true) {
                        if (it2.hasNext()) {
                            WCRenderQueue rq3 = (WCRenderQueue) it2.next();
                            WCRectangle rqRect = rq3.getClip();
                            if (rq3.isOpaque() && rqRect.contains(frame.getEnclosingRect())) {
                                paintLog.log(Level.FINEST, "Dropping: {0}", frame);
                                frame.drop();
                                it.remove();
                                break;
                            }
                        }
                    }
                }
                this.frameQueue.add(this.currentFrame);
                this.currentFrame = new RenderFrame();
                if (this.frameQueue.size() > 10) {
                    paintLog.log(Level.FINEST, "Frame queue exceeded maximum size, clearing and requesting full repaint");
                    dropRenderFrames();
                    repaintAll();
                }
                paintLog.log(Level.FINEST, "Frame queue updated, frameQueue: {0}", this.frameQueue);
            }
        }
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.log(Level.FINEST, "Exiting, dirtyRects: {0}, currentFrame: {1}", new Object[]{this.dirtyRects, this.currentFrame});
        }
    }

    private void scroll(int x2, int y2, int w2, int h2, int dx, int dy) {
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.finest("rect=[" + x2 + ", " + y2 + " " + w2 + LanguageTag.PRIVATEUSE + h2 + "] delta=[" + dx + ", " + dy + "]");
        }
        int dx2 = dx + this.currentFrame.scrollDx;
        int dy2 = dy + this.currentFrame.scrollDy;
        if (Math.abs(dx2) < w2 && Math.abs(dy2) < h2) {
            int cx = dx2 >= 0 ? x2 : x2 - dx2;
            int cy = dy2 >= 0 ? y2 : y2 - dy2;
            int cw = dx2 == 0 ? w2 : w2 - Math.abs(dx2);
            int ch = dy2 == 0 ? h2 : h2 - Math.abs(dy2);
            WCRenderQueue rq = WCGraphicsManager.getGraphicsManager().createRenderQueue(new WCRectangle(0.0f, 0.0f, this.width, this.height), false);
            ByteBuffer buffer = ByteBuffer.allocate(32).order(ByteOrder.nativeOrder()).putInt(40).putInt(this.backbuffer.getID()).putInt(cx).putInt(cy).putInt(cw).putInt(ch).putInt(dx2).putInt(dy2);
            buffer.flip();
            rq.addBuffer(buffer);
            this.currentFrame.drop();
            this.currentFrame.addRenderQueue(rq);
            this.currentFrame.scrollDx = dx2;
            this.currentFrame.scrollDy = dy2;
            if (!this.dirtyRects.isEmpty()) {
                WCRectangle scrollRect = new WCRectangle(x2, y2, w2, h2);
                for (WCRectangle r2 : this.dirtyRects) {
                    if (scrollRect.contains(r2)) {
                        if (paintLog.isLoggable(Level.FINEST)) {
                            paintLog.log(Level.FINEST, "translating old dirty rect by the delta: " + ((Object) r2));
                        }
                        r2.translate(dx2, dy2);
                    }
                }
            }
        }
        addDirtyRect(new WCRectangle(x2, dy2 >= 0 ? y2 : y2 + h2 + dy2, w2, Math.abs(dy2)));
        addDirtyRect(new WCRectangle(dx2 >= 0 ? x2 : x2 + w2 + dx2, y2, Math.abs(dx2), h2 - Math.abs(dy2)));
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/WebPage$RenderFrame.class */
    private static final class RenderFrame {
        private final List<WCRenderQueue> rqList;
        private int scrollDx;
        private int scrollDy;
        private final WCRectangle enclosingRect;

        private RenderFrame() {
            this.rqList = new LinkedList();
            this.enclosingRect = new WCRectangle();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void addRenderQueue(WCRenderQueue rq) {
            if (rq.isEmpty()) {
                return;
            }
            this.rqList.add(rq);
            WCRectangle rqRect = rq.getClip();
            if (this.enclosingRect.isEmpty()) {
                this.enclosingRect.setFrame(rqRect.getX(), rqRect.getY(), rqRect.getWidth(), rqRect.getHeight());
            } else if (!rqRect.isEmpty()) {
                WCRectangle.union(this.enclosingRect, rqRect, this.enclosingRect);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public List<WCRenderQueue> getRQList() {
            return this.rqList;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public WCRectangle getEnclosingRect() {
            return this.enclosingRect;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void drop() {
            for (WCRenderQueue rq : this.rqList) {
                rq.dispose();
            }
            this.rqList.clear();
            this.enclosingRect.setFrame(0.0f, 0.0f, 0.0f, 0.0f);
            this.scrollDx = 0;
            this.scrollDy = 0;
        }

        public String toString() {
            return "RenderFrame{rqList=" + ((Object) this.rqList) + ", enclosingRect=" + ((Object) this.enclosingRect) + "}";
        }
    }

    public WebPageClient getPageClient() {
        return this.pageClient;
    }

    public void setInputMethodClient(InputMethodClient imClient) {
        this.imClient = imClient;
    }

    public void setInputMethodState(boolean state) {
        if (this.imClient != null) {
            this.imClient.activateInputMethods(state);
        }
    }

    public void addLoadListenerClient(LoadListenerClient l2) {
        if (!this.loadListenerClients.contains(l2)) {
            this.loadListenerClients.add(l2);
        }
    }

    private RenderTheme getRenderTheme() {
        return this.renderTheme;
    }

    private static RenderTheme fwkGetDefaultRenderTheme() {
        return ThemeClient.getDefaultRenderTheme();
    }

    private ScrollBarTheme getScrollBarTheme() {
        return this.scrollbarTheme;
    }

    public void setBounds(int x2, int y2, int w2, int h2) {
        lockPage();
        try {
            log.log(Level.FINE, "setBounds: " + x2 + " " + y2 + " " + w2 + " " + h2);
            if (this.isDisposed) {
                log.log(Level.FINE, "setBounds() request for a disposed web page.");
                return;
            }
            this.width = w2;
            this.height = h2;
            twkSetBounds(getPage(), 0, 0, w2, h2);
            repaintAll();
        } finally {
            unlockPage();
        }
    }

    public void setOpaque(long frameID, boolean isOpaque) {
        lockPage();
        try {
            log.log(Level.FINE, "setOpaque: " + isOpaque);
            if (this.isDisposed) {
                log.log(Level.FINE, "setOpaque() request for a disposed web page.");
            } else if (this.frames.contains(Long.valueOf(frameID))) {
                twkSetTransparent(frameID, !isOpaque);
            }
        } finally {
            unlockPage();
        }
    }

    public void setBackgroundColor(long frameID, int backgroundColor) {
        lockPage();
        try {
            log.log(Level.FINE, "setBackgroundColor: " + backgroundColor);
            if (this.isDisposed) {
                log.log(Level.FINE, "setBackgroundColor() request for a disposed web page.");
            } else if (this.frames.contains(Long.valueOf(frameID))) {
                twkSetBackgroundColor(frameID, backgroundColor);
            }
        } finally {
            unlockPage();
        }
    }

    public void setBackgroundColor(int backgroundColor) {
        lockPage();
        try {
            log.log(Level.FINE, "setBackgroundColor: " + backgroundColor + " for all frames");
            if (this.isDisposed) {
                log.log(Level.FINE, "setBackgroundColor() request for a disposed web page.");
                return;
            }
            Iterator<Long> it = this.frames.iterator();
            while (it.hasNext()) {
                long frameID = it.next().longValue();
                twkSetBackgroundColor(frameID, backgroundColor);
            }
        } finally {
            unlockPage();
        }
    }

    public void updateContent(WCRectangle toPaint) {
        lockPage();
        try {
            this.updateContentCycleID++;
            paintLog.log(Level.FINEST, "toPaint: {0}", toPaint);
            if (this.isDisposed) {
                paintLog.fine("updateContent() request for a disposed web page.");
            } else {
                updateDirty(toPaint);
                updateRendering();
            }
        } finally {
            unlockPage();
        }
    }

    public void updateRendering() {
        twkUpdateRendering(getPage());
    }

    public int getUpdateContentCycleID() {
        return this.updateContentCycleID;
    }

    public boolean isRepaintPending() {
        boolean z2;
        lockPage();
        try {
            synchronized (this.frameQueue) {
                z2 = !this.frameQueue.isEmpty();
            }
            unlockPage();
            return z2;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void print(WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        lockPage();
        try {
            WCRenderQueue rq = WCGraphicsManager.getGraphicsManager().createRenderQueue(new WCRectangle(x2, y2, w2, h2), true);
            FutureTask<Void> f2 = new FutureTask<>(() -> {
                twkUpdateContent(getPage(), rq, x2, y2, w2, h2);
            }, null);
            Invoker.getInvoker().invokeOnEventThread(f2);
            try {
                f2.get();
            } catch (InterruptedException e2) {
            } catch (ExecutionException ex) {
                throw new AssertionError(ex);
            }
            rq.decode(gc);
        } finally {
            unlockPage();
        }
    }

    public void paint(WCGraphicsContext gc, int x2, int y2, int w2, int h2) {
        lockPage();
        try {
            if (this.pageClient != null && this.pageClient.isBackBufferSupported()) {
                if (!this.backbuffer.validate(this.width, this.height)) {
                    Invoker.getInvoker().invokeOnEventThread(() -> {
                        repaintAll();
                    });
                    return;
                }
                WCGraphicsContext bgc = this.backbuffer.createGraphics();
                try {
                    paint2GC(bgc);
                    bgc.flush();
                    this.backbuffer.disposeGraphics(bgc);
                    this.backbuffer.flush(gc, x2, y2, w2, h2);
                } catch (Throwable th) {
                    this.backbuffer.disposeGraphics(bgc);
                    throw th;
                }
            } else {
                paint2GC(gc);
            }
        } finally {
            unlockPage();
        }
    }

    private void paint2GC(WCGraphicsContext gc) {
        List<RenderFrame> framesToRender;
        paintLog.finest("Entering");
        gc.setFontSmoothingType(this.fontSmoothingType);
        synchronized (this.frameQueue) {
            framesToRender = new ArrayList<>(this.frameQueue);
            this.frameQueue.clear();
        }
        paintLog.log(Level.FINEST, "Frames to render: {0}", framesToRender);
        for (RenderFrame frame : framesToRender) {
            paintLog.log(Level.FINEST, "Rendering: {0}", frame);
            for (WCRenderQueue rq : frame.getRQList()) {
                gc.saveState();
                if (rq.getClip() != null) {
                    gc.setClip(rq.getClip());
                }
                rq.decode(gc);
                gc.restoreState();
            }
        }
        paintLog.finest("Exiting");
    }

    public void dropRenderFrames() {
        lockPage();
        try {
            this.currentFrame.drop();
            synchronized (this.frameQueue) {
                RenderFrame frame = this.frameQueue.poll();
                while (frame != null) {
                    frame.drop();
                    frame = this.frameQueue.poll();
                }
            }
        } finally {
            unlockPage();
        }
    }

    public void dispatchFocusEvent(WCFocusEvent fe) {
        lockPage();
        try {
            log.log(Level.FINEST, "dispatchFocusEvent: " + ((Object) fe));
            if (this.isDisposed) {
                log.log(Level.FINE, "Focus event for a disposed web page.");
            } else {
                twkProcessFocusEvent(getPage(), fe.getID(), fe.getDirection());
            }
        } finally {
            unlockPage();
        }
    }

    public boolean dispatchKeyEvent(WCKeyEvent ke) {
        lockPage();
        try {
            log.log(Level.FINEST, "dispatchKeyEvent: " + ((Object) ke));
            if (this.isDisposed) {
                log.log(Level.FINE, "Key event for a disposed web page.");
                unlockPage();
                return false;
            }
            if (WCKeyEvent.filterEvent(ke)) {
                log.log(Level.FINEST, "filtered");
                unlockPage();
                return false;
            }
            boolean zTwkProcessKeyEvent = twkProcessKeyEvent(getPage(), ke.getType(), ke.getText(), ke.getKeyIdentifier(), ke.getWindowsVirtualKeyCode(), ke.isShiftDown(), ke.isCtrlDown(), ke.isAltDown(), ke.isMetaDown(), ke.getWhen() / 1000.0d);
            unlockPage();
            return zTwkProcessKeyEvent;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean dispatchMouseEvent(WCMouseEvent me) {
        lockPage();
        try {
            log.log(Level.FINEST, "dispatchMouseEvent: " + me.getX() + "," + me.getY());
            if (this.isDisposed) {
                log.log(Level.FINE, "Mouse event for a disposed web page.");
                unlockPage();
                return false;
            }
            boolean z2 = !isDragConfirmed() && twkProcessMouseEvent(getPage(), me.getID(), me.getButton(), me.getButtonMask(), me.getClickCount(), me.getX(), me.getY(), me.getScreenX(), me.getScreenY(), me.isShiftDown(), me.isControlDown(), me.isAltDown(), me.isMetaDown(), me.isPopupTrigger(), ((double) me.getWhen()) / 1000.0d);
            unlockPage();
            return z2;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean dispatchMouseWheelEvent(WCMouseWheelEvent me) {
        lockPage();
        try {
            log.log(Level.FINEST, "dispatchMouseWheelEvent: " + ((Object) me));
            if (this.isDisposed) {
                log.log(Level.FINE, "MouseWheel event for a disposed web page.");
                unlockPage();
                return false;
            }
            boolean zTwkProcessMouseWheelEvent = twkProcessMouseWheelEvent(getPage(), me.getX(), me.getY(), me.getScreenX(), me.getScreenY(), me.getDeltaX(), me.getDeltaY(), me.isShiftDown(), me.isControlDown(), me.isAltDown(), me.isMetaDown(), me.getWhen() / 1000.0d);
            unlockPage();
            return zTwkProcessMouseWheelEvent;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean dispatchInputMethodEvent(WCInputMethodEvent ie) {
        lockPage();
        try {
            log.log(Level.FINEST, "dispatchInputMethodEvent: " + ((Object) ie));
            if (this.isDisposed) {
                log.log(Level.FINE, "InputMethod event for a disposed web page.");
                unlockPage();
                return false;
            }
            switch (ie.getID()) {
                case 0:
                    boolean zTwkProcessInputTextChange = twkProcessInputTextChange(getPage(), ie.getComposed(), ie.getCommitted(), ie.getAttributes(), ie.getCaretPosition());
                    unlockPage();
                    return zTwkProcessInputTextChange;
                case 1:
                    boolean zTwkProcessCaretPositionChange = twkProcessCaretPositionChange(getPage(), ie.getCaretPosition());
                    unlockPage();
                    return zTwkProcessCaretPositionChange;
                default:
                    unlockPage();
                    return false;
            }
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public int dispatchDragOperation(int commandId, String[] mimeTypes, String[] values, int x2, int y2, int screenX, int screenY, int dndActionId) {
        lockPage();
        try {
            log.log(Level.FINEST, "dispatchDragOperation: " + x2 + "," + y2 + " dndCommand:" + commandId + " dndAction" + dndActionId);
            if (this.isDisposed) {
                log.log(Level.FINE, "DnD event for a disposed web page.");
                unlockPage();
                return 0;
            }
            int iTwkProcessDrag = twkProcessDrag(getPage(), commandId, mimeTypes, values, x2, y2, screenX, screenY, dndActionId);
            unlockPage();
            return iTwkProcessDrag;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void confirmStartDrag() {
        if (this.uiClient != null) {
            this.uiClient.confirmStartDrag();
        }
    }

    public boolean isDragConfirmed() {
        if (this.uiClient != null) {
            return this.uiClient.isDragConfirmed();
        }
        return false;
    }

    public int[] getClientTextLocation(int index) {
        lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientTextLocation() request for a disposed web page.");
                int[] iArr = {0, 0, 0, 0};
                unlockPage();
                return iArr;
            }
            Invoker.getInvoker().checkEventThread();
            int[] iArrTwkGetTextLocation = twkGetTextLocation(getPage(), index);
            unlockPage();
            return iArrTwkGetTextLocation;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public int getClientLocationOffset(int x2, int y2) {
        lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientLocationOffset() request for a disposed web page.");
                unlockPage();
                return 0;
            }
            Invoker.getInvoker().checkEventThread();
            int iTwkGetInsertPositionOffset = twkGetInsertPositionOffset(getPage());
            unlockPage();
            return iTwkGetInsertPositionOffset;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public int getClientInsertPositionOffset() {
        lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientInsertPositionOffset() request for a disposed web page.");
                unlockPage();
                return 0;
            }
            int iTwkGetInsertPositionOffset = twkGetInsertPositionOffset(getPage());
            unlockPage();
            return iTwkGetInsertPositionOffset;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public int getClientCommittedTextLength() {
        lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientCommittedTextOffset() request for a disposed web page.");
                unlockPage();
                return 0;
            }
            int iTwkGetCommittedTextLength = twkGetCommittedTextLength(getPage());
            unlockPage();
            return iTwkGetCommittedTextLength;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String getClientCommittedText() {
        lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientCommittedText() request for a disposed web page.");
                unlockPage();
                return "";
            }
            String strTwkGetCommittedText = twkGetCommittedText(getPage());
            unlockPage();
            return strTwkGetCommittedText;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String getClientSelectedText() {
        lockPage();
        try {
            if (this.isDisposed) {
                log.log(Level.FINE, "getClientSelectedText() request for a disposed web page.");
                unlockPage();
                return "";
            }
            String selectedText = twkGetSelectedText(getPage());
            String str = selectedText != null ? selectedText : "";
            unlockPage();
            return str;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void dispose() {
        lockPage();
        try {
            log.log(Level.FINER, "dispose");
            stop();
            dropRenderFrames();
            this.isDisposed = true;
            twkDestroyPage(this.pPage);
            this.pPage = 0L;
            Iterator<Long> it = this.frames.iterator();
            while (it.hasNext()) {
                long frameID = it.next().longValue();
                log.log(Level.FINE, "Undestroyed frame view: " + frameID);
            }
            this.frames.clear();
            if (this.backbuffer != null) {
                this.backbuffer.deref();
                this.backbuffer = null;
            }
        } finally {
            unlockPage();
        }
    }

    public String getName(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get Name: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getName() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetName = twkGetName(frameID);
            unlockPage();
            return strTwkGetName;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String getURL(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get URL: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getURL() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetURL = twkGetURL(frameID);
            unlockPage();
            return strTwkGetURL;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String getEncoding() {
        lockPage();
        try {
            log.log(Level.FINE, "Get encoding");
            if (this.isDisposed) {
                log.log(Level.FINE, "getEncoding() request for a disposed web page.");
                unlockPage();
                return null;
            }
            String strTwkGetEncoding = twkGetEncoding(getPage());
            unlockPage();
            return strTwkGetEncoding;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setEncoding(String encoding) {
        lockPage();
        try {
            log.log(Level.FINE, "Set encoding: encoding = " + encoding);
            if (this.isDisposed) {
                log.log(Level.FINE, "setEncoding() request for a disposed web page.");
                return;
            }
            if (encoding != null && !encoding.isEmpty()) {
                twkSetEncoding(getPage(), encoding);
            }
        } finally {
            unlockPage();
        }
    }

    public String getInnerText(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get inner text: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getInnerText() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetInnerText = twkGetInnerText(frameID);
            unlockPage();
            return strTwkGetInnerText;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String getRenderTree(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get render tree: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getRenderTree() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetRenderTree = twkGetRenderTree(frameID);
            unlockPage();
            return strTwkGetRenderTree;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public int getUnloadEventListenersCount(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "frame: " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "request for a disposed web page.");
                unlockPage();
                return 0;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return 0;
            }
            int iTwkGetUnloadEventListenersCount = twkGetUnloadEventListenersCount(frameID);
            unlockPage();
            return iTwkGetUnloadEventListenersCount;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void forceRepaint() {
        repaintAll();
        updateContent(new WCRectangle(0.0f, 0.0f, this.width, this.height));
    }

    public String getContentType(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get content type: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getContentType() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetContentType = twkGetContentType(frameID);
            unlockPage();
            return strTwkGetContentType;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String getTitle(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get title: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getTitle() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetTitle = twkGetTitle(frameID);
            unlockPage();
            return strTwkGetTitle;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public WCImage getIcon(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get icon: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getIcon() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String iconURL = twkGetIconURL(frameID);
            if (iconURL == null || iconURL.isEmpty()) {
                unlockPage();
                return null;
            }
            WCImage iconImage = WCGraphicsManager.getGraphicsManager().getIconImage(iconURL);
            unlockPage();
            return iconImage;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void open(long frameID, String url) {
        lockPage();
        try {
            log.log(Level.FINE, "Open URL: " + url);
            if (this.isDisposed) {
                log.log(Level.FINE, "open() request for a disposed web page.");
            } else if (this.frames.contains(Long.valueOf(frameID))) {
                if (twkIsLoading(frameID)) {
                    Invoker.getInvoker().postOnEventThread(() -> {
                        twkOpen(frameID, url);
                    });
                } else {
                    twkOpen(frameID, url);
                }
            }
        } finally {
            unlockPage();
        }
    }

    public void load(long frameID, String text, String contentType) {
        lockPage();
        try {
            log.log(Level.FINE, "Load text: " + text);
            if (text == null) {
                return;
            }
            if (this.isDisposed) {
                log.log(Level.FINE, "load() request for a disposed web page.");
            } else if (this.frames.contains(Long.valueOf(frameID))) {
                if (twkIsLoading(frameID)) {
                    Invoker.getInvoker().postOnEventThread(() -> {
                        twkLoad(frameID, text, contentType);
                    });
                } else {
                    twkLoad(frameID, text, contentType);
                }
            }
        } finally {
            unlockPage();
        }
    }

    public void stop(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Stop loading: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "cancel() request for a disposed web page.");
            } else if (this.frames.contains(Long.valueOf(frameID))) {
                String url = twkGetURL(frameID);
                String contentType = twkGetContentType(frameID);
                twkStop(frameID);
                fireLoadEvent(frameID, 6, url, contentType, 1.0d, 0);
            }
        } finally {
            unlockPage();
        }
    }

    public void stop() {
        lockPage();
        try {
            log.log(Level.FINE, "Stop loading sync");
            if (this.isDisposed) {
                log.log(Level.FINE, "stopAll() request for a disposed web page.");
            } else {
                twkStopAll(getPage());
            }
        } finally {
            unlockPage();
        }
    }

    public void refresh(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Refresh: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "refresh() request for a disposed web page.");
            } else if (this.frames.contains(Long.valueOf(frameID))) {
                twkRefresh(frameID);
            }
        } finally {
            unlockPage();
        }
    }

    public BackForwardList createBackForwardList() {
        return new BackForwardList(this);
    }

    public boolean goBack() {
        lockPage();
        try {
            log.log(Level.FINE, "Go back");
            if (this.isDisposed) {
                log.log(Level.FINE, "goBack() request for a disposed web page.");
                unlockPage();
                return false;
            }
            boolean zTwkGoBackForward = twkGoBackForward(getPage(), -1);
            unlockPage();
            return zTwkGoBackForward;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean goForward() {
        lockPage();
        try {
            log.log(Level.FINE, "Go forward");
            if (this.isDisposed) {
                log.log(Level.FINE, "goForward() request for a disposed web page.");
                unlockPage();
                return false;
            }
            boolean zTwkGoBackForward = twkGoBackForward(getPage(), 1);
            unlockPage();
            return zTwkGoBackForward;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean copy() {
        lockPage();
        try {
            log.log(Level.FINE, "Copy");
            if (this.isDisposed) {
                log.log(Level.FINE, "copy() request for a disposed web page.");
                unlockPage();
                return false;
            }
            long frameID = getMainFrame();
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return false;
            }
            boolean zTwkCopy = twkCopy(frameID);
            unlockPage();
            return zTwkCopy;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean find(String stringToFind, boolean forward, boolean wrap, boolean matchCase) {
        lockPage();
        try {
            log.log(Level.FINE, "Find in page: stringToFind = " + stringToFind + ", " + (forward ? "forward" : "backward") + (wrap ? ", wrap" : "") + (matchCase ? ", matchCase" : ""));
            if (this.isDisposed) {
                log.log(Level.FINE, "find() request for a disposed web page.");
                unlockPage();
                return false;
            }
            boolean zTwkFindInPage = twkFindInPage(getPage(), stringToFind, forward, wrap, matchCase);
            unlockPage();
            return zTwkFindInPage;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean find(long frameID, String stringToFind, boolean forward, boolean wrap, boolean matchCase) {
        lockPage();
        try {
            log.log(Level.FINE, "Find in frame: stringToFind = " + stringToFind + ", " + (forward ? "forward" : "backward") + (wrap ? ", wrap" : "") + (matchCase ? ", matchCase" : ""));
            if (this.isDisposed) {
                log.log(Level.FINE, "find() request for a disposed web page.");
                unlockPage();
                return false;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return false;
            }
            boolean zTwkFindInFrame = twkFindInFrame(frameID, stringToFind, forward, wrap, matchCase);
            unlockPage();
            return zTwkFindInFrame;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void overridePreference(String key, String value) {
        lockPage();
        try {
            twkOverridePreference(getPage(), key, value);
        } finally {
            unlockPage();
        }
    }

    public void resetToConsistentStateBeforeTesting() {
        lockPage();
        try {
            twkResetToConsistentStateBeforeTesting(getPage());
        } finally {
            unlockPage();
        }
    }

    public float getZoomFactor(boolean textOnly) {
        lockPage();
        try {
            log.log(Level.FINE, "Get zoom factor, textOnly=" + textOnly);
            if (this.isDisposed) {
                log.log(Level.FINE, "getZoomFactor() request for a disposed web page.");
                unlockPage();
                return 1.0f;
            }
            long frameID = getMainFrame();
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return 1.0f;
            }
            float fTwkGetZoomFactor = twkGetZoomFactor(frameID, textOnly);
            unlockPage();
            return fTwkGetZoomFactor;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setZoomFactor(float zoomFactor, boolean textOnly) {
        lockPage();
        try {
            log.fine(String.format("Set zoom factor %.2f, textOnly=%b", Float.valueOf(zoomFactor), Boolean.valueOf(textOnly)));
            if (this.isDisposed) {
                log.log(Level.FINE, "setZoomFactor() request for a disposed web page.");
                return;
            }
            long frameID = getMainFrame();
            if (frameID == 0 || !this.frames.contains(Long.valueOf(frameID))) {
                return;
            }
            twkSetZoomFactor(frameID, zoomFactor, textOnly);
        } finally {
            unlockPage();
        }
    }

    public void setFontSmoothingType(int fontSmoothingType) {
        this.fontSmoothingType = fontSmoothingType;
        repaintAll();
    }

    public void reset(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Reset: frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "reset() request for a disposed web page.");
            } else {
                if (frameID == 0 || !this.frames.contains(Long.valueOf(frameID))) {
                    return;
                }
                twkReset(frameID);
            }
        } finally {
            unlockPage();
        }
    }

    public Object executeScript(long frameID, String script) throws JSException {
        lockPage();
        try {
            log.log(Level.FINE, "execute script: \"" + script + "\" in frame = " + frameID);
            if (this.isDisposed) {
                log.log(Level.FINE, "executeScript() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (frameID == 0 || !this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            Object objTwkExecuteScript = twkExecuteScript(frameID, script);
            unlockPage();
            return objTwkExecuteScript;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public long getMainFrame() {
        lockPage();
        try {
            log.log(Level.FINER, "getMainFrame: page = " + this.pPage);
            if (this.isDisposed) {
                log.log(Level.FINE, "getMainFrame() request for a disposed web page.");
                unlockPage();
                return 0L;
            }
            long mainFrameID = twkGetMainFrame(getPage());
            log.log(Level.FINER, "Main frame = " + mainFrameID);
            this.frames.add(Long.valueOf(mainFrameID));
            unlockPage();
            return mainFrameID;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public long getParentFrame(long childID) {
        lockPage();
        try {
            log.log(Level.FINE, "getParentFrame: child = " + childID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getParentFrame() request for a disposed web page.");
                unlockPage();
                return 0L;
            }
            if (!this.frames.contains(Long.valueOf(childID))) {
                unlockPage();
                return 0L;
            }
            long jTwkGetParentFrame = twkGetParentFrame(childID);
            unlockPage();
            return jTwkGetParentFrame;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public List<Long> getChildFrames(long parentID) {
        lockPage();
        try {
            log.log(Level.FINE, "getChildFrames: parent = " + parentID);
            if (this.isDisposed) {
                log.log(Level.FINE, "getChildFrames() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(parentID))) {
                unlockPage();
                return null;
            }
            long[] children = twkGetChildFrames(parentID);
            List<Long> childrenList = new LinkedList<>();
            for (long child : children) {
                childrenList.add(Long.valueOf(child));
            }
            unlockPage();
            return childrenList;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public WCRectangle getVisibleRect(long frameID) {
        lockPage();
        try {
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            int[] arr = twkGetVisibleRect(frameID);
            if (arr == null) {
                unlockPage();
                return null;
            }
            WCRectangle wCRectangle = new WCRectangle(arr[0], arr[1], arr[2], arr[3]);
            unlockPage();
            return wCRectangle;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void scrollToPosition(long frameID, WCPoint p2) {
        lockPage();
        try {
            if (!this.frames.contains(Long.valueOf(frameID))) {
                return;
            }
            twkScrollToPosition(frameID, p2.getIntX(), p2.getIntY());
        } finally {
            unlockPage();
        }
    }

    public WCSize getContentSize(long frameID) {
        lockPage();
        try {
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            int[] arr = twkGetContentSize(frameID);
            if (arr == null) {
                unlockPage();
                return null;
            }
            WCSize wCSize = new WCSize(arr[0], arr[1]);
            unlockPage();
            return wCSize;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public Document getDocument(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "getDocument");
            if (this.isDisposed) {
                log.log(Level.FINE, "getDocument() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            Document documentTwkGetDocument = twkGetDocument(frameID);
            unlockPage();
            return documentTwkGetDocument;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public Element getOwnerElement(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "getOwnerElement");
            if (this.isDisposed) {
                log.log(Level.FINE, "getOwnerElement() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            Element elementTwkGetOwnerElement = twkGetOwnerElement(frameID);
            unlockPage();
            return elementTwkGetOwnerElement;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean executeCommand(String command, String value) {
        lockPage();
        try {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "command: [{0}], value: [{1}]", new Object[]{command, value});
            }
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                unlockPage();
                return false;
            }
            boolean result = twkExecuteCommand(getPage(), command, value);
            log.log(Level.FINE, "result: [{0}]", Boolean.valueOf(result));
            unlockPage();
            return result;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean queryCommandEnabled(String command) {
        lockPage();
        try {
            log.log(Level.FINE, "command: [{0}]", command);
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                unlockPage();
                return false;
            }
            boolean result = twkQueryCommandEnabled(getPage(), command);
            log.log(Level.FINE, "result: [{0}]", Boolean.valueOf(result));
            unlockPage();
            return result;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean queryCommandState(String command) {
        lockPage();
        try {
            log.log(Level.FINE, "command: [{0}]", command);
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                unlockPage();
                return false;
            }
            boolean result = twkQueryCommandState(getPage(), command);
            log.log(Level.FINE, "result: [{0}]", Boolean.valueOf(result));
            unlockPage();
            return result;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public String queryCommandValue(String command) {
        lockPage();
        try {
            log.log(Level.FINE, "command: [{0}]", command);
            if (this.isDisposed) {
                log.log(Level.FINE, "Web page is already disposed");
                unlockPage();
                return null;
            }
            String result = twkQueryCommandValue(getPage(), command);
            log.log(Level.FINE, "result: [{0}]", result);
            unlockPage();
            return result;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean isEditable() {
        lockPage();
        try {
            log.log(Level.FINE, "isEditable");
            if (this.isDisposed) {
                log.log(Level.FINE, "isEditable() request for a disposed web page.");
                unlockPage();
                return false;
            }
            boolean zTwkIsEditable = twkIsEditable(getPage());
            unlockPage();
            return zTwkIsEditable;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setEditable(boolean editable) {
        lockPage();
        try {
            log.log(Level.FINE, "setEditable");
            if (this.isDisposed) {
                log.log(Level.FINE, "setEditable() request for a disposed web page.");
            } else {
                twkSetEditable(getPage(), editable);
            }
        } finally {
            unlockPage();
        }
    }

    public String getHtml(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "getHtml");
            if (this.isDisposed) {
                log.log(Level.FINE, "getHtml() request for a disposed web page.");
                unlockPage();
                return null;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return null;
            }
            String strTwkGetHtml = twkGetHtml(frameID);
            unlockPage();
            return strTwkGetHtml;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public int beginPrinting(float width, float height) {
        lockPage();
        try {
            if (this.isDisposed) {
                log.warning("beginPrinting() called for a disposed web page.");
                unlockPage();
                return 0;
            }
            AtomicReference<Integer> retVal = new AtomicReference<>(0);
            CountDownLatch l2 = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
                try {
                    int nPages = twkBeginPrinting(getPage(), width, height);
                    retVal.set(Integer.valueOf(nPages));
                    l2.countDown();
                } catch (Throwable th) {
                    l2.countDown();
                    throw th;
                }
            });
            try {
                l2.await();
                int iIntValue = retVal.get().intValue();
                unlockPage();
                return iIntValue;
            } catch (InterruptedException e2) {
                throw new RuntimeException(e2);
            }
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void endPrinting() {
        lockPage();
        try {
            if (this.isDisposed) {
                log.warning("endPrinting() called for a disposed web page.");
                return;
            }
            CountDownLatch l2 = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
                try {
                    twkEndPrinting(getPage());
                } finally {
                    l2.countDown();
                }
            });
            try {
                l2.await();
            } catch (InterruptedException e2) {
                throw new RuntimeException(e2);
            }
        } finally {
            unlockPage();
        }
    }

    public void print(WCGraphicsContext gc, int pageNumber, float width) {
        WCRenderQueue rq;
        lockPage();
        try {
            if (this.isDisposed) {
                log.warning("print() called for a disposed web page.");
                return;
            }
            rq = WCGraphicsManager.getGraphicsManager().createRenderQueue(null, true);
            CountDownLatch l2 = new CountDownLatch(1);
            Invoker.getInvoker().invokeOnEventThread(() -> {
                try {
                    twkPrint(getPage(), rq, pageNumber, width);
                    l2.countDown();
                } catch (Throwable th) {
                    l2.countDown();
                    throw th;
                }
            });
            l2.await();
            rq.decode(gc);
        } catch (InterruptedException e2) {
            rq.dispose();
        } finally {
            unlockPage();
        }
    }

    public int getPageHeight() {
        return getFrameHeight(getMainFrame());
    }

    public int getFrameHeight(long frameID) {
        lockPage();
        try {
            log.log(Level.FINE, "Get page height");
            if (this.isDisposed) {
                log.log(Level.FINE, "getFrameHeight() request for a disposed web page.");
                unlockPage();
                return 0;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return 0;
            }
            int height = twkGetFrameHeight(frameID);
            log.log(Level.FINE, "Height = " + height);
            unlockPage();
            return height;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public float adjustFrameHeight(long frameID, float oldTop, float oldBottom, float bottomLimit) {
        lockPage();
        try {
            log.log(Level.FINE, "Adjust page height");
            if (this.isDisposed) {
                log.log(Level.FINE, "adjustFrameHeight() request for a disposed web page.");
                unlockPage();
                return 0.0f;
            }
            if (!this.frames.contains(Long.valueOf(frameID))) {
                unlockPage();
                return 0.0f;
            }
            float fTwkAdjustFrameHeight = twkAdjustFrameHeight(frameID, oldTop, oldBottom, bottomLimit);
            unlockPage();
            return fTwkAdjustFrameHeight;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public boolean getUsePageCache() {
        lockPage();
        try {
            boolean zTwkGetUsePageCache = twkGetUsePageCache(getPage());
            unlockPage();
            return zTwkGetUsePageCache;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setUsePageCache(boolean usePageCache) {
        lockPage();
        try {
            twkSetUsePageCache(getPage(), usePageCache);
        } finally {
            unlockPage();
        }
    }

    public boolean getDeveloperExtrasEnabled() {
        lockPage();
        try {
            boolean result = twkGetDeveloperExtrasEnabled(getPage());
            log.log(Level.FINE, "Getting developerExtrasEnabled, result: [{0}]", Boolean.valueOf(result));
            unlockPage();
            return result;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setDeveloperExtrasEnabled(boolean enabled) {
        lockPage();
        try {
            log.log(Level.FINE, "Setting developerExtrasEnabled, value: [{0}]", Boolean.valueOf(enabled));
            twkSetDeveloperExtrasEnabled(getPage(), enabled);
        } finally {
            unlockPage();
        }
    }

    public boolean isJavaScriptEnabled() {
        lockPage();
        try {
            boolean zTwkIsJavaScriptEnabled = twkIsJavaScriptEnabled(getPage());
            unlockPage();
            return zTwkIsJavaScriptEnabled;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setJavaScriptEnabled(boolean enable) {
        lockPage();
        try {
            twkSetJavaScriptEnabled(getPage(), enable);
        } finally {
            unlockPage();
        }
    }

    public boolean isContextMenuEnabled() {
        lockPage();
        try {
            boolean zTwkIsContextMenuEnabled = twkIsContextMenuEnabled(getPage());
            unlockPage();
            return zTwkIsContextMenuEnabled;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setContextMenuEnabled(boolean enable) {
        lockPage();
        try {
            twkSetContextMenuEnabled(getPage(), enable);
        } finally {
            unlockPage();
        }
    }

    public void setUserStyleSheetLocation(String url) {
        lockPage();
        try {
            twkSetUserStyleSheetLocation(getPage(), url);
        } finally {
            unlockPage();
        }
    }

    public String getUserAgent() {
        lockPage();
        try {
            String strTwkGetUserAgent = twkGetUserAgent(getPage());
            unlockPage();
            return strTwkGetUserAgent;
        } catch (Throwable th) {
            unlockPage();
            throw th;
        }
    }

    public void setUserAgent(String userAgent) {
        lockPage();
        try {
            twkSetUserAgent(getPage(), userAgent);
        } finally {
            unlockPage();
        }
    }

    public void setLocalStorageDatabasePath(String path) {
        lockPage();
        try {
            twkSetLocalStorageDatabasePath(getPage(), path);
        } finally {
            unlockPage();
        }
    }

    public void setLocalStorageEnabled(boolean enabled) {
        lockPage();
        try {
            twkSetLocalStorageEnabled(getPage(), enabled);
        } finally {
            unlockPage();
        }
    }

    public void connectInspectorFrontend() {
        lockPage();
        try {
            log.log(Level.FINE, "Connecting inspector frontend");
            twkConnectInspectorFrontend(getPage());
        } finally {
            unlockPage();
        }
    }

    public void disconnectInspectorFrontend() {
        lockPage();
        try {
            log.log(Level.FINE, "Disconnecting inspector frontend");
            twkDisconnectInspectorFrontend(getPage());
        } finally {
            unlockPage();
        }
    }

    public void dispatchInspectorMessageFromFrontend(String message) {
        lockPage();
        try {
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "Dispatching inspector message from frontend, message: [{0}]", message);
            }
            twkDispatchInspectorMessageFromFrontend(getPage(), message);
        } finally {
            unlockPage();
        }
    }

    private void fwkFrameCreated(long frameID) {
        log.log(Level.FINE, "Frame created: frame = " + frameID);
        if (this.frames.contains(Long.valueOf(frameID))) {
            log.log(Level.FINE, "Error in fwkFrameCreated: frame is already in frames");
        } else {
            this.frames.add(Long.valueOf(frameID));
        }
    }

    private void fwkFrameDestroyed(long frameID) {
        log.log(Level.FINE, "Frame destroyed: frame = " + frameID);
        if (!this.frames.contains(Long.valueOf(frameID))) {
            log.log(Level.FINE, "Error in fwkFrameDestroyed: frame is not found in frames");
        } else {
            this.frames.remove(Long.valueOf(frameID));
        }
    }

    private void fwkRepaint(int x2, int y2, int w2, int h2) {
        lockPage();
        try {
            if (paintLog.isLoggable(Level.FINEST)) {
                paintLog.log(Level.FINEST, "x: {0}, y: {1}, w: {2}, h: {3}", new Object[]{Integer.valueOf(x2), Integer.valueOf(y2), Integer.valueOf(w2), Integer.valueOf(h2)});
            }
            addDirtyRect(new WCRectangle(x2, y2, w2, h2));
        } finally {
            unlockPage();
        }
    }

    private void fwkScroll(int x2, int y2, int w2, int h2, int deltaX, int deltaY) {
        if (paintLog.isLoggable(Level.FINEST)) {
            paintLog.finest("Scroll: " + x2 + " " + y2 + " " + w2 + " " + h2 + Constants.INDENT + deltaX + " " + deltaY);
        }
        if (this.pageClient == null || !this.pageClient.isBackBufferSupported()) {
            paintLog.finest("blit scrolling is switched off");
        } else {
            scroll(x2, y2, w2, h2, deltaX, deltaY);
        }
    }

    private void fwkTransferFocus(boolean forward) {
        log.log(Level.FINER, "Transfer focus " + (forward ? "forward" : "backward"));
        if (this.pageClient != null) {
            this.pageClient.transferFocus(forward);
        }
    }

    private void fwkSetCursor(long id) {
        log.log(Level.FINER, "Set cursor: " + id);
        if (this.pageClient != null) {
            this.pageClient.setCursor(id);
        }
    }

    private void fwkSetFocus(boolean focus) {
        log.log(Level.FINER, "Set focus: " + (focus ? "true" : "false"));
        if (this.pageClient != null) {
            this.pageClient.setFocus(focus);
        }
    }

    private void fwkSetTooltip(String tooltip) {
        log.log(Level.FINER, "Set tooltip: " + tooltip);
        if (this.pageClient != null) {
            this.pageClient.setTooltip(tooltip);
        }
    }

    private void fwkPrint() {
        log.log(Level.FINER, "Print");
        if (this.uiClient != null) {
            this.uiClient.print();
        }
    }

    private void fwkSetRequestURL(long pFrame, int id, String url) {
        log.log(Level.FINER, "Set request URL: id = " + id + ", url = " + url);
        synchronized (this.requestURLs) {
            this.requestURLs.put(Integer.valueOf(id), url);
        }
    }

    private void fwkRemoveRequestURL(long pFrame, int id) {
        log.log(Level.FINER, "Set request URL: id = " + id);
        synchronized (this.requestURLs) {
            this.requestURLs.remove(Integer.valueOf(id));
            this.requestStarted.remove(Integer.valueOf(id));
        }
    }

    private WebPage fwkCreateWindow(boolean menu, boolean status, boolean toolbar, boolean resizable) {
        log.log(Level.FINER, "Create window");
        if (this.uiClient != null) {
            return this.uiClient.createPage(menu, status, toolbar, resizable);
        }
        return null;
    }

    private void fwkShowWindow() {
        log.log(Level.FINER, "Show window");
        if (this.uiClient != null) {
            this.uiClient.showView();
        }
    }

    private void fwkCloseWindow() {
        log.log(Level.FINER, "Close window");
        if (permitCloseWindowAction() && this.uiClient != null) {
            this.uiClient.closePage();
        }
    }

    private WCRectangle fwkGetWindowBounds() {
        WCRectangle bounds;
        log.log(Level.FINE, "Get window bounds");
        if (this.uiClient != null && (bounds = this.uiClient.getViewBounds()) != null) {
            return bounds;
        }
        return fwkGetPageBounds();
    }

    private void fwkSetWindowBounds(int x2, int y2, int w2, int h2) {
        log.log(Level.FINER, "Set window bounds: " + x2 + " " + y2 + " " + w2 + " " + h2);
        if (this.uiClient != null) {
            this.uiClient.setViewBounds(new WCRectangle(x2, y2, w2, h2));
        }
    }

    private WCRectangle fwkGetPageBounds() {
        log.log(Level.FINER, "Get page bounds");
        return new WCRectangle(0.0f, 0.0f, this.width, this.height);
    }

    private void fwkSetScrollbarsVisible(boolean visible) {
    }

    private void fwkSetStatusbarText(String text) {
        log.log(Level.FINER, "Set statusbar text: " + text);
        if (this.uiClient != null) {
            this.uiClient.setStatusbarText(text);
        }
    }

    private String[] fwkChooseFile(String initialFileName, boolean multiple, String mimeFilters) {
        log.log(Level.FINER, "Choose file, initial=" + initialFileName);
        if (this.uiClient != null) {
            return this.uiClient.chooseFile(initialFileName, multiple, mimeFilters);
        }
        return null;
    }

    private void fwkStartDrag(Object image, int imageOffsetX, int imageOffsetY, int eventPosX, int eventPosY, String[] mimeTypes, Object[] values, boolean isImageSource) {
        log.log(Level.FINER, "Start drag: ");
        if (this.uiClient != null) {
            this.uiClient.startDrag(WCImage.getImage(image), imageOffsetX, imageOffsetY, eventPosX, eventPosY, mimeTypes, values, isImageSource);
        }
    }

    private WCPoint fwkScreenToWindow(WCPoint ptScreen) {
        log.log(Level.FINER, "fwkScreenToWindow");
        if (this.pageClient != null) {
            return this.pageClient.screenToWindow(ptScreen);
        }
        return ptScreen;
    }

    private WCPoint fwkWindowToScreen(WCPoint ptWindow) {
        log.log(Level.FINER, "fwkWindowToScreen");
        if (this.pageClient != null) {
            return this.pageClient.windowToScreen(ptWindow);
        }
        return ptWindow;
    }

    private void fwkAlert(String text) {
        log.log(Level.FINE, "JavaScript alert(): text = " + text);
        if (this.uiClient != null) {
            this.uiClient.alert(text);
        }
    }

    private boolean fwkConfirm(String text) {
        log.log(Level.FINE, "JavaScript confirm(): text = " + text);
        if (this.uiClient != null) {
            return this.uiClient.confirm(text);
        }
        return false;
    }

    private String fwkPrompt(String text, String defaultValue) {
        log.log(Level.FINE, "JavaScript prompt(): text = " + text + ", default = " + defaultValue);
        if (this.uiClient != null) {
            return this.uiClient.prompt(text, defaultValue);
        }
        return null;
    }

    private boolean fwkCanRunBeforeUnloadConfirmPanel() {
        log.log(Level.FINE, "JavaScript canRunBeforeUnloadConfirmPanel()");
        if (this.uiClient != null) {
            return this.uiClient.canRunBeforeUnloadConfirmPanel();
        }
        return false;
    }

    private boolean fwkRunBeforeUnloadConfirmPanel(String message) {
        log.log(Level.FINE, "JavaScript runBeforeUnloadConfirmPanel(): message = " + message);
        if (this.uiClient != null) {
            return this.uiClient.runBeforeUnloadConfirmPanel(message);
        }
        return false;
    }

    private void fwkAddMessageToConsole(String message, int lineNumber, String sourceId) {
        log.log(Level.FINE, "fwkAddMessageToConsole(): message = " + message + ", lineNumber = " + lineNumber + ", sourceId = " + sourceId);
        if (this.pageClient != null) {
            this.pageClient.addMessageToConsole(message, lineNumber, sourceId);
        }
    }

    private void fwkFireLoadEvent(long frameID, int state, String url, String contentType, double progress, int errorCode) {
        log.log(Level.FINER, "Load event: pFrame = " + frameID + ", state = " + state + ", url = " + url + ", contenttype=" + contentType + ", progress = " + progress + ", error = " + errorCode);
        fireLoadEvent(frameID, state, url, contentType, progress, errorCode);
    }

    private void fwkFireResourceLoadEvent(long frameID, int state, int id, String contentType, double progress, int errorCode) {
        log.log(Level.FINER, "Resource load event: pFrame = " + frameID + ", state = " + state + ", id = " + id + ", contenttype=" + contentType + ", progress = " + progress + ", error = " + errorCode);
        String url = this.requestURLs.get(Integer.valueOf(id));
        if (url == null) {
            log.log(Level.FINE, "Error in fwkFireResourceLoadEvent: unknown request id " + id);
            return;
        }
        int eventState = state;
        if (state == 20) {
            if (this.requestStarted.contains(Integer.valueOf(id))) {
                eventState = 21;
            } else {
                this.requestStarted.add(Integer.valueOf(id));
            }
        }
        fireResourceLoadEvent(frameID, eventState, url, contentType, progress, errorCode);
    }

    private boolean fwkPermitNavigateAction(long pFrame, String url) {
        log.log(Level.FINE, "Policy: permit NAVIGATE: pFrame = " + pFrame + ", url = " + url);
        if (this.policyClient != null) {
            return this.policyClient.permitNavigateAction(pFrame, str2url(url));
        }
        return true;
    }

    private boolean fwkPermitRedirectAction(long pFrame, String url) {
        log.log(Level.FINE, "Policy: permit REDIRECT: pFrame = " + pFrame + ", url = " + url);
        if (this.policyClient != null) {
            return this.policyClient.permitRedirectAction(pFrame, str2url(url));
        }
        return true;
    }

    private boolean fwkPermitAcceptResourceAction(long pFrame, String url) {
        log.log(Level.FINE, "Policy: permit ACCEPT_RESOURCE: pFrame + " + pFrame + ", url = " + url);
        if (this.policyClient != null) {
            return this.policyClient.permitAcceptResourceAction(pFrame, str2url(url));
        }
        return true;
    }

    private boolean fwkPermitSubmitDataAction(long pFrame, String url, String httpMethod, boolean isSubmit) {
        log.log(Level.FINE, "Policy: permit " + (isSubmit ? "" : "RE") + "SUBMIT_DATA: pFrame = " + pFrame + ", url = " + url + ", httpMethod = " + httpMethod);
        if (this.policyClient != null) {
            if (isSubmit) {
                return this.policyClient.permitSubmitDataAction(pFrame, str2url(url), httpMethod);
            }
            return this.policyClient.permitResubmitDataAction(pFrame, str2url(url), httpMethod);
        }
        return true;
    }

    private boolean fwkPermitEnableScriptsAction(long pFrame, String url) {
        log.log(Level.FINE, "Policy: permit ENABLE_SCRIPTS: pFrame + " + pFrame + ", url = " + url);
        if (this.policyClient != null) {
            return this.policyClient.permitEnableScriptsAction(pFrame, str2url(url));
        }
        return true;
    }

    private boolean fwkPermitNewWindowAction(long pFrame, String url) {
        log.log(Level.FINE, "Policy: permit NEW_PAGE: pFrame = " + pFrame + ", url = " + url);
        if (this.policyClient != null) {
            return this.policyClient.permitNewPageAction(pFrame, str2url(url));
        }
        return true;
    }

    private boolean permitCloseWindowAction() {
        log.log(Level.FINE, "Policy: permit CLOSE_PAGE");
        if (this.policyClient != null) {
            return this.policyClient.permitClosePageAction(getMainFrame());
        }
        return true;
    }

    private void fwkRepaintAll() {
        log.log(Level.FINE, "Repainting the entire page");
        repaintAll();
    }

    private boolean fwkSendInspectorMessageToFrontend(String message) {
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Sending inspector message to frontend, message: [{0}]", message);
        }
        boolean result = false;
        if (this.inspectorClient != null) {
            log.log(Level.FINE, "Invoking inspector client");
            result = this.inspectorClient.sendMessageToFrontend(message);
        }
        if (log.isLoggable(Level.FINE)) {
            log.log(Level.FINE, "Result: [{0}]", Boolean.valueOf(result));
        }
        return result;
    }

    public static int getWorkerThreadCount() {
        return twkWorkerThreadCount();
    }

    private void fwkDidClearWindowObject(long pContext, long pWindowObject) {
        if (this.pageClient != null) {
            this.pageClient.didClearWindowObject(pContext, pWindowObject);
        }
    }

    private URL str2url(String url) {
        try {
            return URLs.newURL(url);
        } catch (MalformedURLException ex) {
            log.log(Level.FINE, "Exception while converting \"" + url + "\" to URL", (Throwable) ex);
            return null;
        }
    }

    private void fireLoadEvent(long frameID, int state, String url, String contentType, double progress, int errorCode) {
        for (LoadListenerClient l2 : this.loadListenerClients) {
            l2.dispatchLoadEvent(frameID, state, url, contentType, progress, errorCode);
        }
    }

    private void fireResourceLoadEvent(long frameID, int state, String url, String contentType, double progress, int errorCode) {
        for (LoadListenerClient l2 : this.loadListenerClients) {
            l2.dispatchResourceLoadEvent(frameID, state, url, contentType, progress, errorCode);
        }
    }

    private void repaintAll() {
        this.dirtyRects.clear();
        addDirtyRect(new WCRectangle(0.0f, 0.0f, this.width, this.height));
    }

    int test_getFramesCount() {
        return this.frames.size();
    }
}
