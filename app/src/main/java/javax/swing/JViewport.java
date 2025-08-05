package javax.swing;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.peer.ComponentPeer;
import java.beans.Transient;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ViewportUI;

/* loaded from: rt.jar:javax/swing/JViewport.class */
public class JViewport extends JComponent implements Accessible {
    private static final String uiClassID = "ViewportUI";
    static final Object EnableWindowBlit = "EnableWindowBlit";
    public static final int BLIT_SCROLL_MODE = 1;
    public static final int BACKINGSTORE_SCROLL_MODE = 2;
    public static final int SIMPLE_SCROLL_MODE = 0;
    private transient boolean repaintAll;
    private transient boolean waitingForRepaint;
    private transient Timer repaintTimer;
    private transient boolean inBlitPaint;
    private boolean hasHadValidView;
    private boolean viewChanged;
    protected boolean isViewSizeSet = false;
    protected Point lastPaintPosition = null;

    @Deprecated
    protected boolean backingStore = false;
    protected transient Image backingStoreImage = null;
    protected boolean scrollUnderway = false;
    private ComponentListener viewListener = null;
    private transient ChangeEvent changeEvent = null;
    private int scrollMode = 1;

    public JViewport() {
        setLayout(createLayoutManager());
        setOpaque(true);
        updateUI();
        setInheritsPopupMenu(true);
    }

    public ViewportUI getUI() {
        return (ViewportUI) this.ui;
    }

    public void setUI(ViewportUI viewportUI) {
        super.setUI((ComponentUI) viewportUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ViewportUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        setView(component);
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        component.removeComponentListener(this.viewListener);
        super.remove(component);
    }

    @Override // javax.swing.JComponent
    public void scrollRectToVisible(Rectangle rectangle) {
        Component view = getView();
        if (view == null) {
            return;
        }
        if (!view.isValid()) {
            validateView();
        }
        int iPositionAdjustment = positionAdjustment(getWidth(), rectangle.width, rectangle.f12372x);
        int iPositionAdjustment2 = positionAdjustment(getHeight(), rectangle.height, rectangle.f12373y);
        if (iPositionAdjustment != 0 || iPositionAdjustment2 != 0) {
            Point viewPosition = getViewPosition();
            Dimension size = view.getSize();
            int i2 = viewPosition.f12370x;
            int i3 = viewPosition.f12371y;
            Dimension extentSize = getExtentSize();
            viewPosition.f12370x -= iPositionAdjustment;
            viewPosition.f12371y -= iPositionAdjustment2;
            if (view.isValid()) {
                if (getParent().getComponentOrientation().isLeftToRight()) {
                    if (viewPosition.f12370x + extentSize.width > size.width) {
                        viewPosition.f12370x = Math.max(0, size.width - extentSize.width);
                    } else if (viewPosition.f12370x < 0) {
                        viewPosition.f12370x = 0;
                    }
                } else if (extentSize.width > size.width) {
                    viewPosition.f12370x = size.width - extentSize.width;
                } else {
                    viewPosition.f12370x = Math.max(0, Math.min(size.width - extentSize.width, viewPosition.f12370x));
                }
                if (viewPosition.f12371y + extentSize.height > size.height) {
                    viewPosition.f12371y = Math.max(0, size.height - extentSize.height);
                } else if (viewPosition.f12371y < 0) {
                    viewPosition.f12371y = 0;
                }
            }
            if (viewPosition.f12370x != i2 || viewPosition.f12371y != i3) {
                setViewPosition(viewPosition);
                this.scrollUnderway = false;
            }
        }
    }

    private void validateView() {
        Container validateRoot = SwingUtilities.getValidateRoot(this, false);
        if (validateRoot == null) {
            return;
        }
        validateRoot.validate();
        RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager((JComponent) this);
        if (repaintManagerCurrentManager != null) {
            repaintManagerCurrentManager.removeInvalidComponent((JComponent) validateRoot);
        }
    }

    private int positionAdjustment(int i2, int i3, int i4) {
        if (i4 >= 0 && i3 + i4 <= i2) {
            return 0;
        }
        if (i4 <= 0 && i3 + i4 >= i2) {
            return 0;
        }
        if (i4 > 0 && i3 <= i2) {
            return ((-i4) + i2) - i3;
        }
        if (i4 >= 0 && i3 >= i2) {
            return -i4;
        }
        if (i4 <= 0 && i3 <= i2) {
            return -i4;
        }
        if (i4 < 0 && i3 >= i2) {
            return ((-i4) + i2) - i3;
        }
        return 0;
    }

    @Override // javax.swing.JComponent
    public final void setBorder(Border border) {
        if (border != null) {
            throw new IllegalArgumentException("JViewport.setBorder() not supported");
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public final Insets getInsets() {
        return new Insets(0, 0, 0, 0);
    }

    @Override // javax.swing.JComponent
    public final Insets getInsets(Insets insets) {
        insets.bottom = 0;
        insets.right = 0;
        insets.top = 0;
        insets.left = 0;
        return insets;
    }

    private Graphics getBackingStoreGraphics(Graphics graphics) {
        Graphics graphics2 = this.backingStoreImage.getGraphics();
        graphics2.setColor(graphics.getColor());
        graphics2.setFont(graphics.getFont());
        graphics2.setClip(graphics.getClipBounds());
        return graphics2;
    }

    private void paintViaBackingStore(Graphics graphics) {
        Graphics backingStoreGraphics = getBackingStoreGraphics(graphics);
        try {
            super.paint(backingStoreGraphics);
            graphics.drawImage(this.backingStoreImage, 0, 0, this);
        } finally {
            backingStoreGraphics.dispose();
        }
    }

    private void paintViaBackingStore(Graphics graphics, Rectangle rectangle) {
        Graphics backingStoreGraphics = getBackingStoreGraphics(graphics);
        try {
            super.paint(backingStoreGraphics);
            graphics.setClip(rectangle);
            graphics.drawImage(this.backingStoreImage, 0, 0, this);
            backingStoreGraphics.dispose();
        } catch (Throwable th) {
            backingStoreGraphics.dispose();
            throw th;
        }
    }

    @Override // javax.swing.JComponent
    public boolean isOptimizedDrawingEnabled() {
        return false;
    }

    @Override // javax.swing.JComponent
    protected boolean isPaintingOrigin() {
        return this.scrollMode == 2;
    }

    private Point getViewLocation() {
        Component view = getView();
        if (view != null) {
            return view.getLocation();
        }
        return new Point(0, 0);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            return;
        }
        if (this.inBlitPaint) {
            super.paint(graphics);
            return;
        }
        if (this.repaintAll) {
            this.repaintAll = false;
            Rectangle clipBounds = graphics.getClipBounds();
            if (clipBounds.width < getWidth() || clipBounds.height < getHeight()) {
                this.waitingForRepaint = true;
                if (this.repaintTimer == null) {
                    this.repaintTimer = createRepaintTimer();
                }
                this.repaintTimer.stop();
                this.repaintTimer.start();
            } else {
                if (this.repaintTimer != null) {
                    this.repaintTimer.stop();
                }
                this.waitingForRepaint = false;
            }
        } else if (this.waitingForRepaint) {
            Rectangle clipBounds2 = graphics.getClipBounds();
            if (clipBounds2.width >= getWidth() && clipBounds2.height >= getHeight()) {
                this.waitingForRepaint = false;
                this.repaintTimer.stop();
            }
        }
        if (!this.backingStore || isBlitting() || getView() == null) {
            super.paint(graphics);
            this.lastPaintPosition = getViewLocation();
            return;
        }
        Rectangle bounds = getView().getBounds();
        if (!isOpaque()) {
            graphics.clipRect(0, 0, bounds.width, bounds.height);
        }
        if (this.backingStoreImage == null) {
            this.backingStoreImage = createImage(width, height);
            Rectangle clipBounds3 = graphics.getClipBounds();
            if (clipBounds3.width != width || clipBounds3.height != height) {
                if (!isOpaque()) {
                    graphics.setClip(0, 0, Math.min(bounds.width, width), Math.min(bounds.height, height));
                } else {
                    graphics.setClip(0, 0, width, height);
                }
                paintViaBackingStore(graphics, clipBounds3);
            } else {
                paintViaBackingStore(graphics);
            }
        } else if (!this.scrollUnderway || this.lastPaintPosition.equals(getViewLocation())) {
            paintViaBackingStore(graphics);
        } else {
            Point point = new Point();
            Point point2 = new Point();
            Dimension dimension = new Dimension();
            Rectangle rectangle = new Rectangle();
            Point viewLocation = getViewLocation();
            if (!computeBlit(viewLocation.f12370x - this.lastPaintPosition.f12370x, viewLocation.f12371y - this.lastPaintPosition.f12371y, point, point2, dimension, rectangle)) {
                paintViaBackingStore(graphics);
            } else {
                int i2 = point2.f12370x - point.f12370x;
                int i3 = point2.f12371y - point.f12371y;
                Rectangle clipBounds4 = graphics.getClipBounds();
                graphics.setClip(0, 0, width, height);
                Graphics backingStoreGraphics = getBackingStoreGraphics(graphics);
                try {
                    backingStoreGraphics.copyArea(point.f12370x, point.f12371y, dimension.width, dimension.height, i2, i3);
                    graphics.setClip(clipBounds4.f12372x, clipBounds4.f12373y, clipBounds4.width, clipBounds4.height);
                    backingStoreGraphics.setClip(bounds.intersection(rectangle));
                    super.paint(backingStoreGraphics);
                    graphics.drawImage(this.backingStoreImage, 0, 0, this);
                    backingStoreGraphics.dispose();
                } catch (Throwable th) {
                    backingStoreGraphics.dispose();
                    throw th;
                }
            }
        }
        this.lastPaintPosition = getViewLocation();
        this.scrollUnderway = false;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void reshape(int i2, int i3, int i4, int i5) {
        boolean z2 = (getWidth() == i4 && getHeight() == i5) ? false : true;
        if (z2) {
            this.backingStoreImage = null;
        }
        super.reshape(i2, i3, i4, i5);
        if (z2 || this.viewChanged) {
            this.viewChanged = false;
            fireStateChanged();
        }
    }

    public void setScrollMode(int i2) {
        this.scrollMode = i2;
        this.backingStore = i2 == 2;
    }

    public int getScrollMode() {
        return this.scrollMode;
    }

    @Deprecated
    public boolean isBackingStoreEnabled() {
        return this.scrollMode == 2;
    }

    @Deprecated
    public void setBackingStoreEnabled(boolean z2) {
        if (z2) {
            setScrollMode(2);
        } else {
            setScrollMode(1);
        }
    }

    private boolean isBlitting() {
        Component view = getView();
        return this.scrollMode == 1 && (view instanceof JComponent) && view.isOpaque();
    }

    public Component getView() {
        if (getComponentCount() > 0) {
            return getComponent(0);
        }
        return null;
    }

    public void setView(Component component) {
        for (int componentCount = getComponentCount() - 1; componentCount >= 0; componentCount--) {
            remove(getComponent(componentCount));
        }
        this.isViewSizeSet = false;
        if (component != null) {
            super.addImpl(component, null, -1);
            this.viewListener = createViewListener();
            component.addComponentListener(this.viewListener);
        }
        if (this.hasHadValidView) {
            fireStateChanged();
        } else if (component != null) {
            this.hasHadValidView = true;
        }
        this.viewChanged = true;
        revalidate();
        repaint();
    }

    public Dimension getViewSize() {
        Component view = getView();
        if (view == null) {
            return new Dimension(0, 0);
        }
        if (this.isViewSizeSet) {
            return view.getSize();
        }
        return view.getPreferredSize();
    }

    public void setViewSize(Dimension dimension) {
        Component view = getView();
        if (view != null && !dimension.equals(view.getSize())) {
            this.scrollUnderway = false;
            view.setSize(dimension);
            this.isViewSizeSet = true;
            fireStateChanged();
        }
    }

    public Point getViewPosition() {
        Component view = getView();
        if (view != null) {
            Point location = view.getLocation();
            location.f12370x = -location.f12370x;
            location.f12371y = -location.f12371y;
            return location;
        }
        return new Point(0, 0);
    }

    public void setViewPosition(Point point) {
        int x2;
        int y2;
        Component view = getView();
        if (view == null) {
            return;
        }
        int i2 = point.f12370x;
        int i3 = point.f12371y;
        if (view instanceof JComponent) {
            JComponent jComponent = (JComponent) view;
            x2 = jComponent.getX();
            y2 = jComponent.getY();
        } else {
            Rectangle bounds = view.getBounds();
            x2 = bounds.f12372x;
            y2 = bounds.f12373y;
        }
        int i4 = -i2;
        int i5 = -i3;
        if (x2 != i4 || y2 != i5) {
            if (!this.waitingForRepaint && isBlitting() && canUseWindowBlitter()) {
                RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager((JComponent) this);
                JComponent jComponent2 = (JComponent) view;
                Rectangle dirtyRegion = repaintManagerCurrentManager.getDirtyRegion(jComponent2);
                if (dirtyRegion == null || !dirtyRegion.contains(jComponent2.getVisibleRect())) {
                    repaintManagerCurrentManager.beginPaint();
                    try {
                        Graphics graphicsSafelyGetGraphics = JComponent.safelyGetGraphics(this);
                        flushViewDirtyRegion(graphicsSafelyGetGraphics, dirtyRegion);
                        view.setLocation(i4, i5);
                        Rectangle rectangle = new Rectangle(0, 0, getWidth(), Math.min(getHeight(), jComponent2.getHeight()));
                        graphicsSafelyGetGraphics.setClip(rectangle);
                        this.repaintAll = windowBlitPaint(graphicsSafelyGetGraphics) && needsRepaintAfterBlit();
                        graphicsSafelyGetGraphics.dispose();
                        repaintManagerCurrentManager.notifyRepaintPerformed(this, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                        repaintManagerCurrentManager.markCompletelyClean((JComponent) getParent());
                        repaintManagerCurrentManager.markCompletelyClean(this);
                        repaintManagerCurrentManager.markCompletelyClean(jComponent2);
                        repaintManagerCurrentManager.endPaint();
                    } catch (Throwable th) {
                        repaintManagerCurrentManager.endPaint();
                        throw th;
                    }
                } else {
                    view.setLocation(i4, i5);
                    this.repaintAll = false;
                }
            } else {
                this.scrollUnderway = true;
                view.setLocation(i4, i5);
                this.repaintAll = false;
            }
            revalidate();
            fireStateChanged();
        }
    }

    public Rectangle getViewRect() {
        return new Rectangle(getViewPosition(), getExtentSize());
    }

    protected boolean computeBlit(int i2, int i3, Point point, Point point2, Dimension dimension, Rectangle rectangle) {
        int iAbs = Math.abs(i2);
        int iAbs2 = Math.abs(i3);
        Dimension extentSize = getExtentSize();
        if (i2 == 0 && i3 != 0 && iAbs2 < extentSize.height) {
            if (i3 < 0) {
                point.f12371y = -i3;
                point2.f12371y = 0;
                rectangle.f12373y = extentSize.height + i3;
            } else {
                point.f12371y = 0;
                point2.f12371y = i3;
                rectangle.f12373y = 0;
            }
            point2.f12370x = 0;
            point.f12370x = 0;
            rectangle.f12372x = 0;
            dimension.width = extentSize.width;
            dimension.height = extentSize.height - iAbs2;
            rectangle.width = extentSize.width;
            rectangle.height = iAbs2;
            return true;
        }
        if (i3 == 0 && i2 != 0 && iAbs < extentSize.width) {
            if (i2 < 0) {
                point.f12370x = -i2;
                point2.f12370x = 0;
                rectangle.f12372x = extentSize.width + i2;
            } else {
                point.f12370x = 0;
                point2.f12370x = i2;
                rectangle.f12372x = 0;
            }
            point2.f12371y = 0;
            point.f12371y = 0;
            rectangle.f12373y = 0;
            dimension.width = extentSize.width - iAbs;
            dimension.height = extentSize.height;
            rectangle.width = iAbs;
            rectangle.height = extentSize.height;
            return true;
        }
        return false;
    }

    @Transient
    public Dimension getExtentSize() {
        return getSize();
    }

    public Dimension toViewCoordinates(Dimension dimension) {
        return new Dimension(dimension);
    }

    public Point toViewCoordinates(Point point) {
        return new Point(point);
    }

    public void setExtentSize(Dimension dimension) {
        if (!dimension.equals(getExtentSize())) {
            setSize(dimension);
            fireStateChanged();
        }
    }

    /* loaded from: rt.jar:javax/swing/JViewport$ViewListener.class */
    protected class ViewListener extends ComponentAdapter implements Serializable {
        protected ViewListener() {
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            JViewport.this.fireStateChanged();
            JViewport.this.revalidate();
        }
    }

    protected ViewListener createViewListener() {
        return new ViewListener();
    }

    protected LayoutManager createLayoutManager() {
        return ViewportLayout.SHARED_INSTANCE;
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        Container parent = getParent();
        if (parent != null) {
            parent.repaint(j2, i2 + getX(), i3 + getY(), i4, i5);
        } else {
            super.repaint(j2, i2, i3, i4, i5);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",isViewSizeSet=" + (this.isViewSizeSet ? "true" : "false") + ",lastPaintPosition=" + (this.lastPaintPosition != null ? this.lastPaintPosition.toString() : "") + ",scrollUnderway=" + (this.scrollUnderway ? "true" : "false");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.awt.Component
    public void firePropertyChange(String str, Object obj, Object obj2) {
        super.firePropertyChange(str, obj, obj2);
        if (str.equals(EnableWindowBlit)) {
            if (obj2 != null) {
                setScrollMode(1);
            } else {
                setScrollMode(0);
            }
        }
    }

    private boolean needsRepaintAfterBlit() {
        Container container;
        ComponentPeer peer;
        Container parent = getParent();
        while (true) {
            container = parent;
            if (container == null || !container.isLightweight()) {
                break;
            }
            parent = container.getParent();
        }
        if (container != null && (peer = container.getPeer()) != null && peer.canDetermineObscurity() && !peer.isObscured()) {
            return false;
        }
        return true;
    }

    private Timer createRepaintTimer() {
        Timer timer = new Timer(300, new ActionListener() { // from class: javax.swing.JViewport.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                if (JViewport.this.waitingForRepaint) {
                    JViewport.this.repaint();
                }
            }
        });
        timer.setRepeats(false);
        return timer;
    }

    private void flushViewDirtyRegion(Graphics graphics, Rectangle rectangle) {
        JComponent jComponent = (JComponent) getView();
        if (rectangle != null && rectangle.width > 0 && rectangle.height > 0) {
            rectangle.f12372x += jComponent.getX();
            rectangle.f12373y += jComponent.getY();
            if (graphics.getClipBounds() == null) {
                graphics.setClip(0, 0, getWidth(), getHeight());
            }
            graphics.clipRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
            Rectangle clipBounds = graphics.getClipBounds();
            if (clipBounds.width > 0 && clipBounds.height > 0) {
                paintView(graphics);
            }
        }
    }

    private boolean windowBlitPaint(Graphics graphics) {
        boolean z2;
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) {
            return false;
        }
        RepaintManager.currentManager((JComponent) this);
        JComponent jComponent = (JComponent) getView();
        if (this.lastPaintPosition == null || this.lastPaintPosition.equals(getViewLocation())) {
            paintView(graphics);
            z2 = false;
        } else {
            Point point = new Point();
            Point point2 = new Point();
            Dimension dimension = new Dimension();
            Rectangle rectangle = new Rectangle();
            Point viewLocation = getViewLocation();
            if (!computeBlit(viewLocation.f12370x - this.lastPaintPosition.f12370x, viewLocation.f12371y - this.lastPaintPosition.f12371y, point, point2, dimension, rectangle)) {
                paintView(graphics);
                z2 = false;
            } else {
                Rectangle rectangleIntersection = jComponent.getBounds().intersection(rectangle);
                rectangleIntersection.f12372x -= jComponent.getX();
                rectangleIntersection.f12373y -= jComponent.getY();
                blitDoubleBuffered(jComponent, graphics, rectangleIntersection.f12372x, rectangleIntersection.f12373y, rectangleIntersection.width, rectangleIntersection.height, point.f12370x, point.f12371y, point2.f12370x, point2.f12371y, dimension.width, dimension.height);
                z2 = true;
            }
        }
        this.lastPaintPosition = getViewLocation();
        return z2;
    }

    private void blitDoubleBuffered(JComponent jComponent, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
        RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager((JComponent) this);
        int i12 = i8 - i6;
        int i13 = i9 - i7;
        Composite composite = null;
        if (graphics instanceof Graphics2D) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            composite = graphics2D.getComposite();
            graphics2D.setComposite(AlphaComposite.Src);
        }
        repaintManagerCurrentManager.copyArea(this, graphics, i6, i7, i10, i11, i12, i13, false);
        if (composite != null) {
            ((Graphics2D) graphics).setComposite(composite);
        }
        int x2 = jComponent.getX();
        int y2 = jComponent.getY();
        graphics.translate(x2, y2);
        graphics.setClip(i2, i3, i4, i5);
        jComponent.paintForceDoubleBuffered(graphics);
        graphics.translate(-x2, -y2);
    }

    private void paintView(Graphics graphics) {
        Rectangle clipBounds = graphics.getClipBounds();
        JComponent jComponent = (JComponent) getView();
        if (jComponent.getWidth() >= getWidth()) {
            int x2 = jComponent.getX();
            int y2 = jComponent.getY();
            graphics.translate(x2, y2);
            graphics.setClip(clipBounds.f12372x - x2, clipBounds.f12373y - y2, clipBounds.width, clipBounds.height);
            jComponent.paintForceDoubleBuffered(graphics);
            graphics.translate(-x2, -y2);
            graphics.setClip(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
            return;
        }
        try {
            this.inBlitPaint = true;
            paintForceDoubleBuffered(graphics);
            this.inBlitPaint = false;
        } catch (Throwable th) {
            this.inBlitPaint = false;
            throw th;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:53:0x013e, code lost:
    
        if (r12 != null) goto L56;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0141, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x0143, code lost:
    
        return true;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [java.awt.Rectangle] */
    /* JADX WARN: Type inference failed for: r0v13, types: [java.awt.Rectangle, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r11v0 */
    /* JADX WARN: Type inference failed for: r11v1 */
    /* JADX WARN: Type inference failed for: r11v2 */
    /* JADX WARN: Type inference failed for: r11v3 */
    /* JADX WARN: Type inference failed for: r11v4, types: [java.awt.Rectangle] */
    /* JADX WARN: Type inference failed for: r11v5 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean canUseWindowBlitter() {
        /*
            Method dump skipped, instructions count: 325
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.JViewport.canUseWindowBlitter():boolean");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJViewport();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JViewport$AccessibleJViewport.class */
    protected class AccessibleJViewport extends JComponent.AccessibleJComponent {
        protected AccessibleJViewport() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.VIEWPORT;
        }
    }
}
