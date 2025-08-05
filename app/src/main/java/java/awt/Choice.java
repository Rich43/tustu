package java.awt;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.peer.ChoicePeer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventListener;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/* loaded from: rt.jar:java/awt/Choice.class */
public class Choice extends Component implements ItemSelectable, Accessible {
    Vector<String> pItems;
    transient ItemListener itemListener;
    private static final String base = "choice";
    private static int nameCounter = 0;
    private static final long serialVersionUID = -4075310674757313071L;
    int selectedIndex = -1;
    private int choiceSerializedDataVersion = 1;

    private static native void initIDs();

    static {
        Toolkit.loadLibraries();
        if (!GraphicsEnvironment.isHeadless()) {
            initIDs();
        }
    }

    public Choice() throws HeadlessException {
        GraphicsEnvironment.checkHeadless();
        this.pItems = new Vector<>();
    }

    @Override // java.awt.Component
    String constructComponentName() {
        String string;
        synchronized (Choice.class) {
            StringBuilder sbAppend = new StringBuilder().append(base);
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
                this.peer = getToolkit().createChoice(this);
            }
            super.addNotify();
        }
    }

    public int getItemCount() {
        return countItems();
    }

    @Deprecated
    public int countItems() {
        return this.pItems.size();
    }

    public String getItem(int i2) {
        return getItemImpl(i2);
    }

    final String getItemImpl(int i2) {
        return this.pItems.elementAt(i2);
    }

    public void add(String str) {
        addItem(str);
    }

    public void addItem(String str) {
        synchronized (this) {
            insertNoInvalidate(str, this.pItems.size());
        }
        invalidateIfValid();
    }

    private void insertNoInvalidate(String str, int i2) {
        if (str == null) {
            throw new NullPointerException("cannot add null item to Choice");
        }
        this.pItems.insertElementAt(str, i2);
        ChoicePeer choicePeer = (ChoicePeer) this.peer;
        if (choicePeer != null) {
            choicePeer.add(str, i2);
        }
        if (this.selectedIndex < 0 || this.selectedIndex >= i2) {
            select(0);
        }
    }

    public void insert(String str, int i2) {
        synchronized (this) {
            if (i2 < 0) {
                throw new IllegalArgumentException("index less than zero.");
            }
            insertNoInvalidate(str, Math.min(i2, this.pItems.size()));
        }
        invalidateIfValid();
    }

    public void remove(String str) {
        synchronized (this) {
            int iIndexOf = this.pItems.indexOf(str);
            if (iIndexOf < 0) {
                throw new IllegalArgumentException("item " + str + " not found in choice");
            }
            removeNoInvalidate(iIndexOf);
        }
        invalidateIfValid();
    }

    public void remove(int i2) {
        synchronized (this) {
            removeNoInvalidate(i2);
        }
        invalidateIfValid();
    }

    private void removeNoInvalidate(int i2) {
        this.pItems.removeElementAt(i2);
        ChoicePeer choicePeer = (ChoicePeer) this.peer;
        if (choicePeer != null) {
            choicePeer.remove(i2);
        }
        if (this.pItems.size() == 0) {
            this.selectedIndex = -1;
        } else if (this.selectedIndex == i2) {
            select(0);
        } else if (this.selectedIndex > i2) {
            select(this.selectedIndex - 1);
        }
    }

    public void removeAll() {
        synchronized (this) {
            if (this.peer != null) {
                ((ChoicePeer) this.peer).removeAll();
            }
            this.pItems.removeAllElements();
            this.selectedIndex = -1;
        }
        invalidateIfValid();
    }

    public synchronized String getSelectedItem() {
        if (this.selectedIndex >= 0) {
            return getItem(this.selectedIndex);
        }
        return null;
    }

    @Override // java.awt.ItemSelectable
    public synchronized Object[] getSelectedObjects() {
        if (this.selectedIndex >= 0) {
            return new Object[]{getItem(this.selectedIndex)};
        }
        return null;
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }

    public synchronized void select(int i2) {
        if (i2 >= this.pItems.size() || i2 < 0) {
            throw new IllegalArgumentException("illegal Choice item position: " + i2);
        }
        if (this.pItems.size() > 0) {
            this.selectedIndex = i2;
            ChoicePeer choicePeer = (ChoicePeer) this.peer;
            if (choicePeer != null) {
                choicePeer.select(i2);
            }
        }
    }

    public synchronized void select(String str) {
        int iIndexOf = this.pItems.indexOf(str);
        if (iIndexOf >= 0) {
            select(iIndexOf);
        }
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

    @Override // java.awt.Component
    public <T extends EventListener> T[] getListeners(Class<T> cls) {
        if (cls != ItemListener.class) {
            return (T[]) super.getListeners(cls);
        }
        return (T[]) AWTEventMulticaster.getListeners(this.itemListener, cls);
    }

    @Override // java.awt.Component
    boolean eventEnabled(AWTEvent aWTEvent) {
        if (aWTEvent.id != 701) {
            return super.eventEnabled(aWTEvent);
        }
        if ((this.eventMask & 512) != 0 || this.itemListener != null) {
            return true;
        }
        return false;
    }

    @Override // java.awt.Component
    protected void processEvent(AWTEvent aWTEvent) {
        if (aWTEvent instanceof ItemEvent) {
            processItemEvent((ItemEvent) aWTEvent);
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

    @Override // java.awt.Component
    protected String paramString() {
        return super.paramString() + ",current=" + getSelectedItem();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        AWTEventMulticaster.save(objectOutputStream, "itemL", this.itemListener);
        objectOutputStream.writeObject(null);
    }

    private void readObject(ObjectInputStream objectInputStream) throws HeadlessException, IOException, ClassNotFoundException {
        GraphicsEnvironment.checkHeadless();
        objectInputStream.defaultReadObject();
        while (true) {
            Object object = objectInputStream.readObject();
            if (null != object) {
                if ("itemL" == ((String) object).intern()) {
                    addItemListener((ItemListener) objectInputStream.readObject());
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
            this.accessibleContext = new AccessibleAWTChoice();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:java/awt/Choice$AccessibleAWTChoice.class */
    protected class AccessibleAWTChoice extends Component.AccessibleAWTComponent implements AccessibleAction {
        private static final long serialVersionUID = 7175603582428509322L;

        public AccessibleAWTChoice() {
            super();
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.COMBO_BOX;
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return 0;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            return false;
        }
    }
}
