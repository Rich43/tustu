package javax.swing;

import java.applet.Applet;
import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.peer.LightweightPeer;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.Transient;
import java.beans.VetoableChangeListener;
import java.beans.VetoableChangeSupport;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleExtendedComponent;
import javax.accessibility.AccessibleKeyBinding;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.Popup;
import javax.swing.TransferHandler;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.AncestorListener;
import javax.swing.event.EventListenerList;
import javax.swing.plaf.ComponentUI;
import javax.swing.table.JTableHeader;
import jdk.jfr.Enabled;
import sun.awt.CausedFocusEvent;
import sun.awt.RequestFocusController;
import sun.awt.SunToolkit;
import sun.swing.SwingUtilities2;
import sun.swing.UIClientPropertyKey;

/* loaded from: rt.jar:javax/swing/JComponent.class */
public abstract class JComponent extends Container implements Serializable, TransferHandler.HasGetTransferHandler {
    private static final String uiClassID = "ComponentUI";
    private static Set<KeyStroke> managingFocusForwardTraversalKeys;
    private static Set<KeyStroke> managingFocusBackwardTraversalKeys;
    private static final int NOT_OBSCURED = 0;
    private static final int PARTIALLY_OBSCURED = 1;
    private static final int COMPLETELY_OBSCURED = 2;
    static boolean DEBUG_GRAPHICS_LOADED;
    private boolean isAlignmentXSet;
    private float alignmentX;
    private boolean isAlignmentYSet;
    private float alignmentY;
    protected transient ComponentUI ui;
    private transient ArrayTable clientProperties;
    private VetoableChangeSupport vetoableChangeSupport;
    private boolean autoscrolls;
    private Border border;
    private int flags;
    transient Component paintingChild;
    public static final int WHEN_FOCUSED = 0;
    public static final int WHEN_ANCESTOR_OF_FOCUSED_COMPONENT = 1;
    public static final int WHEN_IN_FOCUSED_WINDOW = 2;
    public static final int UNDEFINED_CONDITION = -1;
    private static final String KEYBOARD_BINDINGS_KEY = "_KeyboardBindings";
    private static final String WHEN_IN_FOCUSED_WINDOW_BINDINGS = "_WhenInFocusedWindow";
    public static final String TOOL_TIP_TEXT_KEY = "ToolTipText";
    private static final String NEXT_FOCUS = "nextFocus";
    private JPopupMenu popupMenu;
    private static final int IS_DOUBLE_BUFFERED = 0;
    private static final int ANCESTOR_USING_BUFFER = 1;
    private static final int IS_PAINTING_TILE = 2;
    private static final int IS_OPAQUE = 3;
    private static final int KEY_EVENTS_ENABLED = 4;
    private static final int FOCUS_INPUTMAP_CREATED = 5;
    private static final int ANCESTOR_INPUTMAP_CREATED = 6;
    private static final int WIF_INPUTMAP_CREATED = 7;
    private static final int ACTIONMAP_CREATED = 8;
    private static final int CREATED_DOUBLE_BUFFER = 9;
    private static final int IS_PRINTING = 11;
    private static final int IS_PRINTING_ALL = 12;
    private static final int IS_REPAINTING = 13;
    private static final int WRITE_OBJ_COUNTER_FIRST = 14;
    private static final int RESERVED_1 = 15;
    private static final int RESERVED_2 = 16;
    private static final int RESERVED_3 = 17;
    private static final int RESERVED_4 = 18;
    private static final int RESERVED_5 = 19;
    private static final int RESERVED_6 = 20;
    private static final int WRITE_OBJ_COUNTER_LAST = 21;
    private static final int REQUEST_FOCUS_DISABLED = 22;
    private static final int INHERITS_POPUP_MENU = 23;
    private static final int OPAQUE_SET = 24;
    private static final int AUTOSCROLLS_SET = 25;
    private static final int FOCUS_TRAVERSAL_KEYS_FORWARD_SET = 26;
    private static final int FOCUS_TRAVERSAL_KEYS_BACKWARD_SET = 27;
    private InputMap focusInputMap;
    private InputMap ancestorInputMap;
    private ComponentInputMap windowInputMap;
    private ActionMap actionMap;
    private static final String defaultLocale = "JComponent.defaultLocale";
    private static Component componentObtainingGraphicsFrom;
    private transient Object aaTextInfo;
    private static final Hashtable<ObjectInputStream, ReadObjectCallback> readObjectCallbacks = new Hashtable<>(1);
    private static final Object INPUT_VERIFIER_SOURCE_KEY = new StringBuilder("InputVerifierSourceKey");
    private static List<Rectangle> tempRectangles = new ArrayList(11);
    private static Object componentObtainingGraphicsFromLock = new StringBuilder("componentObtainingGraphicsFrom");
    static final RequestFocusController focusController = new RequestFocusController() { // from class: javax.swing.JComponent.1
        @Override // sun.awt.RequestFocusController
        public boolean acceptRequestFocus(Component component, Component component2, boolean z2, boolean z3, CausedFocusEvent.Cause cause) {
            JComponent jComponent;
            InputVerifier inputVerifier;
            Object objAppContextGet;
            if (component2 != null && (component2 instanceof JComponent) && component != null && (component instanceof JComponent) && ((JComponent) component2).getVerifyInputWhenFocusTarget() && (inputVerifier = (jComponent = (JComponent) component).getInputVerifier()) != null && (objAppContextGet = SwingUtilities.appContextGet(JComponent.INPUT_VERIFIER_SOURCE_KEY)) != jComponent) {
                SwingUtilities.appContextPut(JComponent.INPUT_VERIFIER_SOURCE_KEY, jComponent);
                try {
                    boolean zShouldYieldFocus = inputVerifier.shouldYieldFocus(jComponent);
                    if (objAppContextGet != null) {
                        SwingUtilities.appContextPut(JComponent.INPUT_VERIFIER_SOURCE_KEY, objAppContextGet);
                    } else {
                        SwingUtilities.appContextRemove(JComponent.INPUT_VERIFIER_SOURCE_KEY);
                    }
                    return zShouldYieldFocus;
                } catch (Throwable th) {
                    if (objAppContextGet != null) {
                        SwingUtilities.appContextPut(JComponent.INPUT_VERIFIER_SOURCE_KEY, objAppContextGet);
                    } else {
                        SwingUtilities.appContextRemove(JComponent.INPUT_VERIFIER_SOURCE_KEY);
                    }
                    throw th;
                }
            }
            return true;
        }
    };
    protected EventListenerList listenerList = new EventListenerList();
    private InputVerifier inputVerifier = null;
    private boolean verifyInputWhenFocusTarget = true;
    private transient AtomicBoolean revalidateRunnableScheduled = new AtomicBoolean(false);

    static Graphics safelyGetGraphics(Component component) {
        return safelyGetGraphics(component, SwingUtilities.getRoot(component));
    }

    static Graphics safelyGetGraphics(Component component, Component component2) {
        Graphics graphics;
        synchronized (componentObtainingGraphicsFromLock) {
            componentObtainingGraphicsFrom = component2;
            graphics = component.getGraphics();
            componentObtainingGraphicsFrom = null;
        }
        return graphics;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static void getGraphicsInvoked(Component component) {
        JRootPane rootPane;
        if (!isComponentObtainingGraphicsFrom(component) && (rootPane = ((RootPaneContainer) component).getRootPane()) != null) {
            rootPane.disableTrueDoubleBuffering();
        }
    }

    private static boolean isComponentObtainingGraphicsFrom(Component component) {
        boolean z2;
        synchronized (componentObtainingGraphicsFromLock) {
            z2 = componentObtainingGraphicsFrom == component;
        }
        return z2;
    }

    static Set<KeyStroke> getManagingFocusForwardTraversalKeys() {
        synchronized (JComponent.class) {
            if (managingFocusForwardTraversalKeys == null) {
                managingFocusForwardTraversalKeys = new HashSet(1);
                managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 2));
            }
        }
        return managingFocusForwardTraversalKeys;
    }

    static Set<KeyStroke> getManagingFocusBackwardTraversalKeys() {
        synchronized (JComponent.class) {
            if (managingFocusBackwardTraversalKeys == null) {
                managingFocusBackwardTraversalKeys = new HashSet(1);
                managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 3));
            }
        }
        return managingFocusBackwardTraversalKeys;
    }

    private static Rectangle fetchRectangle() {
        Rectangle rectangle;
        Rectangle rectangle2;
        synchronized (tempRectangles) {
            int size = tempRectangles.size();
            if (size > 0) {
                rectangle = tempRectangles.remove(size - 1);
            } else {
                rectangle = new Rectangle(0, 0, 0, 0);
            }
            rectangle2 = rectangle;
        }
        return rectangle2;
    }

    private static void recycleRectangle(Rectangle rectangle) {
        synchronized (tempRectangles) {
            tempRectangles.add(rectangle);
        }
    }

    public void setInheritsPopupMenu(boolean z2) {
        boolean flag = getFlag(23);
        setFlag(23, z2);
        firePropertyChange("inheritsPopupMenu", flag, z2);
    }

    public boolean getInheritsPopupMenu() {
        return getFlag(23);
    }

    public void setComponentPopupMenu(JPopupMenu jPopupMenu) {
        if (jPopupMenu != null) {
            enableEvents(16L);
        }
        JPopupMenu jPopupMenu2 = this.popupMenu;
        this.popupMenu = jPopupMenu;
        firePropertyChange("componentPopupMenu", jPopupMenu2, jPopupMenu);
    }

    public JPopupMenu getComponentPopupMenu() {
        if (!getInheritsPopupMenu()) {
            return this.popupMenu;
        }
        if (this.popupMenu == null) {
            Container parent = getParent();
            while (true) {
                Container container = parent;
                if (container != null) {
                    if (container instanceof JComponent) {
                        return ((JComponent) container).getComponentPopupMenu();
                    }
                    if (!(container instanceof Window) && !(container instanceof Applet)) {
                        parent = container.getParent();
                    } else {
                        return null;
                    }
                } else {
                    return null;
                }
            }
        } else {
            return this.popupMenu;
        }
    }

    public JComponent() {
        enableEvents(8L);
        if (isManagingFocus()) {
            LookAndFeel.installProperty(this, "focusTraversalKeysForward", getManagingFocusForwardTraversalKeys());
            LookAndFeel.installProperty(this, "focusTraversalKeysBackward", getManagingFocusBackwardTraversalKeys());
        }
        super.setLocale(getDefaultLocale());
    }

    public void updateUI() {
    }

    protected void setUI(ComponentUI componentUI) {
        uninstallUIAndProperties();
        this.aaTextInfo = UIManager.getDefaults().get(SwingUtilities2.AA_TEXT_PROPERTY_KEY);
        ComponentUI componentUI2 = this.ui;
        this.ui = componentUI;
        if (this.ui != null) {
            this.ui.installUI(this);
        }
        firePropertyChange("UI", componentUI2, componentUI);
        revalidate();
        repaint();
    }

    private void uninstallUIAndProperties() {
        if (this.ui != null) {
            this.ui.uninstallUI(this);
            if (this.clientProperties != null) {
                synchronized (this.clientProperties) {
                    Object[] keys = this.clientProperties.getKeys(null);
                    if (keys != null) {
                        for (Object obj : keys) {
                            if (obj instanceof UIClientPropertyKey) {
                                putClientProperty(obj, null);
                            }
                        }
                    }
                }
            }
        }
    }

    public String getUIClassID() {
        return uiClassID;
    }

    protected Graphics getComponentGraphics(Graphics graphics) {
        Graphics debugGraphics = graphics;
        if (this.ui != null && DEBUG_GRAPHICS_LOADED && DebugGraphics.debugComponentCount() != 0 && shouldDebugGraphics() != 0 && !(graphics instanceof DebugGraphics)) {
            debugGraphics = new DebugGraphics(graphics, this);
        }
        debugGraphics.setColor(getForeground());
        debugGraphics.setFont(getFont());
        return debugGraphics;
    }

    protected void paintComponent(Graphics graphics) {
        if (this.ui != null) {
            Graphics graphicsCreate = graphics == null ? null : graphics.create();
            try {
                this.ui.update(graphicsCreate, this);
            } finally {
                graphicsCreate.dispose();
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    protected void paintChildren(Graphics graphics) {
        synchronized (getTreeLock()) {
            int componentCount = getComponentCount() - 1;
            if (componentCount < 0) {
                return;
            }
            if (this.paintingChild != null && (this.paintingChild instanceof JComponent) && this.paintingChild.isOpaque()) {
                while (componentCount >= 0 && getComponent(componentCount) != this.paintingChild) {
                    componentCount--;
                }
            }
            Rectangle rectangleFetchRectangle = fetchRectangle();
            boolean z2 = !isOptimizedDrawingEnabled() && checkIfChildObscuredBySibling();
            Rectangle clipBounds = null;
            if (z2) {
                clipBounds = graphics.getClipBounds();
                if (clipBounds == null) {
                    clipBounds = new Rectangle(0, 0, getWidth(), getHeight());
                }
            }
            boolean flag = getFlag(11);
            Window windowAncestor = SwingUtilities.getWindowAncestor(this);
            boolean z3 = windowAncestor == null || windowAncestor.isOpaque();
            while (componentCount >= 0) {
                Component component = getComponent(componentCount);
                if (component != null) {
                    boolean z4 = component instanceof JComponent;
                    if ((!z3 || z4 || isLightweightComponent(component)) && component.isVisible()) {
                        Rectangle bounds = component.getBounds(rectangleFetchRectangle);
                        if (graphics.hitClip(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height)) {
                            if (z2 && componentCount > 0) {
                                int i2 = bounds.f12372x;
                                int i3 = bounds.f12373y;
                                int i4 = bounds.width;
                                int i5 = bounds.height;
                                SwingUtilities.computeIntersection(clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height, bounds);
                                if (getObscuredState(componentCount, bounds.f12372x, bounds.f12373y, bounds.width, bounds.height) != 2) {
                                    bounds.f12372x = i2;
                                    bounds.f12373y = i3;
                                    bounds.width = i4;
                                    bounds.height = i5;
                                }
                            }
                            Graphics graphicsCreate = graphics.create(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
                            graphicsCreate.setColor(component.getForeground());
                            graphicsCreate.setFont(component.getFont());
                            boolean z5 = false;
                            if (z4) {
                                try {
                                    if (getFlag(1)) {
                                        ((JComponent) component).setFlag(1, true);
                                        z5 = true;
                                    }
                                    if (getFlag(2)) {
                                        ((JComponent) component).setFlag(2, true);
                                        z5 = true;
                                    }
                                    if (!flag) {
                                        component.paint(graphicsCreate);
                                    } else if (!getFlag(12)) {
                                        component.print(graphicsCreate);
                                    } else {
                                        component.printAll(graphicsCreate);
                                    }
                                } catch (Throwable th) {
                                    graphicsCreate.dispose();
                                    if (0 != 0) {
                                        ((JComponent) component).setFlag(1, false);
                                        ((JComponent) component).setFlag(2, false);
                                    }
                                    throw th;
                                }
                            } else if (!flag) {
                                component.paint(graphicsCreate);
                            } else if (!getFlag(12)) {
                                component.print(graphicsCreate);
                            } else {
                                component.printAll(graphicsCreate);
                            }
                            graphicsCreate.dispose();
                            if (z5) {
                                ((JComponent) component).setFlag(1, false);
                                ((JComponent) component).setFlag(2, false);
                            }
                        } else {
                            continue;
                        }
                    }
                }
                componentCount--;
            }
            recycleRectangle(rectangleFetchRectangle);
        }
    }

    protected void paintBorder(Graphics graphics) {
        Border border = getBorder();
        if (border != null) {
            border.paintBorder(this, graphics, 0, 0, getWidth(), getHeight());
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        paint(graphics);
    }

    @Override // java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        int i2;
        int i3;
        int width;
        int height;
        boolean z2 = false;
        if (getWidth() <= 0 || getHeight() <= 0) {
            return;
        }
        Graphics graphicsCreate = getComponentGraphics(graphics).create();
        try {
            RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager(this);
            Rectangle clipBounds = graphicsCreate.getClipBounds();
            if (clipBounds == null) {
                i3 = 0;
                i2 = 0;
                width = getWidth();
                height = getHeight();
            } else {
                i2 = clipBounds.f12372x;
                i3 = clipBounds.f12373y;
                width = clipBounds.width;
                height = clipBounds.height;
            }
            if (width > getWidth()) {
                width = getWidth();
            }
            if (height > getHeight()) {
                height = getHeight();
            }
            if (getParent() != null && !(getParent() instanceof JComponent)) {
                adjustPaintFlags();
                z2 = true;
            }
            boolean flag = getFlag(11);
            if (!flag && repaintManagerCurrentManager.isDoubleBufferingEnabled() && !getFlag(1) && isDoubleBuffered() && (getFlag(13) || repaintManagerCurrentManager.isPainting())) {
                repaintManagerCurrentManager.beginPaint();
                try {
                    repaintManagerCurrentManager.paint(this, this, graphicsCreate, i2, i3, width, height);
                    repaintManagerCurrentManager.endPaint();
                } catch (Throwable th) {
                    repaintManagerCurrentManager.endPaint();
                    throw th;
                }
            } else {
                if (clipBounds == null) {
                    graphicsCreate.setClip(i2, i3, width, height);
                }
                if (!rectangleIsObscured(i2, i3, width, height)) {
                    if (!flag) {
                        paintComponent(graphicsCreate);
                        paintBorder(graphicsCreate);
                    } else {
                        printComponent(graphicsCreate);
                        printBorder(graphicsCreate);
                    }
                }
                if (!flag) {
                    paintChildren(graphicsCreate);
                } else {
                    printChildren(graphicsCreate);
                }
            }
        } finally {
            graphicsCreate.dispose();
            if (z2) {
                setFlag(1, false);
                setFlag(2, false);
                setFlag(11, false);
                setFlag(12, false);
            }
        }
    }

    void paintForceDoubleBuffered(Graphics graphics) {
        RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager(this);
        Rectangle clipBounds = graphics.getClipBounds();
        repaintManagerCurrentManager.beginPaint();
        setFlag(13, true);
        try {
            repaintManagerCurrentManager.paint(this, this, graphics, clipBounds.f12372x, clipBounds.f12373y, clipBounds.width, clipBounds.height);
            repaintManagerCurrentManager.endPaint();
            setFlag(13, false);
        } catch (Throwable th) {
            repaintManagerCurrentManager.endPaint();
            setFlag(13, false);
            throw th;
        }
    }

    boolean isPainting() {
        Container parent = this;
        while (true) {
            Container container = parent;
            if (container != null) {
                if ((container instanceof JComponent) && ((JComponent) container).getFlag(1)) {
                    return true;
                }
                parent = container.getParent();
            } else {
                return false;
            }
        }
    }

    private void adjustPaintFlags() {
        Container parent = getParent();
        while (true) {
            Container container = parent;
            if (container != null) {
                if (!(container instanceof JComponent)) {
                    parent = container.getParent();
                } else {
                    JComponent jComponent = (JComponent) container;
                    if (jComponent.getFlag(1)) {
                        setFlag(1, true);
                    }
                    if (jComponent.getFlag(2)) {
                        setFlag(2, true);
                    }
                    if (jComponent.getFlag(11)) {
                        setFlag(11, true);
                    }
                    if (jComponent.getFlag(12)) {
                        setFlag(12, true);
                        return;
                    }
                    return;
                }
            } else {
                return;
            }
        }
    }

    @Override // java.awt.Component
    public void printAll(Graphics graphics) {
        setFlag(12, true);
        try {
            print(graphics);
        } finally {
            setFlag(12, false);
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void print(Graphics graphics) {
        setFlag(11, true);
        firePropertyChange("paintingForPrint", false, true);
        try {
            paint(graphics);
        } finally {
            setFlag(11, false);
            firePropertyChange("paintingForPrint", true, false);
        }
    }

    protected void printComponent(Graphics graphics) {
        paintComponent(graphics);
    }

    protected void printChildren(Graphics graphics) {
        paintChildren(graphics);
    }

    protected void printBorder(Graphics graphics) {
        paintBorder(graphics);
    }

    public boolean isPaintingTile() {
        return getFlag(2);
    }

    public final boolean isPaintingForPrint() {
        return getFlag(11);
    }

    @Deprecated
    public boolean isManagingFocus() {
        return false;
    }

    private void registerNextFocusableComponent() {
        registerNextFocusableComponent(getNextFocusableComponent());
    }

    private void registerNextFocusableComponent(Component component) {
        if (component == null) {
            return;
        }
        Container focusCycleRootAncestor = isFocusCycleRoot() ? this : getFocusCycleRootAncestor();
        FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
        if (!(focusTraversalPolicy instanceof LegacyGlueFocusTraversalPolicy)) {
            focusTraversalPolicy = new LegacyGlueFocusTraversalPolicy(focusTraversalPolicy);
            focusCycleRootAncestor.setFocusTraversalPolicy(focusTraversalPolicy);
        }
        ((LegacyGlueFocusTraversalPolicy) focusTraversalPolicy).setNextFocusableComponent(this, component);
    }

    private void deregisterNextFocusableComponent() {
        Component nextFocusableComponent = getNextFocusableComponent();
        if (nextFocusableComponent == null) {
            return;
        }
        Container focusCycleRootAncestor = isFocusCycleRoot() ? this : getFocusCycleRootAncestor();
        if (focusCycleRootAncestor == null) {
            return;
        }
        FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
        if (focusTraversalPolicy instanceof LegacyGlueFocusTraversalPolicy) {
            ((LegacyGlueFocusTraversalPolicy) focusTraversalPolicy).unsetNextFocusableComponent(this, nextFocusableComponent);
        }
    }

    @Deprecated
    public void setNextFocusableComponent(Component component) {
        boolean zIsDisplayable = isDisplayable();
        if (zIsDisplayable) {
            deregisterNextFocusableComponent();
        }
        putClientProperty(NEXT_FOCUS, component);
        if (zIsDisplayable) {
            registerNextFocusableComponent(component);
        }
    }

    @Deprecated
    public Component getNextFocusableComponent() {
        return (Component) getClientProperty(NEXT_FOCUS);
    }

    public void setRequestFocusEnabled(boolean z2) {
        setFlag(22, !z2);
    }

    public boolean isRequestFocusEnabled() {
        return !getFlag(22);
    }

    @Override // java.awt.Component
    public void requestFocus() {
        super.requestFocus();
    }

    @Override // java.awt.Component
    public boolean requestFocus(boolean z2) {
        return super.requestFocus(z2);
    }

    @Override // java.awt.Component
    public boolean requestFocusInWindow() {
        return super.requestFocusInWindow();
    }

    @Override // java.awt.Component
    protected boolean requestFocusInWindow(boolean z2) {
        return super.requestFocusInWindow(z2);
    }

    public void grabFocus() {
        requestFocus();
    }

    public void setVerifyInputWhenFocusTarget(boolean z2) {
        boolean z3 = this.verifyInputWhenFocusTarget;
        this.verifyInputWhenFocusTarget = z2;
        firePropertyChange("verifyInputWhenFocusTarget", z3, z2);
    }

    public boolean getVerifyInputWhenFocusTarget() {
        return this.verifyInputWhenFocusTarget;
    }

    @Override // java.awt.Component
    public FontMetrics getFontMetrics(Font font) {
        return SwingUtilities2.getFontMetrics(this, font);
    }

    @Override // java.awt.Component
    public void setPreferredSize(Dimension dimension) {
        super.setPreferredSize(dimension);
    }

    @Override // java.awt.Container, java.awt.Component
    @Transient
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        Dimension preferredSize = null;
        if (this.ui != null) {
            preferredSize = this.ui.getPreferredSize(this);
        }
        return preferredSize != null ? preferredSize : super.getPreferredSize();
    }

    @Override // java.awt.Component
    public void setMaximumSize(Dimension dimension) {
        super.setMaximumSize(dimension);
    }

    @Override // java.awt.Container, java.awt.Component
    @Transient
    public Dimension getMaximumSize() {
        if (isMaximumSizeSet()) {
            return super.getMaximumSize();
        }
        Dimension maximumSize = null;
        if (this.ui != null) {
            maximumSize = this.ui.getMaximumSize(this);
        }
        return maximumSize != null ? maximumSize : super.getMaximumSize();
    }

    @Override // java.awt.Component
    public void setMinimumSize(Dimension dimension) {
        super.setMinimumSize(dimension);
    }

    @Override // java.awt.Container, java.awt.Component
    @Transient
    public Dimension getMinimumSize() {
        if (isMinimumSizeSet()) {
            return super.getMinimumSize();
        }
        Dimension minimumSize = null;
        if (this.ui != null) {
            minimumSize = this.ui.getMinimumSize(this);
        }
        return minimumSize != null ? minimumSize : super.getMinimumSize();
    }

    @Override // java.awt.Component
    public boolean contains(int i2, int i3) {
        return this.ui != null ? this.ui.contains(this, i2, i3) : super.contains(i2, i3);
    }

    public void setBorder(Border border) {
        Border border2 = this.border;
        this.border = border;
        firePropertyChange("border", border2, border);
        if (border != border2) {
            if (border == null || border2 == null || !border.getBorderInsets(this).equals(border2.getBorderInsets(this))) {
                revalidate();
            }
            repaint();
        }
    }

    public Border getBorder() {
        return this.border;
    }

    @Override // java.awt.Container
    public Insets getInsets() {
        if (this.border != null) {
            return this.border.getBorderInsets(this);
        }
        return super.getInsets();
    }

    public Insets getInsets(Insets insets) {
        if (insets == null) {
            insets = new Insets(0, 0, 0, 0);
        }
        if (this.border != null) {
            if (this.border instanceof AbstractBorder) {
                return ((AbstractBorder) this.border).getBorderInsets(this, insets);
            }
            return this.border.getBorderInsets(this);
        }
        insets.bottom = 0;
        insets.right = 0;
        insets.top = 0;
        insets.left = 0;
        return insets;
    }

    @Override // java.awt.Container, java.awt.Component
    public float getAlignmentY() {
        if (this.isAlignmentYSet) {
            return this.alignmentY;
        }
        return super.getAlignmentY();
    }

    public void setAlignmentY(float f2) {
        this.alignmentY = f2 > 1.0f ? 1.0f : f2 < 0.0f ? 0.0f : f2;
        this.isAlignmentYSet = true;
    }

    @Override // java.awt.Container, java.awt.Component
    public float getAlignmentX() {
        if (this.isAlignmentXSet) {
            return this.alignmentX;
        }
        return super.getAlignmentX();
    }

    public void setAlignmentX(float f2) {
        this.alignmentX = f2 > 1.0f ? 1.0f : f2 < 0.0f ? 0.0f : f2;
        this.isAlignmentXSet = true;
    }

    public void setInputVerifier(InputVerifier inputVerifier) {
        InputVerifier inputVerifier2 = (InputVerifier) getClientProperty(ClientPropertyKey.JComponent_INPUT_VERIFIER);
        putClientProperty(ClientPropertyKey.JComponent_INPUT_VERIFIER, inputVerifier);
        firePropertyChange("inputVerifier", inputVerifier2, inputVerifier);
    }

    public InputVerifier getInputVerifier() {
        return (InputVerifier) getClientProperty(ClientPropertyKey.JComponent_INPUT_VERIFIER);
    }

    @Override // java.awt.Component
    public Graphics getGraphics() {
        if (DEBUG_GRAPHICS_LOADED && shouldDebugGraphics() != 0) {
            return new DebugGraphics(super.getGraphics(), this);
        }
        return super.getGraphics();
    }

    public void setDebugGraphicsOptions(int i2) {
        DebugGraphics.setDebugOptions(this, i2);
    }

    public int getDebugGraphicsOptions() {
        return DebugGraphics.getDebugOptions(this);
    }

    int shouldDebugGraphics() {
        return DebugGraphics.shouldComponentDebug(this);
    }

    public void registerKeyboardAction(ActionListener actionListener, String str, KeyStroke keyStroke, int i2) {
        InputMap inputMap = getInputMap(i2, true);
        if (inputMap != null) {
            ActionMap actionMap = getActionMap(true);
            ActionStandin actionStandin = new ActionStandin(actionListener, str);
            inputMap.put(keyStroke, actionStandin);
            if (actionMap != null) {
                actionMap.put(actionStandin, actionStandin);
            }
        }
    }

    private void registerWithKeyboardManager(boolean z2) {
        KeyStroke[] keyStrokeArrAllKeys;
        InputMap inputMap = getInputMap(2, false);
        Hashtable hashtable = (Hashtable) getClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS);
        if (inputMap != null) {
            keyStrokeArrAllKeys = inputMap.allKeys();
            if (keyStrokeArrAllKeys != null) {
                for (int length = keyStrokeArrAllKeys.length - 1; length >= 0; length--) {
                    if (!z2 || hashtable == null || hashtable.get(keyStrokeArrAllKeys[length]) == null) {
                        registerWithKeyboardManager(keyStrokeArrAllKeys[length]);
                    }
                    if (hashtable != null) {
                        hashtable.remove(keyStrokeArrAllKeys[length]);
                    }
                }
            }
        } else {
            keyStrokeArrAllKeys = null;
        }
        if (hashtable != null && hashtable.size() > 0) {
            Enumeration enumerationKeys = hashtable.keys();
            while (enumerationKeys.hasMoreElements()) {
                unregisterWithKeyboardManager((KeyStroke) enumerationKeys.nextElement2());
            }
            hashtable.clear();
        }
        if (keyStrokeArrAllKeys != null && keyStrokeArrAllKeys.length > 0) {
            if (hashtable == null) {
                hashtable = new Hashtable(keyStrokeArrAllKeys.length);
                putClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS, hashtable);
            }
            for (int length2 = keyStrokeArrAllKeys.length - 1; length2 >= 0; length2--) {
                hashtable.put(keyStrokeArrAllKeys[length2], keyStrokeArrAllKeys[length2]);
            }
            return;
        }
        putClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS, null);
    }

    private void unregisterWithKeyboardManager() {
        Hashtable hashtable = (Hashtable) getClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS);
        if (hashtable != null && hashtable.size() > 0) {
            Enumeration enumerationKeys = hashtable.keys();
            while (enumerationKeys.hasMoreElements()) {
                unregisterWithKeyboardManager((KeyStroke) enumerationKeys.nextElement2());
            }
        }
        putClientProperty(WHEN_IN_FOCUSED_WINDOW_BINDINGS, null);
    }

    void componentInputMapChanged(ComponentInputMap componentInputMap) {
        InputMap inputMap;
        InputMap inputMap2 = getInputMap(2, false);
        while (true) {
            inputMap = inputMap2;
            if (inputMap == componentInputMap || inputMap == null) {
                break;
            } else {
                inputMap2 = inputMap.getParent();
            }
        }
        if (inputMap != null) {
            registerWithKeyboardManager(false);
        }
    }

    private void registerWithKeyboardManager(KeyStroke keyStroke) {
        KeyboardManager.getCurrentManager().registerKeyStroke(keyStroke, this);
    }

    private void unregisterWithKeyboardManager(KeyStroke keyStroke) {
        KeyboardManager.getCurrentManager().unregisterKeyStroke(keyStroke, this);
    }

    public void registerKeyboardAction(ActionListener actionListener, KeyStroke keyStroke, int i2) {
        registerKeyboardAction(actionListener, null, keyStroke, i2);
    }

    public void unregisterKeyboardAction(KeyStroke keyStroke) {
        ActionMap actionMap = getActionMap(false);
        for (int i2 = 0; i2 < 3; i2++) {
            InputMap inputMap = getInputMap(i2, false);
            if (inputMap != null) {
                Object obj = inputMap.get(keyStroke);
                if (actionMap != null && obj != null) {
                    actionMap.remove(obj);
                }
                inputMap.remove(keyStroke);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public KeyStroke[] getRegisteredKeyStrokes() {
        int[] iArr = new int[3];
        KeyStroke[] keyStrokeArr = new KeyStroke[3];
        for (int i2 = 0; i2 < 3; i2++) {
            InputMap inputMap = getInputMap(i2, false);
            keyStrokeArr[i2] = inputMap != null ? inputMap.allKeys() : null;
            iArr[i2] = keyStrokeArr[i2] != 0 ? keyStrokeArr[i2].length : 0;
        }
        KeyStroke[] keyStrokeArr2 = new KeyStroke[iArr[0] + iArr[1] + iArr[2]];
        int i3 = 0;
        for (int i4 = 0; i4 < 3; i4++) {
            if (iArr[i4] > 0) {
                System.arraycopy(keyStrokeArr[i4], 0, keyStrokeArr2, i3, iArr[i4]);
                i3 += iArr[i4];
            }
        }
        return keyStrokeArr2;
    }

    public int getConditionForKeyStroke(KeyStroke keyStroke) {
        for (int i2 = 0; i2 < 3; i2++) {
            InputMap inputMap = getInputMap(i2, false);
            if (inputMap != null && inputMap.get(keyStroke) != null) {
                return i2;
            }
        }
        return -1;
    }

    public ActionListener getActionForKeyStroke(KeyStroke keyStroke) {
        Object obj;
        ActionMap actionMap = getActionMap(false);
        if (actionMap == null) {
            return null;
        }
        for (int i2 = 0; i2 < 3; i2++) {
            InputMap inputMap = getInputMap(i2, false);
            if (inputMap != null && (obj = inputMap.get(keyStroke)) != null) {
                Action action = actionMap.get(obj);
                if (!(action instanceof ActionStandin)) {
                    return action;
                }
                return ((ActionStandin) action).actionListener;
            }
        }
        return null;
    }

    public void resetKeyboardActions() {
        for (int i2 = 0; i2 < 3; i2++) {
            InputMap inputMap = getInputMap(i2, false);
            if (inputMap != null) {
                inputMap.clear();
            }
        }
        ActionMap actionMap = getActionMap(false);
        if (actionMap != null) {
            actionMap.clear();
        }
    }

    public final void setInputMap(int i2, InputMap inputMap) {
        switch (i2) {
            case 0:
                this.focusInputMap = inputMap;
                setFlag(5, true);
                return;
            case 1:
                this.ancestorInputMap = inputMap;
                setFlag(6, true);
                return;
            case 2:
                if (inputMap != null && !(inputMap instanceof ComponentInputMap)) {
                    throw new IllegalArgumentException("WHEN_IN_FOCUSED_WINDOW InputMaps must be of type ComponentInputMap");
                }
                this.windowInputMap = (ComponentInputMap) inputMap;
                setFlag(7, true);
                registerWithKeyboardManager(false);
                return;
            default:
                throw new IllegalArgumentException("condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
        }
    }

    public final InputMap getInputMap(int i2) {
        return getInputMap(i2, true);
    }

    public final InputMap getInputMap() {
        return getInputMap(0, true);
    }

    public final void setActionMap(ActionMap actionMap) {
        this.actionMap = actionMap;
        setFlag(8, true);
    }

    public final ActionMap getActionMap() {
        return getActionMap(true);
    }

    final InputMap getInputMap(int i2, boolean z2) {
        switch (i2) {
            case 0:
                if (getFlag(5)) {
                    return this.focusInputMap;
                }
                if (z2) {
                    InputMap inputMap = new InputMap();
                    setInputMap(i2, inputMap);
                    return inputMap;
                }
                return null;
            case 1:
                if (getFlag(6)) {
                    return this.ancestorInputMap;
                }
                if (z2) {
                    InputMap inputMap2 = new InputMap();
                    setInputMap(i2, inputMap2);
                    return inputMap2;
                }
                return null;
            case 2:
                if (getFlag(7)) {
                    return this.windowInputMap;
                }
                if (z2) {
                    ComponentInputMap componentInputMap = new ComponentInputMap(this);
                    setInputMap(i2, componentInputMap);
                    return componentInputMap;
                }
                return null;
            default:
                throw new IllegalArgumentException("condition must be one of JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED or JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT");
        }
    }

    final ActionMap getActionMap(boolean z2) {
        if (getFlag(8)) {
            return this.actionMap;
        }
        if (z2) {
            ActionMap actionMap = new ActionMap();
            setActionMap(actionMap);
            return actionMap;
        }
        return null;
    }

    @Override // java.awt.Component
    public int getBaseline(int i2, int i3) {
        super.getBaseline(i2, i3);
        if (this.ui != null) {
            return this.ui.getBaseline(this, i2, i3);
        }
        return -1;
    }

    @Override // java.awt.Component
    public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
        if (this.ui != null) {
            return this.ui.getBaselineResizeBehavior(this);
        }
        return Component.BaselineResizeBehavior.OTHER;
    }

    @Deprecated
    public boolean requestDefaultFocus() {
        Component defaultComponent;
        Container focusCycleRootAncestor = isFocusCycleRoot() ? this : getFocusCycleRootAncestor();
        if (focusCycleRootAncestor != null && (defaultComponent = focusCycleRootAncestor.getFocusTraversalPolicy().getDefaultComponent(focusCycleRootAncestor)) != null) {
            defaultComponent.requestFocus();
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    public void setVisible(boolean z2) {
        if (z2 != isVisible()) {
            super.setVisible(z2);
            if (z2) {
                Container parent = getParent();
                if (parent != null) {
                    Rectangle bounds = getBounds();
                    parent.repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
                }
                revalidate();
            }
        }
    }

    @Override // java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        boolean zIsEnabled = isEnabled();
        super.setEnabled(z2);
        firePropertyChange(Enabled.NAME, zIsEnabled, z2);
        if (z2 != zIsEnabled) {
            repaint();
        }
    }

    @Override // java.awt.Component
    public void setForeground(Color color) {
        Color foreground = getForeground();
        super.setForeground(color);
        if (foreground != null) {
            if (foreground.equals(color)) {
                return;
            }
        } else if (color == null || color.equals(foreground)) {
            return;
        }
        repaint();
    }

    @Override // java.awt.Component
    public void setBackground(Color color) {
        Color background = getBackground();
        super.setBackground(color);
        if (background != null) {
            if (background.equals(color)) {
                return;
            }
        } else if (color == null || color.equals(background)) {
            return;
        }
        repaint();
    }

    @Override // java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        Font font2 = getFont();
        super.setFont(font);
        if (font != font2) {
            revalidate();
            repaint();
        }
    }

    public static Locale getDefaultLocale() {
        Locale locale = (Locale) SwingUtilities.appContextGet(defaultLocale);
        if (locale == null) {
            locale = Locale.getDefault();
            setDefaultLocale(locale);
        }
        return locale;
    }

    public static void setDefaultLocale(Locale locale) {
        SwingUtilities.appContextPut(defaultLocale, locale);
    }

    protected void processComponentKeyEvent(KeyEvent keyEvent) {
    }

    @Override // java.awt.Component
    protected void processKeyEvent(KeyEvent keyEvent) {
        super.processKeyEvent(keyEvent);
        if (!keyEvent.isConsumed()) {
            processComponentKeyEvent(keyEvent);
        }
        boolean zShouldProcess = KeyboardState.shouldProcess(keyEvent);
        if (!keyEvent.isConsumed() && zShouldProcess) {
            if (processKeyBindings(keyEvent, keyEvent.getID() == 401)) {
                keyEvent.consume();
            }
        }
    }

    protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
        InputMap inputMap = getInputMap(i2, false);
        ActionMap actionMap = getActionMap(false);
        if (inputMap != null && actionMap != null && isEnabled()) {
            Object obj = inputMap.get(keyStroke);
            Action action = obj == null ? null : actionMap.get(obj);
            if (action != null) {
                return SwingUtilities.notifyAction(action, keyStroke, keyEvent, this, keyEvent.getModifiers());
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:57:0x00df, code lost:
    
        if (r11 == null) goto L60;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00e9, code lost:
    
        return processKeyBindingsForAllComponents(r7, r11, r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00ea, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    boolean processKeyBindings(java.awt.event.KeyEvent r7, boolean r8) {
        /*
            Method dump skipped, instructions count: 236
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: javax.swing.JComponent.processKeyBindings(java.awt.event.KeyEvent, boolean):boolean");
    }

    static boolean processKeyBindingsForAllComponents(KeyEvent keyEvent, Container container, boolean z2) {
        while (!KeyboardManager.getCurrentManager().fireKeyboardAction(keyEvent, z2, container)) {
            if (container instanceof Popup.HeavyWeightWindow) {
                container = ((Window) container).getOwner();
            } else {
                return false;
            }
        }
        return true;
    }

    public void setToolTipText(String str) {
        String toolTipText = getToolTipText();
        putClientProperty(TOOL_TIP_TEXT_KEY, str);
        ToolTipManager toolTipManagerSharedInstance = ToolTipManager.sharedInstance();
        if (str != null) {
            if (toolTipText == null) {
                toolTipManagerSharedInstance.registerComponent(this);
                return;
            }
            return;
        }
        toolTipManagerSharedInstance.unregisterComponent(this);
    }

    public String getToolTipText() {
        return (String) getClientProperty(TOOL_TIP_TEXT_KEY);
    }

    public String getToolTipText(MouseEvent mouseEvent) {
        return getToolTipText();
    }

    public Point getToolTipLocation(MouseEvent mouseEvent) {
        return null;
    }

    public Point getPopupLocation(MouseEvent mouseEvent) {
        return null;
    }

    public JToolTip createToolTip() {
        JToolTip jToolTip = new JToolTip();
        jToolTip.setComponent(this);
        return jToolTip;
    }

    public void scrollRectToVisible(Rectangle rectangle) {
        Container container;
        int x2 = getX();
        int y2 = getY();
        Container parent = getParent();
        while (true) {
            container = parent;
            if (container == null || (container instanceof JComponent) || (container instanceof CellRendererPane)) {
                break;
            }
            Rectangle bounds = container.getBounds();
            x2 += bounds.f12372x;
            y2 += bounds.f12373y;
            parent = container.getParent();
        }
        if (container != null && !(container instanceof CellRendererPane)) {
            rectangle.f12372x += x2;
            rectangle.f12373y += y2;
            ((JComponent) container).scrollRectToVisible(rectangle);
            rectangle.f12372x -= x2;
            rectangle.f12373y -= y2;
        }
    }

    public void setAutoscrolls(boolean z2) {
        setFlag(25, true);
        if (this.autoscrolls != z2) {
            this.autoscrolls = z2;
            if (z2) {
                enableEvents(16L);
                enableEvents(32L);
            } else {
                Autoscroller.stop(this);
            }
        }
    }

    public boolean getAutoscrolls() {
        return this.autoscrolls;
    }

    public void setTransferHandler(TransferHandler transferHandler) {
        TransferHandler transferHandler2 = (TransferHandler) getClientProperty(ClientPropertyKey.JComponent_TRANSFER_HANDLER);
        putClientProperty(ClientPropertyKey.JComponent_TRANSFER_HANDLER, transferHandler);
        SwingUtilities.installSwingDropTargetAsNecessary(this, transferHandler);
        firePropertyChange("transferHandler", transferHandler2, transferHandler);
    }

    @Override // javax.swing.TransferHandler.HasGetTransferHandler
    public TransferHandler getTransferHandler() {
        return (TransferHandler) getClientProperty(ClientPropertyKey.JComponent_TRANSFER_HANDLER);
    }

    TransferHandler.DropLocation dropLocationForPoint(Point point) {
        return null;
    }

    Object setDropLocation(TransferHandler.DropLocation dropLocation, Object obj, boolean z2) {
        return null;
    }

    void dndDone() {
    }

    @Override // java.awt.Component
    protected void processMouseEvent(MouseEvent mouseEvent) {
        if (this.autoscrolls && mouseEvent.getID() == 502) {
            Autoscroller.stop(this);
        }
        super.processMouseEvent(mouseEvent);
    }

    @Override // java.awt.Component
    protected void processMouseMotionEvent(MouseEvent mouseEvent) {
        boolean z2 = true;
        if (this.autoscrolls && mouseEvent.getID() == 506) {
            z2 = !Autoscroller.isRunning(this);
            Autoscroller.processMouseDragged(mouseEvent);
        }
        if (z2) {
            super.processMouseMotionEvent(mouseEvent);
        }
    }

    void superProcessMouseMotionEvent(MouseEvent mouseEvent) {
        super.processMouseMotionEvent(mouseEvent);
    }

    void setCreatedDoubleBuffer(boolean z2) {
        setFlag(9, z2);
    }

    boolean getCreatedDoubleBuffer() {
        return getFlag(9);
    }

    /* loaded from: rt.jar:javax/swing/JComponent$ActionStandin.class */
    final class ActionStandin implements Action {
        private final ActionListener actionListener;
        private final String command;
        private final Action action;

        ActionStandin(ActionListener actionListener, String str) {
            this.actionListener = actionListener;
            if (actionListener instanceof Action) {
                this.action = (Action) actionListener;
            } else {
                this.action = null;
            }
            this.command = str;
        }

        @Override // javax.swing.Action
        public Object getValue(String str) {
            if (str != null) {
                if (str.equals(Action.ACTION_COMMAND_KEY)) {
                    return this.command;
                }
                if (this.action != null) {
                    return this.action.getValue(str);
                }
                if (str.equals("Name")) {
                    return "ActionStandin";
                }
                return null;
            }
            return null;
        }

        @Override // javax.swing.Action
        public boolean isEnabled() {
            if (this.actionListener == null) {
                return false;
            }
            if (this.action == null) {
                return true;
            }
            return this.action.isEnabled();
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (this.actionListener != null) {
                this.actionListener.actionPerformed(actionEvent);
            }
        }

        @Override // javax.swing.Action
        public void putValue(String str, Object obj) {
        }

        @Override // javax.swing.Action
        public void setEnabled(boolean z2) {
        }

        @Override // javax.swing.Action
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        @Override // javax.swing.Action
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }
    }

    /* loaded from: rt.jar:javax/swing/JComponent$IntVector.class */
    static final class IntVector {
        int[] array = null;
        int count = 0;
        int capacity = 0;

        IntVector() {
        }

        int size() {
            return this.count;
        }

        int elementAt(int i2) {
            return this.array[i2];
        }

        void addElement(int i2) {
            if (this.count == this.capacity) {
                this.capacity = (this.capacity + 2) * 2;
                int[] iArr = new int[this.capacity];
                if (this.count > 0) {
                    System.arraycopy(this.array, 0, iArr, 0, this.count);
                }
                this.array = iArr;
            }
            int[] iArr2 = this.array;
            int i3 = this.count;
            this.count = i3 + 1;
            iArr2[i3] = i2;
        }

        void setElementAt(int i2, int i3) {
            this.array[i3] = i2;
        }
    }

    /* loaded from: rt.jar:javax/swing/JComponent$KeyboardState.class */
    static class KeyboardState implements Serializable {
        private static final Object keyCodesKey = KeyboardState.class;

        KeyboardState() {
        }

        static IntVector getKeyCodeArray() {
            IntVector intVector = (IntVector) SwingUtilities.appContextGet(keyCodesKey);
            if (intVector == null) {
                intVector = new IntVector();
                SwingUtilities.appContextPut(keyCodesKey, intVector);
            }
            return intVector;
        }

        static void registerKeyPressed(int i2) {
            IntVector keyCodeArray = getKeyCodeArray();
            int size = keyCodeArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                if (keyCodeArray.elementAt(i3) == -1) {
                    keyCodeArray.setElementAt(i2, i3);
                    return;
                }
            }
            keyCodeArray.addElement(i2);
        }

        static void registerKeyReleased(int i2) {
            IntVector keyCodeArray = getKeyCodeArray();
            int size = keyCodeArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                if (keyCodeArray.elementAt(i3) == i2) {
                    keyCodeArray.setElementAt(-1, i3);
                    return;
                }
            }
        }

        static boolean keyIsPressed(int i2) {
            IntVector keyCodeArray = getKeyCodeArray();
            int size = keyCodeArray.size();
            for (int i3 = 0; i3 < size; i3++) {
                if (keyCodeArray.elementAt(i3) == i2) {
                    return true;
                }
            }
            return false;
        }

        static boolean shouldProcess(KeyEvent keyEvent) {
            switch (keyEvent.getID()) {
                case 400:
                    return true;
                case 401:
                    if (!keyIsPressed(keyEvent.getKeyCode())) {
                        registerKeyPressed(keyEvent.getKeyCode());
                        return true;
                    }
                    return true;
                case 402:
                    if (keyIsPressed(keyEvent.getKeyCode()) || keyEvent.getKeyCode() == 154) {
                        registerKeyReleased(keyEvent.getKeyCode());
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        }
    }

    @Override // java.awt.Component
    @Deprecated
    public void enable() {
        if (!isEnabled()) {
            super.enable();
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.ENABLED);
            }
        }
    }

    @Override // java.awt.Component
    @Deprecated
    public void disable() {
        if (isEnabled()) {
            super.disable();
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.ENABLED, null);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JComponent$AccessibleJComponent.class */
    public abstract class AccessibleJComponent extends Container.AccessibleAWTContainer implements AccessibleExtendedComponent {
        private volatile transient int propertyListenersCount;

        @Deprecated
        protected FocusListener accessibleFocusHandler;

        @Override // java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Accessible getAccessibleAt(Point point) {
            return super.getAccessibleAt(point);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void removeFocusListener(FocusListener focusListener) {
            super.removeFocusListener(focusListener);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void addFocusListener(FocusListener focusListener) {
            super.addFocusListener(focusListener);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void requestFocus() {
            super.requestFocus();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ boolean isFocusTraversable() {
            return super.isFocusTraversable();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setSize(Dimension dimension) {
            super.setSize(dimension);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Dimension getSize() {
            return super.getSize();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setBounds(Rectangle rectangle) {
            super.setBounds(rectangle);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Rectangle getBounds() {
            return super.getBounds();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setLocation(Point point) {
            super.setLocation(point);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Point getLocation() {
            return super.getLocation();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Point getLocationOnScreen() {
            return super.getLocationOnScreen();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ boolean contains(Point point) {
            return super.contains(point);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ boolean isShowing() {
            return super.isShowing();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setVisible(boolean z2) {
            super.setVisible(z2);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ boolean isVisible() {
            return super.isVisible();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setEnabled(boolean z2) {
            super.setEnabled(z2);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ boolean isEnabled() {
            return super.isEnabled();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ FontMetrics getFontMetrics(Font font) {
            return super.getFontMetrics(font);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setFont(Font font) {
            super.setFont(font);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Font getFont() {
            return super.getFont();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setCursor(Cursor cursor) {
            super.setCursor(cursor);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Cursor getCursor() {
            return super.getCursor();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setForeground(Color color) {
            super.setForeground(color);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Color getForeground() {
            return super.getForeground();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ void setBackground(Color color) {
            super.setBackground(color);
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public /* bridge */ /* synthetic */ Color getBackground() {
            return super.getBackground();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public /* bridge */ /* synthetic */ AccessibleComponent getAccessibleComponent() {
            return super.getAccessibleComponent();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public /* bridge */ /* synthetic */ Locale getLocale() {
            return super.getLocale();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public /* bridge */ /* synthetic */ int getAccessibleIndexInParent() {
            return super.getAccessibleIndexInParent();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public /* bridge */ /* synthetic */ Accessible getAccessibleParent() {
            return super.getAccessibleParent();
        }

        protected AccessibleJComponent() {
            super();
            this.propertyListenersCount = 0;
            this.accessibleFocusHandler = null;
        }

        /* loaded from: rt.jar:javax/swing/JComponent$AccessibleJComponent$AccessibleContainerHandler.class */
        protected class AccessibleContainerHandler implements ContainerListener {
            protected AccessibleContainerHandler() {
            }

            @Override // java.awt.event.ContainerListener
            public void componentAdded(ContainerEvent containerEvent) {
                Component child = containerEvent.getChild();
                if (child != null && (child instanceof Accessible)) {
                    AccessibleJComponent.this.firePropertyChange(AccessibleContext.ACCESSIBLE_CHILD_PROPERTY, null, child.getAccessibleContext());
                }
            }

            @Override // java.awt.event.ContainerListener
            public void componentRemoved(ContainerEvent containerEvent) {
                Component child = containerEvent.getChild();
                if (child != null && (child instanceof Accessible)) {
                    AccessibleJComponent.this.firePropertyChange(AccessibleContext.ACCESSIBLE_CHILD_PROPERTY, child.getAccessibleContext(), null);
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/JComponent$AccessibleJComponent$AccessibleFocusHandler.class */
        protected class AccessibleFocusHandler implements FocusListener {
            protected AccessibleFocusHandler() {
            }

            @Override // java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                if (JComponent.this.accessibleContext != null) {
                    JComponent.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.FOCUSED);
                }
            }

            @Override // java.awt.event.FocusListener
            public void focusLost(FocusEvent focusEvent) {
                if (JComponent.this.accessibleContext != null) {
                    JComponent.this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.FOCUSED, null);
                }
            }
        }

        @Override // java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            super.addPropertyChangeListener(propertyChangeListener);
        }

        @Override // java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
            super.removePropertyChangeListener(propertyChangeListener);
        }

        protected String getBorderTitle(Border border) {
            if (border instanceof TitledBorder) {
                return ((TitledBorder) border).getTitle();
            }
            if (border instanceof CompoundBorder) {
                String borderTitle = getBorderTitle(((CompoundBorder) border).getInsideBorder());
                if (borderTitle == null) {
                    borderTitle = getBorderTitle(((CompoundBorder) border).getOutsideBorder());
                }
                return borderTitle;
            }
            return null;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            AccessibleContext accessibleContext;
            String accessibleName = this.accessibleName;
            if (accessibleName == null) {
                accessibleName = (String) JComponent.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            }
            if (accessibleName == null) {
                accessibleName = getBorderTitle(JComponent.this.getBorder());
            }
            if (accessibleName == null) {
                Object clientProperty = JComponent.this.getClientProperty("labeledBy");
                if ((clientProperty instanceof Accessible) && (accessibleContext = ((Accessible) clientProperty).getAccessibleContext()) != null) {
                    accessibleName = accessibleContext.getAccessibleName();
                }
            }
            return accessibleName;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            AccessibleContext accessibleContext;
            String toolTipText = this.accessibleDescription;
            if (toolTipText == null) {
                toolTipText = (String) JComponent.this.getClientProperty(AccessibleContext.ACCESSIBLE_DESCRIPTION_PROPERTY);
            }
            if (toolTipText == null) {
                try {
                    toolTipText = getToolTipText();
                } catch (Exception e2) {
                }
            }
            if (toolTipText == null) {
                Object clientProperty = JComponent.this.getClientProperty("labeledBy");
                if ((clientProperty instanceof Accessible) && (accessibleContext = ((Accessible) clientProperty).getAccessibleContext()) != null) {
                    toolTipText = accessibleContext.getAccessibleDescription();
                }
            }
            return toolTipText;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SWING_COMPONENT;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JComponent.this.isOpaque()) {
                accessibleStateSet.add(AccessibleState.OPAQUE);
            }
            return accessibleStateSet;
        }

        @Override // java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return super.getAccessibleChildrenCount();
        }

        @Override // java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            return super.getAccessibleChild(i2);
        }

        AccessibleExtendedComponent getAccessibleExtendedComponent() {
            return this;
        }

        public String getToolTipText() {
            return JComponent.this.getToolTipText();
        }

        public String getTitledBorderText() {
            Border border = JComponent.this.getBorder();
            if (border instanceof TitledBorder) {
                return ((TitledBorder) border).getTitle();
            }
            return null;
        }

        public AccessibleKeyBinding getAccessibleKeyBinding() {
            AccessibleContext accessibleContext;
            Object clientProperty = JComponent.this.getClientProperty("labeledBy");
            if ((clientProperty instanceof Accessible) && (accessibleContext = ((Accessible) clientProperty).getAccessibleContext()) != null) {
                AccessibleComponent accessibleComponent = accessibleContext.getAccessibleComponent();
                if (!(accessibleComponent instanceof AccessibleExtendedComponent)) {
                    return null;
                }
                return ((AccessibleExtendedComponent) accessibleComponent).getAccessibleKeyBinding();
            }
            return null;
        }
    }

    private ArrayTable getClientProperties() {
        if (this.clientProperties == null) {
            this.clientProperties = new ArrayTable();
        }
        return this.clientProperties;
    }

    public final Object getClientProperty(Object obj) {
        Object obj2;
        if (obj == SwingUtilities2.AA_TEXT_PROPERTY_KEY) {
            return this.aaTextInfo;
        }
        if (obj == SwingUtilities2.COMPONENT_UI_PROPERTY_KEY) {
            return this.ui;
        }
        if (this.clientProperties == null) {
            return null;
        }
        synchronized (this.clientProperties) {
            obj2 = this.clientProperties.get(obj);
        }
        return obj2;
    }

    public final void putClientProperty(Object obj, Object obj2) {
        if (obj == SwingUtilities2.AA_TEXT_PROPERTY_KEY) {
            this.aaTextInfo = obj2;
            return;
        }
        if (obj2 == null && this.clientProperties == null) {
            return;
        }
        ArrayTable clientProperties = getClientProperties();
        synchronized (clientProperties) {
            Object obj3 = clientProperties.get(obj);
            if (obj2 != null) {
                clientProperties.put(obj, obj2);
            } else if (obj3 == null) {
                return;
            } else {
                clientProperties.remove(obj);
            }
            clientPropertyChanged(obj, obj3, obj2);
            firePropertyChange(obj.toString(), obj3, obj2);
        }
    }

    void clientPropertyChanged(Object obj, Object obj2, Object obj3) {
    }

    void setUIProperty(String str, Object obj) {
        if (str == "opaque") {
            if (!getFlag(24)) {
                setOpaque(((Boolean) obj).booleanValue());
                setFlag(24, false);
                return;
            }
            return;
        }
        if (str == "autoscrolls") {
            if (!getFlag(25)) {
                setAutoscrolls(((Boolean) obj).booleanValue());
                setFlag(25, false);
                return;
            }
            return;
        }
        if (str == "focusTraversalKeysForward") {
            if (!getFlag(26)) {
                super.setFocusTraversalKeys(0, (Set) obj);
            }
        } else {
            if (str == "focusTraversalKeysBackward") {
                if (!getFlag(27)) {
                    super.setFocusTraversalKeys(1, (Set) obj);
                    return;
                }
                return;
            }
            throw new IllegalArgumentException("property \"" + str + "\" cannot be set using this method");
        }
    }

    @Override // java.awt.Container, java.awt.Component
    public void setFocusTraversalKeys(int i2, Set<? extends AWTKeyStroke> set) {
        if (i2 == 0) {
            setFlag(26, true);
        } else if (i2 == 1) {
            setFlag(27, true);
        }
        super.setFocusTraversalKeys(i2, set);
    }

    public static boolean isLightweightComponent(Component component) {
        return component.getPeer() instanceof LightweightPeer;
    }

    @Override // java.awt.Component
    @Deprecated
    public void reshape(int i2, int i3, int i4, int i5) {
        super.reshape(i2, i3, i4, i5);
    }

    @Override // java.awt.Component
    public Rectangle getBounds(Rectangle rectangle) {
        if (rectangle == null) {
            return new Rectangle(getX(), getY(), getWidth(), getHeight());
        }
        rectangle.setBounds(getX(), getY(), getWidth(), getHeight());
        return rectangle;
    }

    @Override // java.awt.Component
    public Dimension getSize(Dimension dimension) {
        if (dimension == null) {
            return new Dimension(getWidth(), getHeight());
        }
        dimension.setSize(getWidth(), getHeight());
        return dimension;
    }

    @Override // java.awt.Component
    public Point getLocation(Point point) {
        if (point == null) {
            return new Point(getX(), getY());
        }
        point.setLocation(getX(), getY());
        return point;
    }

    @Override // java.awt.Component
    public int getX() {
        return super.getX();
    }

    @Override // java.awt.Component
    public int getY() {
        return super.getY();
    }

    @Override // java.awt.Component
    public int getWidth() {
        return super.getWidth();
    }

    @Override // java.awt.Component
    public int getHeight() {
        return super.getHeight();
    }

    @Override // java.awt.Component
    public boolean isOpaque() {
        return getFlag(3);
    }

    public void setOpaque(boolean z2) {
        boolean flag = getFlag(3);
        setFlag(3, z2);
        setFlag(24, true);
        firePropertyChange("opaque", flag, z2);
    }

    boolean rectangleIsObscured(int i2, int i3, int i4, int i5) {
        int componentCount = getComponentCount();
        for (int i6 = 0; i6 < componentCount; i6++) {
            Component component = getComponent(i6);
            int x2 = component.getX();
            int y2 = component.getY();
            int width = component.getWidth();
            int height = component.getHeight();
            if (i2 >= x2 && i2 + i4 <= x2 + width && i3 >= y2 && i3 + i5 <= y2 + height && component.isVisible()) {
                if (component instanceof JComponent) {
                    return component.isOpaque();
                }
                return false;
            }
        }
        return false;
    }

    static final void computeVisibleRect(Component component, Rectangle rectangle) {
        Container parent = component.getParent();
        Rectangle bounds = component.getBounds();
        if (parent == null || (parent instanceof Window) || (parent instanceof Applet)) {
            rectangle.setBounds(0, 0, bounds.width, bounds.height);
            return;
        }
        computeVisibleRect(parent, rectangle);
        rectangle.f12372x -= bounds.f12372x;
        rectangle.f12373y -= bounds.f12373y;
        SwingUtilities.computeIntersection(0, 0, bounds.width, bounds.height, rectangle);
    }

    public void computeVisibleRect(Rectangle rectangle) {
        computeVisibleRect(this, rectangle);
    }

    public Rectangle getVisibleRect() {
        Rectangle rectangle = new Rectangle();
        computeVisibleRect(rectangle);
        return rectangle;
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, boolean z2, boolean z3) {
        super.firePropertyChange(str, z2, z3);
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, int i2, int i3) {
        super.firePropertyChange(str, i2, i3);
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, char c2, char c3) {
        super.firePropertyChange(str, c2, c3);
    }

    protected void fireVetoableChange(String str, Object obj, Object obj2) throws PropertyVetoException {
        if (this.vetoableChangeSupport == null) {
            return;
        }
        this.vetoableChangeSupport.fireVetoableChange(str, obj, obj2);
    }

    public synchronized void addVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (this.vetoableChangeSupport == null) {
            this.vetoableChangeSupport = new VetoableChangeSupport(this);
        }
        this.vetoableChangeSupport.addVetoableChangeListener(vetoableChangeListener);
    }

    public synchronized void removeVetoableChangeListener(VetoableChangeListener vetoableChangeListener) {
        if (this.vetoableChangeSupport == null) {
            return;
        }
        this.vetoableChangeSupport.removeVetoableChangeListener(vetoableChangeListener);
    }

    public synchronized VetoableChangeListener[] getVetoableChangeListeners() {
        if (this.vetoableChangeSupport == null) {
            return new VetoableChangeListener[0];
        }
        return this.vetoableChangeSupport.getVetoableChangeListeners();
    }

    public Container getTopLevelAncestor() {
        Container container;
        Container parent = this;
        while (true) {
            container = parent;
            if (container != null) {
                if ((container instanceof Window) || (container instanceof Applet)) {
                    break;
                }
                parent = container.getParent();
            } else {
                return null;
            }
        }
        return container;
    }

    private AncestorNotifier getAncestorNotifier() {
        return (AncestorNotifier) getClientProperty(ClientPropertyKey.JComponent_ANCESTOR_NOTIFIER);
    }

    public void addAncestorListener(AncestorListener ancestorListener) {
        AncestorNotifier ancestorNotifier = getAncestorNotifier();
        if (ancestorNotifier == null) {
            ancestorNotifier = new AncestorNotifier(this);
            putClientProperty(ClientPropertyKey.JComponent_ANCESTOR_NOTIFIER, ancestorNotifier);
        }
        ancestorNotifier.addAncestorListener(ancestorListener);
    }

    public void removeAncestorListener(AncestorListener ancestorListener) {
        AncestorNotifier ancestorNotifier = getAncestorNotifier();
        if (ancestorNotifier == null) {
            return;
        }
        ancestorNotifier.removeAncestorListener(ancestorListener);
        if (ancestorNotifier.listenerList.getListenerList().length == 0) {
            ancestorNotifier.removeAllListeners();
            putClientProperty(ClientPropertyKey.JComponent_ANCESTOR_NOTIFIER, null);
        }
    }

    public AncestorListener[] getAncestorListeners() {
        AncestorNotifier ancestorNotifier = getAncestorNotifier();
        if (ancestorNotifier == null) {
            return new AncestorListener[0];
        }
        return ancestorNotifier.getAncestorListeners();
    }

    @Override // java.awt.Container, java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        EventListener[] listeners;
        if (cls == AncestorListener.class) {
            listeners = getAncestorListeners();
        } else if (cls == VetoableChangeListener.class) {
            listeners = getVetoableChangeListeners();
        } else if (cls == PropertyChangeListener.class) {
            listeners = getPropertyChangeListeners();
        } else {
            listeners = this.listenerList.getListeners(cls);
        }
        if (listeners.length == 0) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) listeners;
    }

    @Override // java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        firePropertyChange("ancestor", (Object) null, getParent());
        registerWithKeyboardManager(false);
        registerNextFocusableComponent();
    }

    @Override // java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
        firePropertyChange("ancestor", getParent(), (Object) null);
        unregisterWithKeyboardManager();
        deregisterNextFocusableComponent();
        if (getCreatedDoubleBuffer()) {
            RepaintManager.currentManager(this).resetDoubleBuffer();
            setCreatedDoubleBuffer(false);
        }
        if (this.autoscrolls) {
            Autoscroller.stop(this);
        }
    }

    @Override // java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
        RepaintManager.currentManager(SunToolkit.targetToAppContext(this)).addDirtyRegion(this, i2, i3, i4, i5);
    }

    public void repaint(Rectangle rectangle) {
        repaint(0L, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    @Override // java.awt.Component
    public void revalidate() {
        if (getParent() == null) {
            return;
        }
        if (SunToolkit.isDispatchThreadForAppContext(this)) {
            invalidate();
            RepaintManager.currentManager(this).addInvalidComponent(this);
        } else {
            if (this.revalidateRunnableScheduled.getAndSet(true)) {
                return;
            }
            SunToolkit.executeOnEventHandlerThread(this, () -> {
                this.revalidateRunnableScheduled.set(false);
                revalidate();
            });
        }
    }

    @Override // java.awt.Container
    public boolean isValidateRoot() {
        return false;
    }

    public boolean isOptimizedDrawingEnabled() {
        return true;
    }

    protected boolean isPaintingOrigin() {
        return false;
    }

    public void paintImmediately(int i2, int i3, int i4, int i5) {
        Container parent;
        JComponent jComponent = this;
        if (!isShowing()) {
            return;
        }
        JComponent paintingOrigin = SwingUtilities.getPaintingOrigin(this);
        if (paintingOrigin != null) {
            Rectangle rectangleConvertRectangle = SwingUtilities.convertRectangle(jComponent, new Rectangle(i2, i3, i4, i5), paintingOrigin);
            paintingOrigin.paintImmediately(rectangleConvertRectangle.f12372x, rectangleConvertRectangle.f12373y, rectangleConvertRectangle.width, rectangleConvertRectangle.height);
            return;
        }
        while (!jComponent.isOpaque() && (parent = jComponent.getParent()) != null) {
            i2 += jComponent.getX();
            i3 += jComponent.getY();
            jComponent = parent;
            if (!(jComponent instanceof JComponent)) {
                break;
            }
        }
        if (jComponent instanceof JComponent) {
            jComponent._paintImmediately(i2, i3, i4, i5);
        } else {
            jComponent.repaint(i2, i3, i4, i5);
        }
    }

    public void paintImmediately(Rectangle rectangle) {
        paintImmediately(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    boolean alwaysOnTop() {
        return false;
    }

    void setPaintingChild(Component component) {
        this.paintingChild = component;
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Multi-variable type inference failed */
    void _paintImmediately(int i2, int i3, int i4, int i5) {
        boolean z2;
        int i6 = 0;
        int i7 = 0;
        boolean z3 = false;
        JComponent jComponent = null;
        JComponent jComponent2 = this;
        RepaintManager repaintManagerCurrentManager = RepaintManager.currentManager(this);
        ArrayList arrayList = new ArrayList(7);
        int i8 = -1;
        int i9 = 0;
        Rectangle rectangleFetchRectangle = fetchRectangle();
        rectangleFetchRectangle.f12372x = i2;
        rectangleFetchRectangle.f12373y = i3;
        rectangleFetchRectangle.width = i4;
        rectangleFetchRectangle.height = i5;
        boolean z4 = alwaysOnTop() && isOpaque();
        if (z4) {
            SwingUtilities.computeIntersection(0, 0, getWidth(), getHeight(), rectangleFetchRectangle);
            if (rectangleFetchRectangle.width == 0) {
                recycleRectangle(rectangleFetchRectangle);
                return;
            }
        }
        JComponent parent = this;
        JComponent jComponent3 = null;
        while (parent != null && !(parent instanceof Window) && !(parent instanceof Applet)) {
            JComponent jComponent4 = parent instanceof JComponent ? parent : null;
            arrayList.add(parent);
            if (!z4 && jComponent4 != null && !jComponent4.isOptimizedDrawingEnabled()) {
                if (parent == this) {
                    z2 = false;
                } else if (!jComponent4.isPaintingOrigin()) {
                    Component[] components = parent.getComponents();
                    int i10 = 0;
                    while (i10 < components.length && components[i10] != jComponent3) {
                        i10++;
                    }
                    switch (jComponent4.getObscuredState(i10, rectangleFetchRectangle.f12372x, rectangleFetchRectangle.f12373y, rectangleFetchRectangle.width, rectangleFetchRectangle.height)) {
                        case 0:
                            z2 = false;
                            break;
                        case 2:
                            recycleRectangle(rectangleFetchRectangle);
                            return;
                        default:
                            z2 = true;
                            break;
                    }
                } else {
                    z2 = true;
                }
                if (z2) {
                    jComponent2 = jComponent4;
                    i8 = i9;
                    i7 = 0;
                    i6 = 0;
                    z3 = false;
                }
            }
            i9++;
            if (repaintManagerCurrentManager.isDoubleBufferingEnabled() && jComponent4 != null && jComponent4.isDoubleBuffered()) {
                z3 = true;
                jComponent = jComponent4;
            }
            if (!z4) {
                int x2 = parent.getX();
                int y2 = parent.getY();
                SwingUtilities.computeIntersection(0, 0, parent.getWidth(), parent.getHeight(), rectangleFetchRectangle);
                rectangleFetchRectangle.f12372x += x2;
                rectangleFetchRectangle.f12373y += y2;
                i6 += x2;
                i7 += y2;
            }
            jComponent3 = parent;
            parent = parent.getParent();
        }
        if (parent == null || parent.getPeer() == null || rectangleFetchRectangle.width <= 0 || rectangleFetchRectangle.height <= 0) {
            recycleRectangle(rectangleFetchRectangle);
            return;
        }
        jComponent2.setFlag(13, true);
        rectangleFetchRectangle.f12372x -= i6;
        rectangleFetchRectangle.f12373y -= i7;
        if (jComponent2 != this) {
            for (int i11 = i8; i11 > 0; i11--) {
                Component component = (Component) arrayList.get(i11);
                if (component instanceof JComponent) {
                    ((JComponent) component).setPaintingChild((Component) arrayList.get(i11 - 1));
                }
            }
        }
        try {
            Graphics graphicsSafelyGetGraphics = safelyGetGraphics(jComponent2, parent);
            if (graphicsSafelyGetGraphics != null) {
                try {
                    if (z3) {
                        RepaintManager repaintManagerCurrentManager2 = RepaintManager.currentManager(jComponent);
                        repaintManagerCurrentManager2.beginPaint();
                        try {
                            repaintManagerCurrentManager2.paint(jComponent2, jComponent, graphicsSafelyGetGraphics, rectangleFetchRectangle.f12372x, rectangleFetchRectangle.f12373y, rectangleFetchRectangle.width, rectangleFetchRectangle.height);
                            repaintManagerCurrentManager2.endPaint();
                        } catch (Throwable th) {
                            repaintManagerCurrentManager2.endPaint();
                            throw th;
                        }
                    } else {
                        graphicsSafelyGetGraphics.setClip(rectangleFetchRectangle.f12372x, rectangleFetchRectangle.f12373y, rectangleFetchRectangle.width, rectangleFetchRectangle.height);
                        jComponent2.paint(graphicsSafelyGetGraphics);
                    }
                    graphicsSafelyGetGraphics.dispose();
                } catch (Throwable th2) {
                    graphicsSafelyGetGraphics.dispose();
                    throw th2;
                }
            }
            if (jComponent2 != this) {
                for (int i12 = i8; i12 > 0; i12--) {
                    Component component2 = (Component) arrayList.get(i12);
                    if (component2 instanceof JComponent) {
                        ((JComponent) component2).setPaintingChild(null);
                    }
                }
            }
            jComponent2.setFlag(13, false);
            recycleRectangle(rectangleFetchRectangle);
        } catch (Throwable th3) {
            if (jComponent2 != this) {
                for (int i13 = i8; i13 > 0; i13--) {
                    Component component3 = (Component) arrayList.get(i13);
                    if (component3 instanceof JComponent) {
                        ((JComponent) component3).setPaintingChild(null);
                    }
                }
            }
            jComponent2.setFlag(13, false);
            throw th3;
        }
    }

    void paintToOffscreen(Graphics graphics, int i2, int i3, int i4, int i5, int i6, int i7) {
        try {
            setFlag(1, true);
            if (i3 + i5 < i7 || i2 + i4 < i6) {
                setFlag(2, true);
            }
            if (getFlag(13)) {
                paint(graphics);
            } else {
                if (!rectangleIsObscured(i2, i3, i4, i5)) {
                    paintComponent(graphics);
                    paintBorder(graphics);
                }
                paintChildren(graphics);
            }
        } finally {
            setFlag(1, false);
            setFlag(2, false);
        }
    }

    private int getObscuredState(int i2, int i3, int i4, int i5, int i6) {
        boolean zIsOpaque;
        int i7 = 0;
        Rectangle rectangleFetchRectangle = fetchRectangle();
        for (int i8 = i2 - 1; i8 >= 0; i8--) {
            Component component = getComponent(i8);
            if (component.isVisible()) {
                if (component instanceof JComponent) {
                    zIsOpaque = component.isOpaque();
                    if (zIsOpaque || i7 != 1) {
                    }
                } else {
                    zIsOpaque = true;
                }
                Rectangle bounds = component.getBounds(rectangleFetchRectangle);
                if (zIsOpaque && i3 >= bounds.f12372x && i3 + i5 <= bounds.f12372x + bounds.width && i4 >= bounds.f12373y && i4 + i6 <= bounds.f12373y + bounds.height) {
                    recycleRectangle(rectangleFetchRectangle);
                    return 2;
                }
                if (i7 == 0 && i3 + i5 > bounds.f12372x && i4 + i6 > bounds.f12373y && i3 < bounds.f12372x + bounds.width && i4 < bounds.f12373y + bounds.height) {
                    i7 = 1;
                }
            }
        }
        recycleRectangle(rectangleFetchRectangle);
        return i7;
    }

    boolean checkIfChildObscuredBySibling() {
        return true;
    }

    private void setFlag(int i2, boolean z2) {
        if (z2) {
            this.flags |= 1 << i2;
        } else {
            this.flags &= (1 << i2) ^ (-1);
        }
    }

    private boolean getFlag(int i2) {
        int i3 = 1 << i2;
        return (this.flags & i3) == i3;
    }

    static void setWriteObjCounter(JComponent jComponent, byte b2) {
        jComponent.flags = (jComponent.flags & (-4177921)) | (b2 << 14);
    }

    static byte getWriteObjCounter(JComponent jComponent) {
        return (byte) ((jComponent.flags >> 14) & 255);
    }

    public void setDoubleBuffered(boolean z2) {
        setFlag(0, z2);
    }

    @Override // java.awt.Component
    public boolean isDoubleBuffered() {
        return getFlag(0);
    }

    public JRootPane getRootPane() {
        return SwingUtilities.getRootPane(this);
    }

    void compWriteObjectNotify() {
        byte writeObjCounter = getWriteObjCounter(this);
        setWriteObjCounter(this, (byte) (writeObjCounter + 1));
        if (writeObjCounter != 0) {
            return;
        }
        uninstallUIAndProperties();
        if (getToolTipText() != null || (this instanceof JTableHeader)) {
            ToolTipManager.sharedInstance().unregisterComponent(this);
        }
    }

    /* loaded from: rt.jar:javax/swing/JComponent$ReadObjectCallback.class */
    private class ReadObjectCallback implements ObjectInputValidation {
        private final Vector<JComponent> roots = new Vector<>(1);
        private final ObjectInputStream inputStream;

        ReadObjectCallback(ObjectInputStream objectInputStream) throws Exception {
            this.inputStream = objectInputStream;
            objectInputStream.registerValidation(this, 0);
        }

        @Override // java.io.ObjectInputValidation
        public void validateObject() throws InvalidObjectException {
            try {
                Iterator<JComponent> it = this.roots.iterator();
                while (it.hasNext()) {
                    SwingUtilities.updateComponentTreeUI(it.next());
                }
                JComponent.readObjectCallbacks.remove(this.inputStream);
            } catch (Throwable th) {
                JComponent.readObjectCallbacks.remove(this.inputStream);
                throw th;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void registerComponent(JComponent jComponent) {
            Iterator<JComponent> it = this.roots.iterator();
            while (it.hasNext()) {
                JComponent next = it.next();
                Container parent = jComponent;
                while (true) {
                    JComponent jComponent2 = parent;
                    if (jComponent2 != null) {
                        if (jComponent2 != next) {
                            parent = jComponent2.getParent();
                        } else {
                            return;
                        }
                    }
                }
            }
            int i2 = 0;
            while (i2 < this.roots.size()) {
                Container parent2 = this.roots.elementAt(i2).getParent();
                while (true) {
                    JComponent jComponent3 = parent2;
                    if (jComponent3 == null) {
                        break;
                    }
                    if (jComponent3 != jComponent) {
                        parent2 = jComponent3.getParent();
                    } else {
                        int i3 = i2;
                        i2--;
                        this.roots.removeElementAt(i3);
                        break;
                    }
                }
                i2++;
            }
            this.roots.addElement(jComponent);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        ReadObjectCallback readObjectCallback = readObjectCallbacks.get(objectInputStream);
        if (readObjectCallback == null) {
            try {
                Hashtable<ObjectInputStream, ReadObjectCallback> hashtable = readObjectCallbacks;
                ReadObjectCallback readObjectCallback2 = new ReadObjectCallback(objectInputStream);
                readObjectCallback = readObjectCallback2;
                hashtable.put(objectInputStream, readObjectCallback2);
            } catch (Exception e2) {
                throw new IOException(e2.toString());
            }
        }
        readObjectCallback.registerComponent(this);
        int i2 = objectInputStream.readInt();
        if (i2 > 0) {
            this.clientProperties = new ArrayTable();
            for (int i3 = 0; i3 < i2; i3++) {
                this.clientProperties.put(objectInputStream.readObject(), objectInputStream.readObject());
            }
        }
        if (getToolTipText() != null) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
        setWriteObjCounter(this, (byte) 0);
        this.revalidateRunnableScheduled = new AtomicBoolean(false);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (getWriteObjCounter(this) - 1);
            setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
        ArrayTable.writeArrayTable(objectOutputStream, this.clientProperties);
    }

    @Override // java.awt.Container, java.awt.Component
    protected String paramString() {
        String string;
        String string2 = isPreferredSizeSet() ? getPreferredSize().toString() : "";
        String string3 = isMinimumSizeSet() ? getMinimumSize().toString() : "";
        String string4 = isMaximumSizeSet() ? getMaximumSize().toString() : "";
        if (this.border == null) {
            string = "";
        } else {
            string = this.border == this ? "this" : this.border.toString();
        }
        return super.paramString() + ",alignmentX=" + this.alignmentX + ",alignmentY=" + this.alignmentY + ",border=" + string + ",flags=" + this.flags + ",maximumSize=" + string4 + ",minimumSize=" + string3 + ",preferredSize=" + string2;
    }

    @Override // java.awt.Component
    @Deprecated
    public void hide() {
        boolean zIsShowing = isShowing();
        super.hide();
        if (zIsShowing) {
            Container parent = getParent();
            if (parent != null) {
                Rectangle bounds = getBounds();
                parent.repaint(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
            }
            revalidate();
        }
    }
}
