package javax.swing;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.swing.AbstractButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.MenuItemUI;

/* loaded from: rt.jar:javax/swing/JMenuItem.class */
public class JMenuItem extends AbstractButton implements Accessible, MenuElement {
    private static final String uiClassID = "MenuItemUI";
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;
    private boolean isMouseDragged;
    private KeyStroke accelerator;

    public JMenuItem() {
        this((String) null, (Icon) null);
    }

    public JMenuItem(Icon icon) {
        this((String) null, icon);
    }

    public JMenuItem(String str) {
        this(str, (Icon) null);
    }

    public JMenuItem(Action action) {
        this();
        setAction(action);
    }

    public JMenuItem(String str, Icon icon) throws IllegalArgumentException {
        this.isMouseDragged = false;
        setModel(new DefaultButtonModel());
        init(str, icon);
        initFocusability();
    }

    public JMenuItem(String str, int i2) throws IllegalArgumentException {
        this.isMouseDragged = false;
        setModel(new DefaultButtonModel());
        init(str, null);
        setMnemonic(i2);
        initFocusability();
    }

    @Override // javax.swing.AbstractButton
    public void setModel(ButtonModel buttonModel) throws IllegalArgumentException {
        super.setModel(buttonModel);
        if (buttonModel instanceof DefaultButtonModel) {
            ((DefaultButtonModel) buttonModel).setMenuItem(true);
        }
    }

    void initFocusability() {
        setFocusable(false);
    }

    @Override // javax.swing.AbstractButton
    protected void init(String str, Icon icon) {
        if (str != null) {
            setText(str);
        }
        if (icon != null) {
            setIcon(icon);
        }
        addFocusListener(new MenuItemFocusListener());
        setUIProperty(AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, Boolean.FALSE);
        setFocusPainted(false);
        setHorizontalTextPosition(11);
        setHorizontalAlignment(10);
        updateUI();
    }

    /* loaded from: rt.jar:javax/swing/JMenuItem$MenuItemFocusListener.class */
    private static class MenuItemFocusListener implements FocusListener, Serializable {
        private MenuItemFocusListener() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            JMenuItem jMenuItem = (JMenuItem) focusEvent.getSource();
            if (jMenuItem.isFocusPainted()) {
                jMenuItem.repaint();
            }
        }
    }

    public void setUI(MenuItemUI menuItemUI) {
        super.setUI((ButtonUI) menuItemUI);
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent
    public void updateUI() {
        setUI((MenuItemUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public void setArmed(boolean z2) {
        ButtonModel model = getModel();
        model.isArmed();
        if (model.isArmed() != z2) {
            model.setArmed(z2);
        }
    }

    public boolean isArmed() {
        return getModel().isArmed();
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        if (!z2 && !UIManager.getBoolean("MenuItem.disabledAreNavigable")) {
            setArmed(false);
        }
        super.setEnabled(z2);
    }

    @Override // javax.swing.JComponent
    boolean alwaysOnTop() {
        if (SwingUtilities.getAncestorOfClass(JInternalFrame.class, this) != null) {
            return false;
        }
        return true;
    }

    public void setAccelerator(KeyStroke keyStroke) {
        KeyStroke keyStroke2 = this.accelerator;
        this.accelerator = keyStroke;
        repaint();
        revalidate();
        firePropertyChange("accelerator", keyStroke2, this.accelerator);
    }

    public KeyStroke getAccelerator() {
        return this.accelerator;
    }

    @Override // javax.swing.AbstractButton
    protected void configurePropertiesFromAction(Action action) throws IllegalArgumentException {
        super.configurePropertiesFromAction(action);
        configureAcceleratorFromAction(action);
    }

    @Override // javax.swing.AbstractButton
    void setIconFromAction(Action action) {
        Icon icon = null;
        if (action != null) {
            icon = (Icon) action.getValue(Action.SMALL_ICON);
        }
        setIcon(icon);
    }

    @Override // javax.swing.AbstractButton
    void largeIconChanged(Action action) {
    }

    @Override // javax.swing.AbstractButton
    void smallIconChanged(Action action) {
        setIconFromAction(action);
    }

    void configureAcceleratorFromAction(Action action) {
        setAccelerator(action == null ? null : (KeyStroke) action.getValue(Action.ACCELERATOR_KEY));
    }

    @Override // javax.swing.AbstractButton
    protected void actionPropertyChanged(Action action, String str) throws IllegalArgumentException {
        if (str == Action.ACCELERATOR_KEY) {
            configureAcceleratorFromAction(action);
        } else {
            super.actionPropertyChanged(action, str);
        }
    }

    @Override // javax.swing.MenuElement
    public void processMouseEvent(MouseEvent mouseEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        processMenuDragMouseEvent(new MenuDragMouseEvent(mouseEvent.getComponent(), mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), menuElementArr, menuSelectionManager));
    }

    @Override // javax.swing.MenuElement
    public void processKeyEvent(KeyEvent keyEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
        MenuKeyEvent menuKeyEvent = new MenuKeyEvent(keyEvent.getComponent(), keyEvent.getID(), keyEvent.getWhen(), keyEvent.getModifiers(), keyEvent.getKeyCode(), keyEvent.getKeyChar(), menuElementArr, menuSelectionManager);
        processMenuKeyEvent(menuKeyEvent);
        if (menuKeyEvent.isConsumed()) {
            keyEvent.consume();
        }
    }

    public void processMenuDragMouseEvent(MenuDragMouseEvent menuDragMouseEvent) {
        switch (menuDragMouseEvent.getID()) {
            case 502:
                if (this.isMouseDragged) {
                    fireMenuDragMouseReleased(menuDragMouseEvent);
                    break;
                }
                break;
            case 504:
                this.isMouseDragged = false;
                fireMenuDragMouseEntered(menuDragMouseEvent);
                break;
            case 505:
                this.isMouseDragged = false;
                fireMenuDragMouseExited(menuDragMouseEvent);
                break;
            case 506:
                this.isMouseDragged = true;
                fireMenuDragMouseDragged(menuDragMouseEvent);
                break;
        }
    }

    public void processMenuKeyEvent(MenuKeyEvent menuKeyEvent) {
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

    protected void fireMenuDragMouseEntered(MenuDragMouseEvent menuDragMouseEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuDragMouseListener.class) {
                ((MenuDragMouseListener) listenerList[length + 1]).menuDragMouseEntered(menuDragMouseEvent);
            }
        }
    }

    protected void fireMenuDragMouseExited(MenuDragMouseEvent menuDragMouseEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuDragMouseListener.class) {
                ((MenuDragMouseListener) listenerList[length + 1]).menuDragMouseExited(menuDragMouseEvent);
            }
        }
    }

    protected void fireMenuDragMouseDragged(MenuDragMouseEvent menuDragMouseEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuDragMouseListener.class) {
                ((MenuDragMouseListener) listenerList[length + 1]).menuDragMouseDragged(menuDragMouseEvent);
            }
        }
    }

    protected void fireMenuDragMouseReleased(MenuDragMouseEvent menuDragMouseEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuDragMouseListener.class) {
                ((MenuDragMouseListener) listenerList[length + 1]).menuDragMouseReleased(menuDragMouseEvent);
            }
        }
    }

    protected void fireMenuKeyPressed(MenuKeyEvent menuKeyEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuKeyListener.class) {
                ((MenuKeyListener) listenerList[length + 1]).menuKeyPressed(menuKeyEvent);
            }
        }
    }

    protected void fireMenuKeyReleased(MenuKeyEvent menuKeyEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuKeyListener.class) {
                ((MenuKeyListener) listenerList[length + 1]).menuKeyReleased(menuKeyEvent);
            }
        }
    }

    protected void fireMenuKeyTyped(MenuKeyEvent menuKeyEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == MenuKeyListener.class) {
                ((MenuKeyListener) listenerList[length + 1]).menuKeyTyped(menuKeyEvent);
            }
        }
    }

    public void menuSelectionChanged(boolean z2) {
        setArmed(z2);
    }

    public MenuElement[] getSubElements() {
        return new MenuElement[0];
    }

    public Component getComponent() {
        return this;
    }

    public void addMenuDragMouseListener(MenuDragMouseListener menuDragMouseListener) {
        this.listenerList.add(MenuDragMouseListener.class, menuDragMouseListener);
    }

    public void removeMenuDragMouseListener(MenuDragMouseListener menuDragMouseListener) {
        this.listenerList.remove(MenuDragMouseListener.class, menuDragMouseListener);
    }

    public MenuDragMouseListener[] getMenuDragMouseListeners() {
        return (MenuDragMouseListener[]) this.listenerList.getListeners(MenuDragMouseListener.class);
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

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (getUIClassID().equals(uiClassID)) {
            updateUI();
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

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString();
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJMenuItem();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JMenuItem$AccessibleJMenuItem.class */
    protected class AccessibleJMenuItem extends AbstractButton.AccessibleAbstractButton implements ChangeListener {
        private boolean isArmed;
        private boolean hasFocus;
        private boolean isPressed;
        private boolean isSelected;

        AccessibleJMenuItem() {
            super();
            this.isArmed = false;
            this.hasFocus = false;
            this.isPressed = false;
            this.isSelected = false;
            JMenuItem.this.addChangeListener(this);
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU_ITEM;
        }

        private void fireAccessibilityFocusedEvent(JMenuItem jMenuItem) {
            MenuElement[] selectedPath = MenuSelectionManager.defaultManager().getSelectedPath();
            if (selectedPath.length > 0 && jMenuItem == selectedPath[selectedPath.length - 1]) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.FOCUSED);
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, false, true);
            if (JMenuItem.this.getModel().isArmed()) {
                if (!this.isArmed) {
                    this.isArmed = true;
                    firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.ARMED);
                    fireAccessibilityFocusedEvent(JMenuItem.this);
                }
            } else if (this.isArmed) {
                this.isArmed = false;
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.ARMED, null);
            }
            if (JMenuItem.this.isFocusOwner()) {
                if (!this.hasFocus) {
                    this.hasFocus = true;
                    firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.FOCUSED);
                }
            } else if (this.hasFocus) {
                this.hasFocus = false;
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.FOCUSED, null);
            }
            if (JMenuItem.this.getModel().isPressed()) {
                if (!this.isPressed) {
                    this.isPressed = true;
                    firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.PRESSED);
                }
            } else if (this.isPressed) {
                this.isPressed = false;
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.PRESSED, null);
            }
            if (JMenuItem.this.getModel().isSelected()) {
                if (!this.isSelected) {
                    this.isSelected = true;
                    firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.CHECKED);
                    fireAccessibilityFocusedEvent(JMenuItem.this);
                    return;
                }
                return;
            }
            if (this.isSelected) {
                this.isSelected = false;
                firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.CHECKED, null);
            }
        }
    }
}
