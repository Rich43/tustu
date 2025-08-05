package com.sun.javafx.webkit.theme;

import com.sun.javafx.webkit.Accessor;
import com.sun.javafx.webkit.theme.RenderThemeImpl;
import com.sun.webkit.graphics.Ref;
import com.sun.webkit.graphics.ScrollBarTheme;
import com.sun.webkit.graphics.WCGraphicsContext;
import com.sun.webkit.graphics.WCSize;
import java.lang.ref.WeakReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.geometry.Bounds;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollBar;

/* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ScrollBarThemeImpl.class */
public final class ScrollBarThemeImpl extends ScrollBarTheme {
    private static final Logger log;
    private WeakReference<ScrollBar> testSBRef = new WeakReference<>(null);
    private boolean thicknessInitialized = false;
    private final Accessor accessor;
    private final RenderThemeImpl.Pool<ScrollBarWidget> pool;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ScrollBarThemeImpl.class.desiredAssertionStatus();
        log = Logger.getLogger(ScrollBarThemeImpl.class.getName());
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ScrollBarThemeImpl$ScrollBarWidget.class */
    private final class ScrollBarWidget extends ScrollBar implements RenderThemeImpl.Widget {
        private ScrollBarWidget() {
            setOrientation(Orientation.VERTICAL);
            setMin(0.0d);
            setManaged(false);
        }

        @Override // javafx.scene.layout.Region, javafx.scene.Parent, javafx.scene.Node
        public void impl_updatePeer() {
            super.impl_updatePeer();
            ScrollBarThemeImpl.this.initializeThickness();
        }

        @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.Widget
        public RenderThemeImpl.WidgetType getType() {
            return RenderThemeImpl.WidgetType.SCROLLBAR;
        }

        @Override // javafx.scene.control.Control, javafx.scene.Parent
        protected void layoutChildren() {
            super.layoutChildren();
            ScrollBarThemeImpl.this.initializeThickness();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/webkit/theme/ScrollBarThemeImpl$ScrollBarRef.class */
    private static final class ScrollBarRef extends Ref {
        private final WeakReference<ScrollBarWidget> sbRef;

        private ScrollBarRef(ScrollBarWidget sb) {
            this.sbRef = new WeakReference<>(sb);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Control asControl() {
            return this.sbRef.get();
        }
    }

    public ScrollBarThemeImpl(final Accessor accessor) {
        this.accessor = accessor;
        this.pool = new RenderThemeImpl.Pool<>(sb -> {
            accessor.removeChild(sb);
        }, ScrollBarWidget.class);
        accessor.addViewListener(new RenderThemeImpl.ViewListener(this.pool, accessor) { // from class: com.sun.javafx.webkit.theme.ScrollBarThemeImpl.1
            @Override // com.sun.javafx.webkit.theme.RenderThemeImpl.ViewListener, javafx.beans.InvalidationListener
            public void invalidated(Observable ov) {
                super.invalidated(ov);
                ScrollBar testSB = new ScrollBarWidget();
                accessor.addChild(testSB);
                ScrollBarThemeImpl.this.testSBRef = new WeakReference(testSB);
            }
        });
    }

    private static Orientation convertOrientation(int orientation) {
        return orientation == 1 ? Orientation.VERTICAL : Orientation.HORIZONTAL;
    }

    private void adjustScrollBar(ScrollBar sb, int w2, int h2, int orientation) {
        Orientation current = convertOrientation(orientation);
        if (current != sb.getOrientation()) {
            sb.setOrientation(current);
        }
        if (current == Orientation.VERTICAL) {
            w2 = ScrollBarTheme.getThickness();
        } else {
            h2 = ScrollBarTheme.getThickness();
        }
        if (w2 != sb.getWidth() || h2 != sb.getHeight()) {
            sb.resize(w2, h2);
        }
    }

    private void adjustScrollBar(ScrollBar sb, int w2, int h2, int orientation, int value, int visibleSize, int totalSize) {
        adjustScrollBar(sb, w2, h2, orientation);
        boolean disable = totalSize <= visibleSize;
        sb.setDisable(disable);
        if (disable) {
            return;
        }
        if (value < 0) {
            value = 0;
        } else if (value > totalSize - visibleSize) {
            value = totalSize - visibleSize;
        }
        if (sb.getMax() != totalSize || sb.getVisibleAmount() != visibleSize) {
            sb.setValue(0.0d);
            sb.setMax(totalSize);
            sb.setVisibleAmount(visibleSize);
        }
        if (totalSize > visibleSize) {
            float factor = totalSize / (totalSize - visibleSize);
            if (sb.getValue() != value * factor) {
                sb.setValue(value * factor);
            }
        }
    }

    @Override // com.sun.webkit.graphics.ScrollBarTheme
    protected Ref createWidget(long id, int w2, int h2, int orientation, int value, int visibleSize, int totalSize) {
        ScrollBarWidget sb = (ScrollBarWidget) this.pool.get(id);
        if (sb == null) {
            sb = new ScrollBarWidget();
            this.pool.put(id, sb, this.accessor.getPage().getUpdateContentCycleID());
            this.accessor.addChild(sb);
        }
        adjustScrollBar(sb, w2, h2, orientation, value, visibleSize, totalSize);
        return new ScrollBarRef(sb);
    }

    @Override // com.sun.webkit.graphics.ScrollBarTheme
    public void paint(WCGraphicsContext g2, Ref sbRef, int x2, int y2, int pressedPart, int hoveredPart) {
        ScrollBar sb = (ScrollBar) ((ScrollBarRef) sbRef).asControl();
        if (sb == null) {
            return;
        }
        if (log.isLoggable(Level.FINEST)) {
            Logger logger = log;
            Level level = Level.FINEST;
            Object[] objArr = new Object[5];
            objArr[0] = Integer.valueOf(x2);
            objArr[1] = Integer.valueOf(y2);
            objArr[2] = Double.valueOf(sb.getWidth());
            objArr[3] = Double.valueOf(sb.getHeight());
            objArr[4] = sb.getOrientation() == Orientation.VERTICAL ? "VERTICAL" : "HORIZONTAL";
            logger.log(level, "[{0}, {1} {2}x{3}], {4}", objArr);
        }
        g2.saveState();
        g2.translate(x2, y2);
        Renderer.getRenderer().render(sb, g2);
        g2.restoreState();
    }

    @Override // com.sun.webkit.graphics.ScrollBarTheme
    public WCSize getWidgetSize(Ref widget) {
        ScrollBar sb = (ScrollBar) ((ScrollBarRef) widget).asControl();
        if (sb != null) {
            return new WCSize((float) sb.getWidth(), (float) sb.getHeight());
        }
        return new WCSize(0.0f, 0.0f);
    }

    @Override // com.sun.webkit.graphics.ScrollBarTheme
    protected void getScrollBarPartRect(long id, int part, int[] rect) {
        ScrollBar sb = (ScrollBar) this.pool.get(id);
        if (sb == null) {
            return;
        }
        Node node = null;
        if (part == 2) {
            node = getIncButton(sb);
        } else if (part == 1) {
            node = getDecButton(sb);
        } else if (part == 256) {
            node = getTrack(sb);
        }
        if (!$assertionsDisabled && rect.length < 4) {
            throw new AssertionError();
        }
        if (node != null) {
            Bounds bounds = node.getBoundsInParent();
            rect[0] = (int) bounds.getMinX();
            rect[1] = (int) bounds.getMinY();
            rect[2] = (int) bounds.getWidth();
            rect[3] = (int) bounds.getHeight();
        } else {
            rect[3] = 0;
            rect[2] = 0;
            rect[1] = 0;
            rect[0] = 0;
        }
        log.log(Level.FINEST, "id {0} part {1} bounds {2},{3} {4}x{5}", new Object[]{String.valueOf(id), String.valueOf(part), Integer.valueOf(rect[0]), Integer.valueOf(rect[1]), Integer.valueOf(rect[2]), Integer.valueOf(rect[3])});
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initializeThickness() {
        ScrollBar testSB;
        if (this.thicknessInitialized || (testSB = this.testSBRef.get()) == null) {
            return;
        }
        int thickness = (int) testSB.prefWidth(-1.0d);
        if (thickness != 0 && ScrollBarTheme.getThickness() != thickness) {
            ScrollBarTheme.setThickness(thickness);
        }
        this.thicknessInitialized = true;
    }

    private static Node getThumb(ScrollBar scrollBar) {
        return findNode(scrollBar, "thumb");
    }

    private static Node getTrack(ScrollBar scrollBar) {
        return findNode(scrollBar, "track");
    }

    private static Node getIncButton(ScrollBar scrollBar) {
        return findNode(scrollBar, "increment-button");
    }

    private static Node getDecButton(ScrollBar scrollBar) {
        return findNode(scrollBar, "decrement-button");
    }

    private static Node findNode(ScrollBar scrollBar, String styleclass) {
        for (Node n2 : scrollBar.getChildrenUnmodifiable()) {
            if (n2.getStyleClass().contains(styleclass)) {
                return n2;
            }
        }
        return null;
    }
}
