package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameTitlePane.class */
public class MotifInternalFrameTitlePane extends BasicInternalFrameTitlePane implements LayoutManager, ActionListener, PropertyChangeListener {
    SystemButton systemButton;
    MinimizeButton minimizeButton;
    MaximizeButton maximizeButton;
    JPopupMenu systemMenu;
    Title title;
    Color color;
    Color highlight;
    Color shadow;
    public static final int BUTTON_SIZE = 19;
    static Dimension buttonDimension = new Dimension(19, 19);

    public MotifInternalFrameTitlePane(JInternalFrame jInternalFrame) {
        super(jInternalFrame);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void installDefaults() {
        setFont(UIManager.getFont("InternalFrame.titleFont"));
        setPreferredSize(new Dimension(100, 19));
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void uninstallListeners() {
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected PropertyChangeListener createPropertyChangeListener() {
        return this;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected LayoutManager createLayout() {
        return this;
    }

    JPopupMenu getSystemMenu() {
        return this.systemMenu;
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void assembleSystemMenu() {
        this.systemMenu = new JPopupMenu();
        this.systemMenu.add(this.restoreAction).setMnemonic(getButtonMnemonic("restore"));
        this.systemMenu.add(this.moveAction).setMnemonic(getButtonMnemonic("move"));
        this.systemMenu.add(this.sizeAction).setMnemonic(getButtonMnemonic("size"));
        this.systemMenu.add(this.iconifyAction).setMnemonic(getButtonMnemonic("minimize"));
        this.systemMenu.add(this.maximizeAction).setMnemonic(getButtonMnemonic("maximize"));
        this.systemMenu.add(new JSeparator());
        this.systemMenu.add(this.closeAction).setMnemonic(getButtonMnemonic("close"));
        this.systemButton = new SystemButton();
        this.systemButton.addActionListener(new ActionListener() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.1
            @Override // java.awt.event.ActionListener
            public void actionPerformed(ActionEvent actionEvent) {
                MotifInternalFrameTitlePane.this.systemMenu.show(MotifInternalFrameTitlePane.this.systemButton, 0, 19);
            }
        });
        this.systemButton.addMouseListener(new MouseAdapter() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.2
            @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
            public void mousePressed(MouseEvent mouseEvent) throws HeadlessException {
                try {
                    MotifInternalFrameTitlePane.this.frame.setSelected(true);
                } catch (PropertyVetoException e2) {
                }
                if (mouseEvent.getClickCount() == 2) {
                    MotifInternalFrameTitlePane.this.closeAction.actionPerformed(new ActionEvent(mouseEvent.getSource(), 1001, null, mouseEvent.getWhen(), 0));
                    MotifInternalFrameTitlePane.this.systemMenu.setVisible(false);
                }
            }
        });
    }

    private static int getButtonMnemonic(String str) {
        try {
            return Integer.parseInt(UIManager.getString("InternalFrameTitlePane." + str + "Button.mnemonic"));
        } catch (NumberFormatException e2) {
            return -1;
        }
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void createButtons() {
        this.minimizeButton = new MinimizeButton();
        this.minimizeButton.addActionListener(this.iconifyAction);
        this.maximizeButton = new MaximizeButton();
        this.maximizeButton.addActionListener(this.maximizeAction);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void addSubComponents() {
        this.title = new Title(this.frame.getTitle());
        this.title.setFont(getFont());
        add(this.systemButton);
        add(this.title);
        add(this.minimizeButton);
        add(this.maximizeButton);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane, javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
    }

    void setColors(Color color, Color color2, Color color3) {
        this.color = color;
        this.highlight = color2;
        this.shadow = color3;
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        String propertyName = propertyChangeEvent.getPropertyName();
        if (JInternalFrame.IS_SELECTED_PROPERTY.equals(propertyName)) {
            repaint();
        } else if (propertyName.equals("maximizable")) {
            if (((Boolean) propertyChangeEvent.getNewValue()) == Boolean.TRUE) {
                add(this.maximizeButton);
            } else {
                remove(this.maximizeButton);
            }
            revalidate();
            repaint();
        } else if (propertyName.equals("iconable")) {
            if (((Boolean) propertyChangeEvent.getNewValue()) == Boolean.TRUE) {
                add(this.minimizeButton);
            } else {
                remove(this.minimizeButton);
            }
            revalidate();
            repaint();
        } else if (propertyName.equals("title")) {
            repaint();
        }
        enableActions();
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
        return new Dimension(100, 19);
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        int width = getWidth();
        this.systemButton.setBounds(0, 0, 19, 19);
        int i2 = width - 19;
        if (this.frame.isMaximizable()) {
            this.maximizeButton.setBounds(i2, 0, 19, 19);
            i2 -= 19;
        } else if (this.maximizeButton.getParent() != null) {
            this.maximizeButton.getParent().remove(this.maximizeButton);
        }
        if (this.frame.isIconifiable()) {
            this.minimizeButton.setBounds(i2, 0, 19, 19);
            i2 -= 19;
        } else if (this.minimizeButton.getParent() != null) {
            this.minimizeButton.getParent().remove(this.minimizeButton);
        }
        this.title.setBounds(19, 0, i2, 19);
    }

    @Override // javax.swing.plaf.basic.BasicInternalFrameTitlePane
    protected void showSystemMenu() {
        this.systemMenu.show(this.systemButton, 0, 19);
    }

    protected void hideSystemMenu() throws HeadlessException {
        this.systemMenu.setVisible(false);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameTitlePane$FrameButton.class */
    private abstract class FrameButton extends JButton {
        FrameButton() {
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override // java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public void requestFocus() {
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getMinimumSize() {
            return MotifInternalFrameTitlePane.buttonDimension;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            return MotifInternalFrameTitlePane.buttonDimension;
        }

        @Override // javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            Dimension size = getSize();
            int i2 = size.width - 1;
            int i3 = size.height - 1;
            graphics.setColor(MotifInternalFrameTitlePane.this.color);
            graphics.fillRect(1, 1, size.width, size.height);
            boolean zIsPressed = getModel().isPressed();
            graphics.setColor(zIsPressed ? MotifInternalFrameTitlePane.this.shadow : MotifInternalFrameTitlePane.this.highlight);
            graphics.drawLine(0, 0, i2, 0);
            graphics.drawLine(0, 0, 0, i3);
            graphics.setColor(zIsPressed ? MotifInternalFrameTitlePane.this.highlight : MotifInternalFrameTitlePane.this.shadow);
            graphics.drawLine(1, i3, i2, i3);
            graphics.drawLine(i2, 1, i2, i3);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameTitlePane$MinimizeButton.class */
    private class MinimizeButton extends FrameButton {
        private MinimizeButton() {
            super();
        }

        @Override // com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.FrameButton, javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.setColor(MotifInternalFrameTitlePane.this.highlight);
            graphics.drawLine(7, 8, 7, 11);
            graphics.drawLine(7, 8, 10, 8);
            graphics.setColor(MotifInternalFrameTitlePane.this.shadow);
            graphics.drawLine(8, 11, 10, 11);
            graphics.drawLine(11, 9, 11, 11);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameTitlePane$MaximizeButton.class */
    private class MaximizeButton extends FrameButton {
        private MaximizeButton() {
            super();
        }

        @Override // com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.FrameButton, javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            boolean zIsMaximum = MotifInternalFrameTitlePane.this.frame.isMaximum();
            graphics.setColor(zIsMaximum ? MotifInternalFrameTitlePane.this.shadow : MotifInternalFrameTitlePane.this.highlight);
            graphics.drawLine(4, 4, 4, 14);
            graphics.drawLine(4, 4, 14, 4);
            graphics.setColor(zIsMaximum ? MotifInternalFrameTitlePane.this.highlight : MotifInternalFrameTitlePane.this.shadow);
            graphics.drawLine(5, 14, 14, 14);
            graphics.drawLine(14, 5, 14, 14);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameTitlePane$SystemButton.class */
    private class SystemButton extends FrameButton {
        private SystemButton() {
            super();
        }

        @Override // com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.FrameButton, java.awt.Component
        public boolean isFocusTraversable() {
            return false;
        }

        @Override // com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.FrameButton, javax.swing.JComponent, java.awt.Component
        public void requestFocus() {
        }

        @Override // com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.FrameButton, javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.setColor(MotifInternalFrameTitlePane.this.highlight);
            graphics.drawLine(4, 8, 4, 11);
            graphics.drawLine(4, 8, 14, 8);
            graphics.setColor(MotifInternalFrameTitlePane.this.shadow);
            graphics.drawLine(5, 11, 14, 11);
            graphics.drawLine(14, 9, 14, 11);
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifInternalFrameTitlePane$Title.class */
    private class Title extends FrameButton {
        Title(String str) {
            super();
            setText(str);
            setHorizontalAlignment(0);
            setBorder(BorderFactory.createBevelBorder(0, UIManager.getColor("activeCaptionBorder"), UIManager.getColor("inactiveCaptionBorder")));
            addMouseMotionListener(new MouseMotionListener() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.Title.1
                @Override // java.awt.event.MouseMotionListener
                public void mouseDragged(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseMotionListener
                public void mouseMoved(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }
            });
            addMouseListener(new MouseListener() { // from class: com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.Title.2
                @Override // java.awt.event.MouseListener
                public void mouseClicked(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mousePressed(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseReleased(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseEntered(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }

                @Override // java.awt.event.MouseListener
                public void mouseExited(MouseEvent mouseEvent) {
                    Title.this.forwardEventToParent(mouseEvent);
                }
            });
        }

        void forwardEventToParent(MouseEvent mouseEvent) {
            MouseEvent mouseEvent2 = new MouseEvent(getParent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
            AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
            mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
            getParent().dispatchEvent(mouseEvent2);
        }

        @Override // com.sun.java.swing.plaf.motif.MotifInternalFrameTitlePane.FrameButton, javax.swing.JComponent
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            if (MotifInternalFrameTitlePane.this.frame.isSelected()) {
                graphics.setColor(UIManager.getColor("activeCaptionText"));
            } else {
                graphics.setColor(UIManager.getColor("inactiveCaptionText"));
            }
            Dimension size = getSize();
            String title = MotifInternalFrameTitlePane.this.frame.getTitle();
            if (title != null) {
                MotifGraphicsUtils.drawStringInRect(MotifInternalFrameTitlePane.this.frame, graphics, title, 0, 0, size.width, size.height, 0);
            }
        }
    }
}
