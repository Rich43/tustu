package javax.swing;

import Q.a;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.PopupMenuUI;

/* loaded from: rt.jar:javax/swing/JMenu.class */
public class JMenu extends JMenuItem implements Accessible, MenuElement {
    private static final String uiClassID = "MenuUI";
    private JPopupMenu popupMenu;
    private ChangeListener menuChangeListener;
    private MenuEvent menuEvent;
    private int delay;
    private Point customMenuLocation;
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    protected WinListener popupListener;

    public JMenu() {
        this("");
    }

    public JMenu(String str) {
        super(str);
        this.menuChangeListener = null;
        this.menuEvent = null;
        this.customMenuLocation = null;
    }

    public JMenu(Action action) {
        this();
        setAction(action);
    }

    public JMenu(String str, boolean z2) {
        this(str);
    }

    @Override // javax.swing.JMenuItem
    void initFocusability() {
    }

    @Override // javax.swing.JMenuItem, javax.swing.AbstractButton, javax.swing.JComponent
    public void updateUI() {
        setUI((MenuItemUI) UIManager.getUI(this));
        if (this.popupMenu != null) {
            this.popupMenu.setUI((PopupMenuUI) UIManager.getUI(this.popupMenu));
        }
    }

    @Override // javax.swing.JMenuItem, javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.JMenuItem, javax.swing.AbstractButton
    public void setModel(ButtonModel buttonModel) throws IllegalArgumentException {
        ButtonModel model = getModel();
        super.setModel(buttonModel);
        if (model != null && this.menuChangeListener != null) {
            model.removeChangeListener(this.menuChangeListener);
            this.menuChangeListener = null;
        }
        this.model = buttonModel;
        if (buttonModel != null) {
            this.menuChangeListener = createMenuChangeListener();
            buttonModel.addChangeListener(this.menuChangeListener);
        }
    }

    @Override // javax.swing.AbstractButton
    public boolean isSelected() {
        return getModel().isSelected();
    }

    @Override // javax.swing.AbstractButton
    public void setSelected(boolean z2) {
        ButtonModel model = getModel();
        model.isSelected();
        if (z2 != model.isSelected()) {
            getModel().setSelected(z2);
        }
    }

    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return this.popupMenu.isVisible();
    }

    public void setPopupMenuVisible(boolean z2) {
        if (z2 != isPopupMenuVisible()) {
            if (isEnabled() || !z2) {
                ensurePopupMenuCreated();
                if (z2 && isShowing()) {
                    Point customMenuLocation = getCustomMenuLocation();
                    if (customMenuLocation == null) {
                        customMenuLocation = getPopupMenuOrigin();
                    }
                    getPopupMenu().show(this, customMenuLocation.f12370x, customMenuLocation.f12371y);
                    return;
                }
                getPopupMenu().setVisible(false);
            }
        }
    }

    protected Point getPopupMenuOrigin() throws HeadlessException {
        int i2;
        int i3;
        JPopupMenu popupMenu = getPopupMenu();
        Dimension size = getSize();
        Dimension size2 = popupMenu.getSize();
        if (size2.width == 0) {
            size2 = popupMenu.getPreferredSize();
        }
        Point locationOnScreen = getLocationOnScreen();
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        GraphicsConfiguration graphicsConfiguration = getGraphicsConfiguration();
        Rectangle rectangle = new Rectangle(defaultToolkit.getScreenSize());
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        int i4 = 0;
        while (true) {
            if (i4 >= screenDevices.length) {
                break;
            }
            if (screenDevices[i4].getType() == 0) {
                GraphicsConfiguration defaultConfiguration = screenDevices[i4].getDefaultConfiguration();
                if (defaultConfiguration.getBounds().contains(locationOnScreen)) {
                    graphicsConfiguration = defaultConfiguration;
                    break;
                }
            }
            i4++;
        }
        if (graphicsConfiguration != null) {
            rectangle = graphicsConfiguration.getBounds();
            Insets screenInsets = defaultToolkit.getScreenInsets(graphicsConfiguration);
            rectangle.width -= Math.abs(screenInsets.left + screenInsets.right);
            rectangle.height -= Math.abs(screenInsets.top + screenInsets.bottom);
            locationOnScreen.f12370x -= Math.abs(screenInsets.left);
            locationOnScreen.f12371y -= Math.abs(screenInsets.top);
        }
        if (getParent() instanceof JPopupMenu) {
            int i5 = UIManager.getInt("Menu.submenuPopupOffsetX");
            int i6 = UIManager.getInt("Menu.submenuPopupOffsetY");
            if (SwingUtilities.isLeftToRight(this)) {
                i2 = size.width + i5;
                if (locationOnScreen.f12370x + i2 + size2.width >= rectangle.width + rectangle.f12372x && rectangle.width - size.width < 2 * (locationOnScreen.f12370x - rectangle.f12372x)) {
                    i2 = (0 - i5) - size2.width;
                }
            } else {
                i2 = (0 - i5) - size2.width;
                if (locationOnScreen.f12370x + i2 < rectangle.f12372x && rectangle.width - size.width > 2 * (locationOnScreen.f12370x - rectangle.f12372x)) {
                    i2 = size.width + i5;
                }
            }
            i3 = i6;
            if (locationOnScreen.f12371y + i3 + size2.height >= rectangle.height + rectangle.f12373y && rectangle.height - size.height < 2 * (locationOnScreen.f12371y - rectangle.f12373y)) {
                i3 = (size.height - i6) - size2.height;
            }
        } else {
            int i7 = UIManager.getInt("Menu.menuPopupOffsetX");
            int i8 = UIManager.getInt("Menu.menuPopupOffsetY");
            if (SwingUtilities.isLeftToRight(this)) {
                i2 = i7;
                if (locationOnScreen.f12370x + i2 + size2.width >= rectangle.width + rectangle.f12372x && rectangle.width - size.width < 2 * (locationOnScreen.f12370x - rectangle.f12372x)) {
                    i2 = (size.width - i7) - size2.width;
                }
            } else {
                i2 = (size.width - i7) - size2.width;
                if (locationOnScreen.f12370x + i2 < rectangle.f12372x && rectangle.width - size.width > 2 * (locationOnScreen.f12370x - rectangle.f12372x)) {
                    i2 = i7;
                }
            }
            i3 = size.height + i8;
            if (locationOnScreen.f12371y + i3 + size2.height >= rectangle.height + rectangle.f12373y && rectangle.height - size.height < 2 * (locationOnScreen.f12371y - rectangle.f12373y)) {
                i3 = (0 - i8) - size2.height;
            }
        }
        return new Point(i2, i3);
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("Delay must be a positive integer");
        }
        this.delay = i2;
    }

    private void ensurePopupMenuCreated() {
        if (this.popupMenu == null) {
            this.popupMenu = new JPopupMenu();
            this.popupMenu.setInvoker(this);
            this.popupListener = createWinListener(this.popupMenu);
        }
    }

    private Point getCustomMenuLocation() {
        return this.customMenuLocation;
    }

    public void setMenuLocation(int i2, int i3) throws HeadlessException {
        this.customMenuLocation = new Point(i2, i3);
        if (this.popupMenu != null) {
            this.popupMenu.setLocation(i2, i3);
        }
    }

    public JMenuItem add(JMenuItem jMenuItem) {
        ensurePopupMenuCreated();
        return this.popupMenu.add(jMenuItem);
    }

    @Override // java.awt.Container, bA.f
    public Component add(Component component) {
        ensurePopupMenuCreated();
        this.popupMenu.add(component);
        return component;
    }

    @Override // java.awt.Container
    public Component add(Component component, int i2) {
        ensurePopupMenuCreated();
        this.popupMenu.add(component, i2);
        return component;
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

    protected JMenuItem createActionComponent(Action action) {
        JMenuItem jMenuItem = new JMenuItem() { // from class: javax.swing.JMenu.1
            @Override // javax.swing.AbstractButton
            protected PropertyChangeListener createActionPropertyChangeListener(Action action2) {
                PropertyChangeListener propertyChangeListenerCreateActionChangeListener = JMenu.this.createActionChangeListener(this);
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

    public void addSeparator() {
        ensurePopupMenuCreated();
        this.popupMenu.addSeparator();
    }

    public void insert(String str, int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        this.popupMenu.insert(new JMenuItem(str), i2);
    }

    public JMenuItem insert(JMenuItem jMenuItem, int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        this.popupMenu.insert(jMenuItem, i2);
        return jMenuItem;
    }

    public JMenuItem insert(Action action, int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        JMenuItem jMenuItem = new JMenuItem(action);
        jMenuItem.setHorizontalTextPosition(11);
        jMenuItem.setVerticalTextPosition(0);
        this.popupMenu.insert(jMenuItem, i2);
        return jMenuItem;
    }

    public void insertSeparator(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        this.popupMenu.insert(new JPopupMenu.Separator(), i2);
    }

    public JMenuItem getItem(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        Component menuComponent = getMenuComponent(i2);
        if (menuComponent instanceof JMenuItem) {
            return (JMenuItem) menuComponent;
        }
        return null;
    }

    public int getItemCount() {
        return getMenuComponentCount();
    }

    public boolean isTearOff() {
        throw new Error("boolean isTearOff() {} not yet implemented");
    }

    public void remove(JMenuItem jMenuItem) {
        if (this.popupMenu != null) {
            this.popupMenu.remove(jMenuItem);
        }
    }

    @Override // java.awt.Container
    public void remove(int i2) {
        if (i2 < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        if (i2 > getItemCount()) {
            throw new IllegalArgumentException("index greater than the number of items.");
        }
        if (this.popupMenu != null) {
            this.popupMenu.remove(i2);
        }
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        if (this.popupMenu != null) {
            this.popupMenu.remove(component);
        }
    }

    @Override // java.awt.Container
    public void removeAll() {
        if (this.popupMenu != null) {
            this.popupMenu.removeAll();
        }
    }

    public int getMenuComponentCount() {
        int componentCount = 0;
        if (this.popupMenu != null) {
            componentCount = this.popupMenu.getComponentCount();
        }
        return componentCount;
    }

    public Component getMenuComponent(int i2) {
        if (this.popupMenu != null) {
            return this.popupMenu.getComponent(i2);
        }
        return null;
    }

    public Component[] getMenuComponents() {
        if (this.popupMenu != null) {
            return this.popupMenu.getComponents();
        }
        return new Component[0];
    }

    public boolean isTopLevelMenu() {
        return getParent() instanceof JMenuBar;
    }

    public boolean isMenuComponent(Component component) {
        if (component == this) {
            return true;
        }
        if ((component instanceof JPopupMenu) && ((JPopupMenu) component) == getPopupMenu()) {
            return true;
        }
        int menuComponentCount = getMenuComponentCount();
        Component[] menuComponents = getMenuComponents();
        for (int i2 = 0; i2 < menuComponentCount; i2++) {
            Component component2 = menuComponents[i2];
            if (component2 == component) {
                return true;
            }
            if ((component2 instanceof JMenu) && ((JMenu) component2).isMenuComponent(component)) {
                return true;
            }
        }
        return false;
    }

    private Point translateToPopupMenu(Point point) {
        return translateToPopupMenu(point.f12370x, point.f12371y);
    }

    private Point translateToPopupMenu(int i2, int i3) {
        int i4;
        int i5;
        if (getParent() instanceof JPopupMenu) {
            i4 = i2 - getSize().width;
            i5 = i3;
        } else {
            i4 = i2;
            i5 = i3 - getSize().height;
        }
        return new Point(i4, i5);
    }

    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        return this.popupMenu;
    }

    public void addMenuListener(MenuListener menuListener) {
        this.listenerList.add(MenuListener.class, menuListener);
    }

    public void removeMenuListener(MenuListener menuListener) {
        this.listenerList.remove(MenuListener.class, menuListener);
    }

    public MenuListener[] getMenuListeners() {
        return (MenuListener[]) this.listenerList.getListeners(MenuListener.class);
    }

    protected void fireMenuSelected() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuListener.class) {
                if (listenerList[length + 1] == null) {
                    throw new Error(getText() + " has a NULL Listener!! " + length);
                }
                if (this.menuEvent == null) {
                    this.menuEvent = new MenuEvent(this);
                }
                ((MenuListener) listenerList[length + 1]).menuSelected(this.menuEvent);
            }
        }
    }

    protected void fireMenuDeselected() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuListener.class) {
                if (listenerList[length + 1] == null) {
                    throw new Error(getText() + " has a NULL Listener!! " + length);
                }
                if (this.menuEvent == null) {
                    this.menuEvent = new MenuEvent(this);
                }
                ((MenuListener) listenerList[length + 1]).menuDeselected(this.menuEvent);
            }
        }
    }

    protected void fireMenuCanceled() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuListener.class) {
                if (listenerList[length + 1] == null) {
                    throw new Error(getText() + " has a NULL Listener!! " + length);
                }
                if (this.menuEvent == null) {
                    this.menuEvent = new MenuEvent(this);
                }
                ((MenuListener) listenerList[length + 1]).menuCanceled(this.menuEvent);
            }
        }
    }

    @Override // javax.swing.JMenuItem
    void configureAcceleratorFromAction(Action action) {
    }

    /* loaded from: rt.jar:javax/swing/JMenu$MenuChangeListener.class */
    class MenuChangeListener implements ChangeListener, Serializable {
        boolean isSelected = false;

        MenuChangeListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            boolean zIsSelected = ((ButtonModel) changeEvent.getSource()).isSelected();
            if (zIsSelected != this.isSelected) {
                if (zIsSelected) {
                    JMenu.this.fireMenuSelected();
                } else {
                    JMenu.this.fireMenuDeselected();
                }
                this.isSelected = zIsSelected;
            }
        }
    }

    private ChangeListener createMenuChangeListener() {
        return new MenuChangeListener();
    }

    protected WinListener createWinListener(JPopupMenu jPopupMenu) {
        return new WinListener(jPopupMenu);
    }

    /* loaded from: rt.jar:javax/swing/JMenu$WinListener.class */
    protected class WinListener extends WindowAdapter implements Serializable {
        JPopupMenu popupMenu;

        public WinListener(JPopupMenu jPopupMenu) {
            this.popupMenu = jPopupMenu;
        }

        @Override // java.awt.event.WindowAdapter, java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
            JMenu.this.setSelected(false);
        }
    }

    @Override // javax.swing.JMenuItem, javax.swing.MenuElement
    public void menuSelectionChanged(boolean z2) {
        setSelected(z2);
    }

    @Override // javax.swing.JMenuItem, javax.swing.MenuElement
    public MenuElement[] getSubElements() {
        if (this.popupMenu == null) {
            return new MenuElement[0];
        }
        return new MenuElement[]{this.popupMenu};
    }

    @Override // javax.swing.JMenuItem, javax.swing.MenuElement
    public Component getComponent() {
        return this;
    }

    @Override // java.awt.Container, java.awt.Component
    public void applyComponentOrientation(ComponentOrientation componentOrientation) {
        super.applyComponentOrientation(componentOrientation);
        if (this.popupMenu != null) {
            int menuComponentCount = getMenuComponentCount();
            for (int i2 = 0; i2 < menuComponentCount; i2++) {
                getMenuComponent(i2).applyComponentOrientation(componentOrientation);
            }
            this.popupMenu.setComponentOrientation(componentOrientation);
        }
    }

    @Override // java.awt.Component
    public void setComponentOrientation(ComponentOrientation componentOrientation) {
        super.setComponentOrientation(componentOrientation);
        if (this.popupMenu != null) {
            this.popupMenu.setComponentOrientation(componentOrientation);
        }
    }

    @Override // javax.swing.JMenuItem
    public void setAccelerator(KeyStroke keyStroke) {
        throw new Error("setAccelerator() is not defined for JMenu.  Use setMnemonic() instead.");
    }

    @Override // javax.swing.JComponent, java.awt.Component
    protected void processKeyEvent(KeyEvent keyEvent) {
        MenuSelectionManager.defaultManager().processKeyEvent(keyEvent);
        if (keyEvent.isConsumed()) {
            return;
        }
        super.processKeyEvent(keyEvent);
    }

    @Override // javax.swing.AbstractButton
    public void doClick(int i2) {
        MenuSelectionManager.defaultManager().setSelectedPath(buildMenuElementArray(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public MenuElement[] buildMenuElementArray(JMenu jMenu) {
        Vector vector = new Vector();
        MenuContainer popupMenu = jMenu.getPopupMenu();
        while (true) {
            if (popupMenu instanceof JPopupMenu) {
                JPopupMenu jPopupMenu = (JPopupMenu) popupMenu;
                vector.insertElementAt(jPopupMenu, 0);
                popupMenu = jPopupMenu.getInvoker();
            } else if (popupMenu instanceof JMenu) {
                JMenu jMenu2 = (JMenu) popupMenu;
                vector.insertElementAt(jMenu2, 0);
                popupMenu = jMenu2.getParent();
            } else if (popupMenu instanceof JMenuBar) {
                vector.insertElementAt((JMenuBar) popupMenu, 0);
                MenuElement[] menuElementArr = new MenuElement[vector.size()];
                vector.copyInto(menuElementArr);
                return menuElementArr;
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JMenuItem, javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // javax.swing.JMenuItem, java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJMenu();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JMenu$AccessibleJMenu.class */
    protected class AccessibleJMenu extends JMenuItem.AccessibleJMenuItem implements AccessibleSelection {
        protected AccessibleJMenu() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            int i2 = 0;
            for (Component component : JMenu.this.getMenuComponents()) {
                if (component instanceof Accessible) {
                    i2++;
                }
            }
            return i2;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            int i3 = 0;
            for (a aVar : JMenu.this.getMenuComponents()) {
                if (aVar instanceof Accessible) {
                    if (i3 == i2) {
                        if (aVar instanceof JComponent) {
                            aVar.getAccessibleContext().setAccessibleParent(JMenu.this);
                        }
                        return aVar;
                    }
                    i3++;
                }
            }
            return null;
        }

        @Override // javax.swing.JMenuItem.AccessibleJMenuItem, javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath != null) {
                for (int i2 = 0; i2 < selectedPath.length; i2++) {
                    if (selectedPath[i2] == JMenu.this && i2 + 1 < selectedPath.length) {
                        return 1;
                    }
                }
                return 0;
            }
            return 0;
        }

        /* JADX WARN: Code restructure failed: missing block: B:27:0x0047, code lost:
        
            continue;
         */
        @Override // javax.accessibility.AccessibleSelection
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public javax.accessibility.Accessible getAccessibleSelection(int r4) {
            /*
                r3 = this;
                r0 = r4
                if (r0 < 0) goto Lf
                r0 = r4
                r1 = r3
                javax.swing.JMenu r1 = javax.swing.JMenu.this
                int r1 = r1.getItemCount()
                if (r0 < r1) goto L11
            Lf:
                r0 = 0
                return r0
            L11:
                javax.swing.MenuSelectionManager r0 = javax.swing.MenuSelectionManager.defaultManager()
                javax.swing.MenuElement[] r0 = r0.getSelectedPath()
                r5 = r0
                r0 = r5
                if (r0 == 0) goto L4d
                r0 = 0
                r6 = r0
            L1e:
                r0 = r6
                r1 = r5
                int r1 = r1.length
                if (r0 >= r1) goto L4d
                r0 = r5
                r1 = r6
                r0 = r0[r1]
                r1 = r3
                javax.swing.JMenu r1 = javax.swing.JMenu.this
                if (r0 != r1) goto L47
            L2e:
                int r6 = r6 + 1
                r0 = r6
                r1 = r5
                int r1 = r1.length
                if (r0 >= r1) goto L47
                r0 = r5
                r1 = r6
                r0 = r0[r1]
                boolean r0 = r0 instanceof javax.swing.JMenuItem
                if (r0 == 0) goto L2e
                r0 = r5
                r1 = r6
                r0 = r0[r1]
                javax.accessibility.Accessible r0 = (javax.accessibility.Accessible) r0
                return r0
            L47:
                int r6 = r6 + 1
                goto L1e
            L4d:
                r0 = 0
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.JMenu.AccessibleJMenu.getAccessibleSelection(int):javax.accessibility.Accessible");
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath != null) {
                JMenuItem item = JMenu.this.getItem(i2);
                for (MenuElement menuElement : selectedPath) {
                    if (menuElement == item) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            JMenuItem item;
            if (i2 >= 0 && i2 < JMenu.this.getItemCount() && (item = JMenu.this.getItem(i2)) != null) {
                if (item instanceof JMenu) {
                    MenuSelectionManager.defaultManager().setSelectedPath(JMenu.this.buildMenuElementArray((JMenu) item));
                } else {
                    MenuSelectionManager.defaultManager().setSelectedPath(null);
                }
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            JMenuItem item;
            if (i2 >= 0 && i2 < JMenu.this.getItemCount() && (item = JMenu.this.getItem(i2)) != null && (item instanceof JMenu) && item.isSelected()) {
                MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
                MenuElement[] menuElementArr = new MenuElement[selectedPath.length - 2];
                for (int i3 = 0; i3 < selectedPath.length - 2; i3++) {
                    menuElementArr[i3] = selectedPath[i3];
                }
                MenuSelectionManager.defaultManager().setSelectedPath(menuElementArr);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath != null) {
                for (int i2 = 0; i2 < selectedPath.length; i2++) {
                    if (selectedPath[i2] == JMenu.this) {
                        MenuElement[] menuElementArr = new MenuElement[i2 + 1];
                        System.arraycopy(selectedPath, 0, menuElementArr, 0, i2);
                        menuElementArr[i2] = JMenu.this.getPopupMenu();
                        MenuSelectionManager.defaultManager().setSelectedPath(menuElementArr);
                    }
                }
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
        }
    }
}
