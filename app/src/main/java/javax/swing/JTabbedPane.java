package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.plaf.UIResource;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/JTabbedPane.class */
public class JTabbedPane extends JComponent implements Serializable, Accessible, SwingConstants {
    public static final int WRAP_TAB_LAYOUT = 0;
    public static final int SCROLL_TAB_LAYOUT = 1;
    private static final String uiClassID = "TabbedPaneUI";
    protected int tabPlacement;
    private int tabLayoutPolicy;
    protected SingleSelectionModel model;
    private boolean haveRegistered;
    protected ChangeListener changeListener;
    private final List<Page> pages;
    private Component visComp;
    protected transient ChangeEvent changeEvent;

    public JTabbedPane() {
        this(1, 0);
    }

    public JTabbedPane(int i2) {
        this(i2, 0);
    }

    public JTabbedPane(int i2, int i3) {
        this.tabPlacement = 1;
        this.changeListener = null;
        this.visComp = null;
        this.changeEvent = null;
        setTabPlacement(i2);
        setTabLayoutPolicy(i3);
        this.pages = new ArrayList(1);
        setModel(new DefaultSingleSelectionModel());
        updateUI();
    }

    public TabbedPaneUI getUI() {
        return (TabbedPaneUI) this.ui;
    }

    public void setUI(TabbedPaneUI tabbedPaneUI) {
        super.setUI((ComponentUI) tabbedPaneUI);
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (this.pages.get(i2).disabledIcon instanceof UIResource) {
                setDisabledIconAt(i2, null);
            }
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((TabbedPaneUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    /* loaded from: rt.jar:javax/swing/JTabbedPane$ModelListener.class */
    protected class ModelListener implements ChangeListener, Serializable {
        protected ModelListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JTabbedPane.this.fireStateChanged();
        }
    }

    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex < 0) {
            if (this.visComp != null && this.visComp.isVisible()) {
                this.visComp.setVisible(false);
            }
            this.visComp = null;
        } else {
            Component componentAt = getComponentAt(selectedIndex);
            if (componentAt != null && componentAt != this.visComp) {
                boolean z2 = false;
                if (this.visComp != null) {
                    z2 = SwingUtilities.findFocusOwner(this.visComp) != null;
                    if (this.visComp.isVisible()) {
                        this.visComp.setVisible(false);
                    }
                }
                if (!componentAt.isVisible()) {
                    componentAt.setVisible(true);
                }
                if (z2) {
                    SwingUtilities2.tabbedPaneChangeFocusTo(componentAt);
                }
                this.visComp = componentAt;
            }
        }
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    public SingleSelectionModel getModel() {
        return this.model;
    }

    public void setModel(SingleSelectionModel singleSelectionModel) {
        SingleSelectionModel model = getModel();
        if (model != null) {
            model.removeChangeListener(this.changeListener);
            this.changeListener = null;
        }
        this.model = singleSelectionModel;
        if (singleSelectionModel != null) {
            this.changeListener = createChangeListener();
            singleSelectionModel.addChangeListener(this.changeListener);
        }
        firePropertyChange("model", model, singleSelectionModel);
        repaint();
    }

    public int getTabPlacement() {
        return this.tabPlacement;
    }

    public void setTabPlacement(int i2) {
        if (i2 != 1 && i2 != 2 && i2 != 3 && i2 != 4) {
            throw new IllegalArgumentException("illegal tab placement: must be TOP, BOTTOM, LEFT, or RIGHT");
        }
        if (this.tabPlacement != i2) {
            int i3 = this.tabPlacement;
            this.tabPlacement = i2;
            firePropertyChange("tabPlacement", i3, i2);
            revalidate();
            repaint();
        }
    }

    public int getTabLayoutPolicy() {
        return this.tabLayoutPolicy;
    }

    public void setTabLayoutPolicy(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("illegal tab layout policy: must be WRAP_TAB_LAYOUT or SCROLL_TAB_LAYOUT");
        }
        if (this.tabLayoutPolicy != i2) {
            int i3 = this.tabLayoutPolicy;
            this.tabLayoutPolicy = i2;
            firePropertyChange("tabLayoutPolicy", i3, i2);
            revalidate();
            repaint();
        }
    }

    @Transient
    public int getSelectedIndex() {
        return this.model.getSelectedIndex();
    }

    public void setSelectedIndex(int i2) {
        if (i2 != -1) {
            checkIndex(i2);
        }
        setSelectedIndexImpl(i2, true);
    }

    private void setSelectedIndexImpl(int i2, boolean z2) {
        int selectedIndex = this.model.getSelectedIndex();
        Page page = null;
        Page page2 = null;
        String accessibleName = null;
        boolean z3 = z2 && selectedIndex != i2;
        if (z3) {
            if (this.accessibleContext != null) {
                accessibleName = this.accessibleContext.getAccessibleName();
            }
            if (selectedIndex >= 0) {
                page = this.pages.get(selectedIndex);
            }
            if (i2 >= 0) {
                page2 = this.pages.get(i2);
            }
        }
        this.model.setSelectedIndex(i2);
        if (z3) {
            changeAccessibleSelection(page, accessibleName, page2);
        }
    }

    private void changeAccessibleSelection(Page page, String str, Page page2) {
        if (this.accessibleContext == null) {
            return;
        }
        if (page != null) {
            page.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.SELECTED, null);
        }
        if (page2 != null) {
            page2.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.SELECTED);
        }
        this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_NAME_PROPERTY, str, this.accessibleContext.getAccessibleName());
    }

    @Transient
    public Component getSelectedComponent() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex == -1) {
            return null;
        }
        return getComponentAt(selectedIndex);
    }

    public void setSelectedComponent(Component component) {
        int iIndexOfComponent = indexOfComponent(component);
        if (iIndexOfComponent != -1) {
            setSelectedIndex(iIndexOfComponent);
            return;
        }
        throw new IllegalArgumentException("component not found in tabbed pane");
    }

    public void insertTab(String str, Icon icon, Component component, String str2, int i2) {
        int i3 = i2;
        int iIndexOfComponent = indexOfComponent(component);
        if (component != null && iIndexOfComponent != -1) {
            removeTabAt(iIndexOfComponent);
            if (i3 > iIndexOfComponent) {
                i3--;
            }
        }
        int selectedIndex = getSelectedIndex();
        this.pages.add(i3, new Page(this, str != null ? str : "", icon, null, component, str2));
        if (component != null) {
            addImpl(component, null, -1);
            component.setVisible(false);
        } else {
            firePropertyChange("indexForNullComponent", -1, i2);
        }
        if (this.pages.size() == 1) {
            setSelectedIndex(0);
        }
        if (selectedIndex >= i3) {
            setSelectedIndexImpl(selectedIndex + 1, false);
        }
        if (!this.haveRegistered && str2 != null) {
            ToolTipManager.sharedInstance().registerComponent(this);
            this.haveRegistered = true;
        }
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, component);
        }
        revalidate();
        repaint();
    }

    public void addTab(String str, Icon icon, Component component, String str2) {
        insertTab(str, icon, component, str2, this.pages.size());
    }

    public void addTab(String str, Icon icon, Component component) {
        insertTab(str, icon, component, null, this.pages.size());
    }

    public void addTab(String str, Component component) {
        insertTab(str, null, component, null, this.pages.size());
    }

    @Override // java.awt.Container, bA.f
    public Component add(Component component) {
        if (!(component instanceof UIResource)) {
            addTab(component.getName(), component);
        } else {
            super.add(component);
        }
        return component;
    }

    @Override // java.awt.Container
    public Component add(String str, Component component) {
        if (!(component instanceof UIResource)) {
            addTab(str, component);
        } else {
            super.add(str, component);
        }
        return component;
    }

    @Override // java.awt.Container
    public Component add(Component component, int i2) {
        if (!(component instanceof UIResource)) {
            insertTab(component.getName(), null, component, null, i2 == -1 ? getTabCount() : i2);
        } else {
            super.add(component, i2);
        }
        return component;
    }

    @Override // java.awt.Container
    public void add(Component component, Object obj) {
        if (!(component instanceof UIResource)) {
            if (obj instanceof String) {
                addTab((String) obj, component);
                return;
            } else if (obj instanceof Icon) {
                addTab(null, (Icon) obj, component);
                return;
            } else {
                add(component);
                return;
            }
        }
        super.add(component, obj);
    }

    @Override // java.awt.Container
    public void add(Component component, Object obj, int i2) {
        if (!(component instanceof UIResource)) {
            insertTab(obj instanceof String ? (String) obj : null, obj instanceof Icon ? (Icon) obj : null, component, null, i2 == -1 ? getTabCount() : i2);
        } else {
            super.add(component, obj, i2);
        }
    }

    public void removeTabAt(int i2) {
        checkIndex(i2);
        Component componentAt = getComponentAt(i2);
        boolean z2 = false;
        int selectedIndex = getSelectedIndex();
        String accessibleName = null;
        if (componentAt == this.visComp) {
            z2 = SwingUtilities.findFocusOwner(this.visComp) != null;
            this.visComp = null;
        }
        if (this.accessibleContext != null) {
            if (i2 == selectedIndex) {
                this.pages.get(i2).firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.SELECTED, null);
                accessibleName = this.accessibleContext.getAccessibleName();
            }
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, componentAt, null);
        }
        setTabComponentAt(i2, null);
        this.pages.remove(i2);
        putClientProperty("__index_to_remove__", Integer.valueOf(i2));
        if (selectedIndex > i2) {
            setSelectedIndexImpl(selectedIndex - 1, false);
        } else if (selectedIndex >= getTabCount()) {
            setSelectedIndexImpl(selectedIndex - 1, false);
            changeAccessibleSelection(null, accessibleName, selectedIndex != 0 ? this.pages.get(selectedIndex - 1) : null);
        } else if (i2 == selectedIndex) {
            fireStateChanged();
            changeAccessibleSelection(null, accessibleName, this.pages.get(i2));
        }
        if (componentAt != null) {
            Component[] components = getComponents();
            int length = components.length;
            while (true) {
                length--;
                if (length < 0) {
                    break;
                }
                if (components[length] == componentAt) {
                    super.remove(length);
                    componentAt.setVisible(true);
                    break;
                }
            }
        }
        if (z2) {
            SwingUtilities2.tabbedPaneChangeFocusTo(getSelectedComponent());
        }
        revalidate();
        repaint();
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        int iIndexOfComponent = indexOfComponent(component);
        if (iIndexOfComponent != -1) {
            removeTabAt(iIndexOfComponent);
            return;
        }
        Component[] components = getComponents();
        for (int i2 = 0; i2 < components.length; i2++) {
            if (component == components[i2]) {
                super.remove(i2);
                return;
            }
        }
    }

    @Override // java.awt.Container
    public void remove(int i2) {
        removeTabAt(i2);
    }

    @Override // java.awt.Container
    public void removeAll() {
        setSelectedIndexImpl(-1, true);
        int tabCount = getTabCount();
        while (true) {
            int i2 = tabCount;
            tabCount--;
            if (i2 > 0) {
                removeTabAt(tabCount);
            } else {
                return;
            }
        }
    }

    public int getTabCount() {
        return this.pages.size();
    }

    public int getTabRunCount() {
        if (this.ui != null) {
            return ((TabbedPaneUI) this.ui).getTabRunCount(this);
        }
        return 0;
    }

    public String getTitleAt(int i2) {
        return this.pages.get(i2).title;
    }

    public Icon getIconAt(int i2) {
        return this.pages.get(i2).icon;
    }

    public Icon getDisabledIconAt(int i2) {
        Page page = this.pages.get(i2);
        if (page.disabledIcon == null) {
            page.disabledIcon = UIManager.getLookAndFeel().getDisabledIcon(this, page.icon);
        }
        return page.disabledIcon;
    }

    public String getToolTipTextAt(int i2) {
        return this.pages.get(i2).tip;
    }

    public Color getBackgroundAt(int i2) {
        return this.pages.get(i2).getBackground();
    }

    public Color getForegroundAt(int i2) {
        return this.pages.get(i2).getForeground();
    }

    public boolean isEnabledAt(int i2) {
        return this.pages.get(i2).isEnabled();
    }

    public Component getComponentAt(int i2) {
        return this.pages.get(i2).component;
    }

    public int getMnemonicAt(int i2) {
        checkIndex(i2);
        return this.pages.get(i2).getMnemonic();
    }

    public int getDisplayedMnemonicIndexAt(int i2) {
        checkIndex(i2);
        return this.pages.get(i2).getDisplayedMnemonicIndex();
    }

    public Rectangle getBoundsAt(int i2) {
        checkIndex(i2);
        if (this.ui != null) {
            return ((TabbedPaneUI) this.ui).getTabBounds(this, i2);
        }
        return null;
    }

    public void setTitleAt(int i2, String str) {
        Page page = this.pages.get(i2);
        String str2 = page.title;
        page.title = str;
        if (str2 != str) {
            firePropertyChange("indexForTitle", -1, i2);
        }
        page.updateDisplayedMnemonicIndex();
        if (str2 != str && this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, str2, str);
        }
        if (str == null || str2 == null || !str.equals(str2)) {
            revalidate();
            repaint();
        }
    }

    public void setIconAt(int i2, Icon icon) {
        Page page = this.pages.get(i2);
        Icon icon2 = page.icon;
        if (icon != icon2) {
            page.icon = icon;
            if (page.disabledIcon instanceof UIResource) {
                page.disabledIcon = null;
            }
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, icon2, icon);
            }
            revalidate();
            repaint();
        }
    }

    public void setDisabledIconAt(int i2, Icon icon) {
        Icon icon2 = this.pages.get(i2).disabledIcon;
        this.pages.get(i2).disabledIcon = icon;
        if (icon != icon2 && !isEnabledAt(i2)) {
            revalidate();
            repaint();
        }
    }

    public void setToolTipTextAt(int i2, String str) {
        String str2 = this.pages.get(i2).tip;
        this.pages.get(i2).tip = str;
        if (str2 != str && this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, str2, str);
        }
        if (!this.haveRegistered && str != null) {
            ToolTipManager.sharedInstance().registerComponent(this);
            this.haveRegistered = true;
        }
    }

    public void setBackgroundAt(int i2, Color color) {
        Rectangle boundsAt;
        Color color2 = this.pages.get(i2).background;
        this.pages.get(i2).setBackground(color);
        if ((color == null || color2 == null || !color.equals(color2)) && (boundsAt = getBoundsAt(i2)) != null) {
            repaint(boundsAt);
        }
    }

    public void setForegroundAt(int i2, Color color) {
        Rectangle boundsAt;
        Color color2 = this.pages.get(i2).foreground;
        this.pages.get(i2).setForeground(color);
        if ((color == null || color2 == null || !color.equals(color2)) && (boundsAt = getBoundsAt(i2)) != null) {
            repaint(boundsAt);
        }
    }

    public void setEnabledAt(int i2, boolean z2) {
        boolean zIsEnabled = this.pages.get(i2).isEnabled();
        this.pages.get(i2).setEnabled(z2);
        if (z2 != zIsEnabled) {
            revalidate();
            repaint();
        }
    }

    public void setComponentAt(int i2, Component component) {
        Page page = this.pages.get(i2);
        if (component != page.component) {
            boolean z2 = false;
            if (page.component != null) {
                z2 = SwingUtilities.findFocusOwner(page.component) != null;
                synchronized (getTreeLock()) {
                    int componentCount = getComponentCount();
                    Component[] components = getComponents();
                    for (int i3 = 0; i3 < componentCount; i3++) {
                        if (components[i3] == page.component) {
                            super.remove(i3);
                        }
                    }
                }
            }
            page.component = component;
            boolean z3 = getSelectedIndex() == i2;
            if (z3) {
                this.visComp = component;
            }
            if (component != null) {
                component.setVisible(z3);
                addImpl(component, null, -1);
                if (z2) {
                    SwingUtilities2.tabbedPaneChangeFocusTo(component);
                }
            } else {
                repaint();
            }
            revalidate();
        }
    }

    public void setDisplayedMnemonicIndexAt(int i2, int i3) {
        checkIndex(i2);
        this.pages.get(i2).setDisplayedMnemonicIndex(i3);
    }

    public void setMnemonicAt(int i2, int i3) {
        checkIndex(i2);
        this.pages.get(i2).setMnemonic(i3);
        firePropertyChange("mnemonicAt", (Object) null, (Object) null);
    }

    public int indexOfTab(String str) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTitleAt(i2).equals(str == null ? "" : str)) {
                return i2;
            }
        }
        return -1;
    }

    public int indexOfTab(Icon icon) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            Icon iconAt = getIconAt(i2);
            if ((iconAt != null && iconAt.equals(icon)) || (iconAt == null && iconAt == icon)) {
                return i2;
            }
        }
        return -1;
    }

    public int indexOfComponent(Component component) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            Component componentAt = getComponentAt(i2);
            if ((componentAt != null && componentAt.equals(component)) || (componentAt == null && componentAt == component)) {
                return i2;
            }
        }
        return -1;
    }

    public int indexAtLocation(int i2, int i3) {
        if (this.ui != null) {
            return ((TabbedPaneUI) this.ui).tabForCoordinate(this, i2, i3);
        }
        return -1;
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        int iTabForCoordinate;
        if (this.ui != null && (iTabForCoordinate = ((TabbedPaneUI) this.ui).tabForCoordinate(this, mouseEvent.getX(), mouseEvent.getY())) != -1) {
            return this.pages.get(iTabForCoordinate).tip;
        }
        return super.getToolTipText(mouseEvent);
    }

    private void checkIndex(int i2) {
        if (i2 < 0 || i2 >= this.pages.size()) {
            throw new IndexOutOfBoundsException("Index: " + i2 + ", Tab count: " + this.pages.size());
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

    @Override // javax.swing.JComponent
    void compWriteObjectNotify() {
        super.compWriteObjectNotify();
        if (getToolTipText() == null && this.haveRegistered) {
            ToolTipManager.sharedInstance().unregisterComponent(this);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.ui != null && getUIClassID().equals(uiClassID)) {
            this.ui.installUI(this);
        }
        if (getToolTipText() == null && this.haveRegistered) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        if (this.tabPlacement == 1) {
            str = "TOP";
        } else if (this.tabPlacement == 3) {
            str = "BOTTOM";
        } else if (this.tabPlacement == 2) {
            str = "LEFT";
        } else if (this.tabPlacement == 4) {
            str = "RIGHT";
        } else {
            str = "";
        }
        return super.paramString() + ",haveRegistered=" + (this.haveRegistered ? "true" : "false") + ",tabPlacement=" + str;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTabbedPane();
            int tabCount = getTabCount();
            for (int i2 = 0; i2 < tabCount; i2++) {
                this.pages.get(i2).initAccessibleContext();
            }
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JTabbedPane$AccessibleJTabbedPane.class */
    protected class AccessibleJTabbedPane extends JComponent.AccessibleJComponent implements AccessibleSelection, ChangeListener {
        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName != null) {
                return this.accessibleName;
            }
            String str = (String) JTabbedPane.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            if (str != null) {
                return str;
            }
            int selectedIndex = JTabbedPane.this.getSelectedIndex();
            if (selectedIndex >= 0) {
                return ((Page) JTabbedPane.this.pages.get(selectedIndex)).getAccessibleName();
            }
            return super.getAccessibleName();
        }

        public AccessibleJTabbedPane() {
            super();
            JTabbedPane.this.model.addChangeListener(this);
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, null, changeEvent.getSource());
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PAGE_TAB_LIST;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return JTabbedPane.this.getTabCount();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (i2 >= 0 && i2 < JTabbedPane.this.getTabCount()) {
                return (Accessible) JTabbedPane.this.pages.get(i2);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            int iTabForCoordinate = ((TabbedPaneUI) JTabbedPane.this.ui).tabForCoordinate(JTabbedPane.this, point.f12370x, point.f12371y);
            if (iTabForCoordinate == -1) {
                iTabForCoordinate = JTabbedPane.this.getSelectedIndex();
            }
            return getAccessibleChild(iTabForCoordinate);
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            return 1;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            int selectedIndex = JTabbedPane.this.getSelectedIndex();
            if (selectedIndex != -1) {
                return (Accessible) JTabbedPane.this.pages.get(selectedIndex);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            return i2 == JTabbedPane.this.getSelectedIndex();
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            JTabbedPane.this.setSelectedIndex(i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
        }
    }

    /* loaded from: rt.jar:javax/swing/JTabbedPane$Page.class */
    private class Page extends AccessibleContext implements Serializable, Accessible, AccessibleComponent {
        String title;
        Color background;
        Color foreground;
        Icon icon;
        Icon disabledIcon;
        JTabbedPane parent;
        Component component;
        String tip;
        boolean needsUIUpdate;
        Component tabComponent;
        boolean enabled = true;
        int mnemonic = -1;
        int mnemonicIndex = -1;

        Page(JTabbedPane jTabbedPane, String str, Icon icon, Icon icon2, Component component, String str2) {
            this.title = str;
            this.icon = icon;
            this.disabledIcon = icon2;
            this.parent = jTabbedPane;
            setAccessibleParent(jTabbedPane);
            this.component = component;
            this.tip = str2;
            initAccessibleContext();
        }

        void initAccessibleContext() {
            AccessibleContext accessibleContext;
            if (JTabbedPane.this.accessibleContext != null && (this.component instanceof Accessible) && (accessibleContext = this.component.getAccessibleContext()) != null) {
                accessibleContext.setAccessibleParent(this);
            }
        }

        void setMnemonic(int i2) {
            this.mnemonic = i2;
            updateDisplayedMnemonicIndex();
        }

        int getMnemonic() {
            return this.mnemonic;
        }

        void setDisplayedMnemonicIndex(int i2) {
            if (this.mnemonicIndex != i2) {
                if (i2 != -1 && (this.title == null || i2 < 0 || i2 >= this.title.length())) {
                    throw new IllegalArgumentException("Invalid mnemonic index: " + i2);
                }
                this.mnemonicIndex = i2;
                JTabbedPane.this.firePropertyChange("displayedMnemonicIndexAt", (Object) null, (Object) null);
            }
        }

        int getDisplayedMnemonicIndex() {
            return this.mnemonicIndex;
        }

        void updateDisplayedMnemonicIndex() {
            setDisplayedMnemonicIndex(SwingUtilities.findDisplayedMnemonicIndex(this.title, this.mnemonic));
        }

        @Override // javax.accessibility.Accessible
        public AccessibleContext getAccessibleContext() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleName() {
            if (this.accessibleName != null) {
                return this.accessibleName;
            }
            if (this.title != null) {
                return this.title;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public String getAccessibleDescription() {
            if (this.accessibleDescription != null) {
                return this.accessibleDescription;
            }
            if (this.tip != null) {
                return this.tip;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PAGE_TAB;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = this.parent.getAccessibleContext().getAccessibleStateSet();
            accessibleStateSet.add(AccessibleState.SELECTABLE);
            if (this.parent.indexOfTab(this.title) == this.parent.getSelectedIndex()) {
                accessibleStateSet.add(AccessibleState.SELECTED);
            }
            return accessibleStateSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleIndexInParent() {
            return this.parent.indexOfTab(this.title);
        }

        @Override // javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            if (this.component instanceof Accessible) {
                return 1;
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (this.component instanceof Accessible) {
                return (Accessible) this.component;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public Locale getLocale() {
            return this.parent.getLocale();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleComponent getAccessibleComponent() {
            return this;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getBackground() {
            return this.background != null ? this.background : this.parent.getBackground();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBackground(Color color) {
            this.background = color;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Color getForeground() {
            return this.foreground != null ? this.foreground : this.parent.getForeground();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setForeground(Color color) {
            this.foreground = color;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Cursor getCursor() {
            return this.parent.getCursor();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setCursor(Cursor cursor) {
            this.parent.setCursor(cursor);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Font getFont() {
            return this.parent.getFont();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setFont(Font font) {
            this.parent.setFont(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public FontMetrics getFontMetrics(Font font) {
            return this.parent.getFontMetrics(font);
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isEnabled() {
            return this.enabled;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setEnabled(boolean z2) {
            this.enabled = z2;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isVisible() {
            return this.parent.isVisible();
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setVisible(boolean z2) {
            this.parent.setVisible(z2);
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isShowing() {
            return this.parent.isShowing();
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean contains(Point point) {
            return getBounds().contains(point);
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocationOnScreen() {
            Point locationOnScreen = this.parent.getLocationOnScreen();
            Point location = getLocation();
            location.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
            return location;
        }

        @Override // javax.accessibility.AccessibleComponent
        public Point getLocation() {
            Rectangle bounds = getBounds();
            return new Point(bounds.f12372x, bounds.f12373y);
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setLocation(Point point) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Rectangle getBounds() {
            return this.parent.getUI().getTabBounds(this.parent, this.parent.indexOfTab(this.title));
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setBounds(Rectangle rectangle) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Dimension getSize() {
            Rectangle bounds = getBounds();
            return new Dimension(bounds.width, bounds.height);
        }

        @Override // javax.accessibility.AccessibleComponent
        public void setSize(Dimension dimension) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            if (this.component instanceof Accessible) {
                return (Accessible) this.component;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleComponent
        public boolean isFocusTraversable() {
            return false;
        }

        @Override // javax.accessibility.AccessibleComponent
        public void requestFocus() {
        }

        @Override // javax.accessibility.AccessibleComponent
        public void addFocusListener(FocusListener focusListener) {
        }

        @Override // javax.accessibility.AccessibleComponent
        public void removeFocusListener(FocusListener focusListener) {
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleIcon[] getAccessibleIcon() {
            AccessibleIcon accessibleIcon = null;
            if (this.enabled && (this.icon instanceof ImageIcon)) {
                accessibleIcon = (AccessibleIcon) ((ImageIcon) this.icon).getAccessibleContext();
            } else if (!this.enabled && (this.disabledIcon instanceof ImageIcon)) {
                accessibleIcon = (AccessibleIcon) ((ImageIcon) this.disabledIcon).getAccessibleContext();
            }
            if (accessibleIcon != null) {
                return new AccessibleIcon[]{accessibleIcon};
            }
            return null;
        }
    }

    public void setTabComponentAt(int i2, Component component) {
        if (component != null && indexOfComponent(component) != -1) {
            throw new IllegalArgumentException("Component is already added to this JTabbedPane");
        }
        if (component != getTabComponentAt(i2)) {
            int iIndexOfTabComponent = indexOfTabComponent(component);
            if (iIndexOfTabComponent != -1) {
                setTabComponentAt(iIndexOfTabComponent, null);
            }
            this.pages.get(i2).tabComponent = component;
            firePropertyChange("indexForTabComponent", -1, i2);
        }
    }

    public Component getTabComponentAt(int i2) {
        return this.pages.get(i2).tabComponent;
    }

    public int indexOfTabComponent(Component component) {
        for (int i2 = 0; i2 < getTabCount(); i2++) {
            if (getTabComponentAt(i2) == component) {
                return i2;
            }
        }
        return -1;
    }
}
