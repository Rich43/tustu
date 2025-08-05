package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;

/* loaded from: rt.jar:javax/swing/ToolTipManager.class */
public class ToolTipManager extends MouseAdapter implements MouseMotionListener {
    Timer exitTimer;
    Timer insideTimer;
    String toolTipText;
    Point preferredLocation;
    JComponent insideComponent;
    MouseEvent mouseEvent;
    boolean showImmediately;
    private static final Object TOOL_TIP_MANAGER_KEY = new Object();
    transient Popup tipWindow;
    private Window window;
    JToolTip tip;
    private MouseMotionListener moveBeforeEnterListener;
    private KeyListener accessibilityKeyListener;
    private KeyStroke postTip;
    private KeyStroke hideTip;
    private Rectangle popupRect = null;
    private Rectangle popupFrameRect = null;
    boolean enabled = true;
    private boolean tipShowing = false;
    private FocusListener focusChangeListener = null;
    protected boolean lightWeightPopupEnabled = true;
    protected boolean heavyWeightPopupEnabled = false;
    Timer enterTimer = new Timer(750, new insideTimerAction());

    ToolTipManager() {
        this.moveBeforeEnterListener = null;
        this.accessibilityKeyListener = null;
        this.enterTimer.setRepeats(false);
        this.exitTimer = new Timer(500, new outsideTimerAction());
        this.exitTimer.setRepeats(false);
        this.insideTimer = new Timer(4000, new stillInsideTimerAction());
        this.insideTimer.setRepeats(false);
        this.moveBeforeEnterListener = new MoveBeforeEnterListener();
        this.accessibilityKeyListener = new AccessibilityKeyListener();
        this.postTip = KeyStroke.getKeyStroke(112, 2);
        this.hideTip = KeyStroke.getKeyStroke(27, 0);
    }

    public void setEnabled(boolean z2) {
        this.enabled = z2;
        if (!z2) {
            hideTipWindow();
        }
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setLightWeightPopupEnabled(boolean z2) {
        this.lightWeightPopupEnabled = z2;
    }

    public boolean isLightWeightPopupEnabled() {
        return this.lightWeightPopupEnabled;
    }

    public void setInitialDelay(int i2) {
        this.enterTimer.setInitialDelay(i2);
    }

    public int getInitialDelay() {
        return this.enterTimer.getInitialDelay();
    }

    public void setDismissDelay(int i2) {
        this.insideTimer.setInitialDelay(i2);
    }

    public int getDismissDelay() {
        return this.insideTimer.getInitialDelay();
    }

    public void setReshowDelay(int i2) {
        this.exitTimer.setInitialDelay(i2);
    }

    public int getReshowDelay() {
        return this.exitTimer.getInitialDelay();
    }

    private GraphicsConfiguration getDrawingGC(Point point) throws HeadlessException {
        for (GraphicsDevice graphicsDevice : GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()) {
            for (GraphicsConfiguration graphicsConfiguration : graphicsDevice.getConfigurations()) {
                if (graphicsConfiguration.getBounds().contains(point)) {
                    return graphicsConfiguration;
                }
            }
        }
        return null;
    }

    void showTipWindow() throws HeadlessException {
        Point locationOnScreen;
        Point point;
        if (this.insideComponent == null || !this.insideComponent.isShowing()) {
            return;
        }
        if ((!"activeApplication".equals(UIManager.getString("ToolTipManager.enableToolTipMode")) || KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow() != null) && this.enabled) {
            Point locationOnScreen2 = this.insideComponent.getLocationOnScreen();
            if (this.preferredLocation != null) {
                locationOnScreen = new Point(locationOnScreen2.f12370x + this.preferredLocation.f12370x, locationOnScreen2.f12371y + this.preferredLocation.f12371y);
            } else {
                locationOnScreen = this.mouseEvent.getLocationOnScreen();
            }
            GraphicsConfiguration drawingGC = getDrawingGC(locationOnScreen);
            if (drawingGC == null) {
                locationOnScreen = this.mouseEvent.getLocationOnScreen();
                drawingGC = getDrawingGC(locationOnScreen);
                if (drawingGC == null) {
                    drawingGC = this.insideComponent.getGraphicsConfiguration();
                }
            }
            Rectangle bounds = drawingGC.getBounds();
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(drawingGC);
            bounds.f12372x += screenInsets.left;
            bounds.f12373y += screenInsets.top;
            bounds.width -= screenInsets.left + screenInsets.right;
            bounds.height -= screenInsets.top + screenInsets.bottom;
            boolean zIsLeftToRight = SwingUtilities.isLeftToRight(this.insideComponent);
            hideTipWindow();
            this.tip = this.insideComponent.createToolTip();
            this.tip.setTipText(this.toolTipText);
            Dimension preferredSize = this.tip.getPreferredSize();
            if (this.preferredLocation != null) {
                point = locationOnScreen;
                if (!zIsLeftToRight) {
                    point.f12370x -= preferredSize.width;
                }
            } else {
                point = new Point(locationOnScreen2.f12370x + this.mouseEvent.getX(), locationOnScreen2.f12371y + this.mouseEvent.getY() + 20);
                if (!zIsLeftToRight && point.f12370x - preferredSize.width >= 0) {
                    point.f12370x -= preferredSize.width;
                }
            }
            if (this.popupRect == null) {
                this.popupRect = new Rectangle();
            }
            this.popupRect.setBounds(point.f12370x, point.f12371y, preferredSize.width, preferredSize.height);
            if (point.f12370x < bounds.f12372x) {
                point.f12370x = bounds.f12372x;
            } else if ((point.f12370x - bounds.f12372x) + preferredSize.width > bounds.width) {
                point.f12370x = bounds.f12372x + Math.max(0, bounds.width - preferredSize.width);
            }
            if (point.f12371y < bounds.f12373y) {
                point.f12371y = bounds.f12373y;
            } else if ((point.f12371y - bounds.f12373y) + preferredSize.height > bounds.height) {
                point.f12371y = bounds.f12373y + Math.max(0, bounds.height - preferredSize.height);
            }
            PopupFactory sharedInstance = PopupFactory.getSharedInstance();
            if (this.lightWeightPopupEnabled) {
                int popupFitHeight = getPopupFitHeight(this.popupRect, this.insideComponent);
                if (getPopupFitWidth(this.popupRect, this.insideComponent) > 0 || popupFitHeight > 0) {
                    sharedInstance.setPopupType(1);
                } else {
                    sharedInstance.setPopupType(0);
                }
            } else {
                sharedInstance.setPopupType(1);
            }
            this.tipWindow = sharedInstance.getPopup(this.insideComponent, this.tip, point.f12370x, point.f12371y);
            sharedInstance.setPopupType(0);
            this.tipWindow.show();
            Window windowWindowForComponent = SwingUtilities.windowForComponent(this.insideComponent);
            this.window = SwingUtilities.windowForComponent(this.tip);
            if (this.window != null && this.window != windowWindowForComponent) {
                this.window.addMouseListener(this);
            } else {
                this.window = null;
            }
            this.insideTimer.start();
            this.tipShowing = true;
        }
    }

    void hideTipWindow() {
        if (this.tipWindow != null) {
            if (this.window != null) {
                this.window.removeMouseListener(this);
                this.window = null;
            }
            this.tipWindow.hide();
            this.tipWindow = null;
            this.tipShowing = false;
            this.tip = null;
            this.insideTimer.stop();
        }
    }

    public static ToolTipManager sharedInstance() {
        Object objAppContextGet = SwingUtilities.appContextGet(TOOL_TIP_MANAGER_KEY);
        if (objAppContextGet instanceof ToolTipManager) {
            return (ToolTipManager) objAppContextGet;
        }
        ToolTipManager toolTipManager = new ToolTipManager();
        SwingUtilities.appContextPut(TOOL_TIP_MANAGER_KEY, toolTipManager);
        return toolTipManager;
    }

    public void registerComponent(JComponent jComponent) {
        jComponent.removeMouseListener(this);
        jComponent.addMouseListener(this);
        jComponent.removeMouseMotionListener(this.moveBeforeEnterListener);
        jComponent.addMouseMotionListener(this.moveBeforeEnterListener);
        if (jComponent instanceof JMenuItem) {
            ((JMenuItem) jComponent).removeMenuKeyListener((MenuKeyListener) this.accessibilityKeyListener);
            ((JMenuItem) jComponent).addMenuKeyListener((MenuKeyListener) this.accessibilityKeyListener);
        } else {
            jComponent.removeKeyListener(this.accessibilityKeyListener);
            jComponent.addKeyListener(this.accessibilityKeyListener);
        }
    }

    public void unregisterComponent(JComponent jComponent) {
        jComponent.removeMouseListener(this);
        jComponent.removeMouseMotionListener(this.moveBeforeEnterListener);
        if (jComponent instanceof JMenuItem) {
            ((JMenuItem) jComponent).removeMenuKeyListener((MenuKeyListener) this.accessibilityKeyListener);
        } else {
            jComponent.removeKeyListener(this.accessibilityKeyListener);
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseEntered(MouseEvent mouseEvent) throws HeadlessException {
        initiateToolTip(mouseEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initiateToolTip(MouseEvent mouseEvent) throws HeadlessException {
        boolean zEquals;
        if (mouseEvent.getSource() == this.window) {
            return;
        }
        JComponent jComponent = (JComponent) mouseEvent.getSource();
        jComponent.removeMouseMotionListener(this.moveBeforeEnterListener);
        this.exitTimer.stop();
        Point point = mouseEvent.getPoint();
        if (point.f12370x < 0 || point.f12370x >= jComponent.getWidth() || point.f12371y < 0 || point.f12371y >= jComponent.getHeight()) {
            return;
        }
        if (this.insideComponent != null) {
            this.enterTimer.stop();
        }
        jComponent.removeMouseMotionListener(this);
        jComponent.addMouseMotionListener(this);
        boolean z2 = this.insideComponent == jComponent;
        this.insideComponent = jComponent;
        if (this.tipWindow != null) {
            this.mouseEvent = mouseEvent;
            if (this.showImmediately) {
                String toolTipText = jComponent.getToolTipText(mouseEvent);
                Point toolTipLocation = jComponent.getToolTipLocation(mouseEvent);
                if (this.preferredLocation != null) {
                    zEquals = this.preferredLocation.equals(toolTipLocation);
                } else {
                    zEquals = toolTipLocation == null;
                }
                boolean z3 = zEquals;
                if (!z2 || !this.toolTipText.equals(toolTipText) || !z3) {
                    this.toolTipText = toolTipText;
                    this.preferredLocation = toolTipLocation;
                    showTipWindow();
                    return;
                }
                return;
            }
            this.enterTimer.start();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mouseExited(MouseEvent mouseEvent) {
        Window windowAncestor;
        boolean z2 = true;
        if (this.insideComponent == null) {
        }
        if (this.window != null && mouseEvent.getSource() == this.window && this.insideComponent != null) {
            Container topLevelAncestor = this.insideComponent.getTopLevelAncestor();
            if (topLevelAncestor != null) {
                Point point = mouseEvent.getPoint();
                SwingUtilities.convertPointToScreen(point, this.window);
                point.f12370x -= topLevelAncestor.getX();
                point.f12371y -= topLevelAncestor.getY();
                Point pointConvertPoint = SwingUtilities.convertPoint(null, point, this.insideComponent);
                z2 = pointConvertPoint.f12370x < 0 || pointConvertPoint.f12370x >= this.insideComponent.getWidth() || pointConvertPoint.f12371y < 0 || pointConvertPoint.f12371y >= this.insideComponent.getHeight();
            }
        } else if (mouseEvent.getSource() == this.insideComponent && this.tipWindow != null && (windowAncestor = SwingUtilities.getWindowAncestor(this.insideComponent)) != null) {
            Point pointConvertPoint2 = SwingUtilities.convertPoint(this.insideComponent, mouseEvent.getPoint(), windowAncestor);
            Rectangle bounds = this.insideComponent.getTopLevelAncestor().getBounds();
            pointConvertPoint2.f12370x += bounds.f12372x;
            pointConvertPoint2.f12371y += bounds.f12373y;
            Point point2 = new Point(0, 0);
            SwingUtilities.convertPointToScreen(point2, this.tip);
            bounds.f12372x = point2.f12370x;
            bounds.f12373y = point2.f12371y;
            bounds.width = this.tip.getWidth();
            bounds.height = this.tip.getHeight();
            z2 = pointConvertPoint2.f12370x < bounds.f12372x || pointConvertPoint2.f12370x >= bounds.f12372x + bounds.width || pointConvertPoint2.f12371y < bounds.f12373y || pointConvertPoint2.f12371y >= bounds.f12373y + bounds.height;
        }
        if (z2) {
            this.enterTimer.stop();
            if (this.insideComponent != null) {
                this.insideComponent.removeMouseMotionListener(this);
            }
            this.insideComponent = null;
            this.toolTipText = null;
            this.mouseEvent = null;
            hideTipWindow();
            this.exitTimer.restart();
        }
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
    public void mousePressed(MouseEvent mouseEvent) {
        hideTipWindow();
        this.enterTimer.stop();
        this.showImmediately = false;
        this.insideComponent = null;
        this.mouseEvent = null;
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseDragged(MouseEvent mouseEvent) {
    }

    @Override // java.awt.event.MouseAdapter, java.awt.event.MouseMotionListener
    public void mouseMoved(MouseEvent mouseEvent) throws HeadlessException {
        if (this.tipShowing) {
            checkForTipChange(mouseEvent);
            return;
        }
        if (this.showImmediately) {
            JComponent jComponent = (JComponent) mouseEvent.getSource();
            this.toolTipText = jComponent.getToolTipText(mouseEvent);
            if (this.toolTipText != null) {
                this.preferredLocation = jComponent.getToolTipLocation(mouseEvent);
                this.mouseEvent = mouseEvent;
                this.insideComponent = jComponent;
                this.exitTimer.stop();
                showTipWindow();
                return;
            }
            return;
        }
        this.insideComponent = (JComponent) mouseEvent.getSource();
        this.mouseEvent = mouseEvent;
        this.toolTipText = null;
        this.enterTimer.restart();
    }

    private void checkForTipChange(MouseEvent mouseEvent) throws HeadlessException {
        JComponent jComponent = (JComponent) mouseEvent.getSource();
        String toolTipText = jComponent.getToolTipText(mouseEvent);
        Point toolTipLocation = jComponent.getToolTipLocation(mouseEvent);
        if (toolTipText != null || toolTipLocation != null) {
            this.mouseEvent = mouseEvent;
            if (((toolTipText != null && toolTipText.equals(this.toolTipText)) || toolTipText == null) && ((toolTipLocation != null && toolTipLocation.equals(this.preferredLocation)) || toolTipLocation == null)) {
                if (this.tipWindow != null) {
                    this.insideTimer.restart();
                    return;
                } else {
                    this.enterTimer.restart();
                    return;
                }
            }
            this.toolTipText = toolTipText;
            this.preferredLocation = toolTipLocation;
            if (this.showImmediately) {
                hideTipWindow();
                showTipWindow();
                this.exitTimer.stop();
                return;
            }
            this.enterTimer.restart();
            return;
        }
        this.toolTipText = null;
        this.preferredLocation = null;
        this.mouseEvent = null;
        this.insideComponent = null;
        hideTipWindow();
        this.enterTimer.stop();
        this.exitTimer.restart();
    }

    /* loaded from: rt.jar:javax/swing/ToolTipManager$insideTimerAction.class */
    protected class insideTimerAction implements ActionListener {
        protected insideTimerAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws HeadlessException {
            if (ToolTipManager.this.insideComponent != null && ToolTipManager.this.insideComponent.isShowing()) {
                if (ToolTipManager.this.toolTipText == null && ToolTipManager.this.mouseEvent != null) {
                    ToolTipManager.this.toolTipText = ToolTipManager.this.insideComponent.getToolTipText(ToolTipManager.this.mouseEvent);
                    ToolTipManager.this.preferredLocation = ToolTipManager.this.insideComponent.getToolTipLocation(ToolTipManager.this.mouseEvent);
                }
                if (ToolTipManager.this.toolTipText != null) {
                    ToolTipManager.this.showImmediately = true;
                    ToolTipManager.this.showTipWindow();
                    return;
                }
                ToolTipManager.this.insideComponent = null;
                ToolTipManager.this.toolTipText = null;
                ToolTipManager.this.preferredLocation = null;
                ToolTipManager.this.mouseEvent = null;
                ToolTipManager.this.hideTipWindow();
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/ToolTipManager$outsideTimerAction.class */
    protected class outsideTimerAction implements ActionListener {
        protected outsideTimerAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            ToolTipManager.this.showImmediately = false;
        }
    }

    /* loaded from: rt.jar:javax/swing/ToolTipManager$stillInsideTimerAction.class */
    protected class stillInsideTimerAction implements ActionListener {
        protected stillInsideTimerAction() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            ToolTipManager.this.hideTipWindow();
            ToolTipManager.this.enterTimer.stop();
            ToolTipManager.this.showImmediately = false;
            ToolTipManager.this.insideComponent = null;
            ToolTipManager.this.mouseEvent = null;
        }
    }

    /* loaded from: rt.jar:javax/swing/ToolTipManager$MoveBeforeEnterListener.class */
    private class MoveBeforeEnterListener extends MouseMotionAdapter {
        private MoveBeforeEnterListener() {
        }

        @Override // java.awt.event.MouseMotionAdapter, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) throws HeadlessException {
            ToolTipManager.this.initiateToolTip(mouseEvent);
        }
    }

    static Frame frameForComponent(Component component) {
        while (!(component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }

    private FocusListener createFocusChangeListener() {
        return new FocusAdapter() { // from class: javax.swing.ToolTipManager.1
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusLost(FocusEvent focusEvent) {
                ToolTipManager.this.hideTipWindow();
                ToolTipManager.this.insideComponent = null;
                ((JComponent) focusEvent.getSource()).removeFocusListener(ToolTipManager.this.focusChangeListener);
            }
        };
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002b, code lost:
    
        return getWidthAdjust(r9.getBounds(), r7);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getPopupFitWidth(java.awt.Rectangle r7, java.awt.Component r8) {
        /*
            r6 = this;
            r0 = r8
            if (r0 == 0) goto L83
            r0 = r8
            java.awt.Container r0 = r0.getParent()
            r9 = r0
        L9:
            r0 = r9
            if (r0 == 0) goto L83
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JFrame
            if (r0 != 0) goto L22
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JDialog
            if (r0 != 0) goto L22
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JWindow
            if (r0 == 0) goto L2c
        L22:
            r0 = r6
            r1 = r9
            java.awt.Rectangle r1 = r1.getBounds()
            r2 = r7
            int r0 = r0.getWidthAdjust(r1, r2)
            return r0
        L2c:
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JApplet
            if (r0 != 0) goto L3a
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JInternalFrame
            if (r0 == 0) goto L7b
        L3a:
            r0 = r6
            java.awt.Rectangle r0 = r0.popupFrameRect
            if (r0 != 0) goto L4c
            r0 = r6
            java.awt.Rectangle r1 = new java.awt.Rectangle
            r2 = r1
            r2.<init>()
            r0.popupFrameRect = r1
        L4c:
            r0 = r9
            java.awt.Point r0 = r0.getLocationOnScreen()
            r10 = r0
            r0 = r6
            java.awt.Rectangle r0 = r0.popupFrameRect
            r1 = r10
            int r1 = r1.f12370x
            r2 = r10
            int r2 = r2.f12371y
            r3 = r9
            java.awt.Rectangle r3 = r3.getBounds()
            int r3 = r3.width
            r4 = r9
            java.awt.Rectangle r4 = r4.getBounds()
            int r4 = r4.height
            r0.setBounds(r1, r2, r3, r4)
            r0 = r6
            r1 = r6
            java.awt.Rectangle r1 = r1.popupFrameRect
            r2 = r7
            int r0 = r0.getWidthAdjust(r1, r2)
            return r0
        L7b:
            r0 = r9
            java.awt.Container r0 = r0.getParent()
            r9 = r0
            goto L9
        L83:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.ToolTipManager.getPopupFitWidth(java.awt.Rectangle, java.awt.Component):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x002b, code lost:
    
        return getHeightAdjust(r9.getBounds(), r7);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getPopupFitHeight(java.awt.Rectangle r7, java.awt.Component r8) {
        /*
            r6 = this;
            r0 = r8
            if (r0 == 0) goto L83
            r0 = r8
            java.awt.Container r0 = r0.getParent()
            r9 = r0
        L9:
            r0 = r9
            if (r0 == 0) goto L83
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JFrame
            if (r0 != 0) goto L22
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JDialog
            if (r0 != 0) goto L22
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JWindow
            if (r0 == 0) goto L2c
        L22:
            r0 = r6
            r1 = r9
            java.awt.Rectangle r1 = r1.getBounds()
            r2 = r7
            int r0 = r0.getHeightAdjust(r1, r2)
            return r0
        L2c:
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JApplet
            if (r0 != 0) goto L3a
            r0 = r9
            boolean r0 = r0 instanceof javax.swing.JInternalFrame
            if (r0 == 0) goto L7b
        L3a:
            r0 = r6
            java.awt.Rectangle r0 = r0.popupFrameRect
            if (r0 != 0) goto L4c
            r0 = r6
            java.awt.Rectangle r1 = new java.awt.Rectangle
            r2 = r1
            r2.<init>()
            r0.popupFrameRect = r1
        L4c:
            r0 = r9
            java.awt.Point r0 = r0.getLocationOnScreen()
            r10 = r0
            r0 = r6
            java.awt.Rectangle r0 = r0.popupFrameRect
            r1 = r10
            int r1 = r1.f12370x
            r2 = r10
            int r2 = r2.f12371y
            r3 = r9
            java.awt.Rectangle r3 = r3.getBounds()
            int r3 = r3.width
            r4 = r9
            java.awt.Rectangle r4 = r4.getBounds()
            int r4 = r4.height
            r0.setBounds(r1, r2, r3, r4)
            r0 = r6
            r1 = r6
            java.awt.Rectangle r1 = r1.popupFrameRect
            r2 = r7
            int r0 = r0.getHeightAdjust(r1, r2)
            return r0
        L7b:
            r0 = r9
            java.awt.Container r0 = r0.getParent()
            r9 = r0
            goto L9
        L83:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.ToolTipManager.getPopupFitHeight(java.awt.Rectangle, java.awt.Component):int");
    }

    private int getHeightAdjust(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2.f12373y >= rectangle.f12373y && rectangle2.f12373y + rectangle2.height <= rectangle.f12373y + rectangle.height) {
            return 0;
        }
        return ((rectangle2.f12373y + rectangle2.height) - (rectangle.f12373y + rectangle.height)) + 5;
    }

    private int getWidthAdjust(Rectangle rectangle, Rectangle rectangle2) {
        if (rectangle2.f12372x >= rectangle.f12372x && rectangle2.f12372x + rectangle2.width <= rectangle.f12372x + rectangle.width) {
            return 0;
        }
        return ((rectangle2.f12372x + rectangle2.width) - (rectangle.f12372x + rectangle.width)) + 5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void show(JComponent jComponent) throws HeadlessException {
        if (this.tipWindow != null) {
            hideTipWindow();
            this.insideComponent = null;
            return;
        }
        hideTipWindow();
        this.enterTimer.stop();
        this.exitTimer.stop();
        this.insideTimer.stop();
        this.insideComponent = jComponent;
        if (this.insideComponent != null) {
            this.toolTipText = this.insideComponent.getToolTipText();
            this.preferredLocation = new Point(10, this.insideComponent.getHeight() + 10);
            showTipWindow();
            if (this.focusChangeListener == null) {
                this.focusChangeListener = createFocusChangeListener();
            }
            this.insideComponent.addFocusListener(this.focusChangeListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void hide(JComponent jComponent) {
        hideTipWindow();
        jComponent.removeFocusListener(this.focusChangeListener);
        this.preferredLocation = null;
        this.insideComponent = null;
    }

    /* loaded from: rt.jar:javax/swing/ToolTipManager$AccessibilityKeyListener.class */
    private class AccessibilityKeyListener extends KeyAdapter implements MenuKeyListener {
        private AccessibilityKeyListener() {
        }

        @Override // java.awt.event.KeyAdapter, java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) throws HeadlessException {
            if (!keyEvent.isConsumed()) {
                JComponent jComponent = (JComponent) keyEvent.getComponent();
                KeyStroke keyStrokeForEvent = KeyStroke.getKeyStrokeForEvent(keyEvent);
                if (!ToolTipManager.this.hideTip.equals(keyStrokeForEvent)) {
                    if (ToolTipManager.this.postTip.equals(keyStrokeForEvent)) {
                        ToolTipManager.this.show(jComponent);
                        keyEvent.consume();
                        return;
                    }
                    return;
                }
                if (ToolTipManager.this.tipWindow != null) {
                    ToolTipManager.this.hide(jComponent);
                    keyEvent.consume();
                }
            }
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyPressed(MenuKeyEvent menuKeyEvent) throws HeadlessException {
            if (ToolTipManager.this.postTip.equals(KeyStroke.getKeyStrokeForEvent(menuKeyEvent))) {
                MenuElement[] path = menuKeyEvent.getPath();
                MenuElement menuElement = path[path.length - 1];
                MenuElement[] selectedPath = menuKeyEvent.getMenuSelectionManager().getSelectedPath();
                if (menuElement.equals(selectedPath[selectedPath.length - 1])) {
                    ToolTipManager.this.show((JComponent) menuElement.getComponent());
                    menuKeyEvent.consume();
                }
            }
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
        }
    }
}
