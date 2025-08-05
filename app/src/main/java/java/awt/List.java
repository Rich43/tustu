package java.awt;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.peer.ListPeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

/* loaded from: rt.jar:java/awt/List.class */
public class List extends Component implements ItemSelectable, Accessible {
    Vector<String> items;
    int rows;
    boolean multipleMode;
    int[] selected;
    int visibleIndex;
    transient ActionListener actionListener;
    transient ItemListener itemListener;
    private static final String base = "list";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -3304312411574666869L;
    static final int DEFAULT_VISIBLE_ROWS = 4;
    private int listSerializedDataVersion;

    public List() throws HeadlessException {
        this(0, false);
    }

    public List(int i2) throws HeadlessException {
        this(i2, false);
    }

    public List(int i2, boolean z2) throws HeadlessException {
        this.items = new Vector<>();
        this.rows = 0;
        this.multipleMode = false;
        this.selected = new int[0];
        this.visibleIndex = -1;
        this.listSerializedDataVersion = 1;
        GraphicsEnvironment.checkHeadless();
        this.rows = i2 != 0 ? i2 : 4;
        this.multipleMode = z2;
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (List.class) {
            StringBuilder sbAppend = new StringBuilder().append("list");
            int i2 = nameCounter;
            nameCounter = i2 + 1;
            string = sbAppend.append(i2).toString();
        }
        return string;
    }

    @Override // java.awt.Component
    public void addNotify() {
        synchronized (getTreeLock()) {
            if (this.peer == null) {
                this.peer = getToolkit().createList(this);
            }
            super.addNotify();
        }
    }

    @Override // java.awt.Component
    public void removeNotify() {
        synchronized (getTreeLock()) {
            ListPeer listPeer = (ListPeer) this.peer;
            if (listPeer != null) {
                this.selected = listPeer.getSelectedIndexes();
            }
            super.removeNotify();
        }
    }

    public int getItemCount() {
        return countItems();
    }

    @Deprecated
    public int countItems() {
        return this.items.size();
    }

    public String getItem(int i2) {
        return getItemImpl(i2);
    }

    final String getItemImpl(int i2) {
        return this.items.elementAt(i2);
    }

    public synchronized String[] getItems() {
        String[] strArr = new String[this.items.size()];
        this.items.copyInto(strArr);
        return strArr;
    }

    public void add(String str) {
        addItem(str);
    }

    @Deprecated
    public void addItem(String str) {
        addItem(str, -1);
    }

    public void add(String str, int i2) {
        addItem(str, i2);
    }

    @Deprecated
    public synchronized void addItem(String str, int i2) {
        if (i2 < -1 || i2 >= this.items.size()) {
            i2 = -1;
        }
        if (str == null) {
            str = "";
        }
        if (i2 == -1) {
            this.items.addElement(str);
        } else {
            this.items.insertElementAt(str, i2);
        }
        ListPeer listPeer = (ListPeer) this.peer;
        if (listPeer != null) {
            listPeer.add(str, i2);
        }
    }

    public synchronized void replaceItem(String str, int i2) {
        remove(i2);
        add(str, i2);
    }

    public void removeAll() {
        clear();
    }

    @Deprecated
    public synchronized void clear() {
        ListPeer listPeer = (ListPeer) this.peer;
        if (listPeer != null) {
            listPeer.removeAll();
        }
        this.items = new Vector<>();
        this.selected = new int[0];
    }

    public synchronized void remove(String str) {
        int iIndexOf = this.items.indexOf(str);
        if (iIndexOf < 0) {
            throw new IllegalArgumentException("item " + str + " not found in list");
        }
        remove(iIndexOf);
    }

    public void remove(int i2) {
        delItem(i2);
    }

    @Deprecated
    public void delItem(int i2) {
        delItems(i2, i2);
    }

    public synchronized int getSelectedIndex() {
        int[] selectedIndexes = getSelectedIndexes();
        if (selectedIndexes.length == 1) {
            return selectedIndexes[0];
        }
        return -1;
    }

    public synchronized int[] getSelectedIndexes() {
        ListPeer listPeer = (ListPeer) this.peer;
        if (listPeer != null) {
            this.selected = listPeer.getSelectedIndexes();
        }
        return (int[]) this.selected.clone();
    }

    public synchronized String getSelectedItem() {
        int selectedIndex = getSelectedIndex();
        if (selectedIndex < 0) {
            return null;
        }
        return getItem(selectedIndex);
    }

    public synchronized String[] getSelectedItems() {
        int[] selectedIndexes = getSelectedIndexes();
        String[] strArr = new String[selectedIndexes.length];
        for (int i2 = 0; i2 < selectedIndexes.length; i2++) {
            strArr[i2] = getItem(selectedIndexes[i2]);
        }
        return strArr;
    }

    @Override // java.awt.ItemSelectable
    public Object[] getSelectedObjects() {
        return getSelectedItems();
    }

    public void select(int i2) {
        ListPeer listPeer;
        do {
            listPeer = (ListPeer) this.peer;
            if (listPeer != null) {
                listPeer.select(i2);
                return;
            }
            synchronized (this) {
                boolean z2 = false;
                int i3 = 0;
                while (true) {
                    if (i3 >= this.selected.length) {
                        break;
                    }
                    if (this.selected[i3] != i2) {
                        i3++;
                    } else {
                        z2 = true;
                        break;
                    }
                }
                if (!z2) {
                    if (!this.multipleMode) {
                        this.selected = new int[1];
                        this.selected[0] = i2;
                    } else {
                        int[] iArr = new int[this.selected.length + 1];
                        System.arraycopy(this.selected, 0, iArr, 0, this.selected.length);
                        iArr[this.selected.length] = i2;
                        this.selected = iArr;
                    }
                }
            }
        } while (listPeer != this.peer);
    }

    public synchronized void deselect(int i2) {
        ListPeer listPeer = (ListPeer) this.peer;
        if (listPeer != null && (isMultipleMode() || getSelectedIndex() == i2)) {
            listPeer.deselect(i2);
        }
        for (int i3 = 0; i3 < this.selected.length; i3++) {
            if (this.selected[i3] == i2) {
                int[] iArr = new int[this.selected.length - 1];
                System.arraycopy(this.selected, 0, iArr, 0, i3);
                System.arraycopy(this.selected, i3 + 1, iArr, i3, this.selected.length - (i3 + 1));
                this.selected = iArr;
                return;
            }
        }
    }

    public boolean isIndexSelected(int i2) {
        return isSelected(i2);
    }

    @Deprecated
    public boolean isSelected(int i2) {
        for (int i3 : getSelectedIndexes()) {
            if (i3 == i2) {
                return true;
            }
        }
        return false;
    }

    public int getRows() {
        return this.rows;
    }

    public boolean isMultipleMode() {
        return allowsMultipleSelections();
    }

    @Deprecated
    public boolean allowsMultipleSelections() {
        return this.multipleMode;
    }

    public void setMultipleMode(boolean z2) {
        setMultipleSelections(z2);
    }

    @Deprecated
    public synchronized void setMultipleSelections(boolean z2) {
        if (z2 != this.multipleMode) {
            this.multipleMode = z2;
            ListPeer listPeer = (ListPeer) this.peer;
            if (listPeer != null) {
                listPeer.setMultipleMode(z2);
            }
        }
    }

    public int getVisibleIndex() {
        return this.visibleIndex;
    }

    public synchronized void makeVisible(int i2) {
        this.visibleIndex = i2;
        ListPeer listPeer = (ListPeer) this.peer;
        if (listPeer != null) {
            listPeer.makeVisible(i2);
        }
    }

    public Dimension getPreferredSize(int i2) {
        return preferredSize(i2);
    }

    @Deprecated
    public Dimension preferredSize(int i2) {
        Dimension dimensionPreferredSize;
        synchronized (getTreeLock()) {
            ListPeer listPeer = (ListPeer) this.peer;
            if (listPeer != null) {
                dimensionPreferredSize = listPeer.getPreferredSize(i2);
            } else {
                dimensionPreferredSize = super.preferredSize();
            }
        }
        return dimensionPreferredSize;
    }

    @Override // java.awt.Component
    public Dimension getPreferredSize() {
        return preferredSize();
    }

    @Override // java.awt.Component
    @Deprecated
    public Dimension preferredSize() {
        Dimension dimensionPreferredSize;
        synchronized (getTreeLock()) {
            if (this.rows > 0) {
                dimensionPreferredSize = preferredSize(this.rows);
            } else {
                dimensionPreferredSize = super.preferredSize();
            }
        }
        return dimensionPreferredSize;
    }

    public Dimension getMinimumSize(int i2) {
        return minimumSize(i2);
    }

    @Deprecated
    public Dimension minimumSize(int i2) {
        Dimension dimensionMinimumSize;
        synchronized (getTreeLock()) {
            ListPeer listPeer = (ListPeer) this.peer;
            if (listPeer != null) {
                dimensionMinimumSize = listPeer.getMinimumSize(i2);
            } else {
                dimensionMinimumSize = super.minimumSize();
            }
        }
        return dimensionMinimumSize;
    }

    @Override // java.awt.Component
    public Dimension getMinimumSize() {
        return minimumSize();
    }

    @Override // java.awt.Component
    @Deprecated
    public Dimension minimumSize() {
        Dimension dimensionMinimumSize;
        synchronized (getTreeLock()) {
            dimensionMinimumSize = this.rows > 0 ? minimumSize(this.rows) : super.minimumSize();
        }
        return dimensionMinimumSize;
    }

    @Override // java.awt.ItemSelectable
    public synchronized void addItemListener(ItemListener itemListener) {
        if (itemListener == null) {
            return;
        }
        this.itemListener = AWTEventMulticaster.add(this.itemListener, itemListener);
        this.newEventsOnly = true;
    }

    @Override // java.awt.ItemSelectable
    public synchronized void removeItemListener(ItemListener itemListener) {
        if (itemListener == null) {
            return;
        }
        this.itemListener = AWTEventMulticaster.remove(this.itemListener, itemListener);
    }

    public synchronized ItemListener[] getItemListeners() {
        return (ItemListener[]) getListeners(ItemListener.class);
    }

    public synchronized void addActionListener(ActionListener actionListener) {
        if (actionListener == null) {
            return;
        }
        this.actionListener = AWTEventMulticaster.add(this.actionListener, actionListener);
        this.newEventsOnly = true;
    }

    public synchronized void removeActionListener(ActionListener actionListener) {
        if (actionListener == null) {
            return;
        }
        this.actionListener = AWTEventMulticaster.remove(this.actionListener, actionListener);
    }

    public synchronized ActionListener[] getActionListeners() {
        return (ActionListener[]) getListeners(ActionListener.class);
    }

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        EventListener eventListener;
        if (cls == ActionListener.class) {
            eventListener = this.actionListener;
        } else if (cls == ItemListener.class) {
            eventListener = this.itemListener;
        } else {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(eventListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        switch (aWTEvent.id) {
            case 701:
                if ((this.eventMask & 512) != 0 || this.itemListener != null) {
                    return true;
                }
                return false;
            case 1001:
                if ((this.eventMask & 128) != 0 || this.actionListener != null) {
                    return true;
                }
                return false;
            default:
                return super.eventEnabled(aWTEvent);
        }
    }

    @Override // java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof ItemEvent) {
            processItemEvent((ItemEvent) aWTEvent);
        } else if (aWTEvent instanceof ActionEvent) {
            processActionEvent((ActionEvent) aWTEvent);
        } else {
            super.processEvent(aWTEvent);
        }
    }

    protected void processItemEvent(ItemEvent itemEvent) {
        ItemListener itemListener = this.itemListener;
        if (itemListener != null) {
            itemListener.itemStateChanged(itemEvent);
        }
    }

    protected void processActionEvent(ActionEvent actionEvent) {
        ActionListener actionListener = this.actionListener;
        if (actionListener != null) {
            actionListener.actionPerformed(actionEvent);
        }
    }

    @Override // java.awt.Component
    protected String paramString() {
        return super.paramString() + ",selected=" + getSelectedItem();
    }

    @Deprecated
    public synchronized void delItems(int i2, int i3) {
        for (int i4 = i3; i4 >= i2; i4--) {
            this.items.removeElementAt(i4);
        }
        ListPeer listPeer = (ListPeer) this.peer;
        if (listPeer != null) {
            listPeer.delItems(i2, i3);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (this) {
            ListPeer listPeer = (ListPeer) this.peer;
            if (listPeer != null) {
                this.selected = listPeer.getSelectedIndexes();
            }
        }
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "itemL", this.itemListener);
        AWTEventMulticaster.save(objectOutputStream, "actionL", this.actionListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                String strIntern = ((String) object).intern();
                if ("itemL" == strIntern) {
                    addItemListener((ItemListener) objectInputStream.readObject());
                } else if ("actionL" == strIntern) {
                    addActionListener((ActionListener) objectInputStream.readObject());
                } else {
                    objectInputStream.readObject();
                }
            } else {
                return;
            }
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleAWTList();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/List$AccessibleAWTList.class */
    protected class AccessibleAWTList extends Component.AccessibleAWTComponent implements AccessibleSelection, ItemListener, ActionListener {
        private static final long serialVersionUID = 7924617370136012829L;

        public AccessibleAWTList() {
            super();
            List.this.addActionListener(this);
            List.this.addItemListener(this);
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
        }

        @Override // java.awt.event.ItemListener
        public void itemStateChanged(ItemEvent itemEvent) {
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (List.this.isMultipleMode()) {
                accessibleStateSet.add(AccessibleState.MULTISELECTABLE);
            }
            return accessibleStateSet;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.LIST;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            return null;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return List.this.getItemCount();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            synchronized (List.this) {
                if (i2 >= List.this.getItemCount()) {
                    return null;
                }
                return new AccessibleAWTListChild(List.this, i2);
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            return List.this.getSelectedIndexes().length;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            synchronized (List.this) {
                int accessibleSelectionCount = getAccessibleSelectionCount();
                if (i2 < 0 || i2 >= accessibleSelectionCount) {
                    return null;
                }
                return getAccessibleChild(List.this.getSelectedIndexes()[i2]);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            return List.this.isIndexSelected(i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            List.this.select(i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            List.this.deselect(i2);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            synchronized (List.this) {
                int[] selectedIndexes = List.this.getSelectedIndexes();
                if (selectedIndexes == null) {
                    return;
                }
                for (int length = selectedIndexes.length - 1; length >= 0; length--) {
                    List.this.deselect(selectedIndexes[length]);
                }
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
            synchronized (List.this) {
                for (int itemCount = List.this.getItemCount() - 1; itemCount >= 0; itemCount--) {
                    List.this.select(itemCount);
                }
            }
        }

        /* loaded from: rt.jar:java/awt/List$AccessibleAWTList$AccessibleAWTListChild.class */
        protected class AccessibleAWTListChild extends Component.AccessibleAWTComponent implements Accessible {
            private static final long serialVersionUID = 4412022926028300317L;
            private List parent;
            private int indexInParent;

            public AccessibleAWTListChild(List list, int i2) {
                super();
                this.parent = list;
                setAccessibleParent(list);
                this.indexInParent = i2;
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                return this;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return AccessibleRole.LIST_ITEM;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public AccessibleStateSet getAccessibleStateSet() {
                AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
                if (this.parent.isIndexSelected(this.indexInParent)) {
                    accessibleStateSet.add(AccessibleState.SELECTED);
                }
                return accessibleStateSet;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public Locale getLocale() {
                return this.parent.getLocale();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                return this.indexInParent;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                return 0;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                return null;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Color getBackground() {
                return this.parent.getBackground();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setBackground(Color color) {
                this.parent.setBackground(color);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Color getForeground() {
                return this.parent.getForeground();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setForeground(Color color) {
                this.parent.setForeground(color);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Cursor getCursor() {
                return this.parent.getCursor();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setCursor(Cursor cursor) {
                this.parent.setCursor(cursor);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Font getFont() {
                return this.parent.getFont();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setFont(Font font) {
                this.parent.setFont(font);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public FontMetrics getFontMetrics(Font font) {
                return this.parent.getFontMetrics(font);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public boolean isEnabled() {
                return this.parent.isEnabled();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setEnabled(boolean z2) {
                this.parent.setEnabled(z2);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public boolean isVisible() {
                return false;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setVisible(boolean z2) {
                this.parent.setVisible(z2);
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public boolean isShowing() {
                return false;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public boolean contains(Point point) {
                return false;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Point getLocationOnScreen() {
                return null;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Point getLocation() {
                return null;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setLocation(Point point) {
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Rectangle getBounds() {
                return null;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setBounds(Rectangle rectangle) {
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Dimension getSize() {
                return null;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void setSize(Dimension dimension) {
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public Accessible getAccessibleAt(Point point) {
                return null;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public boolean isFocusTraversable() {
                return false;
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void requestFocus() {
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void addFocusListener(FocusListener focusListener) {
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
            public void removeFocusListener(FocusListener focusListener) {
            }
        }
    }
}
