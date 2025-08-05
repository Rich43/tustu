package com.sun.java.swing.plaf.motif;

import com.sun.java.swing.plaf.motif.MotifBorders;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyVetoException;
import java.util.EventListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;
import sun.awt.AWTAccessor;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopIconUI.class */
public class MotifDesktopIconUI extends BasicDesktopIconUI {
    protected DesktopIconActionListener desktopIconActionListener;
    protected DesktopIconMouseListener desktopIconMouseListener;
    protected Icon defaultIcon;
    protected IconButton iconButton;
    protected IconLabel iconLabel;
    private MotifInternalFrameTitlePane sysMenuTitlePane;
    JPopupMenu systemMenu;
    EventListener mml;
    static final int LABEL_HEIGHT = 18;
    static final int LABEL_DIVIDER = 4;
    static final Font defaultTitleFont = new Font("SansSerif", 0, 12);

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifDesktopIconUI();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installDefaults() {
        super.installDefaults();
        setDefaultIcon(UIManager.getIcon("DesktopIcon.icon"));
        this.iconButton = createIconButton(this.defaultIcon);
        this.sysMenuTitlePane = new MotifInternalFrameTitlePane(this.frame);
        this.systemMenu = this.sysMenuTitlePane.getSystemMenu();
        MotifBorders.FrameBorder frameBorder = new MotifBorders.FrameBorder(this.desktopIcon);
        this.desktopIcon.setLayout(new BorderLayout());
        this.iconButton.setBorder(frameBorder);
        this.desktopIcon.add(this.iconButton, BorderLayout.CENTER);
        this.iconLabel = createIconLabel(this.frame);
        this.iconLabel.setBorder(frameBorder);
        this.desktopIcon.add(this.iconLabel, "South");
        this.desktopIcon.setSize(this.desktopIcon.getPreferredSize());
        this.desktopIcon.validate();
        JLayeredPane.putLayer(this.desktopIcon, JLayeredPane.getLayer((JComponent) this.frame));
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installComponents() {
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallComponents() {
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void installListeners() {
        super.installListeners();
        this.desktopIconActionListener = createDesktopIconActionListener();
        this.desktopIconMouseListener = createDesktopIconMouseListener();
        this.iconButton.addActionListener(this.desktopIconActionListener);
        this.iconButton.addMouseListener(this.desktopIconMouseListener);
        this.iconLabel.addMouseListener(this.desktopIconMouseListener);
    }

    JInternalFrame.JDesktopIcon getDesktopIcon() {
        return this.desktopIcon;
    }

    void setDesktopIcon(JInternalFrame.JDesktopIcon jDesktopIcon) {
        this.desktopIcon = jDesktopIcon;
    }

    JInternalFrame getFrame() {
        return this.frame;
    }

    void setFrame(JInternalFrame jInternalFrame) {
        this.frame = jInternalFrame;
    }

    protected void showSystemMenu() {
        this.systemMenu.show(this.iconButton, 0, getDesktopIcon().getHeight());
    }

    protected void hideSystemMenu() throws HeadlessException {
        this.systemMenu.setVisible(false);
    }

    protected IconLabel createIconLabel(JInternalFrame jInternalFrame) {
        return new IconLabel(jInternalFrame);
    }

    protected IconButton createIconButton(Icon icon) {
        return new IconButton(icon);
    }

    protected DesktopIconActionListener createDesktopIconActionListener() {
        return new DesktopIconActionListener();
    }

    protected DesktopIconMouseListener createDesktopIconMouseListener() {
        return new DesktopIconMouseListener();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallDefaults() {
        super.uninstallDefaults();
        this.desktopIcon.setLayout(null);
        this.desktopIcon.remove(this.iconButton);
        this.desktopIcon.remove(this.iconLabel);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.iconButton.removeActionListener(this.desktopIconActionListener);
        this.iconButton.removeMouseListener(this.desktopIconMouseListener);
        this.sysMenuTitlePane.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        JInternalFrame internalFrame = this.desktopIcon.getInternalFrame();
        int iconWidth = this.defaultIcon.getIconWidth();
        int iconHeight = this.defaultIcon.getIconHeight() + 18 + 4;
        Border border = internalFrame.getBorder();
        if (border != null) {
            iconWidth += border.getBorderInsets(internalFrame).left + border.getBorderInsets(internalFrame).right;
            iconHeight += border.getBorderInsets(internalFrame).bottom + border.getBorderInsets(internalFrame).top;
        }
        return new Dimension(iconWidth, iconHeight);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        return getMinimumSize(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicDesktopIconUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        return getMinimumSize(jComponent);
    }

    public Icon getDefaultIcon() {
        return this.defaultIcon;
    }

    public void setDefaultIcon(Icon icon) {
        this.defaultIcon = icon;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopIconUI$IconLabel.class */
    protected class IconLabel extends JPanel {
        JInternalFrame frame;

        IconLabel(JInternalFrame jInternalFrame) {
            this.frame = jInternalFrame;
            setFont(MotifDesktopIconUI.defaultTitleFont);
            addMouseMotionListener(new MouseMotionListener() { // from class: com.sun.java.swing.plaf.motif.MotifDesktopIconUI.IconLabel.1
                @Override // java.awt.event.MouseMotionListener
                public void mouseDragged(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseMotionListener
                public void mouseMoved(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }
            });
            addMouseListener(new MouseListener() { // from class: com.sun.java.swing.plaf.motif.MotifDesktopIconUI.IconLabel.2
                @Override // java.awt.event.MouseListener
                public void mouseClicked(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mousePressed(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseReleased(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseEntered(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseExited(MouseEvent mouseEvent) {
                    IconLabel.this.forwardEventToParent(mouseEvent);
                }
            });
        }

        void forwardEventToParent(MouseEvent mouseEvent) {
            MouseEvent mouseEvent2 = new MouseEvent(getParent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
            AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
            mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
            getParent().dispatchEvent(mouseEvent2);
        }

        @Override // java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getMinimumSize() {
            return new Dimension(MotifDesktopIconUI.this.defaultIcon.getIconWidth() + 1, 22);
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            String title = this.frame.getTitle();
            FontMetrics fontMetrics = this.frame.getFontMetrics(MotifDesktopIconUI.defaultTitleFont);
            int iStringWidth = 4;
            if (title != null) {
                iStringWidth = 4 + SwingUtilities2.stringWidth(this.frame, fontMetrics, title);
            }
            return new Dimension(iStringWidth, 22);
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            super.paint(graphics);
            int width = getWidth() - 1;
            graphics.setColor(UIManager.getColor("inactiveCaptionBorder").darker().darker());
            graphics.setClip(0, 0, getWidth(), getHeight());
            graphics.drawLine(width - 1, 1, width - 1, 1);
            graphics.drawLine(width, 0, width, 0);
            graphics.setColor(UIManager.getColor("inactiveCaption"));
            graphics.fillRect(2, 1, width - 3, 19);
            graphics.setClip(2, 1, width - 4, 18);
            int descent = 18 - SwingUtilities2.getFontMetrics(this.frame, graphics).getDescent();
            graphics.setColor(UIManager.getColor("inactiveCaptionText"));
            String title = this.frame.getTitle();
            if (title != null) {
                SwingUtilities2.drawString(this.frame, graphics, title, 4, descent);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopIconUI$IconButton.class */
    protected class IconButton extends JButton {
        Icon icon;

        IconButton(Icon icon) {
            super(icon);
            this.icon = icon;
            addMouseMotionListener(new MouseMotionListener() { // from class: com.sun.java.swing.plaf.motif.MotifDesktopIconUI.IconButton.1
                @Override // java.awt.event.MouseMotionListener
                public void mouseDragged(MouseEvent mouseEvent) {
                    IconButton.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseMotionListener
                public void mouseMoved(MouseEvent mouseEvent) {
                    IconButton.this.forwardEventToParent(mouseEvent);
                }
            });
            addMouseListener(new MouseListener() { // from class: com.sun.java.swing.plaf.motif.MotifDesktopIconUI.IconButton.2
                @Override // java.awt.event.MouseListener
                public void mouseClicked(MouseEvent mouseEvent) {
                    IconButton.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mousePressed(MouseEvent mouseEvent) {
                    IconButton.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseReleased(MouseEvent mouseEvent) {
                    if (!MotifDesktopIconUI.this.systemMenu.isShowing()) {
                        IconButton.this.forwardEventToParent(mouseEvent);
                    }
                }

                @Override // java.awt.event.MouseListener
                public void mouseEntered(MouseEvent mouseEvent) {
                    IconButton.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseExited(MouseEvent mouseEvent) {
                    IconButton.this.forwardEventToParent(mouseEvent);
                }
            });
        }

        void forwardEventToParent(MouseEvent mouseEvent) {
            MouseEvent mouseEvent2 = new MouseEvent(getParent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
            AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
            mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
            getParent().dispatchEvent(mouseEvent2);
        }

        @Override // java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopIconUI$DesktopIconActionListener.class */
    protected class DesktopIconActionListener implements ActionListener {
        protected DesktopIconActionListener() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MotifDesktopIconUI.this.systemMenu.show(MotifDesktopIconUI.this.iconButton, 0, MotifDesktopIconUI.this.getDesktopIcon().getHeight());
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifDesktopIconUI$DesktopIconMouseListener.class */
    protected class DesktopIconMouseListener extends MouseAdapter {
        protected DesktopIconMouseListener() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
            if (mouseEvent.getClickCount() > 1) {
                try {
                    MotifDesktopIconUI.this.getFrame().setIcon(false);
                } catch (PropertyVetoException e2) {
                }
                MotifDesktopIconUI.this.systemMenu.setVisible(false);
                MotifDesktopIconUI.this.getFrame().getDesktopPane().getDesktopManager().endDraggingFrame((JComponent) mouseEvent.getSource());
            }
        }
    }
}
