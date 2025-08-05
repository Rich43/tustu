package com.sun.org.apache.xerces.internal.impl.dv;

import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.utils.ObjectFactory;
import com.sun.org.apache.xerces.internal.xs.XSObjectList;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/SchemaDVFactory.class */
public abstract class SchemaDVFactory {
    private static final String DEFAULT_FACTORY_CLASS = "com.sun.org.apache.xerces.internal.impl.dv.xs.SchemaDVFactoryImpl";

    public abstract XSSimpleType getBuiltInType(String str);

    public abstract SymbolHash getBuiltInTypes();

    public abstract XSSimpleType createTypeRestriction(String str, String str2, short s2, XSSimpleType xSSimpleType, XSObjectList xSObjectList);

    public abstract XSSimpleType createTypeList(String str, String str2, short s2, XSSimpleType xSSimpleType, XSObjectList xSObjectList);

    public abstract XSSimpleType createTypeUnion(String str, String str2, short s2, XSSimpleType[] xSSimpleTypeArr, XSObjectList xSObjectList);

    public static final synchronized SchemaDVFactory getInstance() throws DVFactoryException {
        return getInstance(DEFAULT_FACTORY_CLASS);
    }

    public static final synchronized SchemaDVFactory getInstance(String factoryClass) throws DVFactoryException {
        try {
            return (SchemaDVFactory) ObjectFactory.newInstance(factoryClass, true);
        } catch (ClassCastException e2) {
            throw new DVFactoryException("Schema factory class " + factoryClass + " does not extend from SchemaDVFactory.");
        }
    }

    protected SchemaDVFactory() {
    }
}
