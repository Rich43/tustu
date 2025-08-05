package com.sun.org.apache.xerces.internal.impl.dv.xs;

import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.util.SymbolHash;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/xs/SchemaDVFactoryImpl.class */
public class SchemaDVFactoryImpl extends BaseSchemaDVFactory {
    static final SymbolHash fBuiltInTypes = new SymbolHash();

    static {
        createBuiltInTypes();
    }

    static void createBuiltInTypes() {
        createBuiltInTypes(fBuiltInTypes, XSSimpleTypeDecl.fAnySimpleType);
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
