package javax.swing.text.html.parser;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Hashtable;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/text/html/parser/Element.class */
public final class Element implements DTDConstants, Serializable {
    public int index;
    public String name;
    public boolean oStart;
    public boolean oEnd;
    public BitSet inclusions;
    public BitSet exclusions;
    public int type = 19;
    public ContentModel content;
    public AttributeList atts;
    public Object data;
    private static final Object MAX_INDEX_KEY = new Object();
    static Hashtable<String, Integer> contentTypes = new Hashtable<>();

    Element() {
    }

    Element(String str, int i2) {
        this.name = str;
        this.index = i2;
        if (i2 > getMaxIndex()) {
            AppContext.getAppContext().put(MAX_INDEX_KEY, Integer.valueOf(i2));
        }
    }

    static {
        contentTypes.put("CDATA", 1);
        contentTypes.put("RCDATA", 16);
        contentTypes.put("EMPTY", 17);
        contentTypes.put("ANY", 19);
    }

    static int getMaxIndex() {
        Integer num = (Integer) AppContext.getAppContext().get(MAX_INDEX_KEY);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public String getName() {
        return this.name;
    }

    public boolean omitStart() {
        return this.oStart;
    }

    public boolean omitEnd() {
        return this.oEnd;
    }

    public int getType() {
        return this.type;
    }

    public ContentModel getContent() {
        return this.content;
    }

    public AttributeList getAttributes() {
        return this.atts;
    }

    public int getIndex() {
        return this.index;
    }

    public boolean isEmpty() {
        return this.type == 17;
    }

    public String toString() {
        return this.name;
    }

    public AttributeList getAttribute(String str) {
        AttributeList attributeList = this.atts;
        while (true) {
            AttributeList attributeList2 = attributeList;
            if (attributeList2 != null) {
                if (!attributeList2.name.equals(str)) {
                    attributeList = attributeList2.next;
                } else {
                    return attributeList2;
                }
            } else {
                return null;
            }
        }
    }

    public AttributeList getAttributeByValue(String str) {
        AttributeList attributeList = this.atts;
        while (true) {
            AttributeList attributeList2 = attributeList;
            if (attributeList2 != null) {
                if (attributeList2.values == null || !attributeList2.values.contains(str)) {
                    attributeList = attributeList2.next;
                } else {
                    return attributeList2;
                }
            } else {
                return null;
            }
        }
    }

    public static int name2type(String str) {
        Integer num = contentTypes.get(str);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }
}
