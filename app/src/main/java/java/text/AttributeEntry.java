package java.text;

import java.text.AttributedCharacterIterator;
import java.util.Map;

/* compiled from: AttributedString.java */
/* loaded from: rt.jar:java/text/AttributeEntry.class */
class AttributeEntry implements Map.Entry<AttributedCharacterIterator.Attribute, Object> {
    private AttributedCharacterIterator.Attribute key;
    private Object value;

    AttributeEntry(AttributedCharacterIterator.Attribute attribute, Object obj) {
        this.key = attribute;
        this.value = obj;
    }

    @Override // java.util.Map.Entry
    public boolean equals(Object obj) {
        if (!(obj instanceof AttributeEntry)) {
            return false;
        }
        AttributeEntry attributeEntry = (AttributeEntry) obj;
        return attributeEntry.key.equals(this.key) && (this.value != null ? attributeEntry.value.equals(this.value) : attributeEntry.value == null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Map.Entry
    public AttributedCharacterIterator.Attribute getKey() {
        return this.key;
    }

    @Override // java.util.Map.Entry
    public Object getValue() {
        return this.value;
    }

    @Override // java.util.Map.Entry
    public Object setValue(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.Map.Entry
    public int hashCode() {
        return this.key.hashCode() ^ (this.value == null ? 0 : this.value.hashCode());
    }

    public String toString() {
        return this.key.toString() + "=" + this.value.toString();
    }
}
