package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.controlpanel.ControlPanel;
import de.muntjak.tinylookandfeel.util.ColorRoutines;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane.class */
public class TinyTitlePane extends JComponent {
    private static final HashMap cache = new HashMap();
    public static Color buttonUpperColor;
    public static Color buttonLowerColor;
    private static final int IMAGE_HEIGHT = 16;
    private static final int IMAGE_WIDTH = 16;
    private static TinyWindowButtonUI iconButtonUI;
    private static TinyWindowButtonUI maxButtonUI;
    private static TinyWindowButtonUI closeButtonUI;
    private PropertyChangeListener propertyChangeListener;
    private JMenuBar menuBar;
    private Image systemIcon;
    private Action closeAction;
    private Action iconifyAction;
    private Action restoreAction;
    private Action maximizeAction;
    private JButton toggleButton;
    private JButton iconifyButton;
    private JButton closeButton;
    private WindowListener windowListener;
    private ComponentListener windowMoveListener;
    private Window window;
    private JRootPane rootPane;
    private int buttonsWidth;
    private int state = -1;
    private TinyRootPaneUI rootPaneUI;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$CaptionKey.class */
    private static class CaptionKey {
        private Color frameCaptionColor;
        private boolean isActive;
        private int titleHeight;

        CaptionKey(Color color, boolean z2, int i2) {
            this.frameCaptionColor = color;
            this.isActive = z2;
            this.titleHeight = i2;
        }

        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof CaptionKey)) {
                return false;
            }
            CaptionKey captionKey = (CaptionKey) obj;
            return this.frameCaptionColor.equals(captionKey.frameCaptionColor) && this.isActive == captionKey.isActive && this.titleHeight == captionKey.titleHeight;
        }

        public int hashCode() {
            return this.frameCaptionColor.hashCode() * (this.isActive ? 1 : 2) * this.titleHeight;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$CloseAction.class */
    private class CloseAction extends AbstractAction {
        private final TinyTitlePane this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CloseAction(TinyTitlePane tinyTitlePane) {
            super(UIManager.getString("MetalTitlePane.closeTitle", tinyTitlePane.getLocale()));
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.this$0.close();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$IconifyAction.class */
    private class IconifyAction extends AbstractAction {
        private final TinyTitlePane this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public IconifyAction(TinyTitlePane tinyTitlePane) {
            super(UIManager.getString("MetalTitlePane.iconifyTitle", tinyTitlePane.getLocale()));
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.this$0.iconify();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$MaximizeAction.class */
    private class MaximizeAction extends AbstractAction {
        private final TinyTitlePane this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public MaximizeAction(TinyTitlePane tinyTitlePane) {
            super(UIManager.getString("MetalTitlePane.maximizeTitle", tinyTitlePane.getLocale()));
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
            this.this$0.maximize();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$PropertyChangeHandler.class */
    private class PropertyChangeHandler implements PropertyChangeListener {
        private final TinyTitlePane this$0;

        private PropertyChangeHandler(TinyTitlePane tinyTitlePane) {
            this.this$0 = tinyTitlePane;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if ("resizable".equals(propertyName) || "state".equals(propertyName)) {
                Frame frame = this.this$0.getFrame();
                if (frame != null) {
                    this.this$0.setState(frame.getExtendedState(), true);
                }
                if ("resizable".equals(propertyName)) {
                    this.this$0.getRootPane().repaint();
                    return;
                }
                return;
            }
            if ("title".equals(propertyName)) {
                this.this$0.repaint();
                return;
            }
            if ("componentOrientation".equals(propertyName)) {
                this.this$0.revalidate();
                this.this$0.repaint();
            } else if ("iconImage".equals(propertyName)) {
                this.this$0.updateSystemIcon();
                this.this$0.revalidate();
                this.this$0.repaint();
            }
        }

        PropertyChangeHandler(TinyTitlePane tinyTitlePane, AnonymousClass1 anonymousClass1) {
            this(tinyTitlePane);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$RestoreAction.class */
    private class RestoreAction extends AbstractAction {
        private final TinyTitlePane this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public RestoreAction(TinyTitlePane tinyTitlePane) {
            super(UIManager.getString("MetalTitlePane.restoreTitle", tinyTitlePane.getLocale()));
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            this.this$0.restore();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$SystemMenuBar.class */
    private class SystemMenuBar extends JMenuBar {
        private final TinyTitlePane this$0;

        private SystemMenuBar(TinyTitlePane tinyTitlePane) {
            this.this$0 = tinyTitlePane;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            int height = getHeight();
            if (this.this$0.systemIcon != null) {
                graphics.drawImage(this.this$0.systemIcon, 0, ((height - 16) / 2) + 1, 16, 16, null);
                return;
            }
            Icon icon = UIManager.getIcon("InternalFrame.icon");
            if (icon != null) {
                icon.paintIcon(this, graphics, 0, ((height - icon.getIconHeight()) / 2) + 1);
            }
        }

        private Window getOwningFrame(Dialog dialog) {
            while (true) {
                Window owner = dialog.getOwner();
                if (owner != null && (owner instanceof Dialog)) {
                    dialog = (Dialog) owner;
                }
                return owner;
            }
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            if (this.this$0.systemIcon != null) {
                return new Dimension(16, 16);
            }
            Icon icon = UIManager.getIcon("InternalFrame.icon");
            if (icon != null) {
                return new Dimension(icon.getIconWidth(), icon.getIconHeight());
            }
            Dimension preferredSize = super.getPreferredSize();
            return new Dimension(Math.max(16, preferredSize.width), Math.max(preferredSize.height, 16));
        }

        SystemMenuBar(TinyTitlePane tinyTitlePane, AnonymousClass1 anonymousClass1) {
            this(tinyTitlePane);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$TitlePaneLayout.class */
    private class TitlePaneLayout implements LayoutManager {
        private final TinyTitlePane this$0;

        private TitlePaneLayout(TinyTitlePane tinyTitlePane) {
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return new Dimension(104, computeHeight());
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return preferredLayoutSize(container);
        }

        private int computeHeight() throws HeadlessException {
            Window window = this.this$0.getWindow();
            if (!(window instanceof Frame)) {
                return window instanceof Dialog ? 29 : 25;
            }
            this.this$0.setMaximizeBounds(this.this$0.getFrame());
            return 29;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int i3;
            if (this.this$0.getWindowDecorationStyle() == 0) {
                this.this$0.buttonsWidth = 0;
                return;
            }
            boolean zIsLeftToRight = this.this$0.window == null ? this.this$0.getRootPane().getComponentOrientation().isLeftToRight() : this.this$0.window.getComponentOrientation().isLeftToRight();
            int width = this.this$0.getWidth();
            if (this.this$0.closeButton != null) {
                i2 = this.this$0.closeButton.getPreferredSize().height;
                i3 = this.this$0.closeButton.getPreferredSize().width;
            } else {
                i2 = 16;
                i3 = 16;
            }
            int height = ((this.this$0.getHeight() - i2) / 2) + (this.this$0.getHeight() == 29 ? 1 : 0);
            int i4 = zIsLeftToRight ? 5 : (width - i3) - 5;
            if (this.this$0.menuBar != null) {
                this.this$0.menuBar.setBounds(i4, height, i3, i2);
            }
            int i5 = (zIsLeftToRight ? width : 0) + (zIsLeftToRight ? (-2) - i3 : 2);
            if (this.this$0.closeButton != null) {
                this.this$0.closeButton.setBounds(i5, height, i3, i2);
            }
            if (!zIsLeftToRight) {
                i5 += i3;
            }
            if (Toolkit.getDefaultToolkit().isFrameStateSupported(6) && this.this$0.toggleButton.getParent() != null) {
                i5 += zIsLeftToRight ? (-2) - i3 : 2;
                this.this$0.toggleButton.setBounds(i5, height, i3, i2);
                if (!zIsLeftToRight) {
                    i5 += i3;
                }
            }
            if (this.this$0.iconifyButton != null && this.this$0.iconifyButton.getParent() != null) {
                i5 += zIsLeftToRight ? (-2) - i3 : 2;
                this.this$0.iconifyButton.setBounds(i5, height, i3, i2);
                if (!zIsLeftToRight) {
                    i5 += i3;
                }
            }
            this.this$0.buttonsWidth = zIsLeftToRight ? width - i5 : i5;
        }

        TitlePaneLayout(TinyTitlePane tinyTitlePane, AnonymousClass1 anonymousClass1) {
            this(tinyTitlePane);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$WindowHandler.class */
    private class WindowHandler extends WindowAdapter {
        private final TinyTitlePane this$0;

        private WindowHandler(TinyTitlePane tinyTitlePane) {
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
            this.this$0.setActive(true);
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
            this.this$0.setActive(false);
        }

        WindowHandler(TinyTitlePane tinyTitlePane, AnonymousClass1 anonymousClass1) {
            this(tinyTitlePane);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTitlePane$WindowMoveListener.class */
    class WindowMoveListener extends ComponentAdapter {
        private final TinyTitlePane this$0;

        WindowMoveListener(TinyTitlePane tinyTitlePane) {
            this.this$0 = tinyTitlePane;
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
            if (this.this$0.getWindowDecorationStyle() == 0) {
                return;
            }
            Window window = this.this$0.getWindow();
            if (window.isShowing()) {
                window.repaint(0, 0, window.getWidth(), 5);
            }
        }

        @Override // java.awt.event.ComponentAdapter, java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            if (this.this$0.getWindowDecorationStyle() == 0) {
                return;
            }
            Window window = this.this$0.getWindow();
            if (window.isShowing()) {
                window.repaint(0, 0, window.getWidth(), 5);
            }
        }
    }

    public TinyTitlePane(JRootPane jRootPane, TinyRootPaneUI tinyRootPaneUI) {
        this.rootPane = jRootPane;
        this.rootPaneUI = tinyRootPaneUI;
        installSubcomponents();
        installDefaults();
        setLayout(createLayout());
    }

    public static void clearCache() {
        cache.clear();
    }

    private void uninstall() {
        uninstallListeners();
        this.window = null;
        removeAll();
    }

    private void installListeners() {
        if (this.window != null) {
            this.windowListener = createWindowListener();
            this.window.addWindowListener(this.windowListener);
            this.propertyChangeListener = createWindowPropertyChangeListener();
            this.window.addPropertyChangeListener(this.propertyChangeListener);
            this.windowMoveListener = new WindowMoveListener(this);
            this.window.addComponentListener(this.windowMoveListener);
            if (this.window instanceof JDialog) {
                TinyPopupFactory.addDialog((JDialog) this.window);
            }
        }
    }

    private void uninstallListeners() {
        if (this.window != null) {
            this.window.removeWindowListener(this.windowListener);
            this.window.removePropertyChangeListener(this.propertyChangeListener);
            this.window.removeComponentListener(this.windowMoveListener);
        }
    }

    private WindowListener createWindowListener() {
        return new WindowHandler(this, null);
    }

    private PropertyChangeListener createWindowPropertyChangeListener() {
        return new PropertyChangeHandler(this, null);
    }

    @Override // javax.swing.JComponent
    public JRootPane getRootPane() {
        return this.rootPane;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getWindowDecorationStyle() {
        return getRootPane().getWindowDecorationStyle();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        uninstallListeners();
        this.window = SwingUtilities.getWindowAncestor(this);
        if (this.window != null) {
            if (this.window instanceof Frame) {
                setState(((Frame) this.window).getExtendedState());
            } else {
                setState(0);
            }
            setActive(this.window.isActive());
            installListeners();
            updateSystemIcon();
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
        uninstallListeners();
        this.window = null;
    }

    private void installSubcomponents() {
        int windowDecorationStyle = getWindowDecorationStyle();
        if (windowDecorationStyle == 1) {
            createActions();
            this.menuBar = createMenuBar();
            add(this.menuBar);
            createButtons(windowDecorationStyle);
            add(this.iconifyButton);
            add(this.toggleButton);
            add(this.closeButton);
            this.iconifyButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.TRUE);
            this.toggleButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.TRUE);
            this.closeButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.TRUE);
            return;
        }
        if (windowDecorationStyle == 2 || windowDecorationStyle == 5 || windowDecorationStyle == 6) {
            createActions();
            this.menuBar = createMenuBar();
            add(this.menuBar);
            createButtons(windowDecorationStyle);
            add(this.closeButton);
            this.closeButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.TRUE);
            return;
        }
        if (windowDecorationStyle == 3 || windowDecorationStyle == 4 || windowDecorationStyle == 7 || windowDecorationStyle == 8) {
            createActions();
            createButtons(windowDecorationStyle);
            add(this.closeButton);
            this.closeButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.TRUE);
        }
    }

    private void installDefaults() {
        setFont(UIManager.getFont("Frame.titleFont", getLocale()));
    }

    private void uninstallDefaults() {
    }

    protected JMenuBar createMenuBar() {
        this.menuBar = new SystemMenuBar(this, null);
        this.menuBar.setFocusable(false);
        this.menuBar.setBorderPainted(true);
        this.menuBar.add(createMenu());
        return this.menuBar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemIcon() {
        this.systemIcon = getWindowIcon(getWindow());
    }

    private Image getWindowIcon(Window window) {
        if (window == null) {
            return null;
        }
        if (window instanceof Frame) {
            return ((Frame) window).getIconImage();
        }
        try {
            List list = (List) window.getClass().getMethod("getIconImages", (Class[]) null).invoke(window, (Object[]) null);
            if (list == null) {
                return null;
            }
            if (list.size() == 0) {
                return getWindowIcon(window.getOwner());
            }
            if (list.size() == 1) {
                return (Image) list.get(0);
            }
            for (int i2 = 0; i2 < list.size(); i2++) {
                Image image = (Image) list.get(i2);
                if (image.getWidth(this) == 16 && image.getHeight(this) == 16) {
                    return image;
                }
            }
            return ((Image) list.get(0)).getScaledInstance(16, 16, 4);
        } catch (Exception e2) {
            return getWindowIcon(window.getOwner());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void close() {
        Window window = getWindow();
        if (window != null) {
            window.dispatchEvent(new WindowEvent(window, 201));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void iconify() {
        Frame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(frame.getExtendedState() | 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maximize() throws HeadlessException {
        Frame frame = getFrame();
        if (frame != null) {
            setMaximizeBounds(frame);
            frame.setExtendedState(frame.getExtendedState() | 6);
        }
    }

    protected void setMaximizeBounds(Frame frame) throws HeadlessException {
        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setMaximizedBounds(new Rectangle(screenInsets.left, screenInsets.top, (screenSize.width - screenInsets.left) - screenInsets.right, (screenSize.height - screenInsets.top) - screenInsets.bottom));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restore() {
        Frame frame = getFrame();
        if (frame == null) {
            return;
        }
        if ((frame.getExtendedState() & 1) == 1) {
            frame.setExtendedState(this.state & (-2));
        } else {
            frame.setExtendedState(this.state & (-7));
        }
    }

    private void createActions() {
        this.closeAction = new CloseAction(this);
        this.iconifyAction = new IconifyAction(this);
        this.restoreAction = new RestoreAction(this);
        this.maximizeAction = new MaximizeAction(this);
    }

    private JMenu createMenu() {
        JMenu jMenu = new JMenu("");
        jMenu.addMenuListener(new MenuListener(this) { // from class: de.muntjak.tinylookandfeel.TinyTitlePane.1
            private final TinyTitlePane this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.swing.event.MenuListener
            public void menuSelected(MenuEvent menuEvent) {
                if (this.this$0.windowHasMenuBar()) {
                    TinyMenuUI.systemMenuShowing = true;
                }
            }

            @Override // javax.swing.event.MenuListener
            public void menuDeselected(MenuEvent menuEvent) {
                TinyMenuUI.systemMenuShowing = false;
            }

            @Override // javax.swing.event.MenuListener
            public void menuCanceled(MenuEvent menuEvent) {
            }
        });
        if (getWindowDecorationStyle() == 1) {
            addSystemMenuItems(jMenu, true);
            jMenu.putClientProperty("isSystemMenu", Boolean.TRUE);
        } else if (getWindowDecorationStyle() != 0) {
            addSystemMenuItems(jMenu, false);
            jMenu.putClientProperty("isSystemMenu", Boolean.TRUE);
        }
        return jMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean windowHasMenuBar() {
        Window window = getWindow();
        return window instanceof JFrame ? ((JFrame) window).getJMenuBar() != null : (window instanceof JDialog) && ((JDialog) window).getJMenuBar() != null;
    }

    private void addSystemMenuItems(JMenu jMenu, boolean z2) {
        getRootPane().getLocale();
        if (z2) {
            JMenuItem jMenuItemAdd = jMenu.add(this.restoreAction);
            jMenuItemAdd.setIcon(MenuItemIconFactory.getSystemRestoreIcon());
            int i2 = getInt("MetalTitlePane.restoreMnemonic", -1);
            if (i2 != -1) {
                jMenuItemAdd.setMnemonic(i2);
            }
            JMenuItem jMenuItemAdd2 = jMenu.add(this.iconifyAction);
            jMenuItemAdd2.setIcon(MenuItemIconFactory.getSystemIconifyIcon());
            int i3 = getInt("MetalTitlePane.iconifyMnemonic", -1);
            if (i3 != -1) {
                jMenuItemAdd2.setMnemonic(i3);
            }
            if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
                JMenuItem jMenuItemAdd3 = jMenu.add(this.maximizeAction);
                jMenuItemAdd3.setIcon(MenuItemIconFactory.getSystemMaximizeIcon());
                int i4 = getInt("MetalTitlePane.maximizeMnemonic", -1);
                if (i4 != -1) {
                    jMenuItemAdd3.setMnemonic(i4);
                }
            }
            jMenu.addSeparator();
        }
        JMenuItem jMenuItemAdd4 = jMenu.add(this.closeAction);
        jMenuItemAdd4.setIcon(MenuItemIconFactory.getSystemCloseIcon());
        int i5 = getInt("MetalTitlePane.closeMnemonic", -1);
        if (i5 != -1) {
            jMenuItemAdd4.setMnemonic(i5);
        }
    }

    protected void createButtons(int i2) {
        if (iconButtonUI == null) {
            iconButtonUI = TinyWindowButtonUI.createButtonUIForType(2);
            maxButtonUI = TinyWindowButtonUI.createButtonUIForType(1);
            closeButtonUI = TinyWindowButtonUI.createButtonUIForType(0);
        }
        this.iconifyButton = new SpecialUIButton(iconButtonUI);
        this.iconifyButton.setAction(this.iconifyAction);
        this.iconifyButton.setText(null);
        this.iconifyButton.setRolloverEnabled(true);
        this.toggleButton = new SpecialUIButton(maxButtonUI);
        this.toggleButton.setAction(this.maximizeAction);
        this.toggleButton.setText(null);
        this.toggleButton.setRolloverEnabled(true);
        this.closeButton = new SpecialUIButton(closeButtonUI);
        this.closeButton.setAction(this.closeAction);
        this.closeButton.setText(null);
        this.closeButton.setRolloverEnabled(true);
        this.closeButton.getAccessibleContext().setAccessibleName("Close");
        this.iconifyButton.getAccessibleContext().setAccessibleName("Iconify");
        this.toggleButton.getAccessibleContext().setAccessibleName("Maximize");
        if (TinyLookAndFeel.controlPanelInstantiated && i2 == 1) {
            ControlPanel.setWindowButtons(new JButton[]{this.iconifyButton, this.toggleButton, this.closeButton});
        }
    }

    private LayoutManager createLayout() {
        return new TitlePaneLayout(this, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActive(boolean z2) {
        if (getWindowDecorationStyle() == 1) {
            Boolean bool = z2 ? Boolean.TRUE : Boolean.FALSE;
            this.iconifyButton.putClientProperty("paintActive", bool);
            this.closeButton.putClientProperty("paintActive", bool);
            this.toggleButton.putClientProperty("paintActive", bool);
            this.iconifyButton.setEnabled(z2);
            this.closeButton.setEnabled(z2);
            this.toggleButton.setEnabled(z2);
        }
        getRootPane().repaint();
    }

    private void setState(int i2) {
        setState(i2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(int i2, boolean z2) {
        if (getWindow() == null || getWindowDecorationStyle() != 1) {
            return;
        }
        if (this.state != i2 || z2) {
            Frame frame = getFrame();
            if (frame != null) {
                JRootPane rootPane = getRootPane();
                if (((i2 & 6) != 6 || ((rootPane.getBorder() != null && !(rootPane.getBorder() instanceof UIResource)) || !frame.isShowing())) && (i2 & 6) != 6) {
                }
                if (frame.isResizable()) {
                    if ((i2 & 4) == 4 || (i2 & 2) == 2) {
                        updateToggleButton(this.restoreAction);
                        this.maximizeAction.setEnabled(false);
                        this.restoreAction.setEnabled(true);
                    } else {
                        updateToggleButton(this.maximizeAction);
                        this.maximizeAction.setEnabled(true);
                        this.restoreAction.setEnabled(false);
                    }
                    if (this.toggleButton.getParent() == null || this.iconifyButton.getParent() == null) {
                        add(this.toggleButton);
                        add(this.iconifyButton);
                        revalidate();
                        repaint();
                    }
                    this.toggleButton.setText(null);
                } else {
                    this.maximizeAction.setEnabled(false);
                    this.restoreAction.setEnabled(false);
                    if (this.toggleButton.getParent() != null) {
                        remove(this.toggleButton);
                        revalidate();
                        repaint();
                    }
                }
            } else {
                this.maximizeAction.setEnabled(false);
                this.restoreAction.setEnabled(false);
                this.iconifyAction.setEnabled(false);
                remove(this.toggleButton);
                remove(this.iconifyButton);
                revalidate();
                repaint();
            }
            this.closeAction.setEnabled(true);
            this.state = i2;
        }
    }

    private void updateToggleButton(Action action) {
        this.toggleButton.setAction(action);
        this.toggleButton.setText(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Frame getFrame() {
        Window window = getWindow();
        if (window instanceof Frame) {
            return (Frame) window;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Window getWindow() {
        return this.window;
    }

    private String getTitle() {
        Window window = getWindow();
        if (window instanceof Frame) {
            return ((Frame) window).getTitle();
        }
        if (window instanceof Dialog) {
            return ((Dialog) window).getTitle();
        }
        return null;
    }

    public boolean isSelected() {
        Window window = getWindow();
        if (window == null) {
            return true;
        }
        return window.isActive();
    }

    public boolean isFrameMaximized() {
        Frame frame = getFrame();
        return frame != null && (frame.getExtendedState() & 6) == 6;
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        String strClippedText;
        if (getFrame() != null) {
            setState(getFrame().getExtendedState());
        }
        Window window = getWindow();
        boolean zIsLeftToRight = window == null ? getRootPane().getComponentOrientation().isLeftToRight() : window.getComponentOrientation().isLeftToRight();
        boolean zIsActive = window == null ? true : window.isActive();
        int width = getWidth();
        int height = getHeight();
        int iComputeStringWidth = zIsLeftToRight ? 5 : width - 5;
        paintCaption(graphics, width, height, zIsActive, 29, window);
        int windowDecorationStyle = getWindowDecorationStyle();
        if (windowDecorationStyle == 1 || windowDecorationStyle == 2 || windowDecorationStyle == 5 || windowDecorationStyle == 6) {
            iComputeStringWidth += zIsLeftToRight ? 21 : -21;
        }
        String title = getTitle();
        if (title != null) {
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int height2 = ((height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent() + (height == 29 ? 2 : 0);
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            if (this.iconifyButton != null && this.iconifyButton.getParent() != null) {
                rectangle = this.iconifyButton.getBounds();
            }
            if (zIsLeftToRight) {
                if (rectangle.f12372x == 0) {
                    rectangle.f12372x = (window.getWidth() - window.getInsets().right) - 2;
                }
                strClippedText = clippedText(title, fontMetrics, (rectangle.f12372x - iComputeStringWidth) - 4);
            } else {
                strClippedText = clippedText(title, fontMetrics, ((iComputeStringWidth - rectangle.f12372x) - rectangle.width) - 4);
                iComputeStringWidth -= SwingUtilities.computeStringWidth(fontMetrics, strClippedText);
            }
            SwingUtilities.computeStringWidth(fontMetrics, strClippedText);
            if (graphics instanceof Graphics2D) {
                ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            if (!zIsActive) {
                graphics.setColor(Theme.frameTitleDisabledColor.getColor());
                graphics.drawString(strClippedText, iComputeStringWidth, height2);
            } else {
                graphics.setColor(Theme.frameTitleShadowColor.getColor());
                graphics.drawString(strClippedText, iComputeStringWidth + 1, height2 + 1);
                graphics.setColor(Theme.frameTitleColor.getColor());
                graphics.drawString(strClippedText, iComputeStringWidth, height2);
            }
        }
    }

    private void paintCaption(Graphics graphics, int i2, int i3, boolean z2, int i4, Window window) {
        if (TinyLookAndFeel.controlPanelInstantiated) {
            paintXPCaptionNoCache(graphics, i2, i3, z2, i4, window);
        } else {
            paintXPCaption(graphics, i2, i3, z2, i4, window);
        }
    }

    private void paintXPCaption(Graphics graphics, int i2, int i3, boolean z2, int i4, Window window) {
        ColorUIResource color;
        ColorUIResource color2 = z2 ? Theme.frameCaptionColor.getColor() : Theme.frameCaptionDisabledColor.getColor();
        graphics.setColor(color2);
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (z2) {
            color = Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        } else {
            color = Theme.frameBorderDisabledColor.getColor();
        }
        graphics.setColor(ColorRoutines.getAlphaColor(color, 82));
        graphics.drawLine(0, 0, 0, 0);
        graphics.drawLine((0 + i2) - 1, 0, (0 + i2) - 1, 0);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 156));
        graphics.drawLine(0 + 1, 0, 0 + 1, 0);
        graphics.drawLine((0 + i2) - 2, 0, (0 + i2) - 2, 0);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 215));
        graphics.drawLine(0 + 2, 0, 0 + 2, 0);
        graphics.drawLine((0 + i2) - 3, 0, (0 + i2) - 3, 0);
        int i5 = 0 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.drawLine(0, i5, 0 + 2, i5);
        graphics.drawLine((0 + i2) - 3, i5, (0 + i2) - 1, i5);
        int i6 = i5 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(0 + 1, i6, 0 + 2, i6);
        graphics.drawLine((0 + i2) - 3, i6, (0 + i2) - 1, i6);
        graphics.setColor(color2);
        graphics.drawLine(0, i6, 0, i6);
        graphics.drawLine((0 + i2) - 1, i6, (0 + i2) - 1, i6);
        int i7 = i6 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(0, i7, 0, i7);
        graphics.drawLine((0 + i2) - 1, i7, (0 + i2) - 1, i7);
        graphics.setColor(ColorRoutines.lighten(color2, 7 * value2));
        graphics.drawLine(0 + 1, i7, 0 + 1, i7);
        graphics.drawLine((0 + i2) - 2, i7, (0 + i2) - 2, i7);
        graphics.setColor(ColorRoutines.lighten(color2, 3 * value2));
        graphics.drawLine(0 + 2, i7, 0 + 2, i7);
        graphics.drawLine((0 + i2) - 3, i7, (0 + i2) - 3, i7);
        int i8 = i7 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.drawLine(0 + 2, i8, 0 + 2, i8);
        graphics.drawLine(((0 + 0) + i2) - 3, i8, (0 + i2) - 3, i8);
        graphics.setColor(ColorRoutines.lighten(color2, 5 * value2));
        graphics.drawLine(0, i8, 0, i8);
        graphics.drawLine((0 + i2) - 1, i8, (0 + i2) - 1, i8);
        graphics.setColor(color2);
        graphics.drawLine(0 + 1, i8, 0 + 1, i8);
        graphics.drawLine((0 + i2) - 2, i8, (0 + i2) - 2, i8);
        int i9 = i8 + 1;
        CaptionKey captionKey = new CaptionKey(Theme.frameCaptionColor.getColor(), z2, i4);
        Object obj = cache.get(captionKey);
        if (obj != null) {
            graphics.drawImage((Image) obj, 0 + 3, 0, (0 + i2) - 3, 0 + 5, 0, 0, 1, 5, window);
            graphics.drawImage((Image) obj, 0, 0 + 5, 0 + i2, 0 + i4, 0, 5, 1, i4, window);
            buttonUpperColor = ColorRoutines.darken(color2, 4 * value);
            buttonLowerColor = ColorRoutines.lighten(color2, 10 * value2);
            return;
        }
        BufferedImage bufferedImage = new BufferedImage(1, i4, 2);
        Graphics graphics2 = bufferedImage.getGraphics();
        graphics2.setColor(color);
        graphics2.drawLine(0, 0, 1, 0);
        graphics2.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics2.drawLine(0, 1, 1, 1);
        graphics2.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics2.drawLine(0, 2, 1, 2);
        graphics2.setColor(color2);
        graphics2.drawLine(0, 3, 1, 3);
        graphics2.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics2.drawLine(0, 4, 1, 4);
        buttonUpperColor = ColorRoutines.darken(color2, 4 * value);
        graphics2.setColor(buttonUpperColor);
        graphics2.drawLine(0, 5, 1, 5);
        graphics2.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics2.drawLine(0, 6, 1, 6);
        graphics2.drawLine(0, 7, 1, 7);
        graphics2.setColor(ColorRoutines.darken(color2, 3 * value));
        graphics2.drawLine(0, 8, 1, 8);
        graphics2.drawLine(0, 9, 1, 9);
        graphics2.drawLine(0, 10, 1, 10);
        graphics2.drawLine(0, 11, 1, 11);
        graphics2.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics2.drawLine(0, 12, 1, 12);
        graphics2.drawLine(0, 13, 1, 13);
        graphics2.drawLine(0, 14, 1, 14);
        graphics2.setColor(ColorRoutines.darken(color2, value));
        graphics2.drawLine(0, 15, 1, 15);
        graphics2.drawLine(0, 16, 1, 16);
        graphics2.setColor(color2);
        graphics2.drawLine(0, 17, 1, 17);
        graphics2.drawLine(0, 18, 1, 18);
        graphics2.setColor(ColorRoutines.lighten(color2, 2 * value2));
        graphics2.drawLine(0, 19, 1, 19);
        graphics2.setColor(ColorRoutines.lighten(color2, 4 * value2));
        graphics2.drawLine(0, 20, 1, 20);
        graphics2.setColor(ColorRoutines.lighten(color2, 5 * value2));
        graphics2.drawLine(0, 21, 1, 21);
        graphics2.setColor(ColorRoutines.lighten(color2, 6 * value2));
        graphics2.drawLine(0, 22, 1, 22);
        graphics2.setColor(ColorRoutines.lighten(color2, 8 * value2));
        graphics2.drawLine(0, 23, 1, 23);
        graphics2.setColor(ColorRoutines.lighten(color2, 9 * value2));
        graphics2.drawLine(0, 24, 1, 24);
        buttonLowerColor = ColorRoutines.lighten(color2, 10 * value2);
        graphics2.setColor(buttonLowerColor);
        graphics2.drawLine(0, 25, 1, 25);
        graphics2.setColor(ColorRoutines.lighten(color2, 4 * value2));
        graphics2.drawLine(0, 26, 1, 26);
        graphics2.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics2.drawLine(0, 27, 1, 27);
        if (z2) {
            graphics2.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics2.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics2.drawLine(0, 28, 1, 28);
        graphics2.dispose();
        graphics.drawImage(bufferedImage, 0 + 3, 0, (0 + i2) - 3, 0 + 5, 0, 0, 1, 5, window);
        graphics.drawImage(bufferedImage, 0, 0 + 5, 0 + i2, 0 + i4, 0, 5, 1, i4, window);
        cache.put(captionKey, bufferedImage);
    }

    private void paintXPCaptionNoCache(Graphics graphics, int i2, int i3, boolean z2, int i4, Window window) {
        ColorUIResource color;
        ColorUIResource color2 = z2 ? Theme.frameCaptionColor.getColor() : Theme.frameCaptionDisabledColor.getColor();
        graphics.setColor(color2);
        int value = Theme.frameSpreadDarkDisabled.getValue();
        int value2 = Theme.frameSpreadLightDisabled.getValue();
        if (z2) {
            color = Theme.frameBorderColor.getColor();
            value = Theme.frameSpreadDark.getValue();
            value2 = Theme.frameSpreadLight.getValue();
        } else {
            color = Theme.frameBorderDisabledColor.getColor();
        }
        graphics.setColor(ColorRoutines.getAlphaColor(color, 82));
        graphics.drawLine(0, 0, 0, 0);
        graphics.drawLine((0 + i2) - 1, 0, (0 + i2) - 1, 0);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 156));
        graphics.drawLine(0 + 1, 0, 0 + 1, 0);
        graphics.drawLine((0 + i2) - 2, 0, (0 + i2) - 2, 0);
        graphics.setColor(ColorRoutines.getAlphaColor(color, 215));
        graphics.drawLine(0 + 2, 0, 0 + 2, 0);
        graphics.drawLine((0 + i2) - 3, 0, (0 + i2) - 3, 0);
        int i5 = 0 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.drawLine(0, i5, 0 + 2, i5);
        graphics.drawLine((0 + i2) - 3, i5, (0 + i2) - 1, i5);
        int i6 = i5 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(0 + 1, i6, 0 + 2, i6);
        graphics.drawLine((0 + i2) - 3, i6, (0 + i2) - 1, i6);
        graphics.setColor(color2);
        graphics.drawLine(0, i6, 0, i6);
        graphics.drawLine((0 + i2) - 1, i6, (0 + i2) - 1, i6);
        int i7 = i6 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(0, i7, 0, i7);
        graphics.drawLine((0 + i2) - 1, i7, (0 + i2) - 1, i7);
        graphics.setColor(ColorRoutines.lighten(color2, 7 * value2));
        graphics.drawLine(0 + 1, i7, 0 + 1, i7);
        graphics.drawLine((0 + i2) - 2, i7, (0 + i2) - 2, i7);
        graphics.setColor(ColorRoutines.lighten(color2, 3 * value2));
        graphics.drawLine(0 + 2, i7, 0 + 2, i7);
        graphics.drawLine((0 + i2) - 3, i7, (0 + i2) - 3, i7);
        int i8 = i7 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.drawLine(0 + 2, i8, 0 + 2, i8);
        graphics.drawLine(((0 + 0) + i2) - 3, i8, (0 + i2) - 3, i8);
        graphics.setColor(ColorRoutines.lighten(color2, 5 * value2));
        graphics.drawLine(0, i8, 0, i8);
        graphics.drawLine((0 + i2) - 1, i8, (0 + i2) - 1, i8);
        graphics.setColor(color2);
        graphics.drawLine(0 + 1, i8, 0 + 1, i8);
        graphics.drawLine((0 + i2) - 2, i8, (0 + i2) - 2, i8);
        graphics.setColor(color);
        graphics.drawLine(0 + 3, 0, (0 + i2) - 4, 0);
        int i9 = 0 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.drawLine(0 + 3, i9, (0 + i2) - 4, i9);
        int i10 = i9 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 10 * value2));
        graphics.drawLine(0 + 3, i10, (0 + i2) - 4, i10);
        int i11 = i10 + 1;
        graphics.setColor(color2);
        graphics.drawLine(0 + 3, i11, (0 + i2) - 4, i11);
        int i12 = i11 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.drawLine(0 + 3, i12, (0 + i2) - 4, i12);
        int i13 = i12 + 1;
        buttonUpperColor = ColorRoutines.darken(color2, 4 * value);
        graphics.setColor(buttonUpperColor);
        graphics.drawLine(0, i13, (0 + i2) - 1, i13);
        int i14 = i13 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 4 * value));
        graphics.fillRect(0, i14, 0 + i2, 2);
        int i15 = i14 + 2;
        graphics.setColor(ColorRoutines.darken(color2, 3 * value));
        graphics.fillRect(0, i15, 0 + i2, 4);
        int i16 = i15 + 4;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.fillRect(0, i16, 0 + i2, 3);
        int i17 = i16 + 3;
        graphics.setColor(ColorRoutines.darken(color2, 1 * value));
        graphics.fillRect(0, i17, 0 + i2, 2);
        int i18 = i17 + 2;
        graphics.setColor(color2);
        graphics.fillRect(0, i18, 0 + i2, 2);
        int i19 = i18 + 2;
        graphics.setColor(ColorRoutines.lighten(color2, 2 * value2));
        graphics.drawLine(0, i19, (0 + i2) - 1, i19);
        int i20 = i19 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 4 * value2));
        graphics.drawLine(0, i20, (0 + i2) - 1, i20);
        int i21 = i20 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 5 * value2));
        graphics.drawLine(0, i21, (0 + i2) - 1, i21);
        int i22 = i21 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 6 * value2));
        graphics.drawLine(0, i22, (0 + i2) - 1, i22);
        int i23 = i22 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 8 * value2));
        graphics.drawLine(0, i23, (0 + i2) - 1, i23);
        int i24 = i23 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 9 * value2));
        graphics.drawLine(0, i24, (0 + i2) - 1, i24);
        int i25 = i24 + 1;
        buttonLowerColor = ColorRoutines.lighten(color2, 10 * value2);
        graphics.setColor(buttonLowerColor);
        graphics.drawLine(0, i25, (0 + i2) - 1, i25);
        int i26 = i25 + 1;
        graphics.setColor(ColorRoutines.lighten(color2, 4 * value2));
        graphics.drawLine(0, i26, (0 + i2) - 1, i26);
        int i27 = i26 + 1;
        graphics.setColor(ColorRoutines.darken(color2, 2 * value));
        graphics.drawLine(0, i27, (0 + i2) - 1, i27);
        int i28 = i27 + 1;
        if (z2) {
            graphics.setColor(Theme.frameLightColor.getColor());
        } else {
            graphics.setColor(Theme.frameLightDisabledColor.getColor());
        }
        graphics.drawLine(0, i28, (0 + i2) - 1, i28);
    }

    private String clippedText(String str, FontMetrics fontMetrics, int i2) {
        if (str == null || str.equals("")) {
            return "";
        }
        if (SwingUtilities.computeStringWidth(fontMetrics, str) > i2) {
            int iComputeStringWidth = SwingUtilities.computeStringWidth(fontMetrics, "...");
            int i3 = 0;
            while (i3 < str.length()) {
                iComputeStringWidth += fontMetrics.charWidth(str.charAt(i3));
                if (iComputeStringWidth > i2) {
                    break;
                }
                i3++;
            }
            str = new StringBuffer().append(str.substring(0, i3)).append("...").toString();
        }
        return str;
    }

    private int getInt(Object obj, int i2) {
        Object obj2 = UIManager.get(obj);
        if (obj2 instanceof Integer) {
            return ((Integer) obj2).intValue();
        }
        if (obj2 instanceof String) {
            try {
                return Integer.parseInt((String) obj2);
            } catch (NumberFormatException e2) {
            }
        }
        return i2;
    }
}
