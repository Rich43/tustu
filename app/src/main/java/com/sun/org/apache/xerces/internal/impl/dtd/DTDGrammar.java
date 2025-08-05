package com.sun.org.apache.xerces.internal.impl.dtd;

import com.sun.org.apache.xerces.internal.impl.dtd.models.CMAny;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMBinOp;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMLeaf;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMNode;
import com.sun.org.apache.xerces.internal.impl.dtd.models.CMUniOp;
import com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator;
import com.sun.org.apache.xerces.internal.impl.dtd.models.DFAContentModel;
import com.sun.org.apache.xerces.internal.impl.dtd.models.MixedContentModel;
import com.sun.org.apache.xerces.internal.impl.dtd.models.SimpleContentModel;
import com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator;
import com.sun.org.apache.xerces.internal.impl.validation.EntityState;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler;
import com.sun.org.apache.xerces.internal.xni.XMLDTDHandler;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDContentModelSource;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDTDSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/DTDGrammar.class */
public class DTDGrammar implements XMLDTDHandler, XMLDTDContentModelHandler, EntityState, Grammar {
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
    protected XMLDTDDescription fGrammarDescription;
    private boolean fMixed;
    protected XMLDTDSource fDTDSource = null;
    protected XMLDTDContentModelSource fDTDContentModelSource = null;
    protected boolean fReadingExternalDTD = false;
    private int fElementDeclCount = 0;
    private QName[][] fElementDeclName = new QName[4];
    private short[][] fElementDeclType = new short[4];
    private int[][] fElementDeclContentSpecIndex = new int[4];
    private ContentModelValidator[][] fElementDeclContentModelValidator = new ContentModelValidator[4];
    private int[][] fElementDeclFirstAttributeDeclIndex = new int[4];
    private int[][] fElementDeclLastAttributeDeclIndex = new int[4];
    private int fAttributeDeclCount = 0;
    private QName[][] fAttributeDeclName = new QName[4];
    private boolean fIsImmutable = false;
    private short[][] fAttributeDeclType = new short[4];
    private String[][][] fAttributeDeclEnumeration = new String[4][];
    private short[][] fAttributeDeclDefaultType = new short[4];
    private DatatypeValidator[][] fAttributeDeclDatatypeValidator = new DatatypeValidator[4];
    private String[][] fAttributeDeclDefaultValue = new String[4];
    private String[][] fAttributeDeclNonNormalizedDefaultValue = new String[4];
    private int[][] fAttributeDeclNextAttributeDeclIndex = new int[4];
    private int fContentSpecCount = 0;
    private short[][] fContentSpecType = new short[4];
    private Object[][] fContentSpecValue = new Object[4];
    private Object[][] fContentSpecOtherValue = new Object[4];
    private int fEntityCount = 0;
    private String[][] fEntityName = new String[4];
    private String[][] fEntityValue = new String[4];
    private String[][] fEntityPublicId = new String[4];
    private String[][] fEntitySystemId = new String[4];
    private String[][] fEntityBaseSystemId = new String[4];
    private String[][] fEntityNotation = new String[4];
    private byte[][] fEntityIsPE = new byte[4];
    private byte[][] fEntityInExternal = new byte[4];
    private int fNotationCount = 0;
    private String[][] fNotationName = new String[4];
    private String[][] fNotationPublicId = new String[4];
    private String[][] fNotationSystemId = new String[4];
    private String[][] fNotationBaseSystemId = new String[4];
    private final Map<String, Integer> fElementIndexMap = new HashMap();
    private final Map<String, Integer> fEntityIndexMap = new HashMap();
    private final Map<String, Integer> fNotationIndexMap = new HashMap();
    private final QName fQName = new QName();
    private final QName fQName2 = new QName();
    protected final XMLAttributeDecl fAttributeDecl = new XMLAttributeDecl();
    private int fLeafCount = 0;
    private int fEpsilonIndex = -1;
    private XMLElementDecl fElementDecl = new XMLElementDecl();
    private XMLEntityDecl fEntityDecl = new XMLEntityDecl();
    private XMLSimpleType fSimpleType = new XMLSimpleType();
    private XMLContentSpec fContentSpec = new XMLContentSpec();
    Map<String, XMLElementDecl> fElementDeclTab = new HashMap();
    private short[] fOpStack = null;
    private int[] fNodeIndexStack = null;
    private int[] fPrevNodeIndexStack = null;
    private int fDepth = 0;
    private boolean[] fPEntityStack = new boolean[4];
    private int fPEDepth = 0;
    private int[][] fElementDeclIsExternal = new int[4];
    private int[][] fAttributeDeclIsExternal = new int[4];
    int valueIndex = -1;
    int prevNodeIndex = -1;
    int nodeIndex = -1;

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dtd/DTDGrammar$ChildrenList.class */
    private static class ChildrenList {
        public int length = 0;
        public QName[] qname = new QName[2];
        public int[] type = new int[2];
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v12, types: [com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator[], com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator[][]] */
    /* JADX WARN: Type inference failed for: r1v14, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v16, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v19, types: [com.sun.org.apache.xerces.internal.xni.QName[], com.sun.org.apache.xerces.internal.xni.QName[][]] */
    /* JADX WARN: Type inference failed for: r1v22, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v24, types: [java.lang.String[][], java.lang.String[][][]] */
    /* JADX WARN: Type inference failed for: r1v26, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v28, types: [com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator[], com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator[][]] */
    /* JADX WARN: Type inference failed for: r1v30, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v32, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v34, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v37, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v39, types: [java.lang.Object[], java.lang.Object[][]] */
    /* JADX WARN: Type inference failed for: r1v41, types: [java.lang.Object[], java.lang.Object[][]] */
    /* JADX WARN: Type inference failed for: r1v44, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v46, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v48, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v50, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v52, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v54, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v56, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v58, types: [byte[], byte[][]] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.sun.org.apache.xerces.internal.xni.QName[], com.sun.org.apache.xerces.internal.xni.QName[][]] */
    /* JADX WARN: Type inference failed for: r1v61, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v63, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v65, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v67, types: [java.lang.String[], java.lang.String[][]] */
    /* JADX WARN: Type inference failed for: r1v8, types: [short[], short[][]] */
    /* JADX WARN: Type inference failed for: r1v89, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v91, types: [int[], int[][]] */
    public DTDGrammar(SymbolTable symbolTable, XMLDTDDescription desc) {
        this.fGrammarDescription = null;
        this.fSymbolTable = symbolTable;
        this.fGrammarDescription = desc;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.Grammar
    public XMLGrammarDescription getGrammarDescription() {
        return this.fGrammarDescription;
    }

    public boolean getElementDeclIsExternal(int elementDeclIndex) {
        if (elementDeclIndex < 0) {
            return false;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        return this.fElementDeclIsExternal[chunk][index] != 0;
    }

    public boolean getAttributeDeclIsExternal(int attributeDeclIndex) {
        if (attributeDeclIndex < 0) {
            return false;
        }
        int chunk = attributeDeclIndex >> 8;
        int index = attributeDeclIndex & 255;
        return this.fAttributeDeclIsExternal[chunk][index] != 0;
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

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startDTD(XMLLocator locator, Augmentations augs) throws XNIException {
        this.fOpStack = null;
        this.fNodeIndexStack = null;
        this.fPrevNodeIndexStack = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startParameterEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
        if (this.fPEDepth == this.fPEntityStack.length) {
            boolean[] entityarray = new boolean[this.fPEntityStack.length * 2];
            System.arraycopy(this.fPEntityStack, 0, entityarray, 0, this.fPEntityStack.length);
            this.fPEntityStack = entityarray;
        }
        this.fPEntityStack[this.fPEDepth] = this.fReadingExternalDTD;
        this.fPEDepth++;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startExternalSubset(XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        this.fReadingExternalDTD = true;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endParameterEntity(String name, Augmentations augs) throws XNIException {
        if (this.fPEDepth > 0) {
            this.fPEDepth--;
            this.fReadingExternalDTD = this.fPEntityStack[this.fPEDepth];
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endExternalSubset(Augmentations augs) throws XNIException {
        this.fReadingExternalDTD = false;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
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
        this.fQName.setValues(null, name, name, null);
        elementDecl.name.setValues(this.fQName);
        elementDecl.contentModelValidator = null;
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
        addContentSpecToElement(elementDecl);
        setElementDecl(this.fCurrentElementIndex, this.fElementDecl);
        int chunk = this.fCurrentElementIndex >> 8;
        int index = this.fCurrentElementIndex & 255;
        ensureElementDeclCapacity(chunk);
        this.fElementDeclIsExternal[chunk][index] = (this.fReadingExternalDTD || this.fPEDepth > 0) ? 1 : 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void attributeDecl(String elementName, String attributeName, String type, String[] enumeration, String defaultType, XMLString defaultValue, XMLString nonNormalizedDefaultValue, Augmentations augs) throws XNIException {
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
                this.fSimpleType.defaultType = (short) 1;
            } else if (defaultType.equals("#IMPLIED")) {
                this.fSimpleType.defaultType = (short) 0;
            } else if (defaultType.equals("#REQUIRED")) {
                this.fSimpleType.defaultType = (short) 2;
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
        int index = this.fCurrentAttributeIndex & 255;
        ensureAttributeDeclCapacity(chunk);
        this.fAttributeDeclIsExternal[chunk][index] = (this.fReadingExternalDTD || this.fPEDepth > 0) ? 1 : 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void internalEntityDecl(String name, XMLString text, XMLString nonNormalizedText, Augmentations augs) throws XNIException {
        int entityIndex = getEntityDeclIndex(name);
        if (entityIndex == -1) {
            int entityIndex2 = createEntityDecl();
            boolean isPE = name.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX);
            boolean inExternal = this.fReadingExternalDTD || this.fPEDepth > 0;
            XMLEntityDecl entityDecl = new XMLEntityDecl();
            entityDecl.setValues(name, null, null, null, null, text.toString(), isPE, inExternal);
            setEntityDecl(entityIndex2, entityDecl);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void externalEntityDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        int entityIndex = getEntityDeclIndex(name);
        if (entityIndex == -1) {
            int entityIndex2 = createEntityDecl();
            boolean isPE = name.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX);
            boolean inExternal = this.fReadingExternalDTD || this.fPEDepth > 0;
            XMLEntityDecl entityDecl = new XMLEntityDecl();
            entityDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId(), null, null, isPE, inExternal);
            setEntityDecl(entityIndex2, entityDecl);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void unparsedEntityDecl(String name, XMLResourceIdentifier identifier, String notation, Augmentations augs) throws XNIException {
        XMLEntityDecl entityDecl = new XMLEntityDecl();
        boolean isPE = name.startsWith(FXMLLoader.RESOURCE_KEY_PREFIX);
        boolean inExternal = this.fReadingExternalDTD || this.fPEDepth > 0;
        entityDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId(), notation, null, isPE, inExternal);
        int entityIndex = getEntityDeclIndex(name);
        if (entityIndex == -1) {
            int entityIndex2 = createEntityDecl();
            setEntityDecl(entityIndex2, entityDecl);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void notationDecl(String name, XMLResourceIdentifier identifier, Augmentations augs) throws XNIException {
        XMLNotationDecl notationDecl = new XMLNotationDecl();
        notationDecl.setValues(name, identifier.getPublicId(), identifier.getLiteralSystemId(), identifier.getBaseSystemId());
        int notationIndex = getNotationDeclIndex(name);
        if (notationIndex == -1) {
            int notationIndex2 = createNotationDecl();
            setNotationDecl(notationIndex2, notationDecl);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endDTD(Augmentations augs) throws XNIException {
        this.fIsImmutable = true;
        if (this.fGrammarDescription.getRootName() == null) {
            int size = this.fElementDeclCount;
            ArrayList elements = new ArrayList(size);
            for (int i2 = 0; i2 < size; i2++) {
                int chunk = i2 >> 8;
                int index = i2 & 255;
                String currName = this.fElementDeclName[chunk][index].rawname;
                elements.add(currName);
            }
            this.fGrammarDescription.setPossibleRoots(elements);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void setDTDSource(XMLDTDSource source) {
        this.fDTDSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public XMLDTDSource getDTDSource() {
        return this.fDTDSource;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startAttlist(String elementName, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endAttlist(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void startConditional(short type, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void ignoredCharacters(XMLString text, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDHandler
    public void endConditional(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void setDTDContentModelSource(XMLDTDContentModelSource source) {
        this.fDTDContentModelSource = source;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public XMLDTDContentModelSource getDTDContentModelSource() {
        return this.fDTDContentModelSource;
    }

    public void startContentModel(String elementName, Augmentations augs) throws XNIException {
        XMLElementDecl elementDecl = this.fElementDeclTab.get(elementName);
        if (elementDecl != null) {
            this.fElementDecl = elementDecl;
        }
        this.fDepth = 0;
        initializeContentModelStack();
    }

    public void startGroup(Augmentations augs) throws XNIException {
        this.fDepth++;
        initializeContentModelStack();
        this.fMixed = false;
    }

    public void pcdata(Augmentations augs) throws XNIException {
        this.fMixed = true;
    }

    public void element(String elementName, Augmentations augs) throws XNIException {
        if (this.fMixed) {
            if (this.fNodeIndexStack[this.fDepth] == -1) {
                this.fNodeIndexStack[this.fDepth] = addUniqueLeafNode(elementName);
                return;
            } else {
                this.fNodeIndexStack[this.fDepth] = addContentSpecNode((short) 4, this.fNodeIndexStack[this.fDepth], addUniqueLeafNode(elementName));
                return;
            }
        }
        this.fNodeIndexStack[this.fDepth] = addContentSpecNode((short) 0, elementName);
    }

    public void separator(short separator, Augmentations augs) throws XNIException {
        if (!this.fMixed) {
            if (this.fOpStack[this.fDepth] != 5 && separator == 0) {
                if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
                    this.fNodeIndexStack[this.fDepth] = addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
                }
                this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
                this.fOpStack[this.fDepth] = 4;
                return;
            }
            if (this.fOpStack[this.fDepth] != 4 && separator == 1) {
                if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
                    this.fNodeIndexStack[this.fDepth] = addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
                }
                this.fPrevNodeIndexStack[this.fDepth] = this.fNodeIndexStack[this.fDepth];
                this.fOpStack[this.fDepth] = 5;
            }
        }
    }

    public void occurrence(short occurrence, Augmentations augs) throws XNIException {
        if (!this.fMixed) {
            if (occurrence == 2) {
                this.fNodeIndexStack[this.fDepth] = addContentSpecNode((short) 1, this.fNodeIndexStack[this.fDepth], -1);
            } else if (occurrence == 3) {
                this.fNodeIndexStack[this.fDepth] = addContentSpecNode((short) 2, this.fNodeIndexStack[this.fDepth], -1);
            } else if (occurrence == 4) {
                this.fNodeIndexStack[this.fDepth] = addContentSpecNode((short) 3, this.fNodeIndexStack[this.fDepth], -1);
            }
        }
    }

    public void endGroup(Augmentations augs) throws XNIException {
        if (!this.fMixed) {
            if (this.fPrevNodeIndexStack[this.fDepth] != -1) {
                this.fNodeIndexStack[this.fDepth] = addContentSpecNode(this.fOpStack[this.fDepth], this.fPrevNodeIndexStack[this.fDepth], this.fNodeIndexStack[this.fDepth]);
            }
            int[] iArr = this.fNodeIndexStack;
            int i2 = this.fDepth;
            this.fDepth = i2 - 1;
            int nodeIndex = iArr[i2];
            this.fNodeIndexStack[this.fDepth] = nodeIndex;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void any(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void empty(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDTDContentModelHandler
    public void endContentModel(Augmentations augs) throws XNIException {
    }

    public boolean isNamespaceAware() {
        return false;
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
        if (elementDecl.type == 3 || elementDecl.type == 2) {
            elementDecl.contentModelValidator = getElementContentModelValidator(elementDeclIndex);
        }
        elementDecl.simpleType.datatypeValidator = null;
        elementDecl.simpleType.defaultType = (short) -1;
        elementDecl.simpleType.defaultValue = null;
        return true;
    }

    QName getElementDeclName(int elementDeclIndex) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return null;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        return this.fElementDeclName[chunk][index];
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
        attributeDecl.simpleType.setValues(attributeType, this.fAttributeDeclName[chunk][index].localpart, this.fAttributeDeclEnumeration[chunk][index], isList, this.fAttributeDeclDefaultType[chunk][index], this.fAttributeDeclDefaultValue[chunk][index], this.fAttributeDeclNonNormalizedDefaultValue[chunk][index], this.fAttributeDeclDatatypeValidator[chunk][index]);
        return true;
    }

    public boolean isCDATAAttribute(QName elName, QName atName) {
        int elDeclIdx = getElementDeclIndex(elName);
        if (getAttributeDecl(elDeclIdx, this.fAttributeDecl) && this.fAttributeDecl.simpleType.type != 0) {
            return false;
        }
        return true;
    }

    public int getEntityDeclIndex(String entityDeclName) {
        if (entityDeclName == null || this.fEntityIndexMap.get(entityDeclName) == null) {
            return -1;
        }
        return this.fEntityIndexMap.get(entityDeclName).intValue();
    }

    public boolean getEntityDecl(int entityDeclIndex, XMLEntityDecl entityDecl) {
        if (entityDeclIndex < 0 || entityDeclIndex >= this.fEntityCount) {
            return false;
        }
        int chunk = entityDeclIndex >> 8;
        int index = entityDeclIndex & 255;
        entityDecl.setValues(this.fEntityName[chunk][index], this.fEntityPublicId[chunk][index], this.fEntitySystemId[chunk][index], this.fEntityBaseSystemId[chunk][index], this.fEntityNotation[chunk][index], this.fEntityValue[chunk][index], this.fEntityIsPE[chunk][index] != 0, this.fEntityInExternal[chunk][index] != 0);
        return true;
    }

    public int getNotationDeclIndex(String notationDeclName) {
        if (notationDeclName == null || this.fNotationIndexMap.get(notationDeclName) == null) {
            return -1;
        }
        return this.fNotationIndexMap.get(notationDeclName).intValue();
    }

    public boolean getNotationDecl(int notationDeclIndex, XMLNotationDecl notationDecl) {
        if (notationDeclIndex < 0 || notationDeclIndex >= this.fNotationCount) {
            return false;
        }
        int chunk = notationDeclIndex >> 8;
        int index = notationDeclIndex & 255;
        notationDecl.setValues(this.fNotationName[chunk][index], this.fNotationPublicId[chunk][index], this.fNotationSystemId[chunk][index], this.fNotationBaseSystemId[chunk][index]);
        return true;
    }

    public boolean getContentSpec(int contentSpecIndex, XMLContentSpec contentSpec) {
        if (contentSpecIndex < 0 || contentSpecIndex >= this.fContentSpecCount) {
            return false;
        }
        int chunk = contentSpecIndex >> 8;
        int index = contentSpecIndex & 255;
        contentSpec.type = this.fContentSpecType[chunk][index];
        contentSpec.value = this.fContentSpecValue[chunk][index];
        contentSpec.otherValue = this.fContentSpecOtherValue[chunk][index];
        return true;
    }

    public int getContentSpecIndex(int elementDeclIndex) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return -1;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        return this.fElementDeclContentSpecIndex[chunk][index];
    }

    public String getContentSpecAsString(int elementDeclIndex) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return null;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        int contentSpecIndex = this.fElementDeclContentSpecIndex[chunk][index];
        XMLContentSpec contentSpec = new XMLContentSpec();
        if (getContentSpec(contentSpecIndex, contentSpec)) {
            StringBuffer str = new StringBuffer();
            int parentContentSpecType = contentSpec.type & 15;
            switch (parentContentSpecType) {
                case 0:
                    str.append('(');
                    if (contentSpec.value == null && contentSpec.otherValue == null) {
                        str.append("#PCDATA");
                    } else {
                        str.append(contentSpec.value);
                    }
                    str.append(')');
                    break;
                case 1:
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    int nextContentSpec = contentSpec.type;
                    if (nextContentSpec == 0) {
                        str.append('(');
                        str.append(contentSpec.value);
                        str.append(')');
                    } else if (nextContentSpec == 3 || nextContentSpec == 2 || nextContentSpec == 1) {
                        str.append('(');
                        appendContentSpec(contentSpec, str, true, parentContentSpecType);
                        str.append(')');
                    } else {
                        appendContentSpec(contentSpec, str, true, parentContentSpecType);
                    }
                    str.append('?');
                    break;
                case 2:
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    int nextContentSpec2 = contentSpec.type;
                    if (nextContentSpec2 == 0) {
                        str.append('(');
                        if (contentSpec.value == null && contentSpec.otherValue == null) {
                            str.append("#PCDATA");
                        } else if (contentSpec.otherValue != null) {
                            str.append("##any:uri=").append(contentSpec.otherValue);
                        } else if (contentSpec.value == null) {
                            str.append(SchemaSymbols.ATTVAL_TWOPOUNDANY);
                        } else {
                            appendContentSpec(contentSpec, str, true, parentContentSpecType);
                        }
                        str.append(')');
                    } else if (nextContentSpec2 == 3 || nextContentSpec2 == 2 || nextContentSpec2 == 1) {
                        str.append('(');
                        appendContentSpec(contentSpec, str, true, parentContentSpecType);
                        str.append(')');
                    } else {
                        appendContentSpec(contentSpec, str, true, parentContentSpecType);
                    }
                    str.append('*');
                    break;
                case 3:
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    int nextContentSpec3 = contentSpec.type;
                    if (nextContentSpec3 == 0) {
                        str.append('(');
                        if (contentSpec.value == null && contentSpec.otherValue == null) {
                            str.append("#PCDATA");
                        } else if (contentSpec.otherValue != null) {
                            str.append("##any:uri=").append(contentSpec.otherValue);
                        } else if (contentSpec.value == null) {
                            str.append(SchemaSymbols.ATTVAL_TWOPOUNDANY);
                        } else {
                            str.append(contentSpec.value);
                        }
                        str.append(')');
                    } else if (nextContentSpec3 == 3 || nextContentSpec3 == 2 || nextContentSpec3 == 1) {
                        str.append('(');
                        appendContentSpec(contentSpec, str, true, parentContentSpecType);
                        str.append(')');
                    } else {
                        appendContentSpec(contentSpec, str, true, parentContentSpecType);
                    }
                    str.append('+');
                    break;
                case 4:
                case 5:
                    appendContentSpec(contentSpec, str, true, parentContentSpecType);
                    break;
                case 6:
                    str.append(SchemaSymbols.ATTVAL_TWOPOUNDANY);
                    if (contentSpec.otherValue != null) {
                        str.append(":uri=");
                        str.append(contentSpec.otherValue);
                        break;
                    }
                    break;
                case 7:
                    str.append("##other:uri=");
                    str.append(contentSpec.otherValue);
                    break;
                case 8:
                    str.append(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL);
                    break;
                default:
                    str.append("???");
                    break;
            }
            return str.toString();
        }
        return null;
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

    protected void addContentSpecToElement(XMLElementDecl elementDecl) {
        if ((this.fDepth == 0 || (this.fDepth == 1 && elementDecl.type == 2)) && this.fNodeIndexStack != null) {
            if (elementDecl.type == 2) {
                int pcdata = addUniqueLeafNode(null);
                if (this.fNodeIndexStack[0] == -1) {
                    this.fNodeIndexStack[0] = pcdata;
                } else {
                    this.fNodeIndexStack[0] = addContentSpecNode((short) 4, pcdata, this.fNodeIndexStack[0]);
                }
            }
            setContentSpecIndex(this.fCurrentElementIndex, this.fNodeIndexStack[this.fDepth]);
        }
    }

    protected ContentModelValidator getElementContentModelValidator(int elementDeclIndex) {
        ContentModelValidator contentModel;
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        ContentModelValidator contentModel2 = this.fElementDeclContentModelValidator[chunk][index];
        if (contentModel2 != null) {
            return contentModel2;
        }
        short s2 = this.fElementDeclType[chunk][index];
        if (s2 == 4) {
            return null;
        }
        int contentSpecIndex = this.fElementDeclContentSpecIndex[chunk][index];
        XMLContentSpec contentSpec = new XMLContentSpec();
        getContentSpec(contentSpecIndex, contentSpec);
        if (s2 == 2) {
            ChildrenList children = new ChildrenList();
            contentSpecTree(contentSpecIndex, contentSpec, children);
            contentModel = new MixedContentModel(children.qname, children.type, 0, children.length, false);
        } else if (s2 == 3) {
            contentModel = createChildModel(contentSpecIndex);
        } else {
            throw new RuntimeException("Unknown content type for a element decl in getElementContentModelValidator() in AbstractDTDGrammar class");
        }
        this.fElementDeclContentModelValidator[chunk][index] = contentModel;
        return contentModel;
    }

    protected int createElementDecl() {
        int chunk = this.fElementDeclCount >> 8;
        int index = this.fElementDeclCount & 255;
        ensureElementDeclCapacity(chunk);
        this.fElementDeclName[chunk][index] = new QName();
        this.fElementDeclType[chunk][index] = -1;
        this.fElementDeclContentModelValidator[chunk][index] = null;
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
        this.fElementDeclName[chunk][index].setValues(elementDecl.name);
        this.fElementDeclType[chunk][index] = elementDecl.type;
        this.fElementDeclContentModelValidator[chunk][index] = elementDecl.contentModelValidator;
        if (elementDecl.simpleType.list) {
            short[] sArr = this.fElementDeclType[chunk];
            sArr[index] = (short) (sArr[index] | 128);
        }
        this.fElementIndexMap.put(elementDecl.name.rawname, Integer.valueOf(elementDeclIndex));
    }

    protected void putElementNameMapping(QName name, int scope, int elementDeclIndex) {
    }

    protected void setFirstAttributeDeclIndex(int elementDeclIndex, int newFirstAttrIndex) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        this.fElementDeclFirstAttributeDeclIndex[chunk][index] = newFirstAttrIndex;
    }

    protected void setContentSpecIndex(int elementDeclIndex, int contentSpecIndex) {
        if (elementDeclIndex < 0 || elementDeclIndex >= this.fElementDeclCount) {
            return;
        }
        int chunk = elementDeclIndex >> 8;
        int index = elementDeclIndex & 255;
        this.fElementDeclContentSpecIndex[chunk][index] = contentSpecIndex;
    }

    protected int createAttributeDecl() {
        int chunk = this.fAttributeDeclCount >> 8;
        int index = this.fAttributeDeclCount & 255;
        ensureAttributeDeclCapacity(chunk);
        this.fAttributeDeclName[chunk][index] = new QName();
        this.fAttributeDeclType[chunk][index] = -1;
        this.fAttributeDeclDatatypeValidator[chunk][index] = null;
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
        this.fAttributeDeclDatatypeValidator[attrChunk][attrIndex] = attributeDecl.simpleType.datatypeValidator;
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

    protected int createContentSpec() {
        int chunk = this.fContentSpecCount >> 8;
        int index = this.fContentSpecCount & 255;
        ensureContentSpecCapacity(chunk);
        this.fContentSpecType[chunk][index] = -1;
        this.fContentSpecValue[chunk][index] = null;
        this.fContentSpecOtherValue[chunk][index] = null;
        int i2 = this.fContentSpecCount;
        this.fContentSpecCount = i2 + 1;
        return i2;
    }

    protected void setContentSpec(int contentSpecIndex, XMLContentSpec contentSpec) {
        int chunk = contentSpecIndex >> 8;
        int index = contentSpecIndex & 255;
        this.fContentSpecType[chunk][index] = contentSpec.type;
        this.fContentSpecValue[chunk][index] = contentSpec.value;
        this.fContentSpecOtherValue[chunk][index] = contentSpec.otherValue;
    }

    protected int createEntityDecl() {
        int chunk = this.fEntityCount >> 8;
        int index = this.fEntityCount & 255;
        ensureEntityDeclCapacity(chunk);
        this.fEntityIsPE[chunk][index] = 0;
        this.fEntityInExternal[chunk][index] = 0;
        int i2 = this.fEntityCount;
        this.fEntityCount = i2 + 1;
        return i2;
    }

    protected void setEntityDecl(int entityDeclIndex, XMLEntityDecl entityDecl) {
        int chunk = entityDeclIndex >> 8;
        int index = entityDeclIndex & 255;
        this.fEntityName[chunk][index] = entityDecl.name;
        this.fEntityValue[chunk][index] = entityDecl.value;
        this.fEntityPublicId[chunk][index] = entityDecl.publicId;
        this.fEntitySystemId[chunk][index] = entityDecl.systemId;
        this.fEntityBaseSystemId[chunk][index] = entityDecl.baseSystemId;
        this.fEntityNotation[chunk][index] = entityDecl.notation;
        this.fEntityIsPE[chunk][index] = entityDecl.isPE ? (byte) 1 : (byte) 0;
        this.fEntityInExternal[chunk][index] = entityDecl.inExternal ? (byte) 1 : (byte) 0;
        this.fEntityIndexMap.put(entityDecl.name, Integer.valueOf(entityDeclIndex));
    }

    protected int createNotationDecl() {
        int chunk = this.fNotationCount >> 8;
        ensureNotationDeclCapacity(chunk);
        int i2 = this.fNotationCount;
        this.fNotationCount = i2 + 1;
        return i2;
    }

    protected void setNotationDecl(int notationDeclIndex, XMLNotationDecl notationDecl) {
        int chunk = notationDeclIndex >> 8;
        int index = notationDeclIndex & 255;
        this.fNotationName[chunk][index] = notationDecl.name;
        this.fNotationPublicId[chunk][index] = notationDecl.publicId;
        this.fNotationSystemId[chunk][index] = notationDecl.systemId;
        this.fNotationBaseSystemId[chunk][index] = notationDecl.baseSystemId;
        this.fNotationIndexMap.put(notationDecl.name, Integer.valueOf(notationDeclIndex));
    }

    protected int addContentSpecNode(short nodeType, String nodeValue) {
        int contentSpecIndex = createContentSpec();
        this.fContentSpec.setValues(nodeType, nodeValue, null);
        setContentSpec(contentSpecIndex, this.fContentSpec);
        return contentSpecIndex;
    }

    protected int addUniqueLeafNode(String elementName) {
        int contentSpecIndex = createContentSpec();
        this.fContentSpec.setValues((short) 0, elementName, null);
        setContentSpec(contentSpecIndex, this.fContentSpec);
        return contentSpecIndex;
    }

    protected int addContentSpecNode(short nodeType, int leftNodeIndex, int rightNodeIndex) {
        int contentSpecIndex = createContentSpec();
        int[] leftIntArray = {leftNodeIndex};
        int[] rightIntArray = {rightNodeIndex};
        this.fContentSpec.setValues(nodeType, leftIntArray, rightIntArray);
        setContentSpec(contentSpecIndex, this.fContentSpec);
        return contentSpecIndex;
    }

    protected void initializeContentModelStack() {
        if (this.fOpStack == null) {
            this.fOpStack = new short[8];
            this.fNodeIndexStack = new int[8];
            this.fPrevNodeIndexStack = new int[8];
        } else if (this.fDepth == this.fOpStack.length) {
            short[] newStack = new short[this.fDepth * 2];
            System.arraycopy(this.fOpStack, 0, newStack, 0, this.fDepth);
            this.fOpStack = newStack;
            int[] newIntStack = new int[this.fDepth * 2];
            System.arraycopy(this.fNodeIndexStack, 0, newIntStack, 0, this.fDepth);
            this.fNodeIndexStack = newIntStack;
            int[] newIntStack2 = new int[this.fDepth * 2];
            System.arraycopy(this.fPrevNodeIndexStack, 0, newIntStack2, 0, this.fDepth);
            this.fPrevNodeIndexStack = newIntStack2;
        }
        this.fOpStack[this.fDepth] = -1;
        this.fNodeIndexStack[this.fDepth] = -1;
        this.fPrevNodeIndexStack[this.fDepth] = -1;
    }

    boolean isImmutable() {
        return this.fIsImmutable;
    }

    private void appendContentSpec(XMLContentSpec contentSpec, StringBuffer str, boolean parens, int parentContentSpecType) {
        int thisContentSpec = contentSpec.type & 15;
        switch (thisContentSpec) {
            case 0:
                if (contentSpec.value == null && contentSpec.otherValue == null) {
                    str.append("#PCDATA");
                    break;
                } else if (contentSpec.value == null && contentSpec.otherValue != null) {
                    str.append("##any:uri=").append(contentSpec.otherValue);
                    break;
                } else if (contentSpec.value == null) {
                    str.append(SchemaSymbols.ATTVAL_TWOPOUNDANY);
                    break;
                } else {
                    str.append(contentSpec.value);
                    break;
                }
                break;
            case 1:
                if (parentContentSpecType == 3 || parentContentSpecType == 2 || parentContentSpecType == 1) {
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    str.append('(');
                    appendContentSpec(contentSpec, str, true, thisContentSpec);
                    str.append(')');
                } else {
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    appendContentSpec(contentSpec, str, true, thisContentSpec);
                }
                str.append('?');
                break;
            case 2:
                if (parentContentSpecType == 3 || parentContentSpecType == 2 || parentContentSpecType == 1) {
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    str.append('(');
                    appendContentSpec(contentSpec, str, true, thisContentSpec);
                    str.append(')');
                } else {
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    appendContentSpec(contentSpec, str, true, thisContentSpec);
                }
                str.append('*');
                break;
            case 3:
                if (parentContentSpecType == 3 || parentContentSpecType == 2 || parentContentSpecType == 1) {
                    str.append('(');
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    appendContentSpec(contentSpec, str, true, thisContentSpec);
                    str.append(')');
                } else {
                    getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                    appendContentSpec(contentSpec, str, true, thisContentSpec);
                }
                str.append('+');
                break;
            case 4:
            case 5:
                if (parens) {
                    str.append('(');
                }
                int type = contentSpec.type;
                int otherValue = ((int[]) contentSpec.otherValue)[0];
                getContentSpec(((int[]) contentSpec.value)[0], contentSpec);
                appendContentSpec(contentSpec, str, contentSpec.type != type, thisContentSpec);
                if (type == 4) {
                    str.append('|');
                } else {
                    str.append(',');
                }
                getContentSpec(otherValue, contentSpec);
                appendContentSpec(contentSpec, str, true, thisContentSpec);
                if (parens) {
                    str.append(')');
                    break;
                }
                break;
            case 6:
                str.append(SchemaSymbols.ATTVAL_TWOPOUNDANY);
                if (contentSpec.otherValue != null) {
                    str.append(":uri=");
                    str.append(contentSpec.otherValue);
                    break;
                }
                break;
            case 7:
                str.append("##other:uri=");
                str.append(contentSpec.otherValue);
                break;
            case 8:
                str.append(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL);
                break;
            default:
                str.append("???");
                break;
        }
    }

    private void printAttribute(int attributeDeclIndex) {
        XMLAttributeDecl attributeDecl = new XMLAttributeDecl();
        if (getAttributeDecl(attributeDeclIndex, attributeDecl)) {
            System.out.print(" { ");
            System.out.print(attributeDecl.name.localpart);
            System.out.print(" }");
        }
    }

    private synchronized ContentModelValidator createChildModel(int contentSpecIndex) {
        XMLContentSpec contentSpec = new XMLContentSpec();
        getContentSpec(contentSpecIndex, contentSpec);
        if ((contentSpec.type & 15) != 6 && (contentSpec.type & 15) != 7 && (contentSpec.type & 15) != 8) {
            if (contentSpec.type == 0) {
                if (contentSpec.value == null && contentSpec.otherValue == null) {
                    throw new RuntimeException("ImplementationMessages.VAL_NPCD");
                }
                this.fQName.setValues(null, (String) contentSpec.value, (String) contentSpec.value, (String) contentSpec.otherValue);
                return new SimpleContentModel(contentSpec.type, this.fQName, null);
            }
            if (contentSpec.type == 4 || contentSpec.type == 5) {
                XMLContentSpec contentSpecLeft = new XMLContentSpec();
                XMLContentSpec contentSpecRight = new XMLContentSpec();
                getContentSpec(((int[]) contentSpec.value)[0], contentSpecLeft);
                getContentSpec(((int[]) contentSpec.otherValue)[0], contentSpecRight);
                if (contentSpecLeft.type == 0 && contentSpecRight.type == 0) {
                    this.fQName.setValues(null, (String) contentSpecLeft.value, (String) contentSpecLeft.value, (String) contentSpecLeft.otherValue);
                    this.fQName2.setValues(null, (String) contentSpecRight.value, (String) contentSpecRight.value, (String) contentSpecRight.otherValue);
                    return new SimpleContentModel(contentSpec.type, this.fQName, this.fQName2);
                }
            } else if (contentSpec.type == 1 || contentSpec.type == 2 || contentSpec.type == 3) {
                XMLContentSpec contentSpecLeft2 = new XMLContentSpec();
                getContentSpec(((int[]) contentSpec.value)[0], contentSpecLeft2);
                if (contentSpecLeft2.type == 0) {
                    this.fQName.setValues(null, (String) contentSpecLeft2.value, (String) contentSpecLeft2.value, (String) contentSpecLeft2.otherValue);
                    return new SimpleContentModel(contentSpec.type, this.fQName, null);
                }
            } else {
                throw new RuntimeException("ImplementationMessages.VAL_CST");
            }
        }
        this.fLeafCount = 0;
        this.fLeafCount = 0;
        CMNode cmn = buildSyntaxTree(contentSpecIndex, contentSpec);
        return new DFAContentModel(cmn, this.fLeafCount, false);
    }

    private final CMNode buildSyntaxTree(int startNode, XMLContentSpec contentSpec) {
        CMNode nodeRet;
        getContentSpec(startNode, contentSpec);
        if ((contentSpec.type & 15) == 6) {
            short s2 = contentSpec.type;
            String str = (String) contentSpec.otherValue;
            int i2 = this.fLeafCount;
            this.fLeafCount = i2 + 1;
            nodeRet = new CMAny(s2, str, i2);
        } else if ((contentSpec.type & 15) == 7) {
            short s3 = contentSpec.type;
            String str2 = (String) contentSpec.otherValue;
            int i3 = this.fLeafCount;
            this.fLeafCount = i3 + 1;
            nodeRet = new CMAny(s3, str2, i3);
        } else if ((contentSpec.type & 15) == 8) {
            short s4 = contentSpec.type;
            int i4 = this.fLeafCount;
            this.fLeafCount = i4 + 1;
            nodeRet = new CMAny(s4, null, i4);
        } else if (contentSpec.type == 0) {
            this.fQName.setValues(null, (String) contentSpec.value, (String) contentSpec.value, (String) contentSpec.otherValue);
            QName qName = this.fQName;
            int i5 = this.fLeafCount;
            this.fLeafCount = i5 + 1;
            nodeRet = new CMLeaf(qName, i5);
        } else {
            int leftNode = ((int[]) contentSpec.value)[0];
            int rightNode = ((int[]) contentSpec.otherValue)[0];
            if (contentSpec.type == 4 || contentSpec.type == 5) {
                nodeRet = new CMBinOp(contentSpec.type, buildSyntaxTree(leftNode, contentSpec), buildSyntaxTree(rightNode, contentSpec));
            } else if (contentSpec.type == 2 || contentSpec.type == 2 || contentSpec.type == 1 || contentSpec.type == 3) {
                nodeRet = new CMUniOp(contentSpec.type, buildSyntaxTree(leftNode, contentSpec));
            } else {
                throw new RuntimeException("ImplementationMessages.VAL_CST");
            }
        }
        return nodeRet;
    }

    private void contentSpecTree(int contentSpecIndex, XMLContentSpec contentSpec, ChildrenList children) {
        getContentSpec(contentSpecIndex, contentSpec);
        if (contentSpec.type == 0 || (contentSpec.type & 15) == 6 || (contentSpec.type & 15) == 8 || (contentSpec.type & 15) == 7) {
            if (children.length == children.qname.length) {
                QName[] newQName = new QName[children.length * 2];
                System.arraycopy(children.qname, 0, newQName, 0, children.length);
                children.qname = newQName;
                int[] newType = new int[children.length * 2];
                System.arraycopy(children.type, 0, newType, 0, children.length);
                children.type = newType;
            }
            children.qname[children.length] = new QName(null, (String) contentSpec.value, (String) contentSpec.value, (String) contentSpec.otherValue);
            children.type[children.length] = contentSpec.type;
            children.length++;
            return;
        }
        int leftNode = contentSpec.value != null ? ((int[]) contentSpec.value)[0] : -1;
        if (contentSpec.otherValue != null) {
            int rightNode = ((int[]) contentSpec.otherValue)[0];
            if (contentSpec.type == 4 || contentSpec.type == 5) {
                contentSpecTree(leftNode, contentSpec, children);
                contentSpecTree(rightNode, contentSpec, children);
            } else {
                if (contentSpec.type == 1 || contentSpec.type == 2 || contentSpec.type == 3) {
                    contentSpecTree(leftNode, contentSpec, children);
                    return;
                }
                throw new RuntimeException("Invalid content spec type seen in contentSpecTree() method of AbstractDTDGrammar class : " + ((int) contentSpec.type));
            }
        }
    }

    private void ensureElementDeclCapacity(int chunk) {
        if (chunk >= this.fElementDeclName.length) {
            this.fElementDeclIsExternal = resize(this.fElementDeclIsExternal, this.fElementDeclIsExternal.length * 2);
            this.fElementDeclName = resize(this.fElementDeclName, this.fElementDeclName.length * 2);
            this.fElementDeclType = resize(this.fElementDeclType, this.fElementDeclType.length * 2);
            this.fElementDeclContentModelValidator = resize(this.fElementDeclContentModelValidator, this.fElementDeclContentModelValidator.length * 2);
            this.fElementDeclContentSpecIndex = resize(this.fElementDeclContentSpecIndex, this.fElementDeclContentSpecIndex.length * 2);
            this.fElementDeclFirstAttributeDeclIndex = resize(this.fElementDeclFirstAttributeDeclIndex, this.fElementDeclFirstAttributeDeclIndex.length * 2);
            this.fElementDeclLastAttributeDeclIndex = resize(this.fElementDeclLastAttributeDeclIndex, this.fElementDeclLastAttributeDeclIndex.length * 2);
        } else if (this.fElementDeclName[chunk] != null) {
            return;
        }
        this.fElementDeclIsExternal[chunk] = new int[256];
        this.fElementDeclName[chunk] = new QName[256];
        this.fElementDeclType[chunk] = new short[256];
        this.fElementDeclContentModelValidator[chunk] = new ContentModelValidator[256];
        this.fElementDeclContentSpecIndex[chunk] = new int[256];
        this.fElementDeclFirstAttributeDeclIndex[chunk] = new int[256];
        this.fElementDeclLastAttributeDeclIndex[chunk] = new int[256];
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void ensureAttributeDeclCapacity(int chunk) {
        if (chunk >= this.fAttributeDeclName.length) {
            this.fAttributeDeclIsExternal = resize(this.fAttributeDeclIsExternal, this.fAttributeDeclIsExternal.length * 2);
            this.fAttributeDeclName = resize(this.fAttributeDeclName, this.fAttributeDeclName.length * 2);
            this.fAttributeDeclType = resize(this.fAttributeDeclType, this.fAttributeDeclType.length * 2);
            this.fAttributeDeclEnumeration = resize(this.fAttributeDeclEnumeration, this.fAttributeDeclEnumeration.length * 2);
            this.fAttributeDeclDefaultType = resize(this.fAttributeDeclDefaultType, this.fAttributeDeclDefaultType.length * 2);
            this.fAttributeDeclDatatypeValidator = resize(this.fAttributeDeclDatatypeValidator, this.fAttributeDeclDatatypeValidator.length * 2);
            this.fAttributeDeclDefaultValue = resize(this.fAttributeDeclDefaultValue, this.fAttributeDeclDefaultValue.length * 2);
            this.fAttributeDeclNonNormalizedDefaultValue = resize(this.fAttributeDeclNonNormalizedDefaultValue, this.fAttributeDeclNonNormalizedDefaultValue.length * 2);
            this.fAttributeDeclNextAttributeDeclIndex = resize(this.fAttributeDeclNextAttributeDeclIndex, this.fAttributeDeclNextAttributeDeclIndex.length * 2);
        } else if (this.fAttributeDeclName[chunk] != null) {
            return;
        }
        this.fAttributeDeclIsExternal[chunk] = new int[256];
        this.fAttributeDeclName[chunk] = new QName[256];
        this.fAttributeDeclType[chunk] = new short[256];
        this.fAttributeDeclEnumeration[chunk] = new String[256];
        this.fAttributeDeclDefaultType[chunk] = new short[256];
        this.fAttributeDeclDatatypeValidator[chunk] = new DatatypeValidator[256];
        this.fAttributeDeclDefaultValue[chunk] = new String[256];
        this.fAttributeDeclNonNormalizedDefaultValue[chunk] = new String[256];
        this.fAttributeDeclNextAttributeDeclIndex[chunk] = new int[256];
    }

    private void ensureEntityDeclCapacity(int chunk) {
        if (chunk >= this.fEntityName.length) {
            this.fEntityName = resize(this.fEntityName, this.fEntityName.length * 2);
            this.fEntityValue = resize(this.fEntityValue, this.fEntityValue.length * 2);
            this.fEntityPublicId = resize(this.fEntityPublicId, this.fEntityPublicId.length * 2);
            this.fEntitySystemId = resize(this.fEntitySystemId, this.fEntitySystemId.length * 2);
            this.fEntityBaseSystemId = resize(this.fEntityBaseSystemId, this.fEntityBaseSystemId.length * 2);
            this.fEntityNotation = resize(this.fEntityNotation, this.fEntityNotation.length * 2);
            this.fEntityIsPE = resize(this.fEntityIsPE, this.fEntityIsPE.length * 2);
            this.fEntityInExternal = resize(this.fEntityInExternal, this.fEntityInExternal.length * 2);
        } else if (this.fEntityName[chunk] != null) {
            return;
        }
        this.fEntityName[chunk] = new String[256];
        this.fEntityValue[chunk] = new String[256];
        this.fEntityPublicId[chunk] = new String[256];
        this.fEntitySystemId[chunk] = new String[256];
        this.fEntityBaseSystemId[chunk] = new String[256];
        this.fEntityNotation[chunk] = new String[256];
        this.fEntityIsPE[chunk] = new byte[256];
        this.fEntityInExternal[chunk] = new byte[256];
    }

    private void ensureNotationDeclCapacity(int chunk) {
        if (chunk >= this.fNotationName.length) {
            this.fNotationName = resize(this.fNotationName, this.fNotationName.length * 2);
            this.fNotationPublicId = resize(this.fNotationPublicId, this.fNotationPublicId.length * 2);
            this.fNotationSystemId = resize(this.fNotationSystemId, this.fNotationSystemId.length * 2);
            this.fNotationBaseSystemId = resize(this.fNotationBaseSystemId, this.fNotationBaseSystemId.length * 2);
        } else if (this.fNotationName[chunk] != null) {
            return;
        }
        this.fNotationName[chunk] = new String[256];
        this.fNotationPublicId[chunk] = new String[256];
        this.fNotationSystemId[chunk] = new String[256];
        this.fNotationBaseSystemId[chunk] = new String[256];
    }

    private void ensureContentSpecCapacity(int chunk) {
        if (chunk >= this.fContentSpecType.length) {
            this.fContentSpecType = resize(this.fContentSpecType, this.fContentSpecType.length * 2);
            this.fContentSpecValue = resize(this.fContentSpecValue, this.fContentSpecValue.length * 2);
            this.fContentSpecOtherValue = resize(this.fContentSpecOtherValue, this.fContentSpecOtherValue.length * 2);
        } else if (this.fContentSpecType[chunk] != null) {
            return;
        }
        this.fContentSpecType[chunk] = new short[256];
        this.fContentSpecValue[chunk] = new Object[256];
        this.fContentSpecOtherValue[chunk] = new Object[256];
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [byte[], byte[][], java.lang.Object] */
    private static byte[][] resize(byte[][] array, int newsize) {
        ?? r0 = new byte[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
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

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator[], com.sun.org.apache.xerces.internal.impl.dv.DatatypeValidator[][], java.lang.Object] */
    private static DatatypeValidator[][] resize(DatatypeValidator[][] array, int newsize) {
        ?? r0 = new DatatypeValidator[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator[], com.sun.org.apache.xerces.internal.impl.dtd.models.ContentModelValidator[][], java.lang.Object] */
    private static ContentModelValidator[][] resize(ContentModelValidator[][] array, int newsize) {
        ?? r0 = new ContentModelValidator[newsize];
        System.arraycopy(array, 0, r0, 0, array.length);
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Object, java.lang.Object[], java.lang.Object[][]] */
    private static Object[][] resize(Object[][] array, int newsize) {
        ?? r0 = new Object[newsize];
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

    @Override // com.sun.org.apache.xerces.internal.impl.validation.EntityState
    public boolean isEntityDeclared(String name) {
        return getEntityDeclIndex(name) != -1;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.validation.EntityState
    public boolean isEntityUnparsed(String name) {
        int entityIndex = getEntityDeclIndex(name);
        if (entityIndex > -1) {
            int chunk = entityIndex >> 8;
            int index = entityIndex & 255;
            return this.fEntityNotation[chunk][index] != null;
        }
        return false;
    }
}
