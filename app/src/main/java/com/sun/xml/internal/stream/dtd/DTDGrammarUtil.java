package com.sun.xml.internal.stream.dtd;

import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLComponentManager;
import com.sun.org.apache.xerces.internal.xni.parser.XMLConfigurationException;
import com.sun.xml.internal.stream.dtd.nonvalidating.DTDGrammar;
import com.sun.xml.internal.stream.dtd.nonvalidating.XMLAttributeDecl;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/stream/dtd/DTDGrammarUtil.class */
public class DTDGrammarUtil {
    protected static final String SYMBOL_TABLE = "http://apache.org/xml/properties/internal/symbol-table";
    protected static final String NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final boolean DEBUG_ATTRIBUTES = false;
    private static final boolean DEBUG_ELEMENT_CHILDREN = false;
    protected DTDGrammar fDTDGrammar;
    protected boolean fNamespaces;
    protected SymbolTable fSymbolTable;
    private int fCurrentElementIndex;
    private int fCurrentContentSpecType;
    private boolean[] fElementContentState;
    private int fElementDepth;
    private boolean fInElementContent;
    private XMLAttributeDecl fTempAttDecl;
    private QName fTempQName;
    private StringBuffer fBuffer;
    private NamespaceContext fNamespaceContext;

    public DTDGrammarUtil(SymbolTable symbolTable) {
        this.fDTDGrammar = null;
        this.fSymbolTable = null;
        this.fCurrentElementIndex = -1;
        this.fCurrentContentSpecType = -1;
        this.fElementContentState = new boolean[8];
        this.fElementDepth = -1;
        this.fInElementContent = false;
        this.fTempAttDecl = new XMLAttributeDecl();
        this.fTempQName = new QName();
        this.fBuffer = new StringBuffer();
        this.fNamespaceContext = null;
        this.fSymbolTable = symbolTable;
    }

    public DTDGrammarUtil(DTDGrammar grammar, SymbolTable symbolTable) {
        this.fDTDGrammar = null;
        this.fSymbolTable = null;
        this.fCurrentElementIndex = -1;
        this.fCurrentContentSpecType = -1;
        this.fElementContentState = new boolean[8];
        this.fElementDepth = -1;
        this.fInElementContent = false;
        this.fTempAttDecl = new XMLAttributeDecl();
        this.fTempQName = new QName();
        this.fBuffer = new StringBuffer();
        this.fNamespaceContext = null;
        this.fDTDGrammar = grammar;
        this.fSymbolTable = symbolTable;
    }

    public DTDGrammarUtil(DTDGrammar grammar, SymbolTable symbolTable, NamespaceContext namespaceContext) {
        this.fDTDGrammar = null;
        this.fSymbolTable = null;
        this.fCurrentElementIndex = -1;
        this.fCurrentContentSpecType = -1;
        this.fElementContentState = new boolean[8];
        this.fElementDepth = -1;
        this.fInElementContent = false;
        this.fTempAttDecl = new XMLAttributeDecl();
        this.fTempQName = new QName();
        this.fBuffer = new StringBuffer();
        this.fNamespaceContext = null;
        this.fDTDGrammar = grammar;
        this.fSymbolTable = symbolTable;
        this.fNamespaceContext = namespaceContext;
    }

    public void reset(XMLComponentManager componentManager) throws XMLConfigurationException {
        this.fDTDGrammar = null;
        this.fInElementContent = false;
        this.fCurrentElementIndex = -1;
        this.fCurrentContentSpecType = -1;
        this.fNamespaces = componentManager.getFeature("http://xml.org/sax/features/namespaces", true);
        this.fSymbolTable = (SymbolTable) componentManager.getProperty("http://apache.org/xml/properties/internal/symbol-table");
        this.fElementDepth = -1;
    }

    public void startElement(QName element, XMLAttributes attributes) throws XNIException {
        handleStartElement(element, attributes);
    }

    public void endElement(QName element) throws XNIException {
        handleEndElement(element);
    }

    public void startCDATA(Augmentations augs) throws XNIException {
    }

    public void endCDATA(Augmentations augs) throws XNIException {
    }

    public void addDTDDefaultAttrs(QName elementName, XMLAttributes attributes) throws XNIException {
        String prefix;
        int index;
        int elementIndex = this.fDTDGrammar.getElementDeclIndex(elementName);
        if (elementIndex == -1 || this.fDTDGrammar == null) {
            return;
        }
        int firstAttributeDeclIndex = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
        while (true) {
            int attlistIndex = firstAttributeDeclIndex;
            if (attlistIndex == -1) {
                break;
            }
            this.fDTDGrammar.getAttributeDecl(attlistIndex, this.fTempAttDecl);
            String attPrefix = this.fTempAttDecl.name.prefix;
            String attLocalpart = this.fTempAttDecl.name.localpart;
            String attRawName = this.fTempAttDecl.name.rawname;
            String attType = getAttributeTypeName(this.fTempAttDecl);
            int attDefaultType = this.fTempAttDecl.simpleType.defaultType;
            String attValue = null;
            if (this.fTempAttDecl.simpleType.defaultValue != null) {
                attValue = this.fTempAttDecl.simpleType.defaultValue;
            }
            boolean specified = false;
            boolean required = attDefaultType == 2;
            boolean cdata = attType == XMLSymbols.fCDATASymbol;
            if (!cdata || required || attValue != null) {
                if (this.fNamespaceContext != null && attRawName.startsWith("xmlns")) {
                    int pos = attRawName.indexOf(58);
                    if (pos != -1) {
                        prefix = attRawName.substring(0, pos);
                    } else {
                        prefix = attRawName;
                    }
                    String prefix2 = this.fSymbolTable.addSymbol(prefix);
                    if (!((NamespaceSupport) this.fNamespaceContext).containsPrefixInCurrentContext(prefix2)) {
                        this.fNamespaceContext.declarePrefix(prefix2, attValue);
                    }
                    specified = true;
                } else {
                    int attrCount = attributes.getLength();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= attrCount) {
                            break;
                        }
                        if (attributes.getQName(i2) != attRawName) {
                            i2++;
                        } else {
                            specified = true;
                            break;
                        }
                    }
                }
            }
            if (!specified && attValue != null) {
                if (this.fNamespaces && (index = attRawName.indexOf(58)) != -1) {
                    attPrefix = this.fSymbolTable.addSymbol(attRawName.substring(0, index));
                    attLocalpart = this.fSymbolTable.addSymbol(attRawName.substring(index + 1));
                }
                this.fTempQName.setValues(attPrefix, attLocalpart, attRawName, this.fTempAttDecl.name.uri);
                attributes.addAttribute(this.fTempQName, attType, attValue);
            }
            firstAttributeDeclIndex = this.fDTDGrammar.getNextAttributeDeclIndex(attlistIndex);
        }
        int attrCount2 = attributes.getLength();
        for (int i3 = 0; i3 < attrCount2; i3++) {
            String attrRawName = attributes.getQName(i3);
            boolean declared = false;
            int firstAttributeDeclIndex2 = this.fDTDGrammar.getFirstAttributeDeclIndex(elementIndex);
            while (true) {
                int position = firstAttributeDeclIndex2;
                if (position == -1) {
                    break;
                }
                this.fDTDGrammar.getAttributeDecl(position, this.fTempAttDecl);
                if (this.fTempAttDecl.name.rawname == attrRawName) {
                    declared = true;
                    break;
                }
                firstAttributeDeclIndex2 = this.fDTDGrammar.getNextAttributeDeclIndex(position);
            }
            if (declared) {
                String type = getAttributeTypeName(this.fTempAttDecl);
                attributes.setType(i3, type);
                if (attributes.isSpecified(i3) && type != XMLSymbols.fCDATASymbol) {
                    normalizeAttrValue(attributes, i3);
                }
            }
        }
    }

    private boolean normalizeAttrValue(XMLAttributes attributes, int index) {
        boolean leadingSpace = true;
        boolean spaceStart = false;
        boolean readingNonSpace = false;
        int count = 0;
        int eaten = 0;
        String attrValue = attributes.getValue(index);
        char[] attValue = new char[attrValue.length()];
        this.fBuffer.setLength(0);
        attrValue.getChars(0, attrValue.length(), attValue, 0);
        for (int i2 = 0; i2 < attValue.length; i2++) {
            if (attValue[i2] == ' ') {
                if (readingNonSpace) {
                    spaceStart = true;
                    readingNonSpace = false;
                }
                if (spaceStart && !leadingSpace) {
                    spaceStart = false;
                    this.fBuffer.append(attValue[i2]);
                    count++;
                } else if (leadingSpace || !spaceStart) {
                    eaten++;
                }
            } else {
                readingNonSpace = true;
                spaceStart = false;
                leadingSpace = false;
                this.fBuffer.append(attValue[i2]);
                count++;
            }
        }
        if (count > 0 && this.fBuffer.charAt(count - 1) == ' ') {
            this.fBuffer.setLength(count - 1);
        }
        String newValue = this.fBuffer.toString();
        attributes.setValue(index, newValue);
        return !attrValue.equals(newValue);
    }

    private String getAttributeTypeName(XMLAttributeDecl attrDecl) {
        switch (attrDecl.simpleType.type) {
            case 1:
                return attrDecl.simpleType.list ? XMLSymbols.fENTITIESSymbol : XMLSymbols.fENTITYSymbol;
            case 2:
                StringBuffer buffer = new StringBuffer();
                buffer.append('(');
                for (int i2 = 0; i2 < attrDecl.simpleType.enumeration.length; i2++) {
                    if (i2 > 0) {
                        buffer.append(CallSiteDescriptor.OPERATOR_DELIMITER);
                    }
                    buffer.append(attrDecl.simpleType.enumeration[i2]);
                }
                buffer.append(')');
                return this.fSymbolTable.addSymbol(buffer.toString());
            case 3:
                return XMLSymbols.fIDSymbol;
            case 4:
                return attrDecl.simpleType.list ? XMLSymbols.fIDREFSSymbol : XMLSymbols.fIDREFSymbol;
            case 5:
                return attrDecl.simpleType.list ? XMLSymbols.fNMTOKENSSymbol : XMLSymbols.fNMTOKENSymbol;
            case 6:
                return XMLSymbols.fNOTATIONSymbol;
            default:
                return XMLSymbols.fCDATASymbol;
        }
    }

    private void ensureStackCapacity(int newElementDepth) {
        if (newElementDepth == this.fElementContentState.length) {
            boolean[] newStack = new boolean[newElementDepth * 2];
            System.arraycopy(this.fElementContentState, 0, newStack, 0, newElementDepth);
            this.fElementContentState = newStack;
        }
    }

    protected void handleStartElement(QName element, XMLAttributes attributes) throws XNIException {
        if (this.fDTDGrammar == null) {
            this.fCurrentElementIndex = -1;
            this.fCurrentContentSpecType = -1;
            this.fInElementContent = false;
            return;
        }
        this.fCurrentElementIndex = this.fDTDGrammar.getElementDeclIndex(element);
        this.fCurrentContentSpecType = this.fDTDGrammar.getContentSpecType(this.fCurrentElementIndex);
        addDTDDefaultAttrs(element, attributes);
        this.fInElementContent = this.fCurrentContentSpecType == 3;
        this.fElementDepth++;
        ensureStackCapacity(this.fElementDepth);
        this.fElementContentState[this.fElementDepth] = this.fInElementContent;
    }

    protected void handleEndElement(QName element) throws XNIException {
        if (this.fDTDGrammar == null) {
            return;
        }
        this.fElementDepth--;
        if (this.fElementDepth < -1) {
            throw new RuntimeException("FWK008 Element stack underflow");
        }
        if (this.fElementDepth < 0) {
            this.fCurrentElementIndex = -1;
            this.fCurrentContentSpecType = -1;
            this.fInElementContent = false;
            return;
        }
        this.fInElementContent = this.fElementContentState[this.fElementDepth];
    }

    public boolean isInElementContent() {
        return this.fInElementContent;
    }

    public boolean isIgnorableWhiteSpace(XMLString text) {
        if (isInElementContent()) {
            for (int i2 = text.offset; i2 < text.offset + text.length; i2++) {
                if (!XMLChar.isSpace(text.ch[i2])) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
