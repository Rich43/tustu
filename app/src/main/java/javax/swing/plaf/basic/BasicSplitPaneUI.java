package javax.swing.plaf.basic;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager2;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.peer.ComponentPeer;
import java.awt.peer.LightweightPeer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SplitPaneUI;
import javax.swing.plaf.UIResource;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI.class */
public class BasicSplitPaneUI extends SplitPaneUI {
    protected static final String NON_CONTINUOUS_DIVIDER = "nonContinuousDivider";
    protected static int KEYBOARD_DIVIDER_MOVE_OFFSET = 3;
    protected JSplitPane splitPane;
    protected BasicHorizontalLayoutManager layoutManager;
    protected BasicSplitPaneDivider divider;
    protected PropertyChangeListener propertyChangeListener;
    protected FocusListener focusListener;
    private Handler handler;
    private Set<KeyStroke> managingFocusForwardTraversalKeys;
    private Set<KeyStroke> managingFocusBackwardTraversalKeys;
    protected int dividerSize;
    protected Component nonContinuousLayoutDivider;
    protected boolean draggingHW;
    protected int beginDragDividerLocation;

    @Deprecated
    protected KeyStroke upKey;

    @Deprecated
    protected KeyStroke downKey;

    @Deprecated
    protected KeyStroke leftKey;

    @Deprecated
    protected KeyStroke rightKey;

    @Deprecated
    protected KeyStroke homeKey;

    @Deprecated
    protected KeyStroke endKey;

    @Deprecated
    protected KeyStroke dividerResizeToggleKey;

    @Deprecated
    protected ActionListener keyboardUpLeftListener;

    @Deprecated
    protected ActionListener keyboardDownRightListener;

    @Deprecated
    protected ActionListener keyboardHomeListener;

    @Deprecated
    protected ActionListener keyboardEndListener;

    @Deprecated
    protected ActionListener keyboardResizeToggleListener;
    private int orientation;
    private int lastDragLocation;
    private boolean continuousLayout;
    private boolean dividerKeyboardResize;
    private boolean dividerLocationIsSet;
    private Color dividerDraggingColor;
    private boolean rememberPaneSizes;
    private boolean keepHidden = false;
    boolean painted;
    boolean ignoreDividerLocationChange;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicSplitPaneUI();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("negativeIncrement"));
        lazyActionMap.put(new Actions("positiveIncrement"));
        lazyActionMap.put(new Actions("selectMin"));
        lazyActionMap.put(new Actions("selectMax"));
        lazyActionMap.put(new Actions("startResize"));
        lazyActionMap.put(new Actions("toggleFocus"));
        lazyActionMap.put(new Actions("focusOutForward"));
        lazyActionMap.put(new Actions("focusOutBackward"));
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.splitPane = (JSplitPane) jComponent;
        this.dividerLocationIsSet = false;
        this.dividerKeyboardResize = false;
        this.keepHidden = false;
        installDefaults();
        installListeners();
        installKeyboardActions();
        setLastDragLocation(-1);
    }

    protected void installDefaults() {
        LookAndFeel.installBorder(this.splitPane, "SplitPane.border");
        LookAndFeel.installColors(this.splitPane, "SplitPane.background", "SplitPane.foreground");
        LookAndFeel.installProperty(this.splitPane, "opaque", Boolean.TRUE);
        if (this.divider == null) {
            this.divider = createDefaultDivider();
        }
        this.divider.setBasicSplitPaneUI(this);
        Border border = this.divider.getBorder();
        if (border == null || !(border instanceof UIResource)) {
            this.divider.setBorder(UIManager.getBorder("SplitPaneDivider.border"));
        }
        this.dividerDraggingColor = UIManager.getColor("SplitPaneDivider.draggingColor");
        setOrientation(this.splitPane.getOrientation());
        Integer num = (Integer) UIManager.get("SplitPane.dividerSize");
        LookAndFeel.installProperty(this.splitPane, JSplitPane.DIVIDER_SIZE_PROPERTY, Integer.valueOf(num == null ? 10 : num.intValue()));
        this.divider.setDividerSize(this.splitPane.getDividerSize());
        this.dividerSize = this.divider.getDividerSize();
        this.splitPane.add(this.divider, JSplitPane.DIVIDER);
        setContinuousLayout(this.splitPane.isContinuousLayout());
        resetLayoutManager();
        if (this.nonContinuousLayoutDivider == null) {
            setNonContinuousLayoutDivider(createDefaultNonContinuousLayoutDivider(), true);
        } else {
            setNonContinuousLayoutDivider(this.nonContinuousLayoutDivider, true);
        }
        if (this.managingFocusForwardTraversalKeys == null) {
            this.managingFocusForwardTraversalKeys = new HashSet();
            this.managingFocusForwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 0));
        }
        this.splitPane.setFocusTraversalKeys(0, this.managingFocusForwardTraversalKeys);
        if (this.managingFocusBackwardTraversalKeys == null) {
            this.managingFocusBackwardTraversalKeys = new HashSet();
            this.managingFocusBackwardTraversalKeys.add(KeyStroke.getKeyStroke(9, 1));
        }
        this.splitPane.setFocusTraversalKeys(1, this.managingFocusBackwardTraversalKeys);
    }

    protected void installListeners() {
        PropertyChangeListener propertyChangeListenerCreatePropertyChangeListener = createPropertyChangeListener();
        this.propertyChangeListener = propertyChangeListenerCreatePropertyChangeListener;
        if (propertyChangeListenerCreatePropertyChangeListener != null) {
            this.splitPane.addPropertyChangeListener(this.propertyChangeListener);
        }
        FocusListener focusListenerCreateFocusListener = createFocusListener();
        this.focusListener = focusListenerCreateFocusListener;
        if (focusListenerCreateFocusListener != null) {
            this.splitPane.addFocusListener(this.focusListener);
        }
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.splitPane, 1, getInputMap(1));
        LazyActionMap.installLazyActionMap(this.splitPane, BasicSplitPaneUI.class, "SplitPane.actionMap");
    }

    InputMap getInputMap(int i2) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.splitPane, this, "SplitPane.ancestorInputMap");
        }
        return null;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallKeyboardActions();
        uninstallListeners();
        uninstallDefaults();
        this.dividerLocationIsSet = false;
        this.dividerKeyboardResize = false;
        this.splitPane = null;
    }

    protected void uninstallDefaults() {
        if (this.splitPane.getLayout() == this.layoutManager) {
            this.splitPane.setLayout(null);
        }
        if (this.nonContinuousLayoutDivider != null) {
            this.splitPane.remove(this.nonContinuousLayoutDivider);
        }
        LookAndFeel.uninstallBorder(this.splitPane);
        if (this.divider.getBorder() instanceof UIResource) {
            this.divider.setBorder(null);
        }
        this.splitPane.remove(this.divider);
        this.divider.setBasicSplitPaneUI(null);
        this.layoutManager = null;
        this.divider = null;
        this.nonContinuousLayoutDivider = null;
        setNonContinuousLayoutDivider(null);
        this.splitPane.setFocusTraversalKeys(0, null);
        this.splitPane.setFocusTraversalKeys(1, null);
    }

    protected void uninstallListeners() {
        if (this.propertyChangeListener != null) {
            this.splitPane.removePropertyChangeListener(this.propertyChangeListener);
            this.propertyChangeListener = null;
        }
        if (this.focusListener != null) {
            this.splitPane.removeFocusListener(this.focusListener);
            this.focusListener = null;
        }
        this.keyboardUpLeftListener = null;
        this.keyboardDownRightListener = null;
        this.keyboardHomeListener = null;
        this.keyboardEndListener = null;
        this.keyboardResizeToggleListener = null;
        this.handler = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.splitPane, null);
        SwingUtilities.replaceUIInputMap(this.splitPane, 1, null);
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    @Deprecated
    protected ActionListener createKeyboardUpLeftListener() {
        return new KeyboardUpLeftHandler();
    }

    @Deprecated
    protected ActionListener createKeyboardDownRightListener() {
        return new KeyboardDownRightHandler();
    }

    @Deprecated
    protected ActionListener createKeyboardHomeListener() {
        return new KeyboardHomeHandler();
    }

    @Deprecated
    protected ActionListener createKeyboardEndListener() {
        return new KeyboardEndHandler();
    }

    @Deprecated
    protected ActionListener createKeyboardResizeToggleListener() {
        return new KeyboardResizeToggleHandler();
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i2) {
        this.orientation = i2;
    }

    public boolean isContinuousLayout() {
        return this.continuousLayout;
    }

    public void setContinuousLayout(boolean z2) {
        this.continuousLayout = z2;
    }

    public int getLastDragLocation() {
        return this.lastDragLocation;
    }

    public void setLastDragLocation(int i2) {
        this.lastDragLocation = i2;
    }

    int getKeyboardMoveIncrement() {
        return 3;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$PropertyHandler.class */
    public class PropertyHandler implements PropertyChangeListener {
        public PropertyHandler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            BasicSplitPaneUI.this.getHandler().propertyChange(propertyChangeEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$FocusHandler.class */
    public class FocusHandler extends FocusAdapter {
        public FocusHandler() {
        }

        @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicSplitPaneUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicSplitPaneUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$KeyboardUpLeftHandler.class */
    public class KeyboardUpLeftHandler implements ActionListener {
        public KeyboardUpLeftHandler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicSplitPaneUI.this.dividerKeyboardResize) {
                BasicSplitPaneUI.this.splitPane.setDividerLocation(Math.max(0, BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane) - BasicSplitPaneUI.this.getKeyboardMoveIncrement()));
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$KeyboardDownRightHandler.class */
    public class KeyboardDownRightHandler implements ActionListener {
        public KeyboardDownRightHandler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicSplitPaneUI.this.dividerKeyboardResize) {
                BasicSplitPaneUI.this.splitPane.setDividerLocation(BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane) + BasicSplitPaneUI.this.getKeyboardMoveIncrement());
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$KeyboardHomeHandler.class */
    public class KeyboardHomeHandler implements ActionListener {
        public KeyboardHomeHandler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicSplitPaneUI.this.dividerKeyboardResize) {
                BasicSplitPaneUI.this.splitPane.setDividerLocation(0);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$KeyboardEndHandler.class */
    public class KeyboardEndHandler implements ActionListener {
        public KeyboardEndHandler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (BasicSplitPaneUI.this.dividerKeyboardResize) {
                Insets insets = BasicSplitPaneUI.this.splitPane.getInsets();
                int i2 = insets != null ? insets.bottom : 0;
                int i3 = insets != null ? insets.right : 0;
                if (BasicSplitPaneUI.this.orientation == 0) {
                    BasicSplitPaneUI.this.splitPane.setDividerLocation(BasicSplitPaneUI.this.splitPane.getHeight() - i2);
                } else {
                    BasicSplitPaneUI.this.splitPane.setDividerLocation(BasicSplitPaneUI.this.splitPane.getWidth() - i3);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$KeyboardResizeToggleHandler.class */
    public class KeyboardResizeToggleHandler implements ActionListener {
        public KeyboardResizeToggleHandler() {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            if (!BasicSplitPaneUI.this.dividerKeyboardResize) {
                BasicSplitPaneUI.this.splitPane.requestFocus();
            }
        }
    }

    public BasicSplitPaneDivider getDivider() {
        return this.divider;
    }

    protected Component createDefaultNonContinuousLayoutDivider() {
        return new Canvas() { // from class: javax.swing.plaf.basic.BasicSplitPaneUI.1
            @Override // java.awt.Canvas, java.awt.Component
            public void paint(Graphics graphics) {
                if (!BasicSplitPaneUI.this.isContinuousLayout() && BasicSplitPaneUI.this.getLastDragLocation() != -1) {
                    Dimension size = BasicSplitPaneUI.this.splitPane.getSize();
                    graphics.setColor(BasicSplitPaneUI.this.dividerDraggingColor);
                    if (BasicSplitPaneUI.this.orientation == 1) {
                        graphics.fillRect(0, 0, BasicSplitPaneUI.this.dividerSize - 1, size.height - 1);
                    } else {
                        graphics.fillRect(0, 0, size.width - 1, BasicSplitPaneUI.this.dividerSize - 1);
                    }
                }
            }
        };
    }

    protected void setNonContinuousLayoutDivider(Component component) {
        setNonContinuousLayoutDivider(component, true);
    }

    protected void setNonContinuousLayoutDivider(Component component, boolean z2) {
        this.rememberPaneSizes = z2;
        if (this.nonContinuousLayoutDivider != null && this.splitPane != null) {
            this.splitPane.remove(this.nonContinuousLayoutDivider);
        }
        this.nonContinuousLayoutDivider = component;
    }

    private void addHeavyweightDivider() {
        if (this.nonContinuousLayoutDivider != null && this.splitPane != null) {
            Component leftComponent = this.splitPane.getLeftComponent();
            Component rightComponent = this.splitPane.getRightComponent();
            int dividerLocation = this.splitPane.getDividerLocation();
            if (leftComponent != null) {
                this.splitPane.setLeftComponent(null);
            }
            if (rightComponent != null) {
                this.splitPane.setRightComponent(null);
            }
            this.splitPane.remove(this.divider);
            this.splitPane.add(this.nonContinuousLayoutDivider, NON_CONTINUOUS_DIVIDER, this.splitPane.getComponentCount());
            this.splitPane.setLeftComponent(leftComponent);
            this.splitPane.setRightComponent(rightComponent);
            this.splitPane.add(this.divider, JSplitPane.DIVIDER);
            if (this.rememberPaneSizes) {
                this.splitPane.setDividerLocation(dividerLocation);
            }
        }
    }

    public Component getNonContinuousLayoutDivider() {
        return this.nonContinuousLayoutDivider;
    }

    public JSplitPane getSplitPane() {
        return this.splitPane;
    }

    public BasicSplitPaneDivider createDefaultDivider() {
        return new BasicSplitPaneDivider(this);
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public void resetToPreferredSizes(JSplitPane jSplitPane) {
        if (this.splitPane != null) {
            this.layoutManager.resetToPreferredSizes();
            this.splitPane.revalidate();
            this.splitPane.repaint();
        }
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public void setDividerLocation(JSplitPane jSplitPane, int i2) {
        if (!this.ignoreDividerLocationChange) {
            this.dividerLocationIsSet = true;
            this.splitPane.revalidate();
            this.splitPane.repaint();
            if (this.keepHidden) {
                Insets insets = this.splitPane.getInsets();
                int orientation = this.splitPane.getOrientation();
                if ((orientation == 0 && i2 != insets.top && i2 != (this.splitPane.getHeight() - this.divider.getHeight()) - insets.top) || (orientation == 1 && i2 != insets.left && i2 != (this.splitPane.getWidth() - this.divider.getWidth()) - insets.left)) {
                    setKeepHidden(false);
                    return;
                }
                return;
            }
            return;
        }
        this.ignoreDividerLocationChange = false;
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public int getDividerLocation(JSplitPane jSplitPane) {
        if (this.orientation == 1) {
            return this.divider.getLocation().f12370x;
        }
        return this.divider.getLocation().f12371y;
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public int getMinimumDividerLocation(JSplitPane jSplitPane) {
        int i2 = 0;
        Component leftComponent = this.splitPane.getLeftComponent();
        if (leftComponent != null && leftComponent.isVisible()) {
            Insets insets = this.splitPane.getInsets();
            Dimension minimumSize = leftComponent.getMinimumSize();
            if (this.orientation == 1) {
                i2 = minimumSize.width;
            } else {
                i2 = minimumSize.height;
            }
            if (insets != null) {
                i2 = this.orientation == 1 ? i2 + insets.left : i2 + insets.top;
            }
        }
        return i2;
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public int getMaximumDividerLocation(JSplitPane jSplitPane) {
        int i2;
        Dimension size = this.splitPane.getSize();
        int i3 = 0;
        Component rightComponent = this.splitPane.getRightComponent();
        if (rightComponent != null) {
            Insets insets = this.splitPane.getInsets();
            Dimension dimension = new Dimension(0, 0);
            if (rightComponent.isVisible()) {
                dimension = rightComponent.getMinimumSize();
            }
            if (this.orientation == 1) {
                i2 = size.width - dimension.width;
            } else {
                i2 = size.height - dimension.height;
            }
            i3 = i2 - this.dividerSize;
            if (insets != null) {
                if (this.orientation == 1) {
                    i3 -= insets.right;
                } else {
                    i3 -= insets.top;
                }
            }
        }
        return Math.max(getMinimumDividerLocation(this.splitPane), i3);
    }

    @Override // javax.swing.plaf.SplitPaneUI
    public void finishedPaintingChildren(JSplitPane jSplitPane, Graphics graphics) {
        if (jSplitPane == this.splitPane && getLastDragLocation() != -1 && !isContinuousLayout() && !this.draggingHW) {
            Dimension size = this.splitPane.getSize();
            graphics.setColor(this.dividerDraggingColor);
            if (this.orientation == 1) {
                graphics.fillRect(getLastDragLocation(), 0, this.dividerSize - 1, size.height - 1);
            } else {
                graphics.fillRect(0, this.lastDragLocation, size.width - 1, this.dividerSize - 1);
            }
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (!this.painted && this.splitPane.getDividerLocation() < 0) {
            this.ignoreDividerLocationChange = true;
            this.splitPane.setDividerLocation(getDividerLocation(this.splitPane));
        }
        this.painted = true;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (this.splitPane != null) {
            return this.layoutManager.preferredLayoutSize(this.splitPane);
        }
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (this.splitPane != null) {
            return this.layoutManager.minimumLayoutSize(this.splitPane);
        }
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        if (this.splitPane != null) {
            return this.layoutManager.maximumLayoutSize(this.splitPane);
        }
        return new Dimension(0, 0);
    }

    public Insets getInsets(JComponent jComponent) {
        return null;
    }

    protected void resetLayoutManager() {
        if (this.orientation == 1) {
            this.layoutManager = new BasicHorizontalLayoutManager(0);
        } else {
            this.layoutManager = new BasicHorizontalLayoutManager(1);
        }
        this.splitPane.setLayout(this.layoutManager);
        this.layoutManager.updateComponents();
        this.splitPane.revalidate();
        this.splitPane.repaint();
    }

    void setKeepHidden(boolean z2) {
        this.keepHidden = z2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean getKeepHidden() {
        return this.keepHidden;
    }

    protected void startDragging() {
        ComponentPeer peer;
        ComponentPeer peer2;
        Component leftComponent = this.splitPane.getLeftComponent();
        Component rightComponent = this.splitPane.getRightComponent();
        this.beginDragDividerLocation = getDividerLocation(this.splitPane);
        this.draggingHW = false;
        if (leftComponent != null && (peer2 = leftComponent.getPeer()) != null && !(peer2 instanceof LightweightPeer)) {
            this.draggingHW = true;
        } else if (rightComponent != null && (peer = rightComponent.getPeer()) != null && !(peer instanceof LightweightPeer)) {
            this.draggingHW = true;
        }
        if (this.orientation == 1) {
            setLastDragLocation(this.divider.getBounds().f12372x);
            this.dividerSize = this.divider.getSize().width;
            if (!isContinuousLayout() && this.draggingHW) {
                this.nonContinuousLayoutDivider.setBounds(getLastDragLocation(), 0, this.dividerSize, this.splitPane.getHeight());
                addHeavyweightDivider();
                return;
            }
            return;
        }
        setLastDragLocation(this.divider.getBounds().f12373y);
        this.dividerSize = this.divider.getSize().height;
        if (!isContinuousLayout() && this.draggingHW) {
            this.nonContinuousLayoutDivider.setBounds(0, getLastDragLocation(), this.splitPane.getWidth(), this.dividerSize);
            addHeavyweightDivider();
        }
    }

    protected void dragDividerTo(int i2) {
        if (getLastDragLocation() != i2) {
            if (isContinuousLayout()) {
                this.splitPane.setDividerLocation(i2);
                setLastDragLocation(i2);
                return;
            }
            int lastDragLocation = getLastDragLocation();
            setLastDragLocation(i2);
            if (this.orientation == 1) {
                if (this.draggingHW) {
                    this.nonContinuousLayoutDivider.setLocation(getLastDragLocation(), 0);
                    return;
                }
                int height = this.splitPane.getHeight();
                this.splitPane.repaint(lastDragLocation, 0, this.dividerSize, height);
                this.splitPane.repaint(i2, 0, this.dividerSize, height);
                return;
            }
            if (this.draggingHW) {
                this.nonContinuousLayoutDivider.setLocation(0, getLastDragLocation());
                return;
            }
            int width = this.splitPane.getWidth();
            this.splitPane.repaint(0, lastDragLocation, width, this.dividerSize);
            this.splitPane.repaint(0, i2, width, this.dividerSize);
        }
    }

    protected void finishDraggingTo(int i2) {
        dragDividerTo(i2);
        setLastDragLocation(-1);
        if (!isContinuousLayout()) {
            this.splitPane.getLeftComponent().getBounds();
            if (this.draggingHW) {
                if (this.orientation == 1) {
                    this.nonContinuousLayoutDivider.setLocation(-this.dividerSize, 0);
                } else {
                    this.nonContinuousLayoutDivider.setLocation(0, -this.dividerSize);
                }
                this.splitPane.remove(this.nonContinuousLayoutDivider);
            }
            this.splitPane.setDividerLocation(i2);
        }
    }

    @Deprecated
    protected int getDividerBorderSize() {
        return 1;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$BasicHorizontalLayoutManager.class */
    public class BasicHorizontalLayoutManager implements LayoutManager2 {
        protected int[] sizes;
        protected Component[] components;
        private int lastSplitPaneSize;
        private boolean doReset;
        private int axis;

        BasicHorizontalLayoutManager(BasicSplitPaneUI basicSplitPaneUI) {
            this(0);
        }

        BasicHorizontalLayoutManager(int i2) {
            this.axis = i2;
            this.components = new Component[3];
            Component[] componentArr = this.components;
            Component[] componentArr2 = this.components;
            this.components[2] = null;
            componentArr2[1] = null;
            componentArr[0] = null;
            this.sizes = new int[3];
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int dividerLocation;
            Dimension size = container.getSize();
            if (size.height <= 0 || size.width <= 0) {
                this.lastSplitPaneSize = 0;
                return;
            }
            int dividerLocation2 = BasicSplitPaneUI.this.splitPane.getDividerLocation();
            Insets insets = BasicSplitPaneUI.this.splitPane.getInsets();
            int availableSize = getAvailableSize(size, insets);
            getSizeForPrimaryAxis(size);
            BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane);
            int sizeForPrimaryAxis = getSizeForPrimaryAxis(insets, true);
            Dimension preferredSize = this.components[2] == null ? null : this.components[2].getPreferredSize();
            if ((this.doReset && !BasicSplitPaneUI.this.dividerLocationIsSet) || dividerLocation2 < 0) {
                resetToPreferredSizes(availableSize);
            } else if (this.lastSplitPaneSize <= 0 || availableSize == this.lastSplitPaneSize || !BasicSplitPaneUI.this.painted || (preferredSize != null && getSizeForPrimaryAxis(preferredSize) != this.sizes[2])) {
                if (preferredSize != null) {
                    this.sizes[2] = getSizeForPrimaryAxis(preferredSize);
                } else {
                    this.sizes[2] = 0;
                }
                setDividerLocation(dividerLocation2 - sizeForPrimaryAxis, availableSize);
                BasicSplitPaneUI.this.dividerLocationIsSet = false;
            } else if (availableSize != this.lastSplitPaneSize) {
                distributeSpace(availableSize - this.lastSplitPaneSize, BasicSplitPaneUI.this.getKeepHidden());
            }
            this.doReset = false;
            BasicSplitPaneUI.this.dividerLocationIsSet = false;
            this.lastSplitPaneSize = availableSize;
            int initialLocation = getInitialLocation(insets);
            byte b2 = false;
            while (b2 < 3) {
                if (this.components[b2 == true ? 1 : 0] != null && this.components[b2 == true ? 1 : 0].isVisible()) {
                    setComponentToSize(this.components[b2 == true ? 1 : 0], this.sizes[b2 == true ? 1 : 0], initialLocation, insets, size);
                    initialLocation += this.sizes[b2 == true ? 1 : 0];
                }
                switch (b2) {
                    case 0:
                        b2 = 2;
                        break;
                    case 1:
                        b2 = 3;
                        break;
                    case 2:
                        b2 = true;
                        break;
                }
            }
            if (BasicSplitPaneUI.this.painted && (dividerLocation = BasicSplitPaneUI.this.getDividerLocation(BasicSplitPaneUI.this.splitPane)) != dividerLocation2 - sizeForPrimaryAxis) {
                int lastDividerLocation = BasicSplitPaneUI.this.splitPane.getLastDividerLocation();
                BasicSplitPaneUI.this.ignoreDividerLocationChange = true;
                try {
                    BasicSplitPaneUI.this.splitPane.setDividerLocation(dividerLocation);
                    BasicSplitPaneUI.this.splitPane.setLastDividerLocation(lastDividerLocation);
                    BasicSplitPaneUI.this.ignoreDividerLocationChange = false;
                } catch (Throwable th) {
                    BasicSplitPaneUI.this.ignoreDividerLocationChange = false;
                    throw th;
                }
            }
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
            boolean z2 = true;
            if (str != null) {
                if (str.equals(JSplitPane.DIVIDER)) {
                    this.components[2] = component;
                    this.sizes[2] = getSizeForPrimaryAxis(component.getPreferredSize());
                } else if (str.equals(JSplitPane.LEFT) || str.equals(JSplitPane.TOP)) {
                    this.components[0] = component;
                    this.sizes[0] = 0;
                } else if (str.equals(JSplitPane.RIGHT) || str.equals(JSplitPane.BOTTOM)) {
                    this.components[1] = component;
                    this.sizes[1] = 0;
                } else if (!str.equals(BasicSplitPaneUI.NON_CONTINUOUS_DIVIDER)) {
                    z2 = false;
                }
            } else {
                z2 = false;
            }
            if (!z2) {
                throw new IllegalArgumentException("cannot add to layout: unknown constraint: " + str);
            }
            this.doReset = true;
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            int sizeForPrimaryAxis = 0;
            int sizeForSecondaryAxis = 0;
            Insets insets = BasicSplitPaneUI.this.splitPane.getInsets();
            for (int i2 = 0; i2 < 3; i2++) {
                if (this.components[i2] != null) {
                    Dimension minimumSize = this.components[i2].getMinimumSize();
                    int sizeForSecondaryAxis2 = getSizeForSecondaryAxis(minimumSize);
                    sizeForPrimaryAxis += getSizeForPrimaryAxis(minimumSize);
                    if (sizeForSecondaryAxis2 > sizeForSecondaryAxis) {
                        sizeForSecondaryAxis = sizeForSecondaryAxis2;
                    }
                }
            }
            if (insets != null) {
                sizeForPrimaryAxis += getSizeForPrimaryAxis(insets, true) + getSizeForPrimaryAxis(insets, false);
                sizeForSecondaryAxis += getSizeForSecondaryAxis(insets, true) + getSizeForSecondaryAxis(insets, false);
            }
            if (this.axis == 0) {
                return new Dimension(sizeForPrimaryAxis, sizeForSecondaryAxis);
            }
            return new Dimension(sizeForSecondaryAxis, sizeForPrimaryAxis);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            int sizeForPrimaryAxis = 0;
            int sizeForSecondaryAxis = 0;
            Insets insets = BasicSplitPaneUI.this.splitPane.getInsets();
            for (int i2 = 0; i2 < 3; i2++) {
                if (this.components[i2] != null) {
                    Dimension preferredSize = this.components[i2].getPreferredSize();
                    int sizeForSecondaryAxis2 = getSizeForSecondaryAxis(preferredSize);
                    sizeForPrimaryAxis += getSizeForPrimaryAxis(preferredSize);
                    if (sizeForSecondaryAxis2 > sizeForSecondaryAxis) {
                        sizeForSecondaryAxis = sizeForSecondaryAxis2;
                    }
                }
            }
            if (insets != null) {
                sizeForPrimaryAxis += getSizeForPrimaryAxis(insets, true) + getSizeForPrimaryAxis(insets, false);
                sizeForSecondaryAxis += getSizeForSecondaryAxis(insets, true) + getSizeForSecondaryAxis(insets, false);
            }
            if (this.axis == 0) {
                return new Dimension(sizeForPrimaryAxis, sizeForSecondaryAxis);
            }
            return new Dimension(sizeForSecondaryAxis, sizeForPrimaryAxis);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
            for (int i2 = 0; i2 < 3; i2++) {
                if (this.components[i2] == component) {
                    this.components[i2] = null;
                    this.sizes[i2] = 0;
                    this.doReset = true;
                }
            }
        }

        @Override // java.awt.LayoutManager2
        public void addLayoutComponent(Component component, Object obj) {
            if (obj == null || (obj instanceof String)) {
                addLayoutComponent((String) obj, component);
                return;
            }
            throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentX(Container container) {
            return 0.0f;
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentY(Container container) {
            return 0.0f;
        }

        @Override // java.awt.LayoutManager2
        public void invalidateLayout(Container container) {
        }

        @Override // java.awt.LayoutManager2
        public Dimension maximumLayoutSize(Container container) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        public void resetToPreferredSizes() {
            this.doReset = true;
        }

        protected void resetSizeAt(int i2) {
            this.sizes[i2] = 0;
            this.doReset = true;
        }

        protected void setSizes(int[] iArr) {
            System.arraycopy(iArr, 0, this.sizes, 0, 3);
        }

        protected int[] getSizes() {
            int[] iArr = new int[3];
            System.arraycopy(this.sizes, 0, iArr, 0, 3);
            return iArr;
        }

        protected int getPreferredSizeOfComponent(Component component) {
            return getSizeForPrimaryAxis(component.getPreferredSize());
        }

        int getMinimumSizeOfComponent(Component component) {
            return getSizeForPrimaryAxis(component.getMinimumSize());
        }

        protected int getSizeOfComponent(Component component) {
            return getSizeForPrimaryAxis(component.getSize());
        }

        protected int getAvailableSize(Dimension dimension, Insets insets) {
            if (insets == null) {
                return getSizeForPrimaryAxis(dimension);
            }
            return getSizeForPrimaryAxis(dimension) - (getSizeForPrimaryAxis(insets, true) + getSizeForPrimaryAxis(insets, false));
        }

        protected int getInitialLocation(Insets insets) {
            if (insets != null) {
                return getSizeForPrimaryAxis(insets, true);
            }
            return 0;
        }

        protected void setComponentToSize(Component component, int i2, int i3, Insets insets, Dimension dimension) {
            if (insets != null) {
                if (this.axis == 0) {
                    component.setBounds(i3, insets.top, i2, dimension.height - (insets.top + insets.bottom));
                    return;
                } else {
                    component.setBounds(insets.left, i3, dimension.width - (insets.left + insets.right), i2);
                    return;
                }
            }
            if (this.axis == 0) {
                component.setBounds(i3, 0, i2, dimension.height);
            } else {
                component.setBounds(0, i3, dimension.width, i2);
            }
        }

        int getSizeForPrimaryAxis(Dimension dimension) {
            if (this.axis == 0) {
                return dimension.width;
            }
            return dimension.height;
        }

        int getSizeForSecondaryAxis(Dimension dimension) {
            if (this.axis == 0) {
                return dimension.height;
            }
            return dimension.width;
        }

        int getSizeForPrimaryAxis(Insets insets, boolean z2) {
            if (this.axis == 0) {
                if (z2) {
                    return insets.left;
                }
                return insets.right;
            }
            if (z2) {
                return insets.top;
            }
            return insets.bottom;
        }

        int getSizeForSecondaryAxis(Insets insets, boolean z2) {
            if (this.axis == 0) {
                if (z2) {
                    return insets.top;
                }
                return insets.bottom;
            }
            if (z2) {
                return insets.left;
            }
            return insets.right;
        }

        protected void updateComponents() {
            Component leftComponent = BasicSplitPaneUI.this.splitPane.getLeftComponent();
            if (this.components[0] != leftComponent) {
                this.components[0] = leftComponent;
                if (leftComponent == null) {
                    this.sizes[0] = 0;
                } else {
                    this.sizes[0] = -1;
                }
            }
            Component rightComponent = BasicSplitPaneUI.this.splitPane.getRightComponent();
            if (this.components[1] != rightComponent) {
                this.components[1] = rightComponent;
                if (rightComponent == null) {
                    this.sizes[1] = 0;
                } else {
                    this.sizes[1] = -1;
                }
            }
            Component[] components = BasicSplitPaneUI.this.splitPane.getComponents();
            Component component = this.components[2];
            this.components[2] = null;
            int length = components.length - 1;
            while (true) {
                if (length < 0) {
                    break;
                }
                if (components[length] == this.components[0] || components[length] == this.components[1] || components[length] == BasicSplitPaneUI.this.nonContinuousLayoutDivider) {
                    length--;
                } else if (component != components[length]) {
                    this.components[2] = components[length];
                } else {
                    this.components[2] = component;
                }
            }
            if (this.components[2] == null) {
                this.sizes[2] = 0;
            } else {
                this.sizes[2] = getSizeForPrimaryAxis(this.components[2].getPreferredSize());
            }
        }

        void setDividerLocation(int i2, int i3) {
            boolean z2 = this.components[0] != null && this.components[0].isVisible();
            boolean z3 = this.components[1] != null && this.components[1].isVisible();
            int i4 = i3;
            if (this.components[2] != null && this.components[2].isVisible()) {
                i4 -= this.sizes[2];
            }
            int iMax = Math.max(0, Math.min(i2, i4));
            if (z2) {
                if (z3) {
                    this.sizes[0] = iMax;
                    this.sizes[1] = i4 - iMax;
                    return;
                } else {
                    this.sizes[0] = i4;
                    this.sizes[1] = 0;
                    return;
                }
            }
            if (z3) {
                this.sizes[1] = i4;
                this.sizes[0] = 0;
            }
        }

        int[] getPreferredSizes() {
            int[] iArr = new int[3];
            for (int i2 = 0; i2 < 3; i2++) {
                if (this.components[i2] != null && this.components[i2].isVisible()) {
                    iArr[i2] = getPreferredSizeOfComponent(this.components[i2]);
                } else {
                    iArr[i2] = -1;
                }
            }
            return iArr;
        }

        int[] getMinimumSizes() {
            int[] iArr = new int[3];
            for (int i2 = 0; i2 < 2; i2++) {
                if (this.components[i2] != null && this.components[i2].isVisible()) {
                    iArr[i2] = getMinimumSizeOfComponent(this.components[i2]);
                } else {
                    iArr[i2] = -1;
                }
            }
            iArr[2] = this.components[2] != null ? getMinimumSizeOfComponent(this.components[2]) : -1;
            return iArr;
        }

        void resetToPreferredSizes(int i2) {
            int[] preferredSizes = getPreferredSizes();
            int i3 = 0;
            for (int i4 = 0; i4 < 3; i4++) {
                if (preferredSizes[i4] != -1) {
                    i3 += preferredSizes[i4];
                }
            }
            if (i3 > i2) {
                preferredSizes = getMinimumSizes();
                i3 = 0;
                for (int i5 = 0; i5 < 3; i5++) {
                    if (preferredSizes[i5] != -1) {
                        i3 += preferredSizes[i5];
                    }
                }
            }
            setSizes(preferredSizes);
            distributeSpace(i2 - i3, false);
        }

        void distributeSpace(int i2, boolean z2) {
            boolean z3 = this.components[0] != null && this.components[0].isVisible();
            boolean z4 = this.components[1] != null && this.components[1].isVisible();
            if (z2) {
                if (z3 && getSizeForPrimaryAxis(this.components[0].getSize()) == 0) {
                    z3 = false;
                    if (z4 && getSizeForPrimaryAxis(this.components[1].getSize()) == 0) {
                        z3 = true;
                    }
                } else if (z4 && getSizeForPrimaryAxis(this.components[1].getSize()) == 0) {
                    z4 = false;
                }
            }
            if (z3 && z4) {
                int resizeWeight = (int) (BasicSplitPaneUI.this.splitPane.getResizeWeight() * i2);
                int i3 = i2 - resizeWeight;
                int[] iArr = this.sizes;
                iArr[0] = iArr[0] + resizeWeight;
                int[] iArr2 = this.sizes;
                iArr2[1] = iArr2[1] + i3;
                int minimumSizeOfComponent = getMinimumSizeOfComponent(this.components[0]);
                int minimumSizeOfComponent2 = getMinimumSizeOfComponent(this.components[1]);
                boolean z5 = this.sizes[0] >= minimumSizeOfComponent;
                boolean z6 = this.sizes[1] >= minimumSizeOfComponent2;
                if (!z5 && !z6) {
                    if (this.sizes[0] < 0) {
                        int[] iArr3 = this.sizes;
                        iArr3[1] = iArr3[1] + this.sizes[0];
                        this.sizes[0] = 0;
                    } else if (this.sizes[1] < 0) {
                        int[] iArr4 = this.sizes;
                        iArr4[0] = iArr4[0] + this.sizes[1];
                        this.sizes[1] = 0;
                    }
                } else if (!z5) {
                    if (this.sizes[1] - (minimumSizeOfComponent - this.sizes[0]) < minimumSizeOfComponent2) {
                        if (this.sizes[0] < 0) {
                            int[] iArr5 = this.sizes;
                            iArr5[1] = iArr5[1] + this.sizes[0];
                            this.sizes[0] = 0;
                        }
                    } else {
                        int[] iArr6 = this.sizes;
                        iArr6[1] = iArr6[1] - (minimumSizeOfComponent - this.sizes[0]);
                        this.sizes[0] = minimumSizeOfComponent;
                    }
                } else if (!z6) {
                    if (this.sizes[0] - (minimumSizeOfComponent2 - this.sizes[1]) < minimumSizeOfComponent) {
                        if (this.sizes[1] < 0) {
                            int[] iArr7 = this.sizes;
                            iArr7[0] = iArr7[0] + this.sizes[1];
                            this.sizes[1] = 0;
                        }
                    } else {
                        int[] iArr8 = this.sizes;
                        iArr8[0] = iArr8[0] - (minimumSizeOfComponent2 - this.sizes[1]);
                        this.sizes[1] = minimumSizeOfComponent2;
                    }
                }
                if (this.sizes[0] < 0) {
                    this.sizes[0] = 0;
                }
                if (this.sizes[1] < 0) {
                    this.sizes[1] = 0;
                    return;
                }
                return;
            }
            if (z3) {
                this.sizes[0] = Math.max(0, this.sizes[0] + i2);
            } else if (z4) {
                this.sizes[1] = Math.max(0, this.sizes[1] + i2);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$BasicVerticalLayoutManager.class */
    public class BasicVerticalLayoutManager extends BasicHorizontalLayoutManager {
        public BasicVerticalLayoutManager() {
            super(1);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$Handler.class */
    private class Handler implements FocusListener, PropertyChangeListener {
        private Handler() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getSource() == BasicSplitPaneUI.this.splitPane) {
                String propertyName = propertyChangeEvent.getPropertyName();
                if (propertyName == "orientation") {
                    BasicSplitPaneUI.this.orientation = BasicSplitPaneUI.this.splitPane.getOrientation();
                    BasicSplitPaneUI.this.resetLayoutManager();
                    return;
                }
                if (propertyName == JSplitPane.CONTINUOUS_LAYOUT_PROPERTY) {
                    BasicSplitPaneUI.this.setContinuousLayout(BasicSplitPaneUI.this.splitPane.isContinuousLayout());
                    if (!BasicSplitPaneUI.this.isContinuousLayout()) {
                        if (BasicSplitPaneUI.this.nonContinuousLayoutDivider == null) {
                            BasicSplitPaneUI.this.setNonContinuousLayoutDivider(BasicSplitPaneUI.this.createDefaultNonContinuousLayoutDivider(), true);
                            return;
                        } else {
                            if (BasicSplitPaneUI.this.nonContinuousLayoutDivider.getParent() == null) {
                                BasicSplitPaneUI.this.setNonContinuousLayoutDivider(BasicSplitPaneUI.this.nonContinuousLayoutDivider, true);
                                return;
                            }
                            return;
                        }
                    }
                    return;
                }
                if (propertyName == JSplitPane.DIVIDER_SIZE_PROPERTY) {
                    BasicSplitPaneUI.this.divider.setDividerSize(BasicSplitPaneUI.this.splitPane.getDividerSize());
                    BasicSplitPaneUI.this.dividerSize = BasicSplitPaneUI.this.divider.getDividerSize();
                    BasicSplitPaneUI.this.splitPane.revalidate();
                    BasicSplitPaneUI.this.splitPane.repaint();
                }
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicSplitPaneUI.this.dividerKeyboardResize = true;
            BasicSplitPaneUI.this.splitPane.repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicSplitPaneUI.this.dividerKeyboardResize = false;
            BasicSplitPaneUI.this.splitPane.repaint();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSplitPaneUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String NEGATIVE_INCREMENT = "negativeIncrement";
        private static final String POSITIVE_INCREMENT = "positiveIncrement";
        private static final String SELECT_MIN = "selectMin";
        private static final String SELECT_MAX = "selectMax";
        private static final String START_RESIZE = "startResize";
        private static final String TOGGLE_FOCUS = "toggleFocus";
        private static final String FOCUS_OUT_FORWARD = "focusOutForward";
        private static final String FOCUS_OUT_BACKWARD = "focusOutBackward";

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JSplitPane jSplitPane = (JSplitPane) actionEvent.getSource();
            BasicSplitPaneUI basicSplitPaneUI = (BasicSplitPaneUI) BasicLookAndFeel.getUIOfType(jSplitPane.getUI(), BasicSplitPaneUI.class);
            if (basicSplitPaneUI == null) {
                return;
            }
            String name = getName();
            if (name == NEGATIVE_INCREMENT) {
                if (basicSplitPaneUI.dividerKeyboardResize) {
                    jSplitPane.setDividerLocation(Math.max(0, basicSplitPaneUI.getDividerLocation(jSplitPane) - basicSplitPaneUI.getKeyboardMoveIncrement()));
                    return;
                }
                return;
            }
            if (name == POSITIVE_INCREMENT) {
                if (basicSplitPaneUI.dividerKeyboardResize) {
                    jSplitPane.setDividerLocation(basicSplitPaneUI.getDividerLocation(jSplitPane) + basicSplitPaneUI.getKeyboardMoveIncrement());
                    return;
                }
                return;
            }
            if (name == SELECT_MIN) {
                if (basicSplitPaneUI.dividerKeyboardResize) {
                    jSplitPane.setDividerLocation(0);
                    return;
                }
                return;
            }
            if (name == SELECT_MAX) {
                if (basicSplitPaneUI.dividerKeyboardResize) {
                    Insets insets = jSplitPane.getInsets();
                    int i2 = insets != null ? insets.bottom : 0;
                    int i3 = insets != null ? insets.right : 0;
                    if (basicSplitPaneUI.orientation == 0) {
                        jSplitPane.setDividerLocation(jSplitPane.getHeight() - i2);
                        return;
                    } else {
                        jSplitPane.setDividerLocation(jSplitPane.getWidth() - i3);
                        return;
                    }
                }
                return;
            }
            if (name == START_RESIZE) {
                if (!basicSplitPaneUI.dividerKeyboardResize) {
                    jSplitPane.requestFocus();
                    return;
                }
                JSplitPane jSplitPane2 = (JSplitPane) SwingUtilities.getAncestorOfClass(JSplitPane.class, jSplitPane);
                if (jSplitPane2 != null) {
                    jSplitPane2.requestFocus();
                    return;
                }
                return;
            }
            if (name == TOGGLE_FOCUS) {
                toggleFocus(jSplitPane);
            } else if (name == FOCUS_OUT_FORWARD) {
                moveFocus(jSplitPane, 1);
            } else if (name == FOCUS_OUT_BACKWARD) {
                moveFocus(jSplitPane, -1);
            }
        }

        private void moveFocus(JSplitPane jSplitPane, int i2) {
            Component componentBefore;
            Component componentBefore2;
            Container focusCycleRootAncestor = jSplitPane.getFocusCycleRootAncestor();
            FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
            if (i2 > 0) {
                componentBefore = focusTraversalPolicy.getComponentAfter(focusCycleRootAncestor, jSplitPane);
            } else {
                componentBefore = focusTraversalPolicy.getComponentBefore(focusCycleRootAncestor, jSplitPane);
            }
            Component component = componentBefore;
            HashSet hashSet = new HashSet();
            if (jSplitPane.isAncestorOf(component)) {
                do {
                    hashSet.add(component);
                    Container focusCycleRootAncestor2 = component.getFocusCycleRootAncestor();
                    FocusTraversalPolicy focusTraversalPolicy2 = focusCycleRootAncestor2.getFocusTraversalPolicy();
                    if (i2 > 0) {
                        componentBefore2 = focusTraversalPolicy2.getComponentAfter(focusCycleRootAncestor2, component);
                    } else {
                        componentBefore2 = focusTraversalPolicy2.getComponentBefore(focusCycleRootAncestor2, component);
                    }
                    component = componentBefore2;
                    if (!jSplitPane.isAncestorOf(component)) {
                        break;
                    }
                } while (!hashSet.contains(component));
            }
            if (component != null && !jSplitPane.isAncestorOf(component)) {
                component.requestFocus();
            }
        }

        private void toggleFocus(JSplitPane jSplitPane) {
            Component leftComponent = jSplitPane.getLeftComponent();
            Component rightComponent = jSplitPane.getRightComponent();
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            Component nextSide = getNextSide(jSplitPane, focusOwner);
            if (nextSide != null) {
                if (focusOwner != null) {
                    if (!SwingUtilities.isDescendingFrom(focusOwner, leftComponent) || !SwingUtilities.isDescendingFrom(nextSide, leftComponent)) {
                        if (SwingUtilities.isDescendingFrom(focusOwner, rightComponent) && SwingUtilities.isDescendingFrom(nextSide, rightComponent)) {
                            return;
                        }
                    } else {
                        return;
                    }
                }
                SwingUtilities2.compositeRequestFocus(nextSide);
            }
        }

        private Component getNextSide(JSplitPane jSplitPane, Component component) {
            Component firstAvailableComponent;
            Component firstAvailableComponent2;
            Component leftComponent = jSplitPane.getLeftComponent();
            Component rightComponent = jSplitPane.getRightComponent();
            if (component != null && SwingUtilities.isDescendingFrom(component, leftComponent) && rightComponent != null && (firstAvailableComponent2 = getFirstAvailableComponent(rightComponent)) != null) {
                return firstAvailableComponent2;
            }
            JSplitPane jSplitPane2 = (JSplitPane) SwingUtilities.getAncestorOfClass(JSplitPane.class, jSplitPane);
            if (jSplitPane2 != null) {
                firstAvailableComponent = getNextSide(jSplitPane2, component);
            } else {
                firstAvailableComponent = getFirstAvailableComponent(leftComponent);
                if (firstAvailableComponent == null) {
                    firstAvailableComponent = getFirstAvailableComponent(rightComponent);
                }
            }
            return firstAvailableComponent;
        }

        private Component getFirstAvailableComponent(Component component) {
            if (component != null && (component instanceof JSplitPane)) {
                JSplitPane jSplitPane = (JSplitPane) component;
                Component firstAvailableComponent = getFirstAvailableComponent(jSplitPane.getLeftComponent());
                if (firstAvailableComponent != null) {
                    component = firstAvailableComponent;
                } else {
                    component = getFirstAvailableComponent(jSplitPane.getRightComponent());
                }
            }
            return component;
        }
    }
}
