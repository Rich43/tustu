package javax.swing;

import com.sun.java.swing.SwingUtilities3;
import java.applet.Applet;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InvocationEvent;
import java.awt.image.VolatileImage;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.DisplayChangedListener;
import sun.awt.SunToolkit;
import sun.java2d.SunGraphicsEnvironment;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;
import sun.security.action.GetPropertyAction;
import sun.swing.SwingAccessor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/RepaintManager.class */
public class RepaintManager {
    static final boolean HANDLE_TOP_LEVEL_PAINT;
    private static final short BUFFER_STRATEGY_NOT_SPECIFIED = 0;
    private static final short BUFFER_STRATEGY_SPECIFIED_ON = 1;
    private static final short BUFFER_STRATEGY_SPECIFIED_OFF = 2;
    private static final short BUFFER_STRATEGY_TYPE;
    private Map<GraphicsConfiguration, VolatileImage> volatileMap;
    private Map<Container, Rectangle> hwDirtyComponents;
    private Map<Component, Rectangle> dirtyComponents;
    private Map<Component, Rectangle> tmpDirtyComponents;
    private List<Component> invalidComponents;
    private List<Runnable> runnableList;
    boolean doubleBufferingEnabled;
    private Dimension doubleBufferMaxSize;
    DoubleBufferInfo standardDoubleBuffer;
    private PaintManager paintManager;
    static boolean volatileImageBufferEnabled;
    private static final int volatileBufferType;
    private static boolean nativeDoubleBuffering;
    private static final int VOLATILE_LOOP_MAX = 2;
    private int paintDepth;
    private short bufferStrategyType;
    private boolean painting;
    private JComponent repaintRoot;
    private Thread paintThread;
    private final ProcessingRunnable processingRunnable;
    Rectangle tmp;
    private List<SwingUtilities2.RepaintListener> repaintListeners;
    private static final Object repaintManagerKey = RepaintManager.class;
    private static final JavaSecurityAccess javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
    private static final DisplayChangedListener displayChangedHandler = new DisplayChangedHandler();

    static {
        volatileImageBufferEnabled = true;
        SwingAccessor.setRepaintManagerAccessor(new SwingAccessor.RepaintManagerAccessor() { // from class: javax.swing.RepaintManager.1
            @Override // sun.swing.SwingAccessor.RepaintManagerAccessor
            public void addRepaintListener(RepaintManager repaintManager, SwingUtilities2.RepaintListener repaintListener) {
                repaintManager.addRepaintListener(repaintListener);
            }

            @Override // sun.swing.SwingAccessor.RepaintManagerAccessor
            public void removeRepaintListener(RepaintManager repaintManager, SwingUtilities2.RepaintListener repaintListener) {
                repaintManager.removeRepaintListener(repaintListener);
            }
        });
        volatileImageBufferEnabled = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.volatileImageBufferEnabled", "true")));
        boolean zIsHeadless = GraphicsEnvironment.isHeadless();
        if (volatileImageBufferEnabled && zIsHeadless) {
            volatileImageBufferEnabled = false;
        }
        nativeDoubleBuffering = "true".equals(AccessController.doPrivileged(new GetPropertyAction("awt.nativeDoubleBuffering")));
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("swing.bufferPerWindow"));
        if (zIsHeadless) {
            BUFFER_STRATEGY_TYPE = (short) 2;
        } else if (str == null) {
            BUFFER_STRATEGY_TYPE = (short) 0;
        } else if ("true".equals(str)) {
            BUFFER_STRATEGY_TYPE = (short) 1;
        } else {
            BUFFER_STRATEGY_TYPE = (short) 2;
        }
        HANDLE_TOP_LEVEL_PAINT = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.handleTopLevelPaint", "true")));
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (localGraphicsEnvironment instanceof SunGraphicsEnvironment) {
            ((SunGraphicsEnvironment) localGraphicsEnvironment).addDisplayChangedListener(displayChangedHandler);
        }
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if ((defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).isSwingBackbufferTranslucencySupported()) {
            volatileBufferType = 3;
        } else {
            volatileBufferType = 1;
        }
    }

    public static RepaintManager currentManager(Component component) {
        return currentManager(AppContext.getAppContext());
    }

    static RepaintManager currentManager(AppContext appContext) {
        RepaintManager repaintManager = (RepaintManager) appContext.get(repaintManagerKey);
        if (repaintManager == null) {
            repaintManager = new RepaintManager(BUFFER_STRATEGY_TYPE);
            appContext.put(repaintManagerKey, repaintManager);
        }
        return repaintManager;
    }

    public static RepaintManager currentManager(JComponent jComponent) {
        return currentManager((Component) jComponent);
    }

    public static void setCurrentManager(RepaintManager repaintManager) {
        if (repaintManager != null) {
            SwingUtilities.appContextPut(repaintManagerKey, repaintManager);
        } else {
            SwingUtilities.appContextRemove(repaintManagerKey);
        }
    }

    public RepaintManager() {
        this((short) 2);
    }

    private RepaintManager(short s2) {
        this.volatileMap = new HashMap(1);
        this.doubleBufferingEnabled = true;
        this.paintDepth = 0;
        this.tmp = new Rectangle();
        this.repaintListeners = new ArrayList(1);
        this.doubleBufferingEnabled = !nativeDoubleBuffering;
        synchronized (this) {
            this.dirtyComponents = new IdentityHashMap();
            this.tmpDirtyComponents = new IdentityHashMap();
            this.bufferStrategyType = s2;
            this.hwDirtyComponents = new IdentityHashMap();
        }
        this.processingRunnable = new ProcessingRunnable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void displayChanged() {
        clearImages();
    }

    public synchronized void addInvalidComponent(JComponent jComponent) {
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            delegate.addInvalidComponent(jComponent);
            return;
        }
        Container validateRoot = SwingUtilities.getValidateRoot(jComponent, true);
        if (validateRoot == null) {
            return;
        }
        if (this.invalidComponents == null) {
            this.invalidComponents = new ArrayList();
        } else {
            int size = this.invalidComponents.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (validateRoot == this.invalidComponents.get(i2)) {
                    return;
                }
            }
        }
        this.invalidComponents.add(validateRoot);
        scheduleProcessingRunnable(SunToolkit.targetToAppContext(jComponent));
    }

    public synchronized void removeInvalidComponent(JComponent jComponent) {
        int iIndexOf;
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            delegate.removeInvalidComponent(jComponent);
        } else if (this.invalidComponents != null && (iIndexOf = this.invalidComponents.indexOf(jComponent)) != -1) {
            this.invalidComponents.remove(iIndexOf);
        }
    }

    private void addDirtyRegion0(Container container, int i2, int i3, int i4, int i5) {
        Container container2;
        if (i4 <= 0 || i5 <= 0 || container == null || container.getWidth() <= 0 || container.getHeight() <= 0 || extendDirtyRegion(container, i2, i3, i4, i5)) {
            return;
        }
        Container container3 = null;
        Container parent = container;
        while (true) {
            container2 = parent;
            if (container2 == null) {
                break;
            }
            if (!container2.isVisible() || container2.getPeer() == null) {
                return;
            }
            if ((container2 instanceof Window) || (container2 instanceof Applet)) {
                break;
            } else {
                parent = container2.getParent();
            }
        }
        if ((container2 instanceof Frame) && (((Frame) container2).getExtendedState() & 1) == 1) {
            return;
        }
        container3 = container2;
        if (container3 == null) {
            return;
        }
        synchronized (this) {
            if (extendDirtyRegion(container, i2, i3, i4, i5)) {
                return;
            }
            this.dirtyComponents.put(container, new Rectangle(i2, i3, i4, i5));
            scheduleProcessingRunnable(SunToolkit.targetToAppContext(container));
        }
    }

    public void addDirtyRegion(JComponent jComponent, int i2, int i3, int i4, int i5) {
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            delegate.addDirtyRegion(jComponent, i2, i3, i4, i5);
        } else {
            addDirtyRegion0(jComponent, i2, i3, i4, i5);
        }
    }

    public void addDirtyRegion(Window window, int i2, int i3, int i4, int i5) {
        addDirtyRegion0(window, i2, i3, i4, i5);
    }

    public void addDirtyRegion(Applet applet, int i2, int i3, int i4, int i5) {
        addDirtyRegion0(applet, i2, i3, i4, i5);
    }

    void scheduleHeavyWeightPaints() {
        synchronized (this) {
            if (this.hwDirtyComponents.size() == 0) {
                return;
            }
            Map<Container, Rectangle> map = this.hwDirtyComponents;
            this.hwDirtyComponents = new IdentityHashMap();
            for (Container container : map.keySet()) {
                Rectangle rectangle = map.get(container);
                if (container instanceof Window) {
                    addDirtyRegion((Window) container, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                } else if (container instanceof Applet) {
                    addDirtyRegion((Applet) container, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                } else {
                    addDirtyRegion0(container, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                }
            }
        }
    }

    void nativeAddDirtyRegion(AppContext appContext, Container container, int i2, int i3, int i4, int i5) {
        if (i4 > 0 && i5 > 0) {
            synchronized (this) {
                Rectangle rectangle = this.hwDirtyComponents.get(container);
                if (rectangle == null) {
                    this.hwDirtyComponents.put(container, new Rectangle(i2, i3, i4, i5));
                } else {
                    this.hwDirtyComponents.put(container, SwingUtilities.computeUnion(i2, i3, i4, i5, rectangle));
                }
            }
            scheduleProcessingRunnable(appContext);
        }
    }

    void nativeQueueSurfaceDataRunnable(AppContext appContext, final Component component, final Runnable runnable) {
        synchronized (this) {
            if (this.runnableList == null) {
                this.runnableList = new LinkedList();
            }
            this.runnableList.add(new Runnable() { // from class: javax.swing.RepaintManager.2
                @Override // java.lang.Runnable
                public void run() {
                    RepaintManager.javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: javax.swing.RepaintManager.2.1
                        /* JADX WARN: Can't rename method to resolve collision */
                        @Override // java.security.PrivilegedAction
                        /* renamed from: run */
                        public Void run2() {
                            runnable.run();
                            return null;
                        }
                    }, AccessController.getContext(), AWTAccessor.getComponentAccessor().getAccessControlContext(component));
                }
            });
        }
        scheduleProcessingRunnable(appContext);
    }

    private synchronized boolean extendDirtyRegion(Component component, int i2, int i3, int i4, int i5) {
        Rectangle rectangle = this.dirtyComponents.get(component);
        if (rectangle != null) {
            SwingUtilities.computeUnion(i2, i3, i4, i5, rectangle);
            return true;
        }
        return false;
    }

    public Rectangle getDirtyRegion(JComponent jComponent) {
        Rectangle rectangle;
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            return delegate.getDirtyRegion(jComponent);
        }
        synchronized (this) {
            rectangle = this.dirtyComponents.get(jComponent);
        }
        if (rectangle == null) {
            return new Rectangle(0, 0, 0, 0);
        }
        return new Rectangle(rectangle);
    }

    public void markCompletelyDirty(JComponent jComponent) {
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            delegate.markCompletelyDirty(jComponent);
        } else {
            addDirtyRegion(jComponent, 0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
    }

    public void markCompletelyClean(JComponent jComponent) {
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            delegate.markCompletelyClean(jComponent);
        } else {
            synchronized (this) {
                this.dirtyComponents.remove(jComponent);
            }
        }
    }

    public boolean isCompletelyDirty(JComponent jComponent) {
        RepaintManager delegate = getDelegate(jComponent);
        if (delegate != null) {
            return delegate.isCompletelyDirty(jComponent);
        }
        Rectangle dirtyRegion = getDirtyRegion(jComponent);
        if (dirtyRegion.width == Integer.MAX_VALUE && dirtyRegion.height == Integer.MAX_VALUE) {
            return true;
        }
        return false;
    }

    public void validateInvalidComponents() {
        synchronized (this) {
            if (this.invalidComponents == null) {
                return;
            }
            List<Component> list = this.invalidComponents;
            this.invalidComponents = null;
            int size = list.size();
            for (int i2 = 0; i2 < size; i2++) {
                final Component component = list.get(i2);
                javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: javax.swing.RepaintManager.3
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        component.validate();
                        return null;
                    }
                }, AccessController.getContext(), AWTAccessor.getComponentAccessor().getAccessControlContext(component));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void prePaintDirtyRegions() {
        Map<Component, Rectangle> map;
        List<Runnable> list;
        synchronized (this) {
            map = this.dirtyComponents;
            list = this.runnableList;
            this.runnableList = null;
        }
        if (list != null) {
            Iterator<Runnable> it = list.iterator();
            while (it.hasNext()) {
                it.next().run();
            }
        }
        paintDirtyRegions();
        if (map.size() > 0) {
            paintDirtyRegions(map);
        }
    }

    private void updateWindows(Map<Component, Rectangle> map) {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (!(defaultToolkit instanceof SunToolkit) || !((SunToolkit) defaultToolkit).needUpdateWindow()) {
            return;
        }
        HashSet hashSet = new HashSet();
        for (Component component : map.keySet()) {
            Window windowAncestor = component instanceof Window ? (Window) component : SwingUtilities.getWindowAncestor(component);
            if (windowAncestor != null && !windowAncestor.isOpaque()) {
                hashSet.add(windowAncestor);
            }
        }
        Iterator<E> it = hashSet.iterator();
        while (it.hasNext()) {
            AWTAccessor.getWindowAccessor().updateWindow((Window) it.next());
        }
    }

    boolean isPainting() {
        return this.painting;
    }

    public void paintDirtyRegions() {
        synchronized (this) {
            Map<Component, Rectangle> map = this.tmpDirtyComponents;
            this.tmpDirtyComponents = this.dirtyComponents;
            this.dirtyComponents = map;
            this.dirtyComponents.clear();
        }
        paintDirtyRegions(this.tmpDirtyComponents);
    }

    private void paintDirtyRegions(final Map<Component, Rectangle> map) {
        if (map.isEmpty()) {
            return;
        }
        final ArrayList arrayList = new ArrayList(map.size());
        Iterator<Component> it = map.keySet().iterator();
        while (it.hasNext()) {
            collectDirtyComponents(map, it.next(), arrayList);
        }
        final AtomicInteger atomicInteger = new AtomicInteger(arrayList.size());
        this.painting = true;
        for (int i2 = 0; i2 < atomicInteger.get(); i2++) {
            try {
                final int i3 = i2;
                final Component component = arrayList.get(i2);
                javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: javax.swing.RepaintManager.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        Graphics graphicsSafelyGetGraphics;
                        Rectangle rectangle = (Rectangle) map.get(component);
                        if (rectangle == null) {
                            return null;
                        }
                        SwingUtilities.computeIntersection(0, 0, component.getWidth(), component.getHeight(), rectangle);
                        if (component instanceof JComponent) {
                            ((JComponent) component).paintImmediately(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                        } else if (component.isShowing() && (graphicsSafelyGetGraphics = JComponent.safelyGetGraphics(component, component)) != null) {
                            graphicsSafelyGetGraphics.setClip(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
                            try {
                                component.paint(graphicsSafelyGetGraphics);
                                graphicsSafelyGetGraphics.dispose();
                            } catch (Throwable th) {
                                graphicsSafelyGetGraphics.dispose();
                                throw th;
                            }
                        }
                        if (RepaintManager.this.repaintRoot != null) {
                            RepaintManager.this.adjustRoots(RepaintManager.this.repaintRoot, arrayList, i3 + 1);
                            atomicInteger.set(arrayList.size());
                            RepaintManager.this.paintManager.isRepaintingRoot = true;
                            RepaintManager.this.repaintRoot.paintImmediately(0, 0, RepaintManager.this.repaintRoot.getWidth(), RepaintManager.this.repaintRoot.getHeight());
                            RepaintManager.this.paintManager.isRepaintingRoot = false;
                            RepaintManager.this.repaintRoot = null;
                            return null;
                        }
                        return null;
                    }
                }, AccessController.getContext(), AWTAccessor.getComponentAccessor().getAccessControlContext(component));
            } finally {
                this.painting = false;
            }
        }
        updateWindows(map);
        map.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void adjustRoots(JComponent jComponent, List<Component> list, int i2) {
        Component component;
        for (int size = list.size() - 1; size >= i2; size--) {
            Component parent = list.get(size);
            while (true) {
                component = parent;
                if (component == jComponent || component == null || !(component instanceof JComponent)) {
                    break;
                } else {
                    parent = component.getParent();
                }
            }
            if (component == jComponent) {
                list.remove(size);
            }
        }
    }

    void collectDirtyComponents(Map<Component, Rectangle> map, Component component, List<Component> list) {
        Container parent;
        Component component2 = component;
        Component component3 = component;
        int x2 = component.getX();
        int y2 = component.getY();
        int width = component.getWidth();
        int height = component.getHeight();
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        this.tmp.setBounds(map.get(component));
        SwingUtilities.computeIntersection(0, 0, width, height, this.tmp);
        if (this.tmp.isEmpty()) {
            return;
        }
        while ((component3 instanceof JComponent) && (parent = component3.getParent()) != null) {
            component3 = parent;
            i3 += x2;
            i5 += y2;
            this.tmp.setLocation(this.tmp.f12372x + x2, this.tmp.f12373y + y2);
            x2 = component3.getX();
            y2 = component3.getY();
            this.tmp = SwingUtilities.computeIntersection(0, 0, component3.getWidth(), component3.getHeight(), this.tmp);
            if (this.tmp.isEmpty()) {
                return;
            }
            if (map.get(component3) != null) {
                component2 = component3;
                i2 = i3;
                i4 = i5;
            }
        }
        if (component != component2) {
            this.tmp.setLocation((this.tmp.f12372x + i2) - i3, (this.tmp.f12373y + i4) - i5);
            SwingUtilities.computeUnion(this.tmp.f12372x, this.tmp.f12373y, this.tmp.width, this.tmp.height, map.get(component2));
        }
        if (!list.contains(component2)) {
            list.add(component2);
        }
    }

    public synchronized String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.dirtyComponents != null) {
            stringBuffer.append("" + ((Object) this.dirtyComponents));
        }
        return stringBuffer.toString();
    }

    public Image getOffscreenBuffer(Component component, int i2, int i3) {
        RepaintManager delegate = getDelegate(component);
        if (delegate != null) {
            return delegate.getOffscreenBuffer(component, i2, i3);
        }
        return _getOffscreenBuffer(component, i2, i3);
    }

    public Image getVolatileOffscreenBuffer(Component component, int i2, int i3) {
        RepaintManager delegate = getDelegate(component);
        if (delegate != null) {
            return delegate.getVolatileOffscreenBuffer(component, i2, i3);
        }
        if (!(component instanceof Window ? (Window) component : SwingUtilities.getWindowAncestor(component)).isOpaque()) {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if ((defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).needUpdateWindow()) {
                return null;
            }
        }
        GraphicsConfiguration graphicsConfiguration = component.getGraphicsConfiguration();
        if (graphicsConfiguration == null) {
            graphicsConfiguration = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        }
        Dimension doubleBufferMaximumSize = getDoubleBufferMaximumSize();
        int i4 = i2 < 1 ? 1 : i2 > doubleBufferMaximumSize.width ? doubleBufferMaximumSize.width : i2;
        int i5 = i3 < 1 ? 1 : i3 > doubleBufferMaximumSize.height ? doubleBufferMaximumSize.height : i3;
        VolatileImage volatileImageCreateCompatibleVolatileImage = this.volatileMap.get(graphicsConfiguration);
        if (volatileImageCreateCompatibleVolatileImage == null || volatileImageCreateCompatibleVolatileImage.getWidth() < i4 || volatileImageCreateCompatibleVolatileImage.getHeight() < i5) {
            if (volatileImageCreateCompatibleVolatileImage != null) {
                volatileImageCreateCompatibleVolatileImage.flush();
            }
            volatileImageCreateCompatibleVolatileImage = graphicsConfiguration.createCompatibleVolatileImage(i4, i5, volatileBufferType);
            this.volatileMap.put(graphicsConfiguration, volatileImageCreateCompatibleVolatileImage);
        }
        return volatileImageCreateCompatibleVolatileImage;
    }

    private Image _getOffscreenBuffer(Component component, int i2, int i3) {
        Dimension doubleBufferMaximumSize = getDoubleBufferMaximumSize();
        if (!(component instanceof Window ? (Window) component : SwingUtilities.getWindowAncestor(component)).isOpaque()) {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if ((defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).needUpdateWindow()) {
                return null;
            }
        }
        if (this.standardDoubleBuffer == null) {
            this.standardDoubleBuffer = new DoubleBufferInfo();
        }
        DoubleBufferInfo doubleBufferInfo = this.standardDoubleBuffer;
        int iMax = i2 < 1 ? 1 : i2 > doubleBufferMaximumSize.width ? doubleBufferMaximumSize.width : i2;
        int iMax2 = i3 < 1 ? 1 : i3 > doubleBufferMaximumSize.height ? doubleBufferMaximumSize.height : i3;
        if (doubleBufferInfo.needsReset || (doubleBufferInfo.image != null && (doubleBufferInfo.size.width < iMax || doubleBufferInfo.size.height < iMax2))) {
            doubleBufferInfo.needsReset = false;
            if (doubleBufferInfo.image != null) {
                doubleBufferInfo.image.flush();
                doubleBufferInfo.image = null;
            }
            iMax = Math.max(doubleBufferInfo.size.width, iMax);
            iMax2 = Math.max(doubleBufferInfo.size.height, iMax2);
        }
        Image imageCreateImage = doubleBufferInfo.image;
        if (doubleBufferInfo.image == null) {
            imageCreateImage = component.createImage(iMax, iMax2);
            doubleBufferInfo.size = new Dimension(iMax, iMax2);
            if (component instanceof JComponent) {
                ((JComponent) component).setCreatedDoubleBuffer(true);
                doubleBufferInfo.image = imageCreateImage;
            }
        }
        return imageCreateImage;
    }

    public void setDoubleBufferMaximumSize(Dimension dimension) {
        this.doubleBufferMaxSize = dimension;
        if (this.doubleBufferMaxSize == null) {
            clearImages();
        } else {
            clearImages(dimension.width, dimension.height);
        }
    }

    private void clearImages() {
        clearImages(0, 0);
    }

    private void clearImages(int i2, int i3) {
        if (this.standardDoubleBuffer != null && this.standardDoubleBuffer.image != null && (this.standardDoubleBuffer.image.getWidth(null) > i2 || this.standardDoubleBuffer.image.getHeight(null) > i3)) {
            this.standardDoubleBuffer.image.flush();
            this.standardDoubleBuffer.image = null;
        }
        Iterator<GraphicsConfiguration> it = this.volatileMap.keySet().iterator();
        while (it.hasNext()) {
            VolatileImage volatileImage = this.volatileMap.get(it.next());
            if (volatileImage.getWidth() > i2 || volatileImage.getHeight() > i3) {
                volatileImage.flush();
                it.remove();
            }
        }
    }

    public Dimension getDoubleBufferMaximumSize() {
        if (this.doubleBufferMaxSize == null) {
            try {
                Rectangle rectangle = new Rectangle();
                for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
                    rectangle = rectangle.union(graphicsDevice.getDefaultConfiguration().getBounds());
                }
                this.doubleBufferMaxSize = new Dimension(rectangle.width, rectangle.height);
            } catch (HeadlessException e2) {
                this.doubleBufferMaxSize = new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        }
        return this.doubleBufferMaxSize;
    }

    public void setDoubleBufferingEnabled(boolean z2) {
        this.doubleBufferingEnabled = z2;
        PaintManager paintManager = getPaintManager();
        if (!z2 && paintManager.getClass() != PaintManager.class) {
            setPaintManager(new PaintManager());
        }
    }

    public boolean isDoubleBufferingEnabled() {
        return this.doubleBufferingEnabled;
    }

    void resetDoubleBuffer() {
        if (this.standardDoubleBuffer != null) {
            this.standardDoubleBuffer.needsReset = true;
        }
    }

    void resetVolatileDoubleBuffer(GraphicsConfiguration graphicsConfiguration) {
        VolatileImage volatileImageRemove = this.volatileMap.remove(graphicsConfiguration);
        if (volatileImageRemove != null) {
            volatileImageRemove.flush();
        }
    }

    boolean useVolatileDoubleBuffer() {
        return volatileImageBufferEnabled;
    }

    private synchronized boolean isPaintingThread() {
        return Thread.currentThread() == this.paintThread;
    }

    void paint(JComponent jComponent, JComponent jComponent2, Graphics graphics, int i2, int i3, int i4, int i5) {
        PaintManager paintManager = getPaintManager();
        if (!isPaintingThread() && paintManager.getClass() != PaintManager.class) {
            paintManager = new PaintManager();
            paintManager.repaintManager = this;
        }
        if (!paintManager.paint(jComponent, jComponent2, graphics, i2, i3, i4, i5)) {
            graphics.setClip(i2, i3, i4, i5);
            jComponent.paintToOffscreen(graphics, i2, i3, i4, i5, i2 + i4, i3 + i5);
        }
    }

    void copyArea(JComponent jComponent, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        getPaintManager().copyArea(jComponent, graphics, i2, i3, i4, i5, i6, i7, z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addRepaintListener(SwingUtilities2.RepaintListener repaintListener) {
        this.repaintListeners.add(repaintListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeRepaintListener(SwingUtilities2.RepaintListener repaintListener) {
        this.repaintListeners.remove(repaintListener);
    }

    void notifyRepaintPerformed(JComponent jComponent, int i2, int i3, int i4, int i5) {
        Iterator<SwingUtilities2.RepaintListener> it = this.repaintListeners.iterator();
        while (it.hasNext()) {
            it.next().repaintPerformed(jComponent, i2, i3, i4, i5);
        }
    }

    void beginPaint() {
        int i2;
        boolean z2 = false;
        Thread threadCurrentThread = Thread.currentThread();
        synchronized (this) {
            i2 = this.paintDepth;
            if (this.paintThread == null || threadCurrentThread == this.paintThread) {
                this.paintThread = threadCurrentThread;
                this.paintDepth++;
            } else {
                z2 = true;
            }
        }
        if (!z2 && i2 == 0) {
            getPaintManager().beginPaint();
        }
    }

    void endPaint() {
        if (isPaintingThread()) {
            PaintManager paintManager = null;
            synchronized (this) {
                int i2 = this.paintDepth - 1;
                this.paintDepth = i2;
                if (i2 == 0) {
                    paintManager = getPaintManager();
                }
            }
            if (paintManager != null) {
                paintManager.endPaint();
                synchronized (this) {
                    this.paintThread = null;
                }
            }
        }
    }

    boolean show(Container container, int i2, int i3, int i4, int i5) {
        return getPaintManager().show(container, i2, i3, i4, i5);
    }

    void doubleBufferingChanged(JRootPane jRootPane) {
        getPaintManager().doubleBufferingChanged(jRootPane);
    }

    void setPaintManager(PaintManager paintManager) {
        PaintManager paintManager2;
        if (paintManager == null) {
            paintManager = new PaintManager();
        }
        synchronized (this) {
            paintManager2 = this.paintManager;
            this.paintManager = paintManager;
            paintManager.repaintManager = this;
        }
        if (paintManager2 != null) {
            paintManager2.dispose();
        }
    }

    private synchronized PaintManager getPaintManager() {
        if (this.paintManager == null) {
            BufferStrategyPaintManager bufferStrategyPaintManager = null;
            if (this.doubleBufferingEnabled && !nativeDoubleBuffering) {
                switch (this.bufferStrategyType) {
                    case 0:
                        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
                        if ((defaultToolkit instanceof SunToolkit) && ((SunToolkit) defaultToolkit).useBufferPerWindow()) {
                            bufferStrategyPaintManager = new BufferStrategyPaintManager();
                            break;
                        }
                        break;
                    case 1:
                        bufferStrategyPaintManager = new BufferStrategyPaintManager();
                        break;
                }
            }
            setPaintManager(bufferStrategyPaintManager);
        }
        return this.paintManager;
    }

    private void scheduleProcessingRunnable(AppContext appContext) {
        if (this.processingRunnable.markPending()) {
            if (Toolkit.getDefaultToolkit() instanceof SunToolkit) {
                SunToolkit.getSystemEventQueueImplPP(appContext).postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), this.processingRunnable));
            } else {
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), this.processingRunnable));
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/RepaintManager$PaintManager.class */
    static class PaintManager {
        protected RepaintManager repaintManager;
        boolean isRepaintingRoot;
        static final /* synthetic */ boolean $assertionsDisabled;

        PaintManager() {
        }

        static {
            $assertionsDisabled = !RepaintManager.class.desiredAssertionStatus();
        }

        public boolean paint(JComponent jComponent, JComponent jComponent2, Graphics graphics, int i2, int i3, int i4, int i5) {
            Image validImage;
            Image validImage2;
            boolean z2 = false;
            if (this.repaintManager.useVolatileDoubleBuffer() && (validImage2 = getValidImage(this.repaintManager.getVolatileOffscreenBuffer(jComponent2, i4, i5))) != null) {
                VolatileImage volatileImage = (VolatileImage) validImage2;
                GraphicsConfiguration graphicsConfiguration = jComponent2.getGraphicsConfiguration();
                for (int i6 = 0; !z2 && i6 < 2; i6++) {
                    if (volatileImage.validate(graphicsConfiguration) == 2) {
                        this.repaintManager.resetVolatileDoubleBuffer(graphicsConfiguration);
                        volatileImage = (VolatileImage) this.repaintManager.getVolatileOffscreenBuffer(jComponent2, i4, i5);
                    }
                    paintDoubleBuffered(jComponent, volatileImage, graphics, i2, i3, i4, i5);
                    z2 = !volatileImage.contentsLost();
                }
            }
            if (!z2 && (validImage = getValidImage(this.repaintManager.getOffscreenBuffer(jComponent2, i4, i5))) != null) {
                paintDoubleBuffered(jComponent, validImage, graphics, i2, i3, i4, i5);
                z2 = true;
            }
            return z2;
        }

        public void copyArea(JComponent jComponent, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
            graphics.copyArea(i2, i3, i4, i5, i6, i7);
        }

        public void beginPaint() {
        }

        public void endPaint() {
        }

        public boolean show(Container container, int i2, int i3, int i4, int i5) {
            return false;
        }

        public void doubleBufferingChanged(JRootPane jRootPane) {
        }

        protected void paintDoubleBuffered(JComponent jComponent, Image image, Graphics graphics, int i2, int i3, int i4, int i5) {
            Graphics graphics2 = image.getGraphics();
            int iMin = Math.min(i4, image.getWidth(null));
            int iMin2 = Math.min(i5, image.getHeight(null));
            try {
                int i6 = i2 + i4;
                for (int i7 = i2; i7 < i6; i7 += iMin) {
                    int i8 = i3 + i5;
                    for (int i9 = i3; i9 < i8; i9 += iMin2) {
                        graphics2.translate(-i7, -i9);
                        graphics2.setClip(i7, i9, iMin, iMin2);
                        if (RepaintManager.volatileBufferType != 1 && (graphics2 instanceof Graphics2D)) {
                            Graphics2D graphics2D = (Graphics2D) graphics2;
                            Color background = graphics2D.getBackground();
                            graphics2D.setBackground(jComponent.getBackground());
                            graphics2D.clearRect(i7, i9, iMin, iMin2);
                            graphics2D.setBackground(background);
                        }
                        jComponent.paintToOffscreen(graphics2, i7, i9, iMin, iMin2, i6, i8);
                        graphics.setClip(i7, i9, iMin, iMin2);
                        if (RepaintManager.volatileBufferType != 1 && (graphics instanceof Graphics2D)) {
                            Graphics2D graphics2D2 = (Graphics2D) graphics;
                            Composite composite = graphics2D2.getComposite();
                            graphics2D2.setComposite(AlphaComposite.Src);
                            graphics2D2.drawImage(image, i7, i9, jComponent);
                            graphics2D2.setComposite(composite);
                        } else {
                            graphics.drawImage(image, i7, i9, jComponent);
                        }
                        graphics2.translate(i7, i9);
                    }
                }
            } finally {
                graphics2.dispose();
            }
        }

        private Image getValidImage(Image image) {
            if (image != null && image.getWidth(null) > 0 && image.getHeight(null) > 0) {
                return image;
            }
            return null;
        }

        protected void repaintRoot(JComponent jComponent) {
            if (!$assertionsDisabled && this.repaintManager.repaintRoot != null) {
                throw new AssertionError();
            }
            if (this.repaintManager.painting) {
                this.repaintManager.repaintRoot = jComponent;
            } else {
                jComponent.repaint();
            }
        }

        protected boolean isRepaintingRoot() {
            return this.isRepaintingRoot;
        }

        protected void dispose() {
        }
    }

    /* loaded from: rt.jar:javax/swing/RepaintManager$DoubleBufferInfo.class */
    private class DoubleBufferInfo {
        public Image image;
        public Dimension size;
        public boolean needsReset;

        private DoubleBufferInfo() {
            this.needsReset = false;
        }
    }

    /* loaded from: rt.jar:javax/swing/RepaintManager$DisplayChangedHandler.class */
    private static final class DisplayChangedHandler implements DisplayChangedListener {
        DisplayChangedHandler() {
        }

        @Override // sun.awt.DisplayChangedListener
        public void displayChanged() {
            scheduleDisplayChanges();
        }

        @Override // sun.awt.DisplayChangedListener
        public void paletteChanged() {
        }

        private static void scheduleDisplayChanges() {
            EventQueue eventQueue;
            for (AppContext appContext : AppContext.getAppContexts()) {
                synchronized (appContext) {
                    if (!appContext.isDisposed() && (eventQueue = (EventQueue) appContext.get(AppContext.EVENT_QUEUE_KEY)) != null) {
                        eventQueue.postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), new DisplayChangedRunnable()));
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/RepaintManager$DisplayChangedRunnable.class */
    private static final class DisplayChangedRunnable implements Runnable {
        private DisplayChangedRunnable() {
        }

        @Override // java.lang.Runnable
        public void run() {
            RepaintManager.currentManager((JComponent) null).displayChanged();
        }
    }

    /* loaded from: rt.jar:javax/swing/RepaintManager$ProcessingRunnable.class */
    private final class ProcessingRunnable implements Runnable {
        private boolean pending;

        private ProcessingRunnable() {
        }

        public synchronized boolean markPending() {
            if (!this.pending) {
                this.pending = true;
                return true;
            }
            return false;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (this) {
                this.pending = false;
            }
            RepaintManager.this.scheduleHeavyWeightPaints();
            RepaintManager.this.validateInvalidComponents();
            RepaintManager.this.prePaintDirtyRegions();
        }
    }

    private RepaintManager getDelegate(Component component) {
        RepaintManager delegateRepaintManager = SwingUtilities3.getDelegateRepaintManager(component);
        if (this == delegateRepaintManager) {
            delegateRepaintManager = null;
        }
        return delegateRepaintManager;
    }
}
