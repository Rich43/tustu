package javax.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.util.Iterator;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.swing.JComponent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.basic.BasicComboPopup;
import sun.awt.SunToolkit;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:javax/swing/JPopupMenu.class */
public class JPopupMenu extends JComponent implements Accessible, MenuElement {
    private static final String uiClassID = "PopupMenuUI";
    static boolean popupPostionFixDisabled;
    transient Component invoker;
    transient Popup popup;
    transient Frame frame;
    private int desiredLocationX;
    private int desiredLocationY;
    private String label;
    private boolean paintBorder;
    private Insets margin;
    private boolean lightWeightPopup;
    private SingleSelectionModel selectionModel;
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    private static final Object defaultLWPopupEnabledKey = new StringBuffer("JPopupMenu.defaultLWPopupEnabledKey");
    private static final Object classLock = new Object();

    static {
        popupPostionFixDisabled = false;
        popupPostionFixDisabled = ((String) AccessController.doPrivileged(new GetPropertyAction("javax.swing.adjustPopupLocationToFit", ""))).equals("false");
    }

    public static void setDefaultLightWeightPopupEnabled(boolean z2) {
        SwingUtilities.appContextPut(defaultLWPopupEnabledKey, Boolean.valueOf(z2));
    }

    public static boolean getDefaultLightWeightPopupEnabled() {
        Boolean bool = (Boolean) SwingUtilities.appContextGet(defaultLWPopupEnabledKey);
        if (bool == null) {
            SwingUtilities.appContextPut(defaultLWPopupEnabledKey, Boolean.TRUE);
            return true;
        }
        return bool.booleanValue();
    }

    public JPopupMenu() {
        this(null);
    }

    public JPopupMenu(String str) {
        this.label = null;
        this.paintBorder = true;
        this.margin = null;
        this.lightWeightPopup = true;
        this.label = str;
        this.lightWeightPopup = getDefaultLightWeightPopupEnabled();
        setSelectionModel(new DefaultSingleSelectionModel());
        enableEvents(16L);
        setFocusTraversalKeysEnabled(false);
        updateUI();
    }

    public PopupMenuUI getUI() {
        return (PopupMenuUI) this.ui;
    }

    public void setUI(PopupMenuUI popupMenuUI) {
        super.setUI((ComponentUI) popupMenuUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((PopupMenuUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // java.awt.Component
    protected void processFocusEvent(FocusEvent focusEvent) {
        super.processFocusEvent(focusEvent);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processKeyEvent(KeyEvent keyEvent) {
        MenuSelectionManager.defaultManager().processKeyEvent(keyEvent);
        if (keyEvent.isConsumed()) {
            return;
        }
        super.processKeyEvent(keyEvent);
    }

    public SingleSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    public void setSelectionModel(SingleSelectionModel singleSelectionModel) {
        this.selectionModel = singleSelectionModel;
    }

    public JMenuItem add(JMenuItem jMenuItem) {
        super.add((Component) jMenuItem);
        return jMenuItem;
    }

    public JMenuItem add(String str) {
        return add(new JMenuItem(str));
    }

    public JMenuItem add(Action action) {
        JMenuItem jMenuItemCreateActionComponent = createActionComponent(action);
        jMenuItemCreateActionComponent.setAction(action);
        add(jMenuItemCreateActionComponent);
        return jMenuItemCreateActionComponent;
    }

    Point adjustPopupLocationToFitScreen(int i2, int i3) throws HeadlessException {
        Rectangle rectangle;
        Point point = new Point(i2, i3);
        if (popupPostionFixDisabled || GraphicsEnvironment.isHeadless()) {
            return point;
        }
        GraphicsConfiguration currentGraphicsConfiguration = getCurrentGraphicsConfiguration(point);
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (currentGraphicsConfiguration != null) {
            rectangle = currentGraphicsConfiguration.getBounds();
        } else {
            rectangle = new Rectangle(defaultToolkit.getScreenSize());
        }
        Dimension preferredSize = getPreferredSize();
        long j2 = point.f12370x + preferredSize.width;
        long j3 = point.f12371y + preferredSize.height;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        if (!canPopupOverlapTaskBar()) {
            Insets screenInsets = defaultToolkit.getScreenInsets(currentGraphicsConfiguration);
            rectangle.f12372x += screenInsets.left;
            rectangle.f12373y += screenInsets.top;
            i4 -= screenInsets.left + screenInsets.right;
            i5 -= screenInsets.top + screenInsets.bottom;
        }
        int i6 = rectangle.f12372x + i4;
        int i7 = rectangle.f12373y + i5;
        if (j2 > i6) {
            point.f12370x = i6 - preferredSize.width;
        }
        if (j3 > i7) {
            point.f12371y = i7 - preferredSize.height;
        }
        if (point.f12370x < rectangle.f12372x) {
            point.f12370x = rectangle.f12372x;
        }
        if (point.f12371y < rectangle.f12373y) {
            point.f12371y = rectangle.f12373y;
        }
        return point;
    }

    private GraphicsConfiguration getCurrentGraphicsConfiguration(Point point) throws HeadlessException {
        GraphicsConfiguration graphicsConfiguration = null;
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        int i2 = 0;
        while (true) {
            if (i2 >= screenDevices.length) {
                break;
            }
            if (screenDevices[i2].getType() == 0) {
                GraphicsConfiguration defaultConfiguration = screenDevices[i2].getDefaultConfiguration();
                if (defaultConfiguration.getBounds().contains(point)) {
                    graphicsConfiguration = defaultConfiguration;
                    break;
                }
            }
            i2++;
        }
        if (graphicsConfiguration == null && getInvoker() != null) {
            graphicsConfiguration = getInvoker().getGraphicsConfiguration();
        }
        return graphicsConfiguration;
    }

    static boolean canPopupOverlapTaskBar() {
        boolean zCanPopupOverlapTaskBar = true;
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        if (defaultToolkit instanceof SunToolkit) {
            zCanPopupOverlapTaskBar = ((SunToolkit) defaultToolkit).canPopupOverlapTaskBar();
        }
        return zCanPopupOverlapTaskBar;
    }

    protected JMenuItem createActionComponent(Action action) {
        JMenuItem jMenuItem = new JMenuItem() { // from class: javax.swing.JPopupMenu.1
            @Override // javax.swing.AbstractButton
            protected PropertyChangeListener createActionPropertyChangeListener(Action action2) {
                PropertyChangeListener propertyChangeListenerCreateActionChangeListener = JPopupMenu.this.createActionChangeListener(this);
                if (propertyChangeListenerCreateActionChangeListener == null) {
                    propertyChangeListenerCreateActionChangeListener = super.createActionPropertyChangeListener(action2);
                }
                return propertyChangeListenerCreateActionChangeListener;
            }
        };
        jMenuItem.setHorizontalTextPosition(11);
        jMenuItem.setVerticalTextPosition(0);
        return jMenuItem;
    }

    protected PropertyChangeListener createActionChangeListener(JMenuItem jMenuItem) {
        return jMenuItem.createActionPropertyChangeListener0(jMenuItem.getAction());
    }

    @Override // java.awt.Container
    public void remove(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        if (i2 > getComponentCount() - 1) {
            throw new IllegalArgumentException("index greater than the number of items.");
        }
        super.remove(i2);
    }

    public void setLightWeightPopupEnabled(boolean z2) {
        this.lightWeightPopup = z2;
    }

    public boolean isLightWeightPopupEnabled() {
        return this.lightWeightPopup;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        String str2 = this.label;
        this.label = str;
        firePropertyChange("label", str2, str);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, str2, str);
        }
        invalidate();
        repaint();
    }

    public void addSeparator() {
        add(new Separator());
    }

    public void insert(Action action, int i2) {
        JMenuItem jMenuItemCreateActionComponent = createActionComponent(action);
        jMenuItemCreateActionComponent.setAction(action);
        insert(jMenuItemCreateActionComponent, i2);
    }

    public void insert(Component component, int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        int componentCount = getComponentCount();
        Vector vector = new Vector();
        for (int i3 = i2; i3 < componentCount; i3++) {
            vector.addElement(getComponent(i2));
            remove(i2);
        }
        add(component);
        Iterator it = vector.iterator();
        while (it.hasNext()) {
            add((Component) it.next());
        }
    }

    public void addPopupMenuListener(PopupMenuListener popupMenuListener) {
        this.listenerList.add(PopupMenuListener.class, popupMenuListener);
    }

    public void removePopupMenuListener(PopupMenuListener popupMenuListener) {
        this.listenerList.remove(PopupMenuListener.class, popupMenuListener);
    }

    public PopupMenuListener[] getPopupMenuListeners() {
        return (PopupMenuListener[]) this.listenerList.getListeners(PopupMenuListener.class);
    }

    public void addMenuKeyListener(MenuKeyListener menuKeyListener) {
        this.listenerList.add(MenuKeyListener.class, menuKeyListener);
    }

    public void removeMenuKeyListener(MenuKeyListener menuKeyListener) {
        this.listenerList.remove(MenuKeyListener.class, menuKeyListener);
    }

    public MenuKeyListener[] getMenuKeyListeners() {
        return (MenuKeyListener[]) this.listenerList.getListeners(MenuKeyListener.class);
    }

    protected void firePopupMenuWillBecomeVisible() {
        Object[] listenerList = this.listenerList.getListenerList();
        PopupMenuEvent popupMenuEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == PopupMenuListener.class) {
                if (popupMenuEvent == null) {
                    popupMenuEvent = new PopupMenuEvent(this);
                }
                ((PopupMenuListener) listenerList[length + 1]).popupMenuWillBecomeVisible(popupMenuEvent);
            }
        }
    }

    protected void firePopupMenuWillBecomeInvisible() {
        Object[] listenerList = this.listenerList.getListenerList();
        PopupMenuEvent popupMenuEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == PopupMenuListener.class) {
                if (popupMenuEvent == null) {
                    popupMenuEvent = new PopupMenuEvent(this);
                }
                ((PopupMenuListener) listenerList[length + 1]).popupMenuWillBecomeInvisible(popupMenuEvent);
            }
        }
    }

    protected void firePopupMenuCanceled() {
        Object[] listenerList = this.listenerList.getListenerList();
        PopupMenuEvent popupMenuEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == PopupMenuListener.class) {
                if (popupMenuEvent == null) {
                    popupMenuEvent = new PopupMenuEvent(this);
                }
                ((PopupMenuListener) listenerList[length + 1]).popupMenuCanceled(popupMenuEvent);
            }
        }
    }

    @Override // javax.swing.JComponent
    boolean alwaysOnTop() {
        return true;
    }

    public void pack() throws HeadlessException {
        if (this.popup != null) {
            Dimension preferredSize = getPreferredSize();
            if (preferredSize == null || preferredSize.width != getWidth() || preferredSize.height != getHeight()) {
                showPopup();
            } else {
                validate();
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setVisible(boolean z2) throws HeadlessException {
        if (z2 == isVisible()) {
            return;
        }
        if (!z2) {
            Boolean bool = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");
            if (bool != null && bool == Boolean.TRUE) {
                putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.FALSE);
                firePopupMenuCanceled();
            }
            getSelectionModel().clearSelection();
        } else if (isPopupMenu()) {
            MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{this});
        }
        if (z2) {
            firePopupMenuWillBecomeVisible();
            showPopup();
            firePropertyChange("visible", Boolean.FALSE, Boolean.TRUE);
        } else if (this.popup != null) {
            firePopupMenuWillBecomeInvisible();
            this.popup.hide();
            this.popup = null;
            firePropertyChange("visible", Boolean.TRUE, Boolean.FALSE);
            if (isPopupMenu()) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
            }
        }
    }

    private void showPopup() throws HeadlessException {
        Popup popup = this.popup;
        if (popup != null) {
            popup.hide();
        }
        PopupFactory sharedInstance = PopupFactory.getSharedInstance();
        if (isLightWeightPopupEnabled()) {
            sharedInstance.setPopupType(0);
        } else {
            sharedInstance.setPopupType(2);
        }
        Point pointAdjustPopupLocationToFitScreen = adjustPopupLocationToFitScreen(this.desiredLocationX, this.desiredLocationY);
        this.desiredLocationX = pointAdjustPopupLocationToFitScreen.f12370x;
        this.desiredLocationY = pointAdjustPopupLocationToFitScreen.f12371y;
        Popup popup2 = getUI().getPopup(this, this.desiredLocationX, this.desiredLocationY);
        sharedInstance.setPopupType(0);
        this.popup = popup2;
        popup2.show();
    }

    @Override // java.awt.Component
    public boolean isVisible() {
        return this.popup != null;
    }

    @Override // java.awt.Component
    public void setLocation(int i2, int i3) throws HeadlessException {
        int i4 = this.desiredLocationX;
        int i5 = this.desiredLocationY;
        this.desiredLocationX = i2;
        this.desiredLocationY = i3;
        if (this.popup != null) {
            if (i2 != i4 || i3 != i5) {
                showPopup();
            }
        }
    }

    private boolean isPopupMenu() {
        return (this.invoker == null || (this.invoker instanceof JMenu)) ? false : true;
    }

    public Component getInvoker() {
        return this.invoker;
    }

    public void setInvoker(Component component) {
        Component component2 = this.invoker;
        this.invoker = component;
        if (component2 != this.invoker && this.ui != null) {
            this.ui.uninstallUI(this);
            this.ui.installUI(this);
        }
        invalidate();
    }

    public void show(Component component, int i2, int i3) {
        setInvoker(component);
        Frame frame = getFrame(component);
        if (frame != this.frame && frame != null) {
            this.frame = frame;
            if (this.popup != null) {
                setVisible(false);
            }
        }
        if (component != null) {
            Point locationOnScreen = component.getLocationOnScreen();
            long j2 = locationOnScreen.f12370x + i2;
            long j3 = locationOnScreen.f12371y + i3;
            if (j2 > 2147483647L) {
                j2 = 2147483647L;
            }
            if (j2 < -2147483648L) {
                j2 = -2147483648L;
            }
            if (j3 > 2147483647L) {
                j3 = 2147483647L;
            }
            if (j3 < -2147483648L) {
                j3 = -2147483648L;
            }
            setLocation((int) j2, (int) j3);
        } else {
            setLocation(i2, i3);
        }
        setVisible(true);
    }

    JPopupMenu getRootPopupMenu() {
        JPopupMenu jPopupMenu;
        JPopupMenu jPopupMenu2 = this;
        while (true) {
            jPopupMenu = jPopupMenu2;
            if (jPopupMenu == null || jPopupMenu.isPopupMenu() || jPopupMenu.getInvoker() == null || jPopupMenu.getInvoker().getParent() == null || !(jPopupMenu.getInvoker().getParent() instanceof JPopupMenu)) {
                break;
            }
            jPopupMenu2 = (JPopupMenu) jPopupMenu.getInvoker().getParent();
        }
        return jPopupMenu;
    }

    @Deprecated
    public Component getComponentAtIndex(int i2) {
        return getComponent(i2);
    }

    public int getComponentIndex(Component component) {
        int componentCount = getComponentCount();
        Component[] components = getComponents();
        for (int i2 = 0; i2 < componentCount; i2++) {
            if (components[i2] == component) {
                return i2;
            }
        }
        return -1;
    }

    public void setPopupSize(Dimension dimension) throws HeadlessException {
        Dimension preferredSize = getPreferredSize();
        setPreferredSize(dimension);
        if (this.popup != null && !preferredSize.equals(getPreferredSize())) {
            showPopup();
        }
    }

    public void setPopupSize(int i2, int i3) throws HeadlessException {
        setPopupSize(new Dimension(i2, i3));
    }

    public void setSelected(Component component) {
        getSelectionModel().setSelectedIndex(getComponentIndex(component));
    }

    public boolean isBorderPainted() {
        return this.paintBorder;
    }

    public void setBorderPainted(boolean z2) {
        this.paintBorder = z2;
        repaint();
    }

    @Override // javax.swing.JComponent
    protected void paintBorder(Graphics graphics) {
        if (isBorderPainted()) {
            super.paintBorder(graphics);
        }
    }

    public Insets getMargin() {
        if (this.margin == null) {
            return new Insets(0, 0, 0, 0);
        }
        return this.margin;
    }

    boolean isSubPopupMenu(JPopupMenu jPopupMenu) {
        JPopupMenu popupMenu;
        int componentCount = getComponentCount();
        Component[] components = getComponents();
        for (int i2 = 0; i2 < componentCount; i2++) {
            Component component = components[i2];
            if ((component instanceof JMenu) && ((popupMenu = ((JMenu) component).getPopupMenu()) == jPopupMenu || popupMenu.isSubPopupMenu(jPopupMenu))) {
                return true;
            }
        }
        return false;
    }

    private static Frame getFrame(Component component) {
        Component component2;
        Component parent = component;
        while (true) {
            component2 = parent;
            if ((component2 instanceof Frame) || component2 == null) {
                break;
            }
            parent = component2.getParent();
        }
        return (Frame) component2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",desiredLocationX=" + this.desiredLocationX + ",desiredLocationY=" + this.desiredLocationY + ",label=" + (this.label != null ? this.label : "") + ",lightWeightPopupEnabled=" + (isLightWeightPopupEnabled() ? "true" : "false") + ",margin=" + (this.margin != null ? this.margin.toString() : "") + ",paintBorder=" + (this.paintBorder ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJPopupMenu();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JPopupMenu$AccessibleJPopupMenu.class */
    protected class AccessibleJPopupMenu extends JComponent.AccessibleJComponent implements PropertyChangeListener {
        protected AccessibleJPopupMenu() {
            super();
            JPopupMenu.this.addPropertyChangeListener(this);
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.POPUP_MENU;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName() == "visible") {
                if (propertyChangeEvent.getOldValue() == Boolean.FALSE && propertyChangeEvent.getNewValue() == Boolean.TRUE) {
                    handlePopupIsVisibleEvent(true);
                } else if (propertyChangeEvent.getOldValue() == Boolean.TRUE && propertyChangeEvent.getNewValue() == Boolean.FALSE) {
                    handlePopupIsVisibleEvent(false);
                }
            }
        }

        private void handlePopupIsVisibleEvent(boolean z2) {
            if (z2) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.VISIBLE);
                fireActiveDescendant();
            } else {
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.VISIBLE, null);
            }
        }

        private void fireActiveDescendant() {
            JList list;
            AccessibleSelection accessibleSelection;
            Accessible accessibleSelection2;
            AccessibleContext accessibleContext;
            AccessibleContext accessibleContext2;
            if ((JPopupMenu.this instanceof BasicComboPopup) && (list = ((BasicComboPopup) JPopupMenu.this).getList()) != null && (accessibleSelection = list.getAccessibleContext().getAccessibleSelection()) != null && (accessibleSelection2 = accessibleSelection.getAccessibleSelection(0)) != null && (accessibleContext = accessibleSelection2.getAccessibleContext()) != null && JPopupMenu.this.invoker != null && (accessibleContext2 = JPopupMenu.this.invoker.getAccessibleContext()) != null) {
                accessibleContext2.firePropertyChange(AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY, null, accessibleContext);
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Vector vector = new Vector();
        objectOutputStream.defaultWriteObject();
        if (this.invoker != null && (this.invoker instanceof Serializable)) {
            vector.addElement("invoker");
            vector.addElement(this.invoker);
        }
        if (this.popup != null && (this.popup instanceof Serializable)) {
            vector.addElement("popup");
            vector.addElement(this.popup);
        }
        objectOutputStream.writeObject(vector);
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Vector vector = (Vector) objectInputStream.readObject();
        int i2 = 0;
        int size = vector.size();
        if (0 < size && vector.elementAt(0).equals("invoker")) {
            int i3 = 0 + 1;
            this.invoker = (Component) vector.elementAt(i3);
            i2 = i3 + 1;
        }
        if (i2 < size && vector.elementAt(i2).equals("popup")) {
            int i4 = i2 + 1;
            this.popup = (Popup) vector.elementAt(i4);
            int i5 = i4 + 1;
        }
    }

    @Override // javax.swing.MenuElement
    public void processMouseEvent(MouseEvent mouseEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
    }

    @Override // javax.swing.MenuElement
    public void processKeyEvent(KeyEvent keyEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        MenuKeyEvent menuKeyEvent = new MenuKeyEvent(keyEvent.getComponent(), keyEvent.getID(), keyEvent.getWhen(), keyEvent.getModifiers(), keyEvent.getKeyCode(), keyEvent.getKeyChar(), menuElementArr, menuSelectionManager);
        processMenuKeyEvent(menuKeyEvent);
        if (menuKeyEvent.isConsumed()) {
            keyEvent.consume();
        }
    }

    private void processMenuKeyEvent(MenuKeyEvent menuKeyEvent) {
        switch (menuKeyEvent.getID()) {
            case 400:
                fireMenuKeyTyped(menuKeyEvent);
                break;
            case 401:
                fireMenuKeyPressed(menuKeyEvent);
                break;
            case 402:
                fireMenuKeyReleased(menuKeyEvent);
                break;
        }
    }

    private void fireMenuKeyPressed(MenuKeyEvent menuKeyEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuKeyListener.class) {
                ((MenuKeyListener) listenerList[length + 1]).menuKeyPressed(menuKeyEvent);
            }
        }
    }

    private void fireMenuKeyReleased(MenuKeyEvent menuKeyEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuKeyListener.class) {
                ((MenuKeyListener) listenerList[length + 1]).menuKeyReleased(menuKeyEvent);
            }
        }
    }

    private void fireMenuKeyTyped(MenuKeyEvent menuKeyEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuKeyListener.class) {
                ((MenuKeyListener) listenerList[length + 1]).menuKeyTyped(menuKeyEvent);
            }
        }
    }

    @Override // javax.swing.MenuElement
    public void menuSelectionChanged(boolean z2) throws HeadlessException {
        if (this.invoker instanceof JMenu) {
            JMenu jMenu = (JMenu) this.invoker;
            if (z2) {
                jMenu.setPopupMenuVisible(true);
            } else {
                jMenu.setPopupMenuVisible(false);
            }
        }
        if (isPopupMenu() && !z2) {
            setVisible(false);
        }
    }

    @Override // javax.swing.MenuElement
    public MenuElement[] getSubElements() {
        Vector vector = new Vector();
        int componentCount = getComponentCount();
        for (int i2 = 0; i2 < componentCount; i2++) {
            MenuContainer component = getComponent(i2);
            if (component instanceof MenuElement) {
                vector.addElement((MenuElement) component);
            }
        }
        MenuElement[] menuElementArr = new MenuElement[vector.size()];
        int size = vector.size();
        for (int i3 = 0; i3 < size; i3++) {
            menuElementArr[i3] = (MenuElement) vector.elementAt(i3);
        }
        return menuElementArr;
    }

    @Override // javax.swing.MenuElement
    public Component getComponent() {
        return this;
    }

    /* loaded from: rt.jar:javax/swing/JPopupMenu$Separator.class */
    public static class Separator extends JSeparator {
        public Separator() {
            super(0);
        }

        @Override // javax.swing.JSeparator, javax.swing.JComponent
        public String getUIClassID() {
            return "PopupMenuSeparatorUI";
        }
    }

    public boolean isPopupTrigger(MouseEvent mouseEvent) {
        return getUI().isPopupTrigger(mouseEvent);
    }
}
