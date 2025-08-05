package javax.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.MenuContainer;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.MenuBarUI;

/* loaded from: rt.jar:javax/swing/JMenuBar.class */
public class JMenuBar extends JComponent implements Accessible, MenuElement {
    private static final String uiClassID = "MenuBarUI";
    private transient SingleSelectionModel selectionModel;
    private boolean paintBorder = true;
    private Insets margin = null;
    private static final boolean TRACE = false;
    private static final boolean VERBOSE = false;
    private static final boolean DEBUG = false;

    public JMenuBar() {
        setFocusTraversalKeysEnabled(false);
        setSelectionModel(new DefaultSingleSelectionModel());
        updateUI();
    }

    public MenuBarUI getUI() {
        return (MenuBarUI) this.ui;
    }

    public void setUI(MenuBarUI menuBarUI) {
        super.setUI((ComponentUI) menuBarUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((MenuBarUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public SingleSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    public void setSelectionModel(SingleSelectionModel singleSelectionModel) {
        SingleSelectionModel singleSelectionModel2 = this.selectionModel;
        this.selectionModel = singleSelectionModel;
        firePropertyChange("selectionModel", singleSelectionModel2, this.selectionModel);
    }

    public JMenu add(JMenu jMenu) {
        super.add((Component) jMenu);
        return jMenu;
    }

    public JMenu getMenu(int i2) {
        Component componentAtIndex = getComponentAtIndex(i2);
        if (componentAtIndex instanceof JMenu) {
            return (JMenu) componentAtIndex;
        }
        return null;
    }

    public int getMenuCount() {
        return getComponentCount();
    }

    public void setHelpMenu(JMenu jMenu) {
        throw new Error("setHelpMenu() not yet implemented.");
    }

    @Transient
    public JMenu getHelpMenu() {
        throw new Error("getHelpMenu() not yet implemented.");
    }

    @Deprecated
    public Component getComponentAtIndex(int i2) {
        if (i2 < 0 || i2 >= getComponentCount()) {
            return null;
        }
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

    public void setSelected(Component component) {
        getSelectionModel().setSelectedIndex(getComponentIndex(component));
    }

    public boolean isSelected() {
        return this.selectionModel.isSelected();
    }

    public boolean isBorderPainted() {
        return this.paintBorder;
    }

    public void setBorderPainted(boolean z2) {
        boolean z3 = this.paintBorder;
        this.paintBorder = z2;
        firePropertyChange(AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, z3, this.paintBorder);
        if (z2 != z3) {
            revalidate();
            repaint();
        }
    }

    @Override // javax.swing.JComponent
    protected void paintBorder(Graphics graphics) {
        if (isBorderPainted()) {
            super.paintBorder(graphics);
        }
    }

    public void setMargin(Insets insets) {
        Insets insets2 = this.margin;
        this.margin = insets;
        firePropertyChange(AbstractButton.MARGIN_CHANGED_PROPERTY, insets2, insets);
        if (insets2 == null || !insets2.equals(insets)) {
            revalidate();
            repaint();
        }
    }

    public Insets getMargin() {
        if (this.margin == null) {
            return new Insets(0, 0, 0, 0);
        }
        return this.margin;
    }

    @Override // javax.swing.MenuElement
    public void processMouseEvent(MouseEvent mouseEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
    }

    @Override // javax.swing.MenuElement
    public void processKeyEvent(KeyEvent keyEvent, MenuElement[] menuElementArr, MenuSelectionManager menuSelectionManager) {
    }

    @Override // javax.swing.MenuElement
    public void menuSelectionChanged(boolean z2) {
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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",margin=" + (this.margin != null ? this.margin.toString() : "") + ",paintBorder=" + (this.paintBorder ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJMenuBar();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JMenuBar$AccessibleJMenuBar.class */
    protected class AccessibleJMenuBar extends JComponent.AccessibleJComponent implements AccessibleSelection {
        protected AccessibleJMenuBar() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            return super.getAccessibleStateSet();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.MENU_BAR;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            if (JMenuBar.this.isSelected()) {
                return 1;
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            if (!JMenuBar.this.isSelected() || i2 != 0) {
                return null;
            }
            int selectedIndex = JMenuBar.this.getSelectionModel().getSelectedIndex();
            if (JMenuBar.this.getComponentAtIndex(selectedIndex) instanceof Accessible) {
                return (Accessible) JMenuBar.this.getComponentAtIndex(selectedIndex);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            return i2 == JMenuBar.this.getSelectionModel().getSelectedIndex();
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            int selectedIndex = JMenuBar.this.getSelectionModel().getSelectedIndex();
            if (i2 == selectedIndex) {
                return;
            }
            if (selectedIndex >= 0 && selectedIndex < JMenuBar.this.getMenuCount() && JMenuBar.this.getMenu(selectedIndex) != null) {
                MenuSelectionManager.defaultManager().setSelectedPath(null);
            }
            JMenuBar.this.getSelectionModel().setSelectedIndex(i2);
            JMenu menu = JMenuBar.this.getMenu(i2);
            if (menu != null) {
                MenuSelectionManager.defaultManager().setSelectedPath(new MenuElement[]{JMenuBar.this, menu, menu.getPopupMenu()});
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            if (i2 >= 0 && i2 < JMenuBar.this.getMenuCount()) {
                if (JMenuBar.this.getMenu(i2) != null) {
                    MenuSelectionManager.defaultManager().setSelectedPath(null);
                }
                JMenuBar.this.getSelectionModel().setSelectedIndex(-1);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            int selectedIndex = JMenuBar.this.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0 && selectedIndex < JMenuBar.this.getMenuCount() && JMenuBar.this.getMenu(selectedIndex) != null) {
                MenuSelectionManager.defaultManager().setSelectedPath(null);
            }
            JMenuBar.this.getSelectionModel().setSelectedIndex(-1);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
        }
    }

    @Override // javax.swing.JComponent
    protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
        boolean zProcessKeyBinding = super.processKeyBinding(keyStroke, keyEvent, i2, z2);
        if (!zProcessKeyBinding) {
            for (MenuElement menuElement : getSubElements()) {
                if (processBindingForKeyStrokeRecursive(menuElement, keyStroke, keyEvent, i2, z2)) {
                    return true;
                }
            }
        }
        return zProcessKeyBinding;
    }

    static boolean processBindingForKeyStrokeRecursive(MenuElement menuElement, KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
        if (menuElement == null) {
            return false;
        }
        Component component = menuElement.getComponent();
        if ((!component.isVisible() && !(component instanceof JPopupMenu)) || !component.isEnabled()) {
            return false;
        }
        if (component != null && (component instanceof JComponent) && ((JComponent) component).processKeyBinding(keyStroke, keyEvent, i2, z2)) {
            return true;
        }
        for (MenuElement menuElement2 : menuElement.getSubElements()) {
            if (processBindingForKeyStrokeRecursive(menuElement2, keyStroke, keyEvent, i2, z2)) {
                return true;
            }
        }
        return false;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        KeyboardManager.getCurrentManager().registerMenuBar(this);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        super.removeNotify();
        KeyboardManager.getCurrentManager().unregisterMenuBar(this);
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
        Object[] objArr = new Object[4];
        if (this.selectionModel instanceof Serializable) {
            int i2 = 0 + 1;
            objArr[0] = "selectionModel";
            int i3 = i2 + 1;
            objArr[i2] = this.selectionModel;
        }
        objectOutputStream.writeObject(objArr);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Object[] objArr = (Object[]) objectInputStream.readObject();
        for (int i2 = 0; i2 < objArr.length && objArr[i2] != null; i2 += 2) {
            if (objArr[i2].equals("selectionModel")) {
                this.selectionModel = (SingleSelectionModel) objArr[i2 + 1];
            }
        }
    }
}
