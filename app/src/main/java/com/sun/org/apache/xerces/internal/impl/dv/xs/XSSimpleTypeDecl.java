package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.DatatypeException;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeFacetException;
import com.sun.org.apache.xerces.internal.impl.dv.InvalidDatatypeValueException;
import com.sun.org.apache.xerces.internal.impl.dv.ValidatedInfo;
import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.dv.xs.ListDV;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.impl.xs.util.ShortListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.StringListImpl;
import com.sun.org.apache.xerces.internal.impl.xs.util.XSObjectListImpl;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xs.ShortList;
import com.sun.org.apache.xerces.internal.xs.StringList;
import com.sun.org.apache.xerces.internal.xs.XSAnnotation;
import com.sun.org.apache.xerces.internal.xs.XSFacet;
import com.sun.org.apache.xerces.internal.xs.XSMultiValueFacet;
import com.sun.org.apache.xerces.internal.xs.XSNamespaceItem;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;
import com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList;
import java.util.AbstractList;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;
import org.w3c.dom.TypeInfo;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl.class */
public class XSSimpleTypeDecl implements XSSimpleType, TypeInfo {
    protected static final short DV_STRING = 1;
    protected static final short DV_BOOLEAN = 2;
    protected static final short DV_DECIMAL = 3;
    protected static final short DV_FLOAT = 4;
    protected static final short DV_DOUBLE = 5;
    protected static final short DV_DURATION = 6;
    protected static final short DV_DATETIME = 7;
    protected static final short DV_TIME = 8;
    protected static final short DV_DATE = 9;
    protected static final short DV_GYEARMONTH = 10;
    protected static final short DV_GYEAR = 11;
    protected static final short DV_GMONTHDAY = 12;
    protected static final short DV_GDAY = 13;
    protected static final short DV_GMONTH = 14;
    protected static final short DV_HEXBINARY = 15;
    protected static final short DV_BASE64BINARY = 16;
    protected static final short DV_ANYURI = 17;
    protected static final short DV_QNAME = 18;
    protected static final short DV_PRECISIONDECIMAL = 19;
    protected static final short DV_NOTATION = 20;
    protected static final short DV_ANYSIMPLETYPE = 0;
    protected static final short DV_ID = 21;
    protected static final short DV_IDREF = 22;
    protected static final short DV_ENTITY = 23;
    protected static final short DV_INTEGER = 24;
    protected static final short DV_LIST = 25;
    protected static final short DV_UNION = 26;
    protected static final short DV_YEARMONTHDURATION = 27;
    protected static final short DV_DAYTIMEDURATION = 28;
    protected static final short DV_ANYATOMICTYPE = 29;
    static final short NORMALIZE_NONE = 0;
    static final short NORMALIZE_TRIM = 1;
    static final short NORMALIZE_FULL = 2;
    static final short SPECIAL_PATTERN_NONE = 0;
    static final short SPECIAL_PATTERN_NMTOKEN = 1;
    static final short SPECIAL_PATTERN_NAME = 2;
    static final short SPECIAL_PATTERN_NCNAME = 3;
    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
    static final String ANY_TYPE = "anyType";
    public static final short YEARMONTHDURATION_DT = 46;
    public static final short DAYTIMEDURATION_DT = 47;
    public static final short PRECISIONDECIMAL_DT = 48;
    public static final short ANYATOMICTYPE_DT = 49;
    static final int DERIVATION_ANY = 0;
    static final int DERIVATION_RESTRICTION = 1;
    static final int DERIVATION_EXTENSION = 2;
    static final int DERIVATION_UNION = 4;
    static final int DERIVATION_LIST = 8;
    private TypeValidator[] fDVs;
    private boolean fIsImmutable;
    private XSSimpleTypeDecl fItemType;
    private XSSimpleTypeDecl[] fMemberTypes;
    private short fBuiltInKind;
    private String fTypeName;
    private String fTargetNamespace;
    private short fFinalSet;
    private XSSimpleTypeDecl fBase;
    private short fVariety;
    private short fValidationDV;
    private short fFacetsDefined;
    private short fFixedFacet;
    private short fWhiteSpace;
    private int fLength;
    private int fMinLength;
    private int fMaxLength;
    private int fTotalDigits;
    private int fFractionDigits;
    private Vector fPattern;
    private Vector fPatternStr;
    private Vector fEnumeration;
    private short[] fEnumerationType;
    private ShortList[] fEnumerationItemType;
    private ShortList fEnumerationTypeList;
    private ObjectList fEnumerationItemTypeList;
    private StringList fLexicalPattern;
    private StringList fLexicalEnumeration;
    private ObjectList fActualEnumeration;
    private Object fMaxInclusive;
    private Object fMaxExclusive;
    private Object fMinExclusive;
    private Object fMinInclusive;
    public XSAnnotation lengthAnnotation;
    public XSAnnotation minLengthAnnotation;
    public XSAnnotation maxLengthAnnotation;
    public XSAnnotation whiteSpaceAnnotation;
    public XSAnnotation totalDigitsAnnotation;
    public XSAnnotation fractionDigitsAnnotation;
    public XSObjectListImpl patternAnnotations;
    public XSObjectList enumerationAnnotations;
    public XSAnnotation maxInclusiveAnnotation;
    public XSAnnotation maxExclusiveAnnotation;
    public XSAnnotation minInclusiveAnnotation;
    public XSAnnotation minExclusiveAnnotation;
    private XSObjectListImpl fFacets;
    private XSObjectListImpl fMultiValueFacets;
    private XSObjectList fAnnotations;
    private short fPatternType;
    private short fOrdered;
    private boolean fFinite;
    private boolean fBounded;
    private boolean fNumeric;
    private XSNamespaceItem fNamespaceItem;
    private boolean fAnonymous;
    private static final TypeValidator[] gDVs = {new AnySimpleDV(), new StringDV(), new BooleanDV(), new DecimalDV(), new FloatDV(), new DoubleDV(), new DurationDV(), new DateTimeDV(), new TimeDV(), new DateDV(), new YearMonthDV(), new YearDV(), new MonthDayDV(), new DayDV(), new MonthDV(), new HexBinaryDV(), new Base64BinaryDV(), new AnyURIDV(), new QNameDV(), new PrecisionDecimalDV(), new QNameDV(), new IDDV(), new IDREFDV(), new EntityDV(), new IntegerDV(), new ListDV(), new UnionDV(), new YearMonthDurationDV(), new DayTimeDurationDV(), new AnyAtomicDV()};
    static final short[] fDVNormalizeType = {0, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 0, 1, 1, 0};
    static final String[] SPECIAL_PATTERN_STRING = {"NONE", SchemaSymbols.ATTVAL_NMTOKEN, "Name", SchemaSymbols.ATTVAL_NCNAME};
    static final String[] WS_FACET_STRING = {SchemaSymbols.ATTVAL_PRESERVE, SchemaSymbols.ATTVAL_REPLACE, SchemaSymbols.ATTVAL_COLLAPSE};
    static final ValidationContext fEmptyContext = new ValidationContext() { // from class: com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl.1
        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needFacetChecking() {
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needExtraChecking() {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needToNormalize() {
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean useNamespaces() {
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isEntityDeclared(String name) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isEntityUnparsed(String name) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isIdDeclared(String name) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public void addId(String name) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public void addIdRef(String name) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public String getSymbol(String symbol) {
            return symbol.intern();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public String getURI(String prefix) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public Locale getLocale() {
            return Locale.getDefault();
        }
    };
    static final XSSimpleTypeDecl fAnySimpleType = new XSSimpleTypeDecl(null, SchemaSymbols.ATTVAL_ANYSIMPLETYPE, 0, 0, false, true, false, true, 1);
    static final XSSimpleTypeDecl fAnyAtomicType = new XSSimpleTypeDecl(fAnySimpleType, "anyAtomicType", 29, 0, false, true, false, true, 49);
    static final ValidationContext fDummyContext = new ValidationContext() { // from class: com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl.4
        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needFacetChecking() {
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needExtraChecking() {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needToNormalize() {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean useNamespaces() {
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isEntityDeclared(String name) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isEntityUnparsed(String name) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isIdDeclared(String name) {
            return false;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public void addId(String name) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public void addIdRef(String name) {
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public String getSymbol(String symbol) {
            return symbol.intern();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public String getURI(String prefix) {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public Locale getLocale() {
            return Locale.getDefault();
        }
    };

    protected static TypeValidator[] getGDVs() {
        return (TypeValidator[]) gDVs.clone();
    }

    protected void setDVs(TypeValidator[] dvs) {
        this.fDVs = dvs;
    }

    public XSSimpleTypeDecl() {
        this.fDVs = gDVs;
        this.fIsImmutable = false;
        this.fFinalSet = (short) 0;
        this.fVariety = (short) -1;
        this.fValidationDV = (short) -1;
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        this.fWhiteSpace = (short) 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fAnnotations = null;
        this.fPatternType = (short) 0;
        this.fNamespaceItem = null;
        this.fAnonymous = false;
    }

    protected XSSimpleTypeDecl(XSSimpleTypeDecl base, String name, short validateDV, short ordered, boolean bounded, boolean finite, boolean numeric, boolean isImmutable, short builtInKind) {
        this.fDVs = gDVs;
        this.fIsImmutable = false;
        this.fFinalSet = (short) 0;
        this.fVariety = (short) -1;
        this.fValidationDV = (short) -1;
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        this.fWhiteSpace = (short) 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fAnnotations = null;
        this.fPatternType = (short) 0;
        this.fNamespaceItem = null;
        this.fAnonymous = false;
        this.fIsImmutable = isImmutable;
        this.fBase = base;
        this.fTypeName = name;
        this.fTargetNamespace = "http://www.w3.org/2001/XMLSchema";
        this.fVariety = (short) 1;
        this.fValidationDV = validateDV;
        this.fFacetsDefined = (short) 16;
        if (validateDV == 0 || validateDV == 29 || validateDV == 1) {
            this.fWhiteSpace = (short) 0;
        } else {
            this.fWhiteSpace = (short) 2;
            this.fFixedFacet = (short) 16;
        }
        this.fOrdered = ordered;
        this.fBounded = bounded;
        this.fFinite = finite;
        this.fNumeric = numeric;
        this.fAnnotations = null;
        this.fBuiltInKind = builtInKind;
    }

    protected XSSimpleTypeDecl(XSSimpleTypeDecl base, String name, String uri, short finalSet, boolean isImmutable, XSObjectList annotations, short builtInKind) {
        this(base, name, uri, finalSet, isImmutable, annotations);
        this.fBuiltInKind = builtInKind;
    }

    protected XSSimpleTypeDecl(XSSimpleTypeDecl base, String name, String uri, short finalSet, boolean isImmutable, XSObjectList annotations) {
        this.fDVs = gDVs;
        this.fIsImmutable = false;
        this.fFinalSet = (short) 0;
        this.fVariety = (short) -1;
        this.fValidationDV = (short) -1;
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        this.fWhiteSpace = (short) 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fAnnotations = null;
        this.fPatternType = (short) 0;
        this.fNamespaceItem = null;
        this.fAnonymous = false;
        this.fBase = base;
        this.fTypeName = name;
        this.fTargetNamespace = uri;
        this.fFinalSet = finalSet;
        this.fAnnotations = annotations;
        this.fVariety = this.fBase.fVariety;
        this.fValidationDV = this.fBase.fValidationDV;
        switch (this.fVariety) {
            case 2:
                this.fItemType = this.fBase.fItemType;
                break;
            case 3:
                this.fMemberTypes = this.fBase.fMemberTypes;
                break;
        }
        this.fLength = this.fBase.fLength;
        this.fMinLength = this.fBase.fMinLength;
        this.fMaxLength = this.fBase.fMaxLength;
        this.fPattern = this.fBase.fPattern;
        this.fPatternStr = this.fBase.fPatternStr;
        this.fEnumeration = this.fBase.fEnumeration;
        this.fEnumerationType = this.fBase.fEnumerationType;
        this.fEnumerationItemType = this.fBase.fEnumerationItemType;
        this.fWhiteSpace = this.fBase.fWhiteSpace;
        this.fMaxExclusive = this.fBase.fMaxExclusive;
        this.fMaxInclusive = this.fBase.fMaxInclusive;
        this.fMinExclusive = this.fBase.fMinExclusive;
        this.fMinInclusive = this.fBase.fMinInclusive;
        this.fTotalDigits = this.fBase.fTotalDigits;
        this.fFractionDigits = this.fBase.fFractionDigits;
        this.fPatternType = this.fBase.fPatternType;
        this.fFixedFacet = this.fBase.fFixedFacet;
        this.fFacetsDefined = this.fBase.fFacetsDefined;
        this.lengthAnnotation = this.fBase.lengthAnnotation;
        this.minLengthAnnotation = this.fBase.minLengthAnnotation;
        this.maxLengthAnnotation = this.fBase.maxLengthAnnotation;
        this.patternAnnotations = this.fBase.patternAnnotations;
        this.enumerationAnnotations = this.fBase.enumerationAnnotations;
        this.whiteSpaceAnnotation = this.fBase.whiteSpaceAnnotation;
        this.maxExclusiveAnnotation = this.fBase.maxExclusiveAnnotation;
        this.maxInclusiveAnnotation = this.fBase.maxInclusiveAnnotation;
        this.minExclusiveAnnotation = this.fBase.minExclusiveAnnotation;
        this.minInclusiveAnnotation = this.fBase.minInclusiveAnnotation;
        this.totalDigitsAnnotation = this.fBase.totalDigitsAnnotation;
        this.fractionDigitsAnnotation = this.fBase.fractionDigitsAnnotation;
        calcFundamentalFacets();
        this.fIsImmutable = isImmutable;
        this.fBuiltInKind = base.fBuiltInKind;
    }

    protected XSSimpleTypeDecl(String name, String uri, short finalSet, XSSimpleTypeDecl itemType, boolean isImmutable, XSObjectList annotations) {
        this.fDVs = gDVs;
        this.fIsImmutable = false;
        this.fFinalSet = (short) 0;
        this.fVariety = (short) -1;
        this.fValidationDV = (short) -1;
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        this.fWhiteSpace = (short) 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fAnnotations = null;
        this.fPatternType = (short) 0;
        this.fNamespaceItem = null;
        this.fAnonymous = false;
        this.fBase = fAnySimpleType;
        this.fTypeName = name;
        this.fTargetNamespace = uri;
        this.fFinalSet = finalSet;
        this.fAnnotations = annotations;
        this.fVariety = (short) 2;
        this.fItemType = itemType;
        this.fValidationDV = (short) 25;
        this.fFacetsDefined = (short) 16;
        this.fFixedFacet = (short) 16;
        this.fWhiteSpace = (short) 2;
        calcFundamentalFacets();
        this.fIsImmutable = isImmutable;
        this.fBuiltInKind = (short) 44;
    }

    protected XSSimpleTypeDecl(String name, String uri, short finalSet, XSSimpleTypeDecl[] memberTypes, XSObjectList annotations) {
        this.fDVs = gDVs;
        this.fIsImmutable = false;
        this.fFinalSet = (short) 0;
        this.fVariety = (short) -1;
        this.fValidationDV = (short) -1;
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        this.fWhiteSpace = (short) 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fAnnotations = null;
        this.fPatternType = (short) 0;
        this.fNamespaceItem = null;
        this.fAnonymous = false;
        this.fBase = fAnySimpleType;
        this.fTypeName = name;
        this.fTargetNamespace = uri;
        this.fFinalSet = finalSet;
        this.fAnnotations = annotations;
        this.fVariety = (short) 3;
        this.fMemberTypes = memberTypes;
        this.fValidationDV = (short) 26;
        this.fFacetsDefined = (short) 16;
        this.fWhiteSpace = (short) 2;
        calcFundamentalFacets();
        this.fIsImmutable = false;
        this.fBuiltInKind = (short) 45;
    }

    protected XSSimpleTypeDecl setRestrictionValues(XSSimpleTypeDecl base, String name, String uri, short finalSet, XSObjectList annotations) {
        if (this.fIsImmutable) {
            return null;
        }
        this.fBase = base;
        this.fAnonymous = false;
        this.fTypeName = name;
        this.fTargetNamespace = uri;
        this.fFinalSet = finalSet;
        this.fAnnotations = annotations;
        this.fVariety = this.fBase.fVariety;
        this.fValidationDV = this.fBase.fValidationDV;
        switch (this.fVariety) {
            case 2:
                this.fItemType = this.fBase.fItemType;
                break;
            case 3:
                this.fMemberTypes = this.fBase.fMemberTypes;
                break;
        }
        this.fLength = this.fBase.fLength;
        this.fMinLength = this.fBase.fMinLength;
        this.fMaxLength = this.fBase.fMaxLength;
        this.fPattern = this.fBase.fPattern;
        this.fPatternStr = this.fBase.fPatternStr;
        this.fEnumeration = this.fBase.fEnumeration;
        this.fEnumerationType = this.fBase.fEnumerationType;
        this.fEnumerationItemType = this.fBase.fEnumerationItemType;
        this.fWhiteSpace = this.fBase.fWhiteSpace;
        this.fMaxExclusive = this.fBase.fMaxExclusive;
        this.fMaxInclusive = this.fBase.fMaxInclusive;
        this.fMinExclusive = this.fBase.fMinExclusive;
        this.fMinInclusive = this.fBase.fMinInclusive;
        this.fTotalDigits = this.fBase.fTotalDigits;
        this.fFractionDigits = this.fBase.fFractionDigits;
        this.fPatternType = this.fBase.fPatternType;
        this.fFixedFacet = this.fBase.fFixedFacet;
        this.fFacetsDefined = this.fBase.fFacetsDefined;
        calcFundamentalFacets();
        this.fBuiltInKind = base.fBuiltInKind;
        return this;
    }

    protected XSSimpleTypeDecl setListValues(String name, String uri, short finalSet, XSSimpleTypeDecl itemType, XSObjectList annotations) {
        if (this.fIsImmutable) {
            return null;
        }
        this.fBase = fAnySimpleType;
        this.fAnonymous = false;
        this.fTypeName = name;
        this.fTargetNamespace = uri;
        this.fFinalSet = finalSet;
        this.fAnnotations = annotations;
        this.fVariety = (short) 2;
        this.fItemType = itemType;
        this.fValidationDV = (short) 25;
        this.fFacetsDefined = (short) 16;
        this.fFixedFacet = (short) 16;
        this.fWhiteSpace = (short) 2;
        calcFundamentalFacets();
        this.fBuiltInKind = (short) 44;
        return this;
    }

    protected XSSimpleTypeDecl setUnionValues(String name, String uri, short finalSet, XSSimpleTypeDecl[] memberTypes, XSObjectList annotations) {
        if (this.fIsImmutable) {
            return null;
        }
        this.fBase = fAnySimpleType;
        this.fAnonymous = false;
        this.fTypeName = name;
        this.fTargetNamespace = uri;
        this.fFinalSet = finalSet;
        this.fAnnotations = annotations;
        this.fVariety = (short) 3;
        this.fMemberTypes = memberTypes;
        this.fValidationDV = (short) 26;
        this.fFacetsDefined = (short) 16;
        this.fWhiteSpace = (short) 2;
        calcFundamentalFacets();
        this.fBuiltInKind = (short) 45;
        return this;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public short getType() {
        return (short) 3;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public short getTypeCategory() {
        return (short) 16;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getName() {
        if (getAnonymous()) {
            return null;
        }
        return this.fTypeName;
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeName() {
        return this.fTypeName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public String getNamespace() {
        return this.fTargetNamespace;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public short getFinal() {
        return this.fFinalSet;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean isFinal(short derivation) {
        return (this.fFinalSet & derivation) != 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public XSTypeDefinition getBaseType() {
        return this.fBase;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean getAnonymous() {
        return this.fAnonymous || this.fTypeName == null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getVariety() {
        if (this.fValidationDV == 0) {
            return (short) 0;
        }
        return this.fVariety;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public boolean isIDType() {
        switch (this.fVariety) {
            case 1:
                return this.fValidationDV == 21;
            case 2:
                return this.fItemType.isIDType();
            case 3:
                for (int i2 = 0; i2 < this.fMemberTypes.length; i2++) {
                    if (this.fMemberTypes[i2].isIDType()) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public short getWhitespace() throws DatatypeException {
        if (this.fVariety == 3) {
            throw new DatatypeException("dt-whitespace", new Object[]{this.fTypeName});
        }
        return this.fWhiteSpace;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public short getPrimitiveKind() {
        if (this.fVariety == 1 && this.fValidationDV != 0) {
            if (this.fValidationDV == 21 || this.fValidationDV == 22 || this.fValidationDV == 23) {
                return (short) 1;
            }
            if (this.fValidationDV == 24) {
                return (short) 3;
            }
            return this.fValidationDV;
        }
        return (short) 0;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getBuiltInKind() {
        return this.fBuiltInKind;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSSimpleTypeDefinition getPrimitiveType() {
        if (this.fVariety == 1 && this.fValidationDV != 0) {
            XSSimpleTypeDecl xSSimpleTypeDecl = this;
            while (true) {
                XSSimpleTypeDecl pri = xSSimpleTypeDecl;
                if (pri.fBase != fAnySimpleType) {
                    xSSimpleTypeDecl = pri.fBase;
                } else {
                    return pri;
                }
            }
        } else {
            return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSSimpleTypeDefinition getItemType() {
        if (this.fVariety == 2) {
            return this.fItemType;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getMemberTypes() {
        if (this.fVariety == 3) {
            return new XSObjectListImpl(this.fMemberTypes, this.fMemberTypes.length);
        }
        return XSObjectListImpl.EMPTY_LIST;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public void applyFacets(XSFacets facets, short presentFacet, short fixedFacet, ValidationContext context) throws InvalidDatatypeFacetException {
        if (context == null) {
            context = fEmptyContext;
        }
        applyFacets(facets, presentFacet, fixedFacet, (short) 0, context);
    }

    void applyFacets1(XSFacets facets, short presentFacet, short fixedFacet) {
        try {
            applyFacets(facets, presentFacet, fixedFacet, (short) 0, fDummyContext);
            this.fIsImmutable = true;
        } catch (InvalidDatatypeFacetException e2) {
            throw new RuntimeException("internal error");
        }
    }

    void applyFacets1(XSFacets facets, short presentFacet, short fixedFacet, short patternType) {
        try {
            applyFacets(facets, presentFacet, fixedFacet, patternType, fDummyContext);
            this.fIsImmutable = true;
        } catch (InvalidDatatypeFacetException e2) {
            throw new RuntimeException("internal error");
        }
    }

    void applyFacets(XSFacets facets, short presentFacet, short fixedFacet, short patternType, ValidationContext context) throws InvalidDatatypeFacetException {
        int result;
        int result2;
        if (this.fIsImmutable) {
            return;
        }
        ValidatedInfo tempInfo = new ValidatedInfo();
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        short allowedFacet = this.fDVs[this.fValidationDV].getAllowedFacets();
        if ((presentFacet & 1) != 0) {
            if ((allowedFacet & 1) == 0) {
                reportError("cos-applicable-facets", new Object[]{"length", this.fTypeName});
            } else {
                this.fLength = facets.length;
                this.lengthAnnotation = facets.lengthAnnotation;
                this.fFacetsDefined = (short) (this.fFacetsDefined | 1);
                if ((fixedFacet & 1) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 1);
                }
            }
        }
        if ((presentFacet & 2) != 0) {
            if ((allowedFacet & 2) == 0) {
                reportError("cos-applicable-facets", new Object[]{"minLength", this.fTypeName});
            } else {
                this.fMinLength = facets.minLength;
                this.minLengthAnnotation = facets.minLengthAnnotation;
                this.fFacetsDefined = (short) (this.fFacetsDefined | 2);
                if ((fixedFacet & 2) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 2);
                }
            }
        }
        if ((presentFacet & 4) != 0) {
            if ((allowedFacet & 4) == 0) {
                reportError("cos-applicable-facets", new Object[]{"maxLength", this.fTypeName});
            } else {
                this.fMaxLength = facets.maxLength;
                this.maxLengthAnnotation = facets.maxLengthAnnotation;
                this.fFacetsDefined = (short) (this.fFacetsDefined | 4);
                if ((fixedFacet & 4) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 4);
                }
            }
        }
        if ((presentFacet & 8) != 0) {
            if ((allowedFacet & 8) == 0) {
                reportError("cos-applicable-facets", new Object[]{"pattern", this.fTypeName});
            } else {
                this.patternAnnotations = facets.patternAnnotations;
                RegularExpression regex = null;
                try {
                    regex = new RegularExpression(facets.pattern, "X", context.getLocale());
                } catch (Exception e2) {
                    reportError("InvalidRegex", new Object[]{facets.pattern, e2.getLocalizedMessage()});
                }
                if (regex != null) {
                    this.fPattern = new Vector();
                    this.fPattern.addElement(regex);
                    this.fPatternStr = new Vector();
                    this.fPatternStr.addElement(facets.pattern);
                    this.fFacetsDefined = (short) (this.fFacetsDefined | 8);
                    if ((fixedFacet & 8) != 0) {
                        this.fFixedFacet = (short) (this.fFixedFacet | 8);
                    }
                }
            }
        }
        if ((presentFacet & 16) != 0) {
            if ((allowedFacet & 16) == 0) {
                reportError("cos-applicable-facets", new Object[]{"whiteSpace", this.fTypeName});
            } else {
                this.fWhiteSpace = facets.whiteSpace;
                this.whiteSpaceAnnotation = facets.whiteSpaceAnnotation;
                this.fFacetsDefined = (short) (this.fFacetsDefined | 16);
                if ((fixedFacet & 16) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 16);
                }
            }
        }
        if ((presentFacet & 2048) != 0) {
            if ((allowedFacet & 2048) == 0) {
                reportError("cos-applicable-facets", new Object[]{"enumeration", this.fTypeName});
            } else {
                this.fEnumeration = new Vector();
                Vector enumVals = facets.enumeration;
                this.fEnumerationType = new short[enumVals.size()];
                this.fEnumerationItemType = new ShortList[enumVals.size()];
                Vector enumNSDecls = facets.enumNSDecls;
                ValidationContextImpl ctx = new ValidationContextImpl(context);
                this.enumerationAnnotations = facets.enumAnnotations;
                for (int i2 = 0; i2 < enumVals.size(); i2++) {
                    if (enumNSDecls != null) {
                        ctx.setNSContext((NamespaceContext) enumNSDecls.elementAt(i2));
                    }
                    try {
                        ValidatedInfo info = getActualEnumValue((String) enumVals.elementAt(i2), ctx, tempInfo);
                        this.fEnumeration.addElement(info.actualValue);
                        this.fEnumerationType[i2] = info.actualValueType;
                        this.fEnumerationItemType[i2] = info.itemValueTypes;
                    } catch (InvalidDatatypeValueException e3) {
                        reportError("enumeration-valid-restriction", new Object[]{enumVals.elementAt(i2), getBaseType().getName()});
                    }
                }
                this.fFacetsDefined = (short) (this.fFacetsDefined | 2048);
                if ((fixedFacet & 2048) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 2048);
                }
            }
        }
        if ((presentFacet & 32) != 0) {
            if ((allowedFacet & 32) == 0) {
                reportError("cos-applicable-facets", new Object[]{"maxInclusive", this.fTypeName});
            } else {
                this.maxInclusiveAnnotation = facets.maxInclusiveAnnotation;
                try {
                    this.fMaxInclusive = this.fBase.getActualValue(facets.maxInclusive, context, tempInfo, true);
                    this.fFacetsDefined = (short) (this.fFacetsDefined | 32);
                    if ((fixedFacet & 32) != 0) {
                        this.fFixedFacet = (short) (this.fFixedFacet | 32);
                    }
                } catch (InvalidDatatypeValueException ide) {
                    reportError(ide.getKey(), ide.getArgs());
                    reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.maxInclusive, "maxInclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 32) != 0 && (this.fBase.fFixedFacet & 32) != 0 && this.fDVs[this.fValidationDV].compare(this.fMaxInclusive, this.fBase.fMaxInclusive) != 0) {
                    reportError("FixedFacetValue", new Object[]{"maxInclusive", this.fMaxInclusive, this.fBase.fMaxInclusive, this.fTypeName});
                }
                try {
                    this.fBase.validate(context, tempInfo);
                } catch (InvalidDatatypeValueException ide2) {
                    reportError(ide2.getKey(), ide2.getArgs());
                    reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.maxInclusive, "maxInclusive", this.fBase.getName()});
                }
            }
        }
        boolean needCheckBase = true;
        if ((presentFacet & 64) != 0) {
            if ((allowedFacet & 64) == 0) {
                reportError("cos-applicable-facets", new Object[]{"maxExclusive", this.fTypeName});
            } else {
                this.maxExclusiveAnnotation = facets.maxExclusiveAnnotation;
                try {
                    this.fMaxExclusive = this.fBase.getActualValue(facets.maxExclusive, context, tempInfo, true);
                    this.fFacetsDefined = (short) (this.fFacetsDefined | 64);
                    if ((fixedFacet & 64) != 0) {
                        this.fFixedFacet = (short) (this.fFixedFacet | 64);
                    }
                } catch (InvalidDatatypeValueException ide3) {
                    reportError(ide3.getKey(), ide3.getArgs());
                    reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.maxExclusive, "maxExclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 64) != 0) {
                    int result3 = this.fDVs[this.fValidationDV].compare(this.fMaxExclusive, this.fBase.fMaxExclusive);
                    if ((this.fBase.fFixedFacet & 64) != 0 && result3 != 0) {
                        reportError("FixedFacetValue", new Object[]{"maxExclusive", facets.maxExclusive, this.fBase.fMaxExclusive, this.fTypeName});
                    }
                    if (result3 == 0) {
                        needCheckBase = false;
                    }
                }
                if (needCheckBase) {
                    try {
                        this.fBase.validate(context, tempInfo);
                    } catch (InvalidDatatypeValueException ide4) {
                        reportError(ide4.getKey(), ide4.getArgs());
                        reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.maxExclusive, "maxExclusive", this.fBase.getName()});
                    }
                } else if ((this.fBase.fFacetsDefined & 32) != 0 && this.fDVs[this.fValidationDV].compare(this.fMaxExclusive, this.fBase.fMaxInclusive) > 0) {
                    reportError("maxExclusive-valid-restriction.2", new Object[]{facets.maxExclusive, this.fBase.fMaxInclusive});
                }
            }
        }
        boolean needCheckBase2 = true;
        if ((presentFacet & 128) != 0) {
            if ((allowedFacet & 128) == 0) {
                reportError("cos-applicable-facets", new Object[]{"minExclusive", this.fTypeName});
            } else {
                this.minExclusiveAnnotation = facets.minExclusiveAnnotation;
                try {
                    this.fMinExclusive = this.fBase.getActualValue(facets.minExclusive, context, tempInfo, true);
                    this.fFacetsDefined = (short) (this.fFacetsDefined | 128);
                    if ((fixedFacet & 128) != 0) {
                        this.fFixedFacet = (short) (this.fFixedFacet | 128);
                    }
                } catch (InvalidDatatypeValueException ide5) {
                    reportError(ide5.getKey(), ide5.getArgs());
                    reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.minExclusive, "minExclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 128) != 0) {
                    int result4 = this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fBase.fMinExclusive);
                    if ((this.fBase.fFixedFacet & 128) != 0 && result4 != 0) {
                        reportError("FixedFacetValue", new Object[]{"minExclusive", facets.minExclusive, this.fBase.fMinExclusive, this.fTypeName});
                    }
                    if (result4 == 0) {
                        needCheckBase2 = false;
                    }
                }
                if (needCheckBase2) {
                    try {
                        this.fBase.validate(context, tempInfo);
                    } catch (InvalidDatatypeValueException ide6) {
                        reportError(ide6.getKey(), ide6.getArgs());
                        reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.minExclusive, "minExclusive", this.fBase.getName()});
                    }
                } else if ((this.fBase.fFacetsDefined & 256) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fBase.fMinInclusive) < 0) {
                    reportError("minExclusive-valid-restriction.3", new Object[]{facets.minExclusive, this.fBase.fMinInclusive});
                }
            }
        }
        if ((presentFacet & 256) != 0) {
            if ((allowedFacet & 256) == 0) {
                reportError("cos-applicable-facets", new Object[]{"minInclusive", this.fTypeName});
            } else {
                this.minInclusiveAnnotation = facets.minInclusiveAnnotation;
                try {
                    this.fMinInclusive = this.fBase.getActualValue(facets.minInclusive, context, tempInfo, true);
                    this.fFacetsDefined = (short) (this.fFacetsDefined | 256);
                    if ((fixedFacet & 256) != 0) {
                        this.fFixedFacet = (short) (this.fFixedFacet | 256);
                    }
                } catch (InvalidDatatypeValueException ide7) {
                    reportError(ide7.getKey(), ide7.getArgs());
                    reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.minInclusive, "minInclusive", this.fBase.getName()});
                }
                if ((this.fBase.fFacetsDefined & 256) != 0 && (this.fBase.fFixedFacet & 256) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fBase.fMinInclusive) != 0) {
                    reportError("FixedFacetValue", new Object[]{"minInclusive", facets.minInclusive, this.fBase.fMinInclusive, this.fTypeName});
                }
                try {
                    this.fBase.validate(context, tempInfo);
                } catch (InvalidDatatypeValueException ide8) {
                    reportError(ide8.getKey(), ide8.getArgs());
                    reportError("FacetValueFromBase", new Object[]{this.fTypeName, facets.minInclusive, "minInclusive", this.fBase.getName()});
                }
            }
        }
        if ((presentFacet & 512) != 0) {
            if ((allowedFacet & 512) == 0) {
                reportError("cos-applicable-facets", new Object[]{"totalDigits", this.fTypeName});
            } else {
                this.totalDigitsAnnotation = facets.totalDigitsAnnotation;
                this.fTotalDigits = facets.totalDigits;
                this.fFacetsDefined = (short) (this.fFacetsDefined | 512);
                if ((fixedFacet & 512) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 512);
                }
            }
        }
        if ((presentFacet & 1024) != 0) {
            if ((allowedFacet & 1024) == 0) {
                reportError("cos-applicable-facets", new Object[]{"fractionDigits", this.fTypeName});
            } else {
                this.fFractionDigits = facets.fractionDigits;
                this.fractionDigitsAnnotation = facets.fractionDigitsAnnotation;
                this.fFacetsDefined = (short) (this.fFacetsDefined | 1024);
                if ((fixedFacet & 1024) != 0) {
                    this.fFixedFacet = (short) (this.fFixedFacet | 1024);
                }
            }
        }
        if (patternType != 0) {
            this.fPatternType = patternType;
        }
        if (this.fFacetsDefined != 0) {
            if ((this.fFacetsDefined & 2) != 0 && (this.fFacetsDefined & 4) != 0 && this.fMinLength > this.fMaxLength) {
                reportError("minLength-less-than-equal-to-maxLength", new Object[]{Integer.toString(this.fMinLength), Integer.toString(this.fMaxLength), this.fTypeName});
            }
            if ((this.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 32) != 0) {
                reportError("maxInclusive-maxExclusive", new Object[]{this.fMaxInclusive, this.fMaxExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 128) != 0 && (this.fFacetsDefined & 256) != 0) {
                reportError("minInclusive-minExclusive", new Object[]{this.fMinInclusive, this.fMinExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 32) != 0 && (this.fFacetsDefined & 256) != 0 && (result2 = this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fMaxInclusive)) != -1 && result2 != 0) {
                reportError("minInclusive-less-than-equal-to-maxInclusive", new Object[]{this.fMinInclusive, this.fMaxInclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 128) != 0 && (result = this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fMaxExclusive)) != -1 && result != 0) {
                reportError("minExclusive-less-than-equal-to-maxExclusive", new Object[]{this.fMinExclusive, this.fMaxExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 32) != 0 && (this.fFacetsDefined & 128) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinExclusive, this.fMaxInclusive) != -1) {
                reportError("minExclusive-less-than-maxInclusive", new Object[]{this.fMinExclusive, this.fMaxInclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 256) != 0 && this.fDVs[this.fValidationDV].compare(this.fMinInclusive, this.fMaxExclusive) != -1) {
                reportError("minInclusive-less-than-maxExclusive", new Object[]{this.fMinInclusive, this.fMaxExclusive, this.fTypeName});
            }
            if ((this.fFacetsDefined & 1024) != 0 && (this.fFacetsDefined & 512) != 0 && this.fFractionDigits > this.fTotalDigits) {
                reportError("fractionDigits-totalDigits", new Object[]{Integer.toString(this.fFractionDigits), Integer.toString(this.fTotalDigits), this.fTypeName});
            }
            if ((this.fFacetsDefined & 1) != 0) {
                if ((this.fBase.fFacetsDefined & 2) != 0 && this.fLength < this.fBase.fMinLength) {
                    reportError("length-minLength-maxLength.1.1", new Object[]{this.fTypeName, Integer.toString(this.fLength), Integer.toString(this.fBase.fMinLength)});
                }
                if ((this.fBase.fFacetsDefined & 4) != 0 && this.fLength > this.fBase.fMaxLength) {
                    reportError("length-minLength-maxLength.2.1", new Object[]{this.fTypeName, Integer.toString(this.fLength), Integer.toString(this.fBase.fMaxLength)});
                }
                if ((this.fBase.fFacetsDefined & 1) != 0 && this.fLength != this.fBase.fLength) {
                    reportError("length-valid-restriction", new Object[]{Integer.toString(this.fLength), Integer.toString(this.fBase.fLength), this.fTypeName});
                }
            }
            if ((this.fBase.fFacetsDefined & 1) != 0 || (this.fFacetsDefined & 1) != 0) {
                if ((this.fFacetsDefined & 2) != 0) {
                    if (this.fBase.fLength < this.fMinLength) {
                        reportError("length-minLength-maxLength.1.1", new Object[]{this.fTypeName, Integer.toString(this.fBase.fLength), Integer.toString(this.fMinLength)});
                    }
                    if ((this.fBase.fFacetsDefined & 2) == 0) {
                        reportError("length-minLength-maxLength.1.2.a", new Object[]{this.fTypeName});
                    }
                    if (this.fMinLength != this.fBase.fMinLength) {
                        reportError("length-minLength-maxLength.1.2.b", new Object[]{this.fTypeName, Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength)});
                    }
                }
                if ((this.fFacetsDefined & 4) != 0) {
                    if (this.fBase.fLength > this.fMaxLength) {
                        reportError("length-minLength-maxLength.2.1", new Object[]{this.fTypeName, Integer.toString(this.fBase.fLength), Integer.toString(this.fMaxLength)});
                    }
                    if ((this.fBase.fFacetsDefined & 4) == 0) {
                        reportError("length-minLength-maxLength.2.2.a", new Object[]{this.fTypeName});
                    }
                    if (this.fMaxLength != this.fBase.fMaxLength) {
                        reportError("length-minLength-maxLength.2.2.b", new Object[]{this.fTypeName, Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fBase.fMaxLength)});
                    }
                }
            }
            if ((this.fFacetsDefined & 2) != 0) {
                if ((this.fBase.fFacetsDefined & 4) != 0) {
                    if (this.fMinLength > this.fBase.fMaxLength) {
                        reportError("minLength-less-than-equal-to-maxLength", new Object[]{Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName});
                    }
                } else if ((this.fBase.fFacetsDefined & 2) != 0) {
                    if ((this.fBase.fFixedFacet & 2) != 0 && this.fMinLength != this.fBase.fMinLength) {
                        reportError("FixedFacetValue", new Object[]{"minLength", Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength), this.fTypeName});
                    }
                    if (this.fMinLength < this.fBase.fMinLength) {
                        reportError("minLength-valid-restriction", new Object[]{Integer.toString(this.fMinLength), Integer.toString(this.fBase.fMinLength), this.fTypeName});
                    }
                }
            }
            if ((this.fFacetsDefined & 4) != 0 && (this.fBase.fFacetsDefined & 2) != 0 && this.fMaxLength < this.fBase.fMinLength) {
                reportError("minLength-less-than-equal-to-maxLength", new Object[]{Integer.toString(this.fBase.fMinLength), Integer.toString(this.fMaxLength)});
            }
            if ((this.fFacetsDefined & 4) != 0 && (this.fBase.fFacetsDefined & 4) != 0) {
                if ((this.fBase.fFixedFacet & 4) != 0 && this.fMaxLength != this.fBase.fMaxLength) {
                    reportError("FixedFacetValue", new Object[]{"maxLength", Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName});
                }
                if (this.fMaxLength > this.fBase.fMaxLength) {
                    reportError("maxLength-valid-restriction", new Object[]{Integer.toString(this.fMaxLength), Integer.toString(this.fBase.fMaxLength), this.fTypeName});
                }
            }
            if ((this.fFacetsDefined & 512) != 0 && (this.fBase.fFacetsDefined & 512) != 0) {
                if ((this.fBase.fFixedFacet & 512) != 0 && this.fTotalDigits != this.fBase.fTotalDigits) {
                    reportError("FixedFacetValue", new Object[]{"totalDigits", Integer.toString(this.fTotalDigits), Integer.toString(this.fBase.fTotalDigits), this.fTypeName});
                }
                if (this.fTotalDigits > this.fBase.fTotalDigits) {
                    reportError("totalDigits-valid-restriction", new Object[]{Integer.toString(this.fTotalDigits), Integer.toString(this.fBase.fTotalDigits), this.fTypeName});
                }
            }
            if ((this.fFacetsDefined & 1024) != 0 && (this.fBase.fFacetsDefined & 512) != 0 && this.fFractionDigits > this.fBase.fTotalDigits) {
                reportError("fractionDigits-totalDigits", new Object[]{Integer.toString(this.fFractionDigits), Integer.toString(this.fTotalDigits), this.fTypeName});
            }
            if ((this.fFacetsDefined & 1024) != 0) {
                if ((this.fBase.fFacetsDefined & 1024) != 0) {
                    if (((this.fBase.fFixedFacet & 1024) != 0 && this.fFractionDigits != this.fBase.fFractionDigits) || (this.fValidationDV == 24 && this.fFractionDigits != 0)) {
                        reportError("FixedFacetValue", new Object[]{"fractionDigits", Integer.toString(this.fFractionDigits), Integer.toString(this.fBase.fFractionDigits), this.fTypeName});
                    }
                    if (this.fFractionDigits > this.fBase.fFractionDigits) {
                        reportError("fractionDigits-valid-restriction", new Object[]{Integer.toString(this.fFractionDigits), Integer.toString(this.fBase.fFractionDigits), this.fTypeName});
                    }
                } else if (this.fValidationDV == 24 && this.fFractionDigits != 0) {
                    reportError("FixedFacetValue", new Object[]{"fractionDigits", Integer.toString(this.fFractionDigits), "0", this.fTypeName});
                }
            }
            if ((this.fFacetsDefined & 16) != 0 && (this.fBase.fFacetsDefined & 16) != 0) {
                if ((this.fBase.fFixedFacet & 16) != 0 && this.fWhiteSpace != this.fBase.fWhiteSpace) {
                    reportError("FixedFacetValue", new Object[]{"whiteSpace", whiteSpaceValue(this.fWhiteSpace), whiteSpaceValue(this.fBase.fWhiteSpace), this.fTypeName});
                }
                if (this.fWhiteSpace == 0 && this.fBase.fWhiteSpace == 2) {
                    reportError("whiteSpace-valid-restriction.1", new Object[]{this.fTypeName, SchemaSymbols.ATTVAL_PRESERVE});
                }
                if (this.fWhiteSpace == 1 && this.fBase.fWhiteSpace == 2) {
                    reportError("whiteSpace-valid-restriction.1", new Object[]{this.fTypeName, SchemaSymbols.ATTVAL_REPLACE});
                }
                if (this.fWhiteSpace == 0 && this.fBase.fWhiteSpace == 1) {
                    reportError("whiteSpace-valid-restriction.2", new Object[]{this.fTypeName});
                }
            }
        }
        if ((this.fFacetsDefined & 1) == 0 && (this.fBase.fFacetsDefined & 1) != 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 1);
            this.fLength = this.fBase.fLength;
            this.lengthAnnotation = this.fBase.lengthAnnotation;
        }
        if ((this.fFacetsDefined & 2) == 0 && (this.fBase.fFacetsDefined & 2) != 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 2);
            this.fMinLength = this.fBase.fMinLength;
            this.minLengthAnnotation = this.fBase.minLengthAnnotation;
        }
        if ((this.fFacetsDefined & 4) == 0 && (this.fBase.fFacetsDefined & 4) != 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 4);
            this.fMaxLength = this.fBase.fMaxLength;
            this.maxLengthAnnotation = this.fBase.maxLengthAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 8) != 0) {
            if ((this.fFacetsDefined & 8) == 0) {
                this.fFacetsDefined = (short) (this.fFacetsDefined | 8);
                this.fPattern = this.fBase.fPattern;
                this.fPatternStr = this.fBase.fPatternStr;
                this.patternAnnotations = this.fBase.patternAnnotations;
            } else {
                for (int i3 = this.fBase.fPattern.size() - 1; i3 >= 0; i3--) {
                    this.fPattern.addElement(this.fBase.fPattern.elementAt(i3));
                    this.fPatternStr.addElement(this.fBase.fPatternStr.elementAt(i3));
                }
                if (this.fBase.patternAnnotations != null) {
                    if (this.patternAnnotations != null) {
                        for (int i4 = this.fBase.patternAnnotations.getLength() - 1; i4 >= 0; i4--) {
                            this.patternAnnotations.addXSObject(this.fBase.patternAnnotations.item(i4));
                        }
                    } else {
                        this.patternAnnotations = this.fBase.patternAnnotations;
                    }
                }
            }
        }
        if ((this.fFacetsDefined & 16) == 0 && (this.fBase.fFacetsDefined & 16) != 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 16);
            this.fWhiteSpace = this.fBase.fWhiteSpace;
            this.whiteSpaceAnnotation = this.fBase.whiteSpaceAnnotation;
        }
        if ((this.fFacetsDefined & 2048) == 0 && (this.fBase.fFacetsDefined & 2048) != 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 2048);
            this.fEnumeration = this.fBase.fEnumeration;
            this.enumerationAnnotations = this.fBase.enumerationAnnotations;
        }
        if ((this.fBase.fFacetsDefined & 64) != 0 && (this.fFacetsDefined & 64) == 0 && (this.fFacetsDefined & 32) == 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 64);
            this.fMaxExclusive = this.fBase.fMaxExclusive;
            this.maxExclusiveAnnotation = this.fBase.maxExclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 32) != 0 && (this.fFacetsDefined & 64) == 0 && (this.fFacetsDefined & 32) == 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 32);
            this.fMaxInclusive = this.fBase.fMaxInclusive;
            this.maxInclusiveAnnotation = this.fBase.maxInclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 128) != 0 && (this.fFacetsDefined & 128) == 0 && (this.fFacetsDefined & 256) == 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 128);
            this.fMinExclusive = this.fBase.fMinExclusive;
            this.minExclusiveAnnotation = this.fBase.minExclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 256) != 0 && (this.fFacetsDefined & 128) == 0 && (this.fFacetsDefined & 256) == 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 256);
            this.fMinInclusive = this.fBase.fMinInclusive;
            this.minInclusiveAnnotation = this.fBase.minInclusiveAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 512) != 0 && (this.fFacetsDefined & 512) == 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 512);
            this.fTotalDigits = this.fBase.fTotalDigits;
            this.totalDigitsAnnotation = this.fBase.totalDigitsAnnotation;
        }
        if ((this.fBase.fFacetsDefined & 1024) != 0 && (this.fFacetsDefined & 1024) == 0) {
            this.fFacetsDefined = (short) (this.fFacetsDefined | 1024);
            this.fFractionDigits = this.fBase.fFractionDigits;
            this.fractionDigitsAnnotation = this.fBase.fractionDigitsAnnotation;
        }
        if (this.fPatternType == 0 && this.fBase.fPatternType != 0) {
            this.fPatternType = this.fBase.fPatternType;
        }
        this.fFixedFacet = (short) (this.fFixedFacet | this.fBase.fFixedFacet);
        calcFundamentalFacets();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public Object validate(String content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (context == null) {
            context = fEmptyContext;
        }
        if (validatedInfo == null) {
            validatedInfo = new ValidatedInfo();
        } else {
            validatedInfo.memberType = null;
        }
        boolean needNormalize = context == null || context.needToNormalize();
        Object ob = getActualValue(content, context, validatedInfo, needNormalize);
        validate(context, validatedInfo);
        return ob;
    }

    protected ValidatedInfo getActualEnumValue(String lexical, ValidationContext ctx, ValidatedInfo info) throws InvalidDatatypeValueException {
        return this.fBase.validateWithInfo(lexical, ctx, info);
    }

    public ValidatedInfo validateWithInfo(String content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (context == null) {
            context = fEmptyContext;
        }
        if (validatedInfo == null) {
            validatedInfo = new ValidatedInfo();
        } else {
            validatedInfo.memberType = null;
        }
        boolean needNormalize = context == null || context.needToNormalize();
        getActualValue(content, context, validatedInfo, needNormalize);
        validate(context, validatedInfo);
        return validatedInfo;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public Object validate(Object content, ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (context == null) {
            context = fEmptyContext;
        }
        if (validatedInfo == null) {
            validatedInfo = new ValidatedInfo();
        } else {
            validatedInfo.memberType = null;
        }
        boolean needNormalize = context == null || context.needToNormalize();
        Object ob = getActualValue(content, context, validatedInfo, needNormalize);
        validate(context, validatedInfo);
        return ob;
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public void validate(ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        if (context == null) {
            context = fEmptyContext;
        }
        if (context.needFacetChecking() && this.fFacetsDefined != 0 && this.fFacetsDefined != 16) {
            checkFacets(validatedInfo);
        }
        if (context.needExtraChecking()) {
            checkExtraRules(context, validatedInfo);
        }
    }

    private void checkFacets(ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        int compare;
        int compare2;
        int totalDigits;
        int scale;
        short primitiveItem1;
        short primitiveItem2;
        Object ob = validatedInfo.actualValue;
        String content = validatedInfo.normalizedValue;
        short type = validatedInfo.actualValueType;
        ShortList itemType = validatedInfo.itemValueTypes;
        if (this.fValidationDV != 18 && this.fValidationDV != 20) {
            int length = this.fDVs[this.fValidationDV].getDataLength(ob);
            if ((this.fFacetsDefined & 4) != 0 && length > this.fMaxLength) {
                throw new InvalidDatatypeValueException("cvc-maxLength-valid", new Object[]{content, Integer.toString(length), Integer.toString(this.fMaxLength), this.fTypeName});
            }
            if ((this.fFacetsDefined & 2) != 0 && length < this.fMinLength) {
                throw new InvalidDatatypeValueException("cvc-minLength-valid", new Object[]{content, Integer.toString(length), Integer.toString(this.fMinLength), this.fTypeName});
            }
            if ((this.fFacetsDefined & 1) != 0 && length != this.fLength) {
                throw new InvalidDatatypeValueException("cvc-length-valid", new Object[]{content, Integer.toString(length), Integer.toString(this.fLength), this.fTypeName});
            }
        }
        if ((this.fFacetsDefined & 2048) != 0) {
            boolean present = false;
            int enumSize = this.fEnumeration.size();
            short primitiveType1 = convertToPrimitiveKind(type);
            int i2 = 0;
            while (true) {
                if (i2 >= enumSize) {
                    break;
                }
                short primitiveType2 = convertToPrimitiveKind(this.fEnumerationType[i2]);
                if ((primitiveType1 == primitiveType2 || ((primitiveType1 == 1 && primitiveType2 == 2) || (primitiveType1 == 2 && primitiveType2 == 1))) && this.fEnumeration.elementAt(i2).equals(ob)) {
                    if (primitiveType1 == 44 || primitiveType1 == 43) {
                        ShortList enumItemType = this.fEnumerationItemType[i2];
                        int typeList1Length = itemType != null ? itemType.getLength() : 0;
                        int typeList2Length = enumItemType != null ? enumItemType.getLength() : 0;
                        if (typeList1Length == typeList2Length) {
                            int j2 = 0;
                            while (j2 < typeList1Length && ((primitiveItem1 = convertToPrimitiveKind(itemType.item(j2))) == (primitiveItem2 = convertToPrimitiveKind(enumItemType.item(j2))) || ((primitiveItem1 == 1 && primitiveItem2 == 2) || (primitiveItem1 == 2 && primitiveItem2 == 1)))) {
                                j2++;
                            }
                            if (j2 == typeList1Length) {
                                present = true;
                                break;
                            }
                        } else {
                            continue;
                        }
                    } else {
                        present = true;
                        break;
                    }
                }
                i2++;
            }
            if (!present) {
                throw new InvalidDatatypeValueException("cvc-enumeration-valid", new Object[]{content, this.fEnumeration.toString()});
            }
        }
        if ((this.fFacetsDefined & 1024) != 0 && (scale = this.fDVs[this.fValidationDV].getFractionDigits(ob)) > this.fFractionDigits) {
            throw new InvalidDatatypeValueException("cvc-fractionDigits-valid", new Object[]{content, Integer.toString(scale), Integer.toString(this.fFractionDigits)});
        }
        if ((this.fFacetsDefined & 512) != 0 && (totalDigits = this.fDVs[this.fValidationDV].getTotalDigits(ob)) > this.fTotalDigits) {
            throw new InvalidDatatypeValueException("cvc-totalDigits-valid", new Object[]{content, Integer.toString(totalDigits), Integer.toString(this.fTotalDigits)});
        }
        if ((this.fFacetsDefined & 32) != 0 && (compare2 = this.fDVs[this.fValidationDV].compare(ob, this.fMaxInclusive)) != -1 && compare2 != 0) {
            throw new InvalidDatatypeValueException("cvc-maxInclusive-valid", new Object[]{content, this.fMaxInclusive, this.fTypeName});
        }
        if ((this.fFacetsDefined & 64) != 0 && this.fDVs[this.fValidationDV].compare(ob, this.fMaxExclusive) != -1) {
            throw new InvalidDatatypeValueException("cvc-maxExclusive-valid", new Object[]{content, this.fMaxExclusive, this.fTypeName});
        }
        if ((this.fFacetsDefined & 256) != 0 && (compare = this.fDVs[this.fValidationDV].compare(ob, this.fMinInclusive)) != 1 && compare != 0) {
            throw new InvalidDatatypeValueException("cvc-minInclusive-valid", new Object[]{content, this.fMinInclusive, this.fTypeName});
        }
        if ((this.fFacetsDefined & 128) != 0 && this.fDVs[this.fValidationDV].compare(ob, this.fMinExclusive) != 1) {
            throw new InvalidDatatypeValueException("cvc-minExclusive-valid", new Object[]{content, this.fMinExclusive, this.fTypeName});
        }
    }

    private void checkExtraRules(ValidationContext context, ValidatedInfo validatedInfo) throws InvalidDatatypeValueException {
        Object ob = validatedInfo.actualValue;
        if (this.fVariety == 1) {
            this.fDVs[this.fValidationDV].checkExtraRules(ob, context);
            return;
        }
        if (this.fVariety == 2) {
            ListDV.ListData values = (ListDV.ListData) ob;
            XSSimpleType memberType = validatedInfo.memberType;
            int len = values.getLength();
            try {
                if (this.fItemType.fVariety == 3) {
                    XSSimpleTypeDecl[] memberTypes = (XSSimpleTypeDecl[]) validatedInfo.memberTypes;
                    for (int i2 = len - 1; i2 >= 0; i2--) {
                        validatedInfo.actualValue = values.item(i2);
                        validatedInfo.memberType = memberTypes[i2];
                        this.fItemType.checkExtraRules(context, validatedInfo);
                    }
                } else {
                    for (int i3 = len - 1; i3 >= 0; i3--) {
                        validatedInfo.actualValue = values.item(i3);
                        this.fItemType.checkExtraRules(context, validatedInfo);
                    }
                }
                return;
            } finally {
                validatedInfo.actualValue = values;
                validatedInfo.memberType = memberType;
            }
        }
        ((XSSimpleTypeDecl) validatedInfo.memberType).checkExtraRules(context, validatedInfo);
    }

    private Object getActualValue(Object content, ValidationContext context, ValidatedInfo validatedInfo, boolean needNormalize) throws InvalidDatatypeValueException {
        String nvalue;
        if (needNormalize) {
            nvalue = normalize(content, this.fWhiteSpace);
        } else {
            nvalue = content.toString();
        }
        if ((this.fFacetsDefined & 8) != 0) {
            if (this.fPattern.size() == 0 && nvalue.length() > 0) {
                throw new InvalidDatatypeValueException("cvc-pattern-valid", new Object[]{content, "(empty string)", this.fTypeName});
            }
            for (int idx = this.fPattern.size() - 1; idx >= 0; idx--) {
                RegularExpression regex = (RegularExpression) this.fPattern.elementAt(idx);
                if (!regex.matches(nvalue)) {
                    throw new InvalidDatatypeValueException("cvc-pattern-valid", new Object[]{content, this.fPatternStr.elementAt(idx), this.fTypeName});
                }
            }
        }
        if (this.fVariety == 1) {
            if (this.fPatternType != 0) {
                boolean seenErr = false;
                if (this.fPatternType == 1) {
                    seenErr = !XMLChar.isValidNmtoken(nvalue);
                } else if (this.fPatternType == 2) {
                    seenErr = !XMLChar.isValidName(nvalue);
                } else if (this.fPatternType == 3) {
                    seenErr = !XMLChar.isValidNCName(nvalue);
                }
                if (seenErr) {
                    throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.1", new Object[]{nvalue, SPECIAL_PATTERN_STRING[this.fPatternType]});
                }
            }
            validatedInfo.normalizedValue = nvalue;
            Object avalue = this.fDVs[this.fValidationDV].getActualValue(nvalue, context);
            validatedInfo.actualValue = avalue;
            validatedInfo.actualValueType = this.fBuiltInKind;
            return avalue;
        }
        if (this.fVariety == 2) {
            StringTokenizer parsedList = new StringTokenizer(nvalue, " ");
            int countOfTokens = parsedList.countTokens();
            Object[] avalue2 = new Object[countOfTokens];
            boolean isUnion = this.fItemType.getVariety() == 3;
            short[] itemTypes = new short[isUnion ? countOfTokens : 1];
            if (!isUnion) {
                itemTypes[0] = this.fItemType.fBuiltInKind;
            }
            XSSimpleTypeDecl[] memberTypes = new XSSimpleTypeDecl[countOfTokens];
            for (int i2 = 0; i2 < countOfTokens; i2++) {
                avalue2[i2] = this.fItemType.getActualValue(parsedList.nextToken(), context, validatedInfo, false);
                if (context.needFacetChecking() && this.fItemType.fFacetsDefined != 0 && this.fItemType.fFacetsDefined != 16) {
                    this.fItemType.checkFacets(validatedInfo);
                }
                memberTypes[i2] = (XSSimpleTypeDecl) validatedInfo.memberType;
                if (isUnion) {
                    itemTypes[i2] = memberTypes[i2].fBuiltInKind;
                }
            }
            ListDV.ListData v2 = new ListDV.ListData(avalue2);
            validatedInfo.actualValue = v2;
            validatedInfo.actualValueType = isUnion ? (short) 43 : (short) 44;
            validatedInfo.memberType = null;
            validatedInfo.memberTypes = memberTypes;
            validatedInfo.itemValueTypes = new ShortListImpl(itemTypes, itemTypes.length);
            validatedInfo.normalizedValue = nvalue;
            return v2;
        }
        Object _content = (this.fMemberTypes.length <= 1 || content == null) ? content : content.toString();
        for (int i3 = 0; i3 < this.fMemberTypes.length; i3++) {
            try {
                Object aValue = this.fMemberTypes[i3].getActualValue(_content, context, validatedInfo, true);
                if (context.needFacetChecking() && this.fMemberTypes[i3].fFacetsDefined != 0 && this.fMemberTypes[i3].fFacetsDefined != 16) {
                    this.fMemberTypes[i3].checkFacets(validatedInfo);
                }
                validatedInfo.memberType = this.fMemberTypes[i3];
                return aValue;
            } catch (InvalidDatatypeValueException e2) {
            }
        }
        StringBuffer typesBuffer = new StringBuffer();
        for (int i4 = 0; i4 < this.fMemberTypes.length; i4++) {
            if (i4 != 0) {
                typesBuffer.append(" | ");
            }
            XSSimpleTypeDecl decl = this.fMemberTypes[i4];
            if (decl.fTargetNamespace != null) {
                typesBuffer.append('{');
                typesBuffer.append(decl.fTargetNamespace);
                typesBuffer.append('}');
            }
            typesBuffer.append(decl.fTypeName);
            if (decl.fEnumeration != null) {
                Vector v3 = decl.fEnumeration;
                typesBuffer.append(" : [");
                for (int j2 = 0; j2 < v3.size(); j2++) {
                    if (j2 != 0) {
                        typesBuffer.append(',');
                    }
                    typesBuffer.append(v3.elementAt(j2));
                }
                typesBuffer.append(']');
            }
        }
        throw new InvalidDatatypeValueException("cvc-datatype-valid.1.2.3", new Object[]{content, this.fTypeName, typesBuffer.toString()});
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType
    public boolean isEqual(Object value1, Object value2) {
        if (value1 == null) {
            return false;
        }
        return value1.equals(value2);
    }

    public boolean isIdentical(Object value1, Object value2) {
        if (value1 == null) {
            return false;
        }
        return this.fDVs[this.fValidationDV].isIdentical(value1, value2);
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

    protected String normalize(Object content, short ws) {
        char ch;
        if (content == null) {
            return null;
        }
        if ((this.fFacetsDefined & 8) == 0) {
            short norm_type = fDVNormalizeType[this.fValidationDV];
            if (norm_type == 0) {
                return content.toString();
            }
            if (norm_type == 1) {
                return XMLChar.trim(content.toString());
            }
        }
        if (!(content instanceof StringBuffer)) {
            String strContent = content.toString();
            return normalize(strContent, ws);
        }
        StringBuffer sb = (StringBuffer) content;
        int len = sb.length();
        if (len == 0) {
            return "";
        }
        if (ws == 0) {
            return sb.toString();
        }
        if (ws == 1) {
            for (int i2 = 0; i2 < len; i2++) {
                char ch2 = sb.charAt(i2);
                if (ch2 == '\t' || ch2 == '\n' || ch2 == '\r') {
                    sb.setCharAt(i2, ' ');
                }
            }
        } else {
            int j2 = 0;
            boolean isLeading = true;
            int i3 = 0;
            while (i3 < len) {
                char ch3 = sb.charAt(i3);
                if (ch3 != '\t' && ch3 != '\n' && ch3 != '\r' && ch3 != ' ') {
                    int i4 = j2;
                    j2++;
                    sb.setCharAt(i4, ch3);
                    isLeading = false;
                } else {
                    while (i3 < len - 1 && ((ch = sb.charAt(i3 + 1)) == '\t' || ch == '\n' || ch == '\r' || ch == ' ')) {
                        i3++;
                    }
                    if (i3 < len - 1 && !isLeading) {
                        int i5 = j2;
                        j2++;
                        sb.setCharAt(i5, ' ');
                    }
                }
                i3++;
            }
            sb.setLength(j2);
        }
        return sb.toString();
    }

    void reportError(String key, Object[] args) throws InvalidDatatypeFacetException {
        throw new InvalidDatatypeFacetException(key, args);
    }

    private String whiteSpaceValue(short ws) {
        return WS_FACET_STRING[ws];
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getOrdered() {
        return this.fOrdered;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean getBounded() {
        return this.fBounded;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean getFinite() {
        return this.fFinite;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean getNumeric() {
        return this.fNumeric;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean isDefinedFacet(short facetName) {
        if (this.fValidationDV == 0 || this.fValidationDV == 29) {
            return false;
        }
        if ((this.fFacetsDefined & facetName) != 0) {
            return true;
        }
        if (this.fPatternType != 0) {
            return facetName == 8;
        }
        if (this.fValidationDV == 24) {
            return facetName == 8 || facetName == 1024;
        }
        return false;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getDefinedFacets() {
        if (this.fValidationDV == 0 || this.fValidationDV == 29) {
            return (short) 0;
        }
        if (this.fPatternType != 0) {
            return (short) (this.fFacetsDefined | 8);
        }
        if (this.fValidationDV == 24) {
            return (short) (this.fFacetsDefined | 8 | 1024);
        }
        return this.fFacetsDefined;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public boolean isFixedFacet(short facetName) {
        if ((this.fFixedFacet & facetName) != 0) {
            return true;
        }
        return this.fValidationDV == 24 && facetName == 1024;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public short getFixedFacets() {
        if (this.fValidationDV == 24) {
            return (short) (this.fFixedFacet | 1024);
        }
        return this.fFixedFacet;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public String getLexicalFacetValue(short facetName) {
        switch (facetName) {
            case 1:
                if (this.fLength == -1) {
                    return null;
                }
                return Integer.toString(this.fLength);
            case 2:
                if (this.fMinLength == -1) {
                    return null;
                }
                return Integer.toString(this.fMinLength);
            case 4:
                if (this.fMaxLength == -1) {
                    return null;
                }
                return Integer.toString(this.fMaxLength);
            case 16:
                if (this.fValidationDV == 0 || this.fValidationDV == 29) {
                    return null;
                }
                return WS_FACET_STRING[this.fWhiteSpace];
            case 32:
                if (this.fMaxInclusive == null) {
                    return null;
                }
                return this.fMaxInclusive.toString();
            case 64:
                if (this.fMaxExclusive == null) {
                    return null;
                }
                return this.fMaxExclusive.toString();
            case 128:
                if (this.fMinExclusive == null) {
                    return null;
                }
                return this.fMinExclusive.toString();
            case 256:
                if (this.fMinInclusive == null) {
                    return null;
                }
                return this.fMinInclusive.toString();
            case 512:
                if (this.fTotalDigits == -1) {
                    return null;
                }
                return Integer.toString(this.fTotalDigits);
            case 1024:
                if (this.fValidationDV == 24) {
                    return "0";
                }
                if (this.fFractionDigits == -1) {
                    return null;
                }
                return Integer.toString(this.fFractionDigits);
            default:
                return null;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public StringList getLexicalEnumeration() {
        if (this.fLexicalEnumeration == null) {
            if (this.fEnumeration == null) {
                return StringListImpl.EMPTY_LIST;
            }
            int size = this.fEnumeration.size();
            String[] strs = new String[size];
            for (int i2 = 0; i2 < size; i2++) {
                strs[i2] = this.fEnumeration.elementAt(i2).toString();
            }
            this.fLexicalEnumeration = new StringListImpl(strs, size);
        }
        return this.fLexicalEnumeration;
    }

    public ObjectList getActualEnumeration() {
        if (this.fActualEnumeration == null) {
            this.fActualEnumeration = new AbstractObjectList() { // from class: com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl.2
                @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
                public int getLength() {
                    if (XSSimpleTypeDecl.this.fEnumeration != null) {
                        return XSSimpleTypeDecl.this.fEnumeration.size();
                    }
                    return 0;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object item) {
                    return XSSimpleTypeDecl.this.fEnumeration != null && XSSimpleTypeDecl.this.fEnumeration.contains(item);
                }

                @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
                public Object item(int index) {
                    if (index >= 0 && index < getLength()) {
                        return XSSimpleTypeDecl.this.fEnumeration.elementAt(index);
                    }
                    return null;
                }
            };
        }
        return this.fActualEnumeration;
    }

    public ObjectList getEnumerationItemTypeList() {
        if (this.fEnumerationItemTypeList == null) {
            if (this.fEnumerationItemType == null) {
                return null;
            }
            this.fEnumerationItemTypeList = new AbstractObjectList() { // from class: com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl.3
                @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
                public int getLength() {
                    if (XSSimpleTypeDecl.this.fEnumerationItemType != null) {
                        return XSSimpleTypeDecl.this.fEnumerationItemType.length;
                    }
                    return 0;
                }

                @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
                public boolean contains(Object item) {
                    if (XSSimpleTypeDecl.this.fEnumerationItemType == null || !(item instanceof ShortList)) {
                        return false;
                    }
                    for (int i2 = 0; i2 < XSSimpleTypeDecl.this.fEnumerationItemType.length; i2++) {
                        if (XSSimpleTypeDecl.this.fEnumerationItemType[i2] == item) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override // com.sun.org.apache.xerces.internal.xs.datatypes.ObjectList
                public Object item(int index) {
                    if (index >= 0 && index < getLength()) {
                        return XSSimpleTypeDecl.this.fEnumerationItemType[index];
                    }
                    return null;
                }
            };
        }
        return this.fEnumerationItemTypeList;
    }

    public ShortList getEnumerationTypeList() {
        if (this.fEnumerationTypeList == null) {
            if (this.fEnumerationType == null) {
                return ShortListImpl.EMPTY_LIST;
            }
            this.fEnumerationTypeList = new ShortListImpl(this.fEnumerationType, this.fEnumerationType.length);
        }
        return this.fEnumerationTypeList;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public StringList getLexicalPattern() {
        String[] strs;
        if (this.fPatternType == 0 && this.fValidationDV != 24 && this.fPatternStr == null) {
            return StringListImpl.EMPTY_LIST;
        }
        if (this.fLexicalPattern == null) {
            int size = this.fPatternStr == null ? 0 : this.fPatternStr.size();
            if (this.fPatternType == 1) {
                strs = new String[size + 1];
                strs[size] = "\\c+";
            } else if (this.fPatternType == 2) {
                strs = new String[size + 1];
                strs[size] = "\\i\\c*";
            } else if (this.fPatternType == 3) {
                strs = new String[size + 2];
                strs[size] = "\\i\\c*";
                strs[size + 1] = "[\\i-[:]][\\c-[:]]*";
            } else if (this.fValidationDV == 24) {
                strs = new String[size + 1];
                strs[size] = "[\\-+]?[0-9]+";
            } else {
                strs = new String[size];
            }
            for (int i2 = 0; i2 < size; i2++) {
                strs[i2] = (String) this.fPatternStr.elementAt(i2);
            }
            this.fLexicalPattern = new StringListImpl(strs, strs.length);
        }
        return this.fLexicalPattern;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getAnnotations() {
        return this.fAnnotations != null ? this.fAnnotations : XSObjectListImpl.EMPTY_LIST;
    }

    private void calcFundamentalFacets() {
        setOrdered();
        setNumeric();
        setBounded();
        setCardinality();
    }

    private void setOrdered() {
        if (this.fVariety == 1) {
            this.fOrdered = this.fBase.fOrdered;
            return;
        }
        if (this.fVariety == 2) {
            this.fOrdered = (short) 0;
            return;
        }
        if (this.fVariety == 3) {
            int length = this.fMemberTypes.length;
            if (length == 0) {
                this.fOrdered = (short) 1;
                return;
            }
            short ancestorId = getPrimitiveDV(this.fMemberTypes[0].fValidationDV);
            boolean commonAnc = ancestorId != 0;
            boolean allFalse = this.fMemberTypes[0].fOrdered == 0;
            for (int i2 = 1; i2 < this.fMemberTypes.length && (commonAnc || allFalse); i2++) {
                if (commonAnc) {
                    commonAnc = ancestorId == getPrimitiveDV(this.fMemberTypes[i2].fValidationDV);
                }
                if (allFalse) {
                    allFalse = this.fMemberTypes[i2].fOrdered == 0;
                }
            }
            if (commonAnc) {
                this.fOrdered = this.fMemberTypes[0].fOrdered;
            } else if (allFalse) {
                this.fOrdered = (short) 0;
            } else {
                this.fOrdered = (short) 1;
            }
        }
    }

    private void setNumeric() {
        if (this.fVariety == 1) {
            this.fNumeric = this.fBase.fNumeric;
            return;
        }
        if (this.fVariety == 2) {
            this.fNumeric = false;
            return;
        }
        if (this.fVariety == 3) {
            XSSimpleType[] memberTypes = this.fMemberTypes;
            for (XSSimpleType xSSimpleType : memberTypes) {
                if (!xSSimpleType.getNumeric()) {
                    this.fNumeric = false;
                    return;
                }
            }
            this.fNumeric = true;
        }
    }

    private void setBounded() {
        if (this.fVariety == 1) {
            if (((this.fFacetsDefined & 256) != 0 || (this.fFacetsDefined & 128) != 0) && ((this.fFacetsDefined & 32) != 0 || (this.fFacetsDefined & 64) != 0)) {
                this.fBounded = true;
                return;
            } else {
                this.fBounded = false;
                return;
            }
        }
        if (this.fVariety == 2) {
            if ((this.fFacetsDefined & 1) != 0 || ((this.fFacetsDefined & 2) != 0 && (this.fFacetsDefined & 4) != 0)) {
                this.fBounded = true;
                return;
            } else {
                this.fBounded = false;
                return;
            }
        }
        if (this.fVariety == 3) {
            XSSimpleTypeDecl[] memberTypes = this.fMemberTypes;
            short ancestorId = 0;
            if (memberTypes.length > 0) {
                ancestorId = getPrimitiveDV(memberTypes[0].fValidationDV);
            }
            for (int i2 = 0; i2 < memberTypes.length; i2++) {
                if (!memberTypes[i2].getBounded() || ancestorId != getPrimitiveDV(memberTypes[i2].fValidationDV)) {
                    this.fBounded = false;
                    return;
                }
            }
            this.fBounded = true;
        }
    }

    private boolean specialCardinalityCheck() {
        if (this.fBase.fValidationDV == 9 || this.fBase.fValidationDV == 10 || this.fBase.fValidationDV == 11 || this.fBase.fValidationDV == 12 || this.fBase.fValidationDV == 13 || this.fBase.fValidationDV == 14) {
            return true;
        }
        return false;
    }

    private void setCardinality() {
        if (this.fVariety == 1) {
            if (this.fBase.fFinite) {
                this.fFinite = true;
                return;
            }
            if ((this.fFacetsDefined & 1) != 0 || (this.fFacetsDefined & 4) != 0 || (this.fFacetsDefined & 512) != 0) {
                this.fFinite = true;
                return;
            }
            if (((this.fFacetsDefined & 256) != 0 || (this.fFacetsDefined & 128) != 0) && ((this.fFacetsDefined & 32) != 0 || (this.fFacetsDefined & 64) != 0)) {
                if ((this.fFacetsDefined & 1024) != 0 || specialCardinalityCheck()) {
                    this.fFinite = true;
                    return;
                } else {
                    this.fFinite = false;
                    return;
                }
            }
            this.fFinite = false;
            return;
        }
        if (this.fVariety == 2) {
            if ((this.fFacetsDefined & 1) != 0 || ((this.fFacetsDefined & 2) != 0 && (this.fFacetsDefined & 4) != 0)) {
                this.fFinite = true;
                return;
            } else {
                this.fFinite = false;
                return;
            }
        }
        if (this.fVariety == 3) {
            XSSimpleType[] memberTypes = this.fMemberTypes;
            for (XSSimpleType xSSimpleType : memberTypes) {
                if (!xSSimpleType.getFinite()) {
                    this.fFinite = false;
                    return;
                }
            }
            this.fFinite = true;
        }
    }

    private short getPrimitiveDV(short validationDV) {
        if (validationDV == 21 || validationDV == 22 || validationDV == 23) {
            return (short) 1;
        }
        if (validationDV == 24) {
            return (short) 3;
        }
        return validationDV;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean derivedFromType(XSTypeDefinition ancestor, short derivation) {
        XSTypeDefinition type;
        if (ancestor == null) {
            return false;
        }
        while (ancestor instanceof XSSimpleTypeDelegate) {
            ancestor = ((XSSimpleTypeDelegate) ancestor).type;
        }
        if (ancestor.getBaseType() == ancestor) {
            return true;
        }
        XSTypeDefinition baseType = this;
        while (true) {
            type = baseType;
            if (type == ancestor || type == fAnySimpleType) {
                break;
            }
            baseType = type.getBaseType();
        }
        return type == ancestor;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSTypeDefinition
    public boolean derivedFrom(String ancestorNS, String ancestorName, short derivation) {
        XSTypeDefinition type;
        if (ancestorName == null) {
            return false;
        }
        if ("http://www.w3.org/2001/XMLSchema".equals(ancestorNS) && "anyType".equals(ancestorName)) {
            return true;
        }
        XSTypeDefinition baseType = this;
        while (true) {
            type = baseType;
            if ((ancestorName.equals(type.getName()) && ((ancestorNS == null && type.getNamespace() == null) || (ancestorNS != null && ancestorNS.equals(type.getNamespace())))) || type == fAnySimpleType) {
                break;
            }
            baseType = type.getBaseType();
        }
        return type != fAnySimpleType;
    }

    public boolean isDOMDerivedFrom(String ancestorNS, String ancestorName, int derivationMethod) {
        if (ancestorName == null) {
            return false;
        }
        if (SchemaSymbols.URI_SCHEMAFORSCHEMA.equals(ancestorNS) && "anyType".equals(ancestorName) && ((derivationMethod & 1) != 0 || derivationMethod == 0)) {
            return true;
        }
        if ((derivationMethod & 1) != 0 && isDerivedByRestriction(ancestorNS, ancestorName, this)) {
            return true;
        }
        if ((derivationMethod & 8) != 0 && isDerivedByList(ancestorNS, ancestorName, this)) {
            return true;
        }
        if ((derivationMethod & 4) != 0 && isDerivedByUnion(ancestorNS, ancestorName, this)) {
            return true;
        }
        if (((derivationMethod & 2) == 0 || (derivationMethod & 1) != 0 || (derivationMethod & 8) != 0 || (derivationMethod & 4) != 0) && (derivationMethod & 2) == 0 && (derivationMethod & 1) == 0 && (derivationMethod & 8) == 0 && (derivationMethod & 4) == 0) {
            return isDerivedByAny(ancestorNS, ancestorName, this);
        }
        return false;
    }

    private boolean isDerivedByAny(String ancestorNS, String ancestorName, XSTypeDefinition type) {
        boolean derivedFrom = false;
        XSTypeDefinition oldType = null;
        while (type != null && type != oldType) {
            if (ancestorName.equals(type.getName()) && ((ancestorNS == null && type.getNamespace() == null) || (ancestorNS != null && ancestorNS.equals(type.getNamespace())))) {
                derivedFrom = true;
                break;
            }
            if (isDerivedByRestriction(ancestorNS, ancestorName, type) || isDerivedByList(ancestorNS, ancestorName, type) || isDerivedByUnion(ancestorNS, ancestorName, type)) {
                return true;
            }
            oldType = type;
            if (((XSSimpleTypeDecl) type).getVariety() == 0 || ((XSSimpleTypeDecl) type).getVariety() == 1) {
                type = type.getBaseType();
            } else if (((XSSimpleTypeDecl) type).getVariety() == 3) {
                if (0 < ((XSSimpleTypeDecl) type).getMemberTypes().getLength()) {
                    return isDerivedByAny(ancestorNS, ancestorName, (XSTypeDefinition) ((XSSimpleTypeDecl) type).getMemberTypes().item(0));
                }
            } else if (((XSSimpleTypeDecl) type).getVariety() == 2) {
                type = ((XSSimpleTypeDecl) type).getItemType();
            }
        }
        return derivedFrom;
    }

    private boolean isDerivedByRestriction(String ancestorNS, String ancestorName, XSTypeDefinition type) {
        XSTypeDefinition oldType = null;
        while (type != null && type != oldType) {
            if (ancestorName.equals(type.getName())) {
                if (ancestorNS == null || !ancestorNS.equals(type.getNamespace())) {
                    if (type.getNamespace() == null && ancestorNS == null) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
            oldType = type;
            type = type.getBaseType();
        }
        return false;
    }

    private boolean isDerivedByList(String ancestorNS, String ancestorName, XSTypeDefinition type) {
        XSTypeDefinition itemType;
        if (type != null && ((XSSimpleTypeDefinition) type).getVariety() == 2 && (itemType = ((XSSimpleTypeDefinition) type).getItemType()) != null && isDerivedByRestriction(ancestorNS, ancestorName, itemType)) {
            return true;
        }
        return false;
    }

    private boolean isDerivedByUnion(String ancestorNS, String ancestorName, XSTypeDefinition type) {
        if (type != null && ((XSSimpleTypeDefinition) type).getVariety() == 3) {
            XSObjectList memberTypes = ((XSSimpleTypeDefinition) type).getMemberTypes();
            for (int i2 = 0; i2 < memberTypes.getLength(); i2++) {
                if (memberTypes.item(i2) != null && isDerivedByRestriction(ancestorNS, ancestorName, (XSSimpleTypeDefinition) memberTypes.item(i2))) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$ValidationContextImpl.class */
    static final class ValidationContextImpl implements ValidationContext {
        final ValidationContext fExternal;
        NamespaceContext fNSContext;

        ValidationContextImpl(ValidationContext external) {
            this.fExternal = external;
        }

        void setNSContext(NamespaceContext nsContext) {
            this.fNSContext = nsContext;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needFacetChecking() {
            return this.fExternal.needFacetChecking();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needExtraChecking() {
            return this.fExternal.needExtraChecking();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean needToNormalize() {
            return this.fExternal.needToNormalize();
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean useNamespaces() {
            return true;
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isEntityDeclared(String name) {
            return this.fExternal.isEntityDeclared(name);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isEntityUnparsed(String name) {
            return this.fExternal.isEntityUnparsed(name);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public boolean isIdDeclared(String name) {
            return this.fExternal.isIdDeclared(name);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public void addId(String name) {
            this.fExternal.addId(name);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public void addIdRef(String name) {
            this.fExternal.addIdRef(name);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public String getSymbol(String symbol) {
            return this.fExternal.getSymbol(symbol);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public String getURI(String prefix) {
            if (this.fNSContext == null) {
                return this.fExternal.getURI(prefix);
            }
            return this.fNSContext.getURI(prefix);
        }

        @Override // com.sun.org.apache.xerces.internal.impl.dv.ValidationContext
        public Locale getLocale() {
            return this.fExternal.getLocale();
        }
    }

    public void reset() {
        if (this.fIsImmutable) {
            return;
        }
        this.fItemType = null;
        this.fMemberTypes = null;
        this.fTypeName = null;
        this.fTargetNamespace = null;
        this.fFinalSet = (short) 0;
        this.fBase = null;
        this.fVariety = (short) -1;
        this.fValidationDV = (short) -1;
        this.fFacetsDefined = (short) 0;
        this.fFixedFacet = (short) 0;
        this.fWhiteSpace = (short) 0;
        this.fLength = -1;
        this.fMinLength = -1;
        this.fMaxLength = -1;
        this.fTotalDigits = -1;
        this.fFractionDigits = -1;
        this.fPattern = null;
        this.fPatternStr = null;
        this.fEnumeration = null;
        this.fEnumerationType = null;
        this.fEnumerationItemType = null;
        this.fLexicalPattern = null;
        this.fLexicalEnumeration = null;
        this.fMaxInclusive = null;
        this.fMaxExclusive = null;
        this.fMinExclusive = null;
        this.fMinInclusive = null;
        this.lengthAnnotation = null;
        this.minLengthAnnotation = null;
        this.maxLengthAnnotation = null;
        this.whiteSpaceAnnotation = null;
        this.totalDigitsAnnotation = null;
        this.fractionDigitsAnnotation = null;
        this.patternAnnotations = null;
        this.enumerationAnnotations = null;
        this.maxInclusiveAnnotation = null;
        this.maxExclusiveAnnotation = null;
        this.minInclusiveAnnotation = null;
        this.minExclusiveAnnotation = null;
        this.fPatternType = (short) 0;
        this.fAnnotations = null;
        this.fFacets = null;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSObject
    public XSNamespaceItem getNamespaceItem() {
        return this.fNamespaceItem;
    }

    public void setNamespaceItem(XSNamespaceItem namespaceItem) {
        this.fNamespaceItem = namespaceItem;
    }

    public String toString() {
        return this.fTargetNamespace + "," + this.fTypeName;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getFacets() {
        if (this.fFacets == null && (this.fFacetsDefined != 0 || this.fValidationDV == 24)) {
            XSFacetImpl[] facets = new XSFacetImpl[10];
            int count = 0;
            if ((this.fFacetsDefined & 16) != 0 && this.fValidationDV != 0 && this.fValidationDV != 29) {
                facets[0] = new XSFacetImpl((short) 16, WS_FACET_STRING[this.fWhiteSpace], (this.fFixedFacet & 16) != 0, this.whiteSpaceAnnotation);
                count = 0 + 1;
            }
            if (this.fLength != -1) {
                facets[count] = new XSFacetImpl((short) 1, Integer.toString(this.fLength), (this.fFixedFacet & 1) != 0, this.lengthAnnotation);
                count++;
            }
            if (this.fMinLength != -1) {
                facets[count] = new XSFacetImpl((short) 2, Integer.toString(this.fMinLength), (this.fFixedFacet & 2) != 0, this.minLengthAnnotation);
                count++;
            }
            if (this.fMaxLength != -1) {
                facets[count] = new XSFacetImpl((short) 4, Integer.toString(this.fMaxLength), (this.fFixedFacet & 4) != 0, this.maxLengthAnnotation);
                count++;
            }
            if (this.fTotalDigits != -1) {
                facets[count] = new XSFacetImpl((short) 512, Integer.toString(this.fTotalDigits), (this.fFixedFacet & 512) != 0, this.totalDigitsAnnotation);
                count++;
            }
            if (this.fValidationDV == 24) {
                facets[count] = new XSFacetImpl((short) 1024, "0", true, this.fractionDigitsAnnotation);
                count++;
            } else if (this.fFractionDigits != -1) {
                facets[count] = new XSFacetImpl((short) 1024, Integer.toString(this.fFractionDigits), (this.fFixedFacet & 1024) != 0, this.fractionDigitsAnnotation);
                count++;
            }
            if (this.fMaxInclusive != null) {
                facets[count] = new XSFacetImpl((short) 32, this.fMaxInclusive.toString(), (this.fFixedFacet & 32) != 0, this.maxInclusiveAnnotation);
                count++;
            }
            if (this.fMaxExclusive != null) {
                facets[count] = new XSFacetImpl((short) 64, this.fMaxExclusive.toString(), (this.fFixedFacet & 64) != 0, this.maxExclusiveAnnotation);
                count++;
            }
            if (this.fMinExclusive != null) {
                facets[count] = new XSFacetImpl((short) 128, this.fMinExclusive.toString(), (this.fFixedFacet & 128) != 0, this.minExclusiveAnnotation);
                count++;
            }
            if (this.fMinInclusive != null) {
                facets[count] = new XSFacetImpl((short) 256, this.fMinInclusive.toString(), (this.fFixedFacet & 256) != 0, this.minInclusiveAnnotation);
                count++;
            }
            this.fFacets = count > 0 ? new XSObjectListImpl(facets, count) : XSObjectListImpl.EMPTY_LIST;
        }
        return this.fFacets != null ? this.fFacets : XSObjectListImpl.EMPTY_LIST;
    }

    @Override // com.sun.org.apache.xerces.internal.xs.XSSimpleTypeDefinition
    public XSObjectList getMultiValueFacets() {
        if (this.fMultiValueFacets == null && ((this.fFacetsDefined & 2048) != 0 || (this.fFacetsDefined & 8) != 0 || this.fPatternType != 0 || this.fValidationDV == 24)) {
            XSMVFacetImpl[] facets = new XSMVFacetImpl[2];
            int count = 0;
            if ((this.fFacetsDefined & 8) != 0 || this.fPatternType != 0 || this.fValidationDV == 24) {
                facets[0] = new XSMVFacetImpl((short) 8, getLexicalPattern(), this.patternAnnotations);
                count = 0 + 1;
            }
            if (this.fEnumeration != null) {
                facets[count] = new XSMVFacetImpl((short) 2048, getLexicalEnumeration(), this.enumerationAnnotations);
                count++;
            }
            this.fMultiValueFacets = new XSObjectListImpl(facets, count);
        }
        return this.fMultiValueFacets != null ? this.fMultiValueFacets : XSObjectListImpl.EMPTY_LIST;
    }

    public Object getMinInclusiveValue() {
        return this.fMinInclusive;
    }

    public Object getMinExclusiveValue() {
        return this.fMinExclusive;
    }

    public Object getMaxInclusiveValue() {
        return this.fMaxInclusive;
    }

    public Object getMaxExclusiveValue() {
        return this.fMaxExclusive;
    }

    public void setAnonymous(boolean anon) {
        this.fAnonymous = anon;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$XSFacetImpl.class */
    private static final class XSFacetImpl implements XSFacet {
        final short kind;
        final String value;
        final boolean fixed;
        final XSObjectList annotations;

        public XSFacetImpl(short kind, String value, boolean fixed, XSAnnotation annotation) {
            this.kind = kind;
            this.value = value;
            this.fixed = fixed;
            if (annotation != null) {
                this.annotations = new XSObjectListImpl();
                ((XSObjectListImpl) this.annotations).addXSObject(annotation);
            } else {
                this.annotations = XSObjectListImpl.EMPTY_LIST;
            }
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSFacet
        public XSAnnotation getAnnotation() {
            return (XSAnnotation) this.annotations.item(0);
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSFacet
        public XSObjectList getAnnotations() {
            return this.annotations;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSFacet
        public short getFacetKind() {
            return this.kind;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSFacet
        public String getLexicalFacetValue() {
            return this.value;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSFacet
        public boolean getFixed() {
            return this.fixed;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public String getName() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public String getNamespace() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public XSNamespaceItem getNamespaceItem() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public short getType() {
            return (short) 13;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$XSMVFacetImpl.class */
    private static final class XSMVFacetImpl implements XSMultiValueFacet {
        final short kind;
        final XSObjectList annotations;
        final StringList values;

        public XSMVFacetImpl(short kind, StringList values, XSObjectList annotations) {
            this.kind = kind;
            this.values = values;
            this.annotations = annotations != null ? annotations : XSObjectListImpl.EMPTY_LIST;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSMultiValueFacet
        public short getFacetKind() {
            return this.kind;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSMultiValueFacet
        public XSObjectList getAnnotations() {
            return this.annotations;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSMultiValueFacet
        public StringList getLexicalFacetValues() {
            return this.values;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public String getName() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public String getNamespace() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public XSNamespaceItem getNamespaceItem() {
            return null;
        }

        @Override // com.sun.org.apache.xerces.internal.xs.XSObject
        public short getType() {
            return (short) 14;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/XSSimpleTypeDecl$AbstractObjectList.class */
    private static abstract class AbstractObjectList extends AbstractList implements ObjectList {
        private AbstractObjectList() {
        }

        @Override // java.util.AbstractList, java.util.List
        public Object get(int index) {
            if (index >= 0 && index < getLength()) {
                return item(index);
            }
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return getLength();
        }
    }

    @Override // org.w3c.dom.TypeInfo
    public String getTypeNamespace() {
        return getNamespace();
    }

    @Override // org.w3c.dom.TypeInfo
    public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
        return isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
    }

    private short convertToPrimitiveKind(short valueType) {
        if (valueType <= 20) {
            return valueType;
        }
        if (valueType <= 29) {
            return (short) 2;
        }
        if (valueType <= 42) {
            return (short) 4;
        }
        return valueType;
    }
}
