package javax.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.DesktopIconUI;
import javax.swing.plaf.InternalFrameUI;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/JInternalFrame.class */
public class JInternalFrame extends JComponent implements Accessible, WindowConstants, RootPaneContainer {
    private static final String uiClassID = "InternalFrameUI";
    protected JRootPane rootPane;
    protected boolean rootPaneCheckingEnabled;
    protected boolean closable;
    protected boolean isClosed;
    protected boolean maximizable;
    protected boolean isMaximum;
    protected boolean iconable;
    protected boolean isIcon;
    protected boolean resizable;
    protected boolean isSelected;
    protected Icon frameIcon;
    protected String title;
    protected JDesktopIcon desktopIcon;
    private Cursor lastCursor;
    private boolean opened;
    private Rectangle normalBounds;
    private int defaultCloseOperation;
    private Component lastFocusOwner;
    public static final String CONTENT_PANE_PROPERTY = "contentPane";
    public static final String MENU_BAR_PROPERTY = "JMenuBar";
    public static final String TITLE_PROPERTY = "title";
    public static final String LAYERED_PANE_PROPERTY = "layeredPane";
    public static final String ROOT_PANE_PROPERTY = "rootPane";
    public static final String GLASS_PANE_PROPERTY = "glassPane";
    public static final String FRAME_ICON_PROPERTY = "frameIcon";
    public static final String IS_SELECTED_PROPERTY = "selected";
    public static final String IS_CLOSED_PROPERTY = "closed";
    public static final String IS_MAXIMUM_PROPERTY = "maximum";
    public static final String IS_ICON_PROPERTY = "icon";
    private static final Object PROPERTY_CHANGE_LISTENER_KEY = new StringBuilder("InternalFramePropertyChangeListener");
    boolean isDragging;
    boolean danger;

    private static void addPropertyChangeListenerIfNecessary() {
        if (AppContext.getAppContext().get(PROPERTY_CHANGE_LISTENER_KEY) == null) {
            FocusPropertyChangeListener focusPropertyChangeListener = new FocusPropertyChangeListener();
            AppContext.getAppContext().put(PROPERTY_CHANGE_LISTENER_KEY, focusPropertyChangeListener);
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(focusPropertyChangeListener);
        }
    }

    /* loaded from: rt.jar:javax/swing/JInternalFrame$FocusPropertyChangeListener.class */
    private static class FocusPropertyChangeListener implements PropertyChangeListener {
        private FocusPropertyChangeListener() {
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName() == "permanentFocusOwner") {
                JInternalFrame.updateLastFocusOwner((Component) propertyChangeEvent.getNewValue());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void updateLastFocusOwner(Component component) {
        if (component != null) {
            Component parent = component;
            while (true) {
                Component component2 = parent;
                if (component2 != null && !(component2 instanceof Window)) {
                    if (component2 instanceof JInternalFrame) {
                        ((JInternalFrame) component2).setLastFocusOwner(component);
                    }
                    parent = component2.getParent();
                } else {
                    return;
                }
            }
        }
    }

    public JInternalFrame() {
        this("", false, false, false, false);
    }

    public JInternalFrame(String str) {
        this(str, false, false, false, false);
    }

    public JInternalFrame(String str, boolean z2) {
        this(str, z2, false, false, false);
    }

    public JInternalFrame(String str, boolean z2, boolean z3) {
        this(str, z2, z3, false, false);
    }

    public JInternalFrame(String str, boolean z2, boolean z3, boolean z4) {
        this(str, z2, z3, z4, false);
    }

    public JInternalFrame(String str, boolean z2, boolean z3, boolean z4, boolean z5) {
        this.rootPaneCheckingEnabled = false;
        this.normalBounds = null;
        this.defaultCloseOperation = 2;
        this.isDragging = false;
        this.danger = false;
        setRootPane(createRootPane());
        setLayout(new BorderLayout());
        this.title = str;
        this.resizable = z2;
        this.closable = z3;
        this.maximizable = z4;
        this.isMaximum = false;
        this.iconable = z5;
        this.isIcon = false;
        setVisible(false);
        setRootPaneCheckingEnabled(true);
        this.desktopIcon = new JDesktopIcon(this);
        updateUI();
        SunToolkit.checkAndSetPolicy(this);
        addPropertyChangeListenerIfNecessary();
    }

    protected JRootPane createRootPane() {
        return new JRootPane();
    }

    public InternalFrameUI getUI() {
        return (InternalFrameUI) this.ui;
    }

    public void setUI(InternalFrameUI internalFrameUI) {
        boolean zIsRootPaneCheckingEnabled = isRootPaneCheckingEnabled();
        try {
            setRootPaneCheckingEnabled(false);
            super.setUI((ComponentUI) internalFrameUI);
        } finally {
            setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((InternalFrameUI) UIManager.getUI(this));
        invalidate();
        if (this.desktopIcon != null) {
            this.desktopIcon.updateUIWhenHidden();
        }
    }

    void updateUIWhenHidden() {
        setUI((InternalFrameUI) UIManager.getUI(this));
        invalidate();
        Component[] components = getComponents();
        if (components != null) {
            for (Component component : components) {
                SwingUtilities.updateComponentTreeUI(component);
            }
        }
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    protected boolean isRootPaneCheckingEnabled() {
        return this.rootPaneCheckingEnabled;
    }

    protected void setRootPaneCheckingEnabled(boolean z2) {
        this.rootPaneCheckingEnabled = z2;
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        if (isRootPaneCheckingEnabled()) {
            getContentPane().add(component, obj, i2);
        } else {
            super.addImpl(component, obj, i2);
        }
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        int componentCount = getComponentCount();
        super.remove(component);
        if (componentCount == getComponentCount()) {
            getContentPane().remove(component);
        }
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        if (isRootPaneCheckingEnabled()) {
            getContentPane().setLayout(layoutManager);
        } else {
            super.setLayout(layoutManager);
        }
    }

    @Deprecated
    public JMenuBar getMenuBar() {
        return getRootPane().getMenuBar();
    }

    public JMenuBar getJMenuBar() {
        return getRootPane().getJMenuBar();
    }

    @Deprecated
    public void setMenuBar(JMenuBar jMenuBar) {
        JMenuBar menuBar = getMenuBar();
        getRootPane().setJMenuBar(jMenuBar);
        firePropertyChange(MENU_BAR_PROPERTY, menuBar, jMenuBar);
    }

    public void setJMenuBar(JMenuBar jMenuBar) {
        JMenuBar menuBar = getMenuBar();
        getRootPane().setJMenuBar(jMenuBar);
        firePropertyChange(MENU_BAR_PROPERTY, menuBar, jMenuBar);
    }

    @Override // javax.swing.RootPaneContainer
    public Container getContentPane() {
        return getRootPane().getContentPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setContentPane(Container container) {
        Container contentPane = getContentPane();
        getRootPane().setContentPane(container);
        firePropertyChange(CONTENT_PANE_PROPERTY, contentPane, container);
    }

    @Override // javax.swing.RootPaneContainer
    public JLayeredPane getLayeredPane() {
        return getRootPane().getLayeredPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setLayeredPane(JLayeredPane jLayeredPane) {
        JLayeredPane layeredPane = getLayeredPane();
        getRootPane().setLayeredPane(jLayeredPane);
        firePropertyChange(LAYERED_PANE_PROPERTY, layeredPane, jLayeredPane);
    }

    @Override // javax.swing.RootPaneContainer
    public Component getGlassPane() {
        return getRootPane().getGlassPane();
    }

    @Override // javax.swing.RootPaneContainer
    public void setGlassPane(Component component) {
        Component glassPane = getGlassPane();
        getRootPane().setGlassPane(component);
        firePropertyChange(GLASS_PANE_PROPERTY, glassPane, component);
    }

    @Override // javax.swing.JComponent
    public JRootPane getRootPane() {
        return this.rootPane;
    }

    protected void setRootPane(JRootPane jRootPane) {
        if (this.rootPane != null) {
            remove(this.rootPane);
        }
        JRootPane rootPane = getRootPane();
        this.rootPane = jRootPane;
        if (this.rootPane != null) {
            boolean zIsRootPaneCheckingEnabled = isRootPaneCheckingEnabled();
            try {
                setRootPaneCheckingEnabled(false);
                add(this.rootPane, BorderLayout.CENTER);
                setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
            } catch (Throwable th) {
                setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
                throw th;
            }
        }
        firePropertyChange(ROOT_PANE_PROPERTY, rootPane, jRootPane);
    }

    public void setClosable(boolean z2) {
        Boolean bool = this.closable ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        this.closable = z2;
        firePropertyChange("closable", bool, bool2);
    }

    public boolean isClosable() {
        return this.closable;
    }

    public boolean isClosed() {
        return this.isClosed;
    }

    public void setClosed(boolean z2) throws PropertyVetoException {
        if (this.isClosed == z2) {
            return;
        }
        Boolean bool = this.isClosed ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        if (z2) {
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
        }
        fireVetoableChange(IS_CLOSED_PROPERTY, bool, bool2);
        this.isClosed = z2;
        if (this.isClosed) {
            setVisible(false);
        }
        firePropertyChange(IS_CLOSED_PROPERTY, bool, bool2);
        if (this.isClosed) {
            dispose();
        } else {
            if (!this.opened) {
            }
        }
    }

    public void setResizable(boolean z2) {
        Boolean bool = this.resizable ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        this.resizable = z2;
        firePropertyChange("resizable", bool, bool2);
    }

    public boolean isResizable() {
        if (this.isMaximum) {
            return false;
        }
        return this.resizable;
    }

    public void setIconifiable(boolean z2) {
        Boolean bool = this.iconable ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        this.iconable = z2;
        firePropertyChange("iconable", bool, bool2);
    }

    public boolean isIconifiable() {
        return this.iconable;
    }

    public boolean isIcon() {
        return this.isIcon;
    }

    public void setIcon(boolean z2) throws PropertyVetoException {
        if (this.isIcon == z2) {
            return;
        }
        firePropertyChange("ancestor", (Object) null, getParent());
        Boolean bool = this.isIcon ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        fireVetoableChange("icon", bool, bool2);
        this.isIcon = z2;
        firePropertyChange("icon", bool, bool2);
        if (z2) {
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_ICONIFIED);
        } else {
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED);
        }
    }

    public void setMaximizable(boolean z2) {
        Boolean bool = this.maximizable ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        this.maximizable = z2;
        firePropertyChange("maximizable", bool, bool2);
    }

    public boolean isMaximizable() {
        return this.maximizable;
    }

    public boolean isMaximum() {
        return this.isMaximum;
    }

    public void setMaximum(boolean z2) throws PropertyVetoException {
        if (this.isMaximum == z2) {
            return;
        }
        Boolean bool = this.isMaximum ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        fireVetoableChange(IS_MAXIMUM_PROPERTY, bool, bool2);
        this.isMaximum = z2;
        firePropertyChange(IS_MAXIMUM_PROPERTY, bool, bool2);
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        String str2 = this.title;
        this.title = str;
        firePropertyChange("title", str2, str);
    }

    public void setSelected(boolean z2) throws PropertyVetoException {
        if (z2 && this.isSelected) {
            restoreSubcomponentFocus();
            return;
        }
        if (this.isSelected == z2) {
            return;
        }
        if (z2) {
            if (this.isIcon) {
                if (!this.desktopIcon.isShowing()) {
                    return;
                }
            } else if (!isShowing()) {
                return;
            }
        }
        Boolean bool = this.isSelected ? Boolean.TRUE : Boolean.FALSE;
        Boolean bool2 = z2 ? Boolean.TRUE : Boolean.FALSE;
        fireVetoableChange(IS_SELECTED_PROPERTY, bool, bool2);
        if (z2) {
            restoreSubcomponentFocus();
        }
        this.isSelected = z2;
        firePropertyChange(IS_SELECTED_PROPERTY, bool, bool2);
        if (this.isSelected) {
            fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_ACTIVATED);
        } else {
            fireInternalFrameEvent(25555);
        }
        repaint();
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setFrameIcon(Icon icon) {
        Icon icon2 = this.frameIcon;
        this.frameIcon = icon;
        firePropertyChange(FRAME_ICON_PROPERTY, icon2, icon);
    }

    public Icon getFrameIcon() {
        return this.frameIcon;
    }

    public void moveToFront() {
        if (isIcon()) {
            if (getDesktopIcon().getParent() instanceof JLayeredPane) {
                ((JLayeredPane) getDesktopIcon().getParent()).moveToFront(getDesktopIcon());
            }
        } else if (getParent() instanceof JLayeredPane) {
            ((JLayeredPane) getParent()).moveToFront(this);
        }
    }

    public void moveToBack() {
        if (isIcon()) {
            if (getDesktopIcon().getParent() instanceof JLayeredPane) {
                ((JLayeredPane) getDesktopIcon().getParent()).moveToBack(getDesktopIcon());
            }
        } else if (getParent() instanceof JLayeredPane) {
            ((JLayeredPane) getParent()).moveToBack(this);
        }
    }

    public Cursor getLastCursor() {
        return this.lastCursor;
    }

    @Override // java.awt.Component
    public void setCursor(Cursor cursor) {
        if (cursor == null) {
            this.lastCursor = null;
            super.setCursor(cursor);
            return;
        }
        int type = cursor.getType();
        if (type != 4 && type != 5 && type != 6 && type != 7 && type != 8 && type != 9 && type != 10 && type != 11) {
            this.lastCursor = cursor;
        }
        super.setCursor(cursor);
    }

    public void setLayer(Integer num) {
        if (getParent() != null && (getParent() instanceof JLayeredPane)) {
            JLayeredPane jLayeredPane = (JLayeredPane) getParent();
            jLayeredPane.setLayer(this, num.intValue(), jLayeredPane.getPosition(this));
        } else {
            JLayeredPane.putLayer(this, num.intValue());
            if (getParent() != null) {
                getParent().repaint(getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public void setLayer(int i2) {
        setLayer(Integer.valueOf(i2));
    }

    public int getLayer() {
        return JLayeredPane.getLayer((JComponent) this);
    }

    public JDesktopPane getDesktopPane() {
        Container container;
        Container parent = getParent();
        while (true) {
            container = parent;
            if (container == null || (container instanceof JDesktopPane)) {
                break;
            }
            parent = container.getParent();
        }
        if (container == null) {
            Container parent2 = getDesktopIcon().getParent();
            while (true) {
                container = parent2;
                if (container == null || (container instanceof JDesktopPane)) {
                    break;
                }
                parent2 = container.getParent();
            }
        }
        return (JDesktopPane) container;
    }

    public void setDesktopIcon(JDesktopIcon jDesktopIcon) {
        JDesktopIcon desktopIcon = getDesktopIcon();
        this.desktopIcon = jDesktopIcon;
        firePropertyChange("desktopIcon", desktopIcon, jDesktopIcon);
    }

    public JDesktopIcon getDesktopIcon() {
        return this.desktopIcon;
    }

    public Rectangle getNormalBounds() {
        if (this.normalBounds != null) {
            return this.normalBounds;
        }
        return getBounds();
    }

    public void setNormalBounds(Rectangle rectangle) {
        this.normalBounds = rectangle;
    }

    public Component getFocusOwner() {
        if (isSelected()) {
            return this.lastFocusOwner;
        }
        return null;
    }

    public Component getMostRecentFocusOwner() {
        if (isSelected()) {
            return getFocusOwner();
        }
        if (this.lastFocusOwner != null) {
            return this.lastFocusOwner;
        }
        FocusTraversalPolicy focusTraversalPolicy = getFocusTraversalPolicy();
        if (focusTraversalPolicy instanceof InternalFrameFocusTraversalPolicy) {
            return ((InternalFrameFocusTraversalPolicy) focusTraversalPolicy).getInitialComponent(this);
        }
        Component defaultComponent = focusTraversalPolicy.getDefaultComponent(this);
        if (defaultComponent != null) {
            return defaultComponent;
        }
        return getContentPane();
    }

    public void restoreSubcomponentFocus() {
        if (isIcon()) {
            SwingUtilities2.compositeRequestFocus(getDesktopIcon());
            return;
        }
        Component permanentFocusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getPermanentFocusOwner();
        if (permanentFocusOwner == null || !SwingUtilities.isDescendingFrom(permanentFocusOwner, this)) {
            setLastFocusOwner(getMostRecentFocusOwner());
            if (this.lastFocusOwner == null) {
                setLastFocusOwner(getContentPane());
            }
            this.lastFocusOwner.requestFocus();
        }
    }

    private void setLastFocusOwner(Component component) {
        this.lastFocusOwner = component;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void reshape(int i2, int i3, int i4, int i5) {
        super.reshape(i2, i3, i4, i5);
        validate();
        repaint();
    }

    public void addInternalFrameListener(InternalFrameListener internalFrameListener) {
        this.listenerList.add(InternalFrameListener.class, internalFrameListener);
        enableEvents(0L);
    }

    public void removeInternalFrameListener(InternalFrameListener internalFrameListener) {
        this.listenerList.remove(InternalFrameListener.class, internalFrameListener);
    }

    public InternalFrameListener[] getInternalFrameListeners() {
        return (InternalFrameListener[]) this.listenerList.getListeners(InternalFrameListener.class);
    }

    protected void fireInternalFrameEvent(int i2) {
        Object[] listenerList = this.listenerList.getListenerList();
        InternalFrameEvent internalFrameEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == InternalFrameListener.class) {
                if (internalFrameEvent == null) {
                    internalFrameEvent = new InternalFrameEvent(this, i2);
                }
                switch (internalFrameEvent.getID()) {
                    case 25549:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameOpened(internalFrameEvent);
                        break;
                    case InternalFrameEvent.INTERNAL_FRAME_CLOSING /* 25550 */:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameClosing(internalFrameEvent);
                        break;
                    case InternalFrameEvent.INTERNAL_FRAME_CLOSED /* 25551 */:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameClosed(internalFrameEvent);
                        break;
                    case InternalFrameEvent.INTERNAL_FRAME_ICONIFIED /* 25552 */:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameIconified(internalFrameEvent);
                        break;
                    case InternalFrameEvent.INTERNAL_FRAME_DEICONIFIED /* 25553 */:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameDeiconified(internalFrameEvent);
                        break;
                    case InternalFrameEvent.INTERNAL_FRAME_ACTIVATED /* 25554 */:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameActivated(internalFrameEvent);
                        break;
                    case 25555:
                        ((InternalFrameListener) listenerList[length + 1]).internalFrameDeactivated(internalFrameEvent);
                        break;
                }
            }
        }
    }

    public void doDefaultCloseAction() {
        fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSING);
        switch (this.defaultCloseOperation) {
            case 1:
                setVisible(false);
                if (isSelected()) {
                    try {
                        setSelected(false);
                        break;
                    } catch (PropertyVetoException e2) {
                        return;
                    }
                }
                break;
            case 2:
                try {
                    fireVetoableChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
                    this.isClosed = true;
                    setVisible(false);
                    firePropertyChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
                    dispose();
                    break;
                } catch (PropertyVetoException e3) {
                    return;
                }
        }
    }

    public void setDefaultCloseOperation(int i2) {
        this.defaultCloseOperation = i2;
    }

    public int getDefaultCloseOperation() {
        return this.defaultCloseOperation;
    }

    public void pack() {
        try {
            if (isIcon()) {
                setIcon(false);
            } else if (isMaximum()) {
                setMaximum(false);
            }
            setSize(getPreferredSize());
            validate();
        } catch (PropertyVetoException e2) {
        }
    }

    @Override // java.awt.Component
    public void show() {
        if (isVisible()) {
            return;
        }
        if (!this.opened) {
            fireInternalFrameEvent(25549);
            this.opened = true;
        }
        getDesktopIcon().setVisible(true);
        toFront();
        super.show();
        if (!this.isIcon && !isSelected()) {
            try {
                setSelected(true);
            } catch (PropertyVetoException e2) {
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void hide() {
        if (isIcon()) {
            getDesktopIcon().setVisible(false);
        }
        super.hide();
    }

    public void dispose() {
        if (isVisible()) {
            setVisible(false);
        }
        if (isSelected()) {
            try {
                setSelected(false);
            } catch (PropertyVetoException e2) {
            }
        }
        if (!this.isClosed) {
            firePropertyChange(IS_CLOSED_PROPERTY, Boolean.FALSE, Boolean.TRUE);
            this.isClosed = true;
        }
        fireInternalFrameEvent(InternalFrameEvent.INTERNAL_FRAME_CLOSED);
    }

    public void toFront() {
        moveToFront();
    }

    public void toBack() {
        moveToBack();
    }

    @Override // java.awt.Container
    public final void setFocusCycleRoot(boolean z2) {
    }

    @Override // java.awt.Container
    public final boolean isFocusCycleRoot() {
        return true;
    }

    @Override // java.awt.Component
    public final Container getFocusCycleRootAncestor() {
        return null;
    }

    public final String getWarningString() {
        return null;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                boolean zIsRootPaneCheckingEnabled = isRootPaneCheckingEnabled();
                try {
                    setRootPaneCheckingEnabled(false);
                    this.ui.installUI(this);
                    setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
                } catch (Throwable th) {
                    setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
                    throw th;
                }
            }
        }
    }

    @Override // javax.swing.JComponent
    void compWriteObjectNotify() {
        boolean zIsRootPaneCheckingEnabled = isRootPaneCheckingEnabled();
        try {
            setRootPaneCheckingEnabled(false);
            super.compWriteObjectNotify();
        } finally {
            setRootPaneCheckingEnabled(zIsRootPaneCheckingEnabled);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        String string = this.rootPane != null ? this.rootPane.toString() : "";
        String str2 = this.rootPaneCheckingEnabled ? "true" : "false";
        String str3 = this.closable ? "true" : "false";
        String str4 = this.isClosed ? "true" : "false";
        String str5 = this.maximizable ? "true" : "false";
        String str6 = this.isMaximum ? "true" : "false";
        String str7 = this.iconable ? "true" : "false";
        String str8 = this.isIcon ? "true" : "false";
        String str9 = this.resizable ? "true" : "false";
        String str10 = this.isSelected ? "true" : "false";
        String string2 = this.frameIcon != null ? this.frameIcon.toString() : "";
        String str11 = this.title != null ? this.title : "";
        String string3 = this.desktopIcon != null ? this.desktopIcon.toString() : "";
        String str12 = this.opened ? "true" : "false";
        if (this.defaultCloseOperation == 1) {
            str = "HIDE_ON_CLOSE";
        } else if (this.defaultCloseOperation == 2) {
            str = "DISPOSE_ON_CLOSE";
        } else if (this.defaultCloseOperation == 0) {
            str = "DO_NOTHING_ON_CLOSE";
        } else {
            str = "";
        }
        return super.paramString() + ",closable=" + str3 + ",defaultCloseOperation=" + str + ",desktopIcon=" + string3 + ",frameIcon=" + string2 + ",iconable=" + str7 + ",isClosed=" + str4 + ",isIcon=" + str8 + ",isMaximum=" + str6 + ",isSelected=" + str10 + ",maximizable=" + str5 + ",opened=" + str12 + ",resizable=" + str9 + ",rootPane=" + string + ",rootPaneCheckingEnabled=" + str2 + ",title=" + str11;
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics graphics) {
        if (this.isDragging) {
            this.danger = true;
        }
        super.paintComponent(graphics);
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJInternalFrame();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JInternalFrame$AccessibleJInternalFrame.class */
    protected class AccessibleJInternalFrame extends JComponent.AccessibleJComponent implements AccessibleValue {
        protected AccessibleJInternalFrame() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            String title = this.accessibleName;
            if (title == null) {
                title = (String) JInternalFrame.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            }
            if (title == null) {
                title = JInternalFrame.this.getTitle();
            }
            return title;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.INTERNAL_FRAME;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(JInternalFrame.this.getLayer());
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number == null) {
                return false;
            }
            JInternalFrame.this.setLayer(new Integer(number.intValue()));
            return true;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return Integer.MIN_VALUE;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return Integer.MAX_VALUE;
        }
    }

    /* loaded from: rt.jar:javax/swing/JInternalFrame$JDesktopIcon.class */
    public static class JDesktopIcon extends JComponent implements Accessible {
        JInternalFrame internalFrame;

        public JDesktopIcon(JInternalFrame jInternalFrame) {
            setVisible(false);
            setInternalFrame(jInternalFrame);
            updateUI();
        }

        public DesktopIconUI getUI() {
            return (DesktopIconUI) this.ui;
        }

        public void setUI(DesktopIconUI desktopIconUI) {
            super.setUI((ComponentUI) desktopIconUI);
        }

        public JInternalFrame getInternalFrame() {
            return this.internalFrame;
        }

        public void setInternalFrame(JInternalFrame jInternalFrame) {
            this.internalFrame = jInternalFrame;
        }

        public JDesktopPane getDesktopPane() {
            if (getInternalFrame() != null) {
                return getInternalFrame().getDesktopPane();
            }
            return null;
        }

        @Override // javax.swing.JComponent
        public void updateUI() {
            boolean z2 = this.ui != null;
            setUI((DesktopIconUI) UIManager.getUI(this));
            invalidate();
            Dimension preferredSize = getPreferredSize();
            setSize(preferredSize.width, preferredSize.height);
            if (this.internalFrame != null && this.internalFrame.getUI() != null) {
                SwingUtilities.updateComponentTreeUI(this.internalFrame);
            }
        }

        void updateUIWhenHidden() {
            setUI((DesktopIconUI) UIManager.getUI(this));
            Dimension preferredSize = getPreferredSize();
            setSize(preferredSize.width, preferredSize.height);
            invalidate();
            Component[] components = getComponents();
            if (components != null) {
                for (Component component : components) {
                    SwingUtilities.updateComponentTreeUI(component);
                }
            }
        }

        @Override // javax.swing.JComponent
        public String getUIClassID() {
            return "DesktopIconUI";
        }

        private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
            objectOutputStream.defaultWriteObject();
            if (getUIClassID().equals("DesktopIconUI")) {
                byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
                JComponent.setWriteObjCounter(this, writeObjCounter);
                if (writeObjCounter == 0 && this.ui != null) {
                    this.ui.installUI(this);
                }
            }
        }

        @Override // java.awt.Component
        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new AccessibleJDesktopIcon();
            }
            return this.accessibleContext;
        }

        /* loaded from: rt.jar:javax/swing/JInternalFrame$JDesktopIcon$AccessibleJDesktopIcon.class */
        protected class AccessibleJDesktopIcon extends JComponent.AccessibleJComponent implements AccessibleValue {
            protected AccessibleJDesktopIcon() {
                super();
            }

            @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return AccessibleRole.DESKTOP_ICON;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleValue getAccessibleValue() {
                return this;
            }

            @Override // javax.accessibility.AccessibleValue
            public Number getCurrentAccessibleValue() {
                AccessibleValue accessibleValue = JDesktopIcon.this.getInternalFrame().getAccessibleContext().getAccessibleValue();
                if (accessibleValue != null) {
                    return accessibleValue.getCurrentAccessibleValue();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleValue
            public boolean setCurrentAccessibleValue(Number number) {
                AccessibleValue accessibleValue;
                if (number != null && (accessibleValue = JDesktopIcon.this.getInternalFrame().getAccessibleContext().getAccessibleValue()) != null) {
                    return accessibleValue.setCurrentAccessibleValue(number);
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleValue
            public Number getMinimumAccessibleValue() {
                Object accessibleContext = JDesktopIcon.this.getInternalFrame().getAccessibleContext();
                if (accessibleContext instanceof AccessibleValue) {
                    return ((AccessibleValue) accessibleContext).getMinimumAccessibleValue();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleValue
            public Number getMaximumAccessibleValue() {
                Object accessibleContext = JDesktopIcon.this.getInternalFrame().getAccessibleContext();
                if (accessibleContext instanceof AccessibleValue) {
                    return ((AccessibleValue) accessibleContext).getMaximumAccessibleValue();
                }
                return null;
            }
        }
    }
}
