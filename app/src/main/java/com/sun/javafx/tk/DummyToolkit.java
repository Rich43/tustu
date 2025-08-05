package com.sun.javafx.tk;

import com.sun.glass.ui.CommonDialogs;
import com.sun.javafx.embed.HostInterface;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.perf.PerformanceTracker;
import com.sun.javafx.runtime.async.AsyncOperation;
import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.scene.text.HitInfo;
import com.sun.javafx.scene.text.TextLayoutFactory;
import com.sun.javafx.tk.Toolkit;
import com.sun.scenario.DelayedRunnable;
import com.sun.scenario.animation.AbstractMasterTimer;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import java.io.File;
import java.io.InputStream;
import java.security.AccessControlContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;
import javafx.geometry.Dimension2D;
import javafx.scene.image.Image;
import javafx.scene.input.Dragboard;
import javafx.scene.input.InputMethodRequests;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.RadialGradient;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/DummyToolkit.class */
public final class DummyToolkit extends Toolkit {
    @Override // com.sun.javafx.tk.Toolkit
    public boolean init() {
        return true;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean canStartNestedEventLoop() {
        return false;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object enterNestedEventLoop(Object key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void exitNestedEventLoop(Object key, Object rval) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKStage createTKStage(Window peerWindow, boolean securityDialog, StageStyle stageStyle, boolean primary, Modality modality, TKStage owner, boolean rtl, AccessControlContext acc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKStage createTKPopupStage(Window peerWindow, StageStyle popupStyle, TKStage owner, AccessControlContext acc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKStage createTKEmbeddedStage(HostInterface host, AccessControlContext acc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public AppletWindow createAppletWindow(long parent, String serverName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void closeAppletWindow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKSystemMenu getSystemMenu() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ImageLoader loadImage(String url, int width, int height, boolean preserveRatio, boolean smooth) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ImageLoader loadImage(InputStream stream, int width, int height, boolean preserveRatio, boolean smooth) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public AsyncOperation loadImageAsync(AsyncOperationListener<? extends ImageLoader> listener, String url, int width, int height, boolean preserveRatio, boolean smooth) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ImageLoader loadPlatformImage(Object platformImage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PlatformImage createPlatformImage(int w2, int h2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void startup(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void defer(Runnable runnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Future addRenderJob(RenderJob rj) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Map<Object, Object> getContextMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getRefreshRate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void setAnimationRunnable(DelayedRunnable animationRunnable) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PerformanceTracker getPerformanceTracker() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PerformanceTracker createPerformanceTracker() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void waitFor(Toolkit.Task t2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createColorPaint(Color paint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createLinearGradientPaint(LinearGradient paint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createRadialGradientPaint(RadialGradient paint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    protected Object createImagePatternPaint(ImagePattern paint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void accumulateStrokeBounds(Shape shape, float[] bbox, StrokeType type, double strokewidth, StrokeLineCap cap, StrokeLineJoin join, float miterLimit, BaseTransform tx) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean strokeContains(Shape shape, double x2, double y2, StrokeType type, double strokewidth, StrokeLineCap cap, StrokeLineJoin join, float miterLimit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Shape createStrokedShape(Shape shape, StrokeType pgtype, double strokewidth, StrokeLineCap pgcap, StrokeLineJoin pgjoin, float miterLimit, float[] dashArray, float dashOffset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getKeyCodeForChar(String character) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Dimension2D getBestCursorSize(int preferredWidth, int preferredHeight) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getMaximumCursorColors() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public PathElement[] convertShapeToFXPath(Object shape) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public HitInfo convertHitInfoToFX(Object hit) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Filterable toFilterable(Image img) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public FilterContext getFilterContext(Object config) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isForwardTraversalKey(KeyEvent e2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isBackwardTraversalKey(KeyEvent e2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean isNestedLoopRunning() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public AbstractMasterTimer getMasterTimer() {
        return null;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public FontLoader getFontLoader() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TextLayoutFactory getTextLayoutFactory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object createSVGPathObject(SVGPath svgpath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Path2D createSVGPath2D(SVGPath svgpath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public boolean imageContains(Object image, float x2, float y2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKClipboard getSystemClipboard() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public TKClipboard getNamedClipboard(String name) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ScreenConfigurationAccessor setScreenConfigurationListener(TKScreenConfigurationListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object getPrimaryScreen() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public List<?> getScreens() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public ScreenConfigurationAccessor getScreenConfigurationAccessor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void registerDragGestureListener(TKScene s2, Set<TransferMode> tms, TKDragGestureListener l2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void startDrag(TKScene scene, Set<TransferMode> tms, TKDragSourceListener l2, Dragboard dragboard) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void enableDrop(TKScene s2, TKDropTargetListener l2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void installInputMethodRequests(TKScene scene, InputMethodRequests requests) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Object renderToImage(Toolkit.ImageRenderingContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public KeyCode getPlatformShortcutKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public Optional<Boolean> isKeyLocked(KeyCode keyCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public CommonDialogs.FileChooserResult showFileChooser(TKStage ownerWindow, String title, File initialDirectory, String initialFileName, FileChooserType fileChooserType, List<FileChooser.ExtensionFilter> extensionFilters, FileChooser.ExtensionFilter selectedFilter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public File showDirectoryChooser(TKStage ownerWindow, String title, File initialDirectory) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override // com.sun.javafx.tk.Toolkit
    public long getMultiClickTime() {
        return 0L;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getMultiClickMaxX() {
        return 0;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public int getMultiClickMaxY() {
        return 0;
    }

    @Override // com.sun.javafx.tk.Toolkit
    public void requestNextPulse() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
