package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.accessibility.AccessibleContext;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.plaf.ActionMapUIResource;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane.class */
public class BasicInternalFrameTitlePane extends JComponent {
    protected JMenuBar menuBar;
    protected JButton iconButton;
    protected JButton maxButton;
    protected JButton closeButton;
    protected JMenu windowMenu;
    protected JInternalFrame frame;
    protected Color selectedTitleColor;
    protected Color selectedTextColor;
    protected Color notSelectedTitleColor;
    protected Color notSelectedTextColor;
    protected Icon maxIcon;
    protected Icon minIcon;
    protected Icon iconIcon;
    protected Icon closeIcon;
    protected PropertyChangeListener propertyChangeListener;
    protected Action closeAction;
    protected Action maximizeAction;
    protected Action iconifyAction;
    protected Action restoreAction;
    protected Action moveAction;
    protected Action sizeAction;
    protected static final String CLOSE_CMD = UIManager.getString("InternalFrameTitlePane.closeButtonText");
    protected static final String ICONIFY_CMD = UIManager.getString("InternalFrameTitlePane.minimizeButtonText");
    protected static final String RESTORE_CMD = UIManager.getString("InternalFrameTitlePane.restoreButtonText");
    protected static final String MAXIMIZE_CMD = UIManager.getString("InternalFrameTitlePane.maximizeButtonText");
    protected static final String MOVE_CMD = UIManager.getString("InternalFrameTitlePane.moveButtonText");
    protected static final String SIZE_CMD = UIManager.getString("InternalFrameTitlePane.sizeButtonText");
    private String closeButtonToolTip;
    private String iconButtonToolTip;
    private String restoreButtonToolTip;
    private String maxButtonToolTip;
    private Handler handler;

    public BasicInternalFrameTitlePane(JInternalFrame jInternalFrame) {
        this.frame = jInternalFrame;
        installTitlePane();
    }

    protected void installTitlePane() {
        installDefaults();
        installListeners();
        createActions();
        enableActions();
        createActionMap();
        setLayout(createLayout());
        assembleSystemMenu();
        createButtons();
        addSubComponents();
        updateProperties();
    }

    private void updateProperties() {
        putClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY, this.frame.getClientProperty(SwingUtilities2.AA_TEXT_PROPERTY_KEY));
    }

    protected void addSubComponents() {
        add(this.menuBar);
        add(this.iconButton);
        add(this.maxButton);
        add(this.closeButton);
    }

    protected void createActions() {
        this.maximizeAction = new MaximizeAction();
        this.iconifyAction = new IconifyAction();
        this.closeAction = new CloseAction();
        this.restoreAction = new RestoreAction();
        this.moveAction = new MoveAction();
        this.sizeAction = new SizeAction();
    }

    ActionMap createActionMap() {
        ActionMapUIResource actionMapUIResource = new ActionMapUIResource();
        actionMapUIResource.put("showSystemMenu", new ShowSystemMenuAction(true));
        actionMapUIResource.put("hideSystemMenu", new ShowSystemMenuAction(false));
        return actionMapUIResource;
    }

    protected void installListeners() {
        if (this.propertyChangeListener == null) {
            this.propertyChangeListener = createPropertyChangeListener();
        }
        this.frame.addPropertyChangeListener(this.propertyChangeListener);
    }

    protected void uninstallListeners() {
        this.frame.removePropertyChangeListener(this.propertyChangeListener);
        this.handler = null;
    }

    protected void installDefaults() {
        this.maxIcon = UIManager.getIcon("InternalFrame.maximizeIcon");
        this.minIcon = UIManager.getIcon("InternalFrame.minimizeIcon");
        this.iconIcon = UIManager.getIcon("InternalFrame.iconifyIcon");
        this.closeIcon = UIManager.getIcon("InternalFrame.closeIcon");
        this.selectedTitleColor = UIManager.getColor("InternalFrame.activeTitleBackground");
        this.selectedTextColor = UIManager.getColor("InternalFrame.activeTitleForeground");
        this.notSelectedTitleColor = UIManager.getColor("InternalFrame.inactiveTitleBackground");
        this.notSelectedTextColor = UIManager.getColor("InternalFrame.inactiveTitleForeground");
        setFont(UIManager.getFont("InternalFrame.titleFont"));
        this.closeButtonToolTip = UIManager.getString("InternalFrame.closeButtonToolTip");
        this.iconButtonToolTip = UIManager.getString("InternalFrame.iconButtonToolTip");
        this.restoreButtonToolTip = UIManager.getString("InternalFrame.restoreButtonToolTip");
        this.maxButtonToolTip = UIManager.getString("InternalFrame.maxButtonToolTip");
    }

    protected void uninstallDefaults() {
    }

    protected void createButtons() {
        this.iconButton = new NoFocusButton("InternalFrameTitlePane.iconifyButtonAccessibleName", "InternalFrameTitlePane.iconifyButtonOpacity");
        this.iconButton.addActionListener(this.iconifyAction);
        if (this.iconButtonToolTip != null && this.iconButtonToolTip.length() != 0) {
            this.iconButton.setToolTipText(this.iconButtonToolTip);
        }
        this.maxButton = new NoFocusButton("InternalFrameTitlePane.maximizeButtonAccessibleName", "InternalFrameTitlePane.maximizeButtonOpacity");
        this.maxButton.addActionListener(this.maximizeAction);
        this.closeButton = new NoFocusButton("InternalFrameTitlePane.closeButtonAccessibleName", "InternalFrameTitlePane.closeButtonOpacity");
        this.closeButton.addActionListener(this.closeAction);
        if (this.closeButtonToolTip != null && this.closeButtonToolTip.length() != 0) {
            this.closeButton.setToolTipText(this.closeButtonToolTip);
        }
        setButtonIcons();
    }

    protected void setButtonIcons() {
        if (this.frame.isIcon()) {
            if (this.minIcon != null) {
                this.iconButton.setIcon(this.minIcon);
            }
            if (this.restoreButtonToolTip != null && this.restoreButtonToolTip.length() != 0) {
                this.iconButton.setToolTipText(this.restoreButtonToolTip);
            }
            if (this.maxIcon != null) {
                this.maxButton.setIcon(this.maxIcon);
            }
            if (this.maxButtonToolTip != null && this.maxButtonToolTip.length() != 0) {
                this.maxButton.setToolTipText(this.maxButtonToolTip);
            }
        } else if (this.frame.isMaximum()) {
            if (this.iconIcon != null) {
                this.iconButton.setIcon(this.iconIcon);
            }
            if (this.iconButtonToolTip != null && this.iconButtonToolTip.length() != 0) {
                this.iconButton.setToolTipText(this.iconButtonToolTip);
            }
            if (this.minIcon != null) {
                this.maxButton.setIcon(this.minIcon);
            }
            if (this.restoreButtonToolTip != null && this.restoreButtonToolTip.length() != 0) {
                this.maxButton.setToolTipText(this.restoreButtonToolTip);
            }
        } else {
            if (this.iconIcon != null) {
                this.iconButton.setIcon(this.iconIcon);
            }
            if (this.iconButtonToolTip != null && this.iconButtonToolTip.length() != 0) {
                this.iconButton.setToolTipText(this.iconButtonToolTip);
            }
            if (this.maxIcon != null) {
                this.maxButton.setIcon(this.maxIcon);
            }
            if (this.maxButtonToolTip != null && this.maxButtonToolTip.length() != 0) {
                this.maxButton.setToolTipText(this.maxButtonToolTip);
            }
        }
        if (this.closeIcon != null) {
            this.closeButton.setIcon(this.closeIcon);
        }
    }

    protected void assembleSystemMenu() {
        this.menuBar = createSystemMenuBar();
        this.windowMenu = createSystemMenu();
        this.menuBar.add(this.windowMenu);
        addSystemMenuItems(this.windowMenu);
        enableActions();
    }

    protected void addSystemMenuItems(JMenu jMenu) {
        jMenu.add(this.restoreAction).setMnemonic(getButtonMnemonic("restore"));
        jMenu.add(this.moveAction).setMnemonic(getButtonMnemonic("move"));
        jMenu.add(this.sizeAction).setMnemonic(getButtonMnemonic("size"));
        jMenu.add(this.iconifyAction).setMnemonic(getButtonMnemonic("minimize"));
        jMenu.add(this.maximizeAction).setMnemonic(getButtonMnemonic("maximize"));
        jMenu.add(new JSeparator());
        jMenu.add(this.closeAction).setMnemonic(getButtonMnemonic("close"));
    }

    private static int getButtonMnemonic(String str) {
        try {
            return Integer.parseInt(UIManager.getString("InternalFrameTitlePane." + str + "Button.mnemonic"));
        } catch (NumberFormatException e2) {
            return -1;
        }
    }

    protected JMenu createSystemMenu() {
        return new JMenu("    ");
    }

    protected JMenuBar createSystemMenuBar() {
        this.menuBar = new SystemMenuBar();
        this.menuBar.setBorderPainted(false);
        return this.menuBar;
    }

    protected void showSystemMenu() {
        this.windowMenu.doClick();
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        int x2;
        paintTitleBackground(graphics);
        if (this.frame.getTitle() != null) {
            boolean zIsSelected = this.frame.isSelected();
            Font font = graphics.getFont();
            graphics.setFont(getFont());
            if (zIsSelected) {
                graphics.setColor(this.selectedTextColor);
            } else {
                graphics.setColor(this.notSelectedTextColor);
            }
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.frame, graphics);
            int height = (((getHeight() + fontMetrics.getAscent()) - fontMetrics.getLeading()) - fontMetrics.getDescent()) / 2;
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            if (this.frame.isIconifiable()) {
                rectangle = this.iconButton.getBounds();
            } else if (this.frame.isMaximizable()) {
                rectangle = this.maxButton.getBounds();
            } else if (this.frame.isClosable()) {
                rectangle = this.closeButton.getBounds();
            }
            String title = this.frame.getTitle();
            if (BasicGraphicsUtils.isLeftToRight(this.frame)) {
                if (rectangle.f12372x == 0) {
                    rectangle.f12372x = this.frame.getWidth() - this.frame.getInsets().right;
                }
                x2 = this.menuBar.getX() + this.menuBar.getWidth() + 2;
                title = getTitle(this.frame.getTitle(), fontMetrics, (rectangle.f12372x - x2) - 3);
            } else {
                x2 = (this.menuBar.getX() - 2) - SwingUtilities2.stringWidth(this.frame, fontMetrics, title);
            }
            SwingUtilities2.drawString(this.frame, graphics, title, x2, height);
            graphics.setFont(font);
        }
    }

    protected void paintTitleBackground(Graphics graphics) {
        if (this.frame.isSelected()) {
            graphics.setColor(this.selectedTitleColor);
        } else {
            graphics.setColor(this.notSelectedTitleColor);
        }
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    protected String getTitle(String str, FontMetrics fontMetrics, int i2) {
        return SwingUtilities2.clipStringIfNecessary(this.frame, fontMetrics, str, i2);
    }

    protected void postClosingEvent(JInternalFrame jInternalFrame) {
        InternalFrameEvent internalFrameEvent = new InternalFrameEvent(jInternalFrame, InternalFrameEvent.INTERNAL_FRAME_CLOSING);
        try {
            Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(internalFrameEvent);
        } catch (SecurityException e2) {
            jInternalFrame.dispatchEvent(internalFrameEvent);
        }
    }

    protected void enableActions() {
        this.restoreAction.setEnabled(this.frame.isMaximum() || this.frame.isIcon());
        this.maximizeAction.setEnabled(!(!this.frame.isMaximizable() || this.frame.isMaximum() || this.frame.isIcon()) || (this.frame.isMaximizable() && this.frame.isIcon()));
        this.iconifyAction.setEnabled(this.frame.isIconifiable() && !this.frame.isIcon());
        this.closeAction.setEnabled(this.frame.isClosable());
        this.sizeAction.setEnabled(false);
        this.moveAction.setEnabled(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected LayoutManager createLayout() {
        return getHandler();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$Handler.class */
    private class Handler implements LayoutManager, PropertyChangeListener {
        private Handler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == JInternalFrame.IS_SELECTED_PROPERTY) {
                BasicInternalFrameTitlePane.this.repaint();
                return;
            }
            if (propertyName == "icon" || propertyName == JInternalFrame.IS_MAXIMUM_PROPERTY) {
                BasicInternalFrameTitlePane.this.setButtonIcons();
                BasicInternalFrameTitlePane.this.enableActions();
                return;
            }
            if ("closable" == propertyName) {
                if (propertyChangeEvent.getNewValue() == Boolean.TRUE) {
                    BasicInternalFrameTitlePane.this.add(BasicInternalFrameTitlePane.this.closeButton);
                } else {
                    BasicInternalFrameTitlePane.this.remove(BasicInternalFrameTitlePane.this.closeButton);
                }
            } else if ("maximizable" == propertyName) {
                if (propertyChangeEvent.getNewValue() == Boolean.TRUE) {
                    BasicInternalFrameTitlePane.this.add(BasicInternalFrameTitlePane.this.maxButton);
                } else {
                    BasicInternalFrameTitlePane.this.remove(BasicInternalFrameTitlePane.this.maxButton);
                }
            } else if ("iconable" == propertyName) {
                if (propertyChangeEvent.getNewValue() == Boolean.TRUE) {
                    BasicInternalFrameTitlePane.this.add(BasicInternalFrameTitlePane.this.iconButton);
                } else {
                    BasicInternalFrameTitlePane.this.remove(BasicInternalFrameTitlePane.this.iconButton);
                }
            }
            BasicInternalFrameTitlePane.this.enableActions();
            BasicInternalFrameTitlePane.this.revalidate();
            BasicInternalFrameTitlePane.this.repaint();
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            int i2;
            int i3 = 22;
            if (BasicInternalFrameTitlePane.this.frame.isClosable()) {
                i3 = 22 + 19;
            }
            if (BasicInternalFrameTitlePane.this.frame.isMaximizable()) {
                i3 += 19;
            }
            if (BasicInternalFrameTitlePane.this.frame.isIconifiable()) {
                i3 += 19;
            }
            FontMetrics fontMetrics = BasicInternalFrameTitlePane.this.frame.getFontMetrics(BasicInternalFrameTitlePane.this.getFont());
            String title = BasicInternalFrameTitlePane.this.frame.getTitle();
            int iStringWidth = title != null ? SwingUtilities2.stringWidth(BasicInternalFrameTitlePane.this.frame, fontMetrics, title) : 0;
            if ((title != null ? title.length() : 0) > 3) {
                int iStringWidth2 = SwingUtilities2.stringWidth(BasicInternalFrameTitlePane.this.frame, fontMetrics, title.substring(0, 3) + "...");
                i2 = i3 + (iStringWidth < iStringWidth2 ? iStringWidth : iStringWidth2);
            } else {
                i2 = i3 + iStringWidth;
            }
            Icon frameIcon = BasicInternalFrameTitlePane.this.frame.getFrameIcon();
            int height = fontMetrics.getHeight() + 2;
            int iMin = 0;
            if (frameIcon != null) {
                iMin = Math.min(frameIcon.getIconHeight(), 16);
            }
            Dimension dimension = new Dimension(i2, Math.max(height, iMin + 2));
            if (BasicInternalFrameTitlePane.this.getBorder() != null) {
                Insets borderInsets = BasicInternalFrameTitlePane.this.getBorder().getBorderInsets(container);
                dimension.height += borderInsets.top + borderInsets.bottom;
                dimension.width += borderInsets.left + borderInsets.right;
            }
            return dimension;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            boolean zIsLeftToRight = BasicGraphicsUtils.isLeftToRight(BasicInternalFrameTitlePane.this.frame);
            int width = BasicInternalFrameTitlePane.this.getWidth();
            int height = BasicInternalFrameTitlePane.this.getHeight();
            int iconHeight = BasicInternalFrameTitlePane.this.closeButton.getIcon().getIconHeight();
            Icon frameIcon = BasicInternalFrameTitlePane.this.frame.getFrameIcon();
            int iconHeight2 = 0;
            if (frameIcon != null) {
                iconHeight2 = frameIcon.getIconHeight();
            }
            BasicInternalFrameTitlePane.this.menuBar.setBounds(zIsLeftToRight ? 2 : (width - 16) - 2, (height - iconHeight2) / 2, 16, 16);
            int i2 = zIsLeftToRight ? (width - 16) - 2 : 2;
            if (BasicInternalFrameTitlePane.this.frame.isClosable()) {
                BasicInternalFrameTitlePane.this.closeButton.setBounds(i2, (height - iconHeight) / 2, 16, 14);
                i2 += zIsLeftToRight ? -18 : 18;
            }
            if (BasicInternalFrameTitlePane.this.frame.isMaximizable()) {
                BasicInternalFrameTitlePane.this.maxButton.setBounds(i2, (height - iconHeight) / 2, 16, 14);
                i2 += zIsLeftToRight ? -18 : 18;
            }
            if (BasicInternalFrameTitlePane.this.frame.isIconifiable()) {
                BasicInternalFrameTitlePane.this.iconButton.setBounds(i2, (height - iconHeight) / 2, 16, 14);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$PropertyChangeHandler.class */
    public class PropertyChangeHandler implements PropertyChangeListener {
        public PropertyChangeHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicInternalFrameTitlePane.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$TitlePaneLayout.class */
    public class TitlePaneLayout implements LayoutManager {
        public TitlePaneLayout() {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
            BasicInternalFrameTitlePane.this.getHandler().addLayoutComponent(str, component);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
            BasicInternalFrameTitlePane.this.getHandler().removeLayoutComponent(component);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return BasicInternalFrameTitlePane.this.getHandler().preferredLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return BasicInternalFrameTitlePane.this.getHandler().minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            BasicInternalFrameTitlePane.this.getHandler().layoutContainer(container);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$CloseAction.class */
    public class CloseAction extends AbstractAction {
        public CloseAction() {
            super(UIManager.getString("InternalFrameTitlePane.closeButtonText"));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicInternalFrameTitlePane.this.frame.isClosable()) {
                BasicInternalFrameTitlePane.this.frame.doDefaultCloseAction();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$MaximizeAction.class */
    public class MaximizeAction extends AbstractAction {
        public MaximizeAction() {
            super(UIManager.getString("InternalFrameTitlePane.maximizeButtonText"));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicInternalFrameTitlePane.this.frame.isMaximizable()) {
                if (BasicInternalFrameTitlePane.this.frame.isMaximum() && BasicInternalFrameTitlePane.this.frame.isIcon()) {
                    try {
                        BasicInternalFrameTitlePane.this.frame.setIcon(false);
                    } catch (PropertyVetoException e2) {
                    }
                } else if (!BasicInternalFrameTitlePane.this.frame.isMaximum()) {
                    try {
                        BasicInternalFrameTitlePane.this.frame.setMaximum(true);
                    } catch (PropertyVetoException e3) {
                    }
                } else {
                    try {
                        BasicInternalFrameTitlePane.this.frame.setMaximum(false);
                    } catch (PropertyVetoException e4) {
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$IconifyAction.class */
    public class IconifyAction extends AbstractAction {
        public IconifyAction() {
            super(UIManager.getString("InternalFrameTitlePane.minimizeButtonText"));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicInternalFrameTitlePane.this.frame.isIconifiable()) {
                if (!BasicInternalFrameTitlePane.this.frame.isIcon()) {
                    try {
                        BasicInternalFrameTitlePane.this.frame.setIcon(true);
                    } catch (PropertyVetoException e2) {
                    }
                } else {
                    try {
                        BasicInternalFrameTitlePane.this.frame.setIcon(false);
                    } catch (PropertyVetoException e3) {
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$RestoreAction.class */
    public class RestoreAction extends AbstractAction {
        public RestoreAction() {
            super(UIManager.getString("InternalFrameTitlePane.restoreButtonText"));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicInternalFrameTitlePane.this.frame.isMaximizable() && BasicInternalFrameTitlePane.this.frame.isMaximum() && BasicInternalFrameTitlePane.this.frame.isIcon()) {
                try {
                    BasicInternalFrameTitlePane.this.frame.setIcon(false);
                } catch (PropertyVetoException e2) {
                }
            } else if (BasicInternalFrameTitlePane.this.frame.isMaximizable() && BasicInternalFrameTitlePane.this.frame.isMaximum()) {
                try {
                    BasicInternalFrameTitlePane.this.frame.setMaximum(false);
                } catch (PropertyVetoException e3) {
                }
            } else if (BasicInternalFrameTitlePane.this.frame.isIconifiable() && BasicInternalFrameTitlePane.this.frame.isIcon()) {
                try {
                    BasicInternalFrameTitlePane.this.frame.setIcon(false);
                } catch (PropertyVetoException e4) {
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$MoveAction.class */
    public class MoveAction extends AbstractAction {
        public MoveAction() {
            super(UIManager.getString("InternalFrameTitlePane.moveButtonText"));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$ShowSystemMenuAction.class */
    private class ShowSystemMenuAction extends AbstractAction {
        private boolean show;

        public ShowSystemMenuAction(boolean z2) {
            this.show = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.show) {
                BasicInternalFrameTitlePane.this.windowMenu.doClick();
            } else {
                BasicInternalFrameTitlePane.this.windowMenu.setVisible(false);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$SizeAction.class */
    public class SizeAction extends AbstractAction {
        public SizeAction() {
            super(UIManager.getString("InternalFrameTitlePane.sizeButtonText"));
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$SystemMenuBar.class */
    public class SystemMenuBar extends JMenuBar {
        public SystemMenuBar() {
        }

        @Override // java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public void requestFocus() {
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            Icon frameIcon = BasicInternalFrameTitlePane.this.frame.getFrameIcon();
            if (frameIcon == null) {
                frameIcon = (Icon) DefaultLookup.get(BasicInternalFrameTitlePane.this.frame, BasicInternalFrameTitlePane.this.frame.getUI(), "InternalFrame.icon");
            }
            if (frameIcon != null) {
                if ((frameIcon instanceof ImageIcon) && (frameIcon.getIconWidth() > 16 || frameIcon.getIconHeight() > 16)) {
                    ((ImageIcon) frameIcon).setImage(((ImageIcon) frameIcon).getImage().getScaledInstance(16, 16, 4));
                }
                frameIcon.paintIcon(this, graphics, 0, 0);
            }
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public boolean isOpaque() {
            return true;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicInternalFrameTitlePane$NoFocusButton.class */
    private class NoFocusButton extends JButton {
        private String uiKey;

        public NoFocusButton(String str, String str2) {
            setFocusPainted(false);
            setMargin(new Insets(0, 0, 0, 0));
            this.uiKey = str;
            Object obj = UIManager.get(str2);
            if (obj instanceof Boolean) {
                setOpaque(((Boolean) obj).booleanValue());
            }
        }

        @Override // java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public void requestFocus() {
        }

        @Override // javax.swing.JButton, java.awt.Component
        public AccessibleContext getAccessibleContext() {
            AccessibleContext accessibleContext = super.getAccessibleContext();
            if (this.uiKey != null) {
                accessibleContext.setAccessibleName(UIManager.getString(this.uiKey));
                this.uiKey = null;
            }
            return accessibleContext;
        }
    }
}
