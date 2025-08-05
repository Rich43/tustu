package com.sun.org.apache.xerces.internal.impl.xs;

import com.sun.org.apache.xerces.internal.impl.dv.DVFactoryException;
import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.identity.IdentityConstraint;
import com.sun.org.apache.xerces.internal.impl.xs.util.ObjectListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.SimpleLocator;
import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMap4Types;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import com.sun.org.apache.xerces.internal.parsers.XML11Configuration;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
import com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSAttributeGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSElementDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSModel;
import com.sun.org.apache.xerces.internal.xs.XSModelGroupDefinition;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSNotationDeclaration;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSParticle;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSWildcard;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import java.lang.ref.SoftReference;
import java.util.Vector;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SchemaGrammar.class */
public class SchemaGrammar implements XSGrammar, XSNamespaceItem {
    String fTargetNamespace;
    SymbolHash fGlobalAttrDecls;
    SymbolHash fGlobalAttrGrpDecls;
    SymbolHash fGlobalElemDecls;
    SymbolHash fGlobalGroupDecls;
    SymbolHash fGlobalNotationDecls;
    SymbolHash fGlobalIDConstraintDecls;
    SymbolHash fGlobalTypeDecls;
    SymbolHash fGlobalAttrDeclsExt;
    SymbolHash fGlobalAttrGrpDeclsExt;
    SymbolHash fGlobalElemDeclsExt;
    SymbolHash fGlobalGroupDeclsExt;
    SymbolHash fGlobalNotationDeclsExt;
    SymbolHash fGlobalIDConstraintDeclsExt;
    SymbolHash fGlobalTypeDeclsExt;
    SymbolHash fAllGlobalElemDecls;
    XSDDescription fGrammarDescription;
    XSAnnotationImpl[] fAnnotations;
    int fNumAnnotations;
    private SymbolTable fSymbolTable;
    private SoftReference fSAXParser;
    private SoftReference fDOMParser;
    private boolean fIsImmutable;
    private static final int BASICSET_COUNT = 29;
    private static final int FULLSET_COUNT = 46;
    private static final int GRAMMAR_XS = 1;
    private static final int GRAMMAR_XSI = 2;
    Vector fImported;
    private static final int INITIAL_SIZE = 16;
    private static final int INC_SIZE = 16;
    private int fCTCount;
    private XSComplexTypeDecl[] fComplexTypeDecls;
    private SimpleLocator[] fCTLocators;
    private static final int REDEFINED_GROUP_INIT_SIZE = 2;
    private int fRGCount;
    private XSGroupDecl[] fRedefinedGroupDecls;
    private SimpleLocator[] fRGLocators;
    boolean fFullChecked;
    private int fSubGroupCount;
    private XSElementDecl[] fSubGroups;
    private static final short MAX_COMP_IDX = 16;
    private XSNamedMap[] fComponents;
    private ObjectList[] fComponentsExt;
    private Vector fDocuments;
    private Vector fLocations;
    public static final XSComplexTypeDecl fAnyType = new XSAnyType();
    public static final BuiltinSchemaGrammar SG_SchemaNS = new BuiltinSchemaGrammar(1, 1);
    private static final BuiltinSchemaGrammar SG_SchemaNSExtended = new BuiltinSchemaGrammar(1, 2);
    public static final XSSimpleType fAnySimpleType = (XSSimpleType) SG_SchemaNS.getGlobalTypeDecl(SchemaSymbols.ATTVAL_ANYSIMPLETYPE);
    public static final BuiltinSchemaGrammar SG_XSI = new BuiltinSchemaGrammar(2, 1);
    private static final boolean[] GLOBAL_COMP = {false, true, true, true, false, true, true, false, false, false, false, true, false, false, false, true, true};

    protected SchemaGrammar() {
        this.fGrammarDescription = null;
        this.fAnnotations = null;
        this.fSymbolTable = null;
        this.fSAXParser = null;
        this.fDOMParser = null;
        this.fIsImmutable = false;
        this.fImported = null;
        this.fCTCount = 0;
        this.fComplexTypeDecls = new XSComplexTypeDecl[16];
        this.fCTLocators = new SimpleLocator[16];
        this.fRGCount = 0;
        this.fRedefinedGroupDecls = new XSGroupDecl[2];
        this.fRGLocators = new SimpleLocator[1];
        this.fFullChecked = false;
        this.fSubGroupCount = 0;
        this.fSubGroups = new XSElementDecl[16];
        this.fComponents = null;
        this.fComponentsExt = null;
        this.fDocuments = null;
        this.fLocations = null;
    }

    public SchemaGrammar(String targetNamespace, XSDDescription grammarDesc, SymbolTable symbolTable) {
        this.fGrammarDescription = null;
        this.fAnnotations = null;
        this.fSymbolTable = null;
        this.fSAXParser = null;
        this.fDOMParser = null;
        this.fIsImmutable = false;
        this.fImported = null;
        this.fCTCount = 0;
        this.fComplexTypeDecls = new XSComplexTypeDecl[16];
        this.fCTLocators = new SimpleLocator[16];
        this.fRGCount = 0;
        this.fRedefinedGroupDecls = new XSGroupDecl[2];
        this.fRGLocators = new SimpleLocator[1];
        this.fFullChecked = false;
        this.fSubGroupCount = 0;
        this.fSubGroups = new XSElementDecl[16];
        this.fComponents = null;
        this.fComponentsExt = null;
        this.fDocuments = null;
        this.fLocations = null;
        this.fTargetNamespace = targetNamespace;
        this.fGrammarDescription = grammarDesc;
        this.fSymbolTable = symbolTable;
        this.fGlobalAttrDecls = new SymbolHash();
        this.fGlobalAttrGrpDecls = new SymbolHash();
        this.fGlobalElemDecls = new SymbolHash();
        this.fGlobalGroupDecls = new SymbolHash();
        this.fGlobalNotationDecls = new SymbolHash();
        this.fGlobalIDConstraintDecls = new SymbolHash();
        this.fGlobalAttrDeclsExt = new SymbolHash();
        this.fGlobalAttrGrpDeclsExt = new SymbolHash();
        this.fGlobalElemDeclsExt = new SymbolHash();
        this.fGlobalGroupDeclsExt = new SymbolHash();
        this.fGlobalNotationDeclsExt = new SymbolHash();
        this.fGlobalIDConstraintDeclsExt = new SymbolHash();
        this.fGlobalTypeDeclsExt = new SymbolHash();
        this.fAllGlobalElemDecls = new SymbolHash();
        if (this.fTargetNamespace == SchemaSymbols.URI_SCHEMAFORSCHEMA) {
            this.fGlobalTypeDecls = SG_SchemaNS.fGlobalTypeDecls.makeClone();
        } else {
            this.fGlobalTypeDecls = new SymbolHash();
        }
    }

    public SchemaGrammar(SchemaGrammar grammar) {
        this.fGrammarDescription = null;
        this.fAnnotations = null;
        this.fSymbolTable = null;
        this.fSAXParser = null;
        this.fDOMParser = null;
        this.fIsImmutable = false;
        this.fImported = null;
        this.fCTCount = 0;
        this.fComplexTypeDecls = new XSComplexTypeDecl[16];
        this.fCTLocators = new SimpleLocator[16];
        this.fRGCount = 0;
        this.fRedefinedGroupDecls = new XSGroupDecl[2];
        this.fRGLocators = new SimpleLocator[1];
        this.fFullChecked = false;
        this.fSubGroupCount = 0;
        this.fSubGroups = new XSElementDecl[16];
        this.fComponents = null;
        this.fComponentsExt = null;
        this.fDocuments = null;
        this.fLocations = null;
        this.fTargetNamespace = grammar.fTargetNamespace;
        this.fGrammarDescription = grammar.fGrammarDescription.makeClone();
        this.fSymbolTable = grammar.fSymbolTable;
        this.fGlobalAttrDecls = grammar.fGlobalAttrDecls.makeClone();
        this.fGlobalAttrGrpDecls = grammar.fGlobalAttrGrpDecls.makeClone();
        this.fGlobalElemDecls = grammar.fGlobalElemDecls.makeClone();
        this.fGlobalGroupDecls = grammar.fGlobalGroupDecls.makeClone();
        this.fGlobalNotationDecls = grammar.fGlobalNotationDecls.makeClone();
        this.fGlobalIDConstraintDecls = grammar.fGlobalIDConstraintDecls.makeClone();
        this.fGlobalTypeDecls = grammar.fGlobalTypeDecls.makeClone();
        this.fGlobalAttrDeclsExt = grammar.fGlobalAttrDeclsExt.makeClone();
        this.fGlobalAttrGrpDeclsExt = grammar.fGlobalAttrGrpDeclsExt.makeClone();
        this.fGlobalElemDeclsExt = grammar.fGlobalElemDeclsExt.makeClone();
        this.fGlobalGroupDeclsExt = grammar.fGlobalGroupDeclsExt.makeClone();
        this.fGlobalNotationDeclsExt = grammar.fGlobalNotationDeclsExt.makeClone();
        this.fGlobalIDConstraintDeclsExt = grammar.fGlobalIDConstraintDeclsExt.makeClone();
        this.fGlobalTypeDeclsExt = grammar.fGlobalTypeDeclsExt.makeClone();
        this.fAllGlobalElemDecls = grammar.fAllGlobalElemDecls.makeClone();
        this.fNumAnnotations = grammar.fNumAnnotations;
        if (this.fNumAnnotations > 0) {
            this.fAnnotations = new XSAnnotationImpl[grammar.fAnnotations.length];
            System.arraycopy(grammar.fAnnotations, 0, this.fAnnotations, 0, this.fNumAnnotations);
        }
        this.fSubGroupCount = grammar.fSubGroupCount;
        if (this.fSubGroupCount > 0) {
            this.fSubGroups = new XSElementDecl[grammar.fSubGroups.length];
            System.arraycopy(grammar.fSubGroups, 0, this.fSubGroups, 0, this.fSubGroupCount);
        }
        this.fCTCount = grammar.fCTCount;
        if (this.fCTCount > 0) {
            this.fComplexTypeDecls = new XSComplexTypeDecl[grammar.fComplexTypeDecls.length];
            this.fCTLocators = new SimpleLocator[grammar.fCTLocators.length];
            System.arraycopy(grammar.fComplexTypeDecls, 0, this.fComplexTypeDecls, 0, this.fCTCount);
            System.arraycopy(grammar.fCTLocators, 0, this.fCTLocators, 0, this.fCTCount);
        }
        this.fRGCount = grammar.fRGCount;
        if (this.fRGCount > 0) {
            this.fRedefinedGroupDecls = new XSGroupDecl[grammar.fRedefinedGroupDecls.length];
            this.fRGLocators = new SimpleLocator[grammar.fRGLocators.length];
            System.arraycopy(grammar.fRedefinedGroupDecls, 0, this.fRedefinedGroupDecls, 0, this.fRGCount);
            System.arraycopy(grammar.fRGLocators, 0, this.fRGLocators, 0, this.fRGCount);
        }
        if (grammar.fImported != null) {
            this.fImported = new Vector();
            for (int i2 = 0; i2 < grammar.fImported.size(); i2++) {
                this.fImported.add(grammar.fImported.elementAt(i2));
            }
        }
        if (grammar.fLocations != null) {
            for (int k2 = 0; k2 < grammar.fLocations.size(); k2++) {
                addDocument(null, (String) grammar.fLocations.elementAt(k2));
            }
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SchemaGrammar$BuiltinSchemaGrammar.class */
    public static class BuiltinSchemaGrammar extends SchemaGrammar {
        private static final String EXTENDED_SCHEMA_FACTORY_CLASS = "com.sun.org.apache.xerces.internal.impl.dv.xs.ExtendedSchemaDVFactoryImpl";

        public BuiltinSchemaGrammar(int grammar, short schemaVersion) throws DVFactoryException {
            SchemaDVFactory schemaFactory;
            if (schemaVersion == 1) {
                schemaFactory = SchemaDVFactory.getInstance();
            } else {
                schemaFactory = SchemaDVFactory.getInstance(EXTENDED_SCHEMA_FACTORY_CLASS);
            }
            if (grammar == 1) {
                this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
                this.fGrammarDescription = new XSDDescription();
                this.fGrammarDescription.fContextType = (short) 3;
                this.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
                this.fGlobalAttrDecls = new SymbolHash(1);
                this.fGlobalAttrGrpDecls = new SymbolHash(1);
                this.fGlobalElemDecls = new SymbolHash(1);
                this.fGlobalGroupDecls = new SymbolHash(1);
                this.fGlobalNotationDecls = new SymbolHash(1);
                this.fGlobalIDConstraintDecls = new SymbolHash(1);
                this.fGlobalAttrDeclsExt = new SymbolHash(1);
                this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
                this.fGlobalElemDeclsExt = new SymbolHash(1);
                this.fGlobalGroupDeclsExt = new SymbolHash(1);
                this.fGlobalNotationDeclsExt = new SymbolHash(1);
                this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
                this.fGlobalTypeDeclsExt = new SymbolHash(1);
                this.fAllGlobalElemDecls = new SymbolHash(1);
                this.fGlobalTypeDecls = schemaFactory.getBuiltInTypes();
                int length = this.fGlobalTypeDecls.getLength();
                XSTypeDefinition[] typeDefinitions = new XSTypeDefinition[length];
                this.fGlobalTypeDecls.getValues(typeDefinitions, 0);
                for (int i2 = 0; i2 < length; i2++) {
                    XSTypeDefinition xtd = typeDefinitions[i2];
                    if (xtd instanceof XSSimpleTypeDecl) {
                        ((XSSimpleTypeDecl) xtd).setNamespaceItem(this);
                    }
                }
                this.fGlobalTypeDecls.put(fAnyType.getName(), fAnyType);
                return;
            }
            if (grammar == 2) {
                this.fTargetNamespace = SchemaSymbols.URI_XSI;
                this.fGrammarDescription = new XSDDescription();
                this.fGrammarDescription.fContextType = (short) 3;
                this.fGrammarDescription.setNamespace(SchemaSymbols.URI_XSI);
                this.fGlobalAttrGrpDecls = new SymbolHash(1);
                this.fGlobalElemDecls = new SymbolHash(1);
                this.fGlobalGroupDecls = new SymbolHash(1);
                this.fGlobalNotationDecls = new SymbolHash(1);
                this.fGlobalIDConstraintDecls = new SymbolHash(1);
                this.fGlobalTypeDecls = new SymbolHash(1);
                this.fGlobalAttrDeclsExt = new SymbolHash(1);
                this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
                this.fGlobalElemDeclsExt = new SymbolHash(1);
                this.fGlobalGroupDeclsExt = new SymbolHash(1);
                this.fGlobalNotationDeclsExt = new SymbolHash(1);
                this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
                this.fGlobalTypeDeclsExt = new SymbolHash(1);
                this.fAllGlobalElemDecls = new SymbolHash(1);
                this.fGlobalAttrDecls = new SymbolHash(8);
                String name = SchemaSymbols.XSI_TYPE;
                String tns = SchemaSymbols.URI_XSI;
                XSSimpleType type = schemaFactory.getBuiltInType(SchemaSymbols.ATTVAL_QNAME);
                this.fGlobalAttrDecls.put(name, new BuiltinAttrDecl(name, tns, type, (short) 1));
                String name2 = SchemaSymbols.XSI_NIL;
                String tns2 = SchemaSymbols.URI_XSI;
                XSSimpleType type2 = schemaFactory.getBuiltInType("boolean");
                this.fGlobalAttrDecls.put(name2, new BuiltinAttrDecl(name2, tns2, type2, (short) 1));
                XSSimpleType anyURI = schemaFactory.getBuiltInType(SchemaSymbols.ATTVAL_ANYURI);
                String name3 = SchemaSymbols.XSI_SCHEMALOCATION;
                String tns3 = SchemaSymbols.URI_XSI;
                XSSimpleType type3 = schemaFactory.createTypeList("#AnonType_schemaLocation", SchemaSymbols.URI_XSI, (short) 0, anyURI, null);
                if (type3 instanceof XSSimpleTypeDecl) {
                    ((XSSimpleTypeDecl) type3).setAnonymous(true);
                }
                this.fGlobalAttrDecls.put(name3, new BuiltinAttrDecl(name3, tns3, type3, (short) 1));
                String name4 = SchemaSymbols.XSI_NONAMESPACESCHEMALOCATION;
                String tns4 = SchemaSymbols.URI_XSI;
                this.fGlobalAttrDecls.put(name4, new BuiltinAttrDecl(name4, tns4, anyURI, (short) 1));
            }
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar, com.sun.org.apache.xerces.internal.xni.grammars.Grammar
        public XMLGrammarDescription getGrammarDescription() {
            return this.fGrammarDescription.makeClone();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void setImportedGrammars(Vector importedGrammars) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeDecl(XSAttributeDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeDecl(XSAttributeDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalElementDecl(XSElementDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalElementDecl(XSElementDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalElementDeclAll(XSElementDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalGroupDecl(XSGroupDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalGroupDecl(XSGroupDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalNotationDecl(XSNotationDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalNotationDecl(XSNotationDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalTypeDecl(XSTypeDefinition decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalTypeDecl(XSTypeDefinition decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalSimpleTypeDecl(XSSimpleType decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalSimpleTypeDecl(XSSimpleType decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addComplexTypeDecl(XSComplexTypeDecl decl, SimpleLocator locator) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addRedefinedGroupDecl(XSGroupDecl derived, XSGroupDecl base, SimpleLocator locator) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public synchronized void addDocument(Object document, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        synchronized DOMParser getDOMParser() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        synchronized SAXParser getSAXParser() {
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SchemaGrammar$Schema4Annotations.class */
    public static final class Schema4Annotations extends SchemaGrammar {
        public static final Schema4Annotations INSTANCE = new Schema4Annotations();

        private Schema4Annotations() {
            this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            this.fGrammarDescription = new XSDDescription();
            this.fGrammarDescription.fContextType = (short) 3;
            this.fGrammarDescription.setNamespace(SchemaSymbols.URI_SCHEMAFORSCHEMA);
            this.fGlobalAttrDecls = new SymbolHash(1);
            this.fGlobalAttrGrpDecls = new SymbolHash(1);
            this.fGlobalElemDecls = new SymbolHash(6);
            this.fGlobalGroupDecls = new SymbolHash(1);
            this.fGlobalNotationDecls = new SymbolHash(1);
            this.fGlobalIDConstraintDecls = new SymbolHash(1);
            this.fGlobalAttrDeclsExt = new SymbolHash(1);
            this.fGlobalAttrGrpDeclsExt = new SymbolHash(1);
            this.fGlobalElemDeclsExt = new SymbolHash(6);
            this.fGlobalGroupDeclsExt = new SymbolHash(1);
            this.fGlobalNotationDeclsExt = new SymbolHash(1);
            this.fGlobalIDConstraintDeclsExt = new SymbolHash(1);
            this.fGlobalTypeDeclsExt = new SymbolHash(1);
            this.fAllGlobalElemDecls = new SymbolHash(6);
            this.fGlobalTypeDecls = SG_SchemaNS.fGlobalTypeDecls;
            XSElementDecl annotationDecl = createAnnotationElementDecl(SchemaSymbols.ELT_ANNOTATION);
            XSElementDecl documentationDecl = createAnnotationElementDecl(SchemaSymbols.ELT_DOCUMENTATION);
            XSElementDecl appinfoDecl = createAnnotationElementDecl(SchemaSymbols.ELT_APPINFO);
            this.fGlobalElemDecls.put(annotationDecl.fName, annotationDecl);
            this.fGlobalElemDecls.put(documentationDecl.fName, documentationDecl);
            this.fGlobalElemDecls.put(appinfoDecl.fName, appinfoDecl);
            this.fGlobalElemDeclsExt.put("," + annotationDecl.fName, annotationDecl);
            this.fGlobalElemDeclsExt.put("," + documentationDecl.fName, documentationDecl);
            this.fGlobalElemDeclsExt.put("," + appinfoDecl.fName, appinfoDecl);
            this.fAllGlobalElemDecls.put(annotationDecl, annotationDecl);
            this.fAllGlobalElemDecls.put(documentationDecl, documentationDecl);
            this.fAllGlobalElemDecls.put(appinfoDecl, appinfoDecl);
            XSComplexTypeDecl annotationType = new XSComplexTypeDecl();
            XSComplexTypeDecl documentationType = new XSComplexTypeDecl();
            XSComplexTypeDecl appinfoType = new XSComplexTypeDecl();
            annotationDecl.fType = annotationType;
            documentationDecl.fType = documentationType;
            appinfoDecl.fType = appinfoType;
            XSAttributeGroupDecl annotationAttrs = new XSAttributeGroupDecl();
            XSAttributeGroupDecl documentationAttrs = new XSAttributeGroupDecl();
            XSAttributeGroupDecl appinfoAttrs = new XSAttributeGroupDecl();
            XSAttributeUseImpl annotationIDAttr = new XSAttributeUseImpl();
            annotationIDAttr.fAttrDecl = new XSAttributeDecl();
            annotationIDAttr.fAttrDecl.setValues(SchemaSymbols.ATT_ID, null, (XSSimpleType) this.fGlobalTypeDecls.get("ID"), (short) 0, (short) 2, null, annotationType, null);
            annotationIDAttr.fUse = (short) 0;
            annotationIDAttr.fConstraintType = (short) 0;
            XSAttributeUseImpl documentationSourceAttr = new XSAttributeUseImpl();
            documentationSourceAttr.fAttrDecl = new XSAttributeDecl();
            documentationSourceAttr.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, null, (XSSimpleType) this.fGlobalTypeDecls.get(SchemaSymbols.ATTVAL_ANYURI), (short) 0, (short) 2, null, documentationType, null);
            documentationSourceAttr.fUse = (short) 0;
            documentationSourceAttr.fConstraintType = (short) 0;
            XSAttributeUseImpl documentationLangAttr = new XSAttributeUseImpl();
            documentationLangAttr.fAttrDecl = new XSAttributeDecl();
            documentationLangAttr.fAttrDecl.setValues("lang".intern(), NamespaceContext.XML_URI, (XSSimpleType) this.fGlobalTypeDecls.get("language"), (short) 0, (short) 2, null, documentationType, null);
            documentationLangAttr.fUse = (short) 0;
            documentationLangAttr.fConstraintType = (short) 0;
            XSAttributeUseImpl appinfoSourceAttr = new XSAttributeUseImpl();
            appinfoSourceAttr.fAttrDecl = new XSAttributeDecl();
            appinfoSourceAttr.fAttrDecl.setValues(SchemaSymbols.ATT_SOURCE, null, (XSSimpleType) this.fGlobalTypeDecls.get(SchemaSymbols.ATTVAL_ANYURI), (short) 0, (short) 2, null, appinfoType, null);
            appinfoSourceAttr.fUse = (short) 0;
            appinfoSourceAttr.fConstraintType = (short) 0;
            XSWildcardDecl otherAttrs = new XSWildcardDecl();
            otherAttrs.fNamespaceList = new String[]{this.fTargetNamespace, null};
            otherAttrs.fType = (short) 2;
            otherAttrs.fProcessContents = (short) 3;
            annotationAttrs.addAttributeUse(annotationIDAttr);
            annotationAttrs.fAttributeWC = otherAttrs;
            documentationAttrs.addAttributeUse(documentationSourceAttr);
            documentationAttrs.addAttributeUse(documentationLangAttr);
            documentationAttrs.fAttributeWC = otherAttrs;
            appinfoAttrs.addAttributeUse(appinfoSourceAttr);
            appinfoAttrs.fAttributeWC = otherAttrs;
            XSParticleDecl annotationParticle = createUnboundedModelGroupParticle();
            XSModelGroupImpl annotationChoice = new XSModelGroupImpl();
            annotationChoice.fCompositor = (short) 101;
            annotationChoice.fParticleCount = 2;
            annotationChoice.fParticles = new XSParticleDecl[2];
            annotationChoice.fParticles[0] = createChoiceElementParticle(appinfoDecl);
            annotationChoice.fParticles[1] = createChoiceElementParticle(documentationDecl);
            annotationParticle.fValue = annotationChoice;
            XSParticleDecl anyWCSequenceParticle = createUnboundedAnyWildcardSequenceParticle();
            annotationType.setValues("#AnonType_" + SchemaSymbols.ELT_ANNOTATION, this.fTargetNamespace, SchemaGrammar.fAnyType, (short) 2, (short) 0, (short) 3, (short) 2, false, annotationAttrs, null, annotationParticle, new XSObjectListImpl(null, 0));
            annotationType.setName("#AnonType_" + SchemaSymbols.ELT_ANNOTATION);
            annotationType.setIsAnonymous();
            documentationType.setValues("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION, this.fTargetNamespace, SchemaGrammar.fAnyType, (short) 2, (short) 0, (short) 3, (short) 3, false, documentationAttrs, null, anyWCSequenceParticle, new XSObjectListImpl(null, 0));
            documentationType.setName("#AnonType_" + SchemaSymbols.ELT_DOCUMENTATION);
            documentationType.setIsAnonymous();
            appinfoType.setValues("#AnonType_" + SchemaSymbols.ELT_APPINFO, this.fTargetNamespace, SchemaGrammar.fAnyType, (short) 2, (short) 0, (short) 3, (short) 3, false, appinfoAttrs, null, anyWCSequenceParticle, new XSObjectListImpl(null, 0));
            appinfoType.setName("#AnonType_" + SchemaSymbols.ELT_APPINFO);
            appinfoType.setIsAnonymous();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar, com.sun.org.apache.xerces.internal.xni.grammars.Grammar
        public XMLGrammarDescription getGrammarDescription() {
            return this.fGrammarDescription.makeClone();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void setImportedGrammars(Vector importedGrammars) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeDecl(XSAttributeDecl decl) {
        }

        public void addGlobalAttributeDecl(XSAttributeGroupDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalElementDecl(XSElementDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalElementDecl(XSElementDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalElementDeclAll(XSElementDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalGroupDecl(XSGroupDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalGroupDecl(XSGroupDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalNotationDecl(XSNotationDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalNotationDecl(XSNotationDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalTypeDecl(XSTypeDefinition decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalTypeDecl(XSTypeDefinition decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalSimpleTypeDecl(XSSimpleType decl) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addGlobalSimpleTypeDecl(XSSimpleType decl, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addComplexTypeDecl(XSComplexTypeDecl decl, SimpleLocator locator) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public void addRedefinedGroupDecl(XSGroupDecl derived, XSGroupDecl base, SimpleLocator locator) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        public synchronized void addDocument(Object document, String location) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        synchronized DOMParser getDOMParser() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar
        synchronized SAXParser getSAXParser() {
            return null;
        }

        private XSElementDecl createAnnotationElementDecl(String localName) {
            XSElementDecl eDecl = new XSElementDecl();
            eDecl.fName = localName;
            eDecl.fTargetNamespace = this.fTargetNamespace;
            eDecl.setIsGlobal();
            eDecl.fBlock = (short) 7;
            eDecl.setConstraintType((short) 0);
            return eDecl;
        }

        private XSParticleDecl createUnboundedModelGroupParticle() {
            XSParticleDecl particle = new XSParticleDecl();
            particle.fMinOccurs = 0;
            particle.fMaxOccurs = -1;
            particle.fType = (short) 3;
            return particle;
        }

        private XSParticleDecl createChoiceElementParticle(XSElementDecl ref) {
            XSParticleDecl particle = new XSParticleDecl();
            particle.fMinOccurs = 1;
            particle.fMaxOccurs = 1;
            particle.fType = (short) 1;
            particle.fValue = ref;
            return particle;
        }

        private XSParticleDecl createUnboundedAnyWildcardSequenceParticle() {
            XSParticleDecl particle = createUnboundedModelGroupParticle();
            XSModelGroupImpl sequence = new XSModelGroupImpl();
            sequence.fCompositor = (short) 102;
            sequence.fParticleCount = 1;
            sequence.fParticles = new XSParticleDecl[1];
            sequence.fParticles[0] = createAnyLaxWildcardParticle();
            particle.fValue = sequence;
            return particle;
        }

        private XSParticleDecl createAnyLaxWildcardParticle() {
            XSParticleDecl particle = new XSParticleDecl();
            particle.fMinOccurs = 1;
            particle.fMaxOccurs = 1;
            particle.fType = (short) 2;
            XSWildcardDecl anyWC = new XSWildcardDecl();
            anyWC.fNamespaceList = null;
            anyWC.fType = (short) 1;
            anyWC.fProcessContents = (short) 3;
            particle.fValue = anyWC;
            return particle;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.Grammar
    public XMLGrammarDescription getGrammarDescription() {
        return this.fGrammarDescription;
    }

    public boolean isNamespaceAware() {
        return true;
    }

    public void setImportedGrammars(Vector importedGrammars) {
        this.fImported = importedGrammars;
    }

    public Vector getImportedGrammars() {
        return this.fImported;
    }

    public final String getTargetNamespace() {
        return this.fTargetNamespace;
    }

    public void addGlobalAttributeDecl(XSAttributeDecl decl) {
        this.fGlobalAttrDecls.put(decl.fName, decl);
        decl.setNamespaceItem(this);
    }

    public void addGlobalAttributeDecl(XSAttributeDecl decl, String location) {
        this.fGlobalAttrDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
        if (decl.getNamespaceItem() == null) {
            decl.setNamespaceItem(this);
        }
    }

    public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl) {
        this.fGlobalAttrGrpDecls.put(decl.fName, decl);
        decl.setNamespaceItem(this);
    }

    public void addGlobalAttributeGroupDecl(XSAttributeGroupDecl decl, String location) {
        this.fGlobalAttrGrpDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
        if (decl.getNamespaceItem() == null) {
            decl.setNamespaceItem(this);
        }
    }

    public void addGlobalElementDeclAll(XSElementDecl decl) {
        if (this.fAllGlobalElemDecls.get(decl) == null) {
            this.fAllGlobalElemDecls.put(decl, decl);
            if (decl.fSubGroup != null) {
                if (this.fSubGroupCount == this.fSubGroups.length) {
                    this.fSubGroups = resize(this.fSubGroups, this.fSubGroupCount + 16);
                }
                XSElementDecl[] xSElementDeclArr = this.fSubGroups;
                int i2 = this.fSubGroupCount;
                this.fSubGroupCount = i2 + 1;
                xSElementDeclArr[i2] = decl;
            }
        }
    }

    public void addGlobalElementDecl(XSElementDecl decl) {
        this.fGlobalElemDecls.put(decl.fName, decl);
        decl.setNamespaceItem(this);
    }

    public void addGlobalElementDecl(XSElementDecl decl, String location) {
        this.fGlobalElemDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
        if (decl.getNamespaceItem() == null) {
            decl.setNamespaceItem(this);
        }
    }

    public void addGlobalGroupDecl(XSGroupDecl decl) {
        this.fGlobalGroupDecls.put(decl.fName, decl);
        decl.setNamespaceItem(this);
    }

    public void addGlobalGroupDecl(XSGroupDecl decl, String location) {
        this.fGlobalGroupDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
        if (decl.getNamespaceItem() == null) {
            decl.setNamespaceItem(this);
        }
    }

    public void addGlobalNotationDecl(XSNotationDecl decl) {
        this.fGlobalNotationDecls.put(decl.fName, decl);
        decl.setNamespaceItem(this);
    }

    public void addGlobalNotationDecl(XSNotationDecl decl, String location) {
        this.fGlobalNotationDeclsExt.put((location != null ? location : "") + "," + decl.fName, decl);
        if (decl.getNamespaceItem() == null) {
            decl.setNamespaceItem(this);
        }
    }

    public void addGlobalTypeDecl(XSTypeDefinition decl) {
        this.fGlobalTypeDecls.put(decl.getName(), decl);
        if (decl instanceof XSComplexTypeDecl) {
            ((XSComplexTypeDecl) decl).setNamespaceItem(this);
        } else if (decl instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl) decl).setNamespaceItem(this);
        }
    }

    public void addGlobalTypeDecl(XSTypeDefinition decl, String location) {
        this.fGlobalTypeDeclsExt.put((location != null ? location : "") + "," + decl.getName(), decl);
        if (decl.getNamespaceItem() == null) {
            if (decl instanceof XSComplexTypeDecl) {
                ((XSComplexTypeDecl) decl).setNamespaceItem(this);
            } else if (decl instanceof XSSimpleTypeDecl) {
                ((XSSimpleTypeDecl) decl).setNamespaceItem(this);
            }
        }
    }

    public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl) {
        this.fGlobalTypeDecls.put(decl.getName(), decl);
        decl.setNamespaceItem(this);
    }

    public void addGlobalComplexTypeDecl(XSComplexTypeDecl decl, String location) {
        this.fGlobalTypeDeclsExt.put((location != null ? location : "") + "," + decl.getName(), decl);
        if (decl.getNamespaceItem() == null) {
            decl.setNamespaceItem(this);
        }
    }

    public void addGlobalSimpleTypeDecl(XSSimpleType decl) {
        this.fGlobalTypeDecls.put(decl.getName(), decl);
        if (decl instanceof XSSimpleTypeDecl) {
            ((XSSimpleTypeDecl) decl).setNamespaceItem(this);
        }
    }

    public void addGlobalSimpleTypeDecl(XSSimpleType decl, String location) {
        this.fGlobalTypeDeclsExt.put((location != null ? location : "") + "," + decl.getName(), decl);
        if (decl.getNamespaceItem() == null && (decl instanceof XSSimpleTypeDecl)) {
            ((XSSimpleTypeDecl) decl).setNamespaceItem(this);
        }
    }

    public final void addIDConstraintDecl(XSElementDecl elmDecl, IdentityConstraint decl) {
        elmDecl.addIDConstraint(decl);
        this.fGlobalIDConstraintDecls.put(decl.getIdentityConstraintName(), decl);
    }

    public final void addIDConstraintDecl(XSElementDecl elmDecl, IdentityConstraint decl, String location) {
        this.fGlobalIDConstraintDeclsExt.put((location != null ? location : "") + "," + decl.getIdentityConstraintName(), decl);
    }

    public final XSAttributeDecl getGlobalAttributeDecl(String declName) {
        return (XSAttributeDecl) this.fGlobalAttrDecls.get(declName);
    }

    public final XSAttributeDecl getGlobalAttributeDecl(String declName, String location) {
        return (XSAttributeDecl) this.fGlobalAttrDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String declName) {
        return (XSAttributeGroupDecl) this.fGlobalAttrGrpDecls.get(declName);
    }

    public final XSAttributeGroupDecl getGlobalAttributeGroupDecl(String declName, String location) {
        return (XSAttributeGroupDecl) this.fGlobalAttrGrpDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final XSElementDecl getGlobalElementDecl(String declName) {
        return (XSElementDecl) this.fGlobalElemDecls.get(declName);
    }

    public final XSElementDecl getGlobalElementDecl(String declName, String location) {
        return (XSElementDecl) this.fGlobalElemDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final XSGroupDecl getGlobalGroupDecl(String declName) {
        return (XSGroupDecl) this.fGlobalGroupDecls.get(declName);
    }

    public final XSGroupDecl getGlobalGroupDecl(String declName, String location) {
        return (XSGroupDecl) this.fGlobalGroupDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final XSNotationDecl getGlobalNotationDecl(String declName) {
        return (XSNotationDecl) this.fGlobalNotationDecls.get(declName);
    }

    public final XSNotationDecl getGlobalNotationDecl(String declName, String location) {
        return (XSNotationDecl) this.fGlobalNotationDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final XSTypeDefinition getGlobalTypeDecl(String declName) {
        return (XSTypeDefinition) this.fGlobalTypeDecls.get(declName);
    }

    public final XSTypeDefinition getGlobalTypeDecl(String declName, String location) {
        return (XSTypeDefinition) this.fGlobalTypeDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final IdentityConstraint getIDConstraintDecl(String declName) {
        return (IdentityConstraint) this.fGlobalIDConstraintDecls.get(declName);
    }

    public final IdentityConstraint getIDConstraintDecl(String declName, String location) {
        return (IdentityConstraint) this.fGlobalIDConstraintDeclsExt.get((location != null ? location : "") + "," + declName);
    }

    public final boolean hasIDConstraints() {
        return this.fGlobalIDConstraintDecls.getLength() > 0;
    }

    public void addComplexTypeDecl(XSComplexTypeDecl decl, SimpleLocator locator) {
        if (this.fCTCount == this.fComplexTypeDecls.length) {
            this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount + 16);
            this.fCTLocators = resize(this.fCTLocators, this.fCTCount + 16);
        }
        this.fCTLocators[this.fCTCount] = locator;
        XSComplexTypeDecl[] xSComplexTypeDeclArr = this.fComplexTypeDecls;
        int i2 = this.fCTCount;
        this.fCTCount = i2 + 1;
        xSComplexTypeDeclArr[i2] = decl;
    }

    public void addRedefinedGroupDecl(XSGroupDecl derived, XSGroupDecl base, SimpleLocator locator) {
        if (this.fRGCount == this.fRedefinedGroupDecls.length) {
            this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount << 1);
            this.fRGLocators = resize(this.fRGLocators, this.fRGCount);
        }
        this.fRGLocators[this.fRGCount / 2] = locator;
        XSGroupDecl[] xSGroupDeclArr = this.fRedefinedGroupDecls;
        int i2 = this.fRGCount;
        this.fRGCount = i2 + 1;
        xSGroupDeclArr[i2] = derived;
        XSGroupDecl[] xSGroupDeclArr2 = this.fRedefinedGroupDecls;
        int i3 = this.fRGCount;
        this.fRGCount = i3 + 1;
        xSGroupDeclArr2[i3] = base;
    }

    final XSComplexTypeDecl[] getUncheckedComplexTypeDecls() {
        if (this.fCTCount < this.fComplexTypeDecls.length) {
            this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
            this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
        }
        return this.fComplexTypeDecls;
    }

    final SimpleLocator[] getUncheckedCTLocators() {
        if (this.fCTCount < this.fCTLocators.length) {
            this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
            this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
        }
        return this.fCTLocators;
    }

    final XSGroupDecl[] getRedefinedGroupDecls() {
        if (this.fRGCount < this.fRedefinedGroupDecls.length) {
            this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount);
            this.fRGLocators = resize(this.fRGLocators, this.fRGCount / 2);
        }
        return this.fRedefinedGroupDecls;
    }

    final SimpleLocator[] getRGLocators() {
        if (this.fRGCount < this.fRedefinedGroupDecls.length) {
            this.fRedefinedGroupDecls = resize(this.fRedefinedGroupDecls, this.fRGCount);
            this.fRGLocators = resize(this.fRGLocators, this.fRGCount / 2);
        }
        return this.fRGLocators;
    }

    final void setUncheckedTypeNum(int newSize) {
        this.fCTCount = newSize;
        this.fComplexTypeDecls = resize(this.fComplexTypeDecls, this.fCTCount);
        this.fCTLocators = resize(this.fCTLocators, this.fCTCount);
    }

    final XSElementDecl[] getSubstitutionGroups() {
        if (this.fSubGroupCount < this.fSubGroups.length) {
            this.fSubGroups = resize(this.fSubGroups, this.fSubGroupCount);
        }
        return this.fSubGroups;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SchemaGrammar$XSAnyType.class */
    private static class XSAnyType extends XSComplexTypeDecl {
        public XSAnyType() {
            this.fName = SchemaSymbols.ATTVAL_ANYTYPE;
            this.fTargetNamespace = SchemaSymbols.URI_SCHEMAFORSCHEMA;
            this.fBaseType = this;
            this.fDerivedBy = (short) 2;
            this.fContentType = (short) 3;
            this.fParticle = null;
            this.fAttrGrp = null;
        }

        public void setValues(String name, String targetNamespace, XSTypeDefinition baseType, short derivedBy, short schemaFinal, short block, short contentType, boolean isAbstract, XSAttributeGroupDecl attrGrp, XSSimpleType simpleType, XSParticleDecl particle) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
        public void setName(String name) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
        public void setIsAbstractType() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
        public void setContainsTypeID() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
        public void setIsAnonymous() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
        public void reset() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl, com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
        public XSObjectList getAttributeUses() {
            return XSObjectListImpl.EMPTY_LIST;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl
        public XSAttributeGroupDecl getAttrGrp() {
            XSWildcardDecl wildcard = new XSWildcardDecl();
            wildcard.fProcessContents = (short) 3;
            XSAttributeGroupDecl attrGrp = new XSAttributeGroupDecl();
            attrGrp.fAttributeWC = wildcard;
            return attrGrp;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl, com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
        public XSWildcard getAttributeWildcard() {
            XSWildcardDecl wildcard = new XSWildcardDecl();
            wildcard.fProcessContents = (short) 3;
            return wildcard;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl, com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
        public XSParticle getParticle() {
            XSWildcardDecl wildcard = new XSWildcardDecl();
            wildcard.fProcessContents = (short) 3;
            XSParticleDecl particleW = new XSParticleDecl();
            particleW.fMinOccurs = 0;
            particleW.fMaxOccurs = -1;
            particleW.fType = (short) 2;
            particleW.fValue = wildcard;
            XSModelGroupImpl group = new XSModelGroupImpl();
            group.fCompositor = (short) 102;
            group.fParticleCount = 1;
            group.fParticles = new XSParticleDecl[1];
            group.fParticles[0] = particleW;
            XSParticleDecl particleG = new XSParticleDecl();
            particleG.fType = (short) 3;
            particleG.fValue = group;
            return particleG;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl, com.sun.org.apache.xerces.internal.xs.XSComplexTypeDefinition
        public XSObjectList getAnnotations() {
            return XSObjectListImpl.EMPTY_LIST;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl, com.sun.org.apache.xerces.internal.xs.XSObject
        public XSNamespaceItem getNamespaceItem() {
            return SchemaGrammar.SG_SchemaNS;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/SchemaGrammar$BuiltinAttrDecl.class */
    private static class BuiltinAttrDecl extends XSAttributeDecl {
        public BuiltinAttrDecl(String name, String tns, XSSimpleType type, short scope) {
            this.fName = name;
            this.fTargetNamespace = tns;
            this.fType = type;
            this.fScope = scope;
        }

        public void setValues(String name, String targetNamespace, XSSimpleType simpleType, short constraintType, short scope, ValidatedInfo valInfo, XSComplexTypeDecl enclosingCT) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl
        public void reset() {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl, com.sun.org.apache.xerces.internal.xs.XSAttributeDeclaration
        public XSAnnotation getAnnotation() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl, com.sun.org.apache.xerces.internal.xs.XSObject
        public XSNamespaceItem getNamespaceItem() {
            return SchemaGrammar.SG_XSI;
        }
    }

    public static SchemaGrammar getS4SGrammar(short schemaVersion) {
        if (schemaVersion == 1) {
            return SG_SchemaNS;
        }
        return SG_SchemaNSExtended;
    }

    static final XSComplexTypeDecl[] resize(XSComplexTypeDecl[] oldArray, int newSize) {
        XSComplexTypeDecl[] newArray = new XSComplexTypeDecl[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
        return newArray;
    }

    static final XSGroupDecl[] resize(XSGroupDecl[] oldArray, int newSize) {
        XSGroupDecl[] newArray = new XSGroupDecl[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
        return newArray;
    }

    static final XSElementDecl[] resize(XSElementDecl[] oldArray, int newSize) {
        XSElementDecl[] newArray = new XSElementDecl[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
        return newArray;
    }

    static final SimpleLocator[] resize(SimpleLocator[] oldArray, int newSize) {
        SimpleLocator[] newArray = new SimpleLocator[newSize];
        System.arraycopy(oldArray, 0, newArray, 0, Math.min(oldArray.length, newSize));
        return newArray;
    }

    public synchronized void addDocument(Object document, String location) {
        if (this.fDocuments == null) {
            this.fDocuments = new Vector();
            this.fLocations = new Vector();
        }
        this.fDocuments.addElement(document);
        this.fLocations.addElement(location);
    }

    public synchronized void removeDocument(int index) {
        if (this.fDocuments != null && index >= 0 && index < this.fDocuments.size()) {
            this.fDocuments.removeElementAt(index);
            this.fLocations.removeElementAt(index);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public String getSchemaNamespace() {
        return this.fTargetNamespace;
    }

    synchronized DOMParser getDOMParser() {
        DOMParser parser;
        if (this.fDOMParser != null && (parser = (DOMParser) this.fDOMParser.get()) != null) {
            return parser;
        }
        XML11Configuration config = new XML11Configuration(this.fSymbolTable);
        config.setFeature("http://xml.org/sax/features/namespaces", true);
        config.setFeature("http://xml.org/sax/features/validation", false);
        DOMParser parser2 = new DOMParser(config);
        try {
            parser2.setFeature("http://apache.org/xml/features/dom/defer-node-expansion", false);
        } catch (SAXException e2) {
        }
        this.fDOMParser = new SoftReference(parser2);
        return parser2;
    }

    synchronized SAXParser getSAXParser() {
        SAXParser parser;
        if (this.fSAXParser != null && (parser = (SAXParser) this.fSAXParser.get()) != null) {
            return parser;
        }
        XML11Configuration config = new XML11Configuration(this.fSymbolTable);
        config.setFeature("http://xml.org/sax/features/namespaces", true);
        config.setFeature("http://xml.org/sax/features/validation", false);
        SAXParser parser2 = new SAXParser(config);
        this.fSAXParser = new SoftReference(parser2);
        return parser2;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public synchronized XSNamedMap getComponents(short objectType) {
        if (objectType <= 0 || objectType > 16 || !GLOBAL_COMP[objectType]) {
            return XSNamedMapImpl.EMPTY_MAP;
        }
        if (this.fComponents == null) {
            this.fComponents = new XSNamedMap[17];
        }
        if (this.fComponents[objectType] == null) {
            SymbolHash table = null;
            switch (objectType) {
                case 1:
                    table = this.fGlobalAttrDecls;
                    break;
                case 2:
                    table = this.fGlobalElemDecls;
                    break;
                case 3:
                case 15:
                case 16:
                    table = this.fGlobalTypeDecls;
                    break;
                case 5:
                    table = this.fGlobalAttrGrpDecls;
                    break;
                case 6:
                    table = this.fGlobalGroupDecls;
                    break;
                case 11:
                    table = this.fGlobalNotationDecls;
                    break;
            }
            if (objectType == 15 || objectType == 16) {
                this.fComponents[objectType] = new XSNamedMap4Types(this.fTargetNamespace, table, objectType);
            } else {
                this.fComponents[objectType] = new XSNamedMapImpl(this.fTargetNamespace, table);
            }
        }
        return this.fComponents[objectType];
    }

    public synchronized ObjectList getComponentsExt(short objectType) {
        if (objectType <= 0 || objectType > 16 || !GLOBAL_COMP[objectType]) {
            return ObjectListImpl.EMPTY_LIST;
        }
        if (this.fComponentsExt == null) {
            this.fComponentsExt = new ObjectList[17];
        }
        if (this.fComponentsExt[objectType] == null) {
            SymbolHash table = null;
            switch (objectType) {
                case 1:
                    table = this.fGlobalAttrDeclsExt;
                    break;
                case 2:
                    table = this.fGlobalElemDeclsExt;
                    break;
                case 3:
                case 15:
                case 16:
                    table = this.fGlobalTypeDeclsExt;
                    break;
                case 5:
                    table = this.fGlobalAttrGrpDeclsExt;
                    break;
                case 6:
                    table = this.fGlobalGroupDeclsExt;
                    break;
                case 11:
                    table = this.fGlobalNotationDeclsExt;
                    break;
            }
            Object[] entries = table.getEntries();
            this.fComponentsExt[objectType] = new ObjectListImpl(entries, entries.length);
        }
        return this.fComponentsExt[objectType];
    }

    public synchronized void resetComponents() {
        this.fComponents = null;
        this.fComponentsExt = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSTypeDefinition getTypeDefinition(String name) {
        return getGlobalTypeDecl(name);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSAttributeDeclaration getAttributeDeclaration(String name) {
        return getGlobalAttributeDecl(name);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSElementDeclaration getElementDeclaration(String name) {
        return getGlobalElementDecl(name);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSAttributeGroupDefinition getAttributeGroup(String name) {
        return getGlobalAttributeGroupDecl(name);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSModelGroupDefinition getModelGroupDefinition(String name) {
        return getGlobalGroupDecl(name);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSNotationDeclaration getNotationDeclaration(String name) {
        return getGlobalNotationDecl(name);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public StringList getDocumentLocations() {
        return new StringListImpl(this.fLocations);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar
    public XSModel toXSModel() {
        return new XSModelImpl(new SchemaGrammar[]{this});
    }

    @Override // com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar
    public XSModel toXSModel(XSGrammar[] grammars) {
        if (grammars == null || grammars.length == 0) {
            return toXSModel();
        }
        int len = grammars.length;
        boolean hasSelf = false;
        int i2 = 0;
        while (true) {
            if (i2 >= len) {
                break;
            }
            if (grammars[i2] != this) {
                i2++;
            } else {
                hasSelf = true;
                break;
            }
        }
        SchemaGrammar[] gs = new SchemaGrammar[hasSelf ? len : len + 1];
        for (int i3 = 0; i3 < len; i3++) {
            gs[i3] = (SchemaGrammar) grammars[i3];
        }
        if (!hasSelf) {
            gs[len] = this;
        }
        return new XSModelImpl(gs);
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSNamespaceItem
    public XSObjectList getAnnotations() {
        if (this.fNumAnnotations == 0) {
            return XSObjectListImpl.EMPTY_LIST;
        }
        return new XSObjectListImpl(this.fAnnotations, this.fNumAnnotations);
    }

    public void addAnnotation(XSAnnotationImpl annotation) {
        if (annotation == null) {
            return;
        }
        if (this.fAnnotations == null) {
            this.fAnnotations = new XSAnnotationImpl[2];
        } else if (this.fNumAnnotations == this.fAnnotations.length) {
            XSAnnotationImpl[] newArray = new XSAnnotationImpl[this.fNumAnnotations << 1];
            System.arraycopy(this.fAnnotations, 0, newArray, 0, this.fNumAnnotations);
            this.fAnnotations = newArray;
        }
        XSAnnotationImpl[] xSAnnotationImplArr = this.fAnnotations;
        int i2 = this.fNumAnnotations;
        this.fNumAnnotations = i2 + 1;
        xSAnnotationImplArr[i2] = annotation;
    }

    public void setImmutable(boolean isImmutable) {
        this.fIsImmutable = isImmutable;
    }

    public boolean isImmutable() {
        return this.fIsImmutable;
    }
}
