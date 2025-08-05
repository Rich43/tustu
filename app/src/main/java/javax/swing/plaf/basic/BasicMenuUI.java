package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.AbstractButton;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicMenuItemUI;
import sun.swing.DefaultLookup;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuUI.class */
public class BasicMenuUI extends BasicMenuItemUI {
    protected ChangeListener changeListener;
    protected MenuListener menuListener;
    private int lastMnemonic = 0;
    private InputMap selectedWindowInputMap;
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    private static boolean crossMenuMnemonic = true;

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicMenuUI();
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        BasicMenuItemUI.loadActionMap(lazyActionMap);
        lazyActionMap.put(new Actions("selectMenu", null, true));
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void installDefaults() {
        super.installDefaults();
        updateDefaultBackgroundColor();
        ((JMenu) this.menuItem).setDelay(200);
        crossMenuMnemonic = UIManager.getBoolean("Menu.crossMenuMnemonic");
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected String getPropertyPrefix() {
        return "Menu";
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void installListeners() {
        super.installListeners();
        if (this.changeListener == null) {
            this.changeListener = createChangeListener(this.menuItem);
        }
        if (this.changeListener != null) {
            this.menuItem.addChangeListener(this.changeListener);
        }
        if (this.menuListener == null) {
            this.menuListener = createMenuListener(this.menuItem);
        }
        if (this.menuListener != null) {
            ((JMenu) this.menuItem).addMenuListener(this.menuListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        updateMnemonicBinding();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    void installLazyActionMap() {
        LazyActionMap.installLazyActionMap(this.menuItem, BasicMenuUI.class, getPropertyPrefix() + ".actionMap");
    }

    void updateMnemonicBinding() {
        int mnemonic = this.menuItem.getModel().getMnemonic();
        int[] iArr = (int[]) DefaultLookup.get(this.menuItem, this, "Menu.shortcutKeys");
        if (iArr == null) {
            iArr = new int[]{8, 40};
        }
        if (mnemonic == this.lastMnemonic) {
            return;
        }
        InputMap uIInputMap = SwingUtilities.getUIInputMap(this.menuItem, 2);
        if (this.lastMnemonic != 0 && uIInputMap != null) {
            for (int i2 : iArr) {
                uIInputMap.remove(KeyStroke.getKeyStroke(this.lastMnemonic, i2, false));
            }
        }
        if (mnemonic != 0) {
            if (uIInputMap == null) {
                uIInputMap = createInputMap(2);
                SwingUtilities.replaceUIInputMap(this.menuItem, 2, uIInputMap);
            }
            for (int i3 : iArr) {
                uIInputMap.put(KeyStroke.getKeyStroke(mnemonic, i3, false), "selectMenu");
            }
        }
        this.lastMnemonic = mnemonic;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void uninstallKeyboardActions() {
        super.uninstallKeyboardActions();
        this.lastMnemonic = 0;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return getHandler();
    }

    protected MenuListener createMenuListener(JComponent jComponent) {
        return null;
    }

    protected ChangeListener createChangeListener(JComponent jComponent) {
        return null;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected PropertyChangeListener createPropertyChangeListener(JComponent jComponent) {
        return getHandler();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    BasicMenuItemUI.Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void uninstallDefaults() {
        this.menuItem.setArmed(false);
        this.menuItem.setSelected(false);
        this.menuItem.resetKeyboardActions();
        super.uninstallDefaults();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        if (this.changeListener != null) {
            this.menuItem.removeChangeListener(this.changeListener);
        }
        if (this.menuListener != null) {
            ((JMenu) this.menuItem).removeMenuListener(this.menuListener);
        }
        this.changeListener = null;
        this.menuListener = null;
        this.handler = null;
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected MenuDragMouseListener createMenuDragMouseListener(JComponent jComponent) {
        return getHandler();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected MenuKeyListener createMenuKeyListener(JComponent jComponent) {
        return (MenuKeyListener) getHandler();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        if (((JMenu) this.menuItem).isTopLevelMenu()) {
            return new Dimension(jComponent.getPreferredSize().width, Short.MAX_VALUE);
        }
        return null;
    }

    protected void setupPostTimer(JMenu jMenu) {
        Timer timer = new Timer(jMenu.getDelay(), new Actions("selectMenu", jMenu, false));
        timer.setRepeats(false);
        timer.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void appendPath(MenuElement[] menuElementArr, MenuElement menuElement) {
        MenuElement[] menuElementArr2 = new MenuElement[menuElementArr.length + 1];
        System.arraycopy(menuElementArr, 0, menuElementArr2, 0, menuElementArr.length);
        menuElementArr2[menuElementArr.length] = menuElement;
        MenuSelectionManager.defaultManager().setSelectedPath(menuElementArr2);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String SELECT = "selectMenu";
        private JMenu menu;
        private boolean force;

        Actions(String str, JMenu jMenu, boolean z2) {
            super(str);
            this.force = false;
            this.menu = jMenu;
            this.force = z2;
        }

        private JMenu getMenu(ActionEvent actionEvent) {
            if (actionEvent.getSource() instanceof JMenu) {
                return (JMenu) actionEvent.getSource();
            }
            return this.menu;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            MenuElement[] menuElementArr;
            JPopupMenu lastPopup;
            JMenu menu = getMenu(actionEvent);
            if (!BasicMenuUI.crossMenuMnemonic && (lastPopup = BasicPopupMenuUI.getLastPopup()) != null && lastPopup != menu.getParent()) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if (this.force) {
                MenuContainer parent = menu.getParent();
                if (parent != null && (parent instanceof JMenuBar)) {
                    MenuElement[] subElements = menu.getPopupMenu().getSubElements();
                    if (subElements.length > 0) {
                        menuElementArr = new MenuElement[]{(MenuElement) parent, menu, menu.getPopupMenu(), subElements[0]};
                    } else {
                        menuElementArr = new MenuElement[]{(MenuElement) parent, menu, menu.getPopupMenu()};
                    }
                    menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
                    return;
                }
                return;
            }
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length > 0 && selectedPath[selectedPath.length - 1] == menu) {
                BasicMenuUI.appendPath(selectedPath, menu.getPopupMenu());
            }
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if (obj instanceof JMenu) {
                return ((JMenu) obj).isEnabled();
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDefaultBackgroundColor() {
        if (!UIManager.getBoolean("Menu.useMenuBarBackgroundForTopLevel")) {
            return;
        }
        JMenu jMenu = (JMenu) this.menuItem;
        if (jMenu.getBackground() instanceof UIResource) {
            if (jMenu.isTopLevelMenu()) {
                jMenu.setBackground(UIManager.getColor("MenuBar.background"));
            } else {
                jMenu.setBackground(UIManager.getColor(getPropertyPrefix() + ".background"));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuUI$MouseInputHandler.class */
    public class MouseInputHandler implements MouseInputListener {
        protected MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mouseClicked(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mouseReleased(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mouseExited(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mouseDragged(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicMenuUI.this.getHandler().mouseMoved(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuUI$ChangeHandler.class */
    public class ChangeHandler implements ChangeListener {
        public JMenu menu;
        public BasicMenuUI ui;
        public boolean isSelected = false;
        public Component wasFocused;

        public ChangeHandler(JMenu jMenu, BasicMenuUI basicMenuUI) {
            this.menu = jMenu;
            this.ui = basicMenuUI;
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicMenuUI$Handler.class */
    private class Handler extends BasicMenuItemUI.Handler implements MenuKeyListener {
        private Handler() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName() == AbstractButton.MNEMONIC_CHANGED_PROPERTY) {
                BasicMenuUI.this.updateMnemonicBinding();
                return;
            }
            if (propertyChangeEvent.getPropertyName().equals("ancestor")) {
                BasicMenuUI.this.updateDefaultBackgroundColor();
            }
            super.propertyChange(propertyChangeEvent);
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            JMenu jMenu = (JMenu) BasicMenuUI.this.menuItem;
            if (!jMenu.isEnabled()) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if (jMenu.isTopLevelMenu()) {
                if (jMenu.isSelected() && jMenu.getPopupMenu().isShowing()) {
                    menuSelectionManagerDefaultManager.clearSelectedPath();
                } else {
                    MenuContainer parent = jMenu.getParent();
                    if (parent != null && (parent instanceof JMenuBar)) {
                        menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{(MenuElement) parent, jMenu});
                    }
                }
            }
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length > 0 && selectedPath[selectedPath.length - 1] != jMenu.getPopupMenu()) {
                if (jMenu.isTopLevelMenu() || jMenu.getDelay() == 0) {
                    BasicMenuUI.appendPath(selectedPath, jMenu.getPopupMenu());
                } else {
                    BasicMenuUI.this.setupPostTimer(jMenu);
                }
            }
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (!((JMenu) BasicMenuUI.this.menuItem).isEnabled()) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            menuSelectionManagerDefaultManager.processMouseEvent(mouseEvent);
            if (!mouseEvent.isConsumed()) {
                menuSelectionManagerDefaultManager.clearSelectedPath();
            }
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            JMenu jMenu = (JMenu) BasicMenuUI.this.menuItem;
            if (!jMenu.isEnabled() && !UIManager.getBoolean("MenuItem.disabledAreNavigable")) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (!jMenu.isTopLevelMenu()) {
                if (selectedPath.length <= 0 || selectedPath[selectedPath.length - 1] != jMenu.getPopupMenu()) {
                    if (jMenu.getDelay() == 0) {
                        BasicMenuUI.appendPath(BasicMenuUI.this.getPath(), jMenu.getPopupMenu());
                        return;
                    } else {
                        menuSelectionManagerDefaultManager.setSelectedPath(BasicMenuUI.this.getPath());
                        BasicMenuUI.this.setupPostTimer(jMenu);
                        return;
                    }
                }
                return;
            }
            if (selectedPath.length > 0 && selectedPath[0] == jMenu.getParent()) {
                MenuElement[] menuElementArr = new MenuElement[3];
                menuElementArr[0] = (MenuElement) jMenu.getParent();
                menuElementArr[1] = jMenu;
                if (BasicPopupMenuUI.getLastPopup() != null) {
                    menuElementArr[2] = jMenu.getPopupMenu();
                }
                menuSelectionManagerDefaultManager.setSelectedPath(menuElementArr);
            }
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (!((JMenu) BasicMenuUI.this.menuItem).isEnabled()) {
                return;
            }
            MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, javax.swing.event.MenuDragMouseListener
        public void menuDragMouseEntered(MenuDragMouseEvent menuDragMouseEvent) {
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, javax.swing.event.MenuDragMouseListener
        public void menuDragMouseDragged(MenuDragMouseEvent menuDragMouseEvent) {
            if (!BasicMenuUI.this.menuItem.isEnabled()) {
                return;
            }
            MenuSelectionManager menuSelectionManager = menuDragMouseEvent.getMenuSelectionManager();
            MenuElement[] path = menuDragMouseEvent.getPath();
            Point point = menuDragMouseEvent.getPoint();
            if (point.f12370x >= 0 && point.f12370x < BasicMenuUI.this.menuItem.getWidth() && point.f12371y >= 0 && point.f12371y < BasicMenuUI.this.menuItem.getHeight()) {
                JMenu jMenu = (JMenu) BasicMenuUI.this.menuItem;
                MenuElement[] selectedPath = menuSelectionManager.getSelectedPath();
                if (selectedPath.length <= 0 || selectedPath[selectedPath.length - 1] != jMenu.getPopupMenu()) {
                    if (jMenu.isTopLevelMenu() || jMenu.getDelay() == 0 || menuDragMouseEvent.getID() == 506) {
                        BasicMenuUI.appendPath(path, jMenu.getPopupMenu());
                        return;
                    } else {
                        menuSelectionManager.setSelectedPath(path);
                        BasicMenuUI.this.setupPostTimer(jMenu);
                        return;
                    }
                }
                return;
            }
            if (menuDragMouseEvent.getID() == 502 && menuSelectionManager.componentForPoint(menuDragMouseEvent.getComponent(), menuDragMouseEvent.getPoint()) == null) {
                menuSelectionManager.clearSelectedPath();
            }
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, javax.swing.event.MenuDragMouseListener
        public void menuDragMouseExited(MenuDragMouseEvent menuDragMouseEvent) {
        }

        @Override // javax.swing.plaf.basic.BasicMenuItemUI.Handler, javax.swing.event.MenuDragMouseListener
        public void menuDragMouseReleased(MenuDragMouseEvent menuDragMouseEvent) {
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
            if ((!BasicMenuUI.crossMenuMnemonic && BasicPopupMenuUI.getLastPopup() != null) || BasicPopupMenuUI.getPopups().size() != 0) {
                return;
            }
            char lowerCase = Character.toLowerCase((char) BasicMenuUI.this.menuItem.getMnemonic());
            MenuElement[] path = menuKeyEvent.getPath();
            if (lowerCase == Character.toLowerCase(menuKeyEvent.getKeyChar())) {
                JPopupMenu popupMenu = ((JMenu) BasicMenuUI.this.menuItem).getPopupMenu();
                ArrayList arrayList = new ArrayList(Arrays.asList(path));
                arrayList.add(popupMenu);
                MenuElement menuElementFindEnabledChild = BasicPopupMenuUI.findEnabledChild(popupMenu.getSubElements(), -1, true);
                if (menuElementFindEnabledChild != null) {
                    arrayList.add(menuElementFindEnabledChild);
                }
                menuKeyEvent.getMenuSelectionManager().setSelectedPath((MenuElement[]) arrayList.toArray(new MenuElement[0]));
                menuKeyEvent.consume();
            }
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyPressed(MenuKeyEvent menuKeyEvent) {
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
        }
    }
}
