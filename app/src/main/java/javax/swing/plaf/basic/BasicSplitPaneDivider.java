package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneDivider.class */
public class BasicSplitPaneDivider extends Container implements PropertyChangeListener {
    protected static final int ONE_TOUCH_SIZE = 6;
    protected static final int ONE_TOUCH_OFFSET = 2;
    protected DragController dragger;
    protected BasicSplitPaneUI splitPaneUI;
    protected int dividerSize = 0;
    protected Component hiddenDivider;
    protected JSplitPane splitPane;
    protected MouseHandler mouseHandler;
    protected int orientation;
    protected JButton leftButton;
    protected JButton rightButton;
    private Border border;
    private boolean mouseOver;
    private int oneTouchSize;
    private int oneTouchOffset;
    private boolean centerOneTouchButtons;

    public BasicSplitPaneDivider(BasicSplitPaneUI basicSplitPaneUI) {
        Cursor predefinedCursor;
        this.oneTouchSize = DefaultLookup.getInt(basicSplitPaneUI.getSplitPane(), basicSplitPaneUI, "SplitPane.oneTouchButtonSize", 6);
        this.oneTouchOffset = DefaultLookup.getInt(basicSplitPaneUI.getSplitPane(), basicSplitPaneUI, "SplitPane.oneTouchButtonOffset", 2);
        this.centerOneTouchButtons = DefaultLookup.getBoolean(basicSplitPaneUI.getSplitPane(), basicSplitPaneUI, "SplitPane.centerOneTouchButtons", true);
        setLayout(new DividerLayout());
        setBasicSplitPaneUI(basicSplitPaneUI);
        this.orientation = this.splitPane.getOrientation();
        if (this.orientation == 1) {
            predefinedCursor = Cursor.getPredefinedCursor(11);
        } else {
            predefinedCursor = Cursor.getPredefinedCursor(9);
        }
        setCursor(predefinedCursor);
        setBackground(UIManager.getColor("SplitPane.background"));
    }

    private void revalidateSplitPane() {
        invalidate();
        if (this.splitPane != null) {
            this.splitPane.revalidate();
        }
    }

    public void setBasicSplitPaneUI(BasicSplitPaneUI basicSplitPaneUI) {
        if (this.splitPane != null) {
            this.splitPane.removePropertyChangeListener(this);
            if (this.mouseHandler != null) {
                this.splitPane.removeMouseListener(this.mouseHandler);
                this.splitPane.removeMouseMotionListener(this.mouseHandler);
                removeMouseListener(this.mouseHandler);
                removeMouseMotionListener(this.mouseHandler);
                this.mouseHandler = null;
            }
        }
        this.splitPaneUI = basicSplitPaneUI;
        if (basicSplitPaneUI != null) {
            this.splitPane = basicSplitPaneUI.getSplitPane();
            if (this.splitPane != null) {
                if (this.mouseHandler == null) {
                    this.mouseHandler = new MouseHandler();
                }
                this.splitPane.addMouseListener(this.mouseHandler);
                this.splitPane.addMouseMotionListener(this.mouseHandler);
                addMouseListener(this.mouseHandler);
                addMouseMotionListener(this.mouseHandler);
                this.splitPane.addPropertyChangeListener(this);
                if (this.splitPane.isOneTouchExpandable()) {
                    oneTouchExpandableChanged();
                    return;
                }
                return;
            }
            return;
        }
        this.splitPane = null;
    }

    public BasicSplitPaneUI getBasicSplitPaneUI() {
        return this.splitPaneUI;
    }

    public void setDividerSize(int i2) {
        this.dividerSize = i2;
    }

    public int getDividerSize() {
        return this.dividerSize;
    }

    public void setBorder(Border border) {
        Border border2 = this.border;
        this.border = border;
    }

    public Border getBorder() {
        return this.border;
    }

    @Override // java.awt.Container
    public Insets getInsets() {
        Border border = getBorder();
        if (border != null) {
            return border.getBorderInsets(this);
        }
        return super.getInsets();
    }

    protected void setMouseOver(boolean z2) {
        this.mouseOver = z2;
    }

    public boolean isMouseOver() {
        return this.mouseOver;
    }

    @Override // java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (this.orientation == 1) {
            return new Dimension(getDividerSize(), 1);
        }
        return new Dimension(1, getDividerSize());
    }

    @Override // java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        Cursor predefinedCursor;
        if (propertyChangeEvent.getSource() == this.splitPane) {
            if (propertyChangeEvent.getPropertyName() == "orientation") {
                this.orientation = this.splitPane.getOrientation();
                if (this.orientation == 1) {
                    predefinedCursor = Cursor.getPredefinedCursor(11);
                } else {
                    predefinedCursor = Cursor.getPredefinedCursor(9);
                }
                setCursor(predefinedCursor);
                revalidateSplitPane();
                return;
            }
            if (propertyChangeEvent.getPropertyName() == JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY) {
                oneTouchExpandableChanged();
            }
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        Border border = getBorder();
        if (border != null) {
            Dimension size = getSize();
            border.paintBorder(this, graphics, 0, 0, size.width, size.height);
        }
    }

    protected void oneTouchExpandableChanged() {
        if (!DefaultLookup.getBoolean(this.splitPane, this.splitPaneUI, "SplitPane.supportsOneTouchButtons", true)) {
            return;
        }
        if (this.splitPane.isOneTouchExpandable() && this.leftButton == null && this.rightButton == null) {
            this.leftButton = createLeftOneTouchButton();
            if (this.leftButton != null) {
                this.leftButton.addActionListener(new OneTouchActionHandler(true));
            }
            this.rightButton = createRightOneTouchButton();
            if (this.rightButton != null) {
                this.rightButton.addActionListener(new OneTouchActionHandler(false));
            }
            if (this.leftButton != null && this.rightButton != null) {
                add(this.leftButton);
                add(this.rightButton);
            }
        }
        revalidateSplitPane();
    }

    protected JButton createLeftOneTouchButton() {
        JButton jButton = new JButton() { // from class: javax.swing.plaf.basic.BasicSplitPaneDivider.1
            @Override // javax.swing.JComponent
            public void setBorder(Border border) {
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                if (BasicSplitPaneDivider.this.splitPane != null) {
                    int[] iArr = new int[3];
                    int[] iArr2 = new int[3];
                    graphics.setColor(getBackground());
                    graphics.fillRect(0, 0, getWidth(), getHeight());
                    graphics.setColor(Color.black);
                    if (BasicSplitPaneDivider.this.orientation == 0) {
                        int iMin = Math.min(getHeight(), BasicSplitPaneDivider.this.oneTouchSize);
                        iArr[0] = iMin;
                        iArr[1] = 0;
                        iArr[2] = iMin << 1;
                        iArr2[0] = 0;
                        iArr2[2] = iMin;
                        iArr2[1] = iMin;
                        graphics.drawPolygon(iArr, iArr2, 3);
                    } else {
                        int iMin2 = Math.min(getWidth(), BasicSplitPaneDivider.this.oneTouchSize);
                        iArr[2] = iMin2;
                        iArr[0] = iMin2;
                        iArr[1] = 0;
                        iArr2[0] = 0;
                        iArr2[1] = iMin2;
                        iArr2[2] = iMin2 << 1;
                    }
                    graphics.fillPolygon(iArr, iArr2, 3);
                }
            }

            @Override // java.awt.Component
            public boolean isFocusTraversable() {
                return false;
            }
        };
        jButton.setMinimumSize(new Dimension(this.oneTouchSize, this.oneTouchSize));
        jButton.setCursor(Cursor.getPredefinedCursor(0));
        jButton.setFocusPainted(false);
        jButton.setBorderPainted(false);
        jButton.setRequestFocusEnabled(false);
        return jButton;
    }

    protected JButton createRightOneTouchButton() {
        JButton jButton = new JButton() { // from class: javax.swing.plaf.basic.BasicSplitPaneDivider.2
            @Override // javax.swing.JComponent
            public void setBorder(Border border) {
            }

            @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
            public void paint(Graphics graphics) {
                if (BasicSplitPaneDivider.this.splitPane != null) {
                    int[] iArr = new int[3];
                    int[] iArr2 = new int[3];
                    graphics.setColor(getBackground());
                    graphics.fillRect(0, 0, getWidth(), getHeight());
                    if (BasicSplitPaneDivider.this.orientation == 0) {
                        int iMin = Math.min(getHeight(), BasicSplitPaneDivider.this.oneTouchSize);
                        iArr[0] = iMin;
                        iArr[1] = iMin << 1;
                        iArr[2] = 0;
                        iArr2[0] = iMin;
                        iArr2[2] = 0;
                        iArr2[1] = 0;
                    } else {
                        int iMin2 = Math.min(getWidth(), BasicSplitPaneDivider.this.oneTouchSize);
                        iArr[2] = 0;
                        iArr[0] = 0;
                        iArr[1] = iMin2;
                        iArr2[0] = 0;
                        iArr2[1] = iMin2;
                        iArr2[2] = iMin2 << 1;
                    }
                    graphics.setColor(Color.black);
                    graphics.fillPolygon(iArr, iArr2, 3);
                }
            }

            @Override // java.awt.Component
            public boolean isFocusTraversable() {
                return false;
            }
        };
        jButton.setMinimumSize(new Dimension(this.oneTouchSize, this.oneTouchSize));
        jButton.setCursor(Cursor.getPredefinedCursor(0));
        jButton.setFocusPainted(false);
        jButton.setBorderPainted(false);
        jButton.setRequestFocusEnabled(false);
        return jButton;
    }

    protected void prepareForDragging() {
        this.splitPaneUI.startDragging();
    }

    protected void dragDividerTo(int i2) {
        this.splitPaneUI.dragDividerTo(i2);
    }

    protected void finishDraggingTo(int i2) {
        this.splitPaneUI.finishDraggingTo(i2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneDivider$MouseHandler.class */
    public class MouseHandler extends MouseAdapter implements MouseMotionListener {
        protected MouseHandler() {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if ((mouseEvent.getSource() == BasicSplitPaneDivider.this || mouseEvent.getSource() == BasicSplitPaneDivider.this.splitPane) && BasicSplitPaneDivider.this.dragger == null && BasicSplitPaneDivider.this.splitPane.isEnabled()) {
                Component nonContinuousLayoutDivider = BasicSplitPaneDivider.this.splitPaneUI.getNonContinuousLayoutDivider();
                if (BasicSplitPaneDivider.this.hiddenDivider != nonContinuousLayoutDivider) {
                    if (BasicSplitPaneDivider.this.hiddenDivider != null) {
                        BasicSplitPaneDivider.this.hiddenDivider.removeMouseListener(this);
                        BasicSplitPaneDivider.this.hiddenDivider.removeMouseMotionListener(this);
                    }
                    BasicSplitPaneDivider.this.hiddenDivider = nonContinuousLayoutDivider;
                    if (BasicSplitPaneDivider.this.hiddenDivider != null) {
                        BasicSplitPaneDivider.this.hiddenDivider.addMouseMotionListener(this);
                        BasicSplitPaneDivider.this.hiddenDivider.addMouseListener(this);
                    }
                }
                if (BasicSplitPaneDivider.this.splitPane.getLeftComponent() != null && BasicSplitPaneDivider.this.splitPane.getRightComponent() != null) {
                    if (BasicSplitPaneDivider.this.orientation == 1) {
                        BasicSplitPaneDivider.this.dragger = BasicSplitPaneDivider.this.new DragController(mouseEvent);
                    } else {
                        BasicSplitPaneDivider.this.dragger = BasicSplitPaneDivider.this.new VerticalDragController(mouseEvent);
                    }
                    if (!BasicSplitPaneDivider.this.dragger.isValid()) {
                        BasicSplitPaneDivider.this.dragger = null;
                    } else {
                        BasicSplitPaneDivider.this.prepareForDragging();
                        BasicSplitPaneDivider.this.dragger.continueDrag(mouseEvent);
                    }
                }
                mouseEvent.consume();
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (BasicSplitPaneDivider.this.dragger != null) {
                if (mouseEvent.getSource() == BasicSplitPaneDivider.this.splitPane) {
                    BasicSplitPaneDivider.this.dragger.completeDrag(mouseEvent.getX(), mouseEvent.getY());
                } else if (mouseEvent.getSource() == BasicSplitPaneDivider.this) {
                    Point location = BasicSplitPaneDivider.this.getLocation();
                    BasicSplitPaneDivider.this.dragger.completeDrag(mouseEvent.getX() + location.f12370x, mouseEvent.getY() + location.f12371y);
                } else if (mouseEvent.getSource() == BasicSplitPaneDivider.this.hiddenDivider) {
                    Point location2 = BasicSplitPaneDivider.this.hiddenDivider.getLocation();
                    BasicSplitPaneDivider.this.dragger.completeDrag(mouseEvent.getX() + location2.f12370x, mouseEvent.getY() + location2.f12371y);
                }
                BasicSplitPaneDivider.this.dragger = null;
                mouseEvent.consume();
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (BasicSplitPaneDivider.this.dragger != null) {
                if (mouseEvent.getSource() == BasicSplitPaneDivider.this.splitPane) {
                    BasicSplitPaneDivider.this.dragger.continueDrag(mouseEvent.getX(), mouseEvent.getY());
                } else if (mouseEvent.getSource() == BasicSplitPaneDivider.this) {
                    Point location = BasicSplitPaneDivider.this.getLocation();
                    BasicSplitPaneDivider.this.dragger.continueDrag(mouseEvent.getX() + location.f12370x, mouseEvent.getY() + location.f12371y);
                } else if (mouseEvent.getSource() == BasicSplitPaneDivider.this.hiddenDivider) {
                    Point location2 = BasicSplitPaneDivider.this.hiddenDivider.getLocation();
                    BasicSplitPaneDivider.this.dragger.continueDrag(mouseEvent.getX() + location2.f12370x, mouseEvent.getY() + location2.f12371y);
                }
                mouseEvent.consume();
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            if (mouseEvent.getSource() == BasicSplitPaneDivider.this) {
                BasicSplitPaneDivider.this.setMouseOver(true);
            }
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (mouseEvent.getSource() == BasicSplitPaneDivider.this) {
                BasicSplitPaneDivider.this.setMouseOver(false);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneDivider$DragController.class */
    public class DragController {
        int initialX;
        int maxX;
        int minX;
        int offset;

        protected DragController(MouseEvent mouseEvent) {
            JSplitPane splitPane = BasicSplitPaneDivider.this.splitPaneUI.getSplitPane();
            Component leftComponent = splitPane.getLeftComponent();
            Component rightComponent = splitPane.getRightComponent();
            this.initialX = BasicSplitPaneDivider.this.getLocation().f12370x;
            if (mouseEvent.getSource() == BasicSplitPaneDivider.this) {
                this.offset = mouseEvent.getX();
            } else {
                this.offset = mouseEvent.getX() - this.initialX;
            }
            if (leftComponent == null || rightComponent == null || this.offset < -1 || this.offset >= BasicSplitPaneDivider.this.getSize().width) {
                this.maxX = -1;
                return;
            }
            Insets insets = splitPane.getInsets();
            if (leftComponent.isVisible()) {
                this.minX = leftComponent.getMinimumSize().width;
                if (insets != null) {
                    this.minX += insets.left;
                }
            } else {
                this.minX = 0;
            }
            if (rightComponent.isVisible()) {
                this.maxX = Math.max(0, (splitPane.getSize().width - (BasicSplitPaneDivider.this.getSize().width + (insets != null ? insets.right : 0))) - rightComponent.getMinimumSize().width);
            } else {
                this.maxX = Math.max(0, splitPane.getSize().width - (BasicSplitPaneDivider.this.getSize().width + (insets != null ? insets.right : 0)));
            }
            if (this.maxX < this.minX) {
                this.maxX = 0;
                this.minX = 0;
            }
        }

        protected boolean isValid() {
            return this.maxX > 0;
        }

        protected int positionForMouseEvent(MouseEvent mouseEvent) {
            return Math.min(this.maxX, Math.max(this.minX, (mouseEvent.getSource() == BasicSplitPaneDivider.this ? mouseEvent.getX() + BasicSplitPaneDivider.this.getLocation().f12370x : mouseEvent.getX()) - this.offset));
        }

        protected int getNeededLocation(int i2, int i3) {
            return Math.min(this.maxX, Math.max(this.minX, i2 - this.offset));
        }

        protected void continueDrag(int i2, int i3) {
            BasicSplitPaneDivider.this.dragDividerTo(getNeededLocation(i2, i3));
        }

        protected void continueDrag(MouseEvent mouseEvent) {
            BasicSplitPaneDivider.this.dragDividerTo(positionForMouseEvent(mouseEvent));
        }

        protected void completeDrag(int i2, int i3) {
            BasicSplitPaneDivider.this.finishDraggingTo(getNeededLocation(i2, i3));
        }

        protected void completeDrag(MouseEvent mouseEvent) {
            BasicSplitPaneDivider.this.finishDraggingTo(positionForMouseEvent(mouseEvent));
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneDivider$VerticalDragController.class */
    protected class VerticalDragController extends DragController {
        protected VerticalDragController(MouseEvent mouseEvent) {
            super(mouseEvent);
            JSplitPane splitPane = BasicSplitPaneDivider.this.splitPaneUI.getSplitPane();
            Component leftComponent = splitPane.getLeftComponent();
            Component rightComponent = splitPane.getRightComponent();
            this.initialX = BasicSplitPaneDivider.this.getLocation().f12371y;
            if (mouseEvent.getSource() == BasicSplitPaneDivider.this) {
                this.offset = mouseEvent.getY();
            } else {
                this.offset = mouseEvent.getY() - this.initialX;
            }
            if (leftComponent == null || rightComponent == null || this.offset < -1 || this.offset > BasicSplitPaneDivider.this.getSize().height) {
                this.maxX = -1;
                return;
            }
            Insets insets = splitPane.getInsets();
            if (leftComponent.isVisible()) {
                this.minX = leftComponent.getMinimumSize().height;
                if (insets != null) {
                    this.minX += insets.top;
                }
            } else {
                this.minX = 0;
            }
            if (rightComponent.isVisible()) {
                this.maxX = Math.max(0, (splitPane.getSize().height - (BasicSplitPaneDivider.this.getSize().height + (insets != null ? insets.bottom : 0))) - rightComponent.getMinimumSize().height);
            } else {
                this.maxX = Math.max(0, splitPane.getSize().height - (BasicSplitPaneDivider.this.getSize().height + (insets != null ? insets.bottom : 0)));
            }
            if (this.maxX < this.minX) {
                this.maxX = 0;
                this.minX = 0;
            }
        }

        @Override // javax.swing.plaf.basic.BasicSplitPaneDivider.DragController
        protected int getNeededLocation(int i2, int i3) {
            return Math.min(this.maxX, Math.max(this.minX, i3 - this.offset));
        }

        @Override // javax.swing.plaf.basic.BasicSplitPaneDivider.DragController
        protected int positionForMouseEvent(MouseEvent mouseEvent) {
            return Math.min(this.maxX, Math.max(this.minX, (mouseEvent.getSource() == BasicSplitPaneDivider.this ? mouseEvent.getY() + BasicSplitPaneDivider.this.getLocation().f12371y : mouseEvent.getY()) - this.offset));
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneDivider$DividerLayout.class */
    protected class DividerLayout implements LayoutManager {
        protected DividerLayout() {
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            if (BasicSplitPaneDivider.this.leftButton != null && BasicSplitPaneDivider.this.rightButton != null && container == BasicSplitPaneDivider.this) {
                if (BasicSplitPaneDivider.this.splitPane.isOneTouchExpandable()) {
                    Insets insets = BasicSplitPaneDivider.this.getInsets();
                    if (BasicSplitPaneDivider.this.orientation == 0) {
                        int i2 = insets != null ? insets.left : 0;
                        int height = BasicSplitPaneDivider.this.getHeight();
                        if (insets != null) {
                            height = Math.max(height - (insets.top + insets.bottom), 0);
                        }
                        int iMin = Math.min(height, BasicSplitPaneDivider.this.oneTouchSize);
                        int i3 = (container.getSize().height - iMin) / 2;
                        if (!BasicSplitPaneDivider.this.centerOneTouchButtons) {
                            i3 = insets != null ? insets.top : 0;
                            i2 = 0;
                        }
                        BasicSplitPaneDivider.this.leftButton.setBounds(i2 + BasicSplitPaneDivider.this.oneTouchOffset, i3, iMin * 2, iMin);
                        BasicSplitPaneDivider.this.rightButton.setBounds(i2 + BasicSplitPaneDivider.this.oneTouchOffset + (BasicSplitPaneDivider.this.oneTouchSize * 2), i3, iMin * 2, iMin);
                        return;
                    }
                    int i4 = insets != null ? insets.top : 0;
                    int width = BasicSplitPaneDivider.this.getWidth();
                    if (insets != null) {
                        width = Math.max(width - (insets.left + insets.right), 0);
                    }
                    int iMin2 = Math.min(width, BasicSplitPaneDivider.this.oneTouchSize);
                    int i5 = (container.getSize().width - iMin2) / 2;
                    if (!BasicSplitPaneDivider.this.centerOneTouchButtons) {
                        i5 = insets != null ? insets.left : 0;
                        i4 = 0;
                    }
                    BasicSplitPaneDivider.this.leftButton.setBounds(i5, i4 + BasicSplitPaneDivider.this.oneTouchOffset, iMin2, iMin2 * 2);
                    BasicSplitPaneDivider.this.rightButton.setBounds(i5, i4 + BasicSplitPaneDivider.this.oneTouchOffset + (BasicSplitPaneDivider.this.oneTouchSize * 2), iMin2, iMin2 * 2);
                    return;
                }
                BasicSplitPaneDivider.this.leftButton.setBounds(-5, -5, 1, 1);
                BasicSplitPaneDivider.this.rightButton.setBounds(-5, -5, 1, 1);
            }
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            if (container != BasicSplitPaneDivider.this || BasicSplitPaneDivider.this.splitPane == null) {
                return new Dimension(0, 0);
            }
            Dimension minimumSize = null;
            if (BasicSplitPaneDivider.this.splitPane.isOneTouchExpandable() && BasicSplitPaneDivider.this.leftButton != null) {
                minimumSize = BasicSplitPaneDivider.this.leftButton.getMinimumSize();
            }
            Insets insets = BasicSplitPaneDivider.this.getInsets();
            int dividerSize = BasicSplitPaneDivider.this.getDividerSize();
            int iMax = dividerSize;
            if (BasicSplitPaneDivider.this.orientation == 0) {
                if (minimumSize != null) {
                    int i2 = minimumSize.height;
                    if (insets != null) {
                        i2 += insets.top + insets.bottom;
                    }
                    iMax = Math.max(iMax, i2);
                }
                dividerSize = 1;
            } else {
                if (minimumSize != null) {
                    int i3 = minimumSize.width;
                    if (insets != null) {
                        i3 += insets.left + insets.right;
                    }
                    dividerSize = Math.max(dividerSize, i3);
                }
                iMax = 1;
            }
            return new Dimension(dividerSize, iMax);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneDivider$OneTouchActionHandler.class */
    private class OneTouchActionHandler implements ActionListener {
        private boolean toMinimum;

        OneTouchActionHandler(boolean z2) {
            this.toMinimum = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            int width;
            Insets insets = BasicSplitPaneDivider.this.splitPane.getInsets();
            int lastDividerLocation = BasicSplitPaneDivider.this.splitPane.getLastDividerLocation();
            int dividerLocation = BasicSplitPaneDivider.this.splitPaneUI.getDividerLocation(BasicSplitPaneDivider.this.splitPane);
            if (this.toMinimum) {
                if (BasicSplitPaneDivider.this.orientation == 0) {
                    if (dividerLocation >= (BasicSplitPaneDivider.this.splitPane.getHeight() - insets.bottom) - BasicSplitPaneDivider.this.getHeight()) {
                        width = Math.min(lastDividerLocation, BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation());
                        BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
                    } else {
                        width = insets.top;
                        BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
                    }
                } else if (dividerLocation >= (BasicSplitPaneDivider.this.splitPane.getWidth() - insets.right) - BasicSplitPaneDivider.this.getWidth()) {
                    width = Math.min(lastDividerLocation, BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation());
                    BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
                } else {
                    width = insets.left;
                    BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
                }
            } else if (BasicSplitPaneDivider.this.orientation == 0) {
                if (dividerLocation == insets.top) {
                    width = Math.min(lastDividerLocation, BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation());
                    BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
                } else {
                    width = (BasicSplitPaneDivider.this.splitPane.getHeight() - BasicSplitPaneDivider.this.getHeight()) - insets.top;
                    BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
                }
            } else if (dividerLocation == insets.left) {
                width = Math.min(lastDividerLocation, BasicSplitPaneDivider.this.splitPane.getMaximumDividerLocation());
                BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(false);
            } else {
                width = (BasicSplitPaneDivider.this.splitPane.getWidth() - BasicSplitPaneDivider.this.getWidth()) - insets.left;
                BasicSplitPaneDivider.this.splitPaneUI.setKeepHidden(true);
            }
            if (dividerLocation != width) {
                BasicSplitPaneDivider.this.splitPane.setDividerLocation(width);
                BasicSplitPaneDivider.this.splitPane.setLastDividerLocation(dividerLocation);
            }
        }
    }
}
