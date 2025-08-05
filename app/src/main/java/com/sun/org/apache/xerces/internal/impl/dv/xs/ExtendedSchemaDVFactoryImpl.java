package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.util.SymbolHash;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/ExtendedSchemaDVFactoryImpl.class */
public class ExtendedSchemaDVFactoryImpl extends BaseSchemaDVFactory {
    static SymbolHash fBuiltInTypes = new SymbolHash();

    static {
        createBuiltInTypes();
    }

    static void createBuiltInTypes() {
        createBuiltInTypes(fBuiltInTypes, XSSimpleTypeDecl.fAnyAtomicType);
        fBuiltInTypes.put("anyAtomicType", XSSimpleTypeDecl.fAnyAtomicType);
        XSSimpleTypeDecl durationDV = (XSSimpleTypeDecl) fBuiltInTypes.get("duration");
        fBuiltInTypes.put("yearMonthDuration", new XSSimpleTypeDecl(durationDV, "yearMonthDuration", (short) 27, (short) 1, false, false, false, true, (short) 46));
        fBuiltInTypes.put("dayTimeDuration", new XSSimpleTypeDecl(durationDV, "dayTimeDuration", (short) 28, (short) 1, false, false, false, true, (short) 47));
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public XSSimpleType getBuiltInType(String name) {
        return (XSSimpleType) fBuiltInTypes.get(name);
    }

    @Override // com.sun.org.apache.xerces.internal.impl.dv.SchemaDVFactory
    public SymbolHash getBuiltInTypes() {
        return fBuiltInTypes.makeClone();
    }
}
