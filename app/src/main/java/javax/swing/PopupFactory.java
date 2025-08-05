package javax.swing;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Popup;
import sun.awt.EmbeddedFrame;
import sun.awt.OSInfo;

/* loaded from: rt.jar:javax/swing/PopupFactory.class */
public class PopupFactory {
    private static final Object SharedInstanceKey = new StringBuffer("PopupFactory.SharedInstanceKey");
    private static final int MAX_CACHE_SIZE = 5;
    static final int LIGHT_WEIGHT_POPUP = 0;
    static final int MEDIUM_WEIGHT_POPUP = 1;
    static final int HEAVY_WEIGHT_POPUP = 2;
    private int popupType = 0;

    public static void setSharedInstance(PopupFactory popupFactory) {
        if (popupFactory == null) {
            throw new IllegalArgumentException("PopupFactory can not be null");
        }
        SwingUtilities.appContextPut(SharedInstanceKey, popupFactory);
    }

    public static PopupFactory getSharedInstance() {
        PopupFactory popupFactory = (PopupFactory) SwingUtilities.appContextGet(SharedInstanceKey);
        if (popupFactory == null) {
            popupFactory = new PopupFactory();
            setSharedInstance(popupFactory);
        }
        return popupFactory;
    }

    void setPopupType(int i2) {
        this.popupType = i2;
    }

    int getPopupType() {
        return this.popupType;
    }

    public Popup getPopup(Component component, Component component2, int i2, int i3) throws IllegalArgumentException {
        if (component2 == null) {
            throw new IllegalArgumentException("Popup.getPopup must be passed non-null contents");
        }
        Popup popup = getPopup(component, component2, i2, i3, getPopupType(component, component2, i2, i3));
        if (popup == null) {
            popup = getPopup(component, component2, i2, i3, 2);
        }
        return popup;
    }

    private int getPopupType(Component component, Component component2, int i2, int i3) {
        int popupType = getPopupType();
        if (component == null || invokerInHeavyWeightPopup(component)) {
            popupType = 2;
        } else if (popupType == 0 && !(component2 instanceof JToolTip) && !(component2 instanceof JPopupMenu)) {
            popupType = 1;
        }
        Component parent = component;
        while (true) {
            Component component3 = parent;
            if (component3 != null) {
                if ((component3 instanceof JComponent) && ((JComponent) component3).getClientProperty(ClientPropertyKey.PopupFactory_FORCE_HEAVYWEIGHT_POPUP) == Boolean.TRUE) {
                    popupType = 2;
                    break;
                }
                parent = component3.getParent();
            } else {
                break;
            }
        }
        return popupType;
    }

    private Popup getPopup(Component component, Component component2, int i2, int i3, int i4) {
        if (GraphicsEnvironment.isHeadless()) {
            return getHeadlessPopup(component, component2, i2, i3);
        }
        switch (i4) {
            case 0:
                return getLightWeightPopup(component, component2, i2, i3);
            case 1:
                return getMediumWeightPopup(component, component2, i2, i3);
            case 2:
                Popup heavyWeightPopup = getHeavyWeightPopup(component, component2, i2, i3);
                if (AccessController.doPrivileged(OSInfo.getOSTypeAction()) == OSInfo.OSType.MACOSX && component != null && EmbeddedFrame.getAppletIfAncestorOf(component) != null) {
                    ((HeavyWeightPopup) heavyWeightPopup).setCacheEnabled(false);
                }
                return heavyWeightPopup;
            default:
                return null;
        }
    }

    private Popup getHeadlessPopup(Component component, Component component2, int i2, int i3) {
        return HeadlessPopup.getHeadlessPopup(component, component2, i2, i3);
    }

    private Popup getLightWeightPopup(Component component, Component component2, int i2, int i3) {
        return LightWeightPopup.getLightWeightPopup(component, component2, i2, i3);
    }

    private Popup getMediumWeightPopup(Component component, Component component2, int i2, int i3) {
        return MediumWeightPopup.getMediumWeightPopup(component, component2, i2, i3);
    }

    private Popup getHeavyWeightPopup(Component component, Component component2, int i2, int i3) {
        if (GraphicsEnvironment.isHeadless()) {
            return getMediumWeightPopup(component, component2, i2, i3);
        }
        return HeavyWeightPopup.getHeavyWeightPopup(component, component2, i2, i3);
    }

    private boolean invokerInHeavyWeightPopup(Component component) {
        if (component != null) {
            Container parent = component.getParent();
            while (true) {
                Container container = parent;
                if (container != null) {
                    if (!(container instanceof Popup.HeavyWeightWindow)) {
                        parent = container.getParent();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    /* loaded from: rt.jar:javax/swing/PopupFactory$HeavyWeightPopup.class */
    private static class HeavyWeightPopup extends Popup {
        private static final Object heavyWeightPopupCacheKey = new StringBuffer("PopupFactory.heavyWeightPopupCache");
        private volatile boolean isCacheEnabled = true;

        private HeavyWeightPopup() {
        }

        static Popup getHeavyWeightPopup(Component component, Component component2, int i2, int i3) {
            Window windowAncestor = component != null ? SwingUtilities.getWindowAncestor(component) : null;
            HeavyWeightPopup heavyWeightPopup = null;
            if (windowAncestor != null) {
                heavyWeightPopup = getRecycledHeavyWeightPopup(windowAncestor);
            }
            boolean z2 = false;
            if (component2 != null && component2.isFocusable() && (component2 instanceof JPopupMenu)) {
                Component[] components = ((JPopupMenu) component2).getComponents();
                int length = components.length;
                int i4 = 0;
                while (true) {
                    if (i4 >= length) {
                        break;
                    }
                    Component component3 = components[i4];
                    if ((component3 instanceof MenuElement) || (component3 instanceof JSeparator)) {
                        i4++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
            }
            if (heavyWeightPopup == null || ((JWindow) heavyWeightPopup.getComponent()).getFocusableWindowState() != z2) {
                if (heavyWeightPopup != null) {
                    heavyWeightPopup._dispose();
                }
                heavyWeightPopup = new HeavyWeightPopup();
            }
            heavyWeightPopup.reset(component, component2, i2, i3);
            if (z2) {
                JWindow jWindow = (JWindow) heavyWeightPopup.getComponent();
                jWindow.setFocusableWindowState(true);
                jWindow.setName("###focusableSwingPopup###");
            }
            return heavyWeightPopup;
        }

        private static HeavyWeightPopup getRecycledHeavyWeightPopup(Window window) {
            synchronized (HeavyWeightPopup.class) {
                Map<Window, List<HeavyWeightPopup>> heavyWeightPopupCache = getHeavyWeightPopupCache();
                if (heavyWeightPopupCache.containsKey(window)) {
                    List<HeavyWeightPopup> list = heavyWeightPopupCache.get(window);
                    if (list.size() > 0) {
                        HeavyWeightPopup heavyWeightPopup = list.get(0);
                        list.remove(0);
                        return heavyWeightPopup;
                    }
                    return null;
                }
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Map<Window, List<HeavyWeightPopup>> getHeavyWeightPopupCache() {
            Map<Window, List<HeavyWeightPopup>> map;
            synchronized (HeavyWeightPopup.class) {
                Map<Window, List<HeavyWeightPopup>> map2 = (Map) SwingUtilities.appContextGet(heavyWeightPopupCacheKey);
                if (map2 == null) {
                    map2 = new HashMap(2);
                    SwingUtilities.appContextPut(heavyWeightPopupCacheKey, map2);
                }
                map = map2;
            }
            return map;
        }

        private static void recycleHeavyWeightPopup(HeavyWeightPopup heavyWeightPopup) {
            List<HeavyWeightPopup> arrayList;
            synchronized (HeavyWeightPopup.class) {
                final Window windowAncestor = SwingUtilities.getWindowAncestor(heavyWeightPopup.getComponent());
                Map<Window, List<HeavyWeightPopup>> heavyWeightPopupCache = getHeavyWeightPopupCache();
                if ((windowAncestor instanceof Popup.DefaultFrame) || !windowAncestor.isVisible()) {
                    heavyWeightPopup._dispose();
                    return;
                }
                if (heavyWeightPopupCache.containsKey(windowAncestor)) {
                    arrayList = heavyWeightPopupCache.get(windowAncestor);
                } else {
                    arrayList = new ArrayList();
                    heavyWeightPopupCache.put(windowAncestor, arrayList);
                    windowAncestor.addWindowListener(new WindowAdapter() { // from class: javax.swing.PopupFactory.HeavyWeightPopup.1
                        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                        public void windowClosed(WindowEvent windowEvent) {
                            List list;
                            synchronized (HeavyWeightPopup.class) {
                                list = (List) HeavyWeightPopup.getHeavyWeightPopupCache().remove(windowAncestor);
                            }
                            if (list != null) {
                                for (int size = list.size() - 1; size >= 0; size--) {
                                    ((HeavyWeightPopup) list.get(size))._dispose();
                                }
                            }
                        }
                    });
                }
                if (arrayList.size() < 5) {
                    arrayList.add(heavyWeightPopup);
                } else {
                    heavyWeightPopup._dispose();
                }
            }
        }

        void setCacheEnabled(boolean z2) {
            this.isCacheEnabled = z2;
        }

        @Override // javax.swing.Popup
        public void hide() {
            super.hide();
            if (this.isCacheEnabled) {
                recycleHeavyWeightPopup(this);
            } else {
                _dispose();
            }
        }

        @Override // javax.swing.Popup
        void dispose() {
        }

        void _dispose() {
            super.dispose();
        }
    }

    /* loaded from: rt.jar:javax/swing/PopupFactory$ContainerPopup.class */
    private static class ContainerPopup extends Popup {
        Component owner;

        /* renamed from: x, reason: collision with root package name */
        int f12801x;

        /* renamed from: y, reason: collision with root package name */
        int f12802y;

        private ContainerPopup() {
        }

        @Override // javax.swing.Popup
        public void hide() {
            Container parent;
            Component component = getComponent();
            if (component != null && (parent = component.getParent()) != null) {
                Rectangle bounds = component.getBounds();
                parent.remove(component);
                parent.repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
            }
            this.owner = null;
        }

        @Override // javax.swing.Popup
        public void pack() {
            Component component = getComponent();
            if (component != null) {
                component.setSize(component.getPreferredSize());
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:8:0x0015  */
        @Override // javax.swing.Popup
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        void reset(java.awt.Component r7, java.awt.Component r8, int r9, int r10) {
            /*
                r6 = this;
                r0 = r7
                boolean r0 = r0 instanceof javax.swing.JFrame
                if (r0 != 0) goto L15
                r0 = r7
                boolean r0 = r0 instanceof javax.swing.JDialog
                if (r0 != 0) goto L15
                r0 = r7
                boolean r0 = r0 instanceof javax.swing.JWindow
                if (r0 == 0) goto L1f
            L15:
                r0 = r7
                javax.swing.RootPaneContainer r0 = (javax.swing.RootPaneContainer) r0
                javax.swing.JLayeredPane r0 = r0.getLayeredPane()
                r7 = r0
            L1f:
                r0 = r6
                r1 = r7
                r2 = r8
                r3 = r9
                r4 = r10
                super.reset(r1, r2, r3, r4)
                r0 = r6
                r1 = r9
                r0.f12801x = r1
                r0 = r6
                r1 = r10
                r0.f12802y = r1
                r0 = r6
                r1 = r7
                r0.owner = r1
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.PopupFactory.ContainerPopup.reset(java.awt.Component, java.awt.Component, int, int):void");
        }

        boolean overlappedByOwnedWindow() {
            Window windowAncestor;
            Window[] ownedWindows;
            Component component = getComponent();
            if (this.owner != null && component != null && (windowAncestor = SwingUtilities.getWindowAncestor(this.owner)) != null && (ownedWindows = windowAncestor.getOwnedWindows()) != null) {
                Rectangle bounds = component.getBounds();
                for (Window window : ownedWindows) {
                    if (window.isVisible() && bounds.intersects(window.getBounds())) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        boolean fitsOnScreen() {
            boolean zContains = false;
            Component component = getComponent();
            if (this.owner != null && component != null) {
                int width = component.getWidth();
                int height = component.getHeight();
                Container container = (Container) SwingUtilities.getRoot(this.owner);
                if ((container instanceof JFrame) || (container instanceof JDialog) || (container instanceof JWindow)) {
                    Rectangle bounds = container.getBounds();
                    Insets insets = container.getInsets();
                    bounds.f12372x += insets.left;
                    bounds.f12373y += insets.top;
                    bounds.width -= insets.left + insets.right;
                    bounds.height -= insets.top + insets.bottom;
                    zContains = JPopupMenu.canPopupOverlapTaskBar() ? bounds.intersection(getContainerPopupArea(container.getGraphicsConfiguration())).contains(this.f12801x, this.f12802y, width, height) : bounds.contains(this.f12801x, this.f12802y, width, height);
                } else if (container instanceof JApplet) {
                    Rectangle bounds2 = container.getBounds();
                    Point locationOnScreen = container.getLocationOnScreen();
                    bounds2.f12372x = locationOnScreen.f12370x;
                    bounds2.f12373y = locationOnScreen.f12371y;
                    zContains = bounds2.contains(this.f12801x, this.f12802y, width, height);
                }
            }
            return zContains;
        }

        Rectangle getContainerPopupArea(GraphicsConfiguration graphicsConfiguration) throws HeadlessException {
            Rectangle rectangle;
            Insets insets;
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if (graphicsConfiguration != null) {
                rectangle = graphicsConfiguration.getBounds();
                insets = defaultToolkit.getScreenInsets(graphicsConfiguration);
            } else {
                rectangle = new Rectangle(defaultToolkit.getScreenSize());
                insets = new Insets(0, 0, 0, 0);
            }
            rectangle.f12372x += insets.left;
            rectangle.f12373y += insets.top;
            rectangle.width -= insets.left + insets.right;
            rectangle.height -= insets.top + insets.bottom;
            return rectangle;
        }
    }

    /* loaded from: rt.jar:javax/swing/PopupFactory$HeadlessPopup.class */
    private static class HeadlessPopup extends ContainerPopup {
        private HeadlessPopup() {
            super();
        }

        static Popup getHeadlessPopup(Component component, Component component2, int i2, int i3) {
            HeadlessPopup headlessPopup = new HeadlessPopup();
            headlessPopup.reset(component, component2, i2, i3);
            return headlessPopup;
        }

        @Override // javax.swing.Popup
        Component createComponent(Component component) {
            return new Panel(new BorderLayout());
        }

        @Override // javax.swing.Popup
        public void show() {
        }

        @Override // javax.swing.PopupFactory.ContainerPopup, javax.swing.Popup
        public void hide() {
        }
    }

    /* loaded from: rt.jar:javax/swing/PopupFactory$LightWeightPopup.class */
    private static class LightWeightPopup extends ContainerPopup {
        private static final Object lightWeightPopupCacheKey = new StringBuffer("PopupFactory.lightPopupCache");

        private LightWeightPopup() {
            super();
        }

        static Popup getLightWeightPopup(Component component, Component component2, int i2, int i3) {
            LightWeightPopup recycledLightWeightPopup = getRecycledLightWeightPopup();
            if (recycledLightWeightPopup == null) {
                recycledLightWeightPopup = new LightWeightPopup();
            }
            recycledLightWeightPopup.reset(component, component2, i2, i3);
            if (!recycledLightWeightPopup.fitsOnScreen() || recycledLightWeightPopup.overlappedByOwnedWindow()) {
                recycledLightWeightPopup.hide();
                return null;
            }
            return recycledLightWeightPopup;
        }

        private static List<LightWeightPopup> getLightWeightPopupCache() {
            List<LightWeightPopup> arrayList = (List) SwingUtilities.appContextGet(lightWeightPopupCacheKey);
            if (arrayList == null) {
                arrayList = new ArrayList();
                SwingUtilities.appContextPut(lightWeightPopupCacheKey, arrayList);
            }
            return arrayList;
        }

        private static void recycleLightWeightPopup(LightWeightPopup lightWeightPopup) {
            synchronized (LightWeightPopup.class) {
                List<LightWeightPopup> lightWeightPopupCache = getLightWeightPopupCache();
                if (lightWeightPopupCache.size() < 5) {
                    lightWeightPopupCache.add(lightWeightPopup);
                }
            }
        }

        private static LightWeightPopup getRecycledLightWeightPopup() {
            synchronized (LightWeightPopup.class) {
                List<LightWeightPopup> lightWeightPopupCache = getLightWeightPopupCache();
                if (lightWeightPopupCache.size() > 0) {
                    LightWeightPopup lightWeightPopup = lightWeightPopupCache.get(0);
                    lightWeightPopupCache.remove(0);
                    return lightWeightPopup;
                }
                return null;
            }
        }

        @Override // javax.swing.PopupFactory.ContainerPopup, javax.swing.Popup
        public void hide() {
            super.hide();
            ((Container) getComponent()).removeAll();
            recycleLightWeightPopup(this);
        }

        @Override // javax.swing.Popup
        public void show() {
            Container layeredPane = null;
            if (this.owner != null) {
                layeredPane = this.owner instanceof Container ? (Container) this.owner : this.owner.getParent();
            }
            Container parent = layeredPane;
            while (true) {
                Container container = parent;
                if (container == null) {
                    break;
                }
                if (container instanceof JRootPane) {
                    if (!(container.getParent() instanceof JInternalFrame)) {
                        layeredPane = ((JRootPane) container).getLayeredPane();
                    }
                } else if (container instanceof Window) {
                    if (layeredPane == null) {
                        layeredPane = container;
                    }
                } else if (container instanceof JApplet) {
                    break;
                }
                parent = container.getParent();
            }
            Point pointConvertScreenLocationToParent = SwingUtilities.convertScreenLocationToParent(layeredPane, this.f12801x, this.f12802y);
            Component component = getComponent();
            component.setLocation(pointConvertScreenLocationToParent.f12370x, pointConvertScreenLocationToParent.f12371y);
            if (layeredPane instanceof JLayeredPane) {
                layeredPane.add(component, JLayeredPane.POPUP_LAYER, 0);
            } else {
                layeredPane.add(component);
            }
        }

        @Override // javax.swing.Popup
        Component createComponent(Component component) {
            JPanel jPanel = new JPanel(new BorderLayout(), true);
            jPanel.setOpaque(true);
            return jPanel;
        }

        @Override // javax.swing.PopupFactory.ContainerPopup, javax.swing.Popup
        void reset(Component component, Component component2, int i2, int i3) {
            super.reset(component, component2, i2, i3);
            JComponent jComponent = (JComponent) getComponent();
            jComponent.setOpaque(component2.isOpaque());
            jComponent.setLocation(i2, i3);
            jComponent.add(component2, BorderLayout.CENTER);
            component2.invalidate();
            pack();
        }
    }

    /* loaded from: rt.jar:javax/swing/PopupFactory$MediumWeightPopup.class */
    private static class MediumWeightPopup extends ContainerPopup {
        private static final Object mediumWeightPopupCacheKey = new StringBuffer("PopupFactory.mediumPopupCache");
        private JRootPane rootPane;

        private MediumWeightPopup() {
            super();
        }

        static Popup getMediumWeightPopup(Component component, Component component2, int i2, int i3) {
            MediumWeightPopup recycledMediumWeightPopup = getRecycledMediumWeightPopup();
            if (recycledMediumWeightPopup == null) {
                recycledMediumWeightPopup = new MediumWeightPopup();
            }
            recycledMediumWeightPopup.reset(component, component2, i2, i3);
            if (!recycledMediumWeightPopup.fitsOnScreen() || recycledMediumWeightPopup.overlappedByOwnedWindow()) {
                recycledMediumWeightPopup.hide();
                return null;
            }
            return recycledMediumWeightPopup;
        }

        private static List<MediumWeightPopup> getMediumWeightPopupCache() {
            List<MediumWeightPopup> arrayList = (List) SwingUtilities.appContextGet(mediumWeightPopupCacheKey);
            if (arrayList == null) {
                arrayList = new ArrayList();
                SwingUtilities.appContextPut(mediumWeightPopupCacheKey, arrayList);
            }
            return arrayList;
        }

        private static void recycleMediumWeightPopup(MediumWeightPopup mediumWeightPopup) {
            synchronized (MediumWeightPopup.class) {
                List<MediumWeightPopup> mediumWeightPopupCache = getMediumWeightPopupCache();
                if (mediumWeightPopupCache.size() < 5) {
                    mediumWeightPopupCache.add(mediumWeightPopup);
                }
            }
        }

        private static MediumWeightPopup getRecycledMediumWeightPopup() {
            synchronized (MediumWeightPopup.class) {
                List<MediumWeightPopup> mediumWeightPopupCache = getMediumWeightPopupCache();
                if (mediumWeightPopupCache.size() > 0) {
                    MediumWeightPopup mediumWeightPopup = mediumWeightPopupCache.get(0);
                    mediumWeightPopupCache.remove(0);
                    return mediumWeightPopup;
                }
                return null;
            }
        }

        @Override // javax.swing.PopupFactory.ContainerPopup, javax.swing.Popup
        public void hide() {
            super.hide();
            this.rootPane.getContentPane().removeAll();
            recycleMediumWeightPopup(this);
        }

        @Override // javax.swing.Popup
        public void show() {
            Component component = getComponent();
            Container parent = null;
            if (this.owner != null) {
                parent = this.owner.getParent();
            }
            while (!(parent instanceof Window) && !(parent instanceof Applet) && parent != null) {
                parent = parent.getParent();
            }
            if (parent instanceof RootPaneContainer) {
                JLayeredPane layeredPane = ((RootPaneContainer) parent).getLayeredPane();
                Point pointConvertScreenLocationToParent = SwingUtilities.convertScreenLocationToParent(layeredPane, this.f12801x, this.f12802y);
                component.setVisible(false);
                component.setLocation(pointConvertScreenLocationToParent.f12370x, pointConvertScreenLocationToParent.f12371y);
                layeredPane.add(component, JLayeredPane.POPUP_LAYER, 0);
            } else {
                Point pointConvertScreenLocationToParent2 = SwingUtilities.convertScreenLocationToParent(parent, this.f12801x, this.f12802y);
                component.setLocation(pointConvertScreenLocationToParent2.f12370x, pointConvertScreenLocationToParent2.f12371y);
                component.setVisible(false);
                parent.add(component);
            }
            component.setVisible(true);
        }

        @Override // javax.swing.Popup
        Component createComponent(Component component) {
            MediumWeightComponent mediumWeightComponent = new MediumWeightComponent();
            this.rootPane = new JRootPane();
            this.rootPane.setOpaque(true);
            mediumWeightComponent.add(this.rootPane, BorderLayout.CENTER);
            return mediumWeightComponent;
        }

        @Override // javax.swing.PopupFactory.ContainerPopup, javax.swing.Popup
        void reset(Component component, Component component2, int i2, int i3) {
            super.reset(component, component2, i2, i3);
            Component component3 = getComponent();
            component3.setLocation(i2, i3);
            this.rootPane.getContentPane().add(component2, BorderLayout.CENTER);
            component2.invalidate();
            component3.validate();
            pack();
        }

        /* loaded from: rt.jar:javax/swing/PopupFactory$MediumWeightPopup$MediumWeightComponent.class */
        private static class MediumWeightComponent extends Panel implements SwingHeavyWeight {
            MediumWeightComponent() {
                super(new BorderLayout());
            }
        }
    }
}
