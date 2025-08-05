package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.DefaultDesktopManager;
import javax.swing.DesktopManager;
import javax.swing.Icon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InternalFrameUI;
import javax.swing.plaf.UIResource;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI.class */
public class BasicInternalFrameUI extends InternalFrameUI {
    protected JInternalFrame frame;
    private Handler handler;
    protected MouseInputAdapter borderListener;
    protected PropertyChangeListener propertyChangeListener;
    protected LayoutManager internalFrameLayout;
    protected ComponentListener componentListener;
    protected MouseInputListener glassPaneDispatcher;
    private InternalFrameListener internalFrameListener;
    protected JComponent northPane;
    protected JComponent southPane;
    protected JComponent westPane;
    protected JComponent eastPane;
    protected BasicInternalFrameTitlePane titlePane;
    private static DesktopManager sharedDesktopManager;
    private Rectangle parentBounds;

    @Deprecated
    protected KeyStroke openMenuKey;
    private boolean componentListenerAdded = false;
    private boolean dragging = false;
    private boolean resizing = false;
    private boolean keyBindingRegistered = false;
    private boolean keyBindingActive = false;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicInternalFrameUI((JInternalFrame) jComponent);
    }

    public BasicInternalFrameUI(JInternalFrame jInternalFrame) {
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (lookAndFeel instanceof BasicLookAndFeel) {
            ((BasicLookAndFeel) lookAndFeel).installAWTEventListener();
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.frame = (JInternalFrame) jComponent;
        installDefaults();
        installListeners();
        installComponents();
        installKeyboardActions();
        LookAndFeel.installProperty(this.frame, "opaque", Boolean.TRUE);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        if (jComponent != this.frame) {
            throw new IllegalComponentStateException(((Object) this) + " was asked to deinstall() " + ((Object) jComponent) + " when it only knows about " + ((Object) this.frame) + ".");
        }
        uninstallKeyboardActions();
        uninstallComponents();
        uninstallListeners();
        uninstallDefaults();
        updateFrameCursor();
        this.handler = null;
        this.frame = null;
    }

    protected void installDefaults() {
        Icon frameIcon = this.frame.getFrameIcon();
        if (frameIcon == null || (frameIcon instanceof UIResource)) {
            this.frame.setFrameIcon(UIManager.getIcon("InternalFrame.icon"));
        }
        Container contentPane = this.frame.getContentPane();
        if (contentPane != null && (contentPane.getBackground() instanceof UIResource)) {
            contentPane.setBackground(null);
        }
        JInternalFrame jInternalFrame = this.frame;
        LayoutManager layoutManagerCreateLayoutManager = createLayoutManager();
        this.internalFrameLayout = layoutManagerCreateLayoutManager;
        jInternalFrame.setLayout(layoutManagerCreateLayoutManager);
        this.frame.setBackground(UIManager.getLookAndFeelDefaults().getColor("control"));
        LookAndFeel.installBorder(this.frame, "InternalFrame.border");
    }

    protected void installKeyboardActions() {
        createInternalFrameListener();
        if (this.internalFrameListener != null) {
            this.frame.addInternalFrameListener(this.internalFrameListener);
        }
        LazyActionMap.installLazyActionMap(this.frame, BasicInternalFrameUI.class, "InternalFrame.actionMap");
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new UIAction("showSystemMenu") { // from class: javax.swing.plaf.basic.BasicInternalFrameUI.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                JInternalFrame jInternalFrame = (JInternalFrame) actionEvent.getSource();
                if (jInternalFrame.getUI() instanceof BasicInternalFrameUI) {
                    JComponent northPane = ((BasicInternalFrameUI) jInternalFrame.getUI()).getNorthPane();
                    if (northPane instanceof BasicInternalFrameTitlePane) {
                        ((BasicInternalFrameTitlePane) northPane).showSystemMenu();
                    }
                }
            }

            @Override // sun.swing.UIAction
            public boolean isEnabled(Object obj) {
                if (obj instanceof JInternalFrame) {
                    JInternalFrame jInternalFrame = (JInternalFrame) obj;
                    if (jInternalFrame.getUI() instanceof BasicInternalFrameUI) {
                        return ((BasicInternalFrameUI) jInternalFrame.getUI()).isKeyBindingActive();
                    }
                    return false;
                }
                return false;
            }
        });
        BasicLookAndFeel.installAudioActionMap(lazyActionMap);
    }

    protected void installComponents() {
        setNorthPane(createNorthPane(this.frame));
        setSouthPane(createSouthPane(this.frame));
        setEastPane(createEastPane(this.frame));
        setWestPane(createWestPane(this.frame));
    }

    protected void installListeners() {
        this.borderListener = createBorderListener(this.frame);
        this.propertyChangeListener = createPropertyChangeListener();
        this.frame.addPropertyChangeListener(this.propertyChangeListener);
        installMouseHandlers(this.frame);
        this.glassPaneDispatcher = createGlassPaneDispatcher();
        if (this.glassPaneDispatcher != null) {
            this.frame.getGlassPane().addMouseListener(this.glassPaneDispatcher);
            this.frame.getGlassPane().addMouseMotionListener(this.glassPaneDispatcher);
        }
        this.componentListener = createComponentListener();
        if (this.frame.getParent() != null) {
            this.parentBounds = this.frame.getParent().getBounds();
        }
        if (this.frame.getParent() != null && !this.componentListenerAdded) {
            this.frame.getParent().addComponentListener(this.componentListener);
            this.componentListenerAdded = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public WindowFocusListener getWindowFocusListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelResize() {
        if (this.resizing && (this.borderListener instanceof BorderListener)) {
            ((BorderListener) this.borderListener).finishMouseReleased();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    InputMap getInputMap(int i2) {
        if (i2 == 2) {
            return createInputMap(i2);
        }
        return null;
    }

    InputMap createInputMap(int i2) {
        Object[] objArr;
        if (i2 == 2 && (objArr = (Object[]) DefaultLookup.get(this.frame, this, "InternalFrame.windowBindings")) != null) {
            return LookAndFeel.makeComponentInputMap(this.frame, objArr);
        }
        return null;
    }

    protected void uninstallDefaults() {
        if (this.frame.getFrameIcon() instanceof UIResource) {
            this.frame.setFrameIcon(null);
        }
        this.internalFrameLayout = null;
        this.frame.setLayout(null);
        LookAndFeel.uninstallBorder(this.frame);
    }

    protected void uninstallComponents() {
        setNorthPane(null);
        setSouthPane(null);
        setEastPane(null);
        setWestPane(null);
        if (this.titlePane != null) {
            this.titlePane.uninstallDefaults();
        }
        this.titlePane = null;
    }

    protected void uninstallListeners() {
        if (this.frame.getParent() != null && this.componentListenerAdded) {
            this.frame.getParent().removeComponentListener(this.componentListener);
            this.componentListenerAdded = false;
        }
        this.componentListener = null;
        if (this.glassPaneDispatcher != null) {
            this.frame.getGlassPane().removeMouseListener(this.glassPaneDispatcher);
            this.frame.getGlassPane().removeMouseMotionListener(this.glassPaneDispatcher);
            this.glassPaneDispatcher = null;
        }
        deinstallMouseHandlers(this.frame);
        this.frame.removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
        this.borderListener = null;
    }

    protected void uninstallKeyboardActions() {
        if (this.internalFrameListener != null) {
            this.frame.removeInternalFrameListener(this.internalFrameListener);
        }
        this.internalFrameListener = null;
        SwingUtilities.replaceUIInputMap(this.frame, 2, null);
        SwingUtilities.replaceUIActionMap(this.frame, null);
    }

    void updateFrameCursor() {
        if (this.resizing) {
            return;
        }
        Cursor lastCursor = this.frame.getLastCursor();
        if (lastCursor == null) {
            lastCursor = Cursor.getPredefinedCursor(0);
        }
        this.frame.setCursor(lastCursor);
    }

    protected LayoutManager createLayoutManager() {
        return getHandler();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (this.frame == jComponent) {
            return this.frame.getLayout().preferredLayoutSize(jComponent);
        }
        return new Dimension(100, 100);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (this.frame == jComponent) {
            return this.frame.getLayout().minimumLayoutSize(jComponent);
        }
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    protected void replacePane(JComponent jComponent, JComponent jComponent2) {
        if (jComponent != null) {
            deinstallMouseHandlers(jComponent);
            this.frame.remove(jComponent);
        }
        if (jComponent2 != null) {
            this.frame.add(jComponent2);
            installMouseHandlers(jComponent2);
        }
    }

    protected void deinstallMouseHandlers(JComponent jComponent) {
        jComponent.removeMouseListener(this.borderListener);
        jComponent.removeMouseMotionListener(this.borderListener);
    }

    protected void installMouseHandlers(JComponent jComponent) {
        jComponent.addMouseListener(this.borderListener);
        jComponent.addMouseMotionListener(this.borderListener);
    }

    protected JComponent createNorthPane(JInternalFrame jInternalFrame) {
        this.titlePane = new BasicInternalFrameTitlePane(jInternalFrame);
        return this.titlePane;
    }

    protected JComponent createSouthPane(JInternalFrame jInternalFrame) {
        return null;
    }

    protected JComponent createWestPane(JInternalFrame jInternalFrame) {
        return null;
    }

    protected JComponent createEastPane(JInternalFrame jInternalFrame) {
        return null;
    }

    protected MouseInputAdapter createBorderListener(JInternalFrame jInternalFrame) {
        return new BorderListener();
    }

    protected void createInternalFrameListener() {
        this.internalFrameListener = getHandler();
    }

    protected final boolean isKeyBindingRegistered() {
        return this.keyBindingRegistered;
    }

    protected final void setKeyBindingRegistered(boolean z2) {
        this.keyBindingRegistered = z2;
    }

    public final boolean isKeyBindingActive() {
        return this.keyBindingActive;
    }

    protected final void setKeyBindingActive(boolean z2) {
        this.keyBindingActive = z2;
    }

    protected void setupMenuOpenKey() {
        SwingUtilities.replaceUIInputMap(this.frame, 2, getInputMap(2));
    }

    protected void setupMenuCloseKey() {
    }

    public JComponent getNorthPane() {
        return this.northPane;
    }

    public void setNorthPane(JComponent jComponent) {
        if (this.northPane != null && (this.northPane instanceof BasicInternalFrameTitlePane)) {
            ((BasicInternalFrameTitlePane) this.northPane).uninstallListeners();
        }
        replacePane(this.northPane, jComponent);
        this.northPane = jComponent;
        if (jComponent instanceof BasicInternalFrameTitlePane) {
            this.titlePane = (BasicInternalFrameTitlePane) jComponent;
        }
    }

    public JComponent getSouthPane() {
        return this.southPane;
    }

    public void setSouthPane(JComponent jComponent) {
        this.southPane = jComponent;
    }

    public JComponent getWestPane() {
        return this.westPane;
    }

    public void setWestPane(JComponent jComponent) {
        this.westPane = jComponent;
    }

    public JComponent getEastPane() {
        return this.eastPane;
    }

    public void setEastPane(JComponent jComponent) {
        this.eastPane = jComponent;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$InternalFramePropertyChangeListener.class */
    public class InternalFramePropertyChangeListener implements PropertyChangeListener {
        public InternalFramePropertyChangeListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicInternalFrameUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$InternalFrameLayout.class */
    public class InternalFrameLayout implements LayoutManager {
        public InternalFrameLayout() {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
            BasicInternalFrameUI.this.getHandler().addLayoutComponent(str, component);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
            BasicInternalFrameUI.this.getHandler().removeLayoutComponent(component);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return BasicInternalFrameUI.this.getHandler().preferredLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return BasicInternalFrameUI.this.getHandler().minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            BasicInternalFrameUI.this.getHandler().layoutContainer(container);
        }
    }

    protected DesktopManager getDesktopManager() {
        if (this.frame.getDesktopPane() != null && this.frame.getDesktopPane().getDesktopManager() != null) {
            return this.frame.getDesktopPane().getDesktopManager();
        }
        if (sharedDesktopManager == null) {
            sharedDesktopManager = createDesktopManager();
        }
        return sharedDesktopManager;
    }

    protected DesktopManager createDesktopManager() {
        return new DefaultDesktopManager();
    }

    protected void closeFrame(JInternalFrame jInternalFrame) {
        BasicLookAndFeel.playSound(this.frame, "InternalFrame.closeSound");
        getDesktopManager().closeFrame(jInternalFrame);
    }

    protected void maximizeFrame(JInternalFrame jInternalFrame) {
        BasicLookAndFeel.playSound(this.frame, "InternalFrame.maximizeSound");
        getDesktopManager().maximizeFrame(jInternalFrame);
    }

    protected void minimizeFrame(JInternalFrame jInternalFrame) {
        if (!jInternalFrame.isIcon()) {
            BasicLookAndFeel.playSound(this.frame, "InternalFrame.restoreDownSound");
        }
        getDesktopManager().minimizeFrame(jInternalFrame);
    }

    protected void iconifyFrame(JInternalFrame jInternalFrame) {
        BasicLookAndFeel.playSound(this.frame, "InternalFrame.minimizeSound");
        getDesktopManager().iconifyFrame(jInternalFrame);
    }

    protected void deiconifyFrame(JInternalFrame jInternalFrame) {
        if (!jInternalFrame.isMaximum()) {
            BasicLookAndFeel.playSound(this.frame, "InternalFrame.restoreUpSound");
        }
        getDesktopManager().deiconifyFrame(jInternalFrame);
    }

    protected void activateFrame(JInternalFrame jInternalFrame) {
        getDesktopManager().activateFrame(jInternalFrame);
    }

    protected void deactivateFrame(JInternalFrame jInternalFrame) {
        getDesktopManager().deactivateFrame(jInternalFrame);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$BorderListener.class */
    public class BorderListener extends MouseInputAdapter implements SwingConstants {
        int _x;
        int _y;
        int __x;
        int __y;
        Rectangle startingBounds;
        int resizeDir;
        protected final int RESIZE_NONE = 0;
        private boolean discardRelease = false;
        int resizeCornerSize = 16;

        protected BorderListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            if (mouseEvent.getClickCount() > 1 && mouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
                if (BasicInternalFrameUI.this.frame.isIconifiable() && BasicInternalFrameUI.this.frame.isIcon()) {
                    try {
                        BasicInternalFrameUI.this.frame.setIcon(false);
                    } catch (PropertyVetoException e2) {
                    }
                } else if (BasicInternalFrameUI.this.frame.isMaximizable()) {
                    if (!BasicInternalFrameUI.this.frame.isMaximum()) {
                        try {
                            BasicInternalFrameUI.this.frame.setMaximum(true);
                        } catch (PropertyVetoException e3) {
                        }
                    } else {
                        try {
                            BasicInternalFrameUI.this.frame.setMaximum(false);
                        } catch (PropertyVetoException e4) {
                        }
                    }
                }
            }
        }

        void finishMouseReleased() {
            if (this.discardRelease) {
                this.discardRelease = false;
                return;
            }
            if (this.resizeDir == 0) {
                BasicInternalFrameUI.this.getDesktopManager().endDraggingFrame(BasicInternalFrameUI.this.frame);
                BasicInternalFrameUI.this.dragging = false;
            } else {
                Window windowAncestor = SwingUtilities.getWindowAncestor(BasicInternalFrameUI.this.frame);
                if (windowAncestor != null) {
                    windowAncestor.removeWindowFocusListener(BasicInternalFrameUI.this.getWindowFocusListener());
                }
                MenuContainer topLevelAncestor = BasicInternalFrameUI.this.frame.getTopLevelAncestor();
                if (topLevelAncestor instanceof RootPaneContainer) {
                    Component glassPane = ((RootPaneContainer) topLevelAncestor).getGlassPane();
                    glassPane.setCursor(Cursor.getPredefinedCursor(0));
                    glassPane.setVisible(false);
                }
                BasicInternalFrameUI.this.getDesktopManager().endResizingFrame(BasicInternalFrameUI.this.frame);
                BasicInternalFrameUI.this.resizing = false;
                BasicInternalFrameUI.this.updateFrameCursor();
            }
            this._x = 0;
            this._y = 0;
            this.__x = 0;
            this.__y = 0;
            this.startingBounds = null;
            this.resizeDir = 0;
            this.discardRelease = true;
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            finishMouseReleased();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            Point pointConvertPoint = SwingUtilities.convertPoint((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY(), null);
            this.__x = mouseEvent.getX();
            this.__y = mouseEvent.getY();
            this._x = pointConvertPoint.f12370x;
            this._y = pointConvertPoint.f12371y;
            this.startingBounds = BasicInternalFrameUI.this.frame.getBounds();
            this.resizeDir = 0;
            this.discardRelease = false;
            try {
                BasicInternalFrameUI.this.frame.setSelected(true);
            } catch (PropertyVetoException e2) {
            }
            Insets insets = BasicInternalFrameUI.this.frame.getInsets();
            Point point = new Point(this.__x, this.__y);
            if (mouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
                Point location = BasicInternalFrameUI.this.getNorthPane().getLocation();
                point.f12370x += location.f12370x;
                point.f12371y += location.f12371y;
            }
            if (mouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane() && point.f12370x > insets.left && point.f12371y > insets.top && point.f12370x < BasicInternalFrameUI.this.frame.getWidth() - insets.right) {
                BasicInternalFrameUI.this.getDesktopManager().beginDraggingFrame(BasicInternalFrameUI.this.frame);
                BasicInternalFrameUI.this.dragging = true;
                return;
            }
            if (!BasicInternalFrameUI.this.frame.isResizable()) {
                return;
            }
            if (mouseEvent.getSource() == BasicInternalFrameUI.this.frame || mouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
                if (point.f12370x <= insets.left) {
                    if (point.f12371y < this.resizeCornerSize + insets.top) {
                        this.resizeDir = 8;
                    } else if (point.f12371y > (BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize) - insets.bottom) {
                        this.resizeDir = 6;
                    } else {
                        this.resizeDir = 7;
                    }
                } else if (point.f12370x >= BasicInternalFrameUI.this.frame.getWidth() - insets.right) {
                    if (point.f12371y < this.resizeCornerSize + insets.top) {
                        this.resizeDir = 2;
                    } else if (point.f12371y > (BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize) - insets.bottom) {
                        this.resizeDir = 4;
                    } else {
                        this.resizeDir = 3;
                    }
                } else if (point.f12371y <= insets.top) {
                    if (point.f12370x < this.resizeCornerSize + insets.left) {
                        this.resizeDir = 8;
                    } else if (point.f12370x > (BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize) - insets.right) {
                        this.resizeDir = 2;
                    } else {
                        this.resizeDir = 1;
                    }
                } else if (point.f12371y >= BasicInternalFrameUI.this.frame.getHeight() - insets.bottom) {
                    if (point.f12370x < this.resizeCornerSize + insets.left) {
                        this.resizeDir = 6;
                    } else if (point.f12370x > (BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize) - insets.right) {
                        this.resizeDir = 4;
                    } else {
                        this.resizeDir = 5;
                    }
                } else {
                    this.discardRelease = true;
                    return;
                }
                Cursor predefinedCursor = Cursor.getPredefinedCursor(0);
                switch (this.resizeDir) {
                    case 1:
                        predefinedCursor = Cursor.getPredefinedCursor(8);
                        break;
                    case 2:
                        predefinedCursor = Cursor.getPredefinedCursor(7);
                        break;
                    case 3:
                        predefinedCursor = Cursor.getPredefinedCursor(11);
                        break;
                    case 4:
                        predefinedCursor = Cursor.getPredefinedCursor(5);
                        break;
                    case 5:
                        predefinedCursor = Cursor.getPredefinedCursor(9);
                        break;
                    case 6:
                        predefinedCursor = Cursor.getPredefinedCursor(4);
                        break;
                    case 7:
                        predefinedCursor = Cursor.getPredefinedCursor(10);
                        break;
                    case 8:
                        predefinedCursor = Cursor.getPredefinedCursor(6);
                        break;
                }
                MenuContainer topLevelAncestor = BasicInternalFrameUI.this.frame.getTopLevelAncestor();
                if (topLevelAncestor instanceof RootPaneContainer) {
                    Component glassPane = ((RootPaneContainer) topLevelAncestor).getGlassPane();
                    glassPane.setVisible(true);
                    glassPane.setCursor(predefinedCursor);
                }
                BasicInternalFrameUI.this.getDesktopManager().beginResizingFrame(BasicInternalFrameUI.this.frame, this.resizeDir);
                BasicInternalFrameUI.this.resizing = true;
                Window windowAncestor = SwingUtilities.getWindowAncestor(BasicInternalFrameUI.this.frame);
                if (windowAncestor != null) {
                    windowAncestor.addWindowFocusListener(BasicInternalFrameUI.this.getWindowFocusListener());
                }
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            int i2;
            int i3;
            if (this.startingBounds == null) {
                return;
            }
            Point pointConvertPoint = SwingUtilities.convertPoint((Component) mouseEvent.getSource(), mouseEvent.getX(), mouseEvent.getY(), null);
            int i4 = this._x - pointConvertPoint.f12370x;
            int i5 = this._y - pointConvertPoint.f12371y;
            Dimension minimumSize = BasicInternalFrameUI.this.frame.getMinimumSize();
            Dimension maximumSize = BasicInternalFrameUI.this.frame.getMaximumSize();
            Insets insets = BasicInternalFrameUI.this.frame.getInsets();
            if (BasicInternalFrameUI.this.dragging) {
                if (BasicInternalFrameUI.this.frame.isMaximum() || (mouseEvent.getModifiers() & 16) != 16) {
                    return;
                }
                Dimension size = BasicInternalFrameUI.this.frame.getParent().getSize();
                int i6 = size.width;
                int i7 = size.height;
                int i8 = this.startingBounds.f12372x - i4;
                int i9 = this.startingBounds.f12373y - i5;
                if (i8 + insets.left <= (-this.__x)) {
                    i8 = ((-this.__x) - insets.left) + 1;
                }
                if (i9 + insets.top <= (-this.__y)) {
                    i9 = ((-this.__y) - insets.top) + 1;
                }
                if (i8 + this.__x + insets.right >= i6) {
                    i8 = ((i6 - this.__x) - insets.right) - 1;
                }
                if (i9 + this.__y + insets.bottom >= i7) {
                    i9 = ((i7 - this.__y) - insets.bottom) - 1;
                }
                BasicInternalFrameUI.this.getDesktopManager().dragFrame(BasicInternalFrameUI.this.frame, i8, i9);
                return;
            }
            if (!BasicInternalFrameUI.this.frame.isResizable()) {
                return;
            }
            int x2 = BasicInternalFrameUI.this.frame.getX();
            int y2 = BasicInternalFrameUI.this.frame.getY();
            BasicInternalFrameUI.this.frame.getWidth();
            BasicInternalFrameUI.this.frame.getHeight();
            BasicInternalFrameUI.this.parentBounds = BasicInternalFrameUI.this.frame.getParent().getBounds();
            switch (this.resizeDir) {
                case 0:
                    return;
                case 1:
                    if (this.startingBounds.height + i5 < minimumSize.height) {
                        i5 = -(this.startingBounds.height - minimumSize.height);
                    } else if (this.startingBounds.height + i5 > maximumSize.height) {
                        i5 = maximumSize.height - this.startingBounds.height;
                    }
                    if (this.startingBounds.f12373y - i5 < 0) {
                        i5 = this.startingBounds.f12373y;
                    }
                    x2 = this.startingBounds.f12372x;
                    y2 = this.startingBounds.f12373y - i5;
                    i2 = this.startingBounds.width;
                    i3 = this.startingBounds.height + i5;
                    break;
                case 2:
                    if (this.startingBounds.height + i5 < minimumSize.height) {
                        i5 = -(this.startingBounds.height - minimumSize.height);
                    } else if (this.startingBounds.height + i5 > maximumSize.height) {
                        i5 = maximumSize.height - this.startingBounds.height;
                    }
                    if (this.startingBounds.f12373y - i5 < 0) {
                        i5 = this.startingBounds.f12373y;
                    }
                    if (this.startingBounds.width - i4 < minimumSize.width) {
                        i4 = this.startingBounds.width - minimumSize.width;
                    } else if (this.startingBounds.width - i4 > maximumSize.width) {
                        i4 = -(maximumSize.width - this.startingBounds.width);
                    }
                    if ((this.startingBounds.f12372x + this.startingBounds.width) - i4 > BasicInternalFrameUI.this.parentBounds.width) {
                        i4 = (this.startingBounds.f12372x + this.startingBounds.width) - BasicInternalFrameUI.this.parentBounds.width;
                    }
                    x2 = this.startingBounds.f12372x;
                    y2 = this.startingBounds.f12373y - i5;
                    i2 = this.startingBounds.width - i4;
                    i3 = this.startingBounds.height + i5;
                    break;
                case 3:
                    if (this.startingBounds.width - i4 < minimumSize.width) {
                        i4 = this.startingBounds.width - minimumSize.width;
                    } else if (this.startingBounds.width - i4 > maximumSize.width) {
                        i4 = -(maximumSize.width - this.startingBounds.width);
                    }
                    if ((this.startingBounds.f12372x + this.startingBounds.width) - i4 > BasicInternalFrameUI.this.parentBounds.width) {
                        i4 = (this.startingBounds.f12372x + this.startingBounds.width) - BasicInternalFrameUI.this.parentBounds.width;
                    }
                    i2 = this.startingBounds.width - i4;
                    i3 = this.startingBounds.height;
                    break;
                case 4:
                    if (this.startingBounds.width - i4 < minimumSize.width) {
                        i4 = this.startingBounds.width - minimumSize.width;
                    } else if (this.startingBounds.width - i4 > maximumSize.width) {
                        i4 = -(maximumSize.width - this.startingBounds.width);
                    }
                    if ((this.startingBounds.f12372x + this.startingBounds.width) - i4 > BasicInternalFrameUI.this.parentBounds.width) {
                        i4 = (this.startingBounds.f12372x + this.startingBounds.width) - BasicInternalFrameUI.this.parentBounds.width;
                    }
                    if (this.startingBounds.height - i5 < minimumSize.height) {
                        i5 = this.startingBounds.height - minimumSize.height;
                    } else if (this.startingBounds.height - i5 > maximumSize.height) {
                        i5 = -(maximumSize.height - this.startingBounds.height);
                    }
                    if ((this.startingBounds.f12373y + this.startingBounds.height) - i5 > BasicInternalFrameUI.this.parentBounds.height) {
                        i5 = (this.startingBounds.f12373y + this.startingBounds.height) - BasicInternalFrameUI.this.parentBounds.height;
                    }
                    i2 = this.startingBounds.width - i4;
                    i3 = this.startingBounds.height - i5;
                    break;
                case 5:
                    if (this.startingBounds.height - i5 < minimumSize.height) {
                        i5 = this.startingBounds.height - minimumSize.height;
                    } else if (this.startingBounds.height - i5 > maximumSize.height) {
                        i5 = -(maximumSize.height - this.startingBounds.height);
                    }
                    if ((this.startingBounds.f12373y + this.startingBounds.height) - i5 > BasicInternalFrameUI.this.parentBounds.height) {
                        i5 = (this.startingBounds.f12373y + this.startingBounds.height) - BasicInternalFrameUI.this.parentBounds.height;
                    }
                    i2 = this.startingBounds.width;
                    i3 = this.startingBounds.height - i5;
                    break;
                case 6:
                    if (this.startingBounds.height - i5 < minimumSize.height) {
                        i5 = this.startingBounds.height - minimumSize.height;
                    } else if (this.startingBounds.height - i5 > maximumSize.height) {
                        i5 = -(maximumSize.height - this.startingBounds.height);
                    }
                    if ((this.startingBounds.f12373y + this.startingBounds.height) - i5 > BasicInternalFrameUI.this.parentBounds.height) {
                        i5 = (this.startingBounds.f12373y + this.startingBounds.height) - BasicInternalFrameUI.this.parentBounds.height;
                    }
                    if (this.startingBounds.width + i4 < minimumSize.width) {
                        i4 = -(this.startingBounds.width - minimumSize.width);
                    } else if (this.startingBounds.width + i4 > maximumSize.width) {
                        i4 = maximumSize.width - this.startingBounds.width;
                    }
                    if (this.startingBounds.f12372x - i4 < 0) {
                        i4 = this.startingBounds.f12372x;
                    }
                    x2 = this.startingBounds.f12372x - i4;
                    y2 = this.startingBounds.f12373y;
                    i2 = this.startingBounds.width + i4;
                    i3 = this.startingBounds.height - i5;
                    break;
                case 7:
                    if (this.startingBounds.width + i4 < minimumSize.width) {
                        i4 = -(this.startingBounds.width - minimumSize.width);
                    } else if (this.startingBounds.width + i4 > maximumSize.width) {
                        i4 = maximumSize.width - this.startingBounds.width;
                    }
                    if (this.startingBounds.f12372x - i4 < 0) {
                        i4 = this.startingBounds.f12372x;
                    }
                    x2 = this.startingBounds.f12372x - i4;
                    y2 = this.startingBounds.f12373y;
                    i2 = this.startingBounds.width + i4;
                    i3 = this.startingBounds.height;
                    break;
                case 8:
                    if (this.startingBounds.width + i4 < minimumSize.width) {
                        i4 = -(this.startingBounds.width - minimumSize.width);
                    } else if (this.startingBounds.width + i4 > maximumSize.width) {
                        i4 = maximumSize.width - this.startingBounds.width;
                    }
                    if (this.startingBounds.f12372x - i4 < 0) {
                        i4 = this.startingBounds.f12372x;
                    }
                    if (this.startingBounds.height + i5 < minimumSize.height) {
                        i5 = -(this.startingBounds.height - minimumSize.height);
                    } else if (this.startingBounds.height + i5 > maximumSize.height) {
                        i5 = maximumSize.height - this.startingBounds.height;
                    }
                    if (this.startingBounds.f12373y - i5 < 0) {
                        i5 = this.startingBounds.f12373y;
                    }
                    x2 = this.startingBounds.f12372x - i4;
                    y2 = this.startingBounds.f12373y - i5;
                    i2 = this.startingBounds.width + i4;
                    i3 = this.startingBounds.height + i5;
                    break;
                default:
                    return;
            }
            BasicInternalFrameUI.this.getDesktopManager().resizeFrame(BasicInternalFrameUI.this.frame, x2, y2, i2, i3);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (!BasicInternalFrameUI.this.frame.isResizable()) {
                return;
            }
            if (mouseEvent.getSource() == BasicInternalFrameUI.this.frame || mouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
                Insets insets = BasicInternalFrameUI.this.frame.getInsets();
                Point point = new Point(mouseEvent.getX(), mouseEvent.getY());
                if (mouseEvent.getSource() == BasicInternalFrameUI.this.getNorthPane()) {
                    Point location = BasicInternalFrameUI.this.getNorthPane().getLocation();
                    point.f12370x += location.f12370x;
                    point.f12371y += location.f12371y;
                }
                if (point.f12370x <= insets.left) {
                    if (point.f12371y < this.resizeCornerSize + insets.top) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(6));
                        return;
                    } else if (point.f12371y > (BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize) - insets.bottom) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(4));
                        return;
                    } else {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(10));
                        return;
                    }
                }
                if (point.f12370x >= BasicInternalFrameUI.this.frame.getWidth() - insets.right) {
                    if (mouseEvent.getY() < this.resizeCornerSize + insets.top) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(7));
                        return;
                    } else if (point.f12371y > (BasicInternalFrameUI.this.frame.getHeight() - this.resizeCornerSize) - insets.bottom) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(5));
                        return;
                    } else {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(11));
                        return;
                    }
                }
                if (point.f12371y <= insets.top) {
                    if (point.f12370x < this.resizeCornerSize + insets.left) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(6));
                        return;
                    } else if (point.f12370x > (BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize) - insets.right) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(7));
                        return;
                    } else {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(8));
                        return;
                    }
                }
                if (point.f12371y >= BasicInternalFrameUI.this.frame.getHeight() - insets.bottom) {
                    if (point.f12370x < this.resizeCornerSize + insets.left) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(4));
                        return;
                    } else if (point.f12370x > (BasicInternalFrameUI.this.frame.getWidth() - this.resizeCornerSize) - insets.right) {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(5));
                        return;
                    } else {
                        BasicInternalFrameUI.this.frame.setCursor(Cursor.getPredefinedCursor(9));
                        return;
                    }
                }
                BasicInternalFrameUI.this.updateFrameCursor();
                return;
            }
            BasicInternalFrameUI.this.updateFrameCursor();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.updateFrameCursor();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.updateFrameCursor();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$ComponentHandler.class */
    public class ComponentHandler implements ComponentListener {
        protected ComponentHandler() {
        }

        @Override // java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            BasicInternalFrameUI.this.getHandler().componentResized(componentEvent);
        }

        @Override // java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
            BasicInternalFrameUI.this.getHandler().componentMoved(componentEvent);
        }

        @Override // java.awt.event.ComponentListener
        public void componentShown(ComponentEvent componentEvent) {
            BasicInternalFrameUI.this.getHandler().componentShown(componentEvent);
        }

        @Override // java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
            BasicInternalFrameUI.this.getHandler().componentHidden(componentEvent);
        }
    }

    protected ComponentListener createComponentListener() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$GlassPaneDispatcher.class */
    protected class GlassPaneDispatcher implements MouseInputListener {
        protected GlassPaneDispatcher() {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mouseExited(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mouseClicked(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mouseReleased(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicInternalFrameUI.this.getHandler().mouseDragged(mouseEvent);
        }
    }

    protected MouseInputListener createGlassPaneDispatcher() {
        return null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$BasicInternalFrameListener.class */
    protected class BasicInternalFrameListener implements InternalFrameListener {
        protected BasicInternalFrameListener() {
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameClosing(internalFrameEvent);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameClosed(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameClosed(internalFrameEvent);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameOpened(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameOpened(internalFrameEvent);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameIconified(internalFrameEvent);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameDeiconified(internalFrameEvent);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameActivated(internalFrameEvent);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.getHandler().internalFrameDeactivated(internalFrameEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameUI$Handler.class */
    private class Handler implements ComponentListener, InternalFrameListener, LayoutManager, MouseInputListener, PropertyChangeListener, WindowFocusListener, SwingConstants {
        private Handler() {
        }

        @Override // java.awt.event.WindowFocusListener
        public void windowGainedFocus(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowFocusListener
        public void windowLostFocus(WindowEvent windowEvent) {
            BasicInternalFrameUI.this.cancelResize();
        }

        @Override // java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            Rectangle bounds = ((Component) componentEvent.getSource()).getBounds();
            JInternalFrame.JDesktopIcon desktopIcon = null;
            if (BasicInternalFrameUI.this.frame != null) {
                desktopIcon = BasicInternalFrameUI.this.frame.getDesktopIcon();
                if (BasicInternalFrameUI.this.frame.isMaximum()) {
                    BasicInternalFrameUI.this.frame.setBounds(0, 0, bounds.width, bounds.height);
                }
            }
            if (desktopIcon != null) {
                Rectangle bounds2 = desktopIcon.getBounds();
                desktopIcon.setBounds(bounds2.f12372x, bounds2.f12373y + (bounds.height - BasicInternalFrameUI.this.parentBounds.height), bounds2.width, bounds2.height);
            }
            if (!BasicInternalFrameUI.this.parentBounds.equals(bounds)) {
                BasicInternalFrameUI.this.parentBounds = bounds;
            }
            if (BasicInternalFrameUI.this.frame != null) {
                BasicInternalFrameUI.this.frame.validate();
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
        }

        @Override // java.awt.event.ComponentListener
        public void componentShown(ComponentEvent componentEvent) {
        }

        @Override // java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameClosed(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.frame.removeInternalFrameListener(BasicInternalFrameUI.this.getHandler());
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameActivated(InternalFrameEvent internalFrameEvent) {
            if (!BasicInternalFrameUI.this.isKeyBindingRegistered()) {
                BasicInternalFrameUI.this.setKeyBindingRegistered(true);
                BasicInternalFrameUI.this.setupMenuOpenKey();
                BasicInternalFrameUI.this.setupMenuCloseKey();
            }
            if (BasicInternalFrameUI.this.isKeyBindingRegistered()) {
                BasicInternalFrameUI.this.setKeyBindingActive(true);
            }
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameDeactivated(InternalFrameEvent internalFrameEvent) {
            BasicInternalFrameUI.this.setKeyBindingActive(false);
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameClosing(InternalFrameEvent internalFrameEvent) {
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameOpened(InternalFrameEvent internalFrameEvent) {
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameIconified(InternalFrameEvent internalFrameEvent) {
        }

        @Override // javax.swing.event.InternalFrameListener
        public void internalFrameDeiconified(InternalFrameEvent internalFrameEvent) {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            Insets insets = BasicInternalFrameUI.this.frame.getInsets();
            Dimension dimension = new Dimension(BasicInternalFrameUI.this.frame.getRootPane().getPreferredSize());
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
            if (BasicInternalFrameUI.this.getNorthPane() != null) {
                Dimension preferredSize = BasicInternalFrameUI.this.getNorthPane().getPreferredSize();
                dimension.width = Math.max(preferredSize.width, dimension.width);
                dimension.height += preferredSize.height;
            }
            if (BasicInternalFrameUI.this.getSouthPane() != null) {
                Dimension preferredSize2 = BasicInternalFrameUI.this.getSouthPane().getPreferredSize();
                dimension.width = Math.max(preferredSize2.width, dimension.width);
                dimension.height += preferredSize2.height;
            }
            if (BasicInternalFrameUI.this.getEastPane() != null) {
                Dimension preferredSize3 = BasicInternalFrameUI.this.getEastPane().getPreferredSize();
                dimension.width += preferredSize3.width;
                dimension.height = Math.max(preferredSize3.height, dimension.height);
            }
            if (BasicInternalFrameUI.this.getWestPane() != null) {
                Dimension preferredSize4 = BasicInternalFrameUI.this.getWestPane().getPreferredSize();
                dimension.width += preferredSize4.width;
                dimension.height = Math.max(preferredSize4.height, dimension.height);
            }
            return dimension;
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            Dimension dimension = new Dimension();
            if (BasicInternalFrameUI.this.getNorthPane() != null && (BasicInternalFrameUI.this.getNorthPane() instanceof BasicInternalFrameTitlePane)) {
                dimension = new Dimension(BasicInternalFrameUI.this.getNorthPane().getMinimumSize());
            }
            Insets insets = BasicInternalFrameUI.this.frame.getInsets();
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
            return dimension;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            Insets insets = BasicInternalFrameUI.this.frame.getInsets();
            int i2 = insets.left;
            int i3 = insets.top;
            int width = (BasicInternalFrameUI.this.frame.getWidth() - insets.left) - insets.right;
            int height = (BasicInternalFrameUI.this.frame.getHeight() - insets.top) - insets.bottom;
            if (BasicInternalFrameUI.this.getNorthPane() != null) {
                Dimension preferredSize = BasicInternalFrameUI.this.getNorthPane().getPreferredSize();
                if (DefaultLookup.getBoolean(BasicInternalFrameUI.this.frame, BasicInternalFrameUI.this, "InternalFrame.layoutTitlePaneAtOrigin", false)) {
                    i3 = 0;
                    height += insets.top;
                    BasicInternalFrameUI.this.getNorthPane().setBounds(0, 0, BasicInternalFrameUI.this.frame.getWidth(), preferredSize.height);
                } else {
                    BasicInternalFrameUI.this.getNorthPane().setBounds(i2, i3, width, preferredSize.height);
                }
                i3 += preferredSize.height;
                height -= preferredSize.height;
            }
            if (BasicInternalFrameUI.this.getSouthPane() != null) {
                Dimension preferredSize2 = BasicInternalFrameUI.this.getSouthPane().getPreferredSize();
                BasicInternalFrameUI.this.getSouthPane().setBounds(i2, (BasicInternalFrameUI.this.frame.getHeight() - insets.bottom) - preferredSize2.height, width, preferredSize2.height);
                height -= preferredSize2.height;
            }
            if (BasicInternalFrameUI.this.getWestPane() != null) {
                Dimension preferredSize3 = BasicInternalFrameUI.this.getWestPane().getPreferredSize();
                BasicInternalFrameUI.this.getWestPane().setBounds(i2, i3, preferredSize3.width, height);
                width -= preferredSize3.width;
                i2 += preferredSize3.width;
            }
            if (BasicInternalFrameUI.this.getEastPane() != null) {
                Dimension preferredSize4 = BasicInternalFrameUI.this.getEastPane().getPreferredSize();
                BasicInternalFrameUI.this.getEastPane().setBounds(width - preferredSize4.width, i3, preferredSize4.width, height);
                width -= preferredSize4.width;
            }
            if (BasicInternalFrameUI.this.frame.getRootPane() != null) {
                BasicInternalFrameUI.this.frame.getRootPane().setBounds(i2, i3, width, height);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            JInternalFrame jInternalFrame = (JInternalFrame) propertyChangeEvent.getSource();
            Object newValue = propertyChangeEvent.getNewValue();
            Object oldValue = propertyChangeEvent.getOldValue();
            if (JInternalFrame.IS_CLOSED_PROPERTY == propertyName) {
                if (newValue == Boolean.TRUE) {
                    BasicInternalFrameUI.this.cancelResize();
                    if (BasicInternalFrameUI.this.frame.getParent() != null && BasicInternalFrameUI.this.componentListenerAdded) {
                        BasicInternalFrameUI.this.frame.getParent().removeComponentListener(BasicInternalFrameUI.this.componentListener);
                    }
                    BasicInternalFrameUI.this.closeFrame(jInternalFrame);
                    return;
                }
                return;
            }
            if (JInternalFrame.IS_MAXIMUM_PROPERTY == propertyName) {
                if (newValue == Boolean.TRUE) {
                    BasicInternalFrameUI.this.maximizeFrame(jInternalFrame);
                    return;
                } else {
                    BasicInternalFrameUI.this.minimizeFrame(jInternalFrame);
                    return;
                }
            }
            if ("icon" == propertyName) {
                if (newValue == Boolean.TRUE) {
                    BasicInternalFrameUI.this.iconifyFrame(jInternalFrame);
                    return;
                } else {
                    BasicInternalFrameUI.this.deiconifyFrame(jInternalFrame);
                    return;
                }
            }
            if (JInternalFrame.IS_SELECTED_PROPERTY == propertyName) {
                if (newValue == Boolean.TRUE && oldValue == Boolean.FALSE) {
                    BasicInternalFrameUI.this.activateFrame(jInternalFrame);
                    return;
                } else {
                    if (newValue == Boolean.FALSE && oldValue == Boolean.TRUE) {
                        BasicInternalFrameUI.this.deactivateFrame(jInternalFrame);
                        return;
                    }
                    return;
                }
            }
            if (propertyName == "ancestor") {
                if (newValue == null) {
                    BasicInternalFrameUI.this.cancelResize();
                }
                if (BasicInternalFrameUI.this.frame.getParent() == null) {
                    BasicInternalFrameUI.this.parentBounds = null;
                } else {
                    BasicInternalFrameUI.this.parentBounds = jInternalFrame.getParent().getBounds();
                }
                if (BasicInternalFrameUI.this.frame.getParent() != null && !BasicInternalFrameUI.this.componentListenerAdded) {
                    jInternalFrame.getParent().addComponentListener(BasicInternalFrameUI.this.componentListener);
                    BasicInternalFrameUI.this.componentListenerAdded = true;
                    return;
                }
                return;
            }
            if ("title" == propertyName || propertyName == "closable" || propertyName == "iconable" || propertyName == "maximizable") {
                Dimension minimumSize = BasicInternalFrameUI.this.frame.getMinimumSize();
                Dimension size = BasicInternalFrameUI.this.frame.getSize();
                if (minimumSize.width > size.width) {
                    BasicInternalFrameUI.this.frame.setSize(minimumSize.width, size.height);
                }
            }
        }
    }
}
