package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRootPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import sun.awt.SunToolkit;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane.class */
class MetalTitlePane extends JComponent {
    private static final Border handyEmptyBorder;
    private static final int IMAGE_HEIGHT = 16;
    private static final int IMAGE_WIDTH = 16;
    private PropertyChangeListener propertyChangeListener;
    private JMenuBar menuBar;
    private Action closeAction;
    private Action iconifyAction;
    private Action restoreAction;
    private Action maximizeAction;
    private JButton toggleButton;
    private JButton iconifyButton;
    private JButton closeButton;
    private Icon maximizeIcon;
    private Icon minimizeIcon;
    private Image systemIcon;
    private WindowListener windowListener;
    private Window window;
    private JRootPane rootPane;
    private int buttonsWidth;
    private MetalRootPaneUI rootPaneUI;
    static final /* synthetic */ boolean $assertionsDisabled;
    private Color inactiveBackground = UIManager.getColor("inactiveCaption");
    private Color inactiveForeground = UIManager.getColor("inactiveCaptionText");
    private Color inactiveShadow = UIManager.getColor("inactiveCaptionBorder");
    private Color activeBumpsHighlight = MetalLookAndFeel.getPrimaryControlHighlight();
    private Color activeBumpsShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
    private Color activeBackground = null;
    private Color activeForeground = null;
    private Color activeShadow = null;
    private MetalBumps activeBumps = new MetalBumps(0, 0, this.activeBumpsHighlight, this.activeBumpsShadow, MetalLookAndFeel.getPrimaryControl());
    private MetalBumps inactiveBumps = new MetalBumps(0, 0, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), MetalLookAndFeel.getControl());
    private int state = -1;

    static {
        $assertionsDisabled = !MetalTitlePane.class.desiredAssertionStatus();
        handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);
    }

    public MetalTitlePane(JRootPane jRootPane, MetalRootPaneUI metalRootPaneUI) {
        this.rootPane = jRootPane;
        this.rootPaneUI = metalRootPaneUI;
        installSubcomponents();
        determineColors();
        installDefaults();
        setLayout(createLayout());
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
        }
    }

    private void uninstallListeners() {
        if (this.window != null) {
            this.window.removeWindowListener(this.windowListener);
            this.window.removePropertyChangeListener(this.propertyChangeListener);
        }
    }

    private WindowListener createWindowListener() {
        return new WindowHandler();
    }

    private PropertyChangeListener createWindowPropertyChangeListener() {
        return new PropertyChangeHandler();
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
            createButtons();
            add(this.iconifyButton);
            add(this.toggleButton);
            add(this.closeButton);
            return;
        }
        if (windowDecorationStyle == 2 || windowDecorationStyle == 3 || windowDecorationStyle == 4 || windowDecorationStyle == 5 || windowDecorationStyle == 6 || windowDecorationStyle == 7 || windowDecorationStyle == 8) {
            createActions();
            createButtons();
            add(this.closeButton);
        }
    }

    private void determineColors() {
        switch (getWindowDecorationStyle()) {
            case 1:
                this.activeBackground = UIManager.getColor("activeCaption");
                this.activeForeground = UIManager.getColor("activeCaptionText");
                this.activeShadow = UIManager.getColor("activeCaptionBorder");
                break;
            case 2:
            case 3:
            default:
                this.activeBackground = UIManager.getColor("activeCaption");
                this.activeForeground = UIManager.getColor("activeCaptionText");
                this.activeShadow = UIManager.getColor("activeCaptionBorder");
                break;
            case 4:
                this.activeBackground = UIManager.getColor("OptionPane.errorDialog.titlePane.background");
                this.activeForeground = UIManager.getColor("OptionPane.errorDialog.titlePane.foreground");
                this.activeShadow = UIManager.getColor("OptionPane.errorDialog.titlePane.shadow");
                break;
            case 5:
            case 6:
            case 7:
                this.activeBackground = UIManager.getColor("OptionPane.questionDialog.titlePane.background");
                this.activeForeground = UIManager.getColor("OptionPane.questionDialog.titlePane.foreground");
                this.activeShadow = UIManager.getColor("OptionPane.questionDialog.titlePane.shadow");
                break;
            case 8:
                this.activeBackground = UIManager.getColor("OptionPane.warningDialog.titlePane.background");
                this.activeForeground = UIManager.getColor("OptionPane.warningDialog.titlePane.foreground");
                this.activeShadow = UIManager.getColor("OptionPane.warningDialog.titlePane.shadow");
                break;
        }
        this.activeBumps.setBumpColors(this.activeBumpsHighlight, this.activeBumpsShadow, this.activeBackground);
    }

    private void installDefaults() {
        setFont(UIManager.getFont("InternalFrame.titleFont", getLocale()));
    }

    private void uninstallDefaults() {
    }

    protected JMenuBar createMenuBar() {
        this.menuBar = new SystemMenuBar();
        this.menuBar.setFocusable(false);
        this.menuBar.setBorderPainted(true);
        this.menuBar.add(createMenu());
        return this.menuBar;
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
            frame.setExtendedState(this.state | 1);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maximize() {
        Frame frame = getFrame();
        if (frame != null) {
            frame.setExtendedState(this.state | 6);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restore() {
        Frame frame = getFrame();
        if (frame == null) {
            return;
        }
        if ((this.state & 1) != 0) {
            frame.setExtendedState(this.state & (-2));
        } else {
            frame.setExtendedState(this.state & (-7));
        }
    }

    private void createActions() {
        this.closeAction = new CloseAction();
        if (getWindowDecorationStyle() == 1) {
            this.iconifyAction = new IconifyAction();
            this.restoreAction = new RestoreAction();
            this.maximizeAction = new MaximizeAction();
        }
    }

    private JMenu createMenu() {
        JMenu jMenu = new JMenu("");
        if (getWindowDecorationStyle() == 1) {
            addMenuItems(jMenu);
        }
        return jMenu;
    }

    private void addMenuItems(JMenu jMenu) {
        getRootPane().getLocale();
        JMenuItem jMenuItemAdd = jMenu.add(this.restoreAction);
        int i2 = MetalUtils.getInt("MetalTitlePane.restoreMnemonic", -1);
        if (i2 != -1) {
            jMenuItemAdd.setMnemonic(i2);
        }
        JMenuItem jMenuItemAdd2 = jMenu.add(this.iconifyAction);
        int i3 = MetalUtils.getInt("MetalTitlePane.iconifyMnemonic", -1);
        if (i3 != -1) {
            jMenuItemAdd2.setMnemonic(i3);
        }
        if (Toolkit.getDefaultToolkit().isFrameStateSupported(6)) {
            JMenuItem jMenuItemAdd3 = jMenu.add(this.maximizeAction);
            int i4 = MetalUtils.getInt("MetalTitlePane.maximizeMnemonic", -1);
            if (i4 != -1) {
                jMenuItemAdd3.setMnemonic(i4);
            }
        }
        jMenu.add(new JSeparator());
        JMenuItem jMenuItemAdd4 = jMenu.add(this.closeAction);
        int i5 = MetalUtils.getInt("MetalTitlePane.closeMnemonic", -1);
        if (i5 != -1) {
            jMenuItemAdd4.setMnemonic(i5);
        }
    }

    private JButton createTitleButton() {
        JButton jButton = new JButton();
        jButton.setFocusPainted(false);
        jButton.setFocusable(false);
        jButton.setOpaque(true);
        return jButton;
    }

    private void createButtons() {
        this.closeButton = createTitleButton();
        this.closeButton.setAction(this.closeAction);
        this.closeButton.setText(null);
        this.closeButton.putClientProperty("paintActive", Boolean.TRUE);
        this.closeButton.setBorder(handyEmptyBorder);
        this.closeButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Close");
        this.closeButton.setIcon(UIManager.getIcon("InternalFrame.closeIcon"));
        if (getWindowDecorationStyle() == 1) {
            this.maximizeIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
            this.minimizeIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
            this.iconifyButton = createTitleButton();
            this.iconifyButton.setAction(this.iconifyAction);
            this.iconifyButton.setText(null);
            this.iconifyButton.putClientProperty("paintActive", Boolean.TRUE);
            this.iconifyButton.setBorder(handyEmptyBorder);
            this.iconifyButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Iconify");
            this.iconifyButton.setIcon(UIManager.getIcon("InternalFrame.iconifyIcon"));
            this.toggleButton = createTitleButton();
            this.toggleButton.setAction(this.restoreAction);
            this.toggleButton.putClientProperty("paintActive", Boolean.TRUE);
            this.toggleButton.setBorder(handyEmptyBorder);
            this.toggleButton.putClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, "Maximize");
            this.toggleButton.setIcon(this.maximizeIcon);
        }
    }

    private LayoutManager createLayout() {
        return new TitlePaneLayout();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setActive(boolean z2) {
        Boolean bool = z2 ? Boolean.TRUE : Boolean.FALSE;
        this.closeButton.putClientProperty("paintActive", bool);
        if (getWindowDecorationStyle() == 1) {
            this.iconifyButton.putClientProperty("paintActive", bool);
            this.toggleButton.putClientProperty("paintActive", bool);
        }
        getRootPane().repaint();
    }

    private void setState(int i2) {
        setState(i2, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setState(int i2, boolean z2) {
        if (getWindow() != null && getWindowDecorationStyle() == 1) {
            if (this.state == i2 && !z2) {
                return;
            }
            Frame frame = getFrame();
            if (frame != null) {
                JRootPane rootPane = getRootPane();
                if ((i2 & 6) != 0 && ((rootPane.getBorder() == null || (rootPane.getBorder() instanceof UIResource)) && frame.isShowing())) {
                    rootPane.setBorder(null);
                } else if ((i2 & 6) == 0) {
                    this.rootPaneUI.installBorder(rootPane);
                }
                if (frame.isResizable()) {
                    if ((i2 & 6) != 0) {
                        updateToggleButton(this.restoreAction, this.minimizeIcon);
                        this.maximizeAction.setEnabled(false);
                        this.restoreAction.setEnabled(true);
                    } else {
                        updateToggleButton(this.maximizeAction, this.maximizeIcon);
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

    private void updateToggleButton(Action action, Icon icon) {
        this.toggleButton.setAction(action);
        this.toggleButton.setIcon(icon);
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

    private Window getWindow() {
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

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        boolean zIsLeftToRight;
        Color color;
        Color color2;
        Color color3;
        MetalBumps metalBumps;
        int i2;
        int i3;
        String strClipStringIfNecessary;
        if (getFrame() != null) {
            setState(getFrame().getExtendedState());
        }
        JRootPane rootPane = getRootPane();
        Window window = getWindow();
        if (window == null) {
            zIsLeftToRight = rootPane.getComponentOrientation().isLeftToRight();
        } else {
            zIsLeftToRight = window.getComponentOrientation().isLeftToRight();
        }
        boolean z2 = zIsLeftToRight;
        boolean zIsActive = window == null ? true : window.isActive();
        int width = getWidth();
        int height = getHeight();
        if (zIsActive) {
            color = this.activeBackground;
            color2 = this.activeForeground;
            color3 = this.activeShadow;
            metalBumps = this.activeBumps;
        } else {
            color = this.inactiveBackground;
            color2 = this.inactiveForeground;
            color3 = this.inactiveShadow;
            metalBumps = this.inactiveBumps;
        }
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(color3);
        graphics.drawLine(0, height - 1, width, height - 1);
        graphics.drawLine(0, 0, 0, 0);
        graphics.drawLine(width - 1, 0, width - 1, 0);
        int iStringWidth = z2 ? 5 : width - 5;
        if (getWindowDecorationStyle() == 1) {
            iStringWidth += z2 ? 21 : -21;
        }
        String title = getTitle();
        if (title != null) {
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(rootPane, graphics);
            graphics.setColor(color2);
            int height2 = ((height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            if (this.iconifyButton != null && this.iconifyButton.getParent() != null) {
                rectangle = this.iconifyButton.getBounds();
            }
            if (z2) {
                if (rectangle.f12372x == 0) {
                    rectangle.f12372x = (window.getWidth() - window.getInsets().right) - 2;
                }
                strClipStringIfNecessary = SwingUtilities2.clipStringIfNecessary(rootPane, fontMetrics, title, (rectangle.f12372x - iStringWidth) - 4);
            } else {
                strClipStringIfNecessary = SwingUtilities2.clipStringIfNecessary(rootPane, fontMetrics, title, ((iStringWidth - rectangle.f12372x) - rectangle.width) - 4);
                iStringWidth -= SwingUtilities2.stringWidth(rootPane, fontMetrics, strClipStringIfNecessary);
            }
            int iStringWidth2 = SwingUtilities2.stringWidth(rootPane, fontMetrics, strClipStringIfNecessary);
            SwingUtilities2.drawString(rootPane, graphics, strClipStringIfNecessary, iStringWidth, height2);
            iStringWidth += z2 ? iStringWidth2 + 5 : -5;
        }
        if (z2) {
            i2 = ((width - this.buttonsWidth) - iStringWidth) - 5;
            i3 = iStringWidth;
        } else {
            i2 = (iStringWidth - this.buttonsWidth) - 5;
            i3 = this.buttonsWidth + 5;
        }
        metalBumps.setBumpArea(i2, getHeight() - (2 * 3));
        metalBumps.paintIcon(this, graphics, i3, 3);
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$CloseAction.class */
    private class CloseAction extends AbstractAction {
        public CloseAction() {
            super(UIManager.getString("MetalTitlePane.closeTitle", MetalTitlePane.this.getLocale()));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MetalTitlePane.this.close();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$IconifyAction.class */
    private class IconifyAction extends AbstractAction {
        public IconifyAction() {
            super(UIManager.getString("MetalTitlePane.iconifyTitle", MetalTitlePane.this.getLocale()));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MetalTitlePane.this.iconify();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$RestoreAction.class */
    private class RestoreAction extends AbstractAction {
        public RestoreAction() {
            super(UIManager.getString("MetalTitlePane.restoreTitle", MetalTitlePane.this.getLocale()));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MetalTitlePane.this.restore();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$MaximizeAction.class */
    private class MaximizeAction extends AbstractAction {
        public MaximizeAction() {
            super(UIManager.getString("MetalTitlePane.maximizeTitle", MetalTitlePane.this.getLocale()));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MetalTitlePane.this.maximize();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$SystemMenuBar.class */
    private class SystemMenuBar extends JMenuBar {
        private SystemMenuBar() {
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            if (isOpaque()) {
                graphics.setColor(getBackground());
                graphics.fillRect(0, 0, getWidth(), getHeight());
            }
            if (MetalTitlePane.this.systemIcon != null) {
                graphics.drawImage(MetalTitlePane.this.systemIcon, 0, 0, 16, 16, null);
                return;
            }
            Icon icon = UIManager.getIcon("InternalFrame.icon");
            if (icon != null) {
                icon.paintIcon(this, graphics, 0, 0);
            }
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            Dimension preferredSize = super.getPreferredSize();
            return new Dimension(Math.max(16, preferredSize.width), Math.max(preferredSize.height, 16));
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$TitlePaneLayout.class */
    private class TitlePaneLayout implements LayoutManager {
        private TitlePaneLayout() {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            int iComputeHeight = computeHeight();
            return new Dimension(iComputeHeight, iComputeHeight);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return preferredLayoutSize(container);
        }

        private int computeHeight() {
            int height = MetalTitlePane.this.rootPane.getFontMetrics(MetalTitlePane.this.getFont()).getHeight() + 7;
            int i2 = 0;
            if (MetalTitlePane.this.getWindowDecorationStyle() == 1) {
                i2 = 16;
            }
            return Math.max(height, i2);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            boolean zIsLeftToRight;
            int iconHeight;
            int iconWidth;
            if (MetalTitlePane.this.window != null) {
                zIsLeftToRight = MetalTitlePane.this.window.getComponentOrientation().isLeftToRight();
            } else {
                zIsLeftToRight = MetalTitlePane.this.getRootPane().getComponentOrientation().isLeftToRight();
            }
            boolean z2 = zIsLeftToRight;
            int width = MetalTitlePane.this.getWidth();
            if (MetalTitlePane.this.closeButton != null && MetalTitlePane.this.closeButton.getIcon() != null) {
                iconHeight = MetalTitlePane.this.closeButton.getIcon().getIconHeight();
                iconWidth = MetalTitlePane.this.closeButton.getIcon().getIconWidth();
            } else {
                iconHeight = 16;
                iconWidth = 16;
            }
            int i2 = z2 ? width : 0;
            int i3 = z2 ? 5 : (width - iconWidth) - 5;
            if (MetalTitlePane.this.menuBar != null) {
                MetalTitlePane.this.menuBar.setBounds(i3, 3, iconWidth, iconHeight);
            }
            int i4 = (z2 ? width : 0) + (z2 ? (-4) - iconWidth : 4);
            if (MetalTitlePane.this.closeButton != null) {
                MetalTitlePane.this.closeButton.setBounds(i4, 3, iconWidth, iconHeight);
            }
            if (!z2) {
                i4 += iconWidth;
            }
            if (MetalTitlePane.this.getWindowDecorationStyle() == 1) {
                if (Toolkit.getDefaultToolkit().isFrameStateSupported(6) && MetalTitlePane.this.toggleButton.getParent() != null) {
                    i4 += z2 ? (-10) - iconWidth : 10;
                    MetalTitlePane.this.toggleButton.setBounds(i4, 3, iconWidth, iconHeight);
                    if (!z2) {
                        i4 += iconWidth;
                    }
                }
                if (MetalTitlePane.this.iconifyButton != null && MetalTitlePane.this.iconifyButton.getParent() != null) {
                    i4 += z2 ? (-2) - iconWidth : 2;
                    MetalTitlePane.this.iconifyButton.setBounds(i4, 3, iconWidth, iconHeight);
                    if (!z2) {
                        i4 += iconWidth;
                    }
                }
            }
            MetalTitlePane.this.buttonsWidth = z2 ? width - i4 : i4;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$PropertyChangeHandler.class */
    private class PropertyChangeHandler implements PropertyChangeListener {
        private PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if ("resizable".equals(propertyName) || "state".equals(propertyName)) {
                Frame frame = MetalTitlePane.this.getFrame();
                if (frame != null) {
                    MetalTitlePane.this.setState(frame.getExtendedState(), true);
                }
                if ("resizable".equals(propertyName)) {
                    MetalTitlePane.this.getRootPane().repaint();
                    return;
                }
                return;
            }
            if ("title".equals(propertyName)) {
                MetalTitlePane.this.repaint();
                return;
            }
            if ("componentOrientation" == propertyName) {
                MetalTitlePane.this.revalidate();
                MetalTitlePane.this.repaint();
            } else if ("iconImage" == propertyName) {
                MetalTitlePane.this.updateSystemIcon();
                MetalTitlePane.this.revalidate();
                MetalTitlePane.this.repaint();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSystemIcon() {
        Window window = getWindow();
        if (window == null) {
            this.systemIcon = null;
            return;
        }
        List<Image> iconImages = window.getIconImages();
        if (!$assertionsDisabled && iconImages == null) {
            throw new AssertionError();
        }
        if (iconImages.size() == 0) {
            this.systemIcon = null;
        } else if (iconImages.size() == 1) {
            this.systemIcon = iconImages.get(0);
        } else {
            this.systemIcon = SunToolkit.getScaledIconImage(iconImages, 16, 16);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalTitlePane$WindowHandler.class */
    private class WindowHandler extends WindowAdapter {
        private WindowHandler() {
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
            MetalTitlePane.this.setActive(true);
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
            MetalTitlePane.this.setActive(false);
        }
    }
}
