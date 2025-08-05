package javax.swing.text.rtf;

import java.util.Dictionary;
import java.util.Enumeration;
import javax.swing.text.AttributeSet;
import javax.swing.text.MutableAttributeSet;

/* loaded from: rt.jar:javax/swing/text/rtf/MockAttributeSet.class */
class MockAttributeSet implements AttributeSet, MutableAttributeSet {
    public Dictionary<Object, Object> backing;

    MockAttributeSet() {
    }

    public boolean isEmpty() {
        return this.backing.isEmpty();
    }

    @Override // javax.swing.text.AttributeSet
    public int getAttributeCount() {
        return this.backing.size();
    }

    @Override // javax.swing.text.AttributeSet
    public boolean isDefined(Object obj) {
        return this.backing.get(obj) != null;
    }

    @Override // javax.swing.text.AttributeSet
    public boolean isEqual(AttributeSet attributeSet) {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.AttributeSet
    public AttributeSet copyAttributes() {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.AttributeSet
    public Object getAttribute(Object obj) {
        return this.backing.get(obj);
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void addAttribute(Object obj, Object obj2) {
        this.backing.put(obj, obj2);
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void addAttributes(AttributeSet attributeSet) {
        Enumeration<?> attributeNames = attributeSet.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object objNextElement = attributeNames.nextElement();
            this.backing.put(objNextElement, attributeSet.getAttribute(objNextElement));
        }
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void removeAttribute(Object obj) {
        this.backing.remove(obj);
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void removeAttributes(AttributeSet attributeSet) {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void removeAttributes(Enumeration<?> enumeration) {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.MutableAttributeSet
    public void setResolveParent(AttributeSet attributeSet) {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.AttributeSet
    public Enumeration getAttributeNames() {
        return this.backing.keys();
    }

    @Override // javax.swing.text.AttributeSet
    public boolean containsAttribute(Object obj, Object obj2) {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.AttributeSet
    public boolean containsAttributes(AttributeSet attributeSet) {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }

    @Override // javax.swing.text.AttributeSet
    public AttributeSet getResolveParent() {
        throw new InternalError("MockAttributeSet: charade revealed!");
    }
}
