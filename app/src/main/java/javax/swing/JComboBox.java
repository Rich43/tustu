package javax.swing;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.IllegalComponentStateException;
import java.awt.ItemSelectable;
import java.awt.MenuContainer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;
import javax.accessibility.AccessibleIcon;
import javax.accessibility.AccessibleRelationSet;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleTable;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.ComboPopup;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:javax/swing/JComboBox.class */
public class JComboBox<E> extends JComponent implements ItemSelectable, ListDataListener, ActionListener, Accessible {
    private static final String uiClassID = "ComboBoxUI";
    protected ComboBoxModel<E> dataModel;
    protected ListCellRenderer<? super E> renderer;
    protected ComboBoxEditor editor;
    private E prototypeDisplayValue;
    private Action action;
    private PropertyChangeListener actionPropertyChangeListener;
    protected int maximumRowCount = 8;
    protected boolean isEditable = false;
    protected KeySelectionManager keySelectionManager = null;
    protected String actionCommand = "comboBoxChanged";
    protected boolean lightWeightPopupEnabled = JPopupMenu.getDefaultLightWeightPopupEnabled();
    protected Object selectedItemReminder = null;
    private boolean firingActionEvent = false;
    private boolean selectingItem = false;

    /* loaded from: rt.jar:javax/swing/JComboBox$KeySelectionManager.class */
    public interface KeySelectionManager {
        int selectionForKey(char c2, ComboBoxModel comboBoxModel);
    }

    public JComboBox(ComboBoxModel<E> comboBoxModel) {
        setModel(comboBoxModel);
        init();
    }

    public JComboBox(E[] eArr) {
        setModel(new DefaultComboBoxModel(eArr));
        init();
    }

    public JComboBox(Vector<E> vector) {
        setModel(new DefaultComboBoxModel(vector));
        init();
    }

    public JComboBox() {
        setModel(new DefaultComboBoxModel());
        init();
    }

    private void init() {
        installAncestorListener();
        setUIProperty("opaque", true);
        updateUI();
    }

    protected void installAncestorListener() {
        addAncestorListener(new AncestorListener() { // from class: javax.swing.JComboBox.1
            @Override // javax.swing.event.AncestorListener
            public void ancestorAdded(AncestorEvent ancestorEvent) {
                JComboBox.this.hidePopup();
            }

            @Override // javax.swing.event.AncestorListener
            public void ancestorRemoved(AncestorEvent ancestorEvent) {
                JComboBox.this.hidePopup();
            }

            @Override // javax.swing.event.AncestorListener
            public void ancestorMoved(AncestorEvent ancestorEvent) {
                if (ancestorEvent.getSource() != JComboBox.this) {
                    JComboBox.this.hidePopup();
                }
            }
        });
    }

    public void setUI(ComboBoxUI comboBoxUI) {
        super.setUI((ComponentUI) comboBoxUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ComboBoxUI) UIManager.getUI(this));
        ListCellRenderer<? super E> renderer = getRenderer();
        if (renderer instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component) renderer);
        }
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public ComboBoxUI getUI() {
        return (ComboBoxUI) this.ui;
    }

    public void setModel(ComboBoxModel<E> comboBoxModel) {
        ComboBoxModel<E> comboBoxModel2 = this.dataModel;
        if (comboBoxModel2 != null) {
            comboBoxModel2.removeListDataListener(this);
        }
        this.dataModel = comboBoxModel;
        this.dataModel.addListDataListener(this);
        this.selectedItemReminder = this.dataModel.getSelectedItem();
        firePropertyChange("model", comboBoxModel2, this.dataModel);
    }

    public ComboBoxModel<E> getModel() {
        return this.dataModel;
    }

    public void setLightWeightPopupEnabled(boolean z2) {
        boolean z3 = this.lightWeightPopupEnabled;
        this.lightWeightPopupEnabled = z2;
        firePropertyChange("lightWeightPopupEnabled", z3, this.lightWeightPopupEnabled);
    }

    public boolean isLightWeightPopupEnabled() {
        return this.lightWeightPopupEnabled;
    }

    public void setEditable(boolean z2) {
        boolean z3 = this.isEditable;
        this.isEditable = z2;
        firePropertyChange(JTree.EDITABLE_PROPERTY, z3, this.isEditable);
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public void setMaximumRowCount(int i2) {
        int i3 = this.maximumRowCount;
        this.maximumRowCount = i2;
        firePropertyChange("maximumRowCount", i3, this.maximumRowCount);
    }

    public int getMaximumRowCount() {
        return this.maximumRowCount;
    }

    public void setRenderer(ListCellRenderer<? super E> listCellRenderer) {
        ListCellRenderer<? super E> listCellRenderer2 = this.renderer;
        this.renderer = listCellRenderer;
        firePropertyChange("renderer", listCellRenderer2, this.renderer);
        invalidate();
    }

    public ListCellRenderer<? super E> getRenderer() {
        return this.renderer;
    }

    public void setEditor(ComboBoxEditor comboBoxEditor) {
        ComboBoxEditor comboBoxEditor2 = this.editor;
        if (this.editor != null) {
            this.editor.removeActionListener(this);
        }
        this.editor = comboBoxEditor;
        if (this.editor != null) {
            this.editor.addActionListener(this);
        }
        firePropertyChange("editor", comboBoxEditor2, this.editor);
    }

    public ComboBoxEditor getEditor() {
        return this.editor;
    }

    public void setSelectedItem(Object obj) {
        Object obj2 = this.selectedItemReminder;
        Object obj3 = obj;
        if (obj2 == null || !obj2.equals(obj)) {
            if (obj != null && !isEditable()) {
                boolean z2 = false;
                int i2 = 0;
                while (true) {
                    if (i2 >= this.dataModel.getSize()) {
                        break;
                    }
                    E elementAt = this.dataModel.getElementAt(i2);
                    if (!obj.equals(elementAt)) {
                        i2++;
                    } else {
                        z2 = true;
                        obj3 = elementAt;
                        break;
                    }
                }
                if (!z2) {
                    return;
                } else {
                    getEditor().setItem(obj);
                }
            }
            this.selectingItem = true;
            this.dataModel.setSelectedItem(obj3);
            this.selectingItem = false;
            if (this.selectedItemReminder != this.dataModel.getSelectedItem()) {
                selectedItemChanged();
            }
        }
        fireActionEvent();
    }

    public Object getSelectedItem() {
        return this.dataModel.getSelectedItem();
    }

    public void setSelectedIndex(int i2) {
        int size = this.dataModel.getSize();
        if (i2 == -1) {
            setSelectedItem(null);
        } else {
            if (i2 < -1 || i2 >= size) {
                throw new IllegalArgumentException("setSelectedIndex: " + i2 + " out of bounds");
            }
            setSelectedItem(this.dataModel.getElementAt(i2));
        }
    }

    @Transient
    public int getSelectedIndex() {
        Object selectedItem = this.dataModel.getSelectedItem();
        int size = this.dataModel.getSize();
        for (int i2 = 0; i2 < size; i2++) {
            E elementAt = this.dataModel.getElementAt(i2);
            if (elementAt != null && elementAt.equals(selectedItem)) {
                return i2;
            }
        }
        return -1;
    }

    public E getPrototypeDisplayValue() {
        return this.prototypeDisplayValue;
    }

    public void setPrototypeDisplayValue(E e2) {
        E e3 = this.prototypeDisplayValue;
        this.prototypeDisplayValue = e2;
        firePropertyChange("prototypeDisplayValue", e3, e2);
    }

    public void addItem(E e2) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) this.dataModel).addElement(e2);
    }

    public void insertItemAt(E e2, int i2) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) this.dataModel).insertElementAt(e2, i2);
    }

    public void removeItem(Object obj) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) this.dataModel).removeElement(obj);
    }

    public void removeItemAt(int i2) {
        checkMutableComboBoxModel();
        ((MutableComboBoxModel) this.dataModel).removeElementAt(i2);
    }

    public void removeAllItems() {
        checkMutableComboBoxModel();
        MutableComboBoxModel mutableComboBoxModel = (MutableComboBoxModel) this.dataModel;
        int size = mutableComboBoxModel.getSize();
        if (mutableComboBoxModel instanceof DefaultComboBoxModel) {
            ((DefaultComboBoxModel) mutableComboBoxModel).removeAllElements();
        } else {
            for (int i2 = 0; i2 < size; i2++) {
                mutableComboBoxModel.removeElement(mutableComboBoxModel.getElementAt(0));
            }
        }
        this.selectedItemReminder = null;
        if (isEditable()) {
            this.editor.setItem(null);
        }
    }

    void checkMutableComboBoxModel() {
        if (!(this.dataModel instanceof MutableComboBoxModel)) {
            throw new RuntimeException("Cannot use this method with a non-Mutable data model.");
        }
    }

    public void showPopup() {
        setPopupVisible(true);
    }

    public void hidePopup() {
        setPopupVisible(false);
    }

    public void setPopupVisible(boolean z2) {
        getUI().setPopupVisible(this, z2);
    }

    public boolean isPopupVisible() {
        return getUI().isPopupVisible(this);
    }

    @Override // java.awt.ItemSelectable
    public void addItemListener(ItemListener itemListener) {
        this.listenerList.add(ItemListener.class, itemListener);
    }

    @Override // java.awt.ItemSelectable
    public void removeItemListener(ItemListener itemListener) {
        this.listenerList.remove(ItemListener.class, itemListener);
    }

    public ItemListener[] getItemListeners() {
        return (ItemListener[]) this.listenerList.getListeners(ItemListener.class);
    }

    public void addActionListener(ActionListener actionListener) {
        this.listenerList.add(ActionListener.class, actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        if (actionListener != null && getAction() == actionListener) {
            setAction(null);
        } else {
            this.listenerList.remove(ActionListener.class, actionListener);
        }
    }

    public ActionListener[] getActionListeners() {
        return (ActionListener[]) this.listenerList.getListeners(ActionListener.class);
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

    public void firePopupMenuWillBecomeVisible() {
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

    public void firePopupMenuWillBecomeInvisible() {
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

    public void firePopupMenuCanceled() {
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

    public void setActionCommand(String str) {
        this.actionCommand = str;
    }

    public String getActionCommand() {
        return this.actionCommand;
    }

    public void setAction(Action action) {
        Action action2 = getAction();
        if (this.action == null || !this.action.equals(action)) {
            this.action = action;
            if (action2 != null) {
                removeActionListener(action2);
                action2.removePropertyChangeListener(this.actionPropertyChangeListener);
                this.actionPropertyChangeListener = null;
            }
            configurePropertiesFromAction(this.action);
            if (this.action != null) {
                if (!isListener(ActionListener.class, this.action)) {
                    addActionListener(this.action);
                }
                this.actionPropertyChangeListener = createActionPropertyChangeListener(this.action);
                this.action.addPropertyChangeListener(this.actionPropertyChangeListener);
            }
            firePropertyChange("action", action2, this.action);
        }
    }

    private boolean isListener(Class cls, ActionListener actionListener) {
        boolean z2 = false;
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == cls && listenerList[length + 1] == actionListener) {
                z2 = true;
            }
        }
        return z2;
    }

    public Action getAction() {
        return this.action;
    }

    protected void configurePropertiesFromAction(Action action) {
        AbstractAction.setEnabledFromAction(this, action);
        AbstractAction.setToolTipTextFromAction(this, action);
        setActionCommandFromAction(action);
    }

    protected PropertyChangeListener createActionPropertyChangeListener(Action action) {
        return new ComboBoxActionPropertyChangeListener(this, action);
    }

    protected void actionPropertyChanged(Action action, String str) {
        if (str == Action.ACTION_COMMAND_KEY) {
            setActionCommandFromAction(action);
        } else if (str == Enabled.NAME) {
            AbstractAction.setEnabledFromAction(this, action);
        } else if (Action.SHORT_DESCRIPTION == str) {
            AbstractAction.setToolTipTextFromAction(this, action);
        }
    }

    private void setActionCommandFromAction(Action action) {
        setActionCommand(action != null ? (String) action.getValue(Action.ACTION_COMMAND_KEY) : null);
    }

    /* loaded from: rt.jar:javax/swing/JComboBox$ComboBoxActionPropertyChangeListener.class */
    private static class ComboBoxActionPropertyChangeListener extends ActionPropertyChangeListener<JComboBox<?>> {
        ComboBoxActionPropertyChangeListener(JComboBox<?> jComboBox, Action action) {
            super(jComboBox, action);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // javax.swing.ActionPropertyChangeListener
        public void actionPropertyChanged(JComboBox<?> jComboBox, Action action, PropertyChangeEvent propertyChangeEvent) {
            if (AbstractAction.shouldReconfigure(propertyChangeEvent)) {
                jComboBox.configurePropertiesFromAction(action);
            } else {
                jComboBox.actionPropertyChanged(action, propertyChangeEvent.getPropertyName());
            }
        }
    }

    protected void fireItemStateChanged(ItemEvent itemEvent) {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ItemListener.class) {
                ((ItemListener) listenerList[length + 1]).itemStateChanged(itemEvent);
            }
        }
    }

    protected void fireActionEvent() {
        if (!this.firingActionEvent) {
            this.firingActionEvent = true;
            ActionEvent actionEvent = null;
            Object[] listenerList = this.listenerList.getListenerList();
            long mostRecentEventTime = EventQueue.getMostRecentEventTime();
            int modifiers = 0;
            AWTEvent currentEvent = EventQueue.getCurrentEvent();
            if (currentEvent instanceof InputEvent) {
                modifiers = ((InputEvent) currentEvent).getModifiers();
            } else if (currentEvent instanceof ActionEvent) {
                modifiers = ((ActionEvent) currentEvent).getModifiers();
            }
            for (int length = listenerList.length - 2; length >= 0; length -= 2) {
                if (listenerList[length] == ActionListener.class) {
                    if (actionEvent == null) {
                        actionEvent = new ActionEvent(this, 1001, getActionCommand(), mostRecentEventTime, modifiers);
                    }
                    ((ActionListener) listenerList[length + 1]).actionPerformed(actionEvent);
                }
            }
            this.firingActionEvent = false;
        }
    }

    protected void selectedItemChanged() {
        if (this.selectedItemReminder != null) {
            fireItemStateChanged(new ItemEvent(this, 701, this.selectedItemReminder, 2));
        }
        this.selectedItemReminder = this.dataModel.getSelectedItem();
        if (this.selectedItemReminder != null) {
            fireItemStateChanged(new ItemEvent(this, 701, this.selectedItemReminder, 1));
        }
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        Object selectedItem = getSelectedItem();
        if (selectedItem == null) {
            return new Object[0];
        }
        return new Object[]{selectedItem};
    }

    @Override // java.awt.event.ActionListener
    public void actionPerformed(ActionEvent actionEvent) {
        setPopupVisible(false);
        getModel().setSelectedItem(getEditor().getItem());
        String actionCommand = getActionCommand();
        setActionCommand("comboBoxEdited");
        fireActionEvent();
        setActionCommand(actionCommand);
    }

    @Override // javax.swing.event.ListDataListener
    public void contentsChanged(ListDataEvent listDataEvent) {
        Object obj = this.selectedItemReminder;
        Object selectedItem = this.dataModel.getSelectedItem();
        if (obj == null || !obj.equals(selectedItem)) {
            selectedItemChanged();
            if (!this.selectingItem) {
                fireActionEvent();
            }
        }
    }

    @Override // javax.swing.event.ListDataListener
    public void intervalAdded(ListDataEvent listDataEvent) {
        if (this.selectedItemReminder != this.dataModel.getSelectedItem()) {
            selectedItemChanged();
        }
    }

    @Override // javax.swing.event.ListDataListener
    public void intervalRemoved(ListDataEvent listDataEvent) {
        contentsChanged(listDataEvent);
    }

    public boolean selectWithKeyChar(char c2) {
        if (this.keySelectionManager == null) {
            this.keySelectionManager = createDefaultKeySelectionManager();
        }
        int iSelectionForKey = this.keySelectionManager.selectionForKey(c2, getModel());
        if (iSelectionForKey != -1) {
            setSelectedIndex(iSelectionForKey);
            return true;
        }
        return false;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        firePropertyChange(Enabled.NAME, !isEnabled(), isEnabled());
    }

    public void configureEditor(ComboBoxEditor comboBoxEditor, Object obj) {
        comboBoxEditor.setItem(obj);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void processKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == 9) {
            hidePopup();
        }
        super.processKeyEvent(keyEvent);
    }

    @Override // javax.swing.JComponent
    protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
        if (super.processKeyBinding(keyStroke, keyEvent, i2, z2)) {
            return true;
        }
        if (!isEditable() || i2 != 0 || getEditor() == null || !Boolean.TRUE.equals(getClientProperty("JComboBox.isTableCellEditor"))) {
            return false;
        }
        Component editorComponent = getEditor().getEditorComponent();
        if (editorComponent instanceof JComponent) {
            return ((JComponent) editorComponent).processKeyBinding(keyStroke, keyEvent, 0, z2);
        }
        return false;
    }

    public void setKeySelectionManager(KeySelectionManager keySelectionManager) {
        this.keySelectionManager = keySelectionManager;
    }

    public KeySelectionManager getKeySelectionManager() {
        return this.keySelectionManager;
    }

    public int getItemCount() {
        return this.dataModel.getSize();
    }

    public E getItemAt(int i2) {
        return this.dataModel.getElementAt(i2);
    }

    protected KeySelectionManager createDefaultKeySelectionManager() {
        return new DefaultKeySelectionManager();
    }

    /* loaded from: rt.jar:javax/swing/JComboBox$DefaultKeySelectionManager.class */
    class DefaultKeySelectionManager implements KeySelectionManager, Serializable {
        DefaultKeySelectionManager() {
        }

        @Override // javax.swing.JComboBox.KeySelectionManager
        public int selectionForKey(char c2, ComboBoxModel comboBoxModel) {
            int i2 = -1;
            Object selectedItem = comboBoxModel.getSelectedItem();
            if (selectedItem != null) {
                int i3 = 0;
                int size = comboBoxModel.getSize();
                while (true) {
                    if (i3 >= size) {
                        break;
                    }
                    if (selectedItem != comboBoxModel.getElementAt(i3)) {
                        i3++;
                    } else {
                        i2 = i3;
                        break;
                    }
                }
            }
            char cCharAt = ("" + c2).toLowerCase().charAt(0);
            int i4 = i2 + 1;
            int size2 = comboBoxModel.getSize();
            for (int i5 = i4; i5 < size2; i5++) {
                E elementAt = comboBoxModel.getElementAt(i5);
                if (elementAt != null && elementAt.toString() != null) {
                    String lowerCase = elementAt.toString().toLowerCase();
                    if (lowerCase.length() > 0 && lowerCase.charAt(0) == cCharAt) {
                        return i5;
                    }
                }
            }
            for (int i6 = 0; i6 < i4; i6++) {
                E elementAt2 = comboBoxModel.getElementAt(i6);
                if (elementAt2 != null && elementAt2.toString() != null) {
                    String lowerCase2 = elementAt2.toString().toLowerCase();
                    if (lowerCase2.length() > 0 && lowerCase2.charAt(0) == cCharAt) {
                        return i6;
                    }
                }
            }
            return -1;
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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",isEditable=" + (this.isEditable ? "true" : "false") + ",lightWeightPopupEnabled=" + (this.lightWeightPopupEnabled ? "true" : "false") + ",maximumRowCount=" + this.maximumRowCount + ",selectedItemReminder=" + (this.selectedItemReminder != null ? this.selectedItemReminder.toString() : "");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJComboBox();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JComboBox$AccessibleJComboBox.class */
    protected class AccessibleJComboBox extends JComponent.AccessibleJComponent implements AccessibleAction, AccessibleSelection {
        private JList popupList;
        private Accessible previousSelectedAccessible;
        private JComboBox<E>.AccessibleJComboBox.EditorAccessibleContext editorAccessibleContext;

        public AccessibleJComboBox() {
            super();
            this.previousSelectedAccessible = null;
            this.editorAccessibleContext = null;
            JComboBox.this.addPropertyChangeListener(new AccessibleJComboBoxPropertyChangeListener());
            setEditorNameAndDescription();
            Accessible accessibleChild = JComboBox.this.getUI().getAccessibleChild(JComboBox.this, 0);
            if (accessibleChild instanceof ComboPopup) {
                this.popupList = ((ComboPopup) accessibleChild).getList();
                this.popupList.addListSelectionListener(new AccessibleJComboBoxListSelectionListener());
            }
            JComboBox.this.addPopupMenuListener(new AccessibleJComboBoxPopupMenuListener());
        }

        /* loaded from: rt.jar:javax/swing/JComboBox$AccessibleJComboBox$AccessibleJComboBoxPropertyChangeListener.class */
        private class AccessibleJComboBoxPropertyChangeListener implements PropertyChangeListener {
            private AccessibleJComboBoxPropertyChangeListener() {
            }

            @Override // java.beans.PropertyChangeListener
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if (propertyChangeEvent.getPropertyName() == "editor") {
                    AccessibleJComboBox.this.setEditorNameAndDescription();
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void setEditorNameAndDescription() {
            AccessibleContext accessibleContext;
            ComboBoxEditor editor = JComboBox.this.getEditor();
            if (editor != null) {
                Component editorComponent = editor.getEditorComponent();
                if ((editorComponent instanceof Accessible) && (accessibleContext = editorComponent.getAccessibleContext()) != null) {
                    accessibleContext.setAccessibleName(getAccessibleName());
                    accessibleContext.setAccessibleDescription(getAccessibleDescription());
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/JComboBox$AccessibleJComboBox$AccessibleJComboBoxPopupMenuListener.class */
        private class AccessibleJComboBoxPopupMenuListener implements PopupMenuListener {
            private AccessibleJComboBoxPopupMenuListener() {
            }

            @Override // javax.swing.event.PopupMenuListener
            public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
                int selectedIndex;
                if (AccessibleJComboBox.this.popupList != null && (selectedIndex = AccessibleJComboBox.this.popupList.getSelectedIndex()) >= 0) {
                    AccessibleJComboBox.this.previousSelectedAccessible = AccessibleJComboBox.this.popupList.getAccessibleContext().getAccessibleChild(selectedIndex);
                }
            }

            @Override // javax.swing.event.PopupMenuListener
            public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
            }

            @Override // javax.swing.event.PopupMenuListener
            public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
            }
        }

        /* loaded from: rt.jar:javax/swing/JComboBox$AccessibleJComboBox$AccessibleJComboBoxListSelectionListener.class */
        private class AccessibleJComboBoxListSelectionListener implements ListSelectionListener {
            private AccessibleJComboBoxListSelectionListener() {
            }

            @Override // javax.swing.event.ListSelectionListener
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                int selectedIndex;
                Accessible accessibleChild;
                if (AccessibleJComboBox.this.popupList != null && (selectedIndex = AccessibleJComboBox.this.popupList.getSelectedIndex()) >= 0 && (accessibleChild = AccessibleJComboBox.this.popupList.getAccessibleContext().getAccessibleChild(selectedIndex)) != null) {
                    if (AccessibleJComboBox.this.previousSelectedAccessible != null) {
                        AccessibleJComboBox.this.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, new PropertyChangeEvent(AccessibleJComboBox.this.previousSelectedAccessible, AccessibleContext.ACCESSIBLE_STATE_PROPERTY, AccessibleState.FOCUSED, null));
                    }
                    AccessibleJComboBox.this.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, new PropertyChangeEvent(accessibleChild, AccessibleContext.ACCESSIBLE_STATE_PROPERTY, null, AccessibleState.FOCUSED));
                    AccessibleJComboBox.this.firePropertyChange(AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY, AccessibleJComboBox.this.previousSelectedAccessible, accessibleChild);
                    AccessibleJComboBox.this.previousSelectedAccessible = accessibleChild;
                }
            }
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            if (JComboBox.this.ui != null) {
                return JComboBox.this.ui.getAccessibleChildrenCount(JComboBox.this);
            }
            return super.getAccessibleChildrenCount();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (JComboBox.this.ui != null) {
                return JComboBox.this.ui.getAccessibleChild(JComboBox.this, i2);
            }
            return super.getAccessibleChild(i2);
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.COMBO_BOX;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (accessibleStateSet == null) {
                accessibleStateSet = new AccessibleStateSet();
            }
            if (JComboBox.this.isPopupVisible()) {
                accessibleStateSet.add(AccessibleState.EXPANDED);
            } else {
                accessibleStateSet.add(AccessibleState.COLLAPSED);
            }
            return accessibleStateSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            if (i2 == 0) {
                return UIManager.getString("ComboBox.togglePopupText");
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return 1;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            if (i2 == 0) {
                JComboBox.this.setPopupVisible(!JComboBox.this.isPopupVisible());
                return true;
            }
            return false;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            if (JComboBox.this.getSelectedItem() != null) {
                return 1;
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            AccessibleContext accessibleContext;
            AccessibleSelection accessibleSelection;
            Accessible accessibleChild = JComboBox.this.getUI().getAccessibleChild(JComboBox.this, 0);
            if (accessibleChild != null && (accessibleChild instanceof ComboPopup) && (accessibleContext = ((ComboPopup) accessibleChild).getList().getAccessibleContext()) != null && (accessibleSelection = accessibleContext.getAccessibleSelection()) != null) {
                return accessibleSelection.getAccessibleSelection(i2);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            return JComboBox.this.getSelectedIndex() == i2;
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            clearAccessibleSelection();
            JComboBox.this.setSelectedIndex(i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            if (JComboBox.this.getSelectedIndex() == i2) {
                clearAccessibleSelection();
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            JComboBox.this.setSelectedIndex(-1);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
        }

        /* loaded from: rt.jar:javax/swing/JComboBox$AccessibleJComboBox$AccessibleEditor.class */
        private class AccessibleEditor implements Accessible {
            private AccessibleEditor() {
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                if (AccessibleJComboBox.this.editorAccessibleContext == null) {
                    MenuContainer editorComponent = JComboBox.this.getEditor().getEditorComponent();
                    if (editorComponent instanceof Accessible) {
                        AccessibleJComboBox.this.editorAccessibleContext = AccessibleJComboBox.this.new EditorAccessibleContext((Accessible) editorComponent);
                    }
                }
                return AccessibleJComboBox.this.editorAccessibleContext;
            }
        }

        /* loaded from: rt.jar:javax/swing/JComboBox$AccessibleJComboBox$EditorAccessibleContext.class */
        private class EditorAccessibleContext extends AccessibleContext {

            /* renamed from: ac, reason: collision with root package name */
            private AccessibleContext f12800ac;

            private EditorAccessibleContext() {
            }

            EditorAccessibleContext(Accessible accessible) {
                this.f12800ac = accessible.getAccessibleContext();
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                return this.f12800ac.getAccessibleName();
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleName(String str) {
                this.f12800ac.setAccessibleName(str);
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                return this.f12800ac.getAccessibleDescription();
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleDescription(String str) {
                this.f12800ac.setAccessibleDescription(str);
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return this.f12800ac.getAccessibleRole();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleStateSet getAccessibleStateSet() {
                return this.f12800ac.getAccessibleStateSet();
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleParent() {
                return this.f12800ac.getAccessibleParent();
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleParent(Accessible accessible) {
                this.f12800ac.setAccessibleParent(accessible);
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                return JComboBox.this.getSelectedIndex();
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                return this.f12800ac.getAccessibleChildrenCount();
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                return this.f12800ac.getAccessibleChild(i2);
            }

            @Override // javax.accessibility.AccessibleContext
            public Locale getLocale() throws IllegalComponentStateException {
                return this.f12800ac.getLocale();
            }

            @Override // javax.accessibility.AccessibleContext
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                this.f12800ac.addPropertyChangeListener(propertyChangeListener);
            }

            @Override // javax.accessibility.AccessibleContext
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                this.f12800ac.removePropertyChangeListener(propertyChangeListener);
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleAction getAccessibleAction() {
                return this.f12800ac.getAccessibleAction();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleComponent getAccessibleComponent() {
                return this.f12800ac.getAccessibleComponent();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleSelection getAccessibleSelection() {
                return this.f12800ac.getAccessibleSelection();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleText getAccessibleText() {
                return this.f12800ac.getAccessibleText();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleEditableText getAccessibleEditableText() {
                return this.f12800ac.getAccessibleEditableText();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleValue getAccessibleValue() {
                return this.f12800ac.getAccessibleValue();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleIcon[] getAccessibleIcon() {
                return this.f12800ac.getAccessibleIcon();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRelationSet getAccessibleRelationSet() {
                return this.f12800ac.getAccessibleRelationSet();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleTable getAccessibleTable() {
                return this.f12800ac.getAccessibleTable();
            }

            @Override // javax.accessibility.AccessibleContext
            public void firePropertyChange(String str, Object obj, Object obj2) {
                this.f12800ac.firePropertyChange(str, obj, obj2);
            }
        }
    }
}
