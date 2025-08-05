package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthInternalFrameTitlePane.class */
class SynthInternalFrameTitlePane extends BasicInternalFrameTitlePane implements SynthUI, PropertyChangeListener {
    protected JPopupMenu systemPopupMenu;
    protected JButton menuButton;
    private SynthStyle style;
    private int titleSpacing;
    private int buttonSpacing;
    private int titleAlignment;

    public SynthInternalFrameTitlePane(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return "InternalFrameTitlePaneUI";
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    public SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private Region getRegion(JComponent jComponent) {
        return SynthLookAndFeel.getRegion(jComponent);
    }

    private int getComponentState(JComponent jComponent) {
        if (this.frame != null && this.frame.isSelected()) {
            return 512;
        }
        return SynthLookAndFeel.getComponentState(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void addSubComponents() {
        this.menuButton.setName("InternalFrameTitlePane.menuButton");
        this.iconButton.setName("InternalFrameTitlePane.iconifyButton");
        this.maxButton.setName("InternalFrameTitlePane.maximizeButton");
        this.closeButton.setName("InternalFrameTitlePane.closeButton");
        add(this.menuButton);
        add(this.iconButton);
        add(this.maxButton);
        add(this.closeButton);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void installListeners() {
        super.installListeners();
        this.frame.addPropertyChangeListener(this);
        addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void uninstallListeners() {
        this.frame.removePropertyChangeListener(this);
        removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    private void updateStyle(JComponent jComponent) {
        SynthContext context = getContext(this, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            this.maxIcon = this.style.getIcon(context, "InternalFrameTitlePane.maximizeIcon");
            this.minIcon = this.style.getIcon(context, "InternalFrameTitlePane.minimizeIcon");
            this.iconIcon = this.style.getIcon(context, "InternalFrameTitlePane.iconifyIcon");
            this.closeIcon = this.style.getIcon(context, "InternalFrameTitlePane.closeIcon");
            this.titleSpacing = this.style.getInt(context, "InternalFrameTitlePane.titleSpacing", 2);
            this.buttonSpacing = this.style.getInt(context, "InternalFrameTitlePane.buttonSpacing", 2);
            String str = (String) this.style.get(context, "InternalFrameTitlePane.titleAlignment");
            this.titleAlignment = 10;
            if (str != null) {
                String upperCase = str.toUpperCase();
                if (upperCase.equals("TRAILING")) {
                    this.titleAlignment = 11;
                } else if (upperCase.equals("CENTER")) {
                    this.titleAlignment = 0;
                }
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void installDefaults() {
        super.installDefaults();
        updateStyle(this);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void uninstallDefaults() {
        SynthContext context = getContext(this, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
        JInternalFrame.JDesktopIcon desktopIcon = this.frame.getDesktopIcon();
        if (desktopIcon != null && desktopIcon.getComponentPopupMenu() == this.systemPopupMenu) {
            desktopIcon.setComponentPopupMenu(null);
        }
        super.uninstallDefaults();
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthInternalFrameTitlePane$JPopupMenuUIResource.class */
    private static class JPopupMenuUIResource extends JPopupMenu implements UIResource {
        private JPopupMenuUIResource() {
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void assembleSystemMenu() {
        JPopupMenu componentPopupMenu;
        this.systemPopupMenu = new JPopupMenuUIResource();
        addSystemMenuItems(this.systemPopupMenu);
        enableActions();
        this.menuButton = createNoFocusButton();
        updateMenuIcon();
        this.menuButton.addMouseListener(new MouseAdapter() { // from class: javax.swing.plaf.synth.SynthInternalFrameTitlePane.1
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mousePressed(MouseEvent mouseEvent) {
                try {
                    SynthInternalFrameTitlePane.this.frame.setSelected(true);
                } catch (PropertyVetoException e2) {
                }
                SynthInternalFrameTitlePane.this.showSystemMenu();
            }
        });
        JPopupMenu componentPopupMenu2 = this.frame.getComponentPopupMenu();
        if (componentPopupMenu2 == null || (componentPopupMenu2 instanceof UIResource)) {
            this.frame.setComponentPopupMenu(this.systemPopupMenu);
        }
        if (this.frame.getDesktopIcon() != null && ((componentPopupMenu = this.frame.getDesktopIcon().getComponentPopupMenu()) == null || (componentPopupMenu instanceof UIResource))) {
            this.frame.getDesktopIcon().setComponentPopupMenu(this.systemPopupMenu);
        }
        setInheritsPopupMenu(true);
    }

    protected void addSystemMenuItems(JPopupMenu jPopupMenu) {
        jPopupMenu.add(this.restoreAction).setMnemonic(getButtonMnemonic("restore"));
        jPopupMenu.add(this.moveAction).setMnemonic(getButtonMnemonic("move"));
        jPopupMenu.add(this.sizeAction).setMnemonic(getButtonMnemonic("size"));
        jPopupMenu.add(this.iconifyAction).setMnemonic(getButtonMnemonic("minimize"));
        jPopupMenu.add(this.maximizeAction).setMnemonic(getButtonMnemonic("maximize"));
        jPopupMenu.add(new JSeparator());
        jPopupMenu.add(this.closeAction).setMnemonic(getButtonMnemonic("close"));
    }

    private static int getButtonMnemonic(String str) {
        try {
            return Integer.parseInt(UIManager.getString("InternalFrameTitlePane." + str + "Button.mnemonic"));
        } catch (NumberFormatException e2) {
            return -1;
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void showSystemMenu() {
        Insets insets = this.frame.getInsets();
        if (!this.frame.isIcon()) {
            this.systemPopupMenu.show(this.frame, this.menuButton.getX(), getY() + getHeight());
        } else {
            this.systemPopupMenu.show(this.menuButton, (getX() - insets.left) - insets.right, ((getY() - this.systemPopupMenu.getPreferredSize().height) - insets.bottom) - insets.top);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane, javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        SynthContext context = getContext(this);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintInternalFrameTitlePaneBackground(context, graphics, 0, 0, getWidth(), getHeight());
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        int x2;
        int x3;
        String title = this.frame.getTitle();
        if (title != null) {
            SynthStyle style = synthContext.getStyle();
            graphics.setColor(style.getColor(synthContext, ColorType.TEXT_FOREGROUND));
            graphics.setFont(style.getFont(synthContext));
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.frame, graphics);
            int height = (((getHeight() + fontMetrics.getAscent()) - fontMetrics.getLeading()) - fontMetrics.getDescent()) / 2;
            JButton jButton = null;
            if (this.frame.isIconifiable()) {
                jButton = this.iconButton;
            } else if (this.frame.isMaximizable()) {
                jButton = this.maxButton;
            } else if (this.frame.isClosable()) {
                jButton = this.closeButton;
            }
            boolean zIsLeftToRight = SynthLookAndFeel.isLeftToRight(this.frame);
            int i2 = this.titleAlignment;
            if (zIsLeftToRight) {
                if (jButton != null) {
                    x3 = jButton.getX() - this.titleSpacing;
                } else {
                    x3 = (this.frame.getWidth() - this.frame.getInsets().right) - this.titleSpacing;
                }
                x2 = this.menuButton.getX() + this.menuButton.getWidth() + this.titleSpacing;
            } else {
                if (jButton != null) {
                    x2 = jButton.getX() + jButton.getWidth() + this.titleSpacing;
                } else {
                    x2 = this.frame.getInsets().left + this.titleSpacing;
                }
                x3 = this.menuButton.getX() - this.titleSpacing;
                if (i2 == 10) {
                    i2 = 11;
                } else if (i2 == 11) {
                    i2 = 10;
                }
            }
            String title2 = getTitle(title, fontMetrics, x3 - x2);
            if (title2 == title) {
                if (i2 == 11) {
                    x2 = x3 - style.getGraphicsUtils(synthContext).computeStringWidth(synthContext, graphics.getFont(), fontMetrics, title);
                } else if (i2 == 0) {
                    int iComputeStringWidth = style.getGraphicsUtils(synthContext).computeStringWidth(synthContext, graphics.getFont(), fontMetrics, title);
                    x2 = Math.min(x3 - iComputeStringWidth, Math.max(x2, (getWidth() - iComputeStringWidth) / 2));
                }
            }
            style.getGraphicsUtils(synthContext).paintText(synthContext, graphics, title2, x2, height - fontMetrics.getAscent(), -1);
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintInternalFrameTitlePaneBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected LayoutManager createLayout() {
        SynthContext context = getContext(this);
        LayoutManager layoutManager = (LayoutManager) this.style.get(context, "InternalFrameTitlePane.titlePaneLayout");
        context.dispose();
        return layoutManager != null ? layoutManager : new SynthTitlePaneLayout();
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getSource() == this) {
            if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
                updateStyle(this);
            }
        } else if (propertyChangeEvent.getPropertyName() == JInternalFrame.FRAME_ICON_PROPERTY) {
            updateMenuIcon();
        }
    }

    private void updateMenuIcon() {
        Icon frameIcon = this.frame.getFrameIcon();
        SynthContext context = getContext(this);
        if (frameIcon != null) {
            Dimension dimension = (Dimension) context.getStyle().get(context, "InternalFrameTitlePane.maxFrameIconSize");
            int i2 = 16;
            int i3 = 16;
            if (dimension != null) {
                i2 = dimension.width;
                i3 = dimension.height;
            }
            if ((frameIcon.getIconWidth() > i2 || frameIcon.getIconHeight() > i3) && (frameIcon instanceof ImageIcon)) {
                frameIcon = new ImageIcon(((ImageIcon) frameIcon).getImage().getScaledInstance(i2, i3, 4));
            }
        }
        context.dispose();
        this.menuButton.setIcon(frameIcon);
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthInternalFrameTitlePane$SynthTitlePaneLayout.class */
    class SynthTitlePaneLayout implements LayoutManager {
        SynthTitlePaneLayout() {
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
            SynthContext context = SynthInternalFrameTitlePane.this.getContext(SynthInternalFrameTitlePane.this);
            int i3 = 0;
            int iMax = 0;
            int i4 = 0;
            if (SynthInternalFrameTitlePane.this.frame.isClosable()) {
                Dimension preferredSize = SynthInternalFrameTitlePane.this.closeButton.getPreferredSize();
                i3 = 0 + preferredSize.width;
                iMax = Math.max(preferredSize.height, 0);
                i4 = 0 + 1;
            }
            if (SynthInternalFrameTitlePane.this.frame.isMaximizable()) {
                Dimension preferredSize2 = SynthInternalFrameTitlePane.this.maxButton.getPreferredSize();
                i3 += preferredSize2.width;
                iMax = Math.max(preferredSize2.height, iMax);
                i4++;
            }
            if (SynthInternalFrameTitlePane.this.frame.isIconifiable()) {
                Dimension preferredSize3 = SynthInternalFrameTitlePane.this.iconButton.getPreferredSize();
                i3 += preferredSize3.width;
                iMax = Math.max(preferredSize3.height, iMax);
                i4++;
            }
            Dimension preferredSize4 = SynthInternalFrameTitlePane.this.menuButton.getPreferredSize();
            int i5 = i3 + preferredSize4.width;
            int iMax2 = Math.max(preferredSize4.height, iMax);
            int iMax3 = i5 + Math.max(0, (i4 - 1) * SynthInternalFrameTitlePane.this.buttonSpacing);
            FontMetrics fontMetrics = SynthInternalFrameTitlePane.this.getFontMetrics(SynthInternalFrameTitlePane.this.getFont());
            SynthGraphicsUtils graphicsUtils = context.getStyle().getGraphicsUtils(context);
            String title = SynthInternalFrameTitlePane.this.frame.getTitle();
            int iComputeStringWidth = title != null ? graphicsUtils.computeStringWidth(context, fontMetrics.getFont(), fontMetrics, title) : 0;
            if ((title != null ? title.length() : 0) > 3) {
                int iComputeStringWidth2 = graphicsUtils.computeStringWidth(context, fontMetrics.getFont(), fontMetrics, title.substring(0, 3) + "...");
                i2 = iMax3 + (iComputeStringWidth < iComputeStringWidth2 ? iComputeStringWidth : iComputeStringWidth2);
            } else {
                i2 = iMax3 + iComputeStringWidth;
            }
            int iMax4 = Math.max(fontMetrics.getHeight() + 2, iMax2);
            int i6 = i2 + SynthInternalFrameTitlePane.this.titleSpacing + SynthInternalFrameTitlePane.this.titleSpacing;
            Insets insets = SynthInternalFrameTitlePane.this.getInsets();
            int i7 = iMax4 + insets.top + insets.bottom;
            int i8 = i6 + insets.left + insets.right;
            context.dispose();
            return new Dimension(i8, i7);
        }

        private int center(Component component, Insets insets, int i2, boolean z2) {
            Dimension preferredSize = component.getPreferredSize();
            if (z2) {
                i2 -= preferredSize.width;
            }
            component.setBounds(i2, insets.top + ((((SynthInternalFrameTitlePane.this.getHeight() - insets.top) - insets.bottom) - preferredSize.height) / 2), preferredSize.width, preferredSize.height);
            if (preferredSize.width > 0) {
                return z2 ? i2 - SynthInternalFrameTitlePane.this.buttonSpacing : i2 + preferredSize.width + SynthInternalFrameTitlePane.this.buttonSpacing;
            }
            return i2;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            Insets insets = container.getInsets();
            if (SynthLookAndFeel.isLeftToRight(SynthInternalFrameTitlePane.this.frame)) {
                center(SynthInternalFrameTitlePane.this.menuButton, insets, insets.left, false);
                int width = SynthInternalFrameTitlePane.this.getWidth() - insets.right;
                if (SynthInternalFrameTitlePane.this.frame.isClosable()) {
                    width = center(SynthInternalFrameTitlePane.this.closeButton, insets, width, true);
                }
                if (SynthInternalFrameTitlePane.this.frame.isMaximizable()) {
                    width = center(SynthInternalFrameTitlePane.this.maxButton, insets, width, true);
                }
                if (SynthInternalFrameTitlePane.this.frame.isIconifiable()) {
                    center(SynthInternalFrameTitlePane.this.iconButton, insets, width, true);
                    return;
                }
                return;
            }
            center(SynthInternalFrameTitlePane.this.menuButton, insets, SynthInternalFrameTitlePane.this.getWidth() - insets.right, true);
            int iCenter = insets.left;
            if (SynthInternalFrameTitlePane.this.frame.isClosable()) {
                iCenter = center(SynthInternalFrameTitlePane.this.closeButton, insets, iCenter, false);
            }
            if (SynthInternalFrameTitlePane.this.frame.isMaximizable()) {
                iCenter = center(SynthInternalFrameTitlePane.this.maxButton, insets, iCenter, false);
            }
            if (SynthInternalFrameTitlePane.this.frame.isIconifiable()) {
                center(SynthInternalFrameTitlePane.this.iconButton, insets, iCenter, false);
            }
        }
    }

    private JButton createNoFocusButton() {
        JButton jButton = new JButton();
        jButton.setFocusable(false);
        jButton.setMargin(new Insets(0, 0, 0, 0));
        return jButton;
    }
}
