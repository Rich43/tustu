package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.XSFacets;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/FullDVFactory.class */
public class FullDVFactory extends BaseDVFactory {
    static final String URI_SCHEMAFORSCHEMA = "http://www.w3.org/2001/XMLSchema";
    static SymbolHash fFullTypes = new SymbolHash(89);

    static {
        createBuiltInTypes(fFullTypes);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.BaseDVFactory, com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public XSSimpleType getBuiltInType(String name) {
        return (XSSimpleType) fFullTypes.get(name);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.xs.BaseDVFactory, com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public SymbolHash getBuiltInTypes() {
        return fFullTypes.makeClone();
    }

    static void createBuiltInTypes(SymbolHash types) {
        BaseDVFactory.createBuiltInTypes(types);
        XSFacets facets = new XSFacets();
        XSSimpleTypeDecl anySimpleType = XSSimpleTypeDecl.fAnySimpleType;
        XSSimpleTypeDecl stringDV = (XSSimpleTypeDecl) types.get("string");
        types.put(SchemaSymbols.ATTVAL_FLOAT, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_FLOAT, (short) 4, (short) 1, true, true, true, true, (short) 5));
        types.put(SchemaSymbols.ATTVAL_DOUBLE, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_DOUBLE, (short) 5, (short) 1, true, true, true, true, (short) 6));
        types.put("duration", new XSSimpleTypeDecl(anySimpleType, "duration", (short) 6, (short) 1, false, false, false, true, (short) 7));
        types.put(SchemaSymbols.ATTVAL_HEXBINARY, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_HEXBINARY, (short) 15, (short) 0, false, false, false, true, (short) 16));
        types.put(SchemaSymbols.ATTVAL_QNAME, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_QNAME, (short) 18, (short) 0, false, false, false, true, (short) 19));
        types.put(SchemaSymbols.ATTVAL_NOTATION, new XSSimpleTypeDecl(anySimpleType, SchemaSymbols.ATTVAL_NOTATION, (short) 20, (short) 0, false, false, false, true, (short) 20));
        facets.whiteSpace = (short) 1;
        XSSimpleTypeDecl normalizedDV = new XSSimpleTypeDecl(stringDV, SchemaSymbols.ATTVAL_NORMALIZEDSTRING, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 21);
        normalizedDV.applyFacets1(facets, (short) 16, (short) 0);
        types.put(SchemaSymbols.ATTVAL_NORMALIZEDSTRING, normalizedDV);
        facets.whiteSpace = (short) 2;
        XSSimpleTypeDecl tokenDV = new XSSimpleTypeDecl(normalizedDV, SchemaSymbols.ATTVAL_TOKEN, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 22);
        tokenDV.applyFacets1(facets, (short) 16, (short) 0);
        types.put(SchemaSymbols.ATTVAL_TOKEN, tokenDV);
        facets.whiteSpace = (short) 2;
        facets.pattern = "([a-zA-Z]{1,8})(-[a-zA-Z0-9]{1,8})*";
        XSSimpleTypeDecl languageDV = new XSSimpleTypeDecl(tokenDV, "language", "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 23);
        languageDV.applyFacets1(facets, (short) 24, (short) 0);
        types.put("language", languageDV);
        facets.whiteSpace = (short) 2;
        XSSimpleTypeDecl nameDV = new XSSimpleTypeDecl(tokenDV, "Name", "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 25);
        nameDV.applyFacets1(facets, (short) 16, (short) 0, (short) 2);
        types.put("Name", nameDV);
        facets.whiteSpace = (short) 2;
        XSSimpleTypeDecl ncnameDV = new XSSimpleTypeDecl(nameDV, SchemaSymbols.ATTVAL_NCNAME, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 26);
        ncnameDV.applyFacets1(facets, (short) 16, (short) 0, (short) 3);
        types.put(SchemaSymbols.ATTVAL_NCNAME, ncnameDV);
        types.put("ID", new XSSimpleTypeDecl(ncnameDV, "ID", (short) 21, (short) 0, false, false, false, true, (short) 27));
        XSSimpleTypeDecl idrefDV = new XSSimpleTypeDecl(ncnameDV, SchemaSymbols.ATTVAL_IDREF, (short) 22, (short) 0, false, false, false, true, (short) 28);
        types.put(SchemaSymbols.ATTVAL_IDREF, idrefDV);
        facets.minLength = 1;
        XSSimpleTypeDecl tempDV = new XSSimpleTypeDecl((String) null, "http://www.w3.org/2001/XMLSchema", (short) 0, idrefDV, true, (XSObjectList) null);
        XSSimpleTypeDecl idrefsDV = new XSSimpleTypeDecl(tempDV, SchemaSymbols.ATTVAL_IDREFS, "http://www.w3.org/2001/XMLSchema", (short) 0, false, (XSObjectList) null);
        idrefsDV.applyFacets1(facets, (short) 2, (short) 0);
        types.put(SchemaSymbols.ATTVAL_IDREFS, idrefsDV);
        XSSimpleTypeDecl entityDV = new XSSimpleTypeDecl(ncnameDV, SchemaSymbols.ATTVAL_ENTITY, (short) 23, (short) 0, false, false, false, true, (short) 29);
        types.put(SchemaSymbols.ATTVAL_ENTITY, entityDV);
        facets.minLength = 1;
        XSSimpleTypeDecl tempDV2 = new XSSimpleTypeDecl((String) null, "http://www.w3.org/2001/XMLSchema", (short) 0, entityDV, true, (XSObjectList) null);
        XSSimpleTypeDecl entitiesDV = new XSSimpleTypeDecl(tempDV2, SchemaSymbols.ATTVAL_ENTITIES, "http://www.w3.org/2001/XMLSchema", (short) 0, false, (XSObjectList) null);
        entitiesDV.applyFacets1(facets, (short) 2, (short) 0);
        types.put(SchemaSymbols.ATTVAL_ENTITIES, entitiesDV);
        facets.whiteSpace = (short) 2;
        XSSimpleTypeDecl nmtokenDV = new XSSimpleTypeDecl(tokenDV, SchemaSymbols.ATTVAL_NMTOKEN, "http://www.w3.org/2001/XMLSchema", (short) 0, false, null, (short) 24);
        nmtokenDV.applyFacets1(facets, (short) 16, (short) 0, (short) 1);
        types.put(SchemaSymbols.ATTVAL_NMTOKEN, nmtokenDV);
        facets.minLength = 1;
        XSSimpleTypeDecl tempDV3 = new XSSimpleTypeDecl((String) null, "http://www.w3.org/2001/XMLSchema", (short) 0, nmtokenDV, true, (XSObjectList) null);
        XSSimpleTypeDecl nmtokensDV = new XSSimpleTypeDecl(tempDV3, SchemaSymbols.ATTVAL_NMTOKENS, "http://www.w3.org/2001/XMLSchema", (short) 0, false, (XSObjectList) null);
        nmtokensDV.applyFacets1(facets, (short) 2, (short) 0);
        types.put(SchemaSymbols.ATTVAL_NMTOKENS, nmtokensDV);
    }
}
