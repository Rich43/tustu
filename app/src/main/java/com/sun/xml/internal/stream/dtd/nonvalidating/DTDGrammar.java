package com.sun.xml.internal.stream.dtd.nonvalidating;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/stream/dtd/nonvalidating/DTDGrammar.class */
public class DTDGrammar {
    public static final int TOP_LEVEL_SCOPE = -1;
    private static final int CHUNK_SHIFT = 8;
    private static final int CHUNK_SIZE = 256;
    private static final int CHUNK_MASK = 255;
    private static final int INITIAL_CHUNK_COUNT = 4;
    private static final short LIST_FLAG = 128;
    private static final short LIST_MASK = -129;
    private static final boolean DEBUG = false;
    protected int fCurrentElementIndex;
    protected int fCurrentAttributeIndex;
    private SymbolTable fSymbolTable;
    protected XMLDTDSource fDTDSource = null;
    protected XMLDTDContentModelSource fDTDContentModelSource = null;
    protected boolean fReadingExternalDTD = false;
    private ArrayList notationDecls = new ArrayList();
    private int fElementDeclCount = 0;
    private QName[][] fElementDeclName = new QName[4];
    private short[][] fElementDeclType = new short[4];
    private int[][] fElementDeclFirstAttributeDeclIndex = new int[4];
    private int[][] fElementDeclLastAttributeDeclIndex = new int[4];
    private int fAttributeDeclCount = 0;
    private QName[][] fAttributeDeclName = new QName[4];
    private short[][] fAttributeDeclType = new short[4];
    private String[][][] fAttributeDeclEnumeration = new String[4][];
    private short[][] fAttributeDeclDefaultType = new short[4];
    private String[][] fAttributeDeclDefaultValue = new String[4];
    private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4];
    private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4];
    private final Map<String, Integer> fElementIndexMap = new HashMap();
    private final QName fQName = new QName();
    protected XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
    private XMLElementDecl fElementDecl = new XMLElementDecl();
    private XMLSimpleType fSimpleType = new XMLSimpleType();
    Map<String, XMLElementDecl> fElementDeclTab = new HashMap();

    /* JADX WARN: Type inference failed for: r1v10, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v12, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v15, types: [com.sun.org.apache.xerces.internal.xni.QName[], com.sun.org.apache.xerces.internal.xni.QName[][]] */
    /* JADX WARN: Type inference failed for: r1v17, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v19, types: [java.lang.String[][], java.lang.String[][][]] */
    /* JADX WARN: Type inference failed for: r1v21, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v23, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v25, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v27, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.sun.org.apache.xerces.internal.xni.QName[], com.sun.org.apache.xerces.internal.xni.QName[][]] */
    /* JADX WARN: Type inference failed for: r1v8, types: [short[], short[][]] */
    public DTDGrammar(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
    }

    public int getAttributeDeclIndex(int elementDeclIndex, String attributeDeclName) {
        int attDefIndex;
        if (elementDeclIndex == -1) {
            return -1;
        }
        int firstAttributeDeclIndex = getFirstAttributeDeclIndex(elementDeclIndex);
        while (true) {
            attDefIndex = firstAttributeDeclIndex;
            if (attDefIndex != -1) {
                getAttributeDecl(attDefIndex, this.fAttributeDecl);
                if (this.fAttributeDecl.name.rawname == attributeDeclName || attributeDeclName.equals(this.fAttributeDecl.name.rawname)) {
                    break;
                }
                firstAttributeDeclIndex = getNextAttributeDeclIndex(attDefIndex);
            } else {
                return -1;
            }
        }
        return attDefIndex;
    }

    public void startDTD(XMLLocator locator, Augmentations augs) throws XNIException {
    }

    public void elementDecl(String name, String contentModel, Augmentations augs) throws XNIException {
        XMLElementDecl tmpElementDecl = this.fElementDeclTab.get(name);
        if (tmpElementDecl != null) {
            if (tmpElementDecl.type == -1) {
                this.fCurrentElementIndex = getElementDeclIndex(name);
            } else {
                return;
            }
        } else {
            this.fCurrentElementIndex = createElementDecl();
        }
        XMLElementDecl elementDecl = new XMLElementDecl();
        QName elementName = new QName(null, name, name, null);
        elementDecl.name.setValues(elementName);
        elementDecl.scope = -1;
        if (contentModel.equals("EMPTY")) {
            elementDecl.type = (short) 1;
        } else if (contentModel.equals("ANY")) {
            elementDecl.type = (short) 0;
        } else if (contentModel.startsWith("(")) {
            if (contentModel.indexOf("#PCDATA") > 0) {
                elementDecl.type = (short) 2;
            } else {
                elementDecl.type = (short) 3;
            }
        }
        this.fElementDeclTab.put(name, elementDecl);
        this.fElementDecl = elementDecl;
        setElementDecl(this.fCurrentElementIndex, this.fElementDecl);
        int chunk = this.fCurrentElementIndex >> 8;
        ensureElementDeclCapacity(chunk);
    }

    public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs) throws XNIException {
        if (type != XMLSymbols.fCDATASymbol && defaultValue != null) {
            normalizeDefaultAttrValue(defaultValue);
        }
        if (!this.fElementDeclTab.containsKey(elementName)) {
            this.fCurrentElementIndex = createElementDecl();
            XMLElementDecl elementDecl = new XMLElementDecl();
            elementDecl.name.setValues(null, elementName, elementName, null);
            elementDecl.scope = -1;
            this.fElementDeclTab.put(elementName, elementDecl);
            setElementDecl(this.fCurrentElementIndex, elementDecl);
        }
        int elementIndex = getElementDeclIndex(elementName);
        if (getAttributeDeclIndex(elementIndex, attributeName) != -1) {
            return;
        }
        this.fCurrentAttributeIndex = createAttributeDecl();
        this.fSimpleType.clear();
        if (defaultType != null) {
            if (defaultType.equals("#FIXED")) {
                XMLSimpleType xMLSimpleType = this.fSimpleType;
                XMLSimpleType xMLSimpleType2 = this.fSimpleType;
                xMLSimpleType.defaultType = (short) 1;
            } else if (defaultType.equals("#IMPLIED")) {
                XMLSimpleType xMLSimpleType3 = this.fSimpleType;
                XMLSimpleType xMLSimpleType4 = this.fSimpleType;
                xMLSimpleType3.defaultType = (short) 0;
            } else if (defaultType.equals("#REQUIRED")) {
                XMLSimpleType xMLSimpleType5 = this.fSimpleType;
                XMLSimpleType xMLSimpleType6 = this.fSimpleType;
                xMLSimpleType5.defaultType = (short) 2;
            }
        }
        this.fSimpleType.defaultValue = defaultValue != null ? defaultValue.toString() : null;
        this.fSimpleType.nonNormalizedDefaultValue = nonNormalizedDefaultValue != null ? nonNormalizedDefaultValue.toString() : null;
        this.fSimpleType.enumeration = enumeration;
        if (type.equals("CDATA")) {
            this.fSimpleType.type = (short) 0;
        } else if (type.equals("ID")) {
            this.fSimpleType.type = (short) 3;
        } else if (type.startsWith(SchemaSymbols.ATTVAL_IDREF)) {
            this.fSimpleType.type = (short) 4;
            if (type.indexOf(PdfOps.S_TOKEN) > 0) {
                this.fSimpleType.list = true;
            }
        } else if (type.equals(SchemaSymbols.ATTVAL_ENTITIES)) {
            this.fSimpleType.type = (short) 1;
            this.fSimpleType.list = true;
        } else if (type.equals(SchemaSymbols.ATTVAL_ENTITY)) {
            this.fSimpleType.type = (short) 1;
        } else if (type.equals(SchemaSymbols.ATTVAL_NMTOKENS)) {
            this.fSimpleType.type = (short) 5;
            this.fSimpleType.list = true;
        } else if (type.equals(SchemaSymbols.ATTVAL_NMTOKEN)) {
            this.fSimpleType.type = (short) 5;
        } else if (type.startsWith(SchemaSymbols.ATTVAL_NOTATION)) {
            this.fSimpleType.type = (short) 6;
        } else if (type.startsWith("ENUMERATION")) {
            this.fSimpleType.type = (short) 2;
        } else {
            System.err.println("!!! unknown attribute type " + type);
        }
        this.fQName.setValues(null, attributeName, attributeName, null);
        this.fAttributeDecl.setValues(this.fQName, this.fSimpleType, false);
        setAttributeDecl(elementIndex, this.fCurrentAttributeIndex, this.fAttributeDecl);
        int chunk = this.fCurrentAttributeIndex >> 8;
        ensureAttributeDeclCapacity(chunk);
    }

    public SymbolTable getSymbolTable() {
        return this.fSymbolTable;
    }

    public int getFirstElementDeclIndex() {
        return this.fElementDeclCount >= 0 ? 0 : -1;
    }

    public int getNextElementDeclIndex(int elementDeclIndex) {
        if (elementDeclIndex < this.fElementDeclCount - 1) {
            return elementDeclIndex + 1;
        }
        return -1;
    }

    public int getElementDeclIndex(String elementDeclName) {
        Integer mapping = this.fElementIndexMap.get(elementDeclName);
        if (mapping == null) {
            mapping = -1;
        }
        return mapping.intValue();
    }

    public int getElementDeclIndex(QName elementDeclQName) {
        return getElementDeclIndex(elementDeclQName.rawname);
    }

    public short getContentSpecType(int elementIndex) {
        if (elementIndex < 0 || elementIndex >= this.fElementDeclCount) {
            return (short) -1;
        }
        int chunk = elementIndex >> 8;
        int index = elementIndex & 255;
        if (this.fElementDeclType[chunk][index] == -1) {
            return (short) -1;
        }
        return (short) (this.fElementDeclType[chunk][index] & LIST_MASK);
    }

    public boolean getElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return false;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        elementDecl.name.setValues(this.fElementDeclName[chunk][index]);
        if (this.fElementDeclType[chunk][index] == -1) {
            elementDecl.type = (short) -1;
            elementDecl.simpleType.list = false;
        } else {
            elementDecl.type = (short) (this.fElementDeclType[chunk][index] & LIST_MASK);
            elementDecl.simpleType.list = (this.fElementDeclType[chunk][index] & 128) != 0;
        }
        elementDecl.simpleType.defaultType = (short) -1;
        elementDecl.simpleType.defaultValue = null;
        return true;
    }

    public int getFirstAttributeDeclIndex(int elementDeclIndex) {
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        return this.fElementDeclFirstAttributeDeclIndex[chunk][index];
    }

    public int getNextAttributeDeclIndex(int attributeDeclIndex) {
        int chunk = attributeDeclIndex >> 8;
        int index = attributeDeclIndex & 255;
        return this.fAttributeDeclNextAttributeDeclIndex[chunk][index];
    }

    public boolean getAttributeDecl(int attributeDeclIndex, XMLAttributeDecl attributeDecl) {
        short attributeType;
        boolean isList;
        if (attributeDeclIndex < 0 || attributeDeclIndex >= this.fAttributeDeclCount) {
            return false;
        }
        int chunk = attributeDeclIndex >> 8;
        int index = attributeDeclIndex & 255;
        attributeDecl.name.setValues(this.fAttributeDeclName[chunk][index]);
        if (this.fAttributeDeclType[chunk][index] == -1) {
            attributeType = -1;
            isList = false;
        } else {
            attributeType = (short) (this.fAttributeDeclType[chunk][index] & LIST_MASK);
            isList = (this.fAttributeDeclType[chunk][index] & 128) != 0;
        }
        attributeDecl.simpleType.setValues(attributeType, this.fAttributeDeclName[chunk][index].localpart, this.fAttributeDeclEnumeration[chunk][index], isList, this.fAttributeDeclDefaultType[chunk][index], this.fAttributeDeclDefaultValue[chunk][index], this.fAttributeDeclNonNormalizedDefaultValue[chunk][index]);
        return true;
    }

    public boolean isCDATAAttribute(QName elName, QName atName) {
        int elDeclIdx = getElementDeclIndex(elName);
        if (getAttributeDecl(elDeclIdx, this.fAttributeDecl) && this.fAttributeDecl.simpleType.type != 0) {
            return false;
        }
        return true;
    }

    public void printElements() {
        int elementDeclIndex = 0;
        XMLElementDecl elementDecl = new XMLElementDecl();
        while (true) {
            int i2 = elementDeclIndex;
            elementDeclIndex++;
            if (getElementDecl(i2, elementDecl)) {
                System.out.println("element decl: " + ((Object) elementDecl.name) + ", " + elementDecl.name.rawname);
            } else {
                return;
            }
        }
    }

    public void printAttributes(int elementDeclIndex) {
        int attributeDeclIndex = getFirstAttributeDeclIndex(elementDeclIndex);
        System.out.print(elementDeclIndex);
        System.out.print(" [");
        while (attributeDeclIndex != -1) {
            System.out.print(' ');
            System.out.print(attributeDeclIndex);
            printAttribute(attributeDeclIndex);
            attributeDeclIndex = getNextAttributeDeclIndex(attributeDeclIndex);
            if (attributeDeclIndex != -1) {
                System.out.print(",");
            }
        }
        System.out.println(" ]");
    }

    protected int createElementDecl() {
        int chunk = this.fElementDeclCount >> 8;
        int index = this.fElementDeclCount & 255;
        ensureElementDeclCapacity(chunk);
        this.fElementDeclName[chunk][index] = new QName();
        this.fElementDeclType[chunk][index] = -1;
        this.fElementDeclFirstAttributeDeclIndex[chunk][index] = -1;
        this.fElementDeclLastAttributeDeclIndex[chunk][index] = -1;
        int i2 = this.fElementDeclCount;
        this.fElementDeclCount = i2 + 1;
        return i2;
    }

    protected void setElementDecl(int elementDeclIndex, XMLElementDecl elementDecl) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        int i2 = elementDecl.scope;
        this.fElementDeclName[chunk][index].setValues(elementDecl.name);
        this.fElementDeclType[chunk][index] = elementDecl.type;
        if (elementDecl.simpleType.list) {
            short[] sArr = this.fElementDeclType[chunk];
            sArr[index] = (short) (sArr[index] | 128);
        }
        this.fElementIndexMap.put(elementDecl.name.rawname, Integer.valueOf(elementDeclIndex));
    }

    protected void setFirstAttributeDeclIndex(int elementDeclIndex, int newFirstAttrIndex) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        this.fElementDeclFirstAttributeDeclIndex[chunk][index] = newFirstAttrIndex;
    }

    protected int createAttributeDecl() {
        int chunk = this.fAttributeDeclCount >> 8;
        int index = this.fAttributeDeclCount & 255;
        ensureAttributeDeclCapacity(chunk);
        this.fAttributeDeclName[chunk][index] = new QName();
        this.fAttributeDeclType[chunk][index] = -1;
        this.fAttributeDeclEnumeration[chunk][index] = null;
        this.fAttributeDeclDefaultType[chunk][index] = 0;
        this.fAttributeDeclDefaultValue[chunk][index] = null;
        this.fAttributeDeclNonNormalizedDefaultValue[chunk][index] = null;
        this.fAttributeDeclNextAttributeDeclIndex[chunk][index] = -1;
        int i2 = this.fAttributeDeclCount;
        this.fAttributeDeclCount = i2 + 1;
        return i2;
    }

    protected void setAttributeDecl(int elementDeclIndex, int attributeDeclIndex, XMLAttributeDecl attributeDecl) {
        int index;
        int attrChunk = attributeDeclIndex >> 8;
        int attrIndex = attributeDeclIndex & 255;
        this.fAttributeDeclName[attrChunk][attrIndex].setValues(attributeDecl.name);
        this.fAttributeDeclType[attrChunk][attrIndex] = attributeDecl.simpleType.type;
        if (attributeDecl.simpleType.list) {
            short[] sArr = this.fAttributeDeclType[attrChunk];
            sArr[attrIndex] = (short) (sArr[attrIndex] | 128);
        }
        this.fAttributeDeclEnumeration[attrChunk][attrIndex] = attributeDecl.simpleType.enumeration;
        this.fAttributeDeclDefaultType[attrChunk][attrIndex] = attributeDecl.simpleType.defaultType;
        this.fAttributeDeclDefaultValue[attrChunk][attrIndex] = attributeDecl.simpleType.defaultValue;
        this.fAttributeDeclNonNormalizedDefaultValue[attrChunk][attrIndex] = attributeDecl.simpleType.nonNormalizedDefaultValue;
        int elemChunk = elementDeclIndex >> 8;
        int elemIndex = elementDeclIndex & 255;
        int i2 = this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex];
        while (true) {
            index = i2;
            if (index == -1 || index == attributeDeclIndex) {
                break;
            }
            i2 = this.fAttributeDeclNextAttributeDeclIndex[index >> 8][index & 255];
        }
        if (index == -1) {
            if (this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] == -1) {
                this.fElementDeclFirstAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
            } else {
                int index2 = this.fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex];
                int attrChunk2 = index2 >> 8;
                this.fAttributeDeclNextAttributeDeclIndex[attrChunk2][index2 & 255] = attributeDeclIndex;
            }
            this.fElementDeclLastAttributeDeclIndex[elemChunk][elemIndex] = attributeDeclIndex;
        }
    }

    public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        XMLNotationDecl notationDecl = new XMLNotationDecl();
        notationDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId());
        this.notationDecls.add(notationDecl);
    }

    public List getNotationDecls() {
        return this.notationDecls;
    }

    private void printAttribute(int attributeDeclIndex) {
        XMLAttributeDecl attributeDecl = new XMLAttributeDecl();
        if (getAttributeDecl(attributeDeclIndex, attributeDecl)) {
            System.out.print(" { ");
            System.out.print(attributeDecl.name.localpart);
            System.out.print(" }");
        }
    }

    private void ensureElementDeclCapacity(int chunk) {
        if (chunk >= this.fElementDeclName.length) {
            this.fElementDeclName = resize(this.fElementDeclName, this.fElementDeclName.length * 2);
            this.fElementDeclType = resize(this.fElementDeclType, this.fElementDeclType.length * 2);
            this.fElementDeclFirstAttributeDeclIndex = resize(this.fElementDeclFirstAttributeDeclIndex, this.fElementDeclFirstAttributeDeclIndex.length * 2);
            this.fElementDeclLastAttributeDeclIndex = resize(this.fElementDeclLastAttributeDeclIndex, this.fElementDeclLastAttributeDeclIndex.length * 2);
        } else if (this.fElementDeclName[chunk] != null) {
            return;
        }
        this.fElementDeclName[chunk] = new QName[256];
        this.fElementDeclType[chunk] = new short[256];
        this.fElementDeclFirstAttributeDeclIndex[chunk] = new int[256];
        this.fElementDeclLastAttributeDeclIndex[chunk] = new int[256];
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void ensureAttributeDeclCapacity(int chunk) {
        if (chunk >= this.fAttributeDeclName.length) {
            this.fAttributeDeclName = resize(this.fAttributeDeclName, this.fAttributeDeclName.length * 2);
            this.fAttributeDeclType = resize(this.fAttributeDeclType, this.fAttributeDeclType.length * 2);
            this.fAttributeDeclEnumeration = resize(this.fAttributeDeclEnumeration, this.fAttributeDeclEnumeration.length * 2);
            this.fAttributeDeclDefaultType = resize(this.fAttributeDeclDefaultType, this.fAttributeDeclDefaultType.length * 2);
            this.fAttributeDeclDefaultValue = resize(this.fAttributeDeclDefaultValue, this.fAttributeDeclDefaultValue.length * 2);
            this.fAttributeDeclNonNormalizedDefaultValue = resize(this.fAttributeDeclNonNormalizedDefaultValue, this.fAttributeDeclNonNormalizedDefaultValue.length * 2);
            this.fAttributeDeclNextAttributeDeclIndex = resize(this.fAttributeDeclNextAttributeDeclIndex, this.fAttributeDeclNextAttributeDeclIndex.length * 2);
        } else if (this.fAttributeDeclName[chunk] != null) {
            return;
        }
        this.fAttributeDeclName[chunk] = new QName[256];
        this.fAttributeDeclType[chunk] = new short[256];
        this.fAttributeDeclEnumeration[chunk] = new String[256];
        this.fAttributeDeclDefaultType[chunk] = new short[256];
        this.fAttributeDeclDefaultValue[chunk] = new String[256];
        this.fAttributeDeclNonNormalizedDefaultValue[chunk] = new String[256];
        this.fAttributeDeclNextAttributeDeclIndex[chunk] = new int[256];
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, short[], short[][]] */
    private static short[][] resize(short[][] array, int newsize) {
        ?? r0 = new short[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [int[], int[][], java.lang.Object] */
    private static int[][] resize(int[][] array, int newsize) {
        ?? r0 = new int[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.xni.QName[], com.sun.org.apache.xerces.internal.xni.QName[][], java.lang.Object] */
    private static QName[][] resize(QName[][] array, int newsize) {
        ?? r0 = new QName[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, java.lang.String[], java.lang.String[][]] */
    private static String[][] resize(String[][] array, int newsize) {
        ?? r0 = new String[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, java.lang.String[][], java.lang.String[][][]] */
    private static String[][][] resize(String[][][] array, int newsize) {
        ?? r0 = new String[newsize][];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    private boolean normalizeDefaultAttrValue(XMLString value) {
        int i2 = value.length;
        boolean skipSpace = true;
        int current = value.offset;
        int end = value.offset + value.length;
        for (int i3 = value.offset; i3 < end; i3++) {
            if (value.ch[i3] == ' ') {
                if (!skipSpace) {
                    int i4 = current;
                    current++;
                    value.ch[i4] = ' ';
                    skipSpace = true;
                }
            } else {
                if (current != i3) {
                    value.ch[current] = value.ch[i3];
                }
                current++;
                skipSpace = false;
            }
        }
        if (current != end) {
            if (skipSpace) {
                current--;
            }
            value.length = current - value.offset;
            return true;
        }
        return false;
    }

    public void endDTD(Augmentations augs) throws XNIException {
    }
}
