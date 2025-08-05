package de.muntjak.tinylookandfeel;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyInternalFrameTitlePane.class */
public class TinyInternalFrameTitlePane extends BasicInternalFrameTitlePane implements LayoutManager {
    protected boolean isPalette;
    private int buttonsWidth;
    static TinyWindowButtonUI iconButtonUI;
    static TinyWindowButtonUI maxButtonUI;
    static TinyWindowButtonUI closeButtonUI;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyInternalFrameTitlePane$RolloverListener.class */
    class RolloverListener implements MouseListener {
        JButton button;
        private final TinyInternalFrameTitlePane this$0;

        public RolloverListener(TinyInternalFrameTitlePane tinyInternalFrameTitlePane, JButton jButton) {
            this.this$0 = tinyInternalFrameTitlePane;
            this.button = jButton;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            this.button.getModel().setRollover(true);
            if (!this.button.isEnabled()) {
                this.button.setEnabled(true);
            }
            this.button.repaint();
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            this.button.getModel().setRollover(false);
            if (!this.this$0.frame.isSelected()) {
                this.button.setEnabled(false);
            }
            this.button.repaint();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyInternalFrameTitlePane$TinyPropertyChangeHandler.class */
    class TinyPropertyChangeHandler extends BasicInternalFrameTitlePane.PropertyChangeHandler {
        private final TinyInternalFrameTitlePane this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        TinyPropertyChangeHandler(TinyInternalFrameTitlePane tinyInternalFrameTitlePane) {
            super();
            this.this$0 = tinyInternalFrameTitlePane;
        }

        @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_SELECTED_PROPERTY)) {
                Boolean bool = (Boolean) propertyChangeEvent.getNewValue();
                this.this$0.iconButton.putClientProperty("paintActive", bool);
                this.this$0.closeButton.putClientProperty("paintActive", bool);
                this.this$0.maxButton.putClientProperty("paintActive", bool);
            }
            super.propertyChange(propertyChangeEvent);
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected PropertyChangeListener createPropertyChangeListener() {
        return new TinyPropertyChangeHandler(this);
    }

    public TinyInternalFrameTitlePane(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
        this.isPalette = false;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected JMenu createSystemMenu() {
        JMenu jMenu = new JMenu("");
        jMenu.addMenuListener(new MenuListener(this) { // from class: de.muntjak.tinylookandfeel.TinyInternalFrameTitlePane.1
            private final TinyInternalFrameTitlePane this$0;

            {
                this.this$0 = this;
            }

            @Override // javax.swing.event.MenuListener
            public void menuSelected(MenuEvent menuEvent) {
                if (this.this$0.frameHasMenuBar()) {
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
        jMenu.putClientProperty("isSystemMenu", Boolean.TRUE);
        return jMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean frameHasMenuBar() {
        return (this.frame == null || this.frame.getJMenuBar() == null) ? false : true;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void addSystemMenuItems(JMenu jMenu) {
        JMenuItem jMenuItemAdd = jMenu.add(this.restoreAction);
        jMenuItemAdd.setIcon(MenuItemIconFactory.getSystemRestoreIcon());
        jMenuItemAdd.setMnemonic('R');
        JMenuItem jMenuItemAdd2 = jMenu.add(this.iconifyAction);
        jMenuItemAdd2.setIcon(MenuItemIconFactory.getSystemIconifyIcon());
        jMenuItemAdd2.setMnemonic('n');
        JMenuItem jMenuItemAdd3 = jMenu.add(this.maximizeAction);
        jMenuItemAdd3.setIcon(MenuItemIconFactory.getSystemMaximizeIcon());
        jMenuItemAdd3.setMnemonic('x');
        jMenu.add(new JSeparator());
        JMenuItem jMenuItemAdd4 = jMenu.add(this.closeAction);
        jMenuItemAdd4.setIcon(MenuItemIconFactory.getSystemCloseIcon());
        jMenuItemAdd4.setMnemonic('C');
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void paintTitleBackground(Graphics graphics) {
    }

    public boolean isFrameSelected() {
        return this.frame.isSelected();
    }

    public boolean isFrameMaximized() {
        return this.frame.isMaximum();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane, javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        this.frame.setOpaque(false);
        boolean zIsLeftToRight = this.frame.getComponentOrientation().isLeftToRight();
        boolean zIsSelected = this.frame.isSelected();
        int width = getWidth();
        int height = getHeight();
        String title = this.frame.getTitle();
        if (title != null) {
            int i2 = zIsLeftToRight ? 24 : ((width - 4) - 16) - 4;
            graphics.setFont(getFont());
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int iStringWidth = fontMetrics.stringWidth(title);
            int height2 = ((height - fontMetrics.getHeight()) / 2) + fontMetrics.getAscent() + 1;
            if (!zIsLeftToRight) {
                i2 -= iStringWidth;
            }
            if (graphics instanceof Graphics2D) {
                ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            }
            if (!zIsSelected) {
                graphics.setColor(Theme.frameTitleDisabledColor.getColor());
                graphics.drawString(title, i2, height2);
            } else {
                graphics.setColor(Theme.frameTitleShadowColor.getColor());
                graphics.drawString(title, i2 + 1, height2 + 1);
                graphics.setColor(Theme.frameTitleColor.getColor());
                graphics.drawString(title, i2, height2);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected LayoutManager createLayout() {
        return this;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void setButtonIcons() {
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void createButtons() {
        if (iconButtonUI == null) {
            iconButtonUI = TinyWindowButtonUI.createButtonUIForType(2);
            maxButtonUI = TinyWindowButtonUI.createButtonUIForType(1);
            closeButtonUI = TinyWindowButtonUI.createButtonUIForType(0);
        }
        this.iconButton = new SpecialUIButton(iconButtonUI);
        this.iconButton.addActionListener(this.iconifyAction);
        this.iconButton.setText(null);
        this.iconButton.setRolloverEnabled(true);
        this.iconButton.addMouseListener(new RolloverListener(this, this.iconButton));
        this.maxButton = new SpecialUIButton(maxButtonUI);
        this.maxButton.addActionListener(this.maximizeAction);
        this.maxButton.setText(null);
        this.maxButton.setRolloverEnabled(true);
        this.maxButton.addMouseListener(new RolloverListener(this, this.maxButton));
        this.closeButton = new SpecialUIButton(closeButtonUI);
        this.closeButton.addActionListener(this.closeAction);
        this.closeButton.setText(null);
        this.closeButton.setRolloverEnabled(true);
        this.closeButton.addMouseListener(new RolloverListener(this, this.closeButton));
        this.iconButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.FALSE);
        this.maxButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.FALSE);
        this.closeButton.putClientProperty(TinyWindowButtonUI.EXTERNAL_FRAME_BUTTON_KEY, Boolean.FALSE);
        this.iconButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.iconifyButtonAccessibleName"));
        this.maxButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.maximizeButtonAccessibleName"));
        this.closeButton.getAccessibleContext().setAccessibleName(UIManager.getString("InternalFrameTitlePane.closeButtonAccessibleName"));
        if (this.frame.isSelected()) {
            activate();
        } else {
            deactivate();
        }
    }

    public void paintPalette(Graphics graphics) {
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        return getPreferredSize(container);
    }

    public Dimension getPreferredSize(Container container) {
        int i2;
        this.isPalette = this.frame.getClientProperty("isPalette") == Boolean.TRUE;
        int i3 = 22;
        if (this.frame.isClosable()) {
            i3 = 22 + 19;
        }
        if (this.frame.isMaximizable()) {
            i3 += 19;
        }
        if (this.frame.isIconifiable()) {
            i3 += 19;
        }
        FontMetrics fontMetrics = getFontMetrics(getFont());
        String title = this.frame.getTitle();
        int iStringWidth = title != null ? fontMetrics.stringWidth(title) : 0;
        if ((title != null ? title.length() : 0) > 3) {
            int iStringWidth2 = fontMetrics.stringWidth(new StringBuffer().append(title.substring(0, 3)).append("...").toString());
            i2 = i3 + (iStringWidth < iStringWidth2 ? iStringWidth : iStringWidth2);
        } else {
            i2 = i3 + iStringWidth;
        }
        Dimension dimension = new Dimension(i2, this.isPalette ? 21 : 25);
        if (getBorder() != null) {
            Insets borderInsets = getBorder().getBorderInsets(container);
            dimension.height += borderInsets.top + borderInsets.bottom;
            dimension.width += borderInsets.left + borderInsets.right;
        }
        return dimension;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        this.isPalette = this.frame.getClientProperty("isPalette") == Boolean.TRUE;
        return new Dimension(32, this.isPalette ? 21 : 25);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return preferredLayoutSize(container);
    }

    public void setPalette(boolean z2) {
        this.isPalette = z2;
    }

    public boolean isPalette() {
        return this.isPalette;
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        this.isPalette = this.frame.getClientProperty("isPalette") == Boolean.TRUE;
        boolean zIsLeftToRight = this.frame.getComponentOrientation().isLeftToRight();
        int i2 = this.closeButton.getPreferredSize().height;
        int height = getHeight();
        int width = getWidth();
        int i3 = zIsLeftToRight ? width : 0;
        int i4 = ((height - i2) / 2) + 1;
        int i5 = this.isPalette ? TinyWindowButtonUI.framePaletteButtonSize.width : TinyWindowButtonUI.frameInternalButtonSize.width;
        Icon frameIcon = this.frame.getFrameIcon();
        int iconHeight = 0;
        if (frameIcon != null) {
            iconHeight = frameIcon.getIconHeight();
        }
        this.menuBar.setBounds(zIsLeftToRight ? 4 : (width - 16) - 4, (height - iconHeight) / 2, 16, 16);
        int i6 = zIsLeftToRight ? width : 0;
        if (this.frame.isClosable()) {
            i6 += zIsLeftToRight ? (-2) - i5 : 2;
            this.closeButton.setBounds(i6, i4, i5, i2);
            if (!zIsLeftToRight) {
                i6 += i5;
            }
        }
        if (this.frame.isMaximizable()) {
            i6 += zIsLeftToRight ? (-2) - i5 : 2;
            this.maxButton.setBounds(i6, i4, i5, i2);
            if (!zIsLeftToRight) {
                i6 += i5;
            }
        }
        if (this.frame.isIconifiable()) {
            i6 += zIsLeftToRight ? (-2) - i5 : 2;
            this.iconButton.setBounds(i6, i4, i5, i2);
            if (!zIsLeftToRight) {
                i6 += i5;
            }
        }
        this.buttonsWidth = zIsLeftToRight ? width - i6 : i6;
    }

    public void activate() {
        this.closeButton.setEnabled(true);
        this.iconButton.setEnabled(true);
        this.maxButton.setEnabled(true);
    }

    public void deactivate() {
        this.closeButton.setEnabled(false);
        this.iconButton.setEnabled(false);
        this.maxButton.setEnabled(false);
    }

    @Override // java.awt.Component, java.awt.MenuContainer
    public Font getFont() {
        return this.isPalette ? UIManager.getFont("InternalFrame.paletteTitleFont") : UIManager.getFont("InternalFrame.normalTitleFont");
    }
}
