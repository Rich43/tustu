package javax.swing.plaf.basic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import javax.swing.AbstractButton;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicBorders;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI.class */
public class BasicToolBarUI extends ToolBarUI implements SwingConstants {
    protected JToolBar toolBar;
    private boolean floating;
    private int floatingX;
    private int floatingY;
    private JFrame floatingFrame;
    private RootPaneContainer floatingToolBar;
    protected DragWindow dragWindow;
    private Container dockingSource;
    protected MouseInputListener dockingListener;
    protected PropertyChangeListener propertyListener;
    protected ContainerListener toolBarContListener;
    protected FocusListener toolBarFocusListener;
    private Handler handler;
    private static Border rolloverBorder;
    private static Border nonRolloverBorder;
    private static Border nonRolloverToggleBorder;

    @Deprecated
    protected KeyStroke upKey;

    @Deprecated
    protected KeyStroke downKey;

    @Deprecated
    protected KeyStroke leftKey;

    @Deprecated
    protected KeyStroke rightKey;
    private static String IS_ROLLOVER = "JToolBar.isRollover";
    private static String FOCUSED_COMP_INDEX = "JToolBar.focusedCompIndex";
    private int dockingSensitivity = 0;
    protected int focusedCompIndex = -1;
    protected Color dockingColor = null;
    protected Color floatingColor = null;
    protected Color dockingBorderColor = null;
    protected Color floatingBorderColor = null;
    protected String constraintBeforeFloating = "North";
    private boolean rolloverBorders = false;
    private HashMap<AbstractButton, Border> borderTable = new HashMap<>();
    private Hashtable<AbstractButton, Boolean> rolloverTable = new Hashtable<>();

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicToolBarUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.toolBar = (JToolBar) jComponent;
        installDefaults();
        installComponents();
        installListeners();
        installKeyboardActions();
        this.dockingSensitivity = 0;
        this.floating = false;
        this.floatingY = 0;
        this.floatingX = 0;
        this.floatingToolBar = null;
        setOrientation(this.toolBar.getOrientation());
        LookAndFeel.installProperty(jComponent, "opaque", Boolean.TRUE);
        if (jComponent.getClientProperty(FOCUSED_COMP_INDEX) != null) {
            this.focusedCompIndex = ((Integer) jComponent.getClientProperty(FOCUSED_COMP_INDEX)).intValue();
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults();
        uninstallComponents();
        uninstallListeners();
        uninstallKeyboardActions();
        if (isFloating()) {
            setFloating(false, null);
        }
        this.floatingToolBar = null;
        this.dragWindow = null;
        this.dockingSource = null;
        jComponent.putClientProperty(FOCUSED_COMP_INDEX, Integer.valueOf(this.focusedCompIndex));
    }

    protected void installDefaults() {
        LookAndFeel.installBorder(this.toolBar, "ToolBar.border");
        LookAndFeel.installColorsAndFont(this.toolBar, "ToolBar.background", "ToolBar.foreground", "ToolBar.font");
        if (this.dockingColor == null || (this.dockingColor instanceof UIResource)) {
            this.dockingColor = UIManager.getColor("ToolBar.dockingBackground");
        }
        if (this.floatingColor == null || (this.floatingColor instanceof UIResource)) {
            this.floatingColor = UIManager.getColor("ToolBar.floatingBackground");
        }
        if (this.dockingBorderColor == null || (this.dockingBorderColor instanceof UIResource)) {
            this.dockingBorderColor = UIManager.getColor("ToolBar.dockingForeground");
        }
        if (this.floatingBorderColor == null || (this.floatingBorderColor instanceof UIResource)) {
            this.floatingBorderColor = UIManager.getColor("ToolBar.floatingForeground");
        }
        Object clientProperty = this.toolBar.getClientProperty(IS_ROLLOVER);
        if (clientProperty == null) {
            clientProperty = UIManager.get("ToolBar.isRollover");
        }
        if (clientProperty != null) {
            this.rolloverBorders = ((Boolean) clientProperty).booleanValue();
        }
        if (rolloverBorder == null) {
            rolloverBorder = createRolloverBorder();
        }
        if (nonRolloverBorder == null) {
            nonRolloverBorder = createNonRolloverBorder();
        }
        if (nonRolloverToggleBorder == null) {
            nonRolloverToggleBorder = createNonRolloverToggleBorder();
        }
        setRolloverBorders(isRolloverBorders());
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.toolBar);
        this.dockingColor = null;
        this.floatingColor = null;
        this.dockingBorderColor = null;
        this.floatingBorderColor = null;
        installNormalBorders(this.toolBar);
        rolloverBorder = null;
        nonRolloverBorder = null;
        nonRolloverToggleBorder = null;
    }

    protected void installComponents() {
    }

    protected void uninstallComponents() {
    }

    protected void installListeners() {
        this.dockingListener = createDockingListener();
        if (this.dockingListener != null) {
            this.toolBar.addMouseMotionListener(this.dockingListener);
            this.toolBar.addMouseListener(this.dockingListener);
        }
        this.propertyListener = createPropertyListener();
        if (this.propertyListener != null) {
            this.toolBar.addPropertyChangeListener(this.propertyListener);
        }
        this.toolBarContListener = createToolBarContListener();
        if (this.toolBarContListener != null) {
            this.toolBar.addContainerListener(this.toolBarContListener);
        }
        this.toolBarFocusListener = createToolBarFocusListener();
        if (this.toolBarFocusListener != null) {
            for (Component component : this.toolBar.getComponents()) {
                component.addFocusListener(this.toolBarFocusListener);
            }
        }
    }

    protected void uninstallListeners() {
        if (this.dockingListener != null) {
            this.toolBar.removeMouseMotionListener(this.dockingListener);
            this.toolBar.removeMouseListener(this.dockingListener);
            this.dockingListener = null;
        }
        if (this.propertyListener != null) {
            this.toolBar.removePropertyChangeListener(this.propertyListener);
            this.propertyListener = null;
        }
        if (this.toolBarContListener != null) {
            this.toolBar.removeContainerListener(this.toolBarContListener);
            this.toolBarContListener = null;
        }
        if (this.toolBarFocusListener != null) {
            for (Component component : this.toolBar.getComponents()) {
                component.removeFocusListener(this.toolBarFocusListener);
            }
            this.toolBarFocusListener = null;
        }
        this.handler = null;
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.toolBar, 1, getInputMap(1));
        LazyActionMap.installLazyActionMap(this.toolBar, BasicToolBarUI.class, "ToolBar.actionMap");
    }

    InputMap getInputMap(int i2) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.toolBar, this, "ToolBar.ancestorInputMap");
        }
        return null;
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("navigateRight"));
        lazyActionMap.put(new Actions("navigateLeft"));
        lazyActionMap.put(new Actions("navigateUp"));
        lazyActionMap.put(new Actions("navigateDown"));
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.toolBar, null);
        SwingUtilities.replaceUIInputMap(this.toolBar, 1, null);
    }

    protected void navigateFocusedComp(int i2) {
        int componentCount = this.toolBar.getComponentCount();
        switch (i2) {
            case 1:
            case 7:
                if (this.focusedCompIndex >= 0 && this.focusedCompIndex < componentCount) {
                    int i3 = this.focusedCompIndex - 1;
                    while (i3 != this.focusedCompIndex) {
                        if (i3 < 0) {
                            i3 = componentCount - 1;
                        }
                        int i4 = i3;
                        i3--;
                        Component componentAtIndex = this.toolBar.getComponentAtIndex(i4);
                        if (componentAtIndex != null && componentAtIndex.isFocusTraversable() && componentAtIndex.isEnabled()) {
                            componentAtIndex.requestFocus();
                            break;
                        }
                    }
                    break;
                }
                break;
            case 3:
            case 5:
                if (this.focusedCompIndex >= 0 && this.focusedCompIndex < componentCount) {
                    int i5 = this.focusedCompIndex + 1;
                    while (i5 != this.focusedCompIndex) {
                        if (i5 >= componentCount) {
                            i5 = 0;
                        }
                        int i6 = i5;
                        i5++;
                        Component componentAtIndex2 = this.toolBar.getComponentAtIndex(i6);
                        if (componentAtIndex2 != null && componentAtIndex2.isFocusTraversable() && componentAtIndex2.isEnabled()) {
                            componentAtIndex2.requestFocus();
                            break;
                        }
                    }
                    break;
                }
                break;
        }
    }

    protected Border createRolloverBorder() {
        Object obj = UIManager.get("ToolBar.rolloverBorder");
        if (obj != null) {
            return (Border) obj;
        }
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new CompoundBorder(new BasicBorders.RolloverButtonBorder(lookAndFeelDefaults.getColor("controlShadow"), lookAndFeelDefaults.getColor("controlDkShadow"), lookAndFeelDefaults.getColor("controlHighlight"), lookAndFeelDefaults.getColor("controlLtHighlight")), new BasicBorders.RolloverMarginBorder());
    }

    protected Border createNonRolloverBorder() {
        Object obj = UIManager.get("ToolBar.nonrolloverBorder");
        if (obj != null) {
            return (Border) obj;
        }
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new CompoundBorder(new BasicBorders.ButtonBorder(lookAndFeelDefaults.getColor("Button.shadow"), lookAndFeelDefaults.getColor("Button.darkShadow"), lookAndFeelDefaults.getColor("Button.light"), lookAndFeelDefaults.getColor("Button.highlight")), new BasicBorders.RolloverMarginBorder());
    }

    private Border createNonRolloverToggleBorder() {
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        return new CompoundBorder(new BasicBorders.RadioButtonBorder(lookAndFeelDefaults.getColor("ToggleButton.shadow"), lookAndFeelDefaults.getColor("ToggleButton.darkShadow"), lookAndFeelDefaults.getColor("ToggleButton.light"), lookAndFeelDefaults.getColor("ToggleButton.highlight")), new BasicBorders.RolloverMarginBorder());
    }

    protected JFrame createFloatingFrame(JToolBar jToolBar) {
        Window windowAncestor = SwingUtilities.getWindowAncestor(jToolBar);
        JFrame jFrame = new JFrame(jToolBar.getName(), windowAncestor != null ? windowAncestor.getGraphicsConfiguration() : null) { // from class: javax.swing.plaf.basic.BasicToolBarUI.1
            @Override // javax.swing.JFrame
            protected JRootPane createRootPane() {
                JRootPane jRootPane = new JRootPane() { // from class: javax.swing.plaf.basic.BasicToolBarUI.1.1
                    private boolean packing = false;

                    @Override // java.awt.Container, java.awt.Component
                    public void validate() {
                        super.validate();
                        if (!this.packing) {
                            this.packing = true;
                            pack();
                            this.packing = false;
                        }
                    }
                };
                jRootPane.setOpaque(true);
                return jRootPane;
            }
        };
        jFrame.getRootPane().setName("ToolBar.FloatingFrame");
        jFrame.setResizable(false);
        jFrame.addWindowListener(createFrameListener());
        return jFrame;
    }

    /* renamed from: javax.swing.plaf.basic.BasicToolBarUI$1ToolBarDialog, reason: invalid class name */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$1ToolBarDialog.class */
    class C1ToolBarDialog extends JDialog {
        public C1ToolBarDialog(Frame frame, String str, boolean z2) {
            super(frame, str, z2);
        }

        public C1ToolBarDialog(Dialog dialog, String str, boolean z2) {
            super(dialog, str, z2);
        }

        @Override // javax.swing.JDialog
        protected JRootPane createRootPane() {
            JRootPane jRootPane = new JRootPane() { // from class: javax.swing.plaf.basic.BasicToolBarUI.1ToolBarDialog.1
                private boolean packing = false;

                @Override // java.awt.Container, java.awt.Component
                public void validate() {
                    super.validate();
                    if (!this.packing) {
                        this.packing = true;
                        C1ToolBarDialog.this.pack();
                        this.packing = false;
                    }
                }
            };
            jRootPane.setOpaque(true);
            return jRootPane;
        }
    }

    protected RootPaneContainer createFloatingWindow(JToolBar jToolBar) {
        C1ToolBarDialog c1ToolBarDialog;
        Window windowAncestor = SwingUtilities.getWindowAncestor(jToolBar);
        if (windowAncestor instanceof Frame) {
            c1ToolBarDialog = new C1ToolBarDialog((Frame) windowAncestor, jToolBar.getName(), false);
        } else if (windowAncestor instanceof Dialog) {
            c1ToolBarDialog = new C1ToolBarDialog((Dialog) windowAncestor, jToolBar.getName(), false);
        } else {
            c1ToolBarDialog = new C1ToolBarDialog((Frame) null, jToolBar.getName(), false);
        }
        c1ToolBarDialog.getRootPane().setName("ToolBar.FloatingWindow");
        c1ToolBarDialog.setTitle(jToolBar.getName());
        c1ToolBarDialog.setResizable(false);
        c1ToolBarDialog.addWindowListener(createFrameListener());
        return c1ToolBarDialog;
    }

    protected DragWindow createDragWindow(JToolBar jToolBar) {
        Window window;
        Window window2 = null;
        if (this.toolBar != null) {
            Container parent = this.toolBar.getParent();
            while (true) {
                window = parent;
                if (window == null || (window instanceof Window)) {
                    break;
                }
                parent = window.getParent();
            }
            if (window != null && (window instanceof Window)) {
                window2 = window;
            }
        }
        if (this.floatingToolBar == null) {
            this.floatingToolBar = createFloatingWindow(this.toolBar);
        }
        if (this.floatingToolBar instanceof Window) {
            window2 = (Window) this.floatingToolBar;
        }
        return new DragWindow(window2);
    }

    public boolean isRolloverBorders() {
        return this.rolloverBorders;
    }

    public void setRolloverBorders(boolean z2) {
        this.rolloverBorders = z2;
        if (this.rolloverBorders) {
            installRolloverBorders(this.toolBar);
        } else {
            installNonRolloverBorders(this.toolBar);
        }
    }

    protected void installRolloverBorders(JComponent jComponent) {
        for (Component component : jComponent.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).updateUI();
                setBorderToRollover(component);
            }
        }
    }

    protected void installNonRolloverBorders(JComponent jComponent) {
        for (Component component : jComponent.getComponents()) {
            if (component instanceof JComponent) {
                ((JComponent) component).updateUI();
                setBorderToNonRollover(component);
            }
        }
    }

    protected void installNormalBorders(JComponent jComponent) {
        for (Component component : jComponent.getComponents()) {
            setBorderToNormal(component);
        }
    }

    protected void setBorderToRollover(Component component) {
        if (component instanceof AbstractButton) {
            AbstractButton abstractButton = (AbstractButton) component;
            Border border = this.borderTable.get(abstractButton);
            if (border == null || (border instanceof UIResource)) {
                this.borderTable.put(abstractButton, abstractButton.getBorder());
            }
            if (abstractButton.getBorder() instanceof UIResource) {
                abstractButton.setBorder(getRolloverBorder(abstractButton));
            }
            this.rolloverTable.put(abstractButton, abstractButton.isRolloverEnabled() ? Boolean.TRUE : Boolean.FALSE);
            abstractButton.setRolloverEnabled(true);
        }
    }

    protected Border getRolloverBorder(AbstractButton abstractButton) {
        return rolloverBorder;
    }

    protected void setBorderToNonRollover(Component component) {
        if (component instanceof AbstractButton) {
            AbstractButton abstractButton = (AbstractButton) component;
            Border border = this.borderTable.get(abstractButton);
            if (border == null || (border instanceof UIResource)) {
                this.borderTable.put(abstractButton, abstractButton.getBorder());
            }
            if (abstractButton.getBorder() instanceof UIResource) {
                abstractButton.setBorder(getNonRolloverBorder(abstractButton));
            }
            this.rolloverTable.put(abstractButton, abstractButton.isRolloverEnabled() ? Boolean.TRUE : Boolean.FALSE);
            abstractButton.setRolloverEnabled(false);
        }
    }

    protected Border getNonRolloverBorder(AbstractButton abstractButton) {
        if (abstractButton instanceof JToggleButton) {
            return nonRolloverToggleBorder;
        }
        return nonRolloverBorder;
    }

    protected void setBorderToNormal(Component component) {
        if (component instanceof AbstractButton) {
            AbstractButton abstractButton = (AbstractButton) component;
            abstractButton.setBorder(this.borderTable.remove(abstractButton));
            Boolean boolRemove = this.rolloverTable.remove(abstractButton);
            if (boolRemove != null) {
                abstractButton.setRolloverEnabled(boolRemove.booleanValue());
            }
        }
    }

    public void setFloatingLocation(int i2, int i3) {
        this.floatingX = i2;
        this.floatingY = i3;
    }

    public boolean isFloating() {
        return this.floating;
    }

    public void setFloating(boolean z2, Point point) {
        if (this.toolBar.isFloatable()) {
            boolean zIsVisible = false;
            Window windowAncestor = SwingUtilities.getWindowAncestor(this.toolBar);
            if (windowAncestor != null) {
                zIsVisible = windowAncestor.isVisible();
            }
            if (this.dragWindow != null) {
                this.dragWindow.setVisible(false);
            }
            this.floating = z2;
            if (this.floatingToolBar == null) {
                this.floatingToolBar = createFloatingWindow(this.toolBar);
            }
            if (z2) {
                if (this.dockingSource == null) {
                    this.dockingSource = this.toolBar.getParent();
                    this.dockingSource.remove(this.toolBar);
                }
                this.constraintBeforeFloating = calculateConstraint();
                if (this.propertyListener != null) {
                    UIManager.addPropertyChangeListener(this.propertyListener);
                }
                this.floatingToolBar.getContentPane().add(this.toolBar, BorderLayout.CENTER);
                if (this.floatingToolBar instanceof Window) {
                    ((Window) this.floatingToolBar).pack();
                    ((Window) this.floatingToolBar).setLocation(this.floatingX, this.floatingY);
                    if (zIsVisible) {
                        ((Window) this.floatingToolBar).show();
                    } else {
                        windowAncestor.addWindowListener(new WindowAdapter() { // from class: javax.swing.plaf.basic.BasicToolBarUI.2
                            @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
                            public void windowOpened(WindowEvent windowEvent) {
                                ((Window) BasicToolBarUI.this.floatingToolBar).show();
                            }
                        });
                    }
                }
            } else {
                if (this.floatingToolBar == null) {
                    this.floatingToolBar = createFloatingWindow(this.toolBar);
                }
                if (this.floatingToolBar instanceof Window) {
                    ((Window) this.floatingToolBar).setVisible(false);
                }
                this.floatingToolBar.getContentPane().remove(this.toolBar);
                String dockingConstraint = getDockingConstraint(this.dockingSource, point);
                if (dockingConstraint == null) {
                    dockingConstraint = "North";
                }
                setOrientation(mapConstraintToOrientation(dockingConstraint));
                if (this.dockingSource == null) {
                    this.dockingSource = this.toolBar.getParent();
                }
                if (this.propertyListener != null) {
                    UIManager.removePropertyChangeListener(this.propertyListener);
                }
                this.dockingSource.add(dockingConstraint, this.toolBar);
            }
            this.dockingSource.invalidate();
            Container parent = this.dockingSource.getParent();
            if (parent != null) {
                parent.validate();
            }
            this.dockingSource.repaint();
        }
    }

    private int mapConstraintToOrientation(String str) {
        int orientation = this.toolBar.getOrientation();
        if (str != null) {
            if (str.equals("East") || str.equals("West")) {
                orientation = 1;
            } else if (str.equals("North") || str.equals("South")) {
                orientation = 0;
            }
        }
        return orientation;
    }

    public void setOrientation(int i2) {
        this.toolBar.setOrientation(i2);
        if (this.dragWindow != null) {
            this.dragWindow.setOrientation(i2);
        }
    }

    public Color getDockingColor() {
        return this.dockingColor;
    }

    public void setDockingColor(Color color) {
        this.dockingColor = color;
    }

    public Color getFloatingColor() {
        return this.floatingColor;
    }

    public void setFloatingColor(Color color) {
        this.floatingColor = color;
    }

    private boolean isBlocked(Component component, Object obj) {
        Component layoutComponent;
        if (component instanceof Container) {
            Container container = (Container) component;
            LayoutManager layout = container.getLayout();
            return (!(layout instanceof BorderLayout) || (layoutComponent = ((BorderLayout) layout).getLayoutComponent(container, obj)) == null || layoutComponent == this.toolBar) ? false : true;
        }
        return false;
    }

    public boolean canDock(Component component, Point point) {
        return (point == null || getDockingConstraint(component, point) == null) ? false : true;
    }

    private String calculateConstraint() {
        String str = null;
        LayoutManager layout = this.dockingSource.getLayout();
        if (layout instanceof BorderLayout) {
            str = (String) ((BorderLayout) layout).getConstraints(this.toolBar);
        }
        return str != null ? str : this.constraintBeforeFloating;
    }

    private String getDockingConstraint(Component component, Point point) {
        int i2;
        if (point == null) {
            return this.constraintBeforeFloating;
        }
        if (component.contains(point)) {
            if (this.toolBar.getOrientation() == 0) {
                i2 = this.toolBar.getSize().height;
            } else {
                i2 = this.toolBar.getSize().width;
            }
            this.dockingSensitivity = i2;
            if (point.f12371y < this.dockingSensitivity && !isBlocked(component, "North")) {
                return "North";
            }
            if (point.f12370x >= component.getWidth() - this.dockingSensitivity && !isBlocked(component, "East")) {
                return "East";
            }
            if (point.f12370x < this.dockingSensitivity && !isBlocked(component, "West")) {
                return "West";
            }
            if (point.f12371y >= component.getHeight() - this.dockingSensitivity && !isBlocked(component, "South")) {
                return "South";
            }
            return null;
        }
        return null;
    }

    protected void dragTo(Point point, Point point2) {
        if (this.toolBar.isFloatable()) {
            try {
                if (this.dragWindow == null) {
                    this.dragWindow = createDragWindow(this.toolBar);
                }
                Point offset = this.dragWindow.getOffset();
                if (offset == null) {
                    Dimension preferredSize = this.toolBar.getPreferredSize();
                    offset = new Point(preferredSize.width / 2, preferredSize.height / 2);
                    this.dragWindow.setOffset(offset);
                }
                Point point3 = new Point(point2.f12370x + point.f12370x, point2.f12371y + point.f12371y);
                Point point4 = new Point(point3.f12370x - offset.f12370x, point3.f12371y - offset.f12371y);
                if (this.dockingSource == null) {
                    this.dockingSource = this.toolBar.getParent();
                }
                this.constraintBeforeFloating = calculateConstraint();
                Point locationOnScreen = this.dockingSource.getLocationOnScreen();
                Point point5 = new Point(point3.f12370x - locationOnScreen.f12370x, point3.f12371y - locationOnScreen.f12371y);
                if (canDock(this.dockingSource, point5)) {
                    this.dragWindow.setBackground(getDockingColor());
                    this.dragWindow.setOrientation(mapConstraintToOrientation(getDockingConstraint(this.dockingSource, point5)));
                    this.dragWindow.setBorderColor(this.dockingBorderColor);
                } else {
                    this.dragWindow.setBackground(getFloatingColor());
                    this.dragWindow.setBorderColor(this.floatingBorderColor);
                    this.dragWindow.setOrientation(this.toolBar.getOrientation());
                }
                this.dragWindow.setLocation(point4.f12370x, point4.f12371y);
                if (!this.dragWindow.isVisible()) {
                    Dimension preferredSize2 = this.toolBar.getPreferredSize();
                    this.dragWindow.setSize(preferredSize2.width, preferredSize2.height);
                    this.dragWindow.show();
                }
            } catch (IllegalComponentStateException e2) {
            }
        }
    }

    protected void floatAt(Point point, Point point2) {
        if (this.toolBar.isFloatable()) {
            try {
                Point offset = this.dragWindow.getOffset();
                if (offset == null) {
                    offset = point;
                    this.dragWindow.setOffset(offset);
                }
                Point point3 = new Point(point2.f12370x + point.f12370x, point2.f12371y + point.f12371y);
                setFloatingLocation(point3.f12370x - offset.f12370x, point3.f12371y - offset.f12371y);
                if (this.dockingSource != null) {
                    Point locationOnScreen = this.dockingSource.getLocationOnScreen();
                    Point point4 = new Point(point3.f12370x - locationOnScreen.f12370x, point3.f12371y - locationOnScreen.f12371y);
                    if (canDock(this.dockingSource, point4)) {
                        setFloating(false, point4);
                    } else {
                        setFloating(true, null);
                    }
                } else {
                    setFloating(true, null);
                }
                this.dragWindow.setOffset(null);
            } catch (IllegalComponentStateException e2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected ContainerListener createToolBarContListener() {
        return getHandler();
    }

    protected FocusListener createToolBarFocusListener() {
        return getHandler();
    }

    protected PropertyChangeListener createPropertyListener() {
        return getHandler();
    }

    protected MouseInputListener createDockingListener() {
        getHandler().tb = this.toolBar;
        return getHandler();
    }

    protected WindowListener createFrameListener() {
        return new FrameListener();
    }

    protected void paintDragWindow(Graphics graphics) {
        graphics.setColor(this.dragWindow.getBackground());
        int width = this.dragWindow.getWidth();
        int height = this.dragWindow.getHeight();
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(this.dragWindow.getBorderColor());
        graphics.drawRect(0, 0, width - 1, height - 1);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String NAVIGATE_RIGHT = "navigateRight";
        private static final String NAVIGATE_LEFT = "navigateLeft";
        private static final String NAVIGATE_UP = "navigateUp";
        private static final String NAVIGATE_DOWN = "navigateDown";

        public Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            String name = getName();
            BasicToolBarUI basicToolBarUI = (BasicToolBarUI) BasicLookAndFeel.getUIOfType(((JToolBar) actionEvent.getSource()).getUI(), BasicToolBarUI.class);
            if (NAVIGATE_RIGHT == name) {
                basicToolBarUI.navigateFocusedComp(3);
                return;
            }
            if (NAVIGATE_LEFT == name) {
                basicToolBarUI.navigateFocusedComp(7);
            } else if (NAVIGATE_UP == name) {
                basicToolBarUI.navigateFocusedComp(1);
            } else if (NAVIGATE_DOWN == name) {
                basicToolBarUI.navigateFocusedComp(5);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$Handler.class */
    private class Handler implements ContainerListener, FocusListener, MouseInputListener, PropertyChangeListener {
        JToolBar tb;
        boolean isDragging;
        Point origin;

        private Handler() {
            this.isDragging = false;
            this.origin = null;
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) {
            Component child = containerEvent.getChild();
            if (BasicToolBarUI.this.toolBarFocusListener != null) {
                child.addFocusListener(BasicToolBarUI.this.toolBarFocusListener);
            }
            if (BasicToolBarUI.this.isRolloverBorders()) {
                BasicToolBarUI.this.setBorderToRollover(child);
            } else {
                BasicToolBarUI.this.setBorderToNonRollover(child);
            }
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) {
            Component child = containerEvent.getChild();
            if (BasicToolBarUI.this.toolBarFocusListener != null) {
                child.removeFocusListener(BasicToolBarUI.this.toolBarFocusListener);
            }
            BasicToolBarUI.this.setBorderToNormal(child);
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            Component component = focusEvent.getComponent();
            BasicToolBarUI.this.focusedCompIndex = BasicToolBarUI.this.toolBar.getComponentIndex(component);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (!this.tb.isEnabled()) {
                return;
            }
            this.isDragging = false;
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (!this.tb.isEnabled()) {
                return;
            }
            if (this.isDragging) {
                Point point = mouseEvent.getPoint();
                if (this.origin == null) {
                    this.origin = mouseEvent.getComponent().getLocationOnScreen();
                }
                BasicToolBarUI.this.floatAt(point, this.origin);
            }
            this.origin = null;
            this.isDragging = false;
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (!this.tb.isEnabled()) {
                return;
            }
            this.isDragging = true;
            Point point = mouseEvent.getPoint();
            if (this.origin == null) {
                this.origin = mouseEvent.getComponent().getLocationOnScreen();
            }
            BasicToolBarUI.this.dragTo(point, this.origin);
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == "lookAndFeel") {
                BasicToolBarUI.this.toolBar.updateUI();
                return;
            }
            if (propertyName != "orientation") {
                if (propertyName == BasicToolBarUI.IS_ROLLOVER) {
                    BasicToolBarUI.this.installNormalBorders(BasicToolBarUI.this.toolBar);
                    BasicToolBarUI.this.setRolloverBorders(((Boolean) propertyChangeEvent.getNewValue()).booleanValue());
                    return;
                }
                return;
            }
            Component[] components = BasicToolBarUI.this.toolBar.getComponents();
            int iIntValue = ((Integer) propertyChangeEvent.getNewValue()).intValue();
            for (int i2 = 0; i2 < components.length; i2++) {
                if (components[i2] instanceof JToolBar.Separator) {
                    JToolBar.Separator separator = (JToolBar.Separator) components[i2];
                    if (iIntValue == 0) {
                        separator.setOrientation(1);
                    } else {
                        separator.setOrientation(0);
                    }
                    Dimension separatorSize = separator.getSeparatorSize();
                    if (separatorSize != null && separatorSize.width != separatorSize.height) {
                        separator.setSeparatorSize(new Dimension(separatorSize.height, separatorSize.width));
                    }
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$FrameListener.class */
    protected class FrameListener extends WindowAdapter {
        protected FrameListener() {
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
            if (BasicToolBarUI.this.toolBar.isFloatable()) {
                if (BasicToolBarUI.this.dragWindow != null) {
                    BasicToolBarUI.this.dragWindow.setVisible(false);
                }
                BasicToolBarUI.this.floating = false;
                if (BasicToolBarUI.this.floatingToolBar == null) {
                    BasicToolBarUI.this.floatingToolBar = BasicToolBarUI.this.createFloatingWindow(BasicToolBarUI.this.toolBar);
                }
                if (BasicToolBarUI.this.floatingToolBar instanceof Window) {
                    ((Window) BasicToolBarUI.this.floatingToolBar).setVisible(false);
                }
                BasicToolBarUI.this.floatingToolBar.getContentPane().remove(BasicToolBarUI.this.toolBar);
                String str = BasicToolBarUI.this.constraintBeforeFloating;
                if (BasicToolBarUI.this.toolBar.getOrientation() == 0) {
                    if (str == "West" || str == "East") {
                        str = "North";
                    }
                } else if (str == "North" || str == "South") {
                    str = "West";
                }
                if (BasicToolBarUI.this.dockingSource == null) {
                    BasicToolBarUI.this.dockingSource = BasicToolBarUI.this.toolBar.getParent();
                }
                if (BasicToolBarUI.this.propertyListener != null) {
                    UIManager.removePropertyChangeListener(BasicToolBarUI.this.propertyListener);
                }
                BasicToolBarUI.this.dockingSource.add(BasicToolBarUI.this.toolBar, str);
                BasicToolBarUI.this.dockingSource.invalidate();
                Container parent = BasicToolBarUI.this.dockingSource.getParent();
                if (parent != null) {
                    parent.validate();
                }
                BasicToolBarUI.this.dockingSource.repaint();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$ToolBarContListener.class */
    public class ToolBarContListener implements ContainerListener {
        protected ToolBarContListener() {
        }

        @Override // java.awt.event.ContainerListener
        public void componentAdded(ContainerEvent containerEvent) {
            BasicToolBarUI.this.getHandler().componentAdded(containerEvent);
        }

        @Override // java.awt.event.ContainerListener
        public void componentRemoved(ContainerEvent containerEvent) {
            BasicToolBarUI.this.getHandler().componentRemoved(containerEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$ToolBarFocusListener.class */
    protected class ToolBarFocusListener implements FocusListener {
        protected ToolBarFocusListener() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicToolBarUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicToolBarUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$PropertyListener.class */
    public class PropertyListener implements PropertyChangeListener {
        protected PropertyListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicToolBarUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$DockingListener.class */
    public class DockingListener implements MouseInputListener {
        protected JToolBar toolBar;
        protected boolean isDragging = false;
        protected Point origin = null;

        public DockingListener(JToolBar jToolBar) {
            this.toolBar = jToolBar;
            BasicToolBarUI.this.getHandler().tb = jToolBar;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().mouseClicked(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().tb = this.toolBar;
            BasicToolBarUI.this.getHandler().mousePressed(mouseEvent);
            this.isDragging = BasicToolBarUI.this.getHandler().isDragging;
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().tb = this.toolBar;
            BasicToolBarUI.this.getHandler().isDragging = this.isDragging;
            BasicToolBarUI.this.getHandler().origin = this.origin;
            BasicToolBarUI.this.getHandler().mouseReleased(mouseEvent);
            this.isDragging = BasicToolBarUI.this.getHandler().isDragging;
            this.origin = BasicToolBarUI.this.getHandler().origin;
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().mouseExited(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().tb = this.toolBar;
            BasicToolBarUI.this.getHandler().origin = this.origin;
            BasicToolBarUI.this.getHandler().mouseDragged(mouseEvent);
            this.isDragging = BasicToolBarUI.this.getHandler().isDragging;
            this.origin = BasicToolBarUI.this.getHandler().origin;
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicToolBarUI.this.getHandler().mouseMoved(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicToolBarUI$DragWindow.class */
    protected class DragWindow extends Window {
        Color borderColor;
        int orientation;
        Point offset;

        DragWindow(Window window) {
            super(window);
            this.borderColor = Color.gray;
            this.orientation = BasicToolBarUI.this.toolBar.getOrientation();
        }

        public int getOrientation() {
            return this.orientation;
        }

        public void setOrientation(int i2) {
            if (!isShowing() || i2 == this.orientation) {
                return;
            }
            this.orientation = i2;
            Dimension size = getSize();
            setSize(new Dimension(size.height, size.width));
            if (this.offset != null) {
                if (BasicGraphicsUtils.isLeftToRight(BasicToolBarUI.this.toolBar)) {
                    setOffset(new Point(this.offset.f12371y, this.offset.f12370x));
                } else if (i2 == 0) {
                    setOffset(new Point(size.height - this.offset.f12371y, this.offset.f12370x));
                } else {
                    setOffset(new Point(this.offset.f12371y, size.width - this.offset.f12370x));
                }
            }
            repaint();
        }

        public Point getOffset() {
            return this.offset;
        }

        public void setOffset(Point point) {
            this.offset = point;
        }

        public void setBorderColor(Color color) {
            if (this.borderColor == color) {
                return;
            }
            this.borderColor = color;
            repaint();
        }

        public Color getBorderColor() {
            return this.borderColor;
        }

        @Override // java.awt.Window, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            BasicToolBarUI.this.paintDragWindow(graphics);
            super.paint(graphics);
        }

        @Override // java.awt.Container
        public Insets getInsets() {
            return new Insets(1, 1, 1, 1);
        }
    }
}
