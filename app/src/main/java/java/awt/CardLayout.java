package java.awt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.commons.math3.linear.ConjugateGradient;

/* loaded from: rt.jar:java/awt/CardLayout.class */
public class CardLayout implements LayoutManager2, Serializable {
    private static final long serialVersionUID = -4328196481005934313L;
    Vector<Card> vector;
    int currentCard;
    int hgap;
    int vgap;
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("tab", Hashtable.class), new ObjectStreamField("hgap", Integer.TYPE), new ObjectStreamField("vgap", Integer.TYPE), new ObjectStreamField(ConjugateGradient.VECTOR, Vector.class), new ObjectStreamField("currentCard", Integer.TYPE)};

    /* loaded from: rt.jar:java/awt/CardLayout$Card.class */
    class Card implements Serializable {
        static final long serialVersionUID = 6640330810709497518L;
        public String name;
        public Component comp;

        public Card(String str, Component component) {
            this.name = str;
            this.comp = component;
        }
    }

    public CardLayout() {
        this(0, 0);
    }

    public CardLayout(int i2, int i3) {
        this.vector = new Vector<>();
        this.currentCard = 0;
        this.hgap = i2;
        this.vgap = i3;
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int i2) {
        this.hgap = i2;
    }

    public int getVgap() {
        return this.vgap;
    }

    public void setVgap(int i2) {
        this.vgap = i2;
    }

    @Override // java.awt.LayoutManager2
    public void addLayoutComponent(Component component, Object obj) {
        synchronized (component.getTreeLock()) {
            if (obj == null) {
                obj = "";
            }
            if (obj instanceof String) {
                addLayoutComponent((String) obj, component);
            } else {
                throw new IllegalArgumentException("cannot add to layout: constraint must be a string");
            }
        }
    }

    @Override // java.awt.LayoutManager
    @Deprecated
    public void addLayoutComponent(String str, Component component) {
        synchronized (component.getTreeLock()) {
            if (!this.vector.isEmpty()) {
                component.setVisible(false);
            }
            for (int i2 = 0; i2 < this.vector.size(); i2++) {
                if (this.vector.get(i2).name.equals(str)) {
                    this.vector.get(i2).comp = component;
                    return;
                }
            }
            this.vector.add(new Card(str, component));
        }
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
        synchronized (component.getTreeLock()) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.vector.size()) {
                    break;
                }
                if (this.vector.get(i2).comp != component) {
                    i2++;
                } else {
                    if (component.isVisible() && component.getParent() != null) {
                        next(component.getParent());
                    }
                    this.vector.remove(i2);
                    if (this.currentCard > i2) {
                        this.currentCard--;
                    }
                }
            }
        }
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < componentCount; i4++) {
                Dimension preferredSize = container.getComponent(i4).getPreferredSize();
                if (preferredSize.width > i2) {
                    i2 = preferredSize.width;
                }
                if (preferredSize.height > i3) {
                    i3 = preferredSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + i2 + (this.hgap * 2), insets.top + insets.bottom + i3 + (this.vgap * 2));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        Dimension dimension;
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            int i2 = 0;
            int i3 = 0;
            for (int i4 = 0; i4 < componentCount; i4++) {
                Dimension minimumSize = container.getComponent(i4).getMinimumSize();
                if (minimumSize.width > i2) {
                    i2 = minimumSize.width;
                }
                if (minimumSize.height > i3) {
                    i3 = minimumSize.height;
                }
            }
            dimension = new Dimension(insets.left + insets.right + i2 + (this.hgap * 2), insets.top + insets.bottom + i3 + (this.vgap * 2));
        }
        return dimension;
    }

    @Override // java.awt.LayoutManager2
    public Dimension maximumLayoutSize(Container container) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentX(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public float getLayoutAlignmentY(Container container) {
        return 0.5f;
    }

    @Override // java.awt.LayoutManager2
    public void invalidateLayout(Container container) {
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        synchronized (container.getTreeLock()) {
            Insets insets = container.getInsets();
            int componentCount = container.getComponentCount();
            boolean z2 = false;
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = container.getComponent(i2);
                component.setBounds(this.hgap + insets.left, this.vgap + insets.top, container.width - (((this.hgap * 2) + insets.left) + insets.right), container.height - (((this.vgap * 2) + insets.top) + insets.bottom));
                if (component.isVisible()) {
                    z2 = true;
                }
            }
            if (!z2 && componentCount > 0) {
                container.getComponent(0).setVisible(true);
            }
        }
    }

    void checkLayout(Container container) {
        if (container.getLayout() != this) {
            throw new IllegalArgumentException("wrong parent for CardLayout");
        }
    }

    public void first(Container container) {
        synchronized (container.getTreeLock()) {
            checkLayout(container);
            int componentCount = container.getComponentCount();
            int i2 = 0;
            while (true) {
                if (i2 >= componentCount) {
                    break;
                }
                Component component = container.getComponent(i2);
                if (!component.isVisible()) {
                    i2++;
                } else {
                    component.setVisible(false);
                    break;
                }
            }
            if (componentCount > 0) {
                this.currentCard = 0;
                container.getComponent(0).setVisible(true);
                container.validate();
            }
        }
    }

    public void next(Container container) {
        synchronized (container.getTreeLock()) {
            checkLayout(container);
            int componentCount = container.getComponentCount();
            for (int i2 = 0; i2 < componentCount; i2++) {
                Component component = container.getComponent(i2);
                if (component.isVisible()) {
                    component.setVisible(false);
                    this.currentCard = (i2 + 1) % componentCount;
                    container.getComponent(this.currentCard).setVisible(true);
                    container.validate();
                    return;
                }
            }
            showDefaultComponent(container);
        }
    }

    public void previous(Container container) {
        synchronized (container.getTreeLock()) {
            checkLayout(container);
            int componentCount = container.getComponentCount();
            int i2 = 0;
            while (i2 < componentCount) {
                Component component = container.getComponent(i2);
                if (!component.isVisible()) {
                    i2++;
                } else {
                    component.setVisible(false);
                    this.currentCard = i2 > 0 ? i2 - 1 : componentCount - 1;
                    container.getComponent(this.currentCard).setVisible(true);
                    container.validate();
                    return;
                }
            }
            showDefaultComponent(container);
        }
    }

    void showDefaultComponent(Container container) {
        if (container.getComponentCount() > 0) {
            this.currentCard = 0;
            container.getComponent(0).setVisible(true);
            container.validate();
        }
    }

    public void last(Container container) {
        synchronized (container.getTreeLock()) {
            checkLayout(container);
            int componentCount = container.getComponentCount();
            int i2 = 0;
            while (true) {
                if (i2 >= componentCount) {
                    break;
                }
                Component component = container.getComponent(i2);
                if (!component.isVisible()) {
                    i2++;
                } else {
                    component.setVisible(false);
                    break;
                }
            }
            if (componentCount > 0) {
                this.currentCard = componentCount - 1;
                container.getComponent(this.currentCard).setVisible(true);
                container.validate();
            }
        }
    }

    public void show(Container container, String str) {
        synchronized (container.getTreeLock()) {
            checkLayout(container);
            Component component = null;
            int size = this.vector.size();
            int i2 = 0;
            while (true) {
                if (i2 >= size) {
                    break;
                }
                Card card = this.vector.get(i2);
                if (!card.name.equals(str)) {
                    i2++;
                } else {
                    component = card.comp;
                    this.currentCard = i2;
                    break;
                }
            }
            if (component != null && !component.isVisible()) {
                int componentCount = container.getComponentCount();
                int i3 = 0;
                while (true) {
                    if (i3 >= componentCount) {
                        break;
                    }
                    Component component2 = container.getComponent(i3);
                    if (!component2.isVisible()) {
                        i3++;
                    } else {
                        component2.setVisible(false);
                        break;
                    }
                }
                component.setVisible(true);
                container.validate();
            }
        }
    }

    public String toString() {
        return getClass().getName() + "[hgap=" + this.hgap + ",vgap=" + this.vgap + "]";
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = objectInputStream.readFields();
        this.hgap = fields.get("hgap", 0);
        this.vgap = fields.get("vgap", 0);
        if (fields.defaulted(ConjugateGradient.VECTOR)) {
            Hashtable hashtable = (Hashtable) fields.get("tab", (Object) null);
            this.vector = new Vector<>();
            if (hashtable != null && !hashtable.isEmpty()) {
                Enumeration enumerationKeys = hashtable.keys();
                while (enumerationKeys.hasMoreElements()) {
                    String str = (String) enumerationKeys.nextElement2();
                    Component component = (Component) hashtable.get(str);
                    this.vector.add(new Card(str, component));
                    if (component.isVisible()) {
                        this.currentCard = this.vector.size() - 1;
                    }
                }
                return;
            }
            return;
        }
        this.vector = (Vector) fields.get(ConjugateGradient.VECTOR, (Object) null);
        this.currentCard = fields.get("currentCard", 0);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        Hashtable hashtable = new Hashtable();
        int size = this.vector.size();
        for (int i2 = 0; i2 < size; i2++) {
            Card card = this.vector.get(i2);
            hashtable.put(card.name, card.comp);
        }
        ObjectOutputStream.PutField putFieldPutFields = objectOutputStream.putFields();
        putFieldPutFields.put("hgap", this.hgap);
        putFieldPutFields.put("vgap", this.vgap);
        putFieldPutFields.put(ConjugateGradient.VECTOR, this.vector);
        putFieldPutFields.put("currentCard", this.currentCard);
        putFieldPutFields.put("tab", hashtable);
        objectOutputStream.writeFields();
    }
}
