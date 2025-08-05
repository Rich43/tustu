package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalInternalFrameTitlePane.class */
public class MetalInternalFrameTitlePane extends BasicInternalFrameTitlePane {
    protected boolean isPalette;
    protected Icon paletteCloseIcon;
    protected int paletteTitleHeight;
    private static final Border handyEmptyBorder = new EmptyBorder(0, 0, 0, 0);
    private String selectedBackgroundKey;
    private String selectedForegroundKey;
    private String selectedShadowKey;
    private boolean wasClosable;
    int buttonsWidth;
    MetalBumps activeBumps;
    MetalBumps inactiveBumps;
    MetalBumps paletteBumps;
    private Color activeBumpsHighlight;
    private Color activeBumpsShadow;

    public MetalInternalFrameTitlePane(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
        this.isPalette = false;
        this.buttonsWidth = 0;
        this.activeBumps = new MetalBumps(0, 0, MetalLookAndFeel.getPrimaryControlHighlight(), MetalLookAndFeel.getPrimaryControlDarkShadow(), UIManager.get("InternalFrame.activeTitleGradient") != null ? null : MetalLookAndFeel.getPrimaryControl());
        this.inactiveBumps = new MetalBumps(0, 0, MetalLookAndFeel.getControlHighlight(), MetalLookAndFeel.getControlDarkShadow(), UIManager.get("InternalFrame.inactiveTitleGradient") != null ? null : MetalLookAndFeel.getControl());
        this.activeBumpsHighlight = MetalLookAndFeel.getPrimaryControlHighlight();
        this.activeBumpsShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        updateOptionPaneState();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void installDefaults() {
        super.installDefaults();
        setFont(UIManager.getFont("InternalFrame.titleFont"));
        this.paletteTitleHeight = UIManager.getInt("InternalFrame.paletteTitleHeight");
        this.paletteCloseIcon = UIManager.getIcon("InternalFrame.paletteCloseIcon");
        this.wasClosable = this.frame.isClosable();
        this.selectedBackgroundKey = null;
        this.selectedForegroundKey = null;
        if (MetalLookAndFeel.usingOcean()) {
            setOpaque(true);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        if (this.wasClosable != this.frame.isClosable()) {
            this.frame.setClosable(this.wasClosable);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void createButtons() {
        super.createButtons();
        Boolean bool = this.frame.isSelected() ? Boolean.TRUE : Boolean.FALSE;
        this.iconButton.putClientProperty("paintActive", bool);
        this.iconButton.setBorder(handyEmptyBorder);
        this.maxButton.putClientProperty("paintActive", bool);
        this.maxButton.setBorder(handyEmptyBorder);
        this.closeButton.putClientProperty("paintActive", bool);
        this.closeButton.setBorder(handyEmptyBorder);
        this.closeButton.setBackground(MetalLookAndFeel.getPrimaryControlShadow());
        if (MetalLookAndFeel.usingOcean()) {
            this.iconButton.setContentAreaFilled(false);
            this.maxButton.setContentAreaFilled(false);
            this.closeButton.setContentAreaFilled(false);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void assembleSystemMenu() {
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void addSystemMenuItems(JMenu jMenu) {
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void showSystemMenu() {
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void addSubComponents() {
        add(this.iconButton);
        add(this.maxButton);
        add(this.closeButton);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected PropertyChangeListener createPropertyChangeListener() {
        return new MetalPropertyChangeHandler();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected LayoutManager createLayout() {
        return new MetalTitlePaneLayout();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalInternalFrameTitlePane$MetalPropertyChangeHandler.class */
    class MetalPropertyChangeHandler extends BasicInternalFrameTitlePane.PropertyChangeHandler {
        MetalPropertyChangeHandler() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName.equals(JInternalFrame.IS_SELECTED_PROPERTY)) {
                Boolean bool = (Boolean) propertyChangeEvent.getNewValue();
                MetalInternalFrameTitlePane.this.iconButton.putClientProperty("paintActive", bool);
                MetalInternalFrameTitlePane.this.closeButton.putClientProperty("paintActive", bool);
                MetalInternalFrameTitlePane.this.maxButton.putClientProperty("paintActive", bool);
            } else if ("JInternalFrame.messageType".equals(propertyName)) {
                MetalInternalFrameTitlePane.this.updateOptionPaneState();
                MetalInternalFrameTitlePane.this.frame.repaint();
            }
            super.propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalInternalFrameTitlePane$MetalTitlePaneLayout.class */
    class MetalTitlePaneLayout extends BasicInternalFrameTitlePane.TitlePaneLayout {
        MetalTitlePaneLayout() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout, java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout, java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout, java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return minimumLayoutSize(container);
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout, java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            int i2;
            int iMax;
            int i3;
            int i4 = 30;
            if (MetalInternalFrameTitlePane.this.frame.isClosable()) {
                i4 = 30 + 21;
            }
            if (MetalInternalFrameTitlePane.this.frame.isMaximizable()) {
                i4 += 16 + (MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4);
            }
            if (MetalInternalFrameTitlePane.this.frame.isIconifiable()) {
                int i5 = i4;
                if (MetalInternalFrameTitlePane.this.frame.isMaximizable()) {
                    i3 = 2;
                } else {
                    i3 = MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4;
                }
                i4 = i5 + 16 + i3;
            }
            FontMetrics fontMetrics = MetalInternalFrameTitlePane.this.frame.getFontMetrics(MetalInternalFrameTitlePane.this.getFont());
            String title = MetalInternalFrameTitlePane.this.frame.getTitle();
            int iStringWidth = title != null ? SwingUtilities2.stringWidth(MetalInternalFrameTitlePane.this.frame, fontMetrics, title) : 0;
            if ((title != null ? title.length() : 0) > 2) {
                int iStringWidth2 = SwingUtilities2.stringWidth(MetalInternalFrameTitlePane.this.frame, fontMetrics, MetalInternalFrameTitlePane.this.frame.getTitle().substring(0, 2) + "...");
                i2 = i4 + (iStringWidth < iStringWidth2 ? iStringWidth : iStringWidth2);
            } else {
                i2 = i4 + iStringWidth;
            }
            if (MetalInternalFrameTitlePane.this.isPalette) {
                iMax = MetalInternalFrameTitlePane.this.paletteTitleHeight;
            } else {
                int height = fontMetrics.getHeight() + 7;
                Icon frameIcon = MetalInternalFrameTitlePane.this.frame.getFrameIcon();
                int iMin = 0;
                if (frameIcon != null) {
                    iMin = Math.min(frameIcon.getIconHeight(), 16);
                }
                iMax = Math.max(height, iMin + 5);
            }
            return new Dimension(i2, iMax);
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout, java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            boolean zIsLeftToRight = MetalUtils.isLeftToRight(MetalInternalFrameTitlePane.this.frame);
            int width = MetalInternalFrameTitlePane.this.getWidth();
            int i3 = zIsLeftToRight ? width : 0;
            int iconHeight = MetalInternalFrameTitlePane.this.closeButton.getIcon().getIconHeight();
            int iconWidth = MetalInternalFrameTitlePane.this.closeButton.getIcon().getIconWidth();
            if (MetalInternalFrameTitlePane.this.frame.isClosable()) {
                if (MetalInternalFrameTitlePane.this.isPalette) {
                    i3 += zIsLeftToRight ? (-3) - (iconWidth + 2) : 3;
                    MetalInternalFrameTitlePane.this.closeButton.setBounds(i3, 2, iconWidth + 2, MetalInternalFrameTitlePane.this.getHeight() - 4);
                    if (!zIsLeftToRight) {
                        i3 += iconWidth + 2;
                    }
                } else {
                    i3 += zIsLeftToRight ? (-4) - iconWidth : 4;
                    MetalInternalFrameTitlePane.this.closeButton.setBounds(i3, 2, iconWidth, iconHeight);
                    if (!zIsLeftToRight) {
                        i3 += iconWidth;
                    }
                }
            }
            if (MetalInternalFrameTitlePane.this.frame.isMaximizable() && !MetalInternalFrameTitlePane.this.isPalette) {
                int i4 = MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4;
                i3 += zIsLeftToRight ? (-i4) - iconWidth : i4;
                MetalInternalFrameTitlePane.this.maxButton.setBounds(i3, 2, iconWidth, iconHeight);
                if (!zIsLeftToRight) {
                    i3 += iconWidth;
                }
            }
            if (MetalInternalFrameTitlePane.this.frame.isIconifiable() && !MetalInternalFrameTitlePane.this.isPalette) {
                if (MetalInternalFrameTitlePane.this.frame.isMaximizable()) {
                    i2 = 2;
                } else {
                    i2 = MetalInternalFrameTitlePane.this.frame.isClosable() ? 10 : 4;
                }
                int i5 = i2;
                i3 += zIsLeftToRight ? (-i5) - iconWidth : i5;
                MetalInternalFrameTitlePane.this.iconButton.setBounds(i3, 2, iconWidth, iconHeight);
                if (!zIsLeftToRight) {
                    i3 += iconWidth;
                }
            }
            MetalInternalFrameTitlePane.this.buttonsWidth = zIsLeftToRight ? width - i3 : i3;
        }
    }

    public void paintPalette(Graphics graphics) {
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.frame);
        int width = getWidth();
        int height = getHeight();
        if (this.paletteBumps == null) {
            this.paletteBumps = new MetalBumps(0, 0, MetalLookAndFeel.getPrimaryControlHighlight(), MetalLookAndFeel.getPrimaryControlInfo(), MetalLookAndFeel.getPrimaryControlShadow());
        }
        ColorUIResource primaryControlShadow = MetalLookAndFeel.getPrimaryControlShadow();
        ColorUIResource primaryControlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
        graphics.setColor(primaryControlShadow);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(primaryControlDarkShadow);
        graphics.drawLine(0, height - 1, width, height - 1);
        int i2 = zIsLeftToRight ? 4 : this.buttonsWidth + 4;
        this.paletteBumps.setBumpArea((width - this.buttonsWidth) - 8, getHeight() - 4);
        this.paletteBumps.paintIcon(this, graphics, i2, 2);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane, javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        MetalBumps metalBumps;
        String str;
        int i2;
        int i3;
        String title;
        if (this.isPalette) {
            paintPalette(graphics);
            return;
        }
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.frame);
        boolean zIsSelected = this.frame.isSelected();
        int width = getWidth();
        int height = getHeight();
        Color windowTitleInactiveBackground = null;
        Color windowTitleInactiveForeground = null;
        Color controlDarkShadow = null;
        if (zIsSelected) {
            if (!MetalLookAndFeel.usingOcean()) {
                this.closeButton.setContentAreaFilled(true);
                this.maxButton.setContentAreaFilled(true);
                this.iconButton.setContentAreaFilled(true);
            }
            if (this.selectedBackgroundKey != null) {
                windowTitleInactiveBackground = UIManager.getColor(this.selectedBackgroundKey);
            }
            if (windowTitleInactiveBackground == null) {
                windowTitleInactiveBackground = MetalLookAndFeel.getWindowTitleBackground();
            }
            if (this.selectedForegroundKey != null) {
                windowTitleInactiveForeground = UIManager.getColor(this.selectedForegroundKey);
            }
            if (this.selectedShadowKey != null) {
                controlDarkShadow = UIManager.getColor(this.selectedShadowKey);
            }
            if (controlDarkShadow == null) {
                controlDarkShadow = MetalLookAndFeel.getPrimaryControlDarkShadow();
            }
            if (windowTitleInactiveForeground == null) {
                windowTitleInactiveForeground = MetalLookAndFeel.getWindowTitleForeground();
            }
            this.activeBumps.setBumpColors(this.activeBumpsHighlight, this.activeBumpsShadow, UIManager.get("InternalFrame.activeTitleGradient") != null ? null : windowTitleInactiveBackground);
            metalBumps = this.activeBumps;
            str = "InternalFrame.activeTitleGradient";
        } else {
            if (!MetalLookAndFeel.usingOcean()) {
                this.closeButton.setContentAreaFilled(false);
                this.maxButton.setContentAreaFilled(false);
                this.iconButton.setContentAreaFilled(false);
            }
            windowTitleInactiveBackground = MetalLookAndFeel.getWindowTitleInactiveBackground();
            windowTitleInactiveForeground = MetalLookAndFeel.getWindowTitleInactiveForeground();
            controlDarkShadow = MetalLookAndFeel.getControlDarkShadow();
            metalBumps = this.inactiveBumps;
            str = "InternalFrame.inactiveTitleGradient";
        }
        if (!MetalUtils.drawGradient(this, graphics, str, 0, 0, width, height, true)) {
            graphics.setColor(windowTitleInactiveBackground);
            graphics.fillRect(0, 0, width, height);
        }
        graphics.setColor(controlDarkShadow);
        graphics.drawLine(0, height - 1, width, height - 1);
        graphics.drawLine(0, 0, 0, 0);
        graphics.drawLine(width - 1, 0, width - 1, 0);
        int iStringWidth = zIsLeftToRight ? 5 : width - 5;
        String title2 = this.frame.getTitle();
        Icon frameIcon = this.frame.getFrameIcon();
        if (frameIcon != null) {
            if (!zIsLeftToRight) {
                iStringWidth -= frameIcon.getIconWidth();
            }
            frameIcon.paintIcon(this.frame, graphics, iStringWidth, (height / 2) - (frameIcon.getIconHeight() / 2));
            iStringWidth += zIsLeftToRight ? frameIcon.getIconWidth() + 5 : -5;
        }
        if (title2 != null) {
            Font font = getFont();
            graphics.setFont(font);
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.frame, graphics, font);
            fontMetrics.getHeight();
            graphics.setColor(windowTitleInactiveForeground);
            int height2 = ((height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent();
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            if (this.frame.isIconifiable()) {
                rectangle = this.iconButton.getBounds();
            } else if (this.frame.isMaximizable()) {
                rectangle = this.maxButton.getBounds();
            } else if (this.frame.isClosable()) {
                rectangle = this.closeButton.getBounds();
            }
            if (zIsLeftToRight) {
                if (rectangle.f12372x == 0) {
                    rectangle.f12372x = (this.frame.getWidth() - this.frame.getInsets().right) - 2;
                }
                title = getTitle(title2, fontMetrics, (rectangle.f12372x - iStringWidth) - 4);
            } else {
                title = getTitle(title2, fontMetrics, ((iStringWidth - rectangle.f12372x) - rectangle.width) - 4);
                iStringWidth -= SwingUtilities2.stringWidth(this.frame, fontMetrics, title);
            }
            int iStringWidth2 = SwingUtilities2.stringWidth(this.frame, fontMetrics, title);
            SwingUtilities2.drawString(this.frame, graphics, title, iStringWidth, height2);
            iStringWidth += zIsLeftToRight ? iStringWidth2 + 5 : -5;
        }
        if (zIsLeftToRight) {
            i2 = ((width - this.buttonsWidth) - iStringWidth) - 5;
            i3 = iStringWidth;
        } else {
            i2 = (iStringWidth - this.buttonsWidth) - 5;
            i3 = this.buttonsWidth + 5;
        }
        metalBumps.setBumpArea(i2, getHeight() - (2 * 3));
        metalBumps.paintIcon(this, graphics, i3, 3);
    }

    public void setPalette(boolean z2) {
        this.isPalette = z2;
        if (this.isPalette) {
            this.closeButton.setIcon(this.paletteCloseIcon);
            if (this.frame.isMaximizable()) {
                remove(this.maxButton);
            }
            if (this.frame.isIconifiable()) {
                remove(this.iconButton);
            }
        } else {
            this.closeButton.setIcon(this.closeIcon);
            if (this.frame.isMaximizable()) {
                add(this.maxButton);
            }
            if (this.frame.isIconifiable()) {
                add(this.iconButton);
            }
        }
        revalidate();
        repaint();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateOptionPaneState() {
        int iIntValue = -2;
        boolean z2 = this.wasClosable;
        Object clientProperty = this.frame.getClientProperty("JInternalFrame.messageType");
        if (clientProperty == null) {
            return;
        }
        if (clientProperty instanceof Integer) {
            iIntValue = ((Integer) clientProperty).intValue();
        }
        switch (iIntValue) {
            case -1:
            case 1:
                this.selectedShadowKey = null;
                this.selectedForegroundKey = null;
                this.selectedBackgroundKey = null;
                z2 = false;
                break;
            case 0:
                this.selectedBackgroundKey = "OptionPane.errorDialog.titlePane.background";
                this.selectedForegroundKey = "OptionPane.errorDialog.titlePane.foreground";
                this.selectedShadowKey = "OptionPane.errorDialog.titlePane.shadow";
                z2 = false;
                break;
            case 2:
                this.selectedBackgroundKey = "OptionPane.warningDialog.titlePane.background";
                this.selectedForegroundKey = "OptionPane.warningDialog.titlePane.foreground";
                this.selectedShadowKey = "OptionPane.warningDialog.titlePane.shadow";
                z2 = false;
                break;
            case 3:
                this.selectedBackgroundKey = "OptionPane.questionDialog.titlePane.background";
                this.selectedForegroundKey = "OptionPane.questionDialog.titlePane.foreground";
                this.selectedShadowKey = "OptionPane.questionDialog.titlePane.shadow";
                z2 = false;
                break;
            default:
                this.selectedShadowKey = null;
                this.selectedForegroundKey = null;
                this.selectedBackgroundKey = null;
                break;
        }
        if (z2 != this.frame.isClosable()) {
            this.frame.setClosable(z2);
        }
    }
}
