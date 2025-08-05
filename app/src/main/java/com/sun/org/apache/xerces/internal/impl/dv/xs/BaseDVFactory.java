package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory;
import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/BaseDVFactory.class */
public class BaseDVFactory extends SchemaDVFactory {
    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
    static SymbolHash fBaseTypes = new SymbolHash(53);

    static {
        createBuiltInTypes(fBaseTypes);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public XSSimpleType getBuiltInType(String name) {
        return (XSSimpleType) fBaseTypes.get(name);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public SymbolHash getBuiltInTypes() {
        return fBaseTypes.makeClone();
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public XSSimpleType createTypeRestriction(String name, String targetNamespace, short finalSet, XSSimpleType base, XSObjectList annotations) {
        return new XSSimpleTypeDecl((XSSimpleTypeDecl) base, name, targetNamespace, finalSet, false, annotations);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public XSSimpleType createTypeList(String name, String targetNamespace, short finalSet, XSSimpleType itemType, XSObjectList annotations) {
        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, (XSSimpleTypeDecl) itemType, false, annotations);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public XSSimpleType createTypeUnion(String name, String targetNamespace, short finalSet, XSSimpleType[] memberTypes, XSObjectList annotations) {
        int typeNum = memberTypes.length;
        XSSimpleTypeDecl[] mtypes = new XSSimpleTypeDecl[typeNum];
        System.arraycopy(memberTypes, 0, mtypes, 0, typeNum);
        return new XSSimpleTypeDecl(name, targetNamespace, finalSet, mtypes, annotations);
    }

    static void createBuiltInTypes(SymbolHash types) {
        XSFacets facets = new XSFacets();
        XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
        types.put(SchemaSymbols.ATTVAL_ANYSIMPLETYPE, anySimpleType);
        XSSimpleTypeDecl stringDV = new XSSimpleTypeDecl(anySimpleType, "string", (short) 1, (short) 0, false, false, false, true, (short) 2);
        types.put("string", stringDV);
        types.put("boolean", new XSSimpleTypeDecl(anySimpleType, "boolean", (short) 2, (short) 0, false, true, false, true, (short) 3));
        XSSimpleTypeDecl decimalDV = new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_DECIMAL, (short) 3, (short) 2, false, false, true, true, (short) 4);
        types.put(SchemaSymbols.ATTVAL_DECIMAL, decimalDV);
        types.put(SchemaSymbols.ATTVAL_ANYURI, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_ANYURI, (short) 17, (short) 0, false, false, false, true, (short) 18));
        types.put(SchemaSymbols.ATTVAL_BASE64BINARY, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_BASE64BINARY, (short) 16, (short) 0, false, false, false, true, (short) 17));
        types.put(SchemaSymbols.ATTVAL_DATETIME, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_DATETIME, (short) 7, (short) 1, false, false, false, true, (short) 8));
        types.put(SchemaSymbols.ATTVAL_TIME, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_TIME, (short) 8, (short) 1, false, false, false, true, (short) 9));
        types.put("date", new XSSimpleTypeDecl(anySimpleType, "date", (short) 9, (short) 1, false, false, false, true, (short) 10));
        types.put(SchemaSymbols.ATTVAL_YEARMONTH, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_YEARMONTH, (short) 10, (short) 1, false, false, false, true, (short) 11));
        types.put(SchemaSymbols.ATTVAL_YEAR, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_YEAR, (short) 11, (short) 1, false, false, false, true, (short) 12));
        types.put(SchemaSymbols.ATTVAL_MONTHDAY, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_MONTHDAY, (short) 12, (short) 1, false, false, false, true, (short) 13));
        types.put(SchemaSymbols.ATTVAL_DAY, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_DAY, (short) 13, (short) 1, false, false, false, true, (short) 14));
        types.put(SchemaSymbols.ATTVAL_MONTH, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_MONTH, (short) 14, (short) 1, false, false, false, true, (short) 15));
        XSSimpleTypeDecl integerDV = new XSSimpleTypeDecl(decimalDV, SchemaSymbols.ATTVAL_INTEGER, (short) 24, (short) 2, false, false, true, true, (short) 30);
        types.put(SchemaSymbols.ATTVAL_INTEGER, integerDV);
        facets.maxInclusive = "0";
        XSSimpleTypeDecl nonPositiveDV = new XSSimpleTypeDecl(integerDV, SchemaSymbols.ATTVAL_NONPOSITIVEINTEGER, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 31);
        nonPositiveDV.applyFacets1(facets, (short) 32, (short) 0);
        types.put(SchemaSymbols.ATTVAL_NONPOSITIVEINTEGER, nonPositiveDV);
        facets.maxInclusive = "-1";
        XSSimpleTypeDecl negativeDV = new XSSimpleTypeDecl(nonPositiveDV, SchemaSymbols.ATTVAL_NEGATIVEINTEGER, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 32);
        negativeDV.applyFacets1(facets, (short) 32, (short) 0);
        types.put(SchemaSymbols.ATTVAL_NEGATIVEINTEGER, negativeDV);
        facets.maxInclusive = "9223372036854775807";
        facets.minInclusive = "-9223372036854775808";
        XSSimpleTypeDecl longDV = new XSSimpleTypeDecl(integerDV, SchemaSymbols.ATTVAL_LONG, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 33);
        longDV.applyFacets1(facets, (short) 288, (short) 0);
        types.put(SchemaSymbols.ATTVAL_LONG, longDV);
        facets.maxInclusive = "2147483647";
        facets.minInclusive = "-2147483648";
        XSSimpleTypeDecl intDV = new XSSimpleTypeDecl(longDV, "int", "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 34);
        intDV.applyFacets1(facets, (short) 288, (short) 0);
        types.put("int", intDV);
        facets.maxInclusive = "32767";
        facets.minInclusive = "-32768";
        XSSimpleTypeDecl shortDV = new XSSimpleTypeDecl(intDV, SchemaSymbols.ATTVAL_SHORT, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 35);
        shortDV.applyFacets1(facets, (short) 288, (short) 0);
        types.put(SchemaSymbols.ATTVAL_SHORT, shortDV);
        facets.maxInclusive = "127";
        facets.minInclusive = "-128";
        XSSimpleTypeDecl byteDV = new XSSimpleTypeDecl(shortDV, SchemaSymbols.ATTVAL_BYTE, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 36);
        byteDV.applyFacets1(facets, (short) 288, (short) 0);
        types.put(SchemaSymbols.ATTVAL_BYTE, byteDV);
        facets.minInclusive = "0";
        XSSimpleTypeDecl nonNegativeDV = new XSSimpleTypeDecl(integerDV, SchemaSymbols.ATTVAL_NONNEGATIVEINTEGER, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 37);
        nonNegativeDV.applyFacets1(facets, (short) 256, (short) 0);
        types.put(SchemaSymbols.ATTVAL_NONNEGATIVEINTEGER, nonNegativeDV);
        facets.maxInclusive = "18446744073709551615";
        XSSimpleTypeDecl unsignedLongDV = new XSSimpleTypeDecl(nonNegativeDV, SchemaSymbols.ATTVAL_UNSIGNEDLONG, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 38);
        unsignedLongDV.applyFacets1(facets, (short) 32, (short) 0);
        types.put(SchemaSymbols.ATTVAL_UNSIGNEDLONG, unsignedLongDV);
        facets.maxInclusive = "4294967295";
        XSSimpleTypeDecl unsignedIntDV = new XSSimpleTypeDecl(unsignedLongDV, SchemaSymbols.ATTVAL_UNSIGNEDINT, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 39);
        unsignedIntDV.applyFacets1(facets, (short) 32, (short) 0);
        types.put(SchemaSymbols.ATTVAL_UNSIGNEDINT, unsignedIntDV);
        facets.maxInclusive = "65535";
        XSSimpleTypeDecl unsignedShortDV = new XSSimpleTypeDecl(unsignedIntDV, SchemaSymbols.ATTVAL_UNSIGNEDSHORT, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 40);
        unsignedShortDV.applyFacets1(facets, (short) 32, (short) 0);
        types.put(SchemaSymbols.ATTVAL_UNSIGNEDSHORT, unsignedShortDV);
        facets.maxInclusive = "255";
        XSSimpleTypeDecl unsignedByteDV = new XSSimpleTypeDecl(unsignedShortDV, SchemaSymbols.ATTVAL_UNSIGNEDBYTE, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 41);
        unsignedByteDV.applyFacets1(facets, (short) 32, (short) 0);
        types.put(SchemaSymbols.ATTVAL_UNSIGNEDBYTE, unsignedByteDV);
        facets.minInclusive = "1";
        XSSimpleTypeDecl positiveIntegerDV = new XSSimpleTypeDecl(nonNegativeDV, SchemaSymbols.ATTVAL_POSITIVEINTEGER, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 42);
        positiveIntegerDV.applyFacets1(facets, (short) 256, (short) 0);
        types.put(SchemaSymbols.ATTVAL_POSITIVEINTEGER, positiveIntegerDV);
    }
}
