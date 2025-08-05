package javax.swing;

import com.sun.java.swing.SwingUtilities3;
import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.ImageCapabilities;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.RepaintManager;
import sun.awt.SubRegionShowable;
import sun.awt.SunToolkit;
import sun.java2d.SunGraphics2D;
import sun.java2d.pipe.hw.ExtendedBufferCapabilities;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:javax/swing/BufferStrategyPaintManager.class */
class BufferStrategyPaintManager extends RepaintManager.PaintManager {
    private static Method COMPONENT_CREATE_BUFFER_STRATEGY_METHOD;
    private static Method COMPONENT_GET_BUFFER_STRATEGY_METHOD;
    private static final PlatformLogger LOGGER = PlatformLogger.getLogger("javax.swing.BufferStrategyPaintManager");
    private ArrayList<BufferInfo> bufferInfos = new ArrayList<>(1);
    private boolean painting;
    private boolean showing;
    private int accumulatedX;
    private int accumulatedY;
    private int accumulatedMaxX;
    private int accumulatedMaxY;
    private JComponent rootJ;
    private int xOffset;
    private int yOffset;
    private Graphics bsg;
    private BufferStrategy bufferStrategy;
    private BufferInfo bufferInfo;
    private boolean disposeBufferOnEnd;

    /* JADX INFO: Access modifiers changed from: private */
    public static Method getGetBufferStrategyMethod() {
        if (COMPONENT_GET_BUFFER_STRATEGY_METHOD == null) {
            getMethods();
        }
        return COMPONENT_GET_BUFFER_STRATEGY_METHOD;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Method getCreateBufferStrategyMethod() {
        if (COMPONENT_CREATE_BUFFER_STRATEGY_METHOD == null) {
            getMethods();
        }
        return COMPONENT_CREATE_BUFFER_STRATEGY_METHOD;
    }

    private static void getMethods() {
        AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.swing.BufferStrategyPaintManager.1
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !BufferStrategyPaintManager.class.desiredAssertionStatus();
            }

            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                try {
                    Method unused = BufferStrategyPaintManager.COMPONENT_CREATE_BUFFER_STRATEGY_METHOD = Component.class.getDeclaredMethod("createBufferStrategy", Integer.TYPE, BufferCapabilities.class);
                    BufferStrategyPaintManager.COMPONENT_CREATE_BUFFER_STRATEGY_METHOD.setAccessible(true);
                    Method unused2 = BufferStrategyPaintManager.COMPONENT_GET_BUFFER_STRATEGY_METHOD = Component.class.getDeclaredMethod("getBufferStrategy", new Class[0]);
                    BufferStrategyPaintManager.COMPONENT_GET_BUFFER_STRATEGY_METHOD.setAccessible(true);
                    return null;
                } catch (NoSuchMethodException e2) {
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError();
                } catch (SecurityException e3) {
                    if ($assertionsDisabled) {
                        return null;
                    }
                    throw new AssertionError();
                }
            }
        });
    }

    BufferStrategyPaintManager() {
    }

    @Override // javax.swing.RepaintManager.PaintManager
    protected void dispose() {
        SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.BufferStrategyPaintManager.2
            @Override // java.lang.Runnable
            public void run() {
                ArrayList arrayList;
                synchronized (BufferStrategyPaintManager.this) {
                    while (BufferStrategyPaintManager.this.showing) {
                        try {
                            BufferStrategyPaintManager.this.wait();
                        } catch (InterruptedException e2) {
                        }
                    }
                    arrayList = BufferStrategyPaintManager.this.bufferInfos;
                    BufferStrategyPaintManager.this.bufferInfos = null;
                }
                BufferStrategyPaintManager.this.dispose(arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispose(List<BufferInfo> list) {
        if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
            LOGGER.finer("BufferStrategyPaintManager disposed", new RuntimeException());
        }
        if (list != null) {
            Iterator<BufferInfo> it = list.iterator();
            while (it.hasNext()) {
                it.next().dispose();
            }
        }
    }

    @Override // javax.swing.RepaintManager.PaintManager
    public boolean show(Container container, int i2, int i3, int i4, int i5) {
        Object bufferStrategy;
        synchronized (this) {
            if (this.painting) {
                return false;
            }
            this.showing = true;
            try {
                BufferInfo bufferInfo = getBufferInfo(container);
                if (bufferInfo != null && bufferInfo.isInSync() && (bufferStrategy = bufferInfo.getBufferStrategy(false)) != null) {
                    SubRegionShowable subRegionShowable = (SubRegionShowable) bufferStrategy;
                    boolean paintAllOnExpose = bufferInfo.getPaintAllOnExpose();
                    bufferInfo.setPaintAllOnExpose(false);
                    if (subRegionShowable.showIfNotLost(i2, i3, i2 + i4, i3 + i5)) {
                        boolean z2 = !paintAllOnExpose;
                        synchronized (this) {
                            this.showing = false;
                            notifyAll();
                        }
                        return z2;
                    }
                    this.bufferInfo.setContentsLostDuringExpose(true);
                }
                synchronized (this) {
                    this.showing = false;
                    notifyAll();
                }
                return false;
            } catch (Throwable th) {
                synchronized (this) {
                    this.showing = false;
                    notifyAll();
                    throw th;
                }
            }
        }
    }

    @Override // javax.swing.RepaintManager.PaintManager
    public boolean paint(JComponent jComponent, JComponent jComponent2, Graphics graphics, int i2, int i3, int i4, int i5) {
        Container containerFetchRoot = fetchRoot(jComponent);
        if (prepare(jComponent, containerFetchRoot, true, i2, i3, i4, i5)) {
            if ((graphics instanceof SunGraphics2D) && ((SunGraphics2D) graphics).getDestination() == containerFetchRoot) {
                int i6 = ((SunGraphics2D) this.bsg).constrainX;
                int i7 = ((SunGraphics2D) this.bsg).constrainY;
                if (i6 != 0 || i7 != 0) {
                    this.bsg.translate(-i6, -i7);
                }
                ((SunGraphics2D) this.bsg).constrain(this.xOffset + i6, this.yOffset + i7, i2 + i4, i3 + i5);
                this.bsg.setClip(i2, i3, i4, i5);
                jComponent.paintToOffscreen(this.bsg, i2, i3, i4, i5, i2 + i4, i3 + i5);
                accumulate(this.xOffset + i2, this.yOffset + i3, i4, i5);
                return true;
            }
            this.bufferInfo.setInSync(false);
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
            LOGGER.finer("prepare failed");
        }
        return super.paint(jComponent, jComponent2, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.RepaintManager.PaintManager
    public void copyArea(JComponent jComponent, Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7, boolean z2) {
        if (prepare(jComponent, fetchRoot(jComponent), false, 0, 0, 0, 0) && this.bufferInfo.isInSync()) {
            if (z2) {
                Rectangle visibleRect = jComponent.getVisibleRect();
                int i8 = this.xOffset + i2;
                int i9 = this.yOffset + i3;
                this.bsg.clipRect(this.xOffset + visibleRect.f12372x, this.yOffset + visibleRect.f12373y, visibleRect.width, visibleRect.height);
                this.bsg.copyArea(i8, i9, i4, i5, i6, i7);
            } else {
                this.bsg.copyArea(this.xOffset + i2, this.yOffset + i3, i4, i5, i6, i7);
            }
            accumulate(i2 + this.xOffset + i6, i3 + this.yOffset + i7, i4, i5);
            return;
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
            LOGGER.finer("copyArea: prepare failed or not in sync");
        }
        if (!flushAccumulatedRegion()) {
            this.rootJ.repaint();
        } else {
            super.copyArea(jComponent, graphics, i2, i3, i4, i5, i6, i7, z2);
        }
    }

    @Override // javax.swing.RepaintManager.PaintManager
    public void beginPaint() {
        synchronized (this) {
            this.painting = true;
            while (this.showing) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                }
            }
        }
        if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
            LOGGER.finest("beginPaint");
        }
        resetAccumulated();
    }

    @Override // javax.swing.RepaintManager.PaintManager
    public void endPaint() {
        if (LOGGER.isLoggable(PlatformLogger.Level.FINEST)) {
            LOGGER.finest("endPaint: region " + this.accumulatedX + " " + this.accumulatedY + " " + this.accumulatedMaxX + " " + this.accumulatedMaxY);
        }
        if (this.painting && !flushAccumulatedRegion()) {
            if (!isRepaintingRoot()) {
                repaintRoot(this.rootJ);
            } else {
                resetDoubleBufferPerWindow();
                this.rootJ.repaint();
            }
        }
        BufferInfo bufferInfo = null;
        synchronized (this) {
            this.painting = false;
            if (this.disposeBufferOnEnd) {
                this.disposeBufferOnEnd = false;
                bufferInfo = this.bufferInfo;
                this.bufferInfos.remove(bufferInfo);
            }
        }
        if (bufferInfo != null) {
            bufferInfo.dispose();
        }
    }

    private boolean flushAccumulatedRegion() {
        boolean z2 = true;
        if (this.accumulatedX != Integer.MAX_VALUE) {
            SubRegionShowable subRegionShowable = (SubRegionShowable) this.bufferStrategy;
            boolean zContentsLost = this.bufferStrategy.contentsLost();
            if (!zContentsLost) {
                subRegionShowable.show(this.accumulatedX, this.accumulatedY, this.accumulatedMaxX, this.accumulatedMaxY);
                zContentsLost = this.bufferStrategy.contentsLost();
            }
            if (zContentsLost) {
                if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                    LOGGER.finer("endPaint: contents lost");
                }
                this.bufferInfo.setInSync(false);
                z2 = false;
            }
        }
        resetAccumulated();
        return z2;
    }

    private void resetAccumulated() {
        this.accumulatedX = Integer.MAX_VALUE;
        this.accumulatedY = Integer.MAX_VALUE;
        this.accumulatedMaxX = 0;
        this.accumulatedMaxY = 0;
    }

    @Override // javax.swing.RepaintManager.PaintManager
    public void doubleBufferingChanged(final JRootPane jRootPane) {
        if ((!jRootPane.isDoubleBuffered() || !jRootPane.getUseTrueDoubleBuffering()) && jRootPane.getParent() != null) {
            if (!SwingUtilities.isEventDispatchThread()) {
                SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.BufferStrategyPaintManager.3
                    @Override // java.lang.Runnable
                    public void run() {
                        BufferStrategyPaintManager.this.doubleBufferingChanged0(jRootPane);
                    }
                });
            } else {
                doubleBufferingChanged0(jRootPane);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doubleBufferingChanged0(JRootPane jRootPane) {
        BufferInfo bufferInfo;
        synchronized (this) {
            while (this.showing) {
                try {
                    wait();
                } catch (InterruptedException e2) {
                }
            }
            bufferInfo = getBufferInfo(jRootPane.getParent());
            if (this.painting && this.bufferInfo == bufferInfo) {
                this.disposeBufferOnEnd = true;
                bufferInfo = null;
            } else if (bufferInfo != null) {
                this.bufferInfos.remove(bufferInfo);
            }
        }
        if (bufferInfo != null) {
            bufferInfo.dispose();
        }
    }

    private boolean prepare(JComponent jComponent, Container container, boolean z2, int i2, int i3, int i4, int i5) {
        if (this.bsg != null) {
            this.bsg.dispose();
            this.bsg = null;
        }
        this.bufferStrategy = null;
        if (container != null) {
            boolean z3 = false;
            BufferInfo bufferInfo = getBufferInfo(container);
            if (bufferInfo == null) {
                z3 = true;
                bufferInfo = new BufferInfo(container);
                this.bufferInfos.add(bufferInfo);
                if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                    LOGGER.finer("prepare: new BufferInfo: " + ((Object) container));
                }
            }
            this.bufferInfo = bufferInfo;
            if (!bufferInfo.hasBufferStrategyChanged()) {
                this.bufferStrategy = bufferInfo.getBufferStrategy(true);
                if (this.bufferStrategy != null) {
                    this.bsg = this.bufferStrategy.getDrawGraphics();
                    if (this.bufferStrategy.contentsRestored()) {
                        z3 = true;
                        if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                            LOGGER.finer("prepare: contents restored in prepare");
                        }
                    }
                    if (bufferInfo.getContentsLostDuringExpose()) {
                        z3 = true;
                        bufferInfo.setContentsLostDuringExpose(false);
                        if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                            LOGGER.finer("prepare: contents lost on expose");
                        }
                    }
                    if (z2 && jComponent == this.rootJ && i2 == 0 && i3 == 0 && jComponent.getWidth() == i4 && jComponent.getHeight() == i5) {
                        bufferInfo.setInSync(true);
                    } else if (z3) {
                        bufferInfo.setInSync(false);
                        if (!isRepaintingRoot()) {
                            repaintRoot(this.rootJ);
                        } else {
                            resetDoubleBufferPerWindow();
                        }
                    }
                    return this.bufferInfos != null;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private Container fetchRoot(JComponent jComponent) {
        boolean z2 = false;
        this.rootJ = jComponent;
        JComponent parent = jComponent;
        this.yOffset = 0;
        this.xOffset = 0;
        while (parent != null && !(parent instanceof Window) && !SunToolkit.isInstanceOf(parent, "java.applet.Applet")) {
            this.xOffset += parent.getX();
            this.yOffset += parent.getY();
            parent = parent.getParent();
            if (parent != null) {
                if (parent instanceof JComponent) {
                    this.rootJ = parent;
                } else if (parent.isLightweight()) {
                    continue;
                } else if (!z2) {
                    z2 = true;
                } else {
                    return null;
                }
            }
        }
        if ((parent instanceof RootPaneContainer) && (this.rootJ instanceof JRootPane) && this.rootJ.isDoubleBuffered() && ((JRootPane) this.rootJ).getUseTrueDoubleBuffering()) {
            return parent;
        }
        return null;
    }

    private void resetDoubleBufferPerWindow() {
        if (this.bufferInfos != null) {
            dispose(this.bufferInfos);
            this.bufferInfos = null;
            this.repaintManager.setPaintManager(null);
        }
    }

    private BufferInfo getBufferInfo(Container container) {
        for (int size = this.bufferInfos.size() - 1; size >= 0; size--) {
            BufferInfo bufferInfo = this.bufferInfos.get(size);
            Container root = bufferInfo.getRoot();
            if (root == null) {
                this.bufferInfos.remove(size);
                if (LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                    LOGGER.finer("BufferInfo pruned, root null");
                }
            } else if (root == container) {
                return bufferInfo;
            }
        }
        return null;
    }

    private void accumulate(int i2, int i3, int i4, int i5) {
        this.accumulatedX = Math.min(i2, this.accumulatedX);
        this.accumulatedY = Math.min(i3, this.accumulatedY);
        this.accumulatedMaxX = Math.max(this.accumulatedMaxX, i2 + i4);
        this.accumulatedMaxY = Math.max(this.accumulatedMaxY, i3 + i5);
    }

    /* loaded from: rt.jar:javax/swing/BufferStrategyPaintManager$BufferInfo.class */
    private class BufferInfo extends ComponentAdapter implements WindowListener {
        private WeakReference<BufferStrategy> weakBS;
        private WeakReference<Container> root;
        private boolean inSync;
        private boolean contentsLostDuringExpose;
        private boolean paintAllOnExpose;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BufferStrategyPaintManager.class.desiredAssertionStatus();
        }

        public BufferInfo(Container container) {
            this.root = new WeakReference<>(container);
            container.addComponentListener(this);
            if (container instanceof Window) {
                ((Window) container).addWindowListener(this);
            }
        }

        public void setPaintAllOnExpose(boolean z2) {
            this.paintAllOnExpose = z2;
        }

        public boolean getPaintAllOnExpose() {
            return this.paintAllOnExpose;
        }

        public void setContentsLostDuringExpose(boolean z2) {
            this.contentsLostDuringExpose = z2;
        }

        public boolean getContentsLostDuringExpose() {
            return this.contentsLostDuringExpose;
        }

        public void setInSync(boolean z2) {
            this.inSync = z2;
        }

        public boolean isInSync() {
            return this.inSync;
        }

        public Container getRoot() {
            if (this.root == null) {
                return null;
            }
            return this.root.get();
        }

        public BufferStrategy getBufferStrategy(boolean z2) {
            BufferStrategy bufferStrategyCreateBufferStrategy = this.weakBS == null ? null : this.weakBS.get();
            if (bufferStrategyCreateBufferStrategy == null && z2) {
                bufferStrategyCreateBufferStrategy = createBufferStrategy();
                if (bufferStrategyCreateBufferStrategy != null) {
                    this.weakBS = new WeakReference<>(bufferStrategyCreateBufferStrategy);
                }
                if (BufferStrategyPaintManager.LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                    BufferStrategyPaintManager.LOGGER.finer("getBufferStrategy: created bs: " + ((Object) bufferStrategyCreateBufferStrategy));
                }
            }
            return bufferStrategyCreateBufferStrategy;
        }

        public boolean hasBufferStrategyChanged() {
            Container root = getRoot();
            if (root != null) {
                BufferStrategy bufferStrategy = null;
                BufferStrategy bufferStrategy2 = getBufferStrategy(false);
                if (!(root instanceof Window)) {
                    try {
                        bufferStrategy = (BufferStrategy) BufferStrategyPaintManager.getGetBufferStrategyMethod().invoke(root, new Object[0]);
                    } catch (IllegalAccessException e2) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    } catch (IllegalArgumentException e3) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    } catch (InvocationTargetException e4) {
                        if (!$assertionsDisabled) {
                            throw new AssertionError();
                        }
                    }
                } else {
                    bufferStrategy = ((Window) root).getBufferStrategy();
                }
                if (bufferStrategy != bufferStrategy2) {
                    if (bufferStrategy2 != null) {
                        bufferStrategy2.dispose();
                    }
                    this.weakBS = null;
                    return true;
                }
                return false;
            }
            return false;
        }

        private BufferStrategy createBufferStrategy() {
            Container root = getRoot();
            if (root == null) {
                return null;
            }
            BufferStrategy bufferStrategyCreateBufferStrategy = null;
            if (SwingUtilities3.isVsyncRequested(root)) {
                bufferStrategyCreateBufferStrategy = createBufferStrategy(root, true);
                if (BufferStrategyPaintManager.LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                    BufferStrategyPaintManager.LOGGER.finer("createBufferStrategy: using vsynced strategy");
                }
            }
            if (bufferStrategyCreateBufferStrategy == null) {
                bufferStrategyCreateBufferStrategy = createBufferStrategy(root, false);
            }
            if (!(bufferStrategyCreateBufferStrategy instanceof SubRegionShowable)) {
                bufferStrategyCreateBufferStrategy = null;
            }
            return bufferStrategyCreateBufferStrategy;
        }

        private BufferStrategy createBufferStrategy(Container container, boolean z2) {
            BufferCapabilities extendedBufferCapabilities = z2 ? new ExtendedBufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), BufferCapabilities.FlipContents.COPIED, ExtendedBufferCapabilities.VSyncType.VSYNC_ON) : new BufferCapabilities(new ImageCapabilities(true), new ImageCapabilities(true), null);
            BufferStrategy bufferStrategy = null;
            if (SunToolkit.isInstanceOf(container, "java.applet.Applet")) {
                try {
                    BufferStrategyPaintManager.getCreateBufferStrategyMethod().invoke(container, 2, extendedBufferCapabilities);
                    bufferStrategy = (BufferStrategy) BufferStrategyPaintManager.getGetBufferStrategyMethod().invoke(container, new Object[0]);
                } catch (IllegalAccessException e2) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                } catch (IllegalArgumentException e3) {
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                } catch (InvocationTargetException e4) {
                    if (BufferStrategyPaintManager.LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                        BufferStrategyPaintManager.LOGGER.finer("createBufferStratety failed", e4);
                    }
                }
            } else {
                try {
                    ((Window) container).createBufferStrategy(2, extendedBufferCapabilities);
                    bufferStrategy = ((Window) container).getBufferStrategy();
                } catch (AWTException e5) {
                    if (BufferStrategyPaintManager.LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                        BufferStrategyPaintManager.LOGGER.finer("createBufferStratety failed", e5);
                    }
                }
            }
            return bufferStrategy;
        }

        public void dispose() {
            Container root = getRoot();
            if (BufferStrategyPaintManager.LOGGER.isLoggable(PlatformLogger.Level.FINER)) {
                BufferStrategyPaintManager.LOGGER.finer("disposed BufferInfo for: " + ((Object) root));
            }
            if (root != null) {
                root.removeComponentListener(this);
                if (root instanceof Window) {
                    ((Window) root).removeWindowListener(this);
                }
                BufferStrategy bufferStrategy = getBufferStrategy(false);
                if (bufferStrategy != null) {
                    bufferStrategy.dispose();
                }
            }
            this.root = null;
            this.weakBS = null;
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
            Container root = getRoot();
            if (root != null && root.isVisible()) {
                root.repaint();
            } else {
                setPaintAllOnExpose(true);
            }
        }

        @Override // java.awt.event.WindowListener
        public void windowIconified(WindowEvent windowEvent) {
            setPaintAllOnExpose(true);
        }

        @Override // java.awt.event.WindowListener
        public void windowClosed(WindowEvent windowEvent) {
            synchronized (BufferStrategyPaintManager.this) {
                while (BufferStrategyPaintManager.this.showing) {
                    try {
                        BufferStrategyPaintManager.this.wait();
                    } catch (InterruptedException e2) {
                    }
                }
                BufferStrategyPaintManager.this.bufferInfos.remove(this);
            }
            dispose();
        }

        @Override // java.awt.event.WindowListener
        public void windowOpened(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
        }
    }
}
