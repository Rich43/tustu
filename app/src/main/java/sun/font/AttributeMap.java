package sun.font;

import java.awt.font.TextAttribute;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:sun/font/AttributeMap.class */
public final class AttributeMap extends AbstractMap<TextAttribute, Object> {
    private AttributeValues values;
    private Map<TextAttribute, Object> delegateMap;
    private static boolean first = false;

    public AttributeMap(AttributeValues attributeValues) {
        this.values = attributeValues;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<TextAttribute, Object>> entrySet() {
        return delegate().entrySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(TextAttribute textAttribute, Object obj) {
        return delegate().put(textAttribute, obj);
    }

    public AttributeValues getValues() {
        return this.values;
    }

    private Map<TextAttribute, Object> delegate() {
        if (this.delegateMap == null) {
            if (first) {
                first = false;
                Thread.dumpStack();
            }
            this.delegateMap = this.values.toMap(new HashMap(27));
            this.values = null;
        }
        return this.delegateMap;
    }

    @Override // java.util.AbstractMap
    public String toString() {
        if (this.values != null) {
            return "map of " + this.values.toString();
        }
        return super.toString();
    }
}
