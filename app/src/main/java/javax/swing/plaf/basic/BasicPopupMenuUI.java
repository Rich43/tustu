package javax.swing.plaf.basic;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.UIResource;
import sun.awt.AppContext;
import sun.awt.SunToolkit;
import sun.awt.UngrabEvent;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicPopupMenuUI.class */
public class BasicPopupMenuUI extends PopupMenuUI {
    static final StringBuilder MOUSE_GRABBER_KEY = new StringBuilder("javax.swing.plaf.basic.BasicPopupMenuUI.MouseGrabber");
    static final StringBuilder MENU_KEYBOARD_HELPER_KEY = new StringBuilder("javax.swing.plaf.basic.BasicPopupMenuUI.MenuKeyboardHelper");
    protected JPopupMenu popupMenu = null;
    private transient PopupMenuListener popupMenuListener = null;
    private MenuKeyListener menuKeyListener = null;
    private static boolean checkedUnpostPopup;
    private static boolean unpostPopup;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicPopupMenuUI();
    }

    public BasicPopupMenuUI() {
        BasicLookAndFeel.needsEventHelper = true;
        LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
        if (lookAndFeel instanceof BasicLookAndFeel) {
            ((BasicLookAndFeel) lookAndFeel).installAWTEventListener();
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.popupMenu = (JPopupMenu) jComponent;
        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    public void installDefaults() {
        if (this.popupMenu.getLayout() == null || (this.popupMenu.getLayout() instanceof UIResource)) {
            this.popupMenu.setLayout(new DefaultMenuLayout(this.popupMenu, 1));
        }
        LookAndFeel.installProperty(this.popupMenu, "opaque", Boolean.TRUE);
        LookAndFeel.installBorder(this.popupMenu, "PopupMenu.border");
        LookAndFeel.installColorsAndFont(this.popupMenu, "PopupMenu.background", "PopupMenu.foreground", "PopupMenu.font");
    }

    protected void installListeners() {
        if (this.popupMenuListener == null) {
            this.popupMenuListener = new BasicPopupMenuListener();
        }
        this.popupMenu.addPopupMenuListener(this.popupMenuListener);
        if (this.menuKeyListener == null) {
            this.menuKeyListener = new BasicMenuKeyListener();
        }
        this.popupMenu.addMenuKeyListener(this.menuKeyListener);
        AppContext appContext = AppContext.getAppContext();
        synchronized (MOUSE_GRABBER_KEY) {
            if (((MouseGrabber) appContext.get(MOUSE_GRABBER_KEY)) == null) {
                appContext.put(MOUSE_GRABBER_KEY, new MouseGrabber());
            }
        }
        synchronized (MENU_KEYBOARD_HELPER_KEY) {
            if (((MenuKeyboardHelper) appContext.get(MENU_KEYBOARD_HELPER_KEY)) == null) {
                MenuKeyboardHelper menuKeyboardHelper = new MenuKeyboardHelper();
                appContext.put(MENU_KEYBOARD_HELPER_KEY, menuKeyboardHelper);
                MenuSelectionManager.defaultManager().addChangeListener(menuKeyboardHelper);
            }
        }
    }

    protected void installKeyboardActions() {
    }

    static InputMap getInputMap(JPopupMenu jPopupMenu, JComponent jComponent) {
        Object[] objArr;
        ComponentInputMap componentInputMapMakeComponentInputMap = null;
        Object[] objArr2 = (Object[]) UIManager.get("PopupMenu.selectedWindowInputMapBindings");
        if (objArr2 != null) {
            componentInputMapMakeComponentInputMap = LookAndFeel.makeComponentInputMap(jComponent, objArr2);
            if (!jPopupMenu.getComponentOrientation().isLeftToRight() && (objArr = (Object[]) UIManager.get("PopupMenu.selectedWindowInputMapBindings.RightToLeft")) != null) {
                ComponentInputMap componentInputMapMakeComponentInputMap2 = LookAndFeel.makeComponentInputMap(jComponent, objArr);
                componentInputMapMakeComponentInputMap2.setParent(componentInputMapMakeComponentInputMap);
                componentInputMapMakeComponentInputMap = componentInputMapMakeComponentInputMap2;
            }
        }
        return componentInputMapMakeComponentInputMap;
    }

    static ActionMap getActionMap() {
        return LazyActionMap.getActionMap(BasicPopupMenuUI.class, "PopupMenu.actionMap");
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("cancel"));
        lazyActionMap.put(new Actions("selectNext"));
        lazyActionMap.put(new Actions("selectPrevious"));
        lazyActionMap.put(new Actions("selectParent"));
        lazyActionMap.put(new Actions("selectChild"));
        lazyActionMap.put(new Actions(RuntimeModeler.RETURN));
        BasicLookAndFeel.installAudioActionMap(lazyActionMap);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults();
        uninstallListeners();
        uninstallKeyboardActions();
        this.popupMenu = null;
    }

    protected void uninstallDefaults() {
        LookAndFeel.uninstallBorder(this.popupMenu);
    }

    protected void uninstallListeners() {
        if (this.popupMenuListener != null) {
            this.popupMenu.removePopupMenuListener(this.popupMenuListener);
        }
        if (this.menuKeyListener != null) {
            this.popupMenu.removeMenuKeyListener(this.menuKeyListener);
        }
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIActionMap(this.popupMenu, null);
        SwingUtilities.replaceUIInputMap(this.popupMenu, 2, null);
    }

    static MenuElement getFirstPopup() {
        MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        MenuElement menuElement = null;
        for (int i2 = 0; menuElement == null && i2 < selectedPath.length; i2++) {
            if (selectedPath[i2] instanceof JPopupMenu) {
                menuElement = selectedPath[i2];
            }
        }
        return menuElement;
    }

    static JPopupMenu getLastPopup() {
        MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        JPopupMenu jPopupMenu = null;
        for (int length = selectedPath.length - 1; jPopupMenu == null && length >= 0; length--) {
            if (selectedPath[length] instanceof JPopupMenu) {
                jPopupMenu = (JPopupMenu) selectedPath[length];
            }
        }
        return jPopupMenu;
    }

    static List<JPopupMenu> getPopups() {
        MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        ArrayList arrayList = new ArrayList(selectedPath.length);
        for (MenuElement menuElement : selectedPath) {
            if (menuElement instanceof JPopupMenu) {
                arrayList.add((JPopupMenu) menuElement);
            }
        }
        return arrayList;
    }

    @Override // javax.swing.plaf.PopupMenuUI
    public boolean isPopupTrigger(MouseEvent mouseEvent) {
        return mouseEvent.getID() == 502 && (mouseEvent.getModifiers() & 4) != 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean checkInvokerEqual(MenuElement menuElement, MenuElement menuElement2) {
        Component component = menuElement.getComponent();
        Component component2 = menuElement2.getComponent();
        if (component instanceof JPopupMenu) {
            component = ((JPopupMenu) component).getInvoker();
        }
        if (component2 instanceof JPopupMenu) {
            component2 = ((JPopupMenu) component2).getInvoker();
        }
        return component == component2;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicPopupMenuUI$BasicPopupMenuListener.class */
    private class BasicPopupMenuListener implements PopupMenuListener {
        private BasicPopupMenuListener() {
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
            BasicLookAndFeel.playSound((JPopupMenu) popupMenuEvent.getSource(), "PopupMenu.popupSound");
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicPopupMenuUI$BasicMenuKeyListener.class */
    private class BasicMenuKeyListener implements MenuKeyListener {
        MenuElement menuToOpen;

        private BasicMenuKeyListener() {
            this.menuToOpen = null;
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
            if (this.menuToOpen != null) {
                JPopupMenu popupMenu = ((JMenu) this.menuToOpen).getPopupMenu();
                MenuElement menuElementFindEnabledChild = BasicPopupMenuUI.findEnabledChild(popupMenu.getSubElements(), -1, true);
                ArrayList arrayList = new ArrayList(Arrays.asList(menuKeyEvent.getPath()));
                arrayList.add(this.menuToOpen);
                arrayList.add(popupMenu);
                if (menuElementFindEnabledChild != null) {
                    arrayList.add(menuElementFindEnabledChild);
                }
                MenuSelectionManager.defaultManager().setSelectedPath((MenuElement[]) arrayList.toArray(new MenuElement[0]));
                menuKeyEvent.consume();
            }
            this.menuToOpen = null;
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyPressed(MenuKeyEvent menuKeyEvent) {
            char keyChar = menuKeyEvent.getKeyChar();
            if (!Character.isLetterOrDigit(keyChar)) {
                return;
            }
            MenuSelectionManager menuSelectionManager = menuKeyEvent.getMenuSelectionManager();
            MenuElement[] path = menuKeyEvent.getPath();
            MenuElement[] subElements = BasicPopupMenuUI.this.popupMenu.getSubElements();
            int i2 = -1;
            int i3 = 0;
            int i4 = -1;
            int[] iArr = null;
            for (int i5 = 0; i5 < subElements.length; i5++) {
                if (subElements[i5] instanceof JMenuItem) {
                    JMenuItem jMenuItem = (JMenuItem) subElements[i5];
                    int mnemonic = jMenuItem.getMnemonic();
                    if (jMenuItem.isEnabled() && jMenuItem.isVisible() && lower(keyChar) == lower(mnemonic)) {
                        if (i3 == 0) {
                            i4 = i5;
                            i3++;
                        } else {
                            if (iArr == null) {
                                iArr = new int[subElements.length];
                                iArr[0] = i4;
                            }
                            int i6 = i3;
                            i3++;
                            iArr[i6] = i5;
                        }
                    }
                    if (jMenuItem.isArmed() || jMenuItem.isSelected()) {
                        i2 = i3 - 1;
                    }
                }
            }
            if (i3 != 0) {
                if (i3 == 1) {
                    JMenuItem jMenuItem2 = (JMenuItem) subElements[i4];
                    if (jMenuItem2 instanceof JMenu) {
                        this.menuToOpen = jMenuItem2;
                    } else if (jMenuItem2.isEnabled()) {
                        menuSelectionManager.clearSelectedPath();
                        jMenuItem2.doClick();
                    }
                    menuKeyEvent.consume();
                    return;
                }
                MenuElement menuElement = subElements[iArr[(i2 + 1) % i3]];
                MenuElement[] menuElementArr = new MenuElement[path.length + 1];
                System.arraycopy(path, 0, menuElementArr, 0, path.length);
                menuElementArr[path.length] = menuElement;
                menuSelectionManager.setSelectedPath(menuElementArr);
                menuKeyEvent.consume();
            }
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
        }

        private char lower(char c2) {
            return Character.toLowerCase(c2);
        }

        private char lower(int i2) {
            return Character.toLowerCase((char) i2);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicPopupMenuUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String CANCEL = "cancel";
        private static final String SELECT_NEXT = "selectNext";
        private static final String SELECT_PREVIOUS = "selectPrevious";
        private static final String SELECT_PARENT = "selectParent";
        private static final String SELECT_CHILD = "selectChild";
        private static final String RETURN = "return";
        private static final boolean FORWARD = true;
        private static final boolean BACKWARD = false;
        private static final boolean PARENT = false;
        private static final boolean CHILD = true;

        Actions(String str) {
            super(str);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            String name = getName();
            if (name == CANCEL) {
                cancel();
                return;
            }
            if (name == SELECT_NEXT) {
                selectItem(true);
                return;
            }
            if (name == SELECT_PREVIOUS) {
                selectItem(false);
                return;
            }
            if (name == SELECT_PARENT) {
                selectParentChild(false);
            } else if (name == SELECT_CHILD) {
                selectParentChild(true);
            } else if (name == "return") {
                doReturn();
            }
        }

        private void doReturn() {
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner != null && !(focusOwner instanceof JRootPane)) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length > 0) {
                MenuElement menuElement = selectedPath[selectedPath.length - 1];
                if (menuElement instanceof JMenu) {
                    MenuElement[] menuElementArr = new MenuElement[selectedPath.length + 1];
                    System.arraycopy(selectedPath, 0, menuElementArr, 0, selectedPath.length);
                    menuElementArr[selectedPath.length] = ((JMenu) menuElement).getPopupMenu();
                    menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
                    return;
                }
                if (menuElement instanceof JMenuItem) {
                    JMenuItem jMenuItem = (JMenuItem) menuElement;
                    if (jMenuItem.getUI() instanceof BasicMenuItemUI) {
                        ((BasicMenuItemUI) jMenuItem.getUI()).doClick(menuSelectionManagerDefaultManager);
                    } else {
                        menuSelectionManagerDefaultManager.clearSelectedPath();
                        jMenuItem.doClick(0);
                    }
                }
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0034 A[PHI: r11
  0x0034: PHI (r11v3 int) = (r11v2 int), (r11v4 int) binds: [B:7:0x0024, B:9:0x0031] A[DONT_GENERATE, DONT_INLINE]] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private void selectParentChild(boolean r7) {
            /*
                Method dump skipped, instructions count: 333
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: javax.swing.plaf.basic.BasicPopupMenuUI.Actions.selectParentChild(boolean):void");
        }

        private void selectItem(boolean z2) {
            MenuElement menuElementFindEnabledChild;
            MenuElement[] menuElementArr;
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length == 0) {
                return;
            }
            int length = selectedPath.length;
            if (length == 1 && (selectedPath[0] instanceof JPopupMenu)) {
                JPopupMenu jPopupMenu = (JPopupMenu) selectedPath[0];
                menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{jPopupMenu, BasicPopupMenuUI.findEnabledChild(jPopupMenu.getSubElements(), -1, z2)});
                return;
            }
            if (length == 2 && (selectedPath[0] instanceof JMenuBar) && (selectedPath[1] instanceof JMenu)) {
                JPopupMenu popupMenu = ((JMenu) selectedPath[1]).getPopupMenu();
                MenuElement menuElementFindEnabledChild2 = BasicPopupMenuUI.findEnabledChild(popupMenu.getSubElements(), -1, true);
                if (menuElementFindEnabledChild2 != null) {
                    menuElementArr = new MenuElement[4];
                    menuElementArr[3] = menuElementFindEnabledChild2;
                } else {
                    menuElementArr = new MenuElement[3];
                }
                System.arraycopy(selectedPath, 0, menuElementArr, 0, 2);
                menuElementArr[2] = popupMenu;
                menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
                return;
            }
            if ((selectedPath[length - 1] instanceof JPopupMenu) && (selectedPath[length - 2] instanceof JMenu)) {
                JMenu jMenu = (JMenu) selectedPath[length - 2];
                MenuElement menuElementFindEnabledChild3 = BasicPopupMenuUI.findEnabledChild(jMenu.getPopupMenu().getSubElements(), -1, z2);
                if (menuElementFindEnabledChild3 != null) {
                    MenuElement[] menuElementArr2 = new MenuElement[length + 1];
                    System.arraycopy(selectedPath, 0, menuElementArr2, 0, length);
                    menuElementArr2[length] = menuElementFindEnabledChild3;
                    menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr2);
                    return;
                }
                if (length > 2 && (selectedPath[length - 3] instanceof JPopupMenu) && (menuElementFindEnabledChild = BasicPopupMenuUI.findEnabledChild(((JPopupMenu) selectedPath[length - 3]).getSubElements(), jMenu, z2)) != null && menuElementFindEnabledChild != jMenu) {
                    MenuElement[] menuElementArr3 = new MenuElement[length - 1];
                    System.arraycopy(selectedPath, 0, menuElementArr3, 0, length - 2);
                    menuElementArr3[length - 2] = menuElementFindEnabledChild;
                    menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr3);
                    return;
                }
                return;
            }
            MenuElement[] subElements = selectedPath[length - 2].getSubElements();
            MenuElement menuElementFindEnabledChild4 = BasicPopupMenuUI.findEnabledChild(subElements, selectedPath[length - 1], z2);
            if (menuElementFindEnabledChild4 == null) {
                menuElementFindEnabledChild4 = BasicPopupMenuUI.findEnabledChild(subElements, -1, z2);
            }
            if (menuElementFindEnabledChild4 != null) {
                selectedPath[length - 1] = menuElementFindEnabledChild4;
                menuSelectionManagerDefaultManager.setSelectedPath(selectedPath);
            }
        }

        private void cancel() {
            JPopupMenu lastPopup = BasicPopupMenuUI.getLastPopup();
            if (lastPopup != null) {
                lastPopup.putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
            }
            if ("hideMenuTree".equals(UIManager.getString("Menu.cancelMode"))) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
            } else {
                shortenSelectedPath();
            }
        }

        private void shortenSelectedPath() {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath.length <= 2) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
                return;
            }
            int length = 2;
            MenuElement menuElement = selectedPath[selectedPath.length - 1];
            JPopupMenu lastPopup = BasicPopupMenuUI.getLastPopup();
            if (menuElement == lastPopup) {
                MenuElement menuElement2 = selectedPath[selectedPath.length - 2];
                if (menuElement2 instanceof JMenu) {
                    length = (!((JMenu) menuElement2).isEnabled() || lastPopup.getComponentCount() <= 0) ? 3 : 1;
                }
            }
            if (selectedPath.length - length <= 2 && !UIManager.getBoolean("Menu.preserveTopLevelSelection")) {
                length = selectedPath.length;
            }
            MenuElement[] menuElementArr = new MenuElement[selectedPath.length - length];
            System.arraycopy(selectedPath, 0, menuElementArr, 0, selectedPath.length - length);
            MenuSelectionManager.defaultManager().setSelectedPath(menuElementArr);
        }
    }

    private static MenuElement nextEnabledChild(MenuElement[] menuElementArr, int i2, int i3) {
        Component component;
        for (int i4 = i2; i4 <= i3; i4++) {
            if (menuElementArr[i4] != null && (component = menuElementArr[i4].getComponent()) != null && ((component.isEnabled() || UIManager.getBoolean("MenuItem.disabledAreNavigable")) && component.isVisible())) {
                return menuElementArr[i4];
            }
        }
        return null;
    }

    private static MenuElement previousEnabledChild(MenuElement[] menuElementArr, int i2, int i3) {
        Component component;
        for (int i4 = i2; i4 >= i3; i4--) {
            if (menuElementArr[i4] != null && (component = menuElementArr[i4].getComponent()) != null && ((component.isEnabled() || UIManager.getBoolean("MenuItem.disabledAreNavigable")) && component.isVisible())) {
                return menuElementArr[i4];
            }
        }
        return null;
    }

    static MenuElement findEnabledChild(MenuElement[] menuElementArr, int i2, boolean z2) {
        MenuElement menuElementPreviousEnabledChild;
        if (z2) {
            menuElementPreviousEnabledChild = nextEnabledChild(menuElementArr, i2 + 1, menuElementArr.length - 1);
            if (menuElementPreviousEnabledChild == null) {
                menuElementPreviousEnabledChild = nextEnabledChild(menuElementArr, 0, i2 - 1);
            }
        } else {
            menuElementPreviousEnabledChild = previousEnabledChild(menuElementArr, i2 - 1, 0);
            if (menuElementPreviousEnabledChild == null) {
                menuElementPreviousEnabledChild = previousEnabledChild(menuElementArr, menuElementArr.length - 1, i2 + 1);
            }
        }
        return menuElementPreviousEnabledChild;
    }

    static MenuElement findEnabledChild(MenuElement[] menuElementArr, MenuElement menuElement, boolean z2) {
        for (int i2 = 0; i2 < menuElementArr.length; i2++) {
            if (menuElementArr[i2] == menuElement) {
                return findEnabledChild(menuElementArr, i2, z2);
            }
        }
        return null;
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicPopupMenuUI$MouseGrabber.class */
    static class MouseGrabber implements ChangeListener, AWTEventListener, ComponentListener, WindowListener {
        Window grabbedWindow;
        MenuElement[] lastPathSelected;

        public MouseGrabber() {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            menuSelectionManagerDefaultManager.addChangeListener(this);
            this.lastPathSelected = menuSelectionManagerDefaultManager.getSelectedPath();
            if (this.lastPathSelected.length != 0) {
                grabWindow(this.lastPathSelected);
            }
        }

        void uninstall() {
            synchronized (BasicPopupMenuUI.MOUSE_GRABBER_KEY) {
                MenuSelectionManager.defaultManager().removeChangeListener(this);
                ungrabWindow();
                AppContext.getAppContext().remove(BasicPopupMenuUI.MOUSE_GRABBER_KEY);
            }
        }

        void grabWindow(MenuElement[] menuElementArr) {
            final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.swing.plaf.basic.BasicPopupMenuUI.MouseGrabber.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    defaultToolkit.addAWTEventListener(MouseGrabber.this, -2147352464L);
                    return null;
                }
            });
            Component component = menuElementArr[0].getComponent();
            if (component instanceof JPopupMenu) {
                component = ((JPopupMenu) component).getInvoker();
            }
            this.grabbedWindow = component instanceof Window ? (Window) component : SwingUtilities.getWindowAncestor(component);
            if (this.grabbedWindow != null) {
                if (defaultToolkit instanceof SunToolkit) {
                    ((SunToolkit) defaultToolkit).grab(this.grabbedWindow);
                } else {
                    this.grabbedWindow.addComponentListener(this);
                    this.grabbedWindow.addWindowListener(this);
                }
            }
        }

        void ungrabWindow() {
            final Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: javax.swing.plaf.basic.BasicPopupMenuUI.MouseGrabber.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    defaultToolkit.removeAWTEventListener(MouseGrabber.this);
                    return null;
                }
            });
            realUngrabWindow();
        }

        void realUngrabWindow() {
            Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
            if (this.grabbedWindow != null) {
                if (defaultToolkit instanceof SunToolkit) {
                    ((SunToolkit) defaultToolkit).ungrab(this.grabbedWindow);
                } else {
                    this.grabbedWindow.removeComponentListener(this);
                    this.grabbedWindow.removeWindowListener(this);
                }
                this.grabbedWindow = null;
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (this.lastPathSelected.length == 0 && selectedPath.length != 0) {
                grabWindow(selectedPath);
            }
            if (this.lastPathSelected.length != 0 && selectedPath.length == 0) {
                ungrabWindow();
            }
            this.lastPathSelected = selectedPath;
        }

        @Override // java.awt.event.AWTEventListener
        public void eventDispatched(AWTEvent aWTEvent) {
            if (aWTEvent instanceof UngrabEvent) {
                cancelPopupMenu();
            }
            if (!(aWTEvent instanceof MouseEvent)) {
                return;
            }
            MouseEvent mouseEvent = (MouseEvent) aWTEvent;
            Component component = mouseEvent.getComponent();
            switch (mouseEvent.getID()) {
                case 501:
                    if (!isInPopup(component)) {
                        if (!(component instanceof JMenu) || !((JMenu) component).isSelected()) {
                            if (!(component instanceof JComponent) || ((JComponent) component).getClientProperty("doNotCancelPopup") != BasicComboBoxUI.HIDE_POPUP_KEY) {
                                cancelPopupMenu();
                                if (UIManager.getBoolean("PopupMenu.consumeEventOnClose") && !(component instanceof MenuElement)) {
                                    mouseEvent.consume();
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case 502:
                    if ((component instanceof MenuElement) || !isInPopup(component)) {
                        if ((component instanceof JMenu) || !(component instanceof JMenuItem)) {
                            MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
                            break;
                        }
                    }
                    break;
                case 506:
                    if ((component instanceof MenuElement) || !isInPopup(component)) {
                        MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
                        break;
                    }
                    break;
                case 507:
                    if (!isInPopup(component)) {
                        if (!(component instanceof JComboBox) || !((JComboBox) component).isPopupVisible()) {
                            cancelPopupMenu();
                            break;
                        }
                    }
                    break;
            }
        }

        boolean isInPopup(Component component) {
            Component parent = component;
            while (true) {
                Component component2 = parent;
                if (component2 != null && !(component2 instanceof Applet) && !(component2 instanceof Window)) {
                    if (!(component2 instanceof JPopupMenu)) {
                        parent = component2.getParent();
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
        }

        void cancelPopupMenu() {
            try {
                Iterator<JPopupMenu> it = BasicPopupMenuUI.getPopups().iterator();
                while (it.hasNext()) {
                    it.next().putClientProperty("JPopupMenu.firePopupMenuCanceled", Boolean.TRUE);
                }
                MenuSelectionManager.defaultManager().clearSelectedPath();
            } catch (Error e2) {
                realUngrabWindow();
                throw e2;
            } catch (RuntimeException e3) {
                realUngrabWindow();
                throw e3;
            }
        }

        @Override // java.awt.event.ComponentListener
        public void componentResized(ComponentEvent componentEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.ComponentListener
        public void componentMoved(ComponentEvent componentEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.ComponentListener
        public void componentShown(ComponentEvent componentEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.ComponentListener
        public void componentHidden(ComponentEvent componentEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.WindowListener
        public void windowClosing(WindowEvent windowEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.WindowListener
        public void windowClosed(WindowEvent windowEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.WindowListener
        public void windowIconified(WindowEvent windowEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.WindowListener
        public void windowDeactivated(WindowEvent windowEvent) {
            cancelPopupMenu();
        }

        @Override // java.awt.event.WindowListener
        public void windowOpened(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowDeiconified(WindowEvent windowEvent) {
        }

        @Override // java.awt.event.WindowListener
        public void windowActivated(WindowEvent windowEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicPopupMenuUI$MenuKeyboardHelper.class */
    static class MenuKeyboardHelper implements ChangeListener, KeyListener {
        private JPopupMenu lastPopup;
        private JRootPane invokerRootPane;
        private InputMap menuInputMap;
        private boolean focusTraversalKeysEnabled;
        private Component lastFocused = null;
        private MenuElement[] lastPathSelected = new MenuElement[0];
        private ActionMap menuActionMap = BasicPopupMenuUI.getActionMap();
        private boolean receivedKeyPressed = false;
        private FocusListener rootPaneFocusListener = new FocusAdapter() { // from class: javax.swing.plaf.basic.BasicPopupMenuUI.MenuKeyboardHelper.1
            @Override // java.awt.event.FocusAdapter, java.awt.event.FocusListener
            public void focusGained(FocusEvent focusEvent) {
                Component oppositeComponent = focusEvent.getOppositeComponent();
                if (oppositeComponent != null) {
                    MenuKeyboardHelper.this.lastFocused = oppositeComponent;
                }
                focusEvent.getComponent().removeFocusListener(this);
            }
        };

        MenuKeyboardHelper() {
        }

        void removeItems() {
            Window focusedWindow;
            if (this.lastFocused != null) {
                if (!this.lastFocused.requestFocusInWindow() && (focusedWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow()) != null && "###focusableSwingPopup###".equals(focusedWindow.getName())) {
                    this.lastFocused.requestFocus();
                }
                this.lastFocused = null;
            }
            if (this.invokerRootPane != null) {
                this.invokerRootPane.removeKeyListener(this);
                this.invokerRootPane.setFocusTraversalKeysEnabled(this.focusTraversalKeysEnabled);
                removeUIInputMap(this.invokerRootPane, this.menuInputMap);
                removeUIActionMap(this.invokerRootPane, this.menuActionMap);
                this.invokerRootPane = null;
            }
            this.receivedKeyPressed = false;
        }

        JPopupMenu getActivePopup(MenuElement[] menuElementArr) {
            for (int length = menuElementArr.length - 1; length >= 0; length--) {
                MenuElement menuElement = menuElementArr[length];
                if (menuElement instanceof JPopupMenu) {
                    return (JPopupMenu) menuElement;
                }
            }
            return null;
        }

        void addUIInputMap(JComponent jComponent, InputMap inputMap) {
            InputMap inputMap2;
            InputMap inputMap3 = null;
            InputMap inputMap4 = jComponent.getInputMap(2);
            while (true) {
                inputMap2 = inputMap4;
                if (inputMap2 == null || (inputMap2 instanceof UIResource)) {
                    break;
                }
                inputMap3 = inputMap2;
                inputMap4 = inputMap2.getParent();
            }
            if (inputMap3 == null) {
                jComponent.setInputMap(2, inputMap);
            } else {
                inputMap3.setParent(inputMap);
            }
            inputMap.setParent(inputMap2);
        }

        void addUIActionMap(JComponent jComponent, ActionMap actionMap) {
            ActionMap actionMap2;
            ActionMap actionMap3 = null;
            ActionMap actionMap4 = jComponent.getActionMap();
            while (true) {
                actionMap2 = actionMap4;
                if (actionMap2 == null || (actionMap2 instanceof UIResource)) {
                    break;
                }
                actionMap3 = actionMap2;
                actionMap4 = actionMap2.getParent();
            }
            if (actionMap3 == null) {
                jComponent.setActionMap(actionMap);
            } else {
                actionMap3.setParent(actionMap);
            }
            actionMap.setParent(actionMap2);
        }

        void removeUIInputMap(JComponent jComponent, InputMap inputMap) {
            InputMap inputMap2 = null;
            InputMap inputMap3 = jComponent.getInputMap(2);
            while (true) {
                InputMap inputMap4 = inputMap3;
                if (inputMap4 != null) {
                    if (inputMap4 == inputMap) {
                        if (inputMap2 == null) {
                            jComponent.setInputMap(2, inputMap.getParent());
                            return;
                        } else {
                            inputMap2.setParent(inputMap.getParent());
                            return;
                        }
                    }
                    inputMap2 = inputMap4;
                    inputMap3 = inputMap4.getParent();
                } else {
                    return;
                }
            }
        }

        void removeUIActionMap(JComponent jComponent, ActionMap actionMap) {
            ActionMap actionMap2 = null;
            ActionMap actionMap3 = jComponent.getActionMap();
            while (true) {
                ActionMap actionMap4 = actionMap3;
                if (actionMap4 != null) {
                    if (actionMap4 == actionMap) {
                        if (actionMap2 == null) {
                            jComponent.setActionMap(actionMap.getParent());
                            return;
                        } else {
                            actionMap2.setParent(actionMap.getParent());
                            return;
                        }
                    }
                    actionMap2 = actionMap4;
                    actionMap3 = actionMap4.getParent();
                } else {
                    return;
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JComponent rootPane;
            if (!(UIManager.getLookAndFeel() instanceof BasicLookAndFeel)) {
                uninstall();
                return;
            }
            MenuElement[] selectedPath = ((MenuSelectionManager) changeEvent.getSource()).getSelectedPath();
            JPopupMenu activePopup = getActivePopup(selectedPath);
            if (activePopup != null && !activePopup.isFocusable()) {
                return;
            }
            if (this.lastPathSelected.length != 0 && selectedPath.length != 0 && !BasicPopupMenuUI.checkInvokerEqual(selectedPath[0], this.lastPathSelected[0])) {
                removeItems();
                this.lastPathSelected = new MenuElement[0];
            }
            if (this.lastPathSelected.length == 0 && selectedPath.length > 0) {
                if (activePopup == null) {
                    if (selectedPath.length == 2 && (selectedPath[0] instanceof JMenuBar) && (selectedPath[1] instanceof JMenu)) {
                        rootPane = (JComponent) selectedPath[1];
                        activePopup = ((JMenu) rootPane).getPopupMenu();
                    } else {
                        return;
                    }
                } else {
                    Component invoker = activePopup.getInvoker();
                    if (invoker instanceof JFrame) {
                        rootPane = ((JFrame) invoker).getRootPane();
                    } else if (invoker instanceof JDialog) {
                        rootPane = ((JDialog) invoker).getRootPane();
                    } else if (invoker instanceof JApplet) {
                        rootPane = ((JApplet) invoker).getRootPane();
                    } else {
                        while (!(invoker instanceof JComponent)) {
                            if (invoker == null) {
                                return;
                            } else {
                                invoker = invoker.getParent();
                            }
                        }
                        rootPane = (JComponent) invoker;
                    }
                }
                this.lastFocused = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                this.invokerRootPane = SwingUtilities.getRootPane(rootPane);
                if (this.invokerRootPane != null) {
                    this.invokerRootPane.addFocusListener(this.rootPaneFocusListener);
                    this.invokerRootPane.requestFocus(true);
                    this.invokerRootPane.addKeyListener(this);
                    this.focusTraversalKeysEnabled = this.invokerRootPane.getFocusTraversalKeysEnabled();
                    this.invokerRootPane.setFocusTraversalKeysEnabled(false);
                    this.menuInputMap = BasicPopupMenuUI.getInputMap(activePopup, this.invokerRootPane);
                    addUIInputMap(this.invokerRootPane, this.menuInputMap);
                    addUIActionMap(this.invokerRootPane, this.menuActionMap);
                }
            } else if (this.lastPathSelected.length != 0 && selectedPath.length == 0) {
                removeItems();
            } else if (activePopup != this.lastPopup) {
                this.receivedKeyPressed = false;
            }
            this.lastPathSelected = selectedPath;
            this.lastPopup = activePopup;
        }

        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            this.receivedKeyPressed = true;
            MenuSelectionManager.defaultManager().processKeyEvent(keyEvent);
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
            if (this.receivedKeyPressed) {
                this.receivedKeyPressed = false;
                MenuSelectionManager.defaultManager().processKeyEvent(keyEvent);
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
            if (this.receivedKeyPressed) {
                MenuSelectionManager.defaultManager().processKeyEvent(keyEvent);
            }
        }

        void uninstall() {
            synchronized (BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY) {
                MenuSelectionManager.defaultManager().removeChangeListener(this);
                AppContext.getAppContext().remove(BasicPopupMenuUI.MENU_KEYBOARD_HELPER_KEY);
            }
        }
    }
}
