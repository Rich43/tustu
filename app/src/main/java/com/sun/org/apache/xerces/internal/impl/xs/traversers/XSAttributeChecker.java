package com.sun.org.apache.xerces.internal.impl.xs.traversers;

import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaGrammar;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.XSAttributeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSGrammarBucket;
import com.sun.org.apache.xerces.internal.impl.xs.util.XInt;
import com.sun.org.apache.xerces.internal.impl.xs.util.XIntPool;
import com.sun.org.apache.xerces.internal.util.DOMUtil;
import com.sun.org.apache.xerces.internal.util.SymbolTable;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.util.XMLSymbols;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/traversers/XSAttributeChecker.class */
public class XSAttributeChecker {
    private static final String ELEMENT_N = "element_n";
    private static final String ELEMENT_R = "element_r";
    private static final String ATTRIBUTE_N = "attribute_n";
    private static final String ATTRIBUTE_R = "attribute_r";
    private static int ATTIDX_COUNT;
    public static final int ATTIDX_ABSTRACT;
    public static final int ATTIDX_AFORMDEFAULT;
    public static final int ATTIDX_BASE;
    public static final int ATTIDX_BLOCK;
    public static final int ATTIDX_BLOCKDEFAULT;
    public static final int ATTIDX_DEFAULT;
    public static final int ATTIDX_EFORMDEFAULT;
    public static final int ATTIDX_FINAL;
    public static final int ATTIDX_FINALDEFAULT;
    public static final int ATTIDX_FIXED;
    public static final int ATTIDX_FORM;
    public static final int ATTIDX_ID;
    public static final int ATTIDX_ITEMTYPE;
    public static final int ATTIDX_MAXOCCURS;
    public static final int ATTIDX_MEMBERTYPES;
    public static final int ATTIDX_MINOCCURS;
    public static final int ATTIDX_MIXED;
    public static final int ATTIDX_NAME;
    public static final int ATTIDX_NAMESPACE;
    public static final int ATTIDX_NAMESPACE_LIST;
    public static final int ATTIDX_NILLABLE;
    public static final int ATTIDX_NONSCHEMA;
    public static final int ATTIDX_PROCESSCONTENTS;
    public static final int ATTIDX_PUBLIC;
    public static final int ATTIDX_REF;
    public static final int ATTIDX_REFER;
    public static final int ATTIDX_SCHEMALOCATION;
    public static final int ATTIDX_SOURCE;
    public static final int ATTIDX_SUBSGROUP;
    public static final int ATTIDX_SYSTEM;
    public static final int ATTIDX_TARGETNAMESPACE;
    public static final int ATTIDX_TYPE;
    public static final int ATTIDX_USE;
    public static final int ATTIDX_VALUE;
    public static final int ATTIDX_ENUMNSDECLS;
    public static final int ATTIDX_VERSION;
    public static final int ATTIDX_XML_LANG;
    public static final int ATTIDX_XPATH;
    public static final int ATTIDX_FROMDEFAULT;
    public static final int ATTIDX_ISRETURNED;
    private static final XIntPool fXIntPool;
    private static final XInt INT_QUALIFIED;
    private static final XInt INT_UNQUALIFIED;
    private static final XInt INT_EMPTY_SET;
    private static final XInt INT_ANY_STRICT;
    private static final XInt INT_ANY_LAX;
    private static final XInt INT_ANY_SKIP;
    private static final XInt INT_ANY_ANY;
    private static final XInt INT_ANY_LIST;
    private static final XInt INT_ANY_NOT;
    private static final XInt INT_USE_OPTIONAL;
    private static final XInt INT_USE_REQUIRED;
    private static final XInt INT_USE_PROHIBITED;
    private static final XInt INT_WS_PRESERVE;
    private static final XInt INT_WS_REPLACE;
    private static final XInt INT_WS_COLLAPSE;
    private static final XInt INT_UNBOUNDED;
    private static final Map fEleAttrsMapG;
    private static final Map fEleAttrsMapL;
    protected static final int DT_ANYURI = 0;
    protected static final int DT_ID = 1;
    protected static final int DT_QNAME = 2;
    protected static final int DT_STRING = 3;
    protected static final int DT_TOKEN = 4;
    protected static final int DT_NCNAME = 5;
    protected static final int DT_XPATH = 6;
    protected static final int DT_XPATH1 = 7;
    protected static final int DT_LANGUAGE = 8;
    protected static final int DT_COUNT = 9;
    private static final XSSimpleType[] fExtraDVs;
    protected static final int DT_BLOCK = -1;
    protected static final int DT_BLOCK1 = -2;
    protected static final int DT_FINAL = -3;
    protected static final int DT_FINAL1 = -4;
    protected static final int DT_FINAL2 = -5;
    protected static final int DT_FORM = -6;
    protected static final int DT_MAXOCCURS = -7;
    protected static final int DT_MAXOCCURS1 = -8;
    protected static final int DT_MEMBERTYPES = -9;
    protected static final int DT_MINOCCURS1 = -10;
    protected static final int DT_NAMESPACE = -11;
    protected static final int DT_PROCESSCONTENTS = -12;
    protected static final int DT_USE = -13;
    protected static final int DT_WHITESPACE = -14;
    protected static final int DT_BOOLEAN = -15;
    protected static final int DT_NONNEGINT = -16;
    protected static final int DT_POSINT = -17;
    protected XSDHandler fSchemaHandler;
    private static boolean[] fSeenTemp;
    static final int INIT_POOL_SIZE = 10;
    static final int INC_POOL_SIZE = 10;
    private static Object[] fTempArray;
    protected SymbolTable fSymbolTable = null;
    protected Map fNonSchemaAttrs = new HashMap();
    protected Vector fNamespaceList = new Vector();
    protected boolean[] fSeen = new boolean[ATTIDX_COUNT];
    Object[][] fArrayPool = new Object[10][ATTIDX_COUNT];
    int fPoolPos = 0;

    static {
        ATTIDX_COUNT = 0;
        int i2 = ATTIDX_COUNT;
        ATTIDX_COUNT = i2 + 1;
        ATTIDX_ABSTRACT = i2;
        int i3 = ATTIDX_COUNT;
        ATTIDX_COUNT = i3 + 1;
        ATTIDX_AFORMDEFAULT = i3;
        int i4 = ATTIDX_COUNT;
        ATTIDX_COUNT = i4 + 1;
        ATTIDX_BASE = i4;
        int i5 = ATTIDX_COUNT;
        ATTIDX_COUNT = i5 + 1;
        ATTIDX_BLOCK = i5;
        int i6 = ATTIDX_COUNT;
        ATTIDX_COUNT = i6 + 1;
        ATTIDX_BLOCKDEFAULT = i6;
        int i7 = ATTIDX_COUNT;
        ATTIDX_COUNT = i7 + 1;
        ATTIDX_DEFAULT = i7;
        int i8 = ATTIDX_COUNT;
        ATTIDX_COUNT = i8 + 1;
        ATTIDX_EFORMDEFAULT = i8;
        int i9 = ATTIDX_COUNT;
        ATTIDX_COUNT = i9 + 1;
        ATTIDX_FINAL = i9;
        int i10 = ATTIDX_COUNT;
        ATTIDX_COUNT = i10 + 1;
        ATTIDX_FINALDEFAULT = i10;
        int i11 = ATTIDX_COUNT;
        ATTIDX_COUNT = i11 + 1;
        ATTIDX_FIXED = i11;
        int i12 = ATTIDX_COUNT;
        ATTIDX_COUNT = i12 + 1;
        ATTIDX_FORM = i12;
        int i13 = ATTIDX_COUNT;
        ATTIDX_COUNT = i13 + 1;
        ATTIDX_ID = i13;
        int i14 = ATTIDX_COUNT;
        ATTIDX_COUNT = i14 + 1;
        ATTIDX_ITEMTYPE = i14;
        int i15 = ATTIDX_COUNT;
        ATTIDX_COUNT = i15 + 1;
        ATTIDX_MAXOCCURS = i15;
        int i16 = ATTIDX_COUNT;
        ATTIDX_COUNT = i16 + 1;
        ATTIDX_MEMBERTYPES = i16;
        int i17 = ATTIDX_COUNT;
        ATTIDX_COUNT = i17 + 1;
        ATTIDX_MINOCCURS = i17;
        int i18 = ATTIDX_COUNT;
        ATTIDX_COUNT = i18 + 1;
        ATTIDX_MIXED = i18;
        int i19 = ATTIDX_COUNT;
        ATTIDX_COUNT = i19 + 1;
        ATTIDX_NAME = i19;
        int i20 = ATTIDX_COUNT;
        ATTIDX_COUNT = i20 + 1;
        ATTIDX_NAMESPACE = i20;
        int i21 = ATTIDX_COUNT;
        ATTIDX_COUNT = i21 + 1;
        ATTIDX_NAMESPACE_LIST = i21;
        int i22 = ATTIDX_COUNT;
        ATTIDX_COUNT = i22 + 1;
        ATTIDX_NILLABLE = i22;
        int i23 = ATTIDX_COUNT;
        ATTIDX_COUNT = i23 + 1;
        ATTIDX_NONSCHEMA = i23;
        int i24 = ATTIDX_COUNT;
        ATTIDX_COUNT = i24 + 1;
        ATTIDX_PROCESSCONTENTS = i24;
        int i25 = ATTIDX_COUNT;
        ATTIDX_COUNT = i25 + 1;
        ATTIDX_PUBLIC = i25;
        int i26 = ATTIDX_COUNT;
        ATTIDX_COUNT = i26 + 1;
        ATTIDX_REF = i26;
        int i27 = ATTIDX_COUNT;
        ATTIDX_COUNT = i27 + 1;
        ATTIDX_REFER = i27;
        int i28 = ATTIDX_COUNT;
        ATTIDX_COUNT = i28 + 1;
        ATTIDX_SCHEMALOCATION = i28;
        int i29 = ATTIDX_COUNT;
        ATTIDX_COUNT = i29 + 1;
        ATTIDX_SOURCE = i29;
        int i30 = ATTIDX_COUNT;
        ATTIDX_COUNT = i30 + 1;
        ATTIDX_SUBSGROUP = i30;
        int i31 = ATTIDX_COUNT;
        ATTIDX_COUNT = i31 + 1;
        ATTIDX_SYSTEM = i31;
        int i32 = ATTIDX_COUNT;
        ATTIDX_COUNT = i32 + 1;
        ATTIDX_TARGETNAMESPACE = i32;
        int i33 = ATTIDX_COUNT;
        ATTIDX_COUNT = i33 + 1;
        ATTIDX_TYPE = i33;
        int i34 = ATTIDX_COUNT;
        ATTIDX_COUNT = i34 + 1;
        ATTIDX_USE = i34;
        int i35 = ATTIDX_COUNT;
        ATTIDX_COUNT = i35 + 1;
        ATTIDX_VALUE = i35;
        int i36 = ATTIDX_COUNT;
        ATTIDX_COUNT = i36 + 1;
        ATTIDX_ENUMNSDECLS = i36;
        int i37 = ATTIDX_COUNT;
        ATTIDX_COUNT = i37 + 1;
        ATTIDX_VERSION = i37;
        int i38 = ATTIDX_COUNT;
        ATTIDX_COUNT = i38 + 1;
        ATTIDX_XML_LANG = i38;
        int i39 = ATTIDX_COUNT;
        ATTIDX_COUNT = i39 + 1;
        ATTIDX_XPATH = i39;
        int i40 = ATTIDX_COUNT;
        ATTIDX_COUNT = i40 + 1;
        ATTIDX_FROMDEFAULT = i40;
        int i41 = ATTIDX_COUNT;
        ATTIDX_COUNT = i41 + 1;
        ATTIDX_ISRETURNED = i41;
        fXIntPool = new XIntPool();
        INT_QUALIFIED = fXIntPool.getXInt(1);
        INT_UNQUALIFIED = fXIntPool.getXInt(0);
        INT_EMPTY_SET = fXIntPool.getXInt(0);
        INT_ANY_STRICT = fXIntPool.getXInt(1);
        INT_ANY_LAX = fXIntPool.getXInt(3);
        INT_ANY_SKIP = fXIntPool.getXInt(2);
        INT_ANY_ANY = fXIntPool.getXInt(1);
        INT_ANY_LIST = fXIntPool.getXInt(3);
        INT_ANY_NOT = fXIntPool.getXInt(2);
        INT_USE_OPTIONAL = fXIntPool.getXInt(0);
        INT_USE_REQUIRED = fXIntPool.getXInt(1);
        INT_USE_PROHIBITED = fXIntPool.getXInt(2);
        INT_WS_PRESERVE = fXIntPool.getXInt(0);
        INT_WS_REPLACE = fXIntPool.getXInt(1);
        INT_WS_COLLAPSE = fXIntPool.getXInt(2);
        INT_UNBOUNDED = fXIntPool.getXInt(-1);
        fEleAttrsMapG = new HashMap(29);
        fEleAttrsMapL = new HashMap(79);
        fExtraDVs = new XSSimpleType[9];
        SchemaGrammar grammar = SchemaGrammar.SG_SchemaNS;
        fExtraDVs[0] = (XSSimpleType) grammar.getGlobalTypeDecl(SchemaSymbols.ATTVAL_ANYURI);
        fExtraDVs[1] = (XSSimpleType) grammar.getGlobalTypeDecl("ID");
        fExtraDVs[2] = (XSSimpleType) grammar.getGlobalTypeDecl(SchemaSymbols.ATTVAL_QNAME);
        fExtraDVs[3] = (XSSimpleType) grammar.getGlobalTypeDecl("string");
        fExtraDVs[4] = (XSSimpleType) grammar.getGlobalTypeDecl(SchemaSymbols.ATTVAL_TOKEN);
        fExtraDVs[5] = (XSSimpleType) grammar.getGlobalTypeDecl(SchemaSymbols.ATTVAL_NCNAME);
        fExtraDVs[6] = fExtraDVs[3];
        fExtraDVs[6] = fExtraDVs[3];
        fExtraDVs[8] = (XSSimpleType) grammar.getGlobalTypeDecl("language");
        int attCount = 0 + 1;
        int attCount2 = attCount + 1;
        int attCount3 = attCount2 + 1;
        int attCount4 = attCount3 + 1;
        int attCount5 = attCount4 + 1;
        int attCount6 = attCount5 + 1;
        int attCount7 = attCount6 + 1;
        int attCount8 = attCount7 + 1;
        int attCount9 = attCount8 + 1;
        int attCount10 = attCount9 + 1;
        int attCount11 = attCount10 + 1;
        int attCount12 = attCount11 + 1;
        int attCount13 = attCount12 + 1;
        int attCount14 = attCount13 + 1;
        int attCount15 = attCount14 + 1;
        int attCount16 = attCount15 + 1;
        int attCount17 = attCount16 + 1;
        int attCount18 = attCount17 + 1;
        int attCount19 = attCount18 + 1;
        int attCount20 = attCount19 + 1;
        int attCount21 = attCount20 + 1;
        int attCount22 = attCount21 + 1;
        int attCount23 = attCount22 + 1;
        int attCount24 = attCount23 + 1;
        int attCount25 = attCount24 + 1;
        int attCount26 = attCount25 + 1;
        int attCount27 = attCount26 + 1;
        int attCount28 = attCount27 + 1;
        int attCount29 = attCount28 + 1;
        int attCount30 = attCount29 + 1;
        int attCount31 = attCount30 + 1;
        int attCount32 = attCount31 + 1;
        int attCount33 = attCount32 + 1;
        int attCount34 = attCount33 + 1;
        int attCount35 = attCount34 + 1;
        int attCount36 = attCount35 + 1;
        int attCount37 = attCount36 + 1;
        int attCount38 = attCount37 + 1;
        int attCount39 = attCount38 + 1;
        int attCount40 = attCount39 + 1;
        int attCount41 = attCount40 + 1;
        int attCount42 = attCount41 + 1;
        int attCount43 = attCount42 + 1;
        int attCount44 = attCount43 + 1;
        int attCount45 = attCount44 + 1;
        int attCount46 = attCount45 + 1;
        int attCount47 = attCount46 + 1;
        OneAttr[] allAttrs = new OneAttr[attCount47 + 1];
        allAttrs[0] = new OneAttr(SchemaSymbols.ATT_ABSTRACT, -15, ATTIDX_ABSTRACT, Boolean.FALSE);
        allAttrs[attCount] = new OneAttr(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, -6, ATTIDX_AFORMDEFAULT, INT_UNQUALIFIED);
        allAttrs[attCount2] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
        allAttrs[attCount3] = new OneAttr(SchemaSymbols.ATT_BASE, 2, ATTIDX_BASE, null);
        allAttrs[attCount4] = new OneAttr(SchemaSymbols.ATT_BLOCK, -1, ATTIDX_BLOCK, null);
        allAttrs[attCount5] = new OneAttr(SchemaSymbols.ATT_BLOCK, -2, ATTIDX_BLOCK, null);
        allAttrs[attCount6] = new OneAttr(SchemaSymbols.ATT_BLOCKDEFAULT, -1, ATTIDX_BLOCKDEFAULT, INT_EMPTY_SET);
        allAttrs[attCount7] = new OneAttr(SchemaSymbols.ATT_DEFAULT, 3, ATTIDX_DEFAULT, null);
        allAttrs[attCount8] = new OneAttr(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, -6, ATTIDX_EFORMDEFAULT, INT_UNQUALIFIED);
        allAttrs[attCount9] = new OneAttr(SchemaSymbols.ATT_FINAL, -3, ATTIDX_FINAL, null);
        allAttrs[attCount10] = new OneAttr(SchemaSymbols.ATT_FINAL, -4, ATTIDX_FINAL, null);
        allAttrs[attCount11] = new OneAttr(SchemaSymbols.ATT_FINALDEFAULT, -5, ATTIDX_FINALDEFAULT, INT_EMPTY_SET);
        allAttrs[attCount12] = new OneAttr(SchemaSymbols.ATT_FIXED, 3, ATTIDX_FIXED, null);
        allAttrs[attCount13] = new OneAttr(SchemaSymbols.ATT_FIXED, -15, ATTIDX_FIXED, Boolean.FALSE);
        allAttrs[attCount14] = new OneAttr(SchemaSymbols.ATT_FORM, -6, ATTIDX_FORM, null);
        allAttrs[attCount15] = new OneAttr(SchemaSymbols.ATT_ID, 1, ATTIDX_ID, null);
        allAttrs[attCount16] = new OneAttr(SchemaSymbols.ATT_ITEMTYPE, 2, ATTIDX_ITEMTYPE, null);
        allAttrs[attCount17] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -7, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
        allAttrs[attCount18] = new OneAttr(SchemaSymbols.ATT_MAXOCCURS, -8, ATTIDX_MAXOCCURS, fXIntPool.getXInt(1));
        allAttrs[attCount19] = new OneAttr(SchemaSymbols.ATT_MEMBERTYPES, -9, ATTIDX_MEMBERTYPES, null);
        allAttrs[attCount20] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, -16, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
        allAttrs[attCount21] = new OneAttr(SchemaSymbols.ATT_MINOCCURS, DT_MINOCCURS1, ATTIDX_MINOCCURS, fXIntPool.getXInt(1));
        allAttrs[attCount22] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, Boolean.FALSE);
        allAttrs[attCount23] = new OneAttr(SchemaSymbols.ATT_MIXED, -15, ATTIDX_MIXED, null);
        allAttrs[attCount24] = new OneAttr(SchemaSymbols.ATT_NAME, 5, ATTIDX_NAME, null);
        allAttrs[attCount25] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, DT_NAMESPACE, ATTIDX_NAMESPACE, INT_ANY_ANY);
        allAttrs[attCount26] = new OneAttr(SchemaSymbols.ATT_NAMESPACE, 0, ATTIDX_NAMESPACE, null);
        allAttrs[attCount27] = new OneAttr(SchemaSymbols.ATT_NILLABLE, -15, ATTIDX_NILLABLE, Boolean.FALSE);
        allAttrs[attCount28] = new OneAttr(SchemaSymbols.ATT_PROCESSCONTENTS, DT_PROCESSCONTENTS, ATTIDX_PROCESSCONTENTS, INT_ANY_STRICT);
        allAttrs[attCount29] = new OneAttr(SchemaSymbols.ATT_PUBLIC, 4, ATTIDX_PUBLIC, null);
        allAttrs[attCount30] = new OneAttr(SchemaSymbols.ATT_REF, 2, ATTIDX_REF, null);
        allAttrs[attCount31] = new OneAttr(SchemaSymbols.ATT_REFER, 2, ATTIDX_REFER, null);
        allAttrs[attCount32] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
        allAttrs[attCount33] = new OneAttr(SchemaSymbols.ATT_SCHEMALOCATION, 0, ATTIDX_SCHEMALOCATION, null);
        allAttrs[attCount34] = new OneAttr(SchemaSymbols.ATT_SOURCE, 0, ATTIDX_SOURCE, null);
        allAttrs[attCount35] = new OneAttr(SchemaSymbols.ATT_SUBSTITUTIONGROUP, 2, ATTIDX_SUBSGROUP, null);
        allAttrs[attCount36] = new OneAttr(SchemaSymbols.ATT_SYSTEM, 0, ATTIDX_SYSTEM, null);
        allAttrs[attCount37] = new OneAttr(SchemaSymbols.ATT_TARGETNAMESPACE, 0, ATTIDX_TARGETNAMESPACE, null);
        allAttrs[attCount38] = new OneAttr(SchemaSymbols.ATT_TYPE, 2, ATTIDX_TYPE, null);
        allAttrs[attCount39] = new OneAttr(SchemaSymbols.ATT_USE, DT_USE, ATTIDX_USE, INT_USE_OPTIONAL);
        allAttrs[attCount40] = new OneAttr(SchemaSymbols.ATT_VALUE, -16, ATTIDX_VALUE, null);
        allAttrs[attCount41] = new OneAttr(SchemaSymbols.ATT_VALUE, DT_POSINT, ATTIDX_VALUE, null);
        allAttrs[attCount42] = new OneAttr(SchemaSymbols.ATT_VALUE, 3, ATTIDX_VALUE, null);
        allAttrs[attCount43] = new OneAttr(SchemaSymbols.ATT_VALUE, DT_WHITESPACE, ATTIDX_VALUE, null);
        allAttrs[attCount44] = new OneAttr(SchemaSymbols.ATT_VERSION, 4, ATTIDX_VERSION, null);
        allAttrs[attCount45] = new OneAttr(SchemaSymbols.ATT_XML_LANG, 8, ATTIDX_XML_LANG, null);
        allAttrs[attCount46] = new OneAttr(SchemaSymbols.ATT_XPATH, 6, ATTIDX_XPATH, null);
        allAttrs[attCount47] = new OneAttr(SchemaSymbols.ATT_XPATH, 7, ATTIDX_XPATH, null);
        Container attrList = Container.getContainer(5);
        attrList.put(SchemaSymbols.ATT_DEFAULT, allAttrs[attCount7]);
        attrList.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount12]);
        attrList.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        attrList.put(SchemaSymbols.ATT_TYPE, allAttrs[attCount38]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTE, attrList);
        Container attrList2 = Container.getContainer(7);
        attrList2.put(SchemaSymbols.ATT_DEFAULT, allAttrs[attCount7]);
        attrList2.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount12]);
        attrList2.put(SchemaSymbols.ATT_FORM, allAttrs[attCount14]);
        attrList2.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList2.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        attrList2.put(SchemaSymbols.ATT_TYPE, allAttrs[attCount38]);
        attrList2.put(SchemaSymbols.ATT_USE, allAttrs[attCount39]);
        fEleAttrsMapL.put(ATTRIBUTE_N, attrList2);
        Container attrList3 = Container.getContainer(5);
        attrList3.put(SchemaSymbols.ATT_DEFAULT, allAttrs[attCount7]);
        attrList3.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount12]);
        attrList3.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList3.put(SchemaSymbols.ATT_REF, allAttrs[attCount30]);
        attrList3.put(SchemaSymbols.ATT_USE, allAttrs[attCount39]);
        fEleAttrsMapL.put(ATTRIBUTE_R, attrList3);
        Container attrList4 = Container.getContainer(10);
        attrList4.put(SchemaSymbols.ATT_ABSTRACT, allAttrs[0]);
        attrList4.put(SchemaSymbols.ATT_BLOCK, allAttrs[attCount4]);
        attrList4.put(SchemaSymbols.ATT_DEFAULT, allAttrs[attCount7]);
        attrList4.put(SchemaSymbols.ATT_FINAL, allAttrs[attCount9]);
        attrList4.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount12]);
        attrList4.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList4.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        attrList4.put(SchemaSymbols.ATT_NILLABLE, allAttrs[attCount27]);
        attrList4.put(SchemaSymbols.ATT_SUBSTITUTIONGROUP, allAttrs[attCount35]);
        attrList4.put(SchemaSymbols.ATT_TYPE, allAttrs[attCount38]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ELEMENT, attrList4);
        Container attrList5 = Container.getContainer(10);
        attrList5.put(SchemaSymbols.ATT_BLOCK, allAttrs[attCount4]);
        attrList5.put(SchemaSymbols.ATT_DEFAULT, allAttrs[attCount7]);
        attrList5.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount12]);
        attrList5.put(SchemaSymbols.ATT_FORM, allAttrs[attCount14]);
        attrList5.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList5.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[attCount17]);
        attrList5.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[attCount20]);
        attrList5.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        attrList5.put(SchemaSymbols.ATT_NILLABLE, allAttrs[attCount27]);
        attrList5.put(SchemaSymbols.ATT_TYPE, allAttrs[attCount38]);
        fEleAttrsMapL.put(ELEMENT_N, attrList5);
        Container attrList6 = Container.getContainer(4);
        attrList6.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList6.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[attCount17]);
        attrList6.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[attCount20]);
        attrList6.put(SchemaSymbols.ATT_REF, allAttrs[attCount30]);
        fEleAttrsMapL.put(ELEMENT_R, attrList6);
        Container attrList7 = Container.getContainer(6);
        attrList7.put(SchemaSymbols.ATT_ABSTRACT, allAttrs[0]);
        attrList7.put(SchemaSymbols.ATT_BLOCK, allAttrs[attCount5]);
        attrList7.put(SchemaSymbols.ATT_FINAL, allAttrs[attCount9]);
        attrList7.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList7.put(SchemaSymbols.ATT_MIXED, allAttrs[attCount22]);
        attrList7.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_COMPLEXTYPE, attrList7);
        Container attrList8 = Container.getContainer(4);
        attrList8.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList8.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        attrList8.put(SchemaSymbols.ATT_PUBLIC, allAttrs[attCount29]);
        attrList8.put(SchemaSymbols.ATT_SYSTEM, allAttrs[attCount36]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_NOTATION, attrList8);
        Container attrList9 = Container.getContainer(2);
        attrList9.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList9.put(SchemaSymbols.ATT_MIXED, allAttrs[attCount22]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXTYPE, attrList9);
        Container attrList10 = Container.getContainer(1);
        attrList10.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLECONTENT, attrList10);
        Container attrList11 = Container.getContainer(2);
        attrList11.put(SchemaSymbols.ATT_BASE, allAttrs[attCount3]);
        attrList11.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_RESTRICTION, attrList11);
        Container attrList12 = Container.getContainer(2);
        attrList12.put(SchemaSymbols.ATT_BASE, allAttrs[attCount2]);
        attrList12.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_EXTENSION, attrList12);
        Container attrList13 = Container.getContainer(2);
        attrList13.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList13.put(SchemaSymbols.ATT_REF, allAttrs[attCount30]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, attrList13);
        Container attrList14 = Container.getContainer(3);
        attrList14.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList14.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[attCount25]);
        attrList14.put(SchemaSymbols.ATT_PROCESSCONTENTS, allAttrs[attCount28]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ANYATTRIBUTE, attrList14);
        Container attrList15 = Container.getContainer(2);
        attrList15.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList15.put(SchemaSymbols.ATT_MIXED, allAttrs[attCount23]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_COMPLEXCONTENT, attrList15);
        Container attrList16 = Container.getContainer(2);
        attrList16.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList16.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ATTRIBUTEGROUP, attrList16);
        Container attrList17 = Container.getContainer(2);
        attrList17.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList17.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_GROUP, attrList17);
        Container attrList18 = Container.getContainer(4);
        attrList18.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList18.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[attCount17]);
        attrList18.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[attCount20]);
        attrList18.put(SchemaSymbols.ATT_REF, allAttrs[attCount30]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_GROUP, attrList18);
        Container attrList19 = Container.getContainer(3);
        attrList19.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList19.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[attCount18]);
        attrList19.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[attCount21]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ALL, attrList19);
        Container attrList20 = Container.getContainer(3);
        attrList20.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList20.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[attCount17]);
        attrList20.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[attCount20]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_CHOICE, attrList20);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SEQUENCE, attrList20);
        Container attrList21 = Container.getContainer(5);
        attrList21.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList21.put(SchemaSymbols.ATT_MAXOCCURS, allAttrs[attCount17]);
        attrList21.put(SchemaSymbols.ATT_MINOCCURS, allAttrs[attCount20]);
        attrList21.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[attCount25]);
        attrList21.put(SchemaSymbols.ATT_PROCESSCONTENTS, allAttrs[attCount28]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ANY, attrList21);
        Container attrList22 = Container.getContainer(2);
        attrList22.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList22.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_UNIQUE, attrList22);
        fEleAttrsMapL.put(SchemaSymbols.ELT_KEY, attrList22);
        Container attrList23 = Container.getContainer(3);
        attrList23.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList23.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        attrList23.put(SchemaSymbols.ATT_REFER, allAttrs[attCount31]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_KEYREF, attrList23);
        Container attrList24 = Container.getContainer(2);
        attrList24.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList24.put(SchemaSymbols.ATT_XPATH, allAttrs[attCount46]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SELECTOR, attrList24);
        Container attrList25 = Container.getContainer(2);
        attrList25.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList25.put(SchemaSymbols.ATT_XPATH, allAttrs[attCount47]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_FIELD, attrList25);
        Container attrList26 = Container.getContainer(1);
        attrList26.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_ANNOTATION, attrList26);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ANNOTATION, attrList26);
        Container attrList27 = Container.getContainer(1);
        attrList27.put(SchemaSymbols.ATT_SOURCE, allAttrs[attCount34]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_APPINFO, attrList27);
        fEleAttrsMapL.put(SchemaSymbols.ELT_APPINFO, attrList27);
        Container attrList28 = Container.getContainer(2);
        attrList28.put(SchemaSymbols.ATT_SOURCE, allAttrs[attCount34]);
        attrList28.put(SchemaSymbols.ATT_XML_LANG, allAttrs[attCount45]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_DOCUMENTATION, attrList28);
        fEleAttrsMapL.put(SchemaSymbols.ELT_DOCUMENTATION, attrList28);
        Container attrList29 = Container.getContainer(3);
        attrList29.put(SchemaSymbols.ATT_FINAL, allAttrs[attCount10]);
        attrList29.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList29.put(SchemaSymbols.ATT_NAME, allAttrs[attCount24]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_SIMPLETYPE, attrList29);
        Container attrList30 = Container.getContainer(2);
        attrList30.put(SchemaSymbols.ATT_FINAL, allAttrs[attCount10]);
        attrList30.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_SIMPLETYPE, attrList30);
        Container attrList31 = Container.getContainer(2);
        attrList31.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList31.put(SchemaSymbols.ATT_ITEMTYPE, allAttrs[attCount16]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_LIST, attrList31);
        Container attrList32 = Container.getContainer(2);
        attrList32.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList32.put(SchemaSymbols.ATT_MEMBERTYPES, allAttrs[attCount19]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_UNION, attrList32);
        Container attrList33 = Container.getContainer(8);
        attrList33.put(SchemaSymbols.ATT_ATTRIBUTEFORMDEFAULT, allAttrs[attCount]);
        attrList33.put(SchemaSymbols.ATT_BLOCKDEFAULT, allAttrs[attCount6]);
        attrList33.put(SchemaSymbols.ATT_ELEMENTFORMDEFAULT, allAttrs[attCount8]);
        attrList33.put(SchemaSymbols.ATT_FINALDEFAULT, allAttrs[attCount11]);
        attrList33.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList33.put(SchemaSymbols.ATT_TARGETNAMESPACE, allAttrs[attCount37]);
        attrList33.put(SchemaSymbols.ATT_VERSION, allAttrs[attCount44]);
        attrList33.put(SchemaSymbols.ATT_XML_LANG, allAttrs[attCount45]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_SCHEMA, attrList33);
        Container attrList34 = Container.getContainer(2);
        attrList34.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList34.put(SchemaSymbols.ATT_SCHEMALOCATION, allAttrs[attCount32]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_INCLUDE, attrList34);
        fEleAttrsMapG.put(SchemaSymbols.ELT_REDEFINE, attrList34);
        Container attrList35 = Container.getContainer(3);
        attrList35.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList35.put(SchemaSymbols.ATT_NAMESPACE, allAttrs[attCount26]);
        attrList35.put(SchemaSymbols.ATT_SCHEMALOCATION, allAttrs[attCount33]);
        fEleAttrsMapG.put(SchemaSymbols.ELT_IMPORT, attrList35);
        Container attrList36 = Container.getContainer(3);
        attrList36.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList36.put(SchemaSymbols.ATT_VALUE, allAttrs[attCount40]);
        attrList36.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount13]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_LENGTH, attrList36);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MINLENGTH, attrList36);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MAXLENGTH, attrList36);
        fEleAttrsMapL.put(SchemaSymbols.ELT_FRACTIONDIGITS, attrList36);
        Container attrList37 = Container.getContainer(3);
        attrList37.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList37.put(SchemaSymbols.ATT_VALUE, allAttrs[attCount41]);
        attrList37.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount13]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_TOTALDIGITS, attrList37);
        Container attrList38 = Container.getContainer(2);
        attrList38.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList38.put(SchemaSymbols.ATT_VALUE, allAttrs[attCount42]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_PATTERN, attrList38);
        Container attrList39 = Container.getContainer(2);
        attrList39.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList39.put(SchemaSymbols.ATT_VALUE, allAttrs[attCount42]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_ENUMERATION, attrList39);
        Container attrList40 = Container.getContainer(3);
        attrList40.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList40.put(SchemaSymbols.ATT_VALUE, allAttrs[attCount43]);
        attrList40.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount13]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_WHITESPACE, attrList40);
        Container attrList41 = Container.getContainer(3);
        attrList41.put(SchemaSymbols.ATT_ID, allAttrs[attCount15]);
        attrList41.put(SchemaSymbols.ATT_VALUE, allAttrs[attCount42]);
        attrList41.put(SchemaSymbols.ATT_FIXED, allAttrs[attCount13]);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MAXINCLUSIVE, attrList41);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MAXEXCLUSIVE, attrList41);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MININCLUSIVE, attrList41);
        fEleAttrsMapL.put(SchemaSymbols.ELT_MINEXCLUSIVE, attrList41);
        fSeenTemp = new boolean[ATTIDX_COUNT];
        fTempArray = new Object[ATTIDX_COUNT];
    }

    public XSAttributeChecker(XSDHandler schemaHandler) {
        this.fSchemaHandler = null;
        this.fSchemaHandler = schemaHandler;
    }

    public void reset(SymbolTable symbolTable) {
        this.fSymbolTable = symbolTable;
        this.fNonSchemaAttrs.clear();
    }

    public Object[] checkAttributes(Element element, boolean isGlobal, XSDocumentInfo schemaDoc) {
        return checkAttributes(element, isGlobal, schemaDoc, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x01c1  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x01d9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object[] checkAttributes(org.w3c.dom.Element r10, boolean r11, com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo r12, boolean r13) throws java.util.MissingResourceException, com.sun.org.apache.xerces.internal.xni.XNIException {
        /*
            Method dump skipped, instructions count: 1100
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.xerces.internal.impl.xs.traversers.XSAttributeChecker.checkAttributes(org.w3c.dom.Element, boolean, com.sun.org.apache.xerces.internal.impl.xs.traversers.XSDocumentInfo, boolean):java.lang.Object[]");
    }

    private Object validate(Object[] attrValues, String attr, String ivalue, int dvIndex, XSDocumentInfo schemaDoc) throws InvalidDatatypeValueException {
        String tempNamespace;
        if (ivalue == null) {
            return null;
        }
        String value = XMLChar.trim(ivalue);
        Object retValue = null;
        switch (dvIndex) {
            case DT_POSINT /* -17 */:
                try {
                    if (value.length() > 0 && value.charAt(0) == '+') {
                        value = value.substring(1);
                    }
                    retValue = fXIntPool.getXInt(Integer.parseInt(value));
                    if (((XInt) retValue).intValue() <= 0) {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{value, SchemaSymbols.ATTVAL_POSITIVEINTEGER});
                    }
                } catch (NumberFormatException e2) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{value, SchemaSymbols.ATTVAL_POSITIVEINTEGER});
                }
                break;
            case -16:
                try {
                    if (value.length() > 0 && value.charAt(0) == '+') {
                        value = value.substring(1);
                    }
                    retValue = fXIntPool.getXInt(Integer.parseInt(value));
                    if (((XInt) retValue).intValue() < 0) {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{value, SchemaSymbols.ATTVAL_NONNEGATIVEINTEGER});
                    }
                } catch (NumberFormatException e3) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{value, SchemaSymbols.ATTVAL_NONNEGATIVEINTEGER});
                }
                break;
            case -15:
                if (value.equals("false") || value.equals("0")) {
                    retValue = Boolean.FALSE;
                    break;
                } else if (value.equals("true") || value.equals("1")) {
                    retValue = Boolean.TRUE;
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{value, "boolean"});
                }
                break;
            case DT_WHITESPACE /* -14 */:
                if (value.equals(SchemaSymbols.ATTVAL_PRESERVE)) {
                    retValue = INT_WS_PRESERVE;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_REPLACE)) {
                    retValue = INT_WS_REPLACE;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_COLLAPSE)) {
                    retValue = INT_WS_COLLAPSE;
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{value, "(preserve | replace | collapse)"});
                }
            case DT_USE /* -13 */:
                if (value.equals(SchemaSymbols.ATTVAL_OPTIONAL)) {
                    retValue = INT_USE_OPTIONAL;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_REQUIRED)) {
                    retValue = INT_USE_REQUIRED;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_PROHIBITED)) {
                    retValue = INT_USE_PROHIBITED;
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{value, "(optional | prohibited | required)"});
                }
            case DT_PROCESSCONTENTS /* -12 */:
                if (value.equals(SchemaSymbols.ATTVAL_STRICT)) {
                    retValue = INT_ANY_STRICT;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_LAX)) {
                    retValue = INT_ANY_LAX;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_SKIP)) {
                    retValue = INT_ANY_SKIP;
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{value, "(lax | skip | strict)"});
                }
            case DT_NAMESPACE /* -11 */:
                if (value.equals(SchemaSymbols.ATTVAL_TWOPOUNDANY)) {
                    retValue = INT_ANY_ANY;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_TWOPOUNDOTHER)) {
                    retValue = INT_ANY_NOT;
                    attrValues[ATTIDX_NAMESPACE_LIST] = new String[]{schemaDoc.fTargetNamespace, null};
                    break;
                } else {
                    retValue = INT_ANY_LIST;
                    this.fNamespaceList.removeAllElements();
                    StringTokenizer tokens = new StringTokenizer(value, " \n\t\r");
                    while (tokens.hasMoreTokens()) {
                        try {
                            String token = tokens.nextToken();
                            if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDLOCAL)) {
                                tempNamespace = null;
                            } else if (token.equals(SchemaSymbols.ATTVAL_TWOPOUNDTARGETNS)) {
                                tempNamespace = schemaDoc.fTargetNamespace;
                            } else {
                                fExtraDVs[0].validate(token, (ValidationContext) schemaDoc.fValidationContext, (ValidatedInfo) null);
                                tempNamespace = this.fSymbolTable.addSymbol(token);
                            }
                            if (!this.fNamespaceList.contains(tempNamespace)) {
                                this.fNamespaceList.addElement(tempNamespace);
                            }
                        } catch (InvalidDatatypeValueException e4) {
                            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{value, "((##any | ##other) | List of (anyURI | (##targetNamespace | ##local)) )"});
                        }
                    }
                    int num = this.fNamespaceList.size();
                    String[] list = new String[num];
                    this.fNamespaceList.copyInto(list);
                    attrValues[ATTIDX_NAMESPACE_LIST] = list;
                    break;
                }
            case DT_MINOCCURS1 /* -10 */:
                if (value.equals("0")) {
                    retValue = fXIntPool.getXInt(0);
                    break;
                } else if (value.equals("1")) {
                    retValue = fXIntPool.getXInt(1);
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{value, "(0 | 1)"});
                }
            case -9:
                Vector memberType = new Vector();
                try {
                    StringTokenizer t2 = new StringTokenizer(value, " \n\t\r");
                    while (t2.hasMoreTokens()) {
                        QName qname = (QName) fExtraDVs[2].validate(t2.nextToken(), (ValidationContext) schemaDoc.fValidationContext, (ValidatedInfo) null);
                        if (qname.prefix == XMLSymbols.EMPTY_STRING && qname.uri == null && schemaDoc.fIsChameleonSchema) {
                            qname.uri = schemaDoc.fTargetNamespace;
                        }
                        memberType.addElement(qname);
                    }
                    retValue = memberType;
                    break;
                } catch (InvalidDatatypeValueException e5) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.2", new Object[]{value, "(List of QName)"});
                }
                break;
            case -8:
                if (value.equals("1")) {
                    retValue = fXIntPool.getXInt(1);
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{value, "(1)"});
                }
            case -7:
                if (value.equals(SchemaSymbols.ATTVAL_UNBOUNDED)) {
                    retValue = INT_UNBOUNDED;
                    break;
                } else {
                    try {
                        retValue = validate(attrValues, attr, value, -16, schemaDoc);
                        break;
                    } catch (NumberFormatException e6) {
                        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{value, "(nonNegativeInteger | unbounded)"});
                    }
                }
            case -6:
                if (value.equals(SchemaSymbols.ATTVAL_QUALIFIED)) {
                    retValue = INT_QUALIFIED;
                    break;
                } else if (value.equals(SchemaSymbols.ATTVAL_UNQUALIFIED)) {
                    retValue = INT_UNQUALIFIED;
                    break;
                } else {
                    throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{value, "(qualified | unqualified)"});
                }
            case -5:
                int choice = 0;
                if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                    choice = 31;
                } else {
                    StringTokenizer t3 = new StringTokenizer(value, " \n\t\r");
                    while (t3.hasMoreTokens()) {
                        String token2 = t3.nextToken();
                        if (token2.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                            choice |= 1;
                        } else if (token2.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                            choice |= 2;
                        } else if (token2.equals(SchemaSymbols.ATTVAL_LIST)) {
                            choice |= 16;
                        } else if (token2.equals("union")) {
                            choice |= 8;
                        } else {
                            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{value, "(#all | List of (extension | restriction | list | union))"});
                        }
                    }
                }
                retValue = fXIntPool.getXInt(choice);
                break;
            case -4:
                int choice2 = 0;
                if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                    choice2 = 31;
                } else {
                    StringTokenizer t4 = new StringTokenizer(value, " \n\t\r");
                    while (t4.hasMoreTokens()) {
                        String token3 = t4.nextToken();
                        if (token3.equals(SchemaSymbols.ATTVAL_LIST)) {
                            choice2 |= 16;
                        } else if (token3.equals("union")) {
                            choice2 |= 8;
                        } else if (token3.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                            choice2 |= 2;
                        } else {
                            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{value, "(#all | List of (list | union | restriction))"});
                        }
                    }
                }
                retValue = fXIntPool.getXInt(choice2);
                break;
            case -3:
            case -2:
                int choice3 = 0;
                if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                    choice3 = 31;
                } else {
                    StringTokenizer t5 = new StringTokenizer(value, " \n\t\r");
                    while (t5.hasMoreTokens()) {
                        String token4 = t5.nextToken();
                        if (token4.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                            choice3 |= 1;
                        } else if (token4.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                            choice3 |= 2;
                        } else {
                            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{value, "(#all | List of (extension | restriction))"});
                        }
                    }
                }
                retValue = fXIntPool.getXInt(choice3);
                break;
            case -1:
                int choice4 = 0;
                if (value.equals(SchemaSymbols.ATTVAL_POUNDALL)) {
                    choice4 = 7;
                } else {
                    StringTokenizer t6 = new StringTokenizer(value, " \n\t\r");
                    while (t6.hasMoreTokens()) {
                        String token5 = t6.nextToken();
                        if (token5.equals(SchemaSymbols.ATTVAL_EXTENSION)) {
                            choice4 |= 1;
                        } else if (token5.equals(SchemaSymbols.ATTVAL_RESTRICTION)) {
                            choice4 |= 2;
                        } else if (token5.equals(SchemaSymbols.ATTVAL_SUBSTITUTION)) {
                            choice4 |= 4;
                        } else {
                            throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{value, "(#all | List of (extension | restriction | substitution))"});
                        }
                    }
                }
                retValue = fXIntPool.getXInt(choice4);
                break;
        }
        return retValue;
    }

    void reportSchemaFatalError(String key, Object[] args, Element ele) throws MissingResourceException, XNIException {
        this.fSchemaHandler.reportSchemaFatalError(key, args, ele);
    }

    void reportSchemaError(String key, Object[] args, Element ele) throws MissingResourceException, XNIException {
        this.fSchemaHandler.reportSchemaError(key, args, ele);
    }

    public void checkNonSchemaAttributes(XSGrammarBucket grammarBucket) throws MissingResourceException, XNIException {
        XSAttributeDecl attrDecl;
        XSSimpleType dv;
        for (Map.Entry entry : this.fNonSchemaAttrs.entrySet()) {
            String attrRName = (String) entry.getKey();
            String attrURI = attrRName.substring(0, attrRName.indexOf(44));
            String attrLocal = attrRName.substring(attrRName.indexOf(44) + 1);
            SchemaGrammar sGrammar = grammarBucket.getGrammar(attrURI);
            if (sGrammar != null && (attrDecl = sGrammar.getGlobalAttributeDecl(attrLocal)) != null && (dv = (XSSimpleType) attrDecl.getTypeDefinition()) != null) {
                Vector values = (Vector) entry.getValue();
                String attrName = (String) values.elementAt(0);
                int count = values.size();
                for (int i2 = 1; i2 < count; i2 += 2) {
                    String elName = (String) values.elementAt(i2);
                    try {
                        dv.validate((String) values.elementAt(i2 + 1), (ValidationContext) null, (ValidatedInfo) null);
                    } catch (InvalidDatatypeValueException ide) {
                        reportSchemaError("s4s-att-invalid-value", new Object[]{elName, attrName, ide.getMessage()}, null);
                    }
                }
            }
        }
    }

    public static String normalize(String content, short ws) {
        char ch;
        int len = content == null ? 0 : content.length();
        if (len == 0 || ws == 0) {
            return content;
        }
        StringBuffer sb = new StringBuffer();
        if (ws == 1) {
            for (int i2 = 0; i2 < len; i2++) {
                char ch2 = content.charAt(i2);
                if (ch2 != '\t' && ch2 != '\n' && ch2 != '\r') {
                    sb.append(ch2);
                } else {
                    sb.append(' ');
                }
            }
        } else {
            boolean isLeading = true;
            int i3 = 0;
            while (i3 < len) {
                char ch3 = content.charAt(i3);
                if (ch3 != '\t' && ch3 != '\n' && ch3 != '\r' && ch3 != ' ') {
                    sb.append(ch3);
                    isLeading = false;
                } else {
                    while (i3 < len - 1 && ((ch = content.charAt(i3 + 1)) == '\t' || ch == '\n' || ch == '\r' || ch == ' ')) {
                        i3++;
                    }
                    if (i3 < len - 1 && !isLeading) {
                        sb.append(' ');
                    }
                }
                i3++;
            }
        }
        return sb.toString();
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [java.lang.Object[], java.lang.Object[][]] */
    protected Object[] getAvailableArray() {
        if (this.fArrayPool.length == this.fPoolPos) {
            this.fArrayPool = new Object[this.fPoolPos + 10];
            for (int i2 = this.fPoolPos; i2 < this.fArrayPool.length; i2++) {
                this.fArrayPool[i2] = new Object[ATTIDX_COUNT];
            }
        }
        Object[] retArray = this.fArrayPool[this.fPoolPos];
        Object[][] objArr = this.fArrayPool;
        int i3 = this.fPoolPos;
        this.fPoolPos = i3 + 1;
        objArr[i3] = null;
        System.arraycopy(fTempArray, 0, retArray, 0, ATTIDX_COUNT - 1);
        retArray[ATTIDX_ISRETURNED] = Boolean.FALSE;
        return retArray;
    }

    public void returnAttrArray(Object[] attrArray, XSDocumentInfo schemaDoc) {
        if (schemaDoc != null) {
            schemaDoc.fNamespaceSupport.popContext();
        }
        if (this.fPoolPos == 0 || attrArray == null || attrArray.length != ATTIDX_COUNT || ((Boolean) attrArray[ATTIDX_ISRETURNED]).booleanValue()) {
            return;
        }
        attrArray[ATTIDX_ISRETURNED] = Boolean.TRUE;
        if (attrArray[ATTIDX_NONSCHEMA] != null) {
            ((Vector) attrArray[ATTIDX_NONSCHEMA]).clear();
        }
        Object[][] objArr = this.fArrayPool;
        int i2 = this.fPoolPos - 1;
        this.fPoolPos = i2;
        objArr[i2] = attrArray;
    }

    public void resolveNamespace(Element element, Attr[] attrs, SchemaNamespaceSupport nsSupport) {
        nsSupport.pushContext();
        for (Attr sattr : attrs) {
            String rawname = DOMUtil.getName(sattr);
            String prefix = null;
            if (rawname.equals(XMLSymbols.PREFIX_XMLNS)) {
                prefix = XMLSymbols.EMPTY_STRING;
            } else if (rawname.startsWith("xmlns:")) {
                prefix = this.fSymbolTable.addSymbol(DOMUtil.getLocalName(sattr));
            }
            if (prefix != null) {
                String uri = this.fSymbolTable.addSymbol(DOMUtil.getValue(sattr));
                nsSupport.declarePrefix(prefix, uri.length() != 0 ? uri : null);
            }
        }
    }
}
