package javax.swing.text.html.parser;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/* loaded from: rt.jar:javax/swing/text/html/parser/AttributeList.class */
public final class AttributeList implements DTDConstants, Serializable {
    public String name;
    public int type;
    public Vector<?> values;
    public int modifier;
    public String value;
    public AttributeList next;
    static Hashtable<Object, Object> attributeTypes = new Hashtable<>();

    AttributeList() {
    }

    public AttributeList(String str) {
        this.name = str;
    }

    public AttributeList(String str, int i2, int i3, String str2, Vector<?> vector, AttributeList attributeList) {
        this.name = str;
        this.type = i2;
        this.modifier = i3;
        this.value = str2;
        this.values = vector;
        this.next = attributeList;
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        return this.type;
    }

    public int getModifier() {
        return this.modifier;
    }

    public Enumeration<?> getValues() {
        if (this.values != null) {
            return this.values.elements();
        }
        return null;
    }

    public String getValue() {
        return this.value;
    }

    public AttributeList getNext() {
        return this.next;
    }

    public String toString() {
        return this.name;
    }

    static {
        defineAttributeType("CDATA", 1);
        defineAttributeType(SchemaSymbols.ATTVAL_ENTITY, 2);
        defineAttributeType(SchemaSymbols.ATTVAL_ENTITIES, 3);
        defineAttributeType("ID", 4);
        defineAttributeType(SchemaSymbols.ATTVAL_IDREF, 5);
        defineAttributeType(SchemaSymbols.ATTVAL_IDREFS, 6);
        defineAttributeType("NAME", 7);
        defineAttributeType("NAMES", 8);
        defineAttributeType(SchemaSymbols.ATTVAL_NMTOKEN, 9);
        defineAttributeType(SchemaSymbols.ATTVAL_NMTOKENS, 10);
        defineAttributeType(SchemaSymbols.ATTVAL_NOTATION, 11);
        defineAttributeType("NUMBER", 12);
        defineAttributeType("NUMBERS", 13);
        defineAttributeType("NUTOKEN", 14);
        defineAttributeType("NUTOKENS", 15);
        attributeTypes.put("fixed", 1);
        attributeTypes.put(SchemaSymbols.ATTVAL_REQUIRED, 2);
        attributeTypes.put(Keywords.FUNC_CURRENT_STRING, 3);
        attributeTypes.put("conref", 4);
        attributeTypes.put("implied", 5);
    }

    static void defineAttributeType(String str, int i2) {
        Integer numValueOf = Integer.valueOf(i2);
        attributeTypes.put(str, numValueOf);
        attributeTypes.put(numValueOf, str);
    }

    public static int name2type(String str) {
        Integer num = (Integer) attributeTypes.get(str);
        if (num == null) {
            return 1;
        }
        return num.intValue();
    }

    public static String type2name(int i2) {
        return (String) attributeTypes.get(Integer.valueOf(i2));
    }
}
