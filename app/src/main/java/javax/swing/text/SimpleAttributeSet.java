package javax.swing.text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;

/* loaded from: rt.jar:javax/swing/text/SimpleAttributeSet.class */
public class SimpleAttributeSet implements MutableAttributeSet, Serializable, Cloneable {
    private static final long serialVersionUID = -6631553454711782652L;
    public static final AttributeSet EMPTY = new EmptyAttributeSet();
    private transient LinkedHashMap<Object, Object> table = new LinkedHashMap<>(3);

    public SimpleAttributeSet() {
    }

    public SimpleAttributeSet(AttributeSet attributeSet) {
        addAttributes(attributeSet);
    }

    public boolean isEmpty() {
        return this.table.isEmpty();
    }

    @Override // javax.swing.text.AttributeSet
    public int getAttributeCount() {
        return this.table.size();
    }

    @Override // javax.swing.text.AttributeSet
    public boolean isDefined(Object obj) {
        return this.table.containsKey(obj);
    }

    @Override // javax.swing.text.AttributeSet
    public boolean isEqual(AttributeSet attributeSet) {
        return getAttributeCount() == attributeSet.getAttributeCount() && containsAttributes(attributeSet);
    }

    @Override // javax.swing.text.AttributeSet
    public AttributeSet copyAttributes() {
        return (AttributeSet) clone();
    }

    @Override // javax.swing.text.AttributeSet
    public Enumeration<?> getAttributeNames() {
        return Collections.enumeration(this.table.keySet());
    }

    @Override // javax.swing.text.AttributeSet
    public Object getAttribute(Object obj) {
        AttributeSet resolveParent;
        Object attribute = this.table.get(obj);
        if (attribute == null && (resolveParent = getResolveParent()) != null) {
            attribute = resolveParent.getAttribute(obj);
        }
        return attribute;
    }

    @Override // javax.swing.text.AttributeSet
    public boolean containsAttribute(Object obj, Object obj2) {
        return obj2.equals(getAttribute(obj));
    }

    @Override // javax.swing.text.AttributeSet
    public boolean containsAttributes(AttributeSet attributeSet) {
        boolean zEquals = true;
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (zEquals && attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            zEquals = attributeSet.getAttribute(objNextElement).equals(getAttribute(objNextElement));
        }
        return zEquals;
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void addAttribute(Object obj, Object obj2) {
        this.table.put(obj, obj2);
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void addAttributes(AttributeSet attributeSet) {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            addAttribute(objNextElement, attributeSet.getAttribute(objNextElement));
        }
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void removeAttribute(Object obj) {
        this.table.remove(obj);
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void removeAttributes(Enumeration<?> enumeration) {
        while (enumeration.hasMoreElements()) {
            removeAttribute(enumeration.nextElement2());
        }
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void removeAttributes(AttributeSet attributeSet) {
        if (attributeSet == this) {
            this.table.clear();
            return;
        }
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            if (attributeSet.getAttribute(objNextElement).equals(getAttribute(objNextElement))) {
                removeAttribute(objNextElement);
            }
        }
    }

    @Override // javax.swing.text.AttributeSet
    public AttributeSet getResolveParent() {
        return (AttributeSet) this.table.get(StyleConstants.ResolveAttribute);
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void setResolveParent(AttributeSet attributeSet) {
        addAttribute(StyleConstants.ResolveAttribute, attributeSet);
    }

    public Object clone() {
        SimpleAttributeSet simpleAttributeSet;
        try {
            simpleAttributeSet = (SimpleAttributeSet) super.clone();
            simpleAttributeSet.table = (LinkedHashMap) this.table.clone();
        } catch (CloneNotSupportedException e2) {
            simpleAttributeSet = null;
        }
        return simpleAttributeSet;
    }

    public int hashCode() {
        return this.table.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AttributeSet) {
            return isEqual((AttributeSet) obj);
        }
        return false;
    }

    public String toString() {
        String str = "";
        Enumeration<?> attributeNames = getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement2();
            Object attribute = getAttribute(objNextElement);
            if (attribute instanceof AttributeSet) {
                str = str + objNextElement + "=**AttributeSet** ";
            } else {
                str = str + objNextElement + "=" + attribute + " ";
            }
        }
        return str;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        StyleContext.writeAttributeSet(objectOutputStream, this);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.table = new LinkedHashMap<>(3);
        StyleContext.readAttributeSet(objectInputStream, this);
    }

    /* loaded from: rt.jar:javax/swing/text/SimpleAttributeSet$EmptyAttributeSet.class */
    static class EmptyAttributeSet implements AttributeSet, Serializable {
        static final long serialVersionUID = -8714803568785904228L;

        EmptyAttributeSet() {
        }

        @Override // javax.swing.text.AttributeSet
        public int getAttributeCount() {
            return 0;
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isDefined(Object obj) {
            return false;
        }

        @Override // javax.swing.text.AttributeSet
        public boolean isEqual(AttributeSet attributeSet) {
            return attributeSet.getAttributeCount() == 0;
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet copyAttributes() {
            return this;
        }

        @Override // javax.swing.text.AttributeSet
        public Object getAttribute(Object obj) {
            return null;
        }

        @Override // javax.swing.text.AttributeSet
        public Enumeration getAttributeNames() {
            return Collections.emptyEnumeration();
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttribute(Object obj, Object obj2) {
            return false;
        }

        @Override // javax.swing.text.AttributeSet
        public boolean containsAttributes(AttributeSet attributeSet) {
            return attributeSet.getAttributeCount() == 0;
        }

        @Override // javax.swing.text.AttributeSet
        public AttributeSet getResolveParent() {
            return null;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof AttributeSet) && ((AttributeSet) obj).getAttributeCount() == 0;
        }

        public int hashCode() {
            return 0;
        }
    }
}
