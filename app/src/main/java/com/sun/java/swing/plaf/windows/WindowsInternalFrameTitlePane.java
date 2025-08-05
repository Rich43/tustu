package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsInternalFrameTitlePane.class */
public class WindowsInternalFrameTitlePane extends BasicInternalFrameTitlePane {
    private Color selectedTitleGradientColor;
    private Color notSelectedTitleGradientColor;
    private JPopupMenu systemPopupMenu;
    private JLabel systemLabel;
    private Font titleFont;
    private int titlePaneHeight;
    private int buttonWidth;
    private int buttonHeight;
    private boolean hotTrackingOn;

    public WindowsInternalFrameTitlePane(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void addSubComponents() {
        add(this.systemLabel);
        add(this.iconButton);
        add(this.maxButton);
        add(this.closeButton);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void installDefaults() {
        super.installDefaults();
        this.titlePaneHeight = UIManager.getInt("InternalFrame.titlePaneHeight");
        this.buttonWidth = UIManager.getInt("InternalFrame.titleButtonWidth") - 4;
        this.buttonHeight = UIManager.getInt("InternalFrame.titleButtonHeight") - 4;
        Object obj = UIManager.get("InternalFrame.titleButtonToolTipsOn");
        this.hotTrackingOn = obj instanceof Boolean ? ((Boolean) obj).booleanValue() : true;
        if (XPStyle.getXP() != null) {
            this.buttonWidth = this.buttonHeight;
            Dimension partSize = XPStyle.getPartSize(TMSchema.Part.WP_CLOSEBUTTON, TMSchema.State.NORMAL);
            if (partSize != null && partSize.width != 0 && partSize.height != 0) {
                this.buttonWidth = (int) ((this.buttonWidth * partSize.width) / partSize.height);
            }
        } else {
            this.buttonWidth += 2;
            setBorder(BorderFactory.createLineBorder(UIManager.getColor("InternalFrame.activeBorderColor"), 1));
        }
        this.selectedTitleGradientColor = UIManager.getColor("InternalFrame.activeTitleGradient");
        this.notSelectedTitleGradientColor = UIManager.getColor("InternalFrame.inactiveTitleGradient");
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void uninstallListeners() {
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void createButtons() {
        super.createButtons();
        if (XPStyle.getXP() != null) {
            this.iconButton.setContentAreaFilled(false);
            this.maxButton.setContentAreaFilled(false);
            this.closeButton.setContentAreaFilled(false);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void setButtonIcons() {
        super.setButtonIcons();
        if (!this.hotTrackingOn) {
            this.iconButton.setToolTipText(null);
            this.maxButton.setToolTipText(null);
            this.closeButton.setToolTipText(null);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane, javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        int iStringWidth;
        int x2;
        XPStyle xp = XPStyle.getXP();
        paintTitleBackground(graphics);
        String title = this.frame.getTitle();
        if (title != null) {
            boolean zIsSelected = this.frame.isSelected();
            Font font = graphics.getFont();
            Font font2 = this.titleFont != null ? this.titleFont : getFont();
            graphics.setFont(font2);
            FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(this.frame, graphics, font2);
            int height = (((getHeight() + fontMetrics.getAscent()) - fontMetrics.getLeading()) - fontMetrics.getDescent()) / 2;
            Rectangle rectangle = new Rectangle(0, 0, 0, 0);
            if (this.frame.isIconifiable()) {
                rectangle = this.iconButton.getBounds();
            } else if (this.frame.isMaximizable()) {
                rectangle = this.maxButton.getBounds();
            } else if (this.frame.isClosable()) {
                rectangle = this.closeButton.getBounds();
            }
            if (WindowsGraphicsUtils.isLeftToRight(this.frame)) {
                if (rectangle.f12372x == 0) {
                    rectangle.f12372x = this.frame.getWidth() - this.frame.getInsets().right;
                }
                x2 = this.systemLabel.getX() + this.systemLabel.getWidth() + 2;
                if (xp != null) {
                    x2 += 2;
                }
                iStringWidth = (rectangle.f12372x - x2) - 2;
            } else {
                if (rectangle.f12372x == 0) {
                    rectangle.f12372x = this.frame.getInsets().left;
                }
                iStringWidth = SwingUtilities2.stringWidth(this.frame, fontMetrics, title);
                int i2 = rectangle.f12372x + rectangle.width + 2;
                if (xp != null) {
                    i2 += 2;
                }
                int x3 = (this.systemLabel.getX() - 2) - i2;
                if (x3 > iStringWidth) {
                    x2 = (this.systemLabel.getX() - 2) - iStringWidth;
                } else {
                    x2 = i2;
                    iStringWidth = x3;
                }
            }
            String title2 = getTitle(this.frame.getTitle(), fontMetrics, iStringWidth);
            if (xp != null) {
                String string = null;
                if (zIsSelected) {
                    string = xp.getString(this, TMSchema.Part.WP_CAPTION, TMSchema.State.ACTIVE, TMSchema.Prop.TEXTSHADOWTYPE);
                }
                if (Constants.ATTRVAL_SINGLE.equalsIgnoreCase(string)) {
                    Point point = xp.getPoint(this, TMSchema.Part.WP_WINDOW, TMSchema.State.ACTIVE, TMSchema.Prop.TEXTSHADOWOFFSET);
                    Color color = xp.getColor(this, TMSchema.Part.WP_WINDOW, TMSchema.State.ACTIVE, TMSchema.Prop.TEXTSHADOWCOLOR, null);
                    if (point != null && color != null) {
                        graphics.setColor(color);
                        SwingUtilities2.drawString(this.frame, graphics, title2, x2 + point.f12370x, height + point.f12371y);
                    }
                }
            }
            graphics.setColor(zIsSelected ? this.selectedTextColor : this.notSelectedTextColor);
            SwingUtilities2.drawString(this.frame, graphics, title2, x2, height);
            graphics.setFont(font);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension dimension = new Dimension(super.getMinimumSize());
        dimension.height = this.titlePaneHeight + 2;
        if (XPStyle.getXP() != null) {
            if (this.frame.isMaximum()) {
                dimension.height--;
            } else {
                dimension.height += 3;
            }
        }
        return dimension;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void paintTitleBackground(Graphics graphics) {
        TMSchema.Part part;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            if (this.frame.isIcon()) {
                part = TMSchema.Part.WP_MINCAPTION;
            } else {
                part = this.frame.isMaximum() ? TMSchema.Part.WP_MAXCAPTION : TMSchema.Part.WP_CAPTION;
            }
            xp.getSkin(this, part).paintSkin(graphics, 0, 0, getWidth(), getHeight(), this.frame.isSelected() ? TMSchema.State.ACTIVE : TMSchema.State.INACTIVE);
            return;
        }
        if (((Boolean) LookAndFeel.getDesktopPropertyValue("win.frame.captionGradientsOn", false)).booleanValue() && (graphics instanceof Graphics2D)) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            Paint paint = graphics2D.getPaint();
            boolean zIsSelected = this.frame.isSelected();
            int width = getWidth();
            if (zIsSelected) {
                graphics2D.setPaint(new GradientPaint(0.0f, 0.0f, this.selectedTitleColor, (int) (width * 0.75d), 0.0f, this.selectedTitleGradientColor));
            } else {
                graphics2D.setPaint(new GradientPaint(0.0f, 0.0f, this.notSelectedTitleColor, (int) (width * 0.75d), 0.0f, this.notSelectedTitleGradientColor));
            }
            graphics2D.fillRect(0, 0, getWidth(), getHeight());
            graphics2D.setPaint(paint);
            return;
        }
        super.paintTitleBackground(graphics);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void assembleSystemMenu() {
        this.systemPopupMenu = new JPopupMenu();
        addSystemMenuItems(this.systemPopupMenu);
        enableActions();
        this.systemLabel = new JLabel(this.frame.getFrameIcon()) { // from class: com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane.1
            @Override // javax.swing.JComponent
            protected void paintComponent(Graphics graphics) {
                int iconWidth;
                int iconHeight;
                double d2;
                int i2 = 0;
                int i3 = 0;
                int width = getWidth();
                int height = getHeight();
                Graphics graphicsCreate = graphics.create();
                if (isOpaque()) {
                    graphicsCreate.setColor(getBackground());
                    graphicsCreate.fillRect(0, 0, width, height);
                }
                Icon icon = getIcon();
                if (icon != null && (iconWidth = icon.getIconWidth()) > 0 && (iconHeight = icon.getIconHeight()) > 0) {
                    if (iconWidth > iconHeight) {
                        i3 = (height - ((width * iconHeight) / iconWidth)) / 2;
                        d2 = width / iconWidth;
                    } else {
                        i2 = (width - ((height * iconWidth) / iconHeight)) / 2;
                        d2 = height / iconHeight;
                    }
                    ((Graphics2D) graphicsCreate).translate(i2, i3);
                    ((Graphics2D) graphicsCreate).scale(d2, d2);
                    icon.paintIcon(this, graphicsCreate, 0, 0);
                }
                graphicsCreate.dispose();
            }
        };
        this.systemLabel.addMouseListener(new MouseAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsInternalFrameTitlePane.2
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mouseClicked(MouseEvent mouseEvent) throws HeadlessException {
                if (mouseEvent.getClickCount() == 2 && WindowsInternalFrameTitlePane.this.frame.isClosable() && !WindowsInternalFrameTitlePane.this.frame.isIcon()) {
                    WindowsInternalFrameTitlePane.this.systemPopupMenu.setVisible(false);
                    WindowsInternalFrameTitlePane.this.frame.doDefaultCloseAction();
                } else {
                    super.mouseClicked(mouseEvent);
                }
            }

            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mousePressed(MouseEvent mouseEvent) {
                try {
                    WindowsInternalFrameTitlePane.this.frame.setSelected(true);
                } catch (PropertyVetoException e2) {
                }
                WindowsInternalFrameTitlePane.this.showSystemPopupMenu(mouseEvent.getComponent());
            }
        });
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
        showSystemPopupMenu(this.systemLabel);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showSystemPopupMenu(Component component) {
        Dimension dimension = new Dimension();
        Border border = this.frame.getBorder();
        if (border != null) {
            dimension.width += border.getBorderInsets(this.frame).left + border.getBorderInsets(this.frame).right;
            dimension.height += border.getBorderInsets(this.frame).bottom + border.getBorderInsets(this.frame).top;
        }
        if (!this.frame.isIcon()) {
            this.systemPopupMenu.show(component, getX() - dimension.width, (getY() + getHeight()) - dimension.height);
        } else {
            this.systemPopupMenu.show(component, getX() - dimension.width, (getY() - this.systemPopupMenu.getPreferredSize().height) - dimension.height);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected PropertyChangeListener createPropertyChangeListener() {
        return new WindowsPropertyChangeHandler();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected LayoutManager createLayout() {
        return new WindowsTitlePaneLayout();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsInternalFrameTitlePane$WindowsTitlePaneLayout.class */
    public class WindowsTitlePaneLayout extends BasicInternalFrameTitlePane.TitlePaneLayout {
        private Insets captionMargin;
        private Insets contentMargin;
        private XPStyle xp;

        WindowsTitlePaneLayout() {
            super();
            this.captionMargin = null;
            this.contentMargin = null;
            this.xp = XPStyle.getXP();
            if (this.xp != null) {
                this.captionMargin = this.xp.getMargin(WindowsInternalFrameTitlePane.this, TMSchema.Part.WP_CAPTION, null, TMSchema.Prop.CAPTIONMARGINS);
                this.contentMargin = this.xp.getMargin(WindowsInternalFrameTitlePane.this, TMSchema.Part.WP_CAPTION, null, TMSchema.Prop.CONTENTMARGINS);
            }
            if (this.captionMargin == null) {
                this.captionMargin = new Insets(0, 2, 0, 2);
            }
            if (this.contentMargin == null) {
                this.contentMargin = new Insets(0, 0, 0, 0);
            }
        }

        private int layoutButton(JComponent jComponent, TMSchema.Part part, int i2, int i3, int i4, int i5, int i6, boolean z2) {
            int i7;
            if (!z2) {
                i2 -= i4;
            }
            jComponent.setBounds(i2, i3, i4, i5);
            if (z2) {
                i7 = i2 + i4 + 2;
            } else {
                i7 = i2 - 2;
            }
            return i7;
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout, java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int iLayoutButton;
            int i3;
            boolean zIsLeftToRight = WindowsGraphicsUtils.isLeftToRight(WindowsInternalFrameTitlePane.this.frame);
            int width = WindowsInternalFrameTitlePane.this.getWidth();
            int height = WindowsInternalFrameTitlePane.this.getHeight();
            int i4 = this.xp != null ? ((height - 2) * 6) / 10 : height - 4;
            if (this.xp != null) {
                i2 = zIsLeftToRight ? this.captionMargin.left + 2 : (width - this.captionMargin.right) - 2;
            } else {
                i2 = zIsLeftToRight ? this.captionMargin.left : width - this.captionMargin.right;
            }
            layoutButton(WindowsInternalFrameTitlePane.this.systemLabel, TMSchema.Part.WP_SYSBUTTON, i2, (height - i4) / 2, i4, i4, 0, zIsLeftToRight);
            if (this.xp != null) {
                iLayoutButton = zIsLeftToRight ? (width - this.captionMargin.right) - 2 : this.captionMargin.left + 2;
                if (WindowsInternalFrameTitlePane.this.frame.isMaximum()) {
                    i3 = 1 + 1;
                } else {
                    i3 = 1 + 5;
                }
            } else {
                iLayoutButton = zIsLeftToRight ? width - this.captionMargin.right : this.captionMargin.left;
                i3 = (height - WindowsInternalFrameTitlePane.this.buttonHeight) / 2;
            }
            if (WindowsInternalFrameTitlePane.this.frame.isClosable()) {
                iLayoutButton = layoutButton(WindowsInternalFrameTitlePane.this.closeButton, TMSchema.Part.WP_CLOSEBUTTON, iLayoutButton, i3, WindowsInternalFrameTitlePane.this.buttonWidth, WindowsInternalFrameTitlePane.this.buttonHeight, 2, !zIsLeftToRight);
            }
            if (WindowsInternalFrameTitlePane.this.frame.isMaximizable()) {
                iLayoutButton = layoutButton(WindowsInternalFrameTitlePane.this.maxButton, TMSchema.Part.WP_MAXBUTTON, iLayoutButton, i3, WindowsInternalFrameTitlePane.this.buttonWidth, WindowsInternalFrameTitlePane.this.buttonHeight, this.xp != null ? 2 : 0, !zIsLeftToRight);
            }
            if (WindowsInternalFrameTitlePane.this.frame.isIconifiable()) {
                layoutButton(WindowsInternalFrameTitlePane.this.iconButton, TMSchema.Part.WP_MINBUTTON, iLayoutButton, i3, WindowsInternalFrameTitlePane.this.buttonWidth, WindowsInternalFrameTitlePane.this.buttonHeight, 0, !zIsLeftToRight);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsInternalFrameTitlePane$WindowsPropertyChangeHandler.class */
    public class WindowsPropertyChangeHandler extends BasicInternalFrameTitlePane.PropertyChangeHandler {
        public WindowsPropertyChangeHandler() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (JInternalFrame.FRAME_ICON_PROPERTY.equals(propertyChangeEvent.getPropertyName()) && WindowsInternalFrameTitlePane.this.systemLabel != null) {
                WindowsInternalFrameTitlePane.this.systemLabel.setIcon(WindowsInternalFrameTitlePane.this.frame.getFrameIcon());
            }
            super.propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsInternalFrameTitlePane$ScalableIconUIResource.class */
    public static class ScalableIconUIResource implements Icon, UIResource {
        private static final int SIZE = 16;
        private Icon[] icons;

        public ScalableIconUIResource(Object[] objArr) {
            this.icons = new Icon[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                if (objArr[i2] instanceof UIDefaults.LazyValue) {
                    this.icons[i2] = (Icon) ((UIDefaults.LazyValue) objArr[i2]).createValue(null);
                } else {
                    this.icons[i2] = (Icon) objArr[i2];
                }
            }
        }

        protected Icon getBestIcon(int i2) {
            int iconWidth;
            int iAbs;
            if (this.icons != null && this.icons.length > 0) {
                int i3 = 0;
                int i4 = Integer.MAX_VALUE;
                for (int i5 = 0; i5 < this.icons.length; i5++) {
                    Icon icon = this.icons[i5];
                    if (icon != null && (iconWidth = icon.getIconWidth()) > 0 && (iAbs = Math.abs(iconWidth - i2)) < i4) {
                        i4 = iAbs;
                        i3 = i5;
                    }
                }
                return this.icons[i3];
            }
            return null;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            int iconWidth;
            Graphics2D graphics2D = (Graphics2D) graphics.create();
            int iconWidth2 = getIconWidth();
            Icon bestIcon = getBestIcon((int) (iconWidth2 * graphics2D.getTransform().getScaleX()));
            if (bestIcon != null && (iconWidth = bestIcon.getIconWidth()) > 0) {
                double d2 = iconWidth2 / iconWidth;
                graphics2D.translate(i2, i3);
                graphics2D.scale(d2, d2);
                bestIcon.paintIcon(component, graphics2D, 0, 0);
            }
            graphics2D.dispose();
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 16;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 16;
        }
    }
}
