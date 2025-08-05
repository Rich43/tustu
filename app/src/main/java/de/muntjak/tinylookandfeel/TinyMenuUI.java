package de.muntjak.tinylookandfeel;

import java.awt.Dimension;
import java.awt.KeyEventPostProcessor;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ActionMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.ComboPopup;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI.class */
public class TinyMenuUI extends TinyMenuItemUI {
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    static final String IS_SYSTEM_MENU_KEY = "isSystemMenu";
    protected ChangeListener changeListener;
    protected PropertyChangeListener propertyChangeListener;
    protected MenuListener menuListener;
    private int lastMnemonic = 0;
    private static MenuListener sharedMenuListener;
    static final AltProcessor ALT_PROCESSOR = new AltProcessor();
    static boolean systemMenuShowing = false;
    private static boolean crossMenuMnemonic = true;

    /* renamed from: de.muntjak.tinylookandfeel.TinyMenuUI$1, reason: invalid class name */
    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$1.class */
    static class AnonymousClass1 {
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$AltProcessor.class */
    static class AltProcessor implements KeyEventPostProcessor {
        static boolean altKeyPressed = false;
        static boolean menuCanceledOnPress = false;
        static JRootPane root = null;
        static Window winAncestor = null;

        AltProcessor() {
        }

        void altPressed(KeyEvent keyEvent) {
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (selectedPath.length > 0 && !(selectedPath[0] instanceof ComboPopup)) {
                menuSelectionManagerDefaultManager.clearSelectedPath();
                menuCanceledOnPress = true;
                keyEvent.consume();
            } else {
                if (selectedPath.length > 0) {
                    menuCanceledOnPress = false;
                    keyEvent.consume();
                    return;
                }
                menuCanceledOnPress = false;
                JMenuBar jMenuBar = root != null ? root.getJMenuBar() : null;
                if (jMenuBar == null && (winAncestor instanceof JFrame)) {
                    jMenuBar = ((JFrame) winAncestor).getJMenuBar();
                }
                if ((jMenuBar != null ? jMenuBar.getMenu(0) : null) != null) {
                    keyEvent.consume();
                }
            }
        }

        void altReleased(KeyEvent keyEvent) {
            if (menuCanceledOnPress) {
                return;
            }
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            if (menuSelectionManagerDefaultManager.getSelectedPath().length == 0) {
                JMenuBar jMenuBar = root != null ? root.getJMenuBar() : null;
                if (jMenuBar == null && (winAncestor instanceof JFrame)) {
                    jMenuBar = ((JFrame) winAncestor).getJMenuBar();
                }
                JMenu menu = jMenuBar != null ? jMenuBar.getMenu(0) : null;
                if (menu != null) {
                    menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{jMenuBar, menu});
                }
            }
        }

        @Override // java.awt.KeyEventPostProcessor
        public boolean postProcessKeyEvent(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() != 18) {
                altKeyPressed = false;
                return false;
            }
            root = SwingUtilities.getRootPane(keyEvent.getComponent());
            if (root == null) {
                return false;
            }
            winAncestor = SwingUtilities.getWindowAncestor(root);
            if (keyEvent.getID() == 401) {
                if (!altKeyPressed) {
                    altPressed(keyEvent);
                }
                altKeyPressed = true;
                return true;
            }
            if (keyEvent.getID() != 402) {
                return false;
            }
            if (altKeyPressed) {
                altReleased(keyEvent);
            }
            altKeyPressed = false;
            return false;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$MenuDragMouseHandler.class */
    private class MenuDragMouseHandler implements MenuDragMouseListener {
        private final TinyMenuUI this$0;

        private MenuDragMouseHandler(TinyMenuUI tinyMenuUI) {
            this.this$0 = tinyMenuUI;
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseEntered(MenuDragMouseEvent menuDragMouseEvent) {
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseDragged(MenuDragMouseEvent menuDragMouseEvent) {
            if (this.this$0.menuItem.isEnabled()) {
                MenuSelectionManager menuSelectionManager = menuDragMouseEvent.getMenuSelectionManager();
                MenuElement[] path = menuDragMouseEvent.getPath();
                Point point = menuDragMouseEvent.getPoint();
                if (point.f12370x < 0 || point.f12370x >= this.this$0.menuItem.getWidth() || point.f12371y < 0 || point.f12371y >= this.this$0.menuItem.getHeight()) {
                    if (menuDragMouseEvent.getID() == 502 && menuSelectionManager.componentForPoint(menuDragMouseEvent.getComponent(), menuDragMouseEvent.getPoint()) == null) {
                        menuSelectionManager.clearSelectedPath();
                        return;
                    }
                    return;
                }
                JMenu jMenu = (JMenu) this.this$0.menuItem;
                MenuElement[] selectedPath = menuSelectionManager.getSelectedPath();
                if (selectedPath.length <= 0 || selectedPath[selectedPath.length - 1] != jMenu.getPopupMenu()) {
                    if (jMenu.isTopLevelMenu() || jMenu.getDelay() == 0 || menuDragMouseEvent.getID() == 506) {
                        TinyMenuUI.appendPath(path, jMenu.getPopupMenu());
                    } else {
                        menuSelectionManager.setSelectedPath(path);
                        this.this$0.setupPostTimer(jMenu);
                    }
                }
            }
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseExited(MenuDragMouseEvent menuDragMouseEvent) {
        }

        @Override // javax.swing.event.MenuDragMouseListener
        public void menuDragMouseReleased(MenuDragMouseEvent menuDragMouseEvent) {
        }

        MenuDragMouseHandler(TinyMenuUI tinyMenuUI, AnonymousClass1 anonymousClass1) {
            this(tinyMenuUI);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$MenuHandler.class */
    private static class MenuHandler implements MenuListener {
        private MenuHandler() {
        }

        @Override // javax.swing.event.MenuListener
        public void menuSelected(MenuEvent menuEvent) {
        }

        @Override // javax.swing.event.MenuListener
        public void menuDeselected(MenuEvent menuEvent) {
        }

        @Override // javax.swing.event.MenuListener
        public void menuCanceled(MenuEvent menuEvent) {
            if (MenuSelectionManager.defaultManager().isComponentPartOfCurrentMenu((JMenu) menuEvent.getSource())) {
                MenuSelectionManager.defaultManager().clearSelectedPath();
            }
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$MenuKeyHandler.class */
    private class MenuKeyHandler implements MenuKeyListener {
        private int[] indexes;
        private char lastMnemonic;
        private int lastIndex;
        private int matches;
        private final TinyMenuUI this$0;

        private MenuKeyHandler(TinyMenuUI tinyMenuUI) {
            this.this$0 = tinyMenuUI;
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyTyped(MenuKeyEvent menuKeyEvent) {
            int mnemonic;
            JPopupMenu activePopupMenu;
            if (this.this$0.menuItem == null) {
                return;
            }
            if ((TinyMenuUI.crossMenuMnemonic || (activePopupMenu = TinyMenuUI.getActivePopupMenu()) == null || activePopupMenu == this.this$0.menuItem.getParent()) && (mnemonic = this.this$0.menuItem.getMnemonic()) != 0) {
                MenuElement[] path = menuKeyEvent.getPath();
                if (lower((char) mnemonic) == lower(menuKeyEvent.getKeyChar())) {
                    JPopupMenu popupMenu = ((JMenu) this.this$0.menuItem).getPopupMenu();
                    MenuElement[] subElements = popupMenu.getSubElements();
                    if (subElements.length > 0) {
                        MenuSelectionManager menuSelectionManager = menuKeyEvent.getMenuSelectionManager();
                        MenuElement[] menuElementArr = new MenuElement[path.length + 2];
                        System.arraycopy(path, 0, menuElementArr, 0, path.length);
                        menuElementArr[path.length] = popupMenu;
                        menuElementArr[path.length + 1] = subElements[0];
                        menuSelectionManager.setSelectedPath(menuElementArr);
                    }
                    menuKeyEvent.consume();
                }
            }
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyPressed(MenuKeyEvent menuKeyEvent) {
            if (this.this$0.menuItem == null) {
                return;
            }
            char keyChar = menuKeyEvent.getKeyChar();
            if (Character.isLetterOrDigit(keyChar)) {
                MenuSelectionManager menuSelectionManager = menuKeyEvent.getMenuSelectionManager();
                MenuElement[] path = menuKeyEvent.getPath();
                MenuElement[] selectedPath = menuSelectionManager.getSelectedPath();
                for (int length = selectedPath.length - 1; length >= 0; length--) {
                    if (selectedPath[length] == this.this$0.menuItem) {
                        JPopupMenu popupMenu = ((JMenu) this.this$0.menuItem).getPopupMenu();
                        MenuElement[] subElements = popupMenu.getSubElements();
                        if (this.indexes == null || this.lastMnemonic != keyChar) {
                            this.matches = 0;
                            this.lastIndex = 0;
                            this.indexes = new int[subElements.length];
                            for (int i2 = 0; i2 < subElements.length; i2++) {
                                if (lower((char) ((JMenuItem) subElements[i2]).getMnemonic()) == lower(keyChar)) {
                                    int[] iArr = this.indexes;
                                    int i3 = this.matches;
                                    this.matches = i3 + 1;
                                    iArr[i3] = i2;
                                }
                            }
                            this.lastMnemonic = keyChar;
                        }
                        if (this.matches != 0) {
                            if (this.matches == 1) {
                                JMenuItem jMenuItem = (JMenuItem) subElements[this.indexes[0]];
                                if (!(jMenuItem instanceof JMenu)) {
                                    menuSelectionManager.clearSelectedPath();
                                    jMenuItem.doClick();
                                }
                            } else {
                                if (this.lastIndex == this.matches) {
                                    this.lastIndex = 0;
                                }
                                int[] iArr2 = this.indexes;
                                int i4 = this.lastIndex;
                                this.lastIndex = i4 + 1;
                                MenuElement menuElement = subElements[iArr2[i4]];
                                MenuElement[] menuElementArr = new MenuElement[path.length + 2];
                                System.arraycopy(path, 0, menuElementArr, 0, path.length);
                                menuElementArr[path.length] = popupMenu;
                                menuElementArr[path.length + 1] = menuElement;
                                menuSelectionManager.setSelectedPath(menuElementArr);
                            }
                        }
                        menuKeyEvent.consume();
                        return;
                    }
                }
            }
        }

        @Override // javax.swing.event.MenuKeyListener
        public void menuKeyReleased(MenuKeyEvent menuKeyEvent) {
        }

        private char lower(char c2) {
            return Character.toLowerCase(c2);
        }

        MenuKeyHandler(TinyMenuUI tinyMenuUI, AnonymousClass1 anonymousClass1) {
            this(tinyMenuUI);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$MouseInputHandler.class */
    protected class MouseInputHandler implements MouseInputListener {
        private final TinyMenuUI this$0;

        protected MouseInputHandler(TinyMenuUI tinyMenuUI) {
            this.this$0 = tinyMenuUI;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            JMenu jMenu = (JMenu) this.this$0.menuItem;
            if (jMenu.isEnabled()) {
                MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
                if (jMenu.isTopLevelMenu()) {
                    if (jMenu.isSelected()) {
                        menuSelectionManagerDefaultManager.clearSelectedPath();
                    } else {
                        MenuContainer parent = jMenu.getParent();
                        if (parent != null && (parent instanceof JMenuBar)) {
                            menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{(MenuElement) parent, jMenu});
                        }
                    }
                }
                MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
                if (selectedPath.length <= 0 || selectedPath[selectedPath.length - 1] == jMenu.getPopupMenu()) {
                    return;
                }
                if (jMenu.isTopLevelMenu() || jMenu.getDelay() == 0) {
                    TinyMenuUI.appendPath(selectedPath, jMenu.getPopupMenu());
                } else {
                    this.this$0.setupPostTimer(jMenu);
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (((JMenu) this.this$0.menuItem).isEnabled()) {
                MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
                menuSelectionManagerDefaultManager.processMouseEvent(mouseEvent);
                if (mouseEvent.isConsumed()) {
                    return;
                }
                menuSelectionManagerDefaultManager.clearSelectedPath();
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            JMenu jMenu = (JMenu) this.this$0.menuItem;
            if (!jMenu.isEnabled() || TinyMenuUI.systemMenuShowing || jMenu.getClientProperty(TinyMenuUI.IS_SYSTEM_MENU_KEY) == Boolean.TRUE) {
                return;
            }
            jMenu.putClientProperty("rollover", Boolean.TRUE);
            MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
            MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
            if (jMenu.isTopLevelMenu()) {
                if (selectedPath.length > 0 && selectedPath[0] == jMenu.getParent()) {
                    menuSelectionManagerDefaultManager.setSelectedPath(new MenuElement[]{(MenuElement) jMenu.getParent(), jMenu, jMenu.getPopupMenu()});
                }
            } else if (selectedPath.length <= 0 || selectedPath[selectedPath.length - 1] != jMenu.getPopupMenu()) {
                if (jMenu.getDelay() == 0) {
                    TinyMenuUI.appendPath(this.this$0.getPath(), jMenu.getPopupMenu());
                } else {
                    menuSelectionManagerDefaultManager.setSelectedPath(this.this$0.getPath());
                    this.this$0.setupPostTimer(jMenu);
                }
            }
            if (jMenu.isTopLevelMenu()) {
                jMenu.repaint();
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            JMenu jMenu = (JMenu) this.this$0.menuItem;
            if (!jMenu.isEnabled() || TinyMenuUI.systemMenuShowing || jMenu.getClientProperty(TinyMenuUI.IS_SYSTEM_MENU_KEY) == Boolean.TRUE) {
                return;
            }
            jMenu.putClientProperty("rollover", Boolean.FALSE);
            if (jMenu.isTopLevelMenu()) {
                jMenu.repaint();
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (((JMenu) this.this$0.menuItem).isEnabled()) {
                MenuSelectionManager.defaultManager().processMouseEvent(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$PostAction.class */
    private static class PostAction extends AbstractAction {
        JMenu menu;
        boolean force;

        PostAction(JMenu jMenu, boolean z2) {
            this.force = false;
            this.menu = jMenu;
            this.force = z2;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JPopupMenu activePopupMenu;
            if (TinyMenuUI.crossMenuMnemonic || (activePopupMenu = TinyMenuUI.getActivePopupMenu()) == null || activePopupMenu == this.menu.getParent()) {
                MenuSelectionManager menuSelectionManagerDefaultManager = MenuSelectionManager.defaultManager();
                if (!this.force) {
                    MenuElement[] selectedPath = menuSelectionManagerDefaultManager.getSelectedPath();
                    if (selectedPath.length <= 0 || selectedPath[selectedPath.length - 1] != this.menu) {
                        return;
                    }
                    TinyMenuUI.appendPath(selectedPath, this.menu.getPopupMenu());
                    return;
                }
                MenuContainer parent = this.menu.getParent();
                if (parent == null || !(parent instanceof JMenuBar)) {
                    return;
                }
                MenuElement[] subElements = this.menu.getPopupMenu().getSubElements();
                menuSelectionManagerDefaultManager.setSelectedPath(subElements.length > 0 ? new MenuElement[]{(MenuElement) parent, this.menu, this.menu.getPopupMenu(), subElements[0]} : new MenuElement[]{(MenuElement) parent, this.menu, this.menu.getPopupMenu()});
            }
        }

        @Override // javax.swing.AbstractAction, javax.swing.Action
        public boolean isEnabled() {
            return this.menu.getModel().isEnabled();
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyMenuUI$PropertyChangeHandler.class */
    private class PropertyChangeHandler implements PropertyChangeListener {
        private final TinyMenuUI this$0;

        private PropertyChangeHandler(TinyMenuUI tinyMenuUI) {
            this.this$0 = tinyMenuUI;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals(AbstractButton.MNEMONIC_CHANGED_PROPERTY)) {
                this.this$0.updateMnemonicBinding();
            }
        }

        PropertyChangeHandler(TinyMenuUI tinyMenuUI, AnonymousClass1 anonymousClass1) {
            this(tinyMenuUI);
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyMenuUI();
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected void installDefaults() {
        super.installDefaults();
        ((JMenu) this.menuItem).setDelay(200);
        crossMenuMnemonic = UIManager.getBoolean("Menu.crossMenuMnemonic");
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    ActionMap getActionMap() {
        return createActionMap();
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    ActionMap createActionMap() {
        ActionMap actionMapCreateActionMap = super.createActionMap();
        if (actionMapCreateActionMap != null) {
            actionMapCreateActionMap.put("selectMenu", new PostAction((JMenu) this.menuItem, true));
        }
        return actionMapCreateActionMap;
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected String getPropertyPrefix() {
        return "Menu";
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected void installListeners() {
        super.installListeners();
        if (this.changeListener == null) {
            this.changeListener = createChangeListener(this.menuItem);
        }
        if (this.changeListener != null) {
            this.menuItem.addChangeListener(this.changeListener);
        }
        if (this.propertyChangeListener == null) {
            this.propertyChangeListener = createPropertyChangeListener(this.menuItem);
        }
        if (this.propertyChangeListener != null) {
            this.menuItem.addPropertyChangeListener(this.propertyChangeListener);
        }
        if (TinyUtils.is1dot4()) {
            MenuKeyListener menuKeyListenerCreateMenuKeyListener = createMenuKeyListener(this.menuItem);
            this.menuKeyListener = menuKeyListenerCreateMenuKeyListener;
            if (menuKeyListenerCreateMenuKeyListener != null) {
                this.menuItem.addMenuKeyListener(this.menuKeyListener);
            }
        }
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
        updateMnemonicBinding();
    }

    void updateMnemonicBinding() {
        int mnemonic = this.menuItem.getModel().getMnemonic();
        int[] iArr = (int[]) UIManager.get("Menu.shortcutKeys");
        if (mnemonic == this.lastMnemonic || iArr == null) {
            return;
        }
        if (this.lastMnemonic != 0 && this.windowInputMap != null) {
            for (int i2 : iArr) {
                this.windowInputMap.remove(KeyStroke.getKeyStroke(this.lastMnemonic, i2, false));
            }
        }
        if (mnemonic != 0) {
            if (this.windowInputMap == null) {
                this.windowInputMap = createInputMap(2);
                SwingUtilities.replaceUIInputMap(this.menuItem, 2, this.windowInputMap);
            }
            for (int i3 : iArr) {
                this.windowInputMap.put(KeyStroke.getKeyStroke(mnemonic, i3, false), "selectMenu");
            }
        }
        this.lastMnemonic = mnemonic;
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected MouseInputListener createMouseInputListener(JComponent jComponent) {
        return new MouseInputHandler(this);
    }

    protected ChangeListener createChangeListener(JComponent jComponent) {
        return null;
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected PropertyChangeListener createPropertyChangeListener(JComponent jComponent) {
        return new PropertyChangeHandler(this, null);
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected void uninstallDefaults() {
        this.menuItem.setArmed(false);
        this.menuItem.setSelected(false);
        this.menuItem.resetKeyboardActions();
        super.uninstallDefaults();
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        if (this.changeListener != null) {
            this.menuItem.removeChangeListener(this.changeListener);
            this.changeListener = null;
        }
        if (this.propertyChangeListener != null) {
            this.menuItem.removePropertyChangeListener(this.propertyChangeListener);
            this.propertyChangeListener = null;
        }
        if (this.menuKeyListener != null) {
            this.menuItem.removeMenuKeyListener(this.menuKeyListener);
            this.menuKeyListener = null;
        }
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI
    protected MenuDragMouseListener createMenuDragMouseListener(JComponent jComponent) {
        return new MenuDragMouseHandler(this, null);
    }

    protected MenuKeyListener createMenuKeyListener(JComponent jComponent) {
        return new MenuKeyHandler(this, null);
    }

    @Override // de.muntjak.tinylookandfeel.TinyMenuItemUI, javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        if (((JMenu) this.menuItem).isTopLevelMenu()) {
            return new Dimension(jComponent.getPreferredSize().width, Short.MAX_VALUE);
        }
        return null;
    }

    protected void setupPostTimer(JMenu jMenu) {
        Timer timer = new Timer(jMenu.getDelay(), new PostAction(jMenu, false));
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

    static JPopupMenu getActivePopupMenu() {
        MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
        for (int length = selectedPath.length - 1; length >= 0; length--) {
            MenuElement menuElement = selectedPath[length];
            if (menuElement instanceof JPopupMenu) {
                return (JPopupMenu) menuElement;
            }
        }
        return null;
    }
}
